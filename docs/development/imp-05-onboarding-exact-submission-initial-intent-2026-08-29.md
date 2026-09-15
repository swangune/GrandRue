# IMP-05 — Exact Onboarding Submission and Initial Configuration Intent

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained nodes:** C4 — Exact final review/submission; C5 — Initial Configuration Intent
**Date:** 29 August 2026
**Status:** **CONFORMING_COMPLETE**

## 1. Governing authority

This slice implements the exact submission and immutable handoff boundary required by:

- `designs/MS-PROT-052 — Adaptive Onboarding Prompt, Question Catalogue & Decision Presentation Contract.md`;
- `designs/MS-PROT-052 v1.1 — Baseline Public Presence & Discovery-Axis Amendment.md`;
- `designs/MS-PROT-052 v1.2 — Onboarding Case Lifecycle, Concurrency & Initial Configuration Intent Handoff Amendment.md`;
- `designs/MS-PROT-040 v1.2 — Initial Merchant Configuration Bootstrap & Serving-Deployment Admission Amendment.md`;
- `designs/MS-IMP-001.md`; and
- `designs/IMPLEMENTATION-RULES.md`.

C4 and C5 were refined into one inseparable atomic slice. MS-PROT-052 v1.2 requires current-revision review validation, Initial Configuration Intent creation and the `IN_PROGRESS` to `SUBMITTED` lifecycle transition to commit as one transaction. Implementing either node alone would create a forbidden partial state.

## 2. Implemented production boundary

The implementation provides:

- exact Onboarding Case/revision and reviewed-candidate affinity;
- submission-time reconstruction and deterministic recomputation from durable effective evidence;
- fail-closed completeness and current Controller/delegated-staff authority checks inside the owning transaction;
- rejection of stale review, changed evidence and incomplete prompt frontiers;
- immutable Initial Configuration Intent identity, source affinity, reviewed semantic seeds, provenance references and unresolved prompt keys;
- an append-only `SUBMIT` case revision and atomic `IN_PROGRESS` to `SUBMITTED` transition;
- request-identity retry that returns the originally committed intent only for the same exact payload;
- request-identity reuse rejection for a different payload;
- intent-identity uniqueness and per-case concurrency serialization; and
- exact durable reconstruction of an Initial Configuration Intent by identity.

The intent remains non-executable proposal/handoff evidence. It does not create Merchant Configuration, select or publish a semantic release, grant entitlement, activate runtime behavior or mutate profile/location authority.

## 3. Durable representation

Flyway migration `V35__onboarding__create_initial_configuration_intent.sql`:

- admits `SUBMIT` as an Onboarding Case mutation kind;
- creates the immutable Initial Configuration Intent root;
- preserves exact source-case and submitted-revision foreign-key affinity;
- stores semantic seeds, provenance references and unresolved prompt keys as normalized immutable children;
- enforces request and intent identities; and
- uses a PostgreSQL `NULLS NOT DISTINCT` uniqueness index so nullable context scope remains an exact set identity.

## 4. Conformance evidence

| Required behavior | Executable evidence | Result |
|---|---|---|
| Exact reviewed revision submits atomically with immutable intent | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Successful retry returns the original intent | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Stale reviewed revision rolls back without intent or lifecycle mutation | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Current authority denial rolls back without partial state | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Reused request identity with different payload is rejected | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Concurrent submissions commit at most one intent | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Semantic seeds and provenance references survive exact reconstruction | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Completeness, stale review, tamper and authority decisions | `OnboardingSubmissionReadinessEvaluatorTest` and existing final-review unit evidence | PASS |

## 5. IMPLEMENTATION-RULES trace

The RED step added the submission integration contract before production types existed; focused test compilation failed on the missing command, intent and submission-store symbols. The GREEN step added the domain contracts, PostgreSQL transaction boundary, migration and reconstruction path. A first focused migration run exposed invalid placement of `NULLS NOT DISTINCT`; the migration was corrected to a dedicated unique index and the focused PostgreSQL profile then passed.

The final canonical gate ran against PostgreSQL 18.6:

```text
mvn --batch-mode clean verify -Ppostgres-it

Java release target:                   25
Flyway migrations validated:           35
production Java sources compiled:      629
test Java sources compiled:            229
unit / conformance tests:              662 PASS
PostgreSQL integration tests:          211 PASS
total Maven tests:                     873 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

## 6. Explicit non-claims

C4/C5 do not claim completion of:

- Configuration Revision materialisation or authoritative persistence;
- semantic-release selection, approval, compilation, publication or activation for this intent;
- profile, location or business-hours fact adoption;
- AI inference or AI-generated proposal authority;
- HTTP/UI transport; or
- end-to-end IMP-05 completion.

## 7. Graph consequence

C4 and C5 are **CONFORMING_COMPLETE**. D1 Configuration Revision materialisation/persistence is now the selected critical **READY** node. B2 durable Business Hours revision authority remains independently READY. IMP-05 remains **PARTIALLY_CONFORMING** until all required children and the establishment-to-active completion proof conform.
