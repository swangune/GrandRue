# IMP-07 P2A Publication Revision / Currentness Persistence Conformance

**Date:** 4 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-07 — Publication → Enquiry vertical slice  
**Implementation node:** IMP-07-P2A — Durable Opportunity revision/currentness persistence foundation  
**Evidence basis:** `development@0a42e52f056b71e6be1165d64f59b3c094149984`  
**Pre-closure verification:** GitHub Actions `Maven Tests` run `33840984639` — **SUCCESS**  
**Scoped result:** **CONFORMING_COMPLETE when the cycle-closing head containing this record passes the repository full verification gate**

This record is implementation evidence only. It creates no semantic, architectural or programme authority.

---

## 1. Governing boundary

P2A is governed by the current accepted Publication authority, MS-IMP-001, IMPLEMENTATION-RULES.md and the current IMP-07 fine-grained graph.

Its allowed persistence scope is limited to:

```text
merchant-scoped Opportunity identity
current revision identity
immutable previously recorded revision identities
durable lifecycle value
durable last-published revision identity where required
expected-current compare-and-set for same-Opportunity persistence
strict Merchant Scope isolation
```

P2A does not own complete material revision representation, complete lifecycle-transition history, logical-operation retry idempotency, Exposure, Opportunity actionability, participation, Enquiry or concrete transport.

---

## 2. Implementation evidence

Production implementation:

```text
src/main/java/mainstreet/publication/OpportunityPublicationStateAuthority.java
src/main/java/mainstreet/publication/PublicationStateConflictException.java
src/main/java/mainstreet/infrastructure/persistence/publication/JooqOpportunityPublicationStateAuthority.java
src/main/resources/db/migration/V53__publication__create_opportunity_revision_currentness.sql
```

Primary integration/concurrency proof:

```text
src/test/java/mainstreet/infrastructure/persistence/publication/JooqOpportunityPublicationStateAuthorityIT.java
```

The implementation preserves the accepted ownership direction:

```text
Publication domain port
        ↓ implemented by
infrastructure persistence adapter
        ↓ stores
PostgreSQL Publication currentness / revision-identity evidence
```

No Surface, Exposure, Enquiry, Provider or generic payload authority is introduced into this persistence boundary.

---

## 3. Required P2A proof matrix

| # | Required proof | Executable evidence | Result |
|---|---|---|---|
| 1 | New draft establishes one durable merchant-scoped Opportunity/current revision record | `draft_establishment_is_durable_and_isolated_by_merchant_scope` | **PASS** |
| 2 | Second material revision records a distinct immutable revision identity and advances only from expected current revision | `revision_advancement_preserves_prior_revision_identity_and_rejects_stale_currentness` | **PASS** |
| 3 | Stale expected-current advancement fails atomically | same stale-currentness test plus authoritative-current assertion | **PASS** |
| 4 | Two concurrent writers expecting the same current revision cannot both advance | `concurrent_advances_from_one_expected_revision_allow_exactly_one_winner` | **PASS** |
| 5 | Lifecycle and published-revision evidence round-trip without collapsing current revision | `lifecycle_and_last_published_revision_round_trip_without_collapsing_current_revision` | **PASS** |
| 6 | Another Merchant Scope cannot read or mutate the same Opportunity identity | `another_merchant_cannot_read_or_advance_the_same_opportunity_identity` | **PASS** |
| 7 | Already-recorded revision identity evidence cannot be reused as though it were a new revision | `previously_recorded_revision_identity_cannot_be_reused_as_a_new_revision` | **PASS** |

Requirement 7 additionally proves transaction rollback: the attempted reuse of `R1` after `R1 → R2` is rejected and the authoritative current state remains `R2` with exactly two recorded revision identities.

---

## 4. Persistence / transaction review

The PostgreSQL layout preserves the P2A distinctions structurally:

```text
opportunity_publication_revision_identity
    primary key = merchant + Opportunity + revision

opportunity_publication_current
    primary key = merchant + Opportunity
    current revision FK → recorded revision identity
    published revision FK → recorded revision identity
    lifecycle check = DRAFT | PUBLISHED | WITHDRAWN
    lifecycle/published-evidence compatibility check
```

The current/published revision foreign keys are deferrable so one transaction may advance the current row and then record a genuinely new revision identity before commit.

`compareAndSet(expectedState, nextState)` fences on the complete authoritative current state used by P2A:

```text
merchant identity
Opportunity identity
current revision identity
lifecycle
published revision identity
```

A current-revision mismatch raises `PublicationRevisionConflictException`; a same-revision lifecycle/published-evidence mismatch raises `PublicationStateConflictException`.

When a new revision identity is required, current-state mutation and revision-identity insertion execute inside one Spring transaction. A duplicate previously recorded revision identity causes the operation to fail and the current-state update to roll back, as directly proven by requirement 7.

---

## 5. Concurrency and isolation falsification

The implementation has been falsified against the material P2A failure modes:

```text
stale writer after another revision advance                 REJECTED
same-revision stale lifecycle writer                        REJECTED
two concurrent writers from one expected current revision   EXACTLY ONE WINNER
cross-Merchant read through same Opportunity identity        REJECTED / EMPTY
cross-Merchant mutation through same Opportunity identity    REJECTED
reuse of already-recorded revision identity                  REJECTED + TRANSACTION ROLLBACK
current revision diverging from last published revision      ROUND-TRIPS WITHOUT COLLAPSE
```

No evidence supports last-write-wins semantics inside this P2A authority.

---

## 6. Recovery / verification trace

The implementation history contains two red verification events that must remain explicit rather than being rewritten away.

### 6.1 Historical P1 cycle-closing false negative

```text
commit  a41934c407eb67da17be8fcedc61071a2e3dc0bc
run     33827472011
result  FAILURE
```

That cycle-closing run failed because `ImplementationProgrammeGateConformanceTest` still asserted the previous IMP-06/P1 navigation wording after the status had legitimately advanced. The failure was a stale governance-harness assertion, not a Publication-domain semantic failure. The historical run remains red and is not retroactively represented as green.

### 6.2 P2A fixture failure and correction

The first complete P2A full-state-CAS verification exposed a PostgreSQL test-fixture defect: `JooqOpportunityPublicationStateAuthorityIT.setUp()` truncated the referenced revision-identity table in a separate `TRUNCATE` statement while `opportunity_publication_current` retained foreign keys to it.

The correction changed only test setup, truncating both P2A tables in one PostgreSQL statement; no schema or production semantics changed.

```text
commit  7472a3a38a407e2fd85b51553ab250a0482c09da
run     33840713030
result  SUCCESS
```

The direct requirement-7 regression proof was then added:

```text
commit  0a42e52f056b71e6be1165d64f59b3c094149984
run     33840984639
result  SUCCESS
```

This is the pre-closure executable evidence basis for P2A.

---

## 7. Structure / paradigm conformance review

Result: **CONFORMING** within P2A scope.

- Domain semantics remain in `mainstreet.publication`; infrastructure implements the domain-owned persistence port.
- PostgreSQL mechanics remain an infrastructure detail.
- Merchant Scope is part of every durable identity/currentness key and query condition.
- Persistence does not infer or own lifecycle transitions; it stores already-valid Publication state.
- No generic JSON/payload representation is introduced to simulate unresolved Opportunity material content.
- Expected-current concurrency is not mislabeled as logical-operation retry idempotency.
- No P2B, P3, Exposure, participation, Enquiry or transport responsibility has leaked into P2A.
- The transaction boundary is narrow: one authoritative current-state CAS plus required revision-identity evidence.

No implementation-discovered material semantic or architectural gap remains open for P2A.

---

## 8. Explicit non-claims

P2A does **not** prove or claim:

```text
complete material Opportunity revision representation
historical material-representation reconstruction
complete lifecycle transition history
logical-operation retry identity or idempotency
Publication application-command orchestration
Publication Exposure contracts/evaluation
request-scoped public Opportunity representation
Opportunity actionability
Opportunity → Enquiry participation
Enquiry persistence/provenance/idempotency
merchant Enquiry observation
concrete public or merchant transport
end-to-end IMP-07 completion
```

These remain downstream according to the refreshed dependency graph.

---

## 9. Conformance conclusion

```text
IMP-07-P1  Opportunity Publication lifecycle/currentness domain foundation
    CONFORMING_COMPLETE

IMP-07-P2A Durable Opportunity revision/currentness persistence foundation
    CONFORMING_COMPLETE
    effective when the cycle-closing head containing this evidence passes full verification

IMP-07 macro target
    IN_PROGRESS
```

After cycle-closing verification succeeds, P2B becomes the next dependency-complete node. No P3, Exposure, participation, Enquiry or concrete transport work becomes READY merely because P2A closes.
