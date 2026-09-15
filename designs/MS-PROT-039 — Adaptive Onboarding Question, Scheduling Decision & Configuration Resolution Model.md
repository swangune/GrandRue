# MS-PROT-039 — Adaptive Onboarding Question, Scheduling Decision & Configuration Resolution Model

**Document ID:** MS-PROT-039
**Version:** 1.1
**Status:** **Accepted after material revision and revalidation**
**Supersedes:** MS-PROT-039 v1.0
**Depends on:** MS-PROT-005, MS-PROT-006, MS-PROT-021, MS-PROT-022, MS-PROT-023, MS-PROT-028, MS-PROT-038
**Purpose:** Define the strict contract by which Main Street determines what onboarding questions to ask, how answers alter the candidate merchant configuration, how unresolved decisions are identified, and how scheduling-related onboarding maps into Main Street's calendar and scheduling semantics without conflating configuration, calendar occupancy, customer commitments, or external infrastructure.

---

## 1. Governing principle

> **Main Street onboarding is a deterministic, graph-guided configuration-discovery process. It asks a merchant only for information necessary to resolve material configuration choices that cannot already be inferred or deterministically derived from registered semantics.**

The onboarding system shall not attempt to construct a complete description of the merchant's business.

It shall determine only enough to create a candidate configuration that:

1. refers exclusively to supported Main Street semantics;
2. contains enough merchant-owned choices and data to resolve applicable configuration;
3. contains no unresolved mandatory decision;
4. can be deterministically validated by the configuration compiler; and
5. can be explained to the merchant in business-facing language before activation.

Canonical flow:

```text
Merchant evidence
        ↓
Candidate semantic selections
        ↓
Registered semantic relationships
        ↓
Candidate configuration graph
        ↓
Identify unresolved material decisions
        ↓
Ask highest-value applicable question
        ↓
Merchant answer
        ↓
Recompute affected configuration
        ↓
Repeat only while necessary
        ↓
Compiler validation
        ↓
Merchant review
        ↓
Approved configuration
```

---

# 2. Architectural boundary

MS-PROT-039 governs:

```text
Onboarding questions
Answer structures
Question applicability
Decision provenance
Question sequencing
Adaptive branch pruning
Merchant corrections
Scheduling-related onboarding
Scheduling-mode selection
Operating-hours configuration
Break configuration
Calendar intent distinctions
Merchant-facing calendar validity representation
Stopping conditions
Candidate review inputs
```

MS-PROT-039 does **not** define:

```text
Java implementation
database schema
Google Calendar API implementation
OAuth implementation
calendar component library
AI provider
question-ranking algorithm
meeting-provider integration
payment provider
notification transport
UI styling
```

Those are downstream concerns.

---

# 3. Fundamental responsibility separation

The following responsibilities shall remain separate.

| Concern                                   | Authority                                      |
| ----------------------------------------- | ---------------------------------------------- |
| Merchant business intent                  | Merchant                                       |
| Interpretation of merchant evidence       | Inference                                      |
| Supported semantic vocabulary             | Main Street Semantic Registry                  |
| Relationships between semantic constructs | Main Street Semantic Registry                  |
| Merchant-selectable policy values         | Registered Main Street definitions             |
| Configuration structural validity         | Compiler                                       |
| Runtime business validity                 | Runtime/application semantics                  |
| Scheduling configuration                  | Main Street configuration                      |
| Scheduling calculation                    | Main Street scheduling engine                  |
| Customer/business relationship            | Main Street operational database               |
| Calendar information persistence          | Google Calendar-backed calendar infrastructure |
| Merchant-facing calendar interaction      | Main Street Calendar                           |
| Calendar business meaning                 | Main Street                                    |
| External meeting delivery                 | Separate integration concern                   |

Hard invariant:

> **No layer may assume authority owned by another layer merely because it possesses related information.**

---

# 4. Question eligibility

A question may enter the onboarding sequence only when answering it can materially affect the candidate configuration.

A question is **material** when at least one valid answer can alter one or more of:

```text
capability activation
policy selection
requirement applicability
offering configuration
resource configuration
capacity participation
scheduling behaviour
interaction mode
access mode
exposure behaviour
notification behaviour
operational data requirement
integration requirement
```

A question shall not be asked merely because the information may be interesting, useful for analytics, or commonly associated with the merchant's industry.

For example:

> "What type of business are you?"

may provide useful contextual evidence.

It must not automatically determine:

```text
capabilities
runtime branches
dashboard type
verification eligibility
scheduling behaviour
```

Business category remains contextual.

---

# 5. Question provenance

Every question asked during onboarding shall have a reason.

Conceptually:

```text
QuestionDecision
{
    question
    triggering_unresolved_decision
    applicable_context
    known_evidence
}
```

Main Street should therefore be able to answer internally:

> Why are we asking this merchant this question?

Valid answer:

```text
Customer booking appears applicable,
but scheduling authority is unresolved.
```

Invalid answer:

```text
Most mechanics use appointments.
```

Statistical likelihood may influence inference.

It does not establish merchant configuration.

---

# 6. Question families

Main Street shall use reusable question families rather than separate questionnaires for each business category.

The core families are:

| Family                | Resolves                                                              |
| --------------------- | --------------------------------------------------------------------- |
| Offering              | What the merchant provides                                            |
| Customer interaction  | What customers may do                                                 |
| Commerce              | Whether supported commercial operations apply                         |
| Scheduling            | Whether appointment-style scheduling applies and how it is controlled |
| Fulfilment            | How an offering reaches or is performed for the customer              |
| Capacity/resource     | Whether limited resources constrain fulfilment                        |
| Customer/context data | What information is required by an applicable operation               |
| Publication/exposure  | What information is made publicly discoverable                        |
| Staff/operations      | Whether staff relationships affect execution                          |
| Notifications         | Which supported operational events require communication              |
| Integration           | Technical connections required by already-selected semantics          |

A question family is not itself a capability.

---

# 7. Structured questions are authoritative for merchant choice

Where an answer has direct configuration consequences, Main Street should use bounded structured choices.

Supported answer forms may include:

```text
SINGLE_SELECT
MULTI_SELECT
BOOLEAN
QUANTITY
DURATION
TEXT
STRUCTURED_VALUE
```

Natural-language answers may supply evidence.

They shall not silently replace bounded merchant decisions where precise semantics are required.

Example:

Merchant writes:

> "People usually contact me and we arrange a convenient time."

Inference may propose:

```text
Scheduling applicable
Merchant-proposed scheduling likely
```

But where that distinction materially determines runtime behaviour, Main Street may still require the merchant to choose the supported scheduling mode explicitly.

---

# 8. Decision provenance states

Each material onboarding decision shall retain provenance.

The minimum conceptual states are:

```text
UNRESOLVED
INFERRED
MERCHANT_SELECTED
DERIVED
DEFAULTED
NOT_APPLICABLE
```

### UNRESOLVED

A material decision still requires evidence or merchant selection.

### INFERRED

Inference has proposed a likely answer.

The answer remains correctable where it represents merchant-owned choice.

### MERCHANT_SELECTED

The merchant has explicitly selected the value.

### DERIVED

The value follows deterministically from registered semantics or another authoritative configuration decision.

### DEFAULTED

A registered default has been applied.

It remains distinguishable from explicit merchant choice.

### NOT_APPLICABLE

The current configuration graph makes the decision irrelevant.

---

# 9. Authority precedence

Where evidence conflicts:

```text
registered invariant
        >
registered semantic consequence
        >
explicit merchant selection
        >
registered default
        >
inference
```

This does not mean a merchant selection can override an invariant.

It means that among legitimate merchant-owned decisions, explicit merchant choice supersedes probabilistic interpretation.

Example:

```text
Merchant description:
"Customers normally book online."

Explicit answer:
"I want to review appointment requests myself."
```

Result:

```text
Scheduling mode =
MERCHANT_PROPOSES_OR_CONFIRMS
```

Inference shall not override the explicit selection.

---

# 10. Adaptive question selection

Main Street shall not use a fixed universal sequence.

The next question is selected from the current unresolved configuration state.

Canonical process:

```text
Current candidate graph
        ↓
Find unresolved mandatory decisions
        ↓
Remove questions whose applicability is false
        ↓
Identify decisions with largest downstream effect
        ↓
Ask appropriate business-facing question
```

High-branching questions should normally precede leaf-level questions.

For example:

```text
Do customers book appointments?
```

should normally precede:

```text
How much preparation buffer is needed?
```

because the latter may become entirely irrelevant.

---

# 11. Branch pruning

When an answer makes a semantic branch irrelevant, questions belonging only to that branch shall be removed.

Example:

```text
Customer appointment scheduling = NOT_APPLICABLE
```

may eliminate questions concerning:

```text
customer self-booking
appointment selection
appointment confirmation
calendar availability
appointment cancellation
appointment buffers
```

unless another selected operation independently requires them.

Main Street shall not preserve irrelevant questions merely because they exist in the global question catalogue.

---

# 12. Repetition rule

Main Street shall not ask the merchant to provide information it already possesses unless one of the following applies:

```text
evidence conflict
context differs materially
merchant changed earlier selection
previous answer became invalid
previous answer no longer resolves the decision
```

Avoiding repetition shall not cause genuinely context-specific questions to be collapsed incorrectly.

---

# 13. Context-scoped decisions

A merchant may operate more than one materially different offering or operation.

Therefore policy and scheduling decisions must not automatically be merchant-wide.

Example:

```text
Mechanic

Oil Change
    customer-selectable appointment

Complex Diagnostics
    merchant-proposed appointment

Parts Sales
    no appointment scheduling
```

This is valid.

Therefore:

> **Where operational behaviour differs by offering or operation, onboarding decisions shall be scoped to the applicable semantic context rather than forced into a single merchant-wide answer.**

---

# 14. Scheduling applicability

Scheduling is not equivalent to every operation involving time.

Main Street shall distinguish appointment-style scheduling from other temporal semantics.

For example:

```text
Consultation at 14:00
    → scheduling

Mechanic appointment at 09:30
    → scheduling

Hair appointment at 11:00
    → scheduling
```

A motel stay from Friday to Sunday may involve:

```text
date-range availability
resource allocation
booking
```

without necessarily using the appointment-calendar model defined here.

Therefore MS-PROT-039 shall not force every temporal business operation through Main Street Calendar.

---

# 15. Scheduling onboarding — first decision

Where appointment-style scheduling is potentially applicable, the first material question is:

> **Does this service require an appointment or agreed service time?**

Supported semantic outcomes:

```text
NO_APPOINTMENT_SCHEDULING
APPOINTMENT_SCHEDULING
CONTEXT_DEPENDENT
```

`CONTEXT_DEPENDENT` means different offerings or operations require different answers.

---

# 16. Scheduling authority model

If appointment scheduling applies, Main Street shall determine the scheduling authority mode.

The supported conceptual modes are:

```text
CUSTOMER_SELECTS_AVAILABLE_TIME

MERCHANT_PROPOSES_OR_CONFIRMS

CONTEXT_DEPENDENT
```

These meanings are strict.

---

# 17. CUSTOMER_SELECTS_AVAILABLE_TIME

This mode means:

> **The merchant has preconfigured the operational boundaries within which Main Street may accept customer-selected appointment times.**

The merchant does not manually approve each valid customer selection.

Main Street determines valid availability using applicable scheduling constraints.

Conceptually:

```text
Operating configuration
        +
Working hours
        +
Breaks
        +
Offering duration
        +
Buffers
        +
Existing scheduled activity
        +
Relevant capacity
        +
Relevant resource constraints
        +
Applicable policies
        ↓
Available appointment opportunities
        ↓
Customer chooses
        ↓
Authoritative revalidation
        ↓
Appointment commitment
```

Critical rule:

> **The customer chooses from Main Street-calculated valid availability. The customer does not choose an arbitrary time.**

If final authoritative revalidation succeeds, no separate merchant approval is required merely because the customer selected the time.

---

# 18. MERCHANT_PROPOSES_OR_CONFIRMS

This mode means:

> **The merchant retains direct control over the final appointment time rather than exposing automatically bookable availability as the commitment mechanism.**

Typical flow:

```text
Customer expresses scheduling intent
        ↓
No appointment commitment yet
        ↓
Merchant reviews current schedule
        ↓
Merchant chooses/proposes valid time
        ↓
Main Street validates proposed time
        ↓
Customer accepts if applicable
        ↓
Appointment commitment established
```

The merchant remains subject to Main Street scheduling constraints.

Merchant control does not mean calendar-level override authority.

---

# 19. Mixed scheduling is valid

A merchant may use both scheduling modes.

The distinction may occur by:

```text
offering
operation
service class
resource context
```

Example:

```text
Initial Consultation
    CUSTOMER_SELECTS_AVAILABLE_TIME

Complex Project Review
    MERCHANT_PROPOSES_OR_CONFIRMS
```

Main Street shall not force the merchant into one global scheduling mode where the business semantics differ.

---

# 20. Main Street Calendar

Main Street shall provide its own merchant-facing calendar.

The merchant-facing abstraction is:

```text
Main Street Calendar
```

The merchant shall not be required to understand or manipulate Main Street's internal semantic graph in order to use it.

The calendar is an operational scheduling surface.

It is **not** the authority that defines scheduling rules.

---

# 21. Google Calendar boundary

Main Street Calendar is backed by Google Calendar-based calendar storage/infrastructure.

The strict responsibility model is:

```text
Merchant
    initiates authorised schedule manipulation

Main Street Engine
    calculates
    validates
    creates
    changes
    interprets schedule information

Google Calendar
    stores calendar information
```

Google Calendar shall not independently determine:

```text
whether an appointment is valid
whether a customer may book
whether capacity exists
whether a merchant may override a constraint
whether a business commitment exists
whether an appointment should be cancelled
```

Canonical invariant:

> **Google Calendar stores scheduling information. Main Street owns the business semantics and the authority to interpret and manipulate that information.**

---

# 22. Scheduling configuration is not calendar data

The following are configuration constraints:

```text
working hours
recurring breaks
offering duration rules
buffers
applicable scheduling policies
```

They shall not be represented merely as ordinary merchant calendar events.

Example:

```text
Working hours:
Monday–Friday
09:00–17:00

Break:
12:30–13:30
```

The calendar shall not create ordinary events:

```text
"Working Hours"
"Break"
```

to represent those constraints.

They belong to configuration.

---

# 23. Valid scheduling surface

Scheduling configuration determines the base valid scheduling surface.

Example:

```text
Configured:
09:00–17:00

Recurring break:
12:30–13:30
```

Base calendar validity becomes:

```text
Before 09:00
    INVALID

09:00–12:30
    VALID

12:30–13:30
    INVALID

13:30–17:00
    VALID

After 17:00
    INVALID
```

The invalid periods are not calendar events.

They are the visual and operational projection of authoritative scheduling constraints.

---

# 24. Calendar operations cannot bypass configuration

A merchant must not be able to create, drag, resize or move a customer appointment into a period prohibited by applicable scheduling configuration.

Example:

```text
Working hours:
09:00–17:00
```

Merchant attempts:

```text
18:00–19:00 customer appointment
```

Required behaviour:

```text
calendar manipulation request
        ↓
Main Street validation
        ↓
OUTSIDE_WORKING_HOURS
        ↓
REJECT
```

Similarly:

```text
Recurring break:
12:30–13:30
```

Merchant attempts:

```text
13:00 customer appointment
```

Result:

```text
REJECT
reason = RECURRING_BREAK
```

Hard invariant:

> **The merchant may change a scheduling constraint through configuration, but may not bypass the currently active constraint through calendar manipulation.**

---

# 25. Invalid calendar time must explain itself

Main Street shall not present non-schedulable time to a merchant without an intelligible reason.

Configuration-derived reasons must have both:

```text
machine-readable reason
merchant-readable explanation
```

Examples:

```text
OUTSIDE_WORKING_HOURS
→ "Outside working hours"

RECURRING_BREAK
→ "Regular break"

CLOSED_DAY
→ "Not a working day"

EXTENDS_BEYOND_WORKING_HOURS
→ "This appointment would finish after working hours"
```

The explanatory UI must work on mobile and shall not depend solely on hover behaviour.

---

# 26. Invalidity is not occupancy

Main Street shall distinguish:

### INVALID

The requested time violates applicable scheduling configuration.

Examples:

```text
outside working hours
recurring break
closed day
appointment would extend beyond valid operating period
```

### UNAVAILABLE

The time is inside the valid scheduling surface but currently cannot support the requested operation.

Examples:

```text
merchant-created unavailability
required staff unavailable
required resource unavailable
capacity exhausted
buffer conflict
```

### OCCUPIED

The time is currently represented by an existing scheduled commitment or explicit schedule intent.

### AVAILABLE

The time is inside the valid scheduling surface and no currently evaluated constraint prevents the requested operation.

These are **derived scheduling classifications**, not necessarily persisted domain states.

---

# 27. Merchant-created unavailability

A merchant must be able to block otherwise-valid time for a specific reason.

Example:

```text
Tuesday
14:00–15:30
```

Merchant intent:

```text
UNAVAILABLE
```

Merchant-defined title:

```text
"Dentist appointment"
```

Conceptually:

```text
ScheduleIntent
{
    intent = UNAVAILABLE
    title = "Dentist appointment"
    start
    end
    merchant_scope
}
```

This is different from:

```text
working hours
recurring breaks
```

because it represents a specific time-bound merchant intent rather than recurring configuration.

---

# 28. Schedule-intent title

The merchant may provide a human-readable title.

Examples include:

```text
Dentist appointment
Supplier meeting
School run
Personal appointment
Training
Bank appointment
```

Main Street shall not derive business semantics from the title.

The machine-readable intent remains authoritative.

Example:

```text
intent = UNAVAILABLE
title = "Dentist appointment"
```

The title does not create:

```text
customer relationship
customer appointment
booking
payment requirement
notification requirement
```

Hard invariant:

> **The title explains the entry to the merchant; the registered intent determines its scheduling meaning.**

---

# 29. Customer-related calendar information

Customer-related schedule information has a stronger contract.

Any calendar information representing a customer appointment, booking or other scheduled customer operation must have a direct corresponding relationship to Main Street operational data.

Conceptually:

```text
Merchant
    ↓
Customer / Customer Context
    ↓
Appointment or Booking
    ↓
Calendar representation
```

Required invariant:

> **No customer-related calendar record may exist as authoritative Main Street business information without a corresponding Main Street operational record and direct customer relationship.**

---

# 30. Customer relationship does not require customer account

The preceding rule does not require every customer to register for a Main Street account.

A guest or unregistered customer may still produce:

```text
merchant-scoped customer context
        ↓
appointment
        ↓
calendar representation
```

Therefore:

```text
customer relationship
    ≠
customer account
```

Main Street must still maintain enough customer context to preserve the business relationship where a customer-related scheduled operation exists.

---

# 31. Merchant-created customer appointment

The same rule applies when the merchant manually creates an appointment.

Example:

```text
Customer phones merchant
        ↓
Merchant creates/selects customer context
        ↓
Merchant creates appointment
        ↓
Main Street validates schedule
        ↓
Appointment stored in Main Street
        ↓
Calendar representation created
```

The merchant shall not create an unstructured calendar item such as:

```text
"John — haircut"
```

and thereby bypass the Main Street customer and appointment model.

If the schedule record concerns a customer operation, Main Street must know:

```text
which customer/context
which operation/appointment
which merchant
which scheduled interval
```

---

# 32. Calendar information categories

The merchant-facing calendar may contain scheduled information from at least two materially different sources:

```text
A. Merchant Schedule Intent
   e.g. UNAVAILABLE
   title = "Dentist appointment"
   customer = none

B. Customer Scheduled Operation
   e.g. Appointment
   customer = Customer-123
   appointment = Appointment-456
```

These shall not be conflated.

Recurring working hours and breaks belong to neither category because they remain scheduling configuration.

---

# 33. Availability calculation

For appointment-style scheduling, the conceptual calculation is:

```text
Configured valid working periods
        ↓
remove recurring break constraints
        ↓
remove merchant UNAVAILABLE intents
        ↓
remove/conflict-check existing customer commitments
        ↓
apply offering duration
        ↓
apply buffers
        ↓
apply resource constraints
        ↓
apply capacity constraints
        ↓
apply other registered scheduling policies
        ↓
candidate appointment availability
```

Availability remains contextual and advisory until authoritative commitment.

---

# 34. Final authoritative revalidation

A previously displayed available time does not establish ownership.

Customer self-booking flow:

```text
Availability calculated
        ↓
Customer selects slot
        ↓
Command submitted
        ↓
Current schedule re-read
        ↓
Constraints re-evaluated
        ↓
Capacity/resources revalidated
        ↓
Appointment committed
```

If another valid commitment has taken the capacity in the meantime:

```text
commitment attempt
        ↓
revalidation fails
        ↓
appointment not created
        ↓
new availability presented
```

This preserves MS-PROT-006 and MS-PROT-023.

---

# 35. Existing commitments and configuration changes

Scheduling configuration changes govern **new scheduling decisions**.

They shall not silently rewrite existing business commitments.

Example:

```text
Existing appointment:
Tuesday 16:00–17:00

Merchant changes working hours:
09:00–16:00
```

The existing appointment remains an existing business commitment.

Main Street may surface the resulting conflict for merchant resolution.

It shall not silently:

```text
cancel appointment
move appointment
delete calendar relationship
reinterpret historical commitment
```

This follows the configuration-evolution boundary established upstream.

---

# 36. External scheduler question is prohibited from the core scheduling branch

The following v1.0 answer set is rejected:

```text
No
Customer chooses
Merchant proposes
External scheduler used
```

It is invalid because it combines:

```text
scheduling applicability
scheduling authority
implementation/integration
```

into one question.

Main Street Calendar is the normal merchant-facing scheduling system.

Therefore core onboarding shall not ask the merchant to choose between:

```text
Main Street
Google Calendar
Calendly
Outlook
```

as though those were alternative business semantics.

---

# 37. Google Calendar is not an onboarding scheduling choice

Google Calendar belongs beneath Main Street Calendar's implementation/storage boundary.

Therefore:

```text
Google Calendar
```

shall not appear alongside:

```text
CUSTOMER_SELECTS_AVAILABLE_TIME
MERCHANT_PROPOSES_OR_CONFIRMS
```

Those concepts answer entirely different questions.

---

# 38. Meeting providers are separate

A remote consultation may subsequently need a delivery mechanism such as:

```text
Google Meet
Zoom
other supported meeting provider
```

That is a remote-service or meeting-delivery integration concern.

It is not part of determining scheduling authority.

MS-PROT-039 therefore does not model meeting-provider selection as a scheduling-mode answer.

---

# 39. Merchant correction

The merchant may revise a previous material answer.

Revision shall trigger recomputation of affected candidate configuration.

Canonical behaviour:

```text
Merchant changes answer
        ↓
Identify dependent inferred/derived decisions
        ↓
Invalidate affected candidate results
        ↓
Reapply registered semantic relationships
        ↓
Determine newly unresolved decisions
        ↓
Ask only newly applicable questions
```

Stale inferred semantics must not remain active merely because they appeared earlier in onboarding.

---

# 40. Defaults

Defaults are permitted only where registered semantics explicitly define them.

A default must be:

```text
registered
valid for the semantic owner
traceable
distinguishable from explicit merchant selection
```

A default shall not silently activate an optional capability merely because similar merchants commonly use it.

Hard distinction:

```text
DEFAULTED
≠
MERCHANT_SELECTED
```

---

# 41. Onboarding stopping condition

Configuration questioning shall stop when all of the following are true:

```text
all mandatory material decisions resolved
all selected semantics resolvable
all registered mandatory dependencies resolvable
all remaining unknowns optional/defaultable/content-level
candidate configuration can be compiled
```

At that point, Main Street should not continue questioning merely to collect more business information.

---

# 42. Merchant review

Merchant review shall present consequential operational behaviour.

Example:

```text
Appointments

Customers can choose from available times:
YES

Working hours:
Monday–Friday
09:00–17:00

Regular break:
12:30–13:30

Main Street will automatically prevent appointments:
• outside your working hours
• during your regular break
• during times you mark unavailable
• where another applicable appointment/resource conflict exists
```

For a different offering:

```text
Project Review

Customers request an appointment:
YES

You choose or confirm the final time:
YES
```

The merchant shall not be asked to review:

```text
semantic node identifiers
dependency edges
compiler IR
availability algebra
calendar correlation internals
```

---

# 43. Falsification review

The revised model was challenged against the following failure cases.

| Falsification case                                                                | Result                                                                             |
| --------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------- |
| Merchant works 09:00–17:00 but drags appointment to 18:00                         | Rejected by working-hours constraint                                               |
| Merchant tries to place appointment inside regular break                          | Rejected by break constraint                                                       |
| Merchant needs dentist appointment during otherwise-valid time                    | Represented as `UNAVAILABLE` intent with merchant title                            |
| Dentist appointment title resembles customer appointment                          | No customer semantics because intent/relationship says otherwise                   |
| Merchant manually books telephone customer                                        | Requires Main Street customer context + appointment before calendar representation |
| Customer self-books a displayed slot                                              | Final authoritative revalidation still required                                    |
| Two customers select final available slot simultaneously                          | Only valid authoritative commitment succeeds                                       |
| Consultant permits self-booking for one service and manual scheduling for another | Supported through context-scoped scheduling policy                                 |
| Merchant changes working hours after existing future booking                      | Existing commitment not silently rewritten                                         |
| Information publisher has no appointments                                         | Scheduling branch becomes not applicable                                           |
| Motel uses stay/date-range booking                                                | Not forced through appointment scheduling                                          |
| Merchant marks 14:00 unavailable                                                  | Calendar occupancy changes; recurring operating configuration does not             |
| Merchant wants to override break directly from calendar                           | Prohibited; configuration must be changed instead                                  |
| Invalid calendar area appears disabled without explanation                        | Prohibited; reason required                                                        |
| Google Calendar has schedule information                                          | Does not gain authority over business semantics                                    |
| Customer-related schedule exists without Main Street relationship                 | Invalid/orphaned business state                                                    |
| External scheduler appears as scheduling-mode answer                              | Rejected                                                                           |
| Free-text business description contradicts explicit scheduling choice             | Explicit merchant choice prevails                                                  |

No remaining falsifier requires architectural revision to this document.

---

# 44. Cross-domain validation

## 44.1 Information publisher

Merchant provides:

```text
scholarship opportunities
funding information
application links
```

Visitors:

```text
browse
search
subscribe
enquire
```

Appointment scheduling:

```text
NOT_APPLICABLE
```

Main Street asks no calendar questions.

**PASS**

---

## 44.2 Online consultant — customer self-booking

Merchant provides:

```text
30-minute consultation
```

Scheduling:

```text
CUSTOMER_SELECTS_AVAILABLE_TIME
```

Configuration:

```text
working hours
recurring breaks
consultation duration
other applicable constraints
```

Main Street calculates customer-visible appointment opportunities.

A valid customer selection proceeds to authoritative revalidation and confirmation without ordinary manual merchant approval.

**PASS**

---

## 44.3 Online consultant — merchant controlled

Merchant prefers to review each enquiry.

Scheduling:

```text
MERCHANT_PROPOSES_OR_CONFIRMS
```

Customer expresses desired consultation.

Merchant reviews Main Street Calendar and proposes a valid time.

Main Street still rejects times outside active scheduling constraints.

**PASS**

---

## 44.4 Mechanic

Merchant has:

```text
Oil Change
    CUSTOMER_SELECTS_AVAILABLE_TIME

Complex Diagnostics
    MERCHANT_PROPOSES_OR_CONFIRMS
```

Both operate through the same Main Street scheduling system.

Capacity/resource constraints may differ.

No `MechanicCalendar` implementation is required.

**PASS**

---

## 44.5 Merchant temporary unavailability

Merchant normally works:

```text
09:00–17:00
```

Merchant creates:

```text
intent = UNAVAILABLE
title = "Dentist appointment"
14:00–15:30
```

Customer availability excludes the period.

No customer relationship is created.

Normal working-hour configuration remains unchanged.

**PASS**

---

## 44.6 Telephone booking

Customer phones merchant.

Merchant creates or selects the relevant customer context.

Merchant creates appointment.

Main Street validates scheduling constraints.

Appointment receives calendar representation.

Customer relationship remains preserved even without a registered customer account.

**PASS**

---

# 45. Accepted invariants

1. Main Street onboarding is graph-driven rather than a fixed universal questionnaire.
2. Questions exist only to resolve material configuration uncertainty.
3. Question families are reusable across business categories.
4. Business category does not determine runtime architecture.
5. Structured merchant selection is authoritative for merchant-owned material choices.
6. Inference cannot invent semantics or semantic relationships.
7. Registered semantic relationships determine derived consequences.
8. Explicit merchant choice supersedes conflicting inference where the merchant owns the choice.
9. Mandatory semantic consequences are not presented as artificial merchant choices.
10. Question branches are pruned when no longer applicable.
11. Known information is not repeatedly requested without a material reason.
12. Decisions may be scoped by offering or operation.
13. Appointment scheduling is not assumed for every temporal business operation.
14. Customer self-booking means selection from Main Street-calculated valid availability.
15. Valid customer selections do not require ordinary manual merchant approval where automatic selection is configured.
16. Merchant-proposed scheduling preserves merchant control of final time.
17. Both scheduling modes may coexist for one merchant.
18. Main Street provides the merchant-facing calendar.
19. Google Calendar provides calendar information storage/infrastructure, not business authority.
20. Main Street owns scheduling calculation and appointment semantics.
21. Working hours and recurring breaks are configuration constraints.
22. Working hours and recurring breaks are not ordinary calendar events.
23. Scheduling configuration determines the valid calendar surface.
24. Calendar manipulation cannot bypass active scheduling configuration.
25. Invalid calendar time must expose an intelligible reason.
26. Configuration invalidity is distinct from contextual unavailability and occupancy.
27. Merchant-created temporary unavailability is a time-specific schedule intent.
28. A merchant-defined title does not determine semantic intent.
29. Customer-related scheduled information must correlate to a Main Street operational record.
30. Customer-related scheduled information must have a direct merchant-scoped customer relationship or context.
31. A customer relationship does not require a persistent customer account.
32. Merchant-created customer appointments follow the same customer/operation relationship rule.
33. Final appointment commitment revalidates current authoritative scheduling reality.
34. Availability does not reserve capacity.
35. Existing commitments are not silently rewritten by later configuration changes.
36. Google Calendar is not a merchant scheduling-mode choice.
37. External scheduler selection is not part of the core scheduling question.
38. Meeting-delivery providers are distinct from scheduling semantics.
39. Defaults remain distinguishable from merchant selections.
40. Onboarding stops when enough information exists for deterministic configuration validation.
41. Merchant review exposes operational meaning, not the semantic graph.

---

# 46. Deferred decisions

The following remain deliberately unresolved:

```text
exact onboarding question wording
exact onboarding screen sequence
question ranking/scoring implementation
AI provider/model
confidence representation
Google Calendar API implementation
Google account/authentication arrangement
calendar persistence mapping
calendar reconciliation mechanism
calendar failure/recovery strategy
exact merchant calendar UI
calendar rendering vocabulary
calendar colour/visual treatment
meeting-provider integration
notification behaviour for schedule changes
precise customer acceptance protocol for merchant-proposed times
resource-specific calendar presentation
multi-staff calendar UX
timezone implementation
recurring exception implementation
holiday configuration
configuration-change conflict resolution workflow
```

None of these deferred decisions may violate the accepted invariants above.

---

# 47. Governance review

### PROPOSE

A strict graph-driven onboarding and scheduling-question model was defined.

**PASS**

### REVIEW / FALSIFICATION

The model was deliberately tested against:

```text
publishers
self-booking consultants
merchant-controlled consultants
mechanics with mixed scheduling
temporary merchant unavailability
manual customer bookings
configuration changes
invalid calendar manipulation
calendar/customer orphan states
Google Calendar authority leakage
```

Material defects from v1.0 were identified and removed.

**PASS**

### VALIDATE

The corrected model represents materially different merchant operations without:

```text
business-type runtime branches
universal scheduling assumptions
external scheduler architecture
calendar-based invariant bypass
fake customer relationships
calendar events representing configuration
```

**PASS**

### ACCEPT

No remaining material contradiction has been identified within the currently accepted Main Street architecture.

**ACCEPTED**

---

# 48. Canonical decision

> **Main Street onboarding asks only the structured, context-relevant questions necessary to resolve a valid merchant configuration. Appointment scheduling is configured independently of calendar infrastructure: a merchant may allow customers to select Main Street-calculated valid availability, retain responsibility for proposing or confirming appointment times, or use different modes for different offerings. Main Street Calendar is the merchant-facing operational surface and uses Google Calendar-based infrastructure to store schedule information, while Main Street retains all scheduling semantics and validation authority. Working hours and recurring breaks are non-bypassable configuration constraints that define the valid scheduling surface and do not appear as ordinary calendar events. Invalid periods remain visible as non-schedulable regions with explicit reasons. Merchant-created temporary unavailability is represented by an explicit scheduling intent with an optional merchant-defined title. Any calendar information relating to a customer must correspond directly to a Main Street operational record and merchant-scoped customer relationship. Calendar manipulation cannot bypass active scheduling constraints, and configuration changes cannot silently rewrite existing business commitments.**
