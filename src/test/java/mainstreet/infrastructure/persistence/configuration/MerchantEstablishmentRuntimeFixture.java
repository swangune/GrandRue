package mainstreet.infrastructure.persistence.configuration;

import mainstreet.application.ConfigurationBackedFirstActivationAuthority;
import mainstreet.application.InitialTrialFromConfigurationActivationHandler;
import mainstreet.application.MerchantScope;
import mainstreet.application.TrustedPlatformHumanPrincipal;
import mainstreet.commercial.CommercialEntitlementException;
import mainstreet.commercial.CommercialEntitlementIdentity;
import mainstreet.commercial.EffectiveCommercialEntitlementAuthority;
import mainstreet.commercial.InitialFullExperienceTrialEstablisher;
import mainstreet.infrastructure.persistence.commercial.JooqInitialFullExperienceTrialGrantAuthority;
import mainstreet.infrastructure.persistence.commercial.JooqInitialFullExperienceTrialStore;
import mainstreet.infrastructure.persistence.deployment.JooqOrdinaryServingGenerationPromotionAuthority;
import mainstreet.infrastructure.persistence.deployment.JooqServingDeploymentAdmissionSnapshotAuthority;
import mainstreet.infrastructure.persistence.merchantaccount.JooqMerchantAccountBootstrapStore;
import mainstreet.infrastructure.persistence.merchantprofile.JooqMerchantPublicDescriptorAuthority;
import mainstreet.infrastructure.persistence.onboarding.JooqOnboardingCaseEvidenceStore;
import mainstreet.infrastructure.persistence.release.JooqSemanticReleaseAdmissionAuthority;
import mainstreet.merchantaccount.MerchantAccountEstablisher;
import mainstreet.merchantprofile.MerchantPublicDescriptor;
import mainstreet.merchantprofile.MerchantPublicDescriptorMutationCommand;
import mainstreet.deployment.InitializeOrdinaryServingAdmissionControlCommand;
import mainstreet.deployment.RecordServingDeploymentAdmissionSnapshotCommand;
import mainstreet.onboarding.DeterministicOnboardingRecomputation;
import mainstreet.onboarding.InitialConfigurationIntent;
import mainstreet.onboarding.InitialConfigurationIntentIdentity;
import mainstreet.onboarding.InitialCustomerInteractionDiscoveryQuestion;
import mainstreet.onboarding.InitialOnboardingPromptCatalogue;
import mainstreet.onboarding.OnboardingAnswerEvidence;
import mainstreet.onboarding.OnboardingAnswerForm;
import mainstreet.onboarding.OnboardingAnswerOrigin;
import mainstreet.onboarding.OnboardingCaseIdentity;
import mainstreet.onboarding.OnboardingCaseRecomputationService;
import mainstreet.onboarding.OnboardingCaseRevision;
import mainstreet.onboarding.OnboardingCompletionPolicy;
import mainstreet.onboarding.OnboardingFinalReview;
import mainstreet.onboarding.OnboardingFinalReviewFactory;
import mainstreet.onboarding.OnboardingFinalReviewService;
import mainstreet.onboarding.OnboardingPromptCompletionRequirement;
import mainstreet.onboarding.OnboardingPromptKey;
import mainstreet.onboarding.OnboardingQuestionDefinitionVersion;
import mainstreet.onboarding.OnboardingQuestionIdentity;
import mainstreet.onboarding.OnboardingSubmissionAuthority;
import mainstreet.onboarding.OnboardingSubmissionReadinessEvaluator;
import mainstreet.onboarding.RecordOnboardingAnswerCommand;
import mainstreet.onboarding.StartInitialOnboardingCaseCommand;
import mainstreet.onboarding.SubmitInitialConfigurationIntentCommand;
import mainstreet.runtime.CommercialEntitlementOperationExecutionGuard;
import mainstreet.runtime.ExecutableSupportOperationExecutionGuard;
import mainstreet.runtime.AuthenticationProvenance;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.OperationFulfilment;
import mainstreet.runtime.ScopedOperationDispatcher;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.configuration.ApproveInitialConfigurationRevisionCommand;
import mainstreet.semantic.configuration.ConfigurationActivation;
import mainstreet.semantic.configuration.ConfigurationActivationRequest;
import mainstreet.semantic.configuration.ConfigurationActivationStatus;
import mainstreet.semantic.configuration.ConfigurationImpactAnalysisResult;
import mainstreet.semantic.configuration.ConfigurationOperationExecutionRequirement;
import mainstreet.semantic.configuration.ConfigurationPackageResolver;
import mainstreet.semantic.configuration.ConfigurationRelease;
import mainstreet.semantic.configuration.InMemoryConfigurationPublication;
import mainstreet.semantic.configuration.MaterialiseInitialConfigurationRevisionCommand;
import mainstreet.semantic.configuration.MerchantConfigurationRevision;
import mainstreet.semantic.configuration.RecordConfigurationImpactReviewEvidenceCommand;
import mainstreet.semantic.configuration.RecordConfigurationNewActivityRequirementSetCommand;
import mainstreet.semantic.configuration.RecordConfigurationValidationEvidenceCommand;
import mainstreet.semantic.configuration.RegisteredConfigurationNewActivityRequirementResolver;
import mainstreet.semantic.configuration.ResolvedConfigurationPackage;
import mainstreet.semantic.executable.ActiveOperationResolver;
import mainstreet.semantic.execution.ExecutableSupportAdmission;
import mainstreet.semantic.execution.ExecutableSupportManifest;
import mainstreet.semantic.execution.ExecutableSupportRegistry;
import mainstreet.semantic.execution.ExecutableSupportRequirement;
import mainstreet.semantic.execution.SemanticExecutionContractReference;
import mainstreet.semantic.registry.InMemorySemanticRegistry;
import mainstreet.semantic.registry.OwnedOperationDefinition;
import mainstreet.semantic.registry.OwnedOperationalObjectDefinition;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.semantic.release.AdvanceOrdinarySemanticReleaseCommand;
import mainstreet.semantic.release.DeploymentSemanticMaterialisationSet;
import mainstreet.semantic.release.PackagedSemanticDefinitionBundle;
import mainstreet.semantic.release.RecordSemanticReleasePurposeAdmissionDecisionCommand;
import mainstreet.semantic.release.SemanticReleaseAdmissionDisposition;
import mainstreet.semantic.release.SemanticReleasePurpose;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;


import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * IMP-05 end-to-end proof that the accepted owner-specific authorities compose
 * into one ordinary establishment-to-active runtime path without a parallel
 * aggregate, caller admission flag or mutable latest-release alias.
 */
final class MerchantEstablishmentRuntimeFixture {
    private final boolean publicationSlice;
    MerchantEstablishmentRuntimeFixture(boolean publicationSlice) {
        this.publicationSlice = publicationSlice;
        this.OPERATION = publicationSlice ? "send-enquiry" : "enquiry.create";
    }
    record Established(MerchantScope scope, JooqConfigurationReleaseActivation activation,
            SemanticRegistrySnapshot semantic, Clock clock, DSLContext dsl,
            DataSourceTransactionManager transactions) { }


    private static final String CONTROLLER = "controller-a";
    private static final String RELEASE = "semantic-release-21";
    private final String OPERATION;
    private static final String PACKAGE = "resolved-package-1";
    private static final String GENERATION = "generation-21";
    private static final Instant NOW = Instant.parse("2026-08-30T12:00:00Z");
    private static final CommercialEntitlementIdentity NEW_ENQUIRY =
            new CommercialEntitlementIdentity("enquiry.new-activity");

    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        transactionManager = new DataSourceTransactionManager(dataSource);
        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(dataSource),
                SQLDialect.POSTGRES
        );
        clearOwnedEvidence();
    }

    Established establish() {
        JooqSemanticReleaseAdmissionAuthority releaseAuthority =
                admitOrdinaryRelease();

        MerchantAccountEstablisher.Result establishment =
                new MerchantAccountEstablisher(
                        principal -> true,
                        new JooqMerchantAccountBootstrapStore(
                                dsl,
                                transactionManager,
                                fixedClock(),
                                () -> "establishment-1",
                                () -> "merchant-established-publication-1"
                        )
                ).establish(
                        "establish-merchant-1",
                        new TrustedPlatformHumanPrincipal(CONTROLLER)
                );
        assertEquals(MerchantAccountEstablisher.Status.SUCCESS, establishment.status());
        MerchantScope merchantScope = establishment.merchantAccount().merchantScope();

        new JooqMerchantPublicDescriptorAuthority(dsl, transactionManager)
                .establish(new MerchantPublicDescriptorMutationCommand(
                        new MerchantPublicDescriptor(
                                merchantScope,
                                "Acme Services",
                                Optional.empty(),
                                Optional.of("Ask Acme about its services"),
                                Optional.empty()
                        ),
                        Optional.empty(),
                        "define-merchant-1",
                        "merchant-approved-profile",
                        CONTROLLER,
                        Optional.of("privileged-browser"),
                        NOW
                ));

        JooqOnboardingCaseEvidenceStore onboarding =
                new JooqOnboardingCaseEvidenceStore(dsl, transactionManager);
        onboarding.startInitial(new StartInitialOnboardingCaseCommand(
                new OnboardingCaseIdentity("onboarding-case-1"),
                merchantScope,
                new OnboardingCaseRevision("onboarding-revision-1"),
                "start-onboarding-1",
                CONTROLLER,
                Optional.of("privileged-browser"),
                NOW
        ));
        onboarding.recordAnswer(new RecordOnboardingAnswerCommand(
                new OnboardingCaseIdentity("onboarding-case-1"),
                new OnboardingCaseRevision("onboarding-revision-1"),
                new OnboardingCaseRevision("onboarding-revision-2"),
                "answer-onboarding-1",
                "answer-evidence-1",
                discoveryAnswer(),
                CONTROLLER,
                Optional.of("privileged-browser"),
                NOW.plusSeconds(1)
        ));
        OnboardingFinalReviewService reviewService = reviewService(onboarding);
        OnboardingFinalReview review = reviewService.createReview(
                new OnboardingCaseIdentity("onboarding-case-1"),
                new OnboardingCaseRevision("onboarding-revision-2")
        );
        InitialConfigurationIntent intent = onboarding.submitInitialConfigurationIntent(
                new SubmitInitialConfigurationIntentCommand(
                        "submit-onboarding-1",
                        new InitialConfigurationIntentIdentity("initial-intent-1"),
                        review,
                        new OnboardingCaseRevision("onboarding-revision-3"),
                        CONTROLLER,
                        Optional.of("privileged-browser"),
                        NOW.plusSeconds(2)
                ),
                completionPolicy(),
                OnboardingSubmissionAuthority.AUTHORISED
        );

        JooqConfigurationRevisionAuthority revisions =
                new JooqConfigurationRevisionAuthority(
                        dsl,
                        transactionManager,
                        onboarding,
                        () -> releaseAuthority.currentOrdinaryReference()
                                .orElseThrow()
                                .semanticRegistryReleaseIdentifier()
                );
        MerchantConfigurationRevision revision = revisions.materialiseInitial(
                new MaterialiseInitialConfigurationRevisionCommand(
                        intent.identity(),
                        "configuration-1",
                        new OnboardingCaseRevision("onboarding-revision-4"),
                        "configuration-bootstrap",
                        Optional.of("onboarding-handoff"),
                        NOW.plusSeconds(3)
                )
        );
        assertEquals(Set.of("publication", "enquiry"),
                revision.configuration().capabilityIdentifiers());

        ResolvedConfigurationPackage basePackage =
                new ConfigurationPackageResolver(
                        new ConfigurationCompiler(semanticRegistry())
                ).resolve(
                        revision.configuration(),
                        "mainstreet-compiler-1",
                        NOW.plusSeconds(4)
                );
        ResolvedConfigurationPackage resolvedPackage = publicationSlice
                ? ResolvedConfigurationPackage.currentFoundation(revision.configuration(),
                        basePackage.executableSemanticModel(),
                        new mainstreet.surface.StaticSurfaceContributionCatalogue(List.of(
                                new mainstreet.surface.StaticSurfaceContribution(
                                        new mainstreet.surface.SurfaceContributionIdentity("enquiry", "send-enquiry"),
                                        mainstreet.surface.SurfaceAudience.PUBLIC,
                                        mainstreet.surface.SurfaceContributionKind.PUBLIC_INTERACTION,
                                        Optional.empty(), Set.of(), Set.of("send-enquiry")))),
                        basePackage.provenance())
                : basePackage;
        JooqConfigurationValidationEvidenceAuthority validations =
                new JooqConfigurationValidationEvidenceAuthority(
                        dsl,
                        transactionManager,
                        revisions
                );
        validations.recordSuccessfulValidation(
                new RecordConfigurationValidationEvidenceCommand(
                        "validation-1",
                        PACKAGE,
                        resolvedPackage,
                        NOW.plusSeconds(5)
                )
        );
        new JooqConfigurationImpactReviewEvidenceAuthority(
                dsl,
                transactionManager,
                validations
        ).recordCompletedReview(new RecordConfigurationImpactReviewEvidenceCommand(
                "impact-review-1",
                new ConfigurationImpactAnalysisResult(
                        merchantScope.merchantIdentifier(),
                        revision.configuration().configurationIdentifier(),
                        RELEASE,
                        "validation-1",
                        PACKAGE,
                        List.of("Customers can send an enquiry"),
                        List.of(),
                        NOW.plusSeconds(6)
                )
        ));
        JooqInitialConfigurationRevisionApprovalAuthority approvals =
                new JooqInitialConfigurationRevisionApprovalAuthority(
                        dsl,
                        transactionManager
                );
        approvals.approve(
                new ApproveInitialConfigurationRevisionCommand(
                        "approve-configuration-1",
                        merchantScope,
                        revision.configuration().configurationIdentifier(),
                        "validation-1",
                        "impact-review-1",
                        CONTROLLER,
                        NOW.plusSeconds(7)
                ),
                trustedController(merchantScope)
        );

        ExecutableSupportRequirement supportRequirement =
                new ExecutableSupportRequirement(contract(OPERATION), Set.of());
        JooqConfigurationNewActivityRequirementSetAuthority requirementSets =
                new JooqConfigurationNewActivityRequirementSetAuthority(
                        dsl,
                        transactionManager,
                        new RegisteredConfigurationNewActivityRequirementResolver(
                                List.of(new ConfigurationOperationExecutionRequirement(
                                        RELEASE,
                                        OPERATION,
                                        supportRequirement
                                ))
                        )
                );
        requirementSets.record(new RecordConfigurationNewActivityRequirementSetCommand(
                PACKAGE,
                resolvedPackage,
                NOW.plusSeconds(8)
        ));

        JooqServingDeploymentAdmissionSnapshotAuthority snapshots =
                new JooqServingDeploymentAdmissionSnapshotAuthority(
                        dsl,
                        transactionManager
                );
        ExecutableSupportManifest supportManifest =
                new ExecutableSupportManifest(
                        "enquiry-path",
                        Set.of(contract(OPERATION))
                );
        snapshots.record(new RecordServingDeploymentAdmissionSnapshotCommand(
                GENERATION,
                new DeploymentSemanticMaterialisationSet(List.of(bundle())),
                List.of(supportManifest),
                NOW.plusSeconds(9)
        ));
        new JooqOrdinaryServingGenerationPromotionAuthority(
                dsl,
                transactionManager,
                snapshots,
                requirementSets
        ).initialize(new InitializeOrdinaryServingAdmissionControlCommand(
                GENERATION,
                NOW.plusSeconds(10)
        ));

        InMemoryConfigurationPublication publication =
                new InMemoryConfigurationPublication();
        ConfigurationRelease release = new ConfigurationRelease(
                "configuration-release-1",
                revision.configuration(),
                resolvedPackage
        );
        publication.publish(release);
        JooqConfigurationReleaseActivation activations =
                new JooqConfigurationReleaseActivation(
                        publication,
                        (merchant, configuration) -> Optional.empty(),
                        approvals,
                        (principal, merchant, configuration) ->
                                principal.equals(CONTROLLER)
                                        && merchant.equals(
                                                merchantScope.merchantIdentifier()
                                        ),
                        (merchant, source, target) -> Optional.empty(),
                        new JooqConfigurationActivationAdmissionAuthority(
                                dsl,
                                requirementSets,
                                snapshots
                        ),
                        fixedClock(),
                        dsl,
                        transactionManager,
                        () -> "configuration-activated-publication-1"
                );
        ConfigurationActivation activation = activations.activate(
                new ConfigurationActivationRequest(
                        "activate-configuration-1",
                        release.releaseIdentifier(),
                        Optional.empty(),
                        CONTROLLER
                )
        ).activation().orElseThrow();
        assertEquals(ConfigurationActivationStatus.SUCCESS,
                activations.activate(new ConfigurationActivationRequest(
                        "activate-configuration-1",
                        release.releaseIdentifier(),
                        Optional.empty(),
                        CONTROLLER
                )).status());
        assertEquals(GENERATION,
                activation.admissionEvidence().orElseThrow()
                        .servingGenerationIdentifier());

        EffectiveCommercialEntitlementAuthority entitlements =
                new EffectiveCommercialEntitlementAuthority(
                        new JooqInitialFullExperienceTrialGrantAuthority(
                                dsl,
                                Set.of(NEW_ENQUIRY)
                        ),
                        fixedClock()
                );
        AtomicBoolean handled = new AtomicBoolean();
        ScopedOperationDispatcher<CreateEnquiry> dispatcher =
                new ScopedOperationDispatcher<>(
                        new ActiveOperationResolver(activations),
                        List.of(
                                (scope, principal, operation) -> { },
                                new ExecutableSupportOperationExecutionGuard(
                                        new ExecutableSupportAdmission(
                                                new ExecutableSupportRegistry(
                                                        List.of(supportManifest)
                                                ),
                                                "enquiry-path",
                                                operation -> supportRequirement
                                        )
                                ),
                                new CommercialEntitlementOperationExecutionGuard(
                                        entitlements,
                                        Map.of(OPERATION, NEW_ENQUIRY)
                                )
                        ),
                        (command, context) -> {
                            handled.set(true);
                            return OperationFulfilment.conformingTo(
                                    context.applicableOperation(),
                                    context.applicableOperation().operation().effects(),
                                    Set.of("EnquiryCreated")
                            );
                        }
                );

        assertThrows(
                CommercialEntitlementException.class,
                () -> dispatcher.dispatch(
                        trustedController(merchantScope),
                        OPERATION,
                        new CreateEnquiry("enquiry-1")
                )
        );
        assertFalse(handled.get());
        assertEquals(OPERATION,
                new ActiveOperationResolver(activations)
                        .resolve(merchantScope, OPERATION)
                        .operation()
                        .identifier());

        new InitialTrialFromConfigurationActivationHandler(
                new InitialFullExperienceTrialEstablisher(
                        new ConfigurationBackedFirstActivationAuthority(activations),
                        new JooqInitialFullExperienceTrialStore(
                                dsl,
                                transactionManager
                        )
                ),
                committed -> "initial-trial-1"
        ).handle(activation).orElseThrow();

        OperationFulfilment fulfilment = dispatcher.dispatch(
                trustedController(merchantScope),
                OPERATION,
                new CreateEnquiry("enquiry-1")
        );
        assertTrue(handled.get());
        assertSame(
                resolvedPackage.executableSemanticModel(),
                fulfilment.applicableOperation().model()
        );
        assertEquals(
                release.releaseIdentifier(),
                fulfilment.applicableOperation().releaseIdentifier()
        );
        assertTrue(entitlements.isEntitled(merchantScope, NEW_ENQUIRY));
        return new Established(merchantScope, activations, semanticRegistry().version(RELEASE).orElseThrow(),
                fixedClock(), dsl, transactionManager);
    }

    private JooqSemanticReleaseAdmissionAuthority admitOrdinaryRelease() {
        JooqSemanticReleaseAdmissionAuthority authority =
                new JooqSemanticReleaseAdmissionAuthority(
                        dsl,
                        transactionManager
                );
        authority.recordDecision(admissionDecision(
                "admit-validation-1",
                SemanticReleasePurpose.NEW_CONFIGURATION_VALIDATION
        ));
        authority.recordDecision(admissionDecision(
                "admit-activity-1",
                SemanticReleasePurpose.NEW_BUSINESS_ACTIVITY
        ));
        authority.advanceOrdinaryReference(new AdvanceOrdinarySemanticReleaseCommand(
                "ordinary-release-reference-1",
                RELEASE,
                0,
                "platform-release-controller",
                "change-ticket-21",
                NOW.minusSeconds(30)
        ));
        return authority;
    }

    private static RecordSemanticReleasePurposeAdmissionDecisionCommand
    admissionDecision(String identifier, SemanticReleasePurpose purpose) {
        return new RecordSemanticReleasePurposeAdmissionDecisionCommand(
                identifier,
                RELEASE,
                purpose,
                SemanticReleaseAdmissionDisposition.ADMITTED,
                "platform-release-controller",
                "change-ticket-21",
                NOW.minusSeconds(60)
        );
    }

    private static OnboardingFinalReviewService reviewService(
            JooqOnboardingCaseEvidenceStore store
    ) {
        OnboardingFinalReviewFactory factory = new OnboardingFinalReviewFactory();
        return new OnboardingFinalReviewService(
                store,
                new OnboardingCaseRecomputationService(
                        store,
                        new DeterministicOnboardingRecomputation(
                                InitialOnboardingPromptCatalogue.registry()
                        )
                ),
                factory,
                new OnboardingSubmissionReadinessEvaluator(factory)
        );
    }

    private static OnboardingCompletionPolicy completionPolicy() {
        return new OnboardingCompletionPolicy(
                Map.of(
                        new OnboardingPromptKey(
                                InitialCustomerInteractionDiscoveryQuestion.identity(),
                                Optional.empty()
                        ),
                        OnboardingPromptCompletionRequirement.REQUIRED
                ),
                List.of()
        );
    }

    private static OnboardingAnswerEvidence discoveryAnswer() {
        return new OnboardingAnswerEvidence(
                new OnboardingQuestionIdentity(
                        "onboarding.discovery",
                        "customer-interactions"
                ),
                new OnboardingQuestionDefinitionVersion("v2"),
                OnboardingAnswerForm.MULTI_SELECT,
                Set.of("PUBLISH_INFORMATION", "SEND_ENQUIRY"),
                Optional.empty(),
                Optional.empty(),
                OnboardingAnswerOrigin.MERCHANT_SELECTED,
                CONTROLLER,
                NOW.plusSeconds(1),
                Optional.of(RELEASE)
        );
    }

    private InMemorySemanticRegistry semanticRegistry() {
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                RELEASE,
                Set.of(
                        new RegisteredCapability("publication", publicationSlice ? List.of(
                                new OwnedOperationalObjectDefinition("opportunity", Set.of(), (String) null)) : List.of(), List.of()),
                        new RegisteredCapability(
                                "enquiry",
                                List.of(new OwnedOperationalObjectDefinition(
                                        "enquiry",
                                        Set.of("requested"),
                                        "requested"
                                )),
                                List.of(OwnedOperationDefinition.creation(
                                        OPERATION,
                                        "enquiry",
                                        "requested",
                                        "EnquiryCreated",
                                        OPERATION
                                ))
                        )
                )
        ));
        return registry;
    }

    private static SemanticExecutionContractReference contract(String identifier) {
        return new SemanticExecutionContractReference(RELEASE, identifier);
    }

    private static PackagedSemanticDefinitionBundle bundle() {
        byte[] semantic = bytes("semantic:" + RELEASE);
        byte[] surface = bytes("surface:" + RELEASE);
        byte[] fulfilment = bytes("fulfilment:" + RELEASE);
        byte[] exposure = new byte[0];
        String provenance = "publication-2026-08-30";
        String digest = PackagedSemanticDefinitionBundle.computeDigest(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT,
                RELEASE,
                provenance,
                semantic,
                surface,
                fulfilment,
                exposure
        );
        return new PackagedSemanticDefinitionBundle(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT,
                RELEASE,
                provenance,
                digest,
                semantic,
                surface,
                fulfilment,
                exposure
        );
    }

    private static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    private static TrustedExecutionContext trustedController(
            MerchantScope merchantScope
    ) {
        return new TrustedExecutionContext(
                merchantScope,
                new ExecutionPrincipal(CONTROLLER),
                Optional.of(new AuthenticationProvenance(
                        "session-controller-a",
                        CONTROLLER,
                        NOW.minusSeconds(1)
                ))
        );
    }

    private static Clock fixedClock() {
        return Clock.fixed(NOW.plusSeconds(11), ZoneOffset.UTC);
    }

    private void clearOwnedEvidence() {
        dsl.execute("truncate table merchant_account cascade");
        dsl.execute(
                "truncate table merchant_public_descriptor_revision, "
                        + "configuration_activation_publication_intent, "
                        + "merchant_current_configuration_activation, "
                        + "configuration_activation, "
                        + "configuration_new_activity_required_contract, "
                        + "configuration_new_activity_requirement, "
                        + "configuration_new_activity_requirement_set, "
                        + "configuration_revision_approval, "
                        + "configuration_impact_review_finding, "
                        + "configuration_impact_review_effect, "
                        + "configuration_impact_review_evidence, "
                        + "configuration_validation_evidence, "
                        + "merchant_configuration_revision_policy, "
                        + "merchant_configuration_revision_capability, "
                        + "merchant_configuration_revision, "
                        + "initial_configuration_intent_unresolved_prompt, "
                        + "initial_configuration_intent_provenance, "
                        + "initial_configuration_intent_semantic_seed, "
                        + "initial_configuration_intent, "
                        + "onboarding_effective_answer, "
                        + "onboarding_answer_evidence, "
                        + "onboarding_case_revision, "
                        + "onboarding_start_request, "
                        + "onboarding_case cascade"
        );
        dsl.execute(
                "truncate table ordinary_new_configuration_semantic_release_pointer, "
                        + "ordinary_new_configuration_semantic_release_reference_revision, "
                        + "semantic_release_purpose_current_admission, "
                        + "semantic_release_purpose_admission_decision, "
                        + "ordinary_serving_generation_promotion, "
                        + "ordinary_serving_admission_control, "
                        + "serving_deployment_executable_support_contract, "
                        + "serving_deployment_executable_support_manifest, "
                        + "serving_deployment_materialised_release, "
                        + "serving_deployment_admission_snapshot cascade"
        );
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required PostgreSQL integration-test environment variable: "
                            + name
            );
        }
        return value;
    }

    private record CreateEnquiry(String enquiryIdentifier) {
    }
}
