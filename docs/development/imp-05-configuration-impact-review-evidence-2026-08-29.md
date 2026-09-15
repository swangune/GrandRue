# IMP-05 — Configuration Impact Review Evidence Authority

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** D2c — Immutable business-facing impact-review evidence
**Date:** 29 August 2026
**Status:** **CONFORMING_COMPLETE**

## 1. Governing authority

This slice implements the durable impact-review-evidence boundary required by:

- `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`;
- `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`;
- `designs/MS-PROT-040 v1.2 — Initial Merchant Configuration Bootstrap & Serving-Deployment Admission Amendment.md`;
- `designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md`;
- `designs/MS-IMP-001.md`; and
- `designs/IMPLEMENTATION-RULES.md`.

D2c records one completed impact-analysis result only when its exact merchant, Configuration Revision, semantic release, validation evidence and retained Resolved Configuration Package evidence reference match existing D2b authority. It is separate from pre-revision onboarding review, merchant approval and activation.

## 2. Implemented production boundary

The implementation provides:

- a Configuration-owned authority to record and retrieve immutable impact-review evidence;
- exact merchant, revision, semantic-release, validation-evidence and package-evidence affinity;
- one or more ordered business-facing effects expressed independently of semantic compiler internals;
- zero or more ordered findings, each with exactly one accepted `BLOCKING`, `CONSEQUENTIAL`, `INFORMATIONAL` or `EXISTING_COMMITMENT_CONFLICT` classification;
- exact impact-analysis completion time;
- database and authority reconstruction without substituting later effect content or findings;
- exact retry that returns the original evidence only for the same complete intent;
- fail-closed rejection of absent validation evidence, affinity mismatch and changed content under a reused evidence identity; and
- a blocking-finding predicate for the downstream approval authority without collapsing review evidence into approval.

No boolean or UI/request assertion can stand in for the completed analysis result or durable evidence identity.

## 3. Durable representation

Flyway migration `V39__configuration__create_impact_review_evidence.sql`:

- adds the exact validation-evidence composite affinity key;
- creates the immutable impact-review evidence root bound by foreign key to the exact validation/revision/release/package tuple;
- stores ordered nonblank business-facing effects in a dependent relation;
- stores ordered nonblank findings in a separate dependent relation;
- constrains every finding to exactly one accepted MS-PROT-040 classification; and
- preserves zero findings while requiring the authority to retain at least one business-facing effect.

## 4. Conformance evidence

| Required behavior | Executable evidence | Result |
|---|---|---|
| Record and reconstruct exact ordered business-facing content and findings | `JooqConfigurationImpactReviewEvidenceAuthorityIT` | PASS |
| Preserve a completed review with zero findings | `JooqConfigurationImpactReviewEvidenceAuthorityIT` | PASS |
| Preserve blocking and existing-commitment findings in review order | `JooqConfigurationImpactReviewEvidenceAuthorityIT` | PASS |
| Exact retry returns the original immutable review | `JooqConfigurationImpactReviewEvidenceAuthorityIT` | PASS |
| Missing validation evidence creates no review | `JooqConfigurationImpactReviewEvidenceAuthorityIT` | PASS |
| Validation/package affinity mismatch creates no review | `JooqConfigurationImpactReviewEvidenceAuthorityIT` | PASS |
| Changed content under one review identity is rejected | `JooqConfigurationImpactReviewEvidenceAuthorityIT` | PASS |
| Database rejects an unclassified finding | `JooqConfigurationImpactReviewEvidenceAuthorityIT` | PASS |

## 5. IMPLEMENTATION-RULES trace

The RED step added the PostgreSQL integration contract before D2c production symbols existed. Focused test compilation failed on 42 missing impact-analysis, evidence, classification, authority and categorized persistence symbols. The GREEN step added only those semantic contracts, the jOOQ authority and the V39 durable representation.

The eight focused D2c contracts passed against PostgreSQL 18.6. The affected onboarding, Configuration Revision, D2b validation and D2c review suites then passed together (32 PASS), including cleanup changes required by the new foreign-key dependency. The canonical gate subsequently passed from a clean build.

```text
mvn --batch-mode clean verify -Ppostgres-it

Java release target:                   25
Flyway migrations validated:           39
production Java sources compiled:      653
test Java sources compiled:            232
unit / conformance tests:              662 PASS
PostgreSQL integration tests:          230 PASS
total Maven tests:                     892 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

## 6. Explicit non-claims

D2c does not claim completion of:

- an impact-review UI, transport representation or rendering layout;
- current-Controller Configuration Revision approval or approval persistence (D3);
- activation, publication or serving-deployment admission;
- later Configuration Revision/change-set mutation;
- AI explanation of the retained evidence; or
- end-to-end IMP-05 completion.

The completed impact-analysis result is trusted internal evidence content, not a caller-provided `reviewed=true` assertion and not merchant approval.

## 7. Graph consequence

D2a deterministic compiler/RCP modelling, D2b durable exact validation/package evidence and D2c immutable business-facing impact-review evidence are **CONFORMING_COMPLETE**. D3 current-Controller exact approval authority/persistence is now the smallest critical **READY** node. IMP-05 remains **PARTIALLY_CONFORMING** until all required children and the establishment-to-active completion proof conform.
