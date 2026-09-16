# MS-PROT-029 — Application Backend & Delivery Surface Architecture

**Document ID:** MS-PROT-029  
**Version:** 1.2  
**Status:** **ACCEPTED after product approval, architecture review and falsification**  
**Approved:** 4 September 2026  
**Depends on:** MS-PROT-020 → MS-PROT-028  
**Related authorities:** MS-PROT-027, MS-PROT-035, MS-PROT-036, MS-PROT-037, MS-PROT-038, MS-PROT-049, MS-PROT-057, MS-PROT-059, MS-PROT-062, MS-PROT-072  
**Purpose:** Define how Main Street serves merchant operations, customer interactions, public merchant websites and other delivery surfaces without duplicating business logic, and make explicit that the authoritative operational core—not any website or AI interface—is the durable product/runtime authority.

---

## 1. Governing principle

> **Main Street is one capability-driven operational platform exposed through multiple delivery surfaces. The merchant operational model and capability-owned runtime are authoritative; websites, dashboards, customer experiences, POS/staff interfaces, integrations and AI interaction are replaceable surfaces or adapters over that core.**

```text
                    MERCHANT BUSINESS INTENT
                             │
                             ▼
             APPROVED MERCHANT CONFIGURATION
                             +
             CAPABILITY-OWNED AUTHORITATIVE STATE
                             │
                             ▼
                 MAIN STREET APPLICATION CORE
                       Modular Monolith
                             │
          ┌──────────────────┼──────────────────┐
          ▼                  ▼                  ▼
     Merchant API       Customer/Public      Integration API
                              APIs
          │                  │                  │
          ▼                  ▼                  ▼
 merchant-web        storefront-web       provider adapters
 staff/POS            customer flows
          │                  │
          └──────────┬───────┘
                     ▼
              AI-assisted interaction
              through same contracts
```

Separate surfaces are security, presentation and interaction boundaries. They are not separate business engines.

---

## 2. Product/runtime boundary

The following distinction is normative:

```text
AUTHORITATIVE CORE
    merchant configuration
    capability-owned business state
    application/domain operations
    authorisation/eligibility
    deterministic validation
    transaction/consistency boundaries
    durable operational evidence

DELIVERY / INTERACTION SURFACES
    merchant dashboard
    public website/storefront
    customer interaction UI
    staff/POS UI
    API/integration representations
    AI assistant experience
```

A delivery surface MAY display authorised projections and collect intent/commands.

A delivery surface MUST NOT silently become:

- semantic authority;
- a second aggregate/domain implementation;
- authoritative business state;
- transaction authority;
- merchant-intent authority; or
- a bespoke per-merchant business runtime.

This boundary remains true even if AI can generate a complete-looking frontend or application scaffold.

---

## 3. Shared business authority

Merchant, customer, public, staff/POS, integration and AI-assisted surfaces MUST reuse the same capability-owned application/domain semantics.

Invalid:

```text
Merchant booking logic
Customer booking logic
Website booking logic
AI booking logic
```

Required:

```text
Booking-owned application/domain authority
        ↑          ↑          ↑          ↑
 Merchant UI   Customer UI  Website   AI assistance
```

Transport, presentation, context establishment and response composition MAY differ. Business invariants do not.

---

## 4. Merchant operational model

The phrase `merchant operational model` describes the coherent, scope-qualified composition of current supported merchant configuration and capability-owned authoritative state.

It is **not** a new universal aggregate or a transfer of ownership into one central `MerchantOperationalModel` object.

Conceptually:

```text
Merchant Profile authority
Configuration authority
Booking/Scheduling authority
Ordering/Payment authority
Inventory authority
Workforce/Access authority
Publication authority
other capability owners
        ↓
coherent merchant operating context
```

Each owner retains its accepted bounded responsibility.

Read-model composition MAY provide convenient merchant/customer views but MUST remain derived unless another accepted authority explicitly establishes otherwise.

---

## 5. Merchant surface

The merchant surface supports authorised work such as:

```text
manage offerings/content
manage bookings/orders/enquiries
manage resources/inventory/staff
manage schedules
view operational projections
configure supported policies/capabilities
manage public presence/storefront information
```

The merchant dashboard is an operational surface over the core. It does not own the semantics it presents.

Only operations relevant to the merchant's active configuration and actor privileges should be exposed.

---

## 6. Staff / POS surface

Staff or POS experiences MAY expose a narrower operational view appropriate to delegated authority and device context.

They MUST invoke the same application authority as equivalent merchant/customer operations where semantics overlap.

A POS frontend MUST NOT become the canonical owner of Order, Payment, Inventory, Appointment or workforce state merely because it is the interaction device used at the counter.

---

## 7. Customer surface

The customer surface supports contextual interactions such as:

```text
enquiry
booking/viewing request
order initiation
secure transaction tracking
customer portal/history where enabled
cancellation/rescheduling where permitted
```

A persistent customer account remains optional where the owning semantics permit guest/contextual interaction.

Customer-visible opportunity does not itself authorise a concurrency-sensitive commitment; the owning operation revalidates authoritative state.

---

## 8. Public / website surface

The public surface serves explicitly exposed merchant information and interaction entry points.

Potential content includes:

```text
merchant information
offerings/services/products
publications/opportunities
categories/classification
portfolio/showcase
opening hours/location where relevant
search/discovery
external application/action links
contact/enquiry
subscription
booking/order/viewing initiation
```

The public website is a generated projection-and-interaction surface over these contracts.

It is **not** the product's source of truth and MUST remain replaceable without changing the meaning of merchant operations.

Physical location remains optional; online consultants and information publishers are valid public-surface users where supported by product policy and semantics.

---

## 9. AI-assisted surface

AI-assisted interaction is another application surface/orchestration shell.

```text
merchant natural language
        ↓
AI interpretation / clarification / proposal
        ↓
same owner-qualified application contract
        ↓
authorisation
        ↓
deterministic validation
        ↓
capability-owned execution
```

AI MUST NOT be given a privileged semantic or mutation path merely because it can interpret natural language.

AI-generated application code per merchant is not an alternative architecture for Main Street's operational core.

MS-PROT-057 remains authoritative for the detailed AI inference/intent boundary.

---

## 10. Information-publisher validation

An information publisher may use:

```text
Public API / website
   ├── opportunity/content list
   ├── category/search projection
   ├── content detail
   ├── external application link
   ├── enquiry/contact
   └── subscription entry point
```

No commerce endpoint set is required unless corresponding capabilities are active.

**PASS**

---

## 11. Online-consultant validation

An online consultant may use:

```text
Public/Customer surface
   ├── services
   ├── consultation details
   ├── enquiry
   ├── booking entry point
   └── optional payment
```

Scheduling may be implemented by Main Street or an external provider integration without changing the business-facing semantic contract.

**PASS**

---

## 12. Security boundaries

Public, customer, staff/POS, merchant, AI-assisted and integration surfaces have different trust assumptions.

- Public requests receive only explicitly permitted projections/interactions.
- Customer operations require the applicable contextual identity/relationship/authority.
- Staff operations require delegated workforce/session/device authority where applicable.
- Merchant operations require authenticated merchant scope and actor authority.
- AI interaction inherits the authority of the established actor/context; AI presence never upgrades authority.
- External provider traffic uses a dedicated authenticated integration boundary.
- Internal identifiers do not confer access merely because a caller supplies them.

Tenant resolution/isolation remains governed by MS-PROT-031 and runtime access composition by the applicable later authorities.

---

## 13. Performance, caching and projection

Public/read-heavy surfaces MAY use caches, search indexes or generated projections more aggressively than command surfaces.

Such representations remain non-authoritative unless an accepted authority explicitly establishes otherwise.

Concurrency-sensitive writes revalidate against the owning authoritative state.

A cache, static site generator, browser state, LLM context window or search index MUST NOT become transaction authority.

---

## 14. Backend composition

The modular backend SHOULD organise business logic by semantic ownership/bounded context, not by HTTP/UI surface.

```text
transport / surface adapters
        ↓
application / use-case layer
        ↓
capability/domain modules
        ↓
persistence / integration boundaries
```

Surface-specific code should primarily handle context establishment, authentication, transport mapping, presentation composition and surface-specific non-authoritative state.

---

## 15. Surface replacement invariant

Main Street MUST be able to change a rendering framework, add a new customer channel, introduce voice/AI interaction or replace the storefront implementation without reclassifying the merchant or creating a new business model.

The dependency direction is:

```text
business authority
      ↓
application/projection/exposure contracts
      ↓
delivery surface
```

Never:

```text
website / AI / POS implementation
      ↓
defines business authority
```

---

## 16. Degradation boundary

A delivery-surface failure MUST NOT redefine authoritative business state.

For example, a storefront/CDN failure does not cancel bookings, mutate orders or disable the merchant dashboard when the owning backend operations remain serviceable.

How an unavailable surface degrades or communicates status remains governed by the applicable resilience/surface authorities.

---

## 17. AI-era resilience test

A visible feature MAY be useful even if general AI can generate it easily.

However, architecture MUST NOT make generated presentation/functionality the durable authority.

The system remains correctly structured when replacing the generated interface leaves intact:

```text
merchant configuration
authoritative state
business invariants
permissions
operational history
provider bindings
transaction/evidence semantics
```

This is the architectural form of the product's AI-substitution test.

---

## 18. Falsification findings

| Tested assumption | Result |
|---|---|
| Website generation can remain the product centre because Main Street also adds bookings/commerce | **FAIL** — generation is increasingly commoditised and surface-centred architecture would duplicate authority |
| One static Business Profile can be the universal source of business truth | **FAIL** — dynamic capability-owned commitments/state disprove universal profile ownership |
| AI can safely generate a bespoke application/runtime per merchant | **FAIL** — creates semantic, security, upgrade and invariant drift |
| Treating the operational core as the product requires one universal central aggregate | **FAIL** — coherent context can compose bounded authoritative owners without collapsing them |
| Separate surfaces require separate domain logic | **FAIL** — duplicates invariants and creates drift |
| AI needs a privileged execution path to be useful | **FAIL** — AI can interpret/prepare intent while reusing existing application contracts |
| Cached public state can authorise commitments | **FAIL** — projections may be stale |
| A website outage should imply the business is unavailable | **FAIL** — unrelated merchant operations may remain healthy |
| Business category should select backend modules | **FAIL** — configuration/capability ownership already determines behaviour |

No remaining falsifier requires changing the accepted capability-owned modular-monolith architecture. The amendment strengthens product/runtime boundaries already implicit in the design.

---

## 19. Accepted invariants

1. One capability-driven modular backend remains the initial application architecture.
2. The merchant operational model/runtime is the authoritative product core; this phrase does not create a universal aggregate.
3. Merchant, staff/POS, customer, public, integration and AI-assisted surfaces are distinct application/presentation boundaries.
4. Business logic and invariants remain capability-owned and shared across surfaces.
5. The website/storefront is a replaceable delivery surface, not business authority.
6. The Business Profile is authoritative only for its bounded profile facts, not all business state.
7. AI assistance converges on the same deterministic application authority and does not gain privileged semantics or mutation rights.
8. Main Street does not generate independent executable business runtimes per merchant.
9. Public/customer/merchant/staff/integration trust assumptions remain distinct.
10. Customer accounts remain optional where supported.
11. Public surfaces support transactional and non-transactional merchants.
12. Physical premises are not required merely to use a public storefront.
13. Caches, projections, generated pages and AI context do not silently become authoritative write state.
14. Business category does not drive generic backend branching.
15. Surface replacement or degradation does not redefine merchant business semantics/state.

---

## 20. Compatibility / implementation impact

This revision does **not** require a new backend topology or transfer semantic ownership.

It is intentionally compatible with the accepted architecture in which:

- capabilities own business semantics;
- projections/exposure govern read presentation;
- application contracts own executable entry points;
- storefront composition derives from configuration/projections;
- AI is untrusted/advisory until validated; and
- cross-capability orchestration remains explicit.

Implementation review should nevertheless reject future work that treats storefront, dashboard or AI code as a shortcut around those boundaries.

---

## 21. Deferred decisions

Exact endpoint paths, gateway/CDN topology, frontend framework evolution, rate limiting, BFF choices, external scheduler adapters, API versioning and future delivery channels remain downstream decisions unless promoted separately.

---

## Governance verdict

```text
PRODUCT DECISION APPROVED        ✓
ARCHITECTURE TRACE REVIEW        ✓
FALSIFICATION                    ✓
OWNERSHIP COLLAPSE REJECTED      ✓
EXISTING RUNTIME COMPATIBILITY   ✓
ACCEPT                           ✓
```

## **Status: ACCEPTED**

### Canonical decision

> **Main Street's authoritative value sits in its capability-driven merchant operational model and governed runtime. Delivery surfaces—including the website and AI assistant—present and invoke that authority but do not replace it.**
