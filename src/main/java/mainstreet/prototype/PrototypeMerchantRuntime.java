package mainstreet.prototype;

import mainstreet.semantic.configuration.ConfigurationActivationRequest;
import mainstreet.semantic.configuration.ConfigurationActivationStatus;
import mainstreet.semantic.configuration.ConfigurationRelease;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.configuration.ConfigurationRevisionApproval;
import mainstreet.semantic.configuration.InMemoryConfigurationPublication;
import mainstreet.semantic.configuration.InMemoryConfigurationReleaseActivation;
import mainstreet.semantic.configuration.MerchantConfiguration;
import grandrue.semantic.executable.ExecutableAllocationClaimEffect;
import grandrue.semantic.executable.ExecutableMerchantModel;
import grandrue.semantic.executable.ExecutableObjectCreationEffect;
import grandrue.semantic.executable.ExecutableOperationalObjectDefinition;
import grandrue.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import grandrue.semantic.executable.ExecutableOperationDefinition;
import grandrue.semantic.executable.ExecutableOperationEffect;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Deterministic executable fixture for the pre-UI backend prototype.
 * Merchant examples differ only by selected capability composition; no
 * business-category switch participates in runtime behaviour.
 */
public final class PrototypeMerchantRuntime {

    public static final String PROTOTYPE_PRINCIPAL = "prototype-operator";
    public static final String SEMANTIC_REGISTRY = "prototype-semantic-1";
    private static final Instant SEEDED_AT =
            Instant.parse("2026-08-26T02:00:00Z");

    private final InMemoryConfigurationPublication publication;
    private final InMemoryConfigurationReleaseActivation activation;
    private final Map<String, ConfigurationRevisionApproval> approvals;
    private final Clock clock;
    private final PrototypeDiscoverySeedResolver discoverySeedResolver;

    private PrototypeMerchantRuntime(
            InMemoryConfigurationPublication publication,
            InMemoryConfigurationReleaseActivation activation,
            Map<String, ConfigurationRevisionApproval> approvals,
            Clock clock,
            PrototypeDiscoverySeedResolver discoverySeedResolver
    ) {
        this.publication = publication;
        this.activation = activation;
        this.approvals = approvals;
        this.clock = clock;
        this.discoverySeedResolver = discoverySeedResolver;
    }

    public static PrototypeMerchantRuntime standard() {
        InMemoryConfigurationPublication publication =
                new InMemoryConfigurationPublication();
        Map<String, ConfigurationRevisionApproval> approvals = new HashMap<>();
        Clock clock = Clock.fixed(SEEDED_AT, ZoneOffset.UTC);

        InMemoryConfigurationReleaseActivation activation =
                new InMemoryConfigurationReleaseActivation(
                        publication,
                        (merchantIdentifier, configurationIdentifier) ->
                                Optional.ofNullable(approvals.get(
                                        approvalKey(
                                                merchantIdentifier,
                                                configurationIdentifier
                                        )
                                )),
                        (principal, merchantIdentifier, configurationIdentifier) ->
                                PROTOTYPE_PRINCIPAL.equals(principal),
                        clock
                );

        PrototypeMerchantRuntime runtime = new PrototypeMerchantRuntime(
                publication,
                activation,
                approvals,
                clock,
                PrototypeDiscoverySeedResolver.standard()
        );

        runtime.seed(
                retailerModel(),
                "prototype-retailer-release-1"
        );
        runtime.seed(
                PrototypeExecutableModelFactory.compose(
                        "prototype-consultant",
                        "prototype-consultant-config-1",
                        1,
                        Set.of("appointment", "scheduling", "customer", "payment")
                ),
                "prototype-consultant-release-1"
        );
        runtime.seed(
                PrototypeExecutableModelFactory.compose(
                        "prototype-motel",
                        "prototype-motel-config-1",
                        1,
                        Set.of("booking", "customer", "payment")
                ),
                "prototype-motel-release-1"
        );
        runtime.seed(
                PrototypeExecutableModelFactory.compose(
                        "prototype-daycare",
                        "prototype-daycare-config-1",
                        1,
                        Set.of("booking", "customer", "payment")
                ),
                "prototype-daycare-release-1"
        );
        runtime.seed(
                PrototypeExecutableModelFactory.compose(
                        "prototype-gardener",
                        "prototype-gardener-config-1",
                        1,
                        Set.of("appointment", "scheduling", "customer", "payment")
                ),
                "prototype-gardener-release-1"
        );

        runtime.seedDiscoveryChoices(
                "prototype-gardener-showcase",
                "prototype-gardener-showcase-config-1",
                1,
                "prototype-gardener-showcase-release-1",
                Set.of(
                        PrototypeCustomerInteractionChoice.PUBLISH_INFORMATION,
                        PrototypeCustomerInteractionChoice.SEND_ENQUIRY
                )
        );
        runtime.seedDiscoveryChoices(
                "prototype-gardener-bookable",
                "prototype-gardener-bookable-config-1",
                1,
                "prototype-gardener-bookable-release-1",
                Set.of(
                        PrototypeCustomerInteractionChoice.PUBLISH_INFORMATION,
                        PrototypeCustomerInteractionChoice.SEND_ENQUIRY,
                        PrototypeCustomerInteractionChoice.ARRANGE_APPOINTMENT
                )
        );
        runtime.seedDiscoveryChoices(
                "prototype-gardener-evolving",
                "prototype-gardener-evolving-config-1",
                1,
                "prototype-gardener-evolving-release-1",
                Set.of(
                        PrototypeCustomerInteractionChoice.PUBLISH_INFORMATION,
                        PrototypeCustomerInteractionChoice.SEND_ENQUIRY
                )
        );
        runtime.seedDiscoveryChoices(
                "prototype-publisher",
                "prototype-publisher-config-1",
                1,
                "prototype-publisher-release-1",
                Set.of(
                        PrototypeCustomerInteractionChoice.PUBLISH_INFORMATION,
                        PrototypeCustomerInteractionChoice.SEND_ENQUIRY
                )
        );

        return runtime;
    }

    public ConfigurationReleaseActivation activation() {
        return activation;
    }

    public PrototypeMerchantView merchant(String merchantIdentifier) {
        var active = activation.current(merchantIdentifier)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown prototype merchant: " + merchantIdentifier
                ));
        ConfigurationRelease release = active.release();
        ExecutableMerchantModel model = release.executableModel();
        return new PrototypeMerchantView(
                merchantIdentifier,
                release.configurationIdentifier(),
                release.releaseIdentifier(),
                model.capabilityIdentifiers(),
                model.operations().stream()
                        .map(ExecutableOperationDefinition::identifier)
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

    /**
     * Prototype simulation of a reviewed and approved merchant configuration
     * change derived from accepted onboarding discovery choices.
     */
    public PrototypeMerchantView activateApprovedDiscoveryChoices(
            String merchantIdentifier,
            String configurationIdentifier,
            long version,
            String releaseIdentifier,
            Set<PrototypeCustomerInteractionChoice> choices
    ) {
        ConfigurationRelease current = activation.current(merchantIdentifier)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cannot evolve an unknown prototype merchant: "
                                + merchantIdentifier
                ))
                .release();
        if (version <= current.version()) {
            throw new IllegalArgumentException(
                    "Evolved configuration version must advance the current version"
            );
        }

        ExecutableMerchantModel model = discoveryModel(
                merchantIdentifier,
                configurationIdentifier,
                version,
                choices
        );
        publishAndActivate(
                model,
                releaseIdentifier,
                Optional.of(current.configurationIdentifier())
        );
        return merchant(merchantIdentifier);
    }

    private void seedDiscoveryChoices(
            String merchantIdentifier,
            String configurationIdentifier,
            long version,
            String releaseIdentifier,
            Set<PrototypeCustomerInteractionChoice> choices
    ) {
        seed(
                discoveryModel(
                        merchantIdentifier,
                        configurationIdentifier,
                        version,
                        choices
                ),
                releaseIdentifier
        );
    }

    private ExecutableMerchantModel discoveryModel(
            String merchantIdentifier,
            String configurationIdentifier,
            long version,
            Set<PrototypeCustomerInteractionChoice> choices
    ) {
        Set<String> candidateSeeds = discoverySeedResolver.resolve(choices);
        return PrototypeExecutableModelFactory.compose(
                merchantIdentifier,
                configurationIdentifier,
                version,
                candidateSeeds
        );
    }

    private void seed(
            ExecutableMerchantModel model,
            String releaseIdentifier
    ) {
        publishAndActivate(model, releaseIdentifier, Optional.empty());
    }

    private void publishAndActivate(
            ExecutableMerchantModel model,
            String releaseIdentifier,
            Optional<String> baseConfigurationIdentifier
    ) {
        MerchantConfiguration configuration = new MerchantConfiguration(
                model.merchantIdentifier(),
                model.modelIdentifier(),
                model.version(),
                SEMANTIC_REGISTRY,
                model.capabilityIdentifiers(),
                Set.of(),
                baseConfigurationIdentifier
        );
        ConfigurationRelease release = new ConfigurationRelease(
                releaseIdentifier,
                configuration,
                model,
                "prototype-bootstrap",
                clock.instant()
        );
        publication.publish(release);
        approvals.put(
                approvalKey(
                        model.merchantIdentifier(),
                        model.modelIdentifier()
                ),
                new ConfigurationRevisionApproval(
                        model.merchantIdentifier(),
                        model.modelIdentifier(),
                        PROTOTYPE_PRINCIPAL,
                        clock.instant()
                )
        );
        var result = activation.activate(new ConfigurationActivationRequest(
                releaseIdentifier + ":activate",
                releaseIdentifier,
                baseConfigurationIdentifier,
                PROTOTYPE_PRINCIPAL
        ));
        if (result.status() != ConfigurationActivationStatus.SUCCESS) {
            throw new IllegalStateException(
                    "Could not activate prototype merchant "
                            + model.merchantIdentifier()
                            + ": " + result.status()
            );
        }
    }

    private static ExecutableMerchantModel retailerModel() {
        Set<String> capabilities = Set.of(
                "ordering", "inventory", "payment", "order-fulfilment"
        );
        ExecutableOperationalObjectTypeIdentity orderType =
                new ExecutableOperationalObjectTypeIdentity("ordering", "order");
        List<ExecutableOperationEffect> effects = List.of(
                new ExecutableObjectCreationEffect(orderType, Optional.empty()),
                new ExecutableAllocationClaimEffect("inventory.stock")
        );
        return new ExecutableMerchantModel(
                "prototype-retailer",
                "prototype-retailer-config-1",
                1,
                SEMANTIC_REGISTRY,
                capabilities,
                List.of(new ExecutableOperationalObjectDefinition(
                        "ordering", "order", Set.of(), Optional.empty(), Optional.empty()
                )),
                List.of(new ExecutableOperationDefinition(
                        "ordering.commit",
                        effects,
                        Set.of("order.committed"),
                        "ordering.commit"
                )),
                List.of()
        );
    }

    private static String approvalKey(
            String merchantIdentifier,
            String configurationIdentifier
    ) {
        return merchantIdentifier + "|" + configurationIdentifier;
    }
}
