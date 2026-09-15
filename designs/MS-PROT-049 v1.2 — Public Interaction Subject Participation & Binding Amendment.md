# MS-PROT-049 v1.2 — Public Interaction Subject Participation & Binding Amendment

**Document ID:** MS-PROT-049  
**Version:** 1.2  
**Status:** **ACCEPTED by manual approval on 26 August 2026**  
**Amends:** MS-PROT-049 v1.0 and v1.1 within the scope defined below  
**Depends on:** MS-PROT-027, MS-PROT-035, MS-PROT-036, MS-PROT-043, MS-PROT-044, MS-PROT-049 v1.0-v1.1, MS-PROT-059, MS-PROT-062, MS-PROT-042 v1.5, MS-PROT-077  
**Approved:** Manual approval on 26 August 2026 after in-chat proposal, authority review, cross-domain falsification, alternatives review and recommendation  
**Purpose:** Define how a legitimate public interaction identifies the exact exposed semantic subject or subjects that may participate in it without transferring semantic ownership, availability truth or execution authority into storefront composition or frontend code.

---

## 1. Governing Decision

MS-PROT-049 v1.0 remains authoritative that a `PUBLIC_INTERACTION` contribution declares a legitimate public/customer-facing interaction entry point. This amendment makes the subject-level boundary explicit.

> **A Public Interaction Contribution identifies merchant-level public interaction applicability. A Public Interaction Binding is a derived audience-safe projection stating that an exposed semantic subject may participate in that registered interaction under the merchant's current resolved semantics. The binding owns no participation truth and grants no execution authority.**

Canonical separation:

```text
AUTHORITATIVE SUBJECT / PROPOSITION
        +
AUTHORITATIVELY OWNED
SUBJECT-INTERACTION PARTICIPATION
        +
ACTIVE MERCHANT SEMANTICS
        +
APPLICABLE PUBLIC_INTERACTION CONTRIBUTION
        +
PUBLIC EXPOSURE / PROJECTION
        ↓
PUBLIC INTERACTION BINDING
        ↓
storefront presentation / customer selection
        ↓
bounded public application use case
        ↓
authoritative subject resolution
        +
runtime revalidation
        ↓
capability-owned execution
```

Hard invariant:

```text
Public Interaction Binding
        ≠
semantic participation authority
        ≠
current availability truth
        ≠
execution authority
```

---

## 2. Scope

This amendment governs only the cross-boundary contract between:

```text
exposed semantic subject
+
public interaction contribution
+
authoritatively owned subject-operation participation
+
public projection
+
public interaction initiation
```

It defines:

- merchant-level interaction contribution versus subject-level interaction binding;
- ownership of subject-operation participation;
- fail-closed behaviour when participation ownership is absent;
- typed semantic subject identity at public boundaries;
- many-to-many interaction cardinality;
- the separation of stable participation from live availability and execution eligibility;
- public-client trust boundaries;
- stale and forged reference handling;
- context carry-forward into interaction preparation;
- backend resolution of internal execution semantics; and
- compatibility with Offering, Listing, Product, Publication and other accepted subject types.

---

## 3. Explicit Non-Goals

This amendment does **not** define:

- a universal Offering schema;
- a universal `PublishedItem`, `InteractionItem`, `StorefrontItem` or `ActionableItem` Operational Object;
- merchant-category storefront templates;
- exact REST URI names;
- React/Next.js component structure;
- page layout, route structure or presentation copy;
- exact projection storage/cache/index technology;
- current stock, capacity or scheduling-availability ownership;
- one universal availability status;
- one generic command/operation dispatch endpoint;
- payment/checkout workflow;
- shopping-cart lifecycle;
- concrete Booking Resource-pool implementation;
- concrete Appointment slot-generation implementation;
- Enquiry field schemas; or
- opaque-reference signing/encoding technology.

Existing authorities remain governing for those concerns.

---

## 4. Public Interaction Contribution and Subject Binding Are Distinct

### 4.1 Public Interaction Contribution

A `PUBLIC_INTERACTION` contribution answers:

> **Does this merchant's active semantic configuration legitimately contribute this type of public interaction surface?**

Examples include:

```text
appointment / arrange-appointment
booking / reserve-subject
ordering / place-order
enquiry / send-enquiry
```

This remains capability-level surface composition semantics under MS-PROT-049 v1.0.

### 4.2 Public Interaction Binding

A Public Interaction Binding answers:

> **Which exact exposed semantic subject may participate in that interaction?**

Examples:

```text
Garden Maintenance Offering
    → arrange-appointment

Standard Room Category
    → reserve-subject

Milk Offering
    → place-order

Property Listing L200
    → send-enquiry
```

A Public Interaction Contribution may legitimately be applicable while the current public binding result is empty.

Example:

```text
Appointment interaction configured
+
no appointment-capable Offering currently exposed
        ↓
no public Appointment subject binding
```

Main Street shall not manufacture a subject merely because the merchant has the interaction capability.

---

## 5. Public Interaction Binding Is a Projection Concept

`Public Interaction Binding` is a projection/read concept.

It is **not**:

```text
Operational Object
Domain Event
business commitment
capability activation fact
operation authorisation
availability reservation
price hold
provider readiness fact
```

A binding is derived from already-authoritative semantics and audience-safe projection/exposure decisions.

It may be materialised or request-scoped according to applicable MS-PROT-027 projection contracts. Its physical storage representation is not specified here.

---

## 6. Single-Owner Participation Invariant

For every projected subject-interaction binding, the authoritative fact that:

```text
Subject S may participate in Interaction / Operation O
```

MUST already have exactly one accepted semantic/configuration/typed-relationship owner.

The binding projector owns none of that meaning.

Possible owners include, according to the applicable capability model:

- Offering/configuration semantics that establish supported operations under MS-PROT-044;
- Booking semantics/configuration that establish an eligible booked subject;
- Enquiry/relationship semantics that establish an eligible Enquiry `SUBJECT`;
- capability-owned typed relationships connecting a subject to a supported operation context; or
- another separately accepted capability/configuration authority.

The generic storefront/surface layer MUST NOT become the owner of those facts.

Canonical direction:

```text
CAPABILITY / CONFIGURATION OWNER
        ↓
ACTIVE RESOLVED SEMANTICS
        ↓
PUBLIC INTERACTION BINDING PROJECTION
        ↓
STOREFRONT
```

Rejected:

```text
STOREFRONT / BINDING PROJECTION
        ↓
invents subject-operation meaning
```

---

## 7. Missing Ownership Must Fail Closed

If no accepted authority establishes the exact subject-interaction participation fact, Main Street MUST NOT produce a binding.

Rejected fallback evidence includes:

```text
business category
subject type alone
page route
component type
presentation label
subject name
provider name
AI inference
frontend convention
```

Example:

```text
Publication active
+
Enquiry active
+
Publication P1 exposed
```

is not sufficient to conclude:

```text
P1 → Enquiry
```

unless an accepted semantic owner establishes that relationship.

Required result when ownership is absent:

```text
no authoritative participation relationship
        ↓
no binding
        ↓
separate design required if product behaviour is needed
```

---

## 8. Surface Applicability Plus Exposure Is Insufficient

The following inference is prohibited:

```text
Public Interaction Contribution exists
+
Subject is publicly exposed
        ↓
subject participates in interaction
```

Counterexample:

```text
Consultant
    publishes Article A
    exposes Consultation Offering C
    supports Appointment
```

Correct:

```text
Consultation Offering C → Appointment
Article A                → no Appointment
```

unless another accepted relationship explicitly states otherwise.

Therefore a binding candidate requires at least:

```text
public interaction contribution applicable
+
subject publicly exposed
+
authoritatively owned subject-interaction participation
```

subject to applicable projection/serviceability constraints.

---

## 9. Subject Type Is Not Participation Authority

The following inference is also prohibited:

```text
Appointment active
+
subject.type = Offering
        ↓
Offering supports Appointment
```

A hybrid merchant may legitimately have:

```text
Offering A
    Ordering only

Offering B
    Appointment only

Offering C
    Appointment + Enquiry
```

Subject semantic type identifies what the subject is. It does not by itself establish which interactions apply.

---

## 10. Semantic Subject Identity Must Remain Resolvable

A public interaction binding must preserve sufficient semantic identity for the backend to distinguish, where applicable:

```text
Offering
Product
ProductVariant
Listing
Publication / Opportunity
Resource
other accepted capability-owned subject
```

A transport API MAY expose an opaque public-safe reference rather than internal semantic fields, provided the backend can authoritatively resolve that reference back to the exact:

```text
merchant scope
owning semantic namespace / capability
semantic subject type
subject identity
```

Rejected as the only semantic locator:

```json
{
  "itemId": "123"
}
```

where `123` could silently mean materially different subject types without typed backend resolution.

Hard rule:

> **Opaque transport identity may hide internal representation; it must not erase semantic identity.**

---

## 11. Offering Is Common but Not Universal

MS-PROT-044 remains authoritative that an Offering represents a merchant-controlled proposition made available for a supported interaction.

Offerings are therefore common interaction subjects, for example:

```text
Structural Consultation Offering
    → Appointment

Executive Room Category Offering
    → Booking

Milk Offering
    → Ordering
```

But Main Street SHALL NOT require every public interaction subject to become an Offering.

Legitimate non-Offering cases may include, where accepted semantics establish them:

```text
Property Listing L200
    → Enquiry SUBJECT

Scholarship Opportunity O1
    → Enquiry SUBJECT / external-action context

Resource R1
    → Booking subject
    where accepted Booking/proposition semantics expose it directly
```

Therefore:

> **Offering is a valid and common interaction subject, not a universal storefront-interaction wrapper.**

---

## 12. Cardinality Is Many-to-Many

Main Street MUST NOT assume:

```text
one subject ↔ one interaction
```

Valid examples include:

### One subject, multiple interactions

```text
Structural Consultation Offering
    ├── Appointment
    └── Enquiry
```

### One interaction, multiple subjects

```text
Ordering
    ├── Milk Offering
    ├── Bread Offering
    └── Coffee Offering
```

### One command, multiple selected subjects

MS-PROT-077 permits one Order to contain one or more Order Commitment Portions.

Therefore individual Ordering bindings identify legitimate orderable candidates; they do not imply one Order per binding.

Hard invariant:

> **A binding identifies legitimate participation. It does not define the complete command cardinality or business commitment.**

---

## 13. Operation Participation Roles Remain Capability-Owned

Some operations require the selected subject to participate in a specific semantic role.

Examples conceptually include:

```text
Appointment
    Offering / service context

Booking
    booked subject

Ordering
    orderable subject / commitment-portion source

Enquiry
    enquiry subject/context
```

Where the applicable role cannot be unambiguously derived from the registered subject-operation relationship, the authoritative capability/configuration semantics MUST preserve enough registered role information for backend resolution.

The browser MUST NOT invent that role.

This amendment does not create one universal public enum of every interaction role.

---

## 14. Internal Execution Identifiers Must Not Be Required Public Knowledge

A public client expresses customer intent concerning the selected public subject.

It MUST NOT need to understand internal execution identifiers merely to initiate a legitimate public interaction.

Example:

```text
Customer selects:
    Garden Maintenance Offering

Customer intent:
    arrange appointment
```

The public client should not be required to understand an internal identifier such as:

```text
gardening.perform
```

if the authoritative Offering/Appointment relationship permits the backend to resolve the current execution context.

Likewise, a Booking customer selecting:

```text
Standard Room
```

must not be required to submit an internal Allocation capacity identifier.

MS-PROT-042 v1.5 remains authoritative that booked-subject truth is distinct from internal allocated Resource/capacity.

Opaque public interaction references MAY be used where useful, but they remain locators and not authority.

---

## 15. Binding Is Not Availability

A legitimate subject-interaction relationship may continue to exist while execution is currently impossible.

Examples:

```text
Garden Maintenance Offering
    supports Appointment
    but no slot remains tomorrow

Standard Room
    supports Booking
    but selected dates have no capacity

Milk Offering
    supports Ordering
    but stock is currently zero
```

Therefore:

```text
subject-interaction participation
        ≠
current availability
```

Availability remains owned by its applicable capability authority, including Scheduling, Booking/Allocation, Inventory or another accepted owner.

A binding MUST NOT persist or imply authoritative fields such as:

```text
currentlyAvailable = true
stockGuaranteed = true
slotGuaranteed = true
capacityGuaranteed = true
```

unless such values are separately projected from their owning authorities under their own contracts.

---

## 16. Binding Is Not Price or Commercial-Term Authority

A binding may be presented alongside current Offering/commercial terms, but it does not lock those terms.

Example:

```text
Offering O1 displayed at £75
        ↓
price changes to £80
        ↓
customer later submits interaction
```

The stale public display does not itself create a £75 commitment or hold.

Applicable current commercial terms MUST be re-established by the owning commitment/Money authority before commitment according to MS-PROT-055, MS-PROT-077 or the applicable Booking/Appointment commercial contract.

---

## 17. Binding Is Not Operation Authorisation

Rendering or possessing a valid binding does not authorise mutation.

Before execution, the backend/application boundary MUST independently resolve and revalidate all applicable dimensions, including where relevant:

```text
merchant scope
current active semantic/configuration context
subject existence and semantic type
current subject-interaction participation
execution principal / Actor Authorisation
Commercial Entitlement
Trust Satisfaction
Operational Eligibility
Provider Readiness
Resource Protection Admission
current capacity / stock / scheduling state
operation Requirements
concurrency-sensitive invariants
```

MS-PROT-062 remains authoritative for execution-decision composition.

Hard rule:

> **A successful read/projection result is never durable mutation authority.**

---

## 18. Stale Binding Behaviour

A public binding may become stale after the merchant activates a newer configuration or changes exposure/subject participation.

Example:

```text
T1: Release R1 exposes O1 → Appointment
T2: browser receives binding
T3: Release R2 removes O1 public Appointment participation
T4: browser submits old interaction
```

For new activity, backend execution MUST resolve the governing current semantics and reject the request where participation is no longer applicable.

The stale binding does not create a grandfathered right to establish a new commitment.

Existing commitments created under earlier releases remain governed by their own historical affinity authorities.

---

## 19. Forged and Cross-Merchant References Must Fail Closed

A browser-provided subject/binding reference is untrusted input.

If a client alters a reference, substitutes another subject or submits a subject belonging to another merchant, the backend MUST independently establish:

```text
merchant scope
subject existence
subject semantic type / owner
current public interaction applicability
current authoritative subject-interaction participation
applicable execution authority / eligibility
```

A globally unique identifier does not grant cross-merchant authority.

A signed or opaque token MAY reduce tampering or lookup cost but possession of the token does not replace authoritative resolution unless a separately accepted authority explicitly establishes such a security contract.

---

## 20. Context Carry-Forward

MS-PROT-043 remains authoritative that customers shall not restate authoritative context Main Street already knows.

Selecting or navigating from a Public Interaction Binding carries forward the exact subject context into interaction preparation.

Example:

```text
Customer views Listing L200
        ↓
clicks Enquire
        ↓
Enquiry context already knows:
    merchant
    Listing L200
```

The customer shall not be asked to identify the Listing again unless a materially different semantic input is required.

The same principle applies to selected Offerings, booked subjects and orderable subjects.

---

## 21. Interaction Preparation and Unresolved Requirements

This amendment does not define universal public forms.

Once the public subject and interaction are known, the application/runtime derives remaining customer input from:

```text
registered Operation Requirements
+
known merchant/subject/customer context
        ↓
unresolved Requirements
        ↓
channel-specific collection projection
```

Examples:

```text
Garden Maintenance Offering
    + Appointment
        ↓
known:
    merchant
    offering/service context

remaining may include:
    candidate date/time
    customer contact/context
    location where applicable
```

and:

```text
Standard Room
    + Booking
        ↓
known:
    merchant
    booked subject

remaining may include:
    reservation period
    customer/guest context
```

Frontend forms collect unresolved Requirements; they do not define the operation's business semantics.

---

## 22. Public Application Use-Case Boundary

Public storefront clients MUST initiate bounded public/customer application use cases rather than submit arbitrary executable semantic commands.

Rejected universal transport contract:

```text
POST /execute
{
    operation: "appointment.confirm",
    subject: "gardening.perform"
}
```

The public boundary should instead express bounded customer intent conceptually equivalent to:

```text
arrange appointment for selected exposed service
reserve selected exposed bookable subject
commit selected orderable subjects
submit enquiry concerning current exposed subject
```

Exact endpoint names and DTO shapes remain governed by MS-PROT-035 and downstream implementation.

---

## 23. Business Category Is Never Binding Evidence

Merchant/business classification remains onboarding/context information.

The following is prohibited:

```text
category = GARDENER
        ↓
Garden Maintenance → Appointment
```

The same-trade/different-configuration invariant requires subject participation to come from active registered semantics and merchant operational choice rather than category.

No missing relationship may be filled by merchant category.

---

## 24. AI Boundary

AI may assist merchants or customers by interpreting intent and selecting among already registered/exposed subjects and interactions.

AI MUST NOT:

- establish a new subject-operation participation relationship;
- infer execution authority from category or natural-language similarity;
- manufacture availability;
- bypass exposure;
- convert a visible Publication/Listing/Product into an Offering; or
- directly authorise mutation from a projected binding.

AI remains inferential/assistive under existing AI authority.

---

## 25. Canonical Public Interaction Flow

The accepted conceptual flow is:

```text
ACTIVE CONFIGURATION
        ↓
PUBLIC_INTERACTION CONTRIBUTIONS
        +
PUBLIC SUBJECT PROJECTIONS
        +
AUTHORITATIVELY OWNED PARTICIPATION RELATIONSHIPS
        ↓
PUBLIC INTERACTION BINDINGS
        ↓
customer chooses subject / interaction
        ↓
context carry-forward
        ↓
derive unresolved Requirements
        ↓
collect customer input
        ↓
bounded public application use case
        ↓
resolve current subject + semantics
        ↓
runtime revalidation
        ↓
CAPABILITY-OWNED OPERATION
        ↓
authoritative effects
```

The storefront remains a projection, composition and intent-collection channel.

---

## 26. Cross-Domain Falsification — Same Trade, Different Configuration

Two gardeners may legitimately have:

```text
Gardener A
    Publication + Enquiry

Gardener B
    Publication + Enquiry + Appointment + Scheduling
```

Gardener A produces no Appointment binding.

Gardener B may produce Appointment bindings only for subjects whose participation is authoritatively established and publicly exposed.

No `GARDENER` runtime branch is required.

**PASS**

---

## 27. Cross-Domain Falsification — Public Article Plus Appointment Offering

A gardener/consultant may expose both:

```text
Article A
Garden Maintenance / Consultation Offering O1
```

while Appointment is active.

Correct result:

```text
O1 → Appointment
A  → no Appointment
```

unless another accepted relationship explicitly establishes Article participation.

This proves that public exposure plus merchant-level interaction applicability is insufficient.

**PASS**

---

## 28. Cross-Domain Falsification — Motel / Booked Subject Versus Allocation

Customer selects:

```text
Standard Room
```

Internal Resource/Allocation may protect pooled or concrete capacity.

The public Booking binding concerns the legitimate booked subject/proposition. It does not replace that subject with an internal Allocation identifier.

**PASS**

---

## 29. Cross-Domain Falsification — Retail Ordering

Public bindings may expose:

```text
Milk   → Ordering
Bread  → Ordering
Coffee → Ordering
```

The customer may select several and establish one Order containing several Order Commitment Portions under MS-PROT-077.

Binding cardinality does not force one-item-per-Order semantics.

**PASS**

---

## 30. Cross-Domain Falsification — Realtor Enquiry

Customer views:

```text
Listing L200
```

and initiates Enquiry.

Where Enquiry semantics establish the relationship:

```text
Enquiry SUBJECT → Listing L200
```

The public binding preserves Listing identity. Listing does not become an Offering merely because it is interactive.

**PASS**

---

## 31. Cross-Domain Falsification — Information Publisher

A scholarship Publication/Opportunity may be publicly exposed while Enquiry is also active.

No Enquiry binding is produced unless accepted semantics establish that exact Publication/Opportunity as an Enquiry subject.

Visible + Enquiry-capable merchant is insufficient.

**PASS**

---

## 32. Cross-Domain Falsification — Hidden Offering / Channel Variation

A consultation Offering may remain operationally valid for merchant-assisted or telephone Appointment entry while the merchant hides it from public storefront exposure.

Required result:

```text
internal Offering → Appointment participation remains valid
public exposure = WITHHOLD
        ↓
no PUBLIC Appointment binding
```

Merchant-assisted channels may continue through the shared authoritative Appointment semantics.

This preserves MS-PROT-059 channel convergence without duplicating Offering truth.

**PASS**

---

## 33. Falsification — Stale Availability

A valid Appointment binding is displayed with an available interval. Another customer consumes the final capacity before confirmation.

Required result:

```text
binding remains semantic participation evidence
+
stale availability does not authorise mutation
+
Appointment/Scheduling revalidation rejects incompatible commitment
```

**PASS**

---

## 34. Rejected Alternatives

### Universal `StorefrontItem`

Rejected because it encourages presentation concerns to flatten Offering, Listing, Publication, Product and other semantic identities into a second domain model.

### Everything becomes Offering

Rejected by MS-PROT-044 because informational and Listing subjects may be interactive without becoming merchant commercial Offerings.

### Surface Contribution owns subject collection

Rejected as semantic authority because capability-level surface metadata and merchant subject/read data have different ownership and lifecycle.

### Frontend infers interaction from subject type

Rejected because one Offering may support Appointment, Ordering, Enquiry, several interactions or none.

### Frontend requires internal operation identifiers

Rejected as a required public contract because public clients should express bounded customer intent while backend semantics resolve internal execution context.

### Derived Public Interaction Binding

Accepted because it is the minimum read-side contract that preserves existing owners while preventing business/category logic from leaking into storefront code.

---

## 35. Accepted Invariants

1. A `PUBLIC_INTERACTION` contribution identifies merchant-level public interaction applicability; it is not itself an interaction subject.
2. A Public Interaction Binding is a derived projection, never an Operational Object or source of business truth.
3. Every subject-interaction participation fact MUST have exactly one accepted semantic/configuration/typed-relationship owner before it may be publicly projected.
4. If no authoritative participation owner exists, Main Street MUST fail closed and produce no binding.
5. Business category, subject type, UI route, presentation label, provider name and AI inference MUST NOT substitute for participation authority.
6. The true semantic identity of the bound subject MUST remain authoritatively resolvable; transport MAY use an opaque public-safe reference without flattening semantic identity.
7. Offering is a common interaction subject but not a universal public-interaction wrapper.
8. Subject-to-interaction cardinality is many-to-many.
9. A binding identifies legitimate participation; it does not define a complete command or business commitment.
10. Binding existence does not establish current availability, price lock, Commercial Entitlement, Actor Authorisation, Provider Readiness or execution success.
11. Public clients express bounded customer intent; backend application/runtime resolves current authoritative semantics before mutation.
12. Stale or forged public references grant no authority.
13. Internal Resource/Allocation identifiers MUST NOT replace the actual customer-facing booked subject merely for execution convenience.
14. Frontend code MUST NOT require business-category branching or internal execution identifiers merely to initiate a legitimate public interaction.
15. Unresolved operation Requirements are derived after known context is carried forward rather than encoded as business-specific frontend domain rules.

---

## 36. Authority Composition

This amendment is intentionally narrow.

The following authorities remain unchanged within their scopes:

- **MS-PROT-027** — projections, Exposure, freshness/serviceability and stale-read boundaries;
- **MS-PROT-035** — API/transport boundaries and identifiers as locators rather than authority;
- **MS-PROT-036** — storefront composition and shared storefront runtime;
- **MS-PROT-043** — Context-Carry-Forward and Enquiry subject/customer context;
- **MS-PROT-044** — Offering, Product, Listing and published-subject boundaries;
- **MS-PROT-049 v1.0** — capability Surface Contribution semantics;
- **MS-PROT-049 v1.1** — contextual interaction availability/provider-readiness boundary for its accepted scope;
- **MS-PROT-059** — channel convergence and contextual Requirements collection;
- **MS-PROT-062** — runtime execution-decision composition;
- **MS-PROT-042 v1.5** — Booking/Appointment/Scheduling execution contracts and booked-subject/Allocation separation; and
- **MS-PROT-077** — Ordering commitment cardinality and authoritative orderable-subject revalidation.

Where a future interaction requires a subject-operation participation fact with no accepted semantic owner, that gap MUST enter the governed design lifecycle rather than be invented by this amendment's generic binding projector.

---

## 37. Governance Verdict

```text
PROPOSAL                     COMPLETE
AUTHORITY REVIEW             COMPLETE
CROSS-DOMAIN FALSIFICATION   COMPLETE
ALTERNATIVES REVIEW          COMPLETE
MANUAL APPROVAL              COMPLETE — 26 AUGUST 2026
FORMALISATION                THIS DOCUMENT
```

**ACCEPTED.** Public storefront interaction subjects are bound through derived, fail-closed projections over already-authoritative subject-operation participation. Surface composition and frontend code remain non-authoritative, business-category-neutral and subject-type-preserving.
