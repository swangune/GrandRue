package grandrue.infrastructure.persistence.deployment;

import grandrue.deployment.RecordServingDeploymentAdmissionSnapshotCommand;
import grandrue.deployment.ServingDeploymentAdmissionSnapshot;
import grandrue.deployment.ServingDeploymentAdmissionSnapshotAuthority;
import grandrue.deployment.ServingDeploymentAdmissionSnapshotFailureCategory;
import grandrue.deployment.ServingDeploymentAdmissionSnapshotPersistenceException;
import grandrue.deployment.ServingDeploymentCohort;
import grandrue.semantic.execution.ExecutableSupportManifest;
import grandrue.semantic.execution.SemanticExecutionContractReference;
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

/** PostgreSQL/jOOQ append-only authority for D5c2 snapshots. */
public final class JooqServingDeploymentAdmissionSnapshotAuthority
        implements ServingDeploymentAdmissionSnapshotAuthority {

    private static final Table<?> SNAPSHOTS = table(
            "serving_deployment_admission_snapshot"
    );
    private static final Table<?> RELEASES = table(
            "serving_deployment_materialised_release"
    );
    private static final Table<?> MANIFESTS = table(
            "serving_deployment_executable_support_manifest"
    );
    private static final Table<?> CONTRACTS = table(
            "serving_deployment_executable_support_contract"
    );
    private static final Field<String> GENERATION_ID = text(
            "generation_identifier"
    );
    private static final Field<String> COHORT_ID = text("cohort_identifier");
    private static final Field<Instant> RECORDED_AT = DSL.field(
            DSL.name("evidence_recorded_at"),
            Instant.class
    );
    private static final Field<String> RELEASE_ID = text(
            "semantic_registry_release_identifier"
    );
    private static final Field<String> BUNDLE_DIGEST = text(
            "packaged_bundle_content_digest"
    );
    private static final Field<String> PATH_ID = text(
            "implementation_path_identifier"
    );
    private static final Field<String> CONTRACT_ID = text(
            "contract_identifier"
    );

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;

    public JooqServingDeploymentAdmissionSnapshotAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public ServingDeploymentAdmissionSnapshot record(
            RecordServingDeploymentAdmissionSnapshotCommand command
    ) {
        Objects.requireNonNull(command, "command");
        ServingDeploymentAdmissionSnapshot requested = toSnapshot(command);
        ServingDeploymentAdmissionSnapshot result = transactionTemplate.execute(
                status -> recordInsideTransaction(requested)
        );
        return Objects.requireNonNull(
                result,
                "Serving snapshot transaction returned no result"
        );
    }

    @Override
    public Optional<ServingDeploymentAdmissionSnapshot> snapshot(
            String generationIdentifier
    ) {
        requireIdentifier(generationIdentifier, "Generation identifier");
        Record header = dsl.select(GENERATION_ID, COHORT_ID, RECORDED_AT)
                .from(SNAPSHOTS)
                .where(GENERATION_ID.eq(generationIdentifier))
                .fetchOne();
        if (header == null) {
            return Optional.empty();
        }
        return Optional.of(new ServingDeploymentAdmissionSnapshot(
                header.get(GENERATION_ID),
                ServingDeploymentCohort.valueOf(header.get(COHORT_ID)),
                loadReleases(generationIdentifier),
                loadManifests(generationIdentifier),
                header.get(RECORDED_AT)
        ));
    }

    private ServingDeploymentAdmissionSnapshot recordInsideTransaction(
            ServingDeploymentAdmissionSnapshot requested
    ) {
        Optional<ServingDeploymentAdmissionSnapshot> existing = snapshot(
                requested.generationIdentifier()
        );
        if (existing.isPresent()) {
            return requireSameIntent(requested, existing.orElseThrow());
        }
        try {
            int inserted = dsl.insertInto(SNAPSHOTS)
                    .columns(GENERATION_ID, COHORT_ID, RECORDED_AT)
                    .values(
                            requested.generationIdentifier(),
                            requested.cohort().name(),
                            requested.evidenceRecordedAt()
                    )
                    .onConflictDoNothing()
                    .execute();
            if (inserted == 0) {
                return snapshot(requested.generationIdentifier())
                        .map(value -> requireSameIntent(requested, value))
                        .orElseThrow(() -> failure(
                                ServingDeploymentAdmissionSnapshotFailureCategory
                                        .PERSISTENCE_FAILURE,
                                "Snapshot insert produced no durable result"
                        ));
            }
            insertChildren(requested);
        } catch (DataAccessException persistenceFailure) {
            throw new ServingDeploymentAdmissionSnapshotPersistenceException(
                    ServingDeploymentAdmissionSnapshotFailureCategory
                            .PERSISTENCE_FAILURE,
                    "Could not persist serving-deployment admission snapshot",
                    persistenceFailure
            );
        }
        return snapshot(requested.generationIdentifier()).orElseThrow(
                () -> new IllegalStateException(
                        "Serving-deployment snapshot was not committed"
                )
        );
    }

    private void insertChildren(ServingDeploymentAdmissionSnapshot snapshot) {
        snapshot.materialisedReleaseDigests().forEach((release, digest) ->
                dsl.insertInto(RELEASES)
                        .columns(GENERATION_ID, RELEASE_ID, BUNDLE_DIGEST)
                        .values(snapshot.generationIdentifier(), release, digest)
                        .execute()
        );
        for (ExecutableSupportManifest manifest : snapshot.supportManifests()) {
            dsl.insertInto(MANIFESTS)
                    .columns(GENERATION_ID, PATH_ID)
                    .values(
                            snapshot.generationIdentifier(),
                            manifest.implementationPathIdentifier()
                    )
                    .execute();
            for (SemanticExecutionContractReference contract
                    : manifest.supportedContracts()) {
                dsl.insertInto(CONTRACTS)
                        .columns(
                                GENERATION_ID,
                                PATH_ID,
                                RELEASE_ID,
                                CONTRACT_ID
                        )
                        .values(
                                snapshot.generationIdentifier(),
                                manifest.implementationPathIdentifier(),
                                contract.semanticRegistryReleaseIdentifier(),
                                contract.contractIdentifier()
                        )
                        .execute();
            }
        }
    }

    private Map<String, String> loadReleases(String generationIdentifier) {
        LinkedHashMap<String, String> releases = new LinkedHashMap<>();
        dsl.select(RELEASE_ID, BUNDLE_DIGEST)
                .from(RELEASES)
                .where(GENERATION_ID.eq(generationIdentifier))
                .orderBy(RELEASE_ID)
                .fetch()
                .forEach(row -> releases.put(
                        row.get(RELEASE_ID),
                        row.get(BUNDLE_DIGEST)
                ));
        return Map.copyOf(releases);
    }

    private Set<ExecutableSupportManifest> loadManifests(
            String generationIdentifier
    ) {
        Map<String, Set<SemanticExecutionContractReference>> grouped =
                new LinkedHashMap<>();
        dsl.select(PATH_ID)
                .from(MANIFESTS)
                .where(GENERATION_ID.eq(generationIdentifier))
                .orderBy(PATH_ID)
                .fetch()
                .forEach(row -> grouped.put(
                        row.get(PATH_ID),
                        new LinkedHashSet<>()
                ));
        dsl.select(PATH_ID, RELEASE_ID, CONTRACT_ID)
                .from(CONTRACTS)
                .where(GENERATION_ID.eq(generationIdentifier))
                .orderBy(PATH_ID, RELEASE_ID, CONTRACT_ID)
                .fetch()
                .forEach(row -> grouped.get(row.get(PATH_ID)).add(
                        new SemanticExecutionContractReference(
                                row.get(RELEASE_ID),
                                row.get(CONTRACT_ID)
                        )
                ));
        return grouped.entrySet().stream()
                .map(entry -> new ExecutableSupportManifest(
                        entry.getKey(),
                        entry.getValue()
                ))
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    private static ServingDeploymentAdmissionSnapshot toSnapshot(
            RecordServingDeploymentAdmissionSnapshotCommand command
    ) {
        return new ServingDeploymentAdmissionSnapshot(
                command.generationIdentifier(),
                ServingDeploymentCohort.ORDINARY,
                command.materialisationSet().contentIdentity(),
                Set.copyOf(command.supportManifests()),
                command.evidenceRecordedAt()
        );
    }

    private static ServingDeploymentAdmissionSnapshot requireSameIntent(
            ServingDeploymentAdmissionSnapshot requested,
            ServingDeploymentAdmissionSnapshot existing
    ) {
        if (!requested.equals(existing)) {
            throw failure(
                    ServingDeploymentAdmissionSnapshotFailureCategory
                            .GENERATION_IDENTITY_CONFLICT,
                    "Generation identity was reused for different snapshot intent"
            );
        }
        return existing;
    }

    private static Table<?> table(String name) {
        return DSL.table(DSL.name(name));
    }

    private static Field<String> text(String name) {
        return DSL.field(DSL.name(name), String.class);
    }

    private static ServingDeploymentAdmissionSnapshotPersistenceException
            failure(
                    ServingDeploymentAdmissionSnapshotFailureCategory category,
                    String message
            ) {
        return new ServingDeploymentAdmissionSnapshotPersistenceException(
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
