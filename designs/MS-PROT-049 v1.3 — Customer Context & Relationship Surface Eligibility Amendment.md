# MS-PROT-049 v1.3 — Customer Context & Relationship Surface Eligibility Amendment

**Document ID:** MS-PROT-049  
**Version:** 1.3  
**Status:** **ACCEPTED by manual approval on 26 August 2026**  
**Amends:** MS-PROT-049 v1.0-v1.2 within CUSTOMER Surface eligibility scope  
**Depends on:** MS-PROT-027 v1.2-v1.3, MS-PROT-043 v1.2-v1.3, MS-PROT-049 v1.0-v1.2, MS-PROT-056 v1.5, MS-PROT-062, MS-PROT-063, MS-PROT-077 and applicable capability-owned relationship authorities  
**Approved:** Manual approval on 26 August 2026 after governed in-chat proposal, authority review, cross-domain falsification, second-order falsification, alternatives review and recommendation under `DESIGN-RULES.md`  
**Purpose:** Define how a registered `CUSTOMER` Surface Contribution becomes contextually eligible for one merchant-scoped customer interaction without making CustomerAccount authentication, CustomerContext existence, client-provided identifiers, Surface, Exposure, presentation or AI inference the owner of customer-business relationship truth.

---

## 1. Governing decision

A `CUSTOMER` Surface Contribution is a candidate customer-facing contribution. It MUST NOT become contextually eligible solely because its owning capability is active, because a CustomerAccount is authenticated, because a CustomerContext exists, or because a client possesses an object/reference identifier.

> **A relationship-bound CUSTOMER Surface Contribution becomes contextually eligible only when its registered owner-qualified Customer Surface Eligibility Requirement is satisfied from current trusted merchant-scoped relationship/context evidence.**

The capability or accepted context authority that owns the underlying relationship remains authoritative. Surface owns only the composition decision that the already-registered contribution participates in the current customer experience.

Canonical separation:

```text
REGISTERED CUSTOMER SURFACE CONTRIBUTION
        +
REGISTERED OWNER-QUALIFIED
CUSTOMER SURFACE ELIGIBILITY REQUIREMENT
        ↓
CURRENT MERCHANT SCOPE
        +
TRUSTED EXECUTION / ACCESS CONTEXT
        +
CAPABILITY / CONTEXT-OWNED
RELATIONSHIP EVIDENCE
        ↓
CUSTOMER SURFACE ELIGIBILITY
        ↓
PROJECTION SERVICEABILITY
        ↓
EXPOSURE
        ↓
CUSTOMER-FACING SURFACE
        ↓
CUSTOMER INTENT
        ↓
RUNTIME AUTHORISATION / ELIGIBILITY
        ↓
CAPABILITY-OWNED OPERATION
```

Hard distinction:

```text
Customer Surface Eligibility
    ≠ CustomerAccount authentication
    ≠ CustomerContext existence
    ≠ Exposure
    ≠ Projection Serviceability
    ≠ operation authorisation
    ≠ Operational Eligibility
    ≠ mutation authority
```

---

## 2. Scope

This amendment governs only the missing contextual membership contract for `CUSTOMER` Surface Contributions.

It defines:

- the `Customer Surface Eligibility Requirement` concept;
- owner-qualified requirement identity;
- the static requirement versus runtime-satisfaction boundary;
- use of current merchant-scoped trusted context and authoritative relationship evidence;
- fail-closed behaviour when relationship authority/evidence is absent or unresolved;
- the distinction between CustomerAccount, CustomerContext, contextual access and customer Surface eligibility;
- guest/transaction-specific contextual access compatibility;
- cross-merchant isolation;
- stale/revoked context consequences;
- CustomerContext reconciliation boundary;
- residual customer Surface participation for existing commitments where already required by accepted residual authority;
- independence between unrelated CUSTOMER contributions; and
- the separation of Surface eligibility from Projection Serviceability, Exposure and execution authority.

---

## 3. Explicit non-goals

This amendment does **not** define:

- a global `Customer` identity primitive;
- CustomerContext lifecycle or reconciliation semantics;
- CustomerAccount authentication technology;
- secure-link/token encoding or signing technology;
- account registration UX;
- a universal Customer Portal domain object;
- one fixed customer dashboard/workspace structure;
- a universal `CustomerStatus` lifecycle;
- guardian/representative/delegate semantics where no accepted authority yet exists;
- one universal customer-to-Booking/Appointment/Order relationship type;
- a merchant-authored or AI-authored eligibility rules DSL;
- Projection formatting, cache/index technology or freshness policy;
- Exposure policy;
- operation authorisation, cancellation/amendment policy or Operational Eligibility;
- payment/provider semantics;
- business-category customer templates; or
- exact Java class/interface or HTTP route shapes.

Existing authorities remain governing for those concerns.

---

## 4. Audience and relationship distinction

`CUSTOMER` is a Surface audience. It is not synonymous with `CustomerAccount`.

The accepted customer model remains:

```text
Visitor
    transient public interaction context

CustomerContext
    merchant-scoped durable customer-specific business relationship

CustomerAccount
    identity/access construct
```

Therefore:

```text
CustomerAccount
    can exist without a qualifying relationship to Merchant M

CustomerContext
    can exist without CustomerAccount

transaction-specific contextual access
    can establish narrow customer-safe access without CustomerAccount
```

A person MAY therefore interact with Main Street as:

- a public Visitor with no customer-specific Surface;
- a guest customer with narrow contextual access to an existing commitment;
- an authenticated CustomerAccount linked to one or more merchant-scoped CustomerContexts; or
- another future customer context accepted by a separate governing authority.

Surface MUST NOT collapse these into one generic `loggedInCustomer` state.

---

## 5. Customer Surface Eligibility Requirement

A **Customer Surface Eligibility Requirement** is:

> **A registered, owner-qualified static requirement referenced by a CUSTOMER Surface Contribution, identifying the accepted relationship/context predicate that must be authoritatively satisfied before that contribution may participate in the current customer Surface.**

Conceptual identity:

```text
CustomerSurfaceEligibilityRequirementIdentity
{
    ownerIdentifier
    requirementIdentifier
}
```

The structure is conceptual and does not mandate one Java type.

Requirement identity MUST be owner-qualified because shared words such as `RELATED_CUSTOMER` MUST NOT imply that Booking, Appointment, Ordering, Conversation or another capability share one universal customer relationship authority.

Illustrative identities may resemble:

```text
booking / related-customer-booking
appointment / related-customer-appointment
ordering / customer-order-context
```

These examples do not reserve canonical identifiers and MUST NOT be implemented until the applicable owning capability has an accepted relationship contract.

---

## 6. Requirement semantic classification

A Customer Surface Eligibility Requirement is:

```text
registered semantic definition
release-affined / static reference
non-Operational Object
non-Domain Event
non-business commitment
non-authentication credential
non-Exposure decision
non-execution permission
```

It has no independent business lifecycle.

Its definition/reference MAY be included in registered semantic/Surface definitions and exact release materialisation according to existing semantic-release architecture.

Current satisfaction is runtime/contextual and MUST NOT be stored as immutable Merchant Configuration or Resolved Configuration Package truth.

---

## 7. Static versus runtime boundary

Static definition may establish:

```text
CUSTOMER contribution C
    requires requirement R
```

Static configuration MUST NOT establish live facts such as:

```text
Alice owns Booking B101
CustomerContext C55 is authenticated now
secure token T is valid now
session S has not been revoked
```

Canonical boundary:

```text
STATIC / RELEASE-AFFINED

CUSTOMER contribution
        ↓
requires R
```

versus:

```text
CURRENT RUNTIME CONTEXT

MerchantScope
+
Trusted Execution / Access Context
+
current capability-owned relationship facts
+
current security/context validity
        ↓
is R satisfied now?
```

A current eligibility decision MUST NOT become a permanent cached access grant.

---

## 8. Single-owner relationship invariant

For every relationship-bound CUSTOMER contribution, the fact used to satisfy its eligibility requirement MUST have exactly one accepted owner.

Examples of possible owners include:

- Booking capability / accepted Booking-customer relationship semantics;
- Appointment capability / accepted Appointment-customer relationship semantics;
- Ordering / accepted purchaser or transaction-specific customer context semantics;
- Conversation / communication participant authority;
- Customer relationship authority under MS-PROT-043; or
- another separately accepted capability/context authority.

Surface MAY ask whether the requirement is satisfied.

Surface MUST NOT:

- manufacture the relationship;
- persist a competing customer-object relationship graph;
- infer it from presentation state;
- infer it from merchant category;
- infer it from subject type;
- infer it from contact-value similarity; or
- mutate the relationship.

Hard invariant:

> **Surface membership authority is not customer-business relationship authority.**

---

## 9. Authentication and CustomerAccount boundary

CustomerAccount authentication establishes identity/access evidence, not merchant relationship truth.

For relationship-bound merchant data:

```text
CustomerAccount authenticated
        ≠
Customer Surface eligibility
```

The current MerchantScope and applicable customer-business relationship MUST still be independently established.

Matching:

```text
email
phone
name
address
```

MUST NOT establish eligibility.

An authenticated CustomerAccount MAY participate in satisfying a requirement where an accepted authority links that account/trusted identity to the applicable merchant-scoped CustomerContext or transaction relationship.

---

## 10. Guest and transaction-specific contextual access

A CustomerAccount is not universally required.

Where MS-PROT-063 or another accepted authority establishes valid transaction-specific contextual access, that trusted context MAY satisfy a narrowly scoped Customer Surface Eligibility Requirement.

Example:

```text
secure guest Booking access proof
        ↓
validate proof
        ↓
Merchant M
Booking B101
bounded customer-safe access context
        ↓
applicable Booking customer requirement satisfied
```

The valid proof MUST NOT be promoted into:

```text
global CustomerAccount
broad merchant-customer identity
authority for unrelated Orders/Appointments/Bookings
```

A secure Booking context for B101 MUST NOT by itself expose B102 or another capability's customer history.

---

## 11. Client input is not relationship evidence

The following MUST NOT become trusted merely because a client supplies them:

```text
customerId
CustomerContext identifier
CustomerAccount identifier
bookingId
appointmentId
orderId
resourceId
role
relationship=true
merchantId
```

They may be locators/input references only.

Trusted application boundaries MUST independently resolve MerchantScope, current context, relationship evidence and requirement satisfaction.

Possession of an opaque or globally unique identifier grants no Customer Surface eligibility.

---

## 12. Fail-closed requirement resolution

Relationship-bound CUSTOMER contribution eligibility MUST fail closed when any required authority cannot be established.

At minimum:

```text
missing requirement definition
missing requirement owner/evaluator
unknown relationship
wrong merchant scope
expired contextual proof
revoked/invalid session where required
forged resource reference
weak contact match only
unresolved guardian/delegate relationship
cross-merchant mismatch
security-context failure
```

MUST result in no eligibility for the affected contribution.

Main Street MUST NOT fall back to:

```text
merchant category
email equality
frontend route
component identity
presentation state
AI inference
provider output
```

---

## 13. Surface eligibility, Projection Serviceability and Exposure

These are independent decisions.

Customer Surface Eligibility asks:

> **Does this registered contribution belong in the current customer relationship/context?**

Projection Serviceability asks:

> **Can the required customer-safe read representation currently be produced according to its governing Projection Contract?**

Exposure asks:

> **May the current audience/context observe the resulting semantic element?**

Canonical order:

```text
registered CUSTOMER contribution
        ↓
Customer Surface Eligibility
        ↓
Projection Serviceability
        ↓
projection elements
        ↓
Exposure
        ↓
customer-visible representation
```

Exposure MUST NOT manufacture Surface membership, consistent with MS-PROT-027.

Eligibility MUST NOT imply Exposure of every field in the underlying object.

Example:

```text
related customer Booking surface eligible
        ↓
Booking date/service   EXPOSE
internal notes         WITHHOLD
internal Allocation    WITHHOLD
```

---

## 14. Surface visibility is not execution authority

Rendering a customer action MUST NOT grant authority to perform that action.

Example:

```text
customer Booking surface
        ↓
"Cancel booking" presentation
        ↓
customer intent
        ↓
current Trusted Execution Context
+
Actor Authorisation
+
Booking policy
+
Operational Eligibility
+
applicable residual/entitlement/trust decisions
        ↓
capability-owned cancellation operation
```

A stale Surface or previously satisfied relationship requirement MUST NOT authorise mutation.

Execution re-enters current runtime authority under MS-PROT-062 and the owning capability.

---

## 15. CustomerContext is not a universal access ticket

CustomerContext remains a merchant-scoped durable business relationship anchor under MS-PROT-043.

Its existence MUST NOT automatically make every CUSTOMER contribution eligible.

Example:

```text
CustomerContext C1
    related to Appointment A1
```

MUST NOT imply:

```text
Order CUSTOMER contribution eligible
Booking CUSTOMER contribution eligible
all Conversation history exposed
```

unless the applicable accepted relationship requirements are independently satisfied.

Each relationship-bound CUSTOMER contribution MUST use the requirement appropriate to its semantic meaning.

---

## 16. CustomerContext reconciliation boundary

MS-PROT-043 v1.3 remains authoritative for non-destructive merchant-scoped CustomerContext reconciliation.

A relationship such as:

```text
C2 RECONCILED_TO C1
```

MUST NOT, by this amendment alone, broaden customer-facing access from C1 to every object historically attached to C2.

Reconciliation may support merchant-facing unified history and future association according to MS-PROT-043. Customer-facing access expansion requires an accepted relationship/access authority that explicitly establishes the consequence.

Therefore:

```text
CustomerContext reconciliation
    ≠ automatic customer observation delegation
```

---

## 17. Residual existing-commitment access

Loss of new-activity capability access, Commercial Entitlement or equivalent current activity permission MUST NOT erase an existing customer commitment.

Where an accepted residual capability/commercial/account authority requires continued customer access to view, fulfil, manage or resolve an existing commitment, a registered residual CUSTOMER contribution MAY remain eligible when its current relationship requirement is satisfied.

Example:

```text
Booking disabled for NEW activity
        +
existing Booking B101 remains
        +
related customer context established
        ↓
required residual Booking customer surface may remain
```

This MUST NOT imply:

```text
new Booking activity reactivated
commercial entitlement restored
capability configuration rewritten
```

The residual authority owns why access survives; Surface only composes the required contribution.

---

## 18. Independent contribution failure

One relationship or projection failure MUST NOT erase unrelated independently legitimate CUSTOMER contributions.

Example:

```text
Booking projection not serviceable
        +
Conversation projection serviceable
        ↓
Booking-dependent contribution withheld/degraded
Conversation contribution may remain
```

A generic `customer surface available = false` flag MUST NOT collapse independent contribution responsibilities.

---

## 19. Business-type neutrality

Business category MUST NOT determine CUSTOMER Surface eligibility.

Rejected:

```text
MOTEL
    → My Booking

GARDENER
    → My Appointment
```

Accepted:

```text
registered Booking CUSTOMER contribution
+
qualifying Booking relationship/context
    → customer Booking surface

registered Appointment CUSTOMER contribution
+
qualifying Appointment relationship/context
    → customer Appointment surface
```

A showcase-only gardener, information publisher or other merchant with no qualifying customer-specific relationship remains valid without a customer portal/workspace.

---

## 20. Information-only and one-off interaction boundary

Public reading and one-off Enquiry MUST NOT manufacture persistent customer Surface membership merely to create a familiar portal pattern.

Where no CustomerContext or other accepted durable customer relationship is required:

```text
PUBLIC interaction
        ↓
no qualifying CUSTOMER relationship requirement
        ↓
no relationship-bound CUSTOMER Surface
```

An Enquiry remains independently governed by MS-PROT-043 and does not automatically create CustomerContext.

---

## 21. Guardian, representative and delegated-customer boundary

This amendment does not invent a universal guardian, parent, representative, household or delegated-customer relationship.

Where a capability requires such a relationship and no accepted authority defines it:

```text
relationship authority absent
        ↓
CUSTOMER eligibility cannot be established generically
        ↓
fail closed
        ↓
separate governed design required
```

Names, email equality, emergency-contact fields or AI inference MUST NOT substitute for an accepted representative relationship.

---

## 22. AI boundary

AI may:

- explain why a customer Surface is or is not available;
- suggest possible identity/customer reconciliation to an authorised merchant;
- interpret customer intent after a legitimate Surface is presented; and
- propose mappings to already-registered semantics during governed configuration/design workflows.

AI MUST NOT:

- create Customer Surface Eligibility Requirements;
- invent runtime customer relationships;
- treat name/email similarity as identity truth;
- broaden contextual access;
- override revoked/expired security context;
- create guardian/delegate authority; or
- turn a probabilistic customer match into Surface eligibility.

Specialists infer; accepted deterministic authorities decide.

---

## 23. Provider boundary

External authentication, identity, messaging, payment or other providers MUST NOT own Customer Surface eligibility.

Provider evidence MAY contribute to trusted context through the applicable accepted adapter/authority.

Provider success, provider account identity or provider session state MUST NOT independently establish merchant-customer relationship truth.

---

## 24. Retry, caching and current-context semantics

Customer Surface eligibility is a read/context decision and MAY be re-evaluated repeatedly without creating business effects.

A previously successful eligibility decision MUST NOT be treated as permanent authority when governing context can change.

At minimum, current evaluation must be capable of reflecting applicable changes such as:

```text
session expiry/revocation
contextual-access proof expiry/revocation
merchant-scope change
relationship removal/correction where governed
security restriction
residual-access change
projection serviceability change
Exposure change
```

Caching MAY be used only where it preserves the current-context/security semantics required by the governing authorities. Cache possession is not authority.

---

## 25. Cross-domain reference scenarios

### 25.1 Guest motel Booking

```text
Visitor browses public Standard Room
        ↓
Booking B101 + CustomerContext C55 established
        ↓
secure transaction-specific Booking access validated
        ↓
Booking customer requirement satisfied
        ↓
CUSTOMER Booking surface
```

No CustomerAccount is required.

### 25.2 Returning customer across merchants

```text
CustomerAccount A1
    ├── Merchant A → CustomerContext CA → Booking B1
    └── Merchant B → CustomerContext CB → Appointment A1
```

The same authenticated identity MAY establish different merchant-scoped customer contexts. Merchant A context MUST NOT expose Merchant B history.

### 25.3 Same trade, different merchant composition

```text
Gardener A
    Publication + Enquiry
    → no Appointment CUSTOMER surface

Gardener B
    Publication + Appointment + Scheduling
    + existing related Appointment
    → Appointment CUSTOMER surface may be eligible
```

### 25.4 Information publisher

Reading a Publication or sending one Enquiry does not automatically create CustomerContext or a persistent CUSTOMER workspace.

### 25.5 Guest Order

An Order may support guest ordering. A valid secure Order context MAY satisfy a narrow Order-customer requirement without manufacturing CustomerAccount or exposing unrelated customer history.

---

## 26. Falsification record

The accepted model was tested against:

- anonymous motel browsing;
- guest Booking without CustomerAccount;
- authenticated account with no merchant relationship;
- one account with relationships to multiple merchants;
- forged Booking/resource identifiers;
- same-email/contact-value collision;
- same-trade merchants with different capability graphs;
- information-only publisher and one-off Enquiry;
- guest Ordering without universal CustomerContext requirement;
- projection failure for one contribution while another remains serviceable;
- stale rendered actions whose mutation is later rejected;
- secure-link expiry/revocation;
- session revocation;
- capability removal after existing commitment;
- commercial downgrade with residual commitments;
- CustomerContext reconciliation;
- unresolved daycare guardian/representative semantics;
- AI-inferred customer similarity; and
- cross-merchant isolation.

The following candidate models were rejected:

```text
CustomerAccount == CUSTOMER audience
CustomerContext == universal customer access
Exposure-only customer membership
resource-ID possession as relationship authority
business-category customer portals
generic eligibility expression DSL
Surface-owned customer relationship graph
frontend relationship decisions
contact-value identity matching
CustomerContext reconciliation as automatic access delegation
CUSTOMER visibility as mutation authority
```

No material contradiction remained after preserving the ownership and current-context boundaries above.

---

## 27. Validation matrix

| Constraint | Result |
|---|---|
| CustomerAccount remains distinct from CustomerContext | PASS |
| Guest/contextual customer access remains possible | PASS |
| Surface owns membership but not customer relationship truth | PASS |
| Relationship requirements are owner-qualified | PASS |
| Live relationship evidence remains outside Merchant Configuration/RCP | PASS |
| Client identifiers do not become authority | PASS |
| Weak contact matching does not establish access | PASS |
| Exposure remains separate | PASS |
| Projection Serviceability remains separate | PASS |
| Execution authority remains separate | PASS |
| Residual existing-commitment access can survive without reopening new activity | PASS |
| CustomerContext reconciliation does not silently broaden access | PASS |
| Cross-merchant isolation preserved | PASS |
| Independent customer contributions fail/degrade independently | PASS |
| Business category remains non-authoritative | PASS |
| AI/provider boundaries preserved | PASS |
| Missing guardian/delegate authority fails closed | PASS |
| No universal customer portal required | PASS |
| Composite architecture preserved | PASS |

---

## 28. Accepted result

MS-PROT-049 composite authority now distinguishes four customer-facing decisions:

```text
CUSTOMER SURFACE ELIGIBILITY
    Does this registered contribution belong in this current merchant-scoped customer context?

PROJECTION SERVICEABILITY
    Can the required read representation currently be produced?

EXPOSURE
    May the audience observe the resulting semantic element?

EXECUTION AUTHORITY
    May the requested business operation actually execute now?
```

The governing rule is:

> **A relationship-bound CUSTOMER Surface Contribution becomes contextually eligible only when its registered owner-qualified Customer Surface Eligibility Requirement is satisfied from current trusted merchant-scoped relationship/context evidence. Capability activation, CustomerAccount authentication, CustomerContext existence, client-provided identifiers, contact-value similarity, Surface state, provider output and AI inference do not independently manufacture that eligibility.**
