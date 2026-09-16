# MS-PROT-087 v1.4 — Merchant Marketing Commercial Access Classification Amendment

**Document ID:** MS-PROT-087  
**Version:** 1.4  
**Status:** ACCEPTED  
**Approved:** 16 September 2026 — explicit manual approval of the complete proposal after Fundamental Vision Conformance, review, falsification, recommendation and complete pre-approval presentation  
**Authority type:** Marketing commercial-access classification amendment  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-087 through v1.3 within commercial-access classification only  
**Depends on:** Composite MS-PROT-087 through v1.3; composite MS-PROT-056 through v1.9; applicable composite MS-PROT-027, MS-PROT-043, MS-PROT-046, MS-PROT-048, MS-PROT-053, MS-PROT-057, MS-PROT-062, MS-PROT-063, MS-PROT-064, MS-PROT-065, MS-PROT-069, MS-PROT-070, MS-PROT-072, MS-PROT-073, MS-PROT-075, MS-PROT-082, MS-PROT-083, MS-PROT-085 and MS-PROT-086 authorities  
**Preserves:** Marketing ownership; Campaign/Audience semantics; Publication ownership; Notification ownership; CustomerContext ownership; direct-Marketing permission/contact-policy authority; Actor Authorisation; Provider Readiness; source-business ownership; analytical ownership; AI non-authority; external-effect uncertainty; data lifecycle and accepted Campaign currentness rules  
**Partially resolves:** `MS-PROT-056-V17-DQ-001` only by supplying the bounded Marketing owner/supporting-service commercial classification  
**Does not resolve:** `MS-PROT-087-DQ-006`; complete Commercial Catalogue Manifest; final Commercial Entitlement identity; pricing or quantitative allowances  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY

---

## 0. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

GrandRue SHALL present Marketing commercially as one understandable Growth service rather than requiring merchants to administer internal Campaign, Audience, scheduling, Notification or entitlement decomposition.

Canonical merchant-facing meaning is:

```text
GROWTH
    → use the governed Marketing Campaign service
```

The internal execution may require:

```text
Campaign authority
+
Audience semantics
+
current marketing/contact permission
+
Actor Authorisation
+
current Commercial permission
+
Publication or Notification authority
+
Provider Readiness
+
Resource Protection
+
current source facts
```

but those predicates SHALL remain GrandRue's internal coordination responsibility.

Commercial classification SHALL NOT weaken any independent Marketing safety or authority requirement.

---

## 1. Governing Decision

Composite MS-PROT-087 SHALL define exactly one initial protected Commercial purpose for the accepted Marketing Campaign service:

```text
CONDUCT_MARKETING_CAMPAIGNS
```

The exact protected access contract SHALL be:

| Exact access contract | Protected Commercial purpose | Standard allocation | Target family |
|---|---|---|---|
| `marketing/campaign-service-access@1` | `CONDUCT_MARKETING_CAMPAIGNS` | GROWTH | `PLATFORM_SERVICE_ACCESS` |

FREE SHALL NOT receive this protected purpose through the standard catalogue.

BUSINESS SHALL NOT receive this protected purpose through the standard catalogue.

GROWTH SHALL receive the protected purpose only through the future exact approved catalogue binding and grant set required by composite MS-PROT-056.

The following exact bounded Marketing access contracts SHALL require **NO INDEPENDENT COMMERCIAL ENTITLEMENT**:

```text
marketing/non-committing-preparation-access@1

marketing/existing-marketing-state-observation-access@1

marketing/campaign-restriction-access@1
```

No `CommercialEntitlementIdentity` is minted by this amendment.

Missing classification or missing binding is not permission.

---

## 2. Protected Scope Is Closed

`marketing/campaign-service-access@1` applies only to materially new use of the Marketing Campaign service already accepted by composite MS-PROT-087 through v1.3.

Its protected scope includes, where the applicable accepted semantic authority permits the operation:

```text
establishing a new MarketingCampaign

committing a new materially applicable CampaignRevision

establishing or materially changing
Campaign-owned audience selection/configuration

performing Campaign-owned Audience evaluation
for new Campaign use

establishing Campaign approval
for future protected use

activating or reactivating a Campaign

establishing or materially changing
Campaign scheduling / recurrence

establishing an accepted Automated Campaign Contract

accepting a new CampaignOccurrence
where that occurrence represents new protected Marketing use

authorising Campaign-owned public-outreach coordination

authorising Campaign-owned direct-outreach coordination

performing the current Marketing externalisation decision
required before a new Campaign effect proceeds
```

The exact semantic operation and its preconditions remain defined by composite MS-PROT-087.

This commercial amendment does not manufacture an operation absent from that authority.

---

## 3. Semantic Campaign Purposes Are Not Commercial Purposes

The following accepted Marketing identities remain Campaign semantic purposes:

```text
marketing/merchant-news-awareness@1
marketing/offering-awareness@1
marketing/customer-appreciation@1
marketing/customer-reengagement@1
```

They SHALL NOT become separate Commercial Entitlement identities merely because they are Campaign purposes.

Therefore:

```text
Campaign semantic purpose
    ≠
CommercialEntitlementPurpose
```

`CONDUCT_MARKETING_CAMPAIGNS` is the Commercial purpose protecting use of the bounded Campaign service.

It does not replace or redefine those Marketing semantic purposes.

---

## 4. Audience Portfolio Boundary

The accepted Marketing Audience portfolio belongs within the protected Marketing Campaign service.

Use of an accepted Audience Definition for materially new Campaign activity therefore remains subject to:

```text
CONDUCT_MARKETING_CAMPAIGNS
```

where `marketing/campaign-service-access@1` applies.

This does not mean Commercial owns Audience meaning.

Marketing retains ownership of:

```text
Audience Definition semantics
candidate derivation
Campaign-specific Audience evaluation
coverage qualification
Campaign-specific recipient eligibility
```

Source capabilities retain their source facts.

CustomerContext retains customer-relationship authority.

Commercial permission SHALL NOT turn:

```text
Audience match
```

into:

```text
permission to contact
```

or:

```text
authority to externalise
```

---

## 5. Non-Committing Preparation

`marketing/non-committing-preparation-access@1` SHALL require **NO INDEPENDENT COMMERCIAL ENTITLEMENT**.

Its scope is limited to preparation that does not yet establish an authoritative protected Marketing effect.

It MAY include, where independently authorised:

```text
human preparation of Campaign copy

editing an uncommitted Campaign draft

previewing candidate Campaign content

reviewing proposed purpose or outreach presentation

preparing material for a later
CampaignRevision commitment
```

Preparation SHALL NOT itself:

```text
commit a MarketingCampaign

commit a materially new CampaignRevision

establish authoritative Campaign approval

establish an Audience evaluation result

establish or change a Campaign schedule

establish an Automated Campaign Contract

create a CampaignOccurrence

authorise direct outreach

authorise public Campaign externalisation

create Publication truth

create Notification truth
```

Current `CONDUCT_MARKETING_CAMPAIGNS` permission SHALL be revalidated before the protected Marketing boundary is crossed.

AI-generated preparation remains subject to the applicable AI authority and any independently applicable commercial classification.

This no-entitlement contract does not itself grant AI inference.

---

## 6. Existing Marketing State Observation

`marketing/existing-marketing-state-observation-access@1` SHALL require **NO INDEPENDENT COMMERCIAL ENTITLEMENT**.

It governs otherwise-authorised bounded observation of retained existing Marketing state.

It MAY include inspection of retained:

```text
MarketingCampaign

CampaignRevision

Campaign approval evidence

Audience Definition/configuration affinity

CampaignOccurrence

Campaign-owned recipient/result evidence

Campaign Outcome Observation

Campaign schedule/history

Campaign cancellation/pause evidence
```

where that information remains retained and independently accessible.

Observation SHALL remain subject to all applicable:

```text
Merchant Scope
Actor Authorisation
data-protection authority
retention
redaction
Projection / Exposure
Resource Protection
source-access restrictions
lifecycle restrictions
```

No-entitlement observation SHALL NOT permit:

```text
new Campaign activation

new CampaignRevision commitment

new Audience evaluation for future use

new scheduled occurrence

new direct outreach

new public Campaign externalisation

new automated Campaign execution
```

Retained history therefore does not become continuing Growth service permission.

---

## 7. Campaign Restriction and Cancellation

`marketing/campaign-restriction-access@1` SHALL require **NO INDEPENDENT COMMERCIAL ENTITLEMENT** where the exact operation only reduces or terminates future Marketing externalisation.

Within already-accepted MS-PROT-087 semantics, this MAY cover applicable:

```text
Campaign pause

Campaign cancellation

schedule cancellation

termination of future automated Campaign execution
```

The exemption SHALL NOT cover:

```text
Campaign activation

Campaign resume

schedule creation

schedule expansion

new recurrence establishment

new CampaignOccurrence

new outreach

material CampaignRevision authoring

broader Audience selection
```

Every applicable Actor Authorisation, Merchant Scope, concurrency, lifecycle, idempotency and safety requirement remains mandatory.

A merchant SHALL NOT be required to regain GROWTH permission merely to stop future Marketing activity that they are otherwise authorised to restrict.

---

## 8. No Generic Committed-Progression Exemption

This amendment deliberately introduces no generic:

```text
marketing/committed-campaign-progression-access@1
```

exemption.

Composite MS-PROT-087 requires mutable eligibility conditions, including current entitlement where applicable, to be revalidated before externalisation.

Therefore:

```text
Campaign approved earlier
    ≠
future externalisation permanently authorised
```

and:

```text
Campaign scheduled earlier
    ≠
future externalisation permanently authorised
```

and:

```text
CampaignOccurrence established earlier
    ≠
all later Marketing-owned externalisation checks waived
```

Where an independently owned downstream fact has already validly committed after its required Marketing checks, subsequent progression/recovery belongs to that downstream authority.

For example:

```text
validly accepted NotificationIntent
    → Notification-owned progression

validly accepted Publication fact
    → Publication-owned progression
```

Commercial change SHALL NOT retrospectively erase those facts.

---

## 9. Standard Commercial Allocation

`CONDUCT_MARKETING_CAMPAIGNS` belongs to:

```text
GROWTH
```

under the standard catalogue allocation.

The standard allocation is therefore:

```text
FREE
    → no standard grant

BUSINESS
    → no standard grant

GROWTH
    → grant required
       through exact approved
       Commercial Access Binding
```

This amendment does not publish that binding.

The future concrete binding SHALL preserve at minimum:

```text
owner
    = Marketing

exact target
    = marketing/campaign-service-access@1

protected purpose
    = CONDUCT_MARKETING_CAMPAIGNS

target family
    = PLATFORM_SERVICE_ACCESS

governing target revision
    = this accepted classification
      composed with the exact accepted
      Marketing semantic authority
```

The final:

```text
CommercialEntitlementIdentity
BindingId
BindingVersion
BindingKey
plan revision identities
explicit grant sets
catalogue revision
publication provenance
```

remain under `MS-PROT-056-V17-DQ-001`.

No runtime permission may be inferred from:

```text
if plan == GROWTH

tier >= GROWTH

feature == MARKETING

hasCampaign = true

merchant has email provider
```

or another plan/feature shortcut.

---

## 10. Current Commercial Revalidation

Current Commercial permission SHALL be evaluated at the authoritative protected Marketing boundary.

An earlier successful permission result SHALL NOT reserve future permission.

In particular:

```text
Campaign prepared while GROWTH
    ≠
later protected commitment automatically allowed
```

```text
Campaign approved while GROWTH
    ≠
later occurrence automatically allowed
```

```text
Campaign scheduled while GROWTH
    ≠
later externalisation automatically allowed
```

Where composite MS-PROT-087 requires current entitlement immediately before externalisation, the current exact Commercial permission SHALL be revalidated there.

---

## 11. Downgrade Boundary

Commercial downgrade SHALL NOT:

```text
delete MarketingCampaign history

delete CampaignRevision history

rewrite Campaign approval

erase CampaignOccurrence evidence

erase Campaign Outcome Observation

erase Notification delivery evidence

erase Publication truth

rewrite marketing-contact-policy evidence

rewrite CustomerContext

rewrite source-business facts
```

Commercial downgrade MAY prevent:

```text
new Campaign commitment

new material CampaignRevision

new Campaign activation

new Campaign schedule/recurrence

new protected Audience evaluation

new CampaignOccurrence

new Marketing externalisation
```

where those operations require `marketing/campaign-service-access@1`.

Bounded observation and restriction remain governed by Sections 6 and 7.

---

## 12. Scheduled and Automated Campaign Boundary

Accepted automated Campaign execution does not create a historical commercial reservation.

For a Campaign occurrence becoming due after commercial permission has changed:

```text
scheduled/automated trigger valid
+
Campaign semantic prerequisites valid
+
current marketing/contact prerequisites valid
+
current Actor Authorisation valid
+
current Provider Readiness valid
+
current Commercial permission absent
        ↓
protected Marketing externalisation
does not proceed
```

The result SHALL preserve the truthful failure/non-execution condition required by the existing Marketing authority.

It SHALL NOT fabricate:

```text
provider delivery

Campaign success

recipient ineligibility

marketing opt-out

Campaign cancellation
```

merely because the Commercial predicate failed.

---

## 13. Publication Boundary

A standalone ordinary Publication or Announcement does not become GROWTH Marketing merely because Marketing can also coordinate public outreach.

Canonical:

```text
merchant creates ordinary website Announcement
        ↓
Publication authority
```

not:

```text
merchant creates ordinary website Announcement
        ↓
Marketing Campaign required
        ↓
GROWTH required
```

Where an accepted Marketing Campaign actually coordinates `WEBSITE_ANNOUNCEMENT_V1`, the Marketing Campaign service requires its own current commercial permission.

Publication still retains ownership of the resulting publication operation and state.

Therefore:

```text
Marketing commercial permission
    ≠ Publication authority
```

and:

```text
FREE Publication permission
    ≠ free Marketing Campaign permission
```

---

## 14. Notification and Direct Outreach Boundary

For `DIRECT_EMAIL_MARKETING_V1`, Marketing retains the commercial boundary for use of the Marketing Campaign service.

Notification retains:

```text
Notification Intent
channel/endpoint resolution
dispatch
Delivery Attempt
Delivery Evidence
provider transport outcome
```

The fact that Notification is necessary to perform direct Marketing SHALL NOT create a second Marketing-specific premium toll merely because it is supporting infrastructure.

However:

```text
Marketing commercial permission
```

does not itself establish:

```text
Notification semantic applicability

active Notification endpoint

Provider Readiness

Resource Protection Admission

marketing/contact permission

recipient identity

current Audience eligibility
```

Every independently applicable requirement remains mandatory.

---

## 15. Marketing Permission and Contact Policy Boundary

Commercial permission is only one predicate.

Canonical:

```text
CONDUCT_MARKETING_CAMPAIGNS
+
current accepted Marketing semantics
+
current Audience eligibility
+
current marketing/contact-policy determination
+
Actor Authorisation
+
Resource Protection
+
Provider Readiness where required
+
current source applicability
        ↓
operation may continue
```

subject to the exact operation's remaining requirements.

Rejected:

```text
GROWTH entitlement
    → permission to contact customer
```

Rejected:

```text
CustomerContext exists
    → permission to contact customer
```

Rejected:

```text
email endpoint exists
    → permission to contact customer
```

Rejected:

```text
Audience match
    → permission to contact customer
```

---

## 16. Source-Business Truth

Commercial Marketing permission SHALL NOT create or alter:

```text
Product
Service
Offering
price
discount
availability
Booking
Appointment
Order
Payment
Refund
Return
customer relationship
```

or another source-owned business fact.

A Campaign may communicate an independently authoritative fact only within the source/currentness rules already accepted by composite MS-PROT-087.

Commercial permission does not make Campaign copy authoritative business truth.

---

## 17. Actor Authorisation

A valid Marketing Commercial grant SHALL NOT grant a person authority to operate Marketing.

The applicable actor must independently satisfy the accepted Actor Authorisation requirements for the exact Marketing operation.

Therefore:

```text
Commercial permission
    ≠ Actor Authorisation
```

A worker or merchant user lacking the required authority cannot obtain Marketing mutation authority merely because the merchant has GROWTH.

Likewise, valid Actor Authorisation cannot bypass missing Commercial permission for a protected Marketing operation.

---

## 18. AI Boundary

AI SHALL NOT infer commercial permission.

AI SHALL NOT convert:

```text
merchant appears to want marketing
```

into:

```text
CONDUCT_MARKETING_CAMPAIGNS satisfied
```

AI may assist only within its accepted authority.

AI-assisted drafting does not preserve a future right to commit, activate or externalise a Campaign.

The current protected Marketing boundary must still be satisfied.

This amendment introduces no new AI entitlement and no autonomous Marketing authority.

---

## 19. Provider Boundary and External-Effect Uncertainty

Provider readiness or provider acceptance SHALL NOT establish Commercial permission.

Conversely, Commercial rejection SHALL NOT establish whether an external provider effect occurred.

Where external-effect outcome is uncertain:

```text
UNKNOWN
```

or its applicable accepted equivalent SHALL remain truthful until existing reconciliation authority resolves it.

A retry SHALL preserve the accepted logical identity and idempotency rules.

Commercial state changes SHALL NOT be used to manufacture duplicate Campaign or Notification effects.

---

## 20. Analytical Boundary

This amendment does not classify Business Intelligence-owned Campaign measures.

Raw retained Campaign evidence and Marketing-owned observations remain distinct from derived analytical meaning.

Therefore:

```text
observe retained Campaign evidence
    ≠
receive every Campaign analytical service
```

Any protected analytical presentation or Campaign measure remains governed by its own accepted Business Intelligence and Commercial authorities.

Marketing commercial permission does not transfer analytical ownership.

---

## 21. Supporting-Service Rule

A service required to complete an authorised Marketing Campaign SHALL NOT receive an additional GROWTH classification merely because Marketing depends on it.

Supporting owners retain their independently accepted classifications.

This amendment does not reclassify:

```text
Publication
Notification
CustomerContext
PermissionGrant / regulatory evidence
Provider Readiness
Resource Protection
Merchant Attention
Business Intelligence
source capabilities
```

The supporting path must still be semantically, operationally and authoritatively valid.

“No additional Marketing toll” does not mean “no independent authority”.

---

## 22. Failure Distinctions

A Commercial rejection SHALL remain distinguishable from:

```text
Actor Authorisation rejection

invalid Campaign state

invalid Campaign Revision

missing merchant approval

Audience ineligibility

Audience evaluation unresolved

marketing/contact policy prohibited

marketing/contact policy unresolved

missing source-business authority

stale source fact

Campaign paused/cancelled

Resource Protection rejection

Provider Readiness failure

Notification failure

Publication failure

external-effect uncertainty

technical failure
```

A Commercial failure SHALL NOT be reported as one of those different conditions.

Likewise, failure of another predicate SHALL NOT be falsely represented as a Commercial denial.

---

## 23. Commercial Purpose Granularity

This amendment deliberately selects one initial protected purpose:

```text
CONDUCT_MARKETING_CAMPAIGNS
```

instead of separately commercialising:

```text
Campaign authoring
Audience management
Campaign scheduling
Campaign automation
direct email
```

because the current accepted standard allocation treats the bounded Campaign and Audience portfolio as one GROWTH service.

This decision is not a claim that future commercial packaging can never differ.

A future split requires a separately accepted authority identifying:

```text
the business justification
the exact owner-qualified targets
the protected-purpose boundaries
the migration/historical effect
the catalogue consequence
```

It SHALL NOT be inferred from implementation module boundaries.

---

## 24. Future Marketing Families

This classification is closed over the accepted composite MS-PROT-087 through v1.3 portfolio.

A future:

```text
Campaign purpose
Audience family
outreach family
trigger family
new Marketing operation
paid-media capability
external advertising service
```

SHALL NOT automatically inherit this classification.

Its governing authority must state whether it:

```text
uses the existing protected purpose

requires another protected purpose

requires no independent Commercial Entitlement

or remains commercially unresolved
```

Missing classification fails closed for catalogue admission.

---

## 25. Paid Advertising Boundary

Nothing in this amendment authorises:

```text
advertising spend

external advertising budgets

paid-media purchase

advertising auctions

automatic bid changes

lookalike advertising

external ad-platform optimisation
```

`MS-PROT-087-DQ-006` remains:

```text
DEFERRED
—
ACTIVE BEFORE PAID ADVERTISING
OR EXTERNAL CAMPAIGN SPEND
```

This amendment does not narrow, resolve or bypass that deferred decision.

---

## 26. Commercial Catalogue Boundary

This amendment supplies only the Marketing owner/supporting-service classification needed by the broader catalogue programme.

It does not establish:

```text
final CommercialEntitlementIdentity

final Commercial Access Binding identity

initial catalogue revision

FREE plan revision identity

BUSINESS plan revision identity

GROWTH plan revision identity

complete grant sets

pricing

usage allowance

fair-use quantity

production publication
```

`MS-PROT-056-V17-DQ-001` therefore remains:

```text
OPEN
```

until the complete Commercial Catalogue Manifest is explicitly approved.

---

## 27. Existing MS-PROT-087 Deferred Questions

This amendment changes no current MS-PROT-087 semantic DQ status.

The existing resolved status of:

```text
MS-PROT-087-DQ-001
MS-PROT-087-DQ-002
MS-PROT-087-DQ-003
MS-PROT-087-DQ-004
MS-PROT-087-DQ-005
```

remains unchanged.

`MS-PROT-087-DQ-006` remains deferred under Section 25.

This commercial classification does not reopen Marketing semantics.

---

## 28. Hard Invariants

### INV-087-V14-001 — Commercial Permission Is Independent

```text
Commercial Entitlement
    ≠ Semantic Applicability
    ≠ Actor Authorisation
    ≠ Resource Protection Admission
    ≠ marketing/contact permission
    ≠ Provider Readiness
    ≠ business truth
```

### INV-087-V14-002 — One Initial Marketing Commercial Purpose

The accepted MS-PROT-087 through v1.3 Campaign service SHALL use exactly:

```text
CONDUCT_MARKETING_CAMPAIGNS
```

as its initial protected Commercial purpose under this amendment.

### INV-087-V14-003 — Marketing Semantic Purposes Are Not Entitlements

The four v1.1 Campaign-purpose identities SHALL NOT independently become Commercial Entitlement purposes through this amendment.

### INV-087-V14-004 — GROWTH Only in the Standard Catalogue

FREE and BUSINESS SHALL NOT receive the protected Marketing purpose through the standard catalogue.

### INV-087-V14-005 — No Tier-Name Runtime Authority

A raw plan or tier name SHALL NOT establish runtime Marketing permission.

### INV-087-V14-006 — Current Permission Before Protected New Use

An earlier grant, approval, schedule or Campaign state SHALL NOT reserve future Commercial permission.

### INV-087-V14-007 — Existing History Is Not Future Marketing Permission

Observation of retained Campaign state SHALL NOT authorise new Marketing activity.

### INV-087-V14-008 — Restriction Must Not Require Repurchase

A bounded operation whose sole effect is to reduce or terminate future Campaign externalisation SHALL NOT require an independent Marketing entitlement under the accepted restriction contract.

### INV-087-V14-009 — Scheduled Campaign Does Not Reserve Commercial Authority

A schedule created under GROWTH SHALL NOT externalise after required current Marketing Commercial permission has ended.

### INV-087-V14-010 — Publication Retains Ownership

Marketing commercial permission SHALL NOT become Publication authority.

### INV-087-V14-011 — Notification Retains Ownership

Marketing commercial permission SHALL NOT become Notification authority or delivery truth.

### INV-087-V14-012 — Supporting Infrastructure Is Not a Second Marketing Toll

A supporting capability SHALL NOT acquire an additional GROWTH Marketing requirement merely because a protected Campaign depends on it.

### INV-087-V14-013 — No Generic Committed-Progression Bypass

No Marketing-owned post-commit exemption SHALL bypass the current-entitlement revalidation required before Campaign externalisation.

### INV-087-V14-014 — No History Destruction on Commercial Loss

Commercial loss SHALL NOT erase or reinterpret accepted Marketing history.

### INV-087-V14-015 — Missing Binding Is Not Permission

An absent, unknown or ambiguous Marketing Commercial binding SHALL fail closed for protected Marketing use.

### INV-087-V14-016 — Future Marketing Is Not Automatically Classified

New Marketing families or operations SHALL require explicit applicable commercial classification before catalogue admission.

### INV-087-V14-017 — Paid Advertising Remains Deferred

`CONDUCT_MARKETING_CAMPAIGNS` SHALL NOT create paid-advertising or external-spend authority.

---

## 29. Trade-offs and Rejected Alternatives

### Rejected — one entitlement per Campaign semantic purpose

This confuses business meaning with packaging and creates unnecessary catalogue complexity.

### Rejected — separate Campaign, Audience, Scheduling and Direct-Outreach commercial products

No current accepted standard allocation requires that fragmentation.

### Rejected — separate automation entitlement

Automation is bounded Campaign execution, not currently a separately allocated Growth service.

### Rejected — all Marketing state inaccessible after downgrade

This would make retained history and safety restrictions hostage to continued subscription.

### Rejected — all previously scheduled work completes after downgrade

This contradicts current-entitlement revalidation before externalisation.

### Rejected — Notification or Publication becomes Marketing-owned

This violates accepted ownership boundaries.

### Accepted trade-off

One protected Marketing service purpose creates a coarser Commercial package than the internal semantic decomposition.

That is deliberate.

The semantic decomposition remains precise internally while the commercial model remains simple for target merchants.

---

## 30. Falsification Result

The proposed classification was challenged against:

```text
a low-software-capacity salon merchant

an information-only merchant publishing
an ordinary website announcement

a BUSINESS merchant attempting Marketing email

a GROWTH merchant without current
marketing-contact permission

a candidate who matches an Audience
but cannot permissibly be contacted

a Campaign scheduled before downgrade

a merchant needing to cancel after downgrade

a merchant needing retained Campaign history

an existing Campaign being reused
after commercial permission ends

Notification as supporting infrastructure

provider uncertainty after possible acceptance

paid advertising hidden behind Campaign authority

future Marketing families attempting
automatic entitlement inheritance
```

The initial split-purpose sketch failed the minimality and semantic-leakage tests.

The refined single-purpose proposal survives the examined cases.

**Falsification outcome: PASS.**

---

## 31. Implementation Consequences

Acceptance of this amendment establishes design authority only.

A future implementation, when separately activated, must preserve at minimum:

```text
exact owner-qualified Marketing access target

exact Commercial purpose resolution

fail-closed missing/ambiguous binding

no raw tier-name permission branch

current permission revalidation

downgrade-safe Campaign execution

no destructive history rewrite

bounded no-entitlement observation

bounded no-entitlement restriction/cancellation

clear Commercial-versus-semantic failure distinctions

no ownership transfer to Commercial
```

This amendment does not select implementation technology, persistence representation, API shape, class names, database schema or transaction mechanism.

No implementation node is activated by this authority.

---

## 32. Governance Consequences

Formalisation of this accepted authority requires:

```text
1. AUTHORITY-INDEX.md
   to compose MS-PROT-087 through v1.4;

2. DEFERRED-DECISION-REGISTER.md
   to record bounded progress under
   MS-PROT-056-V17-DQ-001
   while leaving DQ-001 OPEN;

3. existing MS-PROT-087 DQ statuses
   to remain unchanged;

4. CANONICAL-SEMANTIC-LEXICON.md
   to be reviewed for actual vocabulary impact;

5. IMPLEMENTATION-RULES.md
   to be reviewed for actual procedural impact;

6. DESIGN-CORPUS-CONFORMANCE
   to be performed;

7. implementation activation
   to remain NONE.
```

A lexicon change is not required merely because this authority creates owner-qualified Commercial-access identifiers.

An Implementation Rules change is not required merely because this authority supplies a new accepted classification.

Any later material issue requires a new governed design cycle rather than an unreviewed formalisation change.

---

## 33. Provenance and Corrigendum

The earlier chat-only Marketing frontier sketch that proposed separate direct-outreach and automation commercial purposes was never repository authority.

It is superseded as design discussion by this accepted authority.

No accepted repository authority is corrected by that withdrawal.

This authority was derived from the live accepted:

```text
MS-FUNDAMENTAL-VISION-001

MS-PROT-056 through v1.9

MS-PROT-087 through v1.3

current AUTHORITY-INDEX

current DEFERRED-DECISION-REGISTER

current CANONICAL-SEMANTIC-LEXICON

current DESIGN-CORPUS-CONFORMANCE

current SEQUENCE
```

at the inspected `development` state.

---

## 34. Acceptance Record

The complete authority was explicitly manually approved on 16 September 2026 after the complete Design-Rules lifecycle.

The approved decision is:

> **Accept MS-PROT-087 v1.4 as the Marketing commercial-access classification amendment over composite MS-PROT-087 through v1.3, with exactly one protected Commercial purpose `CONDUCT_MARKETING_CAMPAIGNS`, exact protected access contract `marketing/campaign-service-access@1`, the three stated no-independent-entitlement contracts, GROWTH standard allocation, no final CommercialEntitlementIdentity, no change to existing MS-PROT-087 DQ states, `MS-PROT-087-DQ-006` remaining deferred, `MS-PROT-056-V17-DQ-001` remaining OPEN, and implementation activation NONE.**

**STATUS:** ACCEPTED  
**IMPLEMENTATION ACTIVATION:** NONE
