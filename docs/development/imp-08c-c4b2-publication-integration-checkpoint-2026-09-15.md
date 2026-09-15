# IMP-08C-C4B2 — Merchant Account event publication integration checkpoint

**Date:** 15 September 2026  
**Node:** `IMP-08C-C4B2`  
**Programme state:** `READY` — no completion promotion in this checkpoint  
**Implementation status:** implemented, local full verification pending  
**Test-first specification commit:** `9f94b00b41a2856c92519d281e64f22db93ca2a6`  
**Implementation commit:** `058fb2ea6c055fac7f7ddfe2ac17157e3f4f1f12`

This file is implementation evidence/navigation only. It creates no semantic or design authority.

## Scope and governing boundary

C4B2 is the source/discovery/publication integration child of IMP-08C. Its current graph prerequisites — C4B1, C4C and C4D1 — are already conforming complete. C4B2 does not depend on C4D2B and therefore must not couple technical publication completion to the Standing Free business consequence or its acknowledgement.

Governing accepted constituents inspected for this checkpoint:

- `designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md`: §2 — Canonical Separation; §10 — Publication Failure After Commit; §11 — Event Publication Is Not Reaction Completion; §12 — Publication Responsibility; §14 — Registration Is Required; §16 — Event Reaction Identity; §17 — Per-Reaction Acknowledgement; §23 — Reaction Deduplication; §32 — Historical Affinity.
- `designs/MS-PROT-032 —Backend Module, Bounded Context & Dependency Architecture.md`: §3 — Core backend layers; §7 — Dependency direction; §8 — Capability-to-capability dependencies; §11 — Application orchestration; §14 — Internal events.
- Current C4B1, C4C and C4D1 implementation evidence, plus C4D2B only to verify that C4B2 does not absorb its trusted execution/owner-mutation responsibility.

## Test-first specification

Commit `9f94b00b41a2856c92519d281e64f22db93ca2a6` introduced the C4B2 specification before production support. The tests require:

1. an exact owner-registered `MerchantAccountEstablishedOccurrence` to become one durable C4D1 reaction responsibility before technical publication is recorded;
2. unknown/legacy historical affinity to remain pending and undiscovered rather than use current meaning;
3. absent current reaction registration to fail closed;
4. an expanded source-reaction set to fail closed until its downstream logical-intent mapping has an explicit bounded implementation;
5. reaction-receipt failure to leave the publication responsibility pending;
6. a concurrent technical publication completion to remain distinct from consumer acknowledgement;
7. PostgreSQL restart recovery when the durable reaction receipt commits but `published_at` does not; and
8. no Standing Free baseline or reaction acknowledgement to be manufactured by C4B2.

The tests were deliberately not executed remotely because the active instruction defers Maven verification to a later local run.

## Implementation

Implementation commit `058fb2ea6c055fac7f7ddfe2ac17157e3f4f1f12` adds the smallest bounded integration:

- `MerchantAccountEstablishedPublicationSource` is the Merchant Account owner port for pending durable publication responsibility. Its `PendingPublication` retains an optional owner-registered occurrence; empty means historical affinity is unresolved and must stay unpublished.
- `JooqMerchantAccountEstablishmentPublicationOutbox` implements that owner port using the existing durable outbox. It delegates exact occurrence reconstruction to C4B1's `registeredOccurrence(...)`; no affinity is inferred or backfilled.
- `MerchantAccountEstablishedPublicationWorker` is a narrow application coordinator. For each resolvable pending occurrence it obtains the current reaction registry, requires the currently supported exact Standing Free source-reaction set, accepts the C4D1 `MerchantEventReactionReceipt`, and only then records technical publication.
- The worker never establishes the scheduled execution principal, invokes the Commercial owner operation, or calls reaction acknowledgement. Those remain C4D2B responsibilities.
- `StandingFreeEventReactionContract.downstreamLogicalIntentReference(...)` centralises the stable logical-intent encoding already used by C4D2B, so C4B2 discovery and later trusted reaction execution cannot disagree on the same durable responsibility.

### Recovery ordering

The chosen progression is:

```text
committed Merchant Account publication responsibility
    -> exact historical occurrence reconstruction
    -> current bounded reaction discovery
    -> durable C4D1 reaction receipt
    -> technical outbox published_at
```

If execution stops after the durable receipt but before `published_at`, restart replays the pending owner publication responsibility. C4D1 duplicate convergence returns the same receipt; the retry can then record technical publication. No consumer acknowledgement or Commercial mutation is implied by either record.

## Falsification / non-claims

This checkpoint does **not** claim:

- C4B2 `CONFORMING_COMPLETE` before the full local verification gate;
- C4D2B execution or acknowledgement as part of publication;
- a generic event bus, listener framework or arbitrary reaction dispatcher;
- support for future reaction contracts whose downstream logical-intent mapping has not been implemented;
- liveness/scheduler cadence, C5 operational reconciliation, C4E correction, C3 completion or IMP-08C macro completion;
- reinterpretation of legacy rows with missing/unknown Event Contract affinity.

The fail-closed expanded-registry check is intentionally bounded implementation safety. It prevents a future registered reaction from being silently omitted while this worker only knows how to construct the Standing Free responsibility. Future broader discovery requires explicit accepted mapping/owner contracts rather than inference.

## Verification pending

No Maven or GitHub Actions run is claimed for this checkpoint. The required local closing command remains:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

Until that gate and applicable conformance/static/consistency checks pass, `IMP-08C-C4B2` remains `READY`, the parent C4B/C4 states remain unchanged, and this file is a verification-pending checkpoint only.
