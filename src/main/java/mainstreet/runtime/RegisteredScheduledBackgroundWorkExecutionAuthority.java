package mainstreet.runtime;

import mainstreet.application.MerchantScope;
import grandrue.background.BackgroundWorkContractDefinition;
import grandrue.background.BackgroundWorkContractIdentity;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Current contract-bounded scheduled-principal registration for background work. */
public final class RegisteredScheduledBackgroundWorkExecutionAuthority
        implements ScheduledBackgroundWorkExecutionAuthority {
    private final Map<BackgroundWorkContractIdentity, ExecutionPrincipal> principals;

    public RegisteredScheduledBackgroundWorkExecutionAuthority(
            Map<BackgroundWorkContractIdentity, ExecutionPrincipal> registeredPrincipals) {
        Objects.requireNonNull(registeredPrincipals, "registeredPrincipals");
        registeredPrincipals.forEach((identity, principal) -> {
            Objects.requireNonNull(identity, "background work contract identity");
            Objects.requireNonNull(principal, "scheduled execution principal");
        });
        principals = Map.copyOf(registeredPrincipals);
    }

    @Override
    public TrustedExecutionContext establish(
            BackgroundWorkContractDefinition currentContract,
            MerchantScope merchantScope) {
        Objects.requireNonNull(currentContract, "currentContract");
        Objects.requireNonNull(merchantScope, "merchantScope");
        ExecutionPrincipal principal = Optional.ofNullable(
                        principals.get(currentContract.identity()))
                .orElseThrow(() -> new IllegalStateException(
                        "Background Work Contract has no registered scheduled principal"));
        return new TrustedExecutionContext(
                merchantScope, principal, Optional.empty(), Optional.empty());
    }
}
