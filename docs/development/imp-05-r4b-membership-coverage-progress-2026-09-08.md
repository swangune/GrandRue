# IMP-05-R4B Capability Membership Coverage — 2026-09-08

**State:** IN_PROGRESS (bounded component increment)  
**Branch / entry baseline:** development / 18f457c4  
**Task class:** C — cross-capability implementation of accepted authority.

## Exact accepted authority

Current composition was resolved through `designs/AUTHORITY-INDEX.md` v3.68. Governing provisions:

- MS-PROT-040 v1.0, `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`, §20 Configuration diff and §21 Impact analysis: capability activation/deactivation is a distinct change dimension; compilation alone is insufficient analysis. §22 Impact classifications leaves business findings classified by their meaning. §26 Capability deactivation preserves responsibility after new activity is disabled.
- MS-PROT-040 v1.1, `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`, §4 Semantic-registry affinity and §5 Validation and Resolved Configuration Package production: use each exact revision/release, without latest-release substitution.
- MS-PROT-040 v1.3, `designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md`, §10 Business-Facing Review Boundary and §11 Impact-Evidence Production Predicate: retain intelligible owner effects and require completed analysis of the exact result before recording completed evidence.
- MS-PROT-022 v1.3 base, `designs/MS-PROT-022 — Configuration Resolution & Operational Model.md`, §10 Capability resolution; surviving under accepted v1.4/v1.5 composition: registered REQUIRES closure, not only direct selections, determines membership. MS-PROT-022 v1.5, `designs/MS-PROT-022 v1.5 — Resolved Configuration Package & Static-Dynamic Resolution Boundary Amendment.md`, §1 Governing decision and §3 Static resolution: runtime obligations remain separate from immutable static membership.
- MS-PROT-047 v1.0, `designs/MS-PROT-047 — Capability Configuration Contract.md`, §3 Capability selection and capability configuration are distinct and §4 Semantic ownership: no policy-meaning or capability-activation inference from identifiers.
- Preserved Booking owner contract: MS-PROT-042 v1.4, `designs/MS-PROT-042 v1.4 — Booking Residual Obligation & Discharge Amendment.md`, §§1–5; exact implementation mapping remains in `docs/development/imp-05-r4b-booking-impact-progress-2026-09-06.md`.
- Preserved Publication owner contract: MS-PROT-046 v1.2, `designs/MS-PROT-046 v1.2 — Production Publication Revision, Exposure & Public Interaction Amendment.md`, §1 Publication ownership remains unchanged and §8 Publication state and Exposure remain distinct; exact implementation mapping remains in `docs/development/imp-05-r4b-publication-impact-progress-2026-09-08.md`.
- MS-IMP-001 v1.0, `designs/MS-IMP-001.md`, §11 IMP-05 and §35 Automatic Implementation Authority, under its accepted v1.1 composition. `designs/IMPLEMENTATION-RULES.md` v1.8 §§30, 46–48, 52.14–52.18, 53.1–53.2 and 55 govern execution/evidence. No programme edge or semantic authority is changed.

## Observed gap and implemented scope

ConfigurationImpactAnalyzer composes an unqualified assessment list. It rejects thrown/null/incomplete aggregate results but cannot determine from that list whether a changed capability was omitted. ResolvedPolicyImpactAssessment already checks policy-owner registrations, but does not cover capability membership changes.

`src/main/java/mainstreet/semantic/configuration/CapabilityMembershipImpactAssessment.java` is a read-only implementation of the existing ConfigurationImpactAssessment port. Its immutable, capability-keyed owner map supplies membership assessment coverage. It computes the symmetric difference of independently compiled historical membership and the exact validated candidate membership. Every changed identity must have an entry before any registered owner in this component is invoked. Missing coverage or historical release fails assessment; no fallback business explanation is fabricated.

All registered owners are then invoked in stable capability-identifier order with the same exact context. Unchanged/disabled owners are deliberately retained: for example, Booking must still report residual management when new Booking activity was already disabled. Effects and classified findings pass through unchanged. Owner failure or null result cannot become an empty contribution.

Registration proves only that a declared assessment exists for a changed membership identity. It cannot prove that the registered implementation owns or completely interprets those semantics. An owner's successful empty contribution remains permitted by the existing contribution contract; the composition does not invent a finding merely to fill the review. The top-level analyzer still requires aggregate business-facing evidence.

Paradigm fit: ordinary deterministic set comparison plus explicit orchestration over existing read-only owner ports, within Configuration's existing impact package. No semantic registry definition, provider, database, event, new policy, or external contract is introduced. Scope-aware DDR inspection found no deferred decision implemented here.

## Executable evidence

`src/test/java/mainstreet/semantic/configuration/CapabilityMembershipImpactAssessmentTest.java` covers:

1. Initial enablement with one missing owner fails before registered owners run.
2. Removal requires the historical capability owner even when absent from the candidate.
3. Both directions of release-dependent dependency changes are detected despite identical selections.
4. Removing a redundant direct selection does not invent a membership change.
5. A real BookingAvailabilityImpactAssessment still obtains owner-supplied residual facts when both revisions omit Booking; it preserves the informational finding and merchant scope.
6. A real PublicationAvailabilityImpactAssessment supplies its exact existing contribution.
7. Missing history fails before owner invocation.
8. Stable invocation/effect ordering and exact context/finding preservation.
9. Owner failure and null result fail assessment.
10. Immutable registration snapshot, blank owner rejection, and preservation of an owner's empty result.
11. A real ConfigurationImpactAnalyzer cannot return completed analysis with this component when another assessment's text would otherwise conceal an uncovered changed capability.

Compiler, package resolver and analyzer are real. Revision/validation stores in the analyzer test are mocks; the Booking residual authority is a supplied owner fact. Existing PostgreSQL owner coverage remains in the preceding Booking increment. Synthetic content dependencies and generic fixture findings do not register production semantics.

Test-first RED failed compilation on the missing CapabilityMembershipImpactAssessment class, including consequent lambda target-type errors. Targeted GREEN: 42 tests across CapabilityMembershipImpactAssessmentTest, ConfigurationImpactAnalyzerTest, BookingAvailabilityImpactAssessmentTest, PublicationAvailabilityImpactAssessmentTest and ResolvedPolicyImpactAssessmentTest passed with zero failures/errors/skips. Full gate: `mvn --batch-mode -Dmaven.repo.local=<workspace-cache> clean verify -Ppostgres-it` — BUILD SUCCESS on 8 September 2026 at 16:39 BST, 1212 unit/governance tests plus 403 PostgreSQL integration tests, zero failures/errors/skips. Java 26.0.2 compiled release 25; PostgreSQL 18.6 used the task-isolated database mainstreet_publication_20260908_1616. No test exclusions were used. Local logs are retained under the initiating task workspace work/membership-*.log; no remote CI result is claimed.

Verified production/test commit: `b4644925c7bc89e712a9bef99ea4d868273904bc`. Pre-commit fetch confirmed no new remote development commits; the two preceding local commits remained intact. Accepted designs and AGENTS.md were unchanged. Cycle-closing evidence synchronization is separate. All 11 post-synchronization tests in ImplementationGraphIntegrityTest, ImplementationProgrammeGateConformanceTest and AgentInstructionsConformanceTest passed; git diff --check passed.

## Falsification and remaining work

Observed bypass boundary: ConfigurationImpactAnalyzer still accepts arbitrary assessment lists. This component must be included in the actual production composition; its existence alone does not enforce coverage on all callers. No default owner portfolio is fabricated and no durable R4C composition is claimed.

Unchanged membership does not establish unchanged policy, routing, visibility or commitment semantics. Policy and binding/routing assessment remain separate. An unregistered unchanged capability is outside this component's membership-delta guarantee. Correctness of owner registrations, all remaining semantic-owner effects, concrete policy interpretations, catalogue/resource effects and commitment-conflict assessments remain R4B obligations.

R4B stays IN_PROGRESS. R4C and downstream programme gates stay blocked. No claim of full R4B, IMP-05 or production completion is made.
