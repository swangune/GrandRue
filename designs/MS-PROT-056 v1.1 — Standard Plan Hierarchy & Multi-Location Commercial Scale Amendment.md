# MS-PROT-056 v1.1 — Standard Plan Hierarchy & Multi-Location Commercial Scale Amendment

**Document ID:** MS-PROT-056  
**Version:** 1.1  
**Status:** **ACCEPTED after vision/mission review, cross-domain falsification and manual approval**  
**Amends:** MS-PROT-056 v1.0 — Subscription Plans, Full-Experience Entitlements & Commercial Availability Model  
**Depends on:** MS-PROT-031 v1.1, MS-PROT-040 v1.0, MS-PROT-047 v1.0, MS-PROT-049 v1.0, MS-PROT-050 v1.2, MS-PROT-051 v1.0, MS-PROT-052 v1.1, MS-PROT-054 v1.0, MS-PROT-056 v1.0  
**Purpose:** Resolve the standard Main Street plan hierarchy and its relationship to merchant scale, especially multi-location merchants, without turning business size, location count, billing cadence or negotiated commercial scale into semantic capability architecture.

---

# 1. Governing amendment

MS-PROT-056 v1.0 remains authoritative for:

```text
commercial entitlement versus capability semantics
30-day full-experience entitlement
Day-15 subscription reminders
monthly and annual billing
initial 5% annual discount
early-subscription experience preservation
immediate paid upgrades
end-of-period downgrades/cancellation
configuration/data preservation
residual-management protection
provider/trust/actor separation
```

MS-PROT-056 v1.1 adds the following governing decision:

> **Main Street shall have three standard plan value levels: FREE, BUSINESS and GROWTH. These levels correspond to the merchant outcomes Establish, Operate and Grow. Merchant scale—including multiple operational locations—is an orthogonal commercial dimension and shall not by itself force movement to a higher standard plan.**

Canonical separation:

```text
STANDARD VALUE LEVEL
    FREE     → Establish
    BUSINESS → Operate
    GROWTH   → Grow

        ≠

MERCHANT SCALE
    zero / one / multiple locations
    operational volume
    future justified capacity dimensions

        ≠

BILLING CADENCE
    MONTHLY / ANNUAL

        ≠

SEMANTIC CAPABILITY GRAPH
```

---

# 2. Why three standard plans

Main Street's mission already defines three merchant outcomes:

```text
ESTABLISH
OPERATE
GROW
```

The standard commercial hierarchy shall mirror those outcomes rather than introduce additional tiers solely to create more pricing steps.

The hierarchy is therefore:

```text
FREE
  ↓
BUSINESS
  ↓
GROWTH
```

The hierarchy describes increasing classes of Main Street value.

It does not describe merchant prestige, company size, legal form, staff count, branch count or technical sophistication.

Hard rule:

> **A new standard plan level requires evidence of a materially distinct merchant value state, not merely a desire to segment price or monetise scale.**

---

# 3. FREE means Establish

The purpose of `FREE` is:

> **Enable a merchant to establish and maintain a useful professional digital presence.**

The exact entitlement catalogue remains a versioned commercial policy, but the level exists to support ordinary baseline digital presence rather than intentionally cripple the merchant's representation.

Representative value includes, where supported by the applicable architecture and current catalogue:

```text
merchant public presence
website/storefront presence
public business information
publications/announcements
basic discoverability
basic contact/enquiry presence
basic catalogue/listing presentation
basic analytical visibility
```

The Free plan remains permanent under MS-PROT-056 v1.0.

Hard rule:

> **Free shall not mean insecure, unreliable, inaccessible or deliberately incorrect Main Street. Platform safety, data integrity and baseline product quality are not premium entitlements.**

---

# 4. BUSINESS means Operate

The purpose of `BUSINESS` is:

> **Enable Main Street to participate in the merchant's ordinary day-to-day digital business operation.**

Representative operational value may include, where relevant and supported:

```text
Booking
Appointment
Scheduling
Orders
Payment participation
Inventory
Staff operation
Customer-operation surfaces
operational messaging
operational notifications
custom-domain/platform services
ordinary operational integrations
```

The exact mapping remains a versioned entitlement catalogue rather than a semantic rule embedded in capability handlers.

Hard rule:

> **Ordinary coherent business operation shall not be deliberately fragmented across multiple standard paid tiers merely to manufacture upgrade pressure.**

A merchant should be able to understand Business as:

```text
Main Street helps me operate my business.
```

---

# 5. GROWTH means Grow

The purpose of `GROWTH` is:

> **Enable Main Street to help the merchant improve, automate, optimise and grow beyond ordinary digital operation.**

Representative value may include, where supported:

```text
advanced analytics
business intelligence
customer segmentation
marketing automation
advanced campaign capability
advanced customer engagement
growth-oriented AI assistance
advanced optimisation
advanced operational automation
```

The exact catalogue remains commercial policy.

A merchant should be able to understand Growth as:

```text
Main Street helps me grow and optimise my business.
```

---

# 6. Standard plan hierarchy is monotonic

Within one simultaneously published standard-plan catalogue revision:

```text
Entitlements(FREE)
    ⊆
Entitlements(BUSINESS)
    ⊆
Entitlements(GROWTH)
```

A higher standard plan shall not remove an entitlement granted by the lower standard plan in the same catalogue revision.

Therefore:

```text
upgrade
    → additive commercial availability
```

not:

```text
upgrade
    → gain X but unexpectedly lose Y
```

Hard invariant:

> **Within the standard plan family, moving upward shall not reduce the merchant's resolved standard-plan entitlement set.**

---

# 7. Monotonic hierarchy does not create live inheritance

MS-PROT-056 v1.0 remains authoritative that plan presentation hierarchy is not mutable runtime inheritance.

The standard hierarchy is resolved at plan-catalogue publication time.

Conceptually:

```text
Standard Plan Catalogue Revision K

FREE revision Fk
    ↓ explicit resolved entitlement set

BUSINESS revision Bk
    ↓ explicit resolved entitlement set
    and must be a superset of Fk

GROWTH revision Gk
    ↓ explicit resolved entitlement set
    and must be a superset of Bk
```

After publication, each plan revision retains its own explicit resolved entitlement set.

Rejected:

```text
Growth dynamically extends whatever Business means today
```

because a later Business change could silently change historical Growth subscriptions.

Hard rule:

> **Hierarchy is a publication-time commercial invariant; historical entitlement interpretation remains revision-stable.**

---

# 8. Plan catalogue revision

To compare standard plan levels coherently, Main Street shall retain enough commercial provenance to identify which standard plan revisions were published together as one catalogue generation/revision.

Conceptually:

```text
StandardPlanCatalogueRevision
{
    catalogueRevisionIdentity
    freePlanRevision
    businessPlanRevision
    growthPlanRevision
    provenance
    effectivePolicyWindow
}
```

This is conceptual and does not mandate one Java aggregate.

The catalogue revision permits Main Street to validate:

```text
FREE ⊆ BUSINESS ⊆ GROWTH
```

without introducing live runtime inheritance.

---

# 9. Upgrade and downgrade now have precise standard-plan meaning

Within the standard hierarchy:

```text
FREE < BUSINESS < GROWTH
```

An **upgrade** is movement to a higher standard plan level.

Examples:

```text
FREE → BUSINESS
FREE → GROWTH
BUSINESS → GROWTH
```

MS-PROT-056 v1.0 remains authoritative:

```text
upgrade
    → effective immediately once validly accepted
```

A **downgrade** is movement to a lower standard plan level.

Examples:

```text
GROWTH → BUSINESS
GROWTH → FREE
BUSINESS → FREE
```

MS-PROT-056 v1.0 remains authoritative:

```text
downgrade
    → effective at current paid-period end
```

---

# 10. Billing cadence is orthogonal to plan hierarchy

These are the same standard value level:

```text
BUSINESS MONTHLY
BUSINESS ANNUAL
```

and:

```text
GROWTH MONTHLY
GROWTH ANNUAL
```

Therefore changing:

```text
BUSINESS MONTHLY
    →
BUSINESS ANNUAL
```

is a billing-cadence change, not a plan-level upgrade.

Likewise:

```text
BUSINESS ANNUAL
    →
BUSINESS MONTHLY
```

is not a plan-level downgrade merely because the commitment period becomes shorter.

The timing and financial treatment of cadence-only changes remain commercial/billing policy outside this amendment unless separately accepted.

---

# 11. Enterprise is not a fourth standard value level

The prior PRD's `ENTERPRISE` tier attempted to combine:

```text
advanced capabilities
+
large-scale usage
+
priority infrastructure
+
negotiated integrations/support
```

Those are not one coherent fourth merchant value state.

Main Street therefore shall not use `ENTERPRISE` as a required fourth rung in the standard entitlement hierarchy.

Rejected standard hierarchy:

```text
FREE < BUSINESS < GROWTH < ENTERPRISE
```

Current standard hierarchy:

```text
FREE < BUSINESS < GROWTH
```

Main Street may later use a commercial label such as:

```text
Scale
Custom
Enterprise
```

for negotiated capacity, support, contractual or integration arrangements where justified.

Such an arrangement is not automatically a new semantic plan level and shall not be required merely because a merchant is large or multi-location.

---

# 12. Merchant size is not plan level

The following must remain distinct:

```text
merchant size
        ≠
standard plan level
```

Examples:

- a one-location salon may legitimately choose `GROWTH` because it values advanced automation and analytics;
- a ten-location merchant may legitimately use `BUSINESS` if ordinary digital operation is the value it needs;
- a large merchant may require negotiated scale terms while retaining `BUSINESS` or `GROWTH` entitlements;
- a sole trader may use `GROWTH` without becoming an enterprise merchant.

Hard invariant:

> **Standard plan level represents the class of value Main Street provides, not a classification of merchant size.**

---

# 13. Multi-location merchants remain one Merchant Scope where the business authority is one merchant

MS-PROT-051 and MS-PROT-031 remain authoritative:

```text
Merchant Scope
    ≠
Merchant Location
```

A merchant may have:

```text
zero locations
one location
multiple locations
```

without becoming multiple Main Street tenants merely because locations differ.

Example:

```text
Joe's Barbers
    ├── Swansea
    ├── Cardiff
    └── Newport
```

may remain one Merchant Scope where those locations belong to one merchant authority.

Hard rule:

> **Location count shall not silently redefine tenant identity or force separate merchant accounts.**

---

# 14. Location count is not plan hierarchy

Rejected:

```text
one location
    → BUSINESS

two or more locations
    → GROWTH or ENTERPRISE
```

Correct separation:

```text
Plan Level
    FREE / BUSINESS / GROWTH

        ×

Merchant Scale
    location participation / other justified scale dimensions
```

A merchant does not become `GROWTH` merely by opening another branch.

Likewise a merchant does not remain `BUSINESS` merely because it has one branch if it wants Growth-level value.

---

# 15. Descriptive Merchant Locations are not automatically billable scale units

MS-PROT-051 permits a Merchant Location to represent public, private, administrative, customer-visit or other merchant-scoped physical presence under accepted semantics.

A Merchant Location does not automatically become:

```text
bookable
inventory-bearing
staff-bearing
schedulable
order-fulfilment location
capacity-bearing
```

Therefore:

```text
MerchantLocation record exists
        ≠
commercial scale charge applies
```

Examples that shall not by themselves manufacture a location charge include:

```text
private administrative office
public descriptive branch address
service base that does not participate in charged operations
additional location retained only for accurate business presence
```

Hard invariant:

> **Main Street shall not monetise a Merchant Location merely because an address/location fact exists.**

---

# 16. Operational location scale is an orthogonal commercial dimension

Where future commercial policy charges for additional locations, the charging basis must correspond to meaningful operational participation or justified platform consumption rather than raw location-record count.

Conceptually:

```text
Plan
    BUSINESS or GROWTH

+
Scale terms
    based on justified operational participation
```

Potential evidence may eventually include a location participating in registered capability contexts such as:

```text
location-scoped staff operation
location-scoped inventory
location-scoped scheduling/booking
location-scoped order fulfilment
location-scoped operational configuration
```

This list is illustrative, not a current billing algorithm.

Exact location allowances, thresholds, per-location prices, volume discounts and metering rules remain future commercial-policy decisions.

Hard rule:

> **Scale pricing may consume accepted operational/location semantics; it shall not redefine those semantics or turn `MerchantLocation` into a billing primitive.**

---

# 17. Location capability participation may differ

Locations belonging to one merchant need not participate identically in every capability.

Example:

```text
Merchant: Joe's Barbers

Swansea
    booking enabled/configured for location context
    staff assigned

Cardiff
    booking enabled/configured for location context
    staff assigned

Newport
    walk-in/public-presence only
```

The commercial model shall not require a `MultiLocationBusinessCapability` or separate category template to represent this variation.

Capability/configuration scope remains governed by the applicable accepted semantic contracts.

---

# 18. Full-experience period applies across the merchant's relevant configured model

MS-PROT-056 v1.0 remains authoritative that the initial full-experience grant covers the merchant-relevant supported operating model.

For a multi-location merchant, the 30-day experience shall not artificially restrict evaluation to one location merely because the merchant has several relevant operating locations.

Example:

```text
Merchant
    Swansea  → Booking relevant
    Cardiff  → Booking relevant
    Newport  → Booking relevant

30-day full experience
    → commercial entitlement barrier removed
      for the relevant configured model across those contexts
```

Ordinary semantic/configuration, trust, actor, provider and operational requirements still apply.

This ensures that the merchant can evaluate whether Main Street genuinely streamlines the whole operation.

---

# 19. Plan recommendation may use the configured business model

Because Main Street discovers/configures relevant merchant semantics before commercial filtering, it may determine the lowest standard plan whose entitlement set covers the merchant's currently configured/used commercial requirements.

Conceptually:

```text
configured merchant requirements
        ↓
required commercial entitlements
        ↓
FREE / BUSINESS / GROWTH catalogue comparison
        ↓
lowest standard plan covering them
```

Example merchant-facing result:

```text
Growth is the lowest plan that keeps
all of the functionality you currently use.
```

This is a recommendation/explanation, not a forced plan assignment.

---

# 20. Merchant may choose a lower plan than the recommendation

A merchant remains free to choose a lower standard plan where Main Street supports that commercial choice.

Example:

```text
configured merchant model requires:
    Business operational entitlements
    + Growth advanced analytics

recommended plan:
    GROWTH

merchant selects:
    BUSINESS
```

Result:

```text
Business-entitled functionality
    remains commercially available

Growth-only functionality
    remains configured/preserved where applicable
    but becomes commercially unavailable for protected new use
```

MS-PROT-056 v1.0's preservation and residual-management rules remain authoritative.

Hard rule:

> **Plan recommendation shall not become commercial gatekeeping beyond the entitlement consequences the merchant can review.**

---

# 21. Same platform quality across standard plans

The standard hierarchy shall not be used to sell correctness, baseline security or data integrity as premium capabilities.

Rejected examples:

```text
Free → weaker tenant isolation
Business → ordinary security
Growth → reliable transaction integrity
```

Platform-owned baseline obligations such as:

```text
security invariants
data isolation
data integrity
correct semantic execution
baseline accessibility
baseline reliability
```

remain platform responsibilities according to their governing contracts.

Commercial plans may legitimately differentiate optional advanced services, support or capacity where accepted, but not the correctness of the underlying platform.

---

# 22. Add-ons remain orthogonal

Where Main Street later introduces add-ons:

```text
EffectiveEntitlements
    = standard plan grants
      ∪ add-on grants
      ∪ full-experience grants while active
      ∪ other valid grants
```

An add-on may grant a benefit normally packaged in another level without changing the merchant's standard plan identity if the commercial catalogue permits it.

Therefore:

```text
BUSINESS
+
future Advanced Analytics add-on
```

need not be reclassified as `GROWTH` merely because one Growth-associated entitlement is additionally granted.

Exact add-on catalogues remain future scope.

---

# 23. Negotiated scale arrangements remain outside the standard hierarchy

A merchant with substantial scale may eventually require:

```text
negotiated location pricing
usage/capacity allowances
contractual support
service-level commitments
special integration work
custom commercial terms
```

Those concerns may justify a negotiated commercial arrangement.

They do not by themselves justify a fourth standard semantic/commercial value level.

Conceptually:

```text
Standard plan entitlement level
        FREE / BUSINESS / GROWTH

+
Negotiated scale/commercial terms
```

A large merchant may therefore be, for example:

```text
BUSINESS entitlement level
+
negotiated scale terms
```

or:

```text
GROWTH entitlement level
+
negotiated scale terms
```

without introducing `EnterpriseCapabilityGraph` or another product architecture.

---

# 24. Cross-domain falsification

## 24.1 Information publisher

A scholarship-information publisher may remain `FREE` where its needs are primarily professional presence/publication/enquiry.

It is not forced into Business merely because Main Street has richer operational capabilities elsewhere.

**PASS**

## 24.2 Single-location salon

A salon using ordinary Booking, Scheduling, Payment and Staff operation may select `BUSINESS`.

The merchant does not need Growth merely to operate coherently.

**PASS**

## 24.3 Single-location salon using advanced growth automation

The same salon may select `GROWTH` for advanced analytics, segmentation and automation.

Its single-location status does not make Growth inappropriate.

**PASS**

## 24.4 Three-location barber

One Merchant Scope contains Swansea, Cardiff and Newport locations.

Ordinary operational needs may remain `BUSINESS` while location scale is treated separately by future commercial policy.

The merchant is not forced to Growth/Enterprise merely because location count is three.

**PASS**

## 24.5 Mixed descriptive and operational locations

Merchant has:

```text
public shop
private administrative office
storage/service base
```

The existence of three MerchantLocation records does not automatically create three billable operational locations.

**PASS**

## 24.6 Multi-location Growth merchant

A five-location retailer chooses `GROWTH` because it wants advanced automation/analytics.

Growth entitlement level and location scale compose independently.

**PASS**

## 24.7 Large merchant under negotiated terms

A high-volume merchant with many locations may use `BUSINESS` or `GROWTH` entitlements plus negotiated scale/support terms.

No fourth standard capability tier is required.

**PASS**

## 24.8 Business to Growth upgrade

Upgrade adds the resolved Growth entitlement difference immediately.

Existing location scale does not change merely because plan level changed.

**PASS**

## 24.9 Billing cadence change

Business Monthly to Business Annual remains a cadence change, not a plan-level upgrade.

Entitlements remain identical; the initial 5% annual policy remains governed by MS-PROT-056 v1.0.

**PASS**

## 24.10 New Business catalogue benefit

A later Business catalogue revision adds a benefit.

Historical Growth revisions are not silently mutated through live inheritance; a new catalogue revision resolves explicit plan sets.

**PASS**

---

# 25. Rejected alternatives

| Rejected approach | Why it fails |
|---|---|
| Four standard tiers solely because the PRD listed Enterprise | Adds a level without a distinct merchant value state |
| Enterprise required for multi-location merchants | Conflates scale with value level |
| Growth required above an arbitrary location count | Same conflation and creates artificial upgrade pressure |
| Raw MerchantLocation count as billing quantity | Charges descriptive/private/non-operational location facts as though they were operational consumption |
| Separate tenant/account per branch | Conflicts with Merchant Scope versus Merchant Location where one merchant authority owns the locations |
| Live `Growth extends Business` entitlement inheritance | Allows later lower-plan changes to silently rewrite historical higher-plan meaning |
| Plan tier controls software quality/security | Violates platform obligations and mission accessibility |
| Fragment ordinary operation across paid tiers | Forces merchants to understand packaging to assemble a coherent operating system |
| Treat cadence change as upgrade/downgrade | Confuses price/commitment timing with entitlement value level |
| Create MultiLocationBusiness capability | Turns scale/context into business-category semantics |

No material falsifier remains after the value-level/scale separation.

---

# 26. Accepted invariants

1. Main Street's standard plan hierarchy has three levels: `FREE`, `BUSINESS`, `GROWTH`.
2. Those levels correspond respectively to Establish, Operate and Grow.
3. A fourth standard plan level requires evidence of a materially distinct merchant value state.
4. Within one standard plan catalogue revision, `FREE ⊆ BUSINESS ⊆ GROWTH` by resolved entitlement set.
5. Plan hierarchy is publication-time validation, not live runtime entitlement inheritance.
6. Each published plan revision retains an explicit historically interpretable entitlement set.
7. Plan upgrade means movement upward in `FREE < BUSINESS < GROWTH` and is immediate under MS-PROT-056 v1.0.
8. Plan downgrade means movement downward and becomes effective at current paid-period end under MS-PROT-056 v1.0.
9. Billing cadence remains orthogonal to plan hierarchy.
10. `ENTERPRISE` is not a required fourth standard value level.
11. Merchant size is not standard plan level.
12. Merchant Location count is not standard plan level.
13. Multiple Merchant Locations may remain within one Merchant Scope where one merchant authority owns them.
14. A MerchantLocation record is not automatically a billable scale unit.
15. Any future location-based pricing must correspond to meaningful operational participation or justified platform consumption rather than raw address count.
16. Exact location allowances/prices/thresholds remain future commercial policy.
17. Different locations may participate differently in configured capabilities without a multi-location business template.
18. The 30-day full experience applies across the merchant's relevant supported configured model rather than arbitrarily one location.
19. Main Street may recommend the lowest standard plan covering the merchant's configured commercial requirements.
20. The merchant may select a lower supported plan and accept the resulting commercial locks without losing preserved configuration or residual-management protection.
21. Platform correctness, baseline security and data integrity are not premium plan differentiators.
22. Add-ons and negotiated scale arrangements are orthogonal to the standard plan hierarchy.
23. Large or multi-location merchants do not require a different semantic capability graph solely because of scale.

---

# 27. Decisions intentionally left open

This amendment does not define:

```text
exact FREE entitlement catalogue
exact BUSINESS entitlement catalogue
exact GROWTH entitlement catalogue
plan prices
exact location allowance included in a paid plan
per-operational-location price
location-volume discount schedule
exact definition/metric for billable operational-location participation
usage/capacity metering
negotiated-contract thresholds
support/SLA packages
add-on catalogue
cadence-only change timing/financial treatment
regional tax/VAT treatment
```

Those are commercial-policy/implementation decisions unless later evidence shows that one requires another semantic authority.

---

# 28. Continuous-improvement checkpoint

The earlier four-tier PRD packaged two independent dimensions into `ENTERPRISE`:

```text
more/advanced value
+
merchant scale / priority service
```

The stronger model separates them:

```text
VALUE LEVEL
FREE → BUSINESS → GROWTH

        ×

SCALE / COMMERCIAL CONSUMPTION
locations / capacity / negotiated terms where justified
```

This is simpler for Main Street's target merchants, more faithful to the Establish/Operate/Grow mission, and avoids using plan tiers as hidden business-size categories.

No additional standard plan level or new semantic capability is justified by the current evidence.

---

# 29. Governance verdict

```text
THREE STANDARD VALUE LEVELS      ACCEPTED
FREE = ESTABLISH                 ACCEPTED
BUSINESS = OPERATE               ACCEPTED
GROWTH = GROW                    ACCEPTED
MONOTONIC STANDARD ENTITLEMENTS  ACCEPTED
LIVE PLAN INHERITANCE            REJECTED
ENTERPRISE AS REQUIRED 4TH TIER  REJECTED
MULTI-LOCATION = HIGHER PLAN     REJECTED
RAW LOCATION COUNT BILLING       REJECTED
SCALE ORTHOGONAL TO PLAN LEVEL   ACCEPTED
MONTHLY/ANNUAL ORTHOGONAL        PRESERVED
30-DAY FULL EXPERIENCE           PRESERVED
MANUAL APPROVAL                  ✓
CROSS-DOMAIN FALSIFICATION       ✓
ACCEPT                           ✓
```

### Canonical amended decision

> **Main Street's standard commercial hierarchy is Free → Business → Growth, representing Establish → Operate → Grow. Higher standard levels are monotonic resolved entitlement supersets within a published catalogue revision, but do not use live runtime inheritance. Merchant size and multi-location scale are separate commercial dimensions: additional Merchant Locations do not force a higher plan and do not become billable merely by existing. Future scale pricing must correspond to justified operational participation or consumption while preserving the same capability semantics.**
