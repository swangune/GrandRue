# MS-PROT-039 v1.2 — Provider-Neutral Calendar Onboarding & Scheduling Integration Amendment

**Document ID:** MS-PROT-039  
**Version:** 1.2  
**Status:** **ACCEPTED after cross-contract review and manual approval**  
**Amends:** MS-PROT-039 v1.1 — Adaptive Onboarding Question, Scheduling Decision & Configuration Resolution Model  
**Depends on:** MS-PROT-005, MS-PROT-006, MS-PROT-021, MS-PROT-022 v1.5, MS-PROT-023, MS-PROT-028, MS-PROT-038, MS-PROT-041 v1.1, MS-PROT-047, MS-PROT-048 v1.1, MS-PROT-050 v1.1  
**Purpose:** Align onboarding and scheduling-discovery semantics with the provider-neutral Main Street Calendar model so external calendar connection remains optional integration configuration rather than a prerequisite for Scheduling, Appointment or Main Street Calendar operation.

---

## 1. Governing amendment

MS-PROT-039 v1.1 remains accepted except where this amendment changes calendar-storage assumptions and external-calendar onboarding behaviour.

The governing decision is:

> **Onboarding shall discover and configure Main Street scheduling semantics independently of external calendar providers. External calendar connection may be offered only as an optional integration decision after the relevant Main Street scheduling semantics are understood, unless existing external-calendar evidence is being used solely as advisory onboarding evidence.**

Canonical flow:

```text
Merchant intent
        ↓
Is appointment-style scheduling applicable?
        ↓
Scheduling semantics/configuration
        ↓
Main Street Calendar becomes applicable where required
        ↓
Optional integration questions
        ├── mirror Main Street schedule externally?
        └── use external busy time as availability constraint?
```

Rejected ordering:

```text
Connect Google Calendar
        ↓
therefore Scheduling exists
        ↓
therefore Appointment exists
```

---

## 2. Responsibility-table correction

MS-PROT-039 v1.1 assigned:

```text
Calendar information persistence
    → Google Calendar-backed calendar infrastructure
```

That statement is superseded.

The corrected responsibility model is:

| Concern | Authority |
| --- | --- |
| Merchant business intent | Merchant |
| Interpretation of merchant evidence | Inference |
| Supported semantic vocabulary | Main Street Semantic Registry |
| Capability relationships | Main Street Semantic Registry |
| Merchant-selectable bounded choices | Registered Main Street definitions |
| Configuration structural validity | Compiler |
| Runtime business validity | Runtime/application semantics |
| Public Business Hours | Merchant profile/configuration under MS-PROT-050 |
| Scheduling operating configuration | Main Street configuration |
| Scheduling calculation | Main Street scheduling engine |
| Appointment/Booking commitment | Main Street owning capability semantics |
| Customer/business relationship | Main Street operational state |
| Main Street Calendar read representation | Main Street projection |
| Main Street Calendar interaction | Main Street authorised command surface |
| External calendar records | External provider for its own records |
| External calendar integration interpretation | Main Street registered integration contract |
| External meeting delivery | Separate fulfilment/integration concern |

Hard rule:

> **No external calendar provider is an authority required to establish Main Street Scheduling or Appointment semantics.**

---

## 3. Main Street Calendar is not an onboarding dependency

A merchant can complete scheduling onboarding without connecting any external calendar.

Example:

```text
Merchant:
"Customers choose consultation times on my website."
        ↓
Appointment Scheduling applicable
        ↓
CUSTOMER_SELECTS_AVAILABLE_TIME
        ↓
configure scheduling constraints
        ↓
Main Street Calendar available to merchant
```

No OAuth/provider-selection step is required in this path.

Therefore:

```text
Scheduling complete
        ≠
external calendar connected
```

and:

```text
Main Street Calendar usable
        ≠
Google Calendar account required
```

---

## 4. Integration questions are downstream questions

External calendar questions normally have lower dependency priority than semantic scheduling questions.

The default sequence is:

```text
1. Does appointment-style scheduling apply?
2. Which scheduling authority mode applies?
3. Which contexts/offerings use that mode?
4. What scheduling constraints are required?
5. What capacity/resources participate?
6. Only then: are external calendar integrations useful?
```

This follows the existing high-information-first rule.

A provider question shall not be asked merely because Scheduling exists.

It should be asked only when:

```text
merchant explicitly requests integration
OR
existing evidence indicates an external calendar is relevant
OR
integration materially changes the selected operating experience
```

---

## 5. External calendar connection is not capability activation

Connecting an external calendar may be evidence that scheduling is likely relevant, but it does not activate Scheduling.

Canonical:

```text
Merchant connects/imports calendar evidence
        ↓
Inference may propose Scheduling
        ↓
merchant/registered relationships resolve semantics
        ↓
compiler validates candidate configuration
```

Rejected:

```text
Calendar connected
        ↓
Scheduling automatically active
```

MS-PROT-038 and MS-PROT-048 remain authoritative.

---

## 6. Two independent optional integration decisions

Onboarding must not collapse external calendar integration into a single ambiguous `CONNECT_CALENDAR` decision.

At minimum, two materially distinct intentions exist.

### 6.1 Outbound projection intention

Business-facing meaning:

> **Would you like Main Street appointments or schedule information to appear in an external calendar you use?**

Semantic consequence:

```text
Main Street scheduling truth
        ↓
external projection integration applicable
```

This does not change the validity of Main Street scheduling itself.

### 6.2 Inbound busy-time intention

Business-facing meaning:

> **Should busy periods from an external calendar reduce the appointment times Main Street offers to customers?**

Semantic consequence:

```text
external busy-time evidence
        ↓
registered availability constraint integration
```

This does not convert external events into Main Street customer Appointments.

The merchant may choose either, both, or neither where supported.

---

## 7. No generic two-way-sync choice

Main Street shall not present a single option such as:

```text
Two-way calendar sync = ON
```

unless a future registered integration contract defines exactly what each direction means.

Why:

```text
outbound projection
        ≠
inbound availability constraint
        ≠
external mutation authority
```

A future external-edit-to-Main-Street rescheduling feature would require its own explicit authority and validation contract.

---

## 8. External calendar provider selection is fulfilment/integration configuration

Provider identity is not a Scheduling policy value.

Conceptually:

```text
Scheduling semantics/configuration
        ↓
optional calendar integration role applicable
        ↓
supported provider binding
```

Provider choice therefore follows MS-PROT-048 rather than being embedded in capability meaning.

Onboarding may use business-facing provider names where the merchant needs to choose or authorise a connection, but the internal semantic model remains provider-neutral.

---

## 9. Provider connection lifecycle is separate from onboarding decision meaning

These are different:

```text
Merchant wants external calendar projection
        ≠
provider currently connected
```

A merchant may select an intended integration but still need to authorise credentials later.

Likewise:

```text
Provider token expired
        ≠
merchant no longer wants the integration
        ≠
Scheduling deactivated
```

Onboarding/reconfiguration therefore records intent/configuration separately from mutable connection health.

---

## 10. Existing provider evidence may reduce questions

If a merchant voluntarily connects or references an external calendar before scheduling discovery is complete, Main Street may use that fact as evidence.

Example:

```text
Merchant connects external calendar
and says:
"I use this for client meetings."
```

Inference may propose:

```text
Appointment Scheduling likely
external busy-time import likely relevant
```

But where a material merchant decision remains unresolved, structured confirmation still applies.

The external system does not become semantic authority merely because it supplied useful evidence.

---

## 11. Scheduling configuration terminology alignment

MS-PROT-039 v1.1 uses `working hours` in scheduling examples.

Following MS-PROT-050 v1.1, Public Business Hours and Scheduling operating constraints are distinct.

Where MS-PROT-039 refers to working hours as an Appointment-validity constraint, interpret the term as:

```text
Scheduling operating-time constraint
```

unless the applicable configuration explicitly derives that scheduling basis from Public Business Hours.

Therefore:

```text
Public Business Hours
        ≠
Scheduling operating windows
```

although Scheduling must evaluate Public Business Hours where applicable under MS-PROT-050.

This amendment does not reopen the accepted MS-PROT-050 rules.

---

## 12. Question catalogue implications

The eventual onboarding question catalogue should model external calendar questions as reusable integration-family questions, not business-category questions.

Examples:

```text
External calendar projection relevant?
External busy-time import relevant?
Which supported external calendar connection?
Which calendars/sources are in scope?
```

These questions should appear only after applicability is established.

They shall not be duplicated as:

```text
Salon Google Calendar question
Consultant Google Calendar question
Mechanic Google Calendar question
```

---

## 13. Question applicability examples

### No Scheduling

```text
Publication + Enquiry merchant
Scheduling = NOT_APPLICABLE
```

Result:

- no appointment-calendar questions;
- no external scheduling-calendar questions merely because the merchant has a Google account.

### Scheduling with no integration

```text
Scheduling active
Merchant uses only Main Street Calendar
```

Result:

- scheduling onboarding completes;
- external-calendar integration questions may be skipped or declined;
- no functional limitation in core Scheduling follows from that choice.

### Scheduling with outbound projection only

```text
Scheduling active
External projection = YES
Busy-time import = NO
```

Result:

- Main Street appointments may appear externally;
- arbitrary external events do not automatically block Main Street availability unless another constraint makes them relevant.

### Scheduling with inbound busy-time only

```text
Scheduling active
External projection = NO
Busy-time import = YES
```

Result:

- imported busy periods may narrow appointment availability;
- Main Street need not mirror its own appointments externally under this integration choice.

---

## 14. Merchant-facing review

Configuration review should describe outcomes rather than infrastructure topology.

Example:

```text
Customers can choose available appointment times.

Your Main Street calendar will show appointments and blocked periods.

External calendar:
✓ Use busy periods from your connected calendar when calculating availability
✓ Add confirmed Main Street appointments to your connected calendar
```

If no external integration is selected:

```text
External calendar: Not connected
```

shall not be presented as a scheduling defect.

---

## 15. Configuration evolution

A merchant may add or remove external calendar integration without changing the semantic identity of existing Scheduling or Appointment capability.

Example:

```text
Phase 1
Main Street Scheduling only

Phase 2
+ external busy-time import

Phase 3
+ outbound projection
```

This is integration evolution around stable business semantics.

Removing the provider later does not erase existing Appointments.

---

## 16. Failure/recovery onboarding implications

If a previously configured external provider connection is unhealthy, the merchant should be guided through integration repair rather than being re-onboarded into Scheduling.

Correct:

```text
Scheduling remains active
External calendar connection requires attention
```

Rejected:

```text
Calendar disconnected
→ ask merchant whether they still offer appointments
```

unless independent merchant evidence actually changes that business decision.

---

## 17. Cross-domain falsification

### Information publisher

```text
Publication + Enquiry
No Scheduling
```

No external calendar requirement appears.

**PASS**

### Online consultant using only Main Street

```text
Consultation + Scheduling + Appointment
No external calendar
```

Scheduling onboarding completes and Main Street Calendar operates.

**PASS**

### Online consultant with personal Google Calendar

The consultant wants personal busy periods considered but does not want Main Street events mirrored externally.

Result:

```text
busy-time import = YES
outbound projection = NO
```

**PASS**

### Salon using outbound integration

Merchant wants confirmed appointments visible in an external calendar.

Result:

```text
outbound projection = YES
```

Provider selection does not redefine Appointment semantics.

**PASS**

### Mechanic with mixed scheduling contexts

Some services are appointment-based; parts sales are not.

External calendar questions are scoped only to scheduling-relevant contexts/integration requirements, not to the merchant category as a whole.

**PASS**

### External provider disconnects after onboarding

Scheduling configuration remains valid; connection health becomes integration attention, not semantic deactivation.

**PASS**

---

## 18. Rejected approaches

| Rejected approach | Why it fails |
| --- | --- |
| Require Google Calendar during Scheduling onboarding | Makes external provider a semantic prerequisite |
| Ask provider question before determining Scheduling applicability | Reverses dependency order and increases friction |
| Calendar connection activates Scheduling | Gives integration evidence semantic authority |
| One `CONNECT_CALENDAR` flag | Hides distinct inbound/outbound authority |
| Generic two-way sync | Conceals mutation authority and conflict semantics |
| Treat provider connection health as configuration meaning | Mixes operational connection state with merchant intent |
| Re-ask scheduling questions when OAuth expires | Confuses integration repair with business reconfiguration |
| Use business-category calendar questionnaires | Reintroduces vertical templates |
| Treat Public Business Hours as identical to Scheduling working windows | Conflicts with MS-PROT-050 |

---

## 19. Accepted invariants

1. Scheduling onboarding is provider-independent.
2. Main Street Calendar requires no external calendar connection.
3. External calendar connection cannot activate Scheduling, Appointment or Booking semantics.
4. External calendar evidence may inform inference but remains advisory.
5. Integration questions occur only when materially applicable.
6. Outbound projection and inbound busy-time import are distinct decisions.
7. A generic two-way-sync authority is not assumed.
8. Provider identity belongs to fulfilment/integration configuration, not Scheduling semantics.
9. Provider connection health is operational state, not capability activation state.
10. External busy events may constrain availability only through registered interpretation.
11. Imported external events do not create Main Street customer relationships or Appointments.
12. External provider disconnection does not invalidate Main Street Calendar or existing Appointments.
13. Public Business Hours and Scheduling operating-time constraints remain distinct under MS-PROT-050.
14. Integration questions are reusable across merchant types and are not category templates.
15. Merchant review describes business/integration outcomes rather than internal graph topology.
16. External calendar integration may evolve independently of Scheduling semantic identity.

---

## 20. Continuous-improvement checkpoint

Question:

> **What could we have done better?**

MS-PROT-039 v1.1 correctly separated Scheduling semantics from many implementation details but retained an infrastructure assumption that Calendar information persistence belonged to Google Calendar-backed infrastructure.

The later provider contract and Appointment model reveal that this was stronger than necessary. Scheduling discovery should complete against Main Street-owned semantics first; external calendar use is optional integration configuration.

A second improvement is to split external-calendar intent into outbound projection and inbound busy-time import. This prevents a superficially convenient `sync` option from hiding materially different authority and failure semantics.

A third coherence improvement is to align `working hours` terminology with MS-PROT-050 so Public Business Hours are not silently collapsed into Scheduling operating windows.

No further material issue remains within this bounded alignment amendment after cross-domain falsification.

---

## Governance verdict

**ACCEPTED.**

MS-PROT-039 v1.2 is the current authority where onboarding intersects Scheduling, Main Street Calendar and external calendar integration. MS-PROT-039 v1.1 remains historical accepted evidence and continues to govern adaptive question selection, provenance, branch pruning, scheduling modes and context scoping except where v1.2 explicitly amends provider/calendar assumptions.
