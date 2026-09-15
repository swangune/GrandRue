# MS-PROT-041 v1.1 — Main Street Calendar Projection & External Calendar Integration Amendment

**Document ID:** MS-PROT-041  
**Version:** 1.1  
**Status:** **ACCEPTED after continuous-improvement review and manual approval**  
**Amends:** MS-PROT-041 v1.0 — Main Street Calendar, Scheduling Storage & Appointment Consistency Model  
**Depends on:** MS-PROT-006, MS-PROT-022 v1.5, MS-PROT-023, MS-PROT-025, MS-PROT-026, MS-PROT-027, MS-PROT-028, MS-PROT-039 v1.2, MS-PROT-040, MS-PROT-042 v1.2, MS-PROT-048 v1.1, MS-PROT-049  
**Purpose:** Remove the architectural requirement that Main Street Calendar be backed by Google Calendar or any second canonical schedule-information store, while preserving the valid scheduling, correlation, manipulation, privacy and consistency invariants established by MS-PROT-041 v1.0.

---

## 1. Governing amendment

MS-PROT-041 v1.0 remains accepted except where this amendment changes or clarifies calendar storage and external-provider authority.

The governing decision is:

> **Main Street operational state is authoritative for Main Street scheduling commitments and merchant schedule intents. Main Street Calendar is a projection and authorised manipulation surface over that state. An external calendar is optional integration infrastructure; no external calendar is required to serve as Main Street's canonical schedule-information store.**

Canonical architecture:

```text
Main Street configuration
        +
Main Street operational state
    ├── Appointment
    ├── applicable Booking timing
    ├── Merchant Schedule Intent
    ├── resources / capacity
    └── relevant external constraints
        ↓
Scheduling evaluation
        +
Calendar read projection
        ↓
Main Street Calendar
        │
        ├── merchant interaction
        └── optional external calendar integration
                ├── outbound projection/synchronisation
                └── inbound busy-time constraint import
```

Hard rule:

> **The existence, correctness and operability of Main Street Calendar shall not depend on a merchant connecting Google Calendar or any other external calendar provider.**

---

## 2. Authority correction

MS-PROT-041 v1.0 correctly established that Google Calendar is not Main Street's semantic authority, but it still treated Google Calendar as the schedule-information store used by Main Street Calendar.

That is superseded.

The corrected authority model is:

| Concern | Authority |
| --- | --- |
| Scheduling semantics | Main Street |
| Appointment commitment | Main Street capability-owned operational state |
| Booking commitment | Main Street capability-owned operational state |
| Merchant Schedule Intent | Main Street operational state |
| Working hours / recurring constraints | Main Street configuration |
| Resource/capacity truth | Main Street owning semantics |
| Main Street Calendar read model | Main Street projection |
| Main Street Calendar mutation requests | Main Street command/runtime authority |
| External calendar account / external event | External provider for its own external fact |
| Mapping external evidence into Main Street consequences | Main Street registered interpretation |

An external calendar provider may own its own event record.

It does not own what that record means inside Main Street.

---

## 3. Main Street Calendar is a projection, not a second domain model

Main Street Calendar shall project authoritative Main Street state into a calendar-shaped operational view.

Conceptually:

```text
Appointment A789
scheduled interval = 14:00–15:00
        ↓
Calendar projection
        ↓
Merchant sees 14:00–15:00 appointment
```

Likewise:

```text
Merchant Schedule Intent
UNAVAILABLE
14:00–15:30
        ↓
Calendar projection
        ↓
Merchant sees blocked period
```

The projection may combine:

```text
configuration validity
appointments
merchant schedule intents
resource/capacity constraints
external busy-time constraints where enabled
```

but it shall not create new business semantics.

Hard rule:

> **Calendar representation is derived representation. The authoritative operational objects and configuration remain the source of business truth.**

---

## 4. No mandatory duplicate schedule store

Main Street shall not require this architecture:

```text
Main Street Appointment
        +
mandatory external Calendar Event
```

merely so Main Street Calendar can operate.

The default architecture is:

```text
Main Street Appointment
        ↓
Main Street Calendar projection
```

This removes an otherwise unnecessary second persistence authority and avoids making ordinary scheduling depend on:

```text
external API availability
OAuth state
provider quotas
provider event correlation
external write latency
cross-system reconciliation
```

Those concerns exist only when an external integration is actually enabled.

---

## 5. Existing scheduling distinctions remain authoritative

The following MS-PROT-041 v1.0 distinctions remain unchanged:

```text
OPERATING CONFIGURATION
        ≠
MERCHANT SCHEDULE INTENT
        ≠
CUSTOMER SCHEDULED OPERATION
        ≠
CALENDAR REPRESENTATION
```

Working hours and recurring breaks remain configuration rather than ordinary calendar events.

Merchant one-off unavailability remains distinct from recurring configuration.

A customer Appointment remains distinct from merchant unavailability.

Calendar titles remain descriptive metadata rather than semantic authority.

---

## 6. Main Street Calendar command boundary

Although the calendar is a projection, merchant interaction with it may initiate authoritative commands.

Example:

```text
Merchant drags Appointment A789
14:00–15:00 → 15:00–16:00
        ↓
RescheduleAppointment request
        ↓
authorisation
        ↓
scheduling validation
        ↓
capability-owned mutation
        ↓
updated Appointment
        ↓
calendar projection refresh
```

The UI never directly mutates a projected event.

Likewise:

```text
merchant deletes visual appointment
```

must resolve to the appropriate cancellation operation rather than generic record deletion.

Hard rule:

> **A calendar gesture may request a semantic operation; it is never itself mutation authority.**

---

## 7. Availability inputs

The scheduling engine shall derive availability from authoritative sources appropriate to the operation.

Conceptually:

```text
Active scheduling configuration
    ├── Business Hours where applicable
    ├── scheduling operating windows
    ├── recurring breaks
    ├── duration / buffers
    └── policy/configuration

Main Street operational state
    ├── Merchant Schedule Intents
    ├── Appointments
    ├── applicable commitments
    └── relevant in-flight consistency facts

Resource/capacity state
    ├── staff
    ├── rooms
    ├── equipment
    └── other applicable capacity

Optional imported external constraints
    └── external busy-time evidence

        ↓
Appointment availability
```

External calendar occupancy is therefore optional input, not a required store of Main Street occupancy.

---

## 8. External calendar integration roles

External calendar participation is separated into two optional roles.

### 8.1 Outbound calendar projection

Purpose:

> Mirror selected Main Street schedule information into an external calendar for merchant convenience/interoperability.

Canonical flow:

```text
Main Street authoritative scheduling fact
        ↓
durable integration delivery
        ↓
registered external calendar adapter
        ↓
external calendar event
```

Examples:

```text
Appointment confirmed → mirror event externally
Appointment rescheduled → update mirrored event
Appointment cancelled → cancel/remove mirrored representation
Merchant unavailability → optionally mirror blocked time
```

Provider failure affects external representation health, not the Main Street commitment.

### 8.2 Inbound busy-time constraint import

Purpose:

> Allow merchant-authorised external events to reduce Main Street scheduling availability without granting the external calendar authority to create Main Street customer commitments.

Canonical flow:

```text
Authenticated external calendar evidence
        ↓
merchant/provider scope resolution
        ↓
registered busy-time interpretation
        ↓
ExternalBusyConstraint
        ↓
scheduling availability calculation
```

An imported external event may therefore mean:

```text
this interval should not be offered for new appointments
```

without meaning:

```text
create Appointment
create CustomerContext
create Booking
create payment obligation
```

Hard rule:

> **External busy-time import may constrain availability; it shall not manufacture Main Street business relationships.**

---

## 9. External events are external facts

An external provider is authoritative for whether its own event exists and for provider-owned event attributes.

Main Street decides what, if anything, those external facts mean under a registered integration contract.

Rejected:

```text
Google event title = "John haircut"
        ↓
Main Street creates Customer John + Appointment
```

Accepted:

```text
external event occupies 14:00–15:00
        ↓
registered busy-time import policy
        ↓
14:00–15:00 excluded from new Main Street appointment availability
```

unless the event was itself created from a correlated Main Street object through outbound projection.

---

## 10. Correlation applies only where integration exists

MS-PROT-041 v1.0 required stable correlation between Main Street scheduled operations and external calendar records.

That requirement remains valid **when an external representation exists**.

For outbound representations, Main Street must retain enough information to determine:

```text
merchant scope
Main Street source object
external provider binding/connection
external event identity
projection/integration state
```

The exact storage form remains an implementation decision.

No correlation record is required merely for the internal Main Street Calendar projection.

---

## 11. External consistency states are integration states

Where external projection/synchronisation is enabled, states such as:

```text
PENDING
SYNCHRONISED
RETRY_REQUIRED
RECONCILIATION_REQUIRED
```

remain valid integration-consistency concepts.

They shall not become Appointment lifecycle states.

Canonical separation:

```text
Appointment = CONFIRMED
External calendar projection = RETRY_REQUIRED
```

Main Street Calendar remains able to display the confirmed Appointment because its source is Main Street operational state.

---

## 12. External integration failure

External calendar failure shall not make Main Street Calendar unavailable or erase Main Street business truth.

Example:

```text
Appointment confirmed
        ↓
Google Calendar unavailable
```

Required result:

```text
Appointment = CONFIRMED
Main Street Calendar = shows Appointment
External projection = DEGRADED / RETRY_REQUIRED
```

Likewise, loss of inbound calendar connectivity means imported external busy-time evidence may become stale or unavailable.

The applicable integration contract must define how stale external constraints are treated; Main Street must not silently assume freshness it cannot establish.

---

## 13. External mutation authority

An external calendar shall not autonomously mutate Main Street business commitments merely because a merchant edits an external event.

Default rule:

```text
external event changed
        ↓
provider evidence received
        ↓
registered interpretation
        ↓
no Main Street business mutation unless an explicit supported integration contract authorises a corresponding command path
```

Any future bidirectional appointment-edit integration must define:

```text
authorisation
correlation
conflict handling
policy validation
scheduling revalidation
uncertain outcome handling
customer consequences
```

before external edits may request Main Street mutations.

No generic two-way sync authority is assumed.

---

## 14. Provider selection and Open-First alignment

MS-PROT-048 v1.1 is authoritative for fulfilment/provider selection.

Main Street Calendar core operation requires no paid external calendar service.

External calendar interoperability, when desired, sits behind Main Street-owned integration/fulfilment boundaries.

Conceptually:

```text
Main Street Calendar core
        → Main Street-owned operational/projection logic

External calendar integration role
        ↓
Main Street-owned contract
        ↓
provider adapter where selected
```

No provider name becomes part of Scheduling capability semantics.

---

## 15. Google Calendar status after this amendment

Google Calendar remains a legitimate supported external provider candidate where Main Street chooses to support it.

It may provide:

```text
external event projection
merchant calendar interoperability
optional busy-time import
provider-native notifications/reminders where explicitly used
external ecosystem convenience
```

It is no longer:

```text
mandatory Main Street Calendar backing store
required scheduling dependency
semantic authority
required onboarding prerequisite
```

MS-PROT-041 v1.0 rules concerning Google event correlation, privacy, retries, uncertainty, idempotency and reconciliation remain applicable to Google Calendar **when that integration is enabled**, except where they assumed Google Calendar was the canonical Main Street schedule store.

---

## 16. Privacy and minimisation

External projection shall use only the minimum information required for the merchant's selected integration experience.

A Main Street Appointment may contain richer customer/business relationships than an external calendar needs.

Therefore:

```text
Main Street operational record
        ↓ data-minimised projection
external calendar event
```

not:

```text
copy entire customer/business object into provider event
```

Merchant-only Schedule Intent titles remain private unless explicitly required and permitted by the integration contract.

---

## 17. Merchant scope

Every Main Street calendar projection, imported external constraint and external representation must remain merchant-scoped.

An external account containing events for multiple contexts does not weaken tenant isolation.

A provider event may affect Main Street scheduling only after Main Street resolves:

```text
provider connection
merchant scope
calendar/source scope
applicable import contract
```

---

## 18. Existing commitments and configuration evolution

MS-PROT-040 remains authoritative.

Changing configuration or disconnecting an external calendar shall not rewrite historical Appointment/Booking truth.

Disconnecting an external calendar means:

```text
external integration stops/degrades
```

not:

```text
existing Main Street appointments disappear
```

Likewise, switching external providers changes integration routing, not the semantic meaning of historical Main Street commitments.

---

## 19. Cross-domain falsification

### Consultant with no external calendar

```text
Scheduling + Appointment active
No Google account connected
```

Result:

- Main Street calculates availability;
- Main Street Calendar displays Appointments and unavailability;
- merchant can operate normally;
- no external provider is required.

**PASS**

### Consultant with Google Calendar outbound sync

```text
Appointment confirmed in Main Street
Google projection enabled
```

Result:

- Main Street Calendar shows Appointment immediately from local truth;
- external representation is created asynchronously;
- Google outage does not invalidate Appointment.

**PASS**

### Consultant importing personal calendar busy time

```text
External dentist event 14:00–15:00
busy-time import enabled
```

Result:

- interval may reduce appointment availability;
- no Main Street customer or Appointment is fabricated;
- external title need not be exposed to customers.

**PASS**

### External event edited manually

An externally mirrored event is moved from 14:00 to 16:00 in the provider UI.

Result:

- Main Street Appointment is not silently rescheduled;
- any future supported bidirectional contract must explicitly process that evidence through Main Street validation.

**PASS**

### Provider disconnected

```text
Main Street has future Appointments
External calendar connection expires
```

Result:

- Appointments remain valid;
- Main Street Calendar remains correct;
- integration is degraded;
- external representation/import behaviour follows integration failure contract.

**PASS**

### Motel Booking without Appointment scheduling

Date-range Booking exists without appointment-style scheduling.

Result:

- Booking timing remains owned by Booking semantics;
- Main Street does not force it into Appointment Calendar merely because dates exist.

**PASS**

---

## 20. Rejected approaches

| Rejected approach | Why it fails |
| --- | --- |
| Google Calendar is mandatory schedule store | Creates unnecessary external dependency and duplicate consistency problem |
| Replace Google with another mandatory provider | Changes vendor but preserves the architectural mistake |
| Maintain a second canonical generic calendar database separate from operational objects | Duplicates timing truth without demonstrated need |
| Calendar UI writes generic events directly | Bypasses semantic command authority |
| Imported event automatically becomes Appointment | Gives external provider business-semantic authority |
| External edit silently reschedules Main Street Appointment | Bypasses scheduling, policy and customer consequences |
| Provider outage hides Main Street Appointment | Makes projection correctness depend on integration health |
| Storefront/customer scheduling reads external calendar as sole truth | Violates authoritative Main Street commitment boundary |
| Treat every timed Booking as Appointment Calendar content | Collapses Booking and Appointment semantics |

---

## 21. Accepted invariants

1. Main Street operational state is authoritative for Main Street scheduling commitments and schedule intents.
2. Main Street Calendar is a projection and authorised manipulation surface over authoritative Main Street state.
3. No external calendar provider is required for Main Street Calendar to operate.
4. Working hours and recurring breaks remain configuration, not ordinary calendar records.
5. Appointment and Booking retain their distinct capability-owned semantics.
6. Calendar gestures request semantic commands; they do not mutate projection state directly.
7. External calendar integration is optional and provider-neutral at the semantic boundary.
8. Outbound external calendar projection does not become business authority.
9. Inbound external busy-time import may constrain availability but cannot manufacture customer/business relationships.
10. External event titles are not semantic parsers.
11. Correlation/idempotency/reconciliation apply where external representations exist, not to the internal calendar projection itself.
12. External projection health is separate from Appointment/Booking lifecycle state.
13. Provider outage does not make Main Street Calendar or existing commitments semantically unavailable.
14. External edits do not mutate Main Street commitments without an explicit registered integration command contract.
15. Merchant scope applies to all projected, imported and externally represented schedule information.
16. External payloads shall be data-minimised.
17. Provider binding changes do not rewrite historical Main Street commitments.
18. Main Street Calendar remains usable by merchants who connect no external calendar.

---

## 22. Continuous-improvement checkpoint

Question:

> **What could we have done better?**

MS-PROT-041 v1.0 answered how Main Street could use Google Calendar without surrendering semantic authority. The stronger question is whether Main Street needs Google Calendar as its schedule-information store at all.

Because Appointment already owns its scheduled interval and Main Street owns Merchant Schedule Intent, configuration and commitment authority, requiring a second external schedule store creates synchronisation work without adding semantic authority.

The improvement is therefore not merely to make Google Calendar replaceable. It is to remove an external calendar store as a prerequisite of the Main Street Calendar architecture.

A second improvement is to distinguish outbound external projection from inbound busy-time import. These integrations have different authority and failure semantics and must not be collapsed into a generic two-way calendar sync.

No further material issue remains within this bounded amendment after cross-domain falsification.

---

## Governance verdict

**ACCEPTED.**

MS-PROT-041 v1.1 is the current authority for Main Street Calendar source-of-truth, projection, manipulation and external calendar participation. MS-PROT-041 v1.0 remains historical accepted evidence and continues to govern unchanged scheduling distinctions and provider-consistency rules where an external calendar integration actually exists.
