package mainstreet.semantic.configuration;

import mainstreet.semantic.execution.ExecutableSupportRequirement;
import mainstreet.semantic.execution.SemanticExecutionContractReference;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ConfigurationNewActivityRequirementSetIdentityTest {

    @Test
    void canonical_identity_is_independent_of_requirement_and_participant_order() {
        ExecutableSupportRequirement ordering = requirement(
                "ordering.commit",
                "money.obligation",
                "inventory.claim"
        );
        ExecutableSupportRequirement booking = requirement(
                "booking.create",
                "notification.intent"
        );

        String first = ConfigurationNewActivityRequirementSetIdentity.derive(
                List.of(ordering, booking)
        );
        String reordered = ConfigurationNewActivityRequirementSetIdentity.derive(
                List.of(
                        requirement("booking.create", "notification.intent"),
                        requirement(
                                "ordering.commit",
                                "inventory.claim",
                                "money.obligation"
                        )
                )
        );

        assertEquals(first, reordered);
        assertEquals(
                "ms-reqset-v1:sha256:17dae7dcc7db8288f18e12dfb650c57b4233aff3ed060eb47f3b65247af8a388",
                first
        );
    }

    @Test
    void changing_one_required_contract_changes_the_identity() {
        String first = ConfigurationNewActivityRequirementSetIdentity.derive(
                List.of(requirement("ordering.commit", "inventory.claim"))
        );
        String changed = ConfigurationNewActivityRequirementSetIdentity.derive(
                List.of(requirement("ordering.commit", "inventory.reserve"))
        );

        assertNotEquals(first, changed);
    }

    private static ExecutableSupportRequirement requirement(
            String affectedContract,
            String... requiredContracts
    ) {
        return new ExecutableSupportRequirement(
                reference(affectedContract),
                java.util.Arrays.stream(requiredContracts)
                        .map(ConfigurationNewActivityRequirementSetIdentityTest::reference)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );
    }

    private static SemanticExecutionContractReference reference(
            String contractIdentifier
    ) {
        return new SemanticExecutionContractReference(
                "semantic-release-21",
                contractIdentifier
        );
    }
}
