# MS-PROT-085 v1.4 — Customer Communication Human Response Commercial Access Amendment

**Document ID:** MS-PROT-085  
**Version:** 1.4  
**Status:** ACCEPTED  
**Approved:** 16 September 2026 — explicit manual approval of the complete proposal  
**Authority type:** Merchant Attention commercial-access classification  
**Governed by:** DESIGN-RULES v2.3; DOCUMENT-GOVERNANCE v2.3; MS-FUNDAMENTAL-VISION-001  
**Amends:** MS-PROT-085 v1.2 within commercial-access classification only  
**Depends on:** Composite MS-PROT-085 through v1.3; composite MS-PROT-086 through v1.3; MS-PROT-056 v1.7 §10 and v1.9 §§4–6; composite MS-PROT-062; composite MS-PROT-065  
**Implementation activation:** NONE  
**Purpose:** Resolve the commercial-access treatment of the Customer Communication human-response Attention path without introducing an additional Attention paywall, granting Customer Communication access, or pre-empting the exact commercial contracts still to be classified under MS-PROT-086.

---

# 1. Governing Decision

For exactly:

```text
customer-communication/human-response-required@1
```

Merchant Attention SHALL impose **NO INDEPENDENT COMMERCIAL ENTITLEMENT**.

The commercial treatment is:

| Activity | Commercial requirement |
|---|---|
| Authoritative Customer Communication contribution to the Attention path | No independent Attention entitlement |
| Occurrence establishment and idempotent recovery | No independent Attention entitlement |
| Merchant observation of the Attention occurrence | Current independently valid Customer Communication permission to observe the exact triggering Conversation and Message; no additional Attention entitlement |
| Attention acknowledgement, if later admitted for this family | No independent Attention entitlement, but every independently applicable source and actor requirement remains |
| `HUMAN_RESPONSE_ACCEPTED` | Current independently valid Customer Communication authority and commercial permission for the outbound Message operation; no additional Attention entitlement |
| `HANDLED_OUTSIDE_MAIN_STREET_RECORDED` | Current independently valid observation of the triggering Customer Communication material plus eligible-actor authority; no additional Attention entitlement |
| `Reply in Conversation` Candidate Action | No permission is created by Attention; execution remains entirely governed by Customer Communication |

This amendment SHALL NOT allocate Customer Communication itself to FREE.

It SHALL NOT establish an executable BUSINESS entitlement identity for Customer Communication.

It SHALL NOT substitute a tier-name comparison for the exact Customer Communication commercial binding still required by `MS-PROT-056-V17-DQ-001`.

---

# 2. Scope and Preserved Meaning

This amendment supplies the commercial classification intentionally absent from MS-PROT-085 v1.2 and expressly outside MS-PROT-085 v1.3.

It does not change:

```text
customer-communication/human-response-required@1
```

its activation criteria, occurrence identity, satisfaction paths, Candidate Action, human-handoff semantics, handling evidence, Notification boundary, data lifecycle or owner relationships.

In particular:

```text
Customer Communication
    owns Conversation, Message and response-requirement truth

Merchant Attention
    owns human-handling coordination

Notification
    owns external delivery evidence

source business capability
    owns any underlying business decision or mutation
```

Commercial classification SHALL NOT collapse those ownership boundaries.

---

# 3. Supporting-Service Classification

The Customer Communication human-response Attention path is a **supporting service** within the commercial meaning of MS-PROT-056 v1.7 §10.

A merchant who is independently permitted to use an applicable Customer Communication service SHALL NOT require another commercial purchase merely because that communication requires human handling.

Canonical:

```text
authorised Customer Communication
        ↓
human response required
        ↓
Merchant Attention coordination
```

SHALL NOT become:

```text
authorised Customer Communication
        ↓
additional paid Attention entitlement
        ↓
human response
```

The latter is prohibited because it would make necessary coordination a second architectural toll on an included workflow.

No independent Attention entitlement means exactly **no additional commercial prerequisite owned by Merchant Attention**.

It does not mean unrestricted Customer Communication access.

---

# 4. No New Commercial Entitlement Identity

This amendment SHALL NOT mint a `CommercialEntitlementIdentity` for the Attention path.

It SHALL NOT introduce a synthetic contract such as:

```text
customer-communication-human-response-attention entitlement
```

merely to represent the absence of an independent charge.

The final Commercial Catalogue Manifest SHALL instead preserve this explicit supporting-access classification and bind the protected Customer Communication service through the exact Customer Communication contracts selected by its owning authority.

Absence of an Attention entitlement SHALL NOT be represented as an implicit FREE grant.

---

# 5. Customer Communication Commercial Boundary

MS-PROT-056 v1.7 places:

```text
Human customer communication
```

and:

```text
Routine factual customer-service responses
```

within BUSINESS.

This amendment does not alter that allocation.

Therefore:

```text
NO INDEPENDENT ATTENTION ENTITLEMENT
```

does not imply:

```text
FREE Customer Communication
FREE Conversation observation
FREE merchant Message sending
FREE automated customer-service responses
```

The Customer Communication owner must still define the exact commercial access contracts, protected purposes and bindings needed by the complete catalogue.

Until those exact bindings are accepted, missing commercial information SHALL fail closed for catalogue admission rather than being interpreted as unrestricted access.

---

# 6. Post-Assessment Establishment and Recovery

Where Customer Communication has authoritatively established an exact qualifying response assessment under MS-PROT-086 and contributed the resulting human-response requirement, Merchant Attention may establish or recover the corresponding occurrence without requiring another Commercial Entitlement.

The progression remains subject to the accepted MS-PROT-085 requirements for:

```text
exact Merchant Scope
exact Attention contract identity/version
Customer Communication source ownership
triggering ConversationMessage identity
exact response-assessment activation
stable occurrence identity
trusted execution
required protection and data-use authority
```

A caller-supplied Message identifier, Conversation identifier, tier label or claimed former entitlement SHALL NOT substitute for authoritative source evidence.

A commercial-state change after the qualifying Customer Communication assessment SHALL NOT cause Attention to erase, rewrite or duplicate the already-qualified occurrence.

This progression grants no merchant-facing observation or response permission.

---

# 7. Merchant Observation

Merchant observation of the Attention occurrence SHALL require current independent authority to observe the exact triggering Customer Communication material.

The observation decision must preserve the same exact:

```text
Merchant Scope
Conversation
triggering ConversationMessage
applicable participant/source relationship
current Actor Authorisation
Projection/Exposure
data-use authority
commercial access requirement
```

required by Customer Communication.

Merchant Attention SHALL NOT independently reconstruct or weaken those conditions.

An occurrence identifier does not disclose the protected Conversation.

Historical Attention existence does not create permanent Conversation access.

If Customer Communication denies observation, Merchant Attention SHALL NOT expose the protected Message merely because a handling occurrence exists.

---

# 8. `HUMAN_RESPONSE_ACCEPTED`

`HUMAN_RESPONSE_ACCEPTED` remains dependent upon acceptance of an outbound human `ConversationMessage` by Customer Communication.

Therefore the sequence remains:

```text
eligible human instruction
        ↓
Customer Communication performs
its independently authorised
outbound Message operation
        ↓
durable ConversationMessage accepted
        ↓
Merchant Attention reacts
        ↓
HUMAN_RESPONSE_ACCEPTED
```

The outbound Message operation SHALL satisfy whatever exact current Customer Communication commercial access requirement governs that operation.

Merchant Attention SHALL impose no additional commercial entitlement.

Attention SHALL NOT:

- manufacture Message-send permission;
- infer BUSINESS access from occurrence existence;
- bypass a Customer Communication commercial denial;
- send a Message itself;
- treat a Candidate Action as execution authority; or
- preserve response authority merely because the actor previously participated.

Failure of the Attention reaction after Message acceptance SHALL NOT roll back the accepted Message.

Recovery of the Attention reaction remains supporting progression, not a second commercial use of Customer Communication.

---

# 9. `HANDLED_OUTSIDE_MAIN_STREET_RECORDED`

`HANDLED_OUTSIDE_MAIN_STREET_RECORDED` remains an Attention-owned handling fact.

It does not create a ConversationMessage and does not claim that GrandRue observed the external communication.

The operation SHALL require:

```text
eligible merchant-side actor
+
current authority to observe the exact
triggering Customer Communication material
+
current authority to perform the
Attention handling operation
+
exact occurrence affinity
```

No separate Attention Commercial Entitlement is required.

The operation SHALL NOT become a loophole by which a merchant lacking current access to protected Customer Communication material can discover that material or reconstruct its contents.

A commercial downgrade SHALL NOT itself manufacture this disposition.

---

# 10. Candidate Action Boundary

The optional Candidate Action equivalent to:

```text
Reply in Conversation
```

remains a reference to a Customer Communication operation.

Attention may expose that action only where the applicable presentation and access requirements permit it.

Selecting the Candidate Action SHALL invoke the independently governed Customer Communication operation.

Attention SHALL NOT pre-authorise:

```text
Conversation access
Message acceptance
customer identity
source-business access
refund
cancellation
booking amendment
order amendment
payment action
```

or any other source-capability operation.

---

# 11. Automated Customer-Service Handoff

MS-PROT-086 v1.3 may produce human-response handling when automatic response is unsafe or inappropriate.

This amendment ensures that the required escape path is not commercially fragmented.

For example:

```text
routine request cannot safely be automated
        ↓
Customer Communication establishes
HUMAN_RESPONSE_REQUIRED
        ↓
Attention occurrence established
        ↓
authorised merchant handles
```

SHALL NOT require purchasing a second Attention feature.

However, the handoff does not expand the commercial scope of Customer Communication.

A service not commercially authorised does not become authorised merely because its failed or human-required path uses Merchant Attention.

---

# 12. Commercial Change and Historical Preservation

Loss or change of a Customer Communication commercial grant SHALL NOT:

```text
delete Conversation history
delete Message history
delete Attention occurrences
rewrite response assessments
manufacture handling
manufacture NO_LONGER_APPLICABLE
erase prior handling evidence
```

Post-assessment durable Attention progression may continue under Section 6 where its authoritative source qualification already exists.

Merchant-facing observation and new communication operations remain separately evaluated against current Customer Communication and residual-access authority.

Where current observation is not authorised, the merchant-facing Attention representation SHALL fail closed without claiming that no outstanding communication exists.

This amendment creates no new residual-access category.

---

# 13. Commercial Catalogue Consequence

For `MS-PROT-056-V17-DQ-001`, this amendment contributes exactly the following owner/supporting-service classification:

```text
customer-communication/human-response-required@1
    → supporting Merchant Attention path
    → NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

The final Commercial Catalogue Manifest SHALL NOT mint a plan grant solely for this supporting Attention path.

The manifest must instead resolve the exact Customer Communication protected-service bindings governing the underlying observation and Message operations.

The classification SHALL be represented explicitly.

It SHALL NOT be inferred merely because no Attention entitlement appears in a grant set.

This amendment therefore resolves one supporting-service classification only.

It does not make the Customer Communication portfolio catalogue-complete.

---

# 14. Failure and Recovery Honesty

The following outcomes SHALL remain distinguishable:

| Condition | Required result |
|---|---|
| Attention establishment cannot validate source evidence | Source/qualification failure |
| Customer Communication commercial permission is denied | Commercial access denial at the applicable source operation |
| Commercial decision cannot be established technically | Commercial evaluation unavailable, not authoritative denial |
| Actor authority has ended | Actor-authorisation rejection |
| Conversation or Message is unavailable under current Exposure | Source-observation denial/unavailability |
| Message acceptance commits but Attention reaction fails | Message preserved; Attention reaction may retry |
| Same logical Attention operation is retried | Existing result recovered/converged |
| Notification delivery fails | Delivery failure remains Notification-owned |

No distributed transaction across Commercial, Customer Communication, Merchant Attention or Notification is introduced.

---

# 15. Provider and AI Boundaries

No AI provider or messaging provider acquires Commercial authority through this amendment.

AI SHALL NOT:

```text
grant Customer Communication access
grant Attention access
satisfy handling
manufacture actor authority
convert missing binding information into permission
```

Provider success SHALL NOT establish Commercial permission.

Provider failure SHALL NOT create a new Attention entitlement requirement.

Human-response coordination must remain available according to the accepted underlying workflow even when optional AI automation is unavailable.

---

# 16. Falsification

The proposed classification has been tested against the following design scenarios.

| Challenge | Required result |
|---|---|
| BUSINESS merchant receives a complaint requiring a person | Human-response Attention occurrence can establish without another paid Attention entitlement |
| Routine automated answer cannot safely complete | Human handoff does not require purchase of an additional Attention product |
| FREE merchant lacks the applicable Customer Communication commercial service | No-independent-Attention classification does not grant Conversation or Message access |
| Customer Communication assessment commits, then commercial state changes before Attention recovery | Qualified occurrence recovery may converge; merchant-facing access remains separately evaluated |
| Merchant can observe the Conversation but lacks authority to send | Occurrence may be observable where otherwise authorised; `HUMAN_RESPONSE_ACCEPTED` cannot be manufactured |
| Merchant records that the matter was handled by telephone | `HANDLED_OUTSIDE_MAIN_STREET_RECORDED` may commit only with current source observation and eligible-actor authority |
| Worker can see an Attention identifier but not the source Message | Protected Message remains hidden |
| Candidate Action is displayed | It does not grant Message-send authority |
| Customer asks for a refund | Human handoff creates no refund authority |
| Same response assessment is delivered repeatedly | One logical Attention occurrence |
| Outbound Message commits and Attention update fails | Message remains; Attention convergence retries |
| Notification provider fails | Handling and Message truth remain distinct from delivery |
| Catalogue omits the future Customer Communication binding | Missing information is not interpreted as FREE/unrestricted access |
| Runtime attempts `if plan == BUSINESS` inside Attention | Rejected; exact owner-qualified Commercial access remains required |

**Falsification result:** PASS.

---

# 17. Alternatives Considered

## 17.1 Separate Attention Entitlement

**Rejected.**

A new paid Attention entitlement would charge twice for a necessary human-handoff step in an already-authorised Customer Communication workflow and conflict with MS-PROT-056 v1.7 §10.

## 17.2 Direct BUSINESS Check in Merchant Attention

**Rejected.**

Although human Customer Communication is allocated to BUSINESS, Merchant Attention must not infer runtime permission from a tier label.

Exact Customer Communication bindings remain Commercial- and owner-qualified catalogue work.

## 17.3 Treat No Independent Attention Entitlement as FREE Customer Communication

**Rejected.**

Supporting-service classification does not reclassify the protected service being supported.

This would contradict the accepted BUSINESS allocation and undermine exact catalogue binding.

## 17.4 Permanent Access from Historical Occurrence Existence

**Rejected.**

Attention history is not Conversation access authority.

Current access and any residual access remain independently governed.

## 17.5 Require a Fresh Communication Entitlement for Attention Recovery Itself

**Rejected.**

Where the qualifying Customer Communication assessment already committed, requiring a fresh paid grant merely to establish or recover the durable supporting Attention occurrence would conflate committed source progression with a new communication service use.

New merchant observation or Message activity remains separately gated.

## 17.6 Selected Alternative

**Selected:** explicit no-independent-entitlement classification for the exact Customer Communication human-response Attention family, combined with strict delegation to Customer Communication for protected observation and response permission.

---

# 18. Hard Invariants

1. `customer-communication/human-response-required@1` acquires no independent Commercial Entitlement.
2. No new Commercial Entitlement identity is minted solely for this Attention path.
3. No-independent-entitlement does not mean FREE Customer Communication.
4. MS-PROT-056 v1.7’s BUSINESS allocation for human customer communication remains unchanged.
5. Merchant Attention does not define the Customer Communication commercial entitlement.
6. Merchant Attention does not use plan-name checks as runtime authority.
7. Missing Customer Communication binding information is not permission.
8. Authoritative post-assessment Attention establishment and recovery do not require a second commercial purchase.
9. Post-assessment progression does not grant merchant observation.
10. Merchant observation requires independently valid access to the exact source Conversation and Message.
11. Historical occurrence existence does not grant permanent source access.
12. `HUMAN_RESPONSE_ACCEPTED` requires accepted outbound Customer Communication evidence.
13. Attention cannot bypass commercial or actor denial on the outbound Message operation.
14. `HANDLED_OUTSIDE_MAIN_STREET_RECORDED` does not create a Message.
15. External handling does not disclose source material to an unauthorised actor.
16. A Candidate Action is not execution authority.
17. Attention grants no refund, cancellation, order, booking, payment or other business-operation authority.
18. Customer Communication remains owner of the response-requirement decision.
19. Notification remains owner of external delivery evidence.
20. AI and providers grant no Commercial authority.
21. Commercial changes do not rewrite historical Conversation, Message, assessment or Attention facts.
22. This amendment creates no new residual-access category.
23. The final Commercial Catalogue Manifest must explicitly preserve this supporting classification.
24. Customer Communication’s exact protected-service bindings remain separate catalogue work.
25. `MS-PROT-056-V17-DQ-001` remains OPEN.
26. Acceptance creates no production or implementation activation.

---

# 19. DQ-001 Effect

Upon acceptance and formalisation, `MS-PROT-056-V17-DQ-001` SHALL remain **OPEN**.

The completed Merchant Attention commercial-classification set would then include:

```text
enquiry/initial-submission-review@1
    → no independent Attention entitlement
    → merchant access delegated to exact Enquiry observation permission

customer-communication/human-response-required@1
    → no independent Attention entitlement
    → merchant access delegated to exact Customer Communication access permission
```

This amendment closes the Merchant Attention supporting-commercial classification for the currently accepted two-family Attention portfolio.

It does not supply:

```text
Customer Communication final entitlement identities
Customer Communication exact Commercial Access Bindings
FREE/BUSINESS/GROWTH final grant sets
complete Commercial Catalogue Manifest
cross-binding publication validation
manifest approval
```

Those remain under DQ-001.

No MS-PROT-085 deferred question is reopened.

---

# 20. Fundamental Vision Conformance

**VISION-CONFORMING WITH JUSTIFIED COMPLEXITY.**

The merchant-facing outcome remains simple:

```text
A customer needs a person.
The matter appears for the merchant to handle.
```

The merchant is not required to understand or purchase a second queue, Attention, handoff or workflow product merely because GrandRue internally separates communication truth from handling coordination.

The internal commercial and authority distinctions are justified because they prevent the convenience surface from manufacturing access to protected conversations or business operations.

---

# 21. Governance Outcome

**Architecture review:** PASS.

**Commercial-boundary review:** PASS.

**Supporting-service review:** PASS.

**Authority-separation review:** PASS.

**Falsification review:** PASS.

**Ambiguity review:** PASS.

The proposal distinguishes:

```text
no independent Attention entitlement
    ≠
FREE Customer Communication
    ≠
Customer Communication commercial permission
    ≠
Actor Authorisation
    ≠
Conversation access
    ≠
Message-send authority
    ≠
source-business authority
```

**RECOMMENDATION:** ACCEPT.

**Manual approval:** GRANTED — 16 September 2026.

**Repository formalisation:** COMPLETE FOR THIS AUTHORITY DOCUMENT.

**Implementation promotion:** NONE.

Acceptance authorises this exact commercial classification only. It does not authorise Customer Communication catalogue completion, executable plan grants or production implementation.
