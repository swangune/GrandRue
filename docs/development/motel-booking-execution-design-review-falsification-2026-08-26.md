# Motel / Standalone Booking Execution Design Review & Falsification

**Date:** 26 August 2026  
**Status:** REVIEW / FALSIFICATION EVIDENCE — AWAITING MANUAL APPROVAL  
**Authority:** None. This document is not semantic authority and does not reserve an MS-PROT number.  
**Trigger:** Pre-UI executable prototype stress test using a motel / accommodation merchant.  
**Governing process:** `designs/DESIGN-RULES.md` and `designs/IMPLEMENTATION-RULES.md`.

---

## 1. Problem discovered

The accepted semantic corpus distinguishes Booking from Appointment:

- a Booking reserves a supported bookable subject, resource, capacity or entitlement for a reservation scope;
- an Appointment establishes an agreed service/interaction interval;
- a hotel/motel room-category reservation is a Booking and does not become an Appointment merely because time is involved.

The current executable Booking vertical slice does not preserve that distinction.

`ConfirmBookingCommand` currently requires:

```text
bookingIdentifier
appointmentIdentifier
customerContextIdentifier
scheduledOperationIdentifier
capacityScope
```

`BookingApplicationService` then always establishes, in one `booking.confirm` fulfilment:

```text
Booking
+
Appointment
+
Booking → CustomerContext relationship
+
Appointment → CustomerContext relationship
+
Appointment capacity Allocation
```

The durable handled-command schema likewise makes `appointment_identifier` mandatory for every handled Booking command.

This implementation was sufficient for the original appointment-style consultant vertical slice, but it cannot represent a standalone accommodation Booking without manufacturing an Appointment that the accepted semantic model explicitly rejects.

The motel stress test therefore exposes an implementation/contract gap rather than a need for a `MOTEL` business type.

---

## 2. Existing accepted authority

### MS-PROT-042

Current accepted Booking/Appointment authority establishes:

```text
BOOKING
    reserves something

APPOINTMENT
    schedules a service interaction
```

Accommodation room/category reservation is explicitly Booking semantics.

Booking's conceptual minimum includes:

```text
booking_identity
merchant_scope
customer_relationship
booked_subject
reservation_scope
lifecycle_state
configuration_provenance
```

A Booking may additionally relate to allocation, resource, quantity, payment, policy and fulfilment where applicable. None is universally an Appointment.

### MS-PROT-020 v1.4

Accepted Resource semantics already establish that:

- Resource is a capacity-constraining role;
- a Resource may be individual or pooled;
- `Executive Room Capacity` is a valid pooled Resource example;
- Allocation is an authoritative scoped claim against Resource capacity;
- `Booking → room-category capacity` is an accepted Allocation example;
- exact resource-pool implementation remains downstream.

Therefore a motel does not justify a new motel-specific semantic capability.

### MS-PROT-025 / MS-PROT-072

Where Booking confirmation and required Allocation participate in one invariant, they must commit within the required consistency boundary. Cross-capability orchestration does not transfer semantic ownership.

---

## 3. Material decision that the accepted corpus does not make concrete enough for implementation

The accepted corpus defines the semantic distinction but does not currently establish a concrete executable-operation contract separating standalone Booking confirmation from Appointment confirmation.

The current code has only one concrete prototype path, `booking.confirm`, and that path is composite Booking+Appointment.

The following questions therefore cannot be silently answered by implementation:

1. What concrete registered operation identity represents standalone Booking confirmation?
2. What concrete registered operation identity represents Appointment confirmation?
3. May either operation silently manufacture the other commitment type?
4. What minimum command data belongs universally to Booking confirmation?
5. How does a Booking against a pooled Resource preserve the customer-selected booked subject while Allocation may consume internal capacity that the customer did not select directly?

These are semantic/runtime-contract questions because operation identity and effect sets are release-affined executable semantics.

---

## 4. Proposal

### 4.1 Preserve distinct canonical operations

Establish two distinct registered operation identities:

```text
booking.confirm
appointment.confirm
```

`booking.confirm` SHALL mean:

> Establish one Booking reservation commitment under the Booking capability's accepted semantics.

`appointment.confirm` SHALL mean:

> Establish one Appointment scheduling commitment under the Appointment capability's accepted semantics.

Neither operation SHALL implicitly create the other commitment type merely because the merchant happens to support both capabilities.

Where one genuine business intent requires both commitments, an explicit accepted orchestration may coordinate both capability-owned operations/effects. The existence of such an orchestration is not implied universally and is outside this narrow decision unless a concrete merchant scenario requires it.

### 4.2 Booking command contract

The universal Booking-confirmation command SHALL NOT require an Appointment identity or a scheduled-operation identity.

Its minimum semantic inputs SHALL be equivalent to:

```text
merchant scope            trusted/resolved outside customer control
command identity
booking identity
customer-context identity
booked-subject reference
reservation scope
```

Additional allocation/payment/policy inputs SHALL appear only where their owning accepted contracts require them.

Platform-recorded commitment time remains observed at the authoritative mutation boundary rather than caller supplied.

### 4.3 Booking authoritative effects

For a Booking that requires capacity protection, the authoritative outcome SHALL be equivalent to:

```text
CREATE Booking
+
ESTABLISH Booking → CustomerContext
+
CLAIM required Allocation
+
record Booking reservation commitment as IN_FORCE
+
record handled command/result
+
record booking.confirmed committed fact/outbox evidence
```

The Booking effect set SHALL NOT contain Appointment creation or Appointment relationship effects unless a separately accepted orchestration explicitly requires them.

### 4.4 Appointment authoritative effects

Appointment confirmation SHALL remain Appointment-owned and SHALL establish the agreed service/interaction interval, customer relationship and required capacity claims under Appointment semantics.

It SHALL NOT require a Booking merely for architectural symmetry.

### 4.5 Booked subject versus allocated Resource

A Booking SHALL preserve what the customer/merchant actually reserved as Booking truth.

Example:

```text
booked subject:
    Standard Room

reservation scope:
    28 Aug → 30 Aug
```

Allocation may consume:

```text
pooled Standard Room capacity
```

or, where a supported allocation implementation selects one:

```text
Room 104 capacity
```

The internal allocation target SHALL NOT replace the Booking's booked-subject truth.

Therefore:

```text
BOOKED SUBJECT
    what was reserved

ALLOCATED RESOURCE/CAPACITY
    what capacity currently protects that reservation
```

remain distinguishable.

### 4.6 Pooled Resource implementation boundary

No new motel-specific capacity semantics are proposed.

The first executable motel slice MAY implement the already accepted pooled-Resource semantics using deterministic prototype fixture data and an implementation-specific allocation mechanism, provided that:

- merchant scope is preserved;
- pool/resource membership is not customer-controlled input;
- the final claim is concurrency-safe;
- the Booking and required Allocation commit atomically;
- customer-facing Booking truth remains category/subject based;
- no business-type conditional selects the behaviour; and
- the mechanism does not redefine accepted Resource/Allocation semantics.

The concrete production source of pool membership/capacity remains downstream unless implementation evidence exposes a new material semantic question.

### 4.7 Explicitly deferred from this decision

This decision SHALL NOT define:

- motel check-in/check-out lifecycle;
- room cleaning/turnaround lifecycle;
- maintenance/unavailable room-state lifecycle;
- universal automatic physical room assignment;
- customer-selected room numbers;
- housekeeping;
- multi-room quantity booking in one Booking;
- group reservations;
- overbooking policy;
- cancellation/refund policy beyond already accepted Booking authority;
- payment-provider behaviour;
- a `Motel` capability or merchant type.

Those subjects are unnecessary to prove the standalone Booking architecture and must not be imported into this amendment speculatively.

---

## 5. Alternatives reviewed

### Alternative A — Keep current `booking.confirm` as universally Booking+Appointment

**REJECT.**

It contradicts accepted MS-PROT-042 semantics for accommodation/equipment/capacity reservation and causes false Appointment creation.

### Alternative B — Add `MotelBookingService` or `if (merchantType == MOTEL)`

**REJECT.**

This violates business-type neutrality and would begin accumulating niche-specific applications.

### Alternative C — Force customers to select a concrete room so existing capacity=1 time-window claims can be reused

**REJECT.**

It leaks operational complexity, changes the customer's intended booked subject and defeats accepted pooled-Resource semantics.

### Alternative D — Preserve composite `booking.confirm` and add a second synonym such as `booking.reserve`

**REJECT.**

Two operation identities for materially overlapping Booking confirmation semantics would create ambiguous semantic authority and migration burden without domain justification.

### Alternative E — Correct `booking.confirm` to Booking-only and establish `appointment.confirm` separately

**RECOMMENDED.**

This directly reflects accepted capability ownership, removes synthetic commitments, and scales to accommodation, equipment, facilities and scheduled services without merchant-type branching.

### Alternative F — Design check-in, checkout, room states and housekeeping before adding the motel prototype

**DEFER.**

Those are real future operational questions but are not required to validate standalone Booking confirmation and pooled capacity reservation.

---

## 6. Falsification

### 6.1 Motel / accommodation

Scenario:

```text
Customer reserves Standard Room
28 Aug → 30 Aug
```

Expected authoritative model:

```text
Booking = YES
Appointment = NO
booked subject = Standard Room
reservation scope = requested stay interval
capacity protection = required
```

**Result:** Proposed split passes. Current implementation fails because it requires Appointment identity and creates Appointment state.

### 6.2 Consultant

Scenario:

```text
Customer schedules structural consultation
09:00 → 10:00
```

Expected model:

```text
Appointment = YES
Booking = NO unless an independent reservation commitment genuinely exists
```

**Result:** Proposed split passes and exposes that the current consultant reference slice should ultimately become Appointment-led rather than use synthetic Booking semantics.

### 6.3 Motel with spa

Same merchant offers:

```text
Executive Room stay
    → Booking

Spa treatment Saturday 15:00
    → Appointment
```

**Result:** Distinct operations compose cleanly under one merchant configuration without collapsing the two commitments.

### 6.4 Equipment rental

Customer reserves one excavator for a date range.

Expected:

```text
Booking = YES
Appointment = NO
specific Resource may be allocated
```

**Result:** Same Booking contract works without motel terminology.

### 6.5 Pooled facility/capacity

Customer reserves one unit of a pooled capacity subject.

Expected:

```text
booked subject remains customer-facing subject/category
Allocation protects internal capacity
```

**Result:** Proposed booked-subject/allocation separation survives.

### 6.6 Concurrent final capacity

Two customers attempt the final valid capacity concurrently.

Expected:

```text
only commitments that can coexist may succeed
```

**Result:** Proposal requires atomic claim + Booking commitment and does not permit stale availability to authorise over-allocation.

### 6.7 Retry after lost acknowledgement

Same command identity is retried after transport uncertainty.

Expected:

```text
same logical retry
    → same Booking result
    → no second Allocation
```

**Result:** Existing idempotency architecture remains applicable; split operations improve rather than weaken command identity.

### 6.8 Merchant manual resource assignment

Some merchants may confirm category capacity without choosing a concrete physical resource until later.

**Result:** Proposal deliberately does not make individual resource assignment universal. Pooled capacity remains sufficient at Booking confirmation where the merchant's supported model permits it.

### 6.9 Customer-selected resource

Some equipment/facility merchants may legitimately allow selection of a specific resource.

**Result:** Proposal permits booked subject / allocation target to coincide where the accepted merchant model genuinely exposes the specific Resource; it does not force pooled allocation.

### 6.10 Multi-unit one-Booking request

A customer requests two rooms in one logical Booking.

**Result:** This exposes a future implementation/semantic scope that the first motel slice need not claim to support. The proposed contract does not silently assume that every Booking consumes exactly one pooled unit as a universal semantic law.

---

## 7. Architecture review

| Constraint | Result |
|---|---|
| Booking and Appointment retain separate ownership | PASS |
| No motel/business-type branching | PASS |
| Pooled Resource reuses accepted MS-PROT-020 semantics | PASS |
| Booking truth remains separate from Allocation truth | PASS |
| CustomerContext relationship retained | PASS |
| Atomic capacity protection retained | PASS |
| Idempotency retained | PASS |
| Merchant scope retained | PASS |
| Semantic release operation identity becomes explicit | PASS |
| Provider neutrality retained | PASS |
| Payment remains independent | PASS |
| UI remains projection, not authority | PASS |
| No universal room assignment policy introduced | PASS |
| No speculative housekeeping/check-in model introduced | PASS |

---

## 8. Implementation consequences if approved

Implementation should then proceed test-first under `IMPLEMENTATION-RULES.md`:

1. formalise the approved amendment to current MS-PROT-042 authority;
2. update Authority Index / DDR / Lexicon only where applicable;
3. run corpus conformance;
4. write RED tests proving `booking.confirm` can establish Booking without Appointment;
5. introduce/align `appointment.confirm` tests and Appointment-owned execution path;
6. refactor the current appointment-shaped `ConfirmBookingCommand` / Booking persistence contract so Booking preserves booked subject and reservation scope rather than universally requiring Appointment/scheduled-operation fields;
7. migrate the consultant prototype to Appointment-led execution;
8. add `prototype-motel` using Booking + Customer + applicable Payment capability composition;
9. use accepted pooled Resource/Allocation semantics for the motel fixture without a merchant-type conditional;
10. add durable PostgreSQL tests for merchant isolation, idempotency, final-capacity conflict and restart recovery;
11. expose motel read/write prototype HTTP contracts;
12. rerun full unit + PostgreSQL integration suite and live startup verification;
13. update pre-UI prototype readiness evidence to four reference merchant compositions.

No motel UI should be built on a synthetic Appointment contract.

---

## 9. Recommendation

**RECOMMENDATION: ACCEPT, subject to explicit manual approval.**

Approve the following narrow decision:

> `booking.confirm` becomes the canonical Booking-only reservation commitment operation; `appointment.confirm` becomes the canonical Appointment-only scheduling commitment operation. Neither silently manufactures the other. Booking confirmation preserves a booked-subject reference and reservation scope and may atomically establish Allocation against accepted individual or pooled Resource capacity without replacing booked-subject truth with the internal allocation target. Motel-specific capabilities, check-in/housekeeping semantics and universal concrete-room assignment are excluded from this decision.

This is the smallest correction that makes the accepted Booking/Appointment model executable for both consultants and motels without business-specific branching.

Until explicit manual approval, implementation of the changed operation contracts and `prototype-motel` MUST remain stopped.
