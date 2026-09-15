# MS-PROT-041 — Main Street Calendar, Scheduling Storage & Appointment Consistency Model

**Document ID:** MS-PROT-041
**Version:** 1.0
**Status:** **Accepted**
**Depends on:** MS-PROT-006, MS-PROT-022, MS-PROT-023, MS-PROT-025, MS-PROT-026, MS-PROT-028, MS-PROT-039, MS-PROT-040
**Purpose:** Define the authoritative boundaries, storage responsibilities, consistency requirements, correlation rules, manipulation rules, failure handling and merchant-facing behaviour of Main Street Calendar when Google Calendar is used as the underlying schedule-information store.

---

# 1. Governing principle

> **Main Street owns scheduling semantics, business commitments, customer relationships, validation and scheduling authority. Google Calendar stores schedule information used by Main Street and exposed through Main Street Calendar. Every customer-related calendar record must correspond directly to a Main Street customer relationship and operational record.**

The model is:

```text
                 MAIN STREET

     ┌───────────────────────────────┐
     │ Merchant Configuration        │
     │                               │
     │ Working hours                 │
     │ Recurring breaks              │
     │ Scheduling policies           │
     │ Offering duration             │
     │ Buffers                       │
     └───────────────┬───────────────┘
                     │
                     ▼
             Scheduling Engine
                     │
         ┌───────────┼───────────┐
         │           │           │
         ▼           ▼           ▼
 Main Street DB   Calendar    Resource /
 customer +       schedule    capacity
 appointments     information constraints
         │           │
         └───────┬───┘
                 ▼
          Scheduling Decision
```

Google Calendar participates as calendar information storage.

It is **not** Main Street's semantic authority.

---

# 2. Architectural objectives

This model shall guarantee:

1. no customer-related calendar record without corresponding Main Street customer and operational data;
2. no customer business relationship existing solely in Google Calendar;
3. working hours and recurring breaks remain configuration rather than calendar events;
4. merchant temporary unavailability remains distinct from recurring scheduling configuration;
5. calendar manipulation cannot bypass scheduling constraints;
6. customer bookings and appointments retain Main Street business semantics;
7. merchant-defined calendar titles remain descriptive rather than semantic;
8. Google Calendar cannot autonomously change Main Street business behaviour;
9. calendar and Main Street database divergence is detectable and recoverable;
10. availability remains concurrency-safe even when calendar propagation is delayed or fails;
11. calendar entries remain merchant-scoped;
12. the merchant interacts with **Main Street Calendar**, not Main Street's internal storage architecture.

---

# 3. Explicit non-goals

This document does not define:

```text
Google API library
OAuth implementation
Google account provisioning
Google Workspace configuration
exact Calendar API calls
database technology
frontend calendar component
calendar colours
event notification transport
meeting-provider integration
Google Meet integration
timezone library
retry library
outbox implementation
```

Those are implementation decisions.

This document defines the semantic and consistency contract those implementations must satisfy.

---

# 4. Four distinct scheduling concepts

The following concepts shall not be conflated:

```text
1. OPERATING CONFIGURATION
2. MERCHANT SCHEDULE INTENT
3. CUSTOMER SCHEDULED OPERATION
4. CALENDAR REPRESENTATION
```

---

# 5. Operating configuration

Operating configuration defines when scheduling is normally permitted.

It includes, where applicable:

```text
working hours
recurring breaks
offering duration
preparation buffers
completion buffers
scheduling policy
resource participation
capacity participation
```

Example:

```text
Monday–Friday
09:00–17:00

Break
12:30–13:30
```

These are **configuration constraints**.

They are not calendar events.

---

# 6. Working hours shall not become calendar records

Main Street shall not represent:

```text
09:00–17:00 Working Hours
```

as a Google Calendar event merely to communicate merchant operating configuration.

Likewise:

```text
12:30–13:30 Lunch Break
```

shall not be represented as an ordinary calendar event merely because it affects availability.

Instead:

```text
Configuration
      ↓
Scheduling Engine
      ↓
Valid / Invalid Calendar Surface
```

This distinction is mandatory.

---

# 7. Valid scheduling surface

The active scheduling configuration determines the merchant calendar's base scheduling validity.

Example:

```text
Working hours:
09:00–17:00

Regular break:
12:30–13:30
```

Derived surface:

```text
00:00–09:00     INVALID
09:00–12:30     VALID
12:30–13:30     INVALID
13:30–17:00     VALID
17:00–24:00     INVALID
```

The merchant-facing calendar shall visually distinguish invalid periods.

Invalid periods remain visible sufficiently to preserve calendar orientation but shall not behave as schedulable time.

---

# 8. Invalid calendar periods must expose reasons

An invalid period shall not appear merely as disabled space.

Main Street must be able to explain the governing reason.

Minimum reason vocabulary includes:

```text
OUTSIDE_WORKING_HOURS
RECURRING_BREAK
CLOSED_DAY
EXTENDS_BEYOND_WORKING_HOURS
```

Merchant-facing examples:

```text
Outside working hours

Regular break

Not a working day

This appointment would finish after working hours
```

Machine reason and presentation wording shall remain separate.

---

# 9. Calendar interaction cannot bypass invalidity

Within the merchant-facing calendar, an invalid period must not allow the merchant to bypass active configuration.

Prohibited operations include:

```text
create customer appointment
move customer appointment
resize customer appointment
propose customer appointment
create new schedulable commitment
```

where the resulting operation violates active scheduling constraints.

Example:

```text
Working hours:
09:00–17:00

Merchant attempts:
18:00 appointment
```

Required result:

```text
REJECTED

reason:
OUTSIDE_WORKING_HOURS
```

---

# 10. Configuration change is the only route to changing recurring constraints

A merchant may change:

```text
working hours
recurring breaks
```

through the appropriate configuration surface.

Example:

```text
Old:
09:00–17:00

New:
10:00–18:00
```

This is a configuration change governed by MS-PROT-040.

It is not an ad-hoc calendar override.

Hard invariant:

> **The merchant may change a scheduling rule, but may not bypass the currently active rule through calendar manipulation.**

---

# 11. Merchant schedule intent

A merchant schedule intent represents a specific, time-bound scheduling decision made by the merchant.

Initial required intent:

```text
UNAVAILABLE
```

Example:

```text
intent = UNAVAILABLE
title = "Dentist appointment"
date = 25 August
start = 14:00
end = 15:30
```

Its meaning is:

> This time would otherwise be schedulable, but the merchant does not want it made available for customer scheduling.

---

# 12. Merchant unavailability is not recurring configuration

This:

```text
Tuesday
14:00–15:30
"Dentist appointment"
```

does not modify:

```text
Tuesday working hours
```

If Tuesday normally remains:

```text
09:00–17:00
```

the configuration stays:

```text
09:00–17:00
```

The calendar intent overlays that particular date:

```text
09:00–14:00   potentially available
14:00–15:30   unavailable
15:30–17:00   potentially available
```

Once the period passes, no working-hours restoration is required.

---

# 13. Merchant-defined title

A merchant may assign a descriptive title to an `UNAVAILABLE` intent.

Examples:

```text
Dentist appointment
School run
Supplier meeting
Training
Bank appointment
Personal
```

The title is descriptive metadata.

It shall not determine semantic behaviour.

Therefore:

```text
intent = UNAVAILABLE
title = "Dentist appointment"
```

does **not** imply:

```text
customer appointment
customer relationship
booking
service
payment
notification
```

Hard invariant:

> **Intent determines scheduling meaning. Title provides merchant-facing description.**

---

# 14. Merchant unavailability has no customer relationship

A merchant-created `UNAVAILABLE` intent shall not require a customer.

Conceptually:

```text
ScheduleIntent
    merchant = M123
    intent = UNAVAILABLE
    title = "Dentist appointment"
    start = 14:00
    end = 15:30

customer = NONE
appointment = NONE
booking = NONE
```

This is valid.

---

# 15. Merchant unavailability should normally exist only over otherwise-valid scheduling time

Creating an `UNAVAILABLE` intent entirely within time already prohibited by configuration provides no additional scheduling meaning.

Example:

```text
Working hours end:
17:00

Merchant attempts:
UNAVAILABLE 19:00–20:00
```

Main Street need not permit this as a scheduling operation.

The period is already invalid.

The exact UX response is deferred, but Main Street shall not require artificial unavailability records to represent already-invalid periods.

---

# 16. Customer scheduled operation

A customer-related appointment or booking is fundamentally different from merchant unavailability.

Example:

```text
Customer:
C456

Service:
Oil Change

Appointment:
A789

Time:
14:00–15:00
```

The Main Street database owns the business relationship.

Conceptually:

```text
Merchant M123
      │
      ├── Customer C456
      │
      └── Appointment A789
                │
                ├── Customer C456
                ├── Offering Oil Change
                └── Scheduled interval
```

The calendar stores the corresponding schedule information.

---

# 17. Mandatory customer relationship invariant

> **Every calendar record representing customer activity must have a direct corresponding Main Street operational record and merchant-scoped customer relationship or customer context.**

Forbidden:

```text
Google Calendar

"John - haircut"
15:00–16:00

Main Street DB:
no customer
no appointment
no operation
```

This is not a valid Main Street customer appointment.

---

# 18. Customer relationship does not require an account

A customer may participate without having a persistent Main Street login.

For example:

```text
telephone booking
guest web booking
walk-in appointment entered by merchant
```

may produce:

```text
Merchant-scoped customer context
        ↓
Appointment
        ↓
Calendar representation
```

Therefore:

```text
customer relationship
        ≠
customer account
```

The requirement is relational integrity, not universal account registration.

---

# 19. Manual merchant-created customer appointment

When a customer contacts the merchant outside the customer-facing website—for example by telephone—the merchant may create an appointment manually.

The required sequence is conceptually:

```text
Merchant
    ↓
select/create customer context
    ↓
select offering/service
    ↓
select proposed time
    ↓
Main Street validates schedule
    ↓
Main Street creates appointment
    ↓
calendar representation created
```

The merchant must not bypass this relationship by creating an unrelated calendar event and treating it as a customer appointment.

---

# 20. Calendar representation

A calendar representation stores the scheduled-time information required for Main Street Calendar.

Conceptually, a customer-related representation requires correlation information sufficient to identify its corresponding Main Street business object.

For example:

```text
CalendarRecord
{
    merchant_scope
    calendar_record_id
    intent/type
    start
    end
    main_street_operation_reference
    customer_reference where applicable
}
```

The exact persisted metadata is implementation-specific.

The correlation requirement is not.

---

# 21. Calendar record classes

At minimum, Main Street Calendar must distinguish:

### A. Merchant schedule intent

```text
intent = UNAVAILABLE
customer = NONE
```

Example:

```text
"Dentist appointment"
```

### B. Customer scheduled operation

```text
intent = CUSTOMER_APPOINTMENT
customer = REQUIRED
operational_reference = REQUIRED
```

Example:

```text
"Oil change — Customer A"
```

Working hours and recurring breaks belong to neither class.

---

# 22. Google Calendar responsibility

Within Main Street architecture, Google Calendar is used as a schedule-information store.

It may hold information required for:

```text
calendar display
schedule occupancy
merchant schedule inspection
appointment timing
merchant unavailability
customer appointment representation
```

Google Calendar does not own Main Street business meaning.

---

# 23. No autonomous Google scheduling behaviour

All Main Street scheduling reads and writes are initiated through Main Street-controlled behaviour.

Conceptually:

```text
Merchant
    ↓
Main Street
    ↓
Google Calendar
```

or:

```text
Main Street Engine
    ↓
Google Calendar
```

Google Calendar shall not independently decide to:

```text
create a customer booking
cancel a Main Street booking
move an appointment
create capacity
change working hours
override breaks
change a customer relationship
```

In this architectural sense:

> **Google Calendar performs no autonomous Main Street business read/write decision.**

Main Street or the authorised merchant initiates the business action.

---

# 24. Main Street database responsibility

The Main Street database is authoritative for customer and business relationships.

This includes, where applicable:

```text
merchant
customer/customer context
appointment identity
booking identity
offering
business state
payment relationship
cancellation state
fulfilment state
authorisation
configuration provenance
```

Google Calendar shall not become the sole store for these semantics.

---

# 25. Calendar title is not a customer database

Main Street must never depend on parsing:

```text
"John - haircut"
```

to determine:

```text
customer = John
service = Haircut
```

Customer and operation relationships shall be explicit.

Titles are presentation information.

Business identity shall come from Main Street relationships and correlation metadata.

---

# 26. Scheduling engine inputs

For customer-selectable appointment scheduling, the Main Street scheduling engine evaluates relevant information from distinct sources.

Conceptually:

```text
Active configuration
    ├── working hours
    ├── recurring breaks
    ├── duration
    ├── buffers
    └── policies

Calendar information
    ├── merchant UNAVAILABLE intents
    └── customer scheduled occupancy

Main Street operational state
    ├── customer appointments
    ├── commitments
    └── relevant in-flight consistency state

Capacity/resource state
    ├── staff
    ├── rooms
    ├── equipment
    └── other applicable capacity

                ↓

        Availability Decision
```

No individual source defines availability universally.

---

# 27. Availability sequence

Conceptually:

```text
Candidate interval
        ↓
Inside configured working time?
        │
        ├── NO
        │    → INVALID
        │
        └── YES
              ↓
Intersects recurring break?
        │
        ├── YES
        │    → INVALID
        │
        └── NO
              ↓
Fits required duration/buffers?
        │
        ├── NO
        │    → INVALID / UNAVAILABLE
        │
        └── YES
              ↓
Merchant unavailable intent?
        │
        ├── YES
        │    → UNAVAILABLE
        │
        └── NO
              ↓
Conflicting customer commitment?
        │
        ├── YES
        │    → UNAVAILABLE
        │
        └── NO
              ↓
Required resources/capacity?
        │
        ├── unavailable
        │    → UNAVAILABLE
        │
        └── available
              ↓
          AVAILABLE
```

---

# 28. Available does not mean reserved

MS-PROT-006 remains authoritative:

> **An availability result is advisory.**

Therefore:

```text
AVAILABLE
    ≠
RESERVED
    ≠
ALLOCATED
    ≠
COMMITTED
```

A customer seeing a slot does not own it.

---

# 29. Final commitment must revalidate

Customer-selected scheduling shall perform authoritative revalidation immediately before commitment.

```text
Customer selects 14:00
        ↓
booking command
        ↓
re-read/re-evaluate current schedule
        ↓
revalidate configuration
        ↓
revalidate calendar occupancy
        ↓
revalidate resources/capacity
        ↓
commit if valid
```

An old availability calculation cannot authorise the commitment.

---

# 30. Google Calendar alone cannot provide concurrency correctness

Because Google Calendar is external infrastructure relative to Main Street's local business database, Main Street shall not assume that:

```text
calendar availability check
        +
calendar write
```

constitutes a transactional guarantee over Main Street business commitments.

The authoritative commitment boundary must remain within Main Street-controlled consistency logic.

This follows MS-PROT-025.

---

# 31. Customer commitment and calendar persistence are not one distributed transaction

Main Street cannot assume a single atomic transaction spanning:

```text
Main Street database
+
Google Calendar
```

Therefore the architecture must explicitly tolerate:

```text
DB succeeded / calendar write failed

calendar write succeeded / subsequent local processing failed

calendar timeout with uncertain result

duplicate calendar write request
```

These are consistency states to reconcile.

They must not be ignored.

---

# 32. Business commitment authority

The Main Street business record determines whether a customer business commitment exists.

Therefore:

```text
Appointment A789
state = CONFIRMED
```

is a Main Street business fact.

A Google Calendar record may represent that fact in the scheduling store.

The calendar record does not independently manufacture the commitment.

---

# 33. Preventing double booking during calendar propagation

If a Main Street appointment has become authoritatively committed but its calendar representation has not yet been successfully persisted or confirmed, Main Street must still prevent incompatible new commitments.

Therefore the availability/commitment engine shall consider authoritative Main Street commitment state in addition to externally persisted calendar occupancy where necessary.

Forbidden:

```text
Appointment committed in Main Street DB
        ↓
Google Calendar temporarily unavailable
        ↓
calendar doesn't yet show appointment
        ↓
Main Street treats slot as free
        ↓
second appointment committed
```

The architecture must prevent this.

---

# 34. Calendar representation consistency states

Customer-related calendar correlation may conceptually have states such as:

```text
PENDING
SYNCHRONISED
RETRY_REQUIRED
RECONCILIATION_REQUIRED
```

These need not become customer-facing appointment states.

They describe infrastructure consistency.

They must remain separate from:

```text
Appointment CONFIRMED
Appointment CANCELLED
Appointment COMPLETED
```

---

# 35. Calendar failure shall not silently change customer semantics

Suppose:

```text
Appointment confirmed
        ↓
Google Calendar temporarily unavailable
```

The system must not automatically reinterpret:

```text
CONFIRMED
```

as:

```text
NOT_BOOKED
```

merely because the calendar representation failed.

The calendar consistency problem must be handled separately.

Whether a particular command may be declared fully successful before calendar persistence is confirmed is an implementation/application-contract decision, but no implementation may allow schedule-integrity violations.

---

# 36. Calendar write idempotency

Calendar manipulation must tolerate retries.

A retry must not create:

```text
Appointment A789
→ Calendar Event 1
→ Calendar Event 2
→ Calendar Event 3
```

for one logical appointment.

Main Street must preserve sufficient stable correlation to recognise repeat operations.

Exact idempotency implementation is deferred.

The invariant is mandatory.

---

# 37. Calendar read reconciliation

Main Street must be capable of detecting inconsistencies such as:

```text
customer calendar record
but no corresponding Main Street appointment

Main Street appointment
but missing calendar record

calendar record references wrong merchant

calendar record references missing customer

duplicate calendar representations
```

Such conditions shall not be silently accepted as normal business truth.

---

# 38. Orphan customer calendar records

Definition:

```text
Customer-related calendar record
        +
no valid Main Street operational relationship
        =
ORPHAN
```

An orphan is an integration-consistency defect.

It shall not automatically become:

```text
new customer
new appointment
new booking
```

through inference.

Main Street must reconcile or quarantine it according to the eventual implementation strategy.

---

# 39. Missing customer calendar representation

Definition:

```text
Confirmed Main Street scheduled operation
        +
expected calendar representation absent
        =
MISSING REPRESENTATION
```

Main Street shall retain the underlying business commitment.

The scheduling engine must prevent the missing representation from producing false availability.

A reconciliation process shall restore the calendar representation where appropriate.

---

# 40. Merchant unavailability consistency

Merchant `UNAVAILABLE` intents do not carry customer relationships.

They may therefore have a simpler consistency model than customer appointments.

However, Main Street must still preserve their semantic intent sufficiently to distinguish them from customer operations.

A calendar title alone is insufficient.

Required semantic information includes at minimum:

```text
merchant scope
UNAVAILABLE intent
scheduled interval
```

---

# 41. Merchant calendar manipulation

The merchant may perform supported operations such as:

```text
create unavailability
modify unavailability
delete unavailability

create customer appointment
reschedule customer appointment
cancel customer appointment
```

Main Street must determine the semantic type of the operation before manipulating Google Calendar.

The calendar UI is not allowed to reduce all actions to:

```text
create/edit/delete generic event
```

because customer operations have additional business semantics.

---

# 42. Moving merchant unavailability

Example:

```text
Dentist appointment

14:00–15:00
        ↓
merchant moves
        ↓
15:00–16:00
```

Main Street validates that the resulting unavailability intent is meaningful within the calendar model and updates the calendar record.

No customer operation changes.

---

# 43. Moving a customer appointment

Example:

```text
Customer Appointment A789

14:00–15:00
        ↓
merchant drags to
        ↓
15:00–16:00
```

This is not merely a calendar-event update.

Required conceptual process:

```text
Merchant reschedule intent
        ↓
authorisation
        ↓
scheduling validation
        ↓
customer appointment operation
        ↓
Main Street business state updated
        ↓
calendar representation updated
```

If relevant policies require customer notification or acceptance, those semantics apply separately.

---

# 44. Deleting a customer calendar record

Deleting the visual calendar representation must not become an uncontrolled mechanism for deleting business commitments.

Main Street shall distinguish:

```text
remove merchant unavailability
```

from:

```text
cancel customer appointment
```

For customer operations, merchant action must execute the appropriate Main Street cancellation semantics.

A generic calendar delete must not bypass:

```text
cancellation rules
notifications
refund implications
customer history
audit/provenance
```

---

# 45. Main Street Calendar is a semantic UI

Although the merchant experiences a calendar interface, the interface is not merely a generic Google Calendar clone.

The UI shall understand enough Main Street semantics to distinguish at least:

```text
invalid time
available time
merchant unavailability
customer appointment
```

and applicable operation-specific conditions.

---

# 46. Merchant-facing schedule classifications

The initial merchant-facing conceptual classifications are:

| Classification  | Meaning                                                               |
| --------------- | --------------------------------------------------------------------- |
| **Invalid**     | Configuration prohibits scheduling                                    |
| **Available**   | Potentially schedulable under evaluated constraints                   |
| **Unavailable** | Normally valid time blocked by merchant intent or relevant constraint |
| **Occupied**    | Existing customer scheduled operation occupies the interval           |

The exact labels shown in UI may vary, but the semantics must remain distinguishable.

---

# 47. Reasons must remain precise

Examples:

```text
INVALID
reason = OUTSIDE_WORKING_HOURS

INVALID
reason = RECURRING_BREAK

UNAVAILABLE
reason = MERCHANT_UNAVAILABLE
title = "Dentist appointment"

UNAVAILABLE
reason = RESOURCE_UNAVAILABLE

OCCUPIED
reason = CUSTOMER_APPOINTMENT
appointment = A789
```

Do not flatten all non-available periods into:

```text
BUSY
```

internally.

The reason matters.

---

# 48. Merchant privacy

A merchant-defined title such as:

```text
Dentist appointment
```

is merchant information.

Main Street must not automatically expose it to customers.

Customer-facing availability may simply communicate:

```text
14:00 unavailable
```

The merchant-facing calendar may display:

```text
Dentist appointment
```

Exposure policy remains distinct from scheduling semantics.

---

# 49. Customer privacy

Customer-related calendar representation shall not expose unnecessary customer information merely because Google Calendar can store descriptive event data.

The calendar representation should contain only information required for the legitimate scheduling/merchant experience.

Main Street database remains the proper source for richer customer/business context.

Exact payload minimisation is deferred to implementation/security design.

---

# 50. Merchant scope

Every calendar record used by Main Street must be attributable to the correct merchant scope.

A schedule record belonging to:

```text
Merchant A
```

must not affect availability for:

```text
Merchant B
```

unless a future explicitly shared resource model establishes such a relationship.

Tenant isolation remains authoritative.

---

# 51. Calendar storage does not establish tenant ownership

The fact that two merchants might technically use Google infrastructure does not make their calendar information semantically shared.

Main Street merchant scope remains authoritative.

---

# 52. Existing appointment versus new configuration

As established in MS-PROT-040:

```text
Existing appointment:
16:00–17:00

Merchant later changes:
working hours end at 16:00
```

The existing appointment remains represented.

The new configuration prevents new appointments in that period.

The merchant calendar should be capable of exposing that an existing operation now conflicts with the current scheduling configuration.

It must not silently delete the calendar record.

---

# 53. Conflict reason

Conceptually:

```text
Appointment A789
16:00–17:00

Current scheduling configuration:
working hours end 16:00

status:
EXISTING_COMMITMENT_CONFLICT
```

This is different from saying the appointment never existed.

The merchant may then resolve it through appropriate business operations.

---

# 54. Calendar information shall not define working-hour history

Because working hours are configuration, historical operating rules should be recovered from configuration revision history where necessary.

Main Street shall not attempt to infer:

```text
"What were this merchant's working hours last month?"
```

from Google Calendar gaps.

Absence of events does not prove availability.

---

# 55. Calendar gaps are not automatically availability

This is a critical invariant.

```text
No Google Calendar event at 18:00
```

does **not** imply:

```text
18:00 available
```

because:

```text
working hours may have ended
breaks may apply
resource constraints may apply
offering duration may not fit
capacity may be exhausted elsewhere
```

Therefore:

> **Calendar emptiness is only one input to availability, never the definition of availability.**

---

# 56. Calendar occupancy is not automatically customer commitment

Likewise:

```text
Google Calendar occupied 14:00–15:00
```

does not automatically imply:

```text
customer booking exists
```

It may represent:

```text
merchant UNAVAILABLE intent
```

The record's Main Street intent and relationships determine meaning.

---

# 57. Falsification review

The proposed model was challenged against the following cases.

| Falsification case                                                 | Required result                                                               |
| ------------------------------------------------------------------ | ----------------------------------------------------------------------------- |
| Merchant works 09:00–17:00; calendar empty at 19:00                | 19:00 remains invalid                                                         |
| Recurring break 12:30–13:30; calendar contains no event            | Break remains invalid                                                         |
| Merchant creates "Dentist appointment"                             | `UNAVAILABLE`, no customer                                                    |
| Merchant title says "John appointment" but intent is `UNAVAILABLE` | No customer semantics inferred                                                |
| Customer phones and merchant books appointment                     | Customer context + Main Street appointment required                           |
| Customer appointment exists only in Google Calendar                | Invalid/orphan state                                                          |
| Customer appointment exists in DB but calendar write fails         | Business commitment retained; conflict prevention and reconciliation required |
| Calendar API retries appointment creation                          | Duplicate calendar records prohibited                                         |
| Merchant drags appointment into break                              | Rejected                                                                      |
| Merchant moves dentist unavailability                              | Merchant intent changes only                                                  |
| Merchant deletes customer appointment event                        | Must execute business cancellation semantics                                  |
| Merchant deletes unavailability                                    | Remove scheduling intent only                                                 |
| Calendar event missing after confirmed booking                     | Must not reopen capacity incorrectly                                          |
| Google Calendar has no event for free time                         | Full availability calculation still required                                  |
| New working hours conflict with old appointment                    | Appointment retained and conflict surfaced                                    |
| Customer appointment calendar record references wrong customer     | Consistency defect                                                            |
| Calendar record references wrong merchant                          | Tenant-integrity defect                                                       |
| Google infrastructure modifies nothing autonomously                | Main Street remains business authority                                        |

No falsifier requires rejection of the model.

---

# 58. Cross-domain validation

## 58.1 Online consultant

Configuration:

```text
09:00–17:00
break 12:30–13:30

Scheduling:
CUSTOMER_SELECTS_AVAILABLE_TIME
```

Calendar:

```text
10:00 Customer consultation
14:00 Dentist appointment — UNAVAILABLE
```

Main Street calculates:

```text
before 09:00      invalid
10:00             occupied
12:30–13:30       invalid
14:00             unavailable
free valid periods potentially available
after 17:00       invalid
```

**PASS**

---

## 58.2 Merchant-controlled solicitor

Scheduling:

```text
MERCHANT_PROPOSES_OR_CONFIRMS
```

Customer sends enquiry.

Merchant opens Main Street Calendar.

Main Street shows:

```text
configuration-derived invalid periods
existing customer commitments
merchant unavailable intents
remaining valid time
```

Merchant proposes a valid appointment.

The same calendar model works.

**PASS**

---

## 58.3 Mechanic

Oil Change:

```text
customer-selectable
duration = 60m
technician capacity applies
```

An empty calendar slot does not guarantee availability if the required technician/resource is unavailable.

Calendar + capacity + configuration are evaluated together.

**PASS**

---

## 58.4 Telephone mechanic booking

Customer calls.

Merchant creates/selects customer context and appointment.

Main Street validates current schedule and capacity.

The appointment is stored as a Main Street customer operation and represented in Google Calendar.

**PASS**

---

## 58.5 Merchant personal appointment

Merchant adds:

```text
UNAVAILABLE
"Dentist appointment"
15:00–16:30
```

No customer record is created.

Customer-visible scheduling excludes the period.

**PASS**

---

# 59. Accepted invariants

1. Main Street owns scheduling semantics.
2. Main Street owns customer and business relationships.
3. Google Calendar stores schedule information used by Main Street Calendar.
4. Google Calendar does not independently establish Main Street business behaviour.
5. All scheduling reads/writes originate from authorised Main Street or merchant actions through Main Street.
6. Working hours are configuration, not calendar events.
7. Recurring breaks are configuration, not calendar events.
8. Configuration defines the valid scheduling surface.
9. Invalid periods must expose merchant-readable reasons.
10. Calendar operations cannot bypass scheduling configuration.
11. Merchant temporary unavailability is a time-specific schedule intent.
12. Merchant unavailability may have a merchant-defined title.
13. Title does not determine semantic intent.
14. Merchant unavailability does not require a customer relationship.
15. Customer scheduled operations require Main Street operational records.
16. Every customer-related calendar record requires a corresponding merchant-scoped customer relationship or context.
17. Customer relationship does not require customer account registration.
18. Manual merchant-created customer appointments follow the same relational rules.
19. Calendar representations must carry stable correlation to customer operations where applicable.
20. Calendar emptiness does not imply availability.
21. Calendar occupancy does not automatically imply a customer booking.
22. Availability remains contextual and advisory.
23. Final commitment requires authoritative revalidation.
24. Google Calendar alone cannot enforce Main Street concurrency invariants.
25. Main Street DB and Google Calendar cannot be assumed to share one atomic transaction.
26. Calendar persistence failures must be detectable and reconcilable.
27. Confirmed business commitments must not be forgotten merely because calendar persistence fails.
28. Missing calendar representation must not cause duplicate commitment.
29. Customer-related orphan calendar records are invalid integration states.
30. Calendar write retries must not create duplicate schedule representations.
31. Customer appointment manipulation executes business semantics, not generic event editing.
32. Customer appointment deletion cannot bypass cancellation semantics.
33. Merchant unavailability manipulation remains separate from customer appointment manipulation.
34. Merchant titles and customer details remain subject to appropriate exposure/privacy boundaries.
35. Calendar records remain merchant-scoped.
36. Existing commitments survive later scheduling-configuration changes unless changed through explicit business operations.
37. Main Street must not reconstruct configuration truth from calendar gaps.
38. Main Street Calendar is a semantic operational surface, not merely a generic Google Calendar interface.

---

# 60. Deferred decisions

The following are intentionally deferred:

```text
exact Google Calendar account topology
one calendar vs multiple calendars per merchant
staff sub-calendar architecture
resource-calendar architecture
Google OAuth model
calendar record metadata format
correlation identifier storage
API retry strategy
outbox implementation
reconciliation frequency
calendar sync monitoring
calendar API quota handling
Google outage behaviour
offline merchant-calendar behaviour
timezone implementation
recurrence representation
holiday configuration
calendar migration
calendar archival
privacy payload minimisation
merchant calendar visual design
event colour semantics
calendar search
meeting-link creation
Google Meet integration
notification consequences of rescheduling
customer acceptance of merchant-proposed changes
```

No downstream implementation may violate the accepted invariants.

---

# 61. Governance review

## PROPOSE

A clear ownership and consistency boundary between Main Street configuration, Main Street business records, Main Street Calendar and Google Calendar storage was defined.

**PASS**

## REVIEW / FALSIFICATION

The model was tested against:

```text
calendar-only customer appointments
calendar write failure
duplicate API requests
merchant unavailability
recurring configuration
manual telephone bookings
calendar manipulation
stale calendar state
working-hour changes
resource constraints
customer privacy
tenant isolation
```

The model survives without requiring Google Calendar to become business authority.

**PASS**

## VALIDATE

The same model supports:

```text
customer-self-booking consultants
merchant-controlled professionals
mechanics
manual telephone appointments
merchant personal unavailability
```

without niche-specific calendar architecture.

**PASS**

## ACCEPT

No unresolved contradiction requires architectural revision.

**ACCEPTED**

---

# 62. Canonical decision

> **Main Street Calendar is the merchant-facing scheduling surface. Main Street owns scheduling semantics, customer relationships, business commitments, validation and all authorised manipulation. Google Calendar is the underlying store for schedule information and does not independently determine or alter Main Street business behaviour. Working hours and recurring breaks remain configuration constraints and appear in the merchant calendar only through the validity of the scheduling surface, including explicit reasons for invalid periods. Specific merchant unavailability is represented as a calendar scheduling intent such as `UNAVAILABLE`, optionally carrying a merchant-defined title such as “Dentist appointment”, and has no customer relationship. Any calendar information relating to a customer must correspond directly to a Main Street customer relationship or context and a Main Street operational appointment or booking record. Calendar emptiness does not define availability, calendar occupancy does not automatically define a customer commitment, and all final scheduling commitments must be concurrency-safe and authoritatively revalidated. Because Main Street's database and Google Calendar cannot be treated as one atomic transaction, calendar persistence failures, duplicates and orphaned representations must be explicitly detected and reconciled without compromising business commitments or reopening capacity incorrectly.**

**MS-PROT-041 is ACCEPTED.**

The next consequential specification should be **MS-PROT-042 — Appointment, Booking & Scheduling Operation Model**, defining the exact semantic difference between an appointment, a booking, a scheduling request, a merchant-proposed time, customer acceptance, confirmation, rescheduling and cancellation.
