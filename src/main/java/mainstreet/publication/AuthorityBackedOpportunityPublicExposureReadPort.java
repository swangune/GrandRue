package mainstreet.publication;

import mainstreet.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/**
 * Publication-owned adapter from authoritative Opportunity state/material to the narrow P4 read
 * contract.
 *
 * <p>The adapter resolves temporal evidence from the exact published revision, not necessarily the
 * current revision. A concurrent change that removes or changes the published binding causes the
 * read to fail closed.</p>
 */
public final class AuthorityBackedOpportunityPublicExposureReadPort
        implements OpportunityPublicExposureReadPort {

    private final OpportunityPublicationStateAuthority stateAuthority;

    public AuthorityBackedOpportunityPublicExposureReadPort(
            OpportunityPublicationStateAuthority stateAuthority
    ) {
        this.stateAuthority = Objects.requireNonNull(stateAuthority, "stateAuthority");
    }

    @Override
    public Optional<OpportunityPublicExposureEvidence> currentPublishedExposure(
            MerchantScope merchantScope,
            String opportunityIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireText(opportunityIdentity, "opportunityIdentity");

        Optional<OpportunityPublicationState> initialState = stateAuthority.current(
                merchantScope,
                opportunityIdentity
        ).filter(state -> state.lifecycle() == PublicationLifecycle.PUBLISHED);

        if (initialState.isEmpty()) {
            return Optional.empty();
        }

        String publishedRevisionIdentity = initialState.get()
                .publishedRevisionIdentity()
                .orElseThrow(() -> new IllegalStateException(
                        "PUBLISHED Opportunity is missing published revision evidence"
                ));

        return stateAuthority.revision(
                        merchantScope,
                        opportunityIdentity,
                        publishedRevisionIdentity
                )
                .map(revision -> requireExactAffinity(
                        merchantScope,
                        opportunityIdentity,
                        publishedRevisionIdentity,
                        revision
                ))
                .filter(ignored -> stillPublishedAt(
                        merchantScope,
                        opportunityIdentity,
                        publishedRevisionIdentity
                ))
                .map(revision -> new OpportunityPublicExposureEvidence(
                        merchantScope,
                        opportunityIdentity,
                        publishedRevisionIdentity,
                        revision.publishFrom(),
                        revision.publishUntil()
                ));
    }

    private boolean stillPublishedAt(
            MerchantScope merchantScope,
            String opportunityIdentity,
            String publishedRevisionIdentity
    ) {
        return stateAuthority.current(merchantScope, opportunityIdentity)
                .filter(state -> state.lifecycle() == PublicationLifecycle.PUBLISHED)
                .flatMap(OpportunityPublicationState::publishedRevisionIdentity)
                .filter(publishedRevisionIdentity::equals)
                .isPresent();
    }

    private static OpportunityPublicationMaterialRevision requireExactAffinity(
            MerchantScope merchantScope,
            String opportunityIdentity,
            String publishedRevisionIdentity,
            OpportunityPublicationMaterialRevision revision
    ) {
        Objects.requireNonNull(revision, "revision");
        if (!merchantScope.equals(revision.merchantScope())
                || !opportunityIdentity.equals(revision.opportunityIdentity())
                || !publishedRevisionIdentity.equals(revision.revisionIdentity())) {
            throw new IllegalStateException(
                    "Publication authority returned material with mismatched published affinity"
            );
        }
        return revision;
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
