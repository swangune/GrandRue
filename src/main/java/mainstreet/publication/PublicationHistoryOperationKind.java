package mainstreet.publication;

import java.util.Objects;
import java.util.Optional;

/** Publication History operation kinds governed by MS-PROT-046 v1.3. */
public enum PublicationHistoryOperationKind {
    PUBLISH,
    WITHDRAW,
    REPUBLISH;

    /**
     * Classifies an already-valid same-Opportunity state change. Material revision-only advances
     * intentionally produce no Publication History entry.
     */
    public static Optional<PublicationHistoryOperationKind> between(
            OpportunityPublicationState expectedState,
            OpportunityPublicationState nextState
    ) {
        Objects.requireNonNull(expectedState, "expectedState");
        Objects.requireNonNull(nextState, "nextState");
        requireSamePublication(expectedState, nextState);

        boolean revisionChanged = !expectedState.currentRevisionIdentity().equals(
                nextState.currentRevisionIdentity()
        );
        if (revisionChanged) {
            if (expectedState.lifecycle() != nextState.lifecycle()
                    || !expectedState.publishedRevisionIdentity().equals(
                            nextState.publishedRevisionIdentity()
                    )) {
                throw new IllegalArgumentException(
                        "A material revision advance cannot also change Publication lifecycle or published revision"
                );
            }
            return Optional.empty();
        }

        if (expectedState.equals(nextState)) {
            return Optional.empty();
        }

        if (expectedState.lifecycle() == PublicationLifecycle.DRAFT
                && nextState.lifecycle() == PublicationLifecycle.PUBLISHED
                && publishedCurrent(nextState)) {
            return Optional.of(PUBLISH);
        }

        if (expectedState.lifecycle() == PublicationLifecycle.PUBLISHED
                && nextState.lifecycle() == PublicationLifecycle.PUBLISHED
                && !expectedState.publishedRevisionIdentity().equals(
                        nextState.publishedRevisionIdentity()
                )
                && publishedCurrent(nextState)) {
            return Optional.of(PUBLISH);
        }

        if (expectedState.lifecycle() == PublicationLifecycle.PUBLISHED
                && nextState.lifecycle() == PublicationLifecycle.WITHDRAWN
                && expectedState.publishedRevisionIdentity().equals(
                        nextState.publishedRevisionIdentity()
                )) {
            return Optional.of(WITHDRAW);
        }

        if (expectedState.lifecycle() == PublicationLifecycle.WITHDRAWN
                && nextState.lifecycle() == PublicationLifecycle.PUBLISHED
                && publishedCurrent(nextState)) {
            return Optional.of(REPUBLISH);
        }

        throw new IllegalArgumentException(
                "Unsupported Opportunity Publication state transition"
        );
    }

    private static boolean publishedCurrent(OpportunityPublicationState state) {
        return state.publishedRevisionIdentity()
                .filter(state.currentRevisionIdentity()::equals)
                .isPresent();
    }

    private static void requireSamePublication(
            OpportunityPublicationState expectedState,
            OpportunityPublicationState nextState
    ) {
        if (!expectedState.merchantScope().equals(nextState.merchantScope())
                || !expectedState.opportunityIdentity().equals(nextState.opportunityIdentity())) {
            throw new IllegalArgumentException(
                    "Expected and next Publication state must refer to the same merchant-owned Opportunity"
            );
        }
    }
}
