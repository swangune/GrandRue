package mainstreet.surface;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExposureElementContractElementAudienceIndexTest {

    private static final String RELEASE = "semantic-registry-18";
    private static final ExposableElementReference ELEMENT =
            new ExposableElementReference("profile", "public-display-name");

    @Test
    void resolves_contract_by_exact_release_element_and_audience_without_contract_identity() {
        ExposureElementContract publicContract = contract(
                "public-display-name",
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton()
        );
        ExposureElementContract customerContract = contract(
                "customer-display-name",
                SurfaceAudience.CUSTOMER,
                ExposureMemberIdentitySpecification.singleton()
        );
        ExposureElementContractRegistrySnapshot registry = registry(
                publicContract,
                customerContract
        );

        assertEquals(
                Optional.of(publicContract),
                registry.contract(RELEASE, ELEMENT, SurfaceAudience.PUBLIC)
        );
        assertEquals(
                Optional.of(customerContract),
                registry.contract(RELEASE, ELEMENT, SurfaceAudience.CUSTOMER)
        );
        assertFalse(registry.contract(
                "semantic-registry-other",
                ELEMENT,
                SurfaceAudience.PUBLIC
        ).isPresent());
    }

    @Test
    void known_element_is_distinct_from_no_contract_for_requested_audience() {
        ExposureElementContractRegistrySnapshot registry = registry(contract(
                "public-display-name",
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton()
        ));

        assertTrue(registry.containsElement(RELEASE, ELEMENT));
        assertFalse(registry.contract(
                RELEASE,
                ELEMENT,
                SurfaceAudience.CUSTOMER
        ).isPresent());
        assertFalse(registry.containsElement(
                RELEASE,
                new ExposableElementReference("profile", "unknown-element")
        ));
    }

    @Test
    void rejects_duplicate_element_audience_variant_even_when_contract_ids_differ() {
        assertThrows(
                IllegalArgumentException.class,
                () -> registry(
                        contract(
                                "public-display-name-v1",
                                SurfaceAudience.PUBLIC,
                                ExposureMemberIdentitySpecification.singleton()
                        ),
                        contract(
                                "public-display-name-v2",
                                SurfaceAudience.PUBLIC,
                                ExposureMemberIdentitySpecification.singleton()
                        )
                )
        );
    }

    @Test
    void audience_variants_for_one_element_must_share_member_identity_semantics() {
        assertThrows(
                IllegalArgumentException.class,
                () -> registry(
                        contract(
                                "public-display-name",
                                SurfaceAudience.PUBLIC,
                                ExposureMemberIdentitySpecification.singleton()
                        ),
                        contract(
                                "customer-display-name",
                                SurfaceAudience.CUSTOMER,
                                ExposureMemberIdentitySpecification.instanceQualified(
                                        new ExposureCandidateInstanceKindReference(
                                                "profile",
                                                "merchant-location"
                                        )
                                )
                        )
                )
        );
    }

    @Test
    void element_audience_index_is_immutable_and_deterministic() {
        ExposureElementContract publicContract = contract(
                "public-display-name",
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton()
        );
        ExposureElementContract customerContract = contract(
                "customer-display-name",
                SurfaceAudience.CUSTOMER,
                ExposureMemberIdentitySpecification.singleton()
        );
        ExposureElementContractRegistrySnapshot registry = registry(
                customerContract,
                publicContract
        );

        assertEquals(
                Set.of(SurfaceAudience.PUBLIC, SurfaceAudience.CUSTOMER),
                registry.audiences(RELEASE, ELEMENT)
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> registry.audiences(RELEASE, ELEMENT).clear()
        );
    }

    private static ExposureElementContractRegistrySnapshot registry(
            ExposureElementContract... contracts
    ) {
        return new ExposureElementContractRegistrySnapshot(
                RELEASE,
                Set.of(contracts)
        );
    }

    private static ExposureElementContract contract(
            String contractIdentifier,
            SurfaceAudience audience,
            ExposureMemberIdentitySpecification identitySpecification
    ) {
        return new ExposureElementContract(
                new ExposureElementContractIdentity(
                        ELEMENT.ownerIdentifier(),
                        contractIdentifier
                ),
                ELEMENT,
                audience,
                identitySpecification,
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of()
        );
    }
}
