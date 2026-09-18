package grandrue.workforce;

import grandrue.application.MerchantScope;
import grandrue.runtime.ActorAuthorisationAuthority;
import grandrue.runtime.ExecutionPrincipal;
import mainstreet.semantic.Privilege;

import java.time.Clock;
import java.util.Objects;

/**
 * Workforce-backed implementation of the runtime Actor Authorisation boundary.
 *
 * <p>The execution principal contributes identity/attribution only. Current
 * merchant-scoped workforce facts determine whether the requested registered
 * privilege is held at the current instant.</p>
 *
 * <p>Merchant Operational Device Context remains a separate trusted-context
 * prerequisite under MS-PROT-063 v1.1 / MS-PROT-074 and is deliberately not
 * converted into Actor Authorisation here.</p>
 */
public final class WorkforceActorAuthorisationAuthority
        implements ActorAuthorisationAuthority {

    private final MerchantWorkforceAuthority workforceAuthority;
    private final Clock clock;

    public WorkforceActorAuthorisationAuthority(
            MerchantWorkforceAuthority workforceAuthority,
            Clock clock
    ) {
        this.workforceAuthority = Objects.requireNonNull(
                workforceAuthority,
                "workforceAuthority"
        );
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    @Override
    public boolean isAuthorised(
            MerchantScope merchantScope,
            ExecutionPrincipal principal,
            Privilege requiredPrivilege
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(principal, "principal");
        Objects.requireNonNull(requiredPrivilege, "requiredPrivilege");
        return workforceAuthority.hasPrivilege(
                merchantScope,
                principal.identifier(),
                requiredPrivilege,
                clock.instant()
        );
    }
}
