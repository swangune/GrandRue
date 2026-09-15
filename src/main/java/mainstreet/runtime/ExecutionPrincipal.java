package mainstreet.runtime;

/**
 * Attributable principal for one trusted execution context.
 *
 * <p>The principal identifies who or what is acting. It deliberately carries
 * no privileges, roles or merchant authority. Those are resolved separately
 * for the current Merchant Scope through the applicable authorisation
 * authority.</p>
 *
 * <p>Governed by MS-PROT-062 and MS-PROT-063.</p>
 */
public record ExecutionPrincipal(String identifier) {

    public ExecutionPrincipal {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Execution principal identifier must not be blank"
            );
        }
    }
}
