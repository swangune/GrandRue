package mainstreet.privacy;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Privacy-only eligibility for unchanged personal media use.
 *
 * <p>Every applicable required subject must have a current basis for the exact
 * data scope, purpose and audience. This authority does not establish Media
 * existence, Exposure, guardian status or editorial transformation rights.</p>
 */
public final class PersonalMediaUseEligibilityAuthority {

    private final PersonalDataUseBasisAuthority basisAuthority;

    public PersonalMediaUseEligibilityAuthority(
            PersonalDataUseBasisAuthority basisAuthority) {
        this.basisAuthority = Objects.requireNonNull(basisAuthority, "basisAuthority");
    }

    public boolean isEligible(
            MerchantScope merchantScope,
            String mediaAssetIdentity,
            Collection<DataSubjectReference> requiredSubjects,
            DataUsePurpose purpose,
            String audienceScopeIdentifier,
            Instant instant) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(mediaAssetIdentity, "mediaAssetIdentity");
        Objects.requireNonNull(requiredSubjects, "requiredSubjects");
        Objects.requireNonNull(purpose, "purpose");
        requireIdentifier(audienceScopeIdentifier, "audienceScopeIdentifier");
        Objects.requireNonNull(instant, "instant");

        Set<DataSubjectReference> distinctSubjects = new LinkedHashSet<>(requiredSubjects);
        for (DataSubjectReference subject : distinctSubjects) {
            Objects.requireNonNull(subject, "required subject");
            if (!subject.merchantScope().equals(merchantScope)) {
                throw new IllegalArgumentException(
                        "Personal-media subject reference crosses Merchant Scope"
                );
            }
            if (!basisAuthority.hasEffectiveBasis(
                    subject,
                    mediaAssetIdentity,
                    purpose,
                    audienceScopeIdentifier,
                    instant
            )) {
                return false;
            }
        }
        return true;
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
