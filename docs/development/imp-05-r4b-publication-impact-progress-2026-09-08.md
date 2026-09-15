# IMP-05-R4B Publication Impact Progress — 2026-09-08

**Programme node:** IMP-05-R4B — IN_PROGRESS  
**Branch / entry baseline:** development / 95b48f9a1699fe55f91bfb9f5247cdf0786e1a73  
**Classification:** C — cross-capability implementation of accepted authority.

## Accepted authority

Composition resolved through `designs/AUTHORITY-INDEX.md` v3.68:

- MS-PROT-040 v1.0, `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`, §20 Configuration diff; §21 Impact analysis; §22 Impact classifications; §23 Existing commitments are protected; §26 Capability deactivation. These govern comparison, consequential new-activity changes and non-retroactivity.
- MS-PROT-040 v1.1, `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`, §4 Semantic-registry affinity and §5 Validation and Resolved Configuration Package production. Each revision uses its own pinned release.
- MS-PROT-040 v1.3, `designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md`, §10 Business-Facing Review Boundary and §11 Impact-Evidence Production Predicate. Effects are business-facing and scoped to the exact validated result.
- MS-PROT-046 v1.2, `designs/MS-PROT-046 v1.2 — Production Publication Revision, Exposure & Public Interaction Amendment.md`, §1 Publication ownership remains unchanged; §8 Publication state and Exposure remain distinct; §10 PUBLISHED enters Exposure resolution; §16 Public Enquiry participation must be explicit. Publication membership cannot establish lifecycle, visibility or Enquiry participation.
- MS-PROT-022 v1.5, `designs/MS-PROT-022 v1.5 — Resolved Configuration Package & Static-Dynamic Resolution Boundary Amendment.md`, §1 Governing decision and §3 Static resolution. Immutable capability resolution is separate from current runtime facts.
- MS-IMP-001 v1.0, `designs/MS-IMP-001.md`, §11 IMP-05 — Merchant Definition, Configuration & Activation; accepted v1.1 Workforce amendment does not change this scope. `designs/IMPLEMENTATION-RULES.md` v1.8 §§30, 46–48, 52.14, 53.1–53.2 and 55 govern traceability, verification, synchronization, paradigm fit and context loading.

The MS-PROT-046 v1.3 material/history amendment was checked for scope: this increment does not change its schema, history, or persistence. The current MS-PROT-046-V12-DQ-001–008 catalogue contains no decision implemented by this bounded comparison. No Exposure, transport, provider or retention decision is introduced.

## Implementation and tests

`src/main/java/mainstreet/publication/PublicationAvailabilityImpactAssessment.java` implements the existing ConfigurationImpactAssessment port in the Publication owner package. It compares candidate compiled membership against the independently compiled historical base. Initial absence is distinct from a missing historical release, which fails closed. Changed membership emits one consequential business-facing finding; equal membership emits nothing.

`src/test/java/mainstreet/publication/PublicationAvailabilityImpactAssessmentTest.java` uses the real compiler/package resolver. Seven cases cover initial enablement, deactivation, unchanged enabled membership, unrelated Enquiry membership, dependency changes in both directions across pinned releases, unavailable history, and removal of a redundant direct selection while Publication remains required. The synthetic content dependency is a compiler fixture, not a newly registered production capability or business rule.

Paradigm fit: a read-only typed owner assessment over immutable compiler inputs. Configuration retains orchestration and exact-affinity validation through ConfigurationImpactContext. No persistence, lifecycle mutation, provider access, event, framework, or new abstraction is required.

## Verification

Test-first RED: focused Maven compilation failed on the absent PublicationAvailabilityImpactAssessment class. An earlier sandbox-limited cache-access failure is environmental and is not counted as RED evidence.

Targeted GREEN: `mvn --batch-mode -Dmaven.repo.local=<workspace-cache> -Dtest=PublicationAvailabilityImpactAssessmentTest,ConfigurationImpactAnalyzerTest,Opportunity*Test test` — 73 tests, zero failures/errors/skips, BUILD SUCCESS.

Initial full gate against the pre-existing test database: 1201 unit/governance tests passed; 403 integration tests ran with 5 failures and 3 errors in PrototypeJooqAppointmentUseCaseIT, PrototypeJooqBookingUseCaseIT and MerchantEnquiryQueryT3IT. Missing just-inserted records and extra counts suggested shared-database interference. On a separate database, all 14 tests in those classes passed without code or assertion changes. This supports an environmental diagnosis; it does not establish the identity of an interfering process. A direct Failsafe-only diagnostic invocation initially failed to resolve the lifecycle-generated test-agent property; the successful diagnostic used the normal Maven verify lifecycle.

Full isolated gate: `mvn --batch-mode -Dmaven.repo.local=<workspace-cache> clean verify -Ppostgres-it` — BUILD SUCCESS on 8 September 2026 at 16:24 BST, 1201 unit/governance tests plus 403 PostgreSQL integration tests, zero failures/errors/skips. Local runtime: Java 26.0.2 compiling release 25; PostgreSQL 18.6. The isolated test database was mainstreet_publication_20260908_1616. No test exclusions or weakened assertions were used. Local diagnostic logs are in the initiating task workspace under work/publication-*.log; no remote CI result is claimed.

Verified production/test commit: `13c038149733060752769543f85054c0c8ee229c`. Pre-commit fetch confirmed the entry baseline still matched origin/development; accepted designs and AGENTS.md remained unchanged. The cycle-closing evidence commit is separate. After synchronization, all 11 tests in ImplementationGraphIntegrityTest, ImplementationProgrammeGateConformanceTest and AgentInstructionsConformanceTest passed; git diff --check passed.

## Falsification and limits

Direct-selection comparison would miss release-dependent membership changes and falsely report deactivation when another capability still requires Publication; both counterexamples are covered. Missing history cannot be treated as disabled. Unrelated Enquiry selection produces no Publication effect.

The assessment has no content repository or mutation dependency; it cannot publish, withdraw, archive or rewrite existing information objects. Equal membership does not prove equal policy, content visibility, subscription, notification or public interaction behaviour. Those responsibilities require their own applicable owner evidence. Passing the tests does not prove durable review composition or coverage of every capability.

R4B remains IN_PROGRESS. Policy-owner interpretations, owner-backed commitment conflicts, catalogue/resource effects and other uncovered owner effects remain open. R4C and downstream nodes remain blocked; no macro completion or production-readiness claim is made.
