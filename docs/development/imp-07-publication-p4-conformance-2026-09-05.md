# IMP-07 Publication P4 Conformance Evidence

**Date:** 5 September 2026
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Implementation node:** IMP-07-P4 — Publication PUBLIC Exposure contracts/evaluation
**Authority basis:** accepted `MS-PROT-046 v1.2 + v1.3`, accepted `MS-PROT-027 v1.11`, and completed IMP-06 Exposure spine
**Pre-closure verification base:** `development@452426de5eaba5590d1ce08f53e47f0f632862b6` plus the staged P4 implementation
**Pre-closure full verification:** local `mvn --batch-mode clean verify -Ppostgres-it` — **BUILD SUCCESS**
**Post-inspection adversarial proof repair:** `development@96550fb6e8e04fad233b8d3e9e1193d43d4fb7b8` — exact-head GitHub Actions full gate **SUCCESS**
**Status:** **CONFORMING_COMPLETE effective when the cycle-closing head containing this evidence passes full verification**

This record is implementation evidence only. It creates no semantic, architectural or product authority.

---

## 1. P4 responsibility proved

P4 remains bounded to:

```text
Publication-owned legitimacy for PUBLIC Opportunity Exposure candidacy
+
exact PUBLIC Opportunity Exposure contract registration
+
Publication-owned publishFrom / publishUntil requirement evaluation
+
mechanical use of the generic IMP-06 Exposure resolver
```

It does not materialise the request-scoped public Opportunity representation, determine Opportunity actionability, create Public Interaction participation, create Enquiry state or introduce concrete transport.

---

## 2. Executable conformance matrix

```text
only a currently PUBLISHED Opportunity may produce a PUBLIC candidate              PASS
DRAFT Opportunity produces no PUBLIC candidate                                      PASS
WITHDRAWN Opportunity produces no PUBLIC candidate                                  PASS
candidate identity is owner-qualified and instance-qualified                        PASS
candidate element is publication/public-opportunity-representation                  PASS
contract audience is PUBLIC                                                         PASS
contract baseline is EXPOSE subject to the owned requirement                        PASS
contract has no merchant-choice source                                               PASS
Exposure window is the only P4 requirement                                           PASS
Exposure evidence is reconstructed from the exact published revision                 PASS
published revision rather than the unpublished current revision remains authoritative PASS
concurrent loss/change of the published binding fails closed                         PASS
mismatched published-revision affinity is rejected                                   PASS
trusted admission evaluatedAt is reused for temporal evaluation                      PASS
publishFrom exact-instant lower bound is inclusive                                   PASS
publishUntil exact-instant upper bound is exclusive                                  PASS
calendar-date publishUntil includes the whole recorded local date                    PASS
absent publishFrom means no lower bound                                               PASS
absent publishUntil means no upper bound                                              PASS
missing current PUBLISHED evidence withholds fail closed                             PASS
generic Exposure resolution remains value-blind                                      PASS
P5 request-scoped representation remains separate                                    PASS
P6 Opportunity actionability remains separate                                        PASS
Opportunity → Enquiry participation remains separate                                 PASS
Enquiry persistence/idempotency/revalidation remains separate                        PASS
concrete transport remains separate                                                   PASS
```

The read adapter deliberately resolves temporal evidence from `publishedRevisionIdentity`. A later material edit may advance the current revision without silently changing what is publicly published. The adapter rechecks the published binding after material reconstruction and fails closed if that binding changes. The adapter also rejects material whose merchant, Opportunity or revision affinity does not match the exact published binding.

---

## 3. Direct proof inventory

Production boundary:

```text
OpportunityPublicExposureEvidence
OpportunityPublicExposureReadPort
AuthorityBackedOpportunityPublicExposureReadPort
OpportunityPublicExposureReferences
OpportunityPublicExposureCandidateSource
OpportunityExposureWindowRequirementEvaluator
OpportunityPublicExposureContractPortfolio
```

Executable proof:

```text
AuthorityBackedOpportunityPublicExposureReadPortTest
    4 tests PASS
    - exact published revision remains authoritative over a newer unpublished revision
    - DRAFT and WITHDRAWN evidence fails closed
    - published binding changed between first and second authority reads fails closed
    - mismatched published-revision affinity is rejected

OpportunityPublicExposureCandidateSourceTest
    1 test PASS

OpportunityPublicExposureP4Test
    4 tests PASS

direct P4 proof total
    9 tests
```

`OpportunityPublicExposureP4Test` additionally proves that `public-opportunity-actionability` is not registered by P4.

---

## 4. Repository verification trace

Original targeted gate before the adversarial proof repair:

```text
mvn --batch-mode "-Dtest=AuthorityBackedOpportunityPublicExposureReadPortTest,OpportunityPublicExposureCandidateSourceTest,OpportunityPublicExposureP4Test" test

Tests run: 7
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

Original pre-closure full gate:

```text
git diff --cached --check
mvn --batch-mode clean verify -Ppostgres-it

unit/governance:
    968 unit/governance tests
    failures 0
    errors   0
    skipped  0

PostgreSQL/Failsafe:
    PostgreSQL 18.6
    schema version 55
    55 migrations validated
    329 PostgreSQL integration tests
    failures 0
    errors   0
    skipped  0

result:
    BUILD SUCCESS
    finished 2026-09-05T05:24:16+01:00
```

Post-inspection adversarial proof repair:

```text
commit:
    96550fb6e8e04fad233b8d3e9e1193d43d4fb7b8
    test(imp-07): prove public exposure fail-closed guards

scope:
    test-only change to AuthorityBackedOpportunityPublicExposureReadPortTest
    no production or architectural code changed

new direct executable proof:
    concurrent published-binding change between authority reads fails closed
    mismatched published-revision affinity is rejected

exact-head GitHub Actions:
    run 33947486840
    mvn --batch-mode clean verify -Ppostgres-it
    SUCCESS
```

The proof repair closes an evidence-integrity gap found during post-closure inspection. It does not amend P4 semantics, ownership or production behaviour. No P4 verification finding requires another design amendment.

---

## 5. Closure falsification result

The closure review did not identify a semantic or ownership expansion beyond P4.

The implementation preserves the required separations:

```text
Publication lifecycle legitimacy        upstream of generic Exposure resolution
Publication temporal evidence           Publication-owned narrow read port
Exposure contract resolution            generic / mechanical
public representation materialisation   P5, not P4
Opportunity actionability               P6, not P4
Public Interaction participation        I1/I2, not P4
Enquiry authoritative state             E1/E2/E3, not P4
concrete transport                      T1/T2/T3, not P4
```

The fail-closed path is explicit and now directly exercised at the adapter boundary: inability to establish current PUBLISHED evidence, loss/change of the published binding between authority reads, inability to recover the exact published revision, or mismatched published-revision affinity cannot produce positive public Exposure.

---

## 6. Closure rule and successor

This evidence does **not** recursively declare its own commit verified. Under `IMPLEMENTATION-RULES.md`, P4 becomes effective `CONFORMING_COMPLETE` only when the cycle-closing head containing:

```text
P4 production implementation
+ P4 executable tests
+ this P4 conformance evidence
+ P4 closure graph refresh
+ synchronised implementation-status.md
+ programme-gate assertions
```

passes the repository full verification gate:

`mvn --batch-mode clean verify -Ppostgres-it`

After that gate succeeds, `P5 — Request-scoped public Opportunity representation` becomes the smallest dependency-complete READY node because `P4 + G0` are then conforming. P6 remains separate.
