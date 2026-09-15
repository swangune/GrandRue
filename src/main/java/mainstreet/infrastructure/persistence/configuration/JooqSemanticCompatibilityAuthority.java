package mainstreet.infrastructure.persistence.configuration;

import mainstreet.semantic.configuration.SemanticCompatibilityAuthority;
import mainstreet.semantic.configuration.SemanticCompatibilityDisposition;
import mainstreet.semantic.configuration.SemanticCompatibilityEvidence;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * PostgreSQL/jOOQ authority for exact, directional and reference-scoped
 * semantic compatibility evidence.
 *
 * <p>Evidence is append-only by transition identity. Re-recording identical
 * evidence is idempotent; attempting to bind the same merchant/source/target
 * transition to different evidence is rejected.</p>
 */
public final class JooqSemanticCompatibilityAuthority
        implements SemanticCompatibilityAuthority {

    private static final Table<?> EVIDENCE =
            DSL.table(DSL.name("semantic_compatibility_evidence"));

    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> SOURCE_RELEASE_IDENTIFIER =
            DSL.field(DSL.name("source_release_identifier"), String.class);
    private static final Field<String> TARGET_RELEASE_IDENTIFIER =
            DSL.field(DSL.name("target_release_identifier"), String.class);
    private static final Field<String> SOURCE_SEMANTIC_REGISTRY_VERSION =
            DSL.field(DSL.name("source_semantic_registry_version"), String.class);
    private static final Field<String> TARGET_SEMANTIC_REGISTRY_VERSION =
            DSL.field(DSL.name("target_semantic_registry_version"), String.class);
    private static final Field<String[]> REFERENCE_SCOPE =
            DSL.field(DSL.name("reference_scope"), String[].class);
    private static final Field<String> DISPOSITION =
            DSL.field(DSL.name("disposition"), String.class);
    private static final Field<String> MIGRATION_EVIDENCE_IDENTIFIER =
            DSL.field(DSL.name("migration_evidence_identifier"), String.class);
    private static final Field<String> DECISION_EVIDENCE_IDENTIFIER =
            DSL.field(DSL.name("decision_evidence_identifier"), String.class);

    private final DSLContext dsl;

    public JooqSemanticCompatibilityAuthority(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
    }

    public void record(SemanticCompatibilityEvidence evidence) {
        Objects.requireNonNull(evidence, "evidence");

        String[] referenceScope = evidence.referenceScope().stream()
                .sorted()
                .toArray(String[]::new);

        int inserted = dsl.insertInto(EVIDENCE)
                .columns(
                        MERCHANT_IDENTIFIER,
                        SOURCE_RELEASE_IDENTIFIER,
                        TARGET_RELEASE_IDENTIFIER,
                        SOURCE_SEMANTIC_REGISTRY_VERSION,
                        TARGET_SEMANTIC_REGISTRY_VERSION,
                        REFERENCE_SCOPE,
                        DISPOSITION,
                        MIGRATION_EVIDENCE_IDENTIFIER,
                        DECISION_EVIDENCE_IDENTIFIER
                )
                .values(
                        evidence.merchantIdentifier(),
                        evidence.sourceReleaseIdentifier(),
                        evidence.targetReleaseIdentifier(),
                        evidence.sourceSemanticRegistryVersion(),
                        evidence.targetSemanticRegistryVersion(),
                        referenceScope,
                        evidence.disposition().name(),
                        evidence.migrationEvidenceIdentifier().orElse(null),
                        evidence.decisionEvidenceIdentifier().orElse(null)
                )
                .onConflict(
                        MERCHANT_IDENTIFIER,
                        SOURCE_RELEASE_IDENTIFIER,
                        TARGET_RELEASE_IDENTIFIER
                )
                .doNothing()
                .execute();

        if (inserted == 0) {
            SemanticCompatibilityEvidence existing = evidenceFor(
                    evidence.merchantIdentifier(),
                    evidence.sourceReleaseIdentifier(),
                    evidence.targetReleaseIdentifier()
            ).orElseThrow(() -> new IllegalStateException(
                    "Semantic compatibility evidence disappeared after conflict"
            ));
            if (!existing.equals(evidence)) {
                throw new IllegalArgumentException(
                        "Semantic compatibility transition is already bound to different evidence"
                );
            }
        }
    }

    @Override
    public Optional<SemanticCompatibilityEvidence> evidenceFor(
            String merchantIdentifier,
            String sourceReleaseIdentifier,
            String targetReleaseIdentifier
    ) {
        requireIdentifier(merchantIdentifier, "Merchant identifier");
        requireIdentifier(sourceReleaseIdentifier, "Source release identifier");
        requireIdentifier(targetReleaseIdentifier, "Target release identifier");

        Record record = dsl.select(
                        MERCHANT_IDENTIFIER,
                        SOURCE_RELEASE_IDENTIFIER,
                        TARGET_RELEASE_IDENTIFIER,
                        SOURCE_SEMANTIC_REGISTRY_VERSION,
                        TARGET_SEMANTIC_REGISTRY_VERSION,
                        REFERENCE_SCOPE,
                        DISPOSITION,
                        MIGRATION_EVIDENCE_IDENTIFIER,
                        DECISION_EVIDENCE_IDENTIFIER
                )
                .from(EVIDENCE)
                .where(MERCHANT_IDENTIFIER.eq(merchantIdentifier))
                .and(SOURCE_RELEASE_IDENTIFIER.eq(sourceReleaseIdentifier))
                .and(TARGET_RELEASE_IDENTIFIER.eq(targetReleaseIdentifier))
                .fetchOne();

        if (record == null) {
            return Optional.empty();
        }

        String[] storedScope = Objects.requireNonNull(
                record.get(REFERENCE_SCOPE),
                "Stored semantic compatibility reference scope"
        );
        Set<String> referenceScope = Arrays.stream(storedScope)
                .collect(Collectors.toUnmodifiableSet());

        return Optional.of(new SemanticCompatibilityEvidence(
                record.get(MERCHANT_IDENTIFIER),
                record.get(SOURCE_RELEASE_IDENTIFIER),
                record.get(TARGET_RELEASE_IDENTIFIER),
                record.get(SOURCE_SEMANTIC_REGISTRY_VERSION),
                record.get(TARGET_SEMANTIC_REGISTRY_VERSION),
                referenceScope,
                SemanticCompatibilityDisposition.valueOf(record.get(DISPOSITION)),
                Optional.ofNullable(record.get(MIGRATION_EVIDENCE_IDENTIFIER)),
                Optional.ofNullable(record.get(DECISION_EVIDENCE_IDENTIFIER))
        ));
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
