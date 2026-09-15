package mainstreet.infrastructure.persistence.merchantaccount;

import mainstreet.semantic.event.EventContractAffinity;
import mainstreet.semantic.event.EventContractIdentity;
import mainstreet.merchantaccount.MerchantAccountEstablishedEventContract;
import mainstreet.merchantaccount.MerchantAccountEstablishedOccurrence;
import mainstreet.merchantaccount.MerchantAccountEstablishedOccurrenceAuthority;
import mainstreet.merchantaccount.MerchantAccountEstablishedOccurrenceLookup;
import mainstreet.merchantaccount.MerchantAccountEstablishedPublicationSource;
import mainstreet.merchantaccount.MerchantAccountEstablishedPublicationSource.PendingPublication;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * PostgreSQL/jOOQ access to durable MerchantAccountEstablished publication
 * intent. Publication completion is technical evidence only.
 */
public final class JooqMerchantAccountEstablishmentPublicationOutbox
        implements MerchantAccountEstablishedOccurrenceAuthority,
        MerchantAccountEstablishedOccurrenceLookup,
        MerchantAccountEstablishedPublicationSource {

    private static final Table<?> OUTBOX = DSL.table(
            DSL.name("merchant_account_establishment_publication_intent")
    );
    private static final Field<String> PUBLICATION_INTENT_IDENTIFIER =
            DSL.field(DSL.name("publication_intent_identifier"), String.class);
    private static final Field<String> ESTABLISHMENT_IDENTITY =
            DSL.field(DSL.name("establishment_identity"), String.class);
    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> LOGICAL_REQUEST_IDENTITY =
            DSL.field(DSL.name("logical_establishment_request_identity"), String.class);
    private static final Field<Instant> OCCURRED_AT =
            DSL.field(DSL.name("occurred_at"), Instant.class);
    private static final Field<Instant> PUBLISHED_AT =
            DSL.field(DSL.name("published_at"), Instant.class);

    private static final Field<String> EVENT_OWNER_IDENTIFIER =
            DSL.field(DSL.name("event_owner_identifier"), String.class);
    private static final Field<String> EVENT_CONTRACT_IDENTIFIER =
            DSL.field(DSL.name("event_contract_identifier"), String.class);
    private static final Field<String> EVENT_SEMANTIC_RELEASE =
            DSL.field(DSL.name("event_semantic_release"), String.class);

    private final DSLContext dsl;

    public JooqMerchantAccountEstablishmentPublicationOutbox(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
    }

    public List<MerchantAccountEstablishmentPublicationIntent> pending(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be positive");
        }

        return dsl.select(
                        PUBLICATION_INTENT_IDENTIFIER,
                        ESTABLISHMENT_IDENTITY,
                        MERCHANT_IDENTIFIER,
                        LOGICAL_REQUEST_IDENTITY,
                        OCCURRED_AT,
                        PUBLISHED_AT,
                        EVENT_OWNER_IDENTIFIER,
                        EVENT_CONTRACT_IDENTIFIER,
                        EVENT_SEMANTIC_RELEASE
                )
                .from(OUTBOX)
                .where(PUBLISHED_AT.isNull())
                .orderBy(OCCURRED_AT, PUBLICATION_INTENT_IDENTIFIER)
                .limit(limit)
                .fetch(this::toIntent);
    }

    @Override
    public List<PendingPublication> pendingPublications(int limit) {
        return pending(limit).stream()
                .map(intent -> new PendingPublication(
                        intent.publicationIntentIdentifier(),
                        intent.registeredOccurrence(
                                MerchantAccountEstablishedEventContract.registry())))
                .toList();
    }

    public Optional<MerchantAccountEstablishmentPublicationIntent> intentForEstablishment(
            String establishmentIdentity) {
        requireIdentifier(establishmentIdentity, "establishmentIdentity");

        Record record = dsl.select(
                        PUBLICATION_INTENT_IDENTIFIER,
                        ESTABLISHMENT_IDENTITY,
                        MERCHANT_IDENTIFIER,
                        LOGICAL_REQUEST_IDENTITY,
                        OCCURRED_AT,
                        PUBLISHED_AT,
                        EVENT_OWNER_IDENTIFIER,
                        EVENT_CONTRACT_IDENTIFIER,
                        EVENT_SEMANTIC_RELEASE
                )
                .from(OUTBOX)
                .where(ESTABLISHMENT_IDENTITY.eq(establishmentIdentity))
                .fetchOne();

        return Optional.ofNullable(record).map(this::toIntent);
    }

    /**
     * Resolves only an exact occurrence reproduced from Merchant Account's
     * durable owner evidence; caller-supplied event identity, payload and
     * Merchant Scope are never promoted into authority.
     */
    @Override
    public Optional<MerchantAccountEstablishedOccurrence> authoritativeOccurrence(
            MerchantAccountEstablishedOccurrence suppliedOccurrence) {
        Objects.requireNonNull(suppliedOccurrence, "suppliedOccurrence");
        return authoritativeOccurrence(suppliedOccurrence.fact().establishmentIdentity())
                .filter(suppliedOccurrence::equals);
    }

    @Override
    public Optional<MerchantAccountEstablishedOccurrence> authoritativeOccurrence(
            String establishmentIdentity) {
        return intentForEstablishment(establishmentIdentity)
                .flatMap(intent -> intent.registeredOccurrence(
                        MerchantAccountEstablishedEventContract.registry()));
    }

    public boolean markPublished(
            String publicationIntentIdentifier,
            Instant publishedAt) {
        requireIdentifier(publicationIntentIdentifier, "publicationIntentIdentifier");
        Objects.requireNonNull(publishedAt, "publishedAt");

        return dsl.update(OUTBOX)
                .set(PUBLISHED_AT, publishedAt)
                .where(PUBLICATION_INTENT_IDENTIFIER.eq(publicationIntentIdentifier))
                .and(PUBLISHED_AT.isNull())
                .execute() == 1;
    }

    @Override
    public boolean recordPublished(
            String publicationIntentIdentifier,
            Instant publishedAt) {
        return markPublished(publicationIntentIdentifier, publishedAt);
    }

    private MerchantAccountEstablishmentPublicationIntent toIntent(Record record) {
        return new MerchantAccountEstablishmentPublicationIntent(
                record.get(PUBLICATION_INTENT_IDENTIFIER),
                record.get(ESTABLISHMENT_IDENTITY),
                record.get(MERCHANT_IDENTIFIER),
                record.get(LOGICAL_REQUEST_IDENTITY),
                record.get(OCCURRED_AT),
                Optional.ofNullable(record.get(PUBLISHED_AT)),
                Optional.ofNullable(record.get(EVENT_CONTRACT_IDENTIFIER)).map(identifier ->
                        new EventContractAffinity(new EventContractIdentity(
                                record.get(EVENT_OWNER_IDENTIFIER), identifier), record.get(EVENT_SEMANTIC_RELEASE)))
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
