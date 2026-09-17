package grandrue.infrastructure.persistence.configuration;

import mainstreet.application.MerchantScope;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.semantic.configuration.ApproveInitialConfigurationRevisionCommand;
import mainstreet.semantic.configuration.ConfigurationRevisionApprovalFailureCategory;
import mainstreet.semantic.configuration.ConfigurationRevisionApprovalPersistenceException;
import mainstreet.semantic.configuration.InitialConfigurationRevisionApproval;
import mainstreet.semantic.configuration.InitialConfigurationRevisionApprovalAuthority;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** PostgreSQL authority for exact ordinary first-configuration approvals. */
public final class JooqInitialConfigurationRevisionApprovalAuthority
        implements InitialConfigurationRevisionApprovalAuthority {
    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqInitialConfigurationRevisionApprovalAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(Objects.requireNonNull(transactionManager, "transactionManager"));
    }

    @Override
    public InitialConfigurationRevisionApproval approve(
            ApproveInitialConfigurationRevisionCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(command, "command");
        requireAuthenticatedContext(command, trustedContext);
        InitialConfigurationRevisionApproval result = transactions.execute(status -> approveInside(command));
        return Objects.requireNonNull(result, "Approval transaction returned no result");
    }

    private InitialConfigurationRevisionApproval approveInside(ApproveInitialConfigurationRevisionCommand command) {
        lock("configuration-approval-request|" + command.logicalApprovalRequestIdentifier(), 403);
        Optional<InitialConfigurationRevisionApproval> replay = approvalByRequest(
                command.logicalApprovalRequestIdentifier()
        );
        if (replay.isPresent()) {
            return requireSameIntent(command, replay.orElseThrow());
        }

        String merchant = command.merchantScope().merchantIdentifier();
        lock(merchant, 76);
        Record revision = dsl.fetchOne(
                "select semantic_registry_release_identifier "
                        + "from merchant_configuration_revision "
                        + "where merchant_identifier = ? "
                        + "and configuration_revision_identifier = ? "
                        + "and configuration_version = 1 "
                        + "and base_configuration_revision_identifier is null",
                merchant,
                command.configurationRevisionIdentifier()
        );
        if (revision == null) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory.REVISION_NOT_FOUND_OR_WRONG_MERCHANT,
                    "Ordinary first Configuration Revision does not exist"
            );
        }
        String release = revision.get("semantic_registry_release_identifier", String.class);
        Record validation = dsl.fetchOne(
                "select configuration_revision_identifier, semantic_registry_release_identifier, "
                        + "resolved_package_evidence_identifier, validation_outcome "
                        + "from configuration_validation_evidence "
                        + "where merchant_identifier = ? and validation_evidence_identifier = ?",
                merchant,
                command.validationEvidenceIdentifier()
        );
        if (validation == null
                || !command.configurationRevisionIdentifier().equals(
                        validation.get("configuration_revision_identifier", String.class)
                )
                || !release.equals(validation.get("semantic_registry_release_identifier", String.class))
                || !"SUCCEEDED".equals(validation.get("validation_outcome", String.class))) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .VALIDATION_EVIDENCE_NOT_FOUND_OR_AFFINITY_MISMATCH,
                    "Exact successful validation evidence is required"
            );
        }
        String packageEvidence = validation.get("resolved_package_evidence_identifier", String.class);
        if (packageEvidence == null || packageEvidence.isBlank()) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .PACKAGE_EVIDENCE_NOT_FOUND_OR_AFFINITY_MISMATCH,
                    "Exact package evidence is required"
            );
        }
        Record impact = dsl.fetchOne(
                "select configuration_revision_identifier, semantic_registry_release_identifier, "
                        + "validation_evidence_identifier, resolved_package_evidence_identifier "
                        + "from configuration_impact_review_evidence "
                        + "where merchant_identifier = ? and impact_review_evidence_identifier = ?",
                merchant,
                command.impactReviewEvidenceIdentifier()
        );
        if (impact == null) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .IMPACT_REVIEW_EVIDENCE_NOT_FOUND_OR_AFFINITY_MISMATCH,
                    "Exact impact-review evidence is required"
            );
        }
        if (!packageEvidence.equals(impact.get("resolved_package_evidence_identifier", String.class))) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .PACKAGE_EVIDENCE_NOT_FOUND_OR_AFFINITY_MISMATCH,
                    "Impact review does not resolve the exact validated package evidence"
            );
        }
        if (!command.configurationRevisionIdentifier().equals(
                impact.get("configuration_revision_identifier", String.class)
        )
                || !release.equals(impact.get("semantic_registry_release_identifier", String.class))
                || !command.validationEvidenceIdentifier().equals(
                        impact.get("validation_evidence_identifier", String.class)
                )) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .IMPACT_REVIEW_EVIDENCE_NOT_FOUND_OR_AFFINITY_MISMATCH,
                    "Impact review does not match the exact revision and validation evidence"
            );
        }
        if (dsl.fetchOne(
                "select 1 from configuration_impact_review_finding "
                        + "where merchant_identifier = ? "
                        + "and impact_review_evidence_identifier = ? "
                        + "and impact_classification = 'BLOCKING'",
                merchant,
                command.impactReviewEvidenceIdentifier()
        ) != null) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory.BLOCKING_IMPACT_REMAINS,
                    "Blocking configuration impact remains"
            );
        }
        Record controller = dsl.fetchOne(
                "select controller_relationship_identifier, identity_identifier "
                        + "from merchant_controller_relationship "
                        + "where merchant_identifier = ? and lifecycle = 'ACTIVE' for share",
                merchant
        );
        if (controller == null) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory.CURRENT_CONTROLLER_NOT_FOUND,
                    "Current Merchant Controller does not exist"
            );
        }
        if (!command.approvingPrincipalIdentifier().equals(controller.get("identity_identifier", String.class))) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory.APPROVING_PRINCIPAL_NOT_CURRENT_CONTROLLER,
                    "Approving principal is not the current Merchant Controller"
            );
        }
        InitialConfigurationRevisionApproval approval = new InitialConfigurationRevisionApproval(
                command.logicalApprovalRequestIdentifier(),
                merchant,
                command.configurationRevisionIdentifier(),
                release,
                command.validationEvidenceIdentifier(),
                command.impactReviewEvidenceIdentifier(),
                packageEvidence,
                command.approvingPrincipalIdentifier(),
                controller.get("controller_relationship_identifier", String.class),
                command.approvedAt()
        );
        try {
            dsl.execute(
                    "insert into configuration_revision_approval "
                            + "(logical_approval_request_identifier, merchant_identifier, "
                            + "configuration_revision_identifier, semantic_registry_release_identifier, "
                            + "validation_evidence_identifier, impact_review_evidence_identifier, "
                            + "resolved_package_evidence_identifier, approving_principal_identifier, "
                            + "controller_relationship_identifier, approved_at) "
                            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as timestamptz))",
                    approval.logicalApprovalRequestIdentifier(),
                    approval.merchantIdentifier(),
                    approval.configurationRevisionIdentifier(),
                    approval.semanticRegistryReleaseIdentifier(),
                    approval.validationEvidenceIdentifier(),
                    approval.impactReviewEvidenceIdentifier(),
                    approval.resolvedPackageEvidenceIdentifier(),
                    approval.approvingPrincipalIdentifier(),
                    approval.controllerRelationshipIdentifier(),
                    approval.approvedAt().toString()
            );
        } catch (DataAccessException failure) {
            throw new ConfigurationRevisionApprovalPersistenceException(
                    ConfigurationRevisionApprovalFailureCategory.APPROVAL_CONCURRENCY_CONFLICT,
                    "Approval fact could not commit",
                    failure
            );
        }
        return approval;
    }

    @Override
    public Optional<InitialConfigurationRevisionApproval> approvalByRequest(String requestIdentifier) {
        requireIdentifier(requestIdentifier);
        return Optional.ofNullable(dsl.fetchOne(
                "select * from configuration_revision_approval "
                        + "where logical_approval_request_identifier = ?",
                requestIdentifier
        )).map(this::toApproval);
    }

    @Override
    public Optional<InitialConfigurationRevisionApproval> currentApplicableApproval(
            MerchantScope scope,
            String revisionIdentifier
    ) {
        Objects.requireNonNull(scope, "scope");
        requireIdentifier(revisionIdentifier);
        return Optional.ofNullable(dsl.fetchOne(
                "select a.* from configuration_revision_approval a "
                        + "join merchant_controller_relationship c "
                        + "on c.merchant_identifier = a.merchant_identifier "
                        + "and c.controller_relationship_identifier = a.controller_relationship_identifier "
                        + "and c.identity_identifier = a.approving_principal_identifier "
                        + "where a.merchant_identifier = ? "
                        + "and a.configuration_revision_identifier = ? "
                        + "and c.lifecycle = 'ACTIVE' "
                        + "order by a.approved_at desc limit 1",
                scope.merchantIdentifier(),
                revisionIdentifier
        )).map(this::toApproval);
    }

    private InitialConfigurationRevisionApproval toApproval(Record record) {
        return new InitialConfigurationRevisionApproval(
                record.get("logical_approval_request_identifier", String.class),
                record.get("merchant_identifier", String.class),
                record.get("configuration_revision_identifier", String.class),
                record.get("semantic_registry_release_identifier", String.class),
                record.get("validation_evidence_identifier", String.class),
                record.get("impact_review_evidence_identifier", String.class),
                record.get("resolved_package_evidence_identifier", String.class),
                record.get("approving_principal_identifier", String.class),
                record.get("controller_relationship_identifier", String.class),
                record.get("approved_at", Instant.class)
        );
    }

    private static void requireAuthenticatedContext(
            ApproveInitialConfigurationRevisionCommand command,
            TrustedExecutionContext context
    ) {
        if (context == null || context.authentication().isEmpty()
                || !context.merchantScope().equals(command.merchantScope())
                || !context.principal().identifier().equals(command.approvingPrincipalIdentifier())
                || !context.authentication().orElseThrow().identityIdentifier().equals(
                        command.approvingPrincipalIdentifier()
                )) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory.AUTHENTICATED_PRINCIPAL_REQUIRED,
                    "Matching authenticated trusted principal is required"
            );
        }
    }

    private static InitialConfigurationRevisionApproval requireSameIntent(
            ApproveInitialConfigurationRevisionCommand command,
            InitialConfigurationRevisionApproval approval
    ) {
        if (!approval.merchantIdentifier().equals(command.merchantScope().merchantIdentifier())
                || !approval.configurationRevisionIdentifier().equals(
                        command.configurationRevisionIdentifier()
                )
                || !approval.validationEvidenceIdentifier().equals(command.validationEvidenceIdentifier())
                || !approval.impactReviewEvidenceIdentifier().equals(
                        command.impactReviewEvidenceIdentifier()
                )
                || !approval.approvingPrincipalIdentifier().equals(
                        command.approvingPrincipalIdentifier()
                )
                || !approval.approvedAt().equals(command.approvedAt())) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory.APPROVAL_REQUEST_IDENTITY_CONFLICT,
                    "Approval request identity was reused for different intent"
            );
        }
        return approval;
    }

    private void lock(String value, int seed) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), ?))",
                value,
                seed
        );
    }

    private static void requireIdentifier(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Identifier must not be blank");
        }
    }

    private static ConfigurationRevisionApprovalPersistenceException failure(
            ConfigurationRevisionApprovalFailureCategory category,
            String message
    ) {
        return new ConfigurationRevisionApprovalPersistenceException(category, message);
    }
}
