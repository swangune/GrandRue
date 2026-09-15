package mainstreet.onboarding;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Current registered initial onboarding discovery catalogue.
 */
public final class InitialOnboardingPromptCatalogue {

    private static final String SEMANTIC_NAMESPACE = "mainstreet.semantic";

    private static final OnboardingPromptDefinition
            INITIAL_CUSTOMER_INTERACTION =
            new OnboardingPromptDefinition(
                    new OnboardingPromptKey(
                            InitialCustomerInteractionDiscoveryQuestion
                                    .identity(),
                            Optional.empty()
                    ),
                    InitialCustomerInteractionDiscoveryQuestion
                            .definitionVersion(),
                    InitialCustomerInteractionDiscoveryQuestion.promptClass(),
                    InitialCustomerInteractionDiscoveryQuestion.answerForm(),
                    InitialCustomerInteractionDiscoveryQuestion.priority(),
                    Map.of(
                            InitialCustomerInteractionDiscoveryQuestion
                                    .definitionVersion(),
                            Arrays.stream(
                                            InitialCustomerInteractionDiscoveryOption
                                                    .values()
                                    )
                                    .map(Enum::name)
                                    .collect(java.util.stream.Collectors.toSet())
                    ),
                    OnboardingPromptApplicability.always(),
                    InitialOnboardingPromptCatalogue
                            ::isConsistentInitialSelection
            );

    private static final List<OnboardingDiscoveryMapping> MAPPINGS = List.of(
            mapping("PUBLISH_INFORMATION", "publication"),
            mapping("SEND_ENQUIRY", "enquiry"),
            mapping("ARRANGE_APPOINTMENT", "scheduling"),
            mapping("RESERVE_SUBJECT", "booking"),
            mapping("PLACE_ORDER", "ordering"),
            mapping("SUBSCRIBE_UPDATES", "subscription"),
            new OnboardingDiscoveryMapping(
                    InitialCustomerInteractionDiscoveryQuestion.identity(),
                    InitialCustomerInteractionDiscoveryQuestion
                            .definitionVersion(),
                    "NOTHING_ELSE_FOR_NOW",
                    Set.of()
            )
    );

    private static final OnboardingPromptRegistry REGISTRY =
            new OnboardingPromptRegistry(
                    List.of(INITIAL_CUSTOMER_INTERACTION),
                    MAPPINGS
            );

    private InitialOnboardingPromptCatalogue() {
    }

    public static OnboardingPromptDefinition
            initialCustomerInteractionDefinition() {
        return INITIAL_CUSTOMER_INTERACTION;
    }

    public static OnboardingPromptRegistry registry() {
        return REGISTRY;
    }

    private static boolean isConsistentInitialSelection(
            Set<String> selections
    ) {
        try {
            Set<InitialCustomerInteractionDiscoveryOption> options =
                    selections.stream()
                            .map(InitialCustomerInteractionDiscoveryOption
                                    ::valueOf)
                            .collect(java.util.stream.Collectors.toSet());
            return new InitialCustomerInteractionDiscoverySelectionConsistency()
                    .isConsistent(options);
        } catch (IllegalArgumentException unknownOption) {
            return false;
        }
    }

    private static OnboardingDiscoveryMapping mapping(
            String option,
            String semanticIdentifier
    ) {
        return new OnboardingDiscoveryMapping(
                InitialCustomerInteractionDiscoveryQuestion.identity(),
                InitialCustomerInteractionDiscoveryQuestion
                        .definitionVersion(),
                option,
                Set.of(new OnboardingSemanticSeed(
                        SEMANTIC_NAMESPACE,
                        semanticIdentifier
                ))
        );
    }
}
