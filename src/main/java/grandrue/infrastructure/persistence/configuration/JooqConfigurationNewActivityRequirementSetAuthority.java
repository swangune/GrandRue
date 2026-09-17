package grandrue.infrastructure.persistence.configuration;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.configuration.ConfigurationNewActivityRequirementResolver;
import mainstreet.semantic.configuration.ConfigurationNewActivityRequirementSet;
import mainstreet.semantic.configuration.ConfigurationNewActivityRequirementSetAuthority;
import mainstreet.semantic.configuration.ConfigurationNewActivityRequirementSetFailureCategory;
import mainstreet.semantic.configuration.ConfigurationNewActivityRequirementSetIdentity;
import mainstreet.semantic.configuration.ConfigurationNewActivityRequirementSetPersistenceException;
import mainstreet.semantic.configuration.RecordConfigurationNewActivityRequirementSetCommand;
import mainstreet.semantic.configuration.ResolvedConfigurationPackage;
import mainstreet.semantic.execution.ExecutableSupportRequirement;
import mainstreet.semantic.execution.SemanticExecutionContractReference;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** PostgreSQL/jOOQ authority for immutable normalized D5c1 evidence. */
public final class JooqConfigurationNewActivityRequirementSetAuthority
        implements ConfigurationNewActivityRequirementSetAuthority {

    private static final Table<?> SETS = DSL.table(DSL.name(
            "configuration_new_activity_requirement_set"
    ));
    private static final Table<?> REQUIREMENTS = DSL.table(DSL.name(
            "configuration_new_activity_requirement"
    ));
    private static final Table<?> REQUIRED_CONTRACTS = DSL.table(DSL.name(
            "configuration_new_activity_required_contract"
    ));
    private static final Table<?> VALIDATION = DSL.table(DSL.name(
            "configuration_validation_evidence"
    ));

    private static final Field<String> MERCHANT_ID = text(
            "merchant_identifier"
    );
    private static final Field<String> PACKAGE_ID = text(
            "resolved_package_evidence_identifier"
    );
    private static final Field<String> REVISION_ID = text(
            "configuration_revision_identifier"
    );
    private static final Field<String> RELEASE_ID = text(
            "semantic_registry_release_identifier"
    );
    private static final Field<Integer> CANONICALIZATION_VERSION = DSL.field(
            DSL.name("canonicalization_version"),
            Integer.class
    );
    private static final Field<String> REQUIREMENT_SET_ID = text(
            "requirement_set_identifier"
    );
    private static final Field<Instant> EVIDENCE_PRODUCED_AT = DSL.field(
            DSL.name("evidence_produced_at"),
            Instant.class
    );
    private static final Field<String> AFFECTED_RELEASE_ID = text(
            "affected_release_identifier"
    );
    private static final Field<String> AFFECTED_CONTRACT_ID = text(
            "affected_contract_identifier"
    );
    private static final Field<String> REQUIRED_RELEASE_ID = text(
            "required_release_identifier"
    );
    private static final Field<String> REQUIRED_CONTRACT_ID = text(
            "required_contract_identifier"
    );

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;
    private final ConfigurationNewActivityRequirementResolver resolver;

    public JooqConfigurationNewActivityRequirementSetAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            ConfigurationNewActivityRequirementResolver resolver
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
        this.resolver = Objects.requireNonNull(resolver, "resolver");
    }

    @Override
    public ConfigurationNewActivityRequirementSet record(
            RecordConfigurationNewActivityRequirementSetCommand command
    ) {
        Objects.requireNonNull(command, "command");
        ConfigurationNewActivityRequirementSet result =
                transactionTemplate.execute(
                        status -> recordInsideTransaction(command)
                );
        return Objects.requireNonNull(
                result,
                "Requirement-set transaction returned no result"
        );
    }

    @Override
    public Optional<ConfigurationNewActivityRequirementSet> evidenceForPackage(
            MerchantScope merchantScope,
            String resolvedPackageEvidenceIdentifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(
                resolvedPackageEvidenceIdentifier,
                "Resolved package evidence identifier"
        );
        Record header = dsl.select(
                        MERCHANT_ID,
                        REVISION_ID,
                        RELEASE_ID,
                        PACKAGE_ID,
                        CANONICALIZATION_VERSION,
                        REQUIREMENT_SET_ID,
                        EVIDENCE_PRODUCED_AT
                )
                .from(SETS)
                .where(MERCHANT_ID.eq(merchantScope.merchantIdentifier()))
                .and(PACKAGE_ID.eq(resolvedPackageEvidenceIdentifier))
                .fetchOne();
        if (header == null) {
            return Optional.empty();
        }
        return Optional.of(toEvidence(header, loadRequirements(
                merchantScope.merchantIdentifier(),
                resolvedPackageEvidenceIdentifier
        )));
    }

    private ConfigurationNewActivityRequirementSet recordInsideTransaction(
            RecordConfigurationNewActivityRequirementSetCommand command
    ) {
        ResolvedConfigurationPackage resolvedPackage = command.resolvedPackage();
        requireValidatedPackage(command);

        Set<ExecutableSupportRequirement> requirements;
        try {
            requirements = Set.copyOf(resolver.resolve(resolvedPackage));
        } catch (RuntimeException resolutionFailure) {
            throw new ConfigurationNewActivityRequirementSetPersistenceException(
                    ConfigurationNewActivityRequirementSetFailureCategory
                            .REQUIREMENT_RESOLUTION_FAILED,
                    "Could not derive complete release-affined execution requirements",
                    resolutionFailure
            );
        }
        ConfigurationNewActivityRequirementSet requested = newEvidence(
                command,
                requirements
        );
        MerchantScope merchantScope = new MerchantScope(
                resolvedPackage.merchantIdentifier()
        );
        Optional<ConfigurationNewActivityRequirementSet> committed =
                evidenceForPackage(
                        merchantScope,
                        command.resolvedPackageEvidenceIdentifier()
                );
        if (committed.isPresent()) {
            return requireSameIntent(requested, committed.orElseThrow());
        }

        try {
            int inserted = insertHeader(requested);
            if (inserted == 0) {
                return evidenceForPackage(
                        merchantScope,
                        command.resolvedPackageEvidenceIdentifier()
                ).map(existing -> requireSameIntent(requested, existing))
                        .orElseThrow(() -> failure(
                                ConfigurationNewActivityRequirementSetFailureCategory
                                        .PERSISTENCE_FAILURE,
                                "Requirement-set insert produced no durable result"
                        ));
            }
            insertRequirements(requested);
        } catch (DataAccessException persistenceFailure) {
            throw new ConfigurationNewActivityRequirementSetPersistenceException(
                    ConfigurationNewActivityRequirementSetFailureCategory
                            .PERSISTENCE_FAILURE,
                    "Could not persist Configuration new-activity requirements",
                    persistenceFailure
            );
        }
        return evidenceForPackage(
                merchantScope,
                command.resolvedPackageEvidenceIdentifier()
        ).orElseThrow(() -> new IllegalStateException(
                "Configuration new-activity requirement evidence was not committed"
        ));
    }

    private void requireValidatedPackage(
            RecordConfigurationNewActivityRequirementSetCommand command
    ) {
        ResolvedConfigurationPackage resolvedPackage = command.resolvedPackage();
        boolean exists = dsl.fetchExists(
                DSL.selectOne()
                        .from(VALIDATION)
                        .where(MERCHANT_ID.eq(
                                resolvedPackage.merchantIdentifier()
                        ))
                        .and(REVISION_ID.eq(
                                resolvedPackage
                                        .sourceConfigurationRevisionIdentifier()
                        ))
                        .and(RELEASE_ID.eq(
                                resolvedPackage
                                        .semanticRegistryReleaseIdentifier()
                        ))
                        .and(PACKAGE_ID.eq(
                                command.resolvedPackageEvidenceIdentifier()
                        ))
        );
        if (!exists) {
            throw failure(
                    ConfigurationNewActivityRequirementSetFailureCategory
                            .VALIDATION_PACKAGE_EVIDENCE_NOT_FOUND,
                    "Exact successful validation/package evidence does not exist"
            );
        }
    }

    private int insertHeader(
            ConfigurationNewActivityRequirementSet evidence
    ) {
        return dsl.insertInto(SETS)
                .columns(
                        MERCHANT_ID,
                        PACKAGE_ID,
                        REVISION_ID,
                        RELEASE_ID,
                        CANONICALIZATION_VERSION,
                        REQUIREMENT_SET_ID,
                        EVIDENCE_PRODUCED_AT
                )
                .values(
                        evidence.merchantIdentifier(),
                        evidence.resolvedPackageEvidenceIdentifier(),
                        evidence.configurationRevisionIdentifier(),
                        evidence.semanticRegistryReleaseIdentifier(),
                        evidence.canonicalizationVersion(),
                        evidence.requirementSetIdentifier(),
                        evidence.evidenceProducedAt()
                )
                .onConflictDoNothing()
                .execute();
    }

    private void insertRequirements(
            ConfigurationNewActivityRequirementSet evidence
    ) {
        for (ExecutableSupportRequirement requirement
                : evidence.requirements()) {
            SemanticExecutionContractReference affected =
                    requirement.affectedExecutionContract();
            dsl.insertInto(REQUIREMENTS)
                    .columns(
                            MERCHANT_ID,
                            PACKAGE_ID,
                            REQUIREMENT_SET_ID,
                            RELEASE_ID,
                            AFFECTED_RELEASE_ID,
                            AFFECTED_CONTRACT_ID
                    )
                    .values(
                            evidence.merchantIdentifier(),
                            evidence.resolvedPackageEvidenceIdentifier(),
                            evidence.requirementSetIdentifier(),
                            evidence.semanticRegistryReleaseIdentifier(),
                            affected.semanticRegistryReleaseIdentifier(),
                            affected.contractIdentifier()
                    )
                    .execute();
            for (SemanticExecutionContractReference required
                    : requirement.requiredParticipantAndEffectContracts()) {
                dsl.insertInto(REQUIRED_CONTRACTS)
                        .columns(
                                MERCHANT_ID,
                                PACKAGE_ID,
                                REQUIREMENT_SET_ID,
                                RELEASE_ID,
                                AFFECTED_RELEASE_ID,
                                AFFECTED_CONTRACT_ID,
                                REQUIRED_RELEASE_ID,
                                REQUIRED_CONTRACT_ID
                        )
                        .values(
                                evidence.merchantIdentifier(),
                                evidence.resolvedPackageEvidenceIdentifier(),
                                evidence.requirementSetIdentifier(),
                                evidence.semanticRegistryReleaseIdentifier(),
                                affected.semanticRegistryReleaseIdentifier(),
                                affected.contractIdentifier(),
                                required.semanticRegistryReleaseIdentifier(),
                                required.contractIdentifier()
                        )
                        .execute();
            }
        }
    }

    private Set<ExecutableSupportRequirement> loadRequirements(
            String merchantIdentifier,
            String packageIdentifier
    ) {
        Map<SemanticExecutionContractReference,
                Set<SemanticExecutionContractReference>> grouped =
                new LinkedHashMap<>();
        dsl.select(AFFECTED_RELEASE_ID, AFFECTED_CONTRACT_ID)
                .from(REQUIREMENTS)
                .where(MERCHANT_ID.eq(merchantIdentifier))
                .and(PACKAGE_ID.eq(packageIdentifier))
                .orderBy(AFFECTED_RELEASE_ID, AFFECTED_CONTRACT_ID)
                .fetch()
                .forEach(row -> grouped.put(
                        reference(
                                row.get(AFFECTED_RELEASE_ID),
                                row.get(AFFECTED_CONTRACT_ID)
                        ),
                        new LinkedHashSet<>()
                ));
        dsl.select(
                        AFFECTED_RELEASE_ID,
                        AFFECTED_CONTRACT_ID,
                        REQUIRED_RELEASE_ID,
                        REQUIRED_CONTRACT_ID
                )
                .from(REQUIRED_CONTRACTS)
                .where(MERCHANT_ID.eq(merchantIdentifier))
                .and(PACKAGE_ID.eq(packageIdentifier))
                .orderBy(
                        AFFECTED_RELEASE_ID,
                        AFFECTED_CONTRACT_ID,
                        REQUIRED_RELEASE_ID,
                        REQUIRED_CONTRACT_ID
                )
                .fetch()
                .forEach(row -> grouped.get(reference(
                                row.get(AFFECTED_RELEASE_ID),
                                row.get(AFFECTED_CONTRACT_ID)
                        ))
                        .add(reference(
                                row.get(REQUIRED_RELEASE_ID),
                                row.get(REQUIRED_CONTRACT_ID)
                        )));
        return grouped.entrySet().stream()
                .map(entry -> new ExecutableSupportRequirement(
                        entry.getKey(),
                        entry.getValue()
                ))
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    private static ConfigurationNewActivityRequirementSet newEvidence(
            RecordConfigurationNewActivityRequirementSetCommand command,
            Set<ExecutableSupportRequirement> requirements
    ) {
        ResolvedConfigurationPackage resolvedPackage = command.resolvedPackage();
        return new ConfigurationNewActivityRequirementSet(
                resolvedPackage.merchantIdentifier(),
                resolvedPackage.sourceConfigurationRevisionIdentifier(),
                resolvedPackage.semanticRegistryReleaseIdentifier(),
                command.resolvedPackageEvidenceIdentifier(),
                ConfigurationNewActivityRequirementSetIdentity
                        .CANONICALIZATION_VERSION,
                ConfigurationNewActivityRequirementSetIdentity.derive(
                        requirements
                ),
                requirements,
                command.evidenceProducedAt()
        );
    }

    private static ConfigurationNewActivityRequirementSet toEvidence(
            Record header,
            Set<ExecutableSupportRequirement> requirements
    ) {
        return new ConfigurationNewActivityRequirementSet(
                header.get(MERCHANT_ID),
                header.get(REVISION_ID),
                header.get(RELEASE_ID),
                header.get(PACKAGE_ID),
                header.get(CANONICALIZATION_VERSION),
                header.get(REQUIREMENT_SET_ID),
                requirements,
                header.get(EVIDENCE_PRODUCED_AT)
        );
    }

    private static ConfigurationNewActivityRequirementSet requireSameIntent(
            ConfigurationNewActivityRequirementSet requested,
            ConfigurationNewActivityRequirementSet existing
    ) {
        if (!requested.equals(existing)) {
            throw failure(
                    ConfigurationNewActivityRequirementSetFailureCategory
                            .RESOLVED_PACKAGE_EVIDENCE_IDENTITY_CONFLICT,
                    "Resolved package evidence identity was reused for different intent"
            );
        }
        return existing;
    }

    private static SemanticExecutionContractReference reference(
            String release,
            String contract
    ) {
        return new SemanticExecutionContractReference(release, contract);
    }

    private static Field<String> text(String name) {
        return DSL.field(DSL.name(name), String.class);
    }

    private static ConfigurationNewActivityRequirementSetPersistenceException
            failure(
                    ConfigurationNewActivityRequirementSetFailureCategory category,
                    String message
            ) {
        return new ConfigurationNewActivityRequirementSetPersistenceException(
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
