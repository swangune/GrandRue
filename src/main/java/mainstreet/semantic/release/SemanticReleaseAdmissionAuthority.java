package mainstreet.semantic.release;

import java.util.Optional;

public interface SemanticReleaseAdmissionAuthority {
    SemanticReleasePurposeAdmissionDecision recordDecision(
            RecordSemanticReleasePurposeAdmissionDecisionCommand command
    );

    Optional<SemanticReleasePurposeAdmissionDecision> currentDecision(
            String semanticRegistryReleaseIdentifier,
            SemanticReleasePurpose purpose
    );

    OrdinaryNewConfigurationSemanticReleaseReference advanceOrdinaryReference(
            AdvanceOrdinarySemanticReleaseCommand command
    );

    Optional<OrdinaryNewConfigurationSemanticReleaseReference> currentOrdinaryReference();
}
