package grandrue.application;

import grandrue.commercial.CommercialEntitlementIdentity;
import grandrue.commercial.StandardPlanLevel;
import grandrue.commercial.StandardPlanRevision;
import grandrue.commercial.StandingFreeBaseline;
import grandrue.commercial.StandingFreeBaselineStore;
import grandrue.merchantaccount.MerchantAccountEstablished;
import grandrue.merchantaccount.MerchantAccountEstablishedEventContract;
import grandrue.merchantaccount.MerchantAccountEstablishedOccurrence;
import grandrue.merchantaccount.MerchantAccountEstablishedOccurrenceAuthority;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.RegisteredScheduledEventReactionExecutionAuthority;
import grandrue.runtime.ScheduledEventReactionExecutionAuthority;
import grandrue.runtime.TrustedExecutionContext;
import grandrue.semantic.event.EventReactionAcknowledgement;
import grandrue.semantic.event.EventReactionContractRegistrySnapshot;
import grandrue.semantic.event.EventReactionIdentity;
import grandrue.semantic.event.MerchantEventReactionReceipt;
import grandrue.semantic.event.MerchantEventReactionStore;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandingFreeMerchantAccountEstablishedReactionTest {
    private static final Instant T0 = Instant.parse("2026-08-24T10:00:00Z");
    private static final Instant ACCEPTED_AT = Instant.parse("2026-09-12T08:00:00Z");
    private static final MerchantScope SCOPE = new MerchantScope("merchant-1");
    private static final ExecutionPrincipal PRINCIPAL = new ExecutionPrincipal("scheduled/standing-free");

    @Test
    void trusted_occurrence_and_current_registered_principal_reach_owner_operation_and_acknowledge() {
        var reactions = new InMemoryReactionStore();
        var baselines = new InMemoryBaselineStore();
        var reaction = reaction(authoritativeOccurrence(), StandingFreeEventReactionContract::registry,
                registeredAuthority(), reactions, handler(baselines));

        StandingFreeBaseline baseline = reaction.react(authoritativeOccurrence());

        EventReactionIdentity identity = reactionIdentity();
        MerchantEventReactionReceipt receipt = reactions.receipt(identity).orElseThrow();
        assertEquals(StandingFreeEventReactionContract.AFFINITY, receipt.contractAffinity());
        assertEquals(MerchantAccountEstablishedEventContract.AFFINITY, receipt.sourceEventContract());
        assertEquals(SCOPE, receipt.merchantScope());
        assertEquals("standing-free-baseline/establishment-1", receipt.downstreamLogicalIntentReference());
        assertEquals(ACCEPTED_AT, receipt.acceptedAt());
        assertEquals(baseline.baselineIdentity(),
                reactions.acknowledgement(identity).orElseThrow().outcomeReference());
        assertEquals(T0, baseline.effectiveFrom());
    }

    @Test
    void forged_occurrence_is_rejected_before_receipt_authority_or_owner_operation() {
        var reactions = new InMemoryReactionStore();
        var authorityCalls = new AtomicInteger();
        var ownerCalls = new AtomicInteger();
        ScheduledEventReactionExecutionAuthority authority = (definition, scope) -> {
            authorityCalls.incrementAndGet();
            return new TrustedExecutionContext(scope, PRINCIPAL, Optional.empty());
        };
        var handler = new StandingFreeFromMerchantAccountEstablishedHandler(
                at -> {
                    ownerCalls.incrementAndGet();
                    return freeRevision();
                }, new InMemoryBaselineStore(), ignored -> "baseline-1");
        var reaction = reaction(authoritativeOccurrence(), StandingFreeEventReactionContract::registry,
                authority, reactions, handler);
        var forged = new MerchantAccountEstablishedOccurrence(
                "merchant-account-established-event/forged",
                MerchantAccountEstablishedEventContract.AFFINITY,
                authoritativeOccurrence().fact());

        assertThrows(IllegalStateException.class, () -> reaction.react(forged));
        assertTrue(reactions.receipts.isEmpty());
        assertEquals(0, authorityCalls.get());
        assertEquals(0, ownerCalls.get());
    }

    @Test
    void absent_current_contract_or_registered_principal_is_rejected_before_receipt() {
        var reactions = new InMemoryReactionStore();
        Supplier<EventReactionContractRegistrySnapshot> missingContract = () ->
                new EventReactionContractRegistrySnapshot(
                        StandingFreeEventReactionContract.AFFINITY.semanticRegistryReleaseIdentifier(), List.of());
        var withoutContract = reaction(authoritativeOccurrence(), missingContract,
                registeredAuthority(), reactions, handler(new InMemoryBaselineStore()));
        assertThrows(IllegalStateException.class,
                () -> withoutContract.react(authoritativeOccurrence()));

        var withoutPrincipal = reaction(authoritativeOccurrence(), StandingFreeEventReactionContract::registry,
                new RegisteredScheduledEventReactionExecutionAuthority(Map.of()),
                reactions, handler(new InMemoryBaselineStore()));
        assertThrows(IllegalStateException.class,
                () -> withoutPrincipal.react(authoritativeOccurrence()));
        assertTrue(reactions.receipts.isEmpty());
    }

    @Test
    void authority_cannot_substitute_a_different_merchant_scope() {
        var reactions = new InMemoryReactionStore();
        ScheduledEventReactionExecutionAuthority wrongScope = (definition, scope) ->
                new TrustedExecutionContext(new MerchantScope("merchant-2"), PRINCIPAL, Optional.empty());
        var reaction = reaction(authoritativeOccurrence(), StandingFreeEventReactionContract::registry,
                wrongScope, reactions, handler(new InMemoryBaselineStore()));

        assertThrows(IllegalStateException.class,
                () -> reaction.react(authoritativeOccurrence()));
        assertTrue(reactions.receipts.isEmpty());
    }

    @Test
    void owner_failure_after_acceptance_remains_pending_and_unacknowledged() {
        var reactions = new InMemoryReactionStore();
        var handler = new StandingFreeFromMerchantAccountEstablishedHandler(
                at -> { throw new IllegalStateException("catalogue unavailable"); },
                new InMemoryBaselineStore(), ignored -> "baseline-1");
        var reaction = reaction(authoritativeOccurrence(), StandingFreeEventReactionContract::registry,
                registeredAuthority(), reactions, handler);

        assertThrows(IllegalStateException.class,
                () -> reaction.react(authoritativeOccurrence()));
        assertEquals(List.of(reactions.receipt(reactionIdentity()).orElseThrow()),
                reactions.pending(StandingFreeEventReactionContract.AFFINITY, 10));
        assertTrue(reactions.acknowledgement(reactionIdentity()).isEmpty());
    }

    @Test
    void lost_acknowledgement_retry_revalidates_authority_and_recovers_committed_owner_outcome() {
        var baselines = new InMemoryBaselineStore();
        var durableReactions = new InMemoryReactionStore();
        var failingAckStore = new FailingFirstAcknowledgementStore(durableReactions);
        var firstAuthorityCalls = new AtomicInteger();
        ScheduledEventReactionExecutionAuthority firstAuthority = countingAuthority(firstAuthorityCalls);
        var first = reaction(authoritativeOccurrence(), StandingFreeEventReactionContract::registry,
                firstAuthority, failingAckStore, handler(baselines));

        assertThrows(IllegalStateException.class,
                () -> first.react(authoritativeOccurrence()));
        StandingFreeBaseline committed = baselines.baselineFor(SCOPE).orElseThrow();
        assertTrue(durableReactions.acknowledgement(reactionIdentity()).isEmpty());

        var retryAuthorityCalls = new AtomicInteger();
        var recoveringHandler = new StandingFreeFromMerchantAccountEstablishedHandler(
                at -> { throw new AssertionError("Committed recovery must not query the catalogue"); },
                baselines,
                event -> { throw new AssertionError("Committed recovery must reuse the identity"); });
        var retry = reaction(authoritativeOccurrence(), StandingFreeEventReactionContract::registry,
                countingAuthority(retryAuthorityCalls), durableReactions, recoveringHandler);

        assertEquals(committed, retry.react(authoritativeOccurrence()));
        assertEquals(1, firstAuthorityCalls.get());
        assertEquals(1, retryAuthorityCalls.get());
        assertEquals(committed.baselineIdentity(),
                durableReactions.acknowledgement(reactionIdentity()).orElseThrow().outcomeReference());
    }

    private static StandingFreeMerchantAccountEstablishedReaction reaction(
            MerchantAccountEstablishedOccurrence authoritative,
            Supplier<EventReactionContractRegistrySnapshot> currentContracts,
            ScheduledEventReactionExecutionAuthority executionAuthority,
            MerchantEventReactionStore reactionStore,
            StandingFreeFromMerchantAccountEstablishedHandler handler) {
        MerchantAccountEstablishedOccurrenceAuthority sourceAuthority = candidate ->
                authoritative.equals(candidate) ? Optional.of(authoritative) : Optional.empty();
        return new StandingFreeMerchantAccountEstablishedReaction(sourceAuthority, currentContracts,
                executionAuthority, reactionStore, handler,
                Clock.fixed(ACCEPTED_AT, ZoneOffset.UTC));
    }

    private static ScheduledEventReactionExecutionAuthority registeredAuthority() {
        return new RegisteredScheduledEventReactionExecutionAuthority(
                Map.of(StandingFreeEventReactionContract.IDENTITY, PRINCIPAL));
    }

    private static ScheduledEventReactionExecutionAuthority countingAuthority(AtomicInteger calls) {
        var registered = registeredAuthority();
        return (definition, scope) -> {
            calls.incrementAndGet();
            return registered.establish(definition, scope);
        };
    }

    private static StandingFreeFromMerchantAccountEstablishedHandler handler(InMemoryBaselineStore store) {
        return new StandingFreeFromMerchantAccountEstablishedHandler(
                ignored -> freeRevision(), store, ignored -> "baseline-1");
    }

    private static StandardPlanRevision freeRevision() {
        return new StandardPlanRevision(StandardPlanLevel.FREE, "free-r7",
                Set.of(new CommercialEntitlementIdentity("entitlement-enquiry")));
    }

    private static MerchantAccountEstablishedOccurrence authoritativeOccurrence() {
        return new MerchantAccountEstablishedOccurrence(
                "merchant-account-established-event/publication-1",
                MerchantAccountEstablishedEventContract.AFFINITY,
                new MerchantAccountEstablished("establishment-1", SCOPE, "request-1", T0));
    }

    private static EventReactionIdentity reactionIdentity() {
        return new EventReactionIdentity(authoritativeOccurrence().eventIdentity(),
                StandingFreeEventReactionContract.IDENTITY);
    }

    private static final class InMemoryBaselineStore implements StandingFreeBaselineStore {
        private final Map<MerchantScope, StandingFreeBaseline> baselines = new HashMap<>();

        @Override
        public StandingFreeBaseline establishIfAbsent(StandingFreeBaseline candidate) {
            return baselines.computeIfAbsent(candidate.merchantScope(), ignored -> candidate);
        }

        @Override
        public Optional<StandingFreeBaseline> baselineFor(MerchantScope merchantScope) {
            return Optional.ofNullable(baselines.get(merchantScope));
        }
    }

    private static final class InMemoryReactionStore implements MerchantEventReactionStore {
        private final Map<EventReactionIdentity, MerchantEventReactionReceipt> receipts = new HashMap<>();
        private final Map<EventReactionIdentity, EventReactionAcknowledgement> acknowledgements = new HashMap<>();

        @Override
        public MerchantEventReactionReceipt accept(MerchantEventReactionReceipt candidate) {
            var existing = receipts.putIfAbsent(candidate.identity(), candidate);
            if (existing != null && !existing.sameResponsibility(candidate)) {
                throw new IllegalStateException("conflicting reaction");
            }
            return existing == null ? candidate : existing;
        }

        @Override
        public Optional<MerchantEventReactionReceipt> receipt(EventReactionIdentity identity) {
            return Optional.ofNullable(receipts.get(identity));
        }

        @Override
        public List<MerchantEventReactionReceipt> pending(
                grandrue.semantic.event.EventReactionContractAffinity affinity, int limit) {
            var pending = new ArrayList<MerchantEventReactionReceipt>();
            receipts.values().stream()
                    .filter(receipt -> receipt.contractAffinity().equals(affinity))
                    .filter(receipt -> !acknowledgements.containsKey(receipt.identity()))
                    .limit(limit)
                    .forEach(pending::add);
            return List.copyOf(pending);
        }

        @Override
        public EventReactionAcknowledgement acknowledge(EventReactionAcknowledgement candidate) {
            if (!receipts.containsKey(candidate.identity())) {
                throw new IllegalStateException("unaccepted reaction");
            }
            var existing = acknowledgements.putIfAbsent(candidate.identity(), candidate);
            if (existing != null && !existing.outcomeReference().equals(candidate.outcomeReference())) {
                throw new IllegalStateException("conflicting outcome");
            }
            return existing == null ? candidate : existing;
        }

        @Override
        public Optional<EventReactionAcknowledgement> acknowledgement(EventReactionIdentity identity) {
            return Optional.ofNullable(acknowledgements.get(identity));
        }
    }

    private static final class FailingFirstAcknowledgementStore
            implements MerchantEventReactionStore {
        private final MerchantEventReactionStore delegate;
        private boolean failed;

        private FailingFirstAcknowledgementStore(MerchantEventReactionStore delegate) {
            this.delegate = delegate;
        }

        @Override public MerchantEventReactionReceipt accept(MerchantEventReactionReceipt candidate) {
            return delegate.accept(candidate);
        }
        @Override public Optional<MerchantEventReactionReceipt> receipt(EventReactionIdentity identity) {
            return delegate.receipt(identity);
        }
        @Override public List<MerchantEventReactionReceipt> pending(
                grandrue.semantic.event.EventReactionContractAffinity affinity, int limit) {
            return delegate.pending(affinity, limit);
        }
        @Override public EventReactionAcknowledgement acknowledge(EventReactionAcknowledgement candidate) {
            if (!failed) {
                failed = true;
                throw new IllegalStateException("acknowledgement unavailable");
            }
            return delegate.acknowledge(candidate);
        }
        @Override public Optional<EventReactionAcknowledgement> acknowledgement(EventReactionIdentity identity) {
            return delegate.acknowledgement(identity);
        }
    }
}
