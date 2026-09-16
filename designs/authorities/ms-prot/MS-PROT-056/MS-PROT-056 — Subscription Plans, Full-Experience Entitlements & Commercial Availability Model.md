# MS-PROT-056 — Subscription Plans, Full-Experience Entitlements & Commercial Availability Model

**Document ID:** MS-PROT-056  
**Version:** 1.0  
**Status:** **ACCEPTED after cross-domain falsification and manual approval**  
**Depends on:** MS-PROT-021 v1.3, MS-PROT-022 v1.5, MS-PROT-028 v1.3, MS-PROT-039 v1.2, MS-PROT-040 v1.0, MS-PROT-047 v1.0, MS-PROT-048 v1.1, MS-PROT-049 v1.0, MS-PROT-052 v1.1, MS-PROT-053 v1.0, MS-PROT-054 v1.0, MS-PROT-055 v1.0  
**Closes:** DDR-OD-004 — Plan/tier entitlement versus capability semantics  
**Purpose:** Define the authority boundary among Main Street subscription plans, the initial 30-day full-experience period, commercial entitlements, capability/configuration state, billing cadence, upgrade/downgrade timing and effective operation availability without turning pricing tiers, payment-provider state or feature flags into semantic capability authority.

---

# 1. Governing decision

Main Street shall distinguish:

```text
MERCHANT BUSINESS NEED
        ≠
CAPABILITY RELEVANCE / CONFIGURATION
        ≠
SUBSCRIPTION PLAN
        ≠
COMMERCIAL ENTITLEMENT
        ≠
CAPABILITY ACTIVATION
        ≠
ACTOR AUTHORITY
        ≠
TRUST SATISFACTION
        ≠
PROVIDER AVAILABILITY
        ≠
DEPLOYMENT FEATURE FLAG
        ≠
EFFECTIVE OPERATION AVAILABILITY
```

The governing rule is:

> **A commercial entitlement determines whether Main Street commercially permits a merchant to use an otherwise supported semantic or platform service. Entitlement does not create, activate, configure, authorise, verify or fulfil that semantic. Main Street shall configure the merchant's supported operating model according to business need rather than current subscription tier, then apply commercial availability context to determine which configured parts are presently usable for new activity.**

This document also establishes Main Street's initial 30-day full-experience commercial policy and the timing rules for monthly/annual subscription changes.

---

# 2. Why the separation is necessary

A merchant may simultaneously have:

```text
Booking configured             YES
Booking commercially entitled  NO
Booking actor authorised       YES
Booking provider healthy       YES
Existing Booking commitments   YES
```

Those facts do not collapse into one Boolean such as:

```text
bookingEnabled = false
```

because Main Street still needs to know:

- why new Booking activity is unavailable;
- whether existing commitments remain manageable;
- whether an upgrade restores commercial access immediately;
- whether configuration should remain preserved;
- whether provider repair, trust completion or actor authority would change the result;
- whether the storefront should expose new-booking interaction;
- whether the dashboard must preserve residual management.

The same distinction applies to non-capability platform services such as custom domains, advertising removal or advanced analytics.

---

# 3. Plans are commercial catalogues, not semantic architectures

Commercial plan names such as:

```text
FREE
BUSINESS
GROWTH
ENTERPRISE
```

are product/commercial vocabulary.

They are not semantic capability identifiers.

Rejected:

```text
if plan == BUSINESS
    activate Booking

if plan == GROWTH
    activate Marketing
```

Accepted:

```text
Plan Revision
        ↓
resolved entitlement grants

Merchant Configuration
        ↓
active/configured semantic graph

Runtime/contextual resolution
        ↓
semantic graph
+
effective commercial entitlements
+
other authority/runtime requirements
```

Hard rule:

> **Plan identity shall not become a business-category-like runtime branch that defines merchant semantics.**

---

# 4. Subscription Plan Revision

A subscription plan is a versioned commercial proposition from Main Street to a merchant.

Conceptually:

```text
SubscriptionPlanRevision
{
    planIdentity
    revisionIdentity
    entitlementGrants
    billingOptions
    commercialTerms
    provenance
    effectivePolicyWindow
}
```

This structure is conceptual and does not mandate one Java type.

Once a plan revision has governed a merchant subscription or commercial decision, it must remain historically interpretable.

Changing a plan's commercial packaging for future subscriptions shall not silently rewrite the historical meaning of an already accepted plan revision.

---

# 5. Plan presentation hierarchy is not runtime inheritance

Marketing may validly state:

```text
Growth includes everything in Business
```

but runtime entitlement resolution shall not depend on mutable live inheritance where changing Business silently changes existing Growth subscriptions.

A plan revision shall resolve to an explicit effective entitlement grant set.

Therefore:

```text
marketing hierarchy
    ≠
runtime semantic dependency graph
```

---

# 6. CommercialEntitlement

A `CommercialEntitlement` means:

> **A Main Street-owned commercial permission for a merchant to use an identified semantic or platform service for an identified commercial purpose while the entitlement is effective.**

Conceptually:

```text
CommercialEntitlementDefinition
{
    entitlementIdentity
    targetKind
    targetReference
    accessPurpose
}
```

An entitlement references an already-defined semantic/platform concern.

It does not create the concern it references.

---

# 7. Initial entitlement target families

The minimum target families are:

```text
CAPABILITY_NEW_ACTIVITY
OPERATION_ACCESS
FULFILMENT_OR_INTEGRATION_ACCESS
PLATFORM_SERVICE_ACCESS
PRESENTATION_PRIVILEGE
```

These are conceptual target families rather than mandatory persistence enums.

Examples may include:

```text
Booking new customer activity
campaign execution
external-calendar connection
custom-domain service
advanced analytics service
removal of Main Street advertising/branding where commercially offered
```

The target must remain owned by the architecture that defines it.

---

# 8. Not every priced item is a capability

A subscription catalogue may commercially include materially different architectural concerns.

For example:

```text
Online Booking
    → semantic capability/access

Custom Domain
    → platform service

Remove Advertising
    → presentation privilege

External Payment Provider Connection
    → fulfilment/integration access

Advanced Analytics
    → platform/projection service
```

Rejected:

```text
Everything on pricing page
        ↓
pretend it is a Capability
```

Hard rule:

> **The pricing catalogue consumes accepted architectural concepts; it does not redefine every sellable benefit into a capability.**

---

# 9. Entitlement is permissive, not authoritative

Suppose a paid plan grants commercial access to Booking.

Rejected:

```text
merchant subscribes
        ↓
Booking activates automatically
```

Correct:

```text
merchant subscribes
        ↓
Booking commercial entitlement satisfied
```

Booking activation/configuration still follows the accepted semantic/configuration lifecycle.

Therefore:

> **Entitlement may permit semantic use; it shall not manufacture semantic activation.**

---

# 10. Capability state and entitlement state are independent

Conceptually:

| Configured/active semantic | Commercial entitlement | Meaning |
|---|---|---|
| NO | NO | capability absent/not configured |
| NO | YES | commercially permitted but not configured/activated |
| YES | NO | semantic configuration preserved; protected new activity commercially unavailable |
| YES | YES | commercially permitted, subject to all other requirements |

This table does not imply that every capability must use one physical `active` Boolean.

It expresses the authority separation.

---

# 11. Effective entitlement is contextual

Commercial entitlement may change independently of merchant semantic configuration.

Conceptually:

```text
standing plan grants
+
active full-experience grants
+
paid-plan grants
+
add-on grants where supported
+
other valid commercial grants
+
subscription lifecycle policy
+
current time
        ↓
EffectiveEntitlementSet
```

The `EffectiveEntitlementSet` is contextual commercial truth.

It shall not be permanently compiled into merchant semantic configuration merely because it affects current operation availability.

---

# 12. Commercial availability participates in contextual operation eligibility

Conceptually:

```text
registered semantic operation
+
active/configured merchant semantics
+
commercial entitlement
+
actor authority
+
trust satisfaction
+
operational/business invariants
+
applicable provider fulfilment
        ↓
effective operation eligibility
```

Each input retains its own authority.

Rejected:

```text
featureEnabled = true|false
```

as the universal explanation for operation availability.

---

# 13. Permanent Free plan and full-experience period are different concepts

Main Street retains a permanent Free plan as a standing commercial option.

The initial 30-day full-experience period is **not** the Free plan and is **not** a temporary semantic configuration.

Canonical model:

```text
Standing Plan
    = FREE by default unless a paid subscription is accepted

Initial Full-Experience Entitlement Source
    = temporary 30-day commercial grant
```

During the initial experience period:

```text
EffectiveEntitlements
    = standing-plan grants
      ∪ full-experience grants
      ∪ any other valid grants
```

After expiry, if no paid subscription exists:

```text
EffectiveEntitlements
    = FREE-plan grants
      ∪ any other still-valid grants
```

The merchant account is not disabled merely because the full-experience period ends.

---

# 14. Full experience means merchant-relevant supported capabilities

The 30-day experience shall not expose every Main Street capability to every merchant.

Rejected:

```text
every new merchant
    → Booking
    → Inventory
    → Hotel Allocation
    → every future capability
```

Correct:

```text
merchant business needs
        ↓
adaptive semantic discovery
        ↓
merchant-relevant supported capability graph
        ↓
configuration/activation through normal rules
        ↓
full-experience commercial grants apply
```

Hard rule:

> **The merchant receives a full experience of their supported Main Street operating model, not the entire platform catalogue.**

---

# 15. Subscription tier shall not narrow business discovery

Onboarding shall discover and model what the merchant's business needs before using current subscription tier to determine commercial availability.

Rejected:

```text
current plan = FREE
        ↓
do not ask whether merchant needs Booking
```

Accepted:

```text
merchant intent
        ↓
discover Booking need
        ↓
configure/activate supported Booking semantics
        ↓
entitlement resolution determines current commercial availability
```

Hard rule:

> **A merchant's current budget/plan shall not redefine what Main Street understands their business to need.**

---

# 16. Relevant capabilities should remain configured independent of plan

Where a supported capability is relevant to the merchant and the merchant has completed/accepted its required configuration, that configuration should be preserved even when the current plan does not commercially entitle new use.

Conceptually:

```text
Configured business graph
        ≠
currently entitled graph
```

After the full-experience period:

```text
configured capability
+
entitlement absent
        ↓
commercially unavailable for protected new activity
        ↓
configuration/data preserved
```

A later upgrade may restore commercial access without requiring the merchant to rebuild their operating model, subject to current semantic/configuration compatibility.

---

# 17. Full-experience entitlement does not bypass other requirements

The initial experience removes the commercial entitlement barrier for merchant-relevant supported capability use.

It does not bypass:

```text
required semantic configuration
compiler validation
merchant approval where required
actor authority
trust requirements
provider requirements
security/platform invariants
operational preconditions
```

Therefore:

```text
FULL EXPERIENCE
    ≠
IGNORE REQUIREMENTS
```

Example:

A merchant may be commercially entitled to payment execution during the experience period while still being unable to execute payments until applicable provider/trust requirements are satisfied.

---

# 18. Full-experience scope may evolve during the 30 days

The full-experience grant shall not be limited to a static capability list captured on day 1.

If a merchant legitimately adds another supported capability during the active 30-day period:

```text
trial active
+
new merchant-relevant supported capability configured/activated
        ↓
full-experience commercial entitlement applies
```

This allows the merchant to continue discovering how Main Street fits their operation during the experience period.

---

# 19. Initial experience timing

Main Street's initial product policy is:

```text
Day 1
    full-experience period begins

Days 1–14
    full-experience access
    no mandatory daily subscription reminder

Day 15 through expiry
    daily subscription reminder eligibility

Day 30
    full-experience entitlement expires
```

The exact timestamp arithmetic and timezone implementation remain downstream, but the merchant must receive the full agreed experience duration.

---

# 20. Daily subscription reminders begin on day 15

From day 15 until the full-experience entitlement expires, Main Street shall issue a subscription reminder on each applicable day unless the merchant has already completed the required subscription action or another accepted notification policy makes the reminder unnecessary.

The initial policy shall emit no more than one consolidated trial/subscription reminder per merchant per day.

The notification is a commercial communication consequence of the experience lifecycle.

It is not a semantic capability state transition.

---

# 21. Trial reminders should be merchant-specific

Where possible, subscription reminders should explain the actual impact on the merchant's configured operating model.

Conceptually:

```text
merchant configured graph
        -
post-trial standing/selected-plan entitlements
        ↓
affected commercial functionality
        ↓
merchant-facing expiry/upgrade summary
```

Example presentation:

```text
After your full-experience period:
✓ Website/public presence
✓ Enquiries
✗ New online bookings without an eligible paid plan
✗ Payment execution without an eligible paid plan
```

The exact message wording, channel, visual design and notification template remain presentation/notification implementation concerns.

---

# 22. Subscription plan and billing cadence are separate

Paid Main Street plans shall initially support:

```text
MONTHLY
ANNUAL
```

Billing cadence does not change plan entitlements.

Therefore:

```text
Business Monthly
        and
Business Annual
```

represent the same plan entitlement revision under different commercial billing terms.

Hard rule:

> **Billing cadence shall not create a second capability/entitlement tier within the same plan.**

---

# 23. Initial annual discount policy

Main Street's initial commercial policy is:

> **Annual billing receives a 5% discount relative to twelve monthly billing periods for the same plan revision.**

Conceptually:

```text
AnnualPrice
    = 12 × MonthlyPrice × 0.95
```

This is a versioned commercial term, not a semantic capability invariant.

A future change to the discount for future subscriptions is a commercial-plan/pricing revision and shall not require redefining capability semantics.

Existing accepted commercial periods retain the applicable terms under which they were established.

---

# 24. Subscription

Conceptually:

```text
Subscription
{
    merchantScope
    planRevision
    billingCadence
    acceptedCommercialTerms
    acceptedAt
    currentPeriod
    lifecycle
    scheduledChange?
    provenance
}
```

This is conceptual and does not mandate one aggregate or storage shape.

Subscription is Main Street commercial state.

It is not merchant capability configuration.

---

# 25. First paid subscription during the 30-day experience

A merchant may subscribe before the full-experience entitlement expires.

Hard rule:

> **Subscribing early shall never shorten the merchant's remaining full-experience period or reduce their effective entitlement before its scheduled expiry.**

The paid subscription becomes commercially accepted/effective immediately while the remaining full-experience grant remains valid through its original expiry.

Conceptually:

```text
EffectiveEntitlements during remaining experience
    = FullExperienceGrants
      ∪ PaidPlanGrants
      ∪ other valid grants
```

After the original experience expiry:

```text
EffectiveEntitlements
    = PaidPlanGrants
      ∪ other still-valid grants
```

---

# 26. Remaining experience time is preserved before first renewal

When the first paid subscription is accepted during the initial full-experience period, Main Street preserves the remaining experience duration **in addition to** one full purchased billing period.

Canonical rule:

```text
FirstRenewalBoundary
    = OriginalFullExperienceExpiry
      + one full selected billing interval
```

Therefore a merchant subscribing with 10 experience days remaining receives:

```text
10 remaining experience days
+
one complete monthly period
```

or:

```text
10 remaining experience days
+
one complete annual period
```

before the first ordinary renewal boundary.

This prevents early conversion from penalising the merchant.

---

# 27. Day-20 monthly example

Suppose:

```text
30-day experience
subscription accepted on day 20
remaining experience ≈ 10 days
billing cadence = MONTHLY
```

Then:

```text
subscription accepted/effective immediately
        +
remaining experience preserved
        +
one full normal monthly billing interval
        ↓
first ordinary renewal occurs after
original experience expiry + monthly interval
```

If the product's monthly interval is represented as 30 days, the merchant receives approximately:

```text
10 remaining days + 30 paid days
```

before first renewal.

Exact calendar-period representation belongs to the billing implementation, but the full purchased interval must be preserved.

---

# 28. Day-20 annual example

Suppose:

```text
30-day experience
subscription accepted on day 20
remaining experience ≈ 10 days
billing cadence = ANNUAL
```

Then:

```text
subscription accepted/effective immediately
        +
remaining experience preserved
        +
one full annual billing interval
        ↓
first ordinary annual renewal occurs
one year after the original experience expiry
```

The initial 5% annual discount applies according to the accepted annual commercial terms.

---

# 29. Upgrade between paid plans is immediate

A merchant who upgrades from one paid plan to a higher plan receives the target plan's additional commercial entitlements immediately once the upgrade is validly accepted.

Example:

```text
Business
    ↓ upgrade
Growth
```

Result:

```text
Growth entitlement grants effective immediately
```

subject to the ordinary semantic/configuration/trust/provider/runtime requirements of the newly permitted functionality.

Hard rule:

> **An upgrade shall not wait until the old paid period expires merely to make newly purchased entitlement available.**

---

# 30. Paid-plan upgrade financial proration is downstream

An immediate paid-plan upgrade may require:

```text
credit for unused prior plan value
incremental charge
provider proration
new renewal amount
```

The exact financial/proration formula is a billing implementation/commercial-policy concern and is not required to close DDR-OD-004.

However the billing mechanism shall not silently delay the entitlement timing defined by this document.

---

# 31. Downgrade is effective at the current paid period end

A merchant may request a downgrade at any time, but the lower plan shall normally become effective only when the merchant's current paid entitlement period expires.

Conceptually:

```text
Current plan: Growth
Current period ends: T2
Downgrade requested: T1

T1 ≤ time < T2
    → Growth remains effective

at T2
    → Business becomes effective
```

Hard rule:

> **A scheduled downgrade shall not remove entitlement that the merchant has already purchased for the current paid period.**

---

# 32. Downgrade request is not current-plan mutation

Conceptually:

```text
ScheduledPlanChange
{
    requestedAt
    currentPlanRevision
    targetPlanRevision
    effectiveAt = currentPeriodEnd
}
```

Until the effective boundary:

```text
current effective plan
    = existing paid plan
```

The scheduled downgrade is future commercial intent, not current semantic/configuration state.

---

# 33. Cancellation follows current-period completion

Unless a separately accepted exceptional refund/termination policy applies, cancellation of a paid subscription shall preserve the purchased plan entitlements through the current paid period and then fall back to the applicable standing plan, normally Free.

Canonical pattern:

```text
Paid plan
    ↓ cancellation requested
Paid entitlements remain through current period
    ↓ period end
FREE standing-plan entitlement applies
```

Cancellation shall not delete merchant business data or existing commitments.

---

# 34. Entitlement loss does not deactivate or delete configuration

When a plan downgrade, cancellation, trial expiry or other commercial lifecycle removes an entitlement:

```text
commercial entitlement absent
        ↓
protected new activity unavailable
```

It shall not automatically mean:

```text
delete capability configuration
delete capability data
rewrite capability as never active
```

Configuration remains subject to its normal semantic lifecycle and MS-PROT-054 compatibility requirements.

Hard rule:

> **Commercial locking shall preserve the merchant's valid configured operating model so that future entitlement restoration does not require unnecessary reconstruction.**

---

# 35. Existing commitments survive entitlement loss

A merchant may establish valid business commitments while an entitlement is effective.

Later entitlement loss shall not silently cancel, invalidate, reinterpret or abandon them.

Example:

```text
7 future bookings established during full experience
        ↓
experience expires
        ↓
selected plan does not include new Booking activity
```

Required result:

```text
new bookings
    commercially unavailable

existing 7 bookings
    remain valid

merchant
    retains minimum management access

customers
    retain required interaction with their commitments
```

Hard invariant:

> **Commercial entitlement loss shall not prevent discharge of obligations already validly established.**

---

# 36. Residual management is not a premium privilege

Main Street shall preserve the minimum operational access necessary to honour existing commitments even when the entitlement that permitted new activity is no longer present.

Rejected:

```text
merchant has existing customer obligation
        ↓
"Upgrade to see/manage the customer commitment"
```

The entitlement model may restrict new premium activity and non-essential premium services, but it shall not hold already-created obligations hostage to subscription renewal.

This extends the residual-management rules of MS-PROT-040 and MS-PROT-049.

---

# 37. Commercial lock is a presentation, not a universal state

Merchant-facing UI may use wording such as:

```text
Locked — upgrade to use Inventory
```

but the authoritative explanation is the unsatisfied commercial entitlement requirement.

Main Street must distinguish at least conceptually:

```text
NOT_ENTITLED
NOT_AUTHORISED
TRUST_REQUIREMENT_UNSATISFIED
PROVIDER_UNAVAILABLE
CONFIGURATION_INCOMPLETE
OPERATIONALLY_UNAVAILABLE
```

Rejected:

```text
disabled = true
```

as a universal reason model.

---

# 38. Entitlement restoration can restore commercial availability without semantic recompilation

Suppose:

```text
Booking configured and semantically valid
Booking entitlement absent
```

When the merchant upgrades and the relevant entitlement becomes effective:

```text
commercial restriction disappears
```

without requiring semantic recompilation merely because billing changed, provided the existing configuration remains compatible/current.

If semantic/schema/configuration compatibility has changed materially while the capability was commercially unavailable, MS-PROT-054 governs any required migration before safe use resumes.

---

# 39. Actor authority remains independent

Commercial entitlement never grants actor authority.

Example:

```text
merchant plan grants Staff Management
```

shall not imply:

```text
all staff may manage staff
```

Correct:

```text
commercial entitlement
        +
actor privilege/authority
        ↓
operation eligibility
```

Hard rule:

```text
ENTITLED
    ≠
AUTHORISED
```

---

# 40. Trust remains independent

A plan that commercially includes an operation requiring trust/verification does not satisfy the trust requirement.

Example:

```text
Payment execution commercially entitled
        ≠
payment-provider KYC/KYB satisfied
```

A merchant cannot purchase entitlement as a substitute for security, legal or trust requirements.

---

# 41. Provider fulfilment remains independent

Commercial entitlement does not guarantee provider availability.

Example:

```text
external-calendar integration commercially entitled
+
provider connection unhealthy
```

means:

```text
entitlement remains valid
provider fulfilment is degraded
```

The provider outage shall not be misrepresented as plan loss.

Likewise restoring provider health does not create commercial entitlement if none exists.

---

# 42. Billing provider evidence is not entitlement authority

A billing provider may report states such as:

```text
PAID
FAILED
PAST_DUE
CANCELLED
```

Those are provider-side evidence/state.

Correct boundary:

```text
billing-provider evidence
        ↓
Main Street subscription reconciliation
        ↓
Main Street subscription lifecycle
        ↓
registered commercial access policy
        ↓
EffectiveEntitlementSet
```

Rejected:

```text
provider says PAST_DUE
        ↓ direct
Booking disabled
```

A Main Street grace-period or recovery policy may legitimately preserve entitlement despite temporary provider payment failure.

The exact grace-period/retry policy remains downstream.

---

# 43. Deployment feature flags are not commercial entitlements

Deployment feature flags may legitimately exist for concerns such as:

```text
staged rollout
emergency kill switch
technical experiment
operational safety
```

But:

```text
DeploymentFeatureFlag
        ≠
CommercialEntitlement
        ≠
CapabilityActivation
```

A flag must not simultaneously claim to mean:

```text
merchant paid for capability
merchant configured capability
actor authorised
trust satisfied
provider healthy
```

This document supersedes any legacy mechanism that treats one feature flag as the authority for subscription-tier capability semantics.

---

# 44. Onboarding remains semantic-first during and after experience

Main Street's adaptive onboarding shall continue to discover merchant intent independent of current commercial plan.

If a merchant on Free requests a capability not commercially entitled after the experience period:

```text
business need discovered
        ↓
supported semantic/configuration path identified
        ↓
merchant configuration may be prepared/preserved
        ↓
commercial entitlement requirement explained
```

Main Street shall not distort the business graph merely to fit the current plan.

---

# 45. Surface composition consumes commercial entitlement context

MS-PROT-049 remains authoritative for surface contributions.

Commercial entitlement becomes one contextual eligibility input.

Example:

```text
Booking configured
+
Booking entitlement absent
+
no residual commitments
        ↓
public create-booking interaction ineligible
```

But:

```text
Booking entitlement absent
+
existing Booking commitments
        ↓
minimum residual merchant/customer management surface remains eligible
```

The dashboard may also expose a non-authoritative upgrade affordance where useful.

A pricing/upgrade card does not create capability authority.

---

# 46. Merchant-specific plan comparison

Because Main Street preserves the merchant's configured operating graph, subscription presentation may compare plans against what the merchant actually uses or intends to use.

Conceptually:

```text
configured/relevant operating graph
        +
Plan A grants
Plan B grants
Plan C grants
        ↓
merchant-specific plan impact comparison
```

This is presentation derived from authoritative configuration and commercial-plan facts.

It does not alter either authority.

---

# 47. Add-ons can compose entitlement grants

Future commercial add-ons may contribute entitlement grants without creating new capability semantics merely because they are sold separately.

Conceptually:

```text
Base Plan Grants
+
Add-on Grants
        ↓
EffectiveEntitlementSet
```

Example:

```text
Business
+
Advanced Analytics add-on
```

need not create:

```text
BusinessWithAnalyticsCapabilityGraph
```

The underlying semantic/platform service remains independently defined.

---

# 48. Usage quotas and metering remain future scope

Main Street may later introduce:

```text
API usage allowances
message volumes
location allowances
campaign limits
other quantitative commercial limits
```

This document does not define a generic quota/metering engine.

However future quota semantics shall preserve this invariant:

> **Reducing or exhausting a commercial allowance shall not silently delete authoritative business data or invalidate already-established commitments merely to satisfy the new allowance.**

---

# 49. Main Street subscription pricing may reuse MonetaryAmount

Subscription prices are commercial propositions between Main Street and the merchant.

They may reuse MS-PROT-055 `MonetaryAmount` semantics.

Example:

```text
Business monthly subscription
    → Main Street-owned commercial amount
```

This is distinct from:

```text
Merchant Offering.price
Customer PaymentObligation to merchant
```

The shared Money primitive does not merge commercial ownership.

---

# 50. Plan changes are not semantic migrations

Changing commercial packaging does not itself change capability meaning.

Therefore:

```text
Plan Revision change
        ≠
Semantic Registry migration
        ≠
Merchant Configuration migration
```

Example:

If Main Street changes a future plan so that an existing capability moves from Growth to Business, the capability semantics have not changed merely because commercial packaging changed.

Conversely, a material semantic change to Booking cannot be hidden as a pricing-plan edit.

MS-PROT-054 remains authoritative for semantic/configuration compatibility and migration.

---

# 51. Commercial terms and historical subscription periods

A merchant's accepted subscription period must remain interpretable under the commercial terms accepted for that period.

Changing future:

```text
plan price
annual discount
included entitlement set
renewal policy
```

shall not silently rewrite already-completed historical subscription periods.

Application of changed commercial terms to future renewal periods requires the appropriate product/legal/notification policy and is outside the semantic closure required here.

---

# 52. Data protection and retention

Subscription, entitlement and notification data remain subject to MS-PROT-053.

Commercial lifecycle does not justify indefinite retention of unrelated personal data.

Likewise downgrade/cancellation does not itself authorise deletion of merchant/customer business evidence whose retention is governed by another accepted purpose.

---

# 53. Falsification — scholarship-information publisher

Merchant requires:

```text
Merchant Profile
Publication
Opportunity
Enquiry
```

During the initial experience, those relevant capabilities/services are commercially available according to the full-experience grant.

The merchant is not shown Booking, Inventory or motel allocation merely because those exist elsewhere in Main Street.

After expiry, Free/paid entitlement determines which configured functionality remains available.

**PASS**

---

# 54. Falsification — consultant on Free needing Booking

Merchant expresses:

```text
customers should book consultations
```

Main Street discovers/configures Scheduling/Booking independent of the standing Free plan.

During the full-experience period, commercial entitlement does not block relevant supported use.

After expiry, if the standing plan does not grant new Booking activity:

```text
Booking configuration preserved
new Booking commercially unavailable
```

The system does not pretend the consultant never needed Booking.

**PASS**

---

# 55. Falsification — salon subscribes on day 20

Salon has 10 days of full experience remaining and chooses a paid monthly plan.

Required result:

```text
subscription accepted immediately
remaining experience preserved
paid-plan grants also effective immediately
first normal renewal boundary occurs
after original experience expiry
+ one full monthly interval
```

Early subscription does not reduce access.

**PASS**

---

# 56. Falsification — annual subscriber during experience

Merchant subscribes annually before experience expiry.

Required result:

```text
annual subscription accepted immediately
5% annual commercial discount applies
remaining experience preserved
one full annual billing interval follows
original experience expiry
```

Same plan entitlements apply as the monthly version of that plan.

**PASS**

---

# 57. Falsification — paid upgrade mid-period

Merchant on Business upgrades to Growth halfway through a paid period.

Required result:

```text
Growth commercial entitlements
    effective immediately
```

The billing implementation may calculate an incremental/prorated charge without delaying entitlement until renewal.

**PASS**

---

# 58. Falsification — downgrade requested mid-period

Merchant on Growth requests Business halfway through an already-paid period.

Required result:

```text
Growth entitlements remain
through current paid period

Business begins at period end
```

The merchant is not stripped of already-purchased access.

**PASS**

---

# 59. Falsification — downgrade with future bookings

Merchant's target plan does not grant new Booking activity, but future bookings exist.

At downgrade effective time:

```text
new Booking commercially unavailable
existing bookings remain valid
minimum residual management preserved
```

The merchant is not forced to upgrade merely to honour already-created obligations.

**PASS**

---

# 60. Falsification — payment provider/trust requirement

Merchant plan grants payment execution.

Applicable provider identity/KYC requirement is unsatisfied.

Result:

```text
commercial entitlement = satisfied
trust/provider requirement = unsatisfied
payment execution = unavailable
```

The merchant cannot buy their way through trust requirements.

**PASS**

---

# 61. Falsification — external calendar outage

Merchant is commercially entitled to external-calendar integration and has Scheduling configured.

Provider connection fails.

Result:

```text
commercial entitlement remains
Scheduling semantics remain
provider-dependent fulfilment degrades
attention/repair surface may appear
```

No false plan downgrade occurs.

**PASS**

---

# 62. Falsification — merchant has no interest in Booking

Merchant purchases a plan whose catalogue grants Booking entitlement but has never selected/configured Booking.

Result:

```text
Booking entitlement available
Booking semantic activation absent
no Booking workflow appears merely due to payment
```

**PASS**

---

# 63. Falsification — hybrid merchant

Salon sells products and services.

Relevant configured graph may include:

```text
Publication
Enquiry
Scheduling
Appointment
Booking
Order
Inventory
Payment
```

The initial experience may commercially permit all relevant supported configured capabilities without creating a `HybridTrial` or category-specific plan semantic.

After expiry, entitlement filters commercial use while preserving one coherent merchant graph.

**PASS**

---

# 64. Rejected models

The following are rejected:

1. Plan identity directly activating capabilities.
2. Subscription tier becoming capability configuration.
3. Business/Growth/Enterprise names appearing as semantic runtime branches.
4. Every item on a pricing page being remodelled as a Capability.
5. Capability configuration being deleted merely because entitlement expires.
6. Full experience activating every Main Street capability for every merchant.
7. Current plan suppressing discovery of legitimate merchant business needs.
8. Trial entitlement bypassing trust, provider, actor, compiler or security requirements.
9. A static day-1 trial capability list that cannot include newly relevant supported capability during the 30 days.
10. One generic `featureEnabled` Boolean owning semantic, commercial, authority and provider truth.
11. Billing-provider status directly toggling business capabilities.
12. Entitlement granting actor authority.
13. Entitlement satisfying trust requirements.
14. Entitlement guaranteeing provider health.
15. Deployment feature flags acting as subscription authority.
16. Annual billing having different plan entitlements from monthly billing for the same plan revision.
17. Early first subscription forfeiting remaining experience time.
18. Paid upgrade waiting until renewal to grant newly purchased higher-plan entitlement.
19. Downgrade immediately removing already-paid higher-plan entitlement.
20. Cancellation automatically deleting business data.
21. Entitlement loss cancelling or hiding existing commitments that still require management.
22. Requiring reconfiguration after every commercial lock/unlock where the existing configuration remains valid.
23. Plan packaging change being treated as semantic registry migration.
24. Semantic change being disguised as commercial plan editing.
25. Hard-wiring commercial plan names or the 5% discount into capability semantics.

---

# 65. Accepted invariants

1. Plan, entitlement, semantic capability, configuration, actor authority, trust, provider health and effective operation availability are distinct authorities.
2. A plan is a versioned commercial catalogue, not a semantic architecture.
3. Plan identity shall not activate capabilities.
4. Entitlement references already-defined semantics/platform services and cannot invent them.
5. Entitlement is permissive, not authoritative semantic activation.
6. Capability/configuration state and entitlement state are independent.
7. Effective entitlements are contextual commercial truth rather than permanently compiled merchant semantics.
8. Not every priced benefit is a Capability.
9. The permanent Free plan remains distinct from the temporary full-experience period.
10. New merchants receive an initial 30-day full-experience commercial grant for merchant-relevant supported capabilities/services.
11. Full experience does not mean every Main Street capability.
12. Subscription tier shall not narrow semantic discovery/onboarding of legitimate business needs.
13. Relevant valid capability configuration is preserved independently of current plan entitlement.
14. Full-experience entitlement does not bypass compiler, merchant approval, actor, trust, provider, security or operational requirements.
15. Newly relevant supported capabilities configured during the 30-day period can receive the remaining full-experience entitlement.
16. Daily subscription reminders begin on day 15 and continue through applicable experience expiry, with no more than one consolidated reminder per merchant per day under the initial policy.
17. Reminder content should, where practical, explain merchant-specific post-trial impact.
18. Paid plans support MONTHLY and ANNUAL billing cadence.
19. Billing cadence does not change plan entitlements.
20. The initial annual price receives a 5% discount relative to twelve monthly billing periods for the same plan revision.
21. The 5% value is a versioned commercial policy/term rather than capability semantics.
22. Early first subscription becomes commercially accepted/effective immediately.
23. Early first subscription never shortens the original full-experience period.
24. Remaining full-experience duration is preserved in addition to one complete purchased billing interval before the first ordinary renewal boundary.
25. Paid-plan upgrade is effective immediately.
26. Exact upgrade proration arithmetic remains billing policy/implementation.
27. Paid-plan downgrade is scheduled for current paid-period expiry.
28. Cancellation normally preserves purchased entitlement through current period before fallback to the applicable standing plan.
29. Commercial entitlement loss restricts protected new activity without automatically deleting configuration/data.
30. Existing valid commitments survive entitlement loss.
31. Minimum residual management required to discharge existing commitments is not a premium entitlement that may be withheld.
32. Commercial `LOCKED` is presentation; the authoritative cause is an entitlement decision.
33. Entitlement restoration may restore commercial access without semantic recompilation when configuration remains valid.
34. Entitlement never grants actor authority.
35. Entitlement never satisfies trust requirements.
36. Entitlement never guarantees provider health.
37. Billing-provider evidence must be reconciled into Main Street subscription state before commercial consequences.
38. Deployment feature flags remain separate from commercial entitlement and semantic activation.
39. Surface composition may consume entitlement as a contextual eligibility input while preserving residual obligations.
40. Add-ons may compose entitlement grants without creating new capability semantics.
41. Quantitative quotas/metering remain future scope and may not later justify destructive deletion of authoritative data/commitments.
42. Subscription pricing may reuse MS-PROT-055 Money without merging merchant/customer commercial authority.
43. Commercial plan packaging changes are not semantic migrations.
44. Historical subscription periods remain interpretable under their accepted commercial terms.
45. MS-PROT-053 remains authoritative for subscription/notification data protection and retention.

---

# 66. Deferred decisions

The following remain deliberately downstream:

```text
exact Java/domain types for Plan/Subscription/Entitlement
exact plan catalogue prices
exact Free/Business/Growth/Enterprise entitlement matrix
exact monthly calendar-period representation
subscription invoice persistence
billing provider selection/adapter
payment retry cadence
grace-period duration
failed-payment suspension policy
subscription tax/VAT handling
invoice numbering/content
upgrade proration formula
refund policy for Main Street subscription fees
renewal-notice timing beyond accepted experience reminders
legal subscription notice/consent wording
plan-change notice rules
promotion/coupon semantics
trial eligibility/repeat-trial abuse policy
add-on catalogue
usage quota/metering framework
enterprise negotiated-contract semantics
exact reminder channel/copy/template
self-service billing UI
support/admin override workflow
```

The following remain owned by existing OPEN_DESIGN clusters:

```text
DDR-OD-005
    Business Hours overnight interval versus dated override collision

DDR-OD-006
    minimum Booking/Appointment lifecycle and capability payment-policy consequences

DDR-OD-007
    minimum Enquiry/Conversation/CustomerContext lifecycle and reconciliation

DDR-OD-008
    reusable exposure-policy vocabulary/rule boundary

DDR-OD-009
    Product/Offering/Inventory/variant boundary
```

These downstream details do not reopen DDR-OD-004 unless later evidence falsifies an MS-PROT-056 invariant.

---

# 67. Relationship to legacy Subscription & Billing product evidence

Legacy product evidence correctly expresses outcomes such as:

```text
permanent Free tier
paid upgrades/downgrades
business continuity
monthly/annual commercial options
future add-ons
```

but any legacy statement that treats generic feature flags as direct capability authority is superseded by this accepted boundary.

The accepted interpretation is:

```text
product feature catalogue
        ↓
versioned commercial entitlement grants
        ↓
contextual commercial availability
```

not:

```text
subscription plan
        ↓
generic feature flag
        ↓
semantic capability exists/does not exist
```

---

# 68. Continuous-improvement checkpoint

The initial candidate design considered a simple model:

```text
Plan
    ↓
Feature flags
    ↓
Capabilities on/off
```

This was rejected because it couples:

```text
pricing
semantic activation
configuration
billing provider state
actor authority
trust
provider fulfilment
surface composition
```

The stronger architecture is:

```text
Merchant business needs
        ↓
merchant-relevant configured semantic graph
        │
        ├──────────────────────────────┐
        │                              │
        ▼                              ▼
Full-experience grants           Plan/add-on grants
        │                              │
        └──────────────┬───────────────┘
                       ▼
             EffectiveEntitlementSet
                       │
                       ▼
             commercial availability
                       │
                       ▼
      effective operation/surface eligibility
                       ▲
                       │
          actor / trust / provider /
          runtime business constraints
```

Four material improvements are preserved:

1. Main Street models the merchant's real supported operating needs once, rather than designing a crippled semantic graph around the merchant's current plan.
2. The initial 30-day experience grants commercial access to the merchant-relevant graph without exposing irrelevant platform breadth or bypassing safety/trust/provider requirements.
3. Commercial entitlement loss primarily restricts new activity while preserving configuration, data and residual obligations.
4. Early subscription is commercially favourable: the merchant gains/locks in the paid plan immediately while keeping every remaining experience day plus a complete purchased period before first ordinary renewal.

No additional P0 design cluster was discovered during this resolution.

---

# 69. Governance review

## PROPOSE

Separate subscription plans and commercial entitlements from capability semantics while adding a merchant-relevant 30-day full-experience entitlement, monthly/annual billing and asymmetric upgrade/downgrade timing.

**PASS**

## REVIEW / FALSIFICATION

The model was challenged against:

```text
information publisher
consultant on Free needing Booking
salon subscribing during trial
annual early subscriber
paid mid-period upgrade
scheduled downgrade
future bookings after downgrade
payment trust/provider requirement
external calendar outage
merchant entitled to but not using Booking
hybrid product/service merchant
```

No valid case requires plan-specific semantic capability classes, feature-flag-owned business meaning, destructive downgrade behaviour or subscription-tier-driven onboarding.

**PASS**

## VALIDATE

The model preserves:

```text
semantic/configuration authority
business-needs-first onboarding
merchant continuity
provider neutrality
actor/trust separation
residual commitment protection
commercial flexibility
pricing evolution without semantic migration
```

**PASS**

## MANUAL APPROVAL

The material authority boundary and subsequent product refinements were presented for manual approval and approved on **22 August 2026**.

Approved refinements include:

```text
30-day full merchant-relevant experience
subscription reminders beginning day 15
monthly and annual paid billing
5% annual discount
immediate upgrades
period-end downgrades
period-end cancellation fallback
remaining trial time preserved for early subscribers
one full purchased period added after original trial expiry
configured-but-commercially-locked capabilities preserved
```

**PASS**

## ACCEPT

**MS-PROT-056 v1.0 is ACCEPTED.**

It closes **DDR-OD-004 — Plan/tier entitlement versus capability semantics**.

---

# 70. Canonical decision

> **Main Street shall model a merchant's supported operating needs independently of subscription tier. Relevant capabilities may be discovered, configured and preserved according to accepted semantic/configuration rules even when the standing plan does not commercially permit their new use. Subscription plans are versioned commercial catalogues whose entitlement grants constrain current commercial availability; they do not create capabilities, activate/configure semantics, grant actor authority, satisfy trust, guarantee provider health or substitute for deployment controls. Every new merchant receives an initial 30-day full-experience entitlement covering merchant-relevant supported configured capabilities/services without bypassing ordinary semantic, trust, provider or security requirements. Daily subscription reminders begin on day 15 and should explain merchant-specific post-trial impact. Paid plans support monthly and annual cadence with identical plan entitlements; the initial annual commercial policy discounts annual billing by 5% relative to twelve monthly periods. A first subscription accepted before trial expiry is effective immediately but never consumes remaining experience time: the original experience period remains and one complete purchased billing interval follows it before first ordinary renewal. Paid upgrades take effect immediately; downgrades and ordinary cancellations take effect at the current paid-period boundary. Loss of commercial entitlement restricts applicable new activity without deleting valid configuration/data or abandoning existing commitments; minimum residual management remains available until those obligations are discharged. Commercial packaging, semantic configuration, actor authority, trust, provider fulfilment and deployment flags remain separate authorities.**