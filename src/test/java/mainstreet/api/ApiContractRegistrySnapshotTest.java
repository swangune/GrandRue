package mainstreet.api;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiContractRegistrySnapshotTest {

    private static final ApiContractIdentity IDENTITY =
            new ApiContractIdentity("ordering", "public-order-create");

    @Test
    void resolves_registered_contract_by_owner_qualified_logical_identity() {
        ApiContractRegistration registration = registration(
                ApiSurfaceClass.PUBLIC,
                ApiContractKind.COMMAND
        );
        ApiContractRegistrySnapshot snapshot =
                new ApiContractRegistrySnapshot(Set.of(registration));

        assertEquals(registration, snapshot.contract(IDENTITY).orElseThrow());
        assertTrue(snapshot.contracts().contains(registration));
        assertFalse(snapshot.contract(
                new ApiContractIdentity("ordering", "another-contract")
        ).isPresent());
    }

    @Test
    void preserves_the_complete_initial_surface_and_contract_kind_vocabularies() {
        assertEquals(
                Set.of(
                        ApiSurfaceClass.PUBLIC,
                        ApiSurfaceClass.CUSTOMER_CONTEXTUAL,
                        ApiSurfaceClass.MERCHANT_OPERATIONAL,
                        ApiSurfaceClass.PLATFORM_IDENTITY_BOOTSTRAP,
                        ApiSurfaceClass.PLATFORM_ADMINISTRATIVE,
                        ApiSurfaceClass.INTEGRATION_INGRESS
                ),
                Set.of(ApiSurfaceClass.values())
        );
        assertEquals(
                Set.of(
                        ApiContractKind.COMMAND,
                        ApiContractKind.QUERY,
                        ApiContractKind.CALLBACK,
                        ApiContractKind.MEDIA_TRANSFER
                ),
                Set.of(ApiContractKind.values())
        );
    }

    @Test
    void rejects_duplicate_logical_contract_identity_even_when_metadata_differs() {
        ApiContractRegistration publicCommand = registration(
                ApiSurfaceClass.PUBLIC,
                ApiContractKind.COMMAND
        );
        ApiContractRegistration merchantQuery = new ApiContractRegistration(
                IDENTITY,
                ApiSurfaceClass.MERCHANT_OPERATIONAL,
                ApiContractKind.QUERY,
                new ApiOwnerContractReference("ordering", "order-observation"),
                "merchant-membership-scope"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiContractRegistrySnapshot(
                        Set.of(publicCommand, merchantQuery)
                )
        );
    }

    @Test
    void registry_is_immutable() {
        ApiContractRegistrySnapshot snapshot = new ApiContractRegistrySnapshot(
                Set.of(registration(ApiSurfaceClass.PUBLIC, ApiContractKind.COMMAND))
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> snapshot.contracts().clear()
        );
    }

    @Test
    void identities_and_authority_references_reject_blank_values() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiContractIdentity(" ", "public-order-create")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiOwnerContractReference("ordering", " ")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiContractRegistration(
                        IDENTITY,
                        ApiSurfaceClass.PUBLIC,
                        ApiContractKind.COMMAND,
                        new ApiOwnerContractReference("ordering", "order-create"),
                        " "
                )
        );
    }

    private static ApiContractRegistration registration(
            ApiSurfaceClass surface,
            ApiContractKind kind
    ) {
        return new ApiContractRegistration(
                IDENTITY,
                surface,
                kind,
                new ApiOwnerContractReference("ordering", "order-create"),
                "trusted-public-merchant-scope"
        );
    }
}
