package grandrue.background;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class BackgroundWorkContractDefinitionTest {
    static BackgroundWorkContractDefinition definition(String owner, String contract) {
        return new BackgroundWorkContractDefinition(new BackgroundWorkContractIdentity(owner, contract),
                "owner/purpose", BackgroundExecutionScope.MERCHANT, "owner/trigger",
                new BackgroundWorkTargetReference(owner, "reevaluate"), "owner/bounded-principal",
                "owner/current-due-condition", OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                Set.of("owner/current-authority", "owner/current-subject"),
                "owner/stable-downstream-intent", "owner/retry", "owner/uncertainty",
                "owner/supersession-cancellation", "owner/minimal-data", "owner/semantic-affinity");
    }

    @Test void preserves_explicit_owner_responsibilities_without_execution_or_grants() {
        var value = definition("enquiry", "follow-up");
        assertEquals(new BackgroundWorkContractIdentity("enquiry", "follow-up"), value.identity());
        assertEquals(new BackgroundWorkTargetReference("enquiry", "reevaluate"), value.target());
        assertEquals(BackgroundExecutionScope.MERCHANT, value.authorityScope());
        assertEquals(OverdueHandling.RE_EVALUATE_CURRENT_STATE, value.overdueHandling());
        assertEquals("owner/stable-downstream-intent", value.downstreamLogicalIntentRuleReference());
        assertEquals("owner/uncertainty", value.uncertaintyContractReference());
        assertThrows(UnsupportedOperationException.class, () -> value.currentRevalidationReferences().clear());
    }

    @Test void rejects_absent_or_blank_required_semantics_and_owner_identity() {
        assertThrows(IllegalArgumentException.class, () -> new BackgroundWorkContractIdentity(" ", "job"));
        assertThrows(IllegalArgumentException.class, () -> new BackgroundWorkContractIdentity("owner", ""));
        assertThrows(IllegalArgumentException.class, () -> new BackgroundWorkTargetReference("owner", " "));
        var d = definition("owner", "job");
        for (int field = 0; field < 10; field++) {
            final int invalid = field;
            assertThrows(IllegalArgumentException.class, () -> copy(d, invalid, Set.of("current")));
        }
        assertThrows(IllegalArgumentException.class, () -> copy(d, -1, Set.of()));
        assertThrows(IllegalArgumentException.class, () -> copy(d, -1, Set.of(" ")));
    }

    @Test void registry_requires_exact_release_and_exact_owner_with_no_latest_fallback() {
        var a = definition("enquiry", "follow-up");
        var b = definition("notification", "follow-up");
        var registry = new BackgroundWorkContractRegistrySnapshot("release-1", List.of(a, b));
        assertEquals(Optional.of(a), registry.contract("release-1", a.identity()));
        assertEquals(Optional.of(b), registry.contract("release-1", b.identity()));
        assertEquals(Optional.empty(), registry.contract("release-2", a.identity()));
        assertEquals(Optional.empty(), registry.contract("release-1", new BackgroundWorkContractIdentity("other", "follow-up")));
        assertThrows(IllegalArgumentException.class, () -> registry.contract(" ", a.identity()));
    }

    @Test void duplicate_identity_is_rejected_even_when_definition_is_identical() {
        var a = definition("enquiry", "follow-up");
        assertThrows(IllegalArgumentException.class, () -> new BackgroundWorkContractRegistrySnapshot("r", List.of(a, a)));
        assertThrows(IllegalArgumentException.class, () -> new BackgroundWorkContractRegistrySnapshot("", List.of(a)));
    }

    @Test void registry_defensively_copies_and_empty_registry_remains_fail_closed() {
        var a = definition("enquiry", "follow-up");
        var source = new ArrayList<>(List.of(a));
        var registry = new BackgroundWorkContractRegistrySnapshot("r", source);
        source.clear();
        assertEquals(Set.of(a), registry.contracts());
        assertThrows(UnsupportedOperationException.class, () -> registry.contracts().clear());
        assertEquals(Optional.empty(), new BackgroundWorkContractRegistrySnapshot("r", List.of()).contract("r", a.identity()));
    }

    private static BackgroundWorkContractDefinition copy(BackgroundWorkContractDefinition d, int blank, Set<String> checks) {
        var refs = new String[]{d.purposeReference(), d.triggerFormReference(), d.executionPrincipalContractReference(),
                d.dueConditionReference(), d.downstreamLogicalIntentRuleReference(), d.retryContractReference(),
                d.uncertaintyContractReference(), d.supersessionCancellationRuleReference(),
                d.dataMinimisationContractReference(), d.semanticConfigurationAffinityReference()};
        if (blank >= 0) refs[blank] = " ";
        return new BackgroundWorkContractDefinition(d.identity(), refs[0], d.authorityScope(), refs[1], d.target(),
                refs[2], refs[3], d.overdueHandling(), checks, refs[4], refs[5], refs[6], refs[7], refs[8], refs[9]);
    }
}
