package grandrue.publication;

/**
 * Raised when a Publication persistence mutation targets stale lifecycle or publication
 * evidence while its logical revision identity is still current.
 */
public final class PublicationStateConflictException extends RuntimeException {
    public PublicationStateConflictException(
            OpportunityPublicationState expectedState,
            OpportunityPublicationState currentState
    ) {
        super("Opportunity " + expectedState.opportunityIdentity()
                + " expected publication state [revision="
                + expectedState.currentRevisionIdentity()
                + ", lifecycle=" + expectedState.lifecycle()
                + ", publishedRevision="
                + expectedState.publishedRevisionIdentity().orElse("<none>")
                + "] but current state is [revision="
                + currentState.currentRevisionIdentity()
                + ", lifecycle=" + currentState.lifecycle()
                + ", publishedRevision="
                + currentState.publishedRevisionIdentity().orElse("<none>")
                + "]");
    }
}
