package mainstreet.infrastructure.persistence.configuration;

import mainstreet.application.MerchantScope;
import grandrue.deployment.OrdinaryServingAdmissionLifecycle;
import grandrue.deployment.ServingDeploymentAdmissionSnapshot;
import grandrue.deployment.ServingDeploymentAdmissionSnapshotAuthority;
import mainstreet.semantic.configuration.ConfigurationActivationAdmissionAuthority;
import mainstreet.semantic.configuration.ConfigurationActivationAdmissionEvidence;
import mainstreet.semantic.configuration.ConfigurationActivationAdmissionResult;
import mainstreet.semantic.configuration.ConfigurationActivationStatus;
import mainstreet.semantic.configuration.ConfigurationNewActivityRequirementSet;
import mainstreet.semantic.configuration.ConfigurationNewActivityRequirementSetAuthority;
import mainstreet.semantic.release.SemanticReleaseAdmissionDisposition;
import mainstreet.semantic.release.SemanticReleasePurpose;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** D5c4 PostgreSQL evaluator; all reads participate in the activation transaction. */
public final class JooqConfigurationActivationAdmissionAuthority
        implements ConfigurationActivationAdmissionAuthority {

    private static final Table<?> CONTROL = table("ordinary_serving_admission_control");
    private static final Table<?> CURRENT = table("semantic_release_purpose_current_admission");
    private static final Table<?> DECISIONS = table("semantic_release_purpose_admission_decision");
    private static final Table<?> REQUIREMENT_SETS = table("configuration_new_activity_requirement_set");
    private static final Field<String> COHORT = text("cohort_identifier");
    private static final Field<String> LIFECYCLE = text("lifecycle_state");
    private static final Field<Long> EPOCH = DSL.field(DSL.name("promotion_epoch"), Long.class);
    private static final Field<String> GENERATION = text("current_generation_identifier");
    private static final Field<String> RELEASE = text("semantic_registry_release_identifier");
    private static final Field<String> PURPOSE = text("admission_purpose");
    private static final Field<String> DECISION_ID = text("admission_decision_identifier");
    private static final Field<String> DISPOSITION = text("admission_disposition");
    private static final Field<String> MERCHANT = text("merchant_identifier");
    private static final Field<String> REVISION = text("configuration_revision_identifier");
    private static final Field<String> PACKAGE = text("resolved_package_evidence_identifier");
    private static final Field<String> REQUIREMENT_SET = text("requirement_set_identifier");

    private final DSLContext dsl;
    private final ConfigurationNewActivityRequirementSetAuthority requirementSets;
    private final ServingDeploymentAdmissionSnapshotAuthority snapshots;

    public JooqConfigurationActivationAdmissionAuthority(
            DSLContext dsl,
            ConfigurationNewActivityRequirementSetAuthority requirementSets,
            ServingDeploymentAdmissionSnapshotAuthority snapshots
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.requirementSets = Objects.requireNonNull(requirementSets, "requirementSets");
        this.snapshots = Objects.requireNonNull(snapshots, "snapshots");
    }

    @Override
    public ConfigurationActivationAdmissionResult admit(
            MerchantScope merchantScope,
            String configurationRevisionIdentifier,
            String semanticRegistryReleaseIdentifier,
            Optional<String> approvedResolvedPackageEvidenceIdentifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(configurationRevisionIdentifier, "Configuration Revision identifier");
        requireIdentifier(semanticRegistryReleaseIdentifier, "Semantic Registry Release identifier");
        Objects.requireNonNull(
                approvedResolvedPackageEvidenceIdentifier,
                "approvedResolvedPackageEvidenceIdentifier"
        );

        Record control = dsl.select(LIFECYCLE, EPOCH, GENERATION)
                .from(CONTROL)
                .where(COHORT.eq("ORDINARY"))
                .forShare()
                .fetchOne();
        if (control == null
                || !OrdinaryServingAdmissionLifecycle.STABLE.name().equals(
                        control.get(LIFECYCLE)
                )) {
            return rejected(ConfigurationActivationStatus.DEPLOYMENT_ADMISSION_CONFLICT);
        }

        List<String> purposes = List.of(
                SemanticReleasePurpose.NEW_BUSINESS_ACTIVITY.name(),
                SemanticReleasePurpose.NEW_CONFIGURATION_VALIDATION.name()
        );
        var currentRows = dsl.select(PURPOSE, DECISION_ID)
                .from(CURRENT)
                .where(RELEASE.eq(semanticRegistryReleaseIdentifier))
                .and(PURPOSE.in(purposes))
                .orderBy(PURPOSE)
                .forShare()
                .fetch();

        String validationDecision = decision(
                currentRows,
                semanticRegistryReleaseIdentifier,
                SemanticReleasePurpose.NEW_CONFIGURATION_VALIDATION
        );
        if (validationDecision == null) {
            return rejected(ConfigurationActivationStatus.SEMANTIC_RELEASE_NOT_ADMITTED_FOR_NEW_CONFIGURATION);
        }
        String activityDecision = decision(
                currentRows,
                semanticRegistryReleaseIdentifier,
                SemanticReleasePurpose.NEW_BUSINESS_ACTIVITY
        );
        if (activityDecision == null) {
            return rejected(ConfigurationActivationStatus.SEMANTIC_RELEASE_NOT_ADMITTED_FOR_NEW_BUSINESS_ACTIVITY);
        }

        var requirementQuery = dsl.select(PACKAGE, REQUIREMENT_SET)
                .from(REQUIREMENT_SETS)
                .where(MERCHANT.eq(merchantScope.merchantIdentifier()))
                .and(REVISION.eq(configurationRevisionIdentifier))
                .and(RELEASE.eq(semanticRegistryReleaseIdentifier));
        if (approvedResolvedPackageEvidenceIdentifier.isPresent()) {
            requirementQuery = requirementQuery.and(PACKAGE.eq(
                    approvedResolvedPackageEvidenceIdentifier.orElseThrow()
            ));
        }
        var requirementRows = requirementQuery.fetch();
        if (requirementRows.size() != 1) {
            return rejected(ConfigurationActivationStatus.PACKAGE_MISMATCH);
        }
        String packageIdentifier = requirementRows.get(0).get(PACKAGE);
        ConfigurationNewActivityRequirementSet requirements = requirementSets
                .evidenceForPackage(merchantScope, packageIdentifier)
                .filter(value -> value.configurationRevisionIdentifier().equals(
                        configurationRevisionIdentifier
                ))
                .filter(value -> value.semanticRegistryReleaseIdentifier().equals(
                        semanticRegistryReleaseIdentifier
                ))
                .orElse(null);
        if (requirements == null) {
            return rejected(ConfigurationActivationStatus.PACKAGE_MISMATCH);
        }

        String generationIdentifier = control.get(GENERATION);
        ServingDeploymentAdmissionSnapshot snapshot = snapshots
                .snapshot(generationIdentifier)
                .orElse(null);
        if (snapshot == null) {
            return rejected(ConfigurationActivationStatus.DEPLOYMENT_ADMISSION_CONFLICT);
        }
        String digest = snapshot.materialisedReleaseDigests().get(
                semanticRegistryReleaseIdentifier
        );
        if (digest == null) {
            return rejected(
                    ConfigurationActivationStatus
                            .SEMANTIC_RELEASE_NOT_MATERIALISED_BY_SERVING_DEPLOYMENT
            );
        }
        if (!snapshot.uncoveredRequirements(requirements.requirements()).isEmpty()) {
            return rejected(ConfigurationActivationStatus.EXECUTABLE_SUPPORT_INCOMPLETE);
        }

        return ConfigurationActivationAdmissionResult.admitted(
                new ConfigurationActivationAdmissionEvidence(
                        semanticRegistryReleaseIdentifier,
                        validationDecision,
                        activityDecision,
                        requirements.resolvedPackageEvidenceIdentifier(),
                        requirements.requirementSetIdentifier(),
                        generationIdentifier,
                        control.get(EPOCH),
                        digest
                )
        );
    }

    private String decision(
            org.jooq.Result<? extends Record> currentRows,
            String release,
            SemanticReleasePurpose purpose
    ) {
        String identifier = currentRows.stream()
                .filter(row -> purpose.name().equals(row.get(PURPOSE)))
                .map(row -> row.get(DECISION_ID))
                .findFirst()
                .orElse(null);
        if (identifier == null) {
            return null;
        }
        Record decision = dsl.select(RELEASE, PURPOSE, DISPOSITION)
                .from(DECISIONS)
                .where(DECISION_ID.eq(identifier))
                .fetchOne();
        if (decision == null
                || !release.equals(decision.get(RELEASE))
                || !purpose.name().equals(decision.get(PURPOSE))
                || !SemanticReleaseAdmissionDisposition.ADMITTED.name().equals(
                        decision.get(DISPOSITION)
                )) {
            return null;
        }
        return identifier;
    }

    private static ConfigurationActivationAdmissionResult rejected(
            ConfigurationActivationStatus status
    ) {
        return ConfigurationActivationAdmissionResult.rejected(status);
    }

    private static Table<?> table(String name) {
        return DSL.table(DSL.name(name));
    }

    private static Field<String> text(String name) {
        return DSL.field(DSL.name(name), String.class);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
