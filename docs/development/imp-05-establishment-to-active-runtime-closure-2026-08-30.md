# IMP-05 Establishment-to-Active Runtime Closure Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro-node:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained nodes:** E — Entitlement + runtime eligibility integration; F — Establishment-to-active proof
**Date:** 30 August 2026
**Status:** **CONFORMING_COMPLETE**

---

## 1. Governing authority

This closure is governed by:

- `designs/MS-IMP-001.md`;
- `designs/IMPLEMENTATION-RULES.md`;
- the accepted Merchant Account, Merchant Profile, onboarding, Configuration,
  Semantic Registry, serving-deployment and Commercial authorities already
  implemented by the completed IMP-05 child nodes; and
- the accepted IMP-03 runtime applicability and exact executable-support
  admission boundary.

This record is implementation evidence only. It creates no semantic or
architectural authority.

---

## 2. Existing-code satisfaction decision

The E assessment found that the production authorities already compose the
required runtime boundary:

```text
active exact Configuration Release
        ↓
semantic operation applicability
        ↓
exact executable-support admission
        ↓
current Commercial entitlement
        ↓
capability-owned handler
```

Commercial entitlement remains separate from semantic applicability. An active
operation can remain part of the exact executable model while protected use is
denied. Establishing the accepted initial full-experience trial after the first
committed activation supplies current Commercial grant provenance without
recompiling or changing the active model.

No new production service, aggregate, caller flag, mutable latest-release alias
or duplicate runtime path was required. Under the MS-IMP-001 existing-code
satisfaction rule, the missing work was one complete executable composition
proof.

---

## 3. End-to-end PostgreSQL proof

`JooqMerchantEstablishmentToActiveRuntimeIT` proves one ordinary merchant path
through the real production authorities:

```text
Merchant Account establishment + initial Controller
        ↓
Merchant Public Descriptor definition
        ↓
versioned onboarding answer + exact final review
        ↓
immutable Initial Configuration Intent
        ↓
ordinary-release-affined first Configuration Revision
        ↓
deterministic compilation / exact RCP
        ↓
durable validation + impact review + current-Controller approval
        ↓
exact new-activity requirement set
        ↓
immutable serving generation + STABLE ordinary fence
        ↓
dual-purpose release admission + atomic activation
        ↓
exact active operation resolution
        ↓
executable-support admission
        ↓
Commercial denial before trial establishment
        ↓
first-activation-affined durable trial grant
        ↓
capability-owned handler fulfilment
```

The proof asserts that:

- onboarding-derived semantic seeds become the exact revision capabilities;
- compilation and all durable admission evidence retain exact release/package
  affinity;
- activation retains the exact serving generation identity;
- runtime resolution consumes the exact activated release/model;
- absence of current Commercial entitlement blocks protected execution without
  removing semantic applicability;
- the trial reaction is accepted only against the exact first committed
  activation; and
- after the durable Commercial grant exists, the same applicable operation
  reaches its capability-owned handler with the exact RCP model identity.

Existing focused tests retain the lower-level rejection, retry, concurrency,
historical-affinity and fail-closed evidence for each constituent authority. The
end-to-end test does not replace those deterministic tests.

---

## 4. RED → GREEN record

The new proof initially failed only because of test-fixture defects:

1. incorrect Java package/accessor references while composing existing APIs;
2. a handler fixture reporting an event identity different from the exact
   registered event; and
3. incomplete cleanup for owner-specific evidence tables whose stable request
   identities intentionally outlive Merchant Account foreign-key scope.

The fixture was corrected without changing production behavior or weakening an
assertion. The focused PostgreSQL run then passed:

```text
mvn --batch-mode -Ppostgres-it \
  -Dtest=NoFocusedUnitTest \
  -Dsurefire.failIfNoSpecifiedTests=false \
  -Dit.test=JooqMerchantEstablishmentToActiveRuntimeIT verify

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
Flyway: 51 migrations validated; schema current at V51
```

---

## 5. Canonical verification

The full repository gate ran from an isolated clean copy of committed
`development` plus only this scoped proof and its evidence/status changes. The
six unrelated user-owned worktree changes were not copied into the verification
directory.

```text
mvn --batch-mode clean verify -Ppostgres-it

production Java sources compiled         772
test Java sources compiled               251
unit / conformance tests                 674 PASS
PostgreSQL integration tests             298 PASS
total tests                              972 PASS
Flyway migrations                        51 validated / V51 current
failures / errors / skipped              0 / 0 / 0
result                                   BUILD SUCCESS
```

The first canonical attempt correctly exposed a stale implementation-programme
navigation assertion that still required IMP-05 to be partially conforming.
`implementation-status.md` and its conformance guard were updated to reflect
this approved closure and the resulting IMP-06 readiness. The focused guard
then passed and the complete canonical rerun passed.

---

## 6. Closure consequence

Fine-grained nodes E and F are conforming complete. Every required IMP-05 child
is now either independently implemented through RED → GREEN evidence or
satisfied through conforming existing implementation plus an executable
composition proof.

> **IMP-05 — Merchant Definition, Configuration & Activation: CONFORMING_COMPLETE.**

The next macro target is selected only after refreshing the MS-IMP-001 graph.
