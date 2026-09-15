# Main Street Product Workflow Tree

> **Status:** Non-authoritative product-scope navigation.  
> **Semantic/design authority:** `designs/AUTHORITY-INDEX.md` and its accepted authorities.  
> **Implementation status:** `docs/development/implementation-status.md` and `docs/development/design-implementation-conformance.md`.

This tree is a product-workflow inventory. A node being listed here does **not** mean that its semantics are accepted, that it is implemented, or that this file may override an accepted MS-PROT/TAS authority. New product work may extend this inventory after the applicable governed design work; the former rule that “no feature should exist outside this hierarchy” is retired.

```text
Main Street
│
├── 01. Merchant Onboarding
│   ├── Account / Merchant Account establishment
│   ├── Identity and verification
│   ├── Business profile / locations
│   ├── capability/configuration inference and merchant approval
│   ├── external integrations where selected
│   ├── storefront generation
│   └── first activation / go-live
│
├── 02. Merchant Operations
│   ├── Orders / physical fulfilment
│   ├── Scheduling / Booking / Appointment
│   ├── Inventory / allocation
│   ├── Payments
│   ├── CustomerContext / enquiries
│   └── operational notifications
│
├── 03. Merchant Administration
│   ├── Merchant Profile
│   ├── catalogue / offerings / products
│   ├── inventory
│   ├── workforce / delegated authority
│   ├── Business Hours
│   ├── publication / storefront / branding
│   ├── commercial agreement / subscription
│   ├── integrations / credentials
│   └── governed configuration changes
│
├── 04. Merchant Growth
│   ├── analytics / projections
│   ├── reports
│   ├── SEO/publication support
│   └── bounded AI assistance
│
├── 05. Storefront
│   ├── public merchant information
│   ├── products / offerings
│   ├── booking
│   ├── contact / enquiry
│   ├── announcements / publication
│   └── checkout / customer tracking where enabled
│
├── 06. Customer Journey
│   ├── discover / visit
│   ├── browse
│   ├── contact / enquire
│   ├── order / book
│   ├── payment
│   ├── notification / follow-up
│   └── optional continuing customer relationship
│
├── 07. Workforce
│   ├── invitation / membership
│   ├── authentication and staff operational-device context
│   ├── roles / groups / privileges
│   ├── attributed operations
│   └── revocation / removal
│
├── 08. AI Workflows
│   ├── merchant onboarding/configuration inference
│   ├── content assistance
│   ├── merchant/customer communication assistance
│   ├── analytics explanation
│   └── specialist bounded automation where separately authorised
│
├── 09. Notifications
│   ├── Notification Intent
│   ├── recipient/exposure/preference resolution
│   ├── channel selection
│   ├── Dispatch
│   └── Delivery Attempt / Delivery Evidence
│
├── 10. Platform Services
│   ├── authentication / session establishment
│   ├── runtime authorisation / entitlement / protection decisions
│   ├── semantic registry / configuration / compilation
│   ├── persistence / background work
│   ├── media / credentials
│   ├── provider integrations
│   ├── audit / privacy
│   └── observability / resilience
│
└── 11. System Administration
    ├── monitoring / diagnostics
    ├── support and registered administrative action
    ├── incident/recovery operations
    ├── audit review
    └── platform maintenance
```

Detailed workflow behaviour must be derived from the applicable accepted authority, not inferred from this navigation tree.
