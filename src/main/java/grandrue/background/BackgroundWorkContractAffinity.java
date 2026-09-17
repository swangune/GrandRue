package grandrue.background;

import java.util.Objects;

/** Historical registered-responsibility affinity, distinct from configuration provenance and execution authority. */
public record BackgroundWorkContractAffinity(
        BackgroundWorkContractIdentity contractIdentity,
        String semanticRegistryReleaseIdentifier
) {
    public BackgroundWorkContractAffinity {
        Objects.requireNonNull(contractIdentity, "contractIdentity");
        BackgroundWorkContractIdentity.requireReference(semanticRegistryReleaseIdentifier, "semantic release");
    }
}
