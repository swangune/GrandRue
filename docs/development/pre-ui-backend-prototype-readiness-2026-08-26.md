# Main Street Pre-UI Backend Prototype Readiness

**Status:** PASS — READY TO PIVOT TO UI PROTOTYPING WITH ONE EXPLICIT DEFERRED MUTATION GAP  
**Date:** 26 August 2026  
**Branch:** `development`  
**Authority status:** Derived implementation/conformance evidence only; not semantic authority

---

## 1. Acceptance Question

Can Main Street now run as one Spring Boot/PostgreSQL application in which materially different merchant configurations activate different capabilities, execute selected capability-owned operations through the accepted runtime, persist authoritative state, survive restart/retry, and expose transport/read surfaces without business-category-specific code?

**Verdict: YES for the required pre-UI prototype boundary.**

The remaining Publication mutation contract is explicitly deferred because MS-PROT-046 defines Publication semantics but the accepted corpus does not yet register a concrete executable Publication command/handler identity. A read-only Publication projection is sufficient for the current UI prototype boundary and MUST NOT be mistaken for Publication mutation authority.

---

## 2. Executable Architecture Proven

```text
HTTP transport DTO
        ↓
prototype application boundary
        ↓
TrustedExecutionContext
        ↓
ActiveOperationResolver
        ↓
ScopedOperationDispatcher
        ↓
capability-owned application service
        ↓
capability-owned PostgreSQL unit of work
        ↓
authoritative state + outbox/claims
        ↓
derived HTTP read response
```

The prototype does not introduce a parallel runtime, universal business transaction, merchant-type switch, controller-owned business truth or provider-owned semantic state.

---

## 3. Reference Merchant Matrix

| Reference merchant | Active capabilities | Runtime operation / surface | Persistence | Restart/retry | Result |
|---|---|---|---|---|---|
| `prototype-retailer` | Ordering, Inventory, Payment, Order Fulfilment | `ordering.commit`; Order GET | PostgreSQL Ordering + Inventory claim | PASS | PASS |
| `prototype-consultant` | Booking, Appointment, Customer, Payment | `booking.confirm`; Booking GET | PostgreSQL Booking + Appointment + allocation | PASS | PASS |
| `prototype-publisher` | Publication, Enquiry | Published-content GET projection | seeded projection only | not applicable to mutation | PASS for pre-UI read slice |

The publisher has no synthetic Ordering, Inventory, Booking or Appointment capability. The consultant has no Ordering capability. The retailer has no Appointment capability.

---

## 4. Merchant Configuration / Activation Gate

PASS.

The three reference merchants are published and activated through the existing Configuration Release boundaries rather than through prototype-specific category branching.

The executable model preserves exact configuration identity, version and semantic-registry provenance. An earlier prototype fixture that violated this invariant was rejected by the existing RCP validation and corrected rather than weakening the invariant.

The active merchant read surface exposes derived configuration information only:

```text
GET /prototype/merchants/{merchantIdentifier}
```

It returns the active configuration/release and selected capability/operation identities without becoming configuration authority.

---

## 5. Retailer Vertical Slice

### 5.1 Command surface

```text
POST /prototype/merchants/{merchantIdentifier}/orders
Idempotency-Key: <logical-command-identity>
```

The transport body cannot establish merchant scope. Merchant scope comes from the route and the prototype trusted execution context.

### 5.2 Runtime path

```text
HTTP
→ PrototypeOrderUseCase
→ TrustedExecutionContext
→ ActiveOperationResolver
→ ScopedOperationDispatcher
→ OrderingApplicationService
→ JooqOrderingUnitOfWork
→ Ordering + Inventory-owned claim persistence
```

PASS.

### 5.3 Proven invariants

- exact active semantic/configuration release affinity;
- merchant-scoped Order identity;
- committed commercial provenance preserved;
- required Inventory Claim and Order commitment atomicity;
- idempotent replay by logical command identity;
- same logical retry returns the same Order;
- cross-merchant/inapplicable operation rejected;
- persistence survives adapter/use-case recreation;
- Inventory available-to-promise reflects the committed claim;
- HTTP domain objects are mapped to explicit transport DTOs rather than serialized as domain authority.

---

## 6. Consultant Vertical Slice

### 6.1 Command surface

```text
POST /prototype/merchants/{merchantIdentifier}/bookings
Idempotency-Key: <logical-command-identity>
```

### 6.2 Runtime path

```text
HTTP
→ PrototypeBookingUseCase
→ TrustedExecutionContext
→ ActiveOperationResolver
→ ScopedOperationDispatcher
→ BookingApplicationService
→ JooqBookingUnitOfWork
→ Booking + Appointment + capacity allocation persistence
```

PASS.

### 6.3 Proven invariants

- active Configuration Release selects `booking.confirm`;
- Booking, Appointment and allocation facts commit coherently;
- overlapping authoritative capacity is rejected;
- idempotent replay does not duplicate Booking/Appointment/allocation;
- persistence survives adapter/use-case recreation;
- retailer cannot execute consultant Booking operation;
- response mapping remains outside domain authority.

---

## 7. Information Publisher Slice

PASS for the current pre-UI read boundary.

```text
GET /prototype/merchants/prototype-publisher/publications
```

The seeded response demonstrates:

- independent Publication capability composition;
- PublishedContent identity;
- `PUBLISHED` lifecycle evidence;
- explicit revision;
- publication-window evidence;
- no fabricated commerce/scheduling dependencies.

### 7.1 Explicit deferred gap

The prototype does **not** expose a Publication mutation endpoint.

Reason:

- MS-PROT-046 defines PublishedContent / Opportunity / Announcement authority and lifecycle;
- the current accepted executable corpus does not register a concrete Publication command/operation identity and handler contract;
- inventing `publication.publish`, `publication.create`, lifecycle transition commands or revision semantics inside prototype code would violate DESIGN-RULES and IMPLEMENTATION-RULES.

This gap is non-blocking for the next UI-prototype phase because the current UI can consume the read projection while merchant Publication-authoring UI remains out of scope. Before Publication-authoring implementation begins, the missing executable command contract MUST complete its governed design lifecycle.

---

## 8. Delivery / Runtime Gate

PASS.

Spring Web has been added using the accepted Java/Spring backend stack. Prototype delivery adapters are isolated behind:

```text
spring.profiles.active=prototype
```

The real application has been booted in integration testing with:

- Java 25;
- Spring Boot 4.1;
- embedded Tomcat on an ephemeral port;
- PostgreSQL 18;
- Flyway validating/applying 27 migrations;
- actual prototype Spring beans;
- actual DispatcherServlet HTTP request;
- graceful shutdown.

The application is therefore an executable server, not a set of manually instantiated test objects.

---

## 9. Test Evidence

Latest full verification command:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

Latest verified result on the prototype head:

```text
Unit tests:        464 passed
Integration tests: 158 passed
Total:             622 passed
Failures:          0
Errors:            0
BUILD SUCCESS
```

The integration suite includes the durable prototype retailer, durable prototype consultant and live Spring prototype startup tests.

---

## 10. Operator Run Gate

PASS by repository composition and CI startup proof.

Local operator path is documented in:

```text
docs/development/prototype-runbook.md
```

PostgreSQL can be started with:

```powershell
docker compose -f compose.prototype.yml up -d
```

Main Street can then be started with:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=prototype"
```

The prototype profile has environment-variable overrides for datasource URL, user and password.

---

## 11. Pre-UI Completion Gate

| Gate | Status |
|---|---|
| Full Maven + PostgreSQL suite green | PASS |
| Spring application boots | PASS |
| Flyway schema migration | PASS |
| Three materially different merchant configurations active | PASS |
| Capability selection visible | PASS |
| Runtime operation selected from active release | PASS |
| Retail Order commit/read | PASS |
| Retail Inventory claim atomicity | PASS |
| Retail retry/idempotency | PASS |
| Retail restart recovery | PASS |
| Consultant Booking/Appointment commit/read | PASS |
| Consultant capacity conflict | PASS |
| Consultant retry/idempotency | PASS |
| Consultant restart recovery | PASS |
| Information-only publisher read surface | PASS |
| Publisher remains free of synthetic commerce semantics | PASS |
| Tenant/capability applicability isolation | PASS |
| HTTP transport separated from domain authority | PASS |
| Business-category branching required | NO |
| Publication mutation command contract | DEFERRED — governed semantic gap |

---

## 12. Decision

**The Main Street backend is sufficiently executable to pivot to UI prototyping.**

The UI should now be built against the actual prototype HTTP/read contracts rather than hypothetical screens.

The next UI phase SHOULD preserve the same composition principle:

```text
active merchant capabilities
        ↓
Surface Contributions / projections
        ↓
merchant/customer UI composition
```

The UI MUST NOT create a new universal business-work model or infer business authority from page structure.

Publication authoring remains deliberately deferred until its executable command semantics complete the Design Rules lifecycle.
