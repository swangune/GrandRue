package grandrue.semantic.release;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Loads only the explicitly assigned classpath semantic bundle resources for a
 * deployment. No resource scanning or latest-release selection is performed.
 */
public final class ClasspathSemanticDefinitionBundleLoader {

    private final ClassLoader classLoader;
    private final PackagedSemanticDefinitionBundleTextCodec codec;

    public ClasspathSemanticDefinitionBundleLoader(
            ClassLoader classLoader,
            PackagedSemanticDefinitionBundleTextCodec codec
    ) {
        this.classLoader = Objects.requireNonNull(classLoader, "classLoader");
        this.codec = Objects.requireNonNull(codec, "codec");
    }

    public DeploymentSemanticMaterialisationSet load(
            Collection<String> resourcePaths
    ) {
        Objects.requireNonNull(resourcePaths, "resourcePaths");
        List<PackagedSemanticDefinitionBundle> bundles = new ArrayList<>();
        for (String resourcePath : resourcePaths) {
            if (resourcePath == null || resourcePath.isBlank()) {
                throw new IllegalArgumentException(
                        "Semantic bundle resource path must not be blank"
                );
            }
            try (InputStream input = classLoader.getResourceAsStream(resourcePath)) {
                if (input == null) {
                    throw new IllegalArgumentException(
                            "Assigned semantic bundle resource not found: "
                                    + resourcePath
                    );
                }
                bundles.add(codec.decode(input.readAllBytes()));
            } catch (IOException exception) {
                throw new IllegalStateException(
                        "Unable to read assigned semantic bundle resource: "
                                + resourcePath,
                        exception
                );
            }
        }
        return new DeploymentSemanticMaterialisationSet(bundles);
    }
}
