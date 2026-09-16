# MS-PROT-042 v1.14 — Booking Discharge, Utilisation Outcome & Commitment Amendment Model

**Document ID:** MS-PROT-042  
**Version:** 1.14  
**Status:** **ACCEPTED by manual approval on 9 September 2026**  
**Work package:** `MS-PROT-042-GRP-01`  
**Authority type:** Booking semantic/design amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-042 through v1.13 within Booking natural discharge, Booking utilisation outcome, Booking commitment revision and booked-subject/reservation-scope modification  
**Depends on:** composite MS-PROT-020; MS-PROT-025; composite MS-PROT-042 through v1.13; composite MS-PROT-043; MS-PROT-053; MS-PROT-054; MS-PROT-055; MS-PROT-057; MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; composite MS-PROT-069; MS-PROT-072; composite MS-PROT-075  
**Resolves:** `MS-PROT-042-GRP-01` — Booking natural discharge/utilisation outcome + booked-subject/capability-specific modification semantics  
**Implementation activation:** NONE

---

# 1. Governing Decision

Main Street SHALL keep the following Booking facts separate:

```text
Booking reservation commitment
        ≠
Booking natural discharge
        ≠
Booking utilisation outcome
        ≠
Booked Subject
        ≠
current allocated Resource
        ≠
Payment / Refund
        ≠
foreign operational fulfilment
```

The authoritative Booking history SHALL be composed from:

```text
Booking commitment
+
Booking commitment revisions where authorised
+
Booking cancellation where applicable
+
Booking-owned discharge
+
Booking Utilisation Outcome where established
```

Main Street SHALL NOT introduce a universal Booking lifecycle state machine merely to combine these facts.

---

# 2. Existing Narrow Commitment State Survives

MS-PROT-042 v1.4 remains authoritative for:

```text
IN_FORCE

RELEASED
```

These mean only:

```text
IN_FORCE
    the Booking reservation commitment
    remains operative

RELEASED
    the Booking reservation commitment
    has been authoritatively discharged
```

They SHALL NOT be expanded into generic meanings such as:

```text
CHECKED_IN
IN_USE
COMPLETED
NO_SHOW
FULFILLED
RETURNED
```

---

# 3. Feature Admission

## Representation Test — PASS

Main Street must distinguish situations such as:

```text
hotel reservation was used

customer never arrived

customer used only part of reserved scope

merchant could not honour reservation

reservation right naturally ended

room/resource allocation changed internally

customer changed reservation dates

merchant proposed another booked subject
```

without collapsing them into cancellation or Payment state.

## Coordination Test — PASS

Correct Booking change/outcome semantics coordinate:

```text
Booking
Resource / Allocation
CustomerContext
Payment
Notification
Merchant policy
Audit
Analytics
```

without transferring their authority.

## Administrative-Compression Test — PASS

The merchant should be able to say:

```text
"Move the booking to Saturday."

"Upgrade them to Executive."

"They didn't turn up."

"The stay happened."

"Room 104 is unavailable — use 108."
```

Main Street handles:

```text
commitment revisions
capacity
currentness
policy
customer agreement
atomic claim replacement
discharge
outcome evidence
```

## ERP-drift — PASS

No:

```text
hotel PMS lifecycle
equipment-rental suite
facility-management engine
generic asset-custody system
event-management platform
workflow builder
```

is introduced.

---

# 4. Booked Subject Remains Customer Commitment Truth

The **Booked Subject** remains:

> what the customer and merchant reserved.

Examples:

```text
Standard Room

Executive Room

Meeting Room A

Excavator 7

attendance capacity
```

Booked Subject SHALL remain distinct from the concrete Resource/capacity currently protecting the reservation.

---

# 5. Current Allocation Is Not Booked Subject

Example:

```text
Booked Subject:
    Standard Room

Current operational assignment:
    Room 104
```

Changing:

```text
Room 104 → Room 108
```

does not change the Booking if both Resources satisfy the exact same committed Booked Subject.

Canonical:

```text
Booked Subject
    customer commitment

Allocation
    current operational capacity fulfilment
```

---

# 6. Exact Resource Booking

Different case:

```text
Booked Subject:
    Meeting Room A
```

If exact Room A itself was reserved:

```text
Room A → Room B
```

is not an internal Allocation substitution.

It changes the Booked Subject and therefore requires Booking commitment amendment authority.

---

# 7. Booking Commitment Revision

Booking commitment meaning SHALL support immutable revision.

Conceptually:

```text
BookingCommitmentRevision
{
    MerchantScope

    Booking identity

    revision identity

    CustomerContext affinity

    Booked Subject

    Reservation Scope

    quantity/capacity meaning where applicable

    governing policy provenance

    customer-decision provenance

    supersedes revision?
}
```

The representation is conceptual.

---

# 8. One Current Booking Commitment Revision

For one Booking:

```text
exactly one current
Booking Commitment Revision
```

governs at a time while the Booking exists.

Historical superseded revisions remain authoritative historical evidence.

Modification SHALL NOT overwrite the former commitment in place.

---

# 9. Initial Booking Amendment Dimensions

The initial generic amendment dimensions are:

```text
RESERVATION_SCOPE_CHANGE

QUANTITY_CHANGE

BOOKED_SUBJECT_CHANGE
```

They are closed semantic families.

A particular booked-subject capability may support none, some or all.

No generic JSON-patch-style Booking modification authority is introduced.

---

# 10. Capability-Owned Modification Applicability

The specific Booking semantics associated with the Booked Subject SHALL determine which amendment dimensions are materially applicable.

Example:

```text
accommodation category reservation
    may support date-range change
    and category change

exact equipment reservation
    may support reservation-window change
    but not arbitrary quantity change
```

Main Street SHALL NOT infer supported changes merely from database fields being present.

---

# 11. Booking Amendment Contract

A versioned **Booking Amendment Contract** SHALL identify the registered amendment semantics applicable to a Booked Subject family.

It may establish:

```text
permitted amendment dimensions

required validation

customer-decision requirement class

capacity consequences

policy applicability

material-term presentation requirements
```

The contract is platform-owned semantic infrastructure.

It is not merchant-authored executable logic.

---

# 12. Allocation-Only Reassignment Is Not Booking Modification

Where the Booked Subject remains unchanged and another Resource independently satisfies the same existing reservation:

```text
Allocation reassignment
    ≠ booking.modify
```

Resource/Allocation owns that concrete capacity transition.

Booking may coordinate the requirement.

It does not needlessly revise customer commitment truth.

---

# 13. Booking Modification Requires Current Revision Affinity

Every Booking amendment operation SHALL identify the exact Booking commitment revision expected to govern.

Example:

```text
Booking B100
revision 4
Friday → Sunday

merchant screen still shows revision 4

another actor changes B100 to revision 5

first actor submits change
```

Expected:

```text
stale amendment fails
```

It SHALL NOT mutate revision 5 accidentally.

---

# 14. Canonical Booking Modification Operation

The semantic operation family is:

```text
booking.modify
```

It means:

> authoritatively establish a new Booking Commitment Revision under one registered Booking Amendment Contract after all applicable current authority, policy, customer-decision and capacity invariants succeed.

---

# 15. Existing Reservation Does Not Authorise New Scope

Existing:

```text
Friday → Sunday
```

does not grant authority for:

```text
Saturday → Monday
```

merely because the same Booking exists.

The target reservation must be authoritatively revalidated.

---

# 16. Customer-Initiated Modification

A customer-side Booking modification SHALL require:

```text
trusted MerchantScope

booking / related-customer-booking

exact Booking identity

current Booking revision

trusted customer execution context

applicable policy permission

registered amendment dimension

current capacity/availability validation
```

CustomerAccount authentication alone is insufficient.

---

# 17. Merchant-Initiated Material Modification

A merchant changing a customer-material Booking term SHALL use one of:

```text
CUSTOMER_ACCEPTANCE_REQUIRED

MERCHANT_AUTHORISED_WITHOUT_NEW_ACCEPTANCE
```

using the same commitment-integrity principle already accepted for Appointment change.

Absent exact applicable historical policy authority:

```text
CUSTOMER_ACCEPTANCE_REQUIRED
```

is the default.

---

# 18. Later Policy Cannot Rewrite Existing Booking Terms

Example:

```text
Booking B100
created under Policy P4

P4:
merchant changes require customer acceptance

later:
Policy P5 permits merchant substitution
```

P5 does not retroactively grant authority over B100.

Historical policy affinity from v1.3 remains authoritative.

---

# 19. Merchant-Initiated Change Proposal

Where customer acceptance is required, Main Street SHALL establish a bounded:

**BookingChangeProposal**

before the Booking commitment changes.

Conceptually:

```text
BookingChangeProposal
{
    MerchantScope

    Booking identity

    expected current
    Booking Commitment Revision

    proposed amendment

    proposal deadline

    applicable policy provenance

    customer-decision provenance

    capacity-protection affinity
    where applicable
}
```

---

# 20. BookingChangeProposal Is Not Booking Mutation

```text
proposal exists
    ≠ Booking changed

customer accepts
    ≠ Booking changed

candidate capacity held
    ≠ Booking changed
```

The current Booking remains authoritative until `booking.modify` commits.

---

# 21. Accepted Change May Still Fail

If:

```text
customer accepts new reservation scope
```

but current capacity is no longer available:

```text
proposal remains ACCEPTED

Booking remains unchanged

modification fails
```

Customer decision history SHALL NOT be rewritten to pretend acceptance never occurred.

---

# 22. Existing Capacity Must Not Be Released Prematurely

Where the old Booking remains valid during amendment:

```text
old Allocation / claim
```

SHALL remain protected until the new commitment can be established safely.

Rejected:

```text
release old booking capacity

        ↓

attempt new reservation

        ↓

new reservation fails
```

where this unnecessarily destroys the customer's existing valid commitment.

---

# 23. Atomic Capacity Replacement

Where Booking modification requires replacing reservation capacity:

```text
old claim
+
validated target claim
+
new Booking revision
```

SHALL transition within the applicable invariant-preserving consistency boundary.

There SHALL be no externally observable release/reacquire race.

---

# 24. Quantity Increase

Example:

```text
quantity 1 → 2
```

requires authoritative validation and claim of the additional unit.

The existing unit is not released merely because the increase fails.

Expected failure:

```text
Booking remains quantity 1
```

---

# 25. Quantity Reduction

Example:

```text
quantity 3 → 2
```

may release the exact excess capacity only when the Booking commitment revision establishing quantity 2 commits.

Availability thereafter is recalculated from all remaining authoritative constraints.

---

# 26. Booked Subject Change

A Booked Subject change requires the new subject to satisfy:

```text
registered Booking semantics

current applicability

current availability

capacity/allocation requirements

applicable merchant policy

customer-decision authority

commercial/protection requirements
```

The former subject remains committed until successful replacement.

---

# 27. No Generic “Equivalent Subject” Guessing

Main Street SHALL NOT infer:

```text
Room A ≈ Room B

Vehicle A ≈ Vehicle B

Executive ≈ Standard
```

from labels, price similarity, AI judgement or merchant category.

Operational equivalence must arise from accepted Resource/Booking semantics.

---

# 28. AI Boundary for Modification

AI MAY:

```text
interpret:
"move them to Saturday"

identify candidate amendment dimension

explain consequences

prepare a BookingChangeProposal
```

AI SHALL NOT:

```text
invent customer acceptance

invent equivalence

invent capacity

override policy

commit booking.modify
```

---

# 29. Booking Natural Discharge Contract

The precise way an `IN_FORCE` Booking reservation commitment naturally becomes `RELEASED` SHALL be registered through a:

**Booking Natural Discharge Contract**

owned by Booking semantics.

This prevents generic runtime code from inferring discharge from timestamps.

---

# 30. Initial Natural Discharge Modes

The initial closed modes are:

```text
RESERVATION_SCOPE_END

OWNER_QUALIFIED_TERMINAL_EVIDENCE
```

No universal Booking mode is assumed.

---

# 31. `RESERVATION_SCOPE_END`

This mode applies only where the accepted Booking semantics establish:

> the Booking-owned reservation right itself ceases when the exact reservation scope reaches its authoritative end boundary.

Example candidates include correctly registered:

```text
time-bounded room entitlement

time-bounded facility use right

time-bounded reserved attendance capacity
```

where no Booking-owned reservation right survives beyond that boundary.

---

# 32. Explicit Resolution of v1.4 Time Rule

For a Booking using:

```text
RESERVATION_SCOPE_END
```

the accepted semantics now explicitly establish:

```text
authoritative reservation-scope end reached
        ↓
Booking reservation commitment
        RELEASED
```

This does **not** contradict v1.4.

v1.4 prohibits:

```text
all Bookings
reservationEnd < now
        ↓
RELEASED
```

without an accepted Booking-owned rule.

v1.14 supplies that rule only where the exact Booking Natural Discharge Contract selects `RESERVATION_SCOPE_END`.

---

# 33. `OWNER_QUALIFIED_TERMINAL_EVIDENCE`

This mode applies where reservation discharge cannot safely be determined from reservation-scope end alone.

Booking remains `IN_FORCE` until accepted owner-qualified evidence establishes that its reservation right has been discharged.

Possible future evidence may include:

```text
registered handback fact

registered entitlement termination

other exact Booking-owned terminal evidence
```

This amendment does not manufacture generic custody or return semantics.

---

# 34. Timer Does Not Create Discharge Authority

For `RESERVATION_SCOPE_END`:

```text
scope end
```

is the semantic boundary.

A background timer may:

```text
wake processing
update projection
clean physical state
```

but does not make the discharge true.

Delayed background execution SHALL NOT keep an already-ended reservation commitment semantically `IN_FORCE`.

---

# 35. Foreign Obligations May Survive Booking Discharge

After Booking becomes `RELEASED`, there may still be:

```text
Payment work

Refund work

damage dispute

asset-custody issue

merchant/customer communication

provider reconciliation
```

Those facts do not reactivate the Booking reservation commitment.

This preserves v1.4.

---

# 36. Discharge Is Not Utilisation

Hard distinction:

```text
reservation right ended
    ≠ customer used it
```

A Booking may naturally become `RELEASED` while the utilisation outcome remains:

```text
unknown / not established
```

Example:

```text
reserved room period ends

        ↓

Booking reservation commitment RELEASED

        ↓

Main Street has insufficient evidence
whether customer actually stayed
```

That is legitimate.

---

# 37. Booking Utilisation Outcome

A **Booking Utilisation Outcome** answers:

> What authoritative evidence establishes about whether the customer materially took up the reservation opportunity represented by this Booking?

It is Booking-owned outcome evidence.

It does not redefine reservation commitment state.

---

# 38. Initial Utilisation Outcome Vocabulary

The initial closed vocabulary is:

```text
UTILISED

PARTIALLY_UTILISED

CUSTOMER_NON_UTILISATION

MERCHANT_SIDE_NON_HONOUR
```

No merchant-authored custom outcome names are permitted.

---

# 39. `UTILISED`

`UTILISED` means:

> sufficient authoritative evidence establishes that the customer materially took up the reserved subject/capacity/entitlement to the extent required by the applicable Booking outcome semantics.

It does not universally establish:

```text
customer satisfaction

full physical consumption

Payment

damage-free return

broader service completion

review eligibility
```

---

# 40. `PARTIALLY_UTILISED`

`PARTIALLY_UTILISED` means:

> sufficient authoritative evidence establishes that the reservation opportunity was materially taken up, but not to the extent required for `UTILISED`.

It does not establish a percentage.

It does not calculate:

```text
refund
credit
charge
compensation
```

---

# 41. `CUSTOMER_NON_UTILISATION`

`CUSTOMER_NON_UTILISATION` means:

> the merchant-side reservation opportunity remained available as required, but sufficient authoritative evidence establishes that the customer did not materially take it up.

Examples may include a validly evidenced reservation no-show.

It is not:

```text
Booking cancellation

Payment failure

customer misconduct

automatic non-refundability
```

---

# 42. `MERCHANT_SIDE_NON_HONOUR`

`MERCHANT_SIDE_NON_HONOUR` means:

> sufficient authoritative evidence establishes that the merchant side could not or did not make the booked reservation opportunity available to the extent required for the customer to utilise it.

It does not determine:

```text
legal liability

refund amount

compensation

staff fault

provider fault
```

---

# 43. Cancellation Is Not Utilisation Outcome

A Booking cancelled before its utilisation opportunity SHALL NOT receive an ordinary:

```text
CUSTOMER_NON_UTILISATION
```

merely because the customer later did not use it.

Cancellation and utilisation truth remain separate.

---

# 44. Time Passage Is Not Utilisation Evidence

Even where time passage establishes natural discharge under an exact Discharge Contract:

```text
Booking RELEASED
```

does not determine:

```text
UTILISED
CUSTOMER_NON_UTILISATION
PARTIALLY_UTILISED
MERCHANT_SIDE_NON_HONOUR
```

Separate outcome evidence is required.

---

# 45. Payment Is Not Utilisation Evidence

Rejected:

```text
payment success
        ↓
UTILISED
```

and:

```text
refund
        ↓
not utilised
```

Payment and Booking outcome remain separate.

---

# 46. Allocation Is Not Utilisation Evidence

A concrete Resource having remained reserved does not prove the customer used the reservation.

Likewise:

```text
resource released at reservation end
```

does not prove no-show.

---

# 47. AI Cannot Establish Booking Utilisation Outcome

AI MAY:

```text
summarise evidence

identify Booking needing outcome review

suggest likely classification for human review
```

AI SHALL NOT:

```text
infer no-show from silence

infer utilisation from payment

infer utilisation from clock passage

infer merchant failure from complaint sentiment

commit outcome
```

---

# 48. Outcome Evidence Classes

Initial evidence authority classes are:

```text
AUTHORISED_HUMAN_ATTESTATION

REGISTERED_OWNER_QUALIFIED_EVIDENCE
```

Exactly as with Appointment outcome, provider signals or foreign-capability evidence require an accepted exact evidence contract before establishing Booking outcome truth.

---

# 49. Booking Outcome Operations

The semantic operation families are:

```text
booking.record-utilisation-outcome

booking.correct-utilisation-outcome
```

They remain Booking-owned.

---

# 50. Exact Booking Revision Affinity

Outcome evidence SHALL bind to the exact Booking commitment revision whose reservation opportunity it describes.

If the Booking was materially modified:

```text
Friday → Sunday
        ↓
Saturday → Monday
```

stale evidence concerning the earlier commitment SHALL NOT be silently applied to the later revision.

---

# 51. One Current Utilisation Outcome

For one Booking:

```text
zero or one current
Booking Utilisation Outcome
```

may govern the relevant terminal reservation experience.

Historical superseded outcome revisions may coexist.

Two contradictory independently-current outcomes are prohibited.

---

# 52. Outcome Correction Preserves History

Example:

```text
CUSTOMER_NON_UTILISATION
recorded accidentally

        ↓

merchant proves customer stayed

        ↓

booking.correct-utilisation-outcome

        ↓

current:
UTILISED

historical:
CUSTOMER_NON_UTILISATION
superseded
```

The prior evidence remains auditable.

---

# 53. Correcting Outcome Does Not Reopen Reservation

If:

```text
Booking commitment = RELEASED
```

correcting:

```text
CUSTOMER_NON_UTILISATION
→ UTILISED
```

does not make Booking:

```text
IN_FORCE
```

Reservation commitment and utilisation outcome remain separate authorities.

---

# 54. Correcting Outcome Does Not Reverse Foreign Consequences

If an incorrect outcome previously caused an independently committed:

```text
Payment consequence

Notification

Analytics observation
```

correcting the Booking outcome does not silently rewrite those foreign capabilities.

Applicable reconciliation/remediation authority must handle them.

---

# 55. No Universal Hotel Check-In/Check-Out State

This amendment SHALL NOT introduce:

```text
CHECKED_IN
CHECKED_OUT
OCCUPIED
RETURNED
COLLECTED
```

as generic Booking lifecycle states.

A future exact booked-subject evidence contract may use capability-specific facts such as:

```text
accommodation arrival evidence

equipment handover evidence

resource-return evidence
```

to establish Booking utilisation or discharge where justified.

The generic Booking model does not own those domain-specific facts.

---

# 56. No Universal `COMPLETED` Booking State

The broad historical unresolved question:

```text
exact Booking lifecycle
```

is resolved by rejecting a universal terminal lifecycle machine.

Authoritative Booking truth composes:

```text
commitment revision

+

IN_FORCE / RELEASED

+

cancellation where applicable

+

Utilisation Outcome where established
```

A UI may derive:

```text
Completed
Ended
Used
No-show
```

when an exact projection can truthfully do so.

Those words do not become canonical lifecycle states.

---

# 57. Customer Communication on Merchant-Initiated Material Change

Where the merchant validly changes a customer-material Booking term without a new customer decision under historical policy authority:

```text
Booking owns:
customer must be informed
```

Notification owns delivery.

Main Street SHALL NOT intentionally perform silent material merchant-side Booking change.

---

# 58. Booking Modification Does Not Automatically Change Payment

Changing:

```text
Standard → Executive

Friday–Sunday → Saturday–Monday
```

may have monetary consequences.

But:

```text
booking.modify
    ≠ Payment mutation
```

Booking/policy may establish a commercial consequence requirement.

MS-PROT-055 owns Payment Obligation/Payment execution/refund truth.

---

# 59. Booking Modification Does Not Automatically Rewrite Existing Provider Effects

If an external provider or downstream process has already acted on the old commitment:

```text
new Booking revision
```

does not pretend that prior provider effect never occurred.

Where required, separately governed reconciliation or compensating work follows.

---

# 60. Booking Modification and Capability Deactivation

If Booking is disabled for new activity while an existing Booking remains `IN_FORCE`:

```text
existing Booking amendment/cancellation
may remain available
through residual-management authority
where required
```

according to accepted configuration/residual-surface rules.

Disabling new Booking creation does not strand active commitments.

---

# 61. Booking Historical Identity Survives Modification

A valid Booking modification preserves:

```text
Booking identity
```

while producing a new commitment revision.

A materially distinct new reservation intent MAY instead require:

```text
new Booking identity
```

under its exact Booking Amendment Contract.

The system SHALL NOT preserve identity merely to simplify UI when the business intent is actually a new independent reservation.

---

# 62. Modification Versus Cancel-and-Rebook

Main Street SHALL NOT universally model modification as:

```text
cancel old Booking
+
create new Booking
```

because that fabricates cancellation history and may create avoidable:

```text
capacity races

Payment consequences

Notification consequences

policy consequences
```

A registered Booking modification is its own authoritative operation.

---

# 63. New Independent Booking Remains Permitted

If customer/merchant truly intends:

```text
cancel B100

and independently create B101
```

that remains valid.

The existence of `booking.modify` does not force every changed intention to retain Booking identity.

---

# 64. Concurrency

Concurrent Booking modifications SHALL preserve exact current-revision authority.

Example:

```text
Actor A:
Friday→Sunday
to
Saturday→Monday

Actor B:
Friday→Sunday
to
Thursday→Saturday
```

Only an operation based on the still-current expected revision may commit.

The stale competing operation fails.

---

# 65. Idempotency

Booking modification/outcome operations SHALL comply with MS-PROT-059.

```text
same logical command
+
same intent
    → stable prior result

same logical command
+
different target/outcome
    → conflict
```

Network retry does not multiply:

```text
Booking revisions

Allocation transitions

Outcome revisions
```

---

# 66. Execution Uncertainty

Where Main Street cannot establish whether a capacity-sensitive Booking modification committed:

```text
do not blindly retry as a new modification

do not release both old and new capacity

do not expose contradictory current revisions
```

MS-PROT-069 reconciliation governs.

---

# 67. Audit

Business-significant Booking operations SHALL produce applicable audit evidence including:

```text
Booking modification

merchant/customer decision

Booked Subject change

Reservation Scope change

quantity change

natural discharge where materialised

utilisation outcome

outcome correction
```

Audit remains evidence authority rather than Booking owner.

---

# 68. Merchant Experience

Typical operations SHOULD remain business-native.

Example:

```text
Booking
Standard Room
Fri 18 Sep → Sun 20 Sep

[Change booking]
```

Merchant chooses:

```text
New dates:
Sat 19 Sep → Mon 21 Sep
```

Main Street handles capacity and policy.

Another example:

```text
Room 104 unavailable.

Room 108 can satisfy this
Standard Room reservation.

[Use Room 108]
```

This SHALL NOT appear as a customer Booking amendment when Booked Subject remains `Standard Room`.

---

# 69. Outcome Merchant Experience

Merchant may see:

```text
What happened with this booking?

[It was used]
[Partly used]
[Customer didn't use it]
[We couldn't honour it]
```

only where explicit outcome recording is operationally useful.

Main Street SHOULD prefer deterministic accepted evidence and surface unresolved outcomes as exceptions rather than forcing routine status maintenance.

---

# 70. Falsification — Accommodation Natural End

Scenario:

```text
Booking:
Standard Room
Friday → Sunday

Natural Discharge Contract:
RESERVATION_SCOPE_END
```

Sunday authoritative reservation end passes.

Expected:

```text
Booking commitment = RELEASED
```

without requiring merchant to press:

```text
Complete booking
```

Utilisation outcome remains independently unknown until evidenced.

**PASS**

---

# 71. Falsification — Equipment Requires Explicit Terminal Evidence

Scenario:

Booking semantics use:

```text
OWNER_QUALIFIED_TERMINAL_EVIDENCE
```

Reservation timestamp passes but required Booking-owned terminal evidence is absent.

Expected:

```text
no automatic Booking discharge
```

**PASS**

---

# 72. Falsification — Customer No-Show

Scenario:

Booked opportunity remained available.

Authoritative evidence establishes customer never took it up.

Expected:

```text
Booking Utilisation Outcome =
CUSTOMER_NON_UTILISATION
```

No automatic cancellation or Payment consequence.

**PASS**

---

# 73. Falsification — Merchant Cannot Honour Booking

Scenario:

Merchant's reserved subject cannot be made available.

Expected:

```text
MERCHANT_SIDE_NON_HONOUR
```

without automatically determining:

```text
refund
liability
compensation
```

**PASS**

---

# 74. Falsification — Internal Room Swap

Scenario:

```text
Booked Subject:
Standard Room

Room 104 → Room 108

both satisfy Standard Room
```

Expected:

```text
Resource/Allocation reassignment

no Booking Commitment Revision required
```

**PASS**

---

# 75. Falsification — Exact Room Swap

Scenario:

```text
Booked Subject:
Meeting Room A

merchant proposes Meeting Room B
```

Expected:

```text
BOOKED_SUBJECT_CHANGE

Booking modification authority required
```

**PASS**

---

# 76. Falsification — Date Modification Failure

Scenario:

Customer currently has valid Friday→Sunday Booking.

Customer requests Saturday→Monday.

Monday capacity unavailable.

Expected:

```text
modification fails

old Booking remains Friday→Sunday
```

**PASS**

---

# 77. Falsification — Concurrent Final Capacity

Two Booking modifications contend for final capacity.

Expected:

```text
capacity invariant preserved
only valid coexistence commits
```

**PASS**

---

# 78. Falsification — Policy Changed Later

Booking was established when merchant changes required customer acceptance.

Merchant later changes policy.

Expected:

```text
existing Booking retains
historical policy affinity
```

**PASS**

---

# 79. Falsification — Payment Already Succeeded

Booking is later modified.

Expected:

```text
Payment truth remains historical
Booking modification may produce
separate commercial consequence
```

No payment history rewrite.

**PASS**

---

# 80. Falsification — Utilisation Correction

Incorrect `CUSTOMER_NON_UTILISATION` is corrected to `UTILISED`.

Expected:

```text
new outcome revision

old outcome retained historically

Booking remains RELEASED
```

**PASS**

---

# 81. Falsification — Time Alone Without Contract

A Booking has an end timestamp but its exact Natural Discharge Contract is not `RESERVATION_SCOPE_END`.

Expected:

```text
timestamp passage alone
does not manufacture RELEASED
```

**PASS**

---

# 82. Falsification — Low-Software-Capacity Merchant

Merchant says:

```text
"Move them to Saturday instead."
```

Expected Main Street experience:

```text
show valid new options
show material customer/commercial consequence
obtain required approval
commit safely
```

Merchant does not administer:

```text
commitment revisions

capacity replacement transactions

policy version affinity

reconciliation
```

**PASS**

---

# 83. Cross-Authority Review

The proposal preserves:

```text
Booking
    owns reservation commitment,
    revisions, discharge and utilisation outcome

Resource / Allocation
    owns concrete capacity

CustomerContext
    owns customer relationship

Payment
    owns money/refund truth

Notification
    owns delivery

Merchant Policy
    owns merchant operating intent
    within registered semantics

Audit
    owns audit evidence

AI
    remains non-authoritative
```

**PASS**

---

# 84. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

The design adds internal complexity for:

```text
commitment revision

natural-discharge qualification

outcome provenance

capacity-safe modification

customer decision

historical policy

correction

concurrency
```

because those mechanisms prevent false reservation truth.

Merchant-visible complexity remains:

```text
Change booking

Change dates

Change reserved option

What happened?
```

Main Street absorbs the architecture.

---

# 85. Anti-ERP Review

The proposal does not introduce:

```text
hotel PMS

room-status lifecycle

rental return-management system

asset custody

event-management lifecycle

course management

facility operations suite

generic booking workflow builder
```

Specific operational domains may later contribute exact evidence to Booking without becoming generic Booking states.

**PASS**

---

# 86. Hard Invariants

```text
INV-042-V114-001
Booking commitment truth remains distinct from Booking Utilisation Outcome.

INV-042-V114-002
IN_FORCE / RELEASED remain narrow reservation-commitment meanings.

INV-042-V114-003
Time passage cannot universally discharge Booking.

INV-042-V114-004
RESERVATION_SCOPE_END discharges Booking only where the exact Booking Natural Discharge Contract selects that mode.

INV-042-V114-005
Booking discharge does not establish utilisation outcome.

INV-042-V114-006
Booking utilisation outcome does not establish Payment/refund truth.

INV-042-V114-007
The initial Booking Utilisation Outcome vocabulary is exactly UTILISED, PARTIALLY_UTILISED, CUSTOMER_NON_UTILISATION and MERCHANT_SIDE_NON_HONOUR.

INV-042-V114-008
Cancellation is distinct from customer non-utilisation.

INV-042-V114-009
Booked Subject remains distinct from current Resource/Allocation.

INV-042-V114-010
Changing concrete Allocation within the same Booked Subject does not by itself modify Booking.

INV-042-V114-011
Changing an exact customer-promised Resource requires Booked Subject change authority.

INV-042-V114-012
Booking modification preserves immutable revision history.

INV-042-V114-013
A stale expected Booking revision cannot mutate a newer Booking commitment.

INV-042-V114-014
Existing reservation capacity is not prematurely released during replacement where it remains valid.

INV-042-V114-015
Capacity-sensitive Booking amendment must preserve an atomic replacement invariant.

INV-042-V114-016
Failed modification preserves the old Booking where that commitment remains independently valid.

INV-042-V114-017
Merchant-initiated material Booking change requires customer acceptance absent exact historical policy authority permitting otherwise.

INV-042-V114-018
Later merchant policy cannot retroactively change existing Booking modification authority.

INV-042-V114-019
AI cannot establish customer acceptance, subject equivalence, utilisation outcome or Booking mutation authority.

INV-042-V114-020
Booking modification does not automatically mutate Payment.

INV-042-V114-021
Booking utilisation correction preserves historical evidence.

INV-042-V114-022
Correcting outcome does not restore RELEASED Booking to IN_FORCE.

INV-042-V114-023
No universal CHECKED_IN/CHECKED_OUT/RETURNED/COMPLETED Booking lifecycle is introduced.

INV-042-V114-024
Capability-specific operational evidence may establish Booking truth only through an accepted exact owner-qualified contract.

INV-042-V114-025
Booking modification is not universally modelled as cancel-and-rebook.
```

---

# 87. Rejected Alternatives

Rejected:

```text
reservation end always means Booking complete

reservation end never permits natural discharge

one universal Booking lifecycle

CHECKED_IN / CHECKED_OUT for all Bookings

RETURNED for all Bookings

COMPLETED Booking state

Payment success means Booking used

no payment means Booking unused

Allocation means utilisation

all Resource swaps modify Booking

no Resource swap ever modifies Booking

cancel-and-rebook for every modification

release old capacity before validating replacement

merchant may always alter existing Booking

AI decides equivalent booked subjects

mutable Booking fields without revision history
```

---

# 88. Resolved Work-Package Outcome

`MS-PROT-042-GRP-01` is resolved.

The initial Booking portfolio has resolved:

```text
Booking natural discharge

Booking utilisation outcome

exact Booking lifecycle question

Booked Subject versus Resource assignment

reservation-scope modification

quantity modification

Booked Subject modification

customer authority for material merchant change

capacity-safe amendment
```

No retained GRP-01 semantic DQ remains.

---

# 89. Remaining Grouped Queue

After GRP-01, the only grouped item retained from the v1.9 cleanup is:

```text
MS-PROT-042-GRP-06
Capacity-Demand Exceptions

waiting-list
+
overbooking
```

GRP-06 is explicitly:

```text
REQUIRES FEATURE ADMISSION
```

It SHALL NOT be assumed to belong to MS-PROT-042 merely because the original historical deferred list mentioned it.

Its owner and product admission must be re-established under DESIGN-RULES before any authority is proposed.

---

# 90. Implementation-Rules Impact

Acceptance SHALL NOT activate implementation.

A future conforming implementation must preserve at minimum:

```text
Booking commitment revision affinity

Booked Subject / Allocation separation

Natural Discharge Contract

authoritative time where applicable

outcome provenance

outcome correction

historical policy affinity

customer-decision authority

capacity-safe atomic modification

idempotency

concurrency

uncertainty reconciliation

auditability

no AI semantic authority
```

Exact schema, API, database transactions and UI remain downstream.

---

# 91. Recommendation

**RECOMMENDATION: ACCEPT**

The proposal closes the remaining generic Booking lifecycle/modification gap without building business-type-specific Booking software.

It preserves the core model:

```text
Booking
    reserves something

Resource / Allocation
    protects that reservation

Discharge
    determines whether the reservation right remains operative

Utilisation Outcome
    records what happened to the reservation opportunity

Modification
    changes the customer commitment only through explicit authority
```

It passes:

```text
Feature Admission

Fundamental Vision Conformance

ownership review

ambiguity review

capacity/concurrency review

cross-capability review

anti-ERP review

falsification
```

**Implementation activation: NONE.**
