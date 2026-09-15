# IMP-07 P2B MS-PROT-046 v1.3 Conformance Evidence

**Date:** 4 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Implementation node:** IMP-07-P2B — Reconstructible material revisions + Publication History  
**Authority basis:** composite MS-PROT-045 v1.0 + v1.1 and composite MS-PROT-046 v1.1 + v1.2 + v1.3  
**Code-bearing verification head:** `development@708d12cd66bc209111b37feeb45b8df4691bd7b0`  
**Full verification:** GitHub Actions `Maven Tests` run `33849375998` — **SUCCESS**  
**Result:** **CONFORMING_COMPLETE effective when the cycle-closing head containing this evidence passes the repository full verification gate**

This file is implementation conformance evidence only. It does not create or amend semantic authority.

---

## 1. Test-first recovery trace

The v1.3 recovery preserved history rather than rewriting the earlier P2B sequence:

```text
accepted MS-PROT-046 v1.3 formalisation
    24b1d5ebeb1ad44badbeccb061fd0a2ed0a319f9
    run 33847946375 SUCCESS

v1.3 RED specification / recovery navigation
    c7c65d7247941182eda8df8ed2eb9831342d2263
    run 33848524739 FAILURE — expected RED

minimum typed production repair
    a2743bfca51244c024f66018d8d2e52ec03ad06c
    run 33849064089 FAILURE — stale pre-v1.3 PostgreSQL fixture did not compile

fixture + private transition-evidence correction
    708d12cd66bc209111b37feeb45b8df4691bd7b0
    run 33849375998 SUCCESS
```

The `a2743bf...` failure was not a semantic failure. Production compiled; the failure was test compilation because `JooqOpportunityPublicationStateAuthorityIT` still invoked the superseded generic `OpportunityPublicationMaterialRevision` constructor. The forward correction migrated that fixture to the accepted typed constructor. The same correction preserved `previous_lifecycle` as private V54 persistence evidence required by the existing database integrity constraint without exposing it as Publication History authority.

---

## 2. P2B v1.3 conformance proofs

| Required proof | Evidence | Result |
|---|---|---|
| fixed `publication / opportunity@1` schema affinity | `OpportunityPublicationMaterialRevision.schemaReference()` is fixed to `opportunity@1`; persistence rejects stored schema drift | PASS |
| title exactly one/nonblank | typed constructor requires nonblank `title` | PASS |
| optional provider/source/deadline absence preserved | typed `Optional` material and PostgreSQL reconstruction | PASS |
| role-qualified external links | `OpportunityExternalLinkRole` + absolute `URI` + optional label; list cardinality preserves 0..N | PASS |
| typed calendar-date boundary | `OpportunityCalendarDateBoundary(LocalDate, ZoneId)` and inclusive upper cutoff proof | PASS |
| typed exact-instant boundary | `OpportunityExactInstantBoundary(Instant)` | PASS |
| revision-affined `publishFrom/publishUntil` | typed material record and PostgreSQL round-trip | PASS |
| exact typed historical reconstruction | `JooqOpportunityPublicationRevisionHistoryIT.typed_material_revisions_round_trip_exact_presence_temporal_and_link_evidence` | PASS |
| arbitrary domain schema/field authority cannot escape | typed Publication boundary + fixed allowed v1 persistence field set | PASS |
| DRAFT establishment is not Publication History | explicit integration proof returns empty Publication History after establishment | PASS |
| ordered PUBLISH/WITHDRAW/REPUBLISH history | integration proof preserves exact operation sequence and current/published revision affinity | PASS |
| withdrawal retains previously published revision when current differs | dedicated integration proof | PASS |
| stale material candidate rolls back atomically | dedicated stale-revision integration proof | PASS |
| concurrent same-expected-current exactly one material winner | dedicated concurrency integration proof | PASS |
| Merchant Scope isolation | typed material/history integration proof plus retained P2A tests | PASS |
| logical retry idempotency remains outside P2B | no P3 logical-operation identity introduced | PASS |

---

## 3. Architectural / semantic boundary review

The implementation remains within the approved P2B boundary:

```text
Publication owns:
    Opportunity material revision truth
    exact historical reconstruction
    Publication History

Persistence owns only:
    durable physical representation
    transactional enforcement
    exact reconstruction

Not introduced by P2B:
    P3 logical-operation idempotency
    Exposure
    Opportunity actionability
    Public Interaction participation
    Enquiry
    transport
    Search / Notification / social delivery
    numeric retention policy
```

V54's historically generic field rows remain a private physical encoding. The production domain boundary no longer accepts caller-defined schema versions or arbitrary field identifiers as authoritative Opportunity material.

No new semantic/design amendment is required by this implementation evidence.

---

## 4. Verification conclusion

The code-bearing head `708d12cd66bc209111b37feeb45b8df4691bd7b0` passed the repository full verification gate through GitHub Actions run `33849375998`.

P2B may therefore be classified `CONFORMING_COMPLETE` once the cycle-closing commit containing this evidence, the graph refresh, synchronised implementation status and programme-gate assertions also passes the full repository gate.

P3 MUST remain blocked until that cycle-closing verification succeeds.
