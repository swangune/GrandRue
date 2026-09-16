# MS-PROT-042 v1.5 — Booking, Appointment & Scheduling Execution Contract Amendment

**Document ID:** MS-PROT-042  
**Version:** 1.5  
**Status:** **ACCEPTED by manual approval on 26 August 2026**  
**Amends:** MS-PROT-042 v1.2-v1.4 within the scope defined below  
**Depends on:** MS-PROT-020 v1.4, MS-PROT-023, MS-PROT-025, MS-PROT-041 v1.0-v1.1, MS-PROT-042 v1.2-v1.4, MS-PROT-043, MS-PROT-059, MS-PROT-072  
**Review / falsification evidence:** `docs/development/motel-booking-execution-design-review-falsification-2026-08-26.md`  
**Purpose:** Make the accepted distinction between Booking, Appointment and Scheduling executable by defining their canonical operation identities, minimum command semantics, ownership boundaries, consistency requirements and projection consequences without introducing business-type-specific behaviour.

---

## 1. Governing correction

The accepted corpus already establishes that Booking and Appointment are distinct commitments. Implementation evidence from the motel/accommodation stress test showed that the existing executable prototype nevertheless treated every `booking.confirm` as a composite Booking-plus-Appointment operation.

That implementation shape is not semantically valid as a universal Booking contract.

This amendment therefore establishes the executable boundary:

```text
booking.confirm
    establishes Booking reservation commitment

appointment.confirm
    establishes Appointment scheduled-service commitment

Scheduling
    determines valid appointment intervals and scheduling constraints

Calendar
    projects relevant scheduling and commitment information
```

Hard invariant:

> **Booking, Appointment, Scheduling and Calendar remain distinct authorities. No one of them shall silently manufacture or redefine another merely for implementation convenience.**

---

## 2. Canonical capability operations

The canonical registered operation identities within this scope are:

```text
booking.confirm
appointment.confirm
```

### 2.1 `booking.confirm`

`booking.confirm` SHALL mean:

> **Establish one Booking reservation commitment for a booked subject within a defined reservation scope under the governing Booking semantics.**

It SHALL NOT implicitly create an Appointment.

### 2.2 `appointment.confirm`

`appointment.confirm` SHALL mean:

> **Establish one Appointment commitment for an agreed service/interaction interval after the applicable Scheduling authority has authoritatively revalidated that interval and any required capacity claims can be established.**

It SHALL NOT implicitly create a Booking.

### 2.3 Composite business intents

Where a genuine business intent requires both a Booking and an Appointment, an explicit accepted application orchestration MAY coordinate the two capability-owned operations/effects.

The mere coexistence of Booking and Appointment capabilities for one merchant SHALL NOT imply such orchestration.

---

## 3. Booking execution contract

### 3.1 Minimum Booking input semantics

The universal semantic inputs to `booking.confirm` SHALL be equivalent to:

```text
trusted merchant scope
command identity
booking identity
customer-context identity
booked-subject reference
reservation scope
```

An Appointment identity SHALL NOT be a universal Booking input.

A scheduled-operation identity SHALL NOT be a universal Booking input.

Additional inputs MAY be required where separately accepted Booking, Allocation, Payment, policy, fulfilment or other capability contracts make them applicable.

### 3.2 Booked subject

The booked subject is Booking-owned committed truth describing what the customer/merchant reserved.

Examples:

```text
Standard Room
Executive Room
Excavator 7
Meeting Room A
Attendance capacity
```

The booked subject SHALL remain distinguishable from the Resource or capacity against which Allocation is established.

### 3.3 Reservation scope

Reservation scope expresses the scope secured by the Booking, including a supported date/time range, quantity, entitlement period or other registered reservation dimension.

For the current accommodation slice, a time-window/date-range reservation may be represented by the existing time-window allocation primitive where that representation preserves the accepted Booking meaning.

No Appointment is created merely because the reservation scope contains time.

---

## 4. Booking and Allocation

Where Booking confirmation requires capacity protection, the authoritative outcome SHALL include the required Allocation within the atomic consistency boundary established by MS-PROT-025 and MS-PROT-072.

Conceptually:

```text
booking.confirm
    ↓
revalidate applicable Booking constraints
    ↓
claim required Resource capacity
    +
create Booking
    +
establish Booking → CustomerContext relationship
    +
record reservation commitment IN_FORCE
    +
record handled command/result
    +
record committed fact / outbox evidence
    ↓
COMMIT
```

If any invariant-required part fails, the Booking commitment SHALL NOT partially persist.

Availability observed before confirmation remains advisory and does not grant ownership.

---

## 5. Booked subject versus allocated Resource

The following distinction is mandatory:

```text
BOOKED SUBJECT
    what the customer/merchant reserved

ALLOCATED RESOURCE / CAPACITY
    what Resource capacity currently protects that reservation
```

Example:

```text
Booking booked subject:
    Standard Room

Booking reservation scope:
    28 Aug → 30 Aug

Allocation:
    Standard Room pooled capacity
```

A later supported implementation may allocate a concrete room such as `Room 104`, but that internal assignment SHALL NOT rewrite the Booking's booked-subject truth from `Standard Room` to `Room 104` unless a separately authorised Booking amendment changes what was actually committed.

This rule preserves customer-facing simplicity while retaining internal operational precision.

---

## 6. Pooled Resource compatibility

MS-PROT-020 v1.4 remains authoritative for Resource semantics.

A Booking MAY claim capacity against:

```text
individual Resource
or
pooled Resource / capacity subject
```

A motel/accommodation business therefore does not require a `Motel` capability or merchant-type branch.

The concrete implementation of Resource pool membership, capacity counters and automatic concrete-resource assignment remains downstream implementation work provided that it preserves:

- merchant scope;
- Resource/Allocation ownership;
- concurrency safety;
- Booking/Allocation atomicity where required;
- booked-subject truth; and
- business-type neutrality.

---

## 7. Appointment execution contract

### 7.1 Minimum Appointment semantics

`appointment.confirm` establishes an Appointment with at least:

```text
trusted merchant scope
command identity
appointment identity
customer-context identity
offering/service context
agreed scheduled interval
configuration/release provenance
```

Additional staff/resource/capacity/location/payment semantics are contextual rather than universal.

### 7.2 Appointment does not require Booking

A consultation, haircut, mechanic service, home-service visit or similar scheduled service SHALL NOT require a synthetic Booking merely for architectural symmetry.

The Appointment itself is the scheduled-service commitment.

### 7.3 Appointment capacity

Where the Appointment requires capacity, its confirmation SHALL establish the required Allocation atomically with the Appointment commitment where the capacity claim participates in the commitment invariant.

---

## 8. Scheduling authority

Scheduling is not another name for Appointment.

Scheduling owns the evaluation of scheduling constraints and the derivation/revalidation of valid Appointment intervals from applicable authoritative/configured inputs, including where applicable:

```text
working hours
recurring breaks
offering duration
buffers
merchant ScheduleIntent / UNAVAILABLE periods
existing customer commitments
Resource state
capacity/allocation state
merchant scheduling policy
```

Scheduling answers:

> **Can this supported service/interaction validly occur during this candidate interval under current authoritative constraints?**

Appointment answers:

> **Has an agreed customer-related service/interaction commitment been established for this interval?**

Therefore:

```text
AVAILABLE INTERVAL
    ≠
APPOINTMENT
```

---

## 9. Appointment confirmation and Scheduling revalidation

A customer-visible or merchant-visible available interval is advisory.

Before `appointment.confirm` commits, the applicable Scheduling authority SHALL revalidate the selected interval against current authoritative constraints.

Canonical flow:

```text
Scheduling derives valid intervals
        ↓
customer/merchant selects one
        ↓
appointment.confirm
        ↓
Scheduling revalidation
        ↓
required capacity claim
        +
Appointment commitment
        +
customer relationship
        +
handled command/result
        +
committed fact/outbox evidence
        ↓
COMMIT
```

A stale availability result SHALL NOT authorise an invalid Appointment.

---

## 10. Operating configuration, ScheduleIntent, Appointment and Calendar

The following remain distinct:

```text
OPERATING CONFIGURATION
    working hours
    recurring breaks
    duration
    buffers
    scheduling policy

MERCHANT SCHEDULE INTENT
    specific time-bound merchant scheduling decision
    e.g. UNAVAILABLE

APPOINTMENT
    customer-related scheduled-service commitment

CALENDAR REPRESENTATION
    projection/integration representation of relevant scheduling facts
```

A merchant `UNAVAILABLE` intent SHALL NOT become an Appointment merely because its title says `Dentist appointment`.

Working hours and recurring breaks SHALL NOT become Appointments or ordinary calendar business commitments.

---

## 11. Calendar boundary

MS-PROT-041 remains authoritative for Main Street Calendar.

Calendar is a projection/operational representation of relevant scheduling information. It SHALL NOT:

- become Appointment authority;
- become Scheduling authority;
- redefine working-hours configuration;
- bypass current scheduling constraints;
- infer customer relationships from titles; or
- allow an external calendar provider to autonomously mutate Main Street commitment truth.

Appointment or ScheduleIntent mutation occurs through the applicable Main Street capability operation. Calendar representation follows authoritative state.

---

## 12. Consultant consequence

A standard consultation reference merchant whose material business intent is:

```text
customer schedules consultation
```

SHALL be represented by Appointment + Scheduling semantics rather than by a synthetic Booking + Appointment pair.

If that same merchant separately offers a reservable subject or entitlement, Booking MAY also be active for that independent business meaning.

---

## 13. Accommodation consequence

A standard motel/accommodation room-category reservation SHALL be representable as:

```text
Booking
+
CustomerContext
+
Resource / Capacity / Allocation
```

without Appointment.

Example:

```text
booked subject:
    Standard Room

reservation scope:
    Friday → Sunday

allocation:
    applicable Standard Room capacity
```

Check-in time, checkout time or a date range SHALL NOT by themselves manufacture Appointment semantics.

If the accommodation merchant separately offers a scheduled spa treatment, consultation, airport transfer appointment or another true scheduled-service interaction, Appointment + Scheduling MAY coexist for that distinct operation.

---

## 14. Idempotency and duplicate prevention

Both `booking.confirm` and `appointment.confirm` SHALL preserve the channel-convergence/idempotency requirements of MS-PROT-059.

For each operation:

- retry of the same logical command identity with the same intent SHALL reconcile to the stable prior result;
- reuse of the same command identity with materially different intent SHALL fail;
- an independently initiated new business intent MAY create a distinct commitment even if its business values happen to be equal;
- transport retries SHALL NOT multiply commitments or Allocation claims.

---

## 15. Authorisation and merchant scope

Merchant scope SHALL be established from trusted execution context rather than blindly accepted from customer-controlled transport data.

Actor Authorisation remains separate from Scheduling validity, Booking/Appointment operational eligibility, Commercial Entitlement and Surface Exposure.

No valid Scheduling result grants actor authority.

---

## 16. Events and projection consequences

A successful authoritative operation MAY emit the corresponding committed fact, including:

```text
booking.confirmed
appointment.confirmed
```

where registered in the pinned semantic release.

Events record committed facts and support post-commit reactions/projections. They SHALL NOT be used to make the underlying commitment true after the transaction.

Calendar, notifications, analytics and other projections/reactions consume committed truth according to their own contracts.

---

## 17. Explicit exclusions

This amendment does not define:

- motel check-in/check-out lifecycle;
- room housekeeping/cleaning/turnaround lifecycle;
- maintenance/unavailable room-state lifecycle;
- universal automatic physical-room assignment;
- customer-selected room numbers;
- overbooking policy;
- multi-room/group Booking semantics;
- Booking cancellation/refund rules beyond existing accepted authority;
- a universal Schedule aggregate;
- a universal Availability engine owning every capability's availability semantics;
- frontend calendar implementation;
- external calendar provider technology; or
- a `Motel`, `Hotel`, `Consultant` or other business-type capability.

---

## 18. Implementation migration consequence

The existing executable prototype is known to be non-conforming within this amendment's scope because:

```text
ConfirmBookingCommand
    universally requires appointmentIdentifier
    and scheduledOperationIdentifier

BookingApplicationService
    universally creates Booking + Appointment

booking_handled_command
    universally requires appointment_identifier
```

Conforming implementation SHALL migrate that appointment-shaped prototype contract rather than preserving it as the universal Booking operation.

Migration SHALL preserve already accepted persistence, merchant isolation, release affinity, idempotency and transactional invariants.

Historical prototype data need not be production-migrated where the prototype database is explicitly disposable, but production migration structure SHALL remain forward-only and deterministic.

---

## 19. Falsification record

The recommendation was tested against the following scenarios before manual approval.

| Scenario | Result |
|---|---|
| Motel/customer reserves room category across date range | PASS — Booking only |
| Consultant/customer chooses consultation time | PASS — Appointment + Scheduling only |
| Equipment reservation without service encounter | PASS — Booking only |
| Accommodation merchant separately offers spa treatment | PASS — Booking and Appointment coexist independently |
| Merchant blocks 14:00 for personal dentist visit | PASS — ScheduleIntent, not Appointment |
| Working hours constrain scheduling | PASS — configuration, not Appointment |
| Available slot becomes stale before confirmation | PASS — Appointment revalidation required |
| Concurrent final capacity claim | PASS — Allocation atomicity required |
| Same command retried after lost acknowledgement | PASS — idempotent stable result |
| Same command identity reused with changed intent | PASS — conflict |
| Calendar title contains customer/service-like text | PASS — title does not create semantics |
| External calendar unavailable | PASS — provider failure cannot rewrite commitment truth |
| Merchant supports Booking and Appointment simultaneously | PASS — no implicit cross-creation |

The proposal survived without introducing business-type branching or shared mutable ownership.

---

## 20. Approval and acceptance statement

Manual approval was given on **26 August 2026** after review of the motel stress-test gap and the additional Appointment/Scheduling boundary.

The accepted result is:

> **`booking.confirm` establishes Booking only. `appointment.confirm` establishes Appointment only after Scheduling revalidates the agreed interval. Neither commitment type implicitly creates the other. Scheduling owns scheduling constraints and Appointment availability decisions. Calendar projects scheduling/commitment truth and is not semantic mutation authority. Booking booked-subject truth remains distinct from internal Resource Allocation.**
