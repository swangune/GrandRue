package mainstreet.infrastructure.persistence.commercial;

import mainstreet.commercial.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Versioned, deterministic retained-value encoding, independent of live registration.
 * MS-PROT-056 v1.9, designs/MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment.md,
 * §13 — Retention, recovery and runtime boundaries. This is not an approval signature.
 */
final class CommercialCatalogueManifestCodec {
    byte[] encode(CommercialCatalogueManifest manifest) {
        return bytes(out -> {
            out.writeInt(1);
            text(out, manifest.revision().catalogueRevisionIdentifier());
            for (var plan : List.of(manifest.revision().freePlan(), manifest.revision().businessPlan(), manifest.revision().growthPlan())) {
                text(out, plan.level().name());
                text(out, plan.revisionIdentifier());
                values(out, plan.entitlements(), (stream, grant) -> text(stream, grant.identifier()));
            }
            values(out, manifest.bindings(), this::binding);
            values(out, manifest.allocationConformanceEvidence(), this::text);
            text(out, manifest.approvalProvenanceReference());
        });
    }

    CommercialCatalogueManifest decode(byte[] content) {
        try {
            var input = new DataInputStream(new ByteArrayInputStream(Objects.requireNonNull(content)));
            if (input.readInt() != 1) {
                throw new CatalogueResolutionException(CatalogueResolutionException.Reason.TECHNICAL_FAILURE,
                        "Retained catalogue encoding version is unsupported");
            }
            var revision = new StandardPlanCatalogueRevision(text(input), plan(input), plan(input), plan(input));
            var manifest = new CommercialCatalogueManifest(revision, values(input, this::binding),
                    values(input, this::text), text(input));
            requireEnd(input);
            return manifest;
        } catch (IOException | IllegalArgumentException | NullPointerException failure) {
            throw new CatalogueResolutionException(CatalogueResolutionException.Reason.INTEGRITY_FAILURE,
                    "Retained catalogue manifest is malformed");
        }
    }

    private StandardPlanRevision plan(DataInputStream in) throws IOException {
        return new StandardPlanRevision(StandardPlanLevel.valueOf(text(in)), text(in),
                values(in, stream -> new CommercialEntitlementIdentity(text(stream))));
    }

    private void binding(DataOutputStream out, CommercialAccessBinding value) throws IOException {
        text(out, value.entitlementIdentity().identifier());
        text(out, value.targetKind().name());
        target(out, value.target());
        text(out, value.protectedPurpose());
        text(out, value.governingAuthority());
        values(out, value.supportingAccessRequirements(), (stream, requirement) -> {
            target(stream, requirement.target());
            values(stream, requirement.requiredPurposes(), this::text);
            text(stream, requirement.classificationAuthority());
        });
        text(out, value.newUseAndResidualBoundaryAuthority());
    }

    private CommercialAccessBinding binding(DataInputStream in) throws IOException {
        return new CommercialAccessBinding(new CommercialEntitlementIdentity(text(in)),
                CommercialEntitlementTargetKind.valueOf(text(in)), target(in), text(in), text(in),
                values(in, stream -> new CommercialSupportingAccessRequirement(target(stream),
                        values(stream, this::text), text(stream))), text(in));
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
        for (var value : values) encoded.add(bytes(stream -> writer.write(stream, value)));
        encoded.sort(Arrays::compareUnsigned);
        out.writeInt(encoded.size());
        for (var value : encoded) blob(out, value);
    }

    private <T> Set<T> values(DataInputStream in, Reader<T> reader) throws IOException {
        int count = length(in);
        Set<T> result = new HashSet<>();
        for (int index = 0; index < count; index++) {
            var entry = new DataInputStream(new ByteArrayInputStream(blob(in)));
            if (!result.add(reader.read(entry))) throw new IOException("Duplicate retained value");
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
        if (length < 0 || length > in.available()) throw new IOException("Invalid retained length");
        return length;
    }

    private static void requireEnd(DataInputStream in) throws IOException {
        if (in.available() != 0) throw new IOException("Unexpected trailing retained content");
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

    @FunctionalInterface private interface Output { void write(DataOutputStream out) throws IOException; }
    @FunctionalInterface private interface Writer<T> { void write(DataOutputStream out, T value) throws IOException; }
    @FunctionalInterface private interface Reader<T> { T read(DataInputStream in) throws IOException; }
}
