package grandrue.surface;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExposureElementContractRegistrySnapshotTest {

    private static final String RELEASE = "semantic-registry-17";

    @Test
    void resolves_only_the_exact_contract_from_the_exact_release() {
        ExposureElementContract contract = contract(
                "profile",
                "public-contact-point",
                ExposureDecision.WITHHOLD
        );
        ExposureElementContractRegistrySnapshot registry =
                new ExposureElementContractRegistrySnapshot(
                        RELEASE,
                        Set.of(contract)
                );

        assertEquals(
                Optional.of(contract),
                registry.contract(RELEASE, contract.identity())
        );
        assertFalse(registry.contract(
                "semantic-registry-18",
                contract.identity()
        ).isPresent());
        assertFalse(registry.contract(
                RELEASE,
                new ExposureElementContractIdentity(
                        "profile",
                        "merchant-internal-risk-score"
                )
        ).isPresent());
    }

    @Test
    void rejects_duplicate_contract_identity_within_one_release() {
        ExposureElementContract first = contract(
                "profile",
                "public-contact-point",
                ExposureDecision.WITHHOLD
        );
        ExposureElementContract conflicting = new ExposureElementContract(
                first.identity(),
                first.exposableElementReference(),
                SurfaceAudience.PUBLIC,
                first.memberIdentitySpecification(),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExposureElementContractRegistrySnapshot(
                        RELEASE,
                        Set.of(first, conflicting)
                )
        );
    }

    @Test
    void contract_collections_and_registry_definitions_are_immutable() {
        ExposureRequirementReference requirement =
                new ExposureRequirementReference(
                        "data-protection",
                        "purpose-permits-public-observation"
                );
        ExposureElementContract contract = new ExposureElementContract(
                new ExposureElementContractIdentity(
                        "profile",
                        "public-contact-point"
                ),
                new ExposableElementReference(
                        "profile",
                        "public-contact-point"
                ),
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.instanceQualified(
                        new ExposureCandidateInstanceKindReference(
                                "profile",
                                "contact-point"
                        )
                ),
                ExposureDecision.WITHHOLD,
                Optional.of(new MerchantExposureChoiceSourceReference(
                        "profile",
                        "contact-point-public-exposure"
                )),
                Set.of(requirement)
        );
        ExposureElementContractRegistrySnapshot registry =
                new ExposureElementContractRegistrySnapshot(
                        RELEASE,
                        Set.of(contract)
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> contract.requirementReferences().clear()
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> registry.contracts().clear()
        );
    }

    @Test
    void registers_the_exact_initial_public_merchant_presence_portfolio() {
        ExposureElementContractRegistrySnapshot registry =
                InitialMerchantPresenceExposureContractPortfolio
                        .forRelease(RELEASE);

        assertEquals(RELEASE, registry.semanticRegistryReleaseIdentifier());
        assertEquals(9, registry.contracts().size());
        assertEquals(
                Set.of(
                        "profile/public-display-name",
                        "profile/public-tagline",
                        "profile/public-short-summary",
                        "profile/public-approved-description",
                        "profile/public-contact-point",
                        "profile/public-merchant-location",
                        "profile/public-service-area",
                        "profile/public-external-presence-link",
                        "business-hours/public-business-hours"
                ),
                registry.contracts().stream()
                        .map(value -> value.identity().ownerIdentifier()
                                + "/"
                                + value.identity().contractIdentifier())
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );

        ExposureElementContract contact = required(
                registry,
                "profile",
                "public-contact-point"
        );
        assertEquals(SurfaceAudience.PUBLIC, contact.audience());
        assertEquals(ExposureDecision.WITHHOLD, contact.baselineDecision());
        assertEquals(
                Optional.of(new MerchantExposureChoiceSourceReference(
                        "profile",
                        "contact-point-public-exposure"
                )),
                contact.merchantChoiceSource()
        );
        assertTrue(contact.requirementReferences().isEmpty());

        ExposureElementContract location = required(
                registry,
                "profile",
                "public-merchant-location"
        );
        assertEquals(ExposureDecision.WITHHOLD, location.baselineDecision());
        assertEquals(
                Optional.of(new MerchantExposureChoiceSourceReference(
                        "profile",
                        "merchant-location-public-exposure"
                )),
                location.merchantChoiceSource()
        );

        assertEquals(
                ExposureDecision.EXPOSE,
                required(
                        registry,
                        "business-hours",
                        "public-business-hours"
                ).baselineDecision()
        );
        assertTrue(registry.contracts().stream()
                .allMatch(value -> value.audience() == SurfaceAudience.PUBLIC));
    }

    @Test
    void rejects_blank_owner_qualified_identifiers() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ExposureElementContractIdentity(" ", "contract")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ExposableElementReference("profile", " ")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantExposureChoiceSourceReference("", "source")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ExposureRequirementReference("owner", null)
        );
    }

    private static ExposureElementContract required(
            ExposureElementContractRegistrySnapshot registry,
            String owner,
            String identifier
    ) {
        return registry.contract(
                RELEASE,
                new ExposureElementContractIdentity(owner, identifier)
        ).orElseThrow();
    }

    private static ExposureElementContract contract(
            String owner,
            String identifier,
            ExposureDecision baseline
    ) {
        return new ExposureElementContract(
                new ExposureElementContractIdentity(owner, identifier),
                new ExposableElementReference(owner, identifier),
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.instanceQualified(
                        new ExposureCandidateInstanceKindReference(
                                owner,
                                "contact-point"
                        )
                ),
                baseline,
                Optional.empty(),
                Set.of()
        );
    }
}
