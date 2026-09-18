package grandrue.surface;

import grandrue.semantic.release.SemanticDefinitionSectionDecoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Deterministic registry-owned codec for one exact-release Exposure definition
 * section. Membership identity semantics are explicit published evidence and
 * are never reconstructed from current defaults.
 */
public final class ExposureElementContractRegistryTextCodec
        implements SemanticDefinitionSectionDecoder<ExposureElementContractRegistrySnapshot> {

    private static final String FORMAT = "mainstreet-exposure-definitions-v1";
    private static final String FORMAT_PREFIX = "format=";
    private static final String RELEASE_PREFIX = "release=";
    private static final String CONTRACT_PREFIX = "contract=";
    private static final String ABSENT = "-";
    private static final String SINGLETON = "S";
    private static final String INSTANCE_QUALIFIED = "I";

    private static final Comparator<ExposureElementContract> CONTRACT_ORDER =
            Comparator.comparing((ExposureElementContract contract) ->
                            contract.identity().ownerIdentifier())
                    .thenComparing(contract ->
                            contract.identity().contractIdentifier());

    private static final Comparator<ExposureRequirementReference> REQUIREMENT_ORDER =
            Comparator.comparing(ExposureRequirementReference::ownerIdentifier)
                    .thenComparing(ExposureRequirementReference::requirementIdentifier);

    public byte[] encode(ExposureElementContractRegistrySnapshot registry) {
        if (registry == null) {
            throw new IllegalArgumentException("registry must not be null");
        }

        StringBuilder encoded = new StringBuilder();
        encoded.append(FORMAT_PREFIX).append(FORMAT).append('\n');
        encoded.append(RELEASE_PREFIX)
                .append(encodeText(registry.semanticRegistryReleaseIdentifier()))
                .append('\n');

        registry.contracts().stream()
                .sorted(CONTRACT_ORDER)
                .map(ExposureElementContractRegistryTextCodec::encodeContract)
                .forEach(value -> encoded.append(CONTRACT_PREFIX)
                        .append(value)
                        .append('\n'));

        return encoded.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public ExposureElementContractRegistrySnapshot decode(byte[] publishedDefinitions) {
        if (publishedDefinitions == null) {
            throw new IllegalArgumentException("publishedDefinitions must not be null");
        }

        String text = new String(publishedDefinitions, StandardCharsets.UTF_8);
        String format = null;
        String release = null;
        List<ExposureElementContract> contracts = new ArrayList<>();

        for (String line : text.split("\\n", -1)) {
            if (line.isEmpty()) {
                continue;
            }
            if (line.startsWith(FORMAT_PREFIX)) {
                if (format != null) {
                    throw new IllegalArgumentException(
                            "Duplicate Exposure definition format field"
                    );
                }
                format = line.substring(FORMAT_PREFIX.length());
                continue;
            }
            if (line.startsWith(RELEASE_PREFIX)) {
                if (release != null) {
                    throw new IllegalArgumentException(
                            "Duplicate Exposure definition release field"
                    );
                }
                release = decodeText(line.substring(RELEASE_PREFIX.length()));
                continue;
            }
            if (line.startsWith(CONTRACT_PREFIX)) {
                contracts.add(decodeContract(
                        line.substring(CONTRACT_PREFIX.length())
                ));
                continue;
            }
            throw new IllegalArgumentException(
                    "Unknown Exposure definition field"
            );
        }

        if (!FORMAT.equals(format)) {
            throw new IllegalArgumentException(
                    "Unsupported Exposure definition format"
            );
        }
        if (release == null || release.isBlank()) {
            throw new IllegalArgumentException(
                    "Exposure definition release must be explicit"
            );
        }

        return new ExposureElementContractRegistrySnapshot(
                release,
                Set.copyOf(contracts)
        );
    }

    private static String encodeContract(ExposureElementContract contract) {
        return String.join(
                "|",
                encodeText(contract.identity().ownerIdentifier()),
                encodeText(contract.identity().contractIdentifier()),
                encodeText(contract.exposableElementReference().ownerIdentifier()),
                encodeText(contract.exposableElementReference().elementIdentifier()),
                contract.audience().name(),
                encodeMemberIdentity(contract.memberIdentitySpecification()),
                contract.baselineDecision().name(),
                encodeMerchantChoice(contract.merchantChoiceSource()),
                encodeRequirements(contract.requirementReferences())
        );
    }

    private static ExposureElementContract decodeContract(String encoded) {
        String[] fields = encoded.split("\\|", -1);
        if (fields.length != 9) {
            throw new IllegalArgumentException(
                    "Exposure definition contract must explicitly contain membership semantics"
            );
        }

        return new ExposureElementContract(
                new ExposureElementContractIdentity(
                        decodeText(fields[0]),
                        decodeText(fields[1])
                ),
                new ExposableElementReference(
                        decodeText(fields[2]),
                        decodeText(fields[3])
                ),
                parseEnum(SurfaceAudience.class, fields[4], "audience"),
                decodeMemberIdentity(fields[5]),
                parseEnum(ExposureDecision.class, fields[6], "baseline decision"),
                decodeMerchantChoice(fields[7]),
                decodeRequirements(fields[8])
        );
    }

    private static String encodeMemberIdentity(
            ExposureMemberIdentitySpecification specification
    ) {
        if (specification instanceof ExposureMemberIdentitySpecification.Singleton) {
            return SINGLETON;
        }
        if (specification
                instanceof ExposureMemberIdentitySpecification.InstanceQualified qualified) {
            ExposureCandidateInstanceKindReference kind =
                    qualified.instanceKindReference();
            return INSTANCE_QUALIFIED
                    + ":"
                    + encodeText(kind.ownerIdentifier())
                    + ":"
                    + encodeText(kind.instanceKindIdentifier());
        }
        throw new IllegalArgumentException(
                "Unsupported Exposure member identity specification"
        );
    }

    private static ExposureMemberIdentitySpecification decodeMemberIdentity(
            String encoded
    ) {
        if (SINGLETON.equals(encoded)) {
            return ExposureMemberIdentitySpecification.singleton();
        }
        String prefix = INSTANCE_QUALIFIED + ":";
        if (!encoded.startsWith(prefix)) {
            throw new IllegalArgumentException(
                    "Exposure membership semantics must be explicit"
            );
        }
        String[] parts = encoded.substring(prefix.length()).split(":", -1);
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid instance-qualified Exposure membership semantics"
            );
        }
        return ExposureMemberIdentitySpecification.instanceQualified(
                new ExposureCandidateInstanceKindReference(
                        decodeText(parts[0]),
                        decodeText(parts[1])
                )
        );
    }

    private static String encodeMerchantChoice(
            Optional<MerchantExposureChoiceSourceReference> choice
    ) {
        return choice.map(reference ->
                        encodeText(reference.ownerIdentifier())
                                + ":"
                                + encodeText(reference.sourceIdentifier()))
                .orElse(ABSENT);
    }

    private static Optional<MerchantExposureChoiceSourceReference>
            decodeMerchantChoice(String encoded) {
        if (ABSENT.equals(encoded)) {
            return Optional.empty();
        }
        String[] parts = encoded.split(":", -1);
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid merchant Exposure choice source"
            );
        }
        return Optional.of(new MerchantExposureChoiceSourceReference(
                decodeText(parts[0]),
                decodeText(parts[1])
        ));
    }

    private static String encodeRequirements(
            Set<ExposureRequirementReference> requirements
    ) {
        if (requirements.isEmpty()) {
            return ABSENT;
        }
        return requirements.stream()
                .sorted(REQUIREMENT_ORDER)
                .map(reference -> encodeText(reference.ownerIdentifier())
                        + ":"
                        + encodeText(reference.requirementIdentifier()))
                .collect(Collectors.joining(","));
    }

    private static Set<ExposureRequirementReference> decodeRequirements(
            String encoded
    ) {
        if (ABSENT.equals(encoded)) {
            return Set.of();
        }
        if (encoded.isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid Exposure requirement set"
            );
        }
        return java.util.Arrays.stream(encoded.split(",", -1))
                .map(ExposureElementContractRegistryTextCodec::decodeRequirement)
                .collect(Collectors.toUnmodifiableSet());
    }

    private static ExposureRequirementReference decodeRequirement(String encoded) {
        String[] parts = encoded.split(":", -1);
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid Exposure requirement reference"
            );
        }
        return new ExposureRequirementReference(
                decodeText(parts[0]),
                decodeText(parts[1])
        );
    }

    private static String encodeText(String value) {
        return Base64.getEncoder().encodeToString(
                value.getBytes(StandardCharsets.UTF_8)
        );
    }

    private static String decodeText(String value) {
        try {
            return new String(
                    Base64.getDecoder().decode(value),
                    StandardCharsets.UTF_8
            );
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid Base64 Exposure definition field",
                    exception
            );
        }
    }

    private static <E extends Enum<E>> E parseEnum(
            Class<E> type,
            String value,
            String label
    ) {
        try {
            return Enum.valueOf(type, value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid Exposure definition " + label,
                    exception
            );
        }
    }
}
