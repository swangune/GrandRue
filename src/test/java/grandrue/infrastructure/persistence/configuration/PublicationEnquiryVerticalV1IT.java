package grandrue.infrastructure.persistence.configuration;

import grandrue.api.*;
import grandrue.application.*;
import grandrue.enquiry.*;
import grandrue.enquiry.delivery.*;
import grandrue.infrastructure.persistence.enquiry.*;
import grandrue.infrastructure.persistence.publication.*;
import grandrue.infrastructure.persistence.runtime.JooqSessionRecordStore;
import grandrue.infrastructure.persistence.merchantprofile.JooqMerchantPublicDescriptorAuthority;
import grandrue.merchantprofile.*;
import grandrue.publication.*;
import grandrue.publication.delivery.*;
import grandrue.runtime.*;
import grandrue.semantic.Privilege;
import grandrue.surface.*;
import org.junit.jupiter.api.*;
import org.jooq.impl.DSL;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** One established merchant, durable activation and profile, Publication application, public delivery,
 * atomic Enquiry submission and authenticated merchant observation. No mocked persistence or activation. */
class PublicationEnquiryVerticalV1IT {
    private MerchantEstablishmentRuntimeFixture.Established runtime;
    private JooqOpportunityPublicationApplicationService publication;
    private JooqOpportunityPublicationStateAuthority owner;
    private JooqEnquirySubmissionApplicationService application;
    private JooqEnquirySubmissionStore submissions;
    private JooqSessionRecordStore sessions;
    private MockMvc mvc;
    private final AtomicInteger preparations = new AtomicInteger();
    private final AtomicBoolean contactAllowed = new AtomicBoolean(true);
    private final AtomicBoolean publicAllowed = new AtomicBoolean(true);
    private String authorization;
    private static final String PUBLIC = "/api/public/storefronts/shop/opportunities/O1";
    private static final String SUBMIT = "/api/public/storefronts/shop/enquiries/opportunity";
    private static final String MERCHANT = "/api/merchant/workspaces/shop/enquiries/";

    @BeforeEach
    void setUp() {
        var bootstrap = new MerchantEstablishmentRuntimeFixture(true);
        bootstrap.setUp();
        runtime = bootstrap.establish();
        runtime.dsl().execute("truncate table enquiry_submission_application_request, enquiry_submission, "
                + "opportunity_publication_application_request, opportunity_publication_lifecycle_history, "
                + "opportunity_publication_revision_field_value, opportunity_publication_revision_material, "
                + "opportunity_publication_current, opportunity_publication_revision_identity, authentication_session_record");
        publication = new JooqOpportunityPublicationApplicationService(runtime.dsl(), runtime.transactions());
        owner = new JooqOpportunityPublicationStateAuthority(runtime.dsl(), runtime.transactions());
        application = new JooqEnquirySubmissionApplicationService(runtime.dsl(), runtime.transactions());
        submissions = new JooqEnquirySubmissionStore(runtime.dsl());
        sessions = new JooqSessionRecordStore(runtime.dsl());
        var credential = OpaqueSessionCredential.generate();
        authorization = "Bearer " + credential.value();
        sessions.create(new SessionRecord("v1-session", "controller-a", credential.verifier(),
                runtime.clock().instant().minusSeconds(30), "assurance", "password",
                runtime.clock().instant().plusSeconds(3600), runtime.clock().instant().minusSeconds(30),
                "generation", Optional.empty(), Optional.empty()));
        mvc = compose(application);
        observeProfile();
        publication.establishDraft(new ApplicationRequestIdentity("draft"), material("R1", "Original title"));
        publication.publish(new ApplicationRequestIdentity("publish"), runtime.scope(), "O1", "R1");
    }

    @Test
    void established_merchant_reaches_publication_binding_submission_and_merchant_observation() throws Exception {
        var token = binding();
        submit("request", token).andExpect(status().isOk())
                .andExpect(content().json("{\"outcome\":\"COMPLETED\"}", true));
        mvc.perform(get(MERCHANT + "E1").header("Authorization", authorization)).andExpect(status().isOk())
                .andExpect(jsonPath("$.submission.question").value("Question from visitor"))
                .andExpect(jsonPath("$.contact.email").value("visitor@example.com"))
                .andExpect(jsonPath("$.subjectContext.subjectIdentity").value("O1"))
                .andExpect(jsonPath("$.subjectContext.submissionRevisionIdentity").value("R1"));
        var stored = submissions.submission(runtime.scope(), "E1").orElseThrow();
        assertEquals(runtime.semantic().version(), stored.semanticContext().semanticRegistryReleaseIdentifier());
        assertEquals(Optional.empty(), stored.customerContextIdentity());
        assertEquals(1, count());
    }

    @Test
    void publication_change_preserves_retry_and_original_merchant_provenance_while_rejecting_stale_new_intent() throws Exception {
        var original = binding();
        submit("request", original).andExpect(status().isOk());
        publication.revise(new ApplicationRequestIdentity("revise"), "R1", material("R2", "Changed title"));
        publication.publish(new ApplicationRequestIdentity("publish-r2"), runtime.scope(), "O1", "R2");
        var changed = binding("Changed title");
        submit("request", changed).andExpect(status().isConflict());
        submit("request", original).andExpect(status().isOk());
        submit("fresh-stale", original).andExpect(status().isUnprocessableEntity());
        submit("fresh", changed).andExpect(status().isOk());
        mvc.perform(get(MERCHANT + "E1").header("Authorization", authorization)).andExpect(status().isOk())
                .andExpect(jsonPath("$.subjectContext.submissionRevisionIdentity").value("R1"));
        mvc.perform(get(MERCHANT + "E2").header("Authorization", authorization)).andExpect(status().isOk())
                .andExpect(jsonPath("$.subjectContext.submissionRevisionIdentity").value("R2"));
        assertEquals(2, count());
        assertEquals(2, preparations.get());
    }

    @Test
    void concurrent_same_logical_submission_has_one_commit_and_lost_result_reconciles_after_withdrawal() throws Exception {
        var token = binding();
        var ready = new CountDownLatch(4);
        var start = new CountDownLatch(1);
        try (var pool = Executors.newFixedThreadPool(4)) {
            var results = new ArrayList<Future<Integer>>();
            for (int i = 0; i < 4; i++) results.add(pool.submit(() -> {
                ready.countDown();
                assertTrue(start.await(10, TimeUnit.SECONDS));
                return submit("concurrent", token).andReturn().getResponse().getStatus();
            }));
            assertTrue(ready.await(10, TimeUnit.SECONDS));
            start.countDown();
            for (var result : results) assertEquals(200, result.get(20, TimeUnit.SECONDS));
        }
        assertEquals(1, preparations.get());
        assertEquals(1, count());
        var first = new AtomicBoolean(true);
        mvc = compose((key, intent, preparation) -> {
            var value = application.submit(key, intent, preparation);
            if (first.getAndSet(false)) throw new IllegalStateException("lost application response");
            return value;
        });
        submit("lost", token).andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.outcome").value("OUTCOME_UNCERTAIN"));
        publication.withdraw(new ApplicationRequestIdentity("withdraw"), runtime.scope(), "O1", "R1");
        submit("lost", token).andExpect(status().isOk());
        submit("fresh-after-withdrawal", token).andExpect(status().isUnprocessableEntity());
        assertEquals(2, count());
        mvc.perform(get(MERCHANT + "E2").header("Authorization", authorization)).andExpect(status().isOk())
                .andExpect(jsonPath("$.subjectContext.submissionRevisionIdentity").value("R1"));
    }

    @Test
    void current_scope_admission_and_family_authority_remain_independent_across_the_vertical_path() throws Exception {
        var token = binding();
        mvc.perform(post(SUBMIT.replace("/shop/", "/foreign/")).header("Idempotency-Key", "foreign")
                .contentType(MediaType.APPLICATION_JSON).content(body(token))).andExpect(status().isBadRequest());
        assertEquals(0, count());
        submit("request", token).andExpect(status().isOk());
        contactAllowed.set(false);
        var response = mvc.perform(get(MERCHANT + "E1").header("Authorization", authorization))
                .andExpect(status().isOk()).andExpect(jsonPath("$.contact").doesNotExist())
                .andExpect(jsonPath("$.submission.question").value("Question from visitor"))
                .andReturn().getResponse().getContentAsString();
        assertFalse(response.contains("visitor@example.com"));
        publicAllowed.set(false);
        submit("request", token).andExpect(status().isForbidden());
        sessions.revoke("v1-session", runtime.clock().instant(), "revoked");
        mvc.perform(get(MERCHANT + "E1").header("Authorization", authorization)).andExpect(status().isUnauthorized());
        assertEquals(1, count());
        assertEquals(Optional.of("visitor@example.com"), submissions.submission(runtime.scope(), "E1").orElseThrow().contact().email());
    }

    private MockMvc compose(EnquirySubmissionApplicationService app) {
        var release = runtime.semantic().version();
        IdentitySecurityGenerationAuthority generations = identity -> "generation";
        var admission = new AudienceObservationAdmissionEvaluator(
                new DefaultAuthenticationSessionCurrentnessAuthority(sessions, generations, runtime.clock()),
                scope -> Optional.empty(), (scope, principal) -> scope.equals(runtime.scope()) && principal.equals("controller-a"),
                (context, binding, time) -> AudienceObservationPlatformProtection.SATISFIED, runtime.clock());
        var contexts = new AudienceObservationContextEstablisher(sessions,
                new ContextualAccessProofRuntimeBindingSnapshot(List.of()),
                new ObservationContributionDefinitionRegistrySnapshot(release, List.of(
                        OpportunityMaterialAffinityObservationContributionRegistration.definition())),
                new ObservationContributionRuntimeBindingSnapshot(List.of(
                        OpportunityMaterialAffinityObservationContributionRegistration.runtimeBinding())));
        var routes = new PublicOpportunityRouteScopeAuthority(Map.of("shop", runtime.scope()));
        var codec = new OpportunityEnquiryBindingCodec("test", Map.of("test", new byte[32]));
        var query = PublicOpportunityQuery.create(routes, runtime.activation(), runtime.semantic(), owner, contexts, admission, runtime.clock());
        var bindingQuery = PublicOpportunityEnquiryBindingQuery.create(routes, runtime.activation(), runtime.semantic(),
                owner, contexts, admission, runtime.clock(), codec);
        var delivery = new PublicOpportunityEnquirySubmission(new PublicEnquiryRouteScopeAuthority(Map.of(
                "shop", runtime.scope(), "foreign", new MerchantScope("foreign"))), scope -> publicAllowed.get(), app,
                runtime.activation(), runtime.semantic(), owner, owner, intent -> {
                    int index = preparations.incrementAndGet();
                    return new EnquirySubmission(intent.merchantScope(), "E" + index, runtime.clock().instant(),
                            intent.question(), intent.contact(), new EnquirySemanticContext(release, "prepared", 1),
                            intent.subjectRevision(), Optional.empty());
                }, runtime.clock(), codec);
        var privileges = new HashMap<ExposableElementReference, Privilege>();
        EnquiryMerchantExposureReferences.elements().forEach(e -> privileges.put(e, new Privilege("v1-" + e.elementIdentifier())));
        var authentication = new SessionTrustedExecutionContextEstablisher(
                new SessionCredentialResolver(sessions, generations, runtime.clock()), (scope, identity) -> new ExecutionPrincipal(identity));
        var merchant = MerchantEnquiryQuery.create(new MerchantEnquiryRouteScopeAuthority(Map.of("shop", runtime.scope())),
                runtime.activation(), runtime.semantic(), submissions, authentication, contexts, admission,
                (scope, principal, privilege) -> scope.equals(runtime.scope()) && principal.identifier().equals("controller-a")
                        && (contactAllowed.get() || !privilege.equals(privileges.get(EnquiryMerchantExposureReferences.SUBMITTED_CONTACT))),
                new MerchantEnquiryObservationPrivileges(privileges), runtime.clock());
        return MockMvcBuilders.standaloneSetup(new PublicOpportunityController(query),
                new PublicOpportunityEnquiryController(bindingQuery, delivery), new MerchantEnquiryController(merchant)).build();
    }

    private void observeProfile() {
        var release = runtime.semantic().version();
        var definition = PublicMerchantPresenceQueryPath.definition();
        var contracts = new ApiContractRegistrySnapshot(Set.of(definition.registration()));
        ApiTransportScopeEstablishmentAuthority routes = new ApiTransportScopeEstablishmentAuthority() {
            public String ruleReference() { return definition.registration().scopeEstablishmentRuleReference(); }
            public ApiSurfaceClass surface() { return ApiSurfaceClass.PUBLIC; }
            public Class<? extends ApiTransportScopeEvidence> evidenceType() { return PublicOpportunityRouteScopeAuthority.Route.class; }
            public ApiTransportScope establish(ApiTransportScopeEvidence evidence) {
                if (!((PublicOpportunityRouteScopeAuthority.Route) evidence).locator().equals("shop")) throw new IllegalArgumentException();
                return new MerchantApiTransportScope(runtime.scope());
            }
        };
        var projections = InitialProjectionContractPortfolio.forRelease(release);
        var exposures = InitialMerchantPresenceExposureContractPortfolio.forRelease(release);
        var requests = new ObservationRequestEstablisher(contracts, new ApiTransportScopeAuthorityRegistrySnapshot(List.of(routes)),
                runtime.activation(), runtime.semantic(), projections, exposures);
        var admission = new AudienceObservationAdmissionEvaluator(provenance -> AuthenticationSessionCurrentness.NOT_CURRENT,
                scope -> Optional.empty(), (scope, principal) -> false,
                (context, binding, time) -> AudienceObservationPlatformProtection.SATISFIED, runtime.clock());
        var execution = new PublicBoundedQueryExecution(requests, new AudienceObservationContextEstablisher(sessions),
                new ApiAudienceObservationContextBinder(contracts), admission,
                new ProjectionServiceabilityEvaluationEngine(projections, InitialProjectionPolicyEvaluatorPortfolio.forRelease(release)),
                exposures, new ExposureRequirementEvaluatorBindingSnapshot(release, List.of()),
                new MerchantExposureChoiceEvaluatorBindingSnapshot(release, List.of()), runtime.clock());
        var selected = execution.execute(definition.registration().identity(), new PublicOpportunityRouteScopeAuthority.Route("shop"),
                (scope, request) -> {
                    var observation = new AuthorityBackedMerchantPublicDescriptorProjectionReadPort(
                            new JooqMerchantPublicDescriptorAuthority(runtime.dsl(), runtime.transactions())).observe(scope, runtime.clock().instant());
                    var evidence = new HashSet<ProjectionSourceEvidence>();
                    evidence.add(observation.sourceEvidence());
                    projections.contract(release, new ProjectionContractIdentity("platform", "merchant-presence"))
                            .orElseThrow().authoritativeSourceReferences().stream()
                            .filter(source -> !source.equals(observation.sourceEvidence().sourceReference()))
                            .forEach(source -> evidence.add(new ProjectionSourceEvidence(source,
                                    "v1-unacquired-" + source, Optional.empty(), Optional.empty(), runtime.clock().instant(),
                                    ProjectionSourceAvailability.UNAVAILABLE, ProjectionSourceCompleteness.MISSING,
                                    ProjectionSourceRevocationState.UNVERIFIABLE)));
                    return new PublicBoundedQueryExecution.Material(new BoundedProjectionRead(request,
                            new ProjectionContractIdentity("platform", "merchant-presence"),
                            new ProjectionReadUseIdentity("platform", "public-merchant-presence"),
                            evidence, observation.material().orElseThrow().fragments()), List.of());
                });
        var assembly = new PublicCustomerProjectionAssemblyService().assemble(selected.read(), selected.serviceability(), selected.exposure());
        var presence = new PublicMerchantPresenceQueryPath().bind(assembly);
        assertTrue(presence.assembly().selectedFragments().stream().anyMatch(fragment ->
                ((MerchantPublicDescriptorProjectionFragment) fragment).value().equals("Acme Services")));
    }

    private OpportunityPublicationMaterialRevision material(String revision, String title) {
        return new OpportunityPublicationMaterialRevision(runtime.scope(), "O1", revision, title,
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), List.of(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }
    private String binding() throws Exception { return binding("Original title"); }
    private String binding(String title) throws Exception {
        mvc.perform(get(PUBLIC)).andExpect(status().isOk()).andExpect(jsonPath("$.title").value(title));
        var response = mvc.perform(get(PUBLIC + "/enquiry-binding")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return new tools.jackson.databind.ObjectMapper().readTree(response).get("binding").asText();
    }
    private static String body(String token) {
        return "{\"binding\":\"" + token + "\",\"question\":\"Question from visitor\",\"email\":\"visitor@example.com\"}";
    }
    private org.springframework.test.web.servlet.ResultActions submit(String key, String token) throws Exception {
        return mvc.perform(post(SUBMIT).header("Idempotency-Key", key).contentType(MediaType.APPLICATION_JSON).content(body(token)));
    }
    private int count() { return runtime.dsl().fetchCount(DSL.table("enquiry_submission")); }
}
