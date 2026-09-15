# Implementation programme integrity review — 6 September 2026

## Decision and scope
**Required graph correction: IMP-05 is PARTIALLY_CONFORMING.** Its initial-configuration child proofs remain valid within their recorded scope. They do not establish the unimplemented non-initial revision/approval path. IMP-06, IMP-07 and their dependent macro eligibility are now BLOCKED_DEPENDENCY. Existing completed child evidence is retained; no production code is reverted or newly enabled.

This review reconciles all 24 executable macro targets in MS-IMP-001 v1.0 + v1.1, the reserved IMP-WC family, 136 retained/refined child entries, 45 explicit HARD/PROGRAMME_GATE edges, completion gates, conditional paths and historical correction queues. It is a programme completeness/readiness review with focused source inspection of counterexamples, not a fresh behavioral certification of every historical class.

The accepted macro rules in `designs/MS-IMP-001.md` are unchanged. Its §29 overview is not a complete typed edge list: §21 requires at least one real Payment Obligation producer for Payment completion, not both entire IMP-11 and IMP-12 macros before readiness. The current projection explicitly preserves that distinction and the v1.1 Workforce additions.

## Current baseline and evidence
Repository: `development@91afef489997157558b32e1d312e7df147984a52`, fetched and equal to origin/development before changes. Baseline CI `34010680984`: SUCCESS — 1116 unit/governance + 388 PostgreSQL integration tests, zero failures/errors/skips. That success is regression evidence, not proof that unimplemented operations exist.
Current machine-readable navigation: `docs/development/implementation-programme-state.json`. This review supplies its rationale; neither replaces accepted programme or semantic authority.
The old v1.6 C4C record remains unchanged, blob `2de4ac61d9753c18e4079be0453417c214fa9ed6`. C4C's v1.7 bounded static-contract proof, C4D1 receipt proof and C4D2A recovery proof are retained.

## Exact authority pointers
- MS-IMP-001 v1.0, `designs/MS-IMP-001.md`: §4 — Edge Types; §5 — Existing Implementation May Satisfy a Dependency; §11 — IMP-05 — Merchant Definition, Configuration & Activation; §12 — IMP-06 — Read, Exposure & Transport Spine; §13 — IMP-07 — First Complete Vertical Slice: Publication → Enquiry; §16 — IMP-08C — Durable Execution Foundation; §21 — IMP-13 — Payment; §24 — IMP-16 — Cross-System Lifecycle & Historical Hardening; §30 — Ready-Target Rule; §33 — Target Completion Gate; §36 — Graph Refinement.
- MS-IMP-001 v1.1, `designs/MS-IMP-001 v1.1 — Post-Baseline Workforce Extension Alignment Amendment.md`: §2 — IMP-08B Scope Qualification; §3 — IMP-11 Scope Qualification; §4 — IMP-WF-01 — Workforce Scheduling / Timekeeping / Leave Extension; §5 — IMP-WF-01 Child-Specific Conditional Dependencies; §6 — Workforce Compensation / Payroll Implementation Extension; §7 — No Universal Workforce-Scheduling Dependency for Compensation; §8 — Downstream Hardening / Delivery Consequences.
- MS-PROT-040 v1.0, `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`: §5 — Configuration revision; §13 — Change set; §19 — Candidate generation; §37 — Approval authority; §43 — Concurrent configuration edits; §44 — Base-revision concurrency invariant; §46 — Configuration reinstatement.
- MS-PROT-040 v1.1, `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`: §2 — Ownership; §3 — Configuration Revision identity and immutability; §6 — Approval affinity; §7 — Activation preconditions; §10 — Atomic activation boundary.
- MS-PROT-040 v1.3, `designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md`: §2 — Problem and Governed Scope; §3 — Explicit Non-Goals. Its ordinary-first-configuration scope does not remove the general revision/change authority.
- MS-PROT-026 v1.1, `designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md`: §11 — Event Publication Is Not Reaction Completion; §17 — Per-Reaction Acknowledgement; §21 — Authoritative Downstream Mutation; §23 — Reaction Deduplication.
- MS-PROT-065 v1.1, `designs/MS-PROT-065 v1.1 — Production Durable Background Work, Timer, Attempt & Retry Execution Contract Amendment.md`: §11 — Attempt Evidence Before Consequential Execution; §38 — Event → Durable Work; §39 — Event Reaction Identity vs Work Identity.
- MS-IMPLEMENTATION-RULES-001 v1.7, `designs/IMPLEMENTATION-RULES.md`: §47 — Implementation Evidence; §52.5 — Graph refresh rule; §52.7 — Graph Evidence and Traceability; §52.9 — Existing implementation satisfaction; §52.10 — Macro target decomposition and completion propagation; §52.14 — Mandatory implementation-status synchronisation; §52.16 — Observation, Deduction and Uncertainty Rule; §52.18 — Counterevidence Before Completion; §52.19 — Evidence Locality Rule.

## Findings and corrections

### F1 — Required non-initial Configuration work was omitted from macro completion
OBSERVED:
- `docs/development/imp-05-conformance-baseline-2026-08-28.md`, §2, still classifies Configuration Revision as PARTIALLY_CONFORMING and explicitly excludes later revision/change-set mutation. Its §3 and final E/F closure nevertheless promote IMP-05.
- `docs/development/imp-05-initial-configuration-revision-approval-2026-08-29.md`, §6 — Explicit non-claims, excludes later revision/change-set mutation and identifies the weaker general approval reader.
- `src/main/java/mainstreet/semantic/configuration/ConfigurationRevisionAuthority.java:11` exposes only `materialiseInitial` as a writer.
- `src/main/java/mainstreet/infrastructure/persistence/configuration/JooqConfigurationRevisionAuthority.java:146` implements that writer; :221 tests version 1 existence. There is no second production writer for a replacement revision in the searched configuration source.
- `src/main/java/mainstreet/infrastructure/persistence/configuration/JooqInitialConfigurationRevisionApprovalAuthority.java:45` scopes approval to `configuration_version = 1` and absent base revision (:61–62).
- `src/main/java/mainstreet/semantic/configuration/ConfigurationRevisionApprovalAuthority.java:12` is a reader port, not a durable approval writer.
- `src/main/java/mainstreet/infrastructure/persistence/configuration/JooqConfigurationReleaseActivation.java:432` uses exact current initial approval only when no current revision exists; :453 uses the general reader for replacement.
- `src/test/java/mainstreet/infrastructure/persistence/configuration/JooqConfigurationReleaseActivationIT.java` supplies explicit approval fixtures for general activation. Its initial exact-approval test at :147 is real first-revision composition; these are different evidence scopes.

DEDUCED: initial establishment-to-active proof is not full Configuration lifecycle completion. No accepted macro amendment restricting IMP-05's Configuration Revision/approval scope to first activation was found. This is an **UNSUPPORTED_MACRO_COMPLETION / REQUIRED_IMPLEMENTATION_GAP**, not evidence of malicious or accidental runtime authorization, and not a newly invented semantic requirement.

Correction: add IMP-05-R0 (accepted-contract/dependency assessment, READY), R1 (base-affined non-initial revision/change-set writer), R2 (durable applicable non-initial approval), R3 (real replacement/reinstatement integration and negative/concurrency/retry proof). R1–R3 remain dependency-blocked until their prior assessment/implementation obligations are satisfied. Do not extrapolate the ordinary-first-Controller rule to later delegated approval.

IMP-05 becomes PARTIALLY_CONFORMING. IMP-06/07 historical implementation proofs are retained, but they cannot currently satisfy downstream macro eligibility through an incomplete HARD prerequisite. C4D2B's former local readiness is withdrawn for this macro reason.

### F2 — Narrow frontier navigation hid unfinished branches and correction queues
The previous frontier explicitly scoped itself to C4C→C4D. That scope was legitimate for the prior request, but it was insufficient as the sole current programme graph. The full index now retains C2/C2B/C3, C4/C4B/C4B2, C4D parents, C5 and the required owner corrections.
- `docs/development/IMP-00-IMPLEMENTATION-CONFORMANCE-BASELINE.md`, §§6.1, 6.2 and 11, records two conflicts that were not closed by later static registration.
- Booking remains event-global: `src/main/java/mainstreet/booking/BookingOutbox.java:16`; concrete adapter `src/main/java/mainstreet/infrastructure/persistence/booking/JooqBookingUnitOfWork.java:231`. Track as **IMP-08C-C4E**, not silently complete.
- Notification still assigns `dispatch.dispatchIdentity()` to the provider idempotency reference: `src/main/java/mainstreet/notification/NotificationDeliveryCoordinator.java:62`. Track as **IMP-10-N1**, blocked by its existing programme gates.
- `src/main/java/mainstreet/infrastructure/persistence/merchantaccount/JooqMerchantAccountEstablishmentPublicationOutbox.java:51`, :74, :96 supplies technical pending/lookup/publication operations. It does not prove complete consumer discovery. Track **C4B2** and leave C4B incomplete.
- `src/main/java/mainstreet/background/WorkAttempt.java:8` requires a result classification; `src/main/java/mainstreet/background/DurableWorkStore.java:21` records attempts. These are not proof of a registered, start/progression/recovery worker. C2B/C3 and C5 remain open.

### F3 — Deferred triggers and amendment-specific gates lacked a complete current index
- IMP-08B means Workforce Access & Delegation; IMP-11 means Customer Booking / Appointment Scheduling.
- IMP-WF-01 requires IMP-08B. Its optional durable/provider/Notification/Appointment paths are conditional child edges, not universal HARD edges.
- IMP-WF-01 participates in IMP-16 completion. IMP-WC remains unactivated pending approved decomposition; Payroll is not silently promoted into the current MVP.
- IMP-06 T2d/T3c have concrete IMP-07 instances, not blanket portfolio closure; the remainder stays on demand for later real surfaces.
- IMP-06 T4c's old “concrete query adapter absent” explanation is stale. The concrete contracts now present are single-resource: `src/main/java/mainstreet/publication/delivery/PublicOpportunityQueryContract.java:10` and `src/main/java/mainstreet/enquiry/delivery/MerchantEnquiryQueryContract.java:7`. Neither creates a collection continuation requirement. Correct trigger: a concrete collection query requiring continuation. No invented token format or false “complete” label.
- IMP-07 P6 remains distinct and dependency-blocked on authoritative Opportunity actionability facts.
- MS-WATCH-001 and MS-WATCH-002 remain WATCHING. I1/I2/V1 provide the concrete source/binding evidence that was pending at IMP-06 closure; this does not retire persistent surveillance or create a current hard cycle.

## Macro reconciliation and historical evidence
`implementation-programme-state.json` contains the complete typed state; `inventorySources` records the closure/decomposition files inspected. The child inventory preserves explicit IMP-01, IMP-05, IMP-06, IMP-07 and IMP-08C node identities and retains on-demand/blocked obligations separately from completed scope.
- IMP-00..IMP-04: retain conforming closure evidence. The old IMP-04 security-generation closure error was already corrected by its v2 closure and durable generation implementation; it is not a new open gap.
- IMP-05: PARTIALLY_CONFORMING due to F1, with initial child proofs retained.
- IMP-06..IMP-15 and IMP-WF-01: current dependency eligibility blocked. Their historical or partial implementation is not erased.
- IMP-16 may be assessed incrementally; completion requires its entire accepted set including IMP-WF-01.
- IMP-17..IMP-20 remain blocked by their accepted gates; actual recovery exercise and controlled live-readiness are not inferred from CI.
- Existing pre-IMP-00 Booking/Ordering/Payment/Notification code is baseline evidence, not evidence that later macro nodes were secretly completed out of order.

## Counterevidence and completion falsification
1. **Could the initial activation milestone legitimately narrow all of IMP-05?** The milestone is required, but §11 also includes Configuration Revision and approval, and §33 requires all required children. The original Configuration authority includes change sets/base revisions; v1.3 explicitly narrows only ordinary first approval. No accepted programme narrowing was found.
2. **Could a general activation test or reader port close the missing writers?** No. The tests demonstrate rejection/activation using supplied candidates and approval evidence; they do not establish the durable authoritative creation path. Existing generic activation is preserved.
3. **Must C4D2B depend on all C2/C3 first?** No. MS-PROT-065 v1.1 §38 explicitly permits reactions without DurableWorkInstruction. Do not invent that edge. C4D2B is blocked by the corrected macro chain and still owes its own scoped execution/recovery evidence.
4. **Were C4C/C4D1/C4D2A falsely complete?** Their bounded proofs survive this graph review; none claims the missing Configuration writer, Booking correction, complete background runner or macro closure.
5. **Does every historical blocked label remain a live blocker?** No. IMP-04's old security-generation block was resolved. T4c is now on demand for collection continuation. Later genuine gaps and persistent watches remain explicit.
6. **Does a green suite prove no node was skipped?** No. The suite previously passed with F1 absent. New graph tests prevent disappearance and unsupported readiness; they do not replace future production conformance tests.

UNCERTAIN / future work: exact minimal non-initial Configuration command/delegated approval contract decomposition and the subsequent production proof. IMP-05-R0 must resolve that from accepted composite authority before implementation; any actual missing semantic rule must be escalated then. No unsupported policy or speculative writer is introduced by this review.

## Verification record
Tests first: the new `ImplementationGraphIntegrityTest` initially had a regex-literal fixture compilation error; that was corrected before evaluating the graph. The corrected test then ran 5 tests: 1 failure for stale current completion and 4 errors for the absent programme index. After correction all 8 focused current/historical governance tests passed. This is governance RED, not a production regression.
The five tests cover current classification, accepted macro/edge completeness, acyclic dependency-closed eligibility (including an injected invalid READY state), conditional/completion gate distinctions and retained child/evidence reachability.
Exact test locations: `src/test/java/mainstreet/governance/ImplementationGraphIntegrityTest.java:34`, :44, :70, :82 and :112. The accepted-edge check compares the complete explicit edge set parsed from MS-IMP-001 with the current graph, rejecting invented as well as missing edges. The existing historical-proof and C4C evidence guards were updated only for current navigation/eligibility; their historical proof assertions remain.
Local full verification: `mvn --batch-mode -Dmaven.repo.local=<workspace>/work/m2 test` — 1121 unit/governance tests, zero failures/errors/skips. `git diff --check` passed. No local PostgreSQL result is claimed; full Java 25/PostgreSQL CI remains the required closing gate.
**Correction proof:** `9bc6a4a3d8ec6c6b1b0a291412320336151d4219`, GitHub Actions run `34011672684` — SUCCESS — 1121 unit/governance + 388 PostgreSQL integration tests, zero failures/errors/skips. Command: `mvn --batch-mode clean verify -Ppostgres-it`, Java 25/PostgreSQL 18, `.github/workflows/maven-tests.yml`.
The programme-integrity correction is complete within this review scope on successful cycle-closing synchronization CI. IMP-05 remains PARTIALLY_CONFORMING: passing the graph tests does not supply its missing authoritative implementation. Historical evidence is not edited to manufacture conformance.
