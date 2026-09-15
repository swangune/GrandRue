package mainstreet.merchantprofile;

import java.util.Objects;

/** Explicit optimistic-concurrency expectation for Location Exposure choice. */
public sealed interface ExpectedMerchantLocationExposureChoice
        permits ExpectedMerchantLocationExposureChoice.Absent,
        ExpectedMerchantLocationExposureChoice.Revision {

    static ExpectedMerchantLocationExposureChoice absent() {
        return new Absent();
    }

    static ExpectedMerchantLocationExposureChoice revision(String revisionIdentity) {
        return new Revision(revisionIdentity);
    }

    record Absent() implements ExpectedMerchantLocationExposureChoice {
    }

    record Revision(String revisionIdentity)
            implements ExpectedMerchantLocationExposureChoice {
        public Revision {
            Objects.requireNonNull(revisionIdentity, "revisionIdentity");
            if (revisionIdentity.isBlank()) {
                throw new IllegalArgumentException(
                        "Expected Location Exposure choice revision must not be blank"
                );
            }
        }
    }
}
