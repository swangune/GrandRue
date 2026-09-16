# MS-PROT-042 v1.16 — Booking, Appointment & Scheduling Commercial Access Classification Amendment

**Document ID:** MS-PROT-042  
**Version:** 1.16  
**Status:** ACCEPTED  
**Approved:** 16 September 2026 by explicit manual approval in ChatGPT  
**Authority type:** Booking / Appointment / Scheduling commercial-access classification amendment  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-042 through v1.15 within commercial-access classification only  
**Depends on:** Composite MS-PROT-042 through v1.15; composite MS-PROT-056 through v1.9; composite MS-PROT-062; applicable CustomerContext, Actor Authorisation, Resource, Allocation, Payment, Notification and Exposure authorities  
**Preserves:** Booking, Appointment, Scheduling, Resource/Allocation, Payment and CustomerContext ownership boundaries  
**Partially resolves:** `MS-PROT-056-V17-DQ-001` by supplying the missing Booking/Appointment/Scheduling owner classifications  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY

**Product identity note:** inherited `Main Street` references identify the product currently named **GrandRue**. Stable `MS-*` and `mainstreet.*` identifiers remain unchanged pending separately governed migration.

---

# 0. Fundamental Vision Conformance

GrandRue SHALL continue to configure Booking, Appointment and Scheduling according to the merchant's actual business model independently of current subscription tier.

Commercial permission SHALL govern acquisition of new customer commitment scope.

It SHALL NOT erase configured semantics or make an existing customer commitment impossible to inspect, cancel, discharge or otherwise safely resolve merely because a paid grant ends.

Canonical:

```text
merchant business reality
        ↓
Booking / Appointment / Scheduling configured
        ↓
commercial permission currently available?
        ↓
YES → new commitment scope may be established
NO  → no new commitment scope
      existing commitments remain safely resolvable
```

The distinction is necessary because a merchant who stops paying still has real customers, real reservations and real appointments.

---

# 1. Governing Decision

Composite MS-PROT-042 SHALL define exactly two protected commercial purposes:

```text
ESTABLISH_BOOKING_RESERVATION_COMMITMENT

ESTABLISH_APPOINTMENT_TIME_COMMITMENT
```

They SHALL be represented through these exact owner-qualified access contracts:

| Exact access contract | Protected purpose | Standard allocation | Target family |
|---|---|---|---|
| `booking/reservation-commitment-establishment-access@1` | `ESTABLISH_BOOKING_RESERVATION_COMMITMENT` | BUSINESS + GROWTH | `OPERATION_ACCESS` |
| `appointment/time-commitment-establishment-access@1` | `ESTABLISH_APPOINTMENT_TIME_COMMITMENT` | BUSINESS + GROWTH | `OPERATION_ACCESS` |

FREE SHALL receive neither protected purpose through the standard catalogue.

No third standalone commercial entitlement SHALL be created merely for the Scheduling engine.

Scheduling remains a capability-owned supporting authority used by Appointment operations and other accepted consumers.

---

# 2. Commercial Ownership Boundary

Booking continues to own reservation commitment truth.

Appointment continues to own agreed service-time commitment truth.

Scheduling continues to own calculation and authoritative revalidation of valid Appointment intervals.

Commercial owns:

```text
CommercialEntitlementIdentity
CommercialEntitlementDefinition
Commercial Access Binding
grant provenance
effective commercial permission
plan-revision grant sets
catalogue publication
```

This amendment supplies:

```text
exact owner access contract
+
exact protected commercial purpose
+
explicit no-entitlement classifications
```

It does **not** mint the final `CommercialEntitlementIdentity` values. Those remain Commercial-owned work in `MS-PROT-056-V17-DQ-001`.

---

# 3. Booking — Protected Commitment Establishment

`booking/reservation-commitment-establishment-access@1` SHALL apply whenever an operation would establish reservation commitment scope that the current Booking does not already possess.

It therefore covers the accepted initial:

```text
booking.confirm
```

and any later Booking commitment revision that would authoritatively acquire materially new reservation rights.

Protected examples include:

```text
create a new Booking

increase reserved quantity

extend the reservation into
previously uncommitted time

move the reservation so that
new time/capacity must be acquired

change the Booked Subject

replace an exact booked Resource
with a different Booked Subject

otherwise establish reservation
capacity/right not already contained
in the current commitment
```

The entitlement does not prove capacity exists.

It only supplies the commercial-permission predicate.

Booking must still revalidate all applicable semantic, capacity, policy, customer, payment and execution requirements.

---

# 4. Booking — Preparation

GrandRue SHALL define:

```text
booking/new-commitment-preparation-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

This contract covers bounded non-committing work such as deriving candidate Booking options and determining what would be required to establish a Booking.

Preparation SHALL NOT:

```text
create a Booking
establish a Hold
establish an Allocation
reserve capacity
create durable customer commitment
create permanent commercial permission
```

A preparation result SHALL NOT become retained authority for a later commitment.

At commitment time, the protected Booking permission and every other current requirement SHALL be revalidated.

Customer-facing presentation of an actionable new-Booking interaction SHALL NOT use this preparation exemption to bypass the protected commitment-establishment requirement.

---

# 5. Existing Booking Observation

GrandRue SHALL define:

```text
booking/existing-commitment-observation-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

The exemption applies only to an exact existing Booking that the caller is independently authorised to observe.

It SHALL NOT grant discovery of unrelated Bookings, CustomerContext access, private source information or authority over another Merchant Scope.

Loss of BUSINESS/GROWTH commercial permission therefore SHALL NOT by itself hide legitimate existing Booking commitments from an authorised merchant or customer.

---

# 6. Existing Booking Resolution

GrandRue SHALL define:

```text
booking/existing-commitment-resolution-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

The contract covers only operations whose authoritative effect resolves, restricts, records or discharges an already-existing Booking without acquiring materially new reservation scope.

This includes, where otherwise semantically valid:

```text
Booking cancellation / release

natural Booking discharge

Booking Utilisation Outcome

authorised correction of outcome evidence

a pure reduction of reserved quantity

a pure narrowing of reservation scope
```

A pure restriction qualifies only where the Booking owner can establish that the resulting commitment:

```text
does not acquire a new Booked Subject
does not acquire new time/capacity
does not increase quantity
does not establish another material
reservation right
```

If that classification is unresolved, the operation SHALL NOT be treated as residual resolution merely because it is named `modify`.

---

# 7. Booking Amendment Boundary

Booking amendment is therefore effect-sensitive.

Example:

```text
Friday → Sunday
changes to
Friday → Saturday
```

may qualify as bounded commitment restriction if no new reservation right is acquired.

But:

```text
Friday → Sunday
changes to
Saturday → Monday
```

does not qualify merely because one Booking identity survives.

Monday is newly acquired reservation scope.

The protected:

```text
ESTABLISH_BOOKING_RESERVATION_COMMITMENT
```

permission is therefore required.

Similarly:

```text
Standard Room
→ Executive Room
```

requires the protected path when it establishes a different Booked Subject, even if the merchant describes the change as an upgrade, downgrade or correction.

Commercial classification follows the authoritative effect, not UI wording.

---

# 8. Appointment — Protected Commitment Establishment

`appointment/time-commitment-establishment-access@1` SHALL apply whenever an operation establishes an agreed Appointment interval or replaces the current agreed interval with materially new time commitment.

It covers, where applicable under accepted MS-PROT-042 authority:

```text
appointment.confirm

customer-selected new Appointment

merchant-entered new Appointment

shared Appointment establishment

recurring Appointment commitment establishment

merchant-initiated rescheduling
that establishes a different interval

customer-accepted rescheduling
that establishes a different interval
```

A new Appointment or new interval continues to require all applicable current Scheduling, capacity, customer, policy, authority and consistency predicates.

The entitlement alone cannot manufacture a valid interval.

---

# 9. Appointment Reschedule Proposals

Creation of a reschedule proposal intended to establish a different Appointment interval SHALL be governed through:

```text
appointment/time-commitment-establishment-access@1
```

where the proposal path may obtain capacity protection or otherwise participates in acquiring a replacement interval.

A proposal is not itself an Appointment revision and therefore does not preserve commercial authority indefinitely.

If commercial permission ends before the new Appointment interval commits:

```text
proposal existence
    ≠ retained entitlement

customer acceptance
    ≠ retained entitlement

capacity protection
    ≠ retained entitlement
```

The final reschedule commitment SHALL revalidate current commercial permission.

Where permission is absent, the original Appointment remains authoritative unless another independently valid operation changes or releases it.

Proposal expiry, capacity release and cleanup SHALL remain possible without requiring the lost entitlement.

---

# 10. Appointment Preparation

GrandRue SHALL define:

```text
appointment/new-commitment-preparation-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

It covers non-committing interpretation and candidate preparation.

It SHALL NOT establish:

```text
Appointment
Appointment revision
capacity ownership
Allocation
TimeProposal capacity protection
reschedule commitment
```

A public customer-facing interaction SHALL NOT expose this exemption as though a new Appointment can actually be completed when the merchant lacks the protected Appointment commercial permission.

---

# 11. Existing Appointment Observation

GrandRue SHALL define:

```text
appointment/existing-commitment-observation-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

It permits otherwise-authorised observation of exact existing Appointment commitments and their legitimate customer/merchant representations.

It does not supply:

```text
Actor Authorisation
CustomerContext access
Exposure
merchant ownership
cross-Merchant access
```

Commercial downgrade SHALL NOT erase the customer's existing Appointment from legitimate operational handling.

---

# 12. Existing Appointment Resolution

GrandRue SHALL define:

```text
appointment/existing-commitment-resolution-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

Within accepted Appointment semantics, this contract may cover:

```text
cancel an existing Appointment

record legitimate check-in / arrival evidence

record Appointment occurrence outcome

correct outcome evidence

release obsolete proposal protection

perform support-resource substitution
that does not alter the customer's
agreed Appointment commitment

complete other owner-classified
commitment-resolution consequences
```

It SHALL NOT cover a reschedule into a new interval.

It SHALL NOT cover creation of another Appointment.

It SHALL NOT convert an existing Appointment into a perpetual right to consume new scheduling capacity.

---

# 13. Scheduling Commercial Boundary

GrandRue SHALL define:

```text
scheduling/appointment-availability-evaluation-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

Scheduling is supporting semantic infrastructure, not a separately saleable standard-plan architecture component.

Its evaluation may participate in:

```text
merchant configuration/preparation

authorised operational inspection

new Appointment candidate calculation

authoritative Appointment revalidation
```

However, where Scheduling participates in a customer-facing attempt to establish new Appointment commitment, successful completion remains contingent on:

```text
appointment/time-commitment-establishment-access@1
```

and its current commercial permission.

Therefore:

```text
Scheduling result available
    ≠
new Appointment commercially permitted
```

No FREE merchant obtains BUSINESS Appointment execution merely because Scheduling itself carries no independent entitlement.

---

# 14. Configuration Remains Independent of Tier

This amendment SHALL NOT introduce:

```text
if plan != BUSINESS
    delete Scheduling configuration
```

or:

```text
if plan != BUSINESS
    remove Booking semantics
```

or:

```text
if plan != BUSINESS
    deactivate Appointment capability
```

Merchant Configuration continues to represent supported business reality independently of subscription tier.

Commercial permission affects use for protected new commitment activity.

This preserves upgrade restoration without reconstructing the merchant's operating model.

---

# 15. Entitlement Loss

When the required protected commercial permission ceases and no other valid grant source supplies it:

```text
new Booking reservation commitment
    → denied

Booking amendment acquiring new scope
    → denied

new Appointment commitment
    → denied

Appointment reschedule to new interval
    → denied
```

But:

```text
existing commitment observation
existing cancellation
existing discharge
existing outcome handling
pure commitment restriction
authorised cleanup
```

remain governed by their no-independent-entitlement resolution contracts.

Historical Booking and Appointment facts SHALL NOT be deleted, rewritten or reclassified because commercial permission changed.

---

# 16. Multiple Grant Sources

Standing plan placement does not determine the final answer by itself.

A current effective protected permission MAY derive from any independently accepted Commercial grant source, including:

```text
initial full-experience trial
paid Merchant Commercial Agreement
commercial remediation
another future accepted grant source
```

Loss of one source SHALL NOT deny access while another independently valid source continues to satisfy the exact entitlement.

---

# 17. Payment and Commercial Independence

This amendment creates no universal rule that a Booking or Appointment requires customer payment.

The following remain distinct:

```text
GrandRue subscription entitlement
        ≠
merchant's price
        ≠
customer Payment Obligation
        ≠
provider payment
```

Commercial subscription permission does not satisfy a customer payment prerequisite, and customer payment does not supply the merchant's GrandRue commercial entitlement.

---

# 18. Failure Semantics

For protected new commitment activity, the system SHALL distinguish at least:

```text
COMMERCIAL_PERMISSION_DENIED

COMMERCIAL_PERMISSION_UNRESOLVED

SEMANTICALLY_INAPPLICABLE

ACTOR_NOT_AUTHORISED

CAPACITY_UNAVAILABLE

POLICY_UNSATISFIED

PAYMENT_REQUIREMENT_UNSATISFIED

CONFLICT

TECHNICAL_FAILURE
```

Unknown or missing commercial binding information SHALL NOT be interpreted as exemption.

A previous successful availability calculation SHALL NOT substitute for current commercial permission.

A commercial denial SHALL NOT be represented as:

```text
no slots available
no rooms available
Booking not configured
Appointment not configured
```

when those statements are not true.

---

# 19. Catalogue Consequences

The future MS-PROT-056 manifest SHALL contain exact Commercial Access Bindings for:

```text
booking/reservation-commitment-establishment-access@1
    +
ESTABLISH_BOOKING_RESERVATION_COMMITMENT

appointment/time-commitment-establishment-access@1
    +
ESTABLISH_APPOINTMENT_TIME_COMMITMENT
```

BUSINESS SHALL grant both resulting canonical Commercial Entitlement identities.

GROWTH SHALL explicitly contain the same identities through the standard catalogue hierarchy.

FREE SHALL contain neither.

The manifest SHALL also record the explicit no-independent-entitlement support/resolution classifications established by this amendment so Commercial does not invent additional paywalls around the included workflows.

---

# 20. Review and Falsification

The proposal survives the following cases.

| Challenge | Required outcome |
|---|---|
| Booking capability remains configured after BUSINESS cancellation | Configuration remains; new Booking commitment is denied |
| Merchant still has existing hotel reservations after downgrade | Existing reservations remain observable and resolvable |
| Existing Friday–Sunday Booking is shortened to Friday–Saturday | Residual restriction may proceed if no new right is acquired |
| Existing Friday–Sunday Booking moves to Saturday–Monday | Protected Booking permission required because new reservation scope is acquired |
| Booked subject changes from Standard to Executive | Protected Booking permission required |
| Merchant cancels an existing Booking after downgrade | Cancellation remains available |
| Merchant records no-show/utilisation after downgrade | Resolution/outcome path remains available |
| FREE merchant has Appointment configured | Configuration survives; no new Appointment commitment is permitted |
| Merchant cancels existing Appointment after downgrade | Cancellation remains available |
| Merchant tries to move an Appointment to a new time after downgrade | Protected Appointment permission required |
| Reschedule proposal was created before entitlement loss | Proposal does not preserve entitlement; final commitment revalidates |
| Existing Appointment needs support-resource substitution without changing customer commitment | Existing-commitment resolution remains possible |
| Scheduling engine can calculate a candidate interval | Candidate calculation alone does not grant Appointment establishment |
| Trial remains active after paid-plan cancellation | Effective entitlement may still be supplied by the trial |
| Customer has already paid merchant | Customer payment does not substitute for GrandRue commercial permission |

No falsification requires plan identity to own Booking, Appointment or Scheduling semantics.

No falsification requires existing commitments to become inaccessible after commercial downgrade.

No falsification justifies a separate standalone Scheduling entitlement.

---

# 21. Alternatives Rejected

A single `BUSINESS_BOOKING_ENABLED` or `BUSINESS_APPOINTMENT_ENABLED` Boolean is rejected because it collapses configuration, commercial permission and existing commitment handling.

A blanket BUSINESS requirement for every Booking/Appointment operation is rejected because it would trap merchants and customers inside commitments after entitlement loss.

A blanket residual exemption for every modification is rejected because rescheduling or changing booked scope can acquire materially new scarce capacity.

A separate standard-plan Scheduling entitlement is rejected because Scheduling is supporting semantic authority, not an independently justified commercial product boundary in the accepted allocation.

Tier-name branching is rejected. Runtime SHALL consume exact Commercial Access Bindings.

---

# 22. Amendment Effect

This amendment closes the Booking / Appointment / Scheduling **owner-classification blocker** discovered during `MS-PROT-056-V17-DQ-001` catalogue completion.

It does not mint the final Commercial Entitlement identities.

It does not publish the complete catalogue.

It does not resolve prices or quantitative allowances.

It does not activate implementation.

`MS-PROT-056-V17-DQ-001` therefore remains OPEN until all remaining owner-classification blockers are resolved and Commercial can publish one complete standard manifest.
