# MS-PROTOTYPE-002 — Merchant-Choice Composition Evidence

**Date:** 2026-08-26  
**Status:** Implementation evidence — not semantic authority  
**Branch:** `development`  
**Governing authority:** MS-PROT-052 v1.1, MS-PROT-040, MS-PROT-042 v1.5, existing Configuration Release/activation authorities

## Objective

Prove that Main Street configures merchants from supported operational choices rather than from business category, and that one merchant can later change its active capability composition through the accepted Configuration Release lifecycle.

The prototype must establish:

1. two merchants described by the same trade can have materially different active capability graphs;
2. a merchant can replace one active Configuration Release with another without changing merchant identity;
3. a newly activated capability becomes executable through the existing generic runtime path;
4. onboarding discovery option identities remain evidence/mapping inputs rather than runtime capability identities;
5. unsupported or not-yet-assembled intent fails closed as a configuration gap;
6. read projections vary by merchant-scoped data rather than business-type branches.

## Accepted authority applied

MS-PROT-052 v1.1 establishes that baseline public presence is not a capability and that additional customer-interaction discovery uses stable conceptual options including:

```text
PUBLISH_INFORMATION
SEND_ENQUIRY
ARRANGE_APPOINTMENT
RESERVE_SUBJECT
PLACE_ORDER
SUBSCRIBE_UPDATES
NOTHING_ELSE_FOR_NOW
OTHER
```

The implementation preserves these boundaries:

```text
Discovery option
    != capability identity

Discovery mapping
    -> candidate semantic seed(s)
    != direct activation

Business category
    != configuration authority
```

No new semantic decision was introduced by this milestone.

## Implemented evidence

### Same trade, different capability graphs

`prototype-gardener-showcase`:

```text
Publication
Enquiry
```

No Appointment operation is active.

`prototype-gardener-bookable`:

```text
Publication
Enquiry
Appointment
Scheduling
Customer
```

Executable operation:

```text
appointment.confirm
```

Both are gardening-business examples. No `merchantType`, `gardener`, category or industry branch selects runtime behaviour.

### Deterministic discovery mapping

`PrototypeDiscoverySeedResolver` maps accepted discovery evidence to candidate semantic seeds.

Examples:

```text
PUBLISH_INFORMATION
    -> publication

SEND_ENQUIRY
    -> enquiry

ARRANGE_APPOINTMENT
    -> appointment + scheduling + customer

RESERVE_SUBJECT
    -> booking + customer

PLACE_ORDER
    -> ordering candidate seed

NOTHING_ELSE_FOR_NOW
    -> no additional semantic seed

OTHER
    -> configuration gap / clarification required
```

`NOTHING_ELSE_FOR_NOW` is mutually exclusive with positive additional-interaction choices.

`SUBSCRIBE_UPDATES` remains a configuration gap in this executable prototype because Subscription execution is not part of this slice.

### Capability-driven executable composition

`PrototypeExecutableModelFactory` composes the executable model from already-resolved capability selections.

It contains no merchant-category branching.

Current generic executable contributions include:

```text
Booking
    -> Booking Operational Object
    -> Customer relationship
    -> Allocation effect
    -> booking.confirm

Appointment + Scheduling
    -> Appointment Operational Object
    -> Customer relationship
    -> Allocation effect
    -> appointment.confirm
```

Publication and Enquiry are selectable/read-capable in this milestone without inventing mutation operations.

Ordering discovery remains valid, but choice-driven Ordering executable assembly is explicitly rejected until the generic model factory includes the complete accepted Ordering contribution. The existing retailer Ordering vertical slice remains executable through its established path.

### Merchant evolution

`prototype-gardener-evolving` begins with:

```text
Configuration Release 1
Publication
Enquiry
```

The test then simulates reviewed/approved merchant choices adding `ARRANGE_APPOINTMENT` and publishes/activates:

```text
Configuration Release 2
Publication
Enquiry
Appointment
Scheduling
Customer
```

The activation uses the real in-memory Configuration Publication and Configuration Release Activation boundaries, including:

- explicit revision lineage;
- expected-current configuration;
- explicit prototype approval;
- replacement activation evidence.

Merchant identity remains:

```text
prototype-gardener-evolving
```

After Release 2 becomes current, `appointment.confirm` is resolved through `ActiveOperationResolver` and executes through the same durable Appointment application/Jooq path used by other Appointment-capable merchants.

### Merchant-scoped Publication data

`PrototypePublicationProjection` no longer contains a publisher-specific behaviour branch.

The same capability-gated projection mechanism supplies different merchant-scoped fixture content:

```text
prototype-publisher
    -> Engineering Scholarship 2026

prototype-gardener-showcase
    -> Courtyard Garden Transformation
```

This demonstrates presentation/read variation through data under one projection mechanism rather than category-specific code.

## Verification evidence

Clean baseline after Booking/Appointment test-harness migration:

- GitHub Actions run 811 — **SUCCESS**
- head `374ed2dc446b2c46dd721c21c0817237c2cb8825`

Choice-driven bookable gardener and generic execution path:

- GitHub Actions run 820 — **SUCCESS**
- head `1e034fc76e96f6afc49ea012804e770682b1469a`

Same merchant Release 1 -> Release 2 and newly activated Appointment execution:

- GitHub Actions run 823 — **SUCCESS**
- head `a5d13eb7cc3733a413c343cbec58e33ece5b437c`

Merchant-scoped Publication projection:

- GitHub Actions run 825 — **SUCCESS**
- head `28b9de5fe74d8e674cba9fa39513d35e2eeee619`

The final post-hardening/documentation head must also pass the repository's full `mvn --batch-mode clean verify -Ppostgres-it` workflow before this evidence is treated as closed.

## Acceptance assessment

| Criterion | Result |
|---|---|
| Same trade can select different capability graphs | PASS |
| Category/business label is not runtime authority | PASS |
| Discovery option identity remains distinct from capability identity | PASS |
| `NOTHING_ELSE_FOR_NOW` creates no runtime semantic | PASS |
| Unsupported `OTHER` intent fails closed | PASS |
| Merchant can replace active Configuration Release without changing identity | PASS |
| Newly activated Appointment becomes executable through generic durable path | PASS |
| Publication read variation is merchant-scoped data, not category branching | PASS |
| Booking/Appointment independence remains preserved | PASS |
| Choice-driven incomplete Ordering assembly fails closed | PASS — pending final-head CI confirmation |

## Explicitly deferred / out of scope

This milestone does not invent or complete:

- Publication create/publish/withdraw mutation commands;
- Subscription execution;
- full Ordering contribution assembly inside the new generic choice-driven model factory;
- production onboarding HTTP mutation/approval endpoints;
- natural-language/AI inference implementation;
- child/dependent/safeguarding semantics for daycare;
- gardener routing, weather, quotation, materials or job-management semantics;
- production authentication.

The absence of these features does not weaken this milestone's architectural proof.

## UI handoff consequence

The next UI prototype should consume active configuration/surface truth and visibly demonstrate:

```text
same nominal trade
    + different merchant choices
        -> different storefront/dashboard interactions
```

and eventually:

```text
same merchant identity
    + later approved Configuration Release
        -> changed available interactions
```

The UI must not reconstruct capability selection from merchant category, page template or route name.
