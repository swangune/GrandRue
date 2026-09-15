package mainstreet.merchantprofile;

import mainstreet.surface.ProjectionSourceDependencyReference;
import mainstreet.surface.ProjectionSourceEvidence;

import java.util.Objects;
import java.util.Optional;

/**
 * Profile-owned coherent BR3 observation. It binds typed descriptor material
 * to the exact P2 source evidence established from the same owner read.
 *
 * <p>Construction is intentionally package-owned: callers outside Profile may
 * consume the observation but cannot manufacture a material/evidence pairing.
 * This preserves owner affinity without introducing a shared binding token or
 * making generic Surface infrastructure aware of Merchant Profile identity.</p>
 */
public final class MerchantPublicDescriptorProjectionObservation {

    private static final ProjectionSourceDependencyReference DESCRIPTOR_SOURCE =
            new ProjectionSourceDependencyReference(
                    "profile",
                    "merchant-public-descriptor"
            );

    private final Optional<MerchantPublicDescriptorProjectionMaterial> material;
    private final ProjectionSourceEvidence sourceEvidence;

    MerchantPublicDescriptorProjectionObservation(
            Optional<MerchantPublicDescriptorProjectionMaterial> material,
            ProjectionSourceEvidence sourceEvidence
    ) {
        this.material = Objects.requireNonNull(material, "material");
        this.sourceEvidence = Objects.requireNonNull(sourceEvidence, "sourceEvidence");

        if (!DESCRIPTOR_SOURCE.equals(sourceEvidence.sourceReference())) {
            throw new IllegalArgumentException(
                    "Descriptor observation evidence must belong to the descriptor source"
            );
        }

        if (material.isPresent()) {
            String progress = material.orElseThrow().observedProgressIdentifier();
            Optional<String> expectedProgress = Optional.of(progress);
            if (!sourceEvidence.isCurrent()
                    || !expectedProgress.equals(
                            sourceEvidence.observedProgressIdentifier()
                    )
                    || !expectedProgress.equals(
                            sourceEvidence.requiredCurrentProgressIdentifier()
                    )) {
                throw new IllegalArgumentException(
                        "Descriptor material and P2 evidence must share exact current progress"
                );
            }
        } else if (sourceEvidence.isCurrent()) {
            throw new IllegalArgumentException(
                    "Current descriptor evidence requires descriptor material"
            );
        }
    }

    public Optional<MerchantPublicDescriptorProjectionMaterial> material() {
        return material;
    }

    public ProjectionSourceEvidence sourceEvidence() {
        return sourceEvidence;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MerchantPublicDescriptorProjectionObservation that)) {
            return false;
        }
        return material.equals(that.material)
                && sourceEvidence.equals(that.sourceEvidence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, sourceEvidence);
    }

    @Override
    public String toString() {
        return "MerchantPublicDescriptorProjectionObservation[material="
                + material
                + ", sourceEvidence="
                + sourceEvidence
                + "]";
    }
}
