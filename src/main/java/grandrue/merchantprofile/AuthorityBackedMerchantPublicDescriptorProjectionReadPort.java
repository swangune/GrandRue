package grandrue.merchantprofile;



import grandrue.application.MerchantScope;
import grandrue.surface.ExposableElementReference;
import grandrue.surface.ExposureCandidateObservation;
import grandrue.surface.ProjectionMaterialSourceAffinity;
import grandrue.surface.ProjectionSourceAvailability;
import grandrue.surface.ProjectionSourceCompleteness;
import grandrue.surface.ProjectionSourceDependencyReference;
import grandrue.surface.ProjectionSourceEvidence;
import grandrue.surface.ProjectionSourceRevocationState;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Profile-owned BR3 adapter that acquires one current authoritative Merchant
 * Public Descriptor observation and establishes its typed projection material,
 * exact revision affinity and corresponding P2 evidence without transferring
 * descriptor ownership to generic Projection/Surface infrastructure.
 */
public final class AuthorityBackedMerchantPublicDescriptorProjectionReadPort
        implements MerchantPublicDescriptorProjectionReadPort {

    private static final ProjectionSourceDependencyReference DESCRIPTOR_SOURCE =
            new ProjectionSourceDependencyReference(
                    "profile",
                    "merchant-public-descriptor"
            );

    private final MerchantPublicDescriptorAuthority authority;

    public AuthorityBackedMerchantPublicDescriptorProjectionReadPort(
            MerchantPublicDescriptorAuthority authority
    ) {
        this.authority = Objects.requireNonNull(authority, "authority");
    }

    @Override
    public MerchantPublicDescriptorProjectionObservation observe(
            MerchantScope merchantScope,
            Instant observedAt
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(observedAt, "observedAt");

        Optional<MerchantPublicDescriptorRevision> current =
                authority.current(merchantScope);
        if (current.isEmpty()) {
            return missingObservation(merchantScope, observedAt);
        }

        MerchantPublicDescriptorRevision revision = current.orElseThrow();
        MerchantPublicDescriptorProjectionMaterial material =
                materialFor(merchantScope, revision);
        String progress = material.observedProgressIdentifier();

        return new MerchantPublicDescriptorProjectionObservation(
                Optional.of(material),
                new ProjectionSourceEvidence(
                        DESCRIPTOR_SOURCE,
                        evidenceIdentifier(merchantScope, revision),
                        Optional.of(progress),
                        Optional.of(progress),
                        observedAt,
                        ProjectionSourceAvailability.AVAILABLE,
                        ProjectionSourceCompleteness.COMPLETE,
                        ProjectionSourceRevocationState.NOT_APPLICABLE
                )
        );
    }

    private static MerchantPublicDescriptorProjectionObservation missingObservation(
            MerchantScope merchantScope,
            Instant observedAt
    ) {
        return new MerchantPublicDescriptorProjectionObservation(
                Optional.empty(),
                new ProjectionSourceEvidence(
                        DESCRIPTOR_SOURCE,
                        "profile-descriptor-"
                                + merchantScope.merchantIdentifier()
                                + "-absent-"
                                + observedAt,
                        Optional.empty(),
                        Optional.empty(),
                        observedAt,
                        ProjectionSourceAvailability.UNAVAILABLE,
                        ProjectionSourceCompleteness.MISSING,
                        ProjectionSourceRevocationState.NOT_APPLICABLE
                )
        );
    }

    private static String evidenceIdentifier(
            MerchantScope merchantScope,
            MerchantPublicDescriptorRevision revision
    ) {
        return "profile-descriptor-"
                + merchantScope.merchantIdentifier()
                + "-revision-"
                + revision.revision()
                + "-"
                + revision.provenanceReference();
    }

    private static MerchantPublicDescriptorProjectionMaterial materialFor(
            MerchantScope requestedScope,
            MerchantPublicDescriptorRevision revision
    ) {
        Objects.requireNonNull(revision, "revision");
        MerchantPublicDescriptor descriptor = revision.descriptor();
        if (!requestedScope.equals(descriptor.merchantScope())) {
            throw new IllegalStateException(
                    "Descriptor authority returned material for another Merchant Scope"
            );
        }

        String progress = "descriptor-revision-" + revision.revision();
        ProjectionMaterialSourceAffinity affinity =
                new ProjectionMaterialSourceAffinity(
                        DESCRIPTOR_SOURCE,
                        progress
                );
        List<MerchantPublicDescriptorProjectionFragment> fragments =
                new ArrayList<>();

        fragments.add(fragment(
                "public-display-name",
                descriptor.displayName(),
                affinity
        ));
        descriptor.tagline().ifPresent(value -> fragments.add(fragment(
                "public-tagline",
                value,
                affinity
        )));
        descriptor.shortSummary().ifPresent(value -> fragments.add(fragment(
                "public-short-summary",
                value,
                affinity
        )));
        descriptor.approvedDescription().ifPresent(value -> fragments.add(fragment(
                "public-approved-description",
                value,
                affinity
        )));

        return new MerchantPublicDescriptorProjectionMaterial(
                requestedScope,
                progress,
                fragments
        );
    }

    private static MerchantPublicDescriptorProjectionFragment fragment(
            String elementIdentifier,
            String value,
            ProjectionMaterialSourceAffinity affinity
    ) {
        return new MerchantPublicDescriptorProjectionFragment(
                new ExposureCandidateObservation(
                        new ExposableElementReference(
                                "profile",
                                elementIdentifier
                        ),
                        Optional.empty()
                ),
                Set.of(affinity),
                value
        );
    }
}
