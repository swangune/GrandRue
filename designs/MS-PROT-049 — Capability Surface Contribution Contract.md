# MS-PROT-049 — Capability Surface Contribution Contract

**Document ID:** MS-PROT-049  
**Version:** 1.0  
**Status:** **ACCEPTED after cross-domain falsification and manual approval**  
**Depends on:** MS-PROT-027 v1.1, MS-PROT-029 v1.1, MS-PROT-036 v1.1, MS-PROT-037 v1.1, MS-PROT-040 v1.0, MS-PROT-047 v1.0, MS-PROT-048 v1.1  
**Purpose:** Define how registered capabilities contribute reusable merchant, customer and public operating surfaces without turning capabilities into UI applications, duplicating projection authority, exposing irrelevant platform breadth or creating business-category dashboards/templates.

> **Current-authority navigation notice:** This accepted document remains part of the current composite MS-PROT-049 authority. Within customer-facing storefront presentation-profile and website-presentation recommendation scope, read it together with **MS-PROT-094 v1.0**. MS-PROT-094 supersedes the use of a `registered presentation profile` or equivalent template/theme/style/domain-layout selector as the governing customer-facing website composition mechanism. Capability contributions remain bounded inputs; they do not own whole-site composition. Historical wording below is retained as design provenance and MUST NOT be implemented where MS-PROT-094 governs.

---

## 1. Governing decision

> **A capability may declare bounded surface contributions describing which business-facing workspace, action, attention signal, configuration entry or public interaction it can supply. Surface contributions are declarative inputs to composition; they do not own page layout, routes, frontend components, navigation trees or business authority.**

Canonical separation:

```text
Authoritative operational state
        +
Active / residual merchant semantics
        +
Configuration
        +
Audience / actor authority
        +
Exposure rules
        +
Fulfilment health
        ↓
Applicable capability surface contributions
        ↓
Registered composition targets
        ↓
Projection / presentation composition
        ↓
Merchant / Customer / Public surfaces
```

Hard rule:

> **Capability activation does not imply creation of a screen or navigation item.**

---

## 2. Relationship to existing projection authorities

MS-PROT-049 is deliberately narrower than the existing projection and composition specifications.

- **MS-PROT-027** remains authoritative for read models, audience projection, visibility and the rule that projections are not business truth.
- **MS-PROT-036** remains authoritative for storefront composition and bounded presentation profiles.
- **MS-PROT-037** remains authoritative for merchant-dashboard composition and mobile-first operations.
- **MS-PROT-040** remains authoritative for configuration activation, deactivation and residual management of outstanding commitments.
- **MS-PROT-048** remains authoritative for provider fulfilment and degraded provider health.

MS-PROT-049 answers only:

> **How does a capability declare reusable contributions to those already-defined surfaces?**

Therefore:

```text
Capability Surface Contribution Contract
        ≠
Read Model / Projection Model
        ≠
Dashboard Layout
        ≠
Storefront Template
        ≠
Frontend Component Registry
```

---

## 3. Scope

This document governs:

```text
surface contribution identity
surface contribution ownership
audience applicability
contribution kinds
composition targets
eligibility
actor-authority filtering
residual-management contributions
provider-degradation consequences
composition/deduplication boundaries
business-facing terminology boundary
```

It does not define:

```text
React / Next.js components
route names
navigation hierarchy
visual design
CSS / breakpoints
copywriting
page layout
component library
backend endpoint paths
read-model storage
cache technology
AI-generated layout
business-category templates
```

---

## 4. Contribution, projection and presentation are different

Three concepts shall remain distinct.

### Surface contribution

Declares that a capability can supply a particular business-facing operating contribution.

### Projection

Supplies audience-appropriate read information derived from authoritative state.

### Presentation

Determines how eligible contributions and projections are visually composed.

Canonical flow:

```text
Capability semantics
        ↓
Surface contribution
        ↓
Projection requirements
        ↓
Presentation composition
```

A surface contribution shall not become a second domain model.

---

## 5. Surface contribution identity and ownership

A contribution is platform-registered and owned by one capability or explicitly defined platform context.

Conceptually:

```text
SurfaceContributionIdentity
{
    ownerCapabilityIdentifier
    contributionIdentifier
}
```

A bare contribution identifier is not authoritative across capabilities.

Examples:

```text
booking / manage-bookings
booking / create-booking-action
publication / manage-publications
publication / public-opportunity-entry
inventory / low-stock-attention
```

The merchant cannot author new contribution types, arbitrary routes or executable UI behaviours.

---

## 6. Audience model

Initial semantic audiences are:

```text
MERCHANT
CUSTOMER
PUBLIC
```

Staff are normally authorised actors within the `MERCHANT` audience rather than a fourth semantic audience.

Different staff roles may receive different actions/data through actor authority without requiring separate capability definitions.

Customer account registration remains optional according to MS-PROT-027/043; `CUSTOMER` may represent contextual customer interaction rather than a global account.

---

## 7. Initial contribution-kind algebra

The initial contribution vocabulary is deliberately small:

```text
WORKSPACE
ACTION
ATTENTION
CONFIGURATION
PUBLIC_INTERACTION
```

New kinds require evidence that they represent materially different composition behaviour rather than a visual variant.

---

## 8. WORKSPACE contribution

A `WORKSPACE` contributes an operational area in which an audience can inspect or manage related business activity.

Examples:

```text
Bookings
Appointments
Orders
Products
Inventory
Enquiries
Publications
Customers
Calendar
```

A workspace declaration does not prescribe:

```text
table
list
calendar widget
cards
page route
navigation position
```

Those are presentation/composition decisions.

---

## 9. ACTION contribution

An `ACTION` exposes a legitimate business operation or bounded use case through an applicable surface.

Examples:

```text
Create booking
Confirm booking
Cancel appointment
Publish opportunity
Add product
Respond to enquiry
```

An action contribution references registered application/domain authority; it does not create the operation.

Hard rule:

> **Rendering an action never grants authority to execute it.**

Backend/application authority remains authoritative.

---

## 10. ATTENTION contribution

An `ATTENTION` contribution surfaces work or degraded conditions requiring awareness/action.

Examples:

```text
booking awaiting confirmation
new enquiry
low stock
publication nearing deadline
provider connection requires attention
failed notification delivery requiring intervention
```

Attention signals may be derived from projections or operational/integration state according to the owning contract.

An attention contribution is not itself a universal alert state machine.

---

## 11. CONFIGURATION contribution

A `CONFIGURATION` contribution provides a business-facing entry point to valid bounded configuration for active semantics.

It may expose controls derived from MS-PROT-047 configuration decisions.

It does not redefine those decisions or place merchant-facing wording in the semantic contract.

Examples:

```text
booking confirmation mode
customer cancellation policy
working hours
publication exposure settings
```

Configuration contribution eligibility does not bypass MS-PROT-040 review/approval/activation rules.

---

## 12. PUBLIC_INTERACTION contribution

A `PUBLIC_INTERACTION` contribution exposes a legitimate public/customer-facing interaction entry point.

Examples:

```text
book
order
enquire
subscribe
apply externally
browse opportunities
```

It must correspond to already-supported semantics and exposure rules.

A button, form or route cannot create business meaning merely by existing.

---

## 13. One capability does not equal one screen

Rejected model:

```text
Capability
    ↓
Screen
    ↓
Navigation item
```

Capabilities may:

- contribute several kinds to one composed workspace;
- contribute to an existing shared workspace;
- contribute only an action or attention signal;
- contribute no dedicated visual surface at all.

Examples of potentially headless or mostly contextual capabilities include notification delivery, auditing or infrastructure-support concerns.

Hard rule:

> **Main Street shall not create navigation merely to prove that a capability exists.**

---

## 14. Registered composition target

Multiple capabilities may legitimately contribute to the same business-facing surface.

Example:

```text
Scheduling
    → Calendar workspace

Booking
    → Calendar contextual actions
    → Booking workspace

Appointment
    → Calendar scheduled-operation projection
```

Therefore a contribution may reference a registered **composition target**.

Conceptually:

```text
SurfaceContributionDefinition
{
    identity
    audience
    contributionKind
    compositionTargetReference?
    projectionRequirements
    supportedOperationReferences
    eligibilityContract
    presentationDescriptorReference?
}
```

A composition target is a platform-owned presentation/composition identity. It is not a domain object and does not confer business authority.

Examples might include conceptual targets such as:

```text
merchant/calendar
merchant/bookings
merchant/home-attention
public/primary-actions
public/content
```

Exact identifiers and UI layout are implementation details.

---

## 15. Composition targets prevent duplicate surfaces

Without a composition target, capability composition could produce:

```text
Booking Calendar
Scheduling Calendar
Appointment Calendar
```

when the merchant actually needs one coherent Calendar surface.

Correct pattern:

```text
Scheduling contribution ─┐
Booking contribution ────┼→ registered Calendar target → one composed surface
Appointment contribution ┘
```

Hard rule:

> **Composition shall aggregate compatible contributions rather than mechanically rendering one surface per capability.**

This is a presentation/composition rule, not a merger of semantic ownership.

---

## 16. Eligibility inputs

Surface contribution eligibility may depend on already-authoritative facts such as:

```text
active capability set
resolved merchant configuration
outstanding residual obligations
actor authority
customer relationship/context
public exposure policy
projection availability
fulfilment/provider health
```

Eligibility conditions are platform-owned.

Merchants shall not author arbitrary surface-eligibility scripts.

---

## 17. Active capability contribution

Normally, contributions associated with an active capability become eligible only when their registered applicability conditions are met.

Example:

```text
Booking active
    +
merchant actor has booking-read authority
        ↓
Bookings workspace eligible
```

But active capability alone is insufficient to prove that every registered contribution should appear.

---

## 18. Residual obligations after capability deactivation

MS-PROT-040 establishes that deactivating a capability does not abandon existing commitments.

Therefore contribution resolution must distinguish:

```text
active-for-new-activity
        ≠
required-for-residual-management
```

Example:

```text
Booking disabled for new activity
        +
7 future bookings remain
        ↓
Bookings management workspace remains eligible
        ↓
Create-new-booking action becomes ineligible
```

Once no residual obligation requires management, the residual workspace may disappear according to the accepted lifecycle/projection rules.

Hard rule:

> **Surface resolution must preserve the minimum operating surface required to discharge outstanding obligations.**

---

## 19. Provider degradation does not redefine surfaces

MS-PROT-048 separates provider health from capability activation.

Example:

```text
Scheduling active
Calendly fulfilment bound
Calendly connection unhealthy
```

Required surface behaviour:

```text
Scheduling/Calendar semantics remain visible
        +
provider-dependent action may be unavailable/degraded
        +
attention contribution may explain required intervention
```

Rejected:

```text
provider disconnected
        ↓
Calendar disappears
        ↓
Scheduling appears deactivated
```

Hard rule:

> **Fulfilment health may affect action availability and attention state; it shall not silently redefine capability/surface meaning.**

Provider names should appear only where operationally useful, such as an integration-management or repair context.

---

## 20. Surface visibility is not operation authority

A merchant actor may be allowed to view a workspace but not perform every action inside it.

Example:

```text
Front-desk staff
    can view Bookings
    can confirm Bookings
    cannot change payment settings
```

Therefore:

```text
workspace visibility
        ≠
action authority
```

Frontend filtering improves usability but is never the enforcement boundary.

---

## 21. Business-facing terminology

Contribution definitions may identify semantic/business concepts, but merchant-facing labels remain presentation concerns.

The merchant should see terms such as:

```text
Bookings
Appointments
Orders
Products
Calendar
Enquiries
```

rather than:

```text
Capability graph
Operational Object
Relationship effect
Fulfilment role
Requirement closure
```

Presentation may use registered terminology variants where appropriate without creating business-category semantics.

---

## 22. Business category remains non-authoritative

Rejected:

```text
if merchant.category == SALON
    use SalonDashboard
```

Accepted:

```text
resolved semantics
    +
applicable surface contributions
    +
registered presentation profile
        ↓
composed merchant surface
```

Business category may help onboarding, copy/default ranking or presentation recommendations, but it cannot create surface authority or bespoke runtime code.

---

## 23. Merchant evolution

When a merchant activates new capabilities, the platform resolves additional contributions from the new executable configuration.

Example:

```text
Initial:
Publication + Enquiry

Later:
+ Consultation
+ Scheduling
+ Booking
```

The dashboard/storefront recomposes from contributions.

The merchant is not migrated into another product or category template.

---

## 24. Composition precedence and conflict

Surface composition must not silently choose between semantically conflicting operations.

If two contributions target one composition surface, the composition layer may aggregate them only when their semantic contracts remain independently valid.

Presentation deduplication may combine:

```text
navigation destination
workspace container
attention summary
```

but must not merge:

```text
operation authority
state ownership
configuration ownership
provider authority
```

If contributions cannot be composed without semantic ambiguity, the ambiguity must be resolved in design rather than hidden in UI code.

---

## 25. Projection requirements

A contribution may identify the projections/read information it requires.

The contribution does not own those projections unless the owning capability already does so under accepted semantics.

Stale or cached projections remain non-authoritative according to MS-PROT-027.

Example:

```text
Inventory attention contribution
        ↓
low-stock projection
```

The resulting UI warning does not itself authorise inventory mutation.

---

## 26. No arbitrary frontend behaviour

Surface contribution definitions may not contain merchant-authored or AI-authored executable code, arbitrary scripts or freeform conditional behaviour.

AI may recommend registered presentation/composition options.

AI may not create new executable surface semantics.

This preserves the wider rule:

> **Natural language may propose configuration/presentation. Only registered Main Street semantics become executable.**

---

## 27. Cross-domain falsification

### Scholarship-information publisher

Expected composition:

```text
Publication workspace
Opportunity/content public interaction
Enquiry
Subscription/notification where active
```

No Inventory, Payment, Booking or Scheduling surface appears without corresponding semantics.

**PASS**

### Online consultant

Expected composition:

```text
Services/content
Calendar/Scheduling
Bookings/Appointments
Enquiries
optional Payment
```

External scheduling provider identity remains implementation/integration detail.

**PASS**

### Online consultant with degraded external scheduler

Expected:

```text
Calendar/Bookings remain
provider-dependent actions may degrade
attention signal requests reconnection/reconciliation
```

Capability semantics do not disappear.

**PASS**

### Retailer

Expected:

```text
Products
Orders
Inventory
Customers where active
optional announcements
```

No scheduling surface required.

**PASS**

### Motel

Booking, allocation/resource and customer contributions compose without `MotelDashboard`.

Multiple capability contributions may aggregate into coherent booking/resource workspaces.

**PASS**

### Hybrid product/service merchant

Commerce, Inventory, Scheduling and Booking contributions compose without a hybrid-specific application/template.

**PASS**

### Restricted staff actor

Same merchant configuration produces narrower eligible actions/workspaces according to authority, without creating a staff-specific semantic application.

**PASS**

### Booking deactivated with outstanding commitments

Residual booking management contribution remains; new-booking action disappears.

**PASS**

### Booking deactivated with no residual obligations

Booking operating surface may disappear while history remains available through appropriate historical/audit projections.

**PASS**

### Headless capability

Capability may remain active while contributing only contextual actions/attention/configuration or no dedicated workspace.

No useless navigation entry is required.

**PASS**

---

## 28. Rejected assumptions

| Rejected assumption | Why it fails |
|---|---|
| Every capability needs a screen | Creates irrelevant navigation and platform breadth |
| Every capability needs a menu item | Same problem; infrastructure/support capabilities may be headless |
| Capability owns dashboard layout | Mixes semantics and presentation |
| Dashboard composition should be redesigned here | Already governed by MS-PROT-037 |
| Storefront composition should be redesigned here | Already governed by MS-PROT-036 |
| Projection semantics should be redesigned here | Already governed by MS-PROT-027 |
| One capability = one workspace | Several capabilities may compose into one business-facing workspace |
| Provider failure should hide provider-backed surfaces | Confuses fulfilment health with semantic activation |
| Deactivated capability should immediately disappear | Outstanding obligations may still require management |
| Menu visibility grants authority | Backend authority remains authoritative |
| Business category should choose navigation/dashboard | Recreates niche-specific applications |
| UI component type belongs in semantic registry | Couples domain semantics to frontend technology |

---

## 29. Accepted invariants

1. Surface contributions are declarative composition inputs, not UI applications.
2. MS-PROT-027/036/037 remain the authorities for projection, storefront composition and merchant-dashboard composition.
3. Contribution identity is capability-scoped.
4. Initial audiences are MERCHANT, CUSTOMER and PUBLIC; staff remain merchant actors filtered by authority.
5. Initial contribution kinds are WORKSPACE, ACTION, ATTENTION, CONFIGURATION and PUBLIC_INTERACTION.
6. Capability activation does not imply a screen or navigation item.
7. A capability may be headless or contribute only contextual UI.
8. Multiple capabilities may contribute to one registered composition target.
9. Composition may deduplicate presentation but must not merge semantic ownership or authority.
10. Surface visibility and operation authority are distinct.
11. Residual obligations may keep a minimum management surface after capability deactivation.
12. Provider degradation may alter action availability/attention but does not silently remove semantic surfaces.
13. Business category does not create dashboard/storefront branches.
14. Frontend components, routes and layout do not belong in semantic capability definitions.
15. AI/merchant input cannot author executable surface semantics.
16. Stale projections do not authorise mutation.
17. Composition ambiguity must be resolved explicitly rather than hidden in UI code.

---

## 30. Implementation guidance

No generic surface-contribution Java framework is required merely because this contract now exists.

The implementation should begin only when a concrete merchant/storefront/dashboard vertical slice requires contributions to be resolved.

At that point follow the established workflow:

```text
inspect accepted design
        ↓
inspect current tests/code
        ↓
identify smallest required contribution model
        ↓
RED tests
        ↓
implementation
        ↓
full regression
        ↓
conformance review
```

Do not pre-build a universal UI metadata engine without evidence from real surfaces.

---

## 31. Continuous improvement checkpoint

Question:

> **What could we have done better?**

Two material improvements were identified before acceptance:

1. The originally proposed broad `Merchant Surface Projection Contract` was narrowed because MS-PROT-027, 036 and 037 already own projection/storefront/dashboard composition. The missing abstraction is specifically capability-to-surface contribution.
2. Capability activation was explicitly separated from screen/navigation creation. This prevents semantic breadth from becoming merchant-facing UI breadth.

A third ambiguity was resolved during formalisation:

3. Multiple capabilities may contribute to one registered composition target. Without this rule, Booking/Scheduling/Appointment-style capabilities could mechanically create duplicate business-facing workspaces.

No further material improvement was found that justifies delaying this contract.

---

## Governance verdict

**ACCEPTED.** Cross-domain falsification supports a narrow capability surface-contribution contract. It preserves existing projection/composition authorities, supports residual obligations and degraded providers, prevents one-capability-one-screen proliferation and avoids business-category dashboard/storefront templates.
