# MS-PROT-050 v1.6 — Public Business Hours Commercial Access Contracts Amendment

**Document ID:** MS-PROT-050  
**Version:** 1.6  
**Status:** ACCEPTED  
**Approved:** 15 September 2026 by explicit manual approval in ChatGPT  
**Authority type:** Business Hours commercial-access classification amendment  
**Design node:** Owner-classification prerequisite discovered by `MS-PROT-056-V17-DQ-001` completeness audit  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-050 through v1.5 within commercial-access classification only  
**Depends on:** Composite MS-PROT-050 through v1.5; MS-PROT-051 v1.6; composite MS-PROT-056 through v1.9; composite MS-PROT-027; composite MS-PROT-062; composite MS-PROT-063; composite MS-PROT-071; composite MS-PROT-076  
**Preserves:** Business Hours source ownership; Profile/Location ownership; Scheduling ownership; Storefront/Exposure ownership; Enquiry independence; Commercial ownership of entitlement identities and bindings  
**Does not resolve:** `MS-PROT-056-V17-DQ-001` in full  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING

**Product identity note:** inherited `Main Street` references identify the product currently named **GrandRue**. Stable `MS-*` and `mainstreet.*` identifiers remain unchanged pending separately governed migration.

---

# 1. Governing Decision

Composite MS-PROT-050 SHALL establish the following exact commercial-access contracts:

| Exact owner-qualified contract | Commercial classification | Standard allocation |
|---|---|---|
| `business-hours/public-source-observation-access@1` | No Commercial Entitlement | plan-independent supporting access |
| `business-hours/merchant-observation-access@1` | No Commercial Entitlement | plan-independent bounded access |
| `business-hours/standard-authoring-access@1` | `MAINTAIN_MERCHANT_PRESENCE` | FREE + BUSINESS + GROWTH |
| `business-hours/standard-withdrawal-access@1` | No Commercial Entitlement | plan-independent bounded removal |
| `business-hours/dated-override-maintenance-access@1` | `MAINTAIN_MERCHANT_PRESENCE` | FREE + BUSINESS + GROWTH |

Protected contracts use target family:

```text
OPERATION_ACCESS
```

The concrete `CommercialEntitlementIdentity` values remain Commercial-owned work under:

```text
MS-PROT-056-V17-DQ-001
```

Missing classification SHALL NOT be interpreted as an exemption.

---

# 2. Purpose Reuse

MS-PROT-050 SHALL reuse the already accepted protected commercial purpose:

```text
MAINTAIN_MERCHANT_PRESENCE
```

It SHALL NOT introduce a second purpose merely named:

```text
MAINTAIN_PUBLIC_BUSINESS_HOURS
```

for the current standard catalogue.

Reason:

MS-PROT-056 v1.7 explicitly places:

```text
legitimately public business information
locations
hours
contact details
```

inside the FREE Merchant Public Presence proposition.

Therefore Public Business Hours are a separately owned source responsibility participating in the same commercial merchant value.

Canonical:

```text
profile/merchant-presence-authoring-access@1
        +
MAINTAIN_MERCHANT_PRESENCE

business-hours/standard-authoring-access@1
        +
MAINTAIN_MERCHANT_PRESENCE
```

does **not** mean:

```text
Profile owns Business Hours
```

or:

```text
one entitlement target covers both owners
```

Each exact:

```text
owner-qualified target
+
protected purpose
```

remains a separate Commercial Access Binding under MS-PROT-056 v1.9.

This follows the already accepted pattern whereby MS-PROT-081 and MS-PROT-091 reuse `PLAN_WORKFORCE_SCHEDULE` while retaining separate owner targets.

---

# 3. Ownership Boundary

Business Hours continues to own:

```text
StandardBusinessHoursRevision
current Standard Business Hours pointer
BusinessOperatingOverrideRevision
current dated-override pointer
Effective Business Hours
CurrentBusinessOperatingStatus
Business Hours interval semantics
Business Hours temporal resolution
```

Commercial owns:

```text
CommercialEntitlementIdentity
CommercialEntitlementDefinition
Commercial Access Binding
grant provenance
effective Commercial permission
plan-revision grant sets
catalogue publication
```

Profile does not acquire Business Hours mutation authority merely because both use the commercial purpose:

```text
MAINTAIN_MERCHANT_PRESENCE
```

Commercial-purpose reuse is not semantic ownership transfer.

---

# 4. Public Source Observation

## 4.1 Contract

```text
business-hours/public-source-observation-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for the bounded public-source material already authorised for public representation.

## 4.2 Scope

The contract may supply owner-qualified current material such as:

```text
current applicable public Business Hours
current Effective Business Hours
current OPEN / CLOSED / UNSPECIFIED status
current closing time where established
next-open information where established
scope-qualified public hours
```

only where the corresponding source, Projection and Exposure authority permits it.

## 4.3 Exclusions

The contract does not expose:

```text
revision history
logical request identities
Controller identity
internal concurrency evidence
private provenance
override reason merely because stored
future unpublished operational intent
another Merchant Scope
another location's hours as the selected scope
```

An `optionalReason` recorded on a dated override is not automatically public.

## 4.4 Supporting-access meaning

This explicit no-entitlement classification prevents:

```text
public website shows hours
        ↓
invent another paid Business Hours read entitlement
```

It does not itself grant website delivery.

Public website delivery still requires its existing Storefront and namespace permissions.

---

# 5. Merchant Observation

## 5.1 Contract

```text
business-hours/merchant-observation-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for otherwise-authorised merchant inspection of Business Hours source state.

Potential owner-qualified material includes:

```text
current stable weekly hours
current dated overrides
current Effective Business Hours
CurrentBusinessOperatingStatus
retained stable-hours revisions
retained override revisions
authorised logical-operation results
```

where applicable access and retention authority permits.

## 5.2 Boundary

Observation does not permit mutation.

The exemption does not create:

```text
cross-merchant access
bulk export
staff authoring privileges
unrestricted retained-history access
public Exposure
```

Commercial loss does not by itself make legitimate retained Business Hours records unreadable.

---

# 6. Stable Weekly Hours Authoring

## 6.1 Contract

```text
business-hours/standard-authoring-access@1
```

requires:

```text
MAINTAIN_MERCHANT_PRESENCE
```

## 6.2 Covered operations

The contract covers the existing MS-PROT-050 v1.4 owner behaviour for:

```text
first configuration of
Standard Business Hours

replacement of current
Standard Business Hours
```

including material changes to:

```text
weekly intervals
scope-qualified IANA time zone
```

where otherwise valid.

## 6.3 Existing owner rules remain mandatory

Commercial permission does not waive:

```text
current ACTIVE Controller requirement

OPEN Merchant Account requirement

account-wide Suspension restriction

Business Hours Scope validity

Merchant Location affinity where applicable

expected-current revision

validation

same-scope concurrency

logical retry identity

dated-override reconciliation required
by MS-PROT-050 v1.5
```

Commercial permission means only that GrandRue commercially permits the otherwise-valid source mutation.

---

# 7. Stable Weekly Hours Withdrawal

## 7.1 Contract

```text
business-hours/standard-withdrawal-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for the existing MS-PROT-050 v1.4 stable-hours withdrawal operation.

## 7.2 Reason

Stable-hours withdrawal removes current stable Public Business Hours.

It does not create additional commercially protected service capability.

GrandRue SHALL NOT require purchase of a subscription merely to stop representing stable Business Hours.

## 7.3 Existing reconciliation remains binding

The exemption does not bypass MS-PROT-050 v1.5.

If current/future configured dated overrides would become orphaned:

```text
BUSINESS_HOURS_OVERRIDE_RECONCILIATION_REQUIRED
```

continues to block the withdrawal.

No-entitlement classification is not authority to destroy temporal consistency.

---

# 8. Dated Override Maintenance

## 8.1 Contract

```text
business-hours/dated-override-maintenance-access@1
```

requires:

```text
MAINTAIN_MERCHANT_PRESENCE
```

## 8.2 Covered operations

The contract covers the complete v1.5 dated-override mutation portfolio:

```text
first configuration

replacement

withdrawal
```

of one exact:

```text
BusinessHoursScope
×
localDate
```

override.

## 8.3 Why withdrawal remains protected here

Dated-override withdrawal is materially different from stable-hours withdrawal.

Example:

```text
normal Monday
09:00–17:00

Christmas override
CLOSED
```

Withdrawing that override means:

```text
ordinary Monday hours resume
```

and may therefore make the merchant publicly represented as open.

Likewise withdrawing a special-opening override may narrow hours.

Therefore:

```text
withdraw dated override
```

is not inherently a pure removal/restriction operation.

For commercial classification it belongs to the same bounded maintenance purpose as configuring or replacing that dated override.

This avoids a no-entitlement backdoor that could materially alter current/future Public Business Hours.

---

# 9. FREE Allocation

Both protected Business Hours contracts belong to FREE:

```text
business-hours/standard-authoring-access@1

business-hours/dated-override-maintenance-access@1
```

through:

```text
MAINTAIN_MERCHANT_PRESENCE
```

BUSINESS and GROWTH explicitly include the same grants through the standard hierarchy.

Canonical:

```text
FREE
    → maintain supported
      Public Business Hours

BUSINESS
    → same presence grants
      + operating portfolios

GROWTH
    → same presence grants
      + Business
      + Growth portfolios
```

No merchant must purchase BUSINESS merely to publish or maintain truthful opening hours.

---

# 10. No Per-Location Commercial Fragmentation

A Business Hours Scope may be:

```text
MERCHANT

MERCHANT_LOCATION
```

The number of legitimate Merchant Locations or Business Hours Scopes SHALL NOT create another commercial purpose under this amendment.

Rejected:

```text
first location FREE
additional location BUSINESS
```

Rejected:

```text
per-location Business Hours entitlement
```

unless a future independently justified quantitative/commercial policy is accepted.

Current semantic allocation remains based on service purpose, not descriptive scale.

Prices and quantitative allowances remain outside this amendment.

---

# 11. No Scheduling Entitlement Through Business Hours

Business Hours is an input to Scheduling where applicable.

Therefore:

```text
MAINTAIN_MERCHANT_PRESENCE
```

does not grant:

```text
Appointment creation

Booking creation

Scheduling configuration

Workforce Scheduling

Rota

resource allocation

customer commitment
```

A FREE merchant may truthfully maintain opening hours while Scheduling is not configured or not commercially available.

This preserves the information-only merchant model.

---

# 12. No Reverse Scheduling Authority

Possession of a BUSINESS Scheduling entitlement does not independently grant Business Hours mutation.

Where Business Hours authoring is required, the exact:

```text
business-hours/... access contract
```

must be satisfied through its own Commercial Access Binding.

In the standard catalogue BUSINESS will also contain the FREE Business Hours grants.

That explicit grant inclusion, not plan-name implication, satisfies the requirement.

---

# 13. Storefront Boundary

Business Hours authoring does not grant:

```text
PUBLISH_STOREFRONT_COMPOSITION

SERVE_CUSTOMER_WEBSITE

USE_PLATFORM_WEBSITE_NAMESPACE

USE_MERCHANT_CONTROLLED_WEBSITE_DOMAIN
```

Conversely, website-delivery entitlement does not grant Business Hours mutation.

Canonical:

```text
Business Hours source truth
        ↓
authorised public-source observation
        ↓
Storefront projection/composition
        ↓
website delivery
```

Every boundary retains its own authority.

---

# 14. Enquiry Boundary

Business Hours commercial permission does not grant Enquiry origination or observation.

Enquiry's accepted FREE access contracts remain separately bound.

Likewise:

```text
ORIGINATE_ENQUIRY
```

does not grant Business Hours authoring.

A customer may submit an otherwise-enabled Enquiry while:

```text
OPEN
CLOSED
TEMPORARILY CLOSED
```

exactly as MS-PROT-050 already requires.

---

# 15. Publication Boundary

Business Hours is not Publication-owned PublishedContent.

Therefore:

```text
AUTHOR_AND_PUBLISH_INFORMATION
```

through the Publication contract does not grant Business Hours mutation.

Likewise:

```text
MAINTAIN_MERCHANT_PRESENCE
```

does not grant Publication authoring merely because both belong to FREE.

The catalogue must bind both explicit targets.

---

# 16. Public Status Has No Independent Entitlement

The derived:

```text
CurrentBusinessOperatingStatus
```

does not receive a separate paid entitlement.

It is a deterministic Business Hours result over authoritative source state and current time.

Its public use is subject to its owner-qualified public observation, Projection and Exposure boundaries.

Rejected:

```text
FREE opening hours
+
paid Open Now feature
```

for the current standard catalogue.

No accepted product value distinction justifies that fragmentation.

---

# 17. Historical Recovery

Recovery of an already-committed mutation result is observation/reconciliation of an existing authoritative effect.

It is not a new authoring effect.

Therefore an exact retry may recover its previously committed result according to v1.4/v1.5 even if the originating commercial grant later changes.

A retry that would create a new mutation still requires current commercial permission where the applicable operation is protected.

---

# 18. Commercial Loss

Because the standard FREE plan includes the protected Business Hours purposes, an ordinary merchant retaining Standing FREE remains commercially permitted to maintain Business Hours.

This amendment nevertheless preserves the general distinction:

```text
commercial permission lost
        ≠
Business Hours deleted
```

Loss of a grant source SHALL NOT automatically:

```text
withdraw stable hours

withdraw dated overrides

mark merchant CLOSED

change time zone

delete history

change Exposure

change website composition
```

Source state remains source truth until an authorised source operation changes it.

---

# 19. Account Closure and Suspension

Commercial entitlement does not override account lifecycle.

An account-wide Suspension may block source mutation even where FREE entitlement exists.

A CLOSING/CLOSED account follows its accepted account-lifecycle authority.

No Commercial grant creates an exception.

Where Business Hours cleanup or withdrawal is legitimately required during a lifecycle state, that capability must be supported by accepted owner/account authority; this amendment does not invent a new privileged closure actor.

---

# 20. Staff Authority

This amendment creates no staff privilege to edit Business Hours.

The current v1.4/v1.5 source operation remains Controller-only.

Therefore:

```text
FREE / BUSINESS / GROWTH
        ≠
staff may edit hours
```

A future delegated Business Hours privilege requires separately accepted actor-authority design.

Commercial classification need not be reopened merely because a future authorised actor is added, provided target meaning and protected purpose remain unchanged.

---

# 21. AI Assistance

AI may help prepare a candidate Business Hours mutation in ordinary language.

The commercial predicate applies at the authoritative owner mutation boundary.

AI MUST NOT:

```text
satisfy MAINTAIN_MERCHANT_PRESENCE

invent a grant

infer subscription authority

commit Business Hours directly

treat model confidence as Controller authority
```

AI unavailability does not remove deterministic FREE Business Hours maintenance.

---

# 22. Catalogue Binding Consequence

Following acceptance, Commercial may later create canonical entitlement identities for exactly these protected target/purpose pairs:

```text
business-hours/standard-authoring-access@1
+
MAINTAIN_MERCHANT_PRESENCE

business-hours/dated-override-maintenance-access@1
+
MAINTAIN_MERCHANT_PRESENCE
```

Under MS-PROT-056 v1.9:

```text
same protected purpose
+
different owner-qualified target
        ↓
distinct exact Commercial Access Bindings
```

No wildcard:

```text
MAINTAIN_MERCHANT_PRESENCE
→ everything called presence
```

is permitted.

---

# 23. Supporting Classification Consequence

Catalogue assembly must additionally retain the explicit classifications:

```text
business-hours/public-source-observation-access@1
    → no Commercial Entitlement

business-hours/merchant-observation-access@1
    → no Commercial Entitlement

business-hours/standard-withdrawal-access@1
    → no Commercial Entitlement
```

Those exemptions are positive owner classifications.

They are not missing binding information.

---

# 24. Falsification

## F1 — Information-only publisher

Merchant has no Orders, Booking or Scheduling.

Merchant configures ordinary opening/support hours.

Expected:

```text
FREE
+
MAINTAIN_MERCHANT_PRESENCE
→ commercial requirement satisfied
```

No transaction capability is fabricated.

**PASS**

## F2 — Retailer changes Saturday hours

Expected:

exact stable-hours authoring contract required.

Inventory/Ordering access is irrelevant to this source mutation.

**PASS**

## F3 — Christmas closure

Merchant creates:

```text
25 December = CLOSED
```

Expected:

dated-override maintenance uses the same FREE presence purpose.

**PASS**

## F4 — Withdraw Christmas closure

Ordinary weekly hours would resume.

Expected:

dated-override maintenance remains protected.

No no-entitlement mutation backdoor exists.

**PASS**

## F5 — Remove stable hours entirely

Expected:

standard withdrawal requires no Commercial Entitlement but all owner reconciliation/precondition rules remain.

**PASS**

## F6 — Multiple locations

Merchant configures different Swansea and Cardiff hours.

Expected:

same commercial purpose; exact scope remains source semantics.

No additional tier follows from count.

**PASS**

## F7 — Staff member on GROWTH

Staff lacks Business Hours mutation authority.

Expected:

GROWTH entitlement does not create Controller authority.

**PASS**

## F8 — Website delivered on FREE

Website displays authorised opening hours.

Expected composition:

```text
Business Hours public observation
    no CE

+
Storefront website delivery
    exact FREE grant

+
platform namespace use
    exact FREE grant
```

No duplicate Business Hours read charge.

**PASS**

## F9 — Custom domain

BUSINESS merchant displays the same hours through custom domain.

Expected:

Business Hours commercial meaning unchanged.

Custom-domain permission is a separate Merchant Brand Infrastructure purpose.

**PASS**

## F10 — Scheduling unavailable

Business Hours can still be authored and publicly represented.

Expected:

FREE Business Hours remains valid.

**PASS**

## F11 — Merchant has Scheduling

Scheduling consumes current Effective Business Hours.

Expected:

Scheduling entitlement does not make Scheduling the hours owner and does not bypass Business Hours commercial target.

**PASS**

## F12 — AI unavailable

Merchant can still edit hours deterministically.

**PASS**

## F13 — Old committed retry

Authoring succeeded before a later grant-source transition; acknowledgement was lost.

Expected:

exact retry returns committed result rather than requiring a second mutation.

**PASS**

## F14 — Historical revisions

Merchant downgrades from GROWTH to FREE.

Expected:

Business Hours grants remain because FREE contains them; source history is unchanged.

**PASS**

## F15 — Public reason leakage

Christmas override contains internal descriptive reason.

Expected:

public observation does not automatically expose the reason.

**PASS**

## F16 — Low-software-capacity merchant

Merchant sees:

```text
Opening hours
Special hours
Closed on this date
```

not entitlement graph terminology.

**PASS**

---

# 25. Alternatives

## A. New `MAINTAIN_PUBLIC_BUSINESS_HOURS` purpose

**REJECTED for the initial catalogue.**

Public Business Hours are already explicitly part of the FREE Merchant Public Presence commercial proposition.

A new purpose would create commercial granularity without demonstrated product value.

## B. Reuse the Profile access target

**REJECTED.**

Commercial-purpose reuse does not permit target reuse across semantic owners.

Business Hours requires its own owner-qualified contracts.

## C. One wildcard Merchant Presence entitlement

**REJECTED.**

MS-PROT-056 v1.9 requires exact owner-qualified target bindings.

## D. Make all Business Hours mutations no-entitlement

**REJECTED.**

That would erase the distinction between the commercially admitted FREE service and plan-independent supporting/removal operations.

## E. Charge separately for dated exceptions

**REJECTED.**

Holiday/special hours are necessary truthful maintenance of the same public operating-hours service.

## F. Charge per location

**REJECTED.**

No accepted value or semantic boundary justifies it.

---

# 26. Fundamental Vision Conformance

**VISION-CONFORMING**

The merchant-facing proposition remains simple:

```text
FREE
→ establish and maintain
a useful truthful public presence
```

Business Hours are part of that public presence.

The merchant does not need to understand:

```text
owner-qualified contracts
purpose reuse
Commercial Access Bindings
plan-revision grant sets
```

The internal exactness prevents one commercial permission from becoming semantic ownership.

No unnecessary merchant-facing software complexity is introduced.

---

# 27. Hard Invariants

1. Business Hours remains a separate semantic owner from Profile.
2. Business Hours reuses `MAINTAIN_MERCHANT_PRESENCE` only as commercial-purpose vocabulary.
3. Purpose reuse never permits target wildcarding.
4. Stable-hours authoring requires the protected purpose.
5. Dated-override configure, replace and withdrawal require the protected purpose.
6. Stable-hours withdrawal requires no Commercial Entitlement.
7. Public-source observation requires no Commercial Entitlement.
8. Merchant observation requires no Commercial Entitlement.
9. FREE contains both protected Business Hours targets.
10. BUSINESS and GROWTH explicitly contain the same targets.
11. No per-location commercial distinction is introduced.
12. Business Hours permission does not grant Scheduling.
13. Scheduling permission does not grant Business Hours mutation.
14. Business Hours permission does not grant Storefront delivery.
15. Storefront permission does not grant Business Hours mutation.
16. Business Hours permission does not grant Publication or Enquiry mutation.
17. Current operating status has no independent commercial entitlement.
18. Commercial loss never rewrites Business Hours source truth.
19. Commercial permission never creates actor authority.
20. Commercial permission never bypasses account lifecycle.
21. AI cannot establish or substitute for Commercial permission.
22. Concrete entitlement identities remain MS-PROT-056-owned.
23. Acceptance does not publish a Commercial catalogue.
24. Acceptance does not activate implementation.

---

# 28. DQ-001 Effect

Upon approval:

```text
Public Business Hours
```

becomes owner-classification-complete for the current standard catalogue.

The path becomes:

```text
MS-PROT-050 v1.4
stable hours owner operations
        +
MS-PROT-050 v1.5
dated override owner operations
        +
MS-PROT-050 v1.6
commercial access classification
        ↓
MS-PROT-056 catalogue binding ready
for the Public Business Hours slice
```

`MS-PROT-056-V17-DQ-001` remains OPEN because the corpus-wide audit has identified additional owner-classification gaps outside Business Hours.

---

# 29. Remaining Catalogue Audit State

Acceptance of v1.6 adds Business Hours to the already binding-ready group:

```text
Merchant Profile
Storefront composition/delivery
Website namespace use
Publication
Enquiry
Business Hours
Workforce Scheduling / Timekeeping / Leave
Workforce Compensation / Payroll
Workforce Rota
```

The audit has already established that additional defined-scope portfolios still require owner-local commercial classification before a complete manifest can be approved, including at least:

```text
Product / Offering presentation

Booking / Appointment / Scheduling

Ordering

Payment participation

Inventory

Financial Operations

Business Intelligence analytical portfolios

Human Customer Communication

Routine factual customer-service responses

Campaign / audience execution
```

These SHALL be resolved by their owners rather than inferred from the v1.7/v1.8 allocation tables.

---

# 30. Governance Outcome

**Feature Admission:** PASS  
**Fundamental Vision Conformance:** VISION-CONFORMING  
**Owner-qualified target separation:** PASS  
**Commercial-purpose reuse:** PASS  
**FREE allocation conformance:** PASS  
**Stable/dated-hours distinction:** PASS  
**Public/merchant observation classification:** PASS  
**Withdrawal-boundary review:** PASS  
**Multi-location falsification:** PASS  
**Scheduling boundary:** PASS  
**Storefront boundary:** PASS  
**Anti-wildcard review:** PASS  
**Low-software-capacity merchant falsification:** PASS  
**Ambiguity review:** PASS  

**RECOMMENDATION: ACCEPT**

**Manual approval:** GRANTED — 15 September 2026  
**Repository formalisation:** AUTHORISED AND COMPLETED  
**Implementation activation:** NONE

---

# 31. Acceptance Statement

> **GrandRue shall include maintenance of truthful Public Business Hours within the FREE merchant-presence proposition while preserving Business Hours as a separate semantic owner. Stable weekly authoring and dated-override maintenance use exact Business-Hours-owned targets bound to the shared `MAINTAIN_MERCHANT_PRESENCE` commercial purpose; public and merchant observation and stable-hours withdrawal remain explicitly plan-independent. Commercial-purpose reuse does not transfer source ownership, create wildcard grants, grant actor authority, or confer Scheduling, Publication, Enquiry or Storefront permission.**
