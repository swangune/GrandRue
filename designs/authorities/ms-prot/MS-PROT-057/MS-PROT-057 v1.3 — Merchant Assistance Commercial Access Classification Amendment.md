# MS-PROT-057 v1.3 — Merchant Assistance Commercial Access Classification Amendment

**Document ID:** MS-PROT-057  
**Version:** 1.3  
**Status:** ACCEPTED  
**Approved:** 19 September 2026 — explicit manual approval in ChatGPT after complete pre-approval presentation, Fundamental Vision Conformance, review, falsification and ambiguity review  
**Authority type:** Merchant-assistance supporting commercial-access classification amendment  
**Governed by:** `designs/DESIGN-RULES.md`; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-057 through v1.2 within Commercial access classification only  
**Depends on:** Composite MS-PROT-057 through v1.2; composite MS-PROT-056 through v1.9; applicable owner-qualified access, Actor Authorisation, Projection, Exposure, Resource Protection and provider-readiness authorities  
**Relationship to:** `MS-PROT-056-V17-DQ-001`  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** `VISION-CONFORMING`

---

## 0. Authority Identity Preflight

```text
target branch
    development

review basis HEAD
    88205ed0e4d3fe8d23c40e21a9f00dae0622bac3

current MS-PROT-057 composition
    base + v1.1 + v1.2

MS-PROT-057 v1.3
    unoccupied
```

Latest-head and identifier availability MUST be repeated immediately before any post-approval formalisation.

---

## 1. Governing decision

GrandRue SHALL establish exactly one bounded merchant-assistance supporting access contract:

```text
merchant-assistance/supporting-interaction-access@1
```

Commercial classification:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

Target family:

```text
PLATFORM_SERVICE_ACCESS
```

This contract introduces **no new `CommercialEntitlementIdentity`**.

---

## 2. Why there is no separate AI entitlement

Composite MS-PROT-057 establishes that AI/assistance:

```text
may interpret
may explain
may prepare candidates
may coordinate bounded owners
```

but:

```text
does not own business truth
does not own merchant intent
does not activate configuration
does not grant source authority
does not execute unsupported business operations
```

MS-PROT-056 v1.7 further establishes:

> AI-assisted interaction does not acquire broader permission than the underlying purpose.

Therefore GrandRue SHALL NOT introduce:

```text
USE_AI
USE_CHATGPT
USE_ASSISTANT
AI_PREMIUM_ACCESS
```

as a current standard commercial purpose merely because assistance may use an AI model internally.

---

## 3. Exact bounded responsibility portfolio

`merchant-assistance/supporting-interaction-access@1` applies only to the accepted merchant-assistance responsibility portfolio through MS-PROT-057 v1.2:

```text
Onboarding & Configuration Discovery
Reconfiguration
Website Population & Management
Scheduling & Reservation
Commerce
Merchant Presence
Workforce & Access
Merchant Administration
Customer Engagement
Business Intelligence
```

This does **not** grant those capabilities.

It only governs the assistance layer operating over them.

A later materially new assistance responsibility does not automatically enter this contract.

---

## 4. Presence and website assistance

This classification closes the commercial ambiguity in the FREE portfolio:

```text
Presence and website assistance
```

from MS-PROT-056 v1.7.

Canonical:

```text
FREE merchant
    +
applicable FREE source permission
        ↓
merchant assistance may help
prepare / explain / populate / guide

without another commercial toll
```

For example:

```text
"Add boiler servicing for £95."
```

may be assisted through:

```text
merchant-assistance/supporting-interaction-access@1
```

but authoritative Offering creation still requires:

```text
offering/merchant-definition-authoring-access@1
+
MAINTAIN_MERCHANT_OFFERING_DEFINITION
```

---

## 5. Paid capability assistance

The same rule applies when the underlying operation belongs to BUSINESS or GROWTH.

Example:

```text
"Set up appointments for this service."
```

Assistance itself creates no second commercial requirement.

Actual Appointment commitment capability remains governed by:

```text
appointment/time-commitment-establishment-access@1
+
ESTABLISH_APPOINTMENT_TIME_COMMITMENT
```

where applicable.

Likewise:

```text
"Create a campaign for customers who..."
```

does not make Marketing FREE.

Actual Marketing use still requires:

```text
marketing/campaign-service-access@1
+
CONDUCT_MARKETING_CAMPAIGNS
```

---

## 6. Assistance cannot satisfy an underlying entitlement

Hard invariant:

```text
merchant-assistance/supporting-interaction-access@1
        ≠
underlying capability permission
```

Therefore:

```text
assistant available
+
Ordering entitlement absent
        ↓
cannot create new Order commitment
```

and:

```text
assistant available
+
Marketing entitlement absent
        ↓
cannot execute Marketing Campaign
```

and:

```text
assistant available
+
Listing authoring entitlement absent
        ↓
cannot create or materially revise Listing
```

---

## 7. Non-authoritative preparation

Where the underlying owner already classifies preparation as:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

merchant assistance may help perform that preparation without inventing another entitlement.

Examples include:

```text
drafting content
candidate interpretation
form assistance
validation assistance
preview preparation
```

Preparation remains non-authoritative until the applicable owner operation commits.

---

## 8. Observation and explanation

Merchant assistance may explain already-authorised information only where the caller independently has access to that information.

Canonical:

```text
merchant can observe information
        +
assistance available
        ↓
assistant may explain it

merchant cannot observe information
        ↓
assistant cannot reveal it
```

AI/assistance MUST NOT become an access-control bypass.

---

## 9. Business Intelligence boundary

Business Intelligence assistance SHALL NOT manufacture analytical permission.

Example:

```text
BUSINESS analytics entitlement present
        ↓
assistant may explain authorised BUSINESS analytical material
```

but:

```text
FREE merchant
        ↓
assistant cannot expose BUSINESS analytical material
```

Likewise GROWTH Campaign analytics remains protected independently.

---

## 10. Customer Communication boundary

Routine customer-service automation remains protected by:

```text
customer-communication/automated-response-access@1
+
AUTOMATE_ROUTINE_CUSTOMER_SERVICE
```

Merchant Assistance availability SHALL NOT bypass that commercial classification.

An AI model generating candidate wording is distinct from authoritative automated customer response execution.

---

## 11. Marketing boundary

Merchant Assistance may help prepare Campaign material only within accepted Marketing preparation authority.

It SHALL NOT establish:

```text
Campaign execution permission
audience permission
contact permission
suppression satisfaction
provider readiness
```

Those remain independently governed.

---

## 12. Resource Protection

`NO INDEPENDENT COMMERCIAL ENTITLEMENT` does not mean:

```text
unlimited model calls
unlimited tokens
unlimited image generation
unbounded compute
```

Resource Protection may still enforce:

```text
rate limits
fair-use limits
abuse protection
cost protection
concurrency limits
```

provided those controls do not silently redefine commercial tier semantics.

Quantitative commercial allowances remain under:

```text
MS-PROT-056-V17-DQ-003
```

---

## 13. Provider/model choice

The commercial classification does not depend on:

```text
OpenAI
another hosted model
self-hosted model
deterministic implementation
```

The same assistance contract remains valid if GrandRue changes technical provider.

Commercial permission SHALL NOT be bound to model brand.

---

## 14. AI unavailability

Because merchant assistance is supporting rather than source authority:

```text
AI unavailable
        ↓
ordinary deterministic business operation remains available
```

where the underlying capability is otherwise usable.

This preserves MS-PROT-057 v1.2.

---

## 15. FREE does not imply every AI responsibility is usable

A FREE merchant having access to the assistance layer does not mean every responsibility produces a meaningful surface.

For example:

```text
Business Intelligence assistance
```

requires independently authorised Business Intelligence material.

Similarly:

```text
Workforce assistance
```

is irrelevant where Workforce semantics are not active.

Canonical:

```text
assistance support exists
+
underlying semantics inapplicable
        ↓
no fabricated assistant workflow
```

---

## 16. Future premium AI

This amendment does not prohibit a later separately admitted premium AI service.

But such a service would require:

```text
Feature Admission
exact service boundary
exact target contract
protected commercial purpose
tier allocation
cost/usage treatment
authority boundaries
```

It MUST NOT be inferred from the current supporting-assistance contract.

---

## 17. Notification comparison

Notification does not require another classification amendment for DQ-001.

Current authority already establishes Notification as a supporting delivery substrate, and MS-PROT-056 v1.7 §10 states:

```text
necessary notification paths
must not become a separate premium obstacle
to completing an included workflow
```

MS-PROT-075 further permits Commercial Entitlement to affect a channel only where an accepted Commercial Entitlement Definition establishes that relationship.

No current standalone Notification entitlement definition exists.

Therefore the initial standard manifest SHALL NOT invent one.

---

## 18. First-party client and presentation comparison

Likewise GrandRue SHALL NOT mint entitlements merely for:

```text
merchant-web
Android client
iOS client
Windows client
macOS client
shared presentation architecture
```

MS-PROT-056 v1.7 explicitly introduces no paid differentiation for MS-PROT-093/MS-PROT-094 client and presentation architecture.

---

## 19. Falsification — FREE merchant builds website

Merchant says:

```text
"Add our new repair service to the website."
```

Expected:

```text
assistant may interpret and prepare
Offering authoring requires FREE Offering purpose
Storefront publication requires its FREE purpose
website delivery requires its FREE purpose
```

No extra AI entitlement.

**PASS**

---

## 20. Falsification — BUSINESS booking setup

Merchant says:

```text
"Let customers book 30-minute consultations."
```

Expected:

assistance may guide configuration.

Any protected new Appointment/Booking activity still requires BUSINESS permissions.

**PASS**

---

## 21. Falsification — FREE merchant asks for campaign execution

Merchant says:

```text
"Email this promotion to all my customers."
```

Expected:

assistant may explain that the requested operation is unavailable under current commercial permission.

It cannot execute the GROWTH Marketing operation.

**PASS**

---

## 22. Falsification — AI outage

AI provider is unavailable.

Merchant manually edits:

```text
Product description
Business Hours
Listing price
```

where otherwise authorised.

Expected:

ordinary deterministic paths continue.

**PASS**

---

## 23. Falsification — model provider change

GrandRue switches AI provider.

Expected:

no commercial entitlement identity migration.

No merchant-plan change.

**PASS**

---

## 24. Falsification — sensitive observation

Staff member asks the assistant for data the actor cannot independently access.

Expected:

assistant cannot disclose it.

Commercial assistance availability does not manufacture Actor Authorisation.

**PASS**

---

## 25. Rejected alternative — `USE_AI`

Rejected.

It is too broad and would make internal implementation technology a merchant commercial abstraction.

---

## 26. Rejected alternative — GROWTH-only assistant

Rejected.

It conflicts with the accepted FREE presence-and-website-assistance proposition and would make basic setup depend on GROWTH.

---

## 27. Rejected alternative — one AI entitlement per responsibility

Rejected.

This would expose GrandRue's internal assistance decomposition as commercial product complexity.

---

## 28. Rejected alternative — assistant inherits all permissions

Rejected.

Assistance cannot grant permissions possessed by neither the merchant nor the underlying operation.

---

## 29. Hard invariants

1. Exact access contract is `merchant-assistance/supporting-interaction-access@1`.
2. Target family is `PLATFORM_SERVICE_ACCESS`.
3. It requires no independent Commercial Entitlement.
4. No `CommercialEntitlementIdentity` is minted.
5. The contract is bounded to the accepted MS-PROT-057 v1.2 responsibility portfolio.
6. A future responsibility is not automatically admitted.
7. Assistance does not grant underlying semantic applicability.
8. Assistance does not grant Actor Authorisation.
9. Assistance does not grant underlying Commercial permission.
10. Assistance does not grant Provider Readiness.
11. Assistance does not grant Exposure.
12. Assistance does not create business truth.
13. Assistance does not establish merchant intent where material meaning was inferred without required confirmation.
14. Underlying source operations retain their exact commercial classifications.
15. AI/provider choice is not commercial identity.
16. AI unavailability does not block ordinary deterministic operations.
17. Resource Protection remains independent.
18. Future quantitative allowances remain under DQ-003.
19. No implementation is activated.

---

## 30. DQ-001 audit consequence

Upon acceptance, the final completeness audit finds **no remaining owner/supporting commercial-classification gap in the currently admitted standard portfolio**.

The remaining DQ-001 work becomes exactly:

```text
mint concrete CommercialEntitlementIdentity values
+
construct exact Commercial Access Bindings
+
record conditional/supporting requirements
+
construct explicit FREE snapshot
+
construct explicit BUSINESS snapshot
+
construct explicit GROWTH snapshot
+
prove FREE ⊆ BUSINESS ⊆ GROWTH
+
retain approval provenance
```

That work belongs in:

```text
MS-PROT-056 v1.10
Initial Standard Commercial Catalogue Manifest
```

---

## 31. Reservations remain outside the manifest

Acceptance does not admit these unresolved reservations:

```text
baseline discoverability services
presence-performance analytics
supported taxation calculation/filing
additional forecasting/scenario services
additional analytical optimisation
agentic CRM
```

Nor does it automatically add later semantic capabilities such as:

```text
Waitlist
Review / Reputation
```

without separate Commercial admission.

---

## 32. Governance recommendation

**Fundamental Vision Conformance:** `VISION-CONFORMING`  
**FREE assistance conformity:** PASS  
**Underlying-authority preservation:** PASS  
**AI non-authority:** PASS  
**No premium obstacle:** PASS  
**Provider neutrality:** PASS  
**Commercial simplification:** PASS  
**Resource Protection separation:** PASS  
**Cross-domain falsification:** PASS  
**DQ-001 completeness after acceptance:** PASS  
**Ambiguity review:** PASS

```text
RECOMMENDATION: ACCEPT
```

**Manual approval:** GRANTED — 19 September 2026

---

## 33. Acceptance statement

> **GrandRue shall classify the currently accepted merchant-assistance responsibility portfolio through `merchant-assistance/supporting-interaction-access@1` as a `PLATFORM_SERVICE_ACCESS` contract requiring no independent Commercial Entitlement. Merchant Assistance may interpret, explain, prepare and coordinate only within the independently applicable authority of the underlying business purpose and shall not manufacture semantic applicability, Actor Authorisation, Commercial permission, Provider Readiness, Exposure, merchant intent or business truth. This classification fulfils the accepted FREE presence-and-website-assistance proposition without creating a generic `USE_AI` entitlement, preserves deterministic non-AI operation, remains provider-neutral, permits independent Resource Protection, and closes the final owner/supporting commercial-classification gap required before the initial standard Commercial Catalogue Manifest.**
