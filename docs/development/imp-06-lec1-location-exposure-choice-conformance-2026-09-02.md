# IMP-06 LEC1 — Merchant Location Exposure Choice Conformance — 2 September 2026

**Programme target:** IMP-06 — Read, Exposure & Transport Spine  
**Node:** LEC1 — Merchant Location Exposure-choice persistence  
**Substantive authority:** `MS-PROT-051 v1.5 — Merchant Location Public Exposure Choice Revision & Persistence Amendment`  
**Execution authority:** `designs/IMPLEMENTATION-RULES.md`  
**Status:** `CONFORMING_COMPLETE`

This record is implementation evidence only. It creates no new semantic or architectural authority.

## 1. RED evidence

```text
RED commit: 9b95a4b1da1fa7311d4263fe52e020b5df3f9711
```

The PostgreSQL integration contract intentionally referenced the approved but not-yet-implemented Location Exposure-choice authority and failed at test compilation. That established a genuine RED state before production implementation.

## 2. GREEN implementation

```text
GREEN code-bearing commit: 1b62d7eb4cc67b3a6e036fc7c783e342cd9085cc
commit message: feat: implement merchant location exposure choice authority
```

The implementation adds:

- `MerchantLocationExposure` with `PRIVATE_INTERNAL | PUBLIC`;
- explicit expected-current semantics `ABSENT | exact revision`;
- immutable `MerchantLocationExposureChoiceRevision`;
- `SetMerchantLocationExposureChoiceCommand`;
- `MerchantLocationExposureChoiceAuthority`;
- PostgreSQL/jOOQ authority `JooqMerchantLocationExposureChoiceAuthority`;
- Flyway migration `V52__merchant_profile__create_location_exposure_choice_revision_authority.sql`.

Existing `MerchantLocationRevision` production semantics were not changed.

## 3. Verified invariants

The implementation evidence establishes at least:

- Location may exist with no Exposure choice;
- absence is not persisted as `PRIVATE_INTERNAL`;
- first explicit choice establishes revision 1;
- choice-only mutation advances the choice revision without advancing Location revision;
- same-place Location correction retains the stable-Location choice;
- relocation to a new Location identity does not inherit the prior choice;
- stale expected choice revision fails closed with revision conflict;
- same logical request is idempotent while changed intent conflicts;
- retired Location rejects new choice mutation;
- lifecycle/choice serialization uses the established Merchant/Location lock discipline;
- concurrent first-choice establishment permits only one conforming winner.

## 4. Full verification evidence

```text
workflow:     Maven Tests
run number:   1401
run id:       33642983821
job id:       100290497551
head SHA:     1b62d7eb4cc67b3a6e036fc7c783e342cd9085cc
command:      mvn -B clean verify -Ppostgres-it
conclusion:   SUCCESS
```

The full PostgreSQL profile passed on the exact GREEN code-bearing commit.

## 5. Conformance conclusion

```text
LEC1 Merchant Location Exposure-choice persistence
    → CONFORMING_COMPLETE
```

This closes the executable Profile prerequisite introduced by the approved E4 implementation sequence. It does **not** complete E4.

The next smallest dependency-complete node is:

```text
E4-A — Exposure contract/member identity
    → READY for RED
```

Registry audience-variant indexing, Exposure-definition encoding, instance-aware result membership and the remaining E4 nodes remain downstream and must not be pulled forward before their immediate prerequisite conforms.
