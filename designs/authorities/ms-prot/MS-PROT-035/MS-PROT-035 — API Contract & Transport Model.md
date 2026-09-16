# MS-PROT-035 — API Contract & Transport Model

**Version:** 1.0  
**Status:** **Accepted**  
**Depends on:** MS-PROT-023, MS-PROT-028 → MS-PROT-034  
**Purpose:** Define how `merchant-web`, `storefront-web`, integrations, and future clients communicate with the Spring Boot backend without exposing persistence models or weakening semantic, tenant, and authority boundaries.

## 1. Governing principle

> **APIs expose application use cases and projections, not database tables, domain internals, or generic CRUD.**

```text
Client
  ↓
Delivery/API Contract
  ↓
Application Use Case
  ↓
Capability-owned semantics
  ↓
Authoritative state
```

Transport adapts requests to Main Street semantics. It does not define those semantics.

---

## 2. Separate API surfaces

The backend maintains logically distinct surfaces:

```text
Merchant API
Customer API
Public API
Integration API
```

### Merchant API

For authenticated owners/staff:

```text
manage offerings
accept orders
schedule work
manage resources
configure merchant
manage staff
```

### Customer API

For authenticated or scoped customer interaction:

```text
track order
view lessons
cancel eligible appointment
manage booking
view purchase history
```

### Public API

For anonymous permitted activity:

```text
view services/catalogue
view portfolio
submit enquiry
start booking
place guest order
```

### Integration API

For authenticated external systems:

```text
payment callback
calendar integration
delivery provider callback
```

These may share infrastructure but not authority contracts.

---

## 3. Use-case-oriented endpoints

Reject APIs that mirror persistence:

```text
POST /rows
PATCH /orders/123/state
PUT /resource-instance
```

Prefer semantic operations:

```text
POST /orders/{id}/accept
POST /appointments/{id}/cancel
POST /enquiries
GET  /orders/{id}/tracking
GET  /lessons/upcoming
```

Exact URI naming is implementation detail; the invariant is that API contracts express business intent.

---

## 4. Commands and queries

Transport retains the architectural distinction:

```text
Command
    → authoritative mutation

Query
    → read/projection
```

A write endpoint should not silently behave like a read, and a read request should not perform hidden business mutation.

HTTP method selection should reflect this distinction where practical.

---

## 5. API DTOs are boundary models

Transport objects are not domain objects.

Conceptually:

```text
HTTP JSON
   ↓
Request DTO
   ↓
Application Command
   ↓
Domain
```

and:

```text
Projection
   ↓
Response DTO
   ↓
HTTP JSON
```

Therefore:

```text
API DTO ≠ Domain Aggregate
API DTO ≠ jOOQ Record
API DTO ≠ Database Row
```

This protects domain and database evolution from frontend coupling.

---

## 6. Merchant scope

Merchant-owned API calls execute within resolved `MerchantScope`.

Public storefront:

```text
hostname/site
   ↓
authoritative merchant resolution
   ↓
MerchantScope
```

Merchant dashboard:

```text
authenticated identity
   +
requested merchant
   ↓
validated merchant relationship
   ↓
MerchantScope
```

A body parameter such as:

```json
{"merchantId":"merchant-b"}
```

must never be sufficient to switch tenant authority.

---

## 7. Resource identifiers

Identifiers locate subjects; they do not grant access.

Therefore:

```text
GET /orders/123
```

still requires:

```text
MerchantScope
+
identity/access grant
+
subject relationship
```

where appropriate.

Globally unique IDs would not change this rule.

---

## 8. Validation layers

API validation has multiple levels.

### Transport validation

```text
required JSON field
valid syntax
parseable timestamp
supported media type
```

### Application/domain validation

```text
operation permitted
requirement satisfied
policy permits action
target state valid
capacity available
actor authorised
```

Transport validation must not duplicate domain semantics.

---

## 9. Error contract

Clients need stable categories rather than Java exceptions.

Conceptually:

```text
INVALID_REQUEST
UNAUTHENTICATED
UNAUTHORISED
NOT_FOUND
CONFLICT
REQUIREMENT_UNSATISFIED
OPERATION_NOT_ALLOWED
RATE_LIMITED
INTERNAL_FAILURE
```

Exact codes are deferred.

The contract must preserve enough information for the client to respond correctly without exposing:

```text
stack traces
SQL errors
internal class names
sensitive merchant data
```

---

## 10. Not-found vs forbidden

Security sometimes requires hiding whether a protected object exists.

For example, customer A requesting customer B's order should not necessarily learn:

> "That order exists but belongs to someone else."

Therefore external error representation may intentionally collapse:

```text
not found
forbidden
```

where disclosure would create an information leak.

Internal audit may retain the real reason.

---

## 11. Concurrency conflicts

Some commands may fail because authoritative state changed.

Examples:

```text
slot already booked
room allocated
inventory exhausted
configuration superseded
order already completed
```

These are not generic server errors.

The API must represent them as semantic conflicts so clients can:

```text
refresh
reselect
inform user
retry only where appropriate
```

It must not automatically retry mutation blindly.

---

## 12. Idempotency

Duplicate-sensitive externally initiated operations need a way to recognise the same logical request.

Examples:

```text
place order
create booking
payment callback
refund request
```

The API architecture therefore supports idempotency keys or equivalent operation-specific duplicate handling.

But:

> **Idempotency is not mandatory infrastructure on every endpoint.**

Its use follows operation semantics established by MS-PROT-023.

---

## 13. Request identity is not business identity

Technical request IDs may support tracing:

```text
request-id
correlation-id
```

but they are not automatically:

```text
Command identity
Order identity
Process identity
Event identity
```

These identities must remain semantically distinct.

---

## 14. API versioning

Main Street should avoid premature:

```text
/v1/
/v2/
/v3/
```

for every internal web endpoint.

`merchant-web` and `storefront-web` initially evolve with the backend under Main Street control.

However, externally consumed contracts require controlled compatibility.

Therefore:

```text
Internal first-party API
    → coordinated evolution

External/integration API
    → explicit compatibility/version strategy when published
```

Breaking externally relied-upon contracts must never occur silently.

---

## 15. Additive evolution

Prefer compatible changes such as:

```text
add optional response field
add new endpoint
add new supported operation
```

over changing the meaning of existing fields.

A field named:

```text
status
```

must not silently shift semantic meaning between releases.

Where the contract meaning changes materially, introduce a deliberate contract migration.

---

## 16. Public projection contracts

Public APIs consume public projections.

Example:

```text
GET merchant website services
```

should not expose:

```text
internal state identifiers
staff notes
database IDs unnecessarily
inventory internals
configuration graph
```

The public contract is deliberately smaller than the merchant contract.

---

## 17. Customer projection contracts

Customer responses expose only permitted customer representations.

Example internal order:

```text
fraud_review
kitchen_queue_position
staff_assignment
preparing
```

may become:

```text
Preparing your order
```

Customer API responses therefore follow MS-PROT-027 projections, not raw domain serialization.

---

## 18. Merchant operational contracts

Merchant APIs can expose richer operational information but remain authority-sensitive.

A receptionist and owner may call the same Merchant surface but receive different commands/data according to privileges.

Therefore:

```text
Merchant API
    ≠
all merchant data accessible to every staff member
```

---

## 19. Pagination and filtering

Collection APIs must support bounded retrieval where data may grow.

Examples:

```text
orders
customers
appointments
jobs
audit records
```

Avoid APIs that assume:

```text
GET /orders → every order ever
```

Filtering and pagination are read concerns and should operate on read models/projections where appropriate.

---

## 20. Time representation

Transport must represent absolute timestamps unambiguously.

Merchant-local business time and UTC/instant time are distinct concepts.

For example:

```text
Appointment:
    10:00 Europe/London

Execution:
    authoritative instant
```

API design must not silently discard timezone semantics.

Exact serialization format can follow standard web conventions at implementation time.

---

## 21. Money

Monetary values must not use floating-point semantics.

API contracts should carry conceptually:

```text
amount
currency
```

for example:

```text
1250 GBP
```

rather than assuming:

```text
12.50
```

has universal currency or floating-point precision semantics.

Detailed Money domain design may follow separately.

---

## 22. File/media transport

Large merchant media such as:

```text
portfolio images
product photos
documents
```

should not automatically flow through ordinary business JSON APIs as encoded blobs.

API contracts may coordinate upload/access while dedicated object/media storage handles bytes later.

Detailed media architecture is deferred.

---

## 23. Real-time updates

Order tracking and merchant dashboards may benefit from live updates.

The API model permits future:

```text
polling
SSE
WebSocket
```

but none is mandatory now.

Real-time transport changes delivery mechanics, not authoritative semantics.

A pushed update remains a projection/fact, not authority for mutation.

---

# 24. Falsification review

| Failed assumption | Why it fails |
|---|---|
| One generic API can safely serve all audiences | Merchant, customer and public authority differ |
| CRUD APIs are sufficient | Main Street operations contain semantic intent/invariants |
| Domain entities can be serialized directly | Leaks internals and tightly couples clients |
| Merchant ID supplied by client establishes tenant | Enables cross-tenant manipulation |
| Resource identifier proves access | Enables insecure direct-object access |
| HTTP validation can contain all business rules | Duplicates domain semantics |
| Every failure can be HTTP 400/500 | Clients need semantic conflict/auth decisions |
| Every write can simply be retried | Duplicate-sensitive operations may cause harm |
| Every endpoint requires idempotency | Adds unnecessary infrastructure |
| Internal APIs need permanent version numbers immediately | First-party clients can evolve together |
| External APIs can evolve like internal APIs | External consumers cannot coordinate releases |
| Customer API can expose internal states directly | Violates visibility model |
| Real-time transport should be universal | Many interactions need none |
| Request ID can serve all identities | Request, command, process and event identity differ |
| APIs can return unbounded collections | Data grows indefinitely |

All are rejected.

---

# 25. Cross-domain validation

**Gardener:** anonymous enquiry through Public API; merchant handles quotation/job through Merchant API; customer portal need not exist. **PASS**

**Driving instructor:** public booking entry, authenticated student schedule/history, merchant calendar operations all share backend semantics with separate contracts. **PASS**

**Solicitor:** public enquiry/appointment request plus merchant operations; customer account API can be absent. **PASS**

**Restaurant:** public order creation, scoped guest tracking, merchant order progression and duplicate-safe order creation fit naturally. **PASS**

**Grocery:** inventory conflicts produce semantic conflict rather than generic server failure. **PASS**

**Motel:** stale availability can lead to booking/allocation conflict handled without trusting prior public projection. **PASS**

**Mechanic:** public service request and optional protected job tracker coexist. **PASS**

**Realtor:** public enquiry and viewing request remain distinct use cases without exposing internal lead-management model. **PASS**

---

# 26. Accepted invariants

1. APIs expose application use cases and projections rather than persistence.
2. Merchant, Customer, Public and Integration surfaces remain logically distinct.
3. API DTOs are boundary models.
4. Domain objects and jOOQ records are never public transport contracts by default.
5. Merchant Scope is established independently of untrusted request payloads.
6. Identifiers do not grant access.
7. Transport validation and domain validation remain distinct.
8. Error responses expose stable client-relevant semantics, not implementation exceptions.
9. Security may intentionally hide object existence.
10. Concurrency conflicts are semantic outcomes, not generic server errors.
11. Duplicate-sensitive operations support appropriate idempotency mechanisms.
12. Idempotency is not mandatory on every endpoint.
13. Request, Command, Event, Process and business identifiers remain distinct.
14. Internal first-party and external API compatibility requirements differ.
15. External contracts require deliberate evolution.
16. Public/customer APIs expose projections rather than raw internal state.
17. Merchant APIs remain privilege-sensitive.
18. Collection APIs must remain bounded.
19. Timezone and instant semantics must not be conflated.
20. Monetary values carry explicit currency and non-floating-point semantics.
21. Large media need not pass through ordinary JSON business contracts.
22. Real-time transport is optional and does not redefine authoritative semantics.

---

# 27. Deferred decisions

MS-PROT-035 does **not** yet choose:

```text
exact REST URI conventions
OpenAPI tooling
JSON library configuration
error-code catalogue
pagination scheme
cursor vs offset pagination
SSE vs WebSocket
authentication headers/cookies
CSRF strategy
CORS policy
idempotency-key header format
media upload protocol
external API version syntax
```

These implementation choices must conform to the contract above.

---

# 28. Governance verdict

```text
MS-PROT-035
API Contract & Transport Model

PROPOSE                     ✓
FALSIFICATION REVIEW        ✓
FAILED ASSUMPTIONS REMOVED  ✓
CROSS-DOMAIN VALIDATION     ✓
ACCEPT                      ✓
```

## **Status: ACCEPTED**

### Canonical decision

> **Main Street exposes separate Merchant, Customer, Public and Integration application contracts over the same capability-owned backend. APIs represent use cases and audience-specific projections, never database structures or raw domain objects. Merchant scope and authority are established at trusted boundaries; conflicts and duplicate-sensitive operations receive explicit semantics; and first-party API evolution remains flexible while externally published contracts require deliberate compatibility guarantees.**

The next significant unresolved architecture is **how the merchant websites themselves are assembled from configuration**—page composition, reusable sections, capability-driven routes, business-specific presentation differences, SEO/public projections, and the boundary between controlled customisation and bespoke UI.

That should be **MS-PROT-036 — Storefront Composition & Website Generation Model**.