package mainstreet.publication;

/** Raised when a Publication mutation targets a revision that is no longer current. */
public final class PublicationRevisionConflictException extends RuntimeException {
    public PublicationRevisionConflictException(
            String opportunityIdentity,
            String expectedRevisionIdentity,
            String currentRevisionIdentity
    ) {
        super("Opportunity " + opportunityIdentity
                + " expected current revision " + expectedRevisionIdentity
                + " but current revision is " + currentRevisionIdentity);
    }
}
