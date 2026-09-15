# MS-PROT-030 — Backend & Web Technology Architecture

**Version:** 1.0  
**Status:** **Accepted**  
**Depends on:** MS-PROT-029  
**Purpose:** Select the initial backend and web technologies that implement the accepted Main Street architecture while preserving modularity, local development, public-site performance, mobile-first merchant operation, and future extractability.

Current verification: Spring Boot **4.1.0** is stable and supports Java 17–26; Java **25** is the current LTS while Java 26 is the current feature release. Next.js **16.x** is Active LTS, with 16.3 current in August 2026, and React's current documented release line is **19.2**. 

## 1. Proposed stack

```text
MAIN STREET
│
├── Backend
│   ├── Java 25 LTS
│   ├── Spring Boot 4.1
│   └── Maven
│
├── Merchant Web
│   ├── TypeScript
│   ├── React 19
│   └── Next.js 16
│
└── Storefront Web
    ├── TypeScript
    ├── React 19
    └── Next.js 16
         │
         ├── Public merchant website
         ├── Guest/customer interactions
         ├── Customer tracking
         └── Authenticated customer area where enabled
```

The important architectural choice is **two frontend applications, not three**:

```text
merchant-web

storefront-web
    ├── public
    └── customer
```

The backend remains one modular Spring Boot application initially.

---

# 2. Backend — Java + Spring Boot

### Decision

Use:

> **Java 25 LTS + Spring Boot 4.1 + Maven**

Spring Boot already supports executable standalone applications, security, observability, configuration and the broader Spring ecosystem while remaining compatible with the modular-monolith architecture. Spring Boot 4.1 supports Java versions through Java 26. 

Java 25 rather than Java 26 becomes the **production baseline** because Java 25 is LTS. Java 26 can still be used experimentally, but production should not unnecessarily depend on a short-lived feature release. 

---

# 3. Existing Java prototype

The current semantic/runtime code is retained.

It becomes the beginning of the backend's domain/kernel rather than being rewritten in another language.

Conceptually:

```text
Spring Boot Application
        │
        ├── semantic
        ├── runtime
        ├── configuration
        ├── compiler/resolution
        ├── capabilities
        ├── application
        └── delivery
```

Spring must **wrap the domain**, not infect it.

Core semantic classes should not require:

```text
@RestController
@Entity
@Autowired
HTTP concepts
JSON annotations
```

merely to function.

The existing plain-Java direction remains desirable.

---

# 4. Backend module dependency direction

```text
Delivery
   ↓
Application
   ↓
Capabilities / Domain
   ↓
Domain contracts

Infrastructure
   ─────implements────► contracts
```

Not:

```text
Domain
 ↓
Spring Controller
 ↓
JPA
```

Spring Boot is the application/runtime framework.

It is **not Main Street's domain model**.

---

# 5. Backend delivery surfaces

One Spring Boot deployment exposes three logical application boundaries:

```text
Merchant Delivery
Customer Delivery
Public Delivery
```

Conceptually:

```text
Spring Boot
│
├── merchant.delivery
├── customer.delivery
├── public.delivery
│
├── merchant.application
├── customer.application
├── public.application
│
└── capability modules
```

Exact endpoint URLs are deferred.

The separation must exist in code even while sharing one deployment.

---

# 6. Frontend — React + Next.js + TypeScript

Use:

> **Next.js 16 Active LTS + React 19 + TypeScript**

Next.js supports interactive dynamic applications as well as server/static rendering, which fits Main Street's conflicting surface requirements. It is also self-hostable on a normal Node.js server; adopting it does **not** require Vercel. 

TypeScript is selected because the frontend will have substantial contracts with the backend and multiple configured UI surfaces.

---

# 7. Why two frontend applications

## `merchant-web`

Optimised for:

```text
authenticated operation
mobile-first management
high interactivity
calendar/POS/dashboard behaviour
staff authority
frequent mutations
```

## `storefront-web`

Optimised for:

```text
SEO
merchant branding
public pages
catalogue/services
guest checkout/enquiries
customer tracking
optional customer accounts
```

Public and customer functionality remain together because they form one merchant-facing storefront journey:

```text
anonymous visitor
      ↓
merchant website
      ↓
enquiry/order/booking
      ↓
guest tracking
      ↓
optional authenticated relationship
```

Splitting public and customer into independent applications immediately would duplicate routing, branding, merchant resolution and customer journeys.

---

# 8. Multi-merchant storefront

We do **not** build a separate Next.js application for every merchant.

Instead:

```text
storefront-web
      │
      ├── merchant A domain
      ├── merchant B domain
      └── merchant C domain
             ↓
       resolve merchant
             ↓
   load public configuration/projection
             ↓
       render merchant site
```

The same application renders different websites from controlled configuration.

Thus:

```text
one merchant ≠ one codebase
```

This is essential to Main Street's economics and architecture.

---

# 9. Rendering strategy

Do **not** force one rendering mode everywhere.

### Public content

Prefer server/static/cacheable rendering for:

```text
services
portfolio
menu/catalogue
business profile
opening hours
public content
```

Next.js supports static and dynamic server rendering, and cached/static output is suitable for fast public pages and SEO. 

### Customer interactions

Use dynamic rendering/client interactivity where required:

```text
order tracking
booking
checkout
customer account
lesson history
```

### Merchant dashboard

Primarily dynamic interactive UI.

Therefore:

> **Rendering mode follows the projection/use case, not the application globally.**

---

# 10. Next.js is not a second domain backend

This is a critical constraint.

Next.js can technically implement server actions and API routes, but Main Street must not gradually become:

```text
Spring backend
     +
Next.js business backend
```

The authoritative backend remains Spring Boot.

Next.js server-side functionality may handle frontend concerns such as:

```text
SSR
routing
session forwarding
page composition
caching
frontend-specific aggregation
```

but must not own:

```text
booking invariants
payments semantics
inventory allocation
order transitions
merchant configuration semantics
```

Those remain in Java capability/application modules.

---

# 11. Backend-for-frontend rule

A thin frontend-facing adapter is allowed where necessary.

It may:

```text
aggregate read results
translate frontend representation
forward authenticated context
optimise page loading
```

It may not:

```text
reimplement domain rules
write directly to Main Street database
bypass Spring authorisation
create business state
```

This prevents two competing backends.

---

# 12. Communication

Initial communication between browser/web applications and the Java backend should use ordinary HTTP application APIs.

Do **not** introduce:

```text
Kafka between frontend/backend
GraphQL federation
gRPC browser infrastructure
microservice messaging
```

without demonstrated need.

Real-time delivery such as order tracking may later justify:

```text
SSE
WebSocket
polling
```

but that decision belongs to the interaction requirement.

REST versus GraphQL does not need to be frozen by this document; however, **use-case-oriented HTTP APIs** are the initial default.

---

# 13. Local development

A developer must be able to run conceptually:

```text
MainStreet/
│
├── backend/
├── merchant-web/
└── storefront-web/
```

locally.

Normal development becomes:

```text
Backend
    Maven / Java

Merchant Web
    Node / Next.js

Storefront Web
    Node / Next.js

Local persistence
```

GitHub is irrelevant to whether the applications run.

It remains source control only.

---

# 14. Version policy

Do not architect Main Street around exact transient patch versions.

Record architectural lines:

```text
Java 25 LTS
Spring Boot 4.1.x
Next.js 16.x Active LTS
React 19.x
```

and keep patched versions current within those supported lines.

This is particularly important because Next.js now has explicit Active/Maintenance LTS support, and security patches should remain current. 

---

# FALSIFICATION REVIEW

## 15. Could Spring Boot be unnecessarily heavy?

Yes, a smaller Java framework could provide HTTP APIs with less machinery.

But Main Street requires:

```text
security
transactions
persistence integration
configuration
observability
testing
modular application boundaries
future integration infrastructure
```

The marginal framework simplicity would likely be offset by assembling these concerns manually.

Spring Boot also preserves the existing Java investment.

**Proposal survives.**

---

# 16. Could Spring encourage framework-coupled domain code?

Yes.

This is a legitimate failure mode.

Required constraint:

> **Core semantic/domain objects remain plain Java wherever framework integration is not intrinsically required.**

Spring annotations belong primarily at application/infrastructure/delivery boundaries.

With this constraint, the proposal survives.

---

# 17. Why Java 25 when development currently uses Java 26?

Because Java 26 is the current feature release, whereas Java 25 is the latest LTS. 

Could using 25 unnecessarily restrict us?

Nothing currently designed requires Java 26-only semantics.

Therefore adopting 26 as the production baseline gives us less support stability without demonstrated architectural value.

**Java 26 production baseline rejected. Java 25 LTS accepted.**

---

# 18. Could Next.js duplicate backend responsibilities?

Absolutely.

This is the largest frontend-stack risk.

If developers begin placing:

```text
business validation
order processing
merchant configuration
database writes
```

inside Next.js server code, MS-PROT-029 collapses.

Therefore the authoritative-backend constraint is mandatory.

**Survives only with this restriction.**

---

# 19. Could React/Next.js be excessive for a simple gardener site?

Yes, if every public page required a live Node server and large client JavaScript bundle.

But Next.js supports static/server rendering and cacheable output rather than requiring SPA behaviour everywhere. 

Public pages should minimise client-side JavaScript and use interactive components only where needed.

**Survives.**

---

# 20. Could one storefront application become too complex?

Potentially, because it serves:

```text
public pages
guest ordering
tracking
customer accounts
```

However, these form one customer journey and share:

```text
merchant domain resolution
branding
catalogue
customer interaction
```

Splitting them now creates more duplication than isolation.

If later scaling/security evidence demands it, the internal boundaries must permit extraction.

**Survives.**

---

# 21. Could merchant-web and storefront-web share useful UI code?

Yes.

Duplicating:

```text
buttons
forms
design tokens
basic UI primitives
API contract types
```

would be wasteful.

Therefore shared frontend packages are permitted.

But do **not** create one enormous shared business-component library coupling the two apps.

Share:

```text
design system
safe utilities
generated/API contracts where appropriate
```

not audience-specific business workflows.

---

# 22. Could a single Next.js app for everything be simpler?

Initially, yes.

But merchant and storefront differ materially in:

```text
security boundary
release risk
traffic
SEO
caching
branding
authenticated UX
eventual independent scaling
```

A single frontend application increases coupling precisely where MS-PROT-029 created separate surfaces.

**Single universal frontend rejected.**

---

# 23. Could three frontend applications be safer?

Merchant/public/customer could each be independent.

But customer interaction naturally transitions from public storefront:

```text
browse
→ order/book
→ track
→ optionally sign in
```

Separating public/customer now adds routing, identity and branding complexity without evidence.

**Three-app model rejected initially.**

---

# 24. Could Next.js lock Main Street to Vercel?

No. Next.js documents self-hosting on Node.js and supports normal build/start deployment. 

Therefore Vercel is **not** made an architectural dependency.

This is important for future infrastructure choice.

---

# 25. Could local development become cumbersome with Java + two Node applications?

Yes.

This is the principal operational cost.

But it remains manageable and substantially simpler than:

```text
multiple backend services
three independent frontend stacks
remote-only environments
```

Future local tooling may provide one command to start the stack, but no remote GitHub dependency is required.

**Survives.**

---

# Cross-domain validation

| Business | Public/storefront | Customer | Merchant | Result |
|---|---|---|---|---|
| Gardener | services, portfolio, enquiry | usually none/guest | quotes, jobs, calendar | **PASS** |
| Driving instructor | lessons, booking | persistent student area | calendar, students | **PASS** |
| Solicitor | services, enquiry | notifications/optional access | calendar, clients | **PASS** |
| Restaurant | menu, ordering | tracking | POS/order operations | **PASS** |
| Grocery | catalogue/order | fulfilment tracking/history | inventory/orders | **PASS** |
| Motel | rooms/booking | reservation access | allocation/check-in | **PASS** |
| Mechanic | services/request | optional job tracking | jobs/technicians | **PASS** |
| Realtor | listings/viewing | viewing interaction | leads/calendar | **PASS** |

No business requires a separate backend technology or bespoke frontend codebase.

---

# 26. Accepted technology baseline

| Concern | Decision |
|---|---|
| Backend language | **Java 25 LTS** |
| Backend framework | **Spring Boot 4.1.x** |
| Build | **Maven** |
| Backend topology | **Modular monolith** |
| Public/customer web | **Next.js 16.x + React 19 + TypeScript** |
| Merchant web | **Next.js 16.x + React 19 + TypeScript** |
| Frontend applications | **2 — `merchant-web`, `storefront-web`** |
| Authoritative business backend | **Spring Boot only** |
| Frontend server logic | **Presentation/BFF concerns only** |
| Initial frontend/backend integration | **Use-case-oriented HTTP** |
| Merchant sites | **Multi-tenant/configuration-driven storefront application** |
| Public rendering | **Server/static/cache first where appropriate** |
| Merchant rendering | **Dynamic interactive application** |
| Vercel dependency | **None** |
| GitHub runtime dependency | **None** |

Spring Boot 4.1 is currently stable and compatible with Java 25; Next.js 16 is Active LTS; React 19.2 is the current React documentation line. 

---

# 27. Accepted invariants

1. Java remains Main Street's authoritative backend language.
2. Production targets an LTS Java line, initially Java 25.
3. Spring Boot implements the modular backend.
4. Domain semantics remain framework-light/plain Java where possible.
5. Merchant, customer and public backend surfaces remain logically separate.
6. Spring Boot remains the authoritative business backend.
7. Next.js must not become a competing domain backend.
8. Two frontend applications are used initially: Merchant and Storefront.
9. Public and customer experiences coexist inside Storefront.
10. Merchant UI remains separately deployable/extractable.
11. Merchant websites use one multi-merchant application rather than merchant-specific codebases.
12. Public rendering favours server/static/cacheable output.
13. Customer/merchant interactivity is dynamic where required.
14. Rendering strategy is chosen per use case rather than globally.
15. APIs reflect application use cases, not persistence tables.
16. Initial communication uses ordinary HTTP; additional protocols require evidence.
17. Shared frontend code is limited to genuinely reusable contracts/design primitives.
18. Next.js deployment must remain provider-independent.
19. Framework versions remain on supported stable/LTS lines.
20. The complete stack remains locally runnable without GitHub.

---

# 28. Deferred technology decisions

MS-PROT-030 does **not** yet select:

```text
PostgreSQL or other database
JPA vs JDBC
REST specification details
OpenAPI strategy
authentication implementation
reverse proxy
CDN
containerisation
hosting provider
SSE vs WebSocket
CSS/design system
frontend state library
form library
testing framework beyond existing Java tests
```

Those decisions should be made only when their architectural requirements are sufficiently defined.

---

# 29. Governance verdict

The proposal was attacked on framework weight, Java support lifecycle, framework leakage, Next.js becoming a second backend, SPA overhead, frontend-app boundaries, Vercel dependence, scaling and local-development complexity.

The main modifications required were:

```text
Java 26 → Java 25 LTS production baseline

3 possible frontend surfaces
        ↓
2 frontend applications

Next.js full-stack freedom
        ↓
strict authoritative Spring backend boundary
```

The reduced architecture survives the representative merchant models.

```text
MS-PROT-030
Backend & Web Technology Architecture

PROPOSE                     ✓
FALSIFICATION REVIEW        ✓
FAILED ASSUMPTIONS REMOVED  ✓
REVISED                     ✓
CROSS-DOMAIN VALIDATION     ✓
ACCEPT                      ✓
```

## **Status: ACCEPTED**

### Canonical decision

> **Main Street will initially use a Java 25/Spring Boot 4.1 modular-monolith backend and two TypeScript/React/Next.js web applications: `merchant-web` for authenticated merchant operations and `storefront-web` for public merchant websites, guest/customer interactions, tracking, and optional customer accounts. Spring Boot remains the sole authoritative business backend; Next.js owns web rendering and presentation concerns. All components remain independently bounded and locally runnable, with no Vercel or GitHub runtime dependency.**

The next architectural gap is now **tenant resolution and merchant isolation across the backend and multi-merchant storefront**, so **MS-PROT-031 — Merchant Scope, Tenant Resolution & Isolation Model** should follow.