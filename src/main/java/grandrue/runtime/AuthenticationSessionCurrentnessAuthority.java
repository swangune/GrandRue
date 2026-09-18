package grandrue.runtime;

import mainstreet.runtime.AuthenticationProvenance;

/**
 * Current runtime/authentication authority for one immutable authentication
 * provenance.
 */
@FunctionalInterface
public interface AuthenticationSessionCurrentnessAuthority {

    AuthenticationSessionCurrentness evaluate(
            AuthenticationProvenance provenance
    );
}
