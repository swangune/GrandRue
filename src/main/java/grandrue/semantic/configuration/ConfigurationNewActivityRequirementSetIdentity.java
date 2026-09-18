package grandrue.semantic.configuration;

import grandrue.semantic.execution.ExecutableSupportRequirement;
import grandrue.semantic.execution.SemanticExecutionContractReference;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Versioned deterministic content identity for one exact Configuration
 * New-Activity Execution Requirement Set.
 */
public final class ConfigurationNewActivityRequirementSetIdentity {

    public static final int CANONICALIZATION_VERSION = 1;

    private static final String DOMAIN =
            "mainstreet.configuration-new-activity-requirement-set/v1";
    private static final String PREFIX = "ms-reqset-v1:sha256:";
    private static final Comparator<String> UTF_8_ORDER =
            (left, right) -> java.util.Arrays.compareUnsigned(
                    left.getBytes(StandardCharsets.UTF_8),
                    right.getBytes(StandardCharsets.UTF_8)
            );
    private static final Comparator<SemanticExecutionContractReference>
            CONTRACT_ORDER = Comparator
                    .comparing(
                            SemanticExecutionContractReference
                                    ::semanticRegistryReleaseIdentifier,
                            UTF_8_ORDER
                    )
                    .thenComparing(
                            SemanticExecutionContractReference
                                    ::contractIdentifier,
                            UTF_8_ORDER
                    );

    private ConfigurationNewActivityRequirementSetIdentity() {
    }

    public static String derive(
            Collection<ExecutableSupportRequirement> requirements
    ) {
        Objects.requireNonNull(requirements, "requirements");
        List<ExecutableSupportRequirement> ordered = new ArrayList<>(
                requirements
        );
        ordered.forEach(requirement -> Objects.requireNonNull(
                requirement,
                "requirements contains null"
        ));
        ordered.sort(Comparator.comparing(
                ExecutableSupportRequirement::affectedExecutionContract,
                CONTRACT_ORDER
        ));
        requireUniqueAffectedContracts(ordered);

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try (DataOutputStream output = new DataOutputStream(bytes)) {
                writeString(output, DOMAIN);
                for (ExecutableSupportRequirement requirement : ordered) {
                    writeReference(
                            output,
                            requirement.affectedExecutionContract()
                    );
                    List<SemanticExecutionContractReference> participants =
                            requirement.requiredParticipantAndEffectContracts()
                                    .stream()
                                    .sorted(CONTRACT_ORDER)
                                    .toList();
                    output.writeInt(participants.size());
                    for (SemanticExecutionContractReference participant
                            : participants) {
                        writeReference(output, participant);
                    }
                }
            }
            return PREFIX + java.util.HexFormat.of().formatHex(
                    MessageDigest.getInstance("SHA-256")
                            .digest(bytes.toByteArray())
            );
        } catch (IOException impossible) {
            throw new IllegalStateException(
                    "Could not encode requirement-set identity",
                    impossible
            );
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException(
                    "SHA-256 is unavailable",
                    impossible
            );
        }
    }

    private static void requireUniqueAffectedContracts(
            List<ExecutableSupportRequirement> requirements
    ) {
        SemanticExecutionContractReference previous = null;
        for (ExecutableSupportRequirement requirement : requirements) {
            SemanticExecutionContractReference affected =
                    requirement.affectedExecutionContract();
            if (affected.equals(previous)) {
                throw new IllegalArgumentException(
                        "Duplicate affected execution contract: " + affected
                );
            }
            previous = affected;
        }
    }

    private static void writeReference(
            DataOutputStream output,
            SemanticExecutionContractReference reference
    ) throws IOException {
        writeString(output, reference.semanticRegistryReleaseIdentifier());
        writeString(output, reference.contractIdentifier());
    }

    private static void writeString(
            DataOutputStream output,
            String value
    ) throws IOException {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        output.writeInt(bytes.length);
        output.write(bytes);
    }
}
