package mainstreet.merchantprofile;

import grandrue.application.MerchantScope;
import mainstreet.surface.ProjectionMaterialSourceAffinity;
import mainstreet.surface.ProjectionSourceDependencyReference;

import java.util.List;
import java.util.Objects;

/**
 * Immutable Profile-owned material acquired from one exact current Merchant
 * Public Descriptor revision for bounded Projection use.
 */
public final class MerchantPublicDescriptorProjectionMaterial {

    private static final ProjectionSourceDependencyReference DESCRIPTOR_SOURCE =
            new ProjectionSourceDependencyReference(
                    "profile",
                    "merchant-public-descriptor"
            );

    private final MerchantScope merchantScope;
    private final String observedProgressIdentifier;
    private final List<MerchantPublicDescriptorProjectionFragment> fragments;

    MerchantPublicDescriptorProjectionMaterial(
            MerchantScope merchantScope,
            String observedProgressIdentifier,
            List<MerchantPublicDescriptorProjectionFragment> fragments
    ) {
        this.merchantScope = Objects.requireNonNull(
                merchantScope,
                "merchantScope"
        );
        if (observedProgressIdentifier == null
                || observedProgressIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Descriptor observed progress identifier must not be blank"
            );
        }
        this.observedProgressIdentifier = observedProgressIdentifier;
        this.fragments = List.copyOf(
                Objects.requireNonNull(fragments, "fragments")
        );
        validateFragments();
    }

    public MerchantScope merchantScope() {
        return merchantScope;
    }

    public String observedProgressIdentifier() {
        return observedProgressIdentifier;
    }

    public List<MerchantPublicDescriptorProjectionFragment> fragments() {
        return fragments;
    }

    private void validateFragments() {
        ProjectionMaterialSourceAffinity expected =
                new ProjectionMaterialSourceAffinity(
                        DESCRIPTOR_SOURCE,
                        observedProgressIdentifier
                );
        for (MerchantPublicDescriptorProjectionFragment fragment : fragments) {
            Objects.requireNonNull(fragment, "fragment");
            if (!fragment.sourceAffinities().equals(java.util.Set.of(expected))) {
                throw new IllegalArgumentException(
                        "Descriptor fragment must retain exact descriptor revision affinity"
                );
            }
        }
    }
}
