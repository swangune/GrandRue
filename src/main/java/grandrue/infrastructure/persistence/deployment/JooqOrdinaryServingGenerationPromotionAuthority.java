package grandrue.infrastructure.persistence.deployment;

import mainstreet.application.MerchantScope;
import grandrue.deployment.ActiveConfigurationServingRequirement;
import grandrue.deployment.InitializeOrdinaryServingAdmissionControlCommand;
import grandrue.deployment.ObservedServingGeneration;
import grandrue.deployment.OrdinaryServingAdmissionControl;
import grandrue.deployment.OrdinaryServingAdmissionLifecycle;
import grandrue.deployment.OrdinaryServingGenerationPromotionAuthority;
import grandrue.deployment.PrepareServingGenerationPromotionCommand;
import grandrue.deployment.ReconcileServingGenerationPromotionCommand;
import grandrue.deployment.ServingDeploymentAdmissionSnapshot;
import grandrue.deployment.ServingDeploymentAdmissionSnapshotAuthority;
import grandrue.deployment.ServingDeploymentCohort;
import grandrue.deployment.ServingDeploymentPromotionCoverage;
import grandrue.deployment.ServingDeploymentPromotionCoverageException;
import grandrue.deployment.ServingDeploymentPromotionCoverageFailure;
import grandrue.deployment.ServingGenerationObservationKind;
import grandrue.deployment.ServingGenerationPromotionException;
import grandrue.deployment.ServingGenerationPromotionFailureCategory;
import grandrue.deployment.ServingGenerationPromotionStatus;
import grandrue.deployment.ServingGenerationPromotionTransition;
import mainstreet.semantic.configuration.ConfigurationNewActivityRequirementSet;
import mainstreet.semantic.configuration.ConfigurationNewActivityRequirementSetAuthority;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** PostgreSQL authority for the D5c3 ordinary serving-generation fence. */
public final class JooqOrdinaryServingGenerationPromotionAuthority
        implements OrdinaryServingGenerationPromotionAuthority {

    private static final Table<?> CONTROL = table("ordinary_serving_admission_control");
    private static final Table<?> PROMOTIONS = table("ordinary_serving_generation_promotion");
    private static final Table<?> CURRENT = table("merchant_current_configuration_activation");
    private static final Table<?> REQUIREMENT_SETS = table("configuration_new_activity_requirement_set");
    private static final Field<String> COHORT = text("cohort_identifier");
    private static final Field<String> LIFECYCLE = text("lifecycle_state");
    private static final Field<Long> EPOCH = DSL.field(DSL.name("promotion_epoch"), Long.class);
    private static final Field<String> CURRENT_GENERATION = text("current_generation_identifier");
    private static final Field<String> TARGET_GENERATION = text("target_generation_identifier");
    private static final Field<String> TRANSITION = text("promotion_transition_identifier");
    private static final Field<Instant> LAST_TRANSITION_AT = instant("last_transition_at");
    private static final Field<String> TRANSITION_ID = text("transition_identifier");
    private static final Field<String> PRIOR_GENERATION = text("prior_generation_identifier");
    private static final Field<String> PROMOTION_STATUS = text("promotion_status");
    private static final Field<Instant> PREPARED_AT = instant("prepared_at");
    private static final Field<Instant> RECONCILED_AT = instant("reconciled_at");
    private static final Field<String> MERCHANT_ID = text("merchant_identifier");
    private static final Field<String> REVISION_ID = text("configuration_revision_identifier");
    private static final Field<String> RELEASE_ID = text("release_identifier");
    private static final Field<String> SET_RELEASE_ID = text("semantic_registry_release_identifier");
    private static final Field<String> PACKAGE_ID = text("resolved_package_evidence_identifier");

    private final DSLContext dsl;
    private final TransactionTemplate transactions;
    private final ServingDeploymentAdmissionSnapshotAuthority snapshots;
    private final ConfigurationNewActivityRequirementSetAuthority requirementSets;

    public JooqOrdinaryServingGenerationPromotionAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            ServingDeploymentAdmissionSnapshotAuthority snapshots,
            ConfigurationNewActivityRequirementSetAuthority requirementSets
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(Objects.requireNonNull(transactionManager, "transactionManager"));
        this.snapshots = Objects.requireNonNull(snapshots, "snapshots");
        this.requirementSets = Objects.requireNonNull(requirementSets, "requirementSets");
    }

    @Override
    public OrdinaryServingAdmissionControl initialize(InitializeOrdinaryServingAdmissionControlCommand command) {
        Objects.requireNonNull(command, "command");
        return requiredResult(transactions.execute(status -> {
            requireSnapshot(command.initialGenerationIdentifier());
            Record existing = dsl.select(COHORT, LIFECYCLE, EPOCH, CURRENT_GENERATION,
                            TARGET_GENERATION, TRANSITION, LAST_TRANSITION_AT)
                    .from(CONTROL).where(COHORT.eq(ServingDeploymentCohort.ORDINARY.name()))
                    .forUpdate().fetchOne();
            if (existing != null) {
                OrdinaryServingAdmissionControl value = toControl(existing);
                if (value.lifecycle() != OrdinaryServingAdmissionLifecycle.STABLE
                        || !value.currentGenerationIdentifier().equals(command.initialGenerationIdentifier())
                        || !value.lastTransitionAt().equals(command.initializedAt())) {
                    throw failure(ServingGenerationPromotionFailureCategory.REQUEST_IDENTITY_CONFLICT,
                            "Ordinary serving control was already initialized for different intent");
                }
                return value;
            }
            dsl.insertInto(CONTROL)
                    .columns(COHORT, LIFECYCLE, EPOCH, CURRENT_GENERATION, LAST_TRANSITION_AT)
                    .values(ServingDeploymentCohort.ORDINARY.name(), OrdinaryServingAdmissionLifecycle.STABLE.name(),
                            0L, command.initialGenerationIdentifier(), command.initializedAt())
                    .execute();
            return control().orElseThrow();
        }), "Initialization transaction returned no result");
    }

    @Override
    public Optional<OrdinaryServingAdmissionControl> control() {
        Record record = dsl.select(COHORT, LIFECYCLE, EPOCH, CURRENT_GENERATION,
                        TARGET_GENERATION, TRANSITION, LAST_TRANSITION_AT)
                .from(CONTROL).where(COHORT.eq(ServingDeploymentCohort.ORDINARY.name())).fetchOne();
        return Optional.ofNullable(record).map(JooqOrdinaryServingGenerationPromotionAuthority::toControl);
    }

    @Override
    public ServingGenerationPromotionTransition prepare(PrepareServingGenerationPromotionCommand command) {
        Objects.requireNonNull(command, "command");
        try {
            return requiredResult(transactions.execute(status -> prepareInsideTransaction(command)),
                    "Prepare transaction returned no result");
        } catch (ServingGenerationPromotionException expected) {
            throw expected;
        } catch (DataAccessException failure) {
            throw new ServingGenerationPromotionException(ServingGenerationPromotionFailureCategory.PERSISTENCE_FAILURE,
                    "Could not prepare serving-generation promotion", failure);
        }
    }

    private ServingGenerationPromotionTransition prepareInsideTransaction(PrepareServingGenerationPromotionCommand command) {
        Optional<ServingGenerationPromotionTransition> existing = transition(command.transitionIdentifier());
        if (existing.isPresent()) return requireSamePrepareIntent(command, existing.orElseThrow());

        Record row = dsl.select(COHORT, LIFECYCLE, EPOCH, CURRENT_GENERATION,
                        TARGET_GENERATION, TRANSITION, LAST_TRANSITION_AT)
                .from(CONTROL).where(COHORT.eq(ServingDeploymentCohort.ORDINARY.name()))
                .forUpdate().fetchOne();
        if (row == null) throw failure(ServingGenerationPromotionFailureCategory.CONTROL_NOT_INITIALIZED,
                "Ordinary serving control is not initialized");
        OrdinaryServingAdmissionControl control = toControl(row);
        if (control.lifecycle() != OrdinaryServingAdmissionLifecycle.STABLE) {
            throw failure(ServingGenerationPromotionFailureCategory.CONTROL_NOT_STABLE,
                    "Ordinary serving control is already PROMOTING");
        }
        if (!control.currentGenerationIdentifier().equals(command.expectedCurrentGenerationIdentifier())) {
            throw failure(ServingGenerationPromotionFailureCategory.CURRENT_GENERATION_MISMATCH,
                    "Expected current generation does not match durable control");
        }
        ServingDeploymentAdmissionSnapshot prior = requireSnapshot(control.currentGenerationIdentifier());
        ServingDeploymentAdmissionSnapshot target = requireSnapshot(command.targetGenerationIdentifier());
        requireAllActiveConfigurationsCovered(prior, target);

        long nextEpoch = control.epoch() + 1;
        dsl.insertInto(PROMOTIONS)
                .columns(TRANSITION_ID, COHORT, PRIOR_GENERATION, TARGET_GENERATION,
                        PROMOTION_STATUS, EPOCH, PREPARED_AT)
                .values(command.transitionIdentifier(), ServingDeploymentCohort.ORDINARY.name(),
                        control.currentGenerationIdentifier(), command.targetGenerationIdentifier(),
                        ServingGenerationPromotionStatus.PROMOTING.name(), nextEpoch, command.preparedAt())
                .execute();
        dsl.update(CONTROL)
                .set(LIFECYCLE, OrdinaryServingAdmissionLifecycle.PROMOTING.name())
                .set(EPOCH, nextEpoch)
                .set(TARGET_GENERATION, command.targetGenerationIdentifier())
                .set(TRANSITION, command.transitionIdentifier())
                .set(LAST_TRANSITION_AT, command.preparedAt())
                .where(COHORT.eq(ServingDeploymentCohort.ORDINARY.name())).execute();
        return transition(command.transitionIdentifier()).orElseThrow();
    }

    @Override
    public ServingGenerationPromotionTransition reconcile(ReconcileServingGenerationPromotionCommand command) {
        Objects.requireNonNull(command, "command");
        try {
            return requiredResult(transactions.execute(status -> reconcileInsideTransaction(command)),
                    "Reconciliation transaction returned no result");
        } catch (ServingGenerationPromotionException expected) {
            throw expected;
        } catch (DataAccessException failure) {
            throw new ServingGenerationPromotionException(ServingGenerationPromotionFailureCategory.PERSISTENCE_FAILURE,
                    "Could not reconcile serving-generation promotion", failure);
        }
    }

    private ServingGenerationPromotionTransition reconcileInsideTransaction(ReconcileServingGenerationPromotionCommand command) {
        Record row = dsl.select(COHORT, LIFECYCLE, EPOCH, CURRENT_GENERATION,
                        TARGET_GENERATION, TRANSITION, LAST_TRANSITION_AT)
                .from(CONTROL).where(COHORT.eq(ServingDeploymentCohort.ORDINARY.name()))
                .forUpdate().fetchOne();
        ServingGenerationPromotionTransition promotion = transition(command.transitionIdentifier())
                .orElseThrow(() -> failure(ServingGenerationPromotionFailureCategory.TRANSITION_NOT_FOUND,
                        "Promotion transition does not exist"));
        if (promotion.status() != ServingGenerationPromotionStatus.PROMOTING) {
            return requireSameReconciliationIntent(command, promotion);
        }
        if (row == null) throw failure(ServingGenerationPromotionFailureCategory.CONTROL_NOT_INITIALIZED,
                "Ordinary serving control is not initialized");
        OrdinaryServingAdmissionControl control = toControl(row);
        if (control.lifecycle() != OrdinaryServingAdmissionLifecycle.PROMOTING
                || !control.promotionTransitionIdentifier().orElse("").equals(command.transitionIdentifier())) {
            throw failure(ServingGenerationPromotionFailureCategory.REQUEST_IDENTITY_CONFLICT,
                    "Promotion transition is not the durable in-flight transition");
        }
        ObservedServingGeneration observation = command.observation();
        if (observation.kind() != ServingGenerationObservationKind.HOMOGENEOUS) {
            throw failure(ServingGenerationPromotionFailureCategory.RECONCILIATION_REQUIRED,
                    "Serving state is not homogeneous; control remains PROMOTING");
        }
        String observed = observation.generationIdentifier().orElseThrow();
        ServingGenerationPromotionStatus outcome;
        String stableGeneration;
        if (observed.equals(promotion.targetGenerationIdentifier())) {
            outcome = ServingGenerationPromotionStatus.FINALIZED;
            stableGeneration = promotion.targetGenerationIdentifier();
        } else if (observed.equals(promotion.priorGenerationIdentifier())) {
            outcome = ServingGenerationPromotionStatus.ABORTED;
            stableGeneration = promotion.priorGenerationIdentifier();
        } else {
            throw failure(ServingGenerationPromotionFailureCategory.RECONCILIATION_REQUIRED,
                    "Observed generation is neither target nor prior; control remains PROMOTING");
        }
        dsl.update(PROMOTIONS).set(PROMOTION_STATUS, outcome.name()).set(RECONCILED_AT, command.observedAt())
                .where(TRANSITION_ID.eq(command.transitionIdentifier())).execute();
        dsl.update(CONTROL).set(LIFECYCLE, OrdinaryServingAdmissionLifecycle.STABLE.name())
                .set(CURRENT_GENERATION, stableGeneration).set(TARGET_GENERATION, (String) null)
                .set(TRANSITION, (String) null).set(LAST_TRANSITION_AT, command.observedAt())
                .where(COHORT.eq(ServingDeploymentCohort.ORDINARY.name())).execute();
        return transition(command.transitionIdentifier()).orElseThrow();
    }

    @Override
    public Optional<ServingGenerationPromotionTransition> transition(String transitionIdentifier) {
        if (transitionIdentifier == null || transitionIdentifier.isBlank()) {
            throw new IllegalArgumentException("Transition identifier must not be blank");
        }
        Record record = dsl.select(TRANSITION_ID, PRIOR_GENERATION, TARGET_GENERATION,
                        PROMOTION_STATUS, EPOCH, PREPARED_AT, RECONCILED_AT)
                .from(PROMOTIONS).where(TRANSITION_ID.eq(transitionIdentifier)).fetchOne();
        return Optional.ofNullable(record).map(JooqOrdinaryServingGenerationPromotionAuthority::toTransition);
    }

    private void requireAllActiveConfigurationsCovered(ServingDeploymentAdmissionSnapshot prior,
                                                        ServingDeploymentAdmissionSnapshot target) {
        List<ActiveConfigurationServingRequirement> active = new ArrayList<>();
        dsl.select(MERCHANT_ID, REVISION_ID, RELEASE_ID).from(CURRENT)
                .orderBy(MERCHANT_ID).fetch().forEach(row -> {
                    String merchant = row.get(MERCHANT_ID);
                    String revision = row.get(REVISION_ID);
                    String release = row.get(RELEASE_ID);
                    List<String> packages = dsl.select(PACKAGE_ID).from(REQUIREMENT_SETS)
                            .where(MERCHANT_ID.eq(merchant)).and(REVISION_ID.eq(revision))
                            .and(SET_RELEASE_ID.eq(release)).fetch(PACKAGE_ID);
                    if (packages.isEmpty()) throw failure(
                            ServingGenerationPromotionFailureCategory.ACTIVE_REQUIREMENT_EVIDENCE_MISSING,
                            "Active Configuration has no exact requirement-set evidence");
                    if (packages.size() != 1) throw failure(
                            ServingGenerationPromotionFailureCategory.ACTIVE_REQUIREMENT_EVIDENCE_AMBIGUOUS,
                            "Active Configuration has ambiguous requirement-set evidence");
                    ConfigurationNewActivityRequirementSet evidence = requirementSets
                            .evidenceForPackage(new MerchantScope(merchant), packages.get(0))
                            .orElseThrow(() -> failure(
                                    ServingGenerationPromotionFailureCategory.ACTIVE_REQUIREMENT_EVIDENCE_MISSING,
                                    "Active Configuration requirement-set evidence disappeared"));
                    String digest = prior.materialisedReleaseDigests().get(release);
                    if (digest == null) throw failure(
                            ServingGenerationPromotionFailureCategory.SEMANTIC_MATERIALISATION_INCOMPLETE,
                            "Current generation lacks the active Configuration release");
                    active.add(new ActiveConfigurationServingRequirement(merchant, revision, release, digest,
                            evidence.requirementSetIdentifier(), evidence.requirements()));
                });
        try {
            ServingDeploymentPromotionCoverage.requireCovered(target, active);
        } catch (ServingDeploymentPromotionCoverageException uncovered) {
            ServingGenerationPromotionFailureCategory category =
                    uncovered.failure() == ServingDeploymentPromotionCoverageFailure.SEMANTIC_MATERIALISATION_INCOMPLETE
                            ? ServingGenerationPromotionFailureCategory.SEMANTIC_MATERIALISATION_INCOMPLETE
                            : ServingGenerationPromotionFailureCategory.EXECUTABLE_SUPPORT_INCOMPLETE;
            throw failure(category, uncovered.getMessage());
        }
    }

    private ServingDeploymentAdmissionSnapshot requireSnapshot(String generationIdentifier) {
        return snapshots.snapshot(generationIdentifier).orElseThrow(() -> failure(
                ServingGenerationPromotionFailureCategory.SNAPSHOT_NOT_FOUND,
                "Serving-generation snapshot does not exist: " + generationIdentifier));
    }

    private static ServingGenerationPromotionTransition requireSamePrepareIntent(
            PrepareServingGenerationPromotionCommand command, ServingGenerationPromotionTransition existing) {
        if (!existing.priorGenerationIdentifier().equals(command.expectedCurrentGenerationIdentifier())
                || !existing.targetGenerationIdentifier().equals(command.targetGenerationIdentifier())
                || !existing.preparedAt().equals(command.preparedAt())) {
            throw failure(ServingGenerationPromotionFailureCategory.REQUEST_IDENTITY_CONFLICT,
                    "Transition identity was reused for different prepare intent");
        }
        return existing;
    }

    private static ServingGenerationPromotionTransition requireSameReconciliationIntent(
            ReconcileServingGenerationPromotionCommand command, ServingGenerationPromotionTransition existing) {
        String expected = existing.status() == ServingGenerationPromotionStatus.FINALIZED
                ? existing.targetGenerationIdentifier() : existing.priorGenerationIdentifier();
        if (command.observation().kind() != ServingGenerationObservationKind.HOMOGENEOUS
                || !command.observation().generationIdentifier().orElse("").equals(expected)
                || !existing.reconciledAt().orElseThrow().equals(command.observedAt())) {
            throw failure(ServingGenerationPromotionFailureCategory.REQUEST_IDENTITY_CONFLICT,
                    "Completed transition was retried with different reconciliation intent");
        }
        return existing;
    }

    private static OrdinaryServingAdmissionControl toControl(Record row) {
        return new OrdinaryServingAdmissionControl(ServingDeploymentCohort.valueOf(row.get(COHORT)),
                OrdinaryServingAdmissionLifecycle.valueOf(row.get(LIFECYCLE)), row.get(EPOCH),
                row.get(CURRENT_GENERATION), Optional.ofNullable(row.get(TARGET_GENERATION)),
                Optional.ofNullable(row.get(TRANSITION)), row.get(LAST_TRANSITION_AT));
    }

    private static ServingGenerationPromotionTransition toTransition(Record row) {
        return new ServingGenerationPromotionTransition(row.get(TRANSITION_ID), row.get(PRIOR_GENERATION),
                row.get(TARGET_GENERATION), ServingGenerationPromotionStatus.valueOf(row.get(PROMOTION_STATUS)),
                row.get(EPOCH), row.get(PREPARED_AT), Optional.ofNullable(row.get(RECONCILED_AT)));
    }

    private static Table<?> table(String name) { return DSL.table(DSL.name(name)); }
    private static Field<String> text(String name) { return DSL.field(DSL.name(name), String.class); }
    private static Field<Instant> instant(String name) { return DSL.field(DSL.name(name), Instant.class); }
    private static ServingGenerationPromotionException failure(ServingGenerationPromotionFailureCategory category,
                                                                String message) {
        return new ServingGenerationPromotionException(category, message);
    }
    private static <T> T requiredResult(T result, String message) { return Objects.requireNonNull(result, message); }
}
