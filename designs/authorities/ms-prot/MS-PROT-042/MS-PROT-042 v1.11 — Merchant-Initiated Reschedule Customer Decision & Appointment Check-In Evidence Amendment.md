# MS-PROT-042 v1.11 — Merchant-Initiated Reschedule Customer Decision & Appointment Check-In Evidence Amendment

**Document ID:** MS-PROT-042  
**Version:** 1.11  
**Status:** **ACCEPTED after complete proposal, conformance review, falsification and manual approval on 9 September 2026**  
**Work package:** `MS-PROT-042-GRP-05`  
**Authority type:** Appointment semantic/design amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-042 through v1.10 within merchant-initiated Appointment rescheduling customer-decision requirements and Appointment arrival/check-in evidence  
**Depends on:** composite MS-PROT-042 through v1.10; composite MS-PROT-043; MS-PROT-053; MS-PROT-054; MS-PROT-057; MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; MS-PROT-065; composite MS-PROT-069; MS-PROT-072; composite MS-PROT-075; composite MS-PROT-086  
**Resolves:** `MS-PROT-042-GRP-05` — customer acceptance for merchant-initiated rescheduling + Appointment arrival/check-in semantics  
**Implementation activation:** NONE

---

# 1. Governing Decision

Main Street SHALL distinguish four materially different customer-participation facts:

```text
Appointment already agreed
        ≠
merchant proposes changing its time

customer accepts proposed change
        ≠
merchant changes Appointment without new acceptance

customer checks in / arrives
        ≠
Appointment interaction occurred
```

This amendment establishes:

1. when merchant-initiated Appointment rescheduling requires a new customer decision;
2. how that customer decision is represented;
3. how the existing Appointment behaves while a proposed change is unresolved; and
4. what authoritative Appointment check-in evidence means.

---

# 2. Feature Admission

## Representation Test — PASS

Without this authority Main Street cannot distinguish:

```text
merchant moved Appointment
with customer's agreement

merchant may move Appointment
under existing agreed policy

merchant merely proposed a move

customer arrived

customer actually received the service
```

These facts have materially different consequences.

## Coordination Test — PASS

The amendment coordinates:

```text
Appointment
Scheduling
CustomerContext
Capacity / Allocation
Merchant policy
Notification
Occurrence Outcome
```

without transferring their ownership.

## Administrative-Compression Test — PASS

The merchant should be able to say:

```text
"Move Sarah to 3pm and ask her first."

"Sarah is here."
```

Main Street absorbs:

```text
proposal lifecycle
customer authority
deadline
capacity protection
concurrency
revision affinity
notification
audit
correction history
```

## ERP-drift — PASS

The design does not create:

```text
waiting-room software
queue management
workflow builder
case management
customer-success lifecycle
generic status engine
```

---

# 3. Existing Appointment Commitment Remains Authoritative

A merchant's desire to change an Appointment does not itself change the agreed Appointment.

Canonical:

```text
Appointment A123
10:00–11:00

merchant wants:
15:00–16:00

        ↓

10:00–11:00
remains authoritative

until a valid
reschedule operation commits
```

No proposal may prematurely release the current Appointment interval.

---

# 4. Merchant-Initiated Rescheduling Has Two Authority Modes

The initial registered customer-decision requirement modes are:

```text
CUSTOMER_ACCEPTANCE_REQUIRED

MERCHANT_AUTHORISED_WITHOUT_NEW_ACCEPTANCE
```

These classify the authority required before a merchant-initiated time change may commit.

They are not Appointment lifecycle states.

---

# 5. Default Is Customer Acceptance Required

Where no applicable accepted Appointment policy authoritatively permits the merchant to alter the existing agreed time without a new customer decision:

```text
CUSTOMER_ACCEPTANCE_REQUIRED
```

applies.

Reason:

> An existing Appointment contains an agreed customer-related interval. Merchant intent alone does not silently replace that agreement.

This is a commitment-integrity default, not a universal legal assertion.

---

# 6. Merchant Policy May Permit Rescheduling Without New Acceptance

An Appointment may instead operate under:

```text
MERCHANT_AUTHORISED_WITHOUT_NEW_ACCEPTANCE
```

where the exact governing Appointment policy already establishes that authority.

Example supported business meaning:

```text
merchant may adjust appointment time
within agreed operating terms
without requesting a new customer decision
```

Main Street SHALL NOT invent that authority from convenience.

---

# 7. Historical Policy Affinity Applies

A merchant cannot obtain unilateral rescheduling authority for an existing Appointment merely by changing policy after the customer commitment was established.

Example:

```text
Appointment A
established under Policy P7

P7:
customer acceptance required

later merchant activates P8:
merchant may move appointments directly
```

A remains governed by its applicable historical policy affinity unless a separately authorised operation validly changes the customer's governing terms.

Therefore:

```text
new policy
≠ retroactive rescheduling authority
```

---

# 8. Direct Merchant Rescheduling

Where:

```text
MERCHANT_AUTHORISED_WITHOUT_NEW_ACCEPTANCE
```

is valid, the merchant MAY invoke the existing Appointment rescheduling operation directly.

The operation still requires:

```text
current Appointment revision
current merchant authority
current applicable policy
current Scheduling evaluation
required capacity
transactional consistency
```

Policy authority does not bypass Scheduling.

---

# 9. Merchant-Initiated Rescheduling Requires Customer Communication

A merchant-initiated reschedule committed without a new customer decision SHALL establish a customer communication responsibility.

Conceptually:

```text
merchant-initiated reschedule commits
        ↓
Appointment owns:
customer must be informed

        ↓
Notification authority
selects permitted fulfilment path
```

Notification failure does not undo the Appointment reschedule.

But Main Street SHALL NOT intentionally perform a silent unilateral change.

---

# 10. Acceptance-Required Change Uses AppointmentRescheduleProposal

Where customer acceptance is required, Main Street SHALL establish an:

**AppointmentRescheduleProposal**

before the Appointment interval changes.

Conceptually:

```text
AppointmentRescheduleProposal
{
    MerchantScope

    Appointment identity

    exact source Appointment revision

    proposed target interval

    Proposal Acceptance Deadline

    protection mode

    applicable policy provenance

    proposal identity

    provenance
}
```

The representation is conceptual rather than a mandated persistence class.

---

# 11. AppointmentRescheduleProposal Is Not the Appointment

Hard distinctions:

```text
AppointmentRescheduleProposal
    ≠ Appointment revision

proposal accepted
    ≠ reschedule committed

proposal target interval
    ≠ customer-owned capacity
```

The existing Appointment remains authoritative while the proposal is unresolved.

---

# 12. Proposal Is Affined to Exact Appointment Revision

A reschedule proposal SHALL bind to the exact current Appointment revision from which the change was proposed.

Example:

```text
A123 revision 4
10:00–11:00

        ↓

proposal R9
move to 15:00–16:00
sourceRevision = 4
```

If revision 4 ceases to govern:

```text
proposal R9
cannot mutate a later revision
```

---

# 13. Proposal Meaning Is Immutable

Once externally issued, material proposal meaning SHALL NOT be silently edited.

Material meaning includes:

```text
source Appointment revision
target interval
customer
deadline
protection mode
applicable policy
```

A changed proposal requires:

```text
new AppointmentRescheduleProposal
```

Historical evidence remains.

---

# 14. Reschedule Proposal Deadline

Every AppointmentRescheduleProposal SHALL have an exact:

**Proposal Acceptance Deadline**

using the accepted v1.10 deadline semantics.

Absent an earlier accepted merchant/policy cutoff:

```text
deadline = proposed target interval start
```

No universal:

```text
10-minute
24-hour
48-hour
```

reschedule-response period is introduced.

---

# 15. Proposal Disposition

The current disposition vocabulary is:

```text
OPEN
ACCEPTED
DECLINED
WITHDRAWN
EXPIRED
SUPERSEDED
```

These describe proposal/customer-decision truth.

They do not describe the Appointment lifecycle.

---

# 16. `SUPERSEDED`

`SUPERSEDED` means:

> The source Appointment revision to which the proposal was affined ceased to be current before the proposal could validly produce the intended reschedule.

Examples:

```text
Appointment independently cancelled

Appointment independently rescheduled

another accepted reschedule proposal committed

authoritative occurrence outcome now prevents ordinary rescheduling
```

A superseded proposal cannot be resurrected.

---

# 17. Customer Acceptance Authority

Customer acceptance may be established through exactly one of the following initial authority classes:

```text
DIRECT_CUSTOMER_ACTION

AUTHORISED_MERCHANT_ATTESTATION_OF_CUSTOMER_DECISION
```

---

# 18. Direct Customer Action

`DIRECT_CUSTOMER_ACTION` requires:

```text
trusted MerchantScope

exact Appointment relationship

appointment / related-customer-appointment

exact RescheduleProposal

trusted customer execution context

current proposal eligibility
```

A general logged-in customer account is insufficient without the exact Appointment relationship.

---

# 19. Merchant Attestation of Customer Decision

An appropriately authorised merchant-side actor MAY record that the customer accepted or declined a proposal where the decision occurred:

```text
by telephone

in person

through another legitimate off-platform interaction
```

The operation SHALL preserve:

```text
actor
decision
proposal identity
decision time
provenance
```

Main Street does not require every micro business to force customers into an account merely to acknowledge a phone conversation.

---

# 20. Conversation Message Is Not Automatic Acceptance

Rejected:

```text
Customer sends:
"yeah that's fine"

        ↓

AI / Conversation system
automatically reschedules Appointment
```

A ConversationMessage is communication evidence.

It SHALL NOT itself become the Appointment customer-decision operation unless a later exact accepted interaction contract grants that authority.

---

# 21. AI Cannot Create Customer Acceptance

AI MAY:

```text
identify that a message may contain acceptance

prepare a merchant action

explain the proposed change
```

AI SHALL NOT:

```text
commit customer acceptance

commit customer decline

infer consent from sentiment

infer agreement from silence
```

---

# 22. Acceptance Is Durable Customer-Decision Evidence

If customer acceptance is validly established:

```text
AppointmentRescheduleProposal = ACCEPTED
```

remains historical truth even if the subsequent reschedule commitment fails.

Canonical:

```text
customer accepted 15:00

        ↓

reschedule confirmation fails

        ↓

proposal remains ACCEPTED

Appointment remains at old interval
```

Customer decision and system execution remain separate facts.

---

# 23. Decline Leaves Appointment Unchanged

If:

```text
proposal = DECLINED
```

then:

```text
existing Appointment
remains unchanged
```

No cancellation or new Appointment is created.

---

# 24. Withdrawal Leaves Appointment Unchanged

If the merchant withdraws an OPEN proposal:

```text
proposal = WITHDRAWN
```

and:

```text
current Appointment remains unchanged
```

---

# 25. Expiry Leaves Appointment Unchanged

If the proposal reaches its acceptance deadline:

```text
proposal = EXPIRED
```

The existing Appointment remains authoritative.

Expiry does not mean:

```text
Appointment cancelled
customer no-show
customer rejected merchant
```

---

# 26. Capacity Protection Is Explicit

A reschedule proposal MAY be:

```text
UNPROTECTED
or
PROTECTED
```

using the bounded v1.10 capacity-protection pattern.

The proposal's target interval is not automatically reserved.

---

# 27. Unprotected Reschedule Proposal

For:

```text
UNPROTECTED
```

the existing Appointment retains its current interval claim.

The proposed target interval remains available to other legitimate commitments.

Customer acceptance therefore causes current Scheduling/capacity revalidation.

---

# 28. Protected Reschedule Proposal

For:

```text
PROTECTED
```

Main Street SHALL establish a bounded:

**Appointment Reschedule Proposal Hold**

for the proposed target capacity.

This means the system may temporarily hold:

```text
current Appointment's existing capacity
+
proposed target capacity
```

at the same time.

This temporary double claim is intentional and bounded because the old Appointment remains a real commitment until the customer decision is resolved.

---

# 29. Reschedule Proposal Hold Is Not Final Allocation

```text
Appointment Reschedule Proposal Hold
    ≠ Appointment Allocation
```

It exists solely to protect the candidate target interval while the customer decides.

Its expiry SHALL equal the proposal's exact acceptance deadline.

---

# 30. One Protected Reschedule Proposal Per Appointment

The initial portfolio permits at most:

```text
one current PROTECTED
AppointmentRescheduleProposal
per Appointment
```

This prevents one Appointment from reserving many alternative intervals simultaneously.

Multiple unprotected alternatives are not automatically prohibited but cannot result in multiple Appointment commitments.

---

# 31. Successful Protected Reschedule Is Atomic

When an accepted protected proposal commits:

```text
current Appointment old interval
        +
target Proposal Hold
        ↓

one atomic consistency boundary
        ↓

release old Appointment claim
+
convert/replace target Hold
with Appointment capacity claim
+
establish new Appointment revision
```

There SHALL be no:

```text
release old time
        ↓
release target Hold
        ↓
attempt target Allocation later
```

race window.

---

# 32. Successful Unprotected Reschedule Is Also Atomic

For an accepted unprotected proposal:

```text
revalidate target
        ↓
atomically
release old claim
+
establish target claim
+
new Appointment revision
```

If the target cannot commit:

```text
old Appointment remains unchanged
```

---

# 33. Failed Confirmation After Acceptance

If:

```text
proposal = ACCEPTED
```

but authoritative rescheduling fails:

```text
Appointment remains on its prior revision
proposal remains ACCEPTED
precise failure is retained
target Hold is safely released where applicable
```

There is no partial time move.

---

# 34. Execution Uncertainty

If Main Street cannot establish whether the reschedule operation committed:

```text
do not blindly release capacity
do not retry as a new reschedule
do not expose contradictory Appointment revisions
```

MS-PROT-069 reconciliation governs uncertainty.

---

# 35. Reschedule Changes Time, Not Arbitrary Terms

This amendment authorises change of the agreed scheduled interval.

It does not silently authorise changing:

```text
service identity

customer

price

location terms

Payment obligation

scope of broader work
```

Material changes owned by other semantics require their applicable authority.

---

# 36. Staff/Resource Substitution Is Not Rescheduling

Changing:

```text
Barber A → Barber B
```

while keeping:

```text
14:00–15:00
```

is not automatically Appointment rescheduling.

That remains in `MS-PROT-042-GRP-04`.

---

# 37. Appointment Check-In

An **Appointment Check-In** means:

> Authoritative evidence that the customer has presented themselves or has been recognised as ready for the customer-related interaction represented by the Appointment.

Examples may include:

```text
customer arrives at salon reception

patient reports at front desk

customer presents for consultation

customer is manually recorded as ready
```

---

# 38. Check-In Is Evidence, Not Lifecycle State

Hard distinction:

```text
Appointment Check-In
    ≠ Appointment confirmed

Appointment Check-In
    ≠ IN_PROGRESS

Appointment Check-In
    ≠ OCCURRED

Appointment Check-In
    ≠ COMPLETED

Appointment Check-In
    ≠ payment

Appointment Check-In
    ≠ worked time
```

No universal Appointment `CHECKED_IN` lifecycle state is required.

---

# 39. Check-In Is Optional

Not every Main Street Appointment requires check-in.

A consultant joining a remote call may not use check-in at all.

A small salon may simply record:

```text
Sarah arrived
```

where operationally useful.

Therefore:

```text
absence of check-in evidence
    ≠ customer did not arrive
    ≠ CUSTOMER_NO_SHOW
```

---

# 40. Appointment Owns Check-In Evidence

Appointment SHALL own the canonical arrival/check-in evidence for its own scheduled customer interaction.

Calendar, Customer Messaging, Workforce and external providers do not independently own Appointment Check-In truth.

---

# 41. AppointmentCheckInEvidence

Conceptually:

```text
AppointmentCheckInEvidence
{
    MerchantScope

    Appointment identity

    exact Appointment revision affinity

    observedAt

    recordedAt

    evidence authority

    actor/provenance

    currentness/supersession
}
```

The structure is conceptual.

---

# 42. `observedAt` and `recordedAt` Are Distinct

Main Street SHALL preserve the distinction between:

```text
observedAt
    when the customer was observed/presented

recordedAt
    when Main Street authoritatively recorded the evidence
```

Example:

```text
customer arrives 13:55

receptionist records it at 14:02
```

Main Street shall not falsify either time merely for convenience.

---

# 43. No Universal Check-In Window

This amendment introduces no universal:

```text
15 minutes early

30 minutes early

10 minutes late
```

check-in window.

Merchant operating policy may constrain role-native check-in actions where genuinely required.

The canonical fact remains what was observed.

---

# 44. Initial Check-In Evidence Authority

The initial production evidence authority is:

```text
AUTHORISED_MERCHANT_ATTESTATION
```

An appropriately authorised worker may record that the customer presented for the Appointment.

Examples:

```text
receptionist
service worker
manager
Controller
```

subject to role authority.

---

# 45. Customer Self Check-In Is Not Activated by This Amendment

A future customer self-check-in path may be admitted only through an exact accepted evidence contract.

This amendment does not assume:

```text
QR code

GPS

Bluetooth

website button

NFC

email link
```

is trustworthy check-in evidence.

No particular technology becomes semantic authority.

---

# 46. Contact Identity Does Not Establish Check-In

Rejected:

```text
same email
same phone number
CustomerAccount login
Conversation participation
```

as sufficient proof that:

```text
the customer has arrived
```

The evidence source must satisfy the accepted check-in authority.

---

# 47. Canonical Check-In Operations

This amendment registers:

```text
appointment.record-check-in
appointment.correct-check-in
```

Both remain Appointment-owned operations.

---

# 48. `appointment.record-check-in`

The operation establishes the first current authoritative check-in evidence for one exact Appointment revision.

Minimum semantics include:

```text
trusted MerchantScope
logical command identity
Appointment identity
expected Appointment revision
observedAt
authorised evidence source
actor/provenance
```

---

# 49. Check-In Requires a Valid Appointment

Ordinary check-in SHALL NOT be established for:

```text
nonexistent Appointment

cancelled Appointment

wrong MerchantScope

stale Appointment revision
```

The operation must fail currentness/eligibility validation.

---

# 50. Check-In Before Occurrence Outcome

Ordinary `appointment.record-check-in` is a pre-outcome operational evidence operation.

Once an authoritative Appointment Occurrence Outcome already exists:

```text
ordinary new check-in mutation
    → not permitted
```

If the historical business truth is wrong, the applicable correction/reconciliation authority must address the inconsistency rather than appending contradictory live-operation state.

---

# 51. Check-In Does Not Prove Occurrence

Example:

```text
customer checks in
        ↓
waits
        ↓
service cannot proceed
```

Possible eventual outcome may still require authoritative determination.

Therefore:

```text
Check-In
    ≠ OCCURRED
```

---

# 52. Check-In Does Not Determine No-Show

Likewise:

```text
no Check-In
    ≠ CUSTOMER_NO_SHOW
```

A merchant may never use formal check-in but still perform the service normally.

No-show remains governed by v1.9 outcome evidence.

---

# 53. Check-In May Become Qualified Outcome Evidence Later

An exact accepted Appointment Outcome Evidence Contract MAY consume check-in evidence in the future.

But this amendment establishes no automatic mapping:

```text
Check-In → OCCURRED

No Check-In → CUSTOMER_NO_SHOW
```

Initially, neither implication exists.

---

# 54. Check-In Correction Preserves History

A receptionist may check in the wrong Appointment or record the wrong observed time.

Correction SHALL preserve the former evidence.

Conceptually:

```text
CheckIn Revision 1
observedAt 14:00

        ↓ correction

CheckIn Revision 2
observedAt 13:55
supersedes Revision 1
```

or:

```text
incorrect check-in
        ↓
authorised void correction
```

Historical evidence remains auditable.

---

# 55. One Current Effective Check-In

For one exact Appointment revision:

```text
zero or one current effective
Appointment Check-In
```

may exist.

Historical superseded evidence may coexist.

---

# 56. Check-In and Rescheduling

If an Appointment is rescheduled after check-in:

```text
old check-in
remains historical evidence
affined to old Appointment revision
```

It SHALL NOT automatically become current check-in evidence for the new scheduled interval.

This prevents stale presence evidence from being reused after a material time change.

---

# 57. Check-In Does Not Accept a Reschedule

Hard invariant:

```text
customer checked in
    ≠
customer accepted merchant's proposed new interval
```

Even where both actions occur at reception, the customer decision and arrival evidence remain separate facts.

---

# 58. Reschedule Acceptance Does Not Check Customer In

Likewise:

```text
customer accepts 15:00 reschedule
    ≠
customer has arrived for 15:00 Appointment
```

The two facts SHALL NOT be collapsed for UI convenience.

---

# 59. Check-In Does Not Modify Scheduling Capacity

Recording check-in does not:

```text
release remaining interval
extend Appointment
move Appointment
consume extra capacity
open another slot
```

Scheduling and Allocation remain authoritative.

---

# 60. Check-In Does Not Start Workforce Time

Check-in does not establish:

```text
staff started work

worker attendance

worked duration

compensation
```

Workforce remains independently authoritative.

---

# 61. No Generic Waiting-Room Semantics

Appointment Check-In does not create:

```text
queue number

estimated waiting time

service order

waiting-room priority

automatic next-customer assignment
```

Those would require separate feature admission if target-business evidence later justifies them.

---

# 62. Notification Boundary

A merchant-initiated reschedule may require customer communication.

Notifications remain downstream delivery authority.

Check-in itself does not universally require customer Notification.

---

# 63. Customer Messaging Boundary

Conversation may contain:

```text
"Can you move me to 3?"

"Yes, that's fine."

"I'm outside."
```

Those Messages remain communication facts.

They do not automatically create:

```text
reschedule acceptance

Appointment reschedule

check-in
```

without an exact Appointment-owned operation/evidence contract.

---

# 64. Audit

The following are business-significant and SHALL produce applicable audit evidence:

```text
merchant-initiated reschedule proposal

customer accept/decline decision

merchant attestation of customer decision

direct unilateral reschedule

check-in

check-in correction
```

Audit remains evidence authority rather than Appointment owner.

---

# 65. Merchant UX

Ordinary merchant interactions SHOULD be business-native.

Example:

```text
Sarah — 10:00

Move appointment?
[Choose new time]

Ask Sarah first?
Yes
```

Then:

```text
Sarah accepted 3:00 PM
Appointment moved ✓
```

For arrival:

```text
Sarah — 3:00 PM

[Arrived]
```

No merchant needs to understand:

```text
Appointment revision affinity
Proposal Hold
optimistic concurrency
customer-decision provenance
```

---

# 66. Policy-Driven Administrative Compression

Where the merchant has already established:

```text
always ask customers before moving appointments
```

Main Street SHALL apply that policy automatically.

Where an accepted policy legitimately permits unilateral change:

```text
merchant does not repeatedly configure
the decision requirement per Appointment
```

Main Street translates merchant policy into the correct flow.

---

# 67. Falsification — Merchant Proposes Move

Scenario:

```text
A123 = 10:00

merchant proposes 15:00
customer has not answered
```

Expected:

```text
A123 remains 10:00
```

No old capacity release.

**PASS**

---

# 68. Falsification — Customer Declines

Scenario:

```text
proposal 15:00
customer declines
```

Expected:

```text
proposal = DECLINED
A123 remains 10:00
target Hold released if one existed
```

**PASS**

---

# 69. Falsification — Proposal Expires

Scenario:

Customer does not answer before deadline.

Expected:

```text
proposal = EXPIRED
existing Appointment unchanged
target Hold expires automatically
```

**PASS**

---

# 70. Falsification — Unprotected Target Is Taken

Scenario:

```text
merchant proposes 15:00 UNPROTECTED
customer accepts
another valid Appointment already took 15:00
```

Expected:

```text
proposal = ACCEPTED
reschedule commit fails
old Appointment remains unchanged
```

**PASS**

---

# 71. Falsification — Protected Acceptance

Scenario:

```text
merchant proposes protected 15:00
customer accepts
```

Expected atomic result:

```text
old interval released
target Hold converted/replaced
new Appointment revision established
```

No release/reacquire race.

**PASS**

---

# 72. Falsification — Existing Appointment Changes First

Scenario:

Proposal is based on Appointment revision 4.

Another authorised operation reschedules Appointment to revision 5.

Expected:

```text
old proposal cannot mutate revision 5
proposal becomes non-applicable / SUPERSEDED
```

**PASS**

---

# 73. Falsification — Customer Says “Yes” in Chat

Expected:

```text
Conversation message exists
no automatic Appointment change
```

Merchant may use an authorised customer-decision attestation operation.

**PASS**

---

# 74. Falsification — Telephone Acceptance

Scenario:

Receptionist calls customer.

Customer verbally agrees.

Expected:

```text
authorised receptionist records
customer acceptance
+
provenance
```

No customer account required.

**PASS**

---

# 75. Falsification — No Applicable Reschedule Policy

Scenario:

Merchant attempts to move a confirmed Appointment directly.

No policy authorises unilateral change.

Expected:

```text
direct reschedule denied
customer-acceptance proposal required
```

**PASS**

---

# 76. Falsification — Existing Policy Permits Merchant Change

Scenario:

Appointment was established under policy explicitly permitting merchant rescheduling without a new decision.

Expected:

```text
merchant may reschedule
subject to Scheduling/capacity validation
+
customer communication responsibility
```

**PASS**

---

# 77. Falsification — Later Policy Cannot Rewrite Existing Agreement

Scenario:

Appointment was created when customer acceptance was required.

Merchant later changes policy to permit unilateral changes.

Expected:

```text
existing Appointment does not
retroactively acquire unilateral-reschedule authority
```

**PASS**

---

# 78. Falsification — Customer Arrives

Scenario:

Receptionist records:

```text
Sarah arrived 13:55
```

Expected:

```text
Appointment Check-In evidence exists
```

but:

```text
Appointment Occurrence Outcome
remains unset
```

**PASS**

---

# 79. Falsification — No Check-In Record

Scenario:

Haircut takes place normally.

Salon never uses check-in.

Expected:

```text
absence of check-in
does not create CUSTOMER_NO_SHOW
```

**PASS**

---

# 80. Falsification — Checked In but Service Fails

Scenario:

Customer arrives.

Merchant-side equipment fails before service begins.

Expected:

```text
check-in remains true
outcome determined separately
```

No automatic `OCCURRED`.

**PASS**

---

# 81. Falsification — Wrong Customer Checked In

Expected:

```text
authorised correction
creates superseding evidence
old evidence retained historically
```

**PASS**

---

# 82. Falsification — Check-In Followed by Reschedule

Scenario:

Customer checks in under revision 4.

Appointment is later validly rescheduled to revision 5.

Expected:

```text
revision-4 check-in remains historical
no automatic current check-in on revision 5
```

**PASS**

---

# 83. Falsification — Customer Website Button

Scenario:

Customer presses a generic:

```text
"I'm here"
```

button without an accepted self-check-in evidence contract.

Expected:

```text
no authoritative Appointment Check-In
```

**PASS**

---

# 84. Falsification — Arrival Mistaken for Acceptance

Scenario:

Customer arrives for original 10:00 Appointment while a 15:00 proposal exists.

Expected:

```text
check-in ≠ acceptance of 15:00
```

**PASS**

---

# 85. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

The design introduces internal complexity for:

```text
proposal revisions
customer-decision evidence
historical policy affinity
target capacity protection
atomic rescheduling
check-in provenance
correction
```

because those mechanisms prevent false business commitments.

Merchant-facing operation remains:

```text
Move appointment

Ask customer first

Customer accepted

Customer arrived
```

Main Street absorbs the software complexity.

---

# 86. Ordinary-Staff Training Test

A receptionist already understands:

```text
customer agreed

customer declined

customer arrived
```

They do not need to learn Main Street architecture.

**PASS**

---

# 87. Anti-ERP Review

Rejected expansion:

```text
generic waiting-room management
queue engine
customer lifecycle stages
case-management status
workflow designer
automatic staff dispatch
generic attendance system
```

The amendment remains limited to Appointment commitment change and Appointment-specific arrival evidence.

**PASS**

---

# 88. Hard Invariants

```text
INV-042-V111-001
Merchant intent alone does not change an existing agreed Appointment interval.

INV-042-V111-002
Absent accepted authority to reschedule without new customer acceptance, CUSTOMER_ACCEPTANCE_REQUIRED applies.

INV-042-V111-003
A later merchant policy change cannot retroactively create unilateral rescheduling authority for an existing Appointment.

INV-042-V111-004
An AppointmentRescheduleProposal is not an Appointment revision.

INV-042-V111-005
The existing Appointment remains authoritative until successful rescheduling commits.

INV-042-V111-006
Every reschedule proposal is affined to the exact source Appointment revision.

INV-042-V111-007
Customer acceptance is durable decision evidence even if reschedule execution later fails.

INV-042-V111-008
Conversation content and AI inference do not create customer acceptance authority.

INV-042-V111-009
Merchant attestation may represent a legitimate telephone/in-person customer decision where actor authority and provenance are retained.

INV-042-V111-010
Protected target capacity does not release the existing Appointment interval while the proposal remains unresolved.

INV-042-V111-011
Successful Hold-to-rescheduled-Appointment transition contains no release/reacquire race window.

INV-042-V111-012
Failed rescheduling leaves the prior Appointment authoritative.

INV-042-V111-013
A stale proposal cannot mutate a later Appointment revision.

INV-042-V111-014
Merchant-initiated unilateral rescheduling requires customer communication responsibility.

INV-042-V111-015
Appointment Check-In is evidence, not Appointment lifecycle state.

INV-042-V111-016
Absence of Check-In does not establish CUSTOMER_NO_SHOW.

INV-042-V111-017
Check-In does not establish OCCURRED.

INV-042-V111-018
Check-In is affined to the exact Appointment revision observed.

INV-042-V111-019
Check-In correction preserves historical evidence.

INV-042-V111-020
Check-In does not alter Scheduling capacity or Workforce time.

INV-042-V111-021
Check-In does not satisfy reschedule customer acceptance.

INV-042-V111-022
Reschedule acceptance does not establish Check-In.

INV-042-V111-023
Customer self-check-in requires a separately accepted exact evidence contract before becoming authoritative.

INV-042-V111-024
No generic waiting-room or queue semantics are introduced.

INV-042-V111-025
AI cannot create rescheduling acceptance or check-in authority.
```

---

# 89. Rejected Alternatives

Rejected:

```text
merchant may always move customer Appointment

merchant may never move Appointment without customer acceptance

current merchant policy always governs old Appointments retroactively

proposal immediately moves Appointment

proposal releases old interval before acceptance

customer chat message automatically reschedules

AI interprets "sure" and commits reschedule

customer account alone grants reschedule authority

check-in means Appointment started

check-in means Appointment occurred

no check-in means no-show

check-in changes staff worked time

generic QR/GPS presence automatically equals check-in

check-in automatically carries across reschedule revisions

arrival automatically accepts proposed new time

reschedule acceptance automatically checks customer in

generic waiting-room state machine
```

---

# 90. Resolved Work-Package Outcome

`MS-PROT-042-GRP-05` is resolved by this amendment for the initial portfolio.

Resolved:

```text
customer acceptance requirements
for merchant-initiated rescheduling

+
Appointment arrival/check-in semantics
```

No retained semantic DQ remains inside GRP-05.

---

# 91. Implementation-Rules Impact

Acceptance SHALL NOT activate implementation.

A conforming future implementation must preserve:

```text
exact Appointment revision affinity
historical policy affinity
customer relationship authority
idempotency
proposal deadline
proposal disposition consistency
capacity concurrency
atomic reschedule
customer-decision provenance
check-in evidence provenance
check-in correction history
auditability
no AI authority
```

Exact persistence, transport, UI and provider implementation remain downstream.

---

# 92. Next Group Consequence

With acceptance and formalisation:

```text
MS-PROT-042-GRP-05
    → RESOLVED
```

and the grouped execution overlay advances to:

```text
MS-PROT-042-GRP-04

Appointment Assignment Continuity

staff substitution
+
resource reassignment
```

The global dependency graph remains paused until the approved grouped cleanup is completed or the remaining groups are correctly reclassified.

---

# 93. Recommendation

**RECOMMENDATION: ACCEPT — ACCEPTED BY MANUAL APPROVAL ON 9 SEPTEMBER 2026**

This is the smallest coherent model that preserves:

```text
customer agreement
merchant policy freedom
Appointment commitment integrity
Scheduling/capacity safety
ordinary phone/in-person merchant operation
arrival evidence
outcome epistemic discipline
```

without building:

```text
generic workflow software
waiting-room ERP
customer lifecycle management
```

The proposal passed:

```text
Feature Admission
Fundamental Vision Conformance
ownership review
ambiguity review
cross-capability review
anti-ERP review
falsification
```

**MS-PROT-042 v1.11 is ACCEPTED.**
