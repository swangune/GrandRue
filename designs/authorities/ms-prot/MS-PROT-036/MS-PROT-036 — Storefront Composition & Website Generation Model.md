# MS-PROT-036 — Storefront Composition & Website Generation Model

**Document ID:** MS-PROT-036  
**Version:** 1.2  
**Status:** **ACCEPTED after product approval, design review and falsification**  
**Approved:** 4 September 2026  
**Depends on:** MS-PROT-027, MS-PROT-029, MS-PROT-030, MS-PROT-035  
**Related authorities:** MS-PROT-049, MS-PROT-051, MS-PROT-057, MS-PROT-066  
**Purpose:** Define how Main Street composes merchant public websites and customer-facing interaction flows from authoritative configuration, projections and bounded presentation while making explicit that the storefront is a replaceable delivery surface rather than the merchant business model or product core.

> **Current-authority navigation notice:** This accepted document remains part of the current composite MS-PROT-036 authority. Within storefront presentation/composition, business-data non-ownership and centrally evolvable delivery scope, read it together with **MS-PROT-094 v1.0**. MS-PROT-094 supersedes the `Presentation Profile`/template/theme/named-style/domain-layout selection mechanism within that scope. Historical wording below is retained as design provenance and MUST NOT be implemented where MS-PROT-094 governs.

---

## 1. Governing principle

> **A merchant storefront is a projection-and-interaction surface over the merchant's supported operational model. It is composed from registered capabilities, authorised projections, merchant content and bounded presentation configuration; it is not a custom business application generated per merchant.**

```text
Approved Merchant Configuration
        +
Capability-Owned Authoritative State
        ↓
Authorised Public/Customer Projections
        +
Capability Surface Contributions
        +
Merchant Content / Media
        +
Presentation Profile
        ↓
Storefront Composition
        ↓
Shared storefront-web runtime
```

The storefront reflects business truth. It does not create business truth.

---

## 2. Merchant operational context

For storefront purposes, `merchant operational context` means the coherent, scope-qualified inputs made available from existing authoritative owners.

It does **not** introduce a universal `MerchantOperationalModel` aggregate or repository.

Relevant owners may include:

```text
Merchant Profile / Presence
Merchant Configuration
Offering / Product / Service owners
Publication
Scheduling / Booking
Ordering / Payment
Inventory / Availability
Public Interaction / Exposure
other capability owners
```

Each owner retains its accepted semantic authority.

The storefront consumes the appropriate projection, exposure or application contract rather than reaching around those owners to construct its own business interpretation.

---

## 3. Storefront composition inputs

The storefront MAY derive from:

```text
active merchant configuration
registered capability contributions
public/customer Exposure decisions
authorised projections
merchant offerings/content/media
interaction entry points
merchant brand/presentation preferences
registered presentation components/profiles
```

The Business Profile is therefore one bounded input among several. It is not a universal website or business-state record.

The storefront MUST NOT infer new runtime semantics from business category, page structure, AI output or visual composition.

---

## 4. No niche templates as architecture

Rejected:

```text
businessCategory = GARDENER → GardenerApplication
businessCategory = MOTEL    → MotelApplication
businessCategory = SALON    → SalonApplication
```

Required direction:

```text
active semantic configuration
        ↓
applicable public/customer contributions
        ↓
authorised projections/interactions
        ↓
registered components/layout profile
```

Business category MAY assist onboarding language, classification, default ranking or presentation recommendation. It MUST NOT determine backend behaviour or create a bespoke application branch.

---

## 5. Presentation profile

A presentation profile is a bounded registered arrangement of reusable components/navigation patterns.

It MAY influence:

```text
section order
navigation emphasis
content density
hero/summary treatment
listing/card style
interaction prominence
```

It MUST NOT introduce unsupported business operations or alter the meaning of capability-owned state.

AI MAY recommend a registered profile or bounded presentation variation from authorised merchant context. It MUST NOT generate arbitrary executable frontend behaviour, scripts or domain rules.

---

## 6. Merchant content and controlled customisation

Merchants MAY supply or approve content such as:

```text
logo
brand colour
cover imagery
approved font choice
light/dark preference
names/descriptions
prices where the owning capability permits merchant mutation
media
public copy
```

Presentation customisation MUST NOT expose semantic/runtime internals or permit merchant-authored executable code to become part of the business runtime.

Where a value is capability-owned rather than presentation-owned, storefront tooling MUST use the owning application contract rather than persist a duplicate website-only fact.

---

## 7. Capability-derived routes and surfaces

Routes, sections and interaction entry points SHOULD appear only when the active merchant configuration and authorised public/customer contribution require them.

Examples:

```text
Catalogue / Product      → products surface
Service                  → services surface
Booking                  → booking interaction
Ordering                 → ordering interaction
Enquiry                  → contact/enquiry interaction
Portfolio / Showcase     → portfolio/work surface
Publication              → opportunities/posts/resources
Classification / Search  → category/search surface
Subscription             → subscription interaction
```

Exact route names are implementation details.

The semantic rule is:

> **Surface structure follows applicable registered merchant semantics and exposure; surface structure does not define those semantics.**

---

## 8. Information-publisher storefront

An information publisher MAY require no transactional commerce surface.

Example composition:

```text
/
/opportunities
/opportunities/{slug}
/categories/{category}
/search
/contact
/subscribe
```

No Booking, Payment, Inventory, Allocation or Ordering UI appears unless corresponding semantics are active and exposed.

**PASS**

---

## 9. Online-consultant storefront

Example composition:

```text
/
/services
/consult
/book
/contact
```

Possible supported semantics may include Consultation/Service, Scheduling, Booking, Enquiry and optional Payment.

Scheduling may be fulfilled by Main Street or an external provider behind the accepted provider boundary. Provider choice does not become the storefront's business-model abstraction.

**PASS**

---

## 10. Physical merchant storefront

A mechanic, grocery, salon or accommodation merchant MAY additionally expose location, opening-hour, collection, delivery or other locally relevant information where applicable and authorised.

Physical location remains optional context. Storefront generation does not require a verified physical premises merely to publish a site.

External local-discovery verification remains an external/provider concern where used.

---

## 11. Customer interactions

Interactive components MUST invoke supported customer/public application use cases rather than encode domain logic in frontend components.

```text
UI component
    ↓
Public / Customer application contract
    ↓
Authorisation / eligibility / validation
    ↓
Capability-owned execution
    ↓
Authoritative state
```

A button such as `Book`, `Enquire`, `Order`, `Apply externally` or `Subscribe` appears only where its corresponding semantic interaction is valid and exposed.

AI-generated frontend code does not waive this rule.

---

## 12. Dynamic operational information

Operationally sensitive values such as current availability, stock indication, price, booking opportunity or order state MUST be obtained through the applicable accepted read/projection contracts.

A displayed value is not automatically authority to commit against that value.

Concurrency-sensitive operations MUST revalidate authoritative state through the owning application/domain operation.

---

## 13. Projection and caching

Storefront content MAY use cached/generated projections for performance and search optimisation where safe.

Cached availability, catalogue, search or public material remains a read representation and cannot silently become authoritative business state.

Static-site generation, CDN materialisation, browser state or an LLM-generated page MUST NOT become transaction authority.

---

## 14. Website completeness boundary

A merchant storefront is contextually useful when it contains sufficient truthful information and valid interactions for the merchant's intended public model.

It is NOT defined by completion of every possible Main Street field or page.

Examples:

- a consultant does not require Products;
- an information publisher does not require Booking;
- a plumber does not require a Gallery;
- an enquiry-only organisation does not require Payment.

Missing optional website content MUST NOT block unrelated merchant operations.

A genuine capability-owned requirement MAY still block the specific operation it governs.

> **Website completeness is not business-operability completeness.**

---

## 15. AI website assistance

AI MAY:

```text
draft or improve public copy
identify contextual content gaps
recommend registered presentation options
enhance media where supported
explain what a surface requires
interpret a merchant request about website content
```

AI MUST NOT:

```text
invent merchant facts
activate a capability merely to improve the website
invent semantic relationships
generate arbitrary executable business logic
bypass merchant-intent confirmation where material meaning is inferred
bypass capability-owned application contracts
```

If website assistance discovers probable new operating intent, it MUST hand that intent to the appropriate reconfiguration/capability process rather than mutate business semantics itself.

MS-PROT-057 remains authoritative for the detailed AI-assistance boundary.

---

## 16. White-label / merchant-facing boundary

The customer experience remains merchant-facing. Main Street is infrastructure rather than marketplace identity.

A minimal `Powered by Main Street` attribution MAY be used according to product/commercial policy, but storefront composition MUST NOT require customers to understand Main Street's internal semantic architecture.

---

## 17. Delivery-surface independence

The website renderer is replaceable infrastructure.

Main Street MUST be able to replace frontend technology, add a native app, introduce a voice/AI interaction surface or expose another authorised customer channel without changing merchant business semantics.

Required dependency direction:

```text
capability-owned authority
        ↓
application / projection / Exposure contracts
        ↓
storefront composition
        ↓
renderer / delivery technology
```

Invalid dependency direction:

```text
website template / generated application
        ↓
defines merchant operations
```

---

## 18. Surface degradation

A storefront rendering, frontend-hosting or CDN failure MUST NOT redefine merchant authoritative state.

Where the operational backend and merchant-facing surfaces remain serviceable, unrelated merchant operations may continue even if the public website is degraded.

Customer-facing degradation behaviour remains subject to the applicable resilience and exposure authorities.

---

## 19. AI-era substitution test

A storefront feature MAY remain useful even when general AI can generate a visually equivalent implementation.

Its Main Street-specific value should come from its connection to:

```text
current merchant configuration
authorised projections
capability-owned state
valid interactions
permissions / Exposure
operational continuity
```

Therefore, visual/code generation may accelerate delivery but MUST NOT become the architectural moat or business authority.

---

## 20. Falsification findings

| Tested assumption | Result |
|---|---|
| Website generation should remain the product centre because merchants still need a site | **FAIL** — interface generation is increasingly commoditised and does not own business operation |
| The Business Profile can remain the complete source of website/business truth | **FAIL** — dynamic capability-owned state and interactions disprove universal profile ownership |
| AI-generated bespoke applications can replace storefront composition | **FAIL** — would create semantic, security, upgrade and invariant drift |
| Calling the operational model the product requires one central merchant aggregate | **FAIL** — storefront can compose bounded projections without ownership collapse |
| One fixed storefront fits all merchants | **FAIL** — capabilities/interactions differ materially |
| Each niche needs a bespoke template/application | **FAIL** — creates vertical-specific code and drift |
| Every merchant needs commerce routes | **FAIL** — publishers/enquiry-only organisations disprove it |
| Storefront requires physical premises/location | **FAIL** — online consultants/publishers disprove it |
| AI may generate arbitrary frontend logic because backend remains authoritative | **FAIL** — executable frontend behaviour can still bypass or duplicate authority/security |
| Frontend components may own business rules for convenience | **FAIL** — duplicates backend authority |
| Website incompleteness implies merchant-operability incompleteness | **FAIL** — unrelated capabilities remain valid |

No falsifier requires bespoke merchant applications or transfer of semantic ownership to storefront composition.

---

## 21. Accepted invariants

1. One shared storefront application/runtime serves merchants through composition rather than bespoke per-merchant application generation.
2. The storefront is a replaceable delivery surface over the merchant operational context, not the product's authoritative core.
3. `merchant operational context` does not create a universal aggregate; existing capability owners retain authority.
4. Storefront composition derives from active configuration, authorised projections/Exposure, capability contributions, merchant content and bounded presentation profiles.
5. The Business Profile is one bounded input and is not universal business-state authority.
6. Business category does not create niche runtime/frontend branches.
7. Unsupported capabilities and interactions do not appear merely because AI or a template can generate them.
8. Information publishing and enquiry-only models remain first-class compositions.
9. Online and physical merchants use the same composition machinery.
10. Physical location remains optional context.
11. AI may assist content and recommend registered presentation but cannot invent executable business behaviour or semantic relationships.
12. Frontend interactions delegate to shared capability-owned backend use cases/invariants.
13. Cached/read/generated projections do not become authoritative business state.
14. Website completeness does not gate unrelated business operations.
15. Delivery-surface replacement or degradation does not redefine merchant semantics or authoritative state.

---

## 22. Compatibility / implementation impact

This revision strengthens boundaries already present in MS-PROT-036 v1.1 and MS-PROT-029.

It does NOT require:

- a new storefront architecture;
- a new universal merchant aggregate;
- a new semantic owner for websites;
- per-merchant generated codebases; or
- transfer of authority from capability owners into presentation.

Existing implementation work remains conformant where storefront/public UI consumes accepted projections/exposure and application contracts.

Future website work MUST be reviewed against the surface-authority boundary before introducing local frontend business rules.

---

## 23. Deferred decisions

Exact frontend component library, SEO implementation, theme tokens, route naming, CMS/editor UX, external scheduler integration mode, local-discovery integration and CDN/cache implementation remain downstream decisions unless promoted separately.

---

## Governance verdict

```text
PRODUCT DECISION APPROVED        ✓
DESIGN BOUNDARY REVIEW           ✓
FALSIFICATION                    ✓
UNIVERSAL AGGREGATE REJECTED     ✓
BESPOKE AI APPLICATION REJECTED  ✓
EXISTING ARCHITECTURE COMPATIBLE ✓
ACCEPT                           ✓
```

## **Status: ACCEPTED**

### Canonical decision

> **Main Street's storefront is a generated projection-and-interaction surface over capability-owned merchant operations. AI may improve composition and merchant interaction, but neither the website nor AI-generated code becomes business authority.**
