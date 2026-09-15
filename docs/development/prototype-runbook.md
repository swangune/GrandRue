# Main Street Executable Prototype Runbook

**Status:** Development execution evidence / operator runbook — not semantic authority  
**Branch:** `development`  
**Backend profile:** `prototype`

## Purpose

This runbook starts the executable Main Street prototype and demonstrates that materially different merchant experiences are produced from active capability/configuration semantics rather than merchant trade or business-type branching.

It now covers both:

```text
Spring Boot backend
        +
Next.js storefront-web
```

The public storefront path proves:

```text
active Configuration Release
        ↓
registered Surface Contributions
        ↓
derived public subject bindings
        ↓
interaction-specific storefront form
        ↓
public HTTP intent
        ↓
Spring public application adapter
        ↓
capability-owned internal context resolution
        ↓
authoritative Booking / Appointment / Order execution
        ↓
public-safe receipt
```

The browser does not construct internal operation identifiers, Allocation subjects, prototype Customer identifiers, internal SKU/orderable identifiers, governing release identifiers, or commercial-provenance references.

## Reference merchants

| Merchant | Selected capabilities | Prototype proof |
|---|---|---|
| `prototype-retailer` | Ordering, Inventory, Payment, Order Fulfilment | public `milk-2l` proposition → durable Order |
| `prototype-consultant` | Appointment, Scheduling, Customer, Payment | durable Appointment |
| `prototype-motel` | Booking, Customer, Payment | public `standard-room` → durable Booking |
| `prototype-daycare` | Booking, Customer, Payment | public `full-day-care-session` → internal `daycare-session` Booking |
| `prototype-gardener` | Appointment, Scheduling, Customer, Payment | durable gardening Appointment |
| `prototype-gardener-showcase` | Publication, Enquiry | published portfolio/read projection only |
| `prototype-gardener-bookable` | Publication, Enquiry, Appointment, Scheduling, Customer | portfolio + public `garden-maintenance` Appointment |
| `prototype-gardener-evolving` | Release 1: Publication, Enquiry; later release may add Appointment, Scheduling, Customer | configuration-evolution proof |
| `prototype-publisher` | Publication, Enquiry | seeded published-content read projection |

These are configured instances, not business archetypes. Two gardening businesses deliberately expose different capability graphs.

## 1. Start PostgreSQL

```powershell
docker compose -f compose.prototype.yml up -d
docker compose -f compose.prototype.yml ps
```

Prototype database:

```text
host: localhost:55432
database: mainstreet
user: mainstreet
password: mainstreet
```

If an old disposable prototype volume was initialised with incompatible credentials:

```powershell
docker compose -f compose.prototype.yml down -v
docker compose -f compose.prototype.yml up -d
```

Do not modify an unrelated local PostgreSQL installation.

## 2. Start Spring Boot

From the repository root:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=prototype"
```

The prototype profile defaults to:

```text
jdbc:postgresql://localhost:55432/mainstreet
```

Optional datasource overrides:

```text
MAINSTREET_PROTOTYPE_POSTGRES_URL
MAINSTREET_PROTOTYPE_POSTGRES_USER
MAINSTREET_PROTOTYPE_POSTGRES_PASSWORD
```

Flyway applies the repository migrations and prototype fixture setup seeds the configured reference data.

## 3. Start the storefront

Open a second terminal:

```powershell
cd storefront-web
npm install
npm run dev
```

The storefront runs on:

```text
http://localhost:3000
```

By default it talks to:

```text
http://localhost:8080
```

Override the backend origin when needed with:

```text
MAINSTREET_BACKEND_URL
```

Open the reference-merchant index and then a merchant storefront such as:

```text
http://localhost:3000/m/prototype-gardener-bookable
http://localhost:3000/m/prototype-motel
http://localhost:3000/m/prototype-retailer
http://localhost:3000/m/prototype-gardener-showcase
```

Expected distinction:

```text
prototype-gardener-showcase
    Publication + Enquiry
    no Appointment form

prototype-gardener-bookable
    Publication + Enquiry + Appointment + Scheduling + Customer
    Garden maintenance visit → Arrange a time form
```

The difference comes from active semantics, not from a `gardener` business-type branch.

## 4. Inspect the composed storefront surface

```powershell
curl.exe http://localhost:8080/prototype/merchants/prototype-gardener-showcase/storefront-surface
curl.exe http://localhost:8080/prototype/merchants/prototype-gardener-bookable/storefront-surface
curl.exe http://localhost:8080/prototype/merchants/prototype-motel/storefront-surface
curl.exe http://localhost:8080/prototype/merchants/prototype-retailer/storefront-surface
```

Public binding examples:

```text
Appointment
    public: garden-maintenance
    internal scheduled operation: gardening.perform

Booking
    public: standard-room
    internal capacity identity: not projected

Daycare Booking
    public: full-day-care-session
    internal booked subject: daycare-session

Ordering
    public: milk-2l
    internal orderable/stock subject: sku-1
```

The surface projection exposes the public side only. A binding is a derived locator/projection; it is not participation authority, current availability authority, or execution authority.

## 5. Exercise the public Appointment boundary

```powershell
curl.exe -X POST `
  http://localhost:8080/prototype/public/merchants/prototype-gardener-bookable/appointments `
  -H "Content-Type: application/json" `
  -H "Idempotency-Key: public-gardener-appointment-request-1" `
  -d '{"appointmentIdentifier":"public-gardener-appointment-1","subjectReference":"garden-maintenance","startsAt":"2037-01-12T13:00:00Z","endsAt":"2037-01-12T15:00:00Z"}'
```

The public request does **not** contain:

```text
gardening.perform
customer-1
prototype-gardener-bookable-capacity-1
```

The public receipt returns the public subject reference and commitment timing without internal execution/customer/allocation/release identifiers.

For development diagnostics only, read the internal prototype Appointment:

```powershell
curl.exe http://localhost:8080/prototype/merchants/prototype-gardener-bookable/appointments/public-gardener-appointment-1
```

That internal view shows that Spring resolved the public subject into the existing Appointment/Scheduling execution context.

## 6. Exercise the public Booking boundary

### Motel

```powershell
curl.exe -X POST `
  http://localhost:8080/prototype/public/merchants/prototype-motel/bookings `
  -H "Content-Type: application/json" `
  -H "Idempotency-Key: public-motel-booking-request-1" `
  -d '{"bookingIdentifier":"public-motel-booking-1","subjectReference":"standard-room","startsAt":"2037-01-14T14:00:00Z","endsAt":"2037-01-16T10:00:00Z"}'
```

### Daycare

```powershell
curl.exe -X POST `
  http://localhost:8080/prototype/public/merchants/prototype-daycare/bookings `
  -H "Content-Type: application/json" `
  -H "Idempotency-Key: public-daycare-booking-request-1" `
  -d '{"bookingIdentifier":"public-daycare-booking-1","subjectReference":"full-day-care-session","startsAt":"2037-01-13T08:00:00Z","endsAt":"2037-01-13T17:00:00Z"}'
```

The daycare browser-facing reference is `full-day-care-session`; Spring resolves it to the configured Booking subject `daycare-session`. Neither public request controls Allocation identity.

## 7. Exercise the public Ordering boundary

```powershell
curl.exe -X POST `
  http://localhost:8080/prototype/public/merchants/prototype-retailer/orders `
  -H "Content-Type: application/json" `
  -H "Idempotency-Key: public-retailer-order-request-1" `
  -d '{"orderIdentifier":"public-retailer-order-1","portions":[{"portionIdentifier":"portion-1","subjectReference":"milk-2l","quantity":1}]}'
```

The storefront sends `milk-2l`; it does not send `sku-1`, unit/currency configuration, Inventory subject identity, or commercial terms provenance.

For development diagnostics only:

```powershell
curl.exe http://localhost:8080/prototype/merchants/prototype-retailer/orders/public-retailer-order-1
```

The internal committed Order demonstrates that Spring resolved the public proposition to the configured Ordering/Inventory context.

## 8. Published content and current mutation boundary

```powershell
curl.exe http://localhost:8080/prototype/merchants/prototype-publisher/publications
curl.exe http://localhost:8080/prototype/merchants/prototype-gardener-showcase/publications
```

Both use one merchant-scoped Publication projection mechanism.

The prototype does not invent a permanent Publication authoring mutation merely to make the storefront editable. Publication command semantics must remain governed before such a mutation is introduced.

Likewise, the current Enquiry capability is not given a speculative mutation path here.

## 9. Merchant-choice and configuration-evolution proof

Discovery choices are evidence identities, not capability identities. Current prototype choices include:

```text
PUBLISH_INFORMATION
SEND_ENQUIRY
ARRANGE_APPOINTMENT
RESERVE_SUBJECT
PLACE_ORDER
SUBSCRIBE_UPDATES
NOTHING_ELSE_FOR_NOW
OTHER
```

Examples:

```text
PUBLISH_INFORMATION + SEND_ENQUIRY
    → candidate Publication + Enquiry semantics

ARRANGE_APPOINTMENT
    → candidate Appointment + Scheduling + Customer semantics

NOTHING_ELSE_FOR_NOW
    → no additional semantic seed
```

Automated tests also prove that `prototype-gardener-evolving` can activate a later Configuration Release adding Appointment/Scheduling/Customer without changing merchant identity.

## 10. Verification gates

Backend:

```powershell
mvn --batch-mode clean verify -Ppostgres-it
```

Storefront:

```powershell
cd storefront-web
npm test
npm run build
```

The executable storefront slice is valid when all of the following hold:

- the full Java unit + PostgreSQL integration suite is green;
- the Next.js composition/intent tests and production build are green;
- same-trade merchants can expose different capability/surface compositions;
- storefront forms arise from registered surface contributions and projected bindings;
- Appointment, Booking and Order use distinct bounded public application APIs;
- public requests contain public subject references rather than internal execution context;
- Spring resolves public references to capability-owned execution inputs;
- resulting commitments use the existing durable Appointment, Booking and Ordering paths;
- public receipts do not expose internal scheduled-operation, Allocation, prototype Customer, internal SKU/booked-subject, release, currency/unit, or commercial-provenance identifiers;
- unsupported public subjects fail closed before authoritative execution;
- semantic participation is not presented as live availability;
- a network exception or 5xx response does not get treated as proof that authoritative execution failed;
- uncertain retries preserve the same logical command, object and portion identity;
- a definitive 4xx rejection may establish a fresh logical intent identity for a changed retry;
- no controller or Next.js server action owns business truth; and
- no merchant is selected through business-category branching.

`PrototypeApplicationStartupIT` exercises the real Spring prototype profile and PostgreSQL by submitting public references through the HTTP boundary and then reading the resulting internal durable commitments.

## 11. Execution uncertainty and retry behaviour

The storefront follows the accepted network-failure distinction between transport evidence and business truth.

For the current prototype public mutation boundary:

```text
HTTP 2xx
    → acknowledged completion

HTTP 4xx
    → definitive backend rejection for this attempted intent
    → UI may issue fresh identifiers for a changed retry

HTTP 5xx
or fetch / connection failure
    → outcome is uncertain
    → the form remains mounted
    → the same commandIdentifier is retained
    → the same Appointment / Booking / Order identifier is retained
    → the same Order portion identifier is retained where applicable
    → retry sends the same logical business intent identity
```

The browser must not conclude that a failed network exchange means the business operation did not execute. This prevents a lost acknowledgement from becoming a duplicate Appointment, Booking or Order.

The current prototype does not claim general offline mutation authority. It only preserves one logical intent across safe transport retries while authoritative execution remains in Spring.

## 12. Latest verified executable evidence

Verified executable code head:

```text
c03b418cfb19f49cadfaa91354198b4bbfea9291
```

GitHub Actions evidence:

```text
Storefront Web Tests
run: 32949747825
result: SUCCESS
coverage gate: npm test + Next.js production build

Maven Tests
run: 32949747830
result: SUCCESS
coverage gate: full Java unit + PostgreSQL integration suite
```

The documentation commit that records this evidence is not itself a semantic or executable-code change; the executable evidence above applies to its verified code parent.

## Architectural boundary

```text
merchant evidence / structured discovery
    ↓
registered semantic seed resolution
    ↓
reviewed/approved Configuration Release
    ↓
active capability composition
    ↓
registered Surface Contributions
    ↓
derived public bindings
    ↓
Next.js presentation / thin server action
    ↓ ordinary HTTP
Spring public delivery
    ↓
public application adapter
    ↓ resolves public subject through capability configuration
existing capability-owned application service
    ↓
capability-owned PostgreSQL unit of work
    ↓
public-safe receipt
```

Next.js owns routing, rendering and forwarding only. Spring remains the authoritative backend. Public bindings locate already-authorised semantic participation; they do not create new participation, availability, or execution authority.
