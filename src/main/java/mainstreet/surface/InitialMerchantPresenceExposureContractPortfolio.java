package mainstreet.surface;

import java.util.Optional;
import java.util.Set;

/**
 * Code-owned initial PUBLIC Merchant Presence portfolio accepted by
 * MS-PROT-027 v1.5 and evolved by v1.11. Exposure-definition encoding remains
 * a separate sequential implementation node.
 */
public final class InitialMerchantPresenceExposureContractPortfolio {

    private InitialMerchantPresenceExposureContractPortfolio() {
    }

    public static ExposureElementContractRegistrySnapshot forRelease(
            String semanticRegistryReleaseIdentifier
    ) {
        return new ExposureElementContractRegistrySnapshot(
                semanticRegistryReleaseIdentifier,
                Set.of(
                        baselineExpose(
                                "profile",
                                "public-display-name",
                                ExposureMemberIdentitySpecification.singleton()
                        ),
                        baselineExpose(
                                "profile",
                                "public-tagline",
                                ExposureMemberIdentitySpecification.singleton()
                        ),
                        baselineExpose(
                                "profile",
                                "public-short-summary",
                                ExposureMemberIdentitySpecification.singleton()
                        ),
                        baselineExpose(
                                "profile",
                                "public-approved-description",
                                ExposureMemberIdentitySpecification.singleton()
                        ),
                        merchantChoice(
                                "profile",
                                "public-contact-point",
                                "contact-point-public-exposure",
                                instanceQualified("profile", "contact-point")
                        ),
                        merchantChoice(
                                "profile",
                                "public-merchant-location",
                                "merchant-location-public-exposure",
                                instanceQualified("profile", "merchant-location")
                        ),
                        baselineExpose(
                                "profile",
                                "public-service-area",
                                instanceQualified("profile", "service-area")
                        ),
                        baselineExpose(
                                "profile",
                                "public-external-presence-link",
                                instanceQualified("profile", "external-presence-link")
                        ),
                        baselineExpose(
                                "business-hours",
                                "public-business-hours",
                                instanceQualified(
                                        "business-hours",
                                        "business-hours-scope"
                                )
                        )
                )
        );
    }

    private static ExposureMemberIdentitySpecification instanceQualified(
            String owner,
            String instanceKind
    ) {
        return ExposureMemberIdentitySpecification.instanceQualified(
                new ExposureCandidateInstanceKindReference(owner, instanceKind)
        );
    }

    private static ExposureElementContract baselineExpose(
            String owner,
            String identifier,
            ExposureMemberIdentitySpecification memberIdentitySpecification
    ) {
        return contract(
                owner,
                identifier,
                memberIdentitySpecification,
                ExposureDecision.EXPOSE,
                Optional.empty()
        );
    }

    private static ExposureElementContract merchantChoice(
            String owner,
            String identifier,
            String sourceIdentifier,
            ExposureMemberIdentitySpecification memberIdentitySpecification
    ) {
        return contract(
                owner,
                identifier,
                memberIdentitySpecification,
                ExposureDecision.WITHHOLD,
                Optional.of(new MerchantExposureChoiceSourceReference(
                        owner,
                        sourceIdentifier
                ))
        );
    }

    private static ExposureElementContract contract(
            String owner,
            String identifier,
            ExposureMemberIdentitySpecification memberIdentitySpecification,
            ExposureDecision baselineDecision,
            Optional<MerchantExposureChoiceSourceReference> choiceSource
    ) {
        return new ExposureElementContract(
                new ExposureElementContractIdentity(owner, identifier),
                new ExposableElementReference(owner, identifier),
                SurfaceAudience.PUBLIC,
                memberIdentitySpecification,
                baselineDecision,
                choiceSource,
                Set.of()
        );
    }
}
