package grandrue.runtime;

import grandrue.application.MerchantScope;
import mainstreet.semantic.event.EventReactionContractDefinition;
import mainstreet.semantic.event.EventReactionContractIdentity;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.TrustedExecutionContext;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Current infrastructure registration of bounded scheduled principals. A
 * registration is per Event Reaction Contract; it is not a global SYSTEM
 * permission and it does not derive authority from an event receipt.
 */
public final class RegisteredScheduledEventReactionExecutionAuthority
        implements ScheduledEventReactionExecutionAuthority {
    private final Map<EventReactionContractIdentity, ExecutionPrincipal> principals;

    public RegisteredScheduledEventReactionExecutionAuthority(
            Map<EventReactionContractIdentity, ExecutionPrincipal> registeredPrincipals) {
        Objects.requireNonNull(registeredPrincipals, "registeredPrincipals");
        registeredPrincipals.forEach((identity, principal) -> {
            Objects.requireNonNull(identity, "reaction contract identity");
            Objects.requireNonNull(principal, "scheduled execution principal");
        });
        principals = Map.copyOf(registeredPrincipals);
    }

    @Override
    public TrustedExecutionContext establish(
            EventReactionContractDefinition currentContract,
            MerchantScope merchantScope) {
        Objects.requireNonNull(currentContract, "currentContract");
        Objects.requireNonNull(merchantScope, "merchantScope");
        ExecutionPrincipal principal = Optional.ofNullable(
                        principals.get(currentContract.identity()))
                .orElseThrow(() -> new IllegalStateException(
                        "Event Reaction Contract has no registered scheduled principal"));
        return new TrustedExecutionContext(
                merchantScope,
                principal,
                Optional.empty(),
                Optional.empty());
    }
}
