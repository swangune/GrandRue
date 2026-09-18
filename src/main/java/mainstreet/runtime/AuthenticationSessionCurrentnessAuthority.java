package mainstreet.runtime;

import grandrue.runtime.AuthenticationSessionCurrentness;

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
