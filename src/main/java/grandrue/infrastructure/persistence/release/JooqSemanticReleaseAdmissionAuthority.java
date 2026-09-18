package grandrue.infrastructure.persistence.release;

import grandrue.semantic.release.AdvanceOrdinarySemanticReleaseCommand;
import grandrue.semantic.release.OrdinaryNewConfigurationSemanticReleaseReference;
import grandrue.semantic.release.RecordSemanticReleasePurposeAdmissionDecisionCommand;
import grandrue.semantic.release.SemanticReleaseAdmissionDisposition;
import grandrue.semantic.release.SemanticReleaseAdmissionFailureCategory;
import grandrue.semantic.release.SemanticReleaseAdmissionPersistenceException;
import grandrue.semantic.release.SemanticReleaseAdmissionAuthority;
import grandrue.semantic.release.SemanticReleasePurpose;
import grandrue.semantic.release.SemanticReleasePurposeAdmissionDecision;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** PostgreSQL authority for MS-PROT-040 v1.5 release-purpose currentness. */
public final class JooqSemanticReleaseAdmissionAuthority
        implements SemanticReleaseAdmissionAuthority {

    private static final Table<?> DECISIONS = table(
            "semantic_release_purpose_admission_decision"
    );
    private static final Table<?> CURRENT = table(
            "semantic_release_purpose_current_admission"
    );
    private static final Table<?> REFERENCES = table(
            "ordinary_new_configuration_semantic_release_reference_revision"
    );
    private static final Table<?> POINTER = table(
            "ordinary_new_configuration_semantic_release_pointer"
    );
    private static final Field<String> DECISION_ID = text(
            "admission_decision_identifier"
    );
    private static final Field<String> RELEASE_ID = text(
            "semantic_registry_release_identifier"
    );
    private static final Field<String> PURPOSE = text("admission_purpose");
    private static final Field<String> DISPOSITION = text(
            "admission_disposition"
    );
    private static final Field<String> PRINCIPAL_ID = text(
            "deciding_principal_identifier"
    );
    private static final Field<String> PROVENANCE = text(
            "decision_provenance_reference"
    );
    private static final Field<Instant> DECIDED_AT = instant("decided_at");
    private static final Field<String> REFERENCE_ID = text(
            "reference_revision_identifier"
    );
    private static final Field<String> VALIDATION_DECISION_ID = text(
            "validation_admission_decision_identifier"
    );
    private static final Field<String> ACTIVITY_DECISION_ID = text(
            "business_activity_admission_decision_identifier"
    );
    private static final Field<Long> REFERENCE_EPOCH = DSL.field(
            DSL.name("reference_epoch"), Long.class
    );
    private static final Field<String> SELECTING_PRINCIPAL_ID = text(
            "selecting_principal_identifier"
    );
    private static final Field<String> SELECTION_PROVENANCE = text(
            "selection_provenance_reference"
    );
    private static final Field<Instant> SELECTED_AT = instant("selected_at");
    private static final Field<String> POINTER_ID = text("pointer_identifier");
    private static final Field<String> DECISION_TABLE_ID = qualifiedText(
            "semantic_release_purpose_admission_decision",
            "admission_decision_identifier"
    );
    private static final Field<String> DECISION_TABLE_RELEASE = qualifiedText(
            "semantic_release_purpose_admission_decision",
            "semantic_registry_release_identifier"
    );
    private static final Field<String> DECISION_TABLE_PURPOSE = qualifiedText(
            "semantic_release_purpose_admission_decision", "admission_purpose"
    );
    private static final Field<String> DECISION_TABLE_DISPOSITION = qualifiedText(
            "semantic_release_purpose_admission_decision", "admission_disposition"
    );
    private static final Field<String> DECISION_TABLE_PRINCIPAL = qualifiedText(
            "semantic_release_purpose_admission_decision", "deciding_principal_identifier"
    );
    private static final Field<String> DECISION_TABLE_PROVENANCE = qualifiedText(
            "semantic_release_purpose_admission_decision", "decision_provenance_reference"
    );
    private static final Field<Instant> DECISION_TABLE_AT = qualifiedInstant(
            "semantic_release_purpose_admission_decision", "decided_at"
    );
    private static final Field<String> CURRENT_DECISION_ID = qualifiedText(
            "semantic_release_purpose_current_admission", "admission_decision_identifier"
    );
    private static final Field<String> CURRENT_RELEASE = qualifiedText(
            "semantic_release_purpose_current_admission", "semantic_registry_release_identifier"
    );
    private static final Field<String> CURRENT_PURPOSE = qualifiedText(
            "semantic_release_purpose_current_admission", "admission_purpose"
    );
    private static final Field<String> REFERENCE_TABLE_ID = qualifiedText(
            "ordinary_new_configuration_semantic_release_reference_revision",
            "reference_revision_identifier"
    );
    private static final Field<String> REFERENCE_TABLE_RELEASE = qualifiedText(
            "ordinary_new_configuration_semantic_release_reference_revision",
            "semantic_registry_release_identifier"
    );
    private static final Field<String> REFERENCE_TABLE_VALIDATION_DECISION = qualifiedText(
            "ordinary_new_configuration_semantic_release_reference_revision",
            "validation_admission_decision_identifier"
    );
    private static final Field<String> REFERENCE_TABLE_ACTIVITY_DECISION = qualifiedText(
            "ordinary_new_configuration_semantic_release_reference_revision",
            "business_activity_admission_decision_identifier"
    );
    private static final Field<Long> REFERENCE_TABLE_EPOCH = qualifiedLong(
            "ordinary_new_configuration_semantic_release_reference_revision",
            "reference_epoch"
    );
    private static final Field<String> REFERENCE_TABLE_PRINCIPAL = qualifiedText(
            "ordinary_new_configuration_semantic_release_reference_revision",
            "selecting_principal_identifier"
    );
    private static final Field<String> REFERENCE_TABLE_PROVENANCE = qualifiedText(
            "ordinary_new_configuration_semantic_release_reference_revision",
            "selection_provenance_reference"
    );
    private static final Field<Instant> REFERENCE_TABLE_AT = qualifiedInstant(
            "ordinary_new_configuration_semantic_release_reference_revision",
            "selected_at"
    );
    private static final Field<String> POINTER_REFERENCE_ID = qualifiedText(
            "ordinary_new_configuration_semantic_release_pointer",
            "reference_revision_identifier"
    );
    private static final Field<String> POINTER_TABLE_ID = qualifiedText(
            "ordinary_new_configuration_semantic_release_pointer", "pointer_identifier"
    );
    private static final Field<Long> POINTER_EPOCH = qualifiedLong(
            "ordinary_new_configuration_semantic_release_pointer", "reference_epoch"
    );

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;

    public JooqSemanticReleaseAdmissionAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public SemanticReleasePurposeAdmissionDecision recordDecision(
            RecordSemanticReleasePurposeAdmissionDecisionCommand command
    ) {
        Objects.requireNonNull(command, "command");
        SemanticReleasePurposeAdmissionDecision result = transactionTemplate
                .execute(status -> recordDecisionInsideTransaction(command));
        return Objects.requireNonNull(result, "Decision transaction returned no result");
    }

    @Override
    public Optional<SemanticReleasePurposeAdmissionDecision> currentDecision(
            String semanticRegistryReleaseIdentifier,
            SemanticReleasePurpose purpose
    ) {
        requireIdentifier(semanticRegistryReleaseIdentifier, "Semantic Registry Release identifier");
        Objects.requireNonNull(purpose, "purpose");
        Record row = dsl.select(
                        DECISION_TABLE_ID,
                        DECISION_TABLE_RELEASE,
                        DECISION_TABLE_PURPOSE,
                        DECISION_TABLE_DISPOSITION,
                        DECISION_TABLE_PRINCIPAL,
                        DECISION_TABLE_PROVENANCE,
                        DECISION_TABLE_AT
                )
                .from(CURRENT)
                .join(DECISIONS).on(
                        CURRENT_DECISION_ID.eq(DECISION_TABLE_ID)
                )
                .where(CURRENT_RELEASE.eq(semanticRegistryReleaseIdentifier))
                .and(CURRENT_PURPOSE.eq(purpose.name()))
                .fetchOne();
        return Optional.ofNullable(row).map(JooqSemanticReleaseAdmissionAuthority::decision);
    }

    @Override
    public OrdinaryNewConfigurationSemanticReleaseReference advanceOrdinaryReference(
            AdvanceOrdinarySemanticReleaseCommand command
    ) {
        Objects.requireNonNull(command, "command");
        OrdinaryNewConfigurationSemanticReleaseReference result =
                transactionTemplate.execute(
                        status -> advanceInsideTransaction(command)
                );
        return Objects.requireNonNull(result, "Reference transaction returned no result");
    }

    @Override
    public Optional<OrdinaryNewConfigurationSemanticReleaseReference>
    currentOrdinaryReference() {
        Record row = dsl.select(referenceFields())
                .from(POINTER)
                .join(REFERENCES).on(
                        POINTER_REFERENCE_ID.eq(REFERENCE_TABLE_ID)
                )
                .where(POINTER_TABLE_ID.eq("ORDINARY"))
                .fetchOne();
        return Optional.ofNullable(row).map(
                JooqSemanticReleaseAdmissionAuthority::reference
        );
    }

    private SemanticReleasePurposeAdmissionDecision recordDecisionInsideTransaction(
            RecordSemanticReleasePurposeAdmissionDecisionCommand command
    ) {
        SemanticReleasePurposeAdmissionDecision requested = command.decision();
        Optional<SemanticReleasePurposeAdmissionDecision> existing = decisionById(
                requested.admissionDecisionIdentifier()
        );
        if (existing.isPresent()) {
            return requireSameDecision(requested, existing.orElseThrow());
        }

        lockReleasePurpose(requested.semanticRegistryReleaseIdentifier(), requested.purpose());
        existing = decisionById(requested.admissionDecisionIdentifier());
        if (existing.isPresent()) {
            return requireSameDecision(requested, existing.orElseThrow());
        }

        try {
            dsl.insertInto(DECISIONS)
                    .columns(
                            DECISION_ID, RELEASE_ID, PURPOSE, DISPOSITION,
                            PRINCIPAL_ID, PROVENANCE, DECIDED_AT
                    )
                    .values(
                            requested.admissionDecisionIdentifier(),
                            requested.semanticRegistryReleaseIdentifier(),
                            requested.purpose().name(),
                            requested.disposition().name(),
                            requested.decidingPrincipalIdentifier(),
                            requested.decisionProvenanceReference(),
                            requested.decidedAt()
                    )
                    .execute();
            dsl.insertInto(CURRENT)
                    .columns(RELEASE_ID, PURPOSE, DECISION_ID)
                    .values(
                            requested.semanticRegistryReleaseIdentifier(),
                            requested.purpose().name(),
                            requested.admissionDecisionIdentifier()
                    )
                    .onConflict(RELEASE_ID, PURPOSE)
                    .doUpdate()
                    .set(DECISION_ID, requested.admissionDecisionIdentifier())
                    .execute();
            return requested;
        } catch (DataAccessException failure) {
            throw persistenceFailure("Could not record release-purpose admission decision", failure);
        }
    }

    private OrdinaryNewConfigurationSemanticReleaseReference advanceInsideTransaction(
            AdvanceOrdinarySemanticReleaseCommand command
    ) {
        Optional<OrdinaryNewConfigurationSemanticReleaseReference> existing =
                referenceById(command.referenceRevisionIdentifier());
        if (existing.isPresent()) {
            return requireSameReference(command, existing.orElseThrow());
        }

        lockOrdinaryPointer();
        existing = referenceById(command.referenceRevisionIdentifier());
        if (existing.isPresent()) {
            return requireSameReference(command, existing.orElseThrow());
        }

        Long currentEpoch = dsl.select(POINTER_EPOCH)
                .from(POINTER)
                .where(POINTER_TABLE_ID.eq("ORDINARY"))
                .forUpdate()
                .fetchOne(POINTER_EPOCH);
        long epoch = currentEpoch == null ? 0L : currentEpoch;
        if (command.expectedReferenceEpoch() != epoch) {
            throw failure(
                    SemanticReleaseAdmissionFailureCategory.REFERENCE_EPOCH_CONFLICT,
                    "Ordinary release reference epoch changed"
            );
        }

        var currentDecisions = dsl.select(
                        CURRENT_PURPOSE,
                        CURRENT_DECISION_ID
                )
                .from(CURRENT)
                .where(CURRENT_RELEASE.eq(
                        command.semanticRegistryReleaseIdentifier()
                ))
                .and(CURRENT_PURPOSE.in(
                        SemanticReleasePurpose.NEW_BUSINESS_ACTIVITY.name(),
                        SemanticReleasePurpose.NEW_CONFIGURATION_VALIDATION.name()
                ))
                .orderBy(CURRENT_PURPOSE)
                .forUpdate()
                .fetch();
        if (currentDecisions.size() != 2) {
            throw notAdmittedForBoth();
        }

        SemanticReleasePurposeAdmissionDecision validation = lockedCurrentDecision(
                command.semanticRegistryReleaseIdentifier(),
                SemanticReleasePurpose.NEW_CONFIGURATION_VALIDATION
        );
        SemanticReleasePurposeAdmissionDecision activity = lockedCurrentDecision(
                command.semanticRegistryReleaseIdentifier(),
                SemanticReleasePurpose.NEW_BUSINESS_ACTIVITY
        );
        if (!validation.admitted() || !activity.admitted()) {
            throw notAdmittedForBoth();
        }

        OrdinaryNewConfigurationSemanticReleaseReference requested =
                new OrdinaryNewConfigurationSemanticReleaseReference(
                        command.referenceRevisionIdentifier(),
                        command.semanticRegistryReleaseIdentifier(),
                        validation.admissionDecisionIdentifier(),
                        activity.admissionDecisionIdentifier(),
                        epoch + 1,
                        command.selectingPrincipalIdentifier(),
                        command.selectionProvenanceReference(),
                        command.selectedAt()
                );
        try {
            dsl.insertInto(REFERENCES)
                    .columns(
                            REFERENCE_ID, RELEASE_ID, VALIDATION_DECISION_ID,
                            ACTIVITY_DECISION_ID, REFERENCE_EPOCH,
                            SELECTING_PRINCIPAL_ID, SELECTION_PROVENANCE,
                            SELECTED_AT
                    )
                    .values(
                            requested.referenceRevisionIdentifier(),
                            requested.semanticRegistryReleaseIdentifier(),
                            requested.validationAdmissionDecisionIdentifier(),
                            requested.businessActivityAdmissionDecisionIdentifier(),
                            requested.referenceEpoch(),
                            requested.selectingPrincipalIdentifier(),
                            requested.selectionProvenanceReference(),
                            requested.selectedAt()
                    )
                    .execute();
            dsl.insertInto(POINTER)
                    .columns(POINTER_ID, REFERENCE_EPOCH, REFERENCE_ID)
                    .values("ORDINARY", requested.referenceEpoch(), requested.referenceRevisionIdentifier())
                    .onConflict(POINTER_ID)
                    .doUpdate()
                    .set(REFERENCE_EPOCH, requested.referenceEpoch())
                    .set(REFERENCE_ID, requested.referenceRevisionIdentifier())
                    .execute();
            return requested;
        } catch (DataAccessException failure) {
            throw persistenceFailure("Could not advance ordinary semantic-release reference", failure);
        }
    }

    private SemanticReleasePurposeAdmissionDecision lockedCurrentDecision(
            String release,
            SemanticReleasePurpose purpose
    ) {
        Record row = dsl.select(
                        DECISION_TABLE_ID, DECISION_TABLE_RELEASE,
                        DECISION_TABLE_PURPOSE, DECISION_TABLE_DISPOSITION,
                        DECISION_TABLE_PRINCIPAL, DECISION_TABLE_PROVENANCE,
                        DECISION_TABLE_AT
                )
                .from(CURRENT)
                .join(DECISIONS).on(
                        CURRENT_DECISION_ID.eq(DECISION_TABLE_ID)
                )
                .where(CURRENT_RELEASE.eq(release))
                .and(CURRENT_PURPOSE.eq(purpose.name()))
                .fetchOne();
        if (row == null) {
            throw notAdmittedForBoth();
        }
        return decision(row);
    }

    private Optional<SemanticReleasePurposeAdmissionDecision> decisionById(String id) {
        return dsl.select(
                        DECISION_TABLE_ID, DECISION_TABLE_RELEASE,
                        DECISION_TABLE_PURPOSE, DECISION_TABLE_DISPOSITION,
                        DECISION_TABLE_PRINCIPAL, DECISION_TABLE_PROVENANCE,
                        DECISION_TABLE_AT
                )
                .from(DECISIONS)
                .where(DECISION_ID.eq(id))
                .fetchOptional()
                .map(JooqSemanticReleaseAdmissionAuthority::decision);
    }

    private Optional<OrdinaryNewConfigurationSemanticReleaseReference> referenceById(
            String id
    ) {
        return dsl.select(referenceFields())
                .from(REFERENCES)
                .where(REFERENCE_ID.eq(id))
                .fetchOptional()
                .map(JooqSemanticReleaseAdmissionAuthority::reference);
    }

    private void lockReleasePurpose(String release, SemanticReleasePurpose purpose) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 77))",
                release + "\u001f" + purpose.name()
        );
        dsl.select(CURRENT_DECISION_ID)
                .from(CURRENT)
                .where(CURRENT_RELEASE.eq(release))
                .and(CURRENT_PURPOSE.eq(purpose.name()))
                .forUpdate()
                .fetch();
    }

    private void lockOrdinaryPointer() {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 78))",
                "ORDINARY"
        );
    }

    private static SemanticReleasePurposeAdmissionDecision decision(Record row) {
        return new SemanticReleasePurposeAdmissionDecision(
                row.get(DECISION_TABLE_ID), row.get(DECISION_TABLE_RELEASE),
                SemanticReleasePurpose.valueOf(row.get(DECISION_TABLE_PURPOSE)),
                SemanticReleaseAdmissionDisposition.valueOf(row.get(DECISION_TABLE_DISPOSITION)),
                row.get(DECISION_TABLE_PRINCIPAL), row.get(DECISION_TABLE_PROVENANCE),
                row.get(DECISION_TABLE_AT)
        );
    }

    private static OrdinaryNewConfigurationSemanticReleaseReference reference(Record row) {
        return new OrdinaryNewConfigurationSemanticReleaseReference(
                row.get(REFERENCE_TABLE_ID), row.get(REFERENCE_TABLE_RELEASE),
                row.get(REFERENCE_TABLE_VALIDATION_DECISION),
                row.get(REFERENCE_TABLE_ACTIVITY_DECISION),
                row.get(REFERENCE_TABLE_EPOCH), row.get(REFERENCE_TABLE_PRINCIPAL),
                row.get(REFERENCE_TABLE_PROVENANCE), row.get(REFERENCE_TABLE_AT)
        );
    }

    private static OrdinaryNewConfigurationSemanticReleaseReference requireSameReference(
            AdvanceOrdinarySemanticReleaseCommand command,
            OrdinaryNewConfigurationSemanticReleaseReference existing
    ) {
        boolean same = command.referenceRevisionIdentifier().equals(existing.referenceRevisionIdentifier())
                && command.semanticRegistryReleaseIdentifier().equals(existing.semanticRegistryReleaseIdentifier())
                && command.expectedReferenceEpoch() + 1 == existing.referenceEpoch()
                && command.selectingPrincipalIdentifier().equals(existing.selectingPrincipalIdentifier())
                && command.selectionProvenanceReference().equals(existing.selectionProvenanceReference())
                && command.selectedAt().equals(existing.selectedAt());
        if (!same) {
            throw failure(
                    SemanticReleaseAdmissionFailureCategory.REFERENCE_IDENTITY_CONFLICT,
                    "Reference revision identity was reused for different intent"
            );
        }
        return existing;
    }

    private static SemanticReleasePurposeAdmissionDecision requireSameDecision(
            SemanticReleasePurposeAdmissionDecision requested,
            SemanticReleasePurposeAdmissionDecision existing
    ) {
        if (!requested.equals(existing)) {
            throw failure(
                    SemanticReleaseAdmissionFailureCategory.DECISION_IDENTITY_CONFLICT,
                    "Admission decision identity was reused for different intent"
            );
        }
        return existing;
    }

    private static SemanticReleaseAdmissionPersistenceException notAdmittedForBoth() {
        return failure(
                SemanticReleaseAdmissionFailureCategory.RELEASE_NOT_ADMITTED_FOR_BOTH_PURPOSES,
                "Selected release is not currently admitted for both governed purposes"
        );
    }

    private static org.jooq.SelectFieldOrAsterisk[] referenceFields() {
        return new org.jooq.SelectFieldOrAsterisk[]{
                REFERENCE_TABLE_ID, REFERENCE_TABLE_RELEASE,
                REFERENCE_TABLE_VALIDATION_DECISION,
                REFERENCE_TABLE_ACTIVITY_DECISION,
                REFERENCE_TABLE_EPOCH, REFERENCE_TABLE_PRINCIPAL,
                REFERENCE_TABLE_PROVENANCE, REFERENCE_TABLE_AT
        };
    }

    private static Table<?> table(String name) {
        return DSL.table(DSL.name(name));
    }

    private static Field<String> text(String name) {
        return DSL.field(DSL.name(name), String.class);
    }

    private static Field<Instant> instant(String name) {
        return DSL.field(DSL.name(name), Instant.class);
    }

    private static Field<String> qualifiedText(String table, String name) {
        return DSL.field(DSL.name(table, name), String.class);
    }

    private static Field<Instant> qualifiedInstant(String table, String name) {
        return DSL.field(DSL.name(table, name), Instant.class);
    }

    private static Field<Long> qualifiedLong(String table, String name) {
        return DSL.field(DSL.name(table, name), Long.class);
    }

    private static SemanticReleaseAdmissionPersistenceException failure(
            SemanticReleaseAdmissionFailureCategory category,
            String message
    ) {
        return new SemanticReleaseAdmissionPersistenceException(category, message);
    }

    private static SemanticReleaseAdmissionPersistenceException persistenceFailure(
            String message,
            Throwable cause
    ) {
        return new SemanticReleaseAdmissionPersistenceException(
                SemanticReleaseAdmissionFailureCategory.PERSISTENCE_FAILURE,
                message,
                cause
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
