package grandrue.identitysecurity;

/**
 * Required bootstrap boundary when an Identity's authentication security state
 * is established.
 */
@FunctionalInterface
public interface IdentitySecurityGenerationInitializer {

    void initialize(String identityReference);
}
