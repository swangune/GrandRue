# IMP-08C-C3 Catalogue Rollout Ordering Closure — 19 September 2026

**Node:** `IMP-08C-C3 — Execution failures/outcome ledger`  
**State:** `CONFORMING_COMPLETE`  
**Branch:** `development`  
**Verified source head:** `6737334174d139a550ec860d1ddf96ebe86931d3`  
**Closure claim:** C3 only; IMP-08C remains IN_PROGRESS.

## Closure scope

This record closes the remaining C3 catalogue responsibility after the earlier recovery/attempt, immutable-history, manifest-structure, durable-catalogue, approved initial-manifest and trusted-admission checkpoints.

Accepted authority:

- MS-PROT-056 v1.9 §§7–13 — trusted publication, P0, historical selection, Standing Free coverage, rollout ordering, recovery and fail-closed outcomes;
- MS-PROT-056 v1.10 — exact approved `standard-commercial-catalogue@1` and immutable 13/34/36 plan snapshots;
- MS-PROT-071 §§6, 17 and hard invariants — Merchant Account existence remains commercially independent and Commercial state is not forced into the establishment transaction;
- MS-PROT-063 §§12, 19–20 — platform/system attribution is not universal authority;
- `designs/IMPLEMENTATION-RULES.md` v2.1.

## Tests-first rollout evidence

RED was established by GitHub Actions run `35465630492` / Maven Tests #914.

Test compilation failed only because the bounded rollout-production types did not yet exist:

- `OrdinaryMerchantAccountPathReadiness`;
- `InitialStandardCommercialCatalogueRollout`; and
- `OrdinaryMerchantAccountEstablishmentApplicationService`.

The minimum production implementation committed at `8b8d487510636bfec3aaf102f97931bd5649e577`.

## Implemented composition

### Commercial-owned rollout

`InitialStandardCommercialCatalogueRollout`:

- publishes only `InitialStandardCommercialCatalogue.manifest()`;
- supplies explicit `NO_PREDECESSOR`;
- delegates publication to `CommercialCatalogueStore`, so trusted admission, current platform publication authority, atomic persistence and authoritative publication time remain owner-controlled;
- does not auto-publish at application startup;
- exposes a fail-closed readiness check using exact retained generation evidence;
- requires the retained generation to have no predecessor and exact v1.10 manifest equality;
- reports missing initial publication as `CatalogueResolutionException.NOT_ESTABLISHED`;
- reports malformed/non-approved first-generation evidence as `INTEGRITY_FAILURE`.

### Merchant Account boundary preservation

`OrdinaryMerchantAccountEstablishmentApplicationService` checks the rollout-only
`OrdinaryMerchantAccountPathReadiness` before delegating to the existing
`MerchantAccountEstablisher`.

The Merchant Account owner operation and `JooqMerchantAccountBootstrapStore` are unchanged.

Therefore:

```text
catalogue rollout readiness
        ↓ external application/delivery sequencing

MerchantAccountEstablisher
        ↓ unchanged owner semantics

Merchant Account transaction
```

No Commercial row, catalogue lookup or publication effect is inserted into the Merchant Account transaction.

The repository currently exposes no production Merchant Account HTTP/API route. The application wrapper is the required sequencing boundary for any ordinary delivery path introduced later; production deployment/credential/operator wiring remains IMP-18 scope and may not invent a universal platform superuser.

## Verification

Initial GREEN full gate from implementation head `8b8d487510636bfec3aaf102f97931bd5649e577`:

```text
GitHub Actions run      35465718498
Maven Tests run number  915
command                 mvn --batch-mode clean verify -Ppostgres-it

unit/governance tests   PASS — 1,270 / 0 failures / 0 errors / 0 skipped
PostgreSQL integration  PASS — 445 / 0 failures / 0 errors / 0 skipped
result                  BUILD SUCCESS
```

Additional falsification was then added without changing production code:

- an exact-manifest publication carrying a predecessor is rejected as invalid initial-generation evidence;
- the real PostgreSQL `JooqCommercialCatalogueStore`, exact v1.10 admission and rollout component are composed end-to-end;
- readiness is `NOT_ESTABLISHED` before publication;
- trusted initial publication commits `standard-commercial-catalogue@1` with `NO_PREDECESSOR`;
- readiness becomes true only after that durable commit;
- store recreation retains the exact generation and readiness.

Final node-completion source gate from head `6737334174d139a550ec860d1ddf96ebe86931d3`:

```text
GitHub Actions run      35465918366
Maven Tests run number  916
command                 mvn --batch-mode clean verify -Ppostgres-it

unit/governance tests   PASS — 1,271 / 0 failures / 0 errors / 0 skipped
PostgreSQL integration  PASS — 446 / 0 failures / 0 errors / 0 skipped
result                  BUILD SUCCESS
```

## C3 accumulated proof

Together with the retained C3 evidence, the node now proves:

1. durable attempt/outcome evidence and recovery boundaries;
2. immutable catalogue history and exact-time selection;
3. manifest identity/binding/support completeness;
4. durable catalogue publication/history persistence;
5. exact v1.10 initial catalogue representation;
6. conditional supporting-commercial evidence;
7. trusted exact-manifest admission distinct from platform authorisation;
8. explicit initial publication with `NO_PREDECESSOR`;
9. rollout readiness before ordinary new-account delivery;
10. Merchant Account transaction/commercial-ownership separation; and
11. full unit/governance/PostgreSQL regression conformance.

## Non-claims

C3 closure does not:

- publish a catalogue in a live deployment;
- assign a production publishing principal or credential;
- create a universal SYSTEM/PLATFORM privilege;
- make Merchant Account existence commercially dependent;
- establish prices or quantitative allowances;
- close IMP-08C;
- satisfy C4B2, C4E, C5A or C5;
- activate provider work.

Those responsibilities remain with their accepted owners and later implementation nodes.

## Result

```text
IMP-08C-C3 = CONFORMING_COMPLETE
IMP-08C    = IN_PROGRESS
```
