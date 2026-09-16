# MS-PROT-050 — Business Hours & Scheduling Operating-Time Contract

**Document ID:** MS-PROT-050  
**Version:** 1.0  
**Status:** **ACCEPTED after manual approval and cross-domain falsification**  
**Depends on:** MS-PROT-022 v1.5, MS-PROT-027, MS-PROT-036 v1.1, MS-PROT-039 v1.1, MS-PROT-040, MS-PROT-041, MS-PROT-043 v1.2, MS-PROT-047 v1.0, MS-PROT-049 v1.0  
**Purpose:** Define the authoritative distinction and interaction between public business hours, scheduling operating constraints, staff/resource availability and one-off unavailability; define how business hours constrain customer-facing scheduling; and define how enquiries remain continuously available while Main Street communicates current merchant availability accurately.

---

## 1. Governing decisions

Main Street shall keep the following concepts distinct:

```text
PUBLIC BUSINESS HOURS
        ≠
SCHEDULING OPERATING CONSTRAINTS
        ≠
STAFF / RESOURCE AVAILABILITY
        ≠
ONE-OFF SCHEDULE UNAVAILABILITY
```

The following rules are normative:

> **Public business hours describe when the merchant represents the business as open for business.**

> **Customer-facing scheduling availability must be calculated within the merchant's effective public business hours and may then be narrowed by scheduling, resource, capacity and existing-commitment constraints.**

> **Public business hours do not disable enquiry. Where Enquiry is an enabled public interaction, customers may submit enquiries at any time.**

> **An enquiry submitted outside business hours shall receive an immediate deterministic acknowledgement using authoritative business-hours information, without falsely implying that a human merchant is presently available.**

Canonical boundary:

```text
Merchant Business Hours
        ├──────────────→ public website / contact projection / SEO
        ├──────────────→ open-now / next-open projection
        └──────────────→ outer scheduling-time boundary

Scheduling configuration
        └──────────────→ narrows schedulable time inside business hours

Enquiry
        └──────────────→ remains available independently of open/closed state
```

---

## 2. Why business hours and scheduling hours are different

A merchant may be open for business without offering customer-selectable appointments during every open period.

Example:

```text
Business open:
    09:00–18:00

Customer appointments:
    10:00–16:30
```

A retailer may have:

```text
Business open:
    09:00–20:00

Scheduling:
    NOT APPLICABLE
```

An online consultant may have:

```text
Business availability:
    09:00–17:00

Bookable consultations:
    10:00–15:00
```

Therefore:

```text
public opening hours
    ≠
bookable appointment hours
```

Scheduling shall never silently redefine the merchant's public opening hours.

---

## 3. Public Business Hours

Public Business Hours are merchant-owned structured business information describing when the merchant represents the business as open.

Conceptually:

```text
BusinessHoursConfiguration
{
    timeZone
    standardWeeklyHours
    datedOverrides
}
```

The exact Java representation is downstream.

### 3.1 Time zone

Business hours shall be interpreted in a registered IANA time zone rather than persisted merely as a fixed UTC offset.

This preserves the merchant's local civil-time meaning across daylight-saving changes.

### 3.2 Standard weekly hours

A day may contain:

- zero opening intervals (`CLOSED`);
- one opening interval;
- multiple opening intervals;
- an explicit all-day/open-24-hours representation where supported.

Example:

```text
Monday
    09:00–12:30
    13:30–17:00

Tuesday
    09:00–17:00

Sunday
    CLOSED
```

Multiple intervals shall not be collapsed into an inferred scheduling break. They are authoritative public opening intervals.

### 3.3 Dated overrides

Business hours must support date-specific departure from the weekly pattern, including at minimum:

```text
SPECIAL_OPENING_HOURS
TEMPORARY_CLOSURE
```

Holiday hours are represented as dated overrides rather than as a second weekly calendar.

For a specific local date, a dated override takes precedence over the standard weekly hours for that date.

Example:

```text
Standard Monday:
    09:00–17:00

Bank Holiday Monday:
    CLOSED
```

or:

```text
Standard Thursday:
    09:00–17:00

Special late opening:
    09:00–20:00
```

---

## 4. Public projection requirement

Where public business hours are configured and applicable, Main Street shall expose them through the merchant's public website without requiring a second website-specific hours editor.

Canonical flow:

```text
Merchant edits Business Hours
        ↓
Authoritative structured BusinessHoursConfiguration
        ↓
Public read projection
        ↓
 ┌────────────┬──────────────┬──────────────┬──────────────┐
 ▼            ▼              ▼              ▼
Homepage    Contact        SEO/schema     Public API
```

The presentation layer may display:

```text
Monday       09:00–17:00
Tuesday      09:00–17:00
Wednesday    09:00–17:00
Thursday     09:00–19:00
Friday       09:00–17:00
Saturday     10:00–14:00
Sunday       Closed
```

The storefront remains governed by MS-PROT-036 and MS-PROT-049. MS-PROT-050 defines the authoritative information to project, not page layout.

Hard rule:

> **The merchant manages business-hours information; the merchant does not maintain a separate copy for the website.**

---

## 5. Current open/closed projection

Main Street may derive contextual public status from the authoritative business-hours configuration and current time.

Examples:

```text
Open now · Closes at 17:00
```

```text
Closed · Opens Saturday at 09:00
```

```text
Closed today · Opens Monday at 09:00
```

This is a dynamic projection, not immutable configuration.

Therefore:

```text
BusinessHoursConfiguration
        +
current instant
        +
merchant time zone
        ↓
CurrentBusinessAvailabilityProjection
```

The derived status shall respect dated overrides.

If no authoritative public business hours are configured, Main Street shall not invent `OPEN`, `CLOSED` or `NEXT_OPEN` state.

---

## 6. Scheduling Operating Configuration

Scheduling Operating Configuration defines when appointment-style scheduling is permitted inside the broader merchant business-hours boundary.

It may include, where applicable:

```text
scheduling time windows
recurring scheduling breaks
offering duration
preparation buffer
completion buffer
resource participation
capacity participation
scheduling policies
```

These remain scheduling semantics under MS-PROT-039 / MS-PROT-041.

They are not public opening-hours data merely because both involve time.

---

## 7. Business hours are the outer customer-scheduling boundary

For customer-facing appointment-time presentation, Main Street shall not offer a candidate interval outside the merchant's effective business-open intervals.

Canonical rule:

```text
customer-presentable scheduling interval
    ⊆
merchant effective business-open interval
```

A candidate appointment interval must fit wholly inside an effective open interval unless an accepted future semantic contract explicitly defines another kind of appointment outside normal business operation.

If the merchant wishes to offer an appointment during a period that is normally closed, the merchant must first represent that date/time as an applicable special opening period rather than silently bypass public business hours.

This keeps customer-facing truth coherent:

```text
website says CLOSED
        ≠
booking page offers 18:30 appointment
```

without an explicit special-opening fact.

---

## 8. Scheduling availability calculation

The effective customer-facing scheduling decision is an intersection of distinct authorities.

Conceptually:

```text
Effective business-open intervals
        ∩
Applicable scheduling operating windows
        ∩
Applicable staff/resource availability
        ∩
Applicable capacity
        −
Recurring scheduling breaks
        −
Merchant one-off unavailability
        −
Existing customer commitments
        −
Other authoritative conflicts
        ↓
Candidate scheduling availability
        ↓
Duration/buffer fit validation
        ↓
Customer-presentable date/time options
```

This does not alter MS-PROT-006's rule that an availability result remains advisory until authoritative commitment/revalidation.

---

## 9. Reusing business hours for scheduling

Main Street should not require merchants to enter the same timetable twice when scheduling hours are genuinely the same as business hours.

A scheduling configuration may therefore conceptually identify its base time source as:

```text
USE_BUSINESS_HOURS
CUSTOM_SCHEDULING_HOURS
```

### USE_BUSINESS_HOURS

The merchant's effective public business hours provide the scheduling operating-time baseline.

Scheduling-specific constraints may still narrow it:

```text
Business hours
    09:00–17:00

Scheduling break
    12:30–13:30

Effective scheduling baseline
    09:00–12:30
    13:30–17:00
```

The break does not automatically mean the business is publicly closed during lunch.

### CUSTOM_SCHEDULING_HOURS

The merchant supplies a bounded Scheduling-owned schedule for the applicable scope.

The effective customer scheduling time is still the intersection with public business hours.

Example:

```text
Business hours
    09:00–18:00

Custom scheduling hours
    10:00–16:00

Customer-presentable base scheduling interval
    10:00–16:00
```

A custom scheduling window extending beyond business hours is not customer-presentable outside the business-open boundary.

---

## 10. Scoped scheduling configuration

Scheduling configuration must not be assumed merchant-wide.

It may legitimately vary by accepted semantic context such as:

```text
merchant
operation
offering
resource
```

Example:

```text
Business open:
    09:00–18:00

Initial Consultation:
    09:00–16:00

Complex Review:
    11:00–15:00

Consultant A:
    Monday / Wednesday / Friday only
```

MS-PROT-050 does not introduce a generic inheritance/override language between these scopes.

Hard rule:

> **Scope identity does not itself imply precedence, inheritance or cascading overrides.**

Any resolution relationship between scoped schedules must be platform-defined and validated.

---

## 11. Staff/resource hours are not public business hours

A staff member or resource may participate only during narrower periods.

Example:

```text
Business open:
    Monday–Saturday 09:00–18:00

Staff A:
    Monday–Wednesday 09:00–17:00

Staff B:
    Thursday–Saturday 10:00–18:00
```

The website must not alternate the merchant's opening-hours statement merely because one staff member is unavailable.

Likewise:

```text
staff unavailable
    ≠
business closed
```

Resource availability narrows operations that require the resource; it does not redefine public business identity.

---

## 12. One-off unavailability is not business hours

A merchant schedule intent such as:

```text
UNAVAILABLE
Tuesday 14:00–15:30
"Dentist appointment"
```

remains a one-off scheduling constraint under MS-PROT-041.

It does not automatically change the public business-hours statement.

Therefore:

```text
merchant temporarily unavailable for one scheduling role
    ≠
business publicly closed
```

If the entire business is actually closed for that time/date and the merchant intends that public fact to be communicated, the merchant must use the appropriate business-hours dated override / temporary closure semantics.

---

## 13. Enquiry is independent of business-open state

Enquiry availability shall not be gated by public business hours.

Where the Enquiry public interaction is enabled:

```text
BUSINESS OPEN
    → customer may submit enquiry

BUSINESS CLOSED
    → customer may submit enquiry

TEMPORARY CLOSURE
    → customer may submit enquiry
```

Hard invariant:

> **Opening hours govern when the business represents itself as open; they do not impose a closing time on the merchant's digital enquiry surface.**

This follows MS-PROT-043: an Enquiry is a merchant-scoped request for information/further action, not a scheduling or commitment operation.

---

## 14. Immediate enquiry acknowledgement

A successfully accepted enquiry shall provide an immediate customer acknowledgement.

At minimum the acknowledgement shall establish:

```text
enquiry received
```

Where authoritative business-hours data are available, the response shall also communicate current merchant availability accurately.

Examples while open:

```text
Thanks — your enquiry has been received.
The business is currently open until 17:00.
```

Examples while closed:

```text
Thanks — your enquiry has been received.
The business is currently closed and next opens tomorrow at 09:00.
```

or:

```text
Thanks — your enquiry has been received.
The business is closed today and next opens Monday at 09:00.
```

The exact wording is presentation policy; the underlying facts are structured.

---

## 15. Availability acknowledgement is deterministic, not AI-dependent

The baseline enquiry acknowledgement must not require an AI model to determine whether the merchant is open.

Canonical path:

```text
Enquiry accepted
        +
BusinessHoursConfiguration
        +
current instant
        +
merchant time zone
        ↓
Deterministic availability projection
        ↓
Immediate acknowledgement
```

This guarantees that the fundamental response remains:

- available when AI is disabled;
- explainable;
- reproducible;
- grounded in authoritative merchant data;
- free from model hallucination.

An enabled AI Customer Assistant may subsequently answer richer supported questions using approved merchant information, but AI does not own the open/closed determination.

---

## 16. Acknowledgement must not fabricate merchant response-time promises

`BUSINESS OPEN` does not prove:

```text
merchant is reading the inbox now
merchant will respond immediately
merchant will respond within N minutes
```

Similarly, `BUSINESS CLOSED` does not prove the merchant will wait until opening time before replying.

Therefore Main Street shall distinguish:

```text
current business availability
        ≠
human response SLA
```

Unless the merchant has a separately registered and configured response-time commitment, the automatic acknowledgement shall not promise one.

---

## 17. Transaction and delivery boundary for enquiry acknowledgement

The Enquiry object must be accepted according to its authoritative business operation before optional external delivery side effects are considered complete.

Conceptually:

```text
submit enquiry
        ↓
validate + create Enquiry
        ↓
commit authoritative Enquiry fact
        ↓
return immediate in-page acknowledgement
        ↓
optional durable delivery through supported channel
```

If email/SMS/another transport is used to send an acknowledgement, transport failure must not erase the committed Enquiry.

This follows the transactional-core / compensating-edge discipline and MS-PROT-048 provider boundary.

---

## 18. Enquiry remains available during temporary closure

A temporary business closure changes public open/closed status and scheduling availability but does not remove the Enquiry interaction.

Example:

```text
Business temporarily closed until Monday
        ↓
Website
    "Temporarily closed · reopens Monday at 09:00"

Booking availability
    no customer-presentable times during closure

Enquiry
    remains available 24/7
        ↓
    immediate acknowledgement explains reopening availability
```

This is intentional.

The digital surface remains useful when the physical or staffed business is closed.

---

## 19. Information-only and online merchants

MS-PROT-050 shall not force physical-business semantics onto information publishers or online merchants.

A scholarship-information publisher may:

```text
Publication + Enquiry active
Scheduling absent
```

If it chooses to publish business/support hours, those hours may inform availability acknowledgements.

If it has no meaningful business-hours configuration, Main Street shall still accept enquiries and acknowledge receipt without inventing an open/closed state.

An online consultant may publish business hours and separately configure appointment availability.

Physical premises are not required by this contract.

---

## 20. Storefront and surface contribution boundary

Public Business Hours are public business information, not a new merchant category or custom storefront.

MS-PROT-036 remains authoritative for storefront composition.

MS-PROT-049 remains authoritative for reusable surface contributions.

MS-PROT-050 establishes that presentation may consume:

```text
business-hours public projection
current availability projection
next-open projection
```

without embedding time-resolution business logic in frontend components.

A frontend may format the projection; it must not independently decide business-open state from ungoverned local rules.

---

## 21. SEO and external representations

Where Main Street publishes business-hours structured data for SEO or an external supported representation, it shall derive from the same authoritative BusinessHoursConfiguration used by the public website.

Rejected:

```text
website hours      = one editable copy
SEO hours          = another editable copy
Google/local hours = another Main Street-authored copy
```

Preferred:

```text
BusinessHoursConfiguration
        ↓
channel-specific projection/adaptation
```

External platforms may remain authoritative for their own verification or externally stored representation under their provider contracts.

---

## 22. Validation rules

At minimum, a structured recurring interval shall satisfy:

```text
valid day/date context
valid local start/end values
start < end
no overlapping intervals within the same authoritative set/scope
valid IANA time zone
```

A dated closure shall not require fake zero-duration opening intervals.

A business-hours configuration may legitimately represent a closed day.

Scheduling-specific recurring breaks must lie within the applicable scheduling operating surface they are intended to narrow.

Exact Java types and validation classes remain an implementation concern, but these invariants are normative.

---

## 23. Configuration and runtime boundary

MS-PROT-022 v1.5 remains authoritative for static versus contextual resolution.

Static configuration includes:

```text
business-hours weekly pattern
business-hours dated overrides known to configuration
scheduling operating-time selections
registered schedule scopes
```

Contextual/dynamic resolution includes:

```text
current open/closed result
next-open instant
current resource availability
current commitments
current one-off merchant unavailability
customer-presentable availability for a requested date/time
```

Changing current clock time does not require configuration recompilation.

---

## 24. Onboarding implications

Main Street should avoid duplicate merchant questions.

Where business hours are already known, and Scheduling becomes applicable, onboarding should ask whether scheduling should:

```text
use business hours
or
use narrower custom scheduling hours
```

rather than asking the merchant to re-enter the same schedule by default.

If the merchant selects custom scheduling hours, the resulting structured value remains bounded and scope-aware under MS-PROT-047.

Inference may propose these values, but merchant configuration remains compiler-validated.

---

## 25. Cross-domain falsification

### 25.1 Physical retailer without Scheduling

```text
Business hours: 09:00–20:00
Scheduling: inactive
Enquiry: active
```

Result:

- website shows opening hours;
- no appointment UI appears;
- customer may enquire at 23:00;
- acknowledgement says business is closed and identifies next opening when available.

**PASS**

### 25.2 Salon using same business and scheduling hours

```text
Business hours: 09:00–17:00
Scheduling source: USE_BUSINESS_HOURS
Recurring scheduling break: 12:30–13:30
```

Result:

- website may show 09:00–17:00 as business open;
- customer booking page does not offer 12:30–13:30;
- lunch scheduling break does not falsely claim the whole business is closed.

**PASS**

### 25.3 Consultant with narrower appointment hours

```text
Business hours: 09:00–17:00
Scheduling hours: 10:00–15:00
```

Result:

- website shows 09:00–17:00;
- scheduling exposes only eligible intervals within 10:00–15:00;
- enquiry remains available after 17:00.

**PASS**

### 25.4 Merchant with holiday closure

```text
Normal Monday: 09:00–17:00
Holiday override: CLOSED
```

Result:

- website communicates closure;
- no scheduling slots are offered inside the closure;
- enquiry remains available;
- acknowledgement communicates next authoritative opening.

**PASS**

### 25.5 Staff-specific schedule

```text
Business open: Monday–Saturday
Staff A: Monday–Wednesday
Staff B: Thursday–Saturday
```

Result:

- public business hours remain merchant-level truth;
- staff/resource schedule narrows operations requiring that staff;
- one staff member's absence does not mark the merchant closed.

**PASS**

### 25.6 Scholarship publisher

```text
Publication + Enquiry active
Scheduling inactive
No public business hours configured
```

Result:

- enquiry remains available;
- acknowledgement confirms receipt;
- Main Street does not fabricate `Open now`, `Closed`, or next-open information.

**PASS**

### 25.7 Temporary whole-business closure

```text
Temporary closure: Friday–Sunday
```

Result:

- public website communicates closure/reopening;
- scheduling is bounded by the closure;
- digital Enquiry remains available throughout;
- automated acknowledgement explains current business availability.

**PASS**

---

## 26. Rejected approaches

| Rejected approach | Why it fails |
| --- | --- |
| Treat Scheduling hours as public opening hours | Retailers and narrower appointment windows disprove equivalence |
| Treat public opening hours as all possible availability | Ignores staff, resources, breaks, commitments and capacity |
| Disable enquiry when business closes | Makes the digital storefront unavailable precisely when asynchronous contact is most useful |
| AI decides whether merchant is open | Unnecessary, non-deterministic and risks hallucination |
| Promise immediate human response while business is open | Business-open state is not a response-time SLA |
| Copy hours separately into website/SEO | Breaks single-source-of-truth and creates drift |
| Use fixed UTC offsets for recurring local hours | Breaks local-time meaning across DST changes |
| Treat staff hours as merchant opening hours | Confuses resource availability with public business identity |
| Model temporary personal unavailability as whole-business closure | Conflates one-off operational constraint with public availability |
| Let custom scheduling hours extend beyond closed business hours silently | Website and booking surface would contradict one another |
| Create business-category-specific hour models | Reintroduces vertical templates |

---

## 27. Accepted invariants

1. Public Business Hours and Scheduling Operating Configuration are distinct.
2. Staff/resource availability and one-off unavailability are distinct from both.
3. Business Hours are structured merchant-owned information and may be publicly projected.
4. Website, contact and SEO representations derive from the same authoritative business-hours source.
5. Current open/closed/next-open state is a contextual projection, not static configuration.
6. Customer-presentable scheduling intervals must lie within effective business-open intervals.
7. Scheduling may further narrow business hours; it cannot silently expand beyond them.
8. Scheduling may reuse Business Hours as its base to avoid duplicate merchant entry.
9. Custom scheduling hours remain scope-aware.
10. Scope does not itself imply inheritance or override precedence.
11. Recurring scheduling breaks do not automatically mean the business is publicly closed.
12. Staff/resource unavailability does not automatically mean the business is publicly closed.
13. One-off scheduling unavailability does not automatically modify Business Hours.
14. Whole-business dated closure/override affects public availability and customer scheduling.
15. Enquiry remains available regardless of current business-open state where Enquiry is enabled.
16. Successful Enquiry submission receives immediate acknowledgement.
17. Business-hours availability in that acknowledgement is determined deterministically from authoritative data.
18. AI is optional for richer enquiry assistance and does not own open/closed determination.
19. Availability acknowledgement does not fabricate a human response-time promise.
20. External acknowledgement-delivery failure does not erase a committed Enquiry.
21. If Business Hours are absent, Main Street does not invent open/closed/next-open facts.
22. The contract applies to physical, service, online and information-only merchants without category-specific branches.

---

## 28. Implementation discipline

MS-PROT-050 does not authorise immediate implementation of the entire time/scheduling subsystem.

The first justified TDD slice should prove only the minimum typed configuration and resolution boundary needed by accepted behaviour.

Candidate RED evidence includes:

- valid public weekly business hours can be represented;
- invalid/overlapping intervals are rejected;
- dated closure overrides weekly hours for contextual resolution;
- open/closed/next-open projection is deterministic in the configured time zone;
- Scheduling cannot present an interval outside effective Business Hours;
- Scheduling can use Business Hours as its baseline without copying values;
- narrower scheduling hours intersect rather than override/expand Business Hours;
- Enquiry remains acceptable while the business is closed;
- acknowledgement availability facts are derived without AI;
- no human response SLA is inferred from business-open state;
- absence of Business Hours yields acknowledgement without fabricated availability facts.

Do not build calendar-provider, notification-provider, AI-provider or generic structured-value infrastructure unless a concrete RED requirement demands it.

---

## 29. Continuous-improvement checkpoint

Question:

> **What could we have done better?**

The material improvement discovered during this slice was that the initial proposal treated operating time primarily as Scheduling configuration. Product requirements and cross-domain falsification showed that public Business Hours are an independent merchant/business-profile concern that exists even when Scheduling is absent.

The accepted correction is therefore:

```text
Business Hours
    → independent public business truth
    → website / SEO / availability communication
    → outer scheduling boundary

Scheduling hours
    → optional narrower operational constraint
```

A second improvement was to separate **24/7 digital contactability** from **merchant open state**. Enquiry is asynchronous and remains useful outside business hours; the correct customer experience is immediate acknowledgement and accurate availability information, not a closed enquiry form.

A third improvement was to make the baseline availability acknowledgement deterministic instead of AI-dependent. This produces a cheaper, safer and more reliable core while preserving AI as an optional richer assistant.

No further material architectural issue remained after these refinements and cross-domain falsification.

---

## Governance verdict

**ACCEPTED.**

MS-PROT-050 is the current authority for the distinction and interaction between public Business Hours, scheduling operating-time constraints, resource/staff availability, one-off unavailability, customer scheduling bounds and business-hours-aware enquiry acknowledgement.
