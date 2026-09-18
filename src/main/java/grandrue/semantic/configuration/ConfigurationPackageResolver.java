package grandrue.semantic.configuration;

import grandrue.fulfilment.FulfilmentBindingSetRevision;
import grandrue.fulfilment.FulfilmentContractRegistrySnapshot;
import grandrue.fulfilment.FulfilmentPlan;
import grandrue.fulfilment.FulfilmentPlanResolver;
import mainstreet.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.executable.ExecutableMerchantModel;
import mainstreet.surface.StaticSurfaceContributionCatalogue;
import mainstreet.surface.SurfaceContributionRegistrySnapshot;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Deterministically assembles one immutable Resolved Configuration Package
 * from an authoritative Merchant Configuration revision and its pinned static
 * authorities.
 *
 * <p>This resolver does not approve or activate configuration and deliberately
 * excludes live actor authority, provider health, credentials, current
 * operational state and other contextual runtime facts.</p>
 */
public final class ConfigurationPackageResolver {

    private final ConfigurationCompiler compiler;
    private final Optional<SurfaceContributionRegistrySnapshot>
            surfaceContributionRegistry;
    private final Optional<FulfilmentContractRegistrySnapshot>
            fulfilmentContractRegistry;

    public ConfigurationPackageResolver(ConfigurationCompiler compiler) {
        this(
                compiler,
                Optional.empty(),
                Optional.empty()
        );
    }

    public ConfigurationPackageResolver(
            ConfigurationCompiler compiler,
            SurfaceContributionRegistrySnapshot surfaceContributionRegistry
    ) {
        this(
                compiler,
                Optional.of(Objects.requireNonNull(
                        surfaceContributionRegistry,
                        "surfaceContributionRegistry"
                )),
                Optional.empty()
        );
    }

    public ConfigurationPackageResolver(
            ConfigurationCompiler compiler,
            FulfilmentContractRegistrySnapshot fulfilmentContractRegistry
    ) {
        this(
                compiler,
                Optional.empty(),
                Optional.of(Objects.requireNonNull(
                        fulfilmentContractRegistry,
                        "fulfilmentContractRegistry"
                ))
        );
    }

    public ConfigurationPackageResolver(
            ConfigurationCompiler compiler,
            SurfaceContributionRegistrySnapshot surfaceContributionRegistry,
            FulfilmentContractRegistrySnapshot fulfilmentContractRegistry
    ) {
        this(
                compiler,
                Optional.of(Objects.requireNonNull(
                        surfaceContributionRegistry,
                        "surfaceContributionRegistry"
                )),
                Optional.of(Objects.requireNonNull(
                        fulfilmentContractRegistry,
                        "fulfilmentContractRegistry"
                ))
        );
    }

    private ConfigurationPackageResolver(
            ConfigurationCompiler compiler,
            Optional<SurfaceContributionRegistrySnapshot>
                    surfaceContributionRegistry,
            Optional<FulfilmentContractRegistrySnapshot>
                    fulfilmentContractRegistry
    ) {
        this.compiler = Objects.requireNonNull(compiler, "compiler");
        this.surfaceContributionRegistry = Objects.requireNonNull(
                surfaceContributionRegistry,
                "surfaceContributionRegistry"
        );
        this.fulfilmentContractRegistry = Objects.requireNonNull(
                fulfilmentContractRegistry,
                "fulfilmentContractRegistry"
        );
    }

    public ResolvedConfigurationPackage resolve(
            MerchantConfiguration configuration,
            String compilerIdentifier,
            Instant generatedAt
    ) {
        Objects.requireNonNull(configuration, "configuration");
        if (configuration.fulfilmentBindingSetRevisionReference().isPresent()) {
            throw new IllegalArgumentException(
                    "Configuration pins a fulfilment binding-set revision that must be supplied for exact resolution"
            );
        }
        return resolveInternal(
                configuration,
                Optional.empty(),
                compilerIdentifier,
                generatedAt
        );
    }

    public ResolvedConfigurationPackage resolve(
            MerchantConfiguration configuration,
            FulfilmentBindingSetRevision bindingSetRevision,
            String compilerIdentifier,
            Instant generatedAt
    ) {
        Objects.requireNonNull(bindingSetRevision, "bindingSetRevision");
        return resolveInternal(
                Objects.requireNonNull(configuration, "configuration"),
                Optional.of(bindingSetRevision),
                compilerIdentifier,
                generatedAt
        );
    }

    private ResolvedConfigurationPackage resolveInternal(
            MerchantConfiguration configuration,
            Optional<FulfilmentBindingSetRevision> bindingSetRevision,
            String compilerIdentifier,
            Instant generatedAt
    ) {
        ExecutableMerchantModel executableModel = compiler.compile(configuration);
        StaticSurfaceContributionCatalogue staticSurfaceCatalogue =
                surfaceContributionRegistry
                        .map(registry -> registry.resolveFor(executableModel))
                        .orElseGet(StaticSurfaceContributionCatalogue::empty);

        validateSurfaceFulfilmentRoleDependencies(
                executableModel,
                staticSurfaceCatalogue
        );

        FulfilmentPlan fulfilmentPlan;
        if (bindingSetRevision.isPresent()) {
            FulfilmentContractRegistrySnapshot registry =
                    fulfilmentContractRegistry.orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Fulfilment contract registry is required for binding-set resolution"
                            ));
            fulfilmentPlan = new FulfilmentPlanResolver(registry).resolve(
                    configuration,
                    executableModel,
                    bindingSetRevision.orElseThrow()
            );
        } else {
            fulfilmentPlan = FulfilmentPlan.empty();
        }

        return ResolvedConfigurationPackage.currentFoundation(
                configuration,
                executableModel,
                fulfilmentPlan,
                staticSurfaceCatalogue,
                new ResolvedConfigurationProvenance(
                        compilerIdentifier,
                        generatedAt
                )
        );
    }

    private void validateSurfaceFulfilmentRoleDependencies(
            ExecutableMerchantModel executableModel,
            StaticSurfaceContributionCatalogue staticSurfaceCatalogue
    ) {
        var requiredRoles = staticSurfaceCatalogue.contributions().stream()
                .flatMap(contribution -> contribution
                        .interactionAvailabilityContract()
                        .requiredFulfilmentRole()
                        .stream())
                .distinct()
                .toList();

        if (requiredRoles.isEmpty()) {
            return;
        }

        FulfilmentContractRegistrySnapshot registry =
                fulfilmentContractRegistry.orElseThrow(() ->
                        new IllegalArgumentException(
                                "Fulfilment contract registry is required to validate surface fulfilment-role dependencies"
                        ));

        if (!registry.semanticRegistryReleaseIdentifier().equals(
                executableModel.semanticRegistryVersion()
        )) {
            throw new IllegalArgumentException(
                    "Fulfilment contract registry release does not match executable model"
            );
        }

        for (var roleIdentity : requiredRoles) {
            if (registry.role(roleIdentity).isEmpty()) {
                throw new IllegalArgumentException(
                        "Surface contribution references an unregistered fulfilment role: "
                                + roleIdentity
                );
            }
        }
    }
}
