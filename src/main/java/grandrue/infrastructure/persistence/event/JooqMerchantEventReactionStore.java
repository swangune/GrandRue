package grandrue.infrastructure.persistence.event;

import grandrue.application.MerchantScope;
import grandrue.semantic.event.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JooqMerchantEventReactionStore implements MerchantEventReactionStore {
    private static final String KEY = "event_identity = ? and reaction_owner_identifier = ? and reaction_contract_identifier = ?";
    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqMerchantEventReactionStore(DSLContext dsl, PlatformTransactionManager transactionManager) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        transactions = new TransactionTemplate(Objects.requireNonNull(transactionManager, "transactionManager"));
    }

    @Override
    public MerchantEventReactionReceipt accept(MerchantEventReactionReceipt candidate) {
        Objects.requireNonNull(candidate, "candidate");
        return transactions.execute(status -> {
            lock(candidate.identity());
            var existing = receipt(candidate.identity());
            if (existing.isPresent()) {
                var original = existing.orElseThrow();
                if (!original.sameResponsibility(candidate)) {
                    throw new IllegalStateException("Reaction responsibility conflicts with its original receipt");
                }
                return original;
            }
            dsl.execute("""
                    insert into merchant_event_reaction
                    (event_identity, reaction_owner_identifier, reaction_contract_identifier,
                     reaction_semantic_release, source_owner_identifier, source_contract_identifier,
                     source_semantic_release, merchant_identifier, downstream_intent_reference, accepted_at)
                    values (?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as timestamptz))
                    """, candidate.identity().eventIdentity(),
                    candidate.identity().contractIdentity().ownerIdentifier(),
                    candidate.identity().contractIdentity().contractIdentifier(),
                    candidate.contractAffinity().semanticRegistryReleaseIdentifier(),
                    candidate.sourceEventContract().contractIdentity().ownerIdentifier(),
                    candidate.sourceEventContract().contractIdentity().contractIdentifier(),
                    candidate.sourceEventContract().semanticRegistryReleaseIdentifier(),
                    candidate.merchantScope().merchantIdentifier(),
                    candidate.downstreamLogicalIntentReference(), candidate.acceptedAt());
            return receipt(candidate.identity()).orElseThrow();
        });
    }

    @Override
    public Optional<MerchantEventReactionReceipt> receipt(EventReactionIdentity identity) {
        return row(identity).map(JooqMerchantEventReactionStore::readReceipt);
    }

    @Override
    public List<MerchantEventReactionReceipt> pending(EventReactionContractAffinity affinity, int limit) {
        Objects.requireNonNull(affinity, "affinity");
        if (limit < 1) throw new IllegalArgumentException("limit must be positive");
        return dsl.fetch("""
                select * from merchant_event_reaction
                where reaction_owner_identifier = ? and reaction_contract_identifier = ?
                  and reaction_semantic_release = ? and acknowledged_at is null
                order by accepted_at, event_identity limit ?
                """, affinity.contractIdentity().ownerIdentifier(), affinity.contractIdentity().contractIdentifier(),
                affinity.semanticRegistryReleaseIdentifier(), limit).map(JooqMerchantEventReactionStore::readReceipt);
    }

    @Override
    public EventReactionAcknowledgement acknowledge(EventReactionAcknowledgement candidate) {
        Objects.requireNonNull(candidate, "candidate");
        return transactions.execute(status -> {
            lock(candidate.identity());
            if (receipt(candidate.identity()).isEmpty()) {
                throw new IllegalStateException("Cannot acknowledge an unaccepted reaction");
            }
            var existing = acknowledgement(candidate.identity());
            if (existing.isPresent()) {
                var original = existing.orElseThrow();
                if (!original.outcomeReference().equals(candidate.outcomeReference())) {
                    throw new IllegalStateException("Reaction outcome conflicts with its original acknowledgement");
                }
                return original;
            }
            dsl.execute("update merchant_event_reaction set outcome_reference = ?, acknowledged_at = cast(? as timestamptz) where " + KEY,
                    candidate.outcomeReference(), candidate.acknowledgedAt(), candidate.identity().eventIdentity(),
                    candidate.identity().contractIdentity().ownerIdentifier(),
                    candidate.identity().contractIdentity().contractIdentifier());
            return acknowledgement(candidate.identity()).orElseThrow();
        });
    }

    @Override
    public Optional<EventReactionAcknowledgement> acknowledgement(EventReactionIdentity identity) {
        return row(identity).filter(r -> r.get("acknowledged_at") != null)
                .map(r -> new EventReactionAcknowledgement(identity, r.get("outcome_reference", String.class),
                        r.get("acknowledged_at", Instant.class)));
    }

    private Optional<Record> row(EventReactionIdentity identity) {
        Objects.requireNonNull(identity, "identity");
        return Optional.ofNullable(dsl.fetchOne("select * from merchant_event_reaction where " + KEY,
                identity.eventIdentity(), identity.contractIdentity().ownerIdentifier(),
                identity.contractIdentity().contractIdentifier()));
    }

    private void lock(EventReactionIdentity identity) {
        // Lock before insertion so concurrent first deliveries share the same decision.
        // Authority: MS-PROT-026 v1.1,
        // designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md,
        // §23 — Reaction Deduplication.
        String key = component(identity.eventIdentity())
                + component(identity.contractIdentity().ownerIdentifier())
                + component(identity.contractIdentity().contractIdentifier());
        dsl.fetch("select pg_advisory_xact_lock(hashtextextended(?, 0))", key);
    }

    private static String component(String value) {
        return value.length() + ":" + value;
    }

    private static MerchantEventReactionReceipt readReceipt(Record row) {
        var contract = new EventReactionContractIdentity(row.get("reaction_owner_identifier", String.class),
                row.get("reaction_contract_identifier", String.class));
        return new MerchantEventReactionReceipt(
                new EventReactionIdentity(row.get("event_identity", String.class), contract),
                new EventReactionContractAffinity(contract, row.get("reaction_semantic_release", String.class)),
                new EventContractAffinity(new EventContractIdentity(row.get("source_owner_identifier", String.class),
                        row.get("source_contract_identifier", String.class)), row.get("source_semantic_release", String.class)),
                new MerchantScope(row.get("merchant_identifier", String.class)),
                row.get("downstream_intent_reference", String.class), row.get("accepted_at", Instant.class));
    }
}
