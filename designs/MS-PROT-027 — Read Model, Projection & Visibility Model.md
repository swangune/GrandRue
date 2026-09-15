# MS-PROT-027 — Read Model, Projection & Visibility Model

**Version:** 1.1  
**Status:** **Accepted**  
**Depends on:** MS-PROT-020 → MS-PROT-026  
**Purpose:** Define how authoritative Main Street state becomes merchant-facing, customer-facing and public information without allowing presentation requirements to distort the underlying domain model.

## 1. Governing principle

> **Authoritative state describes what is true; projections determine how that truth is represented to a particular audience.**

```text
Authoritative operational state
             ↓
      projection policy
             ↓
 ┌───────────┼───────────┐
 ▼           ▼           ▼
Merchant   Customer     Public
```

A projection is not a second source of business truth.

## 2. Operational truth and audience views

The same operational fact may produce different audience views.

Example:

```text
Internal job state: inspection_required
Merchant view: Inspection required
Customer view: We are reviewing your request
Public view: not exposed
```

Therefore:

```text
Operational state ≠ customer status ≠ presentation label
```

Presentation vocabulary must not be pushed back into core `State` definitions merely because a UI needs different wording.

## 3. Projection categories

Main Street supports conceptually distinct projections:

- **Merchant operational projection** — work, resources, orders, bookings, enquiries, content, etc.
- **Customer contextual projection** — information related to a specific customer's interaction/relationship.
- **Public projection** — information intentionally published to anyone.
- **Derived analytical/search projection** — optimised read representations that may be eventually consistent.

These may use different storage/read models while remaining derived from authoritative semantics/state.

## 4. Exposure policy

Exposure answers:

> **Which facts/attributes may this audience observe in this context?**

It is distinct from:

```text
Notification  = proactively communicate something
Authorisation = may the actor perform an action?
Projection    = shape/read representation
```

An event can exist without being customer-visible. A customer-visible fact can be displayed without generating a new event.

## 5. Customer accounts are optional

A customer relationship does not require a global Main Street account.

Supported interaction patterns may include:

```text
anonymous public browsing
guest enquiry
secure contextual link
transaction-specific tracking
authenticated customer portal
```

Examples:

- Gardener: enquiry/contact without account.
- Solicitor: appointment information via email/ICS without portal.
- Driving instructor: persistent account/portal may be useful for schedule and history.

The projection model must support all without duplicating domain semantics.

## 6. Merchant projection

Merchant-facing reads should expose business concepts rather than internal semantic machinery.

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

not:

```text
Capabilities
Semantic Operations
Allocation Models
Requirement Graph
```

The merchant dashboard is a projection over the active merchant configuration, authority and current operational state.

## 7. Public projection

Public information may include, where configured:

```text
merchant identity/content
offerings/services/products
publications/opportunities
categories/classification
opening hours/location where relevant
portfolio/showcase
public availability summaries
contact/enquiry entry points
booking/order/viewing initiation
external application/action links
subscription entry points
```

Physical location is optional context, not a prerequisite for public projection.

## 8. Information-publisher validation

An information publisher can operate primarily through projection rather than transaction:

```text
OpportunityPublished
        ↓
Public opportunity page
Search/category projection
Deadline projection
External application link
Optional subscriber notification
```

No Booking, Payment, Inventory, Allocation, Ordering or Scheduling read model is required unless the merchant actually enables those semantics.

**PASS**

## 9. Online-consultant validation

An online consultant may expose:

```text
Services
Consultation information
Available booking entry point
Enquiry/contact
Optional payment
```

Scheduling may be backed by Main Street or an external integration, but the public/customer projection remains business-semantic rather than provider-specific.

**PASS**

## 10. Read models and consistency

A projection may lag authoritative state where no invariant depends on immediate consistency.

Examples:

```text
search index
analytics
customer history summary
dashboard counts
public catalogue cache
```

But projection data must not authorise concurrency-sensitive writes.

```text
Search says "room available"
        ≠
AllocateRoom authorised
```

Write operations revalidate against authoritative state.

## 11. Projection provenance

Where material, Main Street should be able to trace a projected field/status to the underlying authoritative fact/configuration/exposure rule that produced it. This supports debugging and prevents presentation logic from becoming hidden semantic authority.

## 12. Privacy and scope

Projection must respect:

```text
merchant scope
actor/customer relationship
exposure policy
sensitivity
purpose
```

A public projection cannot expose information merely because it exists internally. A customer must not receive another customer's transaction information because identifiers happen to be guessable.

## 13. Falsification findings

Rejected assumptions:

| Failed assumption | Why it fails |
|---|---|
| Operational state should equal customer status | Internal state vocabulary may be inappropriate or sensitive |
| Every customer needs an account | Many interactions are guest/contextual |
| Projection may become authoritative write state | Read models may lag |
| Event existence implies customer visibility | Events may be internal |
| Notification and exposure are the same | One is delivery; one is visibility |
| All merchants need commerce projections | Publishers/consultants may not |
| Public surface requires physical premises | Online/information merchants disprove it |

## 14. Accepted invariants

1. Authoritative state remains the source of operational truth.
2. Audience projection cannot redefine domain semantics.
3. Merchant, customer and public views may legitimately differ.
4. Exposure, notification and authorisation remain distinct.
5. Customer accounts are optional.
6. Projection/read models may be eventually consistent where safe.
7. Stale projections cannot authorise authoritative mutation.
8. Public and customer projections must respect semantic scope/privacy.
9. Information publishing is a first-class projection model, not a commerce special case.
10. Physical premises are optional context, not projection eligibility.

## 15. Deferred decisions

Specific query APIs, cache/index technology, projection storage, UI component implementation and detailed exposure-rule representation remain downstream decisions.

## Governance verdict

**ACCEPTED.** The 20 August 2026 handover expands the validation universe to information publishers and online consultants without changing the projection architecture.
