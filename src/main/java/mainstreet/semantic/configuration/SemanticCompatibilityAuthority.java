package mainstreet.semantic.configuration;

import java.util.Optional;

/**
 * Trusted authority for directional compatibility or migration evidence
 * between two exact configuration releases.
 */
@FunctionalInterface
public interface SemanticCompatibilityAuthority {

    Optional<SemanticCompatibilityEvidence> evidenceFor(
            String merchantIdentifier,
            String sourceReleaseIdentifier,
            String targetReleaseIdentifier
    );
}
