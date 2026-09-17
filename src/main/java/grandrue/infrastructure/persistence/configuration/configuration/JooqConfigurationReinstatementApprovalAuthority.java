package grandrue.infrastructure.persistence.configuration;

import mainstreet.application.MerchantScope;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.semantic.configuration.ApproveConfigurationReinstatementCommand;
import mainstreet.semantic.configuration.ConfigurationReinstatementApproval;
import mainstreet.semantic.configuration.ConfigurationReinstatementApprovalAuthority;
import mainstreet.semantic.configuration.ConfigurationRevisionApprovalFailureCategory;
import mainstreet.semantic.configuration.ConfigurationRevisionApprovalPersistenceException;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * PostgreSQL authority for fresh, exact Configuration reinstatement approval.
 *
 * <p>A genuine reinstatement decision is bound to the immutable
 * Configuration Activation that owns the merchant's current Configuration
 * pointer when the decision is established.</p>
 *
 * <p>The lock order is:</p>
 *
 * <pre>
 * approval logical-request fence 403
 *     -> merchant Configuration-activation fence 41
 *     -> merchant current-authority fence 76
 * </pre>
 *
 * <p>This makes the current activation basis stable while validation,
 * impact-review and current Controller authority are evaluated.</p>
 *
 * <p>This authority intentionally does not broaden
 * InitialConfigurationRevisionApprovalAuthority or
 * NonInitialConfigurationRevisionApprovalAuthority.</p>
 *
 * Authority: MS-PROT-040 v1.8,
 * {@code designs/MS-PROT-040 v1.8 — Configuration Reinstatement Decision
 * Affinity Amendment.md}, §§7, 9, 14–16 — Reinstatement Approval, Approval
 * Authority Separation, Retry and Idempotency, Concurrency, and Persistence
 * Semantics.
 */
public final class JooqConfigurationReinstatementApprovalAuthority
        implements ConfigurationReinstatementApprovalAuthority {

    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqConfigurationReinstatementApprovalAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl =
                Objects.requireNonNull(
                        dsl,
                        "dsl"
                );

        this.transactions =
                new TransactionTemplate(
                        Objects.requireNonNull(
                                transactionManager,
                                "transactionManager"
                        )
                );
    }

    @Override
    public ConfigurationReinstatementApproval approve(
            ApproveConfigurationReinstatementCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(
                command,
                "command"
        );

        requireAuthenticatedContext(
                command,
                trustedContext
        );

        ConfigurationReinstatementApproval result =
                transactions.execute(
                        status -> approveInside(command)
                );

        return Objects.requireNonNull(
                result,
                "Reinstatement approval transaction returned no result"
        );
    }

    private ConfigurationReinstatementApproval approveInside(
            ApproveConfigurationReinstatementCommand command
    ) {
        lock(
                "configuration-approval-request|"
                        + command.logicalApprovalRequestIdentifier(),
                403
        );

        Record replay =
                rawApprovalByRequest(
                        command.logicalApprovalRequestIdentifier()
                );

        if (replay != null) {
            String replayBasis =
                    replay.get(
                            "reinstatement_basis_activation_request_identifier",
                            String.class
                    );

            if (replayBasis == null) {
                throw failure(
                        ConfigurationRevisionApprovalFailureCategory
                                .APPROVAL_REQUEST_IDENTITY_CONFLICT,
                        "Approval request identity was already used "
                                + "outside the reinstatement approval path"
                );
            }

            return requireSameIntent(
                    command,
                    toApproval(replay)
            );
        }

        String merchant =
                command.merchantScope()
                        .merchantIdentifier();

        /*
         * A reinstatement approval establishes its basis against the merchant's
         * exact current Configuration Activation. Prevent that pointer from
         * moving while the decision is constructed.
         */
        lock(
                merchant,
                41
        );

        /*
         * Controller transfer, suspension/closure authority and activation
         * current-authority evaluation share this merchant fence.
         */
        lock(
                merchant,
                76
        );

        Record current =
                dsl.fetchOne(
                        "select activation_request_identifier, "
                                + "configuration_revision_identifier "
                                + "from "
                                + "merchant_current_configuration_activation "
                                + "where merchant_identifier = ?",
                        merchant
                );

        if (current == null) {
            throw new IllegalArgumentException(
                    "Configuration reinstatement requires "
                            + "a current Configuration Activation"
            );
        }

        String currentActivationRequestIdentifier =
                current.get(
                        "activation_request_identifier",
                        String.class
                );

        String currentRevisionIdentifier =
                current.get(
                        "configuration_revision_identifier",
                        String.class
                );

        if (!command
                .reinstatementBasisActivationRequestIdentifier()
                .equals(
                        currentActivationRequestIdentifier
                )) {
            throw new IllegalArgumentException(
                    "Reinstatement Basis Activation "
                            + "does not own the current "
                            + "Configuration pointer"
            );
        }

        if (command
                .configurationRevisionIdentifier()
                .equals(
                        currentRevisionIdentifier
                )) {
            throw new IllegalArgumentException(
                    "Current Configuration Revision "
                            + "is not a reinstatement target"
            );
        }

        Record revision =
                dsl.fetchOne(
                        "select semantic_registry_release_identifier "
                                + "from merchant_configuration_revision "
                                + "where merchant_identifier = ? "
                                + "and configuration_revision_identifier = ?",
                        merchant,
                        command.configurationRevisionIdentifier()
                );

        if (revision == null) {
            throw new IllegalArgumentException(
                    "Configuration reinstatement target does not exist"
            );
        }

        boolean previouslyActivated =
                dsl.fetchExists(
                        dsl.selectOne()
                                .from(
                                        org.jooq.impl.DSL.table(
                                                org.jooq.impl.DSL.name(
                                                        "configuration_activation"
                                                )
                                        )
                                )
                                .where(
                                        org.jooq.impl.DSL.field(
                                                org.jooq.impl.DSL.name(
                                                        "merchant_identifier"
                                                ),
                                                String.class
                                        ).eq(merchant)
                                )
                                .and(
                                        org.jooq.impl.DSL.field(
                                                org.jooq.impl.DSL.name(
                                                        "configuration_revision_identifier"
                                                ),
                                                String.class
                                        ).eq(
                                                command
                                                        .configurationRevisionIdentifier()
                                        )
                                )
                );

        if (!previouslyActivated) {
            throw new IllegalArgumentException(
                    "Configuration Revision has no prior "
                            + "committed activation and therefore "
                            + "is not a reinstatement target"
            );
        }

        String semanticRelease =
                revision.get(
                        "semantic_registry_release_identifier",
                        String.class
                );

        Record validation =
                dsl.fetchOne(
                        "select configuration_revision_identifier, "
                                + "semantic_registry_release_identifier, "
                                + "resolved_package_evidence_identifier, "
                                + "validation_outcome, "
                                + "reinstatement_basis_activation_request_identifier "
                                + "from configuration_validation_evidence "
                                + "where merchant_identifier = ? "
                                + "and validation_evidence_identifier = ?",
                        merchant,
                        command.validationEvidenceIdentifier()
                );

        if (validation == null
                || !command
                .configurationRevisionIdentifier()
                .equals(
                        validation.get(
                                "configuration_revision_identifier",
                                String.class
                        )
                )
                || !semanticRelease.equals(
                        validation.get(
                                "semantic_registry_release_identifier",
                                String.class
                        )
                )
                || !"SUCCEEDED".equals(
                        validation.get(
                                "validation_outcome",
                                String.class
                        )
                )
                || !command
                .reinstatementBasisActivationRequestIdentifier()
                .equals(
                        validation.get(
                                "reinstatement_basis_activation_request_identifier",
                                String.class
                        )
                )) {

            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .VALIDATION_EVIDENCE_NOT_FOUND_OR_AFFINITY_MISMATCH,
                    "Exact successful basis-affined "
                            + "reinstatement validation evidence is required"
            );
        }

        String packageEvidence =
                validation.get(
                        "resolved_package_evidence_identifier",
                        String.class
                );

        if (packageEvidence == null
                || packageEvidence.isBlank()) {

            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .PACKAGE_EVIDENCE_NOT_FOUND_OR_AFFINITY_MISMATCH,
                    "Exact reinstatement package evidence is required"
            );
        }

        Record impact =
                dsl.fetchOne(
                        "select configuration_revision_identifier, "
                                + "semantic_registry_release_identifier, "
                                + "validation_evidence_identifier, "
                                + "resolved_package_evidence_identifier, "
                                + "reinstatement_basis_activation_request_identifier "
                                + "from configuration_impact_review_evidence "
                                + "where merchant_identifier = ? "
                                + "and impact_review_evidence_identifier = ?",
                        merchant,
                        command.impactReviewEvidenceIdentifier()
                );

        if (impact == null) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .IMPACT_REVIEW_EVIDENCE_NOT_FOUND_OR_AFFINITY_MISMATCH,
                    "Exact basis-affined reinstatement "
                            + "impact-review evidence is required"
            );
        }

        if (!packageEvidence.equals(
                impact.get(
                        "resolved_package_evidence_identifier",
                        String.class
                )
        )) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .PACKAGE_EVIDENCE_NOT_FOUND_OR_AFFINITY_MISMATCH,
                    "Reinstatement impact review does not resolve "
                            + "the exact validated package evidence"
            );
        }

        if (!command
                .configurationRevisionIdentifier()
                .equals(
                        impact.get(
                                "configuration_revision_identifier",
                                String.class
                        )
                )
                || !semanticRelease.equals(
                        impact.get(
                                "semantic_registry_release_identifier",
                                String.class
                        )
                )
                || !command
                .validationEvidenceIdentifier()
                .equals(
                        impact.get(
                                "validation_evidence_identifier",
                                String.class
                        )
                )
                || !command
                .reinstatementBasisActivationRequestIdentifier()
                .equals(
                        impact.get(
                                "reinstatement_basis_activation_request_identifier",
                                String.class
                        )
                )) {

            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .IMPACT_REVIEW_EVIDENCE_NOT_FOUND_OR_AFFINITY_MISMATCH,
                    "Reinstatement impact review does not match "
                            + "the exact revision, validation evidence "
                            + "and Reinstatement Basis Activation"
            );
        }

        if (dsl.fetchOne(
                "select 1 "
                        + "from configuration_impact_review_finding "
                        + "where merchant_identifier = ? "
                        + "and impact_review_evidence_identifier = ? "
                        + "and impact_classification = 'BLOCKING'",
                merchant,
                command.impactReviewEvidenceIdentifier()
        ) != null) {

            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .BLOCKING_IMPACT_REMAINS,
                    "Blocking configuration impact remains"
            );
        }

        Record controller =
                dsl.fetchOne(
                        "select controller_relationship_identifier, "
                                + "identity_identifier "
                                + "from merchant_controller_relationship "
                                + "where merchant_identifier = ? "
                                + "and lifecycle = 'ACTIVE' "
                                + "for share",
                        merchant
                );

        if (controller == null) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .CURRENT_CONTROLLER_NOT_FOUND,
                    "Current Merchant Controller does not exist"
            );
        }

        if (!command
                .approvingPrincipalIdentifier()
                .equals(
                        controller.get(
                                "identity_identifier",
                                String.class
                        )
                )) {

            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .APPROVING_PRINCIPAL_NOT_CURRENT_CONTROLLER,
                    "Approving principal is not the current "
                            + "Merchant Controller"
            );
        }

        ConfigurationReinstatementApproval approval =
                new ConfigurationReinstatementApproval(
                        command.logicalApprovalRequestIdentifier(),
                        merchant,
                        command.configurationRevisionIdentifier(),
                        semanticRelease,
                        command.reinstatementBasisActivationRequestIdentifier(),
                        command.validationEvidenceIdentifier(),
                        command.impactReviewEvidenceIdentifier(),
                        packageEvidence,
                        command.approvingPrincipalIdentifier(),
                        controller.get(
                                "controller_relationship_identifier",
                                String.class
                        ),
                        command.approvedAt()
                );

        try {
            dsl.execute(
                    "insert into configuration_revision_approval "
                            + "(logical_approval_request_identifier, "
                            + "merchant_identifier, "
                            + "configuration_revision_identifier, "
                            + "semantic_registry_release_identifier, "
                            + "validation_evidence_identifier, "
                            + "impact_review_evidence_identifier, "
                            + "resolved_package_evidence_identifier, "
                            + "approving_principal_identifier, "
                            + "controller_relationship_identifier, "
                            + "approved_at, "
                            + "reinstatement_basis_activation_request_identifier) "
                            + "values "
                            + "(?, ?, ?, ?, ?, ?, ?, ?, ?, "
                            + "cast(? as timestamptz), ?)",
                    approval.logicalApprovalRequestIdentifier(),
                    approval.merchantIdentifier(),
                    approval.configurationRevisionIdentifier(),
                    approval.semanticRegistryReleaseIdentifier(),
                    approval.validationEvidenceIdentifier(),
                    approval.impactReviewEvidenceIdentifier(),
                    approval.resolvedPackageEvidenceIdentifier(),
                    approval.approvingPrincipalIdentifier(),
                    approval.controllerRelationshipIdentifier(),
                    approval.approvedAt().toString(),
                    approval.reinstatementBasisActivationRequestIdentifier()
            );
        } catch (DataAccessException failure) {
            throw new ConfigurationRevisionApprovalPersistenceException(
                    ConfigurationRevisionApprovalFailureCategory
                            .APPROVAL_CONCURRENCY_CONFLICT,
                    "Reinstatement approval fact could not commit",
                    failure
            );
        }

        return approval;
    }

    @Override
    public Optional<ConfigurationReinstatementApproval>
            approvalByRequest(
                    String requestIdentifier
            ) {
        requireIdentifier(
                requestIdentifier
        );

        Record record =
                dsl.fetchOne(
                        "select * "
                                + "from configuration_revision_approval "
                                + "where logical_approval_request_identifier = ? "
                                + "and reinstatement_basis_activation_request_identifier "
                                + "is not null",
                        requestIdentifier
                );

        return Optional.ofNullable(
                record
        ).map(
                this::toApproval
        );
    }

    @Override
    public Optional<ConfigurationReinstatementApproval>
            currentApplicableApproval(
                    MerchantScope scope,
                    String configurationRevisionIdentifier
            ) {
        Objects.requireNonNull(
                scope,
                "scope"
        );

        requireIdentifier(
                configurationRevisionIdentifier
        );

        /*
         * A historical reinstatement approval is applicable only while:
         *
         * 1. its exact Controller Relationship remains current ACTIVE; and
         * 2. its exact Reinstatement Basis Activation still owns the current
         *    Configuration pointer.
         *
         * Same-revision recurrence therefore cannot revive the approval.
         */
        Record record =
                dsl.fetchOne(
                        "select a.* "
                                + "from configuration_revision_approval a "
                                + "join merchant_controller_relationship c "
                                + "on c.merchant_identifier = "
                                + "a.merchant_identifier "
                                + "and c.controller_relationship_identifier = "
                                + "a.controller_relationship_identifier "
                                + "and c.identity_identifier = "
                                + "a.approving_principal_identifier "
                                + "join "
                                + "merchant_current_configuration_activation m "
                                + "on m.merchant_identifier = "
                                + "a.merchant_identifier "
                                + "and m.activation_request_identifier = "
                                + "a.reinstatement_basis_activation_request_identifier "
                                + "where a.merchant_identifier = ? "
                                + "and a.configuration_revision_identifier = ? "
                                + "and a.reinstatement_basis_activation_request_identifier "
                                + "is not null "
                                + "and c.lifecycle = 'ACTIVE' "
                                + "order by a.approved_at desc, "
                                + "a.logical_approval_request_identifier desc "
                                + "limit 1",
                        scope.merchantIdentifier(),
                        configurationRevisionIdentifier
                );

        return Optional.ofNullable(
                record
        ).map(
                this::toApproval
        );
    }

    private Record rawApprovalByRequest(
            String requestIdentifier
    ) {
        return dsl.fetchOne(
                "select * "
                        + "from configuration_revision_approval "
                        + "where logical_approval_request_identifier = ?",
                requestIdentifier
        );
    }

    private ConfigurationReinstatementApproval toApproval(
            Record record
    ) {
        return new ConfigurationReinstatementApproval(
                record.get(
                        "logical_approval_request_identifier",
                        String.class
                ),
                record.get(
                        "merchant_identifier",
                        String.class
                ),
                record.get(
                        "configuration_revision_identifier",
                        String.class
                ),
                record.get(
                        "semantic_registry_release_identifier",
                        String.class
                ),
                record.get(
                        "reinstatement_basis_activation_request_identifier",
                        String.class
                ),
                record.get(
                        "validation_evidence_identifier",
                        String.class
                ),
                record.get(
                        "impact_review_evidence_identifier",
                        String.class
                ),
                record.get(
                        "resolved_package_evidence_identifier",
                        String.class
                ),
                record.get(
                        "approving_principal_identifier",
                        String.class
                ),
                record.get(
                        "controller_relationship_identifier",
                        String.class
                ),
                record.get(
                        "approved_at",
                        Instant.class
                )
        );
    }

    private static void requireAuthenticatedContext(
            ApproveConfigurationReinstatementCommand command,
            TrustedExecutionContext context
    ) {
        if (context == null
                || context.authentication().isEmpty()
                || !context
                .merchantScope()
                .equals(
                        command.merchantScope()
                )
                || !context
                .principal()
                .identifier()
                .equals(
                        command.approvingPrincipalIdentifier()
                )
                || !context
                .authentication()
                .orElseThrow()
                .identityIdentifier()
                .equals(
                        command.approvingPrincipalIdentifier()
                )) {

            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .AUTHENTICATED_PRINCIPAL_REQUIRED,
                    "Matching authenticated trusted principal is required"
            );
        }
    }

    private static ConfigurationReinstatementApproval
            requireSameIntent(
                    ApproveConfigurationReinstatementCommand command,
                    ConfigurationReinstatementApproval approval
            ) {
        boolean sameIntent =
                approval
                        .merchantIdentifier()
                        .equals(
                                command.merchantScope()
                                        .merchantIdentifier()
                        )
                        && approval
                        .configurationRevisionIdentifier()
                        .equals(
                                command.configurationRevisionIdentifier()
                        )
                        && approval
                        .reinstatementBasisActivationRequestIdentifier()
                        .equals(
                                command
                                        .reinstatementBasisActivationRequestIdentifier()
                        )
                        && approval
                        .validationEvidenceIdentifier()
                        .equals(
                                command.validationEvidenceIdentifier()
                        )
                        && approval
                        .impactReviewEvidenceIdentifier()
                        .equals(
                                command.impactReviewEvidenceIdentifier()
                        )
                        && approval
                        .approvingPrincipalIdentifier()
                        .equals(
                                command.approvingPrincipalIdentifier()
                        )
                        && approval
                        .approvedAt()
                        .equals(
                                command.approvedAt()
                        );

        if (!sameIntent) {
            throw failure(
                    ConfigurationRevisionApprovalFailureCategory
                            .APPROVAL_REQUEST_IDENTITY_CONFLICT,
                    "Reinstatement approval request identity "
                            + "was reused for different intent"
            );
        }

        return approval;
    }

    private void lock(
            String value,
            int seed
    ) {
        dsl.fetch(
                "select pg_advisory_xact_lock("
                        + "hashtextextended(cast(? as text), ?))",
                value,
                seed
        );
    }

    private static void requireIdentifier(
            String value
    ) {
        if (value == null
                || value.isBlank()) {

            throw new IllegalArgumentException(
                    "Identifier must not be blank"
            );
        }
    }

    private static ConfigurationRevisionApprovalPersistenceException failure(
            ConfigurationRevisionApprovalFailureCategory category,
            String message
    ) {
        return new ConfigurationRevisionApprovalPersistenceException(
                category,
                message
        );
    }
}
