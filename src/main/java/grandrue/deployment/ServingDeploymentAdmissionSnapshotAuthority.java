package grandrue.deployment;

import java.util.Optional;

/** Append-only authority for exact serving-generation admission evidence. */
public interface ServingDeploymentAdmissionSnapshotAuthority {

    ServingDeploymentAdmissionSnapshot record(
            RecordServingDeploymentAdmissionSnapshotCommand command
    );

    Optional<ServingDeploymentAdmissionSnapshot> snapshot(
            String generationIdentifier
    );
}
