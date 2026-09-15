package mainstreet.semantic.release;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Startup composition for deployment-packaged semantic definition evidence.
 *
 * <p>The returned repository becomes available only after all assigned bundles
 * have materialised and every exact release required by the serving cohort has
 * been proven present. This class does not establish executable support.</p>
 */
public final class SemanticReleaseBootstrap {

    private final ClasspathSemanticDefinitionBundleLoader loader;
    private final SemanticReleaseMaterialiser materialiser;

    public SemanticReleaseBootstrap(
            ClasspathSemanticDefinitionBundleLoader loader,
            SemanticReleaseMaterialiser materialiser
    ) {
        this.loader = Objects.requireNonNull(loader, "loader");
        this.materialiser = Objects.requireNonNull(
                materialiser,
                "materialiser"
        );
    }

    public SemanticReleaseAssemblyRepository bootstrap(
            Collection<String> assignedBundleResources,
            Collection<String> requiredReleaseIdentifiers
    ) {
        Objects.requireNonNull(
                assignedBundleResources,
                "assignedBundleResources"
        );
        Objects.requireNonNull(
                requiredReleaseIdentifiers,
                "requiredReleaseIdentifiers"
        );

        DeploymentSemanticMaterialisationSet deploymentSet = loader.load(
                assignedBundleResources
        );
        SemanticReleaseAssemblyRepository repository = materialiser.materialise(
                deploymentSet
        );

        Set<String> required = new HashSet<>();
        for (String releaseIdentifier : requiredReleaseIdentifiers) {
            if (releaseIdentifier == null || releaseIdentifier.isBlank()) {
                throw new IllegalArgumentException(
                        "Required semantic release identifier must not be blank"
                );
            }
            required.add(releaseIdentifier);
        }

        for (String releaseIdentifier : required) {
            if (repository.release(releaseIdentifier).isEmpty()) {
                throw new SemanticMaterialisationException(
                        SemanticMaterialisationFailure.REQUIRED_RELEASE_NOT_MATERIALISED,
                        "Required exact semantic release is not materialised: "
                                + releaseIdentifier
                );
            }
        }

        return repository;
    }
}
