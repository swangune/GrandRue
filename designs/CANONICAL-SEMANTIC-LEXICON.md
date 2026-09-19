# GrandRue Canonical Semantic Lexicon

**Version:** 1.60
**Status:** ACCEPTED governance terminology authority
**Governed by:** `DOCUMENT-GOVERNANCE.md`
**Purpose:** Disambiguate high-risk shared terminology across GrandRue without replacing the accepted design documents that own substantive semantics.

---

## 1. Governing rule

> **The lexicon governs names and qualification rules; accepted design authorities govern business meaning, invariants, lifecycle and mutation ownership.**

Shared English vocabulary does not imply shared domain ownership.

When context is materially ambiguous, use the qualified canonical term.

---

## 2. Fulfilment and Shipment

Bare `Fulfilment` is unsafe in cross-domain normative prose.

### Provider Fulfilment / Capability Fulfilment

**Authority:** composite MS-PROT-048 through v1.5.
**Meaning class:** the technical responsibility by which an internal or external provider discharges a capability or registered platform-authority responsibility.

Preferred cross-domain terms:

```text
Provider Fulfilment
Capability Fulfilment
Fulfilment Role
```

Provider Fulfilment does not create the business or platform semantic responsibility that it discharges. Composite MS-PROT-048 distinguishes merchant-routed fulfilment from platform-routed shared infrastructure and permits registered platform authorities to own fulfilment requirements without manufacturing a business capability.

### Order Fulfilment

**Authority:** composite MS-PROT-060 through v1.1.
A merchant-scoped durable Operational Object representing authoritative satisfaction evidence against one exact Order. An Order Fulfilment contains one or more quantity-bearing Order Fulfilment Satisfaction Portions referencing exact Order Commitment Portions.

Order Fulfilment does not rewrite Order commitment truth and is not a Shipment.

### Order Fulfilment Satisfaction Portion

**Authority:** MS-PROT-060 v1.1.
A durable quantity-bearing satisfaction fact within one Order Fulfilment identifying the exact Order Commitment Portion satisfied, supplied quantity/unit semantics, actual supplied subject and required historical fulfilment provenance.

For one Order Commitment Portion, cumulative authoritative satisfied quantity must not exceed current committed quantity less Ordering-owned released quantity.

### Satisfaction Anchor

**Authority:** MS-PROT-060 v1.1.
The registered fulfilment-method fact whose occurrence makes an Order Fulfilment Satisfaction Portion authoritative.

Initial registered semantic families are:

```text
COLLECTION_HANDOVER
OUTBOUND_MOVEMENT_HANDOVER
```

`ready for collection` is not collection satisfaction; provider tracking progress is not an Order Fulfilment Satisfaction Anchor unless accepted semantics establish the required physical handover.

### Shipment

**Authority:** composite MS-PROT-060 through v1.1.
A merchant-scoped durable physical-movement attempt associated with one Order Fulfilment in the initial production model.

Shipment does not own Order Fulfilment quantity. Collection requires no Shipment. Multiple Shipments may concern one already-satisfied scope without multiplying fulfilled quantity.

Shipment authoritative truth is composed from bounded facts such as dispatch and terminal outcome rather than a carrier-inspired universal lifecycle.

The initial terminal outcome vocabulary is:

```text
DELIVERED
NOT_DELIVERED
```

Provider-specific tracking/status vocabulary remains evidence until interpreted by Shipment authority.

### Shipment Preparation Request

**Authority:** MS-PROT-060 v1.1, composed with MS-PROT-048/MS-PROT-069.
A durable progression identity created before one logical external-provider side effect that prepares a future physical Shipment. It retains exact Merchant Scope, intended movement scope, destination/method, provider/binding provenance and correlation/idempotency identity.

It is not the Shipment, Order Fulfilment or provider transaction.

### ProviderShipmentEvidence

**Authority:** MS-PROT-060 v1.1.
Authenticated/correlated external evidence concerning a Shipment Preparation Request or Shipment. Provider-native result/event vocabulary is evidence and must pass registered provider interpretation before it may establish Shipment-owned dispatch or terminal-outcome truth.

Hard distinctions:

```text
Provider Fulfilment
    ≠ Order Fulfilment

Order Fulfilment
    ≠ Shipment

Shipment
    ≠ Order commitment

Shipment
    ≠ Inventory Movement

Shipment dispatch
    ≠ DELIVERED

Shipment failure
    ≠ Order release
    ≠ Refund
    ≠ Inventory restoration

replacement Shipment
    ≠ additional Order Fulfilment quantity
```

### OperationFulfilment

**Class:** implementation/runtime terminology.
**Rule:** shall not be assumed to mean Provider Fulfilment or Order Fulfilment merely because the word is shared.

### QuantityFulfilment

**Class:** implementation/inventory quantity terminology.
**Rule:** shall not become the canonical name for Order Fulfilment.

---

## 3. Availability

Bare `Availability` is permitted only when the owning context is unambiguous.

Qualified canonical terms include:

```text
Booking Availability
Appointment Availability
Appointment Scheduling Evaluation
Workforce availability evidence
Inventory Availability / Available-to-Promise Quantity
Provider Readiness / Provider Availability
```

**Authorities:** composite MS-PROT-042 through v1.8 for Booking/Appointment/Scheduling distinctions and arrangement-qualified Workforce-evidence interoperability; composite MS-PROT-081 through v1.1 for Workforce Scheduling/availability facts; composite MS-PROT-058 through v1.2 for Inventory; composite MS-PROT-048 through v1.5 for provider fulfilment/readiness.

No universal `Availability` or `Schedule` aggregate is authorised merely because the words are reused.

### Appointment Scheduling Evaluation

**Authority:** MS-PROT-042 v1.6 within the composite MS-PROT-042 authority.
A Scheduling-owned, merchant-scoped, side-effect-free evaluation of whether one candidate Appointment interval may currently proceed toward commitment under all applicable Scheduling constraints whose required authoritative evidence can be established.

The canonical result vocabulary is:

```text
SCHEDULABLE
NOT_SCHEDULABLE
UNRESOLVED
```

Canonical distinction:

```text
SCHEDULABLE
    all applicable Scheduling constraints are established and satisfied

NOT_SCHEDULABLE
    sufficient authoritative evidence establishes at least one rejecting constraint

UNRESOLVED
    no established rejecting constraint already determines the result,
    but one or more applicable constraints cannot be evaluated from sufficient evidence
```

Hard distinctions:

```text
SCHEDULABLE ≠ reservation ≠ Appointment commitment
UNRESOLVED ≠ free capacity
Provider Readiness ≠ Appointment Scheduling Evaluation
Calendar representation ≠ Scheduling authority
Workforce Scheduling ≠ Appointment Scheduling
```

### Scheduling Constraint Evidence / Evidence Sufficiency

**Authority:** composite MS-PROT-042 through v1.8.
Scheduling Constraint Evidence is the current authoritative or accepted provider/owner-derived evidence required to evaluate one applicable Appointment Scheduling constraint. Evidence Sufficiency asks whether that evidence is sufficiently trustworthy, current and complete under its owning/integration contract to evaluate the constraint now.

Composite MS-PROT-042 through v1.8 permits applicable current owner-qualified Workforce availability evidence from MS-PROT-081 only where an explicit accepted link exists between the exact Workforce Scheduling Arrangement and schedulable Resource. Merchant Membership, Identity or display-name similarity does not establish that link. The evidence does not transfer Workforce or Appointment ownership.

No global freshness duration or provider-status shortcut is implied by these terms.

### Inventory Stock Position

**Authority:** composite MS-PROT-058 through v1.2.
An Inventory-owned authoritative stock position for one exact Merchant Scope, stock-bearing subject, Inventory scope and any additional registered stock-partition dimensions required by the applicable semantics.

Hard distinction:

```text
Inventory position established with quantity 0
    = known zero stock

Inventory position not established
    ≠ known zero stock
```

An absent position shall not be interpreted as zero merely because a persistence row is missing.

### Inventory Claim / Inventory Claim Resolution

**Authority:** composite MS-PROT-058 through v1.2.
An Inventory Claim is an authoritative reservation of a quantity against an exact Inventory position/scope. Its original claimed quantity is immutable.

An Inventory Claim Resolution is an immutable quantity-bearing fact resolving part or all of that claim through one of the currently accepted resolution meanings:

```text
RELEASED
EXPIRED
FULFILLED
```

Canonical distinction:

```text
Claim
    original reservation truth

Claim Resolution
    quantity removed from the remaining reservation

Inventory Movement
    physical/operational stock consequence where applicable
```

Release/expiry do not increase stock-on-hand. Fulfilment records the applicable stock movement and reduces stock-on-hand atomically with the fulfilled claim-resolution quantity.

### Available-to-Promise Quantity

**Authority:** composite MS-PROT-058 through v1.2.
A derived Inventory read quantity computed from authoritative stock-on-hand and the remaining quantities of active Inventory Claims under the exact Inventory scope and applicable constraints.

Hard distinction:

```text
Available-to-Promise read
    ≠ Inventory Claim
    ≠ reservation
    ≠ Order commitment authority
```

Initial production reads remain request-scoped owner queries unless a later MS-PROT-027 Projection Contract trigger is demonstrated.

---

## 4. Customer concepts

Use the most precise term where identity/access semantics matter.

### Visitor

A person interacting without an established authenticated merchant-customer account context.

### CustomerContext

**Authority:** MS-PROT-043.
Merchant-scoped durable business relationship/context concerning a customer.

### CustomerAccount

**Authority:** MS-PROT-043 and later customer-access authorities.
Authenticated identity/access construct associated with an authorised merchant-customer context.

Hard distinction:

```text
CustomerContext ≠ CustomerAccount
Order ≠ requires CustomerAccount
```

Bare `Customer` is acceptable in ordinary business prose but shall not substitute for these distinctions where identity or authority depends on them.

### Booking / Appointment related-customer requirements

**Authority:** MS-PROT-042 v1.6, composed with MS-PROT-049 v1.3 and MS-PROT-027 v1.5.
The canonical owner-qualified requirement identities are:

```text
booking / related-customer-booking
appointment / related-customer-appointment
```

They identify capability-owned predicates establishing that the current trusted customer context is authoritatively related to the exact Booking or Appointment in the exact Merchant Scope.

They are not generic `RELATED_CUSTOMER` authority and are not satisfied merely by CustomerAccount authentication, possession of an object identifier or matching contact values.

Satisfying either relationship requirement does not itself grant Exposure of every field or authority to cancel, reschedule or refund.

### Order related-customer requirement

**Authority:** MS-PROT-077 v1.1, composed with MS-PROT-049 v1.3 and MS-PROT-027 v1.5.
The canonical owner-qualified requirement identity is:

```text
ordering / related-customer-order
```

It establishes that the current trusted customer access context is authoritatively related to the exact Order within the exact Merchant Scope, either through an accepted Order → CustomerContext relationship and trusted access to that CustomerContext or through an accepted transaction-specific contextual access bound to the exact Order.

It is not satisfied merely by CustomerAccount authentication, Order-identifier possession, a client-supplied CustomerContext identifier, or matching name/email/phone/address values.

Satisfying the requirement grants neither blanket Order-field Exposure nor amendment/release/refund authority.

MS-PROT-060 v1.1 reuses this requirement for customer-safe Order Fulfilment and Shipment tracking because those facts are subordinate to one exact Order relationship; it does not create a separate fulfilment-customer or shipment-customer relationship.

MS-PROT-061 v1.1 likewise reuses this requirement for structured Returns customer observation because no ReturnCase/Return Operational Object is introduced; it does not create a separate return-customer relationship.

### Payment related-customer requirement

**Authority:** MS-PROT-055 v1.1, composed with MS-PROT-049 v1.3 and MS-PROT-027 v1.5.
The canonical owner-qualified requirement identity is:

```text
payment / related-customer-payment-obligation
```

It establishes customer relationship eligibility for an exact Payment Obligation by resolving the obligation's exact owner-qualified commercial source and reusing that source authority's accepted customer relationship predicate.

Payment therefore does not create a competing CustomerContext or generic payer relationship. CustomerAccount authentication, Payment Obligation ID possession, provider transaction identifiers, payer/cardholder names, provider-customer identifiers, receipt possession or contact-value similarity do not independently satisfy the requirement.

---

## 5. Location

Bare `Location` is unsafe where ownership or exposure matters.

Qualified concepts include:

```text
MerchantLocation
Customer Delivery Destination
Service Area
Inventory Scope Location
Resource Location
Provider Location Evidence
```

**Authority:** MS-PROT-051 for MerchantLocation and merchant-presence semantics; applicable capability authorities for other contexts.

A customer delivery destination is not a MerchantLocation.

### PostalAddressV1

**Authority:** composite MS-PROT-051 through v1.2.
The provider-neutral structured international postal-address value retained by
an exact Merchant Location revision. It includes an ISO country code, ordered
address lines and optional dependent-locality, locality, administrative-area,
postal-code and sorting-code components under explicit schema, normalization
and country-registry affinity.

`PostalAddressV1` is a revisioned value, not `MerchantLocation` identity,
Exposure, verification, deliverability, serviceability, a formatted display
string, provider place identity or geocoder authority.


### Location Resolution

**Authority:** composite MS-PROT-051 through v1.7.  
The bounded process by which candidate geographical evidence becomes an accepted precise destination for one Merchant Location revision. Location Resolution establishes where customer navigation should lead; it does not establish a Trust Claim that the merchant owns, occupies or is authorised to represent the premises.

### Customer Navigation Point

**Authority:** MS-PROT-051 v1.7.  
The precise provider-neutral accepted geographic destination used when an eligible Merchant Location is rendered as a public customer destination. A Customer Navigation Point is not Merchant Location identity, PostalAddressV1, Exposure, provider place identity or Location Verification.

### Location Verification

**Authority:** composite MS-PROT-028 through v1.3, composed with MS-PROT-051 v1.7.  
A Trust-domain process that may substantiate one specific scoped claim concerning a premises, merchant or relationship to a premises. Location Verification is optional for ordinary precise public navigation unless a separately accepted bounded Trust requirement requires it.

Hard distinctions:

    Location Resolution
        ≠ Location Verification

    Customer Navigation Point
        ≠ PostalAddressV1
        ≠ MerchantLocation identity
        ≠ Exposure

    precise public destination
        ≠ independently verified premises claim

---

## 6. Exposure and visibility

### Exposure

**Authority:** composite MS-PROT-027 through v1.5.
Preferred canonical term for the deterministic decision of whether a particular already-legitimate semantic element may be observed by a particular audience in the current authoritative context.

The verdict vocabulary is:

```text
EXPOSE
WITHHOLD
```

Exposure does not create Surface membership, Projection Serviceability, business truth, operation authority, discoverability or presentation placement.

### Exposure Element Contract

**Authority:** MS-PROT-027 v1.5.
A registered, owner-qualified, release-affined definition establishing how one class of exposable semantic/projection element may be evaluated for one audience without owning the underlying business fact.

An Exposure Element Contract is not a universal visibility status, merchant-authored rules DSL or source-fact authority.

### Audience Observation Context

**Authority:** MS-PROT-027 v1.5.
Server-established request/context evidence supplied to Exposure resolution, including the applicable Merchant Scope, audience and any required trusted principal, relationship, secure-context or other registered evidence.

Hard distinction:

```text
Audience Observation Context
    ≠ client-asserted audience authority
    ≠ authentication by itself
    ≠ customer relationship authority
    ≠ Actor Authorisation
```

### Visibility

May describe presentation/UI state, but shall not silently redefine Exposure authority.

Canonical distinction:

```text
Exposure
    audience observation decision for an already-legitimate element

Presentation visibility
    rendered UI/display consequence
```

---

## 7. Operation

`Operation` may appear at several layers.

Qualified terms should be used where ambiguity is possible:

```text
Capability Operation
Application Operation / Use Case
Runtime Operation Invocation
Provider Operation
```

Capability semantic meaning remains owned by the applicable capability authority. Application orchestration coordinates but does not steal domain ownership. Provider operations remain infrastructure-facing.

---

## 8. Status

Bare `Status` is high-risk when used as authoritative domain truth.

Preferred distinction:

```text
Authoritative lifecycle state
Authoritative fact/outcome
Derived operational status
Projection/display status
Provider-reported status
```

A convenient UI status shall not automatically become a domain lifecycle state.

Provider-specific statuses shall not redefine GrandRue business semantics unless an accepted authority explicitly maps them into platform-owned facts.

---

## 9. Policy

`Policy` must identify its owner where ambiguity is possible.

Common classes:

```text
Merchant Policy
Capability Policy
Platform/Safety Policy
Provider Policy/Evidence
```

Merchant Policy represents merchant-authoritative operating choices within registered GrandRue semantics.

GrandRue may help infer, validate and deterministically execute merchant-approved policy; it does not decide legitimate merchant operating choices merely because it provides the infrastructure.

---

## 10. Configuration

### Merchant Configuration

Authoritative accepted merchant semantic choices and structured business configuration.

### Resolved Configuration Package

Derived deterministic output of semantic compilation/resolution. It is not a second merchant authority.

### Configuration Revision Approval

**Authority:** composite MS-PROT-040 through v1.8.

An immutable Configuration-owned approval fact accepting one exact immutable Configuration Revision against exact applicable evidence.

For ordinary non-initial approval under v1.6, exact affinity includes:

```text
Merchant Scope
Configuration Revision
Semantic Registry Release
successful validation evidence
completed impact-review evidence
Resolved Configuration Package evidence
approving principal
Controller Relationship
approval time
```

For reinstatement under v1.8, the approval additionally carries the exact Reinstatement Basis Activation and is supported only by validation/package and impact-review evidence carrying that same exact basis.

Approval is not activation.

### Historical Configuration Approval / Currently Applicable Configuration Approval

**Authority:** composite MS-PROT-040 through v1.8.

A committed approval is historical evidence immediately after commit.

A Currently Applicable Configuration Approval is a historical approval that still satisfies the exact current applicability predicates required for activation.

For ordinary forward non-initial approval, the exact Controller Relationship recorded by the approval must still be the current ACTIVE Merchant Controller Relationship.

For reinstatement, current applicability additionally requires:

```text
approval.reinstatementBasisActivation
    =
merchantCurrentConfigurationActivation.activationRequestIdentifier
```

If the current activation pointer moves, the prior reinstatement approval loses current applicability even if the same Configuration Revision later becomes current again.

Hard distinction:

```text
historical approval exists
    != currently applicable approval
    != active Configuration
```

The same human Identity later becoming Controller through a different Controller Relationship does not revive the older approval.

### Configuration Reinstatement

**Authority:** MS-PROT-040 v1.8.

A new activation of a previously activated, currently superseded immutable Configuration Revision after fresh current validation/package evidence, current impact review, new current Controller approval, compatibility, serving admission and concurrency checks.

Reinstatement is not rollback and is not historical activation replay.

A superseded initial Configuration Revision is eligible for reinstatement through the bounded reinstatement path; its historical first-configuration approval is not reinstatement authority.

### Reinstatement Basis Activation

**Authority:** MS-PROT-040 v1.8.

The exact committed Configuration Activation that owns the merchant's current Configuration pointer when a reinstatement decision is established.

Its canonical identity is that activation's exact `activation_request_identifier`.

Hard distinction:

```text
same current Configuration Revision
    != same Reinstatement Basis Activation
```

Validation evidence, impact-review evidence and Configuration Revision Approval produced for reinstatement carry exact affinity to the same Reinstatement Basis Activation.

If the current activation pointer moves, the prior reinstatement approval is no longer currently applicable even if the same revision later becomes current again.

### Configuration Activation

**Authority:** composite MS-PROT-040 through v1.8.

The atomic authority boundary that makes one exact Configuration Revision the merchant's current active Configuration after all applicable approval, concurrency, semantic-compatibility and serving-admission predicates succeed.

Activation does not create validation, impact evidence or approval.

### Ordinary Non-Initial Configuration Activation Authority

**Authority:** composite MS-PROT-040 through v1.8.

For the current MVP, ordinary replacement/reinstatement activation requires a GrandRue-established trusted authenticated execution context whose principal is the current ACTIVE Merchant Controller for an OPEN, unsuspended Merchant Account.

For reinstatement, the target must be a genuinely superseded previously activated Configuration Revision and the exact Reinstatement Basis Activation recorded by the approval must still own the merchant's current Configuration pointer.

Hard distinctions:

```text
authenticated Identity
    != current Controller authority

caller-supplied initiatingPrincipalIdentifier
    != trusted activation authority

historical approval
    != current activation authority

activation replay
    != reinstatement

same Configuration Revision
    != same Reinstatement Basis Activation
```

No generic Workforce `configuration.approve` or `configuration.activate` privilege is established by composite MS-PROT-040 through v1.8.

### Runtime Context

Live facts such as actor authority, provider health, current operational state or entitlement context that shall not be confused with stable merchant configuration merely because they influence execution.

**Authorities:** MS-PROT-021, MS-PROT-022 and composite MS-PROT-040 through v1.8.

---

## 11. Applicability
**Semantic Applicability** asks:

> Does this functionality make semantic/business sense for this merchant's resolved operating model?

It is determined from accepted semantic configuration/resolution.

Applicability does not manufacture entitlement.

---

## 12. Commercial Entitlement

**Commercial Entitlement** asks:

> Is this merchant commercially permitted to use this GrandRue functionality now?

**Authority:** MS-PROT-056.

Entitlement does not create semantic applicability, actor privileges or provider readiness.

### Storefront Publication Selection

Under MS-PROT-036 v1.4 §3, an immutable Storefront-owned selection fact identifying one Composition Revision or `NO_COMPOSITION` for an exact merchant storefront. It is distinct from capability-owned Publication, source Exposure, hostname binding and website-delivery permission.

### Commercial Access Binding

**Authority:** MS-PROT-056 v1.9 §3.

The Commercial-owned association between one stable entitlement identity and one exact, already-governed access point for one protected commercial purpose.

### MAINTAIN_MERCHANT_OFFERING_DEFINITION

**Authority:** MS-PROT-044 v1.3.

The protected Commercial purpose for otherwise-valid establishment or material revision of Offering-owned merchant proposition truth. It is allocated to FREE + BUSINESS + GROWTH for the current standard catalogue. It does not grant Product, Inventory, Ordering, Booking, Appointment, Payment, Quotation, Invoicing, Exposure, Storefront or Actor Authorisation.

### MAINTAIN_MERCHANT_PRODUCT_DEFINITION

**Authority:** MS-PROT-044 v1.3.

The protected Commercial purpose for otherwise-valid establishment or material revision of Product and, where semantically justified, ProductVariant definition truth. It is allocated to FREE + BUSINESS + GROWTH for the current standard catalogue. Product remains optional, ProductVariant remains Product-owned, and this purpose does not grant Inventory, transaction, Exposure, Storefront or Actor Authorisation.

### MAINTAIN_MERCHANT_LISTING_DEFINITION

**Authority:** MS-PROT-044 v1.6.

The protected Commercial purpose for otherwise-valid creation of a new Listing or establishment of a new current immutable ListingRevision. It is allocated to FREE + BUSINESS + GROWTH for the current standard catalogue. It does not grant underlying-subject mutation, Category authority, Physical Location authority, Exposure, Storefront delivery, Enquiry, Booking, Appointment, Ordering, Payment, Quotation, Invoicing or Actor Authorisation. Listing observation and terminal withdrawal require no independent Commercial Entitlement.

### Commercial Catalogue Manifest

**Authority:** MS-PROT-056 v1.9 §3.

The complete immutable content proposed for one standard catalogue generation, including definitions, bindings, explicit plan grants and approval provenance.

### Commercial Catalogue Publication

**Authority:** MS-PROT-056 v1.9 §3.

The Commercial-owned committed fact that one exact manifest became the next published generation at its authoritative publication instant.

### Initial Standard Commercial Catalogue

**Authority:** MS-PROT-056 v1.10.

The exact approved initial immutable standard catalogue manifest identified as `standard-commercial-catalogue@1`. It contains exactly three explicit standard plan revisions:

```text
standard-plan/free@1
standard-plan/business@1
standard-plan/growth@1
```

and exactly 36 stable Commercial Entitlement identities/bindings. The explicit grant snapshots contain exactly 13 FREE, 34 BUSINESS and 36 GROWTH identities and satisfy:

```text
FREE ⊂ BUSINESS ⊂ GROWTH
```

Approval of this manifest is not Commercial Catalogue Publication and does not establish its effective start.

### Conditional Commercial Supporting Requirement

**Authority:** MS-PROT-056 v1.10.

Manifest evidence that another protected Commercial purpose is required only when the exact owner-qualified semantic condition that requires that support applies. It does not create the condition; the source owner's accepted authority remains controlling.

Canonical example:

```text
website delivery
    +
actual namespace family

PLATFORM_DELEGATED_NAMESPACE
    → USE_PLATFORM_WEBSITE_NAMESPACE

MERCHANT_CONTROLLED_DOMAIN
    → USE_MERCHANT_CONTROLLED_WEBSITE_DOMAIN
```

A conditional supporting requirement MUST NOT be flattened into an unconditional requirement for every alternative purpose.

### Initial Commercial Entitlement Identity Convention

**Authority:** MS-PROT-056 v1.10.

Initial standard-catalogue identities use:

```text
commercial-entitlement/{owner}/{bounded-target}@1
```

The identity belongs to Commercial and names one immutable exact target/protected-purpose binding. Shared protected purpose does not merge different owner-qualified targets, and unchanged repackaging does not create a new entitlement identity.

These concepts are distinct from Merchant Publication, Merchant Configuration and Semantic Registry Release publication. Source owners retain their operations, analytical definitions, service contracts, applicability rules, actor authority and business invariants.

---

## 13. Actor Authorisation

**Actor Authorisation** asks:

> May this principal perform this operation for this merchant/context?

It is independent of commercial entitlement.

A paid plan cannot grant staff privileges, and staff privileges cannot manufacture a paid entitlement.

---

## 14. Operational Eligibility

**Operational Eligibility** asks:

> Do current capability-owned business invariants permit this operation now?

Examples include current appointment capacity, lifecycle state, Inventory claim compatibility or other authoritative operational constraints.

Operational eligibility is not equivalent to entitlement or Exposure.

---

## 15. Fulfilment Routing, Provider Connection and Provider Readiness

### Fulfilment Routing Scope

**Authority:** MS-PROT-048 v1.5 within the composite MS-PROT-048 authority.
Identifies which accepted authority chooses the technical fulfiller for an already-defined Fulfilment Requirement.

The canonical routing scopes are:

```text
MERCHANT_ROUTED
PLATFORM_ROUTED
```

`MERCHANT_ROUTED` uses merchant-specific Fulfilment Binding Set authority where provider routing materially belongs to the Merchant Configuration/provider context.

`PLATFORM_ROUTED` uses shared GrandRue technical infrastructure where provider choice is not itself merchant business semantics.

Hard distinction:

```text
routing scope
    ≠ business applicability
    ≠ Fulfilment Requirement ownership
    ≠ Provider Readiness
```

### PlatformFulfilmentBindingSetRevision

**Authority:** MS-PROT-048 v1.5.
An immutable platform-scoped routing definition identifying the exact release-affined shared technical fulfilment route for registered platform-routed responsibilities.

Initial production binds an exact compatible revision to serving/deployment admission rather than using a separately mutable global `currentProvider` pointer.

It MUST NOT be represented by a fake GrandRue merchant or copied into every merchant's business configuration.

### ProviderConnection

**Authority:** composite MS-PROT-048 through v1.5.
A stable GrandRue operational identity representing one authorised relationship between an applicable GrandRue scope/responsibility and an external provider account/context.

A ProviderConnection may be merchant-scoped or platform-scoped according to accepted routing/responsibility authority.

A ProviderConnection is live integration state. It is not:

```text
Fulfilment Binding
PlatformFulfilmentBindingSetRevision
Credential Binding
raw credential/token/API key
ProviderDefinition
capability configuration
provider-wide health
```

Credential rotation does not by itself create a new ProviderConnection. Connection existence does not imply Provider Readiness.

### Provider Readiness

**Authority:** MS-PROT-048 v1.4 within the composite MS-PROT-048 through v1.5 authority.
Asks:

> Can this exact already-selected fulfilment path currently satisfy the applicable role/context/obligation responsibility safely enough to attempt execution?

The initial runtime verdict vocabulary is:

```text
READY
DEGRADED
NOT_READY
UNKNOWN
```

Provider Readiness is current, obligation-qualified runtime evidence. `UNKNOWN` is not `READY`; new provider-dependent execution fails closed where readiness cannot be established.

Hard distinction:

```text
Provider-wide Health
    ≠ ProviderConnection
    ≠ Provider Readiness

Credential usability
    ≠ Provider Readiness

Provider Readiness
    ≠ execution success
    ≠ execution certainty after an attempt
    ≠ Fulfilment Binding
    ≠ PlatformFulfilmentBindingSetRevision
    ≠ capability applicability
```

Provider readiness changes shall not silently mutate merchant configuration, capability semantics, merchant Fulfilment Binding Set revisions or platform binding revisions.

---

## 16. Surface and Exposure

Surface membership/composition and Exposure are distinct.

A Surface contribution or contextual Surface-eligibility decision determines whether an already-registered contribution legitimately participates in the applicable audience experience. Projection Serviceability determines whether the required read representation can currently be produced. Exposure then determines whether an already-legitimate candidate semantic element may be observed by the current audience/context.

Canonical distinction:

```text
Surface membership / eligibility
    contribution legitimately participates

Projection Serviceability
    required representation may be served

Exposure
    candidate element may be observed

Operation authority / invokability
    independently revalidated by runtime and capability authority
```

Hard rule:

> **Exposure governs observation only. It does not make an operation invokable or executable, and it does not create Surface membership.**

The phrase `Surface Exposure` should not be used as a combined authority where it could collapse these distinct decisions.

---

## 17. Execution Decision

`Execution Decision` is a cross-cutting architectural phrase, not a mandated universal software component.

Conceptually, execution may require applicable dimensions such as:

```text
semantic applicability
+
commercial entitlement where required
+
actor authorisation
+
resource protection admission where applicable
+
operational eligibility
+
trust/provider prerequisites where applicable
```

This lexicon does **not** authorise a universal `EligibilityEngine` or similar central domain mechanism.

---

## 18. Projection

A Projection is a derived/read representation of authoritative state for a purpose or audience.

It is not mutation authority merely because it displays current data.

Stale projections shall not authorise business commitments.

**Authorities:** MS-PROT-027, MS-PROT-059 and applicable capability authorities.

### Bounded Projection Read

**Authority:** MS-PROT-027 v1.13.
An immutable request-bound Projection observation containing exact typed Projection material together with the exact source evidence and runtime affinity used by the governed bounded response path.

Canonical distinction:

```text
Bounded Projection Read
    ≠ authoritative business state
    ≠ Exposure membership
    ≠ mutation authority
    ≠ continuation identity
    ≠ guarantee of globally latest state through final response transmission
```

Its opaque bounded-read binding identifies one exact runtime observation only. Audience representation must select exposed material from that same bounded read rather than treating Exposure membership as permission to reacquire owner state.

### Public Interaction Binding

**Authority:** MS-PROT-049 v1.2.
A derived audience-safe projection association stating that an exposed semantic subject may participate in a registered public interaction under the merchant's current resolved semantics.

It is not an Operational Object and it owns no subject-operation participation truth.

Canonical distinction:

```text
Public Interaction Contribution
    merchant-level surface interaction applicability

Public Interaction Binding
    subject-level derived participation projection

Operational Eligibility / Availability
    current capability-owned execution conditions

Execution Authority
    backend/runtime decision and capability-owned mutation authority
```

A Public Interaction Binding MUST fail closed where no accepted semantic/configuration/typed-relationship owner establishes the subject-interaction participation fact. It MUST NOT be inferred from merchant category, subject type, UI route, presentation label, provider name or AI guess.

---

## 19. Order and Ordering

These terms are high-risk because purchase commitment, Inventory, Payment, Order Fulfilment and generic customer work are independently owned.

### Order

**Authority:** composite MS-PROT-077 through v1.1.
A merchant-scoped Operational Object representing an accepted purchase/order commitment containing one or more durable Order Commitment Portions.

An Order is **not** a universal customer-work object.

Canonical distinction:

```text
Order
    purchase/order commitment

Booking
    reservation commitment

Appointment
    scheduled-service commitment

Enquiry
    request for information/contact/further action
```

### Ordering

**Authority:** composite MS-PROT-077 through v1.1.
The capability/semantic owner of Order identity, committed subjects/quantities/terms, durable Order Commitment Portions and Order-side amendment/release truth.

Ordering MUST NOT be used as a synonym for:

```text
Inventory
Payment
Order Fulfilment
Shipment
Checkout UI
shopping basket
```

### Order Commitment Portion

**Authority:** composite MS-PROT-077 through v1.1.
A durable identity-bearing portion of an Order commitment used to preserve what was committed and to provide a stable subject for later fulfilment, release/amendment, payment relationship or other governed consequence.

`Order Line` may be presentation/implementation terminology only where it maps exactly to the accepted portion semantics.

### Order Amendment

**Authority:** MS-PROT-077 v1.1.
An immutable Ordering-owned fact associated with one Order that preserves explicit release quantities and any newly established commitment portions/provenance without destructively changing the original Order Commitment Portions.

Canonical examples:

```text
quantity 5 → 3
    original 5 survives
    amendment releases 2

quantity 5 → 7
    original 5 survives
    amendment establishes new commitment quantity 2
```

A release-only amendment is the authoritative Order-side fact behind presentation such as cancelling an item or all remaining commitment. `Cancel Order` is not a universal terminal lifecycle state and does not imply Refund, stock movement, Shipment cancellation or erasure of historical Order truth.

### Business Order

**Class:** historical ADR-005 terminology.
**Rule:** MUST NOT be used as the current universal semantic owner for every customer work item. MS-PROT-077 supersedes that universalisation scope. A unified POS/dashboard is surface/application composition, not evidence that Booking, Appointment, Enquiry and Order share one owner.

Hard distinctions:

```text
Order ≠ Inventory Claim
Order ≠ Payment Obligation
Order ≠ provider transaction
Order ≠ Order Fulfilment
Order ≠ Shipment
Order ≠ CustomerAccount
Order Amendment ≠ Refund
Order release ≠ Inventory stock increment
```

---

## 20. Commitment

`Commitment` is a broader cross-domain term used only when a rule genuinely applies to multiple durable business commitments.

Examples include:

```text
Booking commitment
Appointment commitment
Order commitment
Payment obligation
```

The broad term does not erase the owning capability's specific semantics.

---

## 21. Resource Protection, quota and restriction

These terms are high-risk because operational fairness, Commercial allowances and account/security consequences are separately owned.

### Resource Protection Admission

**Authority:** MS-PROT-073 and MS-PROT-062 v1.1.
Asks whether attempted work is currently admitted to consume applicable protected platform resources.

It is not Actor Authorisation, Commercial Entitlement, Operational Eligibility or Provider Readiness.

### Operational Fairness Limit / Protection Limit

Use for a limit whose purpose is protection of shared platform capacity or tenant fairness under MS-PROT-073.

Do not call such a limit a Commercial quota merely because it is numeric.

### Commercial Usage Quota / Commercial Usage Allowance

**Authority class:** Commercial authority, including MS-PROT-056 where applicable.
Use for a purchased/contractual allowance or commercial usage boundary.

Canonical distinction:

```text
Operational Fairness Limit
    protects shared platform resources

Commercial Usage Quota
    defines purchased/contractual usage rights
```

Bare `merchant quota` is unsafe in cross-domain normative prose; qualify the owner and purpose.

### Temporary Protective Restriction

**Authority:** MS-PROT-073.
A bounded protection-specific restriction on a Protection Subject / Protection Target.

It MUST NOT be called or treated as `Merchant Account suspension`, `identity revocation`, `commercial cancellation` or `permanent ban` unless the applicable owning authority independently establishes that consequence.

---

## 22. Workforce membership, scheduling and access

These terms are high-risk because Identity, merchant-control authority, workforce delegation, Workforce Scheduling/Timekeeping, compensation and device trust are independently owned.

### Merchant Membership

**Authority:** composite MS-PROT-074 through v1.2.
A merchant-scoped workforce relationship through which an Identity may receive Merchant Role Assignments and participate in separately authorised workforce self-service.

It is not legal-employment truth, Merchant Controller authority, a Workforce Scheduling Arrangement, a Compensation Relationship or Payroll participation.

Canonical distinction:

```text
Identity
    who is authenticated

Merchant Membership
    workforce relationship to one Merchant Scope

Workforce Scheduling Arrangement
    one coherent scheduling/time/leave affinity for that Membership

Merchant Controller relationship
    merchant-control authority

Compensation Relationship
    separately governed compensation relationship
```

`Staff` may be used as a merchant-facing/business classification for a workforce member but MUST NOT substitute for the underlying Identity/Membership/Arrangement/Role/Compensation distinctions where authority matters.

### Workforce Scheduling Arrangement

**Authority:** composite MS-PROT-081 through v1.1.
A durable merchant-scoped scheduling relationship connecting one Merchant Membership to one coherent set of Workforce Time Terms and, where applicable, one exact Compensation Relationship affinity.

One Merchant Membership may have multiple Workforce Scheduling Arrangements. Multiple Arrangements may reference the same Compensation Relationship where valid. The Arrangement is scheduling/time/leave affinity only; it is not employment status, an employment contract, a Payroll Participant, a Compensation Relationship or general HR relationship.

Hard distinction:

```text
Merchant Membership
    ≠ Workforce Scheduling Arrangement

Workforce Scheduling Arrangement
    ≠ Compensation Relationship
    ≠ employment/legal status
```

### Merchant Role Assignment

**Authority:** composite MS-PROT-074 through v1.2.
An authoritative merchant-scoped assignment of a role/profile composed from registered privileges to a Membership or Merchant Access Group and an applicable scope.

A job title or role label alone is not authority. Membership-relative Personal Workforce Self-Service Actor Authorisation is a separate own-subject rule and is not an implicit Merchant Role Assignment.

### Merchant Operational Device Authorisation

**Authority:** composite MS-PROT-074 through v1.2; trusted-context consequences in composite MS-PROT-063 through v1.2.
An explicit Merchant Controller-established authorisation permitting a specific device/application context to establish staff merchant-operational contexts for one Merchant Scope.

It MUST NOT be shortened to `trusted device` in normative cross-domain prose where that wording could imply global hardware trust, device ownership or actor authority.

Hard distinction:

```text
Merchant Operational Device Authorisation
    ≠ staff authentication
    ≠ Merchant Membership
    ≠ Merchant Role Assignment
    ≠ Merchant Controller Session
    ≠ Personal Workforce Self-Service Context
```

### Membership Security Projection

A minimal private-device projection containing only information necessary for staff identity, invitation, credential/PIN and membership-security administration.

### Personal Workforce Self-Service Context

**Authority:** MS-PROT-063 v1.2, composed with composite MS-PROT-074 through v1.2 and composite MS-PROT-081 through v1.1.
A purpose-bound trusted context establishing the authenticated person's own current Merchant Membership, Merchant Scope and an explicitly permitted self-service operation. For Arrangement-specific workforce operations, exact current Workforce Scheduling Arrangement affinity also participates in authorisation. It permits only the worker's own supported schedule/time/leave self-service and does not establish general merchant-operational access.

Hard distinction:

```text
Personal Security Context
    ≠ Personal Workforce Self-Service Context
    ≠ Staff Operational Context
```

A manager role on the same Identity does not turn a personal self-service context into authority to manage another worker. Personal Workforce Self-Service Context does not by itself grant Compensation/pay-document access; MS-PROT-080 requires exact related-Payee and Compensation Relationship authority for that purpose.

### Personal Workforce Self-Service Projection

**Authority:** composite MS-PROT-074 through v1.2 and composite MS-PROT-081 through v1.1; compensation elements additionally governed by MS-PROT-080 v1.3.
A purpose-bound personal-device projection of the authenticated workforce member's own permitted schedule, shift offers/decisions, time record, break events and leave information.

Permitted compensation/pay documents may participate only where MS-PROT-080 independently establishes exact related-Payee/Compensation Relationship authority plus applicable privacy/Exposure. Membership or self-service context alone is insufficient.

It is not a Merchant Operational Projection.

### Merchant Operational Projection

Merchant-owned operational/business information presented to authorised merchant actors for merchant operation/administration.

For staff, normal Merchant Operational Projections require the applicable Staff Operational Context and Merchant Operational Device Authorisation. Personal Workforce Self-Service Projection is a separately governed narrower projection and does not weaken that requirement.

Canonical distinction:

```text
Membership Security Projection
    own identity/security administration

Personal Workforce Self-Service Projection
    own purpose-bound schedule/time/leave self-service
    + separately authorised own Compensation elements where applicable

Merchant Operational Projection
    merchant-wide or business-operational information
    requiring applicable staff operational-device context
```

### Workforce Scheduling

**Authority:** composite MS-PROT-081 through v1.1.
The workforce authority that establishes planned work through direct assignment or offer/acceptance according to the exact Workforce Scheduling Arrangement's applicable Workforce Time Terms. It is distinct from Appointment Scheduling.

### Scheduled Work Commitment

**Authority:** composite MS-PROT-081 through v1.1.
The authoritative merchant-scoped fact that one Merchant Membership is scheduled to perform work over one explicit interval under one exact shift revision and one exact Workforce Scheduling Arrangement. It includes the applicable Scheduled Breaks but is not proof that work occurred.

Canonical distinction:

```text
Shift Offer
    proposed work requiring worker decision where applicable

Scheduled Work Commitment
    arrangement-qualified authoritative planned work commitment

Attendance / Worked-Time Evidence
    evidence of what actually occurred
```

### Scheduled Break / Actual Break / Worked Time

**Authorities:** composite MS-PROT-081 through v1.1; compensation consequence in composite MS-PROT-080 through v1.3.
A Scheduled Break is part of the Scheduled Work Commitment. Actual Break derives from accepted Timekeeping evidence. Worked Time excludes actual break intervals.

Hard distinctions:

```text
Scheduled Break
    ≠ Actual Break
    ≠ Worked Time
    ≠ break-related Compensation consequence

Excess Break Time
    ≠ Worked Time
    ≠ time-based compensation input
```

No generic `Paid Break Entitlement` authority is canonical. Whether and how break time contributes monetarily is a Workforce Compensation-owned consequence derived from exact break evidence, Compensation Terms and jurisdiction rules.

### Approved Worked-Time Evidence

**Authorities:** composite MS-PROT-081 through v1.1 and composite MS-PROT-080 through v1.3.
The Timekeeping-owned approved evidence of actual Worked Time. It retains exact Workforce Scheduling Arrangement affinity and, where that Arrangement references a Compensation Relationship, Workforce Compensation consumes that exact affinity rather than locating a current relationship later.

It is not itself a Compensation Amount, break-related compensation consequence or leave-pay consequence.

### Approved Leave / Leave Pay

**Authorities:** composite MS-PROT-081 through v1.1 for Leave; composite MS-PROT-080 through v1.3 for compensation consequence.
Approved Leave is authoritative workforce-absence/availability evidence bound to one exact Workforce Scheduling Arrangement. It does not itself calculate or guarantee Leave Pay. Where compensation applies, the Arrangement supplies exact Compensation Relationship affinity to Workforce Compensation.

Canonical distinction:

```text
Approved Leave
    ≠ Leave Pay

Leave under Arrangement A
    ≠ automatically Leave under Arrangement B

Payroll participation
    ≠ leave eligibility
```

### Related-Payee Compensation Requirement

**Authority:** MS-PROT-080 v1.3.
The owner-qualified relationship requirement conceptually identified as:

```text
workforce-compensation / related-payee-compensation
```

It asks whether the current trusted principal is authoritatively related to the exact Payee and exact Compensation Relationship for the requested Compensation self-service purpose.

It is not blanket Exposure, Actor Authorisation by itself, Merchant Membership or the Compensation Relationship itself. Matching name, email, phone or bank details cannot independently satisfy it. Generic former/non-member/legal-entity Payee transport, audience and representative mechanics remain deferred under MS-PROT-080-V11-DQ-013.

---

## 23. Notification intent, dispatch, delivery attempt and delivery evidence

These terms are high-risk because business facts, logical communication responsibility, transport effects, provider evidence and business acknowledgement are independently owned.

### Notification Intent

**Authority:** composite MS-PROT-075 through v1.1.
One logical durable communication responsibility established under a Notification Contract.

It is not the Domain Event or business fact that caused the communication.

Canonical distinction:

```text
Domain Event / business fact
    authoritative fact/history

Notification Intent
    logical communication responsibility
```

### Notification Preference

**Authority:** composite MS-PROT-075 through v1.1.
A recipient/context preference that may narrow or rank already-permitted notification delivery choices where the Notification Contract allows preference control.

It MUST NOT be used as a synonym for marketing consent, lawful communication authority or Exposure permission.

### Notification Dispatch

**Authority:** composite MS-PROT-075 through v1.1.
One logical recipient-and-channel delivery obligation derived from a Notification Intent.

A Dispatch is not one physical provider request.

Changing channel requires a distinct Dispatch under the same Notification Intent where independently authorised; an EMAIL DeliveryAttempt does not mutate into SMS.

### Notification message-delivery fulfilment role

**Authority:** MS-PROT-075 v1.1, composed with composite MS-PROT-048 through v1.5.
The canonical provider-neutral technical role is:

```text
notification / message-delivery
```

Provider/internal fulfilment is qualified by the exact Notification Channel context, such as EMAIL, SMS, PUSH or IN_APP where supported.

The fulfilment path answers how an already-authorised Dispatch may be delivered; it does not create communication permission or the business reason for the Notification Intent.

### Delivery Attempt

**Authority:** composite MS-PROT-075 through v1.1.
A durable Notification-owned identity allocating one logical physical delivery effect against one exact Notification Dispatch and one exact fulfilment path. It is durably established before an external provider side effect begins.

One Dispatch may have multiple separately authorised Delivery Attempts. Same-attempt transport replay retains the same attempt/provider-effect identity; a genuinely new physical effect receives a new DeliveryAttempt identity.

### Provider Effect Identity

**Authority:** MS-PROT-075 v1.1.
The stable idempotency/correlation identity for the exact logical provider effect represented by one DeliveryAttempt.

Hard distinction:

```text
Notification Dispatch identity
    logical recipient+channel obligation

DeliveryAttempt identity
    logical physical attempt

Provider Effect Identity
    exact external provider effect correlation/idempotency
```

Dispatch identity MUST NOT be treated as the universal provider idempotency key where multiple legitimate physical attempts may exist.

### Delivery Evidence

**Authority:** composite MS-PROT-075 through v1.1.
Notification-owned evidence describing what GrandRue can substantiate about a Notification Dispatch/DeliveryAttempt after authenticated, correlated and registered interpretation of synchronous or asynchronous provider-native evidence.

Canonical distinction:

```text
provider accepted
    ≠ provider reported delivered

provider reported delivered
    ≠ recipient read/opened

recipient read/opened
    ≠ business acknowledgement
```

Provider-native statuses and callbacks are evidence until admitted through the Notification evidence boundary. Duplicate provider evidence must not multiply semantic consequences.

Bare normative phrases such as `notification sent`, `customer notified` or `message delivered` are unsafe where transport/evidence semantics matter; qualify the exact evidence or lifecycle fact.

### Notification Read State

Presentation state such as `READ` / `UNREAD` belongs to the Notification projection.

It MUST NOT be interpreted as capability-owned acknowledgement, acceptance, consent or completion unless a separately authorised business operation establishes that fact.

### Notification fallback

**Authority:** MS-PROT-075 v1.1.
Same-channel provider fallback and cross-channel fallback are distinct. Same-channel fallback requires an accepted alternate fulfilment path plus safe prior-attempt resolution/duplicate semantics. Cross-channel fallback requires a separately authorised Dispatch for the new channel.

An `EXECUTION_UNCERTAIN` prior attempt blocks blind provider or channel fallback unless reconciliation or a separately accepted duplicate-safe contract establishes another effect is safe.

---

## 24. Merchant Account lifecycle, suspension and control

These terms are high-risk because Merchant Account existence, lifecycle, account-wide restriction, Controller relationship and authentication credentials are independently owned.

### Merchant Account Lifecycle

**Authority:** MS-PROT-076.
The post-establishment tenancy lifecycle:

```text
OPEN → CLOSING → CLOSED
```

`OPEN` does not mean configured, entitled, verified, published or operationally ready. `CLOSED` is terminal but does not mean physically deleted.

### Merchant Account Suspension

**Authority:** MS-PROT-076.
An explicit merchant-wide restriction justified by an accepted merchant-wide security, legal, account-control or administrative authority.

It is orthogonal to Merchant Account Lifecycle and MUST NOT be used as a synonym for:

```text
Temporary Protective Restriction
Commercial cancellation / payment failure
Provider failure
Staff Membership suspension
narrow TrustRequirement failure
```

### Merchant Controller Relationship

**Authorities:** MS-PROT-028, MS-PROT-071 v1.1, MS-PROT-076.
The GrandRue account-control relationship linking one authenticated Identity to ultimate control of one non-closed Merchant Account.

In MS-PROT-076 v1.0, every OPEN/CLOSING Merchant Account has exactly one ACTIVE Merchant Controller relationship. It does not prove legal ownership of the real-world business.

### Merchant Controller Credential / Session

Authentication material or Session continuity belonging to the Controller Identity/security context.

Canonical distinction:

```text
Controller password/credential reset
    ≠ Merchant Controller relationship transfer
    ≠ Merchant Account Suspension
    ≠ Staff session invalidation
    ≠ Merchant Operational Device deauthorisation
```

Ordinary credential reset may invalidate the Controller Identity's affected sessions while the Merchant Account, Controller relationship, staff access and business operations continue under their own authorities.

### Merchant Control Recovery

**Authority:** MS-PROT-076, with trust/security boundaries from MS-PROT-028/MS-PROT-064.
An exceptional path for replacing/restoring ultimate GrandRue merchant control when ordinary current-Controller transfer cannot establish the required change.

It is distinct from credential recovery and from normal Controller transfer.

### CLOSING vs CLOSED

```text
CLOSING
    terminal progression; no new ordinary business activity;
    purpose-bound existing-commitment resolution may continue

CLOSED
    terminal Merchant Account lifecycle;
    no ordinary merchant operation or active Merchant Controller
```

Neither state automatically deletes historical business/evidence data; post-closure disposition remains governed by MS-PROT-053.

---

## 25. Media source, rendition and purpose-bound personal-media use

These terms are high-risk because source preservation, delivery optimisation, Exposure and personal-data-use authority are independently owned.

### Canonical Media Source

**Authority:** composite MS-PROT-066.
The preserved merchant source asset from which authorised technical renditions may be derived.

It is not a storage URL/key and it is not replaced semantically by a smaller/encoded delivery representation.

### Media Rendition

**Authority:** composite MS-PROT-066.
A deterministic derived technical representation of a Canonical Media Source under a registered processing profile/version.

Canonical distinction:

```text
Canonical Media Source
    preserves source asset

Media Rendition
    optimises authorised delivery/use
```

`Media Rendition` MUST NOT be treated as a new business asset merely because its encoding, size, dimensions or delivery characteristics differ.

`lossless` and `controlled lossy` describe registered technical fidelity policy; neither term creates Exposure or business authority.

### INFORMATION_VIDEO

**Authority:** MS-PROT-066 v1.2 within the composite MS-PROT-066 authority.
A media role for informational video that may use registered long-form delivery profiles.

It MUST NOT inherit the 30-second limit of PRODUCT_VIDEO or short PROFILE/PROMOTIONAL profiles merely because the media type is video.

### PersonalDataUseBasis

**Authority:** MS-PROT-053 v1.1 within the composite MS-PROT-053 authority.
A bounded authority/evidence record used by applicable governed policy to establish whether a defined scope of personal information may be used for a defined purpose and audience.

Hard distinction:

```text
PersonalDataUseBasis
    ≠ universal consent
    ≠ parentConsent boolean
    ≠ Exposure
    ≠ MediaAsset existence
    ≠ CustomerContext
    ≠ guardian/legal-representative truth
```

### PUBLIC_PROMOTIONAL_EXPOSURE

**Authority:** MS-PROT-053 v1.1.
A purpose for public/promotional use of personal information/media, such as a merchant website gallery or other separately supported public promotional distribution.

Possession or operational use of the data does not imply this purpose is authorised.

### DataSubjectReference

A bounded subject reference used where needed to associate applicable personal-data use requirements/evidence without manufacturing a global GrandRue account or broader business relationship.

Canonical distinction:

```text
DataSubjectReference
    ≠ Identity
    ≠ CustomerAccount
    ≠ CustomerContext
    ≠ Merchant Membership
```

Where no applicable personal-data use requirement exists, GrandRue must not create fictitious subject/basis semantics merely because an asset is media.

---

## 26. Payment obligation, execution, application and refund

These terms are high-risk because commercial commitment, obligation, provider execution, evidence, settlement and refund are independently meaningful.

**Authority:** composite MS-PROT-055 through v1.1.

### Payment Obligation

A durable merchant-scoped Payment-owned business fact that a defined monetary amount is required to be discharged in relation to one exact owner-qualified commercial source under accepted payment/commercial semantics.

The original obligation is immutable historical truth. Later authorised amount changes use Payment Obligation Adjustment facts rather than destructive mutation.

### Payment Obligation Adjustment

An immutable Payment-owned `INCREASE` or `DECREASE` fact changing the effective amount of an existing Payment Obligation while preserving the original obligation and source provenance.

An adjustment is not automatically a Refund, credit or source-commitment mutation.

### Payment Due Evaluation

The deterministic current result of the obligation's registered Payment Due Condition Contract:

```text
DUE
NOT_DUE
UNRESOLVED
```

Hard distinction:

```text
outstanding obligation amount
    ≠ current amount due
```

A future obligation may be outstanding while `NOT_DUE`. `UNRESOLVED` is not `DUE` and cannot authorise ordinary payment execution.

### PaymentExecutionRequest

A durable Payment-owned progression identity representing one logical request to discharge one or more currently payable Payment Obligations through an already-selected `payment / payment-execution` fulfilment path.

It exists before the external financial side effect and retains exact amount, obligation-target, correlation and provider/binding provenance. It is not the provider transaction or PaymentApplication.

### ProviderPaymentEvidence

Authenticated/correlated external evidence describing provider-side payment execution. Provider-native result/status vocabulary remains evidence and must pass a registered provider interpretation before it may support GrandRue payment truth.

### PaymentApplication

An immutable Payment-owned interpretation fact applying a bounded amount from accepted ProviderPaymentEvidence to one Payment Obligation. It does not mutate either source.

Canonical cardinality permits multiple applications per obligation and multiple applications from provider evidence where bounded by accepted amount/currency rules.

### Provider Settlement

Provider-side financial evidence describing what the provider settles to the merchant. It is not customer payment amount and does not rewrite the Payment Obligation or PaymentApplication.

### RefundExecutionRequest

A durable Payment-owned progression identity representing one authorised external refund side effect under the `payment / refund-execution` fulfilment responsibility. It retains historical provider/binding affinity and is not the Refund fact itself.

### Refund

A durable Payment-owned fact establishing that a specific amount previously received through an accepted payment path was subsequently returned through authorised refund execution.

A Refund does not delete/reverse the original PaymentApplication, does not rewrite the original Payment Obligation and does not automatically cancel or reinstate the source business commitment.

Hard distinctions:

```text
Business commitment
    ≠ Payment Obligation

Payment Obligation
    ≠ PaymentExecutionRequest

PaymentExecutionRequest
    ≠ provider transaction

ProviderPaymentEvidence
    ≠ PaymentApplication

PaymentApplication
    ≠ Provider Settlement

RefundExecutionRequest
    ≠ Refund

Refund
    ≠ cancellation
    ≠ obligation reversal
```

A universal authoritative `Payment.status` is not accepted; convenient payment labels are derived projections over the underlying facts.

---

## 26A. Jurisdiction and regulatory administration

These terms are high-risk because merchant location, jurisdiction resolution, regulatory support, provider readiness, regulatory administration and capability-owned obligations remain independently meaningful.

**Authority:** MS-PROT-082 through v1.1, composed with composite MS-PROT-048 for Provider Fulfilment/Provider Readiness and the applicable capability authorities for source business facts.

### Jurisdiction / Jurisdiction Context

A **Jurisdiction** is a stable JRA-owned reference to a governmental, statutory or regulatory territorial authority boundary that may participate in a registered Regulatory Purpose.

A **Jurisdiction Context** is an immutable DERIVED, purpose-qualified result resolving the exact Jurisdictions relevant to one Regulatory Purpose, Regulatory Subject Reference and governing-time context from exact owner-qualified source facts.

Hard distinctions:

```text
MerchantLocation
    ≠ Jurisdiction
    ≠ Jurisdiction Context

merchant address/country
    ≠ universal regulatory jurisdiction

Jurisdiction hierarchy
    ≠ automatic regulatory applicability
```

There is no universal authoritative `merchant.taxJurisdiction` shared by every capability. MerchantLocation and other capability-owned facts may supply source evidence but remain owned by their accepted authorities.

### Regulatory Determination

An immutable DERIVED JRA record of one Regulatory Purpose evaluation bound to exact source-fact revisions, exact Jurisdiction Context, governing time, exact Regulatory Rule Set Release and exact evaluator/provider evidence.

A Regulatory Determination is GrandRue's traceable evaluation result. It is not legislation, legal advice, a provider-native status or authority to mutate the source capability facts.

### Regulatory Administrative Requirement / Payment Obligation

A **Regulatory Administrative Requirement** is a JRA-owned merchant-specific tracking record establishing that, according to an accepted Regulatory Determination, an administrative regulatory action must be performed, evidenced or resolved.

The fully qualified term is mandatory in normative cross-domain prose. It MUST NOT be shortened to bare `Obligation` where that could collide with accepted domain concepts.

Canonical distinction:

```text
Regulatory Administrative Requirement
    ≠ Payment Obligation
    ≠ Payroll obligation/consequence
    ≠ Booking/Appointment commitment
    ≠ Order
    ≠ customer commitment
```

A regulatory amount or filing requirement does not itself authorise funds movement. Payment and other capability-owned consequences remain with their accepted owners.

### Jurisdiction Support Class / Provider Readiness

A **Jurisdiction Support Class** is the platform's purpose-specific support declaration for one exact Regulatory Purpose + Jurisdiction scope + Jurisdiction Pack Release + effective support interval. The canonical classes are:

```text
NATIVE
PROVIDER_BACKED
ASSISTED
ESCALATION_REQUIRED
UNSUPPORTED
```

It is not current Provider Readiness.

Hard distinction:

```text
Jurisdiction Support Class
    ≠ Semantic Applicability
    ≠ Commercial Entitlement
    ≠ Actor Authorisation
    ≠ Operational Eligibility
    ≠ Provider Readiness
    ≠ Input Completeness
    ≠ Surface Exposure
```

`PROVIDER_BACKED` with an unavailable provider means the purpose is supported in principle but cannot currently be fulfilled through that path. Provider outage therefore does not silently rewrite jurisdiction support semantics.

---

## 26B. Business Intelligence and Business Health

These terms are high-risk because source business facts, derived analytical meaning, platform Operational Health, Merchant Attention, governance recommendations and executable merchant instructions remain independently owned.

**Authority:** composite MS-PROT-083 through v1.3, composed with composite MS-PROT-068 for Operational Health, MS-PROT-043 for Merchant Attention, composite MS-PROT-027 for Projection/Exposure and the applicable capability authorities for source business facts.

### Business Health

**Business Health** is derived analytical assessment/profile meaning describing the condition or performance of the merchant's business under governed Analytical Measure and Business Health Indicator definitions.

Hard distinction:

```text
Business Health
    ≠ Operational Health
    ≠ Provider Health / Provider Readiness
    ≠ Merchant Attention
    ≠ source business fact
```

`Business Health` MUST be qualified where `health` could otherwise be confused with MS-PROT-068 platform/provider Operational Health. MS-PROT-083 v1.0 does not establish a universal 0–100 Business Health score.

### Analytical Claim

An **Analytical Claim** is an atomic governed analytical proposition supported by exact analytical evidence. A material claim is classified as:

```text
OBSERVED
DERIVED
INFERRED
UNKNOWN
```

Hard distinction:

```text
Analytical Claim
    ≠ source business fact

INFERRED
    ≠ OBSERVED

UNKNOWN
    ≠ known zero
```

### Scenario Evaluation

A **Scenario Evaluation** is an explicitly hypothetical analytical evaluation under stated assumptions.

Canonical distinction:

```text
Scenario Evaluation
    ≠ Business Forecast

Scenario assumption
    ≠ Merchant Configuration
    ≠ Merchant Intent
    ≠ Command
```

A merchant asking what would happen under a hypothetical change does not thereby instruct GrandRue to perform that change.

### Business Recommendation

A **Business Recommendation** is a non-authoritative decision-support artifact suggesting that the merchant consider an action on the basis of governed analytical evidence.

The fully qualified term should be used in cross-domain normative prose where `recommendation` could collide with design/governance lifecycle terminology.

Hard distinction:

```text
Business Recommendation
    ≠ design recommendation
    ≠ governance recommendation
    ≠ Merchant Intent
    ≠ Actor Authorisation
    ≠ Commercial Entitlement
    ≠ Operational Eligibility
    ≠ Command
```

A merchant's acceptance or modification of a Business Recommendation creates a new merchant instruction that must still pass ordinary current capability/runtime authority.

### Recommendation Premise Assessment

A **Recommendation Premise Assessment** is the current analytical evaluation of whether the evidence and method premises underlying a retained Business Recommendation remain sufficiently current.

The canonical result vocabulary is:

```text
PREMISES_CURRENT
PREMISES_STALE
PREMISES_UNRESOLVED
```

Hard distinction:

```text
Recommendation Premise Assessment
    ≠ Projection Serviceability
    ≠ Actor Authorisation
    ≠ Commercial Entitlement
    ≠ Operational Eligibility
```

`PREMISES_CURRENT` means only that the analytical premises remain current under the Recommendation Definition. It does not establish that the recommended operation may execute.

---

## 26C. Merchant Attention and Governed Action Handoff

These terms are high-risk because source business truth, handling coordination, notification, operational alerting, analytical recommendation and executable instruction remain independently owned.

**Authority:** composite MS-PROT-085 through v1.1, composed with the applicable source owner, composite MS-PROT-075 for Notification, composite MS-PROT-068 for Operational Health/Alerts, composite MS-PROT-069 for Reconciliation, MS-PROT-083 for Business Intelligence and composite MS-PROT-027 for Projection/Exposure.

### Attention Contract

An **Attention Contract** is the versioned owner-qualified semantic definition under which one registered source family may contribute a handling episode to Merchant Attention.

It is not a generic workflow definition and does not transfer source ownership.

### Attention Contribution

An **Attention Contribution** is source-owner-qualified evidence or a request for Attention evaluation.

Hard distinction:

```text
Attention Contribution
    ≠ Merchant Attention Occurrence
    ≠ source business fact
    ≠ command
```

### Merchant Attention Occurrence

A **Merchant Attention Occurrence** is a durable Attention-owned fact that one qualifying source episode requires or required handling in one exact Merchant Scope.

Hard distinction:

```text
Merchant Attention Occurrence
    ≠ Operational Alert
    ≠ Notification
    ≠ Business Recommendation
    ≠ Reconciliation
    ≠ source lifecycle state
```

### Attention Handling Fact

An **Attention Handling Fact** is immutable evidence of an Attention-owned acknowledgement, assignment, snooze or contract-defined disposition.

```text
Attention handling
    ≠ source resolution
    ≠ Notification read
    ≠ actor authorisation
```

### Candidate Action

A **Candidate Action** is a safe reference to a registered source-owner operation or explicit external handling path that an eligible actor may consider.

```text
Candidate Action
    ≠ Merchant Intent
    ≠ Actor Authorisation
    ≠ Operational Eligibility
    ≠ Command
    ≠ execution result
```

Selection requires a new instruction and current runtime authority before any source-owner operation may execute.

### Attention Coverage

**Attention Coverage** qualifies which applicable registered source families were evaluable for one Attention observation and with what serviceability.

An empty visible Attention result is not evidence that every business source was checked or that the business is safe.

---

### Initial Enquiry Review

**Authority:** MS-PROT-085 v1.1 — `enquiry / initial-submission-review@1`.

`INITIAL_REVIEW_RECORDED` is an immutable Attention-only disposition recording that an eligible Merchant Controller explicitly reviewed one Enquiry's initial submission. It is not acknowledgement, Notification read, a customer response, Enquiry resolution or creation of a business commitment.

`RecordInitialEnquiryReview` is the Attention-owned operation that records that disposition with exact occurrence/revision affinity and logical operation identity. The contract selects no staff assignment, snooze, source-operation Candidate Action or historical backfill.

---

## 26D. Customer Messaging and Customer-Service Handoff

These terms are high-risk because Enquiry truth, durable communication continuity, channel delivery, human handling, protected source facts and AI assistance remain independently owned.

**Authority:** MS-PROT-086 v1.0, composed with composite MS-PROT-043 for Enquiry, MS-PROT-059 for interaction channels, composite MS-PROT-075 for Notification, MS-PROT-085 for Merchant Attention, composite MS-PROT-057 for AI and the applicable source owner, access and Projection/Exposure authorities.

### Conversation Creation Contract

A **Conversation Creation Contract** is the immutable versioned owner-qualified semantic definition governing when durable communication creates a new Conversation, links an existing Conversation, requires no Conversation, rejects or remains unresolved.

### Conversation

A **Conversation** is the merchant-scoped durable continuity identity for accepted customer/merchant communication.

Hard distinction:

```text
Conversation
    ≠ Enquiry
    ≠ Notification
    ≠ Merchant Attention Occurrence
    ≠ source business aggregate
    ≠ provider thread
    ≠ universal open/closed case
```

### Conversation Relationship

A **Conversation Relationship** is an owner-qualified link showing that communication materially concerns an exact subject under a defined relationship kind.

```text
Conversation Relationship
    ≠ source access
    ≠ source mutation authority
    ≠ participant identity
    ≠ lifecycle coupling
```

### Conversation Participant Binding

A **Conversation Participant Binding** records the accepted basis on which a customer-side, merchant-side or automated-assistance participant takes part in one Conversation.

A contact endpoint alone is not a participant binding, and historical participation is not current access authority.

### Guest Conversation Access Grant

A **Guest Conversation Access Grant** is a purpose-bound, time-bounded and revocable authority permitting specified guest operations for one exact Conversation.

Contact information is not a Guest Conversation Access Grant.

### Conversation Channel Binding

A **Conversation Channel Binding** relates one Conversation to one accepted merchant/provider route, channel and external thread where applicable.

```text
channel binding
    ≠ Conversation identity
    ≠ participant identity
    ≠ delivery proof
```

### Conversation Message

A **Conversation Message** is an immutable accepted communication contribution within one exact Conversation.

```text
Conversation Message
    ≠ draft
    ≠ Notification
    ≠ delivery/read evidence
    ≠ business acknowledgement
    ≠ Command
    ≠ source-operation result
```

### Customer-Service Response Contract

A **Customer-Service Response Contract** is the immutable versioned definition of the evidence, coverage, response modes, prohibited commitments, access and human-handoff rules for one registered customer-service request family.

### Customer-Service Response Assessment

A **Customer-Service Response Assessment** is communication-owned decision evidence producing one of:

```text
AUTOMATED_INFORMATIONAL_RESPONSE_ELIGIBLE
DRAFT_FOR_HUMAN_REVIEW
HUMAN_RESPONSE_REQUIRED
NO_RESPONSE_REQUIRED
UNRESOLVED
```

It is not the response, source business truth or an authority grant. AI confidence alone cannot establish automated-response eligibility.

---

## 26E. Merchant Marketing Campaign and Governed Outreach

These terms are high-risk because Campaign purpose, customer relationship, marketing permission, audience evaluation, Publication, Notification delivery, Conversation, source-business truth and analytical attribution remain independently owned.

**Authority:** composite MS-PROT-087 through v1.3, composed with composite MS-PROT-043 for CustomerContext, MS-PROT-046 for Publication/Announcement, composite MS-PROT-053 and MS-PROT-082 for data-protection/regulatory contact authority, composite MS-PROT-057 for AI, composite MS-PROT-075 for Notification, composite MS-PROT-083 through v1.1 for analytics/recommendations and Campaign measurement, MS-PROT-086 for Conversation, MS-PROT-088 for merchant sender identity and the applicable source/provider authorities.

### Marketing Campaign

A **Marketing Campaign** is a Merchant-scoped definition of one coherent marketing purpose whose execution occurs only through approved Campaign Revisions and Campaign Occurrences.

```text
Marketing Campaign
    ≠ Publication
    ≠ Notification
    ≠ Conversation
    ≠ CustomerContext
    ≠ Business Recommendation
    ≠ provider campaign object
```

### Campaign Revision

A **Campaign Revision** is an immutable version of the exact purpose, content, Audience Definition, outreach families, business references, scheduling, personalisation and suppression meaning proposed for approval or execution.

Material change requires a new revision and applicable approval.

### Audience Definition

An **Audience Definition** is a Merchant-scoped, purpose-bound, versioned and explainable definition of the source evidence, inclusion, exclusion, identity/deduplication, minimum-evidence and unresolved semantics used to derive candidate recipients.

It is not a stored permanent customer label and does not create marketing permission.

### Candidate Recipient

A **Candidate Recipient** is a subject considered under one Audience Definition.

```text
candidate recipient
    ≠ eligible recipient
    ≠ contact endpoint
    ≠ permission to communicate
```

### Marketing Recipient Eligibility Assessment

A **Marketing Recipient Eligibility Assessment** determines whether one candidate may participate in one exact Campaign Occurrence through one exact outreach family, producing exactly:

```text
ELIGIBLE
INELIGIBLE
UNRESOLVED
```

Only `ELIGIBLE` may proceed toward externalisation. Material unresolved conditions fail closed.

### Campaign Approval

A **Campaign Approval** is merchant-authorised evidence permitting one exact Campaign Revision, Audience Definition version, outreach scope, schedule/activation scope and personalisation scope.

Approval is not delivery, engagement, conversion or authority for materially changed content.

### Direct Marketing Contact Policy Determination

**Authority:** MS-PROT-087 v1.3 composed with MS-PROT-082 and applicable data-protection authority.
A **Direct Marketing Contact Policy Determination** is the current jurisdiction-qualified decision establishing whether one exact candidate/contact basis may receive one exact direct Marketing outreach, with canonical outcomes `PERMITTED`, `PROHIBITED` or `UNRESOLVED`. Audience membership, CustomerContext, known endpoint and Notification Preference do not create this authority.

### Marketing Contact Suppression

**Authority:** MS-PROT-087 v1.3 composed with applicable data-protection/regulatory authority.
A **Marketing Contact Suppression** is durable evidence that direct Marketing contact must not occur within an exact merchant/email-endpoint or merchant/CustomerContext scope. It is distinct from Campaign-specific suppression, does not delete CustomerContext and does not suppress independently governed operational/security communication.

### Automated Campaign Contract

**Authority:** MS-PROT-087 v1.3.
An **Automated Campaign Contract** is merchant-authorised standing authority for one exact Campaign Revision, admitted trigger/recurrence family, bounded effective interval and repeat policy. Initial standing authority is finite, cannot exceed 12 calendar months, and never creates standing recipient permission.

```text
Automated Campaign Contract
    ≠ Marketing permission
    ≠ current recipient eligibility
    ≠ AI autonomy
```

### Automation Schedule Occurrence Identity

**Authority:** MS-PROT-087 v1.3 composed with MS-PROT-065.
A stable identity for one logical automated Campaign due point. Duplicate scheduler wakes/retries converge on the same logical occurrence; clock passage alone does not create Campaign externalisation authority.

### Campaign Occurrence

A **Campaign Occurrence** is one exact intended execution of an approved Campaign Revision under a merchant instruction, schedule or Automated Campaign Contract.

It is not proof that any Publication or Notification external effect occurred.

### Campaign Suppression Result

A **Campaign Suppression Result** is campaign-side evidence that one candidate/outreach path must not proceed because of campaign-specific frequency, duplication, source-state, identity, protection, provider or other accepted Campaign rules. Durable recipient-originated/regulatory direct-Marketing opt-out belongs to `Marketing Contact Suppression` under MS-PROT-087 v1.3 and applicable regulatory/data-protection authority.

Suppression scope remains exact and does not erase CustomerContext or suppress independently governed operational communication.

### Campaign Outcome Observation

A **Campaign Outcome Observation** records provider-, Publication-, Notification-, interaction- or source-qualified campaign-related evidence without silently claiming engagement, conversion, revenue impact or causation.

Campaign performance and attribution remain governed by composite MS-PROT-083 through v1.1.

### Campaign Analytical Measure

**Authority:** MS-PROT-083 v1.1.
A registered Campaign-specific Analytical Measure Definition that derives a bounded count from exact owner-qualified Campaign, Notification, suppression or Publication evidence. The initial portfolio is limited to recipient eligibility, logical direct-email responsibility, canonical Notification delivery-evidence class, Campaign-linked suppression and Campaign-linked Announcement publication counts.

Hard distinction:

```text
Campaign Analytical Measure
    ≠ Campaign Outcome Observation
    ≠ provider dashboard metric
    ≠ customer engagement lifecycle
    ≠ commercial conversion
    ≠ Campaign revenue / ROI
```

### DIRECT_EXECUTION_TRACE_V1

**Authority:** MS-PROT-083 v1.1.
The deterministic initial Campaign attribution rule permitting an effect to be associated with an exact Campaign Occurrence only where accepted semantic provenance directly establishes that relationship, such as an exact Campaign-created Notification responsibility, its canonical delivery evidence, an exact Campaign-linked Announcement publication or an exact Campaign-bound unsubscribe/suppression operation.

Canonical distinction:

```text
DIRECT_EXECUTION_TRACE_V1
    = deterministic execution provenance

DIRECT_EXECUTION_TRACE_V1
    ≠ statistical attribution
    ≠ customer engagement proof
    ≠ commercial conversion
    ≠ downstream Order / Booking / Appointment / Payment causation
    ≠ revenue / retention / ROI causation
```

---

## 26F. Financial Operations, Financial Evidence and Financial Health

These terms are high-risk because Payment, Workforce Compensation, Inventory, Regulatory Administration, provider/account evidence, Financial Operations and Business Intelligence retain separate authority.

**Authority:** MS-PROT-084 v1.1, composed with canonical Money, composite MS-PROT-055, composite MS-PROT-080, composite MS-PROT-082, composite MS-PROT-083 and MS-PROT-092.

### Financial Operations

The bounded residual financial owner for finance-native facts whose more specific business meaning is not already owned by another accepted capability.

Canonical rule:

```text
source-specific business fact exists
    → source capability remains owner
    → Financial Operations may reference / consume it

no source-specific owner exists
+
finance-native fact is required
    → Financial Operations may own that fact
```

Financial Operations is not a universal ledger, general ledger, universal payable/receivable or universal transaction store.

### Operating Cost Arrangement

A Financial-Operations-owned ongoing or known cost relationship represented through immutable version-affined terms. The initial structural families are `ONE_OFF`, `RECURRING_FIXED` and `RECURRING_VARIABLE`; these are recurrence/amount semantics, not an accounting chart of expense categories.

### Operating Cost Occurrence

An immutable Financial-Operations-owned fact representing one exact operating-cost incidence. It does not by itself establish a Payable, payment, tax deductibility or statutory-accounting expense classification.

### Operating Cost Adjustment

An immutable same-currency increase or decrease to one Operating Cost Occurrence. It preserves history and must not make the effective occurrence amount negative.

### Finance-Native Payable

A Financial-Operations-owned monetary obligation the merchant owes where no accepted source capability already owns that obligation meaning.

### Finance-Native Receivable

A Financial-Operations-owned monetary obligation another party owes the merchant where no accepted source capability already owns that receivable meaning.

Hard distinction:

```text
Operating Cost Occurrence
    ≠ Finance-Native Payable
    ≠ payment

Finance-Native Receivable
    ≠ ProviderPaymentEvidence
```

### Financial Application

An owner-qualified immutable application of accepted evidence to one Finance-Native Payable or Receivable for satisfaction/discharge purposes. It is same-currency unless accepted conversion authority first establishes an authoritative source-currency amount. Duplicate evidence must not duplicate the economic effect.

Excess applied amount is reconciliation evidence only and does not become a credit, Refund, new Payable/Receivable, revenue, expense or owner capital without separately accepted semantic authority.

### Financing Arrangement

A Financial-Operations-owned accepted merchant financing relationship that may retain externally supplied terms and evidence. It does not authorise GrandRue-generated APR, interest, amortisation, settlement or future-balance calculations.

### Financing Payment Schedule

A version-affined externally supplied or owner-qualified financing schedule. It is not a Finance-Native Payable and must not be retrospectively rewritten when a provider/lender supplies a later schedule.

### Owner Capital Movement

An accepted monetary movement between an owner/controlling economic participant and the business with explicit `INTO_BUSINESS` or `OUT_OF_BUSINESS` direction. It is not automatically revenue, expense, profit, loss, salary, dividend or loan.

### Capital Acquisition

A bounded fact that the merchant acquired a durable business asset/resource for an accepted monetary amount. It does not establish accounting capitalisation, current asset value, depreciation, tax basis, useful life or balance-sheet treatment.

### Bounded Counterparty Reference

The minimum financial-fact reference needed to identify the external party relevant to that fact. It does not create a standalone supplier lifecycle, supplier CRM, procurement workflow or global counterparty master.

### Financial Account Reference

A reference to an external or manually represented financial account/equivalent position source. Connection/existence does not establish that every associated amount is attributable to the business.

### Financial Account Applicability

Purpose-qualified authority identifying whether and what account evidence is eligible for one exact analytical/financial purpose. The canonical outcomes are:

```text
FULL_ACCOUNT_POSITION_ELIGIBLE
BOUNDED_EVIDENCE_ONLY
NOT_APPLICABLE
UNRESOLVED
```

Hard distinction:

```text
business relevance of an account
    ≠ whole-balance business authority
```

Mixed-use account transactions may be eligible for a bounded reconciliation purpose while whole-account cash-position applicability remains unresolved.

### Financial Account Position Evidence

Owner/provider-qualified evidence of an account position retaining exact account reference, position meaning, Money, polarity, currency, observation/effective time, provenance, currentness and coverage qualification. Liability-like position evidence must not silently become positive cash.

### Financial Transaction Evidence

An account transaction observation retaining exact account reference, direction, Money, time and provenance. It is evidence, not operating cost, revenue, capital movement or satisfaction classification merely because a provider description or AI suggests one.

### Economic Exposure Relationship

An owner-qualified relationship recording that semantically distinct financial facts materially concern the same underlying economic exposure. The initial canonical relation is:

```text
SAME_ECONOMIC_EXPOSURE
```

Semantic distinctness does not imply additive independence. Where material overlap cannot be established or excluded, a dependent aggregate remains unresolved rather than double-counting.

### Financial Health

A derived analytical specialization of MS-PROT-083 over exact owner-qualified Financial Operations, Payment/source and Regulatory financial facts. It is not source financial truth, a statutory financial statement or a second authoritative financial aggregate.

Financial Health uses the canonical analytical assessment vocabulary:

```text
NO_MATERIAL_CONCERN
MONITOR
MATERIAL_CONCERN
UNKNOWN
NOT_APPLICABLE
```

`UNKNOWN` is a valid result. No universal Financial Health score is established.

Canonical high-risk distinctions:

```text
customer obligation
    ≠ provider transaction
    ≠ provider settlement
    ≠ revenue
    ≠ profit

document extraction candidate
    ≠ authoritative financial fact

Financial Health
    ≠ source financial truth

missing financial evidence
    ≠ zero
```

---

## 27. Returns, merchant authority and returned stock

These terms are high-risk because merchant business discretion, optional structured Returns infrastructure, Refund, replacement movement and Inventory physical truth remain independently owned.

### Returns Capability

**Authority:** composite MS-PROT-061 through v1.1.
An optional merchant-policy capability that systematises recurring return-policy semantics and structured return infrastructure such as policy publication and provider-backed return-label preparation.

Its current Semantic Applicability comes only from accepted Merchant Configuration.

Hard distinction:

```text
Returns disabled
    ≠ merchant prohibited from accepting a return

Returns enabled
    ≠ individual return approved

Ordering / Inventory / Shipment / Payment applicable
    ≠ Returns applicable
```

Customer request, merchant category and AI inference do not activate Returns.

### Merchant Return Decision

**Authority:** MS-PROT-061 v1.1.
The merchant's authoritative business judgement for one specific customer situation. It is distinct from configured recurring Returns Policy and does not require a ReturnCase Operational Object.

A merchant may authorise independently owned consequences such as Refund, replacement or physical stock receipt even when structured Returns is disabled, provided each owning capability's own requirements are satisfied.

### Returns Policy

**Authority:** composite MS-PROT-061 through v1.1.
The merchant's recurring configured return terms where the optional Returns capability is applicable.

Current policy does not rewrite historical Order return-policy provenance, and policy matching does not automatically adjudicate an individual return.

### ReturnLabelPreparationRequest

**Authority:** MS-PROT-061 v1.1, composed with MS-PROT-048/MS-PROT-069.
A durable Returns-owned progression identity established before one logical provider-backed return-label side effect. Ordinary new structured requests require Returns applicability or exact residual historical authority plus merchant carriage authorisation and Provider Readiness.

It is not a Return approval, ReturnCase, Refund, Shipment or Inventory receipt.

### ReturnedStockReceipt

**Authority:** MS-PROT-058 v1.2.
An immutable Inventory-owned fact establishing that an exact quantity of identifiable previously supplied goods physically re-entered merchant control.

It does not require Returns capability applicability and does not make the goods sellable.

### ReturnedStockDisposition

**Authority:** MS-PROT-058 v1.2.
An immutable quantity-bearing Inventory fact establishing treatment of part or all of a ReturnedStockReceipt.

Initial stock-effect classification is:

```text
SELLABLE_REENTRY
NO_SELLABLE_REENTRY
```

Only `SELLABLE_REENTRY` may create the positive Inventory Movement whose registered cause is `RETURN`.

### Inventory RETURN movement

**Authority:** MS-PROT-058 v1.2.
An authorised positive sellable-stock movement backed by an existing ReturnedStockReceipt and explicit `SELLABLE_REENTRY` disposition.

It is not courier evidence, customer return intent, Refund evidence or Returns capability applicability.

Hard distinctions:

```text
Returns Capability
    ≠ merchant business sovereignty

Merchant Return Decision
    ≠ Returns Policy

Refund
    ≠ physical returned-stock receipt

ReturnedStockReceipt
    ≠ sellable stock

Courier return movement evidence
    ≠ ReturnedStockReceipt

ReturnedStockReceipt
    ≠ ReturnedStockDisposition

SELLABLE_REENTRY
    ≠ reopening original Inventory Claim
```

---

## 28. Release-Purpose Admission and Ordinary Release Reference

### Semantic Release-Purpose Admission Decision

**Authority:** MS-PROT-040 v1.5.

An immutable platform-administration fact admitting or withdrawing one exact immutable Semantic Registry Release for exactly one governed purpose: `NEW_CONFIGURATION_VALIDATION` or `NEW_BUSINESS_ACTIVITY`.

It is distinct from deployment materialisation/support evidence, Merchant approval and the Ordinary New-Configuration Semantic Release Reference.

### Ordinary New-Configuration Semantic Release Reference

**Authority:** composite MS-PROT-040 through v1.5.

The versioned platform-administration selection of the exact Semantic Registry Release used for ordinary new-Configuration creation/validation. Its current singleton pointer is not itself release-purpose admission authority.

Hard distinctions:

```text
release-purpose admission
    ≠ serving deployment evidence

ordinary new-Configuration release reference
    ≠ release-purpose admission

revision's pinned release
    ≠ necessarily the current ordinary reference
```

---

## 29. Standard Business Hours Revision and Withdrawal

### Standard Business Hours Revision

**Authority:** composite MS-PROT-050 through v1.4.

An immutable exact revision of stable weekly Public Business Hours for one
explicit Business Hours Scope. Its scope-local current pointer establishes
currentness without mutating historical revisions.

### Standard Business Hours Withdrawal

**Authority:** MS-PROT-050 v1.4.

An immutable revision establishing intentional absence of current stable weekly
Public Business Hours for one exact scope. It is not a configured closed week,
Merchant Location retirement or a temporary/dynamic closure.

Hard distinctions:

```text
configured weekly intervals = []
    ≠ Standard Business Hours Withdrawal

Standard Business Hours Withdrawal
    ≠ Business Operating Override

stable weekly revision
    ≠ Merchant Configuration Revision
```

---

## 29A. Merchant Brand Infrastructure

These terms are high-risk because merchant tenancy, public website routing, domain control, business email identity, mailbox semantics and provider fulfilment remain independently owned.

**Authority:** MS-PROT-088 v1.0, composed with composite MS-PROT-031 for Merchant Scope, MS-PROT-036 for Storefront, MS-PROT-056 for Commercial Entitlement, MS-PROT-067 for credentials, composite MS-PROT-075 for Notification and composite MS-PROT-086 for Customer Messaging.

### Merchant Brand Infrastructure

The GrandRue platform-service concern coordinating a merchant-facing Internet identity across an authorised brand namespace, public website hostname and business email sender identity while absorbing registrar/DNS/certificate/sender-authentication administration.

It is not the merchant business-semantic model, a registrar console, DNS product, mailbox provider or tenant identity.

### Merchant Brand Namespace

A domain namespace that GrandRue is authorised to use for one Merchant Scope under MS-PROT-088.

Initial families are:

```text
PLATFORM_DELEGATED_NAMESPACE
MERCHANT_CONTROLLED_DOMAIN
```

A Merchant Brand Namespace does not itself establish Merchant Scope, legal identity or domain-control proof.

### Platform Website Namespace Assignment

A **Platform Website Namespace Assignment** is a Merchant Brand Infrastructure-owned fact under MS-PROT-088 v1.3 §3 containing:

- assignment identity;
- exact Merchant Scope;
- canonical hostname;
- exact parent namespace reference;
- non-reused allocation discriminator;
- naming-rule revision;
- website-purpose qualification;
- predecessor assignment identity, or `NO_ASSIGNMENT`;
- originating logical request and approval references;
- acting principal and Controller Relationship reference;
- authoritative allocation instant in UTC.

Its lifecycle is `ASSIGNED → RETIRED`. `RETIRED` is terminal for that assignment. The allocation fact and original content remain immutable; retirement retains its own attributable transition evidence. The latest assignment identity needed for concurrency remains retained, including when retired.

An assignment is not Merchant Scope identity, domain ownership, binding authority or commercial permission. MS-PROT-088 v1.3 owns the substantive lifecycle; this entry is terminology navigation only.

### Domain Control Evidence

Deterministic, purpose-qualified evidence that the exact Merchant Scope is currently authorised to use a merchant-controlled domain for an accepted GrandRue purpose.

Hard distinction:

```text
Domain Control Evidence
    ≠ Merchant Scope identity
    ≠ legal-business verification
    ≠ controller identity
    ≠ unrestricted authority over every service beneath the domain
```

### Website Hostname Binding

An MS-PROT-088 relationship associating an authorised hostname with the exact Merchant Scope's Storefront delivery surface.

It resolves routing scope but does not own Storefront content or capability business truth.

### Website Binding Selection

A **Website Binding Selection** is an immutable Merchant Brand Infrastructure fact under MS-PROT-088 v1.2 §3 containing:

- selection identity;
- Merchant Scope and namespace family;
- predecessor selection identity, or `NO_SELECTION`;
- an exact hostname portfolio and existing storefront identity, or `NO_BINDING`;
- references to the namespace and authority evidence supporting the decision;
- originating operation and logical request identity;
- acting principal and Controller Relationship reference;
- authoritative UTC commit instant.

There is at most one current selection per Merchant Scope and namespace family. Before any selection, the state is `NO_SELECTION`. Neither `NO_SELECTION` nor a current `NO_BINDING` authorises routing. A new selection does not mutate an earlier selection.

Recorded evidence explains the historical decision. It does not freeze domain control, commercial permission or technical readiness for future use. MS-PROT-088 v1.2 owns the substantive lifecycle; this entry is terminology navigation only.

### Website Connection Request

A **Website Connection Request** is a Merchant Brand Infrastructure-owned durable fact under MS-PROT-088 v1.4 §3 recording immutable approval content for one exact hostname portfolio, Merchant Scope, namespace family and existing storefront, with expected binding and predecessor-request identities, namespace/control references, Controller/principal attribution, contract/release provenance and UTC acceptance instant.

Its request lifecycle is `PENDING` followed by one terminal outcome: `SELECTED`, `CANCELLED`, `SUPERSEDED` or `STOPPED`. There is at most one pending request per Merchant Scope and namespace family; the latest request identity remains available after termination for concurrency checks.

Request acceptance is not Website Binding Selection, domain control, namespace assignment, hostname reservation, external-effect permission, publication or public availability. MS-PROT-088 v1.4 §§6–12 govern acceptance, bounded completion, cancellation and recovery; this entry supplies terminology navigation only.

### Website Infrastructure Plan

A **Website Infrastructure Plan** is an immutable Merchant Brand Infrastructure record under MS-PROT-088 v1.5 §3 defining one bounded set of intended technical effects for an exact Merchant Scope, namespace family and hostname portfolio, affined to an originating connection request or current binding selection.

Its purpose is `CONNECTION_PREPARATION`, `WEBSITE_CUTOVER`, `MAINTENANCE` or `CLEANUP`. Its content identifies exact changes or the bounded certificate-challenge form, ordering, expected resource state, approval or registered maintenance/cleanup authority, exact fulfilment provenance, logical acceptance identity, principal and UTC acceptance instant.

Plan content is distinct from progression permission, external-effect identity/certainty, current DNS observations, certificate usability, Website Binding Selection and public delivery. Stopping a plan does not erase unresolved effects. MS-PROT-088 v1.5 §§3–17 owns the substantive execution and recovery rules; this entry is terminology navigation only.

### Business Email Identity

A merchant-branded public sender identity used by an independently authorised GrandRue communication responsibility.

Examples may render as `bookings@merchant.example` or `support@merchant.example`.

Hard distinction:

```text
Business Email Identity
    ≠ mailbox
    ≠ Notification
    ≠ Conversation
    ≠ delivery provider
    ≠ generic inbound-email capability
```

### Merchant-Controlled Domain / Platform-Delegated Namespace

A **Merchant-Controlled Domain** is a domain or delegated subdomain for which adequate current control has been established for the intended purpose and whose merchant portability is preserved.

A **Platform-Delegated Namespace** is a merchant-qualified child namespace beneath a GrandRue-controlled parent domain. It is platform-managed and is not represented as merchant-owned or externally transferable.

Canonical cross-domain distinction:

```text
Merchant Scope
    ≠ domain

Domain connection
    ≠ registrar transfer

Website binding
    ≠ business-email binding

Business Email Identity
    ≠ mailbox
```

---

## 30. Canonical qualification rule

When a term has more than one valid GrandRue meaning:

```text
cross-domain normative context
    → qualify the term

single bounded context with obvious owner
    → short form may be used
```

The lexicon shall remain intentionally small. New entries require demonstrated ambiguity risk, not mere vocabulary reuse.

---

## 31. Single-Current-Document Rule

This lexicon is the sole current terminology-governance authority. Future approved terminology-governance changes MUST be merged into this file and versioned in place; Git history preserves revision provenance.

Semantic authorities referenced by the lexicon remain independently governed by `AUTHORITY-INDEX.md`.


---

## Appointment Occurrence Outcome

**Authority:** composite MS-PROT-042 through v1.9.
**Meaning class:** Appointment-owned current authoritative classification of what materially happened to the scheduled customer interaction represented by one exact Appointment revision.

Initial closed outcomes:

```text
OCCURRED
PARTIAL_OCCURRENCE
CUSTOMER_NO_SHOW
MERCHANT_SIDE_NON_OCCURRENCE
```

Qualification rules:

```text
Appointment Occurrence Outcome
    ≠ Appointment commitment
    ≠ Appointment cancellation
    ≠ Payment / Refund
    ≠ Workforce time
    ≠ customer satisfaction
    ≠ broader service/work fulfilment
```

Clock passage, payment state, provider presence, Customer Messaging content and AI confidence do not independently establish an Appointment Occurrence Outcome. When correction is required, composite MS-PROT-042 v1.9 preserves superseded outcome evidence rather than erasing history.


---

## TimeProposal, Proposal Acceptance Deadline & Appointment Proposal Hold

**Authority:** composite MS-PROT-042 through v1.10.

### TimeProposal

Appointment-owned pre-commit customer-decision opportunity in which a merchant offers one exact candidate interval through the merchant-controlled scheduling flow.

```text
TimeProposal
    ≠ Appointment
    ≠ Allocation
    ≠ capacity ownership
    ≠ Appointment Proposal Hold
```

### Proposal Acceptance Deadline

The exact authoritative instant before which customer acceptance of one TimeProposal may be established. Absent an earlier accepted merchant/policy cutoff, composite MS-PROT-042 v1.10 defaults it to the proposed Appointment interval start. Acceptance at or after the deadline is invalid.

### Appointment Proposal Hold

Proposal-specific temporary capacity protection that exists only for a PROTECTED TimeProposal. It expires at the exact Proposal Acceptance Deadline and must not remain capacity-blocking after semantic expiry merely because cleanup is delayed.

```text
Appointment Proposal Hold
    ≠ TimeProposal
    ≠ Appointment
    ≠ final Appointment Allocation
    ≠ universal Hold authority
```

The term does not promote historical MS-PROT-006 wholesale; its current authority is the bounded Appointment proposal use established by MS-PROT-042 v1.10.


---

## AppointmentRescheduleProposal

**Authority:** composite MS-PROT-042 through v1.11.

An **AppointmentRescheduleProposal** is an Appointment-owned, exact-revision-affined proposal to change the scheduled interval of an already existing Appointment where the governing customer-decision mode requires or uses a proposed change before the reschedule commits.

```text
AppointmentRescheduleProposal
    ≠ Appointment revision
    ≠ customer acceptance
    ≠ committed reschedule
    ≠ final capacity allocation
```

The existing Appointment remains authoritative until a valid reschedule operation commits. A proposal may use explicit target-capacity protection, but proposal acceptance remains durable customer-decision evidence distinct from successful reschedule execution.

---

## Appointment Check-In

**Authority:** composite MS-PROT-042 through v1.11.

**Appointment Check-In** is Appointment-owned evidence that the customer presented themselves or was recognised as ready for the exact Appointment interaction/revision.

```text
Appointment Check-In
    ≠ Appointment lifecycle state
    ≠ Appointment Occurrence Outcome
    ≠ CUSTOMER_NO_SHOW
    ≠ reschedule acceptance
    ≠ Scheduling capacity mutation
    ≠ Workforce time
```

Absence of check-in does not establish no-show, and check-in does not establish occurrence. Initial authoritative check-in is merchant-attested; customer self-check-in requires a separately accepted exact evidence contract.

---

## Appointment Series

**Authority:** composite MS-PROT-042 through v1.13.

An **Appointment Series** is an Appointment-owned merchant-scoped Operational Object representing an authorised repeating customer-related scheduling arrangement from which independent Appointment commitments may be established. The Series is not itself an Appointment and does not reserve future capacity merely by existing.

## Series Occurrence Identity

**Authority:** composite MS-PROT-042 through v1.13.

A **Series Occurrence Identity** is the stable logical identity of one recurring position within an Appointment Series. One Series Occurrence Identity may establish at most one Appointment identity; later cancellation, no-show, rescheduling or outcome does not make that occurrence eligible to manufacture a replacement Appointment.

## Appointment Participation

**Authority:** composite MS-PROT-042 through v1.13.

An **Appointment Participation** is the Appointment-owned durable relationship establishing that one exact CustomerContext participates in one exact shared Appointment. Participation does not merge CustomerContexts, expose other participants, grant whole-Appointment control or own Resource capacity.

## Appointment Participant Attendance Outcome

**Authority:** composite MS-PROT-042 through v1.13.

An **Appointment Participant Attendance Outcome** records participant-specific attendance truth for one exact Appointment Participation using the initial closed vocabulary `ATTENDED`, `PARTIAL_ATTENDANCE` and `NO_SHOW`. It remains distinct from the Appointment Occurrence Outcome, withdrawal, check-in, Payment and customer satisfaction.

---

## Booking Commitment Revision

**Authority:** composite MS-PROT-042 through v1.14.

A **Booking Commitment Revision** is the immutable Booking-owned revision of the customer reservation commitment, including the current Booked Subject, Reservation Scope and applicable quantity/capacity meaning. Exactly one current revision governs while historical superseded revisions remain authoritative evidence. It is distinct from Resource/Allocation assignment and from Payment truth.

## Booking Amendment Contract

**Authority:** composite MS-PROT-042 through v1.14.

A **Booking Amendment Contract** is the versioned registered semantic contract establishing which generic Booking amendment dimensions are supported for an applicable Booked Subject family, including required validation, customer-decision requirements and capacity consequences. It is not merchant-authored executable logic and does not grant a generic patch-any-field capability.

## BookingChangeProposal

**Authority:** composite MS-PROT-042 through v1.14.

A **BookingChangeProposal** is a bounded proposal for a merchant-initiated material Booking change where customer acceptance is required. The proposal, customer acceptance and any candidate capacity protection are not themselves Booking mutation; the existing Booking remains authoritative until `booking.modify` commits.

## Booking Natural Discharge Contract

**Authority:** composite MS-PROT-042 through v1.14.

A **Booking Natural Discharge Contract** registers how an `IN_FORCE` Booking reservation commitment may naturally become `RELEASED`. The initial closed modes are `RESERVATION_SCOPE_END` and `OWNER_QUALIFIED_TERMINAL_EVIDENCE`. Time passage discharges a Booking only when the exact accepted contract selects the scope-end mode; there is no universal timestamp-to-release rule.

## Booking Utilisation Outcome

**Authority:** composite MS-PROT-042 through v1.14.

A **Booking Utilisation Outcome** records Booking-owned evidence about whether the customer materially took up the reservation opportunity. The initial closed vocabulary is `UTILISED`, `PARTIALLY_UTILISED`, `CUSTOMER_NON_UTILISATION` and `MERCHANT_SIDE_NON_HONOUR`. It remains distinct from Booking `IN_FORCE`/`RELEASED`, cancellation, Payment/refund, Resource Allocation and foreign fulfilment truth.

Hard distinctions:

```text
Booked Subject ≠ current Resource/Allocation
Booking discharge ≠ Booking utilisation
Booking utilisation ≠ Payment/refund
booking.modify ≠ cancel-and-rebook
BookingChangeProposal ≠ Booking mutation
```

---

## Appointment Support Requirement

**Authority:** composite MS-PROT-042 through v1.12.

An **Appointment Support Requirement** is the Appointment-owned statement of what supporting Resource capability or exact Resource identity must be available for the agreed Appointment interaction. It is independently revisioned from the scheduled interval and from the concrete Resource Allocation that currently satisfies it.

Initial closed selection modes are:

```text
ANY_ELIGIBLE_RESOURCE
EXACT_RESOURCE
```

`ANY_ELIGIBLE_RESOURCE` permits any authoritatively eligible Resource satisfying the registered requirement. `EXACT_RESOURCE` makes the exact Resource identity part of the customer-relevant Appointment commitment.

Hard distinction:

```text
Appointment Support Requirement
    ≠ current Resource Allocation
    ≠ Workforce availability
    ≠ Appointment Scheduling Evaluation
```

A current assignment does not by itself make that Resource identity customer-promised.

---

## Current Supporting Resource

**Authority:** composite MS-PROT-042 through v1.12, composed with Resource/Allocation and Workforce authorities.

The **Current Supporting Resource** is the concrete Resource whose authoritative Resource/Allocation state currently satisfies an applicable Appointment Support Requirement for the scheduled interval.

Appointment owns what support is required; Resource/Allocation owns which concrete capacity currently satisfies it. For a worker-backed Resource, applicable Workforce evidence is admitted only through the accepted exact Workforce Scheduling Arrangement ↔ schedulable Resource affinity.

Changing the Current Supporting Resource without changing the Support Requirement is not, by itself, Appointment rescheduling or a customer-commitment amendment.

---

## Appointment Support Evaluation

**Authority:** composite MS-PROT-042 through v1.12.

An **Appointment Support Evaluation** is the Scheduling-owned evaluation of whether an applicable current Appointment Support Requirement is presently satisfied for the scheduled interval.

Canonical result vocabulary:

```text
SATISFIED
UNSATISFIED
UNRESOLVED
```

`SATISFIED` requires sufficient current authoritative evidence of valid supporting capacity. `UNSATISFIED` means sufficient evidence establishes the requirement is not currently satisfied. `UNRESOLVED` means the requirement applies but sufficient evidence cannot currently be established.

None of these values is an Appointment lifecycle state or Appointment Occurrence Outcome. In particular, `UNSATISFIED` and `UNRESOLVED` do not cancel the Appointment.

---

## Appointment Support Change Classification

**Authority:** composite MS-PROT-042 through v1.12.

Canonical support-change classes are:

```text
SUBSTITUTION_WITHIN_REQUIREMENT
MATERIAL_SUPPORT_REQUIREMENT_CHANGE
```

`SUBSTITUTION_WITHIN_REQUIREMENT` replaces the concrete supporting Resource with another Resource that independently satisfies exactly the same current Appointment Support Requirement. It does not change customer commitment meaning and does not require new customer acceptance solely because the Resource changes.

`MATERIAL_SUPPORT_REQUIREMENT_CHANGE` changes the Appointment Support Requirement itself, including replacing an `EXACT_RESOURCE` promise with a different Resource. It is an Appointment commitment amendment and requires the applicable customer/policy authority.

---

## AppointmentSupportChangeProposal

**Authority:** composite MS-PROT-042 through v1.12.

An **AppointmentSupportChangeProposal** is an Appointment-owned, exact-currentness-affined proposal for a material Appointment Support Requirement change where applicable customer-decision semantics require or use a proposal before the change commits.

```text
AppointmentSupportChangeProposal
    ≠ Support Requirement revision
    ≠ customer acceptance
    ≠ Resource Allocation
    ≠ successful assignment
```

The existing Support Requirement remains authoritative until a valid material change commits. Customer acceptance remains durable decision evidence even if later target Resource execution fails.

---

## Appointment Assignment Proposal Hold

**Authority:** composite MS-PROT-042 through v1.12, composed with Resource/Allocation capacity authority.

An **Appointment Assignment Proposal Hold** is optional proposal-specific protection of the exact target Resource capacity required by a PROTECTED AppointmentSupportChangeProposal while the existing Appointment support remains authoritative.

```text
Appointment Assignment Proposal Hold
    ≠ Appointment Support Requirement
    ≠ customer acceptance
    ≠ final Resource Allocation
```

The Hold expires semantically with the exact proposal deadline and cannot remain authoritatively blocking capacity merely because cleanup is delayed.

---

## Capacity Waitlist

**Authority:** MS-PROT-089 v1.0; feature admission and overbooking rejection originate in MS-PROT-042 v1.15.

A Capacity Waitlist is the bounded capability that owns queued unmet customer demand and promotion coordination for an exact accepted source-qualified capacity opportunity. It does not own eventual Booking, Appointment, Appointment Participation, Resource, Allocation or Scheduling truth.

### Waitlist Target

A Waitlist-owned grouping identity for one exact source-qualified unmet-demand meaning under one immutable versioned Waitlist Opportunity Contract. Target equivalence must be deterministic under the source-qualified contract; display similarity or AI inference is insufficient.

### Waitlist Entry

A durable merchant-scoped fact that one exact CustomerContext explicitly requested to wait for one exact Waitlist Target. It is not a Booking, Appointment, Appointment Participation, Hold, Allocation, Enquiry, Conversation or Merchant Attention Occurrence.

Initial membership requires direct customer action or authorised merchant attestation of the customer request. CustomerAccount is optional; contact-value equality does not establish identity or merge membership.

### Queue Precedence

The immutable Waitlist-owned ordering of current Waitlist Entries for one exact Waitlist Target. The initial policy is FIFO by GrandRue authoritative admission order with deterministic total ordering for concurrency. Client timestamps, merchant backdating, VIP/value/AI scores and manual reordering do not create precedence authority.

### Waitlist Promotion

A durable Waitlist-owned coordination fact selecting one exact current Waitlist Entry for one current source-qualified opportunity. The initial model permits at most one active Promotion per exact Waitlist Target.

Promotion protection is exactly:

```text
UNPROTECTED
SOURCE_PROTECTED
```

`SOURCE_PROTECTED` means the source capability has separately established accepted source-owned protection; Waitlist never manufactures Hold or Allocation truth.

### Customer Promotion Response

An exact response to one active Waitlist Promotion. Initial response meanings are:

```text
ACCEPT
DECLINE
```

`ACCEPT` authorises an attempt of the exact source-owner commitment operation; it is not itself Booking, Appointment or Appointment Participation truth. A Conversation Message is not automatically a Waitlist response or business command.

Hard distinctions:

```text
Waitlist membership ≠ capacity reservation
Queue Precedence ≠ source commitment priority over all demand
Promotion ≠ Hold / Allocation
Notification delivery ≠ customer acceptance
Promotion ACCEPT ≠ source commitment established
Waitlist ≠ Marketing permission
Waitlist ≠ generic Queue / CRM / workflow engine
```

The initial target portfolio is limited to exact Appointment interval, exact shared-Appointment participation and exact Booking reservation contracts. Broad preference windows, product-restock waitlists, inventory backorders, priority classes and batch offers require new Feature Admission rather than silent expansion.

## Overbooking — rejected initial capability

**Authority:** MS-PROT-042 v1.15.

Overbooking means intentionally permitting authoritative customer commitments beyond ordinary simultaneously satisfiable capacity. It is not the same as stale availability or a concurrency defect. Overbooking is not admitted into the initial GrandRue portfolio; merchant policy, AI prediction or inflated capacity configuration cannot create that authority.

---

## Review / Reputation coordination

### Review Destination Binding

**Authority:** MS-PROT-090 v1.0.
A merchant-scoped provider-qualified binding identifying the exact external review destination to which an eligible customer may be directed. It is not Merchant identity, MerchantLocation identity, ProviderConnection or review content.

### Review Experience Contract

**Authority:** MS-PROT-090 v1.0.
An immutable versioned source-owner-qualified semantic contract establishing whether exact source-owned customer-experience evidence is sufficient for the narrow purpose of external review solicitation. It does not own or redefine Appointment, Booking or other source outcomes.

### Review Solicitation

**Authority:** MS-PROT-090 v1.0.
A bounded GrandRue coordination responsibility for issuing one neutral targeted external-review request to an independently eligible CustomerContext under current Review Destination, source experience, contact-permission, preference and Notification constraints. Initial policy is `DISABLED` or `ALL_ELIGIBLE_ONCE`; solicitation is not review truth or review-conversion attribution.

### External Review Reference

**Authority:** MS-PROT-090 v1.0.
The minimum provider-qualified locator/provenance retained to coordinate observation and handling of one external provider review. It is not a native GrandRue review and does not transfer provider review-content/rating ownership.

### Merchant Review Response Instruction

**Authority:** MS-PROT-090 v1.0.
A durable exact merchant-approved provider-bound public-response instruction for one exact External Review Reference and exact reviewed-material affinity. It precedes provider effect; every initial external reply requires specific merchant approval and stale approval cannot survive material review change.

Hard distinctions:

```text
source customer-experience truth
    ≠ Review Solicitation eligibility by itself

Review Solicitation
    ≠ external review
    ≠ customer satisfaction

External Review Reference
    ≠ native GrandRue review

provider review content/rating
    ≠ GrandRue reputation score

merchant reply approval
    ≠ provider reply effect
```

---

## Customer Return Analytics (MS-PROT-083 v1.2)

### Customer Return Activity Contract

**Authority:** MS-PROT-083 v1.2.
A versioned analytical interpretation contract defining how exact source-owned Order Fulfilment, Appointment occurrence or Booking utilisation evidence may participate in customer-return analysis. It does not own or rewrite the source business outcome.

### Customer Return Activity

**Authority:** MS-PROT-083 v1.2.
A purpose-bound analytical participation fact meaning only that one exact source-owned business interaction has reached the accepted point at which it may contribute to customer-return analysis. It is not customer satisfaction, retention, loyalty, profitability or review sentiment.

### RETURNING (Customer-Return Analytical Classification)

**Authority:** MS-PROT-083 v1.2.
An analytical evaluation outcome meaning that qualifying customer activity exists in the current observation window and at least one earlier qualifying activity is authoritatively established. It MUST NOT be used as a durable CustomerContext lifecycle state.

### Returning Customer Share

**Authority:** MS-PROT-083 v1.2.
The share of customers with qualifying represented activity in an exact observation window who have qualifying prior activity, only when prior-history classification coverage is sufficient for the entire intended denominator. It is not a universal Customer Retention Rate.

### Returned After Quiet Period

**Authority:** MS-PROT-083 v1.2.
A purpose-bound analytical condition in which qualifying current activity follows an exact bounded period with no qualifying activity, given sufficient historical coverage. It does not create `LAPSED`, `REACTIVATED` or `WIN_BACK` CustomerContext state and does not imply Marketing causation.
---

## 32. Quotation, issued revision and recipient response

These terms are high-risk because customer requests, merchant commercial offers, Orders, Payment Obligations, Inventory and future Invoice truth are independently owned.

### Quotation

**Authority:** MS-PROT-095 v1.0.  
A merchant-scoped Operational Object representing one merchant commercial-offer lineage addressed to one recipient context.

Quotation owns merchant preparation, immutable issued Quotation revisions, quoted commercial terms and Quotation-owned recipient response facts.

Quotation is not an Enquiry, Order, Appointment, Invoice, Payment Obligation, PDF, email or generic document.

### Issued Quotation Revision

**Authority:** MS-PROT-095 v1.0.  
An immutable identity-bearing Quotation-owned commercial proposition containing the exact scope and quoted commercial amounts the merchant issued to the recipient at one point in time.

An issued revision MUST NOT be edited in place. A material commercial change requires another revision.

### Quotation Item

**Authority:** MS-PROT-095 v1.0.  
An identity-bearing component of one Issued Quotation Revision describing one quoted commercial scope and its exact quoted monetary contribution. A source Offering, Product, ProductVariant or other accepted subject reference does not transfer source ownership to Quotation.

### Quotation Acceptance

**Authority:** MS-PROT-095 v1.0.  
An immutable Quotation-owned fact that an authorised recipient, or an authorised merchant actor recording an externally received response with that provenance, accepted one exact Issued Quotation Revision.

Quotation Acceptance is not an Order and is not payment evidence.

### Quotation Decline

**Authority:** MS-PROT-095 v1.0.  
An immutable Quotation-owned fact that an authorised recipient, or an authorised merchant actor recording an externally received response with that provenance, declined one exact Issued Quotation Revision.

### Quotation Withdrawal

**Authority:** MS-PROT-095 v1.0.  
An immutable Quotation-owned fact that an authorised merchant actor withdrew one exact outstanding Issued Quotation Revision before acceptance.

### Quotation Request

**Authority:** MS-PROT-095 v1.0 composed with composite MS-PROT-043.  
Merchant/customer-facing wording for customer intent to obtain a quotation. It does not identify a separate Operational Object. The authoritative customer request remains an Enquiry; Quotation begins with the merchant commercial-offer lineage.

Canonical distinctions:

```text
Quotation Request
    customer intent / Enquiry-owned request

Quotation
    merchant commercial-offer lineage

Issued Quotation Revision
    exact immutable merchant offer

Quotation Acceptance
    response to exact issued revision

Order
    accepted purchase/order commitment

Payment Obligation
    Money-owned amount required to be discharged

Invoice
    merchant-issued customer billing record under MS-PROT-096
```

Hard boundaries:

```text
Get a quotation
    ≠ create QuotationRequest Operational Object

Quotation Acceptance
    ≠ Order

Quotation Acceptance
    ≠ Inventory reservation

Quotation Acceptance
    ≠ Payment Obligation

Quotation Acceptance
    ≠ Invoice
```
---

## 33. Invoice, Payment Binding and billing resolution

These terms are high-risk because billing representation, customer monetary obligation, payment execution, Financial Operations and customer relationship are independently owned.

### Invoice

**Authority:** MS-PROT-096 v1.0.  
A merchant-scoped Operational Object representing one merchant-issued customer billing record. Invoicing owns Invoice identity, issued content, Invoice Reference, Invoice Items, Invoice Payment Binding, withdrawal and Invoice-owned billing-cancellation truth.

Invoice is not a Payment Obligation, Amount Due, PaymentApplication, Finance-Native Receivable, PDF, email or generic accounting record.

### Invoice Item

**Authority:** MS-PROT-096 v1.0.  
An identity-bearing component of one Invoice describing one billed commercial scope and one exact billed monetary contribution.

### Invoice Reference

**Authority:** MS-PROT-096 v1.0.  
A merchant-scoped human-facing immutable reference allocated to one Invoice at issuance. It is distinct from Invoice identity, unique within Merchant Scope and never reused after allocation.

### Invoice Payment Binding

**Authority:** MS-PROT-096 v1.0.  
The immutable relationship between one issued Invoice and exactly one Payment Obligation. The initial binding modes are `INVOICE_ESTABLISHES_OBLIGATION` and `EXISTING_OBLIGATION`.

The binding does not transfer Payment ownership to Invoicing.

### Invoice Withdrawal

**Authority:** MS-PROT-096 v1.0.  
An immutable Invoicing-owned fact that one issued Invoice is no longer the current customer billing representation. Withdrawal does not itself mutate the bound Payment Obligation.

### Invoice Billing Cancellation

**Authority:** MS-PROT-096 v1.0.  
An immutable Invoicing-owned fact cancelling billing created by an Invoice whose issuance itself established its Payment Obligation. Required Payment reduction remains Payment-owned and is coordinated atomically; Refund is separate.

### Invoice customer relationship

**Authority:** MS-PROT-096 v1.0 composed with composite MS-PROT-043 and customer-surface/access authority.  
Where recipient context is a CustomerContext, the Invoice retains an owner-qualified relationship to that exact CustomerContext. Merchant customer-history projections may compose authorised Invoice information through that relationship, but CustomerContext does not own or duplicate Invoice or Payment truth. A one-off guest Invoice does not require manufacture of a CustomerContext.

Canonical distinctions:

```text
Invoice
    merchant-issued billing record

Payment Obligation
    customer monetary obligation

Amount Due
    Payment-derived current result

Finance-Native Receivable
    residual Financial Operations obligation only
    where no more specific source owns the meaning

Invoice → CustomerContext
    customer-history relationship
    not ownership transfer
```

Hard boundaries:

```text
Invoice total
    ≠ Amount Due

Invoice paid presentation
    ≠ Invoice-owned paid state

Invoice Withdrawal
    ≠ Payment Obligation cancellation

Invoice Billing Cancellation
    ≠ Refund

CustomerContext
    ≠ Invoice owner
```

---

## 34. Merchant subject categorisation and structured merchandise/listing attributes

### Merchant Subject Category

**Authority:** MS-PROT-044 v1.4.

A merchant-scoped, stable, non-executable classification identity used to organise supported merchant business subjects. The initial target families are exactly Offering, Product and Listing. A Category label or hierarchy does not create price, inventory, lifecycle, Exposure, customer-operation or other capability semantics.

This term is distinct from Merchant Profile `MerchantClassificationEntryV1`, which classifies the merchant/business itself.

### Merchant Category Assignment

**Authority:** MS-PROT-044 v1.4.

A durable owner-qualified relationship between one Merchant Subject Category and one exact eligible semantic target, preserving both target family and target identity. Direct assignment is distinct from derived ancestor membership. Assignment or removal does not mutate the classified target's business truth.

### Merchandise Condition

**Authority:** MS-PROT-044 v1.4 composed with MS-PROT-045.

A registered structured business attribute describing applicable merchandise condition where the relevant schema admits it. The initial values are exactly:

```text
NEW
USED
REFURBISHED
```

Merchandise Condition is not a Merchant Subject Category and does not automatically establish ProductVariant identity. Where condition varies by independently tracked physical unit, unit-level semantics must not be falsely promoted to Product truth.

### Listing Transaction Mode

**Authority:** MS-PROT-044 v1.4 composed with MS-PROT-045.

A registered Listing-owned structured value describing the market proposition of an applicable Listing. The initial values are exactly:

```text
SALE
RENT
```

Listing Transaction Mode is not a Category and does not itself establish Order, Booking, tenancy, lease or other transaction-execution authority. It belongs to Listing rather than the underlying Property/subject.

Canonical distinctions:

```text
Category
    organisation only

Merchandise Condition
    structured descriptive meaning

Listing Transaction Mode
    Listing proposition meaning

Capability participation
    executable customer-operation meaning
```

---

## 35. Listing identity, revision and lifecycle

### Listing Revision

**Authority:** MS-PROT-044 v1.5.

An immutable Listing-owned representation of the exact current proposition truth established at one point in a Listing lineage. It retains schema/version affinity and predecessor provenance where applicable. A material Listing-owned change creates another revision; earlier revisions are not mutated.

### ACTIVE Listing

**Authority:** MS-PROT-044 v1.5.

A Listing whose proposition remains current for the merchant. ACTIVE does not itself mean publicly exposed, operationally available, sold/unsold, let/unlet or executable by a customer.

### WITHDRAWN Listing

**Authority:** MS-PROT-044 v1.5.

A terminal Listing lifecycle condition meaning the merchant has ended that Listing as a current proposition for new ordinary Listing-based interaction. Identity, revisions, primary-subject affinity and historical consumer relationships remain retained under applicable lifecycle authority. Reactivation is not admitted in the initial model.

Canonical distinctions:

```text
Listing identity
    ≠ Listing Revision

ACTIVE
    ≠ public Exposure
    ≠ operational availability

WITHDRAWN
    ≠ deleted subject
    ≠ deleted history

SALE ↔ RENT
    → new Listing identity
    not ordinary revision
```

---

## 36. Physical location, navigation resolution and map presentation

### PhysicalLocationBindingV1

**Authority:** MS-PROT-045 v1.2.

Reusable provider-neutral physical-location binding with exactly two initial forms:

```text
MERCHANT_LOCATION_REFERENCE
INDEPENDENT_PHYSICAL_PLACE
```

The first references an existing Merchant Location without copying its address. The second carries an independently located physical-place value owned by the applicable source subject. The binding does not create a universal Location Operational Object.

### PhysicalPlaceValueV1

**Authority:** MS-PROT-045 v1.2.

Reusable structured value for an independently located subject. It may contain a reusable `PostalAddressV1`, merchant-approved place label and an accepted provider-neutral navigation point where applicable. Address and coordinates do not become subject identity.

### AcceptedNavigationPoint

**Authority:** MS-PROT-045 v1.2 composed with MS-PROT-051 v1.2/v1.7.

The precise provider-neutral physical point accepted by the applicable owning operation for navigation/presentation purposes. Coordinates and resolution provenance may exist internally, but ordinary merchants are not required to understand or edit them. A provider response or provider place identifier is candidate/integration evidence, not this authoritative value.

### MapProviderLocationBinding

**Authority:** MS-PROT-045 v1.2.

Provider-specific technical integration evidence relating one authoritative provider-neutral location to an external map/geocoding provider reference. It may be rebuilt or replaced when providers change and MUST NOT replace Physical Location source truth.

### Map Presentation

**Authority:** MS-PROT-045 v1.2 composed with Projection, Exposure and Storefront authority.

A derived customer-facing map, directions or street-level imagery representation produced from already-authorised provider-neutral location truth plus Exposure, provider readiness and Storefront composition. It is not authoritative location state.

Canonical distinctions:

```text
PostalAddressV1
    ≠ physical subject identity

AcceptedNavigationPoint
    ≠ provider identifier

Physical Location
    ≠ map provider binding

Map / directions / street-level imagery
    ≠ source business truth
```
---

## 37. Merchant assistance commercial-support boundary

### Merchant Assistance Supporting Interaction

**Authority:** MS-PROT-057 v1.3.

The exact bounded `merchant-assistance/supporting-interaction-access@1` platform-service access contract over the merchant-assistance responsibility portfolio accepted through MS-PROT-057 v1.2. It requires no independent Commercial Entitlement and mints no `CommercialEntitlementIdentity`.

It permits assistance to interpret, explain, prepare and coordinate only within independently applicable underlying authority. It does not create semantic applicability, Actor Authorisation, Commercial permission, Provider Readiness, Exposure, merchant intent or business truth.

Canonical distinctions:

```text
Merchant Assistance
    ≠ underlying capability permission
    ≠ Actor Authorisation
    ≠ Commercial Entitlement
    ≠ business truth

AI/provider choice
    ≠ commercial identity

NO INDEPENDENT COMMERCIAL ENTITLEMENT
    ≠ unlimited resource consumption
```

