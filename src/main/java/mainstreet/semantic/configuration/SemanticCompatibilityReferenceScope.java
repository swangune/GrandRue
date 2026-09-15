package mainstreet.semantic.configuration;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Derives the semantic roots actually relied upon by one active release.
 * Capability roots include compiler-resolved dependencies from the executable
 * model; explicitly selected policies are retained as narrower semantic roots.
 * Compatibility evaluation may traverse the semantic graph from these roots,
 * but unrelated registry semantics are outside this release's scope.
 */
public final class SemanticCompatibilityReferenceScope {

    private SemanticCompatibilityReferenceScope() {
    }

    public static Set<String> forSource(ConfigurationRelease source) {
        Objects.requireNonNull(source, "source");
        Set<String> references = new HashSet<>();
        source.executableModel().capabilityIdentifiers().forEach(
                identifier -> references.add("capability:" + identifier)
        );
        source.configuration().policySelections().forEach(
                selection -> references.add(
                        "policy:"
                                + selection.ownerCapabilityIdentifier()
                                + "/"
                                + selection.policyIdentifier()
                )
        );
        return Set.copyOf(references);
    }
}
