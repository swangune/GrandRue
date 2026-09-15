package mainstreet.runtime;

import mainstreet.application.MerchantScope;

/**
 * Resolves the contextual execution principal for an authenticated identity in
 * one already-resolved Merchant Scope. Implementations may consult current
 * controller/staff/customer relationship authority as applicable.
 */
@FunctionalInterface
public interface ScopedExecutionPrincipalResolver {

    ExecutionPrincipal resolve(
            MerchantScope merchantScope,
            String authenticatedIdentityIdentifier
    );
}
