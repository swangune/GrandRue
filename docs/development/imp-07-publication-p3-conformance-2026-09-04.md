# IMP-07 Publication P3 Conformance Evidence

**Date:** 4 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Implementation node:** IMP-07-P3 — Application mutation + transaction + retry idempotency  
**Authority basis:** accepted Publication/application/transaction authority, including `MS-PROT-059 v1.0` logical-operation idempotency  
**Code-bearing verification head:** `development@f481ec46c4253d0db3dbfa95e397cf596c010860`  
**Decisive full verification:** GitHub Actions run `33883111122` — **SUCCESS**  
**Status:** **CONFORMING_COMPLETE effective when the cycle-closing head containing this evidence passes full verification**

This record is implementation evidence only. It creates no semantic, architectural or product authority.

---

## 1. P3 responsibility proved

P3 remains bounded to:

```text
Publication application mutation orchestration
+
required atomic transaction composition
+
MS-PROT-059 logical-operation retry idempotency
```

The implemented boundary proves that the canonical application request identity is not transport identity, trace identity, Merchant Scope identity or Publication/business-object identity. Retry reconciliation is evaluated from the application request record before fresh current-state mutation evaluation.

---

## 2. Executable conformance matrix

```text
canonical ApplicationRequestIdentity is explicit and independent             PASS
same logical request + same intent reconciles to the prior committed result   PASS
retry reconciliation precedes fresh current-state evaluation                  PASS
same logical request + materially changed intent conflicts explicitly         PASS
distinct stale request remains an authoritative revision conflict             PASS
state + material + request record compose atomically                          PASS
forced downstream failure rolls the whole application mutation back           PASS
concurrent same logical deliveries serialize/reconcile to one business effect PASS
one current state after concurrent replay                                     PASS
one revision identity after concurrent replay                                 PASS
one material revision after concurrent replay                                 PASS
one application request record after concurrent replay                        PASS
no duplicate Publication History from DRAFT retry/replay                       PASS
Publication state/material/history ownership remains in P2B                    PASS
no Exposure semantics introduced                                               PASS
no Enquiry semantics introduced                                                PASS
no transport semantics introduced                                              PASS
no actor-authority semantics introduced                                        PASS
```

The concurrent replay proof is test-only and directly exercises two simultaneous deliveries carrying the same `ApplicationRequestIdentity`. Both deliveries reconcile to the same result, and authoritative row counts prove that retry delivery does not multiply business effects.

---

## 3. Verification trace

P3 was deliberately not closed on a generic Maven failure. The trace distinguishes semantic proof from fixture compatibility:

```text
P3 contract RED / proof start
    7c92d5a6377fe1298f40b16712f2c2aa5d625afa

P3 retry-idempotency implementation
    03e168ba82f0bf5e07a68a91f0a5da78d69439d8

atomic rollback proof head
    20bcefce94d1de98d1aaa729bea747d6389a4bc4
    961/961 unit/governance tests green
    JooqOpportunityPublicationApplicationServiceIT 6/6 green
    remaining PostgreSQL errors traced to pre-V55 fixture truncation sets

mechanical fixture compatibility repair
    556ff3c9458a53981834f4dbf4b4742ea55db558
    run 33881445271 SUCCESS

concurrent logical-retry reconciliation proof
    f481ec46c4253d0db3dbfa95e397cf596c010860
    run 33883111122 SUCCESS
```

The V55-related fixture repair changed only older Publication integration-test cleanup compatibility. It is not evidence of a P3 production/design defect and does not alter P3 semantics.

---

## 4. Closure falsification result

The closure review found no remaining semantic, ownership or transaction defect within P3. The last identified gap was evidence-only: concurrent delivery of the same logical application request had not been proved directly. `f481ec46...` closes that proof gap and its full unit + PostgreSQL integration gate passed.

No P3 finding requires another design amendment.

---

## 5. Closure rule and successor

This evidence does **not** recursively declare its own commit verified. Under `IMPLEMENTATION-RULES.md`, P3 becomes effective `CONFORMING_COMPLETE` only when the cycle-closing head containing:

```text
this P3 conformance evidence
+ P3 closure graph refresh
+ synchronised implementation-status.md
+ programme-gate assertions
```

passes the repository full verification gate:

`mvn --batch-mode clean verify -Ppostgres-it`

After that gate succeeds, `P4 — Publication PUBLIC Exposure contracts/evaluation` becomes the smallest dependency-complete READY node because its dependencies are `P3 + G0`. P5 remains blocked on P4.
