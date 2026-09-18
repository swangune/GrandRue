package grandrue.enquiry;

import grandrue.surface.ExposureDecision;
import grandrue.surface.ExposureElementContract;
import grandrue.surface.ExposureElementContractIdentity;
import grandrue.surface.ExposureElementContractRegistrySnapshot;
import grandrue.surface.ExposureMemberIdentitySpecification;
import grandrue.surface.SurfaceAudience;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Initial four MERCHANT-only families. EXPOSE is conditional on the owner observation requirement,
 * never a default permission for an admitted merchant actor. No PUBLIC/CUSTOMER contracts exist.
 */
public final class EnquiryMerchantExposureContractPortfolio {
    private EnquiryMerchantExposureContractPortfolio() { }

    public static Set<ExposureElementContract> contracts() {
        return EnquiryMerchantExposureReferences.elements().stream().map(element -> new ExposureElementContract(
                new ExposureElementContractIdentity("enquiry", element.elementIdentifier()),
                element, SurfaceAudience.MERCHANT,
                ExposureMemberIdentitySpecification.instanceQualified(
                        EnquiryMerchantExposureReferences.ENQUIRY_INSTANCE_KIND),
                ExposureDecision.EXPOSE, Optional.empty(),
                Set.of(EnquiryMerchantExposureReferences.OBSERVATION_REQUIREMENT)))
                .collect(Collectors.toUnmodifiableSet());
    }

    public static ExposureElementContractRegistrySnapshot forRelease(String semanticRegistryReleaseIdentifier) {
        return new ExposureElementContractRegistrySnapshot(semanticRegistryReleaseIdentifier, contracts());
    }
}
