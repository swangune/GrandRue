package grandrue.infrastructure.persistence.commercial;

import grandrue.commercial.CatalogueResolutionException;
import grandrue.commercial.CommercialAccessBinding;
import grandrue.commercial.CommercialAccessTarget;
import grandrue.commercial.CommercialCatalogueManifest;
import grandrue.commercial.CommercialConditionalSupportAlternative;
import grandrue.commercial.CommercialConditionalSupportingAccessRequirement;
import grandrue.commercial.CommercialEntitlementIdentity;
import grandrue.commercial.CommercialEntitlementTargetKind;
import grandrue.commercial.CommercialRequiredPurpose;
import grandrue.commercial.CommercialSupportingAccessRequirement;
import grandrue.commercial.StandardPlanCatalogueRevision;
import grandrue.commercial.StandardPlanLevel;
import grandrue.commercial.StandardPlanRevision;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Versioned, deterministic retained-value encoding, independent of live registration.
 * MS-PROT-056 v1.9 §13 governs retained catalogue evidence.
 * MS-PROT-056 v1.10 §§11–15 adds retained conditional supporting-commercial evidence.
 * This is not an approval signature.
 */
final class CommercialCatalogueManifestCodec {
    private static final int VERSION_1 = 1;
    private static final int VERSION_2 = 2;

    byte[] encode(CommercialCatalogueManifest manifest) {
        return bytes(out -> {
            out.writeInt(VERSION_2);
            text(out, manifest.revision().catalogueRevisionIdentifier());
            for (var plan : List.of(
                    manifest.revision().freePlan(),
                    manifest.revision().businessPlan(),
                    manifest.revision().growthPlan())) {
                text(out, plan.level().name());
                text(out, plan.revisionIdentifier());
                values(out, plan.entitlements(), (stream, grant) -> text(stream, grant.identifier()));
            }
            values(out, manifest.bindings(), this::bindingV2);
            values(out, manifest.allocationConformanceEvidence(), this::text);
            text(out, manifest.approvalProvenanceReference());
        });
    }

    CommercialCatalogueManifest decode(byte[] content) {
        try {
            var input = new DataInputStream(new ByteArrayInputStream(Objects.requireNonNull(content)));
            int version = input.readInt();
            if (version != VERSION_1 && version != VERSION_2) {
                throw new CatalogueResolutionException(
                        CatalogueResolutionException.Reason.TECHNICAL_FAILURE,
                        "Retained catalogue encoding version is unsupported");
            }
            var revision = new StandardPlanCatalogueRevision(
                    text(input),
                    plan(input),
                    plan(input),
                    plan(input));
            var manifest = new CommercialCatalogueManifest(
                    revision,
                    values(input, stream -> binding(stream, version)),
                    values(input, this::text),
                    text(input));
            requireEnd(input);
            return manifest;
        } catch (IOException | IllegalArgumentException | NullPointerException failure) {
            throw new CatalogueResolutionException(
                    CatalogueResolutionException.Reason.INTEGRITY_FAILURE,
                    "Retained catalogue manifest is malformed");
        }
    }

    private StandardPlanRevision plan(DataInputStream in) throws IOException {
        return new StandardPlanRevision(
                StandardPlanLevel.valueOf(text(in)),
                text(in),
                values(in, stream -> new CommercialEntitlementIdentity(text(stream))));
    }

    private void bindingV2(DataOutputStream out, CommercialAccessBinding value) throws IOException {
        text(out, value.entitlementIdentity().identifier());
        text(out, value.targetKind().name());
        target(out, value.target());
        text(out, value.protectedPurpose());
        text(out, value.governingAuthority());
        values(out, value.supportingAccessRequirements(), this::support);
        values(out, value.conditionalSupportingAccessRequirements(), this::conditionalSupport);
        text(out, value.newUseAndResidualBoundaryAuthority());
    }

    private CommercialAccessBinding binding(DataInputStream in, int version) throws IOException {
        var identity = new CommercialEntitlementIdentity(text(in));
        var kind = CommercialEntitlementTargetKind.valueOf(text(in));
        var target = target(in);
        var purpose = text(in);
        var governingAuthority = text(in);
        var support = values(in, this::support);
        if (version == VERSION_1) {
            return new CommercialAccessBinding(
                    identity,
                    kind,
                    target,
                    purpose,
                    governingAuthority,
                    support,
                    text(in));
        }
        var conditional = values(in, this::conditionalSupport);
        return new CommercialAccessBinding(
                identity,
                kind,
                target,
                purpose,
                governingAuthority,
                support,
                conditional,
                text(in));
    }

    private void support(
            DataOutputStream out,
            CommercialSupportingAccessRequirement requirement
    ) throws IOException {
        target(out, requirement.target());
        values(out, requirement.requiredPurposes(), this::text);
        text(out, requirement.classificationAuthority());
    }

    private CommercialSupportingAccessRequirement support(DataInputStream in) throws IOException {
        return new CommercialSupportingAccessRequirement(
                target(in),
                values(in, this::text),
                text(in));
    }

    private void conditionalSupport(
            DataOutputStream out,
            CommercialConditionalSupportingAccessRequirement requirement
    ) throws IOException {
        text(out, requirement.conditionAuthority());
        values(out, requirement.alternatives(), this::conditionalAlternative);
        text(out, requirement.classificationAuthority());
    }

    private CommercialConditionalSupportingAccessRequirement conditionalSupport(
            DataInputStream in
    ) throws IOException {
        return new CommercialConditionalSupportingAccessRequirement(
                text(in),
                values(in, this::conditionalAlternative),
                text(in));
    }

    private void conditionalAlternative(
            DataOutputStream out,
            CommercialConditionalSupportAlternative alternative
    ) throws IOException {
        text(out, alternative.conditionValue());
        values(out, alternative.requiredPurposes(), this::requiredPurpose);
    }

    private CommercialConditionalSupportAlternative conditionalAlternative(
            DataInputStream in
    ) throws IOException {
        return new CommercialConditionalSupportAlternative(
                text(in),
                values(in, this::requiredPurpose));
    }

    private void requiredPurpose(
            DataOutputStream out,
            CommercialRequiredPurpose required
    ) throws IOException {
        target(out, required.target());
        text(out, required.protectedPurpose());
    }

    private CommercialRequiredPurpose requiredPurpose(DataInputStream in) throws IOException {
        return new CommercialRequiredPurpose(target(in), text(in));
    }

    private void target(DataOutputStream out, CommercialAccessTarget target) throws IOException {
        text(out, target.owner());
        text(out, target.targetIdentity());
        text(out, target.contractRevision());
    }

    private CommercialAccessTarget target(DataInputStream in) throws IOException {
        return new CommercialAccessTarget(text(in), text(in), text(in));
    }

    private void text(DataOutputStream out, String text) throws IOException {
        blob(out, text.getBytes(StandardCharsets.UTF_8));
    }

    private String text(DataInputStream in) throws IOException {
        return StandardCharsets.UTF_8.newDecoder().decode(ByteBuffer.wrap(blob(in))).toString();
    }

    private <T> void values(DataOutputStream out, Set<T> values, Writer<T> writer) throws IOException {
        var encoded = new ArrayList<byte[]>();
        for (var value : values) {
            encoded.add(bytes(stream -> writer.write(stream, value)));
        }
        encoded.sort(Arrays::compareUnsigned);
        out.writeInt(encoded.size());
        for (var value : encoded) {
            blob(out, value);
        }
    }

    private <T> Set<T> values(DataInputStream in, Reader<T> reader) throws IOException {
        int count = length(in);
        Set<T> result = new HashSet<>();
        for (int index = 0; index < count; index++) {
            var entry = new DataInputStream(new ByteArrayInputStream(blob(in)));
            if (!result.add(reader.read(entry))) {
                throw new IOException("Duplicate retained value");
            }
            requireEnd(entry);
        }
        return Set.copyOf(result);
    }

    private static void blob(DataOutputStream out, byte[] bytes) throws IOException {
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    private static byte[] blob(DataInputStream in) throws IOException {
        return in.readNBytes(length(in));
    }

    private static int length(DataInputStream in) throws IOException {
        int length = in.readInt();
        if (length < 0 || length > in.available()) {
            throw new IOException("Invalid retained length");
        }
        return length;
    }

    private static void requireEnd(DataInputStream in) throws IOException {
        if (in.available() != 0) {
            throw new IOException("Unexpected trailing retained content");
        }
    }

    private static byte[] bytes(Output output) {
        try {
            var buffer = new ByteArrayOutputStream();
            var stream = new DataOutputStream(buffer);
            output.write(stream);
            stream.flush();
            return buffer.toByteArray();
        } catch (IOException failure) {
            throw new UncheckedIOException(failure);
        }
    }

    @FunctionalInterface
    private interface Output {
        void write(DataOutputStream out) throws IOException;
    }

    @FunctionalInterface
    private interface Writer<T> {
        void write(DataOutputStream out, T value) throws IOException;
    }

    @FunctionalInterface
    private interface Reader<T> {
        T read(DataInputStream in) throws IOException;
    }
}
