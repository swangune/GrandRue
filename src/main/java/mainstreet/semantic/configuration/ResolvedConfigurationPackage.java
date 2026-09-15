package mainstreet.semantic.configuration;

import mainstreet.fulfilment.FulfilmentPlan;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.surface.StaticSurfaceContributionCatalogue;

import java.util.Objects;

/**
 * Immutable derived configuration package governed by MS-PROT-022 v1.5 and
 * the accepted MS-PROT-048 fulfilment amendment chain.
 * Live actor authority, provider health, current operational state and other
 * contextual facts are deliberately excluded.
 */
public record ResolvedConfigurationPackage(
        String merchantIdentifier,
        String sourceConfigurationRevisionIdentifier,
        String semanticRegistryReleaseIdentifier,
        ExecutableMerchantModel executableSemanticModel,
        ResolvedCapabilityConfiguration resolvedConfiguration,
        FulfilmentPlan fulfilmentPlan,
        StaticSurfaceContributionCatalogue staticSurfaceContributionCatalogue,
        ResolvedConfigurationProvenance provenance
) {
    public ResolvedConfigurationPackage {
        requireIdentifier(merchantIdentifier, "Merchant identifier");
        requireIdentifier(
                sourceConfigurationRevisionIdentifier,
                "Source configuration revision identifier"
        );
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        Objects.requireNonNull(
                executableSemanticModel,
                "executableSemanticModel"
        );
        Objects.requireNonNull(resolvedConfiguration, "resolvedConfiguration");
        Objects.requireNonNull(fulfilmentPlan, "fulfilmentPlan");
        Objects.requireNonNull(
                staticSurfaceContributionCatalogue,
                "staticSurfaceContributionCatalogue"
        );
        Objects.requireNonNull(provenance, "provenance");

        requireExecutableModelAffinity(
                merchantIdentifier,
                sourceConfigurationRevisionIdentifier,
                semanticRegistryReleaseIdentifier,
                executableSemanticModel
        );
    }

    public static ResolvedConfigurationPackage currentFoundation(
            MerchantConfiguration configuration,
            ExecutableMerchantModel executableSemanticModel,
            ResolvedConfigurationProvenance provenance
    ) {
        return currentFoundation(
                configuration,
                executableSemanticModel,
                FulfilmentPlan.empty(),
                StaticSurfaceContributionCatalogue.empty(),
                provenance
        );
    }

    public static ResolvedConfigurationPackage currentFoundation(
            MerchantConfiguration configuration,
            ExecutableMerchantModel executableSemanticModel,
            StaticSurfaceContributionCatalogue staticSurfaceContributionCatalogue,
            ResolvedConfigurationProvenance provenance
    ) {
        return currentFoundation(
                configuration,
                executableSemanticModel,
                FulfilmentPlan.empty(),
                staticSurfaceContributionCatalogue,
                provenance
        );
    }

    public static ResolvedConfigurationPackage currentFoundation(
            MerchantConfiguration configuration,
            ExecutableMerchantModel executableSemanticModel,
            FulfilmentPlan fulfilmentPlan,
            StaticSurfaceContributionCatalogue staticSurfaceContributionCatalogue,
            ResolvedConfigurationProvenance provenance
    ) {
        Objects.requireNonNull(configuration, "configuration");
        Objects.requireNonNull(executableSemanticModel, "executableSemanticModel");
        Objects.requireNonNull(fulfilmentPlan, "fulfilmentPlan");
        Objects.requireNonNull(
                staticSurfaceContributionCatalogue,
                "staticSurfaceContributionCatalogue"
        );

        requireConfigurationModelAffinity(configuration, executableSemanticModel);
        requireFulfilmentAffinity(configuration, fulfilmentPlan);

        return new ResolvedConfigurationPackage(
                configuration.merchantIdentifier(),
                configuration.configurationIdentifier(),
                configuration.semanticRegistryVersion(),
                executableSemanticModel,
                new ResolvedCapabilityConfiguration(
                        executableSemanticModel.policies()
                ),
                fulfilmentPlan,
                staticSurfaceContributionCatalogue,
                provenance
        );
    }

    public void requireSourceConfiguration(MerchantConfiguration configuration) {
        Objects.requireNonNull(configuration, "configuration");
        if (!merchantIdentifier.equals(configuration.merchantIdentifier())
                || !sourceConfigurationRevisionIdentifier.equals(
                        configuration.configurationIdentifier()
                )
                || !semanticRegistryReleaseIdentifier.equals(
                        configuration.semanticRegistryVersion()
                )) {
            throw new IllegalArgumentException(
                    "Resolved package provenance does not match configuration"
            );
        }
        if (!executableSemanticModel.capabilityIdentifiers().containsAll(
                configuration.capabilityIdentifiers()
        )) {
            throw new IllegalArgumentException(
                    "Resolved package omits a selected capability"
            );
        }
        requireFulfilmentAffinity(configuration, fulfilmentPlan);
    }

    private static void requireFulfilmentAffinity(
            MerchantConfiguration configuration,
            FulfilmentPlan fulfilmentPlan
    ) {
        if (configuration.fulfilmentBindingSetRevisionReference().isPresent()) {
            if (!configuration.fulfilmentBindingSetRevisionReference().equals(
                    fulfilmentPlan.sourceBindingSetRevision()
            )) {
                throw new IllegalArgumentException(
                        "Resolved fulfilment plan does not match configuration binding-set revision"
                );
            }
        } else if (fulfilmentPlan.sourceBindingSetRevision().isPresent()) {
            throw new IllegalArgumentException(
                    "Resolved fulfilment plan has binding-set provenance absent from configuration"
            );
        }
    }

    private static void requireConfigurationModelAffinity(
            MerchantConfiguration configuration,
            ExecutableMerchantModel model
    ) {
        if (!configuration.merchantIdentifier().equals(model.merchantIdentifier())
                || !configuration.configurationIdentifier().equals(
                        model.modelIdentifier()
                )
                || configuration.version() != model.version()
                || !configuration.semanticRegistryVersion().equals(
                        model.semanticRegistryVersion()
                )) {
            throw new IllegalArgumentException(
                    "Executable model provenance does not match configuration"
            );
        }
        if (!model.capabilityIdentifiers().containsAll(
                configuration.capabilityIdentifiers()
        )) {
            throw new IllegalArgumentException(
                    "Executable model omits a selected capability"
            );
        }
    }

    private static void requireExecutableModelAffinity(
            String merchantIdentifier,
            String sourceConfigurationRevisionIdentifier,
            String semanticRegistryReleaseIdentifier,
            ExecutableMerchantModel model
    ) {
        if (!merchantIdentifier.equals(model.merchantIdentifier())
                || !sourceConfigurationRevisionIdentifier.equals(
                        model.modelIdentifier()
                )
                || !semanticRegistryReleaseIdentifier.equals(
                        model.semanticRegistryVersion()
                )) {
            throw new IllegalArgumentException(
                    "Executable model provenance does not match resolved package"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
