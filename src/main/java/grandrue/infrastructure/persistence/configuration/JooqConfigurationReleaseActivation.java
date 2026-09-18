package grandrue.infrastructure.persistence.configuration;

import grandrue.application.MerchantScope;
import mainstreet.semantic.configuration.ActiveRelease;
import mainstreet.semantic.configuration.ConfigurationActivation;
import mainstreet.semantic.configuration.ConfigurationActivationAdmissionAuthority;
import mainstreet.semantic.configuration.ConfigurationActivationAdmissionEvidence;
import mainstreet.semantic.configuration.ConfigurationActivationAdmissionResult;
import mainstreet.semantic.configuration.ConfigurationActivationAuthorizationAuthority;
import mainstreet.semantic.configuration.ConfigurationActivationRequest;
import mainstreet.semantic.configuration.ConfigurationActivationResult;
import mainstreet.semantic.configuration.ConfigurationActivationStatus;
import mainstreet.semantic.configuration.ConfigurationPublication;
import mainstreet.semantic.configuration.ConfigurationRelease;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.configuration.ConfigurationReinstatementApproval;
import mainstreet.semantic.configuration.ConfigurationReinstatementApprovalApplicabilityAuthority;
import mainstreet.semantic.configuration.ConfigurationRevisionApproval;
import mainstreet.semantic.configuration.ConfigurationRevisionApprovalAuthority;
import mainstreet.semantic.configuration.InitialConfigurationRevisionApproval;
import mainstreet.semantic.configuration.InitialConfigurationRevisionApprovalApplicabilityAuthority;
import mainstreet.semantic.configuration.NonInitialConfigurationRevisionApproval;
import mainstreet.semantic.configuration.NonInitialConfigurationRevisionApprovalApplicabilityAuthority;
import mainstreet.semantic.configuration.SemanticCompatibilityAuthority;
import mainstreet.semantic.configuration.SemanticCompatibilityEvidence;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * PostgreSQL/jOOQ adapter for the accepted Merchant Configuration activation
 * boundary.
 *
 * <p>The Configuration Revision and Resolved Configuration Package remain
 * owned by the configuration/publication authority. This adapter persists only
 * immutable activation evidence and the merchant's current activation pointer.
 * It resolves current execution material by the exact persisted release
 * identifier and never substitutes the latest publication.</p>
 *
 * <p>Cross-registry activation is fail-closed unless exact MS-PROT-054
 * compatibility/migration evidence permits the directional transition from the
 * currently active release to the candidate release.</p>
 *
 * <p>Ordinary first activation is fail-closed unless the D3 authority resolves
 * a then-current Controller approval whose immutable revision and semantic
 * release affinity match the exact release candidate.</p>
 *
 * <p>Real non-initial activation is fail-closed unless an exact currently
 * applicable non-initial approval is available. That approval retains
 * validation, impact-review and exact Resolved Configuration Package affinity
 * for serving admission.</p>
 *
 * <p>Current activation actor authority is evaluated only after this adapter
 * acquires both the merchant activation serialization lock and the shared
 * Merchant Account current-authority lock. Merchant Controller transfer,
 * Merchant Account suspension/closure transitions and Configuration
 * activation therefore cannot cross the current-authority decision boundary
 * inconsistently.</p>
 *
 * <p>Successful activation also records durable publication intent for the
 * accepted ConfigurationRevisionActivated post-commit fact in the same
 * transaction. The intent is delivery/recovery infrastructure and does not
 * become Configuration or downstream business authority.</p>
 *
 * Reinstatement authority: MS-PROT-040 v1.8,
 * {@code designs/MS-PROT-040 v1.8 — Configuration Reinstatement Decision
 * Affinity Amendment.md}, §§10–12 and 14 — Reinstatement Classification
 * During Activation, Current Applicability of Reinstatement Approval,
 * Activation, and Retry and Idempotency.
 */
public final class JooqConfigurationReleaseActivation
        implements ConfigurationReleaseActivation {

    private static final ConfigurationActivationAdmissionAuthority
            LEGACY_PRE_D5C4_ADMISSION =
            (merchant, revision, release, packageId) ->
                    ConfigurationActivationAdmissionResult.rejected(
                            ConfigurationActivationStatus
                                    .DEPLOYMENT_ADMISSION_CONFLICT
                    );

    private static final
            NonInitialConfigurationRevisionApprovalApplicabilityAuthority
            LEGACY_PRE_R3_NON_INITIAL_APPROVAL =
            (merchantScope, revisionIdentifier) ->
                    Optional.empty();

    private static final
            ConfigurationReinstatementApprovalApplicabilityAuthority
            LEGACY_PRE_R3B_REINSTATEMENT_APPROVAL =
            (merchantScope, revisionIdentifier) -> Optional.empty();

    private static final Table<?> ACTIVATION =
            DSL.table(
                    DSL.name("configuration_activation")
            );

    private static final Table<?> CURRENT =
            DSL.table(
                    DSL.name(
                            "merchant_current_configuration_activation"
                    )
            );

    private static final Table<?> PUBLICATION_OUTBOX =
            DSL.table(
                    DSL.name(
                            "configuration_activation_publication_intent"
                    )
            );

    private static final Field<String> PUBLICATION_INTENT_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "publication_intent_identifier"
                    ),
                    String.class
            );

    private static final Field<String> ACTIVATION_REQUEST_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "activation_request_identifier"
                    ),
                    String.class
            );

    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "merchant_identifier"
                    ),
                    String.class
            );

    private static final Field<String>
            CONFIGURATION_REVISION_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "configuration_revision_identifier"
                    ),
                    String.class
            );

    private static final Field<String> RELEASE_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "release_identifier"
                    ),
                    String.class
            );

    private static final Field<String>
            EXPECTED_CURRENT_CONFIGURATION_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "expected_current_configuration_identifier"
                    ),
                    String.class
            );

    private static final Field<String>
            INITIATING_PRINCIPAL_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "initiating_principal_identifier"
                    ),
                    String.class
            );

    private static final Field<Instant> ACTIVATED_AT =
            DSL.field(
                    DSL.name(
                            "activated_at"
                    ),
                    Instant.class
            );

    private static final Field<Instant> OCCURRED_AT =
            DSL.field(
                    DSL.name(
                            "occurred_at"
                    ),
                    Instant.class
            );

    private static final Field<String>
            REPLACED_CONFIGURATION_REVISION_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "replaced_configuration_revision_identifier"
                    ),
                    String.class
            );

    private static final Field<Integer>
            ADMISSION_CONTRACT_VERSION =
            DSL.field(
                    DSL.name(
                            "admission_contract_version"
                    ),
                    Integer.class
            );

    private static final Field<String>
            SEMANTIC_REGISTRY_RELEASE_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "semantic_registry_release_identifier"
                    ),
                    String.class
            );

    private static final Field<String>
            VALIDATION_ADMISSION_DECISION_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "validation_admission_decision_identifier"
                    ),
                    String.class
            );

    private static final Field<String>
            VALIDATION_ADMISSION_PURPOSE =
            DSL.field(
                    DSL.name(
                            "validation_admission_purpose"
                    ),
                    String.class
            );

    private static final Field<String>
            BUSINESS_ACTIVITY_ADMISSION_DECISION_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "business_activity_admission_decision_identifier"
                    ),
                    String.class
            );

    private static final Field<String>
            BUSINESS_ACTIVITY_ADMISSION_PURPOSE =
            DSL.field(
                    DSL.name(
                            "business_activity_admission_purpose"
                    ),
                    String.class
            );

    private static final Field<String>
            RESOLVED_PACKAGE_EVIDENCE_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "resolved_package_evidence_identifier"
                    ),
                    String.class
            );

    private static final Field<String>
            REQUIREMENT_SET_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "requirement_set_identifier"
                    ),
                    String.class
            );

    private static final Field<String>
            SERVING_GENERATION_IDENTIFIER =
            DSL.field(
                    DSL.name(
                            "serving_generation_identifier"
                    ),
                    String.class
            );

    private static final Field<Long>
            SERVING_GENERATION_EPOCH =
            DSL.field(
                    DSL.name(
                            "serving_generation_epoch"
                    ),
                    Long.class
            );

    private static final Field<String>
            PACKAGED_BUNDLE_CONTENT_DIGEST =
            DSL.field(
                    DSL.name(
                            "packaged_bundle_content_digest"
                    ),
                    String.class
            );

    private final ConfigurationPublication publication;

    private final ConfigurationRevisionApprovalAuthority
            approvalAuthority;

    private final
            InitialConfigurationRevisionApprovalApplicabilityAuthority
            initialApprovalAuthority;

    private final
            NonInitialConfigurationRevisionApprovalApplicabilityAuthority
            nonInitialApprovalAuthority;

    private final ConfigurationReinstatementApprovalApplicabilityAuthority
            reinstatementApprovalAuthority;

    private final ConfigurationActivationAuthorizationAuthority
            authorizationAuthority;

    private final SemanticCompatibilityAuthority
            compatibilityAuthority;

    private final ConfigurationActivationAdmissionAuthority
            admissionAuthority;

    private final Clock clock;

    private final DSLContext dsl;

    private final TransactionTemplate transactionTemplate;

    private final Supplier<String>
            publicationIntentIdentifierFactory;

    /**
     * Backward-compatible construction is fail-closed for ordinary first
     * activation and semantic-registry transitions.
     */
    JooqConfigurationReleaseActivation(
            ConfigurationPublication publication,
            ConfigurationRevisionApprovalAuthority approvalAuthority,
            ConfigurationActivationAuthorizationAuthority
                    authorizationAuthority,
            Clock clock,
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this(
                publication,
                approvalAuthority,
                noInitialApprovalAuthority(),
                authorizationAuthority,
                noCompatibilityAuthority(),
                clock,
                dsl,
                transactionManager,
                () -> UUID.randomUUID().toString()
        );
    }

    JooqConfigurationReleaseActivation(
            ConfigurationPublication publication,
            ConfigurationRevisionApprovalAuthority approvalAuthority,
            ConfigurationActivationAuthorizationAuthority
                    authorizationAuthority,
            SemanticCompatibilityAuthority compatibilityAuthority,
            Clock clock,
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            Supplier<String> publicationIntentIdentifierFactory
    ) {
        this(
                publication,
                approvalAuthority,
                noInitialApprovalAuthority(),
                authorizationAuthority,
                compatibilityAuthority,
                clock,
                dsl,
                transactionManager,
                publicationIntentIdentifierFactory
        );
    }

    JooqConfigurationReleaseActivation(
            ConfigurationPublication publication,
            ConfigurationRevisionApprovalAuthority approvalAuthority,
            InitialConfigurationRevisionApprovalApplicabilityAuthority
                    initialApprovalAuthority,
            ConfigurationActivationAuthorizationAuthority
                    authorizationAuthority,
            Clock clock,
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this(
                publication,
                approvalAuthority,
                initialApprovalAuthority,
                authorizationAuthority,
                noCompatibilityAuthority(),
                clock,
                dsl,
                transactionManager,
                () -> UUID.randomUUID().toString()
        );
    }

    JooqConfigurationReleaseActivation(
            ConfigurationPublication publication,
            ConfigurationRevisionApprovalAuthority approvalAuthority,
            ConfigurationActivationAuthorizationAuthority
                    authorizationAuthority,
            SemanticCompatibilityAuthority compatibilityAuthority,
            Clock clock,
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this(
                publication,
                approvalAuthority,
                noInitialApprovalAuthority(),
                authorizationAuthority,
                compatibilityAuthority,
                clock,
                dsl,
                transactionManager,
                () -> UUID.randomUUID().toString()
        );
    }

    JooqConfigurationReleaseActivation(
            ConfigurationPublication publication,
            ConfigurationRevisionApprovalAuthority approvalAuthority,
            InitialConfigurationRevisionApprovalApplicabilityAuthority
                    initialApprovalAuthority,
            ConfigurationActivationAuthorizationAuthority
                    authorizationAuthority,
            SemanticCompatibilityAuthority compatibilityAuthority,
            Clock clock,
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this(
                publication,
                approvalAuthority,
                initialApprovalAuthority,
                authorizationAuthority,
                compatibilityAuthority,
                clock,
                dsl,
                transactionManager,
                () -> UUID.randomUUID().toString()
        );
    }

    JooqConfigurationReleaseActivation(
            ConfigurationPublication publication,
            ConfigurationRevisionApprovalAuthority approvalAuthority,
            ConfigurationActivationAuthorizationAuthority
                    authorizationAuthority,
            Clock clock,
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            Supplier<String> publicationIntentIdentifierFactory
    ) {
        this(
                publication,
                approvalAuthority,
                noInitialApprovalAuthority(),
                authorizationAuthority,
                noCompatibilityAuthority(),
                clock,
                dsl,
                transactionManager,
                publicationIntentIdentifierFactory
        );
    }

    JooqConfigurationReleaseActivation(
            ConfigurationPublication publication,
            ConfigurationRevisionApprovalAuthority approvalAuthority,
            InitialConfigurationRevisionApprovalApplicabilityAuthority
                    initialApprovalAuthority,
            ConfigurationActivationAuthorizationAuthority
                    authorizationAuthority,
            Clock clock,
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            Supplier<String> publicationIntentIdentifierFactory
    ) {
        this(
                publication,
                approvalAuthority,
                initialApprovalAuthority,
                authorizationAuthority,
                noCompatibilityAuthority(),
                clock,
                dsl,
                transactionManager,
                publicationIntentIdentifierFactory
        );
    }

    JooqConfigurationReleaseActivation(
            ConfigurationPublication publication,
            ConfigurationRevisionApprovalAuthority approvalAuthority,
            InitialConfigurationRevisionApprovalApplicabilityAuthority
                    initialApprovalAuthority,
            ConfigurationActivationAuthorizationAuthority
                    authorizationAuthority,
            SemanticCompatibilityAuthority compatibilityAuthority,
            Clock clock,
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            Supplier<String> publicationIntentIdentifierFactory
    ) {
        this(
                publication,
                approvalAuthority,
                initialApprovalAuthority,
                authorizationAuthority,
                compatibilityAuthority,
                noAdmissionAuthority(),
                clock,
                dsl,
                transactionManager,
                publicationIntentIdentifierFactory
        );
    }

    /**
     * Existing D5c4 constructor retained for source compatibility.
     *
     * <p>Because it does not supply exact non-initial approval affinity, a
     * real non-initial serving-admission attempt through this constructor
     * fails closed.</p>
     */
    public JooqConfigurationReleaseActivation(
            ConfigurationPublication publication,
            ConfigurationRevisionApprovalAuthority approvalAuthority,
            InitialConfigurationRevisionApprovalApplicabilityAuthority
                    initialApprovalAuthority,
            ConfigurationActivationAuthorizationAuthority
                    authorizationAuthority,
            SemanticCompatibilityAuthority compatibilityAuthority,
            ConfigurationActivationAdmissionAuthority
                    admissionAuthority,
            Clock clock,
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            Supplier<String> publicationIntentIdentifierFactory
    ) {
        this(
                publication,
                approvalAuthority,
                initialApprovalAuthority,
                LEGACY_PRE_R3_NON_INITIAL_APPROVAL,
                authorizationAuthority,
                compatibilityAuthority,
                admissionAuthority,
                clock,
                dsl,
                transactionManager,
                publicationIntentIdentifierFactory
        );
    }

    /**
     * Complete R3 activation composition.
     */
    public JooqConfigurationReleaseActivation(
            ConfigurationPublication publication,
            ConfigurationRevisionApprovalAuthority approvalAuthority,
            InitialConfigurationRevisionApprovalApplicabilityAuthority
                    initialApprovalAuthority,
            NonInitialConfigurationRevisionApprovalApplicabilityAuthority
                    nonInitialApprovalAuthority,
            ConfigurationActivationAuthorizationAuthority
                    authorizationAuthority,
            SemanticCompatibilityAuthority compatibilityAuthority,
            ConfigurationActivationAdmissionAuthority
                    admissionAuthority,
            Clock clock,
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            Supplier<String> publicationIntentIdentifierFactory
    ) {
        this(
                publication,
                approvalAuthority,
                initialApprovalAuthority,
                nonInitialApprovalAuthority,
                LEGACY_PRE_R3B_REINSTATEMENT_APPROVAL,
                authorizationAuthority,
                compatibilityAuthority,
                admissionAuthority,
                clock,
                dsl,
                transactionManager,
                publicationIntentIdentifierFactory
        );
    }

    /**
     * Complete R3B activation composition.
     */
    public JooqConfigurationReleaseActivation(
            ConfigurationPublication publication,
            ConfigurationRevisionApprovalAuthority approvalAuthority,
            InitialConfigurationRevisionApprovalApplicabilityAuthority
                    initialApprovalAuthority,
            NonInitialConfigurationRevisionApprovalApplicabilityAuthority
                    nonInitialApprovalAuthority,
            ConfigurationReinstatementApprovalApplicabilityAuthority
                    reinstatementApprovalAuthority,
            ConfigurationActivationAuthorizationAuthority
                    authorizationAuthority,
            SemanticCompatibilityAuthority compatibilityAuthority,
            ConfigurationActivationAdmissionAuthority admissionAuthority,
            Clock clock,
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            Supplier<String> publicationIntentIdentifierFactory
    ) {
        this.publication =
                Objects.requireNonNull(
                        publication,
                        "publication"
                );

        this.approvalAuthority =
                Objects.requireNonNull(
                        approvalAuthority,
                        "approvalAuthority"
                );

        this.initialApprovalAuthority =
                Objects.requireNonNull(
                        initialApprovalAuthority,
                        "initialApprovalAuthority"
                );

        this.nonInitialApprovalAuthority =
                Objects.requireNonNull(
                        nonInitialApprovalAuthority,
                        "nonInitialApprovalAuthority"
                );

        this.reinstatementApprovalAuthority =
                Objects.requireNonNull(
                        reinstatementApprovalAuthority,
                        "reinstatementApprovalAuthority"
                );

        this.authorizationAuthority =
                Objects.requireNonNull(
                        authorizationAuthority,
                        "authorizationAuthority"
                );

        this.compatibilityAuthority =
                Objects.requireNonNull(
                        compatibilityAuthority,
                        "compatibilityAuthority"
                );

        this.admissionAuthority =
                Objects.requireNonNull(
                        admissionAuthority,
                        "admissionAuthority"
                );

        this.clock =
                Objects.requireNonNull(
                        clock,
                        "clock"
                );

        this.dsl =
                Objects.requireNonNull(
                        dsl,
                        "dsl"
                );

        this.transactionTemplate =
                new TransactionTemplate(
                        Objects.requireNonNull(
                                transactionManager,
                                "transactionManager"
                        )
                );

        this.publicationIntentIdentifierFactory =
                Objects.requireNonNull(
                        publicationIntentIdentifierFactory,
                        "publicationIntentIdentifierFactory"
                );
    }

    @Override
    public ConfigurationActivationResult activate(
            ConfigurationActivationRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request"
        );

        ConfigurationActivationResult result =
                transactionTemplate.execute(status -> {
                    lockActivationRequest(
                            request.activationRequestIdentifier()
                    );

                    PersistedActivation committed =
                            persistedActivation(
                                    request.activationRequestIdentifier()
                            ).orElse(null);

                    /*
                     * Historical exact replay is deliberately resolved before
                     * any current business-authority checks. It creates no new
                     * activation mutation. Replay is valid only while the exact
                     * committed activation still owns the merchant's durable
                     * current activation pointer.
                     */
                    if (committed != null) {
                        requireSameIntent(
                                request,
                                committed
                        );

                        ConfigurationActivation historical =
                                committed.activation();

                        lockMerchantActivation(
                                historical.merchantIdentifier()
                        );

                        if (!isCurrentActivation(historical)) {
                            return ConfigurationActivationResult.rejected(
                                    ConfigurationActivationStatus
                                            .ACTIVATION_CONFLICT
                            );
                        }

                        return ConfigurationActivationResult.success(
                                historical
                        );
                    }

                    Optional<ConfigurationRelease> releaseCandidate =
                            publication.release(
                                    request.releaseIdentifier()
                            );

                    if (releaseCandidate.isEmpty()) {
                        return ConfigurationActivationResult.rejected(
                                ConfigurationActivationStatus
                                        .VALIDATION_REJECTION
                        );
                    }

                    ConfigurationRelease release =
                            releaseCandidate.orElseThrow();

                    /*
                     * Configuration activation writers serialize first,
                     * preserving the established activation lock order.
                     */
                    lockMerchantActivation(
                            release.merchantIdentifier()
                    );

                    /*
                     * Shared Merchant Account / Controller current-authority
                     * fence. Merchant Controller transfer, suspension,
                     * closure and approval-currentness transitions use seed 76.
                     *
                     * All mutable actor/account authority below is therefore
                     * evaluated while that authority cannot cross underneath
                     * this transaction.
                     */
                    lockMerchantAuthority(
                            release.merchantIdentifier()
                    );

                    if (!authorizationAuthority.isAuthorized(
                            request.initiatingPrincipalIdentifier(),
                            release.merchantIdentifier(),
                            release.configurationIdentifier()
                    )) {
                        return ConfigurationActivationResult.rejected(
                                ConfigurationActivationStatus
                                        .AUTHORISATION_REJECTION
                        );
                    }

                    Optional<ActiveRelease> current =
                            current(
                                    release.merchantIdentifier()
                            );

                    Optional<String> currentRevisionIdentifier =
                            current.map(
                                    active ->
                                            active.release()
                                                    .configurationIdentifier()
                            );

                    if (!request
                            .expectedCurrentConfigurationIdentifier()
                            .equals(
                                    currentRevisionIdentifier
                            )) {
                        return ConfigurationActivationResult.rejected(
                                ConfigurationActivationStatus
                                        .ACTIVATION_CONFLICT
                        );
                    }

                    boolean reinstatement = false;

                    if (current.isPresent()) {
                        String targetRevisionIdentifier =
                                release.configurationIdentifier();

                        String currentRevision =
                                currentRevisionIdentifier.orElseThrow();

                        if (targetRevisionIdentifier.equals(
                                currentRevision
                        )) {
                            return ConfigurationActivationResult.rejected(
                                    ConfigurationActivationStatus
                                            .ACTIVATION_CONFLICT
                            );
                        }

                        /*
                         * MS-PROT-040 v1.8 classifies a non-current target that
                         * has any prior committed activation for this merchant
                         * as reinstatement before historical base affinity is
                         * considered.
                         *
                         * This classification precedes historical base
                         * affinity so reinstatement cannot fall through to
                         * either initial or forward-replacement approval.
                         */
                        reinstatement = hasPriorCommittedActivation(
                                release.merchantIdentifier(),
                                targetRevisionIdentifier
                        );

                        if (!reinstatement && !release
                                .configuration()
                                .baseConfigurationIdentifier()
                                .equals(
                                        currentRevisionIdentifier
                                )) {
                            return ConfigurationActivationResult.rejected(
                                    ConfigurationActivationStatus
                                            .ACTIVATION_CONFLICT
                            );
                        }
                    } else if (release
                            .configuration()
                            .baseConfigurationIdentifier()
                            .isPresent()) {
                        return ConfigurationActivationResult.rejected(
                                ConfigurationActivationStatus
                                        .ACTIVATION_CONFLICT
                        );
                    }

                    Optional<String> approvedPackageIdentifier =
                            Optional.empty();

                    if (current.isEmpty()) {
                        Optional<
                                InitialConfigurationRevisionApproval
                                > approval =
                                initialApprovalAuthority
                                        .currentApplicableApproval(
                                                new MerchantScope(
                                                        release
                                                                .merchantIdentifier()
                                                ),
                                                release
                                                        .configurationIdentifier()
                                        );

                        if (approval.isEmpty()
                                || !initialApprovalMatchesRelease(
                                        approval.orElseThrow(),
                                        release
                                )) {
                            return ConfigurationActivationResult
                                    .rejected(
                                            ConfigurationActivationStatus
                                                    .APPROVAL_REQUIRED
                                    );
                        }

                        approvedPackageIdentifier =
                                Optional.of(
                                        approval.orElseThrow()
                                                .resolvedPackageEvidenceIdentifier()
                                );

                    } else if (reinstatement) {
                        String reinstatementBasisActivationIdentifier =
                                currentActivationRequestIdentifier(
                                        release.merchantIdentifier()
                                ).orElseThrow(() ->
                                        new IllegalStateException(
                                                "Current Configuration release "
                                                        + "has no exact "
                                                        + "activation pointer"
                                        )
                                );

                        Optional<ConfigurationReinstatementApproval>
                                approval =
                                reinstatementApprovalAuthority
                                        .currentApplicableApproval(
                                                new MerchantScope(
                                                        release
                                                                .merchantIdentifier()
                                                ),
                                                release
                                                        .configurationIdentifier()
                                        );

                        if (approval.isEmpty()
                                || !reinstatementApprovalMatchesRelease(
                                        approval.orElseThrow(),
                                        release,
                                        reinstatementBasisActivationIdentifier
                                )) {
                            return ConfigurationActivationResult
                                    .rejected(
                                            ConfigurationActivationStatus
                                                    .APPROVAL_REQUIRED
                                    );
                        }

                        approvedPackageIdentifier =
                                Optional.of(
                                        approval.orElseThrow()
                                                .resolvedPackageEvidenceIdentifier()
                                );

                    } else {
                        if (nonInitialApprovalAuthority
                                != LEGACY_PRE_R3_NON_INITIAL_APPROVAL) {

                            Optional<
                                    NonInitialConfigurationRevisionApproval
                                    > approval =
                                    nonInitialApprovalAuthority
                                            .currentApplicableApproval(
                                                    new MerchantScope(
                                                            release
                                                                    .merchantIdentifier()
                                                    ),
                                                    release
                                                            .configurationIdentifier()
                                            );

                            if (approval.isEmpty()
                                    || !nonInitialApprovalMatchesRelease(
                                            approval.orElseThrow(),
                                            release
                                    )) {
                                return ConfigurationActivationResult
                                        .rejected(
                                                ConfigurationActivationStatus
                                                        .APPROVAL_REQUIRED
                                        );
                            }

                            approvedPackageIdentifier =
                                    Optional.of(
                                            approval.orElseThrow()
                                                    .resolvedPackageEvidenceIdentifier()
                                    );

                        } else {
                            /*
                             * The legacy reduced approval fact does not carry
                             * exact approved package affinity. It may support
                             * older in-memory/pre-admission fixtures but cannot
                             * authorize a real serving-admission decision.
                             */
                            if (admissionAuthority
                                    != LEGACY_PRE_D5C4_ADMISSION) {
                                return ConfigurationActivationResult
                                        .rejected(
                                                ConfigurationActivationStatus
                                                        .APPROVAL_REQUIRED
                                        );
                            }

                            Optional<ConfigurationRevisionApproval>
                                    approval =
                                    approvalAuthority.approvalFor(
                                            release.merchantIdentifier(),
                                            release.configurationIdentifier()
                                    );

                            if (approval.isEmpty()
                                    || !approvalMatchesRelease(
                                            approval.orElseThrow(),
                                            release
                                    )) {
                                return ConfigurationActivationResult
                                        .rejected(
                                                ConfigurationActivationStatus
                                                        .APPROVAL_REQUIRED
                                        );
                            }
                        }
                    }

                    if (current.isPresent()) {
                        ConfigurationRelease source =
                                current.orElseThrow()
                                        .release();

                        if (!source
                                .semanticRegistryVersion()
                                .equals(
                                        release.semanticRegistryVersion()
                                )
                                && !compatibilityPermits(
                                        source,
                                        release
                                )) {
                            return ConfigurationActivationResult.rejected(
                                    ConfigurationActivationStatus
                                            .VALIDATION_REJECTION
                            );
                        }
                    }

                    Optional<ConfigurationActivationAdmissionEvidence>
                            admissionEvidence =
                            Optional.empty();

                    if (admissionAuthority
                            != LEGACY_PRE_D5C4_ADMISSION) {

                        ConfigurationActivationAdmissionResult admission =
                                admissionAuthority.admit(
                                        new MerchantScope(
                                                release.merchantIdentifier()
                                        ),
                                        release.configurationIdentifier(),
                                        release.semanticRegistryVersion(),
                                        approvedPackageIdentifier
                                );

                        if (admission.status()
                                != ConfigurationActivationStatus.SUCCESS) {
                            return ConfigurationActivationResult.rejected(
                                    admission.status()
                            );
                        }

                        admissionEvidence =
                                admission.evidence();
                    }

                    ConfigurationActivation activation =
                            new ConfigurationActivation(
                                    request
                                            .activationRequestIdentifier(),
                                    release
                                            .merchantIdentifier(),
                                    release
                                            .configurationIdentifier(),
                                    release
                                            .releaseIdentifier(),
                                    clock.instant(),
                                    currentRevisionIdentifier,
                                    admissionEvidence
                            );

                    insertActivationEvidence(
                            request,
                            activation
                    );

                    insertPublicationIntent(
                            activation
                    );

                    moveCurrentPointer(
                            activation,
                            currentRevisionIdentifier
                    );

                    return ConfigurationActivationResult.success(
                            activation
                    );
                });

        return Objects.requireNonNull(
                result,
                "Configuration activation transaction returned no result"
        );
    }

    @Override
    public Optional<ActiveRelease> current(
            String merchantIdentifier
    ) {
        requireIdentifier(
                merchantIdentifier,
                "Merchant identifier"
        );

        String releaseIdentifier =
                dsl.select(
                                RELEASE_IDENTIFIER
                        )
                        .from(
                                CURRENT
                        )
                        .where(
                                MERCHANT_IDENTIFIER.eq(
                                        merchantIdentifier
                                )
                        )
                        .fetchOne(
                                RELEASE_IDENTIFIER
                        );

        if (releaseIdentifier == null) {
            return Optional.empty();
        }

        ConfigurationRelease release =
                publication.release(
                                releaseIdentifier
                        )
                        .orElseThrow(
                                () -> new IllegalStateException(
                                        "Current configuration activation "
                                                + "references unavailable "
                                                + "exact release: "
                                                + releaseIdentifier
                                )
                        );

        if (!release
                .merchantIdentifier()
                .equals(
                        merchantIdentifier
                )) {
            throw new IllegalStateException(
                    "Persisted current release belongs to "
                            + "a different merchant: "
                            + releaseIdentifier
            );
        }

        return Optional.of(
                new ActiveRelease(
                        release
                )
        );
    }

    @Override
    public Optional<ConfigurationActivation>
            committedActivation(
                    String activationRequestIdentifier
            ) {
        requireIdentifier(
                activationRequestIdentifier,
                "Activation request identifier"
        );

        return persistedActivation(
                activationRequestIdentifier
        ).map(
                PersistedActivation::activation
        );
    }

    private boolean isCurrentActivation(
            ConfigurationActivation activation
    ) {
        String currentActivationRequestIdentifier =
                dsl.select(
                                ACTIVATION_REQUEST_IDENTIFIER
                        )
                        .from(
                                CURRENT
                        )
                        .where(
                                MERCHANT_IDENTIFIER.eq(
                                        activation.merchantIdentifier()
                                )
                        )
                        .fetchOne(
                                ACTIVATION_REQUEST_IDENTIFIER
                        );

        return activation
                .activationRequestIdentifier()
                .equals(
                        currentActivationRequestIdentifier
                );
    }

    private boolean hasPriorCommittedActivation(
            String merchantIdentifier,
            String configurationRevisionIdentifier
    ) {
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(
                                ACTIVATION
                        )
                        .where(
                                MERCHANT_IDENTIFIER.eq(
                                        merchantIdentifier
                                )
                        )
                        .and(
                                CONFIGURATION_REVISION_IDENTIFIER.eq(
                                        configurationRevisionIdentifier
                                )
                        )
        );
    }

    private Optional<String> currentActivationRequestIdentifier(
            String merchantIdentifier
    ) {
        return Optional.ofNullable(
                dsl.select(
                                ACTIVATION_REQUEST_IDENTIFIER
                        )
                        .from(
                                CURRENT
                        )
                        .where(
                                MERCHANT_IDENTIFIER.eq(
                                        merchantIdentifier
                                )
                        )
                        .fetchOne(
                                ACTIVATION_REQUEST_IDENTIFIER
                        )
        );
    }

    private boolean compatibilityPermits(
            ConfigurationRelease source,
            ConfigurationRelease target
    ) {
        return compatibilityAuthority
                .evidenceFor(
                        source.merchantIdentifier(),
                        source.releaseIdentifier(),
                        target.releaseIdentifier()
                )
                .filter(
                        evidence ->
                                evidence.matches(
                                        source,
                                        target
                                )
                )
                .filter(
                        SemanticCompatibilityEvidence
                                ::permitsActivation
                )
                .isPresent();
    }

    private Optional<PersistedActivation> persistedActivation(
            String activationRequestIdentifier
    ) {
        Record record =
                dsl.select(
                                ACTIVATION_REQUEST_IDENTIFIER,
                                MERCHANT_IDENTIFIER,
                                CONFIGURATION_REVISION_IDENTIFIER,
                                RELEASE_IDENTIFIER,
                                EXPECTED_CURRENT_CONFIGURATION_IDENTIFIER,
                                INITIATING_PRINCIPAL_IDENTIFIER,
                                ACTIVATED_AT,
                                REPLACED_CONFIGURATION_REVISION_IDENTIFIER,
                                ADMISSION_CONTRACT_VERSION,
                                SEMANTIC_REGISTRY_RELEASE_IDENTIFIER,
                                VALIDATION_ADMISSION_DECISION_IDENTIFIER,
                                BUSINESS_ACTIVITY_ADMISSION_DECISION_IDENTIFIER,
                                RESOLVED_PACKAGE_EVIDENCE_IDENTIFIER,
                                REQUIREMENT_SET_IDENTIFIER,
                                SERVING_GENERATION_IDENTIFIER,
                                SERVING_GENERATION_EPOCH,
                                PACKAGED_BUNDLE_CONTENT_DIGEST
                        )
                        .from(
                                ACTIVATION
                        )
                        .where(
                                ACTIVATION_REQUEST_IDENTIFIER.eq(
                                        activationRequestIdentifier
                                )
                        )
                        .fetchOne();

        if (record == null) {
            return Optional.empty();
        }

        Optional<ConfigurationActivationAdmissionEvidence>
                admissionEvidence =
                record.get(
                        ADMISSION_CONTRACT_VERSION
                ) == null
                        ? Optional.empty()
                        : Optional.of(
                                new ConfigurationActivationAdmissionEvidence(
                                        record.get(
                                                SEMANTIC_REGISTRY_RELEASE_IDENTIFIER
                                        ),
                                        record.get(
                                                VALIDATION_ADMISSION_DECISION_IDENTIFIER
                                        ),
                                        record.get(
                                                BUSINESS_ACTIVITY_ADMISSION_DECISION_IDENTIFIER
                                        ),
                                        record.get(
                                                RESOLVED_PACKAGE_EVIDENCE_IDENTIFIER
                                        ),
                                        record.get(
                                                REQUIREMENT_SET_IDENTIFIER
                                        ),
                                        record.get(
                                                SERVING_GENERATION_IDENTIFIER
                                        ),
                                        record.get(
                                                SERVING_GENERATION_EPOCH
                                        ),
                                        record.get(
                                                PACKAGED_BUNDLE_CONTENT_DIGEST
                                        )
                                )
                        );

        ConfigurationActivation activation =
                new ConfigurationActivation(
                        record.get(
                                ACTIVATION_REQUEST_IDENTIFIER
                        ),
                        record.get(
                                MERCHANT_IDENTIFIER
                        ),
                        record.get(
                                CONFIGURATION_REVISION_IDENTIFIER
                        ),
                        record.get(
                                RELEASE_IDENTIFIER
                        ),
                        record.get(
                                ACTIVATED_AT
                        ),
                        Optional.ofNullable(
                                record.get(
                                        REPLACED_CONFIGURATION_REVISION_IDENTIFIER
                                )
                        ),
                        admissionEvidence
                );

        return Optional.of(
                new PersistedActivation(
                        activation,
                        Optional.ofNullable(
                                record.get(
                                        EXPECTED_CURRENT_CONFIGURATION_IDENTIFIER
                                )
                        ),
                        record.get(
                                INITIATING_PRINCIPAL_IDENTIFIER
                        )
                )
        );
    }

    private void insertActivationEvidence(
            ConfigurationActivationRequest request,
            ConfigurationActivation activation
    ) {
        if (activation
                .admissionEvidence()
                .isEmpty()) {

            dsl.insertInto(
                            ACTIVATION
                    )
                    .columns(
                            ACTIVATION_REQUEST_IDENTIFIER,
                            MERCHANT_IDENTIFIER,
                            CONFIGURATION_REVISION_IDENTIFIER,
                            RELEASE_IDENTIFIER,
                            EXPECTED_CURRENT_CONFIGURATION_IDENTIFIER,
                            INITIATING_PRINCIPAL_IDENTIFIER,
                            ACTIVATED_AT,
                            REPLACED_CONFIGURATION_REVISION_IDENTIFIER
                    )
                    .values(
                            activation
                                    .activationRequestIdentifier(),
                            activation
                                    .merchantIdentifier(),
                            activation
                                    .configurationRevisionIdentifier(),
                            activation
                                    .releaseIdentifier(),
                            request
                                    .expectedCurrentConfigurationIdentifier()
                                    .orElse(null),
                            request
                                    .initiatingPrincipalIdentifier(),
                            activation
                                    .activatedAt(),
                            activation
                                    .replacedConfigurationRevisionIdentifier()
                                    .orElse(null)
                    )
                    .execute();

            return;
        }

        ConfigurationActivationAdmissionEvidence evidence =
                activation
                        .admissionEvidence()
                        .orElseThrow();

        dsl.insertInto(
                        ACTIVATION
                )
                .columns(
                        ACTIVATION_REQUEST_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        CONFIGURATION_REVISION_IDENTIFIER,
                        RELEASE_IDENTIFIER,
                        EXPECTED_CURRENT_CONFIGURATION_IDENTIFIER,
                        INITIATING_PRINCIPAL_IDENTIFIER,
                        ACTIVATED_AT,
                        REPLACED_CONFIGURATION_REVISION_IDENTIFIER,
                        ADMISSION_CONTRACT_VERSION,
                        SEMANTIC_REGISTRY_RELEASE_IDENTIFIER,
                        VALIDATION_ADMISSION_DECISION_IDENTIFIER,
                        VALIDATION_ADMISSION_PURPOSE,
                        BUSINESS_ACTIVITY_ADMISSION_DECISION_IDENTIFIER,
                        BUSINESS_ACTIVITY_ADMISSION_PURPOSE,
                        RESOLVED_PACKAGE_EVIDENCE_IDENTIFIER,
                        REQUIREMENT_SET_IDENTIFIER,
                        SERVING_GENERATION_IDENTIFIER,
                        SERVING_GENERATION_EPOCH,
                        PACKAGED_BUNDLE_CONTENT_DIGEST
                )
                .values(
                        activation
                                .activationRequestIdentifier(),
                        activation
                                .merchantIdentifier(),
                        activation
                                .configurationRevisionIdentifier(),
                        activation
                                .releaseIdentifier(),
                        request
                                .expectedCurrentConfigurationIdentifier()
                                .orElse(null),
                        request
                                .initiatingPrincipalIdentifier(),
                        activation
                                .activatedAt(),
                        activation
                                .replacedConfigurationRevisionIdentifier()
                                .orElse(null),
                        1,
                        evidence
                                .semanticRegistryReleaseIdentifier(),
                        evidence
                                .validationAdmissionDecisionIdentifier(),
                        "NEW_CONFIGURATION_VALIDATION",
                        evidence
                                .businessActivityAdmissionDecisionIdentifier(),
                        "NEW_BUSINESS_ACTIVITY",
                        evidence
                                .resolvedPackageEvidenceIdentifier(),
                        evidence
                                .requirementSetIdentifier(),
                        evidence
                                .servingGenerationIdentifier(),
                        evidence
                                .servingGenerationEpoch(),
                        evidence
                                .packagedBundleContentDigest()
                )
                .execute();
    }

    private void insertPublicationIntent(
            ConfigurationActivation activation
    ) {
        String publicationIntentIdentifier =
                publicationIntentIdentifierFactory.get();

        requireIdentifier(
                publicationIntentIdentifier,
                "Publication intent identifier"
        );

        dsl.insertInto(
                        PUBLICATION_OUTBOX
                )
                .columns(
                        PUBLICATION_INTENT_IDENTIFIER,
                        ACTIVATION_REQUEST_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        CONFIGURATION_REVISION_IDENTIFIER,
                        RELEASE_IDENTIFIER,
                        OCCURRED_AT
                )
                .values(
                        publicationIntentIdentifier,
                        activation
                                .activationRequestIdentifier(),
                        activation
                                .merchantIdentifier(),
                        activation
                                .configurationRevisionIdentifier(),
                        activation
                                .releaseIdentifier(),
                        activation
                                .activatedAt()
                )
                .execute();
    }

    private void moveCurrentPointer(
            ConfigurationActivation activation,
            Optional<String> currentRevisionIdentifier
    ) {
        if (currentRevisionIdentifier.isEmpty()) {
            dsl.insertInto(
                            CURRENT
                    )
                    .columns(
                            MERCHANT_IDENTIFIER,
                            ACTIVATION_REQUEST_IDENTIFIER,
                            CONFIGURATION_REVISION_IDENTIFIER,
                            RELEASE_IDENTIFIER
                    )
                    .values(
                            activation
                                    .merchantIdentifier(),
                            activation
                                    .activationRequestIdentifier(),
                            activation
                                    .configurationRevisionIdentifier(),
                            activation
                                    .releaseIdentifier()
                    )
                    .execute();

            return;
        }

        int updated =
                dsl.update(
                                CURRENT
                        )
                        .set(
                                ACTIVATION_REQUEST_IDENTIFIER,
                                activation
                                        .activationRequestIdentifier()
                        )
                        .set(
                                CONFIGURATION_REVISION_IDENTIFIER,
                                activation
                                        .configurationRevisionIdentifier()
                        )
                        .set(
                                RELEASE_IDENTIFIER,
                                activation
                                        .releaseIdentifier()
                        )
                        .where(
                                MERCHANT_IDENTIFIER.eq(
                                        activation
                                                .merchantIdentifier()
                                )
                        )
                        .and(
                                CONFIGURATION_REVISION_IDENTIFIER.eq(
                                        currentRevisionIdentifier
                                                .orElseThrow()
                                )
                        )
                        .execute();

        if (updated != 1) {
            throw new IllegalStateException(
                    "Current configuration pointer changed "
                            + "during serialised activation"
            );
        }
    }

    private void lockActivationRequest(
            String activationRequestIdentifier
    ) {
        dsl.fetch(
                "select pg_advisory_xact_lock("
                        + "hashtextextended(cast(? as text), 40))",
                activationRequestIdentifier
        );
    }

    private void lockMerchantActivation(
            String merchantIdentifier
    ) {
        dsl.fetch(
                "select pg_advisory_xact_lock("
                        + "hashtextextended(cast(? as text), 41))",
                merchantIdentifier
        );
    }

    /**
     * Shared current Merchant Account / Controller authority fence.
     *
     * JooqMerchantAccountLifecycleStore uses the same merchant identity and
     * seed 76 for Controller transfer, suspension and closure transitions.
     */
    private void lockMerchantAuthority(
            String merchantIdentifier
    ) {
        dsl.fetch(
                "select pg_advisory_xact_lock("
                        + "hashtextextended(cast(? as text), 76))",
                merchantIdentifier
        );
    }

    private static SemanticCompatibilityAuthority
            noCompatibilityAuthority() {
        return (merchant, source, target) ->
                Optional.empty();
    }

    private static
            InitialConfigurationRevisionApprovalApplicabilityAuthority
            noInitialApprovalAuthority() {
        return (merchantScope, revisionIdentifier) ->
                Optional.empty();
    }

    private static ConfigurationActivationAdmissionAuthority
            noAdmissionAuthority() {
        return LEGACY_PRE_D5C4_ADMISSION;
    }

    private static void requireSameIntent(
            ConfigurationActivationRequest request,
            PersistedActivation committed
    ) {
        ConfigurationActivation activation =
                committed.activation();

        boolean sameIntent =
                request.releaseIdentifier()
                        .equals(
                                activation.releaseIdentifier()
                        )
                        && request
                        .expectedCurrentConfigurationIdentifier()
                        .equals(
                                committed
                                        .expectedCurrentConfigurationIdentifier()
                        )
                        && request
                        .initiatingPrincipalIdentifier()
                        .equals(
                                committed
                                        .initiatingPrincipalIdentifier()
                        );

        if (!sameIntent) {
            throw new IllegalArgumentException(
                    "Activation request identity is already bound "
                            + "to different intent: "
                            + request.activationRequestIdentifier()
            );
        }
    }

    private static boolean approvalMatchesRelease(
            ConfigurationRevisionApproval approval,
            ConfigurationRelease release
    ) {
        return approval
                .merchantIdentifier()
                .equals(
                        release.merchantIdentifier()
                )
                && approval
                .configurationRevisionIdentifier()
                .equals(
                        release.configurationIdentifier()
                );
    }

    private static boolean initialApprovalMatchesRelease(
            InitialConfigurationRevisionApproval approval,
            ConfigurationRelease release
    ) {
        return approval
                .merchantIdentifier()
                .equals(
                        release.merchantIdentifier()
                )
                && approval
                .configurationRevisionIdentifier()
                .equals(
                        release.configurationIdentifier()
                )
                && approval
                .semanticRegistryReleaseIdentifier()
                .equals(
                        release.semanticRegistryVersion()
                );
    }

    private static boolean nonInitialApprovalMatchesRelease(
            NonInitialConfigurationRevisionApproval approval,
            ConfigurationRelease release
    ) {
        return approval
                .merchantIdentifier()
                .equals(
                        release.merchantIdentifier()
                )
                && approval
                .configurationRevisionIdentifier()
                .equals(
                        release.configurationIdentifier()
                )
                && approval
                .semanticRegistryReleaseIdentifier()
                .equals(
                        release.semanticRegistryVersion()
                );
    }

    private static boolean reinstatementApprovalMatchesRelease(
            ConfigurationReinstatementApproval approval,
            ConfigurationRelease release,
            String reinstatementBasisActivationIdentifier
    ) {
        return approval
                .merchantIdentifier()
                .equals(
                        release.merchantIdentifier()
                )
                && approval
                .configurationRevisionIdentifier()
                .equals(
                        release.configurationIdentifier()
                )
                && approval
                .semanticRegistryReleaseIdentifier()
                .equals(
                        release.semanticRegistryVersion()
                )
                && approval
                .reinstatementBasisActivationRequestIdentifier()
                .equals(
                        reinstatementBasisActivationIdentifier
                );
    }

    private static void requireIdentifier(
            String value,
            String label
    ) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    label + " must not be blank"
            );
        }
    }

    private record PersistedActivation(
            ConfigurationActivation activation,
            Optional<String>
                    expectedCurrentConfigurationIdentifier,
            String initiatingPrincipalIdentifier
    ) {
    }
}
