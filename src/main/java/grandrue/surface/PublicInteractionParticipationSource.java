package grandrue.surface;

import java.util.Set;

/**
 * Capability-owned boundary for establishing positive subject-interaction
 * participation under one exact resolved merchant context.
 */
@FunctionalInterface
public interface PublicInteractionParticipationSource {

    Set<PublicInteractionParticipationFact> currentParticipation(
            PublicInteractionParticipationRequest request
    );
}
