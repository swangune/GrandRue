# IMP-06 T1b4 Bounded Query Conformance Record

> **Date:** 3 September 2026  
> **Branch:** `development`  
> **Node:** `T1b4 — first production bounded query definition/path`  
> **Result:** **CONFORMING_COMPLETE**  
> **Verified code-bearing baseline:** `development@cd5434eba2d81598c8320c95d5d31d06ae07dfe0`

This record closes T1b4 under accepted MS-IMP-001 sequencing and IMPLEMENTATION-RULES v1.6. It is implementation evidence only and does not create semantic, architectural, API or programme authority.

---

## 1. Governing authority and scope

T1b4 is governed principally by:

- `MS-PROT-035 v1.1 — Production API Contract Registration, Transport Outcome & Initial Surface Portfolio Amendment`, especially the registered query-contract responsibilities;
- composite `MS-PROT-027`, especially v1.13 exact bounded-read representation affinity, generic Surface isolation, smallest concrete query proof, adversarial verification and implementation ordering;
- `MS-PROT-027 v1.4 — Initial Production Projection Portfolio & Concrete Contract Registration Amendment`, which establishes `platform / merchant-presence` and read use `platform / public-merchant-presence`;
- completed S2 PUBLIC/CUSTOMER bounded assembly;
- completed T1a registration spine and T4a bounded-collection contract;
- `designs/MS-IMP-001.md`; and
- `designs/IMPLEMENTATION-RULES.md` v1.6.

The accepted T1b4 boundary is:

```text
exact bounded Projection read
    ↓
P2 serviceability affined to that read
    ↓
current E4 Exposure resolution
    ↓
BR6 same-read positive fragment selection
    ↓
S2 immutable PUBLIC/CUSTOMER assembly
    ↓
T1b4 registered bounded query definition/path
```

T1b4 does not acquire business values and does not create transport-specific response payloads. Its responsibility is to establish one concrete production query contract and preserve the already-safe S2 bounded representation as the exact downstream handoff.

---

## 2. Governing invariant

For the first production PUBLIC Merchant Presence query:

```text
ApiQueryContractDefinition
    identity: platform / public-merchant-presence
    surface: PUBLIC
    owner: platform / merchant-presence
        +
exact PublicCustomerProjectionAssembly A1
        ↓
PublicMerchantPresenceBoundedQuery
    - same query definition
    - same A1 object
    - same bounded-read binding retained by A1
```

The path must not:

```text
re-read owner repositories after E4
substitute a newer owner value
serialize arbitrary current owner entities
move business values into E4
interpret Profile/Booking/Inventory payloads in generic Surface
use Map<String,Object> or JsonNode as a generic value escape hatch
manufacture continuation identity
encode or validate continuation tokens
introduce provider/capability execution dependencies
perform HTTP transport mapping
```

`BoundedProjectionReadBinding` remains observation affinity. It is not a continuation identity, domain identity or API cursor.

---

## 3. Tests-first evidence

Tests-only RED commit:

- `0cf184b64e1c039e4b0787a957ba3715bd4ad9e6` — `test(imp-06): add T1b4 bounded query RED`.

GitHub Actions:

- run `33799007443` / job `100793539315` — **FAILED as intended**.

The RED was compile-time absence of the T1b4 production contract/path types. It did not weaken or contradict existing production behaviour.

---

## 4. Minimum production implementation

Production commits:

- `c1e82baa97bb42fa785bb9858a2c234065beb8f0` — `feat(imp-06): add T1b4 query contract definition`;
- `67bd05b2f2a4832522b5d256bb92bd7cc31c75f4` — `feat(imp-06): add T1b4 exact assembly handoff`;
- `690260a10c5e57e48418dc762d0065b8e9c9d1fc` — `feat(imp-06): complete T1b4 bounded query path`.

The implementation adds only:

- `ApiQueryContractDefinition` — immutable registered query responsibilities required by MS-PROT-035 v1.1;
- `PublicMerchantPresenceBoundedQuery` — immutable handoff containing the exact accepted query definition and exact S2 assembly; and
- `PublicMerchantPresenceQueryPath` — the first concrete `platform / public-merchant-presence` query path targeting `platform / merchant-presence`.

The static query definition records:

- PUBLIC surface;
- QUERY contract kind;
- owner-qualified projection identity;
- merchant-scope establishment rule;
- PUBLIC Audience Observation Context reference;
- no relationship requirement for this initial public read;
- Merchant Presence P2 serviceability reference;
- current Exposure contract reference;
- explicit no-client-filter/sort contract;
- single bounded-read bound rule;
- S2 assembly as the safe representation boundary; and
- Merchant Presence reduced/unserviceable representation reference.

No HTTP endpoint, owner repository query, serializer, provider adapter, persistence migration or continuation implementation was added.

---

## 5. Verification correction and GREEN evidence

The first implementation-head run exposed a non-semantic test compilation mistake:

- code head: `690260a10c5e57e48418dc762d0065b8e9c9d1fc`;
- run `33799164011` / job `100794050588` — **FAILED**;
- production compilation succeeded; test compilation failed because the new test referred to the existing S2 accessor as `binding()` instead of `boundedProjectionReadBinding()`.

The assertion intent was unchanged. The accessor was corrected by:

- `aa90c611189d372d0d523fa47d4e799f8f747d16` — `test(imp-06): correct T1b4 S2 binding accessor`.

Corrected full verification:

- run `33799343313` / job `100794628506` — **SUCCESS**.

No production semantic change was needed to obtain the corrected GREEN.

---

## 6. Adversarial and architecture review

Adversarial hardening commit:

- `cd5434eba2d81598c8320c95d5d31d06ae07dfe0` — `test(imp-06): harden T1b4 bounded query invariants`.

The hardened T1b4 tests prove:

1. the concrete query uses the accepted `platform / public-merchant-presence` identity;
2. the concrete owner is exactly `platform / merchant-presence`;
3. the contract remains PUBLIC and QUERY;
4. scope, audience, P2, Exposure, filter/sort, bound, safe-response and unavailable-response references cannot silently drift in the initial path;
5. the exact S2 assembly object is preserved without remapping or replacement;
6. the exact bounded-read binding retained by S2 is therefore preserved through the T1b4 handoff;
7. non-QUERY registration is rejected;
8. query-identity laundering and projection-owner laundering are rejected;
9. Exposure-contract membership is defensively copied and immutable; and
10. the query path/handoff expose no repository, provider, capability, continuation, `JsonNode`, generic `Map` or Merchant-Presence-business-payload dependency.

### Falsification findings

The principal v1.13 failure modes were actively tested/reviewed:

```text
post-E4 owner re-query
    → structurally unavailable from the T1b4 path

cross-query / wrong-owner laundering
    → constructor rejection

generic business-payload interpretation in Surface
    → no generic payload type or owner-specific value dependency

continuation identity smuggled into bounded-read affinity
    → no continuation dependency; deferred to T4b/T4c authority

transport DTO mistaken for S2 material
    → T1b4 carries exact S2 assembly; no transport mapping added
```

No implementation-discovered material design ambiguity remains for T1b4.

---

## 7. Final verification

Final code-bearing baseline:

`development@cd5434eba2d81598c8320c95d5d31d06ae07dfe0`

GitHub Actions:

- workflow: `Maven Tests`;
- run: `33799702740`;
- job/check: `100795878911`;
- command configured by workflow: `mvn --batch-mode clean verify -Ppostgres-it`;
- result: **SUCCESS**.

The hardened adversarial test set therefore passes together with the repository's unit/conformance and PostgreSQL integration verification. This record does not invent an aggregate test count that is not required for the closure decision.

---

## 8. Composite-architecture conformance

T1b4 uses the smallest fitting constituent pattern:

```text
immutable declarative query contract
    +
owner-qualified concrete query identity
    +
immutable exact-S2 handoff
```

It does not introduce a generic repository abstraction, distributed workflow, event choreography, generic payload bag, provider framework, controller or continuation protocol. Query contract metadata remains declarative; bounded read material remains owned by the projection/Surface chain; business-value interpretation remains outside generic Surface.

Result: **PASS**.

---

## 9. Explicit non-claims and deferred responsibilities

T1b4 closure does **not** claim completion of:

- a concrete HTTP/query adapter;
- concrete owner-private business-value-to-transport mapping;
- continuation-token affinity, validation, encoding or decoding;
- T2d concrete adapters generally;
- T3c concrete mappings generally;
- T4b continuation affinity/validation;
- T4c opaque continuation encoding/adapter;
- S3 Public Interaction Binding; or
- IMP-06 as a macro target.

The separation is deliberate:

```text
T1b4 bounded query definition/path
    ≠ T4b continuation affinity/validation
    ≠ T4c continuation encoding/adapter
    ≠ concrete HTTP transport adapter
```

---

## 10. Dependency consequence

T1b4 is **CONFORMING_COMPLETE**.

Its completion satisfies the declared dependency for:

```text
T4b — continuation affinity/validation
    → READY
```

No broader promotion follows automatically:

- `IMP-06` remains **PARTIALLY_CONFORMING**;
- `S3` remains **BLOCKED_DEPENDENCY** on authoritative capability-owned Public Interaction participation sources under `MS-WATCH-002`;
- `T4c` remains **BLOCKED_DEPENDENCY** on T4b plus a concrete query adapter; and
- `IMP-07` remains **BLOCKED_DEPENDENCY** on complete IMP-06.

The next implementation cycle may select T4b only after this evidence, the T1b4 graph refresh and `implementation-status.md` synchronisation are committed consistently and the resulting cycle-closing head verifies successfully.