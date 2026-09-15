package mainstreet.runtime;

import java.util.Objects;

/**
 * One-time result of successful human Session establishment.
 *
 * <p>The Session Identity is non-secret. The credential is the bearer secret
 * that a transport adapter may place into the hardened browser-cookie boundary
 * defined by ADR-014. Rendering never exposes the raw bearer.</p>
 */
public record EstablishedHumanSession(
        String sessionIdentity,
        OpaqueSessionCredential credential
) {
    public EstablishedHumanSession {
        if (sessionIdentity == null || sessionIdentity.isBlank()) {
            throw new IllegalArgumentException(
                    "Session identity must not be blank"
            );
        }
        Objects.requireNonNull(credential, "credential");
    }

    @Override
    public String toString() {
        return "EstablishedHumanSession[sessionIdentity="
                + sessionIdentity
                + ", credential=REDACTED]";
    }
}
