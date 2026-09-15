# MS-PROT-050 v1.2 — Business Hours Scope & Multi-Location Amendment

**Document ID:** MS-PROT-050  
**Version:** 1.2  
**Status:** **ACCEPTED after merchant-profile review and manual approval**  
**Amends:** MS-PROT-050 v1.1 — Business Operating Hours, Scheduling-Time & Digital Contactability Amendment  
**Depends on:** MS-PROT-021 v1.3, MS-PROT-022 v1.5, MS-PROT-027 v1.1, MS-PROT-031 v1.1, MS-PROT-036 v1.1, MS-PROT-039 v1.2, MS-PROT-041 v1.1, MS-PROT-043 v1.2, MS-PROT-049 v1.0, MS-PROT-051 v1.0  
**Purpose:** Extend Public Business Hours from an implicitly merchant-wide model to an explicitly scoped model that supports online/single-context merchants and multi-location merchants without treating locations as tenants, inventing implicit inheritance, or synthesising misleading merchant-wide operating status.

---

# 1. Governing amendment

MS-PROT-050 v1.1 remains accepted except where this amendment changes or clarifies the scope of Public Business Hours and `CurrentBusinessOperatingStatus`.

The governing decision is:

> **Every authoritative Public Business Hours set and every derived `CurrentBusinessOperatingStatus` shall be evaluated within an explicit Business Hours Scope. Initial supported scopes are Merchant Scope and Merchant Location Scope. Main Street shall not infer cross-scope inheritance, precedence or merchant-wide OPEN/CLOSED status from unrelated location-scoped hours.**

Canonical scope model:

```text
BusinessHoursScope
    ├── MERCHANT
    │      merchantScope
    │
    └── MERCHANT_LOCATION
           merchantScope
           merchantLocationIdentity
```

Canonical resolution:

```text
BusinessHoursScope
        +
StandardBusinessHours(scope)
        +
BusinessOperatingOverride(scope, localDate)
        +
current instant
        ↓
CurrentBusinessOperatingStatus(scope)
```

Hard invariant:

> **Operating status is always scope-qualified.**

---

# 2. Why v1.2 is required

MS-PROT-050 v1.1 established correct temporal semantics but largely described Business Hours at merchant scope.

That is insufficient for merchants such as:

```text
Smith & Co

Swansea location
    Mon–Fri 09:00–17:00

Cardiff location
    Mon–Sat 10:00–18:00
```

At 17:30 on Friday:

```text
Swansea = CLOSED
Cardiff = OPEN
```

There is no universally truthful answer to:

```text
Merchant = OPEN ?
```

unless Main Street has a separately configured merchant-scope operating context whose semantics actually answer that question.

Therefore:

```text
location-specific operating truth
        ≠
merchant-wide operating truth
```

---

# 3. Business Hours Scope

A Business Hours Scope identifies the exact merchant operating context whose public hours are being described.

Initial scopes:

```text
MERCHANT
MERCHANT_LOCATION
```

This is deliberately narrower than a generic arbitrary-scope DSL.

Additional scope kinds require accepted cross-domain evidence and explicit design authority.

---

# 4. Merchant Scope hours

Merchant Scope hours represent operating hours for the merchant as a whole where that meaning is legitimate and useful.

Examples:

```text
online consultant availability for general business contact
information publisher support hours
single-location merchant represented without separate location-specific hours
remote service operation with one public operating schedule
```

Conceptually:

```text
BusinessHoursScope
{
    kind = MERCHANT
    merchantScope = M123
}
```

Merchant-scope hours do not imply the merchant has a physical location.

---

# 5. Merchant Location Scope hours

Merchant Location Scope hours represent public operating hours for one specific Merchant Location under MS-PROT-051.

Conceptually:

```text
BusinessHoursScope
{
    kind = MERCHANT_LOCATION
    merchantScope = M123
    merchantLocationIdentity = L1
}
```

Example:

```text
Location L1 — Swansea
09:00–17:00

Location L2 — Cardiff
10:00–18:00
```

Each location has independently resolvable operating truth.

---

# 6. Business Hours Scope does not create a tenant

MS-PROT-031 remains authoritative.

```text
Merchant Location
        ≠
Merchant Scope / tenant
```

Therefore:

```text
Merchant M123
    ├── Location L1 hours
    └── Location L2 hours
```

remains one tenant with multiple scoped operating contexts.

Tenant isolation is not weakened or duplicated by Business Hours scoping.

---

# 7. Merchant and location hours may coexist

A merchant may legitimately have both:

```text
merchant-scope hours
```

and:

```text
location-scope hours
```

when they describe different truthful operating contexts.

Example:

```text
Merchant-wide remote support:
Mon–Fri 08:00–20:00

Swansea customer-facing office:
Mon–Fri 09:00–17:00

Cardiff customer-facing office:
Mon–Fri 10:00–18:00
```

These are not overrides of one another.

They are separate scoped facts.

Hard rule:

> **Coexisting scopes do not imply a precedence hierarchy.**

---

# 8. No implicit inheritance

Main Street shall not assume:

```text
merchant-scope hours
        ↓ inherited by
all Merchant Locations
```

nor:

```text
location has no hours
        ↓
use merchant hours automatically
```

unless a future accepted contract explicitly establishes such inheritance semantics.

The initial model intentionally rejects implicit inheritance because it creates hidden precedence and surprising behaviour.

If two locations share the same hours, the merchant UX may offer:

```text
copy hours
apply same values to selected locations
bulk edit
```

but the resulting authoritative values remain explicitly scoped.

Hard invariant:

> **Editing convenience does not create semantic inheritance.**

---

# 9. No implicit precedence

When Merchant Scope hours and Merchant Location Scope hours coexist, neither automatically overrides the other.

Incorrect:

```text
merchant hours = 09:00–17:00
location hours = 10:00–18:00
        ↓
“location overrides merchant”
```

unless an explicit consumer contract says it is evaluating location scope and therefore uses the location-scoped hours.

The correct rule is:

```text
consumer determines required scope
        ↓
resolve hours for that scope
```

not:

```text
collect all hours
        ↓
run precedence algorithm
```

---

# 10. No synthetic merchant-wide OPEN/CLOSED from location status

Suppose:

```text
Location L1 = CLOSED
Location L2 = OPEN
```

Main Street shall not produce:

```text
CurrentBusinessOperatingStatus(Merchant) = OPEN
```

merely because one location is open.

Likewise it shall not produce `CLOSED` merely because a selected location is closed while another remains open.

A merchant-wide status exists only if Merchant Scope Business Hours have been explicitly configured for a meaningful merchant-wide operating context.

Hard invariant:

> **Location operating status cannot be collapsed into merchant operating status by boolean aggregation.**

---

# 11. Aggregate presentation summaries are not operating authority

A storefront may present a derived human-readable summary such as:

```text
2 locations open now
```

or:

```text
Swansea closed · Cardiff open until 18:00
```

where useful.

Such a summary is a projection over multiple scope-qualified statuses.

It is not itself:

```text
CurrentBusinessOperatingStatus(MERCHANT)
```

unless Merchant Scope hours independently establish that fact.

Projection wording shall preserve the underlying scope distinction.

---

# 12. Standard Business Hours are scope-qualified

MS-PROT-050 v1.1's `StandardBusinessHours` becomes explicitly scoped.

Conceptually:

```text
StandardBusinessHours
{
    businessHoursScope
    timeZone
    weeklyOperatingIntervals
}
```

Examples:

```text
StandardBusinessHours(
    scope = MERCHANT(M123),
    zone = Europe/London,
    ...
)
```

```text
StandardBusinessHours(
    scope = MERCHANT_LOCATION(M123, L1),
    zone = Europe/London,
    ...
)
```

The exact implementation representation remains downstream.

---

# 13. Time zone is scope-qualified

A multi-location merchant may operate across time zones.

Therefore time zone cannot be assumed to be one universal merchant value for all Business Hours scopes.

Example:

```text
Merchant M123

London location
    zone = Europe/London

New York location
    zone = America/New_York
```

Each scope resolves local civil time under its own authoritative IANA zone.

Merchant Scope hours likewise carry the zone appropriate to that merchant-wide operating context.

Hard rule:

> **Business Hours recurrence is interpreted in the time zone of its Business Hours Scope.**

---

# 14. Dated overrides are scope-qualified

MS-PROT-050 v1.1's `BusinessOperatingOverride` becomes explicitly scoped.

Conceptually:

```text
BusinessOperatingOverride
{
    businessHoursScope
    localDate
    effectiveOpenIntervals
    provenance
    optionalReason
}
```

Example:

```text
Swansea L1
2026-12-24
09:00–13:00
```

shall not alter:

```text
Cardiff L2
2026-12-24
10:00–18:00
```

unless a separate L2 override exists.

---

# 15. Scope-qualified override uniqueness

The v1.1 rule:

> one authoritative effective operating-hours interpretation per merchant scope/local date

is refined to:

> **For one Business Hours Scope and local date, Main Street shall resolve one authoritative effective operating-hours interpretation.**

Therefore:

```text
(scope S, date D)
        ↓
zero or one effective override interpretation
```

The system shall not rely on merchant-authored priority rules among competing overrides for the same scope/date.

---

# 16. Effective Business Hours are scope-qualified

Canonical:

```text
StandardBusinessHours(scope)
        +
BusinessOperatingOverride(scope, localDate)
        ↓
EffectiveBusinessHours(scope, localDate)
```

No data from another scope participates unless an explicit accepted contract requires it.

---

# 17. CurrentBusinessOperatingStatus is scope-qualified

Conceptually:

```text
CurrentBusinessOperatingStatus
{
    businessHoursScope
    state
    currentOperatingIntervalEnd?
    nextOpenInstant?
    basis
}
```

States remain:

```text
OPEN
CLOSED
UNSPECIFIED
```

`UNSPECIFIED` is evaluated for the requested scope.

Example:

```text
CurrentBusinessOperatingStatus(L1) = OPEN
CurrentBusinessOperatingStatus(L2) = UNSPECIFIED
```

is valid.

---

# 18. UNSPECIFIED is not inherited

If no Public Business Hours exist for Location L2:

```text
CurrentBusinessOperatingStatus(L2) = UNSPECIFIED
```

Main Street shall not silently substitute:

```text
Merchant Scope hours
```

or:

```text
Location L1 hours
```

unless an explicit future inheritance/binding contract says otherwise.

This prevents false operating claims.

---

# 19. Next-open resolution remains within scope

For:

```text
CurrentBusinessOperatingStatus(scope = L1)
```

`nextOpenInstant` shall be calculated from effective hours for L1 only.

It shall not jump to another location merely because another branch opens sooner.

A storefront may separately offer:

```text
Nearest/other location open now
```

through a location-discovery projection, but that is not L1's next-open status.

---

# 20. Overnight intervals remain scope-local

The v1.1 cross-midnight rule remains unchanged but is evaluated in the scope's time zone.

Example:

```text
Location L1
Friday 20:00 → Saturday 02:00
Europe/London
```

At Saturday 01:00 local time:

```text
CurrentBusinessOperatingStatus(L1) = OPEN
```

A different location/time zone is evaluated independently.

---

# 21. DST semantics remain scope-local

For every scope:

```text
current instant
        ↓
convert using scope IANA zone
        ↓
local date/time
        ↓
resolve EffectiveBusinessHours(scope)
        ↓
OPEN / CLOSED / UNSPECIFIED
```

Authoritative scheduling commitments still resolve ambiguous local civil times to unique instants before commitment.

---

# 22. Storefront projection for single-scope merchants

For an online or single-context merchant with Merchant Scope hours:

```text
Merchant Public Business Hours
        ↓
Storefront Opening Hours
        ↓
Open now / Closed status
```

The customer experience remains exactly as defined by v1.1.

No Merchant Location is required.

---

# 23. Storefront projection for location-scoped merchants

For a merchant with location-specific hours, the storefront shall preserve the association:

```text
Location L1
    address / label
    hours
    current status

Location L2
    address / label
    hours
    current status
```

The UI may compose these elegantly but may not present one location's hours as though they apply to all locations.

Hard rule:

> **Location-scoped Business Hours shall remain visibly attributable to the applicable location wherever ambiguity would otherwise arise.**

---

# 24. Contact projection and hours scope

A contact/location page may show hours for the relevant location.

Example:

```text
Swansea
12 High Street
Open 09:00–17:00
01234 111111
```

while another location may show different hours/contact information.

Contact-point scope is governed by MS-PROT-051.

Business Hours do not automatically select a contact point; projection composition uses explicit location/scope associations.

---

# 25. SEO structured data must preserve location scope

Where Main Street emits structured SEO/local-business data for multiple locations, it shall not flatten conflicting location hours into one merchant-wide schedule.

Each projected location representation should use the hours applicable to that location according to the supported SEO schema/representation.

Exact SEO technology remains downstream.

Hard invariant:

> **SEO projection shall not manufacture a broader operating-hours claim than the authoritative scope supports.**

---

# 26. Scheduling must resolve applicable Business Hours Scope

MS-PROT-050 v1.1 established Business Hours as a mandatory scheduling input where configured and relevant.

This is refined to:

> **Scheduling must evaluate the Business Hours Scope explicitly applicable to the requested Offering/Appointment/service context.**

Canonical:

```text
Appointment / Offering context
        +
explicit location/merchant operating binding
        ↓
applicable BusinessHoursScope
        ↓
EffectiveBusinessHours(scope)
        +
Scheduling constraints
        +
current operational state
        ↓
customer-presentable appointment options
```

---

# 27. Scheduling cannot guess location

If an Appointment Offering can occur at multiple locations, Main Street must not arbitrarily select one location's Business Hours.

The flow must resolve the intended location/context through supported semantics before location-dependent availability is final.

Valid possibilities may include:

```text
customer selects location
Offering is bound to one location
resource/fulfilment context determines location
merchant chooses location
```

where accepted contracts support them.

Rejected:

```text
first MerchantLocation in database
        ↓
use its hours
```

---

# 28. Scheduling cannot merge multiple location hours into one availability window

Suppose:

```text
Offering O1
available at L1 and L2

L1 hours: 09:00–17:00
L2 hours: 12:00–20:00
```

Main Street may present location-qualified appointment choices.

But it shall not create one false generic schedule:

```text
09:00–20:00
```

without preserving which location supports each time.

Availability remains contextual and provenance-bearing.

---

# 29. By-appointment exceptions remain scope-qualified

MS-PROT-050 v1.1 permits explicitly registered service/appointment periods outside ordinary Public Business Hours where truthfully represented.

Such an exception must remain associated with the relevant operating context.

Example:

```text
Swansea office public hours:
09:00–17:00

Evening consultations at Swansea:
by appointment until 19:00
```

This does not automatically permit evening appointments at Cardiff.

Nor does it make the merchant globally OPEN until 19:00.

---

# 30. Emergency-service semantics remain distinct from location hours

A tradesperson may have:

```text
Merchant Scope ordinary business hours:
08:00–17:00

Emergency call-out semantics:
24 hours
```

or location-specific public premises hours plus wider emergency availability.

The v1.1 distinction remains:

```text
ordinary operating status
        ≠
emergency/by-appointment service availability
```

Multi-location scoping does not collapse them.

---

# 31. Enquiry remains independent of Business Hours Scope

MS-PROT-050 v1.1's digital-contactability rule remains unchanged.

For any applicable merchant/location operating status:

```text
OPEN
CLOSED
UNSPECIFIED
```

must not by itself disable an otherwise enabled Enquiry interaction.

The customer may submit a merchant-scoped Enquiry regardless of whether a particular location is currently closed.

---

# 32. Enquiry acknowledgement must use the correct operating context

Where an Enquiry is initiated from a location-specific context, an acknowledgement may include that location's deterministic status.

Example:

```text
Customer viewing Swansea location
        ↓
submits Enquiry
        ↓
ReceiptAcknowledgement:
"The Swansea location is currently closed and reopens tomorrow at 09:00."
```

If the Enquiry is merchant-general and no Merchant Scope Business Hours exist, Main Street must not choose one branch and present its status as merchant-wide truth.

It may instead omit operating-status wording or present appropriately qualified multi-location information through a projection contract.

---

# 33. Receipt acknowledgement does not promise response time

The v1.1 rule remains unchanged:

```text
location/business open
        ≠
human currently available
        ≠
response-time SLA
```

Therefore location-specific OPEN/CLOSED status may inform the customer without promising when the merchant will reply.

---

# 34. Business Operating Override lifecycle remains contextual

A location-specific emergency closure is contextual operating state and need not recompile the merchant's semantic package.

Example:

```text
Cardiff L2
power outage
closed from 14:00 today
```

This produces/updates an L2-scoped `BusinessOperatingOverride`.

It does not:

```text
deactivate Merchant
change Swansea hours
recompile unrelated capabilities
```

MS-PROT-022 v1.5 remains authoritative for static/contextual separation.

---

# 35. Stable scope references and configuration lifecycle

A Scheduling/Offering configuration may hold a stable reference to:

```text
BusinessHoursScope = MerchantLocation L1
```

Changing that binding to L2 may be a configuration change requiring a new validated configuration revision.

By contrast:

```text
L1 closes early today
```

is contextual operating state and does not change the stable binding.

This preserves:

```text
which scope applies
        = stable configuration

what that scope's current effective hours are
        = configuration + contextual override
```

---

# 36. Location removal must preserve referential integrity

If Merchant Location L1 is referenced by active configuration, hours, outstanding commitments or historical records, deleting L1 cannot leave dangling references.

MS-PROT-051 and MS-PROT-040 govern the broader lifecycle.

Business Hours records for a retired location may need historical retention even when the location is no longer public/current.

Hard invariant:

> **Location retirement shall not erase the historical operating context of existing commitments or audit records.**

---

# 37. No category-specific hours model

Multi-location support shall not create:

```text
RetailOpeningHours
RestaurantOpeningHours
SalonOpeningHours
HotelOpeningHours
```

The same scope model supports:

```text
retailer branches
professional offices
clinic locations
workshops
service centres
other legitimate merchant locations
```

Business category remains contextual metadata.

---

# 38. Information publisher falsification

Scenario:

```text
Scholarship publisher
no physical location
Merchant Scope hours:
09:00–17:00 support hours
```

Result:

- Merchant Scope hours remain valid;
- no Merchant Location is required;
- Enquiry remains available when CLOSED;
- storefront may show support/business hours if configured.

**PASS**

---

# 39. Online consultant falsification

Scenario:

```text
Remote consultant
Merchant Scope hours:
09:00–17:00
Scheduling:
10:00–16:00
```

Result:

- scope = MERCHANT;
- Scheduling evaluates Merchant Scope hours plus narrower Scheduling constraints;
- no physical location required.

**PASS**

---

# 40. Single physical retailer falsification

Scenario:

```text
Retailer
Location L1
Mon–Sat 09:00–18:00
```

Result:

- location-scoped hours are valid;
- storefront associates address/hours/status with L1;
- Merchant Scope OPEN/CLOSED need not exist.

**PASS**

---

# 41. Multi-location retailer falsification

Scenario:

```text
Location L1 Swansea
09:00–17:00

Location L2 Cardiff
10:00–18:00
```

At 17:30:

```text
L1 CLOSED
L2 OPEN
```

Result:

- statuses remain distinct;
- no synthetic merchant `OPEN` status is created;
- storefront may say Cardiff open / Swansea closed;
- scheduling/location interactions use applicable scope.

**PASS**

---

# 42. Multi-time-zone merchant falsification

Scenario:

```text
London L1
Europe/London
09:00–17:00

New York L2
America/New_York
09:00–17:00
```

Result:

- each scope evaluates local civil time independently;
- next-open instants are calculated from the correct zone;
- one location's local date does not determine the other's override date.

**PASS**

---

# 43. Mixed remote and physical operation falsification

Scenario:

```text
Merchant-wide remote support
08:00–20:00

Physical office L1
09:00–17:00
```

Result:

- Merchant Scope and L1 Scope hours coexist;
- neither overrides the other;
- remote Enquiry/support projection may use Merchant Scope;
- office visit/scheduling context may use L1 Scope.

**PASS**

---

# 44. Location-specific emergency closure falsification

Scenario:

```text
L1 normal: 09:00–17:00
L2 normal: 09:00–17:00

L1 closes today at 13:00
```

Result:

- L1 override changes L1 EffectiveBusinessHours only;
- L2 remains unchanged;
- no semantic recompilation required merely for the closure;
- public projections refresh accordingly.

**PASS**

---

# 45. Location-specific appointment falsification

Scenario:

```text
Consultation Offering O1
available at L1 and L2

L1 hours: 09:00–17:00
L2 hours: 12:00–20:00
```

Result:

- customer scheduling preserves location context;
- 18:00 may be available at L2 but not L1;
- system cannot merge hours into one unexplained 09:00–20:00 window.

**PASS**

---

# 46. General Enquiry on multi-location merchant falsification

Scenario:

```text
Merchant has only location-scoped hours
L1 closed
L2 open
Customer submits general Enquiry from homepage
```

Result:

- Enquiry accepted;
- Main Street does not claim merchant-wide OPEN/CLOSED;
- acknowledgement may omit status or give explicitly qualified location summary;
- ReceiptAcknowledgement remains distinct from EnquiryResponse.

**PASS**

---

# 47. Rejected alternatives

## A. One hours set per Merchant

Rejected because multi-location merchants may have materially different operating schedules.

## B. Merchant Location as tenant

Rejected because location is contextual presence inside one Merchant Scope.

## C. Merchant hours automatically inherited by every location

Rejected because it creates hidden precedence and may publish false branch hours.

## D. Location hours automatically override merchant hours

Rejected because merchant and location scopes may describe different operating contexts rather than parent/child values.

## E. Merchant OPEN when any location is OPEN

Rejected because it manufactures merchant-wide truth from branch-level facts.

## F. Merchant CLOSED only when all locations are CLOSED

Rejected for the same reason; it creates an unstated aggregation definition.

## G. Merge all location hours for scheduling

Rejected because resulting availability loses the location that makes the time valid.

## H. Dated override applies to all locations by default

Rejected because emergency/holiday changes may be location-specific.

## I. One merchant-wide time zone

Rejected because multi-location merchants may operate across time zones.

---

# 48. Accepted invariants

1. Every Public Business Hours set has an explicit Business Hours Scope.
2. Initial supported scope kinds are Merchant and Merchant Location.
3. Operating status is always scope-qualified.
4. Merchant Location hours do not create a tenant boundary.
5. Merchant-scope and location-scope hours may coexist when they describe different operating contexts.
6. Coexisting scopes do not create implicit precedence.
7. Merchant hours do not automatically inherit to locations.
8. Missing location hours do not automatically fall back to merchant hours.
9. Editing/bulk-copy convenience does not create inheritance semantics.
10. Location OPEN/CLOSED values shall not be collapsed into a synthetic merchant-wide OPEN/CLOSED state.
11. Aggregate multi-location summaries are projections, not merchant operating authority.
12. StandardBusinessHours carry Business Hours Scope and scope-appropriate IANA time zone.
13. BusinessOperatingOverride is scope-qualified.
14. One Business Hours Scope/local date resolves one authoritative effective-hours interpretation.
15. `CurrentBusinessOperatingStatus` carries its Business Hours Scope.
16. `UNSPECIFIED` is scope-specific and shall not be silently inherited.
17. `nextOpenInstant` is resolved within the requested scope.
18. Cross-midnight and DST semantics remain local to each scope's time zone.
19. Storefront/SEO projections shall preserve location attribution where required for truthfulness.
20. Scheduling must resolve the applicable Business Hours Scope explicitly.
21. Scheduling cannot guess location or merge distinct location hours into one unqualified availability window.
22. By-appointment/emergency exceptions remain scope/context-qualified.
23. Enquiry eligibility remains independent of OPEN/CLOSED status.
24. Business-hours-aware Enquiry acknowledgement must use the correct operating context and cannot invent merchant-wide status.
25. Temporary operating overrides remain contextual state and do not automatically trigger semantic recompilation.
26. Stable references to Business Hours Scope may participate in configuration and therefore follow configuration lifecycle when bindings change.
27. Location retirement must preserve referential/historical integrity.
28. The same scope model supports physical, online, multi-location, multi-time-zone and hybrid merchants without category branches.

---

# 49. Relationship to MS-PROT-051

MS-PROT-051 owns Merchant Location identity and the Business Profile/presence composition boundary.

MS-PROT-050 owns Business Hours temporal meaning.

Canonical separation:

```text
MS-PROT-051
MerchantLocation L1 exists
        ↓

MS-PROT-050
BusinessHoursScope = L1
hours + overrides + time zone
        ↓
CurrentBusinessOperatingStatus(L1)
```

Neither contract absorbs the other's authority.

---

# 50. Implementation consequence

No implementation is required during the active design-first phase.

When implementation resumes, avoid a generic unbounded scope system solely because two scope kinds now exist.

The initial implementation should support only the accepted scope algebra required by concrete tests:

```text
MERCHANT
MERCHANT_LOCATION
```

No inheritance engine, precedence DSL or generic scope graph is justified by this amendment.

---

# 51. Continuous-improvement checkpoint

The key improvement is avoiding two opposite errors:

```text
one merchant-wide hours set
```

which is too narrow, and:

```text
arbitrary cascading scope hierarchy
```

which is too general.

The accepted middle is:

```text
small explicit scope algebra
        +
no implicit inheritance
        +
consumer resolves exact applicable scope
```

This is sufficient for publishers, online consultants, single physical merchants, multi-location merchants and mixed remote/physical operations without category-specific logic.

No further material correction was found within this bounded multi-location Business Hours scope after falsification.

---

# Governance verdict

**ACCEPTED.** Public Business Hours and operating status are scope-qualified. Main Street supports merchant-wide and Merchant Location-specific operating contexts without hidden inheritance, arbitrary precedence or misleading merchant-wide aggregation.