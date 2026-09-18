package grandrue.runtime;

/**
 * Current security-generation authority for one authenticated Identity.
 *
 * <p>A security-generation reference is session invalidation evidence. It is
 * not Merchant Scope, relationship, role, privilege or business authority.</p>
 */
@FunctionalInterface
public interface IdentitySecurityGenerationAuthority {

    String currentSecurityGenerationReference(String identityReference);
}
