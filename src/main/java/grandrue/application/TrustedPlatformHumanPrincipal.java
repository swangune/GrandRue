package grandrue.application;

import java.util.Objects;

/**
 * Trusted application-boundary representation of an authenticated human
 * principal acting in PLATFORM scope.
 *
 * <p>Construction of this value is downstream of MS-PROT-063 trusted-principal
 * establishment. It does not itself grant Merchant Account establishment
 * authority.</p>
 */
public record TrustedPlatformHumanPrincipal(String identityIdentifier) {

    public TrustedPlatformHumanPrincipal {
        Objects.requireNonNull(identityIdentifier, "identityIdentifier");
        if (identityIdentifier.isBlank()) {
            throw new IllegalArgumentException("identityIdentifier must not be blank");
        }
    }
}
