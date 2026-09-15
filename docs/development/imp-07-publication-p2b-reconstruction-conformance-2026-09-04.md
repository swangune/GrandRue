# IMP-07 P2B Publication Material Revision / Lifecycle History Conformance

**Date:** 4 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-07 — Publication → Enquiry vertical slice  
**Implementation node:** IMP-07-P2B — Reconstructible material revisions + lifecycle history  
**Evidence basis:** `development@2b0aa60d34eca6fc36c8e2feb3f38f80f5f538c2`  
**Verified implementation run:** GitHub Actions `Maven Tests` run `33846476217` — **SUCCESS**  
**Scoped result:** **CONFORMING_COMPLETE when the cycle-closing head containing this record passes the repository full verification gate**

This record is implementation evidence only. It creates no semantic, architectural or programme authority.

---

## 1. Governing boundary

P2B is derived from the surviving accepted composite Publication/schema authority:

- `MS-PROT-046 v1.1 + v1.2` — stable merchant-scoped Publication identity, immutable materially relevant revisions, exact revision reconstruction, lifecycle and publication-history preservation, expected-current concurrency;
- `MS-PROT-045 v1.1` — immutable schema identity/version, registered FieldDefinition identity, storage-name non-authority, capability-owned mutation semantics;
- the completed P2A durable currentness/revision-identity foundation; and
- `IMPLEMENTATION-RULES.md` v1.6.

P2B implements only:

```text
exact merchant-owned Opportunity revision affinity
+
exact immutable schema version affinity
+
registered field identity + persisted value representation
+
reconstruction by exact revision identity
+
ordered establish / publish / withdraw / republish evidence
+
atomic composition with P2A currentness CAS
```

P2B does not own application mutation orchestration, logical-operation retry idempotency, Exposure, Opportunity actionability, Public Interaction participation, Enquiry or transport.

---

## 2. Test-first trace

The executable P2B requirements were committed before production implementation:

```text
commit  cc7019749b93dba8deac71aba34da9953f3b24ad
message test(imp-07): require reconstructible publication revisions
run     33845980433
result  FAILURE
```

The RED run is expected historical evidence: the new P2B types, persistence API and migration did not yet exist.

Minimum production implementation then landed at:

```text
commit  2b0aa60d34eca6fc36c8e2feb3f38f80f5f538c2
message feat(imp-07): persist reconstructible publication history
run     33846476217
result  SUCCESS
```

The successful run executed the repository unit and PostgreSQL integration gate.

---

## 3. Production implementation

Domain/persistence contract:

```text
src/main/java/mainstreet/publication/OpportunityPublicationFieldValue.java
src/main/java/mainstreet/publication/OpportunityPublicationMaterialRevision.java
src/main/java/mainstreet/publication/OpportunityPublicationLifecycleHistoryEntry.java
src/main/java/mainstreet/publication/PublicationTransitionKind.java
src/main/java/mainstreet/publication/OpportunityPublicationStateAuthority.java
```

PostgreSQL/jOOQ implementation:

```text
src/main/java/mainstreet/infrastructure/persistence/publication/JooqOpportunityPublicationStateAuthority.java
src/main/resources/db/migration/V54__publication__create_revision_material_lifecycle_history.sql
```

Primary executable proof:

```text
src/test/java/mainstreet/infrastructure/persistence/publication/JooqOpportunityPublicationRevisionHistoryIT.java
src/test/java/mainstreet/infrastructure/persistence/publication/JooqOpportunityPublicationStateAuthorityIT.java
```

---

## 4. Material revision representation

P2B deliberately rejects a generic JSON/metadata-map model.

Canonical persistence composition is:

```text
merchant + Opportunity + revision identity
        ↓
exact Publication-owned schema identifier/version
        ↓
registered field identifier
        ↓
persistence value representation
```

`OpportunityPublicationMaterialRevision` therefore carries:

```text
MerchantScope
Opportunity identity
revision identity
OwnedSchemaReference
ordered unique OpportunityPublicationFieldValue values
```

The schema reference is capability-local. In this Publication-owned type/table, the owning capability is therefore fixed by context rather than redundantly persisted as merchant data.

`canonicalValue` is explicitly a persistence representation. It does not create DataConcept meaning, field semantics, validation policy, Java-property identity, JSON-path identity or SQL-column identity. Interpretation remains governed by the exact immutable registered schema version and field identifiers.

The representation permits schema evolution without rewriting historical revisions:

```text
R1 → opportunity@1 + fields under schema v1
R2 → opportunity@2 + fields under schema v2
```

Both remain reconstructible under their original semantic affinity.

---

## 5. Lifecycle-history representation

P2B records only authoritative Publication lifecycle operations:

```text
ESTABLISH
PUBLISH
WITHDRAW
REPUBLISH
```

Material revision-only advances create no lifecycle event.

A second `PUBLISH` is nevertheless recorded when a PUBLISHED Opportunity has been materially revised and the merchant explicitly publishes the newer current revision:

```text
PUBLISHED current=R1 published=R1
        ↓ revise
PUBLISHED current=R2 published=R1
        ↓ publish R2
PUBLISHED current=R2 published=R2
```

That second publish is materially different even though the lifecycle enum remains `PUBLISHED`.

`PublicationTransitionKind.between(...)` keeps transition classification in the Publication domain rather than making the jOOQ adapter infer business meaning. It also rejects a single persistence change that attempts to combine material revision advancement with lifecycle/published-revision mutation.

History preserves, in order:

```text
transition sequence
transition kind
previous lifecycle where applicable
resulting lifecycle
current revision identity
published revision identity where applicable
```

No clock/timestamp semantics are invented by P2B.

---

## 6. P2B proof matrix

| # | Required proof | Executable evidence | Result |
|---|---|---|---|
| 1 | A materially superseded revision remains reconstructible by exact identity | `material_revisions_remain_reconstructible_after_later_edits` | **PASS** |
| 2 | Historical reconstruction preserves exact schema version and registered field identities | `historical_revision_preserves_exact_schema_version_and_field_identity` | **PASS** |
| 3 | Publish, publish-new-revision, withdraw and republish evidence remain ordered and exact | `lifecycle_history_preserves_publish_withdraw_republish_and_new_revision_publish` | **PASS** |
| 4 | Stale expected-current failure leaves no orphan material revision | `stale_revision_failure_rolls_back_candidate_material` | **PASS** |
| 5 | Material revisions and lifecycle history remain Merchant Scope isolated | `revision_material_and_lifecycle_history_remain_merchant_scoped` | **PASS** |
| 6 | Two concurrent revisions from one expected state persist exactly one winning new material revision | `concurrent_revisions_from_one_expected_state_persist_exactly_one_new_material_revision` | **PASS** |
| 7 | New material is required exactly when logical revision identity changes and must match exact affinity | `persistence_requires_new_material_exactly_when_revision_identity_changes` | **PASS** |

P2A CAS/currentness tests were retained and adapted to the stronger port, rather than preserving a legacy material-less write API.

---

## 7. Transaction / rollback analysis

For revision advancement, the adapter executes in one Spring transaction:

```text
full-state expected-current CAS
        ↓ success only
record new immutable revision identity
        ↓
record exact material revision + field rows
        ↓
record lifecycle history only if a lifecycle/publication operation occurred
        ↓
commit
```

Because V53 current-revision foreign keys are deferrable, currentness may advance before the new revision identity is inserted inside the same transaction; commit cannot expose a broken revision reference.

If revision identity reuse, material persistence or another transaction step fails, the state update rolls back.

A stale writer fails at the CAS before candidate material/history is inserted. The integration suite directly proves that the stale candidate revision remains absent.

Same-Opportunity concurrent writers retain P2A exactly-one-winner semantics. Only the winning revision identity and material representation survive.

---

## 8. Persistence layout review

V54 adds:

```text
opportunity_publication_revision_material
    PK merchant + Opportunity + revision
    exact schema identifier/version
    FK → immutable revision identity

opportunity_publication_revision_field_value
    PK merchant + Opportunity + revision + field identifier
    persistence value representation
    FK → material revision

opportunity_publication_lifecycle_history
    PK merchant + Opportunity + transition sequence
    constrained transition kind/lifecycle shape
    FK current/published revision evidence → revision identity
```

The migration intentionally does not create a foreign key from the already-existing P2A currentness table to the new material table. Such a migration would require inventing historical material for any pre-V54 P2A rows. New P2B writes instead guarantee material/currentness atomicity through the persistence port and executable transaction tests.

No index/cache/search/read-model architecture is introduced by this node.

---

## 9. Falsification results

The implementation was reviewed against these failure modes:

```text
old revision overwritten by later edit                         REJECTED
schema v1 history interpreted as schema v2                     REJECTED
SQL/JSON/property name treated as semantic field identity      REJECTED
arbitrary Map<String,Object> Publication metadata authority    REJECTED
revision advance without material representation               REJECTED
lifecycle-only mutation carrying fake new material             REJECTED
material belonging to another merchant/revision                REJECTED
stale writer leaves orphan material                            REJECTED
concurrent losers leave material rows                          REJECTED
revision-only edit emits false lifecycle transition            REJECTED
publish newer revision while already PUBLISHED is lost         REJECTED
withdrawal loses exact last-published revision                 REJECTED
republish loses prior lifecycle evidence                       REJECTED
persistence adapter invents Opportunity field semantics        REJECTED
```

No falsification finding requires a design amendment.

---

## 10. Architecture/paradigm conformance

P2B uses the accepted composite architecture appropriately:

```text
Publication invariant / transition classification
    → Publication domain

immutable revision/currentness atomicity
    → transactional consistency

PostgreSQL/jOOQ
    → persistence adapter

semantic field meaning
    → registered capability-owned schema

historical reconstruction
    → ordinary typed immutable values + relational persistence
```

Not introduced:

```text
framework-owned domain semantics
generic runtime object patching
business-category branching
cross-capability direct mutation
post-commit event substitution for atomic persistence
Exposure semantics
Enquiry semantics
transport endpoint semantics
logical retry/idempotency machinery
```

Result: **CONFORMING within P2B scope**.

---

## 11. P3 boundary exposed by P2B

P2B makes no claim that callers are yet authorised application mutation paths.

P3 must derive and prove, before production code:

1. capability-owned Publication mutation operations/orchestration;
2. one atomic application transaction covering authoritative state + material/history persistence;
3. logical-operation retry idempotency under MS-PROT-059, distinct from expected-current CAS;
4. registered schema/FieldDefinition conformance before a material revision reaches persistence;
5. merchant/actor authorisation and accepted command ownership as applicable; and
6. no transport/Exposure/Enquiry expansion before their graph dependencies are READY.

The P2B persistence port is infrastructure for those operations; it is not itself application mutation authority.

---

## 12. Completion result

```text
IMP-07-P2B
    reconstructible material revisions                PASS
    immutable schema-version affinity                  PASS
    exact historical field identity                    PASS
    required lifecycle/publication history             PASS
    stale-writer atomic rollback                       PASS
    concurrent one-winner material persistence         PASS
    Merchant Scope isolation                           PASS
    P2A regression preservation                        PASS

No active design gate discovered.
No downstream dependency bypassed.
```

P2B becomes effectively `CONFORMING_COMPLETE` when the cycle-closing commit containing this record, the refreshed graph, synchronised implementation status and updated governance assertion passes the repository full verification gate.

Under `POST_COMMIT_CI_RESULT_DOES_NOT_REQUIRE_STATUS_REWRITE`, that successful external CI result satisfies the recorded condition directly; it does not require a status-only self-recording commit.
