# IMP-07 Graph Refresh — P2B MS-PROT-046 v1.3 Conformance Recovery

**Date:** 4 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-07 — Publication → Enquiry vertical slice  
**Authority basis:** accepted composite `MS-PROT-046 v1.1 + v1.2 + v1.3`  
**Formalisation verification:** `development@24b1d5ebeb1ad44badbeccb061fd0a2ed0a319f9`, GitHub Actions run `33847946375` — **SUCCESS**  
**Status:** **CURRENT IMP-07 FINE-GRAINED GRAPH — P2B RECOVERY REQUIRED**

This record is implementation/dependency navigation only. It creates no semantic or architectural authority.

---

## 1. Recovery trigger

Before MS-PROT-046 v1.3 was approved, P2B implementation/evidence advanced on `development` and the cycle-closing commit `ce05c106bb9f0e849493e69a274ab32b7c7d29f7` passed GitHub Actions run `33847078205`.

That historical green run is retained. It proves the pre-v1.3 implementation was internally executable; it does **not** prove conformance to authority that had not yet been approved/formalised.

MS-PROT-046 v1.3 now requires materially stronger P2B contracts, including:

```text
bounded typed publication / opportunity@1 material
role-qualified external links
typed Opportunity temporal boundaries
revision-affined publishFrom / publishUntil evidence
Publication History containing PUBLISH / WITHDRAW / REPUBLISH only
```

Current pre-v1.3 implementation instead exposes a generic `fieldIdentifier + canonicalValue` material API and records `ESTABLISH` as lifecycle/publication history.

Therefore the prior P2B closure classification is superseded for current navigation.

---

## 2. Current fine-grained state

```text
G0  IMP-06 Projection / Exposure / Surface / API spine        CONFORMING_COMPLETE

P1  Publication lifecycle/currentness domain foundation       CONFORMING_COMPLETE
P2A durable revision/currentness persistence foundation       CONFORMING_COMPLETE
P2B reconstructible material revisions + Publication History  PARTIALLY_CONFORMING — RECOVERY_REQUIRED
P3  application mutation + transaction + retry idempotency    BLOCKED_DEPENDENCY — P2B

P4  PUBLIC Exposure contracts/evaluation                      BLOCKED_DEPENDENCY — P3 + G0
P5  request-scoped public Opportunity representation          BLOCKED_DEPENDENCY — P4 + G0
P6  Opportunity actionability                                 BLOCKED_DEPENDENCY — authoritative Opportunity facts

I1  Opportunity → enquiry/send-enquiry participation source   BLOCKED_DEPENDENCY — P4/P5 + G0
I2  concrete Public Interaction Binding proof                 BLOCKED_DEPENDENCY — I1

E1  durable Enquiry submission/provenance                     BLOCKED_DEPENDENCY — I2
E2  Enquiry retry idempotency + transaction proof             BLOCKED_DEPENDENCY — E1
E3  stale subject-binding revalidation                        BLOCKED_DEPENDENCY — I2 + E1

M1  MERCHANT Enquiry Exposure contracts                       BLOCKED_DEPENDENCY — E1 + G0
M2  request-scoped merchant Enquiry representation            BLOCKED_DEPENDENCY — M1

T1/T2/T3 concrete adapters                                    ON_DEMAND — owning node first
V1  full Publication → Enquiry vertical proof                 BLOCKED_DEPENDENCY
```

P3 is **not READY** while P2B recovery is incomplete.

---

## 3. Current conformance delta

| MS-PROT-046 v1.3 requirement | Current pre-v1.3 implementation | Recovery state |
|---|---|---|
| fixed `publication / opportunity@1` material contract | caller supplies arbitrary schema reference/version | FAIL |
| bounded typed Opportunity v1 fields | generic `fieldIdentifier + canonicalValue` | FAIL |
| `title` exactly one and nonblank | no schema-level typed title contract | FAIL |
| role-qualified 0..N external links | no typed role/link representation | FAIL |
| `CALENDAR_DATE(LocalDate + ZoneId)` / `EXACT_INSTANT(Instant)` | arbitrary strings | FAIL |
| revision-affined `publishFrom/publishUntil` | absent from typed domain material | FAIL |
| exact reconstruction of typed presence/absence/material | generic string reconstruction only | PARTIAL |
| schema affinity immutable | schema id/version retained | PASS FOUNDATION |
| material revision identity/immutability | already durable/immutable | PASS FOUNDATION |
| Publication History = PUBLISH/WITHDRAW/REPUBLISH | includes ESTABLISH | FAIL |
| PUBLISHED→PUBLISHED publish retained | supported | PASS FOUNDATION |
| stale/concurrent atomicity and Merchant Scope isolation | supported | PASS FOUNDATION |
| logical retry idempotency remains outside P2B | remains outside | PASS |

---

## 4. Recovery strategy

The existing commits and V54 migration are historical implementation evidence and SHALL NOT be rewritten merely to manufacture a clean sequence.

Recovery proceeds forward through IMPLEMENTATION-RULES.md:

```text
accepted v1.3 authority + green formalisation gate
        ↓
write/update failing tests for exact v1.3 contracts
        ↓
expected RED against pre-v1.3 implementation
        ↓
minimum typed domain/persistence repair
        ↓
targeted + PostgreSQL integration verification
        ↓
architecture/corpus/static/full verification
        ↓
new v1.3 P2B conformance evidence
        ↓
new graph refresh + implementation-status sync
        ↓
cycle-closing commit/CI
        ↓ SUCCESS
P2B = CONFORMING_COMPLETE
        ↓
P3 = READY
```

Physical persistence encoding remains an implementation detail. Recovery MUST NOT expose arbitrary field/schema authority merely because historical V54 storage is structurally generic.

---

## 5. Test-first recovery contract

The RED specification SHALL require at least:

1. Opportunity material has fixed `opportunity@1` schema affinity.
2. `title` is nonblank and exactly one.
3. optional provider/source/deadline facts remain truly absent when absent.
4. external links preserve role + absolute URI + optional label and permit multiple same-role values.
5. calendar-date boundaries preserve `LocalDate + ZoneId`.
6. exact-instant boundaries preserve `Instant`.
7. `publishFrom/publishUntil` round-trip as revision-affined material.
8. reconstruction cannot accept arbitrary future schema versions/field identifiers.
9. DRAFT establishment creates material revision but no Publication History entry.
10. publish / publish-new-revision / withdraw / republish produce ordered Publication History with exact current/published revision affinity.
11. existing stale-write/concurrency/immutability/Merchant Scope guarantees remain green.

---

## 6. Historical evidence treatment

The following remain true historical records and are not rewritten:

```text
cc701974...  pre-v1.3 P2B RED specification
2b0aa60d...  pre-v1.3 P2B implementation green
ce05c106...  pre-v1.3 P2B cycle-closing green
```

Their previous `CONFORMING_COMPLETE` conclusion is not current authority navigation after v1.3. Current navigation is this recovery graph.

---

## 7. Next governed action

```text
commit exact v1.3 failing tests + recovery navigation
        ↓
verify expected RED
        ↓
implement minimum P2B v1.3 repair
```

Do not begin P3, Exposure, Participation, Enquiry or transport work before P2B returns to `CONFORMING_COMPLETE` under MS-PROT-046 v1.3.
