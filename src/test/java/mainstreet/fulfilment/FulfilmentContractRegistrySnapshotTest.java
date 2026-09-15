package mainstreet.fulfilment;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FulfilmentContractRegistrySnapshotTest {

    private static final FulfilmentRoleIdentity APPOINTMENT_EXECUTION =
            new FulfilmentRoleIdentity("scheduling", "appointment-execution");

    @Test
    void evaluates_provider_compatibility_against_required_registered_obligations() {
        FulfilmentRoleDefinition role = new FulfilmentRoleDefinition(
                APPOINTMENT_EXECUTION,
                Set.of("create-appointment", "cancel-appointment"),
                "scheduling-appointment-authority-v1",
                "scheduling-appointment-evidence-v1",
                "scheduling-appointment-failure-v1"
        );
        ProviderDefinition provider = new ProviderDefinition(
                "provider-x",
                "provider-x-contract-v3",
                Set.of(new ProviderFulfilmentSupport(
                        APPOINTMENT_EXECUTION,
                        Set.of("create-appointment")
                ))
        );
        FulfilmentContractRegistrySnapshot snapshot =
                new FulfilmentContractRegistrySnapshot(
                        "semantic-registry-1.0",
                        Set.of(role),
                        Set.of(provider)
                );

        assertTrue(snapshot.providerSupports(
                "provider-x",
                APPOINTMENT_EXECUTION,
                Set.of("create-appointment")
        ));
        assertFalse(snapshot.providerSupports(
                "provider-x",
                APPOINTMENT_EXECUTION,
                Set.of("create-appointment", "cancel-appointment")
        ));
        assertTrue(snapshot.role(APPOINTMENT_EXECUTION).isPresent());
        assertTrue(snapshot.provider("provider-x").isPresent());
    }

    @Test
    void rejects_provider_support_for_an_unregistered_role() {
        ProviderDefinition provider = new ProviderDefinition(
                "provider-x",
                "provider-x-contract-v3",
                Set.of(new ProviderFulfilmentSupport(
                        APPOINTMENT_EXECUTION,
                        Set.of("create-appointment")
                ))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new FulfilmentContractRegistrySnapshot(
                        "semantic-registry-1.0",
                        Set.of(),
                        Set.of(provider)
                )
        );
    }

    @Test
    void rejects_provider_claim_for_an_obligation_not_declared_by_the_role() {
        FulfilmentRoleDefinition role = new FulfilmentRoleDefinition(
                APPOINTMENT_EXECUTION,
                Set.of("create-appointment"),
                "scheduling-appointment-authority-v1",
                "scheduling-appointment-evidence-v1",
                "scheduling-appointment-failure-v1"
        );
        ProviderDefinition provider = new ProviderDefinition(
                "provider-x",
                "provider-x-contract-v3",
                Set.of(new ProviderFulfilmentSupport(
                        APPOINTMENT_EXECUTION,
                        Set.of("reschedule-appointment")
                ))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new FulfilmentContractRegistrySnapshot(
                        "semantic-registry-1.0",
                        Set.of(role),
                        Set.of(provider)
                )
        );
    }

    @Test
    void rejects_compatibility_question_outside_registered_role_obligations() {
        FulfilmentContractRegistrySnapshot snapshot =
                new FulfilmentContractRegistrySnapshot(
                        "semantic-registry-1.0",
                        Set.of(new FulfilmentRoleDefinition(
                                APPOINTMENT_EXECUTION,
                                Set.of("create-appointment"),
                                "scheduling-appointment-authority-v1",
                                "scheduling-appointment-evidence-v1",
                                "scheduling-appointment-failure-v1"
                        )),
                        Set.of()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> snapshot.providerSupports(
                        "missing-provider",
                        APPOINTMENT_EXECUTION,
                        Set.of("reschedule-appointment")
                )
        );
    }
}
