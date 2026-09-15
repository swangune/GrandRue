# MS-PROT-050 v1.1 — Business Operating Hours, Scheduling-Time & Digital Contactability Amendment

**Document ID:** MS-PROT-050  
**Version:** 1.1  
**Status:** **ACCEPTED after continuous-improvement review and manual approval**  
**Amends:** MS-PROT-050 v1.0  
**Depends on:** MS-PROT-021 v1.3, MS-PROT-022 v1.5, MS-PROT-027, MS-PROT-036 v1.1, MS-PROT-039 v1.1, MS-PROT-040, MS-PROT-041, MS-PROT-043 v1.2, MS-PROT-047 v1.0, MS-PROT-049 v1.0  
**Purpose:** Correct and refine the ownership, lifecycle, temporal and customer-experience boundaries established by MS-PROT-050 v1.0 while preserving its accepted separation between Business Hours, Scheduling, resource/staff availability, one-off unavailability and Enquiry.

---

## 1. Governing amendment

MS-PROT-050 v1.0 remains accepted except where this amendment changes or clarifies it.

The following distinctions remain authoritative:

```text
PUBLIC BUSINESS HOURS
        ≠
SCHEDULING OPERATING CONSTRAINTS
        ≠
STAFF / RESOURCE AVAILABILITY
        ≠
ONE-OFF SCHEDULE UNAVAILABILITY
        ≠
DIGITAL ENQUIRY ELIGIBILITY
```

This amendment adds eight corrections:

1. Public Business Hours are merchant-profile / merchant-configuration data, not Scheduling capability configuration.
2. Stable weekly hours and effective-dated operating exceptions have different lifecycles.
3. Scheduling must always evaluate Business Hours but may use explicitly supported appointment/service periods outside normal Business Hours where that distinction is truthfully projected.
4. Business operating status is a first-class deterministic contextual result.
5. Configured Public Business Hours must be discoverable on the merchant storefront.
6. Enquiry receipt acknowledgement is distinct from substantive enquiry response.
7. Overnight and daylight-saving-time semantics are explicit.
8. Business-hours state never disables an otherwise enabled Enquiry interaction; this is not an infrastructure-uptime guarantee.

---

## 2. Public Business Hours ownership

MS-PROT-050 v1.0 described Business Hours as merchant-owned structured information but did not make the semantic ownership boundary sufficiently explicit.

The accepted model is:

```text
Merchant Profile / Merchant Configuration
        owns
Public Business Hours

Scheduling
        consumes Business Hours

Enquiry / Communication
        consumes CurrentBusinessOperatingStatus

Storefront / Contact / SEO
        projects Business Hours
```

Public Business Hours are therefore **not owned by Scheduling** and do not require Scheduling to be active.

A retailer, publisher, tradesperson or other merchant may publish Business Hours while Scheduling is absent.

MS-PROT-021 remains the broad authority permitting merchant configuration to contain schedules, merchant-owned business information and other stable merchant parameters.

MS-PROT-047 remains authoritative for **capability-owned configuration decisions**. Public Business Hours do not become a Scheduling-owned `STRUCTURED_BOUNDED_VALUE` merely because they use a structured time representation.

Hard rule:

> **A reusable value representation does not transfer semantic ownership.**

---

## 3. Stable weekly hours versus effective-dated operating exceptions

The v1.0 aggregate:

```text
BusinessHoursConfiguration
{
    timeZone
    standardWeeklyHours
    datedOverrides
}
```

is refined into two lifecycle classes.

### 3.1 Stable weekly Business Hours

Conceptually:

```text
StandardBusinessHours
{
    timeZone
    weeklyOperatingIntervals
}
```

These are stable merchant configuration.

Examples:

```text
Monday–Friday 09:00–17:00
Saturday 10:00–14:00
Sunday CLOSED
```

### 3.2 Effective-dated operating exceptions

Conceptually:

```text
BusinessOperatingOverride
{
    merchantScope
    localDate
    effectiveOpenIntervals
    provenance
    optionalReason
}
```

Examples:

```text
2026-12-25
    effectiveOpenIntervals = []
    reason = Christmas closure

2026-12-24
    effectiveOpenIntervals = [09:00–13:00]

2026-08-21
    effectiveOpenIntervals = [09:00–14:05]
    reason = emergency power-cut closure
```

A dated override replaces the standard weekly hours for the affected local date; it is not an additional rule that must be merged through arbitrary priority logic.

Hard rule:

> **For one merchant scope and local date there must be one authoritative effective operating-hours interpretation. Main Street shall not rely on merchant-authored precedence between overlapping opening/closure rules.**

This model supports holidays, emergency closures and special openings without requiring the entire semantic configuration graph to be recompiled for every short-lived operating exception.

---

## 4. Static and contextual lifecycle boundary

MS-PROT-022 v1.5 remains authoritative.

Static/stable inputs include:

```text
standard weekly Business Hours
merchant time zone
stable Scheduling operating configuration
stable scheduling scope bindings
```

Effective-dated contextual inputs may include:

```text
BusinessOperatingOverride
merchant one-off schedule intent
resource/staff availability
current commitments
current capacity
current instant
```

Contextual results include:

```text
CurrentBusinessOperatingStatus
next-open instant
customer-presentable scheduling options
```

An emergency closure therefore need not imply recompilation of unrelated merchant semantics.

---

## 5. Business operating status contract

The term `CurrentBusinessAvailabilityProjection` is superseded by the more precise:

```text
CurrentBusinessOperatingStatus
```

because `availability` is already used for scheduling/resource decisions.

Conceptually:

```text
CurrentBusinessOperatingStatus
{
    state
    currentOperatingIntervalEnd?
    nextOpenInstant?
    basis
}
```

Initial states:

```text
OPEN
CLOSED
UNSPECIFIED
```

`UNSPECIFIED` means Main Street lacks authoritative Public Business Hours for the merchant/context and must not fabricate open/closed status.

Possible basis/provenance may include:

```text
STANDARD_HOURS
DATED_OVERRIDE
TEMPORARY_CLOSURE
SPECIAL_OPENING
```

Exact Java names remain downstream.

Hard rule:

> **`OPEN` means the merchant represents the business as operating at the current instant. It does not mean an appointment is available, a staff member is free, capacity exists, or a human will answer immediately.**

---

## 6. Effective Business Hours

For any local date, Main Street resolves effective operating intervals from:

```text
StandardBusinessHours
        +
applicable BusinessOperatingOverride
        ↓
EffectiveBusinessHours
```

If an authoritative dated override exists for the date, its `effectiveOpenIntervals` replace the standard weekly intervals for that date.

This intentionally avoids a generic precedence language.

---

## 7. Overnight operating intervals

MS-PROT-050 v1.0's simple `start < end` validation is insufficient for merchants that operate across midnight.

Main Street shall support operating periods such as:

```text
Friday 20:00 → Saturday 02:00
```

The merchant-facing semantic meaning is one cross-midnight operating interval.

An implementation may normalise this internally into adjacent local-date intervals, but storage normalisation must not change the merchant-facing meaning.

Hard rule:

> **The time model must represent legitimate cross-midnight business operation without requiring a business-category exception.**

---

## 8. Local civil time and daylight-saving transitions

Recurring Business Hours are expressed in the merchant's configured IANA time zone and interpreted as local civil-time recurrence.

For current operating status:

```text
current instant
        ↓ convert using merchant IANA zone
merchant local date/time
        ↓
resolve effective local operating interval
        ↓
OPEN / CLOSED / UNSPECIFIED
```

This remains deterministic across daylight-saving transitions.

Where a local civil time is skipped or repeated by a zone transition, a customer scheduling commitment must ultimately resolve to one unambiguous real instant before authoritative commitment.

Hard rule:

> **Human-facing recurrence is local-time based; authoritative scheduled commitments are instant-resolved before commitment.**

The exact library/API used for zone resolution is an implementation decision.

---

## 9. Scheduling must consider Business Hours

Business Hours remain a mandatory scheduling input wherever Public Business Hours are configured and relevant to the offered interaction.

However, v1.0's absolute rule:

```text
customer scheduling ⊆ public Business Hours
```

is refined because some merchants legitimately provide by-appointment or emergency services outside normal public operating hours.

The accepted relationship is:

```text
Business Hours
        ↓ mandatory scheduling input
Scheduling operating semantics
        ↓
customer-presentable appointment periods
```

The normal case remains:

```text
customer-presentable appointment periods
        ⊆
EffectiveBusinessHours
```

But explicitly supported service/appointment operating semantics may permit periods outside ordinary Business Hours if the distinction is represented truthfully to customers.

Examples:

```text
Office hours: 09:00–17:00
Evening consultations: by appointment until 19:00
```

```text
Normal trade hours: 08:00–17:00
Emergency call-outs: 24 hours
```

Rejected:

```text
website says CLOSED
booking interface silently exposes unexplained 19:00 slots
```

Accepted:

```text
Business Hours: 09:00–17:00
Appointment availability: evenings by appointment
```

Hard rule:

> **Scheduling may extend beyond ordinary Public Business Hours only through platform-defined semantics that preserve a truthful distinction between public operating hours and supported by-appointment/service availability.**

This is not permission for arbitrary merchant-authored scheduling exceptions.

---

## 10. Scheduling availability calculation

Customer-presentable Scheduling remains the intersection of applicable authorities.

Conceptually:

```text
Business-hours / supported by-appointment time basis
        ∩
Scheduling operating windows
        ∩
Staff/resource availability
        ∩
Capacity
        −
Recurring scheduling breaks
        −
One-off merchant/resource unavailability
        −
Existing commitments/conflicts
        ↓
Candidate scheduling availability
        ↓
Duration/buffer fit
        ↓
Customer-presentable date/time options
```

MS-PROT-006's advisory-availability rule remains unchanged.

---

## 11. Storefront visibility of Public Business Hours

Where the merchant has intentionally configured Public Business Hours for public use, the shared storefront must provide an easily discoverable Business Hours projection.

Hard rule:

> **Configured Public Business Hours shall not depend on a merchant separately enabling or maintaining a website-specific opening-hours copy.**

Presentation may determine:

```text
placement
visual form
compact/full representation
responsive treatment
```

but may not silently omit the only configured Public Business Hours from all discoverable storefront surfaces.

Examples include:

```text
Opening hours section
Contact surface
Open now / Closed status
structured SEO data
```

MS-PROT-036 and MS-PROT-049 remain presentation/composition authorities.

---

## 12. Enquiry eligibility is independent of Business Hours

The phrase `Enquiry is available 24/7` is clarified semantically.

The hard rule is:

> **Current Business Hours state shall never be used as the eligibility condition that disables an otherwise enabled Enquiry interaction.**

Therefore:

```text
OPEN             → Enquiry may be submitted
CLOSED           → Enquiry may be submitted
TEMPORARY CLOSED → Enquiry may be submitted
```

This does not promise uninterrupted infrastructure availability and does not override:

```text
security controls
rate limits
abuse prevention
legal restrictions
planned/unplanned service outages
```

Business closure and digital enquiry eligibility remain separate concerns.

---

## 13. Receipt acknowledgement versus Enquiry response

A successful Enquiry submission produces a **receipt acknowledgement**.

It does not automatically mean the Enquiry has been substantively answered.

Canonical separation:

```text
Enquiry accepted
        ↓
ReceiptAcknowledgement
        ├── confirms receipt
        └── may include deterministic Business Operating Status

Enquiry remains operationally unresolved as applicable
        ↓
AI substantive answer OR merchant response OR later action
```

Hard rule:

> **ReceiptAcknowledgement is not EnquiryResponse.**

This distinction must be preserved in merchant inbox state, unresolved-enquiry counts, analytics and automation.

---

## 14. Deterministic acknowledgement

Where authoritative Business Hours exist:

```text
Enquiry accepted
        +
CurrentBusinessOperatingStatus
        ↓
ReceiptAcknowledgement
```

Possible presentation:

```text
Thanks — your enquiry has been received.
The business is currently closed and next opens tomorrow at 09:00.
```

or:

```text
Thanks — your enquiry has been received.
The business is currently open until 17:00.
```

If operating status is `UNSPECIFIED`, acknowledgement confirms receipt without inventing availability facts.

AI remains unnecessary for this baseline response.

---

## 15. No response-time inference

The following remain distinct:

```text
Business operating status
        ≠
human availability
        ≠
merchant response-time commitment
```

Therefore neither `OPEN` nor `CLOSED` authorises Main Street to infer a response SLA.

A future response-time commitment would require a separate registered contract.

---

## 16. Single-source projection

The same authoritative Business Hours / Effective Business Hours must feed all channel-specific representations under Main Street control.

```text
Public Business Hours
        +
Effective-dated operating overrides
        ↓
Effective Business Hours
        ├── Storefront
        ├── Contact projection
        ├── CurrentBusinessOperatingStatus
        ├── SEO structured data
        └── Scheduling input
```

External platforms may retain authority for their own stored/verified representations under MS-PROT-048.

No duplicate merchant-editable copy is introduced for website or SEO.

---

## 17. Validation rules

At minimum:

1. merchant time zone must be a valid IANA zone;
2. weekly recurring intervals must be valid local-time operating intervals;
3. overlapping intervals within the same effective set/scope are rejected;
4. closed days are represented without fake zero-duration intervals;
5. cross-midnight intervals are supported and deterministically normalisable;
6. one merchant scope/local date must not resolve through contradictory competing overrides;
7. a dated override's effective intervals become the authoritative intervals for that date;
8. customer scheduling commitments must resolve to unambiguous instants;
9. Scheduling cannot fabricate hours absent from a registered scheduling/business-time basis;
10. Business Hours state cannot disable an otherwise eligible Enquiry interaction.

---

## 18. Cross-domain falsification

### Retailer

```text
Business Hours: 09:00–20:00
Scheduling: inactive
Enquiry: active
```

Result:

- website exposes hours;
- current operating status is deterministic;
- no Scheduling surface appears;
- Enquiry remains eligible after closing.

**PASS**

### Salon

```text
Business Hours: 09:00–17:00
Appointments: 09:00–17:00
Scheduling break: 12:30–13:30
```

Result:

- website shows business open 09:00–17:00;
- booking excludes the break;
- break does not mark the merchant publicly closed.

**PASS**

### Solicitor with evening appointments

```text
Business Hours: 09:00–17:00
Registered evening appointment period: until 19:00
```

Result:

- public hours remain 09:00–17:00;
- customer may see evening appointment availability only where the by-appointment distinction is explicitly represented;
- merchant is not falsely shown as generally open until 19:00.

**PASS**

### Emergency tradesperson

```text
Business Hours: 08:00–17:00
Emergency call-out semantics: 24-hour
```

Result:

- ordinary operating status may be CLOSED at 22:00;
- emergency service availability may still exist under its registered semantics;
- Enquiry/contact remains eligible;
- UI must distinguish emergency availability from ordinary open state.

**PASS**

### Late-night venue

```text
Friday operating period: 20:00–02:00
```

Result:

- operating status remains OPEN after midnight until the interval ends;
- implementation may normalise across local dates without changing merchant meaning.

**PASS**

### DST transition

Recurring local hours occur over a zone transition.

Result:

- current status is derived from instant → local time conversion;
- scheduling commitment resolves ambiguous local times to one real instant before commitment.

**PASS**

### Emergency closure

```text
Standard: 09:00–17:00
Today: power cut at 14:05
```

Result:

- an effective-dated operating override can close the business from 14:05;
- unrelated merchant semantic compilation need not be rebuilt merely because the temporary operating fact changed;
- storefront/status/scheduling consume the effective hours.

**PASS**

### Scholarship publisher without hours

```text
Publication + Enquiry active
Business Hours absent
```

Result:

- Enquiry remains eligible;
- acknowledgement confirms receipt;
- operating status is `UNSPECIFIED`, not fabricated.

**PASS**

---

## 19. Rejected approaches

| Rejected approach | Why it fails |
| --- | --- |
| Scheduling owns Public Business Hours | Business Hours exist when Scheduling is absent |
| Force Public Business Hours through capability configuration | Confuses merchant-profile/configuration authority with capability-owned decision authority |
| Recompile whole merchant model for emergency closure | Wrong lifecycle for short-lived operating fact |
| Multiple competing override rules with priorities | Creates hidden merchant-authored precedence logic |
| Every appointment must lie inside public open hours | Emergency/by-appointment services disprove the universal rule |
| Silently expose after-hours appointments | Contradicts public operating truth |
| Treat `OPEN` as generic availability | Conflates business operation, scheduling, resource and human response semantics |
| Omit configured hours from storefront | Fails the goal of reducing unnecessary customer enquiries |
| Use `24/7 Enquiry` as uptime promise | Mixes semantic eligibility with infrastructure SLA |
| Treat receipt acknowledgement as enquiry resolution | Corrupts inbox/analytics/automation semantics |
| Reject cross-midnight hours | Fails legitimate late-night merchants |
| Fixed UTC-offset recurrence | Breaks local civil-time meaning across DST |

---

## 20. Accepted invariants

1. Public Business Hours are merchant-profile / merchant-configuration information, not Scheduling-owned configuration.
2. Scheduling may consume Business Hours without owning them.
3. Standard weekly hours and effective-dated operating overrides have distinct lifecycles.
4. One local date resolves to one authoritative effective operating-hours interpretation for the merchant scope.
5. `CurrentBusinessOperatingStatus` is contextual and uses `OPEN`, `CLOSED` or `UNSPECIFIED` semantics.
6. `OPEN` does not imply appointment availability, staff availability, capacity or response SLA.
7. Cross-midnight operating intervals are valid.
8. Recurrence uses merchant-local civil time in an IANA time zone.
9. Scheduled commitments resolve to unambiguous real instants before commitment.
10. Scheduling must always evaluate Business Hours where applicable.
11. The normal scheduling case remains within Effective Business Hours.
12. Registered by-appointment/service semantics may extend beyond normal Business Hours only when truthfully represented.
13. Scheduling-specific breaks/resources/capacity may narrow available times without redefining Business Hours.
14. Configured Public Business Hours must remain discoverable on the storefront.
15. Website/SEO/contact/status derive from one authoritative hours source under Main Street control.
16. Business-hours state never disables an otherwise enabled Enquiry interaction.
17. This Enquiry rule is not an infrastructure uptime guarantee.
18. ReceiptAcknowledgement is distinct from EnquiryResponse.
19. Deterministic operating-status acknowledgement does not require AI.
20. Business-open state never implies a human response SLA.
21. Absence of authoritative Business Hours yields `UNSPECIFIED`, not invented open/closed facts.
22. No business-category-specific time model is required.

---

## 21. Implementation discipline

The first RED implementation slice remains intentionally narrow.

It should prove, in order:

1. typed stable weekly Business Hours;
2. IANA time-zone validation;
3. same-day and cross-midnight interval validity;
4. effective-dated override replacement for a local date;
5. deterministic `CurrentBusinessOperatingStatus` resolution;
6. `UNSPECIFIED` when authoritative hours are absent;
7. storefront/read-projection source can consume the same authoritative hours without duplication;
8. business-hours state does not gate Enquiry eligibility;
9. ReceiptAcknowledgement remains distinct from EnquiryResponse;
10. Scheduling consumes the hours/status model without yet implementing a full scheduling engine.

Do not implement generic scheduling inheritance, arbitrary time-rule DSLs, provider infrastructure or AI-dependent availability logic.

A separate RED slice is required before implementing by-appointment/out-of-hours scheduling because that behaviour must be backed by a concrete registered semantic contract rather than a generic escape hatch.

---

## 22. Continuous-improvement checkpoint

Question:

> **What could we have done better?**

The review identified that v1.0 correctly separated Business Hours from Scheduling but still left Business Hours too close to the capability-configuration model. It also over-constrained Scheduling to a universal subset rule, put stable and temporary operating facts in one lifecycle, and did not fully specify overnight/DST or receipt-acknowledgement semantics.

This amendment corrects those weaknesses without reopening the successful separations established by v1.0.

No additional material issue remains within the bounded scope of this amendment after the approved corrections and cross-domain falsification. Future concrete implementation evidence may still trigger another improvement review under Main Street governance.

---

## Governance verdict

**ACCEPTED.**

MS-PROT-050 v1.1 is the current authority for Public Business Hours ownership, effective operating-hours resolution, scheduling-time interaction, storefront discoverability, digital Enquiry eligibility and business-hours-aware receipt acknowledgement. MS-PROT-050 v1.0 remains historical accepted evidence and is superseded where v1.1 differs.