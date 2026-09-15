package mainstreet.publication;

/**
 * Raised when one durable application request identity is presented with intent that differs
 * from the already-committed Publication operation bound to that identity.
 */
public final class PublicationApplicationRequestConflictException extends RuntimeException {

    public PublicationApplicationRequestConflictException(String requestIdentity) {
        super("Publication application request identity is bound to different intent: "
                + requestIdentity);
    }
}
