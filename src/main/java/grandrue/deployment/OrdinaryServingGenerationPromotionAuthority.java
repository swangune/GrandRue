package grandrue.deployment;

import java.util.Optional;

public interface OrdinaryServingGenerationPromotionAuthority {
    OrdinaryServingAdmissionControl initialize(InitializeOrdinaryServingAdmissionControlCommand command);
    Optional<OrdinaryServingAdmissionControl> control();
    ServingGenerationPromotionTransition prepare(PrepareServingGenerationPromotionCommand command);
    ServingGenerationPromotionTransition reconcile(ReconcileServingGenerationPromotionCommand command);
    Optional<ServingGenerationPromotionTransition> transition(String transitionIdentifier);
}
