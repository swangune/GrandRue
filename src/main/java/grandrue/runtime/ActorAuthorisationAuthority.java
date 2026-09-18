package grandrue.runtime;

import grandrue.application.MerchantScope;
import grandrue.semantic.Privilege;

/**
 * Resolves whether one trusted execution principal currently holds a required
 * privilege in one explicit Merchant Scope.
 *
 * <p>This boundary does not prescribe how authority is derived. Merchant
 * Controller relationships, Merchant Memberships, groups and role assignments
 * remain owned by their accepted authorities and may back an implementation
 * later. The runtime depends only on the current scoped decision.</p>
 *
 * <p>Governed by MS-PROT-062, MS-PROT-063 and MS-PROT-074.</p>
 */
@FunctionalInterface
public interface ActorAuthorisationAuthority {

    boolean isAuthorised(
            MerchantScope merchantScope,
            ExecutionPrincipal principal,
            Privilege requiredPrivilege
    );
}
