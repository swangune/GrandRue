# MS-PROT-037 — Merchant Dashboard Composition & Mobile-First Operations Model

**Version:** 1.1  
**Status:** **Accepted**  
**Depends on:** MS-PROT-027, MS-PROT-029 → MS-PROT-036  
**Purpose:** Define how one `merchant-web` application adapts to materially different merchants without producing niche-specific dashboards, exposing irrelevant functions, or sacrificing Main Street's mobile-first operational requirement.

> **Current-authority navigation notice:** This accepted document remains part of the current composite MS-PROT-037 authority. Read it together with **MS-PROT-093 v1.0** for the widened first-party merchant-client portfolio and platform-native delivery model, and with **MS-PROT-094 v1.0** for merchant presentation consistency. Historical `merchant-web`-only/native-app-deferred wording below is retained as design provenance and MUST NOT be implemented where MS-PROT-093 or MS-PROT-094 governs.

## 1. Governing principle

> **The merchant dashboard is an operational projection of the merchant's enabled capabilities, actor privileges, current work and presentation configuration—not a fixed collection of every Main Street feature.**

```text
Merchant Configuration
        +
Enabled Capabilities
        +
Actor Authority
        +
Operational Projections
        ↓
Merchant Dashboard Composition
```

## 2. Platform breadth vs merchant simplicity

Main Street may support a broad capability universe:

```text
Publication
Enquiries
Bookings
Scheduling
Payments
Inventory
Orders
Delivery
Allocation
Messaging
Staff
Notifications
Analytics
Website
...
```

An individual merchant should see only the surfaces needed to operate its active configuration.

Hard UX invariant:

> **A merchant must never be exposed to platform breadth that is irrelevant to its active operating configuration.**

Related constraint:

> **Adding a capability should introduce only the minimum additional dashboard surface required to operate it.**

## 3. Business-facing terminology

Dashboard navigation should use merchant language rather than internal architecture.

Prefer:

```text
Appointments
Orders
Products
Rooms
Enquiries
Posts
Customers
Calendar
```

Avoid exposing:

```text
Capabilities
Semantic Operations
Allocation Models
Requirements
Resources
```

unless a specialised administrative surface genuinely requires those terms.

## 4. Navigation composition

Dashboard navigation is derived from active capabilities and actor authority.

Conceptually:

```text
Capability/Projection Registry
        ↓
Eligible merchant surfaces
        ↓
Actor privilege filter
        ↓
Navigation/workspace composition
```

Business category may help onboarding or naming defaults but must not create niche-specific dashboard code branches.

## 5. Operational home

The home/dashboard summary should prioritise work requiring attention rather than expose every subsystem.

Potential summary inputs:

```text
upcoming appointments/bookings
new enquiries
orders needing action
content deadlines/publication tasks
resource/inventory alerts
customer messages
failed external integrations
```

Only applicable signals appear.

## 6. Mobile-first invariant

The merchant must be able to perform ordinary daily operations from a phone.

Mobile-first means more than responsive layout. Core workflows should avoid requiring:

```text
wide desktop-only tables
hover-dependent controls
multi-window coordination
large local device storage
unnecessary administrative depth
```

High-frequency actions should be reachable with minimal navigation and use touch-appropriate controls.

## 7. Information-publisher dashboard

A publication-first merchant might receive:

```text
Home
Opportunities / Content
Categories
Enquiries
Subscribers / Notifications
Website
Analytics (if enabled)
```

It should not receive:

```text
Inventory
Payments
Appointments
Allocation
Orders
```

unless those semantics are later activated.

**PASS**

## 8. Online-consultant dashboard

An online consultant might receive:

```text
Home
Services
Calendar
Bookings
Enquiries
Payments (if enabled)
Website
Customers/history (if enabled)
```

An external scheduler integration may feed the Calendar/Bookings surface without making the provider itself the dashboard's business abstraction.

**PASS**

## 9. Physical mechanic dashboard

A mechanic might receive:

```text
Home
Services
Appointments
Customers / Vehicles
Payments
Staff (if enabled)
Website
```

Physical location/local-discovery integration may be configured separately and is not a prerequisite for the dashboard model.

**PASS**

## 10. Grocery dashboard

A grocery merchant might receive:

```text
Home
Products
Orders
Inventory
Delivery
Collection
Payments
Website
```

This uses the same dashboard-composition machinery as non-commerce merchants.

**PASS**

## 11. Merchant configuration evolution

A merchant may expand over time without migrating to another Main Street product.

Example:

```text
Initial:
Publication + Enquiry

Later:
Publication + Enquiry + Consultation + Scheduling + Booking + Payment
```

The dashboard recomposes from the new validated configuration; it does not convert the merchant into a different application type.

## 12. Actor-specific dashboard projection

The same merchant configuration may produce different staff/owner views according to authority.

Example:

```text
Owner: settings + payments + staff + operations
Front-desk staff: bookings + enquiries
Content editor: publications + website
```

Authority filtering must not be implemented merely as hidden menu items; backend application authority remains authoritative.

## 13. Presentation and configuration

The dashboard may offer bounded merchant configuration controls for active semantics, but merchants should not be required to understand the internal configuration graph.

Complexity compression is a design objective:

```text
semantic/configuration complexity
        ↓
business-facing controls
```

Adaptive onboarding/configuration may progressively introduce new surfaces as merchant choices activate additional capabilities.

## 14. Performance and offline/device constraints

The merchant web application should remain lightweight enough for ordinary mobile use. Heavy reports or large datasets should use server-side pagination/filtering/projections rather than requiring large client downloads.

No requirement for a native mobile application is established by this document.

## 15. Falsification findings

Rejected assumptions:

| Failed assumption | Why it fails |
|---|---|
| Every merchant gets the same navigation | Platform breadth would overwhelm narrow merchants |
| Each niche needs its own dashboard app | Duplicates code and undermines capability composition |
| Business category should determine menus | Active semantics/authority provide the actual requirement |
| Hiding a menu item is sufficient authorisation | Backend authority must remain authoritative |
| Mobile-first means only responsive CSS | Operational workflows can still be desktop-dependent |
| Every merchant needs commerce surfaces | Publishers/consultants disprove it |
| Adding one capability may expose whole subsystems | Violates simplicity boundary |

## 16. Accepted invariants

1. One `merchant-web` application serves materially different merchants.
2. Dashboard surfaces derive from active configuration, projections and actor authority.
3. Irrelevant platform capabilities remain hidden/uninstantiated for the merchant experience.
4. Merchant terminology remains business-facing.
5. Adding a capability introduces only the minimum required operating surface.
6. Ordinary merchant operations remain mobile-first.
7. Business category does not create niche-specific dashboard code.
8. Authority is enforced server-side/application-side, not by navigation visibility alone.
9. Merchant configuration may evolve and the dashboard recomposes without product migration.
10. Information publishers, online consultants and physical merchants use the same composition mechanism.

## 17. Deferred decisions

Exact navigation hierarchy, component library, dashboard layout, mobile breakpoints, analytics widgets, notification centre UX, native-app decision and detailed merchant settings UX remain downstream implementation decisions.

## Governance verdict

**ACCEPTED.** The 20 August 2026 handover confirms the existing projection-derived dashboard architecture and expands its validation set.
