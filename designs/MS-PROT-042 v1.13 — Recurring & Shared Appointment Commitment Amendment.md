# MS-PROT-042 v1.13 — Recurring & Shared Appointment Commitment Amendment

**Document ID:** MS-PROT-042  
**Version:** 1.13  
**Status:** **ACCEPTED after complete proposal, conformance review, falsification and manual approval on 9 September 2026**  
**Approved:** Manual approval of the complete proposed authority on 9 September 2026  
**Work package:** `MS-PROT-042-GRP-03`  
**Authority type:** Appointment semantic/design amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-042 through v1.12 within recurring Appointment semantics, shared/multi-customer Appointment semantics, participant-specific commitment/outcome truth and recurring-occurrence establishment  
**Depends on:** composite MS-PROT-042 through v1.12; composite MS-PROT-043; MS-PROT-050; MS-PROT-053; MS-PROT-054; MS-PROT-057; MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; MS-PROT-065; composite MS-PROT-069; MS-PROT-072; composite MS-PROT-075; composite MS-PROT-086  
**Resolves:** `MS-PROT-042-GRP-03` — recurring Appointment + group/multi-customer Appointment semantics  
**Implementation activation:** NONE

---

# 1. Governing Decision

Main Street SHALL distinguish:

```text
one Appointment
    ≠
a recurring Appointment series

recurrence rule
    ≠
future Appointment commitment

shared Appointment
    ≠
several unrelated Appointments at the same time

Appointment occurrence outcome
    ≠
individual participant attendance outcome

customer related to shared Appointment
    ≠
customer authorised to observe or control other participants

series change
    ≠
silent mutation of already-committed Appointments
```

A recurring arrangement SHALL therefore use an Appointment-owned **Appointment Series** whose recurrence semantics can authorise future Appointment establishment.

Every actual scheduled interaction remains an independently authoritative **Appointment**.

A shared Appointment SHALL remain one Appointment commitment with zero or more explicitly represented **Appointment Participations** relating exact CustomerContexts to that Appointment.

---

# 2. Why This Authority Is Required

The accepted corpus already establishes that:

```text
Appointment
    = agreed customer-related service/interaction interval

appointment.confirm
    requires current Scheduling revalidation

CustomerContext
    = merchant-scoped durable customer relationship

Appointment Occurrence Outcome
    = what happened to the scheduled interaction

Appointment Support Requirement
    = what supporting capacity the Appointment requires
```

It does not yet determine:

```text
weekly recurring appointments

daily repeating visits

recurring classes or consultations

one shared session involving several customers

one participant withdrawing while others remain

individual participant no-show inside an otherwise successful group session

changing one occurrence versus changing a repeating arrangement

how future recurring occurrences become real Appointments

how recurrence survives retries/outages without duplicating Appointments
```

Those decisions materially affect commitment ownership, capacity, customer access, occurrence truth and administrative burden.

---

# 3. Feature Admission

## 3.1 Representation Test — PASS

Target merchants materially operate patterns such as:

```text
weekly therapy session

weekly tutoring

daily care visit

weekly barber/grooming slot

fitness or training class

group consultation

recurring professional appointment
```

Representing each as unrelated manually-created Appointments loses real business meaning and creates avoidable merchant work.

## 3.2 Coordination Test — PASS

Correct recurrence/shared-participant semantics must coordinate:

```text
Appointment
Scheduling
CustomerContext
Resource / Allocation
Workforce
Notification
Payment where separately applicable
Occurrence Outcome
Merchant Attention
```

without transferring their ownership.

## 3.3 Administrative-Compression Test — PASS

A merchant SHOULD be able to express business language such as:

```text
"Book her every Wednesday at 10."

"Run this class every Monday."

"Add James to Friday's session."

"Sarah can't attend this one."

"Stop her future sessions after October."
```

Main Street should absorb:

```text
series identity
recurrence calculation
timezone handling
currentness
duplicate prevention
participant relationships
capacity checks
future occurrence creation
exceptions
audit
```

## 3.4 ERP-Drift Test — PASS

This amendment does not create:

```text
course management
membership management
club administration
event ticketing
learning management
generic roster software
generic workflow automation
subscription billing
class curriculum
waiting-list management
```

It adds only the minimum semantics required to represent repeated and shared Appointment commitments.

---

# 4. Appointment Series

An **Appointment Series** is:

> An Appointment-owned merchant-scoped Operational Object representing an authorised repeating customer-related scheduling arrangement from which individual Appointment commitments may be established.

An Appointment Series has independent durable identity.

Conceptually:

```text
AppointmentSeries
{
    MerchantScope

    seriesIdentity

    scheduled operation / Offering context

    recurrence pattern

    local-time / timezone semantics

    applicable participant defaults

    applicable Appointment Support Requirement template

    effective range

    customer-decision provenance

    governing policy provenance

    revision history
}
```

The exact persistence representation is downstream.

---

# 5. Appointment Series Is Not an Appointment

Hard distinction:

```text
Appointment Series
    ≠ Appointment

recurrence candidate
    ≠ Appointment

future expected occurrence
    ≠ committed Appointment

series exists
    ≠ future capacity reserved
```

An Appointment exists only after the ordinary authoritative Appointment establishment path succeeds.

Therefore:

> **An Appointment Series does not manufacture future Appointment commitment truth merely because a recurrence calculation says an occurrence should exist.**

---

# 6. Every Materialised Occurrence Is an Independent Appointment

Every successfully established recurring occurrence SHALL have its own:

```text
Appointment identity
Appointment revision/currentness
agreed interval
customer relationships
Support Requirement
Resource/Allocation consequences where applicable
Scheduling evidence
outcome evidence
cancellation/rescheduling history
```

The Appointment SHALL retain provenance identifying:

```text
source Appointment Series
source Series revision
series occurrence identity
```

but remains authoritative independently after commitment.

---

# 7. Appointment Series Occurrence Identity

Each logical recurring position SHALL have a stable:

**Series Occurrence Identity**

within one Appointment Series.

Conceptually:

```text
SeriesIdentity
+
logical occurrence ordinal
    ↓
Series Occurrence Identity
```

Once a Series Occurrence Identity has produced an Appointment:

```text
that occurrence is consumed
```

Cancellation, no-show, refund, rescheduling or other later Appointment change SHALL NOT make the same Series Occurrence Identity eligible to manufacture a replacement Appointment.

Hard invariant:

> **One Series Occurrence Identity may establish at most one Appointment identity.**

---

# 8. Clock Passage Does Not Create Recurring Appointments

Rejected:

```text
Wednesday 10:00 arrives
        ↓
create Appointment
```

Time may wake durable work under MS-PROT-065.

Time does not itself create commitment authority.

Canonical:

```text
authoritative Appointment Series
        +
eligible Series Occurrence
        +
durable progression
        ↓
current Scheduling evaluation
        +
required capacity claim
        +
appointment.confirm semantics
        ↓
Appointment
```

---

# 9. Recurring Materialisation Must Reuse Appointment Authority

Recurring Appointment establishment SHALL NOT have a weaker commitment path than ordinary Appointment establishment.

Before each recurring occurrence commits, Main Street SHALL establish:

```text
current Series revision applicability
current Series Occurrence eligibility
current Scheduling evaluation = SCHEDULABLE
current Appointment Support Requirement
required Resource/Allocation capacity
current merchant/customer authority
exact active semantic/configuration affinity
```

If any invariant-required part cannot be established:

```text
no Appointment is created
```

---

# 10. Recurring Customer Agreement Is Standing Authority Only Within Its Exact Scope

An Appointment Series MAY establish standing customer-decision authority for future occurrences that remain exactly within the accepted Series meaning.

Initial customer-decision authority classes are:

```text
DIRECT_CUSTOMER_ACTION

AUTHORISED_MERCHANT_ATTESTATION_OF_CUSTOMER_DECISION
```

Merchant attestation permits ordinary micro-business realities such as:

```text
customer agreed by telephone
customer agreed in person
customer agreed through legitimate off-platform communication
```

The attestation SHALL preserve provenance.

---

# 11. Series Authority Is Bounded

Series authority applies only to the exact governing Series revision.

It SHALL NOT authorise arbitrary changes to:

```text
service / Offering identity

material duration

recurrence cadence

material scheduled-time meaning

customer participant set

location terms

customer-agreed exact Resource requirement

commercial terms

other separately governed business facts
```

A materially different repeating arrangement requires an authorised Series revision or a new Appointment Series as applicable.

---

# 12. Appointment Series Revision

Appointment Series meaning SHALL be revisioned immutably.

A revision MAY change applicable future recurrence semantics.

It SHALL NOT rewrite historical Series revisions.

Material meaning includes:

```text
recurrence pattern

local start-time meaning

duration

scheduled operation / Offering

default participant relationships

effective range

Support Requirement template

governing customer-decision/policy provenance
```

---

# 13. Series Revision Never Silently Rewrites Existing Appointments

Hard invariant:

> **Once an Appointment has been established from a Series Occurrence, later Appointment Series revision does not alter that Appointment.**

Therefore:

```text
change repeating schedule
        ↓

uncommitted future occurrence semantics
may change

already committed Appointments
remain authoritative
```

Any already-committed Appointment requiring change must use the existing Appointment mutation authority.

This includes v1.11 customer-decision requirements for merchant-initiated rescheduling.

---

# 14. “This Occurrence” Versus “Future Occurrences”

Main Street SHALL preserve the business distinction between:

```text
THIS OCCURRENCE
```

and:

```text
FUTURE UNCOMMITTED OCCURRENCES
```

### This occurrence

Where an Appointment already exists:

```text
operate on that exact Appointment
```

using existing cancellation, rescheduling, participant or other Appointment authority.

### Future uncommitted occurrences

A valid Appointment Series revision MAY alter subsequent occurrence generation.

It does not mutate existing Appointment commitments.

---

# 15. Already-Committed Future Appointments Are Not Series Configuration

If Main Street has already committed several future Appointments from a Series:

```text
Series revision
    ≠
bulk rewrite authority over those Appointments
```

Main Street MAY provide a compressed merchant interaction such as:

```text
Change future sessions
```

but internally it must distinguish:

```text
change Series for uncommitted future occurrences
+
coordinate separately authorised operations
for already-committed Appointments
```

No UI convenience may erase those authority boundaries.

---

# 16. Initial Recurrence Pattern Portfolio

The initial recurring Appointment portfolio SHALL use registered, deterministic, local-calendar recurrence patterns.

Initial pattern families are:

```text
DAILY_LOCAL@1

WEEKLY_LOCAL@1

MONTHLY_DAY_OF_MONTH_LOCAL@1
```

All require:

```text
IANA time zone

local start time

duration

effective start date

positive interval

termination:
    occurrence count
    OR local end date
    OR OPEN_ENDED
```

`WEEKLY_LOCAL@1` additionally requires one or more weekdays.

`MONTHLY_DAY_OF_MONTH_LOCAL@1` requires a calendar day `1..31`.

Where a requested day does not exist in a particular month:

```text
that month produces no occurrence
```

Main Street SHALL NOT silently move it to the final day of the month.

More complex rules such as:

```text
last weekday of month
third Tuesday
custom RRULE import
irregular custom calendars
```

require later registered recurrence-pattern authority.

---

# 17. Local Civil Time Governs Recurrence Meaning

Recurring business schedules are expressed in merchant/customer local civil-time meaning.

Example:

```text
Every Wednesday at 10:00 Europe/London
```

SHALL remain:

```text
10:00 local
```

across ordinary daylight-saving offset changes.

Rejected:

```text
fixed UTC interval
        ↓
appointment becomes 09:00 or 11:00 local
```

merely because UTC arithmetic was convenient.

---

# 18. DST and Temporally Ambiguous Occurrences

If an occurrence's local date/time under the governing timezone rules:

```text
does not map to a real instant
```

or:

```text
maps to multiple instants
```

and no exact accepted resolution rule already removes that ambiguity:

```text
the occurrence is TEMPORALLY UNRESOLVED
```

It SHALL NOT be silently shifted or guessed.

Main Street SHALL surface the exception for resolution rather than requiring the merchant to understand timezone mechanics during normal setup.

---

# 19. Open-Ended Series Do Not Create Infinite Commitments

An Appointment Series MAY be open-ended.

Open-ended means:

> the recurring arrangement remains in force until validly ended.

It does not mean:

```text
infinite Appointments exist

infinite capacity is reserved

infinite Payment Obligations exist
```

Individual Appointments are established progressively.

The exact future materialisation horizon is downstream operational/configuration policy and SHALL NOT redefine Series business meaning.

---

# 20. Failed Recurring Occurrence Materialisation

If one recurring occurrence cannot establish an Appointment because Scheduling is:

```text
NOT_SCHEDULABLE
```

or:

```text
UNRESOLVED
```

or required capacity cannot commit:

```text
no Appointment is fabricated
```

The Appointment Series itself remains in force unless separately ended or revised.

Main Street SHOULD surface the unresolved occurrence as an exception requiring attention.

Failure of one occurrence does not automatically cancel the entire Series.

---

# 21. No Historical Backfill

A system outage or delayed scheduler SHALL NOT create a historical Appointment after its intended interaction start merely to make recurrence bookkeeping complete.

Rejected:

```text
planned Wednesday 10:00 occurrence

system recovers Thursday

        ↓

create Appointment dated yesterday
```

unless an independent authorised historical-reconstruction authority explicitly establishes that commitment actually existed.

A missed materialisation remains an exception/evidence condition.

---

# 22. Ending an Appointment Series

Ending a Series SHALL prevent creation of later uncommitted occurrences after the effective termination boundary.

It SHALL NOT automatically:

```text
cancel already committed Appointments

erase earlier occurrences

erase participant history

rewrite Appointment outcomes
```

If existing future Appointments must be cancelled, Main Street SHALL coordinate the applicable Appointment cancellation operations separately.

---

# 23. Shared Appointment

A **Shared Appointment** is:

> One Appointment commitment whose scheduled customer-related interaction legitimately involves multiple represented CustomerContexts.

Example:

```text
Yoga class
Friday 18:00

Appointment A500
    participant Sarah
    participant James
    participant Mohammed
```

This is one Appointment.

It is not:

```text
three duplicate Appointments
at the same time
```

---

# 24. Appointment Participation

An **Appointment Participation** is the Appointment-owned durable relationship establishing that one exact CustomerContext participates in one exact Appointment.

Conceptually:

```text
AppointmentParticipation
{
    participationIdentity

    MerchantScope

    Appointment identity

    CustomerContext identity

    participation revision/currentness

    provenance
}
```

Hard distinction:

```text
Appointment Participation
    ≠ CustomerContext identity

Appointment Participation
    ≠ Appointment Occurrence Outcome

Appointment Participation
    ≠ Resource Allocation
```

---

# 25. Shared Appointment Does Not Create Customer-to-Customer Identity

Customers participating in the same Appointment do not thereby gain a relationship to one another.

Rejected:

```text
same shared Appointment
        ↓
participants may see each other's
CustomerContext/contact information
```

The existing:

```text
appointment / related-customer-appointment
```

requirement SHALL succeed independently for each legitimately related CustomerContext.

It does not expose another participant's private information.

---

# 26. Participant Access Is Self-Scoped

A customer's relationship to a shared Appointment establishes eligibility concerning:

```text
that customer's own participation
+
shared Appointment information independently exposable to that customer
```

It SHALL NOT by itself grant authority to:

```text
view other participant identities

view other participant contact details

view other participant attendance outcomes

withdraw another participant

cancel the shared Appointment

reschedule the shared Appointment

accept changes for another participant
```

Generic group-member visibility is not authorised.

---

# 27. Adding a Participant

Adding a participant to an existing Appointment SHALL require:

```text
current Appointment revision

exact CustomerContext

applicable actor/customer authority

current participant-capacity validation

required Resource/Allocation capacity

duplicate-participation prevention
```

Where participant capacity is invariant-required:

```text
capacity claim
+
Appointment Participation
```

SHALL commit atomically.

---

# 28. Participant Capacity Remains Owner-Qualified

Appointment owns:

```text
who is committed to participate
```

It does not steal ownership of:

```text
room capacity
seat capacity
staff capacity
Resource capacity
Allocation truth
```

Those remain with their accepted owners.

An Offering or Resource may therefore establish an applicable participant-capacity constraint without becoming Appointment participant authority.

---

# 29. One CustomerContext Cannot Accidentally Occupy Multiple Seats

Initial portfolio rule:

```text
one CustomerContext
    ≤
one current Appointment Participation
per Appointment
```

Repeated transport requests or duplicate UI submissions SHALL converge to the existing participation rather than multiplying capacity.

Unnamed guest quantities and delegated party-size semantics are outside this amendment.

---

# 30. Participant Withdrawal Is Not Appointment Cancellation

A participant may cease participating without cancelling the shared Appointment.

Canonical:

```text
Sarah withdraws
from class Appointment A500

        ↓

Sarah participation released

James participation remains
Mohammed participation remains

Appointment A500 remains
```

Applicable participant-specific capacity SHALL be released safely.

---

# 31. One Participant Cannot Cancel the Shared Appointment by Relationship Alone

A participant's relationship to the Appointment does not make that participant owner of the entire shared commitment.

A customer-side operation may therefore withdraw:

```text
their own participation
```

where permitted.

It SHALL NOT automatically invoke:

```text
appointment.cancel
```

for the entire shared Appointment.

Whole-Appointment cancellation remains governed by its accepted Appointment authority.

---

# 32. Appointment Occurrence Outcome Remains Shared-Interaction Truth

MS-PROT-042 v1.9 remains authoritative.

For a shared Appointment:

**Appointment Occurrence Outcome** describes what happened to the scheduled shared interaction.

Example:

```text
class held
3 of 5 customers attended
```

may produce:

```text
Appointment outcome:
    OCCURRED
```

The absence of two participants does not make the whole Appointment:

```text
CUSTOMER_NO_SHOW
```

---

# 33. Participant Attendance Outcome

This amendment establishes **Appointment Participant Attendance Outcome**.

Initial closed vocabulary:

```text
ATTENDED

PARTIAL_ATTENDANCE

NO_SHOW
```

These concern one exact active Appointment Participation.

They do not redefine the overall Appointment Occurrence Outcome.

---

# 34. `ATTENDED`

`ATTENDED` means:

> Sufficient authoritative evidence establishes that this Appointment participant materially participated in the scheduled interaction.

It does not establish:

```text
customer satisfaction

payment

full broader service fulfilment

exact time present

worker time

review eligibility
```

---

# 35. `PARTIAL_ATTENDANCE`

`PARTIAL_ATTENDANCE` means:

> Sufficient authoritative evidence establishes that this participant materially participated, but not to the extent required for `ATTENDED`.

It does not establish a percentage.

---

# 36. Participant `NO_SHOW`

Participant `NO_SHOW` means:

> The participant remained expected for the Appointment, but sufficient authoritative evidence establishes that the participant did not participate sufficiently for their participation to occur.

It is not:

```text
whole Appointment cancellation

whole Appointment CUSTOMER_NO_SHOW

payment default

misconduct

automatic financial consequence
```

---

# 37. Withdrawal Is Not Participant No-Show

If a participation was validly withdrawn before the relevant interaction:

```text
that participant SHALL NOT later
receive ordinary NO_SHOW
for that withdrawn participation
```

Cancellation/withdrawal truth and attendance outcome remain separate.

---

# 38. Shared Appointment Outcome Consistency

The following consistency rules apply.

### At least one participant materially participates

The overall Appointment MAY be:

```text
OCCURRED
or
PARTIAL_OCCURRENCE
```

according to v1.9 evidence.

Individual non-attendees may independently be `NO_SHOW`.

### No expected participant materially participates

Where merchant-side interaction remained available and sufficient evidence exists:

```text
Appointment
    may be CUSTOMER_NO_SHOW
```

with applicable individual `NO_SHOW` evidence.

### Merchant side cannot provide the interaction

Where:

```text
Appointment outcome =
MERCHANT_SIDE_NON_OCCURRENCE
```

Main Street SHALL NOT use participant `NO_SHOW` merely to blame customers for an interaction that merchant-side evidence establishes could not occur.

---

# 39. Participant Outcome Evidence

Initial participant attendance evidence classes are:

```text
AUTHORISED_HUMAN_ATTESTATION

REGISTERED_OWNER_QUALIFIED_EVIDENCE
```

An appropriately authorised merchant-side actor MAY record participant attendance.

AI SHALL NOT establish attendance/no-show authority.

Customer presence signals, payment, location data, check-in, Conversation messages or provider data do not become attendance truth unless an exact accepted evidence contract grants that meaning.

---

# 40. Check-In Is Not Attendance

Existing Appointment Check-In semantics remain unchanged.

For a shared Appointment:

```text
participant checked in
    ≠ ATTENDED

participant did not check in
    ≠ NO_SHOW
```

Check-In may become evidence under a future exact participant-outcome evidence contract.

It is not sufficient by itself.

---

# 41. Participant Outcome Correction

Participant Attendance Outcome SHALL support append-only correction equivalent to the existing Appointment outcome correction pattern.

For one current Appointment Participation:

```text
zero or one current
Participant Attendance Outcome
```

may govern.

Historical superseded revisions remain available for audit.

No contradictory independently-current participant outcomes are permitted.

---

# 42. Recurring Shared Appointment

The two models compose directly.

Example:

```text
Appointment Series S20
Weekly Friday class

default participants:
Sarah
James
Mohammed
        ↓

Occurrence 1
Appointment A1
3 participations

Occurrence 2
Appointment A2
3 participations
```

The Series does not become one giant Appointment.

Each occurrence remains independently:

```text
schedulable
committed
capacity-bound
participant-bound
cancelable
outcome-bearing
```

---

# 43. Series Participant Defaults Are Not Existing Appointment Mutation Authority

An Appointment Series MAY carry a default participant set for future occurrence establishment.

Changing that default:

```text
affects eligible uncommitted future occurrences
```

It SHALL NOT silently add/remove participants from already-committed Appointments.

Main Street MAY coordinate those changes for the merchant through existing participant operations.

---

# 44. Joining or Leaving a Series

Adding a customer to future recurring sessions SHALL establish or revise their Series-level participation authority.

Removing a customer from future recurring sessions SHALL stop that CustomerContext being included in later uncommitted occurrence establishment.

Neither operation silently mutates already-committed future Appointment Participations.

The merchant-facing interaction may still be compressed into:

```text
"Stop Sarah's future sessions"
```

while Main Street internally separates:

```text
Series future default change

from

existing committed Appointment changes
```

---

# 45. Customer Decision and Series Changes

A material Series change affecting a customer's agreed recurring arrangement SHALL require appropriate current authority.

Merchant intent alone SHALL NOT silently change customer-agreed material recurrence meaning unless the governing accepted Appointment policy already grants that authority.

Historical Series/customer-decision provenance remains applicable.

AI cannot create customer agreement.

Conversation text alone cannot automatically mutate Series authority.

---

# 46. Recurrence and Payment Remain Separate

Appointment Series SHALL NOT automatically manufacture:

```text
subscription
invoice schedule
Payment Obligation
deposit schedule
membership
prepaid package
```

Where recurring appointments also have recurring payment obligations, Payment/Commercial authorities must establish those independently.

Canonical:

```text
recurring Appointment
    ≠ recurring payment
```

---

# 47. Recurrence and Workforce Remain Separate

An Appointment Series SHALL NOT create:

```text
staff shift
Workforce schedule
worked-time fact
pay entitlement
```

Recurring Appointments may consume applicable current Workforce evidence through the accepted Resource/Workforce affinity.

Workforce availability changes may cause a future occurrence exception.

They do not rewrite Series/customer commitment truth.

---

# 48. Recurrence and Notification Remain Separate

Appointment or Series authority determines when customer communication is required.

Notification owns delivery.

Examples:

```text
occurrence successfully created
occurrence cannot be created
material committed change
series termination
```

may establish communication responsibilities where an accepted contract requires them.

Notification failure does not rewrite Appointment/Series truth.

---

# 49. AI Boundary

AI MAY:

```text
interpret:
"Book Sarah every Wednesday at 10"

propose a registered recurrence pattern

explain exceptions

summarise affected future appointments

prepare participant changes
```

AI SHALL NOT:

```text
invent a recurrence rule not accepted by the merchant/customer

guess DST ambiguity

create customer agreement

declare Scheduling successful

override participant capacity

infer participant attendance

infer no-show from silence

rewrite existing Appointments
```

AI output remains candidate interpretation until deterministic validation and applicable authority succeed.

---

# 50. Low-Administration Merchant Experience

The merchant SHALL NOT normally manage concepts such as:

```text
SeriesOccurrenceIdentity
Series revision affinity
participant-capacity claims
timezone-rule ambiguity
durable materialisation work
```

Expected business interaction:

```text
Customer: Sarah

Service: Weekly tutoring

When: Wednesday at 10:00

Starts: 16 September

Ends: No end date

[Create recurring appointments]
```

or:

```text
Friday Yoga Class

Sarah
James
Mohammed

[Add participant]
```

Only exceptions requiring real business decisions should surface.

---

# 51. Explicit Non-Goals

This amendment does not define:

```text
recurring Booking semantics

multi-room/group Booking

waitlists

overbooking

class/course curriculum

membership plans

customer loyalty

event ticketing

unnamed guest/party quantities

one customer acting as organiser for other customers

guardian/dependent authority

team/household customer identity

attendance percentages

generic recurrence DSL

arbitrary RFC 5545 RRULE import

bulk transactional rewrite of many committed Appointments

Payment subscription semantics

generic event-management capability
```

`MS-PROT-042-GRP-06` remains separate.

---

# 52. Fundamental Vision Conformance

## Business-to-Software Translation

PASS.

Merchant language can remain:

```text
"Every Wednesday at 10."
"Add Sarah to the class."
"She isn't coming this week."
```

Main Street translates that into exact semantics.

## Administrative Compression

PASS.

Without Series semantics the merchant must repeatedly recreate equivalent Appointments.

Without shared-participant semantics the merchant must either duplicate group sessions or maintain participation externally.

## Ordinary-Staff Training

PASS.

Staff need to understand:

```text
who is attending
who arrived
whether the session happened
```

not internal semantic machinery.

## Role-Native Operation

PASS.

Appointment and participant actions can appear within the merchant's operational schedule rather than as separate software modules.

## Target-Market Proportionality

PASS.

The design deliberately excludes broader class/event/course management.

## Capability Depth

PASS.

Only recurrence, participation and attendance facts necessary for correct Appointment operation are added.

## Cross-Capability Value

PASS.

Scheduling, Workforce, Allocation, CustomerContext, Notification and Attention can coordinate around a correct Appointment truth without absorbing it.

## Exception-Driven Operation

PASS.

Normal recurrence can run quietly; only failed occurrences, capacity conflicts, customer decisions and ambiguous times need merchant attention.

### Vision outcome

**VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

The added internal complexity is required to prevent false commitments, duplicated capacity, customer-data leakage and incorrect group attendance semantics. It is intentionally hidden from ordinary merchant administration.

---

# 53. Architecture Review

## Ownership Review — PASS

```text
Appointment
    owns Appointment Series
    owns Appointment
    owns Appointment Participation
    owns participant attendance outcome

Scheduling
    owns schedulability

Resource / Allocation
    owns capacity

Workforce
    owns workforce availability

CustomerContext
    owns merchant/customer relationship

Notification
    owns delivery

Payment
    owns money/payment truth

Calendar
    projects facts

AI
    assists interpretation only
```

No shared mutation authority is introduced.

## Commitment Integrity — PASS

Series generation cannot bypass `appointment.confirm`.

Existing `appointment.confirm` authority requires Scheduling revalidation before commitment.

## Customer Identity Boundary — PASS

CustomerContext remains merchant-scoped relationship truth rather than a global person/identity record.

## Outcome Boundary — PASS

The existing Appointment Occurrence Outcome remains interaction-level truth, so participant attendance does not need to distort the v1.9 outcome vocabulary.

## Rescheduling Boundary — PASS

Series revision cannot bypass the accepted rule that already-agreed Appointment changes retain customer/policy authority.

## Resource/Workforce Boundary — PASS

Series/shared semantics consume accepted Appointment Support Requirement and Resource/Allocation truth rather than claiming supporting capacity themselves.

---

# 54. Falsification

| Scenario | Expected result |
|---|---|
| Merchant creates weekly Wednesday 10:00 tutoring | PASS — one Series, independent Appointments |
| Wednesday occurrence successfully materialises | PASS — ordinary Scheduling + capacity + Appointment commitment |
| Retry after lost acknowledgement | PASS — same Series Occurrence cannot create duplicate Appointment |
| Appointment later cancelled | PASS — Series Occurrence remains consumed; cancellation does not recreate it |
| Scheduler outage lasts past occurrence start | PASS — no fabricated historical Appointment |
| Next occurrence conflicts with current authoritative capacity | PASS — no Appointment; Series remains; exception surfaced |
| Required provider/workforce evidence unresolved | PASS — occurrence remains uncommitted |
| Series is open-ended | PASS — no infinite Appointment/capacity creation |
| Merchant changes weekly time | PASS — uncommitted future Series meaning changes; committed Appointments remain |
| Merchant selects “this occurrence only” | PASS — exact Appointment operation |
| DST changes UTC offset | PASS — local 10:00 remains local 10:00 |
| Recurrence falls in nonexistent DST local time | PASS — no silent time shift |
| Same group session has five customers | PASS — one Appointment, five Participations |
| Two customers concurrently attempt final class seat | PASS — one capacity claim wins; no over-capacity duplication |
| Sarah withdraws from class | PASS — Sarah participation removed; Appointment remains |
| Sarah withdraws before event | PASS — no later ordinary NO_SHOW for Sarah |
| Three attend and two no-show | PASS — Appointment can be OCCURRED; participant outcomes remain individual |
| Nobody attends but merchant was ready | PASS — Appointment may be CUSTOMER_NO_SHOW; participants may have NO_SHOW evidence |
| Merchant cannot provide class | PASS — MERCHANT_SIDE_NON_OCCURRENCE; participant NO_SHOW not fabricated |
| Participant signs into customer account | PASS — does not reveal other participant identities |
| Customer knows another participant's email | PASS — no access/identity authority |
| One participant tries to cancel entire group Appointment | PASS — related-customer relationship insufficient |
| Merchant adds customer to future recurring series | PASS — future defaults only; existing Appointments separately governed |
| Merchant removes customer from Series | PASS — no silent rewriting of committed future Participations |
| AI reads “every Wednesday-ish” | PASS — must resolve exact registered semantics before authority |
| AI infers no-show from silence | PASS — prohibited |
| Recurring Appointment has monthly invoice | PASS — Payment remains independent |
| Staff assigned to recurring Appointment goes on leave | PASS — Workforce affects support/capacity evidence, not Series ownership |

No falsification requires business-type branching, generic event management or shared mutable authority.

---

# 55. Residual Scope

`MS-PROT-042-GRP-03` is resolved for the initial Main Street portfolio.

No retained GRP-03 semantic DQ remains for:

```text
recurring Appointment ownership

future occurrence establishment

Series revision versus existing Appointment authority

initial recurrence pattern portfolio

timezone/DST ambiguity

shared multi-customer Appointment

participant relationship

participant withdrawal

participant attendance outcome

participant privacy/access

recurring shared Appointment composition
```

The following remain intentionally outside GRP-03:

```text
GRP-01
Booking Outcome & Change Semantics

GRP-06
Capacity-Demand Exceptions
including waiting-list/overbooking feature admission
```

---

# 56. Implementation-Rules Impact

**Implementation activation remains NONE.**

Acceptance of this authority permits later implementation architecture to represent these semantics.

It does not instruct implementation to begin immediately and does not change the current implementation programme frontier by itself.

---

# 57. Recommendation

**RECOMMENDATION: ACCEPT**

Reason:

The proposal introduces the minimum additional authority needed to represent two common small-business realities:

```text
repeated customer appointments
+
several customers participating in one scheduled interaction
```

while preserving the fundamental distinctions already established by MS-PROT-042:

```text
Scheduling evaluates
Appointment commits
Resource/Allocation owns capacity
CustomerContext owns merchant/customer relationship
Occurrence Outcome records what happened
```

The most important design choice is:

> **Recurrence does not weaken Appointment commitment admission, and sharing an Appointment does not merge customers or grant cross-participant authority.**

The design adds internal precision specifically to remove recurring administration from merchants rather than exposing additional software machinery.

---

# 58. Acceptance Statement

Manual approval was given on **9 September 2026** after complete proposal presentation, vision-conformance review, ownership review and falsification.

The accepted result is:

> **Main Street represents a recurring scheduling arrangement as an Appointment-owned Appointment Series, while every actual occurrence remains an independently authoritative Appointment established through ordinary current Scheduling and capacity admission. A Series Occurrence may create at most one Appointment and cannot be recreated after later cancellation or outcome. Series revision affects only eligible uncommitted future occurrences and never silently rewrites existing Appointment commitments. A shared Appointment remains one Appointment with explicit CustomerContext-specific Appointment Participations; each participant's access, withdrawal and attendance truth is independently scoped, while overall Appointment Occurrence Outcome continues to describe the shared scheduled interaction. Recurrence, group participation, capacity, Workforce, Payment, Notification and customer identity remain separate bounded authorities.**

**Implementation activation: NONE.**
