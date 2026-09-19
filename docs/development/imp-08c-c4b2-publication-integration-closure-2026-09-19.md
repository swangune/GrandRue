# IMP-08C-C4B2 Merchant Account Event Publication Integration Closure — 19 September 2026

**Node:** `IMP-08C-C4B2 — Source/discovery/publication integration`  
**State:** `CONFORMING_COMPLETE`  
**Branch:** `development`  
**Verified source head:** `6737334174d139a550ec860d1ddf96ebe86931d3`  
**Closure reconciliation parent:** `e3761e83bf151fe37600fb03a264201afa53520a`  
**Parent consequence:** `IMP-08C-C4B = CONFORMING_COMPLETE`; `IMP-08C-C4` remains `PARTIALLY_CONFORMING`.

## Closure scope

This record closes the verification-pending C4B2 checkpoint from 15 September 2026. No new production semantics are introduced.

Accepted boundary:

- MS-PROT-026 v1.1 §§2, 10–12, 14, 16–17, 23 and 32 — occurrence/publication/reaction separation, recoverable publication responsibility, registered reaction discovery, stable reaction identity, independent acknowledgement, duplicate convergence and historical affinity;
- retained C4B1 Merchant Account occurrence mapping;
- retained C4C Event Reaction Contract registration;
- retained C4D1 durable per-reaction receipt storage; and
- `designs/IMPLEMENTATION-RULES.md` v2.1.

## Implemented progression

The already-checkpointed production path remains:

```text
committed Merchant Account publication responsibility
        ↓
exact historical MerchantAccountEstablished occurrence
        ↓
exact current registered Standing Free reaction discovery
        ↓
durable C4D1 MerchantEventReactionReceipt
        ↓
technical outbox published_at
```

The progression deliberately does not execute the Commercial consequence and does not acknowledge the Event Reaction. Those remain C4D responsibilities.

Unknown or legacy Event Contract affinity remains unresolved and pending. It is never reinterpreted through the current contract. An expanded current reaction set also fails closed until every newly registered source reaction has an explicit bounded logical-intent mapping.

## Verification reconciliation

The 15 September checkpoint deferred the full Maven/PostgreSQL gate. A later full repository gate executed after C4B2 was already present:

```text
GitHub Actions run      35465918366
Maven Tests run number  916
branch                  development
verified source head    6737334174d139a550ec860d1ddf96ebe86931d3
command                 mvn --batch-mode clean verify -Ppostgres-it

unit/governance tests   PASS — 1,271 / 0 failures / 0 errors / 0 skipped
PostgreSQL integration  PASS — 446 / 0 failures / 0 errors / 0 skipped
result                  BUILD SUCCESS
```

The C4B2 implementation and executable proof blobs at that verified source head are byte-identical to current `development` at closure reconciliation:

- `MerchantAccountEstablishedPublicationWorker.java` — `6335688da21a4d80c4a6449366d41e7f9784a10f`;
- `MerchantAccountEstablishedPublicationSource.java` — `5d80063cecabce5275b0ff359a38e472f2a0f235`;
- `JooqMerchantAccountEstablishmentPublicationOutbox.java` — `400423f10955c8b73fbd91082ff9b7a6ead2adbe`;
- `StandingFreeEventReactionContract.java` — `275fc4bf90f9f41bc358686379b146293a0f80cf`;
- `MerchantAccountEstablishedPublicationWorkerTest.java` — `b8fc0414c5fd25b32c8be4bb89dfa229d92ae430`; and
- `JooqMerchantAccountEstablishmentPublicationOutboxIT.java` — `82b7d4af016a110a9f6d25ca21ee4059af07271b`.

Therefore the later full gate verifies the exact C4B2 code and tests now being closed; rerunning unchanged work is not required for this state transition.

## Falsification retained

The existing C4B2 tests prove that:

- missing/unknown historical affinity does not create a reaction or technical publication;
- missing current reaction registration fails closed;
- an unsupported additional source reaction fails closed rather than being silently omitted;
- reaction-receipt failure leaves publication pending;
- publication completion cannot manufacture consumer acknowledgement; and
- retry after a durable receipt but before `published_at` converges on the same responsibility.

## Non-claims

This closure does not:

- make `published_at` a consumer acknowledgement;
- execute or acknowledge the Standing Free consequence;
- create a generic event bus or listener framework;
- reinterpret legacy event rows;
- close C4E, C5A, C5, C4 or IMP-08C; or
- reopen migration or post-migration verification.

## Result

```text
IMP-08C-C4B2 = CONFORMING_COMPLETE
IMP-08C-C4B  = CONFORMING_COMPLETE
IMP-08C-C4   = PARTIALLY_CONFORMING
IMP-08C      = IN_PROGRESS
```
