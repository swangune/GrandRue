package grandrue.deployment;

import grandrue.semantic.execution.ExecutableSupportManifest;
import mainstreet.semantic.release.DeploymentSemanticMaterialisationSet;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/** Trusted deployment request to retain one exact observed generation. */
public record RecordServingDeploymentAdmissionSnapshotCommand(
        String generationIdentifier,
        DeploymentSemanticMaterialisationSet materialisationSet,
        List<ExecutableSupportManifest> supportManifests,
        Instant evidenceRecordedAt
) {
    public RecordServingDeploymentAdmissionSnapshotCommand(
            String generationIdentifier,
            DeploymentSemanticMaterialisationSet materialisationSet,
            Collection<ExecutableSupportManifest> supportManifests,
            Instant evidenceRecordedAt
    ) {
        this(
                generationIdentifier,
                materialisationSet,
                List.copyOf(Objects.requireNonNull(
                        supportManifests,
                        "supportManifests"
                )),
                evidenceRecordedAt
        );
    }

    public RecordServingDeploymentAdmissionSnapshotCommand {
        if (generationIdentifier == null || generationIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Generation identifier must not be blank"
            );
        }
        Objects.requireNonNull(materialisationSet, "materialisationSet");
        supportManifests = List.copyOf(
                Objects.requireNonNull(supportManifests, "supportManifests")
        );
        Objects.requireNonNull(evidenceRecordedAt, "evidenceRecordedAt");
    }
}
