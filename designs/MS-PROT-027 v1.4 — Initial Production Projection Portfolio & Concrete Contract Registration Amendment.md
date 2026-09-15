# MS-PROT-027 v1.4 — Initial Production Projection Portfolio & Concrete Contract Registration Amendment

**Document ID:** MS-PROT-027  
**Version:** 1.4  
**Status:** **ACCEPTED after governed review, falsification, ambiguity review and manual approval**  
**Approved:** Manual approval on 27 August 2026  
**Amends:** MS-PROT-027 v1.1, v1.2 and v1.3 only within initial production projection selection and concrete contract-registration scope  
**Depends on:** MS-PROT-025; MS-PROT-026; MS-PROT-027 v1.1–v1.3; MS-PROT-036 v1.1; MS-PROT-037 v1.1; MS-PROT-041 v1.1; MS-PROT-049 v1.0–v1.3; MS-PROT-050; MS-PROT-051 v1.1; MS-PROT-053; MS-PROT-059; MS-PROT-069; MS-PROT-070; MS-PROT-072; MS-PROT-079  
**Resolves:** `MS-PROT-051-V11-DQ-005`  
**Closes:** MS-PROT-079 Target 7 — Projection contracts  
**Purpose:** Select Main Street's minimum initial production projection architecture, instantiate the owner-scoped Projection Contracts already required by accepted semantics, and prevent future capability implementation from introducing undocumented caches/read models or a speculative universal projection framework.

---

# 1. Governing Decision

Initial Main Street production SHALL use the **smallest projection architecture justified by accepted semantics**.

The default remains:

```text
authoritative owner
        ↓
accepted query boundary
        ↓
request-scoped derivation/composition
        ↓
consumer
```

A separate persisted, cached or asynchronously maintained read model SHALL NOT be introduced merely because CQRS/read-model infrastructure is conventional.

Where MS-PROT-027 v1.3 Trigger A–G applies, the projection instead requires an explicit registered Projection Contract.

Therefore:

```text
SYNCHRONOUS REQUEST-SCOPED READ
    default where sufficient

EXPLICIT PROJECTION CONTRACT
    mandatory where v1.3 trigger applies

GENERIC PROJECTION PLATFORM
    not required
```

---

# 2. Initial Production Projection Portfolio

Target 7 establishes two concrete cross-cutting Projection Contracts required before later capability targets:

```text
1. merchant-presence
2. merchant-calendar
```

Other current reads remain owner queries or request-scoped compositions unless an accepted contract proves that one of MS-PROT-027 v1.3's triggers applies.

Later capability targets MUST explicitly make that determination.

---

# 3. No Universal Projection Registry Service

A stable catalogue of Projection Contract identities is required for governance and dependency resolution.

That does **not** imply a production service such as:

```text
ProjectionRegistryServer
GenericProjectionEngine
ProjectionDSL
ProjectionDatabase
```

A concrete implementation may ultimately use ordinary Java registration/configuration, module-owned contracts or another simple mechanism.

The architectural requirement is stable contract identity and enforceable semantics, not infrastructure ceremony.

---

# Contract 1 — Merchant Presence Projection

# 4. Identity

Canonical contract identity:

```text
platform / merchant-presence
```

The precise code representation may differ, but the identity must remain stable and unambiguous.

---

# 5. Projection Owner

The projection owner is the **Merchant Presence read/composition responsibility**.

This is a projection owner, not a new business-fact owner.

It may combine facts owned by:

```text
MS-PROT-051
    Merchant Public Descriptor
    Contact Points
    Merchant Locations
    Service Areas
    External-Presence Links

MS-PROT-050
    Public Business Hours where applicable
```

and other separately authorised presentation references where a later accepted contract requires them.

The projection owner gains no mutation authority over those facts.

---

# 6. Why a Contract Is Mandatory

`merchant-presence` satisfies MS-PROT-027 v1.3 applicability independently of physical caching because it is a **multi-source composition** and baseline public/storefront presence is an established platform read responsibility.

It therefore cannot be left as:

```text
frontend queries several tables
and hopes they describe one current merchant
```

---

# 7. Source Authority

The projection SHALL preserve exact source ownership.

Conceptually:

```text
Merchant descriptor ───────────┐
Contact points ────────────────┤
Merchant Locations ────────────┤
Service areas ─────────────────┤
External-presence links ───────┤
Public Business Hours ─────────┘
                               ↓
                    merchant-presence projection
```

No copy inside the projection becomes authoritative merely because it is easier to query.

---

# 8. Initial Materialisation Strategy

Initial production SHALL **not require a separately persisted Merchant Presence read model**.

The initial contract is satisfied through bounded request-scoped projection from current owner queries.

This deliberately avoids creating:

```text
merchant_profile_projection table
Redis merchant profile
storefront JSON snapshot
search document
```

before performance or distribution requirements justify them.

Those may later be introduced, but Trigger A/B would then require the same contract to define appropriate checkpoint/materialisation evidence.

---

# 9. Freshness Evidence

For the initial request-scoped implementation, freshness is established from the authoritative revision/current-state evidence returned by each participating owner during the projection request.

Conceptually:

```text
descriptorRevision
contactPointRevision(s)
locationRevision(s)
serviceAreaRevision(s)
externalPresenceRevision(s)
businessHoursRevision/scope where included
```

A single:

```text
generatedAt = 14:32
```

is insufficient to claim that every source has equal freshness.

---

# 10. No Global Atomic Snapshot Requirement

Merchant Presence is informational projection.

Its sources do not require one giant cross-owner transaction solely to render a storefront.

Therefore Target 7 does **not** require:

```text
Profile
+
Business Hours
+
Media
+
Presentation
```

to share one database transaction/version.

Each participating source retains its own exact current authority.

The projection must simply avoid making false claims about provenance/currentness.

---

# 11. Profile Corrections

Suppose:

```text
telephone revision 5 → revision 6
```

An authoritative Profile mutation commits first.

Any later Merchant Presence projection must derive from the current owner state.

A previous projected telephone value has no authority to overwrite or restore revision 5.

---

# 12. Location Relocation

Because MS-PROT-051 v1.1 requires real relocation to create a new MerchantLocation identity:

```text
L1 retired
L2 active
```

Merchant Presence must project current eligible location identities.

It must not silently render L1's historical address as though it were L2 merely because an old projection contains it.

---

# 13. Stale Serving — Initial Rule

Initial production SHALL NOT deliberately serve known-stale Merchant Profile/Location facts merely to preserve public availability.

Therefore:

```text
known stale presence element
    → do not represent it as current
```

This is intentionally conservative.

A future CDN/static-generation architecture may establish bounded stale serving through a governed amendment or contract evolution.

---

# 14. Missing Source Evidence

Failure of one source does not give the Projection Owner permission to invent its value.

Where possible, independently established elements may remain serviceable.

Example:

```text
Business Hours source unavailable
Profile descriptor current
```

may yield a truthful reduced presence:

```text
name
description
contact
location

opening-hours element unavailable
```

rather than claiming cached hours are current.

However, if the minimum merchant identity/descriptor necessary to render a truthful presence cannot be established, the Merchant Presence projection is not serviceable as a complete merchant-presence representation.

---

# 15. Element-Level Independence

A source failure must not unnecessarily erase independently current elements.

Thus:

```text
telephone source valid
hours source unavailable
```

does not automatically require the telephone to disappear.

This follows MS-PROT-027 v1.3's reduced-projection rule.

---

# 16. Exposure Boundary

Target 7 determines whether the projection data is technically serviceable.

It does **not** determine whether a particular audience may observe it.

Canonical:

```text
authoritative Profile fact
        ↓
merchant-presence projection serviceability
        ↓
Target-8 Exposure resolution
        ↓
audience-visible result
```

Therefore:

```text
location exists
+
projection current
```

still does not mean:

```text
PUBLIC may see location
```

---

# 17. Revocation-Sensitive Behaviour

Where current privacy/security/Exposure authority determines that previously projected information must no longer be exposed, ordinary stale-serving tolerance cannot preserve it.

The contract therefore retains the v1.3 rule:

```text
current revocation evidence
    outranks
old projection availability
```

The exact Exposure resolver is Target 8.

---

# 18. Rebuildability

The initial request-scoped Merchant Presence projection has no separate mutable projection store to rebuild.

Its rebuild semantics are:

```text
re-query retained current authoritative sources
        ↓
rederive representation
```

Accordingly, its rebuildability claim is no stronger than availability/retention of those authoritative sources.

If future implementation introduces persisted materialisation, that implementation must define a genuine rebuild/checkpoint strategy under this contract before the materialised representation is relied upon.

---

# 19. Reconciliation

There is initially no independent Merchant Presence truth requiring business reconciliation.

A discrepancy such as:

```text
projection says phone 111
Profile authority says phone 222
```

has one resolution:

```text
projection converges toward Profile authority
```

never:

```text
Profile authority changed back to 111
```

---

# 20. Access

Projection generation occurs only inside the resolved Merchant Scope.

Access to source facts remains subject to owner/security constraints.

Projection composition does not broaden access.

Target 8 subsequently determines audience Exposure.

---

# Contract 2 — Main Street Calendar Projection

# 21. Identity

Canonical contract identity:

```text
calendar / merchant-calendar
```

It corresponds to the Main Street Calendar responsibility already established by MS-PROT-041 v1.1.

---

# 22. Projection Owner

The Projection Owner is the **Main Street Calendar read/composition responsibility**.

It owns calendar-shaped derivation and serviceability.

It does not own:

```text
Appointment
Booking
Scheduling rules
Business Hours
Merchant Schedule Intent
Resource/capacity truth
external provider events
```

---

# 23. Why a Contract Is Mandatory

Calendar satisfies multiple v1.3 triggers:

```text
Trigger C
    registered Surface/composition dependency

Trigger F
    multiple independently current source authorities

Trigger D where applicable
    optional external constraint source may fail
```

Future caching/materialisation would additionally activate A/B.

---

# 24. Calendar Source Set

The projection may compose:

```text
Appointment commitments

applicable Booking timing

Merchant Schedule Intents

applicable Scheduling configuration

Business Hours / operating windows

resource/capacity representation where required

accepted external busy-time constraints
    where an integration is enabled
```

Each remains owned by its accepted authority.

---

# 25. Calendar Representation ≠ Availability Authority

This distinction is mandatory.

```text
Calendar visually shows:
    14:00 appears open

        ≠

Scheduling authoritatively guarantees:
    14:00 may now be committed
```

A booking/appointment command still revalidates authoritative Scheduling state.

MS-PROT-041 already requires Calendar gestures to resolve to semantic commands rather than mutate projected events.

---

# 26. Freshness Evidence

Calendar freshness cannot be represented by one `updatedAt`.

The contract must retain or establish sufficient independently material source evidence for the requested view.

Conceptually:

```text
Appointment source progress
Booking source progress
Schedule-Intent source progress
Scheduling/configuration identity
Business-Hours revision
capacity source progress where applicable
external-busy evidence provenance where applicable
```

The exact checkpoint representation remains implementation detail.

---

# 27. Calendar Read-Use Distinction

The same calendar representation supports materially different read purposes.

At minimum, the contract distinguishes:

```text
A. committed/scheduled-work overview

B. availability-oriented interpretation
```

A representation may remain useful for A while being unsafe for B.

---

# 28. Committed-Work Overview

A merchant may continue to view independently established current Main Street commitments where optional external-constraint evidence is temporarily unavailable.

Example:

```text
Appointments current
External calendar unavailable
```

Result:

```text
existing Main Street Appointments remain visible
```

because the external provider does not own them.

This preserves MS-PROT-041 v1.1.

---

# 29. Availability-Oriented Serviceability

A view claiming or supporting **current availability** requires all authoritative inputs that the applicable Scheduling contract says are necessary for that availability decision.

If a required input cannot be established:

```text
do not claim current availability
```

The system may still render a reduced committed-work view.

This prevents:

```text
Google disconnected
    ↓
calendar treats unknown external time as free
```

where imported busy constraints materially participate.

---

# 30. External Provider Outage

External calendar failure must not erase Main Street commitments.

Therefore:

```text
Appointment A = current
external calendar unavailable
```

may yield:

```text
A shown
external-constraint-dependent availability degraded
```

not:

```text
A disappears
```

and not:

```text
all unknown time assumed available
```

---

# 31. Stale External Constraints

If an external busy-time constraint becomes too stale to satisfy its governing integration/read contract, Calendar must stop treating it as current constraint evidence.

The owning Scheduling/provider contract later determines the operational consequence for offering new slots.

Calendar cannot invent that consequence.

---

# 32. Partial Serviceability

Calendar may truthfully degrade to a narrower representation.

Examples:

```text
commitments only
without availability claim
```

or:

```text
Main Street schedule only
with external integration state unavailable
```

where those remaining elements have independently valid evidence.

No universal `DEGRADED` domain state is introduced.

---

# 33. Rebuild

Initial Main Street Calendar may be derived request-by-request from authoritative Main Street state.

No mandatory second Calendar store exists.

If future performance requirements materialise Calendar state, its contract must preserve enough source progress/provenance to rebuild it.

A rebuild never repairs Appointment/Booking source truth.

---

# 34. External Calendar Projection Is Different

The outbound:

```text
Main Street Appointment
        ↓
Google/Microsoft/etc calendar event
```

is an **integration projection**, not the same contract as `calendar/merchant-calendar`.

Its provider-specific delivery/reconciliation remains under MS-PROT-041/MS-PROT-048 and later Provider/Events targets.

Target 7 does not collapse them.

---

# Storefront and Dashboard Decisions

# 35. Storefront Composition Is Initially Request-Scoped

MS-PROT-036 permits cached/generated storefront projections but does not require them.

Target 7 therefore chooses the simpler initial architecture:

```text
serviceable projection/read inputs
+
current surface/configuration inputs
+
Exposure
        ↓
request-scoped storefront composition
```

The storefront as a whole is **not initially required to be a separately persisted projection**.

---

# 36. No Static Storefront Authority

Initial production SHALL NOT require:

```text
one generated storefront JSON document
one generated static website snapshot
one merchant HTML source of truth
```

as architectural authority.

If static generation/CDN caching is introduced later, Trigger A/B/G must be satisfied by an explicit contract before that representation becomes serviceable production data.

---

# 37. Dashboard Composition Is Initially Request-Scoped

MS-PROT-037 already defines the dashboard as composition over:

```text
Merchant Configuration
Enabled semantics
Actor authority
Operational projections
```

and explicitly requires backend authority to remain authoritative.

Therefore the initial dashboard navigation/workspace composition is request-scoped.

No materialised:

```text
MerchantDashboardProjection
```

is required.

---

# 38. Dashboard Actor Authority Is Current

Dashboard composition SHALL NOT cache actor authority into a durable read model and use that cached result as security authority.

Current access must continue through the trusted/runtime authority boundary.

A cached menu is never permission.

---

# 39. Dashboard Home/Attention Projection

The "merchant home" attention summary in MS-PROT-037 is optional composition, not yet a mandatory independently materialised Projection Contract.

Initially it may query/combine serviceable owner reads request-by-request.

If Target 10–19 introduces asynchronous/cached attention aggregation, that projection must receive its own contract before use.

---

# Later Capability Projection Rule

# 40. Target-Closure Obligation

For every later capability target, the target cannot be Design-Closed until each required read representation has been classified as exactly one of:

```text
A. authoritative owner query

B. synchronous request-scoped projection
   satisfying the MS-PROT-027 v1.3 exception

C. explicit registered Projection Contract
```

"Frontend will cache it" is not a fourth category.

---

# 41. Explicit Trigger Evaluation

Each later target must evaluate MS-PROT-027 v1.3 Triggers A–G.

If any trigger applies, an explicit Projection Contract is mandatory.

The later target cannot defer its semantic serviceability to implementation.

---

# 42. Capability-Specific Examples

Likely future candidates include:

```text
Publication/search/catalogue projections
    → Target 10

Appointment availability / scheduling views
    → Target 11

Inventory availability/catalogue/order views
    → Target 12

Payment status/read representations
    → Target 13

Shipment tracking
    → Target 14

Notification delivery views
    → Target 16

cross-cutting background/event-derived views
    → Target 18
```

This list does **not** pre-accept that each requires persistent projection machinery.

Each owner must apply the A–G tests.

---

# Failure Semantics

# 43. Required Distinctions

Target 7 retains the v1.3 distinctions:

```text
SOURCE BUSINESS REJECTION

EXPOSURE / SECURITY / PRIVACY WITHHOLD

PROJECTION NOT SERVICEABLE

PROJECTION INFRASTRUCTURE FAILURE

PROJECTION REBUILD / CATCH-UP
```

These remain semantically distinct even if Target 20 later maps some to common HTTP responses.

---

# 44. No Stale Data for Command Authority

Under no Target-7 contract may:

```text
cached availability
cached stock
cached profile
cached calendar state
cached dashboard state
```

become mutation authority.

Commands use their owning capability/application authority and revalidate applicable business state.

---

# 45. Falsification Record

| Scenario | Required result |
|---|---|
| Brochure merchant has profile + telephone only | Merchant Presence composes validly; no capability-specific read model required |
| Merchant has no location | No fake location projected |
| Merchant changes telephone | Next projection uses current Profile authority |
| Old cached telephone remains somewhere | It cannot overwrite current authority and cannot be treated as currently serviceable without an applicable contract |
| Merchant moves from L1 to L2 | Current presence resolves L2; history may still refer to L1 |
| Business Hours unavailable | Presence may omit/degrade hours without inventing them |
| Private home address exists | Projection serviceability does not grant PUBLIC Exposure |
| Address Exposure revoked | Old representation cannot continue exposing it merely because it is "fresh enough" |
| Storefront request | Composes request-scoped; no mandatory static site snapshot |
| Dashboard request | Composes request-scoped; no global DashboardProjection required |
| Staff privilege revoked | Cached/navigation representation cannot restore authority |
| Main Street Appointment exists, Google Calendar down | Appointment remains visible in Calendar |
| Google busy-time import unavailable | Calendar must not silently claim provider-dependent availability |
| Calendar looks empty at 14:00 | Appointment command still revalidates Scheduling authority |
| Calendar materialisation later introduced | Trigger A/B requires checkpoint/rebuild semantics before production reliance |
| Publication cache introduced at Target 10 | Target 10 must register a contract rather than relying on generic cache defaults |
| Inventory cache introduced at Target 12 | It remains read-only; Order commit revalidates Inventory |
| Projection infrastructure fails | Source business truth remains intact |
| Projection rebuild fails | Rebuild cannot mutate authoritative source to "repair" it |
| One source of multi-source view lags | `generatedAt` cannot pretend every source is equally current |

**Result:** PASS.

---

# 46. Rejected Alternatives

## 46.1 One generic Main Street Projection Engine

**Rejected.** MS-PROT-027 v1.3 found no evidence requiring a generic framework.

## 46.2 Materialise every dashboard/storefront view

**Rejected.** Introduces persistence, invalidation and reconciliation problems before they are required.

## 46.3 Use Redis everywhere

**Rejected.** Technology is not a Projection Contract.

## 46.4 One `lastUpdated` timestamp

**Rejected.** Cannot represent independently current multi-source projections.

## 46.5 One global TTL

**Rejected.** Already prohibited by v1.3.

## 46.6 Make the storefront generated artefact authoritative

**Rejected.** Storefront is composition/projection, not merchant business truth.

## 46.7 Make dashboard composition a security cache

**Rejected.** Projection cannot become Actor Authorisation.

## 46.8 Make Calendar a second schedule database

**Rejected.** MS-PROT-041 v1.1 already rejects this.

## 46.9 Hide all Calendar data if Google fails

**Rejected.** External calendar does not own Main Street commitments.

## 46.10 Treat missing external data as free availability

**Rejected.** Unknown evidence cannot be converted into current availability.

## 46.11 Design all later capability projections now

**Rejected.** Would pre-empt Targets 10–19 before their actual operational consistency requirements are closed.

---

# 47. Hard Invariants

```text
MS-PROT-027 v1.3 remains the generic Projection Contract authority.

No new generic projection framework is introduced by Target 7.

Initial production defaults to authoritative queries /
request-scoped composition.

A materialised projection exists only where justified.

Every v1.3 A–G trigger requires an explicit contract.

Merchant Presence has one explicit Projection Contract.

Main Street Calendar has one explicit Projection Contract.

Merchant Presence combines source facts without taking ownership.

Profile source revisions remain independently authoritative.

Presence serviceability does not create Exposure.

Known-stale Profile/Location facts are not intentionally served
as current in the initial architecture.

Merchant Presence initially requires no separate projection store.

Main Street Calendar remains derived from Main Street truth.

Calendar does not become Scheduling availability authority.

Calendar may preserve current commitments while an optional
external provider is unavailable.

Unknown external constraint state never means free time.

Storefront composition is initially request-scoped.

Dashboard composition is initially request-scoped.

Dashboard projection state never becomes Actor Authorisation.

No whole-storefront or whole-dashboard materialisation is mandatory.

Later capability targets must explicitly classify every required
read representation before Design-Closure.

Future materialisation activates applicable v1.3 obligations.

Projection failure never rewrites source business truth.

Projection rebuild never mutates source truth.

Stale projection data never becomes command authority.
```

---

# 48. Deferred Question Catalogue

The following retained questions are deferred and inactive until their stated revisit trigger:

| ID | Deferred question | Classification / owner | Revisit trigger |
|---|---|---|---|
| `MS-PROT-027-V14-DQ-001` | Exact Java representation/registration mechanism for Projection Contracts | Implementation architecture | During production query/projection implementation |
| `MS-PROT-027-V14-DQ-002` | Exact source-revision/checkpoint representation | Implementation detail | When first non-request-scoped projection is implemented |
| `MS-PROT-027-V14-DQ-003` | Whether any initial Merchant Presence data needs physical cache/materialisation | Performance architecture | Only if measured storefront performance requires it |
| `MS-PROT-027-V14-DQ-004` | Exact CDN/static-generation contract for storefronts | Delivery architecture | Before CDN/static storefront generation is enabled |
| `MS-PROT-027-V14-DQ-005` | Exact Calendar projection materialisation/cache strategy | Target 11 / implementation | When Calendar performance requirements are measured during Scheduling implementation |
| `MS-PROT-027-V14-DQ-006` | Exact projection rebuild worker/process mechanism | Target 18 | During Events/background-process completion if materialised projections require it |
| `MS-PROT-027-V14-DQ-007` | Exact projection-update idempotency/checkpoint persistence schema | Target 18 / persistence implementation | When asynchronous projection updates are introduced |
| `MS-PROT-027-V14-DQ-008` | Exact stale/degraded transport representation | Target 20 | During Production APIs |
| `MS-PROT-027-V14-DQ-009` | Exact projection lag/serviceability operational telemetry | Target 19 | During Observability/Reconciliation |
| `MS-PROT-027-V14-DQ-010` | Whether Redis or another dedicated cache is justified | Implementation/performance architecture | Only after measured DB/query load demonstrates need |
| `MS-PROT-027-V14-DQ-011` | Exact search-index technology and rebuild contract | Later owning capability | When Publication/Product/search requirements demonstrate an index is needed |
| `MS-PROT-027-V14-DQ-012` | Generic reusable projection-support library/framework | Future implementation architecture | Only after multiple concrete implementations demonstrate meaningful common behaviour |
| `MS-PROT-027-V14-DQ-013` | Exact reduced/degraded Calendar presentation semantics | Target 11/UI implementation | During integrated Calendar/Scheduling design and later UI |
| `MS-PROT-027-V14-DQ-014` | Exact field/element-level Profile projection provenance encoding | Persistence/query implementation | During Merchant Presence implementation |

Stable traceability rules:

1. These IDs remain stable.
2. Material architecture promotion follows DESIGN-RULES.
3. Implementation detail follows IMPLEMENTATION-RULES where applicable.
4. Any resolving authority/evidence MUST be recorded in the canonical Deferred Decision Register.
5. A deferred item MUST NOT silently weaken the explicit owner, freshness, serviceability, Exposure separation, source-authority or no-stale-write invariants accepted here.

---

# 49. Resolution of MS-PROT-051-V11-DQ-005

`MS-PROT-051-V11-DQ-005` is RESOLVED by this amendment:

> **Initial Merchant Profile/Location public/presence projection uses the `platform/merchant-presence` Projection Contract; it initially derives request-scoped from independently authoritative Profile/Location and applicable Business Hours sources, preserves source-revision provenance, does not intentionally stale-serve known-outdated profile/location facts, and remains separate from Exposure and business mutation authority.**

The item MUST NOT be deferred again under another identifier merely to postpone the same architecture decision.

---

# 50. Target-7 Conformance Gate

Target 7 is Design-Closed only when the accepted corpus guarantees:

```text
[x] MS-PROT-027 v1.3 remains generic projection authority
[x] no speculative generic framework is required
[x] initial projection portfolio is explicit
[x] request-scoped projection is the default
[x] Merchant Presence contract identity is explicit
[x] Merchant Presence owner is explicit
[x] Merchant Presence authoritative sources are explicit
[x] Profile/Location revisions remain source authority
[x] multi-source freshness is not collapsed into generatedAt
[x] Presence stale-serving rule is explicit
[x] missing-source behaviour is explicit
[x] Presence rebuild/recompute semantics are explicit
[x] Profile projection is separate from Exposure
[x] MS-PROT-051-V11-DQ-005 is resolved
[x] Calendar contract identity is explicit
[x] Calendar owner/source composition is explicit
[x] Calendar committed-work and availability use differ
[x] optional-provider outage cannot erase Main Street commitments
[x] unknown provider evidence cannot manufacture free availability
[x] Calendar remains non-authoritative
[x] Storefront whole-view materialisation is not mandatory
[x] Dashboard whole-view materialisation is not mandatory
[x] current actor authority is never delegated to dashboard cache
[x] later targets have explicit A–G classification obligation
[x] stale projections cannot authorise mutation
[x] rebuild cannot mutate source truth
[x] deferred implementation choices have stable IDs
```

---

# 51. Amendment Effect

MS-PROT-027 v1.4:

1. preserves v1.3 as the generic projection-contract authority;
2. rejects speculative universal projection infrastructure;
3. establishes request-scoped composition as the initial default;
4. establishes the initial required production projection portfolio;
5. registers `platform/merchant-presence`;
6. defines its sources, freshness, serviceability, stale-serving and recomputation semantics;
7. resolves `MS-PROT-051-V11-DQ-005`;
8. registers `calendar/merchant-calendar`;
9. defines its multi-source/currentness and partial-serviceability contract;
10. preserves current Main Street commitments during optional external-calendar failure;
11. prevents Calendar projection from becoming Scheduling authority;
12. makes storefront composition initially request-scoped;
13. makes dashboard composition initially request-scoped;
14. prohibits cached dashboard state from becoming actor authority;
15. requires every later capability target to classify each required read representation under the v1.3 A–G rules before Design-Closure;
16. catalogues `MS-PROT-027-V14-DQ-001` through `MS-PROT-027-V14-DQ-014`;
17. closes **MS-PROT-079 Target 7 — Projection contracts**; and
18. activates **Target 8 — Exposure resolution**.

---

# 52. Acceptance Statement

> **Main Street does not build a projection platform merely because projections exist. Initial production derives reads directly from authoritative owners wherever that is sufficient, and introduces an explicit Projection Contract only where accepted serviceability requirements demand one. Merchant Presence and Main Street Calendar are the initial cross-cutting contracts; every later cached, asynchronous, multi-source, source-outage or revocation-sensitive representation must earn and declare its own contract before production use.**

Canonical model:

```text
AUTHORITATIVE OWNER
        │
        ├── simple/current query
        │       ↓
        │   request-scoped projection
        │
        └── A–G trigger applies
                ↓
        explicit Projection Contract
                ↓
        freshness/serviceability evidence
                ↓
        Exposure / security / authority
                ↓
             consumer
```

**Review:** PASS  
**Cross-authority review:** PASS  
**Falsification:** PASS  
**Ambiguity review:** PASS  
**Manual approval:** GRANTED on 27 August 2026  
**Governance verdict:** **ACCEPTED**
