# MS-PROT-048 v1.6 — Geographic Carrier Serviceability & Shipment Route Resolution Amendment

**Document ID:** MS-PROT-048  
**Version:** 1.6  
**Status:** ACCEPTED  
**Approved:** 16 September 2026  
**Authority type:** Provider Fulfilment serviceability and route-resolution amendment  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-048 through v1.5 within route-sensitive Shipment-provider serviceability and runtime fulfiller resolution only  
**Depends on:** Composite MS-PROT-048 through v1.5; composite MS-PROT-054; composite MS-PROT-060 through v1.2; composite MS-PROT-062; MS-PROT-067; MS-PROT-068; composite MS-PROT-069; MS-PROT-070; MS-PROT-072  
**Preserves:** Shipment ownership; Order Fulfilment ownership; Merchant Configuration activation authority; ProviderConnection identity; Provider Readiness; open-first selection; provider neutrality; historical provider affinity  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY

---

# 0. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

A merchant should not need to understand or maintain:

```text
country × city × carrier × postcode
provider integration matrices
carrier API availability
provider coverage maps
provider fallback trees
```

to ship a customer's Order.

GrandRue SHALL absorb that infrastructure complexity while preserving the merchant's ability to choose a supported carrier where the merchant deliberately makes that choice.

Merchant-facing intent remains approximately:

```text
deliver this Order
```

Where the merchant has chosen a carrier, GrandRue SHALL present and use only carrier options available for the merchant's location and applicable movement context.

Where the merchant has not chosen a specific carrier, GrandRue may internally determine:

```text
where is it leaving from?

where is it going?

what movement responsibility
was actually committed?

which admitted carriers can
serve that exact movement?

which of those paths is
currently executable?

which accepted routing rule
selects among them?
```

The additional architecture is justified because real carrier infrastructure is geographically uneven and changes independently of merchant business semantics.

---

# 1. Governing Decision

Composite MS-PROT-048 SHALL distinguish:

```text
FULFILMENT REQUIREMENT
    what technical responsibility is required?

STATIC FULFILMENT BINDING
    what accepted routing mechanism governs it?

PROVIDER SERVICEABILITY
    which admitted provider paths can serve
    this exact movement context?

ROUTE RESOLUTION
    which serviceable executable path
    is selected for this new attempt?

PROVIDER READINESS
    can that provider path currently
    be attempted safely?

EXECUTION
    perform the already-selected attempt
```

Canonical:

```text
shipment / shipment-preparation
        ↓
accepted fulfilment binding
        ↓
exact movement context
        ↓
Provider Serviceability
        ↓
eligible candidate set
        ↓
current Provider Readiness
        ↓
deterministic Route Resolution
        ↓
EXACT provider/binding/connection
        ↓
ShipmentPreparationRequest
        ↓
provider side effect
```

Provider Serviceability SHALL NOT become Shipment business authority.

---

# 2. Current Gap

Existing MS-PROT-048 correctly supports:

```text
Role
→ Binding
→ ProviderConnection
→ Provider Readiness
```

where the binding identifies an already-selected fulfiller.

That is sufficient for technical responsibilities whose provider does not depend materially on the individual transaction geography.

It is insufficient for physical carrier infrastructure where:

```text
Provider P works in country A

but

does not serve locality X
```

or:

```text
Provider P is appropriate
for domestic movement

Provider Q is available for
local on-demand movement
```

or:

```text
provider network exists nationally

but exact origin/destination
cannot currently be served
```

A provider-wide compatibility or health Boolean cannot answer those questions.

---

# 3. Scope Is Initially Shipment-Specific

This amendment activates runtime serviceability resolution only for:

```text
shipment / shipment-preparation
```

under composite MS-PROT-060.

It does not automatically generalise geographic serviceability to:

```text
Payment
Notification
Calendar
Scheduling
Publication
AI
other Provider Fulfilment roles
```

A future role may reuse the model only through separately accepted applicability.

This avoids asserting unjustified platform-wide generality.

---

# 4. Exact Binding vs Serviceability-Resolved Binding

A Fulfilment Binding Selection SHALL support two bounded routing modes where registered for the role:

```text
EXACT_FULFILLER
```

and:

```text
SERVICEABILITY_RESOLVED
```

## `EXACT_FULFILLER`

Preserves current MS-PROT-048 behaviour.

The accepted binding directly identifies the fulfiller/provider.

Where the merchant has deliberately selected a supported carrier, that carrier remains the selected fulfiller, subject to GrandRue presenting only options available for the merchant's location and applicable movement context.

## `SERVICEABILITY_RESOLVED`

The accepted binding identifies:

```text
an exact admitted provider candidate portfolio
+
an exact route-selection policy revision
```

rather than one provider that must serve every possible movement.

Only roles explicitly registered for this routing mode may use it.

---

# 5. No Runtime Provider Discovery

`SERVICEABILITY_RESOLVED` SHALL NOT mean:

```text
search internet for a carrier

ask AI which company looks good

accept merchant-entered API URL

call arbitrary delivery marketplace

discover arbitrary provider at runtime
```

Every candidate provider must already be admitted through MS-PROT-048 provider authority.

Canonical:

```text
registered ProviderDefinition
+
role compatibility
+
accepted provider contract
+
applicable connection model
+
admitted routing portfolio
        ↓
eligible candidate for evaluation
```

Runtime resolution chooses only among already-governed candidates.

GrandRue SHALL present merchants only with admitted carrier options that are available for the merchant's location and applicable movement context.

---

# 6. Provider Candidate Portfolio Revision

A serviceability-resolved binding SHALL reference one exact immutable:

```text
ProviderCandidatePortfolioRevision
```

Conceptually:

```text
ProviderCandidatePortfolioRevision
{
    portfolioIdentity
    revision

    roleIdentity
    semanticContext

    candidates[]
}
```

Each candidate identifies at least:

```text
provider / fulfiller identity

ProviderConnection identity
where required

serviceability contract identity/version

provider-contract provenance

applicable routing qualifications
```

The portfolio contains no credentials and no live readiness state.

---

# 7. Routing-Scope Ownership

Where GrandRue supplies the carrier infrastructure and individual carrier choice is not itself merchant business semantics:

```text
routingScope = PLATFORM_ROUTED
```

SHALL be preferred.

Example:

```text
merchant says:
deliver customer orders

GrandRue determines:
appropriate admitted carrier
for each exact route
```

A change in the platform's carrier portfolio therefore need not rewrite every merchant's business configuration.

Where the merchant chooses from supported carrier options presented by GrandRue, that choice SHALL be constrained to carriers available for the merchant's location and applicable movement context.

---

# 8. Merchant-Specific Carrier Accounts Remain Supported

Where the merchant's own carrier account or deliberate provider selection materially governs execution:

```text
routingScope = MERCHANT_ROUTED
```

continues to apply.

Examples include:

```text
merchant contracts directly
with one carrier

merchant-owned ProviderConnection

merchant explicitly requires
a particular supported provider
```

GrandRue SHALL still present only carrier options available in the merchant's location and applicable movement context.

An exact merchant selection may remain:

```text
EXACT_FULFILLER
```

and SHALL NOT be silently replaced merely because GrandRue prefers another carrier.

---

# 9. GrandRue-Managed Selection Is Not Merchant Policy

Where routing is `PLATFORM_ROUTED`, selection such as:

```text
national carrier for one country

different national/regional carrier
for another country

local on-demand network
for a same-locality movement
```

is infrastructure resolution.

It does not redefine:

```text
what customer ordered

whether delivery was promised

destination

delivery charge

merchant return policy

Order quantity

Order Fulfilment quantity
```

Those remain with their existing owners.

Where the merchant is choosing a carrier from GrandRue-presented options, the available options are infrastructure choices exposed within the merchant's location and movement constraints; the choice does not transfer carrier-coverage administration to the merchant.

---

# 10. Provider Serviceability

**Provider Serviceability** means:

> A current, route-qualified determination of whether an admitted provider path is capable of serving the exact required physical movement according to its accepted provider contract.

Initial result vocabulary:

```text
SERVICEABLE

NOT_SERVICEABLE

UNKNOWN
```

`UNKNOWN` SHALL fail closed for new carrier selection.

---

# 11. Serviceability Is Not Readiness

Hard distinction:

```text
SERVICEABILITY
    can this provider serve
    this exact movement geography?

READINESS
    can this exact provider/connection
    currently be executed safely?
```

Therefore all combinations are possible conceptually:

```text
SERVICEABLE + READY

SERVICEABLE + NOT_READY

SERVICEABLE + UNKNOWN readiness

NOT_SERVICEABLE + provider otherwise healthy
```

Provider-wide health does not establish route serviceability.

---

# 12. Route-Qualified Serviceability Context

Serviceability SHALL be evaluated from server-established authoritative movement context.

It MAY include, where already legitimately available:

```text
origin country

origin region/locality/postal geography

origin coordinates where authorised

destination country

destination region/locality/postal geography

destination coordinates where authorised

same/different country relationship

same/different applicable locality relationship

committed movement method

exact shipment-preparation role/context

required provider obligations
```

This amendment does not create a new geographic truth owner.

It consumes location/geography evidence owned by existing authorities.

---

# 13. Country Alone Is Insufficient

The following inference is prohibited:

```text
Provider P supports United Kingdom
        ↓
Provider P can serve every UK route
```

Likewise:

```text
Provider Q operates in Germany
        ↓
every German postcode is serviceable
```

Serviceability MAY be resolved at finer granularity where provider infrastructure requires it.

Examples include:

```text
region
city/locality
postal area
provider zone
route pair
geographic boundary
```

Exact coverage representation is implementation/provider-contract scope.

---

# 14. Local Infrastructure Matters

Route resolution SHALL be capable of representing cases where materially different infrastructure exists within the same country.

Illustrative only:

```text
same-city movement
    → local/on-demand provider
      may be serviceable

inter-city domestic movement
    → national parcel/postal carrier
      may be serviceable

rural origin
    → local on-demand provider
      may be NOT_SERVICEABLE

cross-border movement
    → different candidate portfolio
      may apply
```

These are routing situations, not fixed carrier brands.

---

# 15. No Hard-Coded Brand Geography

MS-PROT-048 SHALL NOT contain normative rules such as:

```text
United Kingdom → Royal Mail

Germany → DHL

same city → Uber
```

Such mappings would become stale infrastructure assumptions embedded in semantic authority.

Instead:

```text
geographic/serviceability evidence
+
exact admitted provider portfolio
+
exact routing policy revision
        ↓
provider resolution
```

determines the result.

Provider names belong in admitted provider/routing data.

---

# 16. Illustrative Carrier Behaviour

A conforming deployment could legitimately resolve:

```text
UK domestic movement
    → admitted Provider A

German domestic movement
    → admitted Provider B

same-locality on-demand movement
    → admitted Provider C
```

where those providers are actually serviceable and ready under the governing revision.

Today those roles might correspond to familiar national or local carrier brands.

The protocol does not assume that the same brands, coverage or relative suitability remain true later.

---

# 17. Provider Serviceability Evidence

An accepted provider serviceability contract MAY consume evidence such as:

```text
provider-declared service zones

authenticated provider coverage lookup

admitted platform-maintained
coverage datasets

postal/route tables

provider network capability metadata

current accepted operational
coverage evidence
```

Evidence source and freshness semantics SHALL be explicit.

---

# 18. AI Cannot Establish Serviceability

AI MAY:

```text
interpret a merchant-facing request

explain why a carrier was unavailable

summarise routing choices
```

AI SHALL NOT authoritatively infer:

```text
this postcode is covered

this carrier operates here

this route is supported

this provider is currently available
```

from model knowledge or web-style inference.

Provider Serviceability requires deterministic admitted evidence.

---

# 19. Serviceability Evidence Freshness

Carrier infrastructure can change without a semantic release.

Therefore serviceability evidence SHALL have explicit freshness or validity interpretation.

Rejected:

```text
provider served this town last year
        ↓
SERVICEABLE forever
```

No universal TTL is imposed.

The provider contract determines evidence validity.

When required evidence cannot establish current serviceability:

```text
UNKNOWN
```

not:

```text
SERVICEABLE
```

---

# 20. Static Provider Compatibility Still Applies First

A candidate cannot reach serviceability evaluation unless it already satisfies the applicable MS-PROT-048 static contract.

At minimum:

```text
registered provider

registered Shipment role compatibility

required obligations supported

interaction contract compatible

evidence contract compatible

required connection model compatible
```

must already hold.

Route serviceability cannot make an incompatible provider compatible.

---

# 21. Runtime Candidate Resolution

For one new Shipment preparation, resolution SHALL be:

```text
exact candidate portfolio revision
        ↓
static-compatible candidates
        ↓
evaluate exact-route serviceability
        ↓
retain SERVICEABLE candidates
        ↓
evaluate applicable current readiness
        ↓
retain executable candidates
        ↓
apply exact routing-policy revision
        ↓
one exact provider path
```

Only then may a `ShipmentPreparationRequest` be durably established.

---

# 22. Route Selection Policy Revision

A serviceability-resolved binding SHALL reference an immutable:

```text
ProviderRouteSelectionPolicyRevision
```

The policy determines how multiple otherwise eligible candidates are resolved.

It SHALL be deterministic and version-identifiable.

It MUST NOT be:

```text
whatever provider responds first

AI preference

frontend choice

unordered database result

random provider

mutable global carrier variable
```

unless a separately accepted policy explicitly authorises such behaviour.

---

# 23. Initial Selection Inputs

The initial policy MAY distinguish candidates using only accepted infrastructure/routing evidence, including:

```text
exact serviceability

applicable movement geography

current Provider Readiness

registered route/service fit

exact platform routing precedence
for the applicable geographic context
```

This amendment does not admit unconstrained optimisation.

---

# 24. “Best” Has a Governed Meaning

GrandRue SHALL NOT claim one provider is objectively:

```text
BEST
```

without a governed comparison definition.

For this amendment:

> “preferred carrier” means the highest-priority eligible carrier under the exact accepted route-selection policy revision.

It does not inherently mean:

```text
cheapest

fastest

most reliable

lowest carbon

highest customer rating
```

Those optimisation dimensions require separately accepted data, semantics and policy.

---

# 25. Geographic Preference Profiles

A route-selection policy MAY contain different provider precedence for different admitted geographic contexts.

Conceptually:

```text
geographic context A
    candidate preference:
        P1
        P2
        P3

geographic context B
    candidate preference:
        P4
        P2
```

Only providers that are actually:

```text
SERVICEABLE
+
currently executable
```

remain candidates.

The preference list does not manufacture coverage.

---

# 26. Local-On-Demand vs Wider-Area Delivery

The routing model SHALL permit an admitted local/on-demand provider to outrank a wider-area carrier for an exact locality-bound movement where:

```text
provider is serviceable

applicable obligations match

readiness permits execution

governing routing policy
gives that path precedence
```

The architecture SHALL also permit that provider to disappear automatically from consideration when the destination lies outside its serviceable infrastructure.

No business-category branch is required.

---

# 27. No Merchant Carrier Matrix

GrandRue SHALL NOT require ordinary merchants to configure:

```text
if postcode starts SA → Carrier A

if London → Carrier B

if Germany → Carrier C

if local → Carrier D
```

where GrandRue manages the carrier infrastructure.

Infrastructure selection is an administrative-compression responsibility.

Merchants SHALL be able to choose a carrier from the supported options GrandRue presents, provided those options are available for the merchant's location and applicable movement context.

Exception-driven intervention may be exposed when:

```text
no serviceable route exists

provider account action is required

merchant-specific decision is genuinely required
```

---

# 28. No Serviceable Provider

Where every admitted candidate resolves:

```text
NOT_SERVICEABLE
```

the route result SHALL be:

```text
NO_SERVICEABLE_PROVIDER
```

GrandRue SHALL NOT silently:

```text
change destination

change delivery to collection

invent merchant self-delivery

select an unregistered carrier

pretend dispatch succeeded
```

The business operation remains unresolved according to its owning workflow.

---

# 29. Serviceability Unresolved

Where required evidence cannot establish whether any candidate serves the route:

```text
SERVICEABILITY_UNRESOLVED
```

SHALL remain distinguishable from:

```text
NO_SERVICEABLE_PROVIDER
```

This matters operationally:

```text
NO_SERVICEABLE_PROVIDER
    known coverage failure

SERVICEABILITY_UNRESOLVED
    insufficient trustworthy evidence
```

Both fail closed for new provider side effects.

---

# 30. Multiple Eligible Providers

If multiple providers remain eligible, the exact policy SHALL resolve one.

If the accepted policy cannot deterministically choose:

```text
ROUTE_SELECTION_UNRESOLVED
```

is required.

GrandRue SHALL NOT use arbitrary list order.

---

# 31. Readiness-Aware Fallback Before Durable Attempt

For a new operation, an accepted serviceability-resolved policy MAY choose the next eligible provider where a more preferred candidate is:

```text
NOT_READY
```

or:

```text
UNKNOWN
```

provided:

```text
no provider side effect
for that candidate has begun

no ShipmentPreparationRequest
has been durably bound to it
```

This is governed candidate resolution, not silent substitution of an existing attempt.

---

# 32. Exact Provider Affinity Before Side Effect

Before any external carrier side effect, the resulting `ShipmentPreparationRequest` SHALL retain:

```text
exact provider identity

exact ProviderConnection
where applicable

exact candidate portfolio revision

exact route-selection policy revision

exact serviceability decision/provenance

exact governing fulfilment binding revision

stable provider correlation identity
```

After this point the request is provider-affined.

---

# 33. No Silent Rebinding After Durable Acceptance

Once:

```text
ShipmentPreparationRequest
    → Provider A
```

has been durably accepted, later changes to:

```text
preferred provider

portfolio revision

serviceability data

provider ranking

current deployment

merchant configuration
```

SHALL NOT reinterpret that request as:

```text
ShipmentPreparationRequest
    → Provider B
```

Historical affinity remains mandatory.

---

# 34. Provider Failure After Selection

If Provider A becomes unavailable after the request is affined:

```text
Provider A not ready
```

does not automatically mean:

```text
run same request against Provider B
```

The existing request must first be resolved according to its certainty/reconciliation contract.

Only a separately authorised new logical Shipment preparation attempt may undergo route resolution again.

This prevents duplicate carrier bookings and duplicate physical movement.

---

# 35. Provider Callback After Portfolio Change

Suppose:

```text
R7 preferred Provider A

ShipmentPreparationRequest S1
bound to Provider A

later routing revision R8
prefers Provider B
```

A callback for S1 SHALL still resolve against:

```text
Provider A
+
historical connection
+
historical binding
+
historical route policy provenance
```

R8 is irrelevant to interpretation of S1.

---

# 36. Merchant Provider Preference Boundary

Where the merchant has made a legitimate exact provider-routing choice:

```text
EXACT_FULFILLER
```

GrandRue SHALL NOT override it through platform routing optimisation.

Where the merchant uses GrandRue-managed carrier resolution:

```text
SERVICEABILITY_RESOLVED
```

the merchant may choose from the supported carrier options GrandRue presents for the merchant's location and applicable movement context. GrandRue SHALL NOT require the merchant to maintain carrier coverage rules, postcode matrices or provider fallback logic.

This preserves merchant control without transferring infrastructure administration to the merchant.

---

# 37. Collection and Merchant Self-Delivery

No external provider resolution is required for:

```text
COLLECTION_HANDOVER
```

or an accepted:

```text
merchant self-delivery path
```

that requires no external carrier.

Canonical:

```text
fulfilment method does not require
external shipment provider
        ↓
do not run carrier resolution
```

The existence of carrier infrastructure SHALL NOT force external carrier use.

---

# 38. Committed Fulfilment Terms Remain Authoritative

Carrier resolution SHALL operate within the Order's already-committed fulfilment terms.

It SHALL NOT silently change:

```text
collection → delivery

delivery → collection

destination

customer-agreed movement method

other commitment-affecting terms
```

merely because another provider would be easier to use.

Provider routing adapts infrastructure to the commitment.

It does not adapt the commitment to infrastructure.

---

# 39. Open-First Rule Survives

MS-PROT-048 v1.1 remains authoritative.

Physical carrier-network access may inherently require an external provider because a software library cannot substitute for:

```text
postal network

courier fleet

physical transport infrastructure
```

Open libraries SHOULD still be preferred where suitable for:

```text
protocol/client implementation

routing adapters

address handling

non-authoritative technical support
```

but open software does not manufacture a physical delivery network.

---

# 40. Provider Routing Is Not Commercial Entitlement

This amendment introduces no subscription entitlement.

Carrier serviceability answers:

```text
can this infrastructure serve the route?
```

Commercial authority answers:

```text
is the underlying protected
business operation commercially permitted?
```

MS-PROT-060 v1.2 remains no-independent-entitlement support/residual authority for fulfilment of existing Orders.

Carrier/provider commercial arrangements, usage charges and future premium integrations remain separately governed.

---

# 41. Failure Vocabulary

The Shipment/provider boundary SHALL distinguish at least:

```text
NO_SERVICEABLE_PROVIDER

SERVICEABILITY_UNRESOLVED

ROUTE_SELECTION_UNRESOLVED

PROVIDER_NOT_READY

PROVIDER_BUSINESS_REJECTION

PROVIDER_EXECUTION_UNCERTAIN

HISTORICAL_BINDING_UNRESOLVABLE

TECHNICAL_FAILURE
```

These SHALL NOT be collapsed into:

```text
DELIVERY_FAILED
```

before an authoritative Shipment outcome actually establishes such meaning.

---

# 42. Falsification

## UK domestic route

An admitted national carrier is serviceable and ready.

Expected:

```text
eligible
→ selectable under exact routing policy
```

**PASS**

## Different national infrastructure

The admitted carrier portfolio for another country differs.

Expected:

```text
different serviceability result
and/or routing precedence
without changing Shipment semantics
```

**PASS**

## Same-locality on-demand provider

An admitted local delivery network serves the exact origin/destination locality.

Expected:

```text
may outrank wider-area carrier
where exact routing policy says so
```

**PASS**

## Rural destination

Local on-demand provider does not cover destination.

Expected:

```text
NOT_SERVICEABLE
→ excluded
```

**PASS**

## Provider supports country but not postcode

Expected:

```text
country-level presence
does not manufacture SERVICEABLE
```

**PASS**

## Cross-border route

Only cross-border-capable admitted providers remain serviceable.

Expected:

```text
domestic-only candidate excluded
```

**PASS**

## Provider healthy but route unsupported

Expected:

```text
NOT_SERVICEABLE
even if provider health/readiness
is otherwise good
```

**PASS**

## Route serviceable but connection revoked

Expected:

```text
SERVICEABLE
+
NOT_READY
→ no new execution
```

**PASS**

## Preferred provider temporarily unavailable

A lower-priority candidate is serviceable and READY before any durable attempt exists.

Expected:

```text
accepted routing policy may select
next eligible provider
```

**PASS**

## Provider outage after request accepted

Expected:

```text
do not silently substitute provider
reconcile/resolve historical request first
```

**PASS**

## Two equally eligible candidates and no discriminator

Expected:

```text
ROUTE_SELECTION_UNRESOLVED
```

not arbitrary selection.

**PASS**

## No admitted carrier serves route

Expected:

```text
NO_SERVICEABLE_PROVIDER
```

No silent switch to collection.

**PASS**

## Merchant has exact carrier account

Expected:

```text
merchant-routed exact binding survives
GrandRue does not override it
```

**PASS**

## Carrier portfolio changes tomorrow

Expected:

```text
new work may use new revision
existing Shipment attempts retain
historical provider affinity
```

**PASS**

## Collection Order

Expected:

```text
carrier resolution not invoked
```

**PASS**

## Small merchant

Merchant supplies ordinary delivery facts only.

Expected:

```text
no country/provider matrix
no postcode routing rules
no carrier-integration administration
```

**PASS**

## Merchant carrier choice

Merchant chooses a carrier from the options GrandRue presents.

Expected:

```text
only carriers available for the
merchant's location and movement context
are presented

merchant choice is preserved
GrandRue does not require coverage
or fallback configuration
```

**PASS**

---

# 43. Alternatives Rejected

## Hard-coded country-to-carrier table in Shipment

Rejected because provider infrastructure changes and Shipment does not own routing infrastructure.

## `if country == UK then RoyalMail`

Rejected because national presence does not prove exact route serviceability and embeds brands in business semantics.

## AI chooses the carrier

Rejected because model confidence is not provider serviceability or routing authority.

## Provider health chooses the carrier

Rejected because readiness and geographic serviceability answer different questions.

## Merchant configures every locality

Rejected because this transfers infrastructure complexity to small merchants.

## Mutable `currentBestCarrier`

Rejected because it destroys revision affinity and deterministic historical reconstruction.

## Silent failover after provider side effect

Rejected because it can duplicate bookings and physical movement.

## One carrier per country

Rejected because countries contain materially different local infrastructure and movement patterns.

## One global carrier

Rejected because no provider-neutral architecture should assume universal physical-network coverage.

## Separate Logistics semantic authority

Rejected because the missing responsibility is provider routing, already owned by MS-PROT-048.

---

# 44. Deferred Scope

This amendment deliberately does not establish:

```text
live carrier price comparison

cheapest-carrier optimisation

delivery-time optimisation

carbon optimisation

provider-rating optimisation

customer carrier selection

package-level logistics

weight/dimension rating

live carrier-rate quoting

warehouse routing

customs/import brokerage

multi-carrier load balancing

arbitrary traffic splitting

provider marketplace

specific Royal Mail integration

specific DHL integration

specific Uber integration

specific provider SDK

specific provider API

exact coverage-dataset format

exact geospatial engine

exact routing-rule persistence
```

Any such feature requires its own accepted business/infrastructure justification.

---

# 45. Conformance Criteria

A conforming implementation MUST prove:

```text
[ ] Shipment remains owner of movement truth

[ ] MS-PROT-048 remains owner of provider routing

[ ] Provider Serviceability is distinct from Provider Readiness

[ ] exact route/locality may affect carrier availability

[ ] country-level provider presence is insufficient by itself

[ ] only registered providers may enter resolution

[ ] AI cannot establish coverage

[ ] exact routing policy is deterministic and revisioned

[ ] no arbitrary provider ordering is used

[ ] merchant-specific exact routing is not overridden

[ ] merchants may choose among supported carriers
    presented as available for their location

[ ] platform-managed routing does not require
    merchant carrier matrices

[ ] collection/self-delivery bypass carrier resolution

[ ] committed fulfilment terms cannot be rewritten
    to fit available providers

[ ] exact provider affinity is durable before side effect

[ ] later routing changes do not rewrite existing attempts

[ ] uncertain existing provider attempts are not
    silently rebound

[ ] no hard-coded provider brand is semantic authority

[ ] failures distinguish lack of coverage,
    unresolved coverage, readiness and execution uncertainty
```

---

# 46. Amendment Effect

This amendment closes the architectural gap required to support locality-sensitive carrier selection while preserving the current Provider Fulfilment model and allowing merchants to choose from carrier options GrandRue presents as available for their location.

Composite resolution becomes:

```text
business commitment
        ↓
MS-PROT-060 fulfilment requirement
        ↓
MS-PROT-048 routing mode
        ↓
exact route context
        ↓
provider serviceability
        ↓
provider readiness
        ↓
deterministic route resolution
        ↓
exact provider affinity
        ↓
ShipmentPreparationRequest
        ↓
provider execution
        ↓
MS-PROT-060 Shipment evidence
```

It does not create:

```text
new Shipment semantics

new Order semantics

new commercial entitlement

a Logistics bounded context

hard-coded country/provider rules

autonomous AI routing

price/rate optimisation
```

# Recommendation

**RECOMMENDATION: ACCEPT**