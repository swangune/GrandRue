# MS-PROT-042 v1.4 — Booking Residual Obligation & Discharge Amendment

**Document ID:** MS-PROT-042  
**Version:** 1.4  
**Status:** **ACCEPTED by manual approval on 25 August 2026**  
**Amends:** MS-PROT-042 v1.2 and v1.3 within the scope defined below  
**Depends on:** MS-PROT-040, MS-PROT-042 v1.2-v1.3, MS-PROT-049  
**Purpose:** Define the Booking-owned authority that determines whether an existing Booking still imposes an outstanding operational obligation requiring residual merchant management after Booking is disabled for new activity.

---

## 1. Governing decision

A Booking remains historically true independently of whether it still requires current operational management.

Main Street shall therefore distinguish:

```text
Booking historical existence
        ≠
Booking-owned outstanding operational obligation
```

The Booking capability owns the determination of whether its reservation commitment remains operative. Surface composition may consume that determination but shall not infer or redefine it.

---

## 2. Narrow reservation-commitment state

This amendment does **not** introduce a universal Booking lifecycle or a universal fulfilment-outcome enumeration.

For residual-obligation purposes, the minimum Booking-owned distinction is:

```text
IN_FORCE
    reservation commitment remains operative

RELEASED
    Booking-owned reservation commitment has been authoritatively discharged
```

This distinction describes only the Booking-owned reservation commitment. It shall not collapse or substitute for separately owned facts such as:

```text
payment state
refund state
service fulfilment
customer no-show
merchant/provider no-show
notification delivery
provider execution/readiness
```

Consequently:

```text
Booking reservation commitment truth
    ≠ Payment truth
    ≠ Refund truth
    ≠ Fulfilment/outcome truth
    ≠ Notification truth
    ≠ Provider truth
```

---

## 3. Outstanding Booking-owned obligation

A Booking has an outstanding Booking-owned operational obligation while its authoritative reservation commitment is `IN_FORCE`, or while a Booking-owned consequence required to terminate or discharge that reservation commitment remains unresolved.

Conceptually:

```text
Booking-owned authoritative facts
        ↓
Booking residual-obligation authority
        ↓
has outstanding Booking obligation?
        │
        ├── YES → residual Booking management surface may remain required
        └── NO  → Booking no longer requires residual operating management
```

The decision is capability-owned. Presentation code, Surface Contribution resolution, Payment, Notification, Provider Fulfilment and other foreign capabilities shall not independently decide whether the Booking reservation obligation is outstanding.

---

## 4. Discharge

The Booking-owned reservation obligation becomes discharged only through authoritative Booking-owned evidence established under registered Booking semantics.

Examples of events/operations that **may** establish discharge where separately registered and valid include:

```text
authoritative Booking cancellation
another Booking-owned terminal discharge operation
registered natural completion semantics where explicitly defined
```

The existence of a historical Booking record is retained after discharge.

Cancellation remains:

```text
CancelBooking
    ≠ DeleteBooking
```

and discharge of the reservation commitment does not erase historical or audit evidence.

---

## 5. Time passage is not universal discharge authority

The mere passage of time beyond a Booking reservation interval shall **not** universally change a Booking from `IN_FORCE` to `RELEASED`.

Rejected inference:

```text
reservationEnd < now
        ↓
automatically RELEASED
```

A Booking type may have registered semantics under which natural completion of its reservation scope constitutes authoritative discharge, but that must be explicitly established by the owning Booking semantics. No generic residual-surface adapter may invent that rule from timestamps alone.

This prevents presentation/runtime convenience from silently defining Booking lifecycle semantics.

---

## 6. Foreign-capability obligations do not keep the Booking reservation obligation alive

A foreign-capability obligation does not by itself make the Booking reservation commitment outstanding after Booking-owned discharge.

Example:

```text
Booking reservation obligation = RELEASED
Payment refund obligation       = OUTSTANDING
```

Required consequence:

```text
Booking-owned reservation obligation
    does not become IN_FORCE merely because Payment still has work
```

Payment, Refund, Notification, Provider Fulfilment and other capabilities retain responsibility for their own outstanding obligations and operating surfaces.

Cross-capability orchestration may coordinate consequences but shall not transfer semantic ownership.

---

## 7. Residual surface consequence

MS-PROT-040 and MS-PROT-049 remain authoritative for capability deactivation and residual surface composition.

For Booking:

```text
Booking disabled for new activity
        +
at least one Booking-owned outstanding operational obligation
        ↓
minimum registered Booking residual-management surface remains eligible
```

while:

```text
new Booking creation activity
        ↓
remains disabled
```

When no Booking-owned outstanding operational obligation remains, Booking no longer requires a residual operating surface merely because historical Booking records exist. Historical records may remain available through appropriate history/audit/projection surfaces.

---

## 8. Authority boundary

The accepted authority boundary is:

```text
Booking-owned authoritative state/evidence
        ↓
Booking residual-obligation authority
        ↓
boolean/query result for current merchant scope
        ↓
ResidualSurfaceObligationAuthority consumption
        ↓
MS-PROT-049 contextual surface resolution
```

Hard rules:

1. Surface composition asks; Booking decides.
2. The residual-surface layer shall not infer Booking discharge from reservation dates alone.
3. A stored convenience boolean such as `booking.outstanding=true/false` shall not become an independently mutable second authority.
4. If implementation persists a narrow reservation-commitment state or discharge evidence, it must remain Booking-owned and mutated only through registered Booking authority.
5. Historical accessibility is separate from residual operational management.
6. Discharging Booking does not imply payment/refund/notification/provider success.

---

## 9. Implementation consequence

A conforming implementation may expose a Booking-owned query/decision contract such as:

```text
BookingResidualObligationAuthority

hasOutstandingBookingObligation(
    MerchantScope
)
```

or an equivalent capability-owned contract.

MS-PROT-049 may adapt/consume that result. The Surface subsystem shall not inspect Booking persistence directly or duplicate Booking lifecycle rules.

The exact storage representation is an implementation concern provided that:

- `IN_FORCE` versus authoritative discharge remains deterministic;
- no mutable derived boolean becomes a second source of truth;
- Booking owns mutation of its reservation commitment;
- foreign-capability state does not redefine the result; and
- time passage alone is not treated as universal discharge authority.

---

## 10. Validation record

This amendment resolves the implementation-discovered ambiguity encountered while attempting to provide a concrete Booking adapter for `ResidualSurfaceObligationAuthority`.

Validation against accepted boundaries:

| Constraint | Result |
|---|---|
| Existing commitments survive capability deactivation | PASS |
| Minimum residual management surface remains possible | PASS |
| Booking owns Booking commitment truth | PASS |
| Surface composition does not invent Booking lifecycle | PASS |
| Payment/refund truth remains separate | PASS |
| Fulfilment/outcome truth remains separate | PASS |
| Historical Booking evidence survives discharge | PASS |
| No universal `COMPLETED`/`NO_SHOW` lifecycle invented | PASS |
| Time passage alone is not universal discharge | PASS |
| No mutable convenience boolean becomes independent authority | PASS |

No broader Booking lifecycle, fulfilment-outcome vocabulary or merchant-policy rule is introduced by this amendment.
