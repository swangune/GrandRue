# IMP-05 — Durable Onboarding Case / Evidence Store

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation  
**Fine-grained node:** C2 — Durable Onboarding Case / evidence store  
**Date:** 28 August 2026  
**Implementation head verified:** `95154e3b7814800bffb5630bfa43033ceab03122`  
**Status:** **CONFORMING_COMPLETE**

## 1. Governing authority

This node implements the persistence boundary required by:

- `designs/MS-PROT-052 — Adaptive Onboarding Prompt, Question Catalogue & Decision Presentation Contract.md`;
- `designs/MS-PROT-052 v1.2 — Onboarding Case Lifecycle, Concurrency & Initial Configuration Intent Handoff Amendment.md`;
- `designs/MS-IMP-001.md`; and
- `designs/IMPLEMENTATION-RULES.md`.

The completed boundary is a durable, merchant-owned Onboarding Case and immutable answer-evidence authority. It does not promote raw onboarding answers to Merchant Configuration or runtime authority.

## 2. Implemented production boundary

The implementation provides:

- one durable current ordinary initial Onboarding Case per Merchant Account;
- case ownership by Merchant Account rather than the initiating Controller;
- exact opaque case revisions advanced by material answer mutation;
- immutable answer evidence containing stable question definition/version, answer form, option-set or structured-value reference, origin, actor, time and optional semantic release;
- corrections that append evidence and atomically replace the effective-answer pointer without rewriting history;
- expected-revision optimistic concurrency;
- retry-safe case-start and answer-mutation identities;
- exact replay of a previously committed mutation outcome even after a later correction;
- rejection when a request identifier is reused with different intent; and
- deterministic persistence semantics with no AI dependency.

Production types include `OnboardingCaseEvidenceStore`, start and answer commands/results, evidence/revision values, persistence failure categories and the jOOQ/PostgreSQL adapter.

Flyway migration `V34__onboarding__create_case_evidence_authority.sql` adds:

- `onboarding_case`;
- `onboarding_start_request`;
- `onboarding_case_revision`;
- `onboarding_answer_evidence`; and
- `onboarding_effective_answer`.

A partial unique index enforces the one-current-initial-case invariant. Transaction-scoped advisory locks serialize merchant start, case mutation and request-identity races without introducing last-write-wins behavior.

## 3. Conformance evidence

| Required behavior | Executable evidence | Result |
|---|---|---|
| Stable answer definition and provenance | `OnboardingAnswerEvidenceTest` | PASS |
| Exactly one option-set or structured-value reference | `OnboardingAnswerEvidenceTest` | PASS |
| Repeated starts resolve one current initial case | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Start retries persist exact receipts without a duplicate case | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Answer mutation advances the exact case revision | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Correction preserves prior evidence and moves the effective pointer | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Stale expected revision conflicts without appending evidence | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Retry after later correction returns the original committed result | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Reused request identifier with different intent is rejected | `JooqOnboardingCaseEvidenceStoreIT` | PASS |
| Concurrent mutations from one revision yield one winner | `JooqOnboardingCaseEvidenceStoreIT` | PASS |

## 4. IMPLEMENTATION-RULES trace

The test-first sequence is preserved in repository history:

1. `8a1751594083c486638ce67b89bbcda66569a884` — failing executable specification; Maven Tests #1288 failed at test compilation because the new C2 production contracts did not exist.
2. `48ec168df55e69443cae74a0dc6cb393f3c72c33` — initial production persistence implementation.
3. `7678c35cee263ad7c2f6d5dbaf0506c88b00eddc` — exact retry-intent reconstruction after later corrections.
4. `95154e3b7814800bffb5630bfa43033ceab03122` — integration-fixture correction and verified GREEN head.

Maven Tests #1290 showed that production compilation, all 642 unit tests and Flyway V34 succeeded; its six integration errors occurred during fixture reset before behavioral test bodies ran. The atomic fixture correction was then independently verified by Maven Tests #1291.

## 5. Verified baseline

```text
workflow:                           Maven Tests
run number:                         1291
run id:                             33214448643
head:                               95154e3b7814800bffb5630bfa43033ceab03122
result:                             SUCCESS
Java target / CI JDK:               25 / Temurin 25
PostgreSQL:                         18.6
Flyway migrations:                 34
production Java sources compiled:  601
test Java sources compiled:        226
unit tests:                         642 PASS
PostgreSQL integration tests:      203 PASS
total Maven tests:                  845 PASS
failures / errors / skipped:       0 / 0 / 0
```

Focused C2 evidence comprises two unit tests and six PostgreSQL integration tests.

## 6. Explicit non-claims

C2 does not claim completion of:

- C3 adaptive catalogue/frontier recomputation or superseded-branch pruning;
- C4 final review, case submission or reviewed-revision affinity;
- C5 immutable Initial Configuration Intent handoff;
- profile-fact adoption or conflict resolution;
- trusted Controller/delegated-staff application authorization;
- Merchant Configuration revision materialisation;
- transport/UI delivery; or
- end-to-end IMP-05 completion.

Raw answers remain non-executable evidence. Any later configuration effect must pass through the distinct reviewed intent, configuration revision, compilation, publication and activation authorities.

## 7. Graph consequence

C2 is **CONFORMING_COMPLETE**. C3 deterministic onboarding recomputation is now the selected **READY** node. IMP-05 remains **PARTIALLY_CONFORMING** until all required children and the establishment-to-active completion proof conform.
