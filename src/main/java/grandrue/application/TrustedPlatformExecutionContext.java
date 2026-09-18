package grandrue.application;

import mainstreet.runtime.ExecutionPrincipal;
import java.util.Objects;

/**
 * Attribution established by a trusted PLATFORM-scope boundary, not permission.
 * No merchant is fabricated for platform-owned operations.
 * MS-PROT-063 v1.0 — Authentication, Session & Trusted Execution Principal Establishment Model,
 * §3.4 — Trusted Execution Context; §19 — Platform scope; §20 — Context propagation boundary.
 */
public record TrustedPlatformExecutionContext(ExecutionPrincipal principal) {
    public TrustedPlatformExecutionContext {
        Objects.requireNonNull(principal, "principal");
    }
}
