package mainstreet.surface;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExposureMemberIdentitySpecificationTest {

    private static final String RELEASE = "semantic-registry-17";

    @Test
    void singleton_membership_is_explicit_contract_meaning() {
        ExposureElementContract contract = contract(
                "profile",
                "public-display-name",
                ExposureMemberIdentitySpecification.singleton()
        );

        assertEquals(
                ExposureMemberIdentitySpecification.singleton(),
                contract.memberIdentitySpecification()
        );
    }

    @Test
    void instance_qualified_membership_retains_exact_owner_qualified_kind() {
        ExposureCandidateInstanceKindReference kind =
                new ExposureCandidateInstanceKindReference(
                        "profile",
                        "contact-point"
                );
        ExposureElementContract contract = contract(
                "profile",
                "public-contact-point",
                ExposureMemberIdentitySpecification.instanceQualified(kind)
        );

        assertEquals(
                ExposureMemberIdentitySpecification.instanceQualified(kind),
                contract.memberIdentitySpecification()
        );
    }

    @Test
    void instance_qualified_membership_rejects_cross_owner_kind() {
        assertThrows(
                IllegalArgumentException.class,
                () -> contract(
                        "profile",
                        "public-contact-point",
                        ExposureMemberIdentitySpecification.instanceQualified(
                                new ExposureCandidateInstanceKindReference(
                                        "booking",
                                        "booking"
                                )
                        )
                )
        );
    }

    @Test
    void contract_rejects_missing_membership_semantics() {
        assertThrows(
                NullPointerException.class,
                () -> new ExposureElementContract(
                        new ExposureElementContractIdentity(
                                "profile",
                                "public-display-name"
                        ),
                        new ExposableElementReference(
                                "profile",
                                "public-display-name"
                        ),
                        SurfaceAudience.PUBLIC,
                        null,
                        ExposureDecision.EXPOSE,
                        Optional.empty(),
                        Set.of()
                )
        );
    }

    @Test
    void initial_portfolio_declares_the_approved_membership_semantics() {
        ExposureElementContractRegistrySnapshot registry =
                InitialMerchantPresenceExposureContractPortfolio.forRelease(RELEASE);

        assertSingleton(registry, "profile", "public-display-name");
        assertSingleton(registry, "profile", "public-tagline");
        assertSingleton(registry, "profile", "public-short-summary");
        assertSingleton(registry, "profile", "public-approved-description");

        assertInstanceQualified(
                registry,
                "profile",
                "public-contact-point",
                "profile",
                "contact-point"
        );
        assertInstanceQualified(
                registry,
                "profile",
                "public-merchant-location",
                "profile",
                "merchant-location"
        );
        assertInstanceQualified(
                registry,
                "profile",
                "public-service-area",
                "profile",
                "service-area"
        );
        assertInstanceQualified(
                registry,
                "profile",
                "public-external-presence-link",
                "profile",
                "external-presence-link"
        );
        assertInstanceQualified(
                registry,
                "business-hours",
                "public-business-hours",
                "business-hours",
                "business-hours-scope"
        );
    }

    private static void assertSingleton(
            ExposureElementContractRegistrySnapshot registry,
            String owner,
            String identifier
    ) {
        assertEquals(
                ExposureMemberIdentitySpecification.singleton(),
                required(registry, owner, identifier).memberIdentitySpecification()
        );
    }

    private static void assertInstanceQualified(
            ExposureElementContractRegistrySnapshot registry,
            String owner,
            String identifier,
            String instanceOwner,
            String instanceKind
    ) {
        assertEquals(
                ExposureMemberIdentitySpecification.instanceQualified(
                        new ExposureCandidateInstanceKindReference(
                                instanceOwner,
                                instanceKind
                        )
                ),
                required(registry, owner, identifier).memberIdentitySpecification()
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
            ExposureMemberIdentitySpecification specification
    ) {
        return new ExposureElementContract(
                new ExposureElementContractIdentity(owner, identifier),
                new ExposableElementReference(owner, identifier),
                SurfaceAudience.PUBLIC,
                specification,
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of()
        );
    }
}
