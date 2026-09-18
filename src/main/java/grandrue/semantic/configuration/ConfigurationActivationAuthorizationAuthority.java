package grandrue.semantic.configuration;

/**
 * Current business-authority decision for one exact Merchant Configuration
 * activation attempt.
 *
 * <p>This authority does not establish authentication and must not interpret
 * caller-supplied identifiers as trusted identity by themselves. Production
 * activation first binds a request to a TrustedExecutionContext at the
 * application boundary, then evaluates this current business authority inside
 * the serialized activation transaction.</p>
 *
 * <p>For the current Main Street MVP, approved MS-PROT-040 v1.7 establishes
 * ordinary non-initial Configuration activation authority as the current
 * active Merchant Controller of an OPEN, unsuspended Merchant Account.</p>
 *
 * Authority: composite MS-PROT-040 through approved v1.7.
 */
@FunctionalInterface
public interface ConfigurationActivationAuthorizationAuthority {

    boolean isAuthorized(
            String initiatingPrincipalIdentifier,
            String merchantIdentifier,
            String configurationRevisionIdentifier
    );
}