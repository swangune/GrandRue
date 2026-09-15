package mainstreet.background;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class DurableWorkContractAffinityTest {
    private static final Instant NOW = Instant.parse("2026-09-05T00:00:00Z");
    private static final BackgroundWorkContractDefinition CONTRACT =
            BackgroundWorkContractDefinitionTest.definition("owner", "review");
    private static final BackgroundWorkContractRegistrySnapshot REGISTRY =
            new BackgroundWorkContractRegistrySnapshot("release-1", List.of(CONTRACT));

    private static DurableWorkInstruction work(String target, String retry, OverdueHandling overdue,
                                                BackgroundExecutionScope scope) {
        return new DurableWorkInstruction("work-1", "owner", scope,
                scope == BackgroundExecutionScope.MERCHANT ? Optional.of(new MerchantScope("merchant-a")) : Optional.empty(),
                NOW, target, "correlation", Optional.empty(), Optional.of("configuration-7"),
                retry, overdue, NOW);
    }
    private static DurableWorkInstruction work() {
        return work("reevaluate", "owner/retry", OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                BackgroundExecutionScope.MERCHANT);
    }
    private static BackgroundWorkContractAffinity affinity(String release, String owner, String id) {
        return new BackgroundWorkContractAffinity(new BackgroundWorkContractIdentity(owner, id), release);
    }

    @Test void legacy_instruction_does_not_infer_registration_from_opaque_provenance() {
        assertTrue(work().contractAffinity().isEmpty());
        assertTrue(work().registeredContract(REGISTRY).isEmpty());
    }
    @Test void exact_affinity_preserves_configuration_and_resolves_static_contract_only() {
        var bound = work().withContractAffinity(affinity("release-1", "owner", "review"));
        assertEquals(Optional.of(CONTRACT), bound.registeredContract(REGISTRY));
        assertEquals(Optional.of("configuration-7"), bound.semanticProvenanceReference());
        assertEquals("work-1", bound.workIdentity());
        assertNotEquals(work(), bound);
    }
    @Test void wrong_release_or_unknown_identity_never_uses_current_or_same_named_contract() {
        assertTrue(work().withContractAffinity(affinity("release-2", "owner", "review"))
                .registeredContract(REGISTRY).isEmpty());
        assertTrue(work().withContractAffinity(affinity("release-1", "owner", "unknown"))
                .registeredContract(REGISTRY).isEmpty());
        var other = new BackgroundWorkContractRegistrySnapshot("release-1",
                List.of(BackgroundWorkContractDefinitionTest.definition("other", "review")));
        assertTrue(work().withContractAffinity(affinity("release-1", "owner", "review"))
                .registeredContract(other).isEmpty());
    }
    @Test void static_scope_target_retry_and_overdue_mismatch_cannot_resolve() {
        for (var instruction : List.of(
                work("different-target", "owner/retry", OverdueHandling.RE_EVALUATE_CURRENT_STATE, BackgroundExecutionScope.MERCHANT),
                work("reevaluate", "other-retry", OverdueHandling.RE_EVALUATE_CURRENT_STATE, BackgroundExecutionScope.MERCHANT),
                work("reevaluate", "owner/retry", OverdueHandling.EXPIRE_WITHOUT_EXECUTION, BackgroundExecutionScope.MERCHANT),
                work("reevaluate", "owner/retry", OverdueHandling.RE_EVALUATE_CURRENT_STATE, BackgroundExecutionScope.PLATFORM))) {
            assertTrue(instruction.withContractAffinity(affinity("release-1", "owner", "review"))
                    .registeredContract(REGISTRY).isEmpty());
        }
    }
    @Test void malformed_affinity_and_cross_owner_binding_are_rejected() {
        assertThrows(IllegalArgumentException.class, () -> affinity(" ", "owner", "review"));
        assertThrows(NullPointerException.class, () -> new BackgroundWorkContractAffinity(null, "r"));
        assertThrows(IllegalArgumentException.class, () -> work().withContractAffinity(affinity("r", "other", "review")));
        assertThrows(NullPointerException.class, () -> work().withContractAffinity(null));
        assertThrows(NullPointerException.class, () -> work().registeredContract(null));
    }
}
