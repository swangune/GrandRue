package grandrue.publication;

import grandrue.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/**
 * Immutable Publication currentness/lifecycle state for one merchant-owned Opportunity.
 *
 * <p>This value owns no Opportunity content, Exposure, participation, or execution semantics.
 * It records only stable object affinity, current logical revision, lifecycle, and the exact
 * revision that is presently authoritative for publication.</p>
 */
public record OpportunityPublicationState(
        MerchantScope merchantScope,
        String opportunityIdentity,
        String currentRevisionIdentity,
        PublicationLifecycle lifecycle,
        Optional<String> publishedRevisionIdentity
) {
    public OpportunityPublicationState {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireText(opportunityIdentity, "opportunityIdentity");
        requireText(currentRevisionIdentity, "currentRevisionIdentity");
        Objects.requireNonNull(lifecycle, "lifecycle");
        Objects.requireNonNull(publishedRevisionIdentity, "publishedRevisionIdentity");
        publishedRevisionIdentity.ifPresent(
                value -> requireText(value, "publishedRevisionIdentity")
        );

        if (lifecycle == PublicationLifecycle.DRAFT && publishedRevisionIdentity.isPresent()) {
            throw new IllegalArgumentException("A DRAFT Opportunity cannot have a published revision");
        }
        if (lifecycle != PublicationLifecycle.DRAFT && publishedRevisionIdentity.isEmpty()) {
            throw new IllegalArgumentException(
                    "A PUBLISHED or WITHDRAWN Opportunity must preserve publication revision evidence"
            );
        }
    }

    public static OpportunityPublicationState draft(
            MerchantScope merchantScope,
            String opportunityIdentity,
            String currentRevisionIdentity
    ) {
        return new OpportunityPublicationState(
                merchantScope,
                opportunityIdentity,
                currentRevisionIdentity,
                PublicationLifecycle.DRAFT,
                Optional.empty()
        );
    }

    /**
     * Advances the current logical revision while preserving publication state and evidence.
     * Revising a PUBLISHED Opportunity does not implicitly publish the new revision.
     */
    public OpportunityPublicationState revise(
            String expectedCurrentRevisionIdentity,
            String newRevisionIdentity
    ) {
        requireExpectedCurrent(expectedCurrentRevisionIdentity);
        requireText(newRevisionIdentity, "newRevisionIdentity");
        if (currentRevisionIdentity.equals(newRevisionIdentity)) {
            throw new IllegalArgumentException("A material revision must have a new revision identity");
        }
        return new OpportunityPublicationState(
                merchantScope,
                opportunityIdentity,
                newRevisionIdentity,
                lifecycle,
                publishedRevisionIdentity
        );
    }

    /** Publishes the exact current revision. WITHDRAWN state requires explicit republish. */
    public OpportunityPublicationState publish(String expectedCurrentRevisionIdentity) {
        requireExpectedCurrent(expectedCurrentRevisionIdentity);
        if (lifecycle == PublicationLifecycle.WITHDRAWN) {
            throw new IllegalStateException("WITHDRAWN Opportunity requires explicit republish");
        }
        return new OpportunityPublicationState(
                merchantScope,
                opportunityIdentity,
                currentRevisionIdentity,
                PublicationLifecycle.PUBLISHED,
                Optional.of(currentRevisionIdentity)
        );
    }

    /**
     * Withdraws a currently published Opportunity only when the caller targets the exact current
     * logical revision, while retaining evidence of the last revision actually published.
     */
    public OpportunityPublicationState withdraw(String expectedCurrentRevisionIdentity) {
        requireExpectedCurrent(expectedCurrentRevisionIdentity);
        if (lifecycle != PublicationLifecycle.PUBLISHED) {
            throw new IllegalStateException("Only a PUBLISHED Opportunity can be withdrawn");
        }
        return new OpportunityPublicationState(
                merchantScope,
                opportunityIdentity,
                currentRevisionIdentity,
                PublicationLifecycle.WITHDRAWN,
                publishedRevisionIdentity
        );
    }

    /** Explicitly republishes a withdrawn Opportunity over its exact current revision. */
    public OpportunityPublicationState republish(String expectedCurrentRevisionIdentity) {
        requireExpectedCurrent(expectedCurrentRevisionIdentity);
        if (lifecycle != PublicationLifecycle.WITHDRAWN) {
            throw new IllegalStateException("Only a WITHDRAWN Opportunity can be republished");
        }
        return new OpportunityPublicationState(
                merchantScope,
                opportunityIdentity,
                currentRevisionIdentity,
                PublicationLifecycle.PUBLISHED,
                Optional.of(currentRevisionIdentity)
        );
    }

    private void requireExpectedCurrent(String expectedCurrentRevisionIdentity) {
        requireText(expectedCurrentRevisionIdentity, "expectedCurrentRevisionIdentity");
        if (!currentRevisionIdentity.equals(expectedCurrentRevisionIdentity)) {
            throw new PublicationRevisionConflictException(
                    opportunityIdentity,
                    expectedCurrentRevisionIdentity,
                    currentRevisionIdentity
            );
        }
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
