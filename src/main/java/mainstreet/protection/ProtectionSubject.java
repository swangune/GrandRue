package mainstreet.protection;

/**
 * Operational scope whose consumption is measured or restricted by one
 * protection policy. Scope identifiers remain extensible and do not imply
 * trusted identity merely by participation in protection correlation.
 */
public record ProtectionSubject(
        String scopeIdentifier,
        String subjectIdentifier
) {

    public ProtectionSubject {
        if (scopeIdentifier == null || scopeIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Protection subject scope identifier must not be blank"
            );
        }
        if (subjectIdentifier == null || subjectIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Protection subject identifier must not be blank"
            );
        }
    }
}
