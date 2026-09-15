# IMP-07 Graph Refresh — P2B MS-PROT-046 v1.3 Closure

**Date:** 4 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-07 — Publication → Enquiry vertical slice  
**Authority basis:** accepted composite `MS-PROT-046 v1.1 + v1.2 + v1.3`  
**Code-bearing verification:** `development@708d12cd66bc209111b37feeb45b8df4691bd7b0`, GitHub Actions run `33849375998` — **SUCCESS**  
**Status:** **CURRENT IMP-07 FINE-GRAINED GRAPH — P2B closure effective when this cycle-closing head passes full verification**

This record is implementation/dependency navigation only. It creates no semantic or architectural authority.

---

## 1. Current result

```text
G0  IMP-06 Projection / Exposure / Surface / API spine        CONFORMING_COMPLETE
P1  Publication lifecycle/currentness domain foundation       CONFORMING_COMPLETE
P2A durable revision/currentness persistence foundation       CONFORMING_COMPLETE
P2B reconstructible material revisions + Publication History  CONFORMING_COMPLETE AFTER CYCLE-CLOSING CI
P3  application mutation + transaction + retry idempotency    READY AFTER THE SAME CYCLE-CLOSING CI
```

P2B conformance evidence:

`docs/development/imp-07-publication-p2b-v13-conformance-2026-09-04.md`

---

## 2. Closure basis

The approved MS-PROT-046 v1.3 recovery contract is now executable and verified:

```text
fixed publication / opportunity@1 material                         PASS
nonblank exactly-one title                                         PASS
true absence for optional provider/source/deadline                 PASS
role-qualified absolute external links with multiplicity           PASS
CALENDAR_DATE(LocalDate + ZoneId)                                  PASS
EXACT_INSTANT(Instant)                                              PASS
revision-affined publishFrom/publishUntil                          PASS
exact typed historical reconstruction                              PASS
arbitrary domain schema/field authority rejected at boundary       PASS
DRAFT establishment produces no Publication History                PASS
ordered PUBLISH/WITHDRAW/REPUBLISH history                         PASS
withdrawal retains exact previously published revision             PASS
stale candidate material rolls back                                PASS
concurrent same-expected-current exactly one material winner       PASS
Merchant Scope isolation                                           PASS
P3 retry-idempotency semantics not introduced                      PASS
```

Code-bearing full verification:

```text
head    708d12cd66bc209111b37feeb45b8df4691bd7b0
run     33849375998
result  SUCCESS
```

No closure finding requires another semantic/design amendment.

---

## 3. Fine-grained IMP-07 graph

```text
Prerequisite spine
    G0 IMP-06 Projection / Exposure / Surface / API spine       CONFORMING_COMPLETE

Publication authoritative state
    P1 lifecycle/currentness domain foundation                  CONFORMING_COMPLETE
    P2A durable revision/currentness persistence foundation      CONFORMING_COMPLETE
    P2B reconstructible material revisions + Publication History CONFORMING_COMPLETE AFTER CYCLE-CLOSING CI
    P3 application mutation + transaction + retry idempotency    READY AFTER CYCLE-CLOSING CI

Publication public observation
    P4 Publication PUBLIC Exposure contracts/evaluation          BLOCKED_DEPENDENCY — P3 + G0
    P5 request-scoped public Opportunity representation          BLOCKED_DEPENDENCY — P4 + G0
    P6 Opportunity actionability derivation                      BLOCKED_DEPENDENCY — authoritative Opportunity facts

Public interaction participation
    I1 owner-qualified Opportunity → enquiry/send-enquiry source BLOCKED_DEPENDENCY — P4/P5 + G0
    I2 concrete Public Interaction Binding proof                 BLOCKED_DEPENDENCY — I1

Enquiry authoritative state
    E1 durable Enquiry submission/provenance foundation          BLOCKED_DEPENDENCY — I2
    E2 Enquiry logical retry idempotency + transaction proof     BLOCKED_DEPENDENCY — E1
    E3 stale subject-binding authoritative revalidation          BLOCKED_DEPENDENCY — I2 + E1

Merchant observation
    M1 MERCHANT Enquiry Exposure contracts                       BLOCKED_DEPENDENCY — E1 + G0
    M2 request-scoped merchant Enquiry representation            BLOCKED_DEPENDENCY — M1

Concrete transport / vertical proof
    T1 concrete PUBLIC Publication query adapter                 ON_DEMAND — P5
    T2 concrete PUBLIC Enquiry submission adapter                ON_DEMAND — E2/E3
    T3 concrete MERCHANT Enquiry query adapter                   ON_DEMAND — M2
    V1 full Publication → Enquiry integration path               BLOCKED_DEPENDENCY — downstream owners
```

---

## 4. Next READY boundary — P3

After the cycle-closing head containing this graph, the P2B conformance evidence, synchronised `implementation-status.md` and updated programme-gate test passes the repository full verification gate, P3 becomes the smallest dependency-complete READY node.

P3 responsibility remains exactly:

```text
Publication application mutation orchestration
+
required atomic transaction composition
+
MS-PROT-059 logical-operation retry idempotency
```

Before production code, exact P3 contracts MUST be re-derived from the accepted Publication, application, transaction and idempotency authorities through `IMPLEMENTATION-RULES.md`.

P3 MUST NOT silently absorb Exposure, public representation, Opportunity → Enquiry participation, Enquiry persistence or transport.

---

## 5. Historical recovery trace

History remains explicit:

```text
pre-v1.3 P2B RED          cc7019749b93dba8deac71aba34da9953f3b24ad
pre-v1.3 P2B GREEN        2b0aa60d34eca6fc36c8e2feb3f38f80f5f538c2
pre-v1.3 P2B cycle-close  ce05c106bb9f0e849493e69a274ab32b7c7d29f7
v1.3 authority            24b1d5ebeb1ad44badbeccb061fd0a2ed0a319f9
v1.3 RED recovery         c7c65d7247941182eda8df8ed2eb9831342d2263
v1.3 typed repair         a2743bfca51244c024f66018d8d2e52ec03ad06c
v1.3 verified repair      708d12cd66bc209111b37feeb45b8df4691bd7b0
```

The pre-v1.3 closure remains historical evidence but does not replace current v1.3 conformance.

---

## 6. Next governed action

```text
cycle-closing head containing:
    P2B v1.3 conformance evidence
    + this graph refresh
    + synchronised implementation-status.md
    + programme-gate assertion update
        ↓
repository full verification gate
        ↓ SUCCESS
P2B = CONFORMING_COMPLETE
        ↓
P3 = READY
        ↓
execute P3 through IMPLEMENTATION-RULES.md
```

Do not begin P3 before this cycle-closing CI succeeds. Do not begin P4/P5, Participation, Enquiry or concrete transport before their own graph dependencies become READY.
