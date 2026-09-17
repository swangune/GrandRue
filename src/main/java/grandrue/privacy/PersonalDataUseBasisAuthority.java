package grandrue.privacy;

import java.time.Instant;

/** Current purpose/audience-bound personal-data use authority. */
@FunctionalInterface
public interface PersonalDataUseBasisAuthority {

    boolean hasEffectiveBasis(
            DataSubjectReference subject,
            String dataScopeIdentifier,
            DataUsePurpose purpose,
            String audienceScopeIdentifier,
            Instant instant
    );
}
