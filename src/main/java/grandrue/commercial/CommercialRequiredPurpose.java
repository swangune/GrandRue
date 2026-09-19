package grandrue.commercial;

import java.util.Objects;

/**
 * One exact supporting Commercial target/purpose pair.
 * MS-PROT-056 v1.10 — Initial Standard Commercial Catalogue Manifest,
 * §§11–15 — conditional and composed protected-purpose support.
 */
public record CommercialRequiredPurpose(
        CommercialAccessTarget target,
        String protectedPurpose
) {
    public CommercialRequiredPurpose {
        Objects.requireNonNull(target, "target");
        CommercialAccessTarget.requireExactReference(protectedPurpose, "protectedPurpose");
    }
}
