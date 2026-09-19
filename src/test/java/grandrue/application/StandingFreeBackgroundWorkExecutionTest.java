package grandrue.application;

import grandrue.background.BackgroundExecutionScope;
import grandrue.background.BackgroundWorkContractAffinity;
import grandrue.background.BackgroundWorkContractRegistrySnapshot;
import grandrue.background.DurableWorkInstruction;
import grandrue.background.OverdueHandling;
import grandrue.commercial.CommercialEntitlementIdentity;
import grandrue.commercial.StandardPlanLevel;
import grandrue.commercial.StandardPlanRevision;
import grandrue.commercial.StandingFreeBaseline;
import grandrue.commercial.StandingFreeBaselineStore;
import grandrue.merchantaccount.MerchantAccountEstablished;
import grandrue.merchantaccount.MerchantAccountEstablishedEventContract;
import grandrue.merchantaccount.MerchantAccountEstablishedOccurrence;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.RegisteredScheduledBackgroundWorkExecutionAuthority;
import grandrue.runtime.ScheduledBackgroundWorkExecutionAuthority;
import grandrue.runtime.TrustedExecutionContext;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandingFreeBackgroundWorkExecutionTest {
    private static final Instant T0 = Instant.parse("2026-08-24T10:00:00Z");
    private static final Instant DUE_AT = Instant.parse("2026-09-12T09:00:00Z");
    private static final Instant EXECUTED_AT = DUE_AT.plusSeconds(30);
    private static final String HISTORICAL_RELEASE = "standing-free-background-work@0";
    private static final MerchantScope SCOPE = new MerchantScope("merchant-1");
    private static final ExecutionPrincipal PRINCIPAL =
            new ExecutionPrincipal("scheduled/standing-free-reconciliation");

    @Test
    void historical_affinity_current_registration_and_trusted_source_reach_the_exact_owner_operation() {
        var baselines = new InMemoryBaselineStore();
        var execution = execution(
                historicalContracts(), StandingFreeBackgroundWorkContract::registry,
                registeredAuthority(), baselines);

        StandingFreeBaseline baseline = execution.execute(instruction(SCOPE, HISTORICAL_RELEASE, DUE_AT));

        assertEquals("baseline-1", baseline.baselineIdentity());
        assertEquals(SCOPE, baseline.merchantScope());
        assertEquals("establishment-1",
                baseline.originatingMerchantAccountEstablishmentIdentity());
        assertEquals(T0, baseline.effectiveFrom());
        assertEquals("free-r7", baseline.freePlanRevisionIdentity());
    }

    @Test
    void work_that_is_not_due_cannot_use_the_instruction_as_advance_authority() {
        var ownerCalls = new AtomicInteger();
        var execution = execution(
                historicalContracts(), StandingFreeBackgroundWorkContract::registry,
                registeredAuthority(), new InMemoryBaselineStore(), ownerCalls);

        assertThrows(IllegalStateException.class, () -> execution.execute(
                instruction(SCOPE, HISTORICAL_RELEASE, EXECUTED_AT.plusSeconds(1))));
        assertEquals(0, ownerCalls.get());
    }

    @Test
    void legacy_unknown_or_mismatched_historical_contract_affinity_is_not_executable() {
        var execution = execution(
                historicalContracts(), StandingFreeBackgroundWorkContract::registry,
                registeredAuthority(), new InMemoryBaselineStore());
        DurableWorkInstruction registered = instruction(SCOPE, HISTORICAL_RELEASE, DUE_AT);
        DurableWorkInstruction legacy = new DurableWorkInstruction(
                registered.workIdentity(), registered.ownerContextIdentifier(),
                registered.executionScope(), registered.merchantScope(), registered.dueAt(),
                registered.responsibilityIdentifier(), registered.correlationIdentifier(),
                registered.causationIdentifier(), registered.semanticProvenanceReference(),
                registered.retryPolicyReference(), registered.overdueHandling(), registered.createdAt());
        DurableWorkInstruction unknownRelease = registered.withContractAffinity(
                new BackgroundWorkContractAffinity(
                        StandingFreeBackgroundWorkContract.IDENTITY, "unknown-release"));

        assertThrows(IllegalStateException.class, () -> execution.execute(legacy));
        assertThrows(IllegalStateException.class, () -> execution.execute(unknownRelease));
    }

    @Test
    void missing_current_registration_or_principal_fails_before_owner_mutation() {
        var ownerCalls = new AtomicInteger();
        Supplier<BackgroundWorkContractRegistrySnapshot> emptyCurrent = () ->
                new BackgroundWorkContractRegistrySnapshot(
                        StandingFreeBackgroundWorkContract.AFFINITY
                                .semanticRegistryReleaseIdentifier(), List.of());
        var withoutCurrent = execution(
                historicalContracts(), emptyCurrent, registeredAuthority(),
                new InMemoryBaselineStore(), ownerCalls);
        var withoutPrincipal = execution(
                historicalContracts(), StandingFreeBackgroundWorkContract::registry,
                new RegisteredScheduledBackgroundWorkExecutionAuthority(Map.of()),
                new InMemoryBaselineStore(), ownerCalls);

        assertThrows(IllegalStateException.class,
                () -> withoutCurrent.execute(instruction(SCOPE, HISTORICAL_RELEASE, DUE_AT)));
        assertThrows(IllegalStateException.class,
                () -> withoutPrincipal.execute(instruction(SCOPE, HISTORICAL_RELEASE, DUE_AT)));
        assertEquals(0, ownerCalls.get());
    }

    @Test
    void persisted_scope_or_authority_scope_substitution_is_rejected() {
        var ownerCalls = new AtomicInteger();
        var execution = execution(
                historicalContracts(), StandingFreeBackgroundWorkContract::registry,
                registeredAuthority(), new InMemoryBaselineStore(), ownerCalls);
        ScheduledBackgroundWorkExecutionAuthority wrongScope = (contract, scope) ->
                new TrustedExecutionContext(
                        new MerchantScope("merchant-forged"), PRINCIPAL, Optional.empty());
        var authoritySubstitution = execution(
                historicalContracts(), StandingFreeBackgroundWorkContract::registry,
                wrongScope, new InMemoryBaselineStore(), ownerCalls);

        assertThrows(IllegalStateException.class, () -> execution.execute(
                instruction(new MerchantScope("merchant-forged"), HISTORICAL_RELEASE, DUE_AT)));
        assertThrows(IllegalStateException.class, () -> authoritySubstitution.execute(
                instruction(SCOPE, HISTORICAL_RELEASE, DUE_AT)));
        assertEquals(0, ownerCalls.get());
    }

    @Test
    void missing_authoritative_establishment_fails_closed_and_committed_retry_reuses_owner_truth() {
        var baselines = new InMemoryBaselineStore();
        var missingSource = new StandingFreeBackgroundWorkExecution(
                ignored -> Optional.empty(), historicalContracts(),
                StandingFreeBackgroundWorkContract::registry, registeredAuthority(),
                handler(baselines, new AtomicInteger()), fixedClock());
        assertThrows(IllegalStateException.class, () -> missingSource.execute(
                instruction(SCOPE, HISTORICAL_RELEASE, DUE_AT)));

        var first = execution(historicalContracts(), StandingFreeBackgroundWorkContract::registry,
                registeredAuthority(), baselines);
        StandingFreeBaseline committed = first.execute(
                instruction(SCOPE, HISTORICAL_RELEASE, DUE_AT));
        var recoveringHandler = new StandingFreeFromMerchantAccountEstablishedHandler(
                ignored -> { throw new AssertionError("Committed recovery must not query the catalogue"); },
                baselines,
                ignored -> { throw new AssertionError("Committed recovery must reuse the identity"); });
        var retry = new StandingFreeBackgroundWorkExecution(
                id -> id.equals("establishment-1")
                        ? Optional.of(authoritativeOccurrence()) : Optional.empty(),
                historicalContracts(), StandingFreeBackgroundWorkContract::registry,
                registeredAuthority(), recoveringHandler, fixedClock());

        assertEquals(committed, retry.execute(
                instruction(SCOPE, HISTORICAL_RELEASE, DUE_AT)));
        assertEquals(1, baselines.baselines.size());
    }

    private static StandingFreeBackgroundWorkExecution execution(
            Function<String, Optional<BackgroundWorkContractRegistrySnapshot>> historicalContracts,
            Supplier<BackgroundWorkContractRegistrySnapshot> currentContracts,
            ScheduledBackgroundWorkExecutionAuthority authority,
            InMemoryBaselineStore baselines) {
        return execution(historicalContracts, currentContracts, authority, baselines,
                new AtomicInteger());
    }

    private static StandingFreeBackgroundWorkExecution execution(
            Function<String, Optional<BackgroundWorkContractRegistrySnapshot>> historicalContracts,
            Supplier<BackgroundWorkContractRegistrySnapshot> currentContracts,
            ScheduledBackgroundWorkExecutionAuthority authority,
            InMemoryBaselineStore baselines,
            AtomicInteger ownerCalls) {
        return new StandingFreeBackgroundWorkExecution(
                id -> id.equals("establishment-1")
                        ? Optional.of(authoritativeOccurrence()) : Optional.empty(),
                historicalContracts, currentContracts, authority,
                handler(baselines, ownerCalls), fixedClock());
    }

    private static StandingFreeFromMerchantAccountEstablishedHandler handler(
            InMemoryBaselineStore baselines, AtomicInteger ownerCalls) {
        return new StandingFreeFromMerchantAccountEstablishedHandler(
                ignored -> {
                    ownerCalls.incrementAndGet();
                    return new StandardPlanRevision(StandardPlanLevel.FREE, "free-r7",
                            Set.of(new CommercialEntitlementIdentity("entitlement-enquiry")));
                }, baselines, ignored -> "baseline-1");
    }

    private static Function<String, Optional<BackgroundWorkContractRegistrySnapshot>>
            historicalContracts() {
        var historical = new BackgroundWorkContractRegistrySnapshot(
                HISTORICAL_RELEASE, List.of(StandingFreeBackgroundWorkContract.DEFINITION));
        return release -> HISTORICAL_RELEASE.equals(release)
                ? Optional.of(historical) : Optional.empty();
    }

    private static ScheduledBackgroundWorkExecutionAuthority registeredAuthority() {
        return new RegisteredScheduledBackgroundWorkExecutionAuthority(Map.of(
                StandingFreeBackgroundWorkContract.IDENTITY, PRINCIPAL));
    }

    private static DurableWorkInstruction instruction(
            MerchantScope scope, String historicalRelease, Instant dueAt) {
        return new DurableWorkInstruction(
                "work/standing-free/establishment-1",
                "commercial",
                BackgroundExecutionScope.MERCHANT,
                Optional.of(scope),
                dueAt,
                StandingFreeBackgroundWorkContract.DEFINITION.target().targetIdentifier(),
                "merchant-account-establishment/request-1",
                Optional.of("establishment-1"),
                Optional.of("merchant-account-established-event/publication-1"),
                StandingFreeBackgroundWorkContract.DEFINITION.retryContractReference(),
                OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                DUE_AT.minusSeconds(30),
                Optional.of(new BackgroundWorkContractAffinity(
                        StandingFreeBackgroundWorkContract.IDENTITY, historicalRelease)));
    }

    private static MerchantAccountEstablishedOccurrence authoritativeOccurrence() {
        return new MerchantAccountEstablishedOccurrence(
                "merchant-account-established-event/publication-1",
                MerchantAccountEstablishedEventContract.AFFINITY,
                new MerchantAccountEstablished("establishment-1", SCOPE, "request-1", T0));
    }

    private static Clock fixedClock() {
        return Clock.fixed(EXECUTED_AT, ZoneOffset.UTC);
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
}
