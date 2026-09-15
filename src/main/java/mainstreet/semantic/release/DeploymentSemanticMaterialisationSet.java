package mainstreet.semantic.release;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.HashSet;

/**
 * Immutable exact set of packaged semantic releases assigned to one deployment
 * for startup materialisation.
 */
public final class DeploymentSemanticMaterialisationSet {

    private final List<PackagedSemanticDefinitionBundle> bundles;
    private final Map<String, String> contentIdentity;

    public DeploymentSemanticMaterialisationSet(
            Collection<PackagedSemanticDefinitionBundle> bundles
    ) {
        Objects.requireNonNull(bundles, "bundles");
        List<PackagedSemanticDefinitionBundle> copy = List.copyOf(bundles);
        Set<String> releases = new HashSet<>();
        TreeMap<String, String> digestsByRelease = new TreeMap<>();
        for (PackagedSemanticDefinitionBundle bundle : copy) {
            Objects.requireNonNull(bundle, "semantic definition bundle");
            if (!releases.add(bundle.releaseIdentifier())) {
                throw new SemanticMaterialisationException(
                        SemanticMaterialisationFailure.DUPLICATE_RELEASE_IDENTITY,
                        "Duplicate semantic release in deployment materialisation set: "
                                + bundle.releaseIdentifier()
                );
            }
            digestsByRelease.put(
                    bundle.releaseIdentifier(),
                    bundle.contentDigest()
            );
        }
        this.bundles = copy;
        this.contentIdentity = Collections.unmodifiableMap(digestsByRelease);
    }

    public List<PackagedSemanticDefinitionBundle> bundles() {
        return bundles;
    }

    /**
     * Returns the deterministic serving-cohort content identity required by
     * ADR-013: exact release identifier to exact immutable bundle digest.
     */
    public Map<String, String> contentIdentity() {
        return contentIdentity;
    }
}
