# IMP-05 — Configuration Validation Evidence Authority

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** D2b — Durable exact validation / Resolved Configuration Package evidence
**Date:** 29 August 2026
**Status:** **CONFORMING_COMPLETE**

## 1. Governing authority

This slice implements the durable validation-evidence boundary required by:

- `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`;
- `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`;
- `designs/MS-PROT-040 v1.2 — Initial Merchant Configuration Bootstrap & Serving-Deployment Admission Amendment.md`;
- `designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md`;
- `designs/MS-IMP-001.md`; and
- `designs/IMPLEMENTATION-RULES.md`.

D2b records successful validation only from a real `ResolvedConfigurationPackage` that exactly matches an existing immutable Configuration Revision. It does not accept a caller-provided validity boolean or treat pre-revision onboarding review as configuration evidence.

## 2. Implemented production boundary

The implementation provides:

- a Configuration-owned authority to record and retrieve immutable successful-validation evidence;
- exact Merchant Scope, Configuration Revision and semantic-release affinity;
- an exact retained immutable Resolved Configuration Package evidence identity;
- compiler identity and package-generation provenance taken from the supplied package rather than restated by the caller;
- a stored successful outcome and evidence-production time;
- authoritative reconstruction of the stored Configuration Revision before accepting evidence;
- package-to-revision equality enforcement through `ResolvedConfigurationPackage.requireSourceConfiguration`;
- retry identity that returns the original record only for the same complete evidence intent;
- fail-closed rejection of revision/package affinity mismatches and reused evidence identities; and
- database uniqueness for both validation-evidence and package-evidence identities.

## 3. Durable representation

Flyway migration `V38__configuration__create_validation_evidence.sql`:

- establishes the revision/release composite key used by dependent evidence;
- creates append-only `configuration_validation_evidence` rows;
- binds each row by foreign key to the exact merchant, Configuration Revision and semantic release;
- permits only the `SUCCEEDED` validation outcome;
- retains exact package-evidence and compiler provenance; and
- rejects blank identities and reuse of an exact package-evidence identity.

## 4. Conformance evidence

| Required behavior | Executable evidence | Result |
|---|---|---|
| Record and read immutable exact revision/release/package evidence | `JooqConfigurationValidationEvidenceAuthorityIT` | PASS |
| Exact retry returns the original durable evidence | `JooqConfigurationValidationEvidenceAuthorityIT` | PASS |
| Package affinity mismatch with the stored revision fails closed | `JooqConfigurationValidationEvidenceAuthorityIT` | PASS |
| Reuse of a validation-evidence identity for different intent fails closed | `JooqConfigurationValidationEvidenceAuthorityIT` | PASS |
| Database rejects reuse of the exact package-evidence reference | `JooqConfigurationValidationEvidenceAuthorityIT` | PASS |

## 5. IMPLEMENTATION-RULES trace

The RED step added the PostgreSQL integration contract before D2b production symbols existed. Focused test compilation failed on the missing validation evidence authority, command, record, outcome and categorized persistence-failure contracts. The GREEN step added only those semantic contracts, the jOOQ authority and the V38 durable representation.

Focused verification passed the five D2b contracts together with the six upstream Configuration Revision authority contracts (11 PASS). Fixing the new foreign-key fixture dependency then passed the affected onboarding, revision and validation suites together (25 PASS). The first canonical run correctly exposed that one older onboarding cleanup fixture did not truncate the new dependent evidence table; that fixture was corrected and the canonical gate was repeated from a clean build.

The final canonical gate ran against PostgreSQL 18.6:

```text
mvn --batch-mode clean verify -Ppostgres-it

Java release target:                   25
Flyway migrations validated:           38
production Java sources compiled:      644
test Java sources compiled:            231
unit / conformance tests:              662 PASS
PostgreSQL integration tests:          222 PASS
total Maven tests:                     884 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

## 6. Explicit non-claims

D2b does not claim completion of:

- immutable business-facing impact-review evidence (D2c);
- current-Controller Configuration Revision approval or approval persistence (D3);
- publication, activation or serving-deployment admission;
- later Configuration Revision/change-set mutation;
- HTTP/UI transport; or
- end-to-end IMP-05 completion.

The retained package evidence is an exact immutable provenance/reference contract; it is not a newly invented universal digest or alternative RCP representation.

## 7. Graph consequence

D2a deterministic compiler/RCP modelling, D2b durable exact validation/package evidence and D2c immutable business-facing impact-review evidence are **CONFORMING_COMPLETE**. D3 current-Controller exact approval authority/persistence is now the smallest critical **READY** node. IMP-05 remains **PARTIALLY_CONFORMING** until all required children and the establishment-to-active completion proof conform.
