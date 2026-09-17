package grandrue.infrastructure.security.webauthn;

import org.springframework.security.web.webauthn.api.AuthenticatorSelectionCriteria;
import org.springframework.security.web.webauthn.api.UserVerificationRequirement;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

/**
 * ADR-015 production policy for the privileged Main Street WebAuthn relying party.
 *
 * <p>The policy is intentionally independent of merchant storefront/custom domains.
 * Only explicitly configured HTTPS origins participate in privileged authentication.</p>
 */
public record PrivilegedWebAuthnRelyingPartyPolicy(
        String relyingPartyIdentifier,
        String relyingPartyDisplayName,
        Set<String> allowedOrigins
) {

    public PrivilegedWebAuthnRelyingPartyPolicy {
        relyingPartyIdentifier = requireText(relyingPartyIdentifier, "relyingPartyIdentifier");
        relyingPartyDisplayName = requireText(relyingPartyDisplayName, "relyingPartyDisplayName");
        Objects.requireNonNull(allowedOrigins, "allowedOrigins");
        if (allowedOrigins.isEmpty()) {
            throw new IllegalArgumentException("At least one privileged WebAuthn origin is required");
        }
        allowedOrigins.forEach(PrivilegedWebAuthnRelyingPartyPolicy::requireHttpsOrigin);
        allowedOrigins = Set.copyOf(allowedOrigins);
    }

    public UserVerificationRequirement userVerificationRequirement() {
        return UserVerificationRequirement.REQUIRED;
    }

    public AuthenticatorSelectionCriteria registrationAuthenticatorSelection() {
        return AuthenticatorSelectionCriteria.builder()
                .userVerification(userVerificationRequirement())
                .build();
    }

    private static String requireText(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
        return value;
    }

    private static void requireHttpsOrigin(String value) {
        String origin = requireText(value, "allowedOrigin");
        URI uri;
        try {
            uri = URI.create(origin);
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid privileged WebAuthn origin: " + origin, ex);
        }

        boolean invalid = !"https".equalsIgnoreCase(uri.getScheme())
                || uri.getHost() == null
                || uri.getHost().isBlank()
                || uri.getUserInfo() != null
                || (uri.getRawPath() != null && !uri.getRawPath().isEmpty())
                || uri.getRawQuery() != null
                || uri.getRawFragment() != null;
        if (invalid) {
            throw new IllegalArgumentException(
                    "Privileged WebAuthn origins must be explicit HTTPS origins without path, query, fragment or user info: "
                            + origin
            );
        }
    }
}
