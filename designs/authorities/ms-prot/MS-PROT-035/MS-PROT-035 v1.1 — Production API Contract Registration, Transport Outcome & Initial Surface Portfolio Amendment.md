# MS-PROT-035 v1.1 — Production API Contract Registration, Transport Outcome & Initial Surface Portfolio Amendment

**Document ID:** MS-PROT-035  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** Manual approval on 27 August 2026 after in-chat proposal, authority trace, implementation-evidence review, falsification, ambiguity review and recommendation  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-035 v1.0 only within production API-contract registration, surface classification, trusted request-context establishment, command/query/callback/media transport, idempotency/concurrency representation, transport outcome/failure semantics, compatibility and initial production API portfolio  
**Preserves:** use-case-oriented APIs; API DTO/domain separation; trusted Merchant Scope; identifiers do not grant access; transport/domain-validation separation; audience-specific projections; selective idempotency; bounded collections; explicit time/currency semantics; external compatibility discipline; optional real-time transport  
**Depends on:** MS-PROT-023; composite MS-PROT-027; MS-PROT-029; MS-PROT-031; MS-PROT-035 v1.0; MS-PROT-040; MS-PROT-043; MS-PROT-046; composite MS-PROT-048; MS-PROT-051; MS-PROT-052; composite MS-PROT-053; MS-PROT-054; composite MS-PROT-055; composite MS-PROT-057; composite MS-PROT-058; MS-PROT-059; composite MS-PROT-060; composite MS-PROT-061; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; composite MS-PROT-066; MS-PROT-067; composite MS-PROT-068; composite MS-PROT-069; MS-PROT-070; MS-PROT-071; MS-PROT-072; MS-PROT-073; MS-PROT-074; composite MS-PROT-075; MS-PROT-076; composite MS-PROT-077; MS-PROT-079; ADR-014  
**Closes:** MS-PROT-079 Target 20 — Production APIs  
**Purpose:** Make Main Street’s accepted capability operations, projections, Exposure contracts, trusted principals, provider callbacks and operational interventions production-transportable without permitting routes, DTOs, HTTP status codes, client-provided identifiers or frontend conventions to become semantic authority.

---

## 1. Governing Decision

MS-PROT-035 v1.0 remains authoritative:

> **APIs expose application use cases and projections, not database tables, domain internals or generic CRUD.**

Target 20 adds the production contract:

```text
external / first-party interaction
        ↓
registered API Contract
        ↓
trusted request-context establishment
        ↓
transport validation
        ↓
owner-qualified operation / query / callback
        ↓
accepted runtime authority
        ↓
transport-safe result
```

Hard invariant:

```text
API route
API DTO
HTTP method
HTTP status
frontend component
client identifier
        ≠
semantic authority
```

---

# API Surface Classification

## 2. Production Surface Classes

The initial backend recognises the following logical transport surfaces:

```text
PUBLIC
CUSTOMER_CONTEXTUAL
MERCHANT_OPERATIONAL
PLATFORM_IDENTITY_BOOTSTRAP
PLATFORM_ADMINISTRATIVE
INTEGRATION_INGRESS
```

These are transport/trust boundaries.

They are not separate business systems.

---

## 3. PUBLIC

`PUBLIC` permits only:

```text
publicly exposable queries
+
explicitly registered anonymous/public operations
```

Merchant Scope is established from trusted routing such as:

```text
resolved storefront path
custom-domain mapping
other registered public merchant locator
```

A request-body `merchantId` cannot establish scope.

---

## 4. CUSTOMER_CONTEXTUAL

`CUSTOMER_CONTEXTUAL` supports:

```text
authenticated CustomerAccount context
or
bounded Guest Contextual Access Proof
```

for exact merchant/resource/relationship scope.

Therefore:

```text
CustomerAccount
    is not mandatory

guest
    does not mean PUBLIC authority

resource identifier
    does not establish customer relationship
```

A guest with an exact secure Order access proof may therefore use a Customer-contextual contract without becoming a registered CustomerAccount.

---

## 5. MERCHANT_OPERATIONAL

`MERCHANT_OPERATIONAL` serves Merchant Controllers and authorised staff operating within one resolved Merchant Scope.

Canonical:

```text
authenticated identity
        +
current merchant relationship
        +
selected Merchant Scope
        +
required operational-device context
        +
Actor Authorisation
```

where applicable.

---

## 6. Staff Does Not Create a Separate Business API

Staff does not receive a parallel:

```text
Staff API business model
```

Instead:

```text
MERCHANT_OPERATIONAL API
        +
staff Execution Principal
        +
Merchant Membership
        +
Role Assignment
        +
Merchant Operational Device Context
```

determines permitted use.

The same semantic operation may therefore be used by:

```text
Merchant Controller
authorised manager
authorised receptionist
```

with different Actor Authorisation.

---

## 7. PLATFORM_IDENTITY_BOOTSTRAP

Some legitimate operations occur before a Merchant Scope exists.

Examples include:

```text
authentication ceremony
session establishment
Merchant Account establishment
initial Controller/bootstrap progression
```

These use explicit platform/bootstrap scope.

Main Street MUST NOT manufacture:

```text
fake MerchantScope
```

to fit merchant-oriented API infrastructure.

---

## 8. PLATFORM_ADMINISTRATIVE

Operational/platform administrators use a distinct privileged boundary for accepted:

```text
manual reconciliation
registered operational intervention
semantic/release administration
security administration
other explicitly accepted platform operations
```

This API MUST NOT provide:

```text
generic database edit
generic force-success
generic change-any-status
unbounded merchant impersonation
```

Platform administration remains subject to strong authentication, current authority and Audit requirements.

---

## 9. INTEGRATION_INGRESS

External providers and integrations enter through `INTEGRATION_INGRESS`.

Examples include:

```text
payment provider callback
notification provider callback
shipment/carrier callback
return-label provider callback
calendar/provider callback
authorisation redirect/callback
```

Integration ingress is not Merchant API traffic and is not customer/public traffic.

---

# API Contract Registration

## 10. Registered Production API Contract

Every production endpoint/use case SHALL implement one registered API contract.

The logical contract identity is independent from its physical URI.

Conceptually:

```text
ApiContractIdentity
{
    owner
    contractIdentifier
}
```

Example:

```text
ordering / public-order-create
```

A path such as:

```text
POST /orders
```

does not become the contract identity.

---

## 11. API Contract Kinds

Target 20 defines four production contract kinds:

```text
ApiCommandContract
ApiQueryContract
ApiCallbackContract
ApiMediaTransferContract
```

These are logical contract types.

They do not mandate four Java interfaces.

---

## 12. No Generic Dynamic API Dispatcher

Rejected:

```text
POST /execute
{
    "operation": "...",
    "arguments": ...
}
```

as the ordinary public production API.

A generic transport dispatcher would weaken:

```text
schema validation
authority review
Exposure
error disclosure
idempotency policy
compatibility
attack-surface control
```

Registered operations remain explicit.

---

# Command Contracts

## 13. ApiCommandContract

A command contract SHALL determine enough information to establish:

```text
contract identity
surface
owning application operation
scope-establishment rule
eligible principal class(es)
transport input contract
logical-idempotency requirement
optimistic-concurrency requirement where applicable
completion mode
safe result representation
safe rejection representation
resource-protection target where applicable
data-protection classification
```

The API contract does not redefine the owning business operation.

---

## 14. Transport Request Identity

A physical HTTP request may have:

```text
requestId
traceId
correlationId
```

These remain distinct from:

```text
logical Command identity
business-object identity
Domain Event identity
DurableWorkInstruction identity
provider-effect identity
```

Transport adapters MUST NOT substitute request IDs for semantic operation identity.

---

# Trusted Context

## 15. Server-Established Context

The API layer SHALL establish trusted context before invoking application semantics.

Conceptually:

```text
incoming transport
        ↓
authentication / origin validation
        ↓
scope resolution
        ↓
principal resolution
        ↓
audience / contextual relationship
        ↓
TrustedExecutionContext
```

Client values may contribute evidence.

They never become trusted merely because they appear in JSON, query parameters, headers or route variables.

---

## 16. Merchant Scope

For merchant operations:

```text
requested merchant reference
        +
authenticated identity
        +
current merchant relationship
        ↓
validated Merchant Scope
```

For public/storefront operations:

```text
validated site/domain/merchant route
        ↓
Merchant Scope
```

For integration callbacks:

```text
authenticated provider source
+
historical provider/connection correlation
        ↓
applicable scope
```

---

## 17. Identifiers Are Locators Only

A supplied:

```text
OrderId
BookingId
AppointmentId
CustomerId
MediaAssetId
MerchantId
PaymentId
```

locates a candidate subject.

It does not grant:

```text
read access
write authority
relationship eligibility
merchant scope
Exposure
```

---

# Validation

## 18. Transport Validation

Transport owns validation such as:

```text
malformed JSON
required transport field absent
invalid syntactic identifier
unsupported media type
invalid timestamp encoding
invalid pagination token encoding
payload exceeds transport bound
```

---

## 19. Semantic Validation

The owning authority remains responsible for:

```text
operation applicability
Actor Authorisation
Commercial Entitlement
trust satisfaction
business requirements
capacity
lifecycle eligibility
Provider Readiness
authoritative concurrency
historical affinity
```

Transport MUST NOT duplicate these business rules.

---

# Idempotency

## 20. Client Retry Identity

Where the owning operation is duplicate-sensitive and an external/client retry may occur, its ApiCommandContract SHALL define an accepted client logical-retry mechanism.

This may be physically represented by:

```text
idempotency header
request field
provider correlation
opaque operation token
```

but the transport representation is not itself the semantic authority.

---

## 21. Idempotency Scope

A client retry identity is interpreted within at least:

```text
ApiCommandContract identity
+
trusted operation scope
+
logical caller/origin context where required
```

It MUST NOT become one platform-global key accidentally shared across merchants or unrelated operations.

---

## 22. Same Retry Identity, Same Intent

If the same logical retry identity is received with materially equivalent intent:

```text
same logical operation
        ↓
recover / return existing accepted result
```

where supported.

It MUST NOT create an additional effect.

---

## 23. Retry Identity Reused for Different Intent

If the same logical retry identity is reused with materially different request intent:

```text
IDEMPOTENCY_CONFLICT
```

or an equivalent owner-safe conflict MUST result.

Main Street MUST NOT silently reinterpret the second payload as the original operation.

---

## 24. Not Every Command Requires a Client Key

MS-PROT-035/MS-PROT-059 remain authoritative:

```text
idempotency mechanism
    is operation-specific
```

A command whose accepted semantics do not require client retry identity must not acquire artificial idempotency merely for framework uniformity.

---

# Optimistic Concurrency

## 25. Revision-Affined Mutations

Where an accepted owner operation requires optimistic concurrency, the API contract SHALL expose the required expected-authority revision/precondition.

Examples include applicable:

```text
Profile revision
Publication revision
Configuration revision
Onboarding case revision
Order revision
```

---

## 26. Concurrency Token Is Not Authority

A client-supplied expected revision means:

```text
I intend to mutate the version I observed
```

not:

```text
this version is still authoritative
```

The server performs current authoritative comparison.

---

## 27. Stale Revision

A failed expected-revision check produces:

```text
CONFLICT
```

with a safe owner-qualified reason where useful.

The API MUST NOT automatically overwrite current state.

---

# Command Completion Semantics

## 28. ApiCommandOutcomeClass

Transport SHALL distinguish:

```text
COMPLETED
ACCEPTED_PENDING
REJECTED
OUTCOME_UNCERTAIN
```

These are transport/execution outcome classes.

They are not universal business lifecycle states.

---

## 29. COMPLETED

`COMPLETED` means:

> The requested authoritative operation has reached the success boundary defined by its owning Operation Contract.

A completed Order command does not require every later:

```text
Notification
projection update
analytics reaction
```

to have completed.

---

## 30. ACCEPTED_PENDING

`ACCEPTED_PENDING` means:

> Main Street has durably accepted responsibility for the requested logical operation/process, but the requested authoritative outcome has not yet reached its completion boundary.

This is permitted only where the accepted owner/process contract actually allows asynchronous progression.

It MUST NOT be used simply because:

```text
notifications are pending
```

after an otherwise completed business command.

---

## 31. OUTCOME_UNCERTAIN

`OUTCOME_UNCERTAIN` means:

> A duplicate-sensitive consequential execution may have occurred, but Main Street cannot yet safely establish the result.

Canonical:

```text
provider request attempted
        ↓
acknowledgement lost
        ↓
OUTCOME_UNCERTAIN
```

The API MUST preserve the same logical operation/reference required for reconciliation.

It MUST NOT instruct the caller to create a new business operation blindly.

---

## 32. REJECTED

`REJECTED` means:

> The requested business operation was not accepted for authoritative execution because an applicable admission/validation condition rejected it.

Independent security/audit/protection evidence may still exist.

---

# Error / Problem Contract

## 33. Stable Top-Level Problem Categories

The production API SHALL preserve at least the following client-relevant categories:

```text
INVALID_REQUEST
UNAUTHENTICATED
NOT_AUTHORISED
NOT_FOUND_OR_NOT_ACCESSIBLE
NOT_APPLICABLE
NOT_ENTITLED
TRUST_REQUIREMENT_UNSATISFIED
OPERATIONALLY_INELIGIBLE
PROVIDER_UNAVAILABLE
REPRESENTATION_UNAVAILABLE
CONFLICT
IDEMPOTENCY_CONFLICT
RATE_LIMITED
OUTCOME_UNCERTAIN
TEMPORARILY_UNAVAILABLE
INTERNAL_FAILURE
```

These categories compose existing accepted authorities.

They do not create business lifecycle states.

---

## 34. Owner-Qualified Safe Detail

An API problem MAY additionally expose an owner-qualified safe code such as conceptually:

```text
ordering / order-no-longer-amendable
inventory / insufficient-available-quantity
scheduling / requested-slot-conflict
```

where exposing the detail is safe and useful.

Transport MUST NOT invent owner-specific business reasons.

---

## 35. Security Disclosure

Internal truth may be:

```text
target exists
but caller has no relationship
```

while external representation is:

```text
NOT_FOUND_OR_NOT_ACCESSIBLE
```

where revealing existence would create a security/privacy leak.

---

## 36. Internal Exceptions Are Never API Contracts

The following MUST NOT appear as stable external semantics:

```text
Java exception class
SQL exception
stack trace
table/column name
internal package
provider secret
raw OAuth error containing sensitive information
```

---

## 37. HTTP Status Is Secondary Transport Mapping

HTTP status codes SHALL be compatible with the semantic outcome but do not themselves define it.

The stable API problem/outcome contract is authoritative for client behaviour.

In particular:

```text
OUTCOME_UNCERTAIN
```

MUST NOT be reduced to an undifferentiated server error whose natural client reaction is to create a fresh operation.

Exact HTTP-status mapping remains implementation scope provided the semantic distinction survives.

---

## 38. Retry Guidance

Where API responses provide retry guidance, it must derive from accepted owner/resilience semantics.

Conceptually valid guidance may include:

```text
retry same logical request
refresh current state before another operation
reauthenticate
retry after resource-protection window
do not retry automatically
await reconciliation / query current outcome
```

A generic:

```text
500 → retry
```

rule is prohibited.

---

# Query Contracts

## 39. ApiQueryContract

A query contract SHALL identify:

```text
contract identity
surface
owning query / Projection Contract
scope-resolution rule
Audience Observation Context
relationship requirement where applicable
Projection Serviceability requirement
Exposure contract(s)
filter/sort contract
pagination/bound rule
safe response representation
unavailable/not-accessible representation
```

---

## 40. Queries Do Not Mutate Business Truth

A `GET`-like transport operation MUST NOT silently create:

```text
business commitment
customer relationship
read acknowledgement with business meaning
inventory allocation
payment fact
```

unless the invoked operation is explicitly modelled as a Command rather than a query.

---

# Exposure

## 41. Exposure Remains Element-Level

The API consumes MS-PROT-027 Exposure.

Canonical:

```text
legitimate candidate
+
Projection Serviceability
+
Audience Observation Context
+
Exposure
        ↓
API representation
```

The API does not decide independently that a field is public because:

```text
it exists in DTO
```

---

## 42. WITHHOLD Is Not Automatically an External Error

For an otherwise valid resource:

```text
WITHHOLD element
```

may result in:

```text
element omitted
or
safe reduced representation
```

according to the owning API/Exposure contract.

The response MUST NOT reveal that protected information exists merely through a special field/error if that would defeat Exposure.

---

## 43. Whole-Resource Protection

Where the caller must not learn whether the resource exists:

```text
NOT_FOUND_OR_NOT_ACCESSIBLE
```

may represent:

```text
absent
not related
withheld
unauthorised
```

at the external boundary.

Internal evidence remains more precise.

---

# Projection Serviceability

## 44. Projection Failure Is Not Exposure Denial

The API SHALL distinguish internally:

```text
representation cannot currently be served
```

from:

```text
audience is not permitted to observe it
```

Therefore:

```text
Projection unavailable
    ≠ WITHHOLD
```

---

## 45. Reduced / Degraded Reads

Where a Projection Contract explicitly permits a reduced or stale representation, the API may serve that representation.

Any client-visible serviceability/freshness signal must be safe and must not imply mutation authority.

---

## 46. Stale Read Is Never Write Authority

A client may observe:

```text
stock available
appointment slot available
provider connected
```

and later attempt mutation.

The command still executes current runtime evaluation and authoritative revalidation.

---

# Collections, Filtering and Pagination

## 47. Bounded Collection Contract

Every potentially growing collection query SHALL define:

```text
maximum bounded page size
stable ordering basis
permitted filters
continuation semantics
scope/query affinity
```

The exact numeric page size is implementation/configuration scope unless business semantics require otherwise.

---

## 48. No Arbitrary Query DSL

Clients MUST NOT receive generic authority to construct:

```text
SQL-like expressions
arbitrary field paths
dynamic domain predicates
```

against internal data.

Supported filters are registered read-contract semantics.

---

## 49. Continuation Tokens

Where continuation tokens are used, they SHALL be:

```text
opaque to the client
bound to applicable query semantics
bound to Merchant/Audience scope where required
```

A continuation token:

```text
≠ authentication credential
≠ resource authority
≠ merchant scope
```

---

## 50. Cross-Scope Token Reuse

A continuation token produced for:

```text
Merchant A
```

MUST NOT expose or continue a collection under:

```text
Merchant B
```

merely because it is structurally valid.

---

# Time and Money

## 51. Time

Transport SHALL preserve the semantic distinction among:

```text
instant
local date
local time
zoned merchant/customer time
```

where the owning contract requires it.

No API field may silently discard required timezone semantics.

---

## 52. Money

Monetary contracts expose:

```text
amount
currency
```

under composite MS-PROT-055 Money semantics.

Floating-point transport semantics MUST NOT become monetary authority.

Exact JSON numeric/string representation remains implementation scope provided precision is preserved.

---

# Integration Callbacks

## 53. ApiCallbackContract

Each provider callback class SHALL have a registered ApiCallbackContract defining:

```text
provider/integration role
source-authentication method
correlation requirements
historical ProviderConnection/binding context where applicable
duplicate/replay identity
payload-validation rule
evidence owner
processing mode
acknowledgement semantics
safe rejection semantics
```

---

## 54. Callback Authentication

A valid callback signature or integration credential establishes:

```text
trusted source/integrity evidence
```

It does not establish:

```text
payment settled
shipment delivered
notification read
return accepted
calendar truth
```

Provider evidence remains subject to owner-qualified interpretation.

---

## 55. Callback Scope

Callback merchant/provider scope SHALL be established from trusted registered integration context and historical correlation.

A callback body field such as:

```text
merchantId
```

is not independently sufficient to select Merchant Scope.

---

## 56. Callback Acknowledgement

Transport acknowledgement means only the exact registered ingress responsibility has been accepted/processed according to its contract.

It MUST NOT mean:

```text
provider business claim accepted as Main Street truth
```

unless the owner interpretation actually established that fact.

---

## 57. Durable Callback Acceptance

Where callback processing continues asynchronously, Main Street MUST establish sufficient durable evidence before acknowledging acceptance such that provider retry is not required merely to preserve the callback.

---

## 58. Duplicate Callback

A duplicate callback correlated to the same provider effect SHALL converge on the existing logical provider evidence/processing responsibility.

It MUST NOT create:

```text
new Order
new Payment
new Shipment
new Notification Intent
```

solely because the provider delivered the message again.

---

## 59. Unknown or Conflicting Callback

An authenticated callback that cannot be correlated or conflicts with current evidence remains an explicit:

```text
unresolved / reconciliation condition
```

where the applicable provider contract requires it.

The integration transport does not choose business truth.

---

# Media Transport

## 60. Media Transfer Is Not Ordinary Business JSON

Large media bytes need not traverse normal business JSON endpoints.

The production API SHALL support bounded media-transfer coordination consistent with MS-PROT-066.

---

## 61. Upload Authority

A Media upload begins from bounded Main Street authority establishing at least:

```text
Merchant/actor scope
permitted media role/context
applicable size/type/profile constraints
bounded transfer identity
expiry/lifetime
storage destination authority
```

The client does not obtain arbitrary storage write authority.

---

## 62. Direct Upload

A direct client-to-storage mechanism MAY be used.

Canonical:

```text
Main Street authorises bounded upload
        ↓
client transfers bytes
        ↓
Main Street confirms/validates source
        ↓
MediaAsset technical lifecycle
        ↓
capability owner establishes attachment
```

---

## 63. Upload Completion Is Not Business Attachment

Hard separation:

```text
bytes uploaded
    ≠ MediaAsset validated
    ≠ business attachment established
    ≠ public Exposure granted
```

---

## 64. Media Download

Protected media access SHALL derive from:

```text
current principal/context
+
Merchant Scope
+
business relationship where applicable
+
Exposure/access authority
```

A storage URL itself is not access authority.

---

## 65. Bounded Delivery References

Signed/temporary delivery references MAY be used.

They must be:

```text
bounded in authority
bounded in lifetime where required
compatible with applicable Exposure
```

and MUST NOT redefine MediaAsset identity.

---

# Authentication / Session Transport

## 66. ADR-014 Is Not Reopened

Target 20 does not redesign:

```text
WebAuthn/passkey-first authentication
opaque server-authoritative sessions
protected host-bound cookies
CSRF/XSS controls
step-up authentication
```

Those remain governed by ADR-014 and MS-PROT-063.

The API consumes the resulting Trusted Execution Context.

---

## 67. Client Identity Headers Are Not Authentication

Ordinary clients MUST NOT establish authenticated identity using arbitrary headers such as:

```text
X-User-Id
X-Staff-Id
X-Merchant-Id
```

unless a separately authenticated trusted internal/integration boundary explicitly defines such evidence.

---

# Resource Protection

## 68. API Route Is Not ProtectionTarget

MS-PROT-073 remains authoritative:

```text
ProtectionTarget
    ≠ URL
    ≠ HTTP method
    ≠ controller
```

Transport invokes applicable registered Platform Resource Protection.

---

## 69. RATE_LIMITED

Where resource admission rejects an attempt:

```text
RATE_LIMITED
```

may be exposed safely.

This says:

```text
platform resource admission denied now
```

not:

```text
operation semantically invalid
merchant not entitled
actor unauthorised
business rejected
```

---

# Initial Production API Portfolio

## 70. Portfolio Principle

Target 20 SHALL NOT expose every internal capability operation merely because it exists.

The initial production portfolio includes only contracts needed by:

```text
first-party merchant operation
first-party public/storefront interaction
customer/guest contextual interaction
required provider integrations
required platform operational administration
```

Each concrete endpoint must map to an accepted owner operation/query.

---

## 71. PLATFORM_IDENTITY_BOOTSTRAP Portfolio

The initial platform/bootstrap family SHALL support the required contracts for:

```text
authentication ceremony
session establishment / termination
current authenticated-context establishment
Merchant Account establishment
initial Controller/bootstrap flow
merchant-context selection where one identity
has multiple merchant relationships
```

Exact WebAuthn/browser payload formats remain ADR-014 implementation scope.

---

## 72. MERCHANT_OPERATIONAL — Definition and Administration

The initial merchant operational family SHALL support accepted contracts for:

```text
Onboarding Case
    open/read
    answer/correct
    review
    submit Initial Configuration Intent

Configuration
    inspect pending/active revision
    approve/activate where authorised

Merchant Profile / Location / Business Hours
    read
    create/revise
    retire where applicable
    manage accepted public Exposure choices

Workforce
    invitation
    membership administration
    group/role assignment
    bounded operational-device administration

Provider Connections
    establish/manage/re-authorise
    observe safe readiness/attention projection

Media
    upload authority
    upload confirmation
    attach/detach through owning capability
```

Only accepted owner operations may be exposed.

---

## 73. MERCHANT_OPERATIONAL — Content and Interaction

The initial portfolio SHALL support applicable owner contracts for:

```text
Publication
    create
    revise
    publish
    withdraw
    query

Enquiry
    list/read
    merchant-safe attention/communication operations
    where already accepted

AI assistance
    invoke registered merchant-side
    inference/assistance contracts
    without arbitrary semantic execution
```

---

## 74. MERCHANT_OPERATIONAL — Scheduling

The initial merchant portfolio SHALL expose applicable:

```text
Booking reads/operations
Appointment reads/operations
Scheduling evaluation
merchant-assisted appointment/booking creation
authorised cancellation/rescheduling
where accepted
Calendar projection
```

The API SHALL NOT merge Booking, Appointment and Calendar into one transport-owned scheduling state.

---

## 75. MERCHANT_OPERATIONAL — Ordering / Inventory

The initial portfolio SHALL expose accepted owner operations required for:

```text
Order observation
Order commitment/amendment/release
Inventory Position observation
authorised stock adjustment/transfer
Inventory Claim consequence where directly
merchant-operable
returned-stock receipt
returned-stock disposition
```

The API does not expose raw:

```text
quantity field overwrite
order status overwrite
inventory row update
```

---

## 76. MERCHANT_OPERATIONAL — Payment

The merchant operational surface SHALL expose only applicable Payment-owned commands/queries such as:

```text
Payment Obligation observation
authorised payment execution initiation
where merchant-mediated
authorised Refund request
Payment/Refund evidence-safe observation
uncertainty/pending projection
```

A route named `/refund` does not itself establish Refund permission.

---

## 77. MERCHANT_OPERATIONAL — Fulfilment / Shipment

The portfolio SHALL expose applicable contracts for:

```text
order-fulfilment.satisfy
Shipment preparation
redispatch where authorised
Shipment outcome recording where merchant-authoritative
customer-safe tracking information observation
```

Order Fulfilment and Shipment remain separate authorities.

---

## 78. MERCHANT_OPERATIONAL — Returns

Where Returns capability is applicable, merchant operational contracts may expose:

```text
structured Return Policy management
return-specific instructions
returns.return-label.prepare
```

Independently applicable merchant operations such as:

```text
Refund
replacement
Inventory returned-stock receipt
```

MUST NOT be hidden behind Returns applicability.

---

## 79. MERCHANT_OPERATIONAL — Notifications

The initial merchant surface may expose safe Notification-related queries/settings where accepted, including applicable:

```text
recipient/preference configuration
delivery evidence summaries
provider attention/readiness projection
```

It MUST NOT expose a raw:

```text
set delivered = true
```

operation.

---

# Public Portfolio

## 80. PUBLIC Read Portfolio

The initial public surface SHALL support applicable exposed projections for:

```text
Merchant Presence
public Business Hours
public Locations / Service Areas
Offerings / Products / catalogue
Publication / Opportunity
public media
public scheduling information/availability
other registered PUBLIC Surface contributions
```

Only EXPOSE results become public elements.

---

## 81. PUBLIC Mutation Portfolio

The initial public surface may expose registered operations including:

```text
Enquiry submission
public Booking/Appointment initiation
where merchant configuration permits it
guest Order creation
where applicable
payment initiation required by an accepted
public commercial operation
```

Each converges on the same owner operation used by other channels.

---

# Customer / Guest Portfolio

## 82. Related-Customer Reads

The customer-contextual portfolio SHALL support accepted relationship-bound contracts such as:

```text
related Booking
related Appointment
related Order
related Payment Obligation
Order Fulfilment / Shipment tracking
historically applicable Return policy/instructions
```

using the exact owner-qualified customer relationship predicates already accepted.

---

## 83. Customer Mutations

Only explicitly accepted customer operations are exposed.

Potential examples include:

```text
cancellation/rescheduling
where accepted
customer-authorised contextual updates
applicable payment continuation
```

Target 20 does not invent:

```text
self-service return approval
automatic refund
universal order cancellation
```

where owner semantics do not already authorise them.

---

# Integration Portfolio

## 84. Initial Provider Callback Families

The initial INTEGRATION_INGRESS portfolio SHALL support applicable callback contracts for the concrete providers eventually implemented under accepted roles including:

```text
Payment execution/refund evidence
Notification delivery evidence
Shipment preparation/tracking evidence
Return-label evidence
external Calendar/scheduling evidence
provider authorisation/connection callbacks
```

Only implemented provider roles need physical callback endpoints.

---

# Platform Administrative Portfolio

## 85. Operational Administration

The initial platform-administrative API SHALL expose only registered actions required to operate the accepted production system.

Candidate required families include:

```text
manual reconciliation intervention
uncertain-provider-effect investigation
registered provider/connection remediation
semantic/deployment admission observation
registered exceptional security/account operations
operational status observation
```

---

## 86. No Generic Force Endpoint

Rejected:

```text
POST /admin/force-status
POST /admin/update-row
POST /admin/mark-success
POST /admin/run-sql
```

as ordinary production API authority.

Administrative mutation must still invoke the owning accepted operation.

---

# Reconciliation API

## 87. Reconciliation Observation

Operational APIs may expose:

```text
exact reconciliation responsibility
age
evidence-acquisition state
safe uncertainty classification
manual-intervention requirement
```

according to MS-PROT-068/069.

---

## 88. Reconciliation Mutation

Manual reconciliation transport invokes:

```text
registered owner-qualified intervention
```

It cannot offer:

```text
reconciled = true
```

as an independent transport mutation.

---

# Compatibility

## 89. Contract Identity Is Not URI Version

A stable API contract may be mapped to different physical routing over time without changing semantic identity.

Likewise:

```text
/v1
```

in a URI is not sufficient compatibility governance.

---

## 90. Coordinated First-Party Contracts

Merchant-web/storefront-web and backend may initially evolve together where deployment coordination is guaranteed.

Breaking coordinated changes still require:

```text
contract-aware implementation update
+
tests
+
coordinated deployment
```

They need not receive a permanent public version namespace merely because they are HTTP APIs.

---

## 91. Published External Contracts

Any contract consumed independently by external clients/providers requires explicit compatibility handling.

Breaking change requires one of:

```text
new compatible contract revision
parallel compatibility period
explicit migration
new endpoint/contract identity
```

It MUST NOT silently change the meaning of an existing field.

---

## 92. API Contract Evolution Does Not Rewrite Domain Semantics

API versioning is a delivery concern.

A new API contract cannot:

```text
rename one business concept
and thereby change its semantic owner

merge two accepted domain concepts

invent new permission

change historical commitment meaning
```

---

# Real-Time Transport

## 93. Polling / SSE / WebSocket

Target 20 does not require one real-time transport.

Where introduced:

```text
polling
SSE
WebSocket
```

delivers query/projection information.

It does not create a new authoritative mutation channel.

---

## 94. Pushed Data Is Still a Projection

A pushed message informing a client:

```text
Order changed
```

is not authority for a subsequent mutation.

The client must still execute the appropriate Command against current authority.

---

# Falsification

## 95. Client Sends Merchant B in Body While Authenticated for Merchant A

Expected:

```text
body value does not switch scope
trusted Merchant Scope remains Merchant A
cross-scope access rejected
```

**PASS**

---

## 96. Staff Logs In from Personal Device

Where merchant operational access requires authorised device context:

```text
identity valid
+
membership valid
+
operational-device requirement unsatisfied
        ↓
merchant operational data not exposed
```

**PASS**

---

## 97. Customer Guesses Another Order ID

Expected:

```text
identifier locates candidate only
relationship proof fails
external result:
NOT_FOUND_OR_NOT_ACCESSIBLE
```

**PASS**

---

## 98. Public Element Is WITHHOLD

Expected:

```text
element omitted / resource safely hidden
```

not:

```text
"secret field exists but you cannot see it"
```

**PASS**

---

## 99. Projection Is Unavailable

Expected:

```text
REPRESENTATION_UNAVAILABLE
or accepted reduced representation
```

according to Projection Contract.

It is not represented internally as Exposure WITHHOLD.

**PASS**

---

## 100. Customer Saw Available Slot; Merchant Books It First

Expected:

```text
public projection was not mutation authority
appointment command revalidates
CONFLICT
```

No double booking.

**PASS**

---

## 101. Order Request Times Out After Commit

Client retries using same logical retry identity.

Expected:

```text
existing accepted Order result recovered
```

not second Order.

**PASS**

---

## 102. Same Idempotency Token, Different Order

Expected:

```text
IDEMPOTENCY_CONFLICT
```

No payload replacement.

**PASS**

---

## 103. Payment Provider Timeout

Expected external classification preserves:

```text
OUTCOME_UNCERTAIN
```

or a safe equivalent.

Client is not instructed to create a second fresh payment.

**PASS**

---

## 104. Valid Webhook Signature, Wrong Correlation

Expected:

```text
trusted source
≠ trusted business fact
correlation/reconciliation required
```

**PASS**

---

## 105. Duplicate Provider Callback

Expected:

```text
same provider-effect processing identity
```

No duplicated Payment/Shipment/Notification fact.

**PASS**

---

## 106. Async Command Accepted

If the owner contract permits asynchronous logical completion:

```text
ACCEPTED_PENDING
```

means responsibility accepted.

It does not claim completed business outcome.

**PASS**

---

## 107. Order Completed but Notification Pending

Expected:

```text
Order command = COMPLETED
Notification responsibility progresses independently
```

The API does not report the Order itself as pending.

**PASS**

---

## 108. Pagination Token Replayed in Another Merchant

Expected:

```text
token rejected / cannot cross Merchant Scope
```

**PASS**

---

## 109. Media Bytes Uploaded

Expected:

```text
upload complete
≠ validated MediaAsset
≠ business attachment
≠ public Exposure
```

**PASS**

---

## 110. Operator Wants to Clear Payment Reconciliation

Expected:

```text
no force-reconciled endpoint
owner-qualified Payment/reconciliation operation required
```

**PASS**

---

## 111. Protection Limit Reached

Expected:

```text
RATE_LIMITED
```

without:

```text
Merchant Configuration mutation
Entitlement loss
business rejection fact
```

**PASS**

---

# Rejected Alternatives

## 112. Rejected Designs

Target 20 rejects:

```text
one undifferentiated API for every audience
separate staff business model
guest = public authority
client merchantId = Merchant Scope
resource ID = access
DTO = domain aggregate
API path = semantic operation identity
HTTP status = business state
generic CRUD over domain aggregates
generic PATCH status endpoints
universal dynamic /execute endpoint
one idempotency key namespace across platform
request ID = Command identity
stale projection = mutation authority
WITHHOLD = projection failure
projection failure = WITHHOLD
provider signature = business truth
webhook delivery = new business command
upload completion = media attachment
admin UI = invariant bypass
rate limit = entitlement loss
API version = semantic-release identity
```

---

# Deferred / Implementation Scope

## 113. Deferred

Target 20 deliberately does not select:

```text
exact REST URI naming
exact HTTP status mapping
exact JSON property naming
Jackson configuration
OpenAPI tooling/generator
exact error-message wording/localisation
exact request/correlation header names
exact idempotency header name
exact idempotency persistence implementation
exact ETag vs body revision representation
exact pagination-token encoding
cursor vs offset physical implementation
where both preserve the registered contract
numeric page-size defaults/maximums
API gateway product
BFF framework
CORS configuration
exact CSRF implementation
subject to ADR-014
cookie implementation details
subject to ADR-014
SSE vs WebSocket
media multipart vs presigned upload protocol
object-storage provider
signed-URL mechanism
exact external URI-version syntax
provider-specific webhook routes
provider-specific callback payload mapping
GraphQL introduction
exact controller/package layout
exact API documentation portal
client SDK generation
```

These choices must conform to this authority.

---

# Conformance

## 114. Target-20 Conformance Criteria

A conforming production transport must prove:

```text
[ ] every endpoint maps to a registered API Contract
[ ] API contract identity is not derived from route/controller naming
[ ] every request enters through one explicit surface class
[ ] Merchant Scope is server-established from trusted context
[ ] client identifiers do not grant access
[ ] staff uses merchant operational semantics under staff authority
    rather than a duplicate staff business API
[ ] guest contextual access remains distinct from public authority
[ ] bootstrap/platform operations do not manufacture fake Merchant Scope
[ ] platform-admin mutation uses registered owner operations
[ ] transport and semantic validation remain separate
[ ] duplicate-sensitive commands preserve logical retry identity
[ ] idempotency tokens are scope/contract-affined
[ ] token reuse with different intent conflicts
[ ] revision-sensitive mutations expose expected revision/precondition
[ ] stale revisions do not overwrite current authority
[ ] COMPLETED, ACCEPTED_PENDING, REJECTED and OUTCOME_UNCERTAIN
    cannot be conflated
[ ] provider uncertainty does not become blind client retry
[ ] stable problem categories preserve owner rejection meaning
[ ] security may collapse forbidden/existence detail externally
[ ] internal exceptions do not become API contracts
[ ] query responses consume Projection Serviceability and Exposure
[ ] WITHHOLD does not become projection-unavailable
[ ] stale/read projections never become mutation authority
[ ] growing collections remain bounded
[ ] continuation state cannot cross trusted scope
[ ] callback source, scope and correlation are independently established
[ ] valid callback authentication does not create business truth
[ ] duplicate callbacks do not multiply effects
[ ] media upload does not imply attachment or Exposure
[ ] rate limiting does not become business/entitlement authority
[ ] API compatibility changes cannot rewrite domain meaning
[ ] exact routes/frameworks/header names remain replaceable
```

---

# 115. Target-20 Closure Effect

With this amendment accepted:

```text
API architectural principle
    → composite MS-PROT-035 through v1.1

trusted principal/session
    → composite MS-PROT-063 + ADR-014

Merchant Scope
    → MS-PROT-031

runtime command decision
    → composite MS-PROT-062

idempotency
    → MS-PROT-059 + owner Operation Contract

Projection / Exposure
    → composite MS-PROT-027

provider callbacks
    → composite MS-PROT-048
      + applicable provider/capability authority

media transfer
    → composite MS-PROT-066

resource protection
    → MS-PROT-073

reconciliation
    → composite MS-PROT-069

operational evidence
    → composite MS-PROT-068
```

No material Target-20 API semantic decision remains for implementation to invent.

Subject to governance propagation:

```text
Target 20 — Production APIs
    DESIGN-CLOSED

Target 21 — Backup / restore / disaster recovery
    CURRENT ACTIVE TARGET
```

---

# 116. Governing Principle

> **Main Street’s production APIs are explicit audience- and owner-qualified transport contracts over accepted application operations and projections. They establish trusted context before execution, preserve semantic idempotency and concurrency without turning headers or request IDs into business identity, expose uncertainty without encouraging duplicate effects, consume Projection and Exposure rather than reimplementing them, treat provider callbacks as evidence ingress rather than business truth, and remain replaceable delivery adapters rather than a new semantic owner.**

---

# 117. Acceptance Statement

MS-PROT-035 v1.1 completes the production transport boundary for the Main Street backend without designing endpoints around tables, business categories, frontend screens or framework conventions.

The authority is intentionally precise about:

```text
what each API contract means
which trust surface it belongs to
which semantic owner it invokes
how retry/concurrency work
how reads/exposure work
how callbacks/media work
how uncertainty is represented
```

while deliberately leaving:

```text
URI spelling
HTTP library details
JSON naming
OpenAPI tooling
header names
pagination encoding
gateway technology
media transport mechanism
```

to implementation where those choices cannot alter accepted semantics.

**Status: ACCEPTED**
