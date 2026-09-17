package grandrue.infrastructure.persistence.configuration;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.configuration.ConfigurationImpactAnalysisResult;
import mainstreet.semantic.configuration.ConfigurationImpactClassification;
import mainstreet.semantic.configuration.ConfigurationImpactFinding;
import mainstreet.semantic.configuration.ConfigurationImpactReviewEvidence;
import mainstreet.semantic.configuration.ConfigurationImpactReviewEvidenceAuthority;
import mainstreet.semantic.configuration.ConfigurationImpactReviewEvidenceFailureCategory;
import mainstreet.semantic.configuration.ConfigurationImpactReviewEvidencePersistenceException;
import mainstreet.semantic.configuration.ConfigurationValidationEvidence;
import mainstreet.semantic.configuration.ConfigurationValidationEvidenceAuthority;
import mainstreet.semantic.configuration.ConfigurationValidationOutcome;
import mainstreet.semantic.configuration.RecordConfigurationImpactReviewEvidenceCommand;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * PostgreSQL/jOOQ authority for immutable exact impact-review evidence.
 *
 * Authority: composite MS-PROT-040 through v1.8, especially v1.8 §§5–6.
 */
public final class JooqConfigurationImpactReviewEvidenceAuthority
        implements ConfigurationImpactReviewEvidenceAuthority {

    private static final Table<?> EVIDENCE = DSL.table(
            DSL.name("configuration_impact_review_evidence")
    );
    private static final Table<?> EFFECT = DSL.table(
            DSL.name("configuration_impact_review_effect")
    );
    private static final Table<?> FINDING = DSL.table(
            DSL.name("configuration_impact_review_finding")
    );
    private static final Field<String> MERCHANT_ID = DSL.field(
            DSL.name("merchant_identifier"),
            String.class
    );
    private static final Field<String> IMPACT_EVIDENCE_ID = DSL.field(
            DSL.name("impact_review_evidence_identifier"),
            String.class
    );
    private static final Field<String> REVISION_ID = DSL.field(
            DSL.name("configuration_revision_identifier"),
            String.class
    );
    private static final Field<String> SEMANTIC_RELEASE_ID = DSL.field(
            DSL.name("semantic_registry_release_identifier"),
            String.class
    );
    private static final Field<String> VALIDATION_EVIDENCE_ID = DSL.field(
            DSL.name("validation_evidence_identifier"),
            String.class
    );
    private static final Field<String> PACKAGE_EVIDENCE_ID = DSL.field(
            DSL.name("resolved_package_evidence_identifier"),
            String.class
    );
    private static final Field<Instant> IMPACT_COMPLETED_AT = DSL.field(
            DSL.name("impact_analysis_completed_at"),
            Instant.class
    );
    private static final Field<String> REINSTATEMENT_BASIS_ID = DSL.field(
            DSL.name("reinstatement_basis_activation_request_identifier"),
            String.class
    );
    private static final Field<Integer> EFFECT_SEQUENCE = DSL.field(
            DSL.name("effect_sequence"),
            Integer.class
    );
    private static final Field<String> EFFECT_CONTENT = DSL.field(
            DSL.name("business_facing_effect"),
            String.class
    );
    private static final Field<Integer> FINDING_SEQUENCE = DSL.field(
            DSL.name("finding_sequence"),
            Integer.class
    );
    private static final Field<String> FINDING_CLASSIFICATION = DSL.field(
            DSL.name("impact_classification"),
            String.class
    );
    private static final Field<String> FINDING_CONTENT = DSL.field(
            DSL.name("business_facing_finding"),
            String.class
    );

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;
    private final ConfigurationValidationEvidenceAuthority
            validationEvidenceAuthority;

    public JooqConfigurationImpactReviewEvidenceAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            ConfigurationValidationEvidenceAuthority
                    validationEvidenceAuthority
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
        this.validationEvidenceAuthority = Objects.requireNonNull(
                validationEvidenceAuthority,
                "validationEvidenceAuthority"
        );
    }

    @Override
    public ConfigurationImpactReviewEvidence recordCompletedReview(
            RecordConfigurationImpactReviewEvidenceCommand command
    ) {
        Objects.requireNonNull(command, "command");
        ConfigurationImpactReviewEvidence result = transactionTemplate.execute(
                status -> recordInsideTransaction(command)
        );
        return Objects.requireNonNull(
                result,
                "Impact-review evidence transaction returned no result"
        );
    }

    @Override
    public Optional<ConfigurationImpactReviewEvidence> evidence(
            MerchantScope merchantScope,
            String impactReviewEvidenceIdentifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(
                impactReviewEvidenceIdentifier,
                "Impact-review evidence identifier"
        );
        String merchantIdentifier = merchantScope.merchantIdentifier();
        Record row = dsl.select(
                        IMPACT_EVIDENCE_ID,
                        MERCHANT_ID,
                        REVISION_ID,
                        SEMANTIC_RELEASE_ID,
                        VALIDATION_EVIDENCE_ID,
                        PACKAGE_EVIDENCE_ID,
                        IMPACT_COMPLETED_AT,
                        REINSTATEMENT_BASIS_ID
                )
                .from(EVIDENCE)
                .where(MERCHANT_ID.eq(merchantIdentifier))
                .and(IMPACT_EVIDENCE_ID.eq(impactReviewEvidenceIdentifier))
                .fetchOne();
        if (row == null) {
            return Optional.empty();
        }
        List<String> effects = dsl.select(EFFECT_CONTENT)
                .from(EFFECT)
                .where(MERCHANT_ID.eq(merchantIdentifier))
                .and(IMPACT_EVIDENCE_ID.eq(impactReviewEvidenceIdentifier))
                .orderBy(EFFECT_SEQUENCE)
                .fetch(EFFECT_CONTENT);
        List<ConfigurationImpactFinding> findings = dsl.select(
                        FINDING_CLASSIFICATION,
                        FINDING_CONTENT
                )
                .from(FINDING)
                .where(MERCHANT_ID.eq(merchantIdentifier))
                .and(IMPACT_EVIDENCE_ID.eq(impactReviewEvidenceIdentifier))
                .orderBy(FINDING_SEQUENCE)
                .fetch(finding -> new ConfigurationImpactFinding(
                        ConfigurationImpactClassification.valueOf(
                                finding.get(FINDING_CLASSIFICATION)
                        ),
                        finding.get(FINDING_CONTENT)
                ));
        return Optional.of(new ConfigurationImpactReviewEvidence(
                row.get(IMPACT_EVIDENCE_ID),
                row.get(MERCHANT_ID),
                row.get(REVISION_ID),
                row.get(SEMANTIC_RELEASE_ID),
                row.get(VALIDATION_EVIDENCE_ID),
                row.get(PACKAGE_EVIDENCE_ID),
                effects,
                findings,
                row.get(IMPACT_COMPLETED_AT),
                Optional.ofNullable(row.get(REINSTATEMENT_BASIS_ID))
        ));
    }

    private ConfigurationImpactReviewEvidence recordInsideTransaction(
            RecordConfigurationImpactReviewEvidenceCommand command
    ) {
        ConfigurationImpactAnalysisResult analysis =
                command.impactAnalysisResult();
        MerchantScope merchantScope = new MerchantScope(
                analysis.merchantIdentifier()
        );
        Optional<ConfigurationImpactReviewEvidence> committed = evidence(
                merchantScope,
                command.impactReviewEvidenceIdentifier()
        );
        if (committed.isPresent()) {
            return requireSameIntent(command, committed.orElseThrow());
        }

        ConfigurationValidationEvidence validation =
                validationEvidenceAuthority.evidence(
                                merchantScope,
                                analysis.validationEvidenceIdentifier()
                        )
                        .orElseThrow(() -> failure(
                                ConfigurationImpactReviewEvidenceFailureCategory
                                        .VALIDATION_EVIDENCE_NOT_FOUND,
                                "Configuration Validation Evidence does not exist"
                        ));
        requireMatchingValidation(
                analysis,
                validation,
                command.reinstatementBasisActivationRequestIdentifier()
        );

        ConfigurationImpactReviewEvidence requested = toEvidence(command);
        int inserted;
        try {
            inserted = insertEvidence(requested);
            if (inserted == 1) {
                insertEffects(requested);
                insertFindings(requested);
            }
        } catch (DataAccessException persistenceFailure) {
            throw new ConfigurationImpactReviewEvidencePersistenceException(
                    ConfigurationImpactReviewEvidenceFailureCategory
                            .PERSISTENCE_FAILURE,
                    "Could not persist Configuration Impact Review Evidence",
                    persistenceFailure
            );
        }
        if (inserted == 0) {
            return evidence(
                    merchantScope,
                    command.impactReviewEvidenceIdentifier()
            ).map(existing -> requireSameIntent(command, existing))
                    .orElseThrow(() -> failure(
                            ConfigurationImpactReviewEvidenceFailureCategory
                                    .PERSISTENCE_FAILURE,
                            "Impact-review insert produced no durable result"
                    ));
        }
        return evidence(
                merchantScope,
                command.impactReviewEvidenceIdentifier()
        ).orElseThrow(() -> new IllegalStateException(
                "Configuration Impact Review Evidence was not committed"
        ));
    }

    private int insertEvidence(ConfigurationImpactReviewEvidence evidence) {
        return dsl.insertInto(EVIDENCE)
                .columns(
                        MERCHANT_ID,
                        IMPACT_EVIDENCE_ID,
                        REVISION_ID,
                        SEMANTIC_RELEASE_ID,
                        VALIDATION_EVIDENCE_ID,
                        PACKAGE_EVIDENCE_ID,
                        IMPACT_COMPLETED_AT,
                        REINSTATEMENT_BASIS_ID
                )
                .values(
                        evidence.merchantIdentifier(),
                        evidence.impactReviewEvidenceIdentifier(),
                        evidence.configurationRevisionIdentifier(),
                        evidence.semanticRegistryReleaseIdentifier(),
                        evidence.validationEvidenceIdentifier(),
                        evidence.resolvedPackageEvidenceIdentifier(),
                        evidence.impactAnalysisCompletedAt(),
                        evidence.reinstatementBasisActivationRequestIdentifier()
                                .orElse(null)
                )
                .onConflictDoNothing()
                .execute();
    }

    private void insertEffects(ConfigurationImpactReviewEvidence evidence) {
        for (int sequence = 0;
             sequence < evidence.businessFacingEffects().size();
             sequence++) {
            dsl.insertInto(EFFECT)
                    .columns(
                            MERCHANT_ID,
                            IMPACT_EVIDENCE_ID,
                            EFFECT_SEQUENCE,
                            EFFECT_CONTENT
                    )
                    .values(
                            evidence.merchantIdentifier(),
                            evidence.impactReviewEvidenceIdentifier(),
                            sequence,
                            evidence.businessFacingEffects().get(sequence)
                    )
                    .execute();
        }
    }

    private void insertFindings(ConfigurationImpactReviewEvidence evidence) {
        for (int sequence = 0;
             sequence < evidence.findings().size();
             sequence++) {
            ConfigurationImpactFinding finding =
                    evidence.findings().get(sequence);
            dsl.insertInto(FINDING)
                    .columns(
                            MERCHANT_ID,
                            IMPACT_EVIDENCE_ID,
                            FINDING_SEQUENCE,
                            FINDING_CLASSIFICATION,
                            FINDING_CONTENT
                    )
                    .values(
                            evidence.merchantIdentifier(),
                            evidence.impactReviewEvidenceIdentifier(),
                            sequence,
                            finding.classification().name(),
                            finding.businessFacingDescription()
                    )
                    .execute();
        }
    }

    private static void requireMatchingValidation(
            ConfigurationImpactAnalysisResult analysis,
            ConfigurationValidationEvidence validation,
            Optional<String> reinstatementBasisActivationRequestIdentifier
    ) {
        if (!analysis.merchantIdentifier().equals(
                validation.merchantIdentifier()
        )
                || !analysis.configurationRevisionIdentifier().equals(
                        validation.configurationRevisionIdentifier()
                )
                || !analysis.semanticRegistryReleaseIdentifier().equals(
                        validation.semanticRegistryReleaseIdentifier()
                )
                || !analysis.validationEvidenceIdentifier().equals(
                        validation.validationEvidenceIdentifier()
                )
                || !analysis.resolvedPackageEvidenceIdentifier().equals(
                        validation.resolvedPackageEvidenceIdentifier()
                )
                || !reinstatementBasisActivationRequestIdentifier.equals(
                        validation
                                .reinstatementBasisActivationRequestIdentifier()
                )
                || validation.outcome()
                        != ConfigurationValidationOutcome.SUCCEEDED) {
            throw failure(
                    ConfigurationImpactReviewEvidenceFailureCategory
                            .VALIDATION_EVIDENCE_AFFINITY_MISMATCH,
                    "Impact analysis does not match exact validation evidence"
            );
        }
    }

    private static ConfigurationImpactReviewEvidence requireSameIntent(
            RecordConfigurationImpactReviewEvidenceCommand command,
            ConfigurationImpactReviewEvidence existing
    ) {
        ConfigurationImpactReviewEvidence requested = toEvidence(command);
        if (!requested.equals(existing)) {
            throw failure(
                    ConfigurationImpactReviewEvidenceFailureCategory
                            .IMPACT_REVIEW_EVIDENCE_IDENTITY_CONFLICT,
                    "Impact-review evidence identity was reused for different intent"
            );
        }
        return existing;
    }

    private static ConfigurationImpactReviewEvidence toEvidence(
            RecordConfigurationImpactReviewEvidenceCommand command
    ) {
        ConfigurationImpactAnalysisResult analysis =
                command.impactAnalysisResult();
        return new ConfigurationImpactReviewEvidence(
                command.impactReviewEvidenceIdentifier(),
                analysis.merchantIdentifier(),
                analysis.configurationRevisionIdentifier(),
                analysis.semanticRegistryReleaseIdentifier(),
                analysis.validationEvidenceIdentifier(),
                analysis.resolvedPackageEvidenceIdentifier(),
                analysis.businessFacingEffects(),
                analysis.findings(),
                analysis.completedAt(),
                command.reinstatementBasisActivationRequestIdentifier()
        );
    }

    private static ConfigurationImpactReviewEvidencePersistenceException failure(
            ConfigurationImpactReviewEvidenceFailureCategory category,
            String message
    ) {
        return new ConfigurationImpactReviewEvidencePersistenceException(
                category,
                message
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
