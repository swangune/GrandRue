package mainstreet.infrastructure.persistence.configuration;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.configuration.ConfigurationRevisionAuthority;
import mainstreet.semantic.configuration.ConfigurationValidationEvidence;
import mainstreet.semantic.configuration.ConfigurationValidationEvidenceAuthority;
import mainstreet.semantic.configuration.ConfigurationValidationEvidenceFailureCategory;
import mainstreet.semantic.configuration.ConfigurationValidationEvidencePersistenceException;
import mainstreet.semantic.configuration.ConfigurationValidationOutcome;
import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.configuration.RecordConfigurationValidationEvidenceCommand;
import mainstreet.semantic.configuration.ResolvedConfigurationPackage;
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

/**
 * PostgreSQL/jOOQ authority for immutable exact validation/package evidence.
 * Source contents apply equally to initial and replacement revisions.
 *
 * Authority: designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md,
 * §3 Configuration Revision identity and immutability; §4 Semantic-registry affinity;
 * §5 Validation and Resolved Configuration Package production;
 * designs/MS-PROT-040 v1.8 — Configuration Reinstatement Decision Affinity Amendment.md,
 * §§5–6.
 */
public final class JooqConfigurationValidationEvidenceAuthority
        implements ConfigurationValidationEvidenceAuthority {

    private static final Table<?> EVIDENCE = DSL.table(
            DSL.name("configuration_validation_evidence")
    );
    private static final Field<String> MERCHANT_ID = DSL.field(
            DSL.name("merchant_identifier"),
            String.class
    );
    private static final Field<String> EVIDENCE_ID = DSL.field(
            DSL.name("validation_evidence_identifier"),
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
    private static final Field<String> PACKAGE_EVIDENCE_ID = DSL.field(
            DSL.name("resolved_package_evidence_identifier"),
            String.class
    );
    private static final Field<String> OUTCOME = DSL.field(
            DSL.name("validation_outcome"),
            String.class
    );
    private static final Field<String> COMPILER_ID = DSL.field(
            DSL.name("compiler_identifier"),
            String.class
    );
    private static final Field<Instant> PACKAGE_GENERATED_AT = DSL.field(
            DSL.name("package_generated_at"),
            Instant.class
    );
    private static final Field<Instant> EVIDENCE_PRODUCED_AT = DSL.field(
            DSL.name("evidence_produced_at"),
            Instant.class
    );
    private static final Field<String> REINSTATEMENT_BASIS_ID = DSL.field(
            DSL.name("reinstatement_basis_activation_request_identifier"),
            String.class
    );

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;
    private final ConfigurationRevisionAuthority revisionAuthority;

    public JooqConfigurationValidationEvidenceAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            ConfigurationRevisionAuthority revisionAuthority
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
        this.revisionAuthority = Objects.requireNonNull(
                revisionAuthority,
                "revisionAuthority"
        );
    }

    @Override
    public ConfigurationValidationEvidence recordSuccessfulValidation(
            RecordConfigurationValidationEvidenceCommand command
    ) {
        Objects.requireNonNull(command, "command");
        ConfigurationValidationEvidence result = transactionTemplate.execute(
                status -> recordInsideTransaction(command)
        );
        return Objects.requireNonNull(
                result,
                "Validation evidence transaction returned no result"
        );
    }

    @Override
    public Optional<ConfigurationValidationEvidence> evidence(
            MerchantScope merchantScope,
            String validationEvidenceIdentifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(
                validationEvidenceIdentifier,
                "Validation evidence identifier"
        );
        return Optional.ofNullable(selectFields()
                        .from(EVIDENCE)
                        .where(MERCHANT_ID.eq(
                                merchantScope.merchantIdentifier()
                        ))
                        .and(EVIDENCE_ID.eq(validationEvidenceIdentifier))
                        .fetchOne())
                .map(JooqConfigurationValidationEvidenceAuthority::toEvidence);
    }

    private ConfigurationValidationEvidence recordInsideTransaction(
            RecordConfigurationValidationEvidenceCommand command
    ) {
        ResolvedConfigurationPackage resolvedPackage =
                command.resolvedPackage();
        MerchantScope merchantScope = new MerchantScope(
                resolvedPackage.merchantIdentifier()
        );
        Optional<ConfigurationValidationEvidence> committed = evidence(
                merchantScope,
                command.validationEvidenceIdentifier()
        );
        if (committed.isPresent()) {
            return requireSameIntent(command, committed.orElseThrow());
        }

        MerchantConfiguration configuration = revisionAuthority.configuration(
                        merchantScope,
                        resolvedPackage
                                .sourceConfigurationRevisionIdentifier()
                )
                .orElseThrow(() -> failure(
                        ConfigurationValidationEvidenceFailureCategory
                                .CONFIGURATION_REVISION_NOT_FOUND,
                        "Configuration Revision does not exist"
                ));
        try {
            resolvedPackage.requireSourceConfiguration(configuration);
        } catch (IllegalArgumentException mismatch) {
            throw new ConfigurationValidationEvidencePersistenceException(
                    ConfigurationValidationEvidenceFailureCategory
                            .RESOLVED_PACKAGE_AFFINITY_MISMATCH,
                    "Resolved package does not match the stored Configuration Revision",
                    mismatch
            );
        }

        ConfigurationValidationEvidence evidence = toEvidence(command);
        int inserted;
        try {
            inserted = insert(evidence);
        } catch (DataAccessException persistenceFailure) {
            throw new ConfigurationValidationEvidencePersistenceException(
                    ConfigurationValidationEvidenceFailureCategory
                            .PERSISTENCE_FAILURE,
                    "Could not persist Configuration Validation Evidence",
                    persistenceFailure
            );
        }
        if (inserted == 0) {
            Optional<ConfigurationValidationEvidence> raced = evidence(
                    merchantScope,
                    command.validationEvidenceIdentifier()
            );
            if (raced.isPresent()) {
                return requireSameIntent(command, raced.orElseThrow());
            }
            if (packageEvidenceExists(
                    merchantScope,
                    command.resolvedPackageEvidenceIdentifier()
            )) {
                throw failure(
                        ConfigurationValidationEvidenceFailureCategory
                                .RESOLVED_PACKAGE_EVIDENCE_IDENTITY_CONFLICT,
                        "Resolved package evidence identity already exists"
                );
            }
            throw failure(
                    ConfigurationValidationEvidenceFailureCategory
                            .PERSISTENCE_FAILURE,
                    "Validation evidence insert produced no durable result"
            );
        }
        return evidence(
                merchantScope,
                command.validationEvidenceIdentifier()
        ).orElseThrow(() -> new IllegalStateException(
                "Configuration Validation Evidence was not committed"
        ));
    }

    private int insert(ConfigurationValidationEvidence evidence) {
        return dsl.insertInto(EVIDENCE)
                .columns(
                        MERCHANT_ID,
                        EVIDENCE_ID,
                        REVISION_ID,
                        SEMANTIC_RELEASE_ID,
                        PACKAGE_EVIDENCE_ID,
                        OUTCOME,
                        COMPILER_ID,
                        PACKAGE_GENERATED_AT,
                        EVIDENCE_PRODUCED_AT,
                        REINSTATEMENT_BASIS_ID
                )
                .values(
                        evidence.merchantIdentifier(),
                        evidence.validationEvidenceIdentifier(),
                        evidence.configurationRevisionIdentifier(),
                        evidence.semanticRegistryReleaseIdentifier(),
                        evidence.resolvedPackageEvidenceIdentifier(),
                        evidence.outcome().name(),
                        evidence.compilerIdentifier(),
                        evidence.packageGeneratedAt(),
                        evidence.evidenceProducedAt(),
                        evidence.reinstatementBasisActivationRequestIdentifier()
                                .orElse(null)
                )
                .onConflictDoNothing()
                .execute();
    }

    private boolean packageEvidenceExists(
            MerchantScope merchantScope,
            String resolvedPackageEvidenceIdentifier
    ) {
        return dsl.fetchExists(
                DSL.selectOne()
                        .from(EVIDENCE)
                        .where(MERCHANT_ID.eq(
                                merchantScope.merchantIdentifier()
                        ))
                        .and(PACKAGE_EVIDENCE_ID.eq(
                                resolvedPackageEvidenceIdentifier
                        ))
        );
    }

    private static ConfigurationValidationEvidence requireSameIntent(
            RecordConfigurationValidationEvidenceCommand command,
            ConfigurationValidationEvidence existing
    ) {
        ConfigurationValidationEvidence requested = toEvidence(command);
        if (!requested.equals(existing)) {
            throw failure(
                    ConfigurationValidationEvidenceFailureCategory
                            .VALIDATION_EVIDENCE_IDENTITY_CONFLICT,
                    "Validation evidence identity was reused for different intent"
            );
        }
        return existing;
    }

    private static ConfigurationValidationEvidence toEvidence(
            RecordConfigurationValidationEvidenceCommand command
    ) {
        ResolvedConfigurationPackage resolvedPackage =
                command.resolvedPackage();
        return new ConfigurationValidationEvidence(
                command.validationEvidenceIdentifier(),
                resolvedPackage.merchantIdentifier(),
                resolvedPackage.sourceConfigurationRevisionIdentifier(),
                resolvedPackage.semanticRegistryReleaseIdentifier(),
                command.resolvedPackageEvidenceIdentifier(),
                ConfigurationValidationOutcome.SUCCEEDED,
                resolvedPackage.provenance().compilerIdentifier(),
                resolvedPackage.provenance().generatedAt(),
                command.evidenceProducedAt(),
                command.reinstatementBasisActivationRequestIdentifier()
        );
    }

    private org.jooq.SelectSelectStep<? extends Record> selectFields() {
        return dsl.select(
                EVIDENCE_ID,
                MERCHANT_ID,
                REVISION_ID,
                SEMANTIC_RELEASE_ID,
                PACKAGE_EVIDENCE_ID,
                OUTCOME,
                COMPILER_ID,
                PACKAGE_GENERATED_AT,
                EVIDENCE_PRODUCED_AT,
                REINSTATEMENT_BASIS_ID
        );
    }

    private static ConfigurationValidationEvidence toEvidence(Record row) {
        return new ConfigurationValidationEvidence(
                row.get(EVIDENCE_ID),
                row.get(MERCHANT_ID),
                row.get(REVISION_ID),
                row.get(SEMANTIC_RELEASE_ID),
                row.get(PACKAGE_EVIDENCE_ID),
                ConfigurationValidationOutcome.valueOf(row.get(OUTCOME)),
                row.get(COMPILER_ID),
                row.get(PACKAGE_GENERATED_AT),
                row.get(EVIDENCE_PRODUCED_AT),
                Optional.ofNullable(row.get(REINSTATEMENT_BASIS_ID))
        );
    }

    private static ConfigurationValidationEvidencePersistenceException failure(
            ConfigurationValidationEvidenceFailureCategory category,
            String message
    ) {
        return new ConfigurationValidationEvidencePersistenceException(
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
