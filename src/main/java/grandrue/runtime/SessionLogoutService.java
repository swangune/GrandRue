package grandrue.runtime;

import mainstreet.runtime.AuthenticationException;
import mainstreet.runtime.AuthenticationFailureCategory;
import mainstreet.runtime.SessionRecord;
import mainstreet.runtime.SessionRecordStore;

import java.time.Clock;
import java.util.Objects;

/**
 * Revokes the durable ADR-014 Session Record selected by a presented opaque
 * bearer credential.
 *
 * <p>Logout changes session continuity only. It does not change Identity,
 * Merchant Scope, merchant relationships, roles, privileges or other business
 * authority.</p>
 */
public final class SessionLogoutService {

    private static final String LOGOUT_REASON = "logout";

    private final SessionRecordStore sessionRecordStore;
    private final Clock clock;

    public SessionLogoutService(
            SessionRecordStore sessionRecordStore,
            Clock clock
    ) {
        this.sessionRecordStore = Objects.requireNonNull(
                sessionRecordStore,
                "sessionRecordStore"
        );
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public SessionRecord logout(String presentedCredential) {
        String verifier = OpaqueSessionCredential
                .verifierForPresentedCredential(presentedCredential);
        SessionRecord selected = sessionRecordStore
                .sessionByCredentialVerifier(verifier)
                .orElseThrow(() -> new AuthenticationException(
                        AuthenticationFailureCategory.AUTHENTICATION_FAILED,
                        "Session credential is not current"
                ));

        // A retry after the original revocation committed must not mutate or
        // replace the authoritative revocation evidence.
        if (selected.revoked()) {
            return selected;
        }

        return sessionRecordStore.revoke(
                selected.sessionIdentity(),
                clock.instant(),
                LOGOUT_REASON
        );
    }
}
