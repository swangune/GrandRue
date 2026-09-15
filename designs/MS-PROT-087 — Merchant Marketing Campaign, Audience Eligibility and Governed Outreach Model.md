# MS-PROT-087 — Merchant Marketing Campaign, Audience Eligibility and Governed Outreach Model

**Document ID:** MS-PROT-087  
**Version:** 1.0  
**Status:** **ACCEPTED by manual approval on 8 September 2026**  
**Approved:** Explicit manual approval on 8 September 2026 after Fundamental Vision Conformance, review, two-stage falsification, targeted ambiguity correction, corpus-conformance review and recommendation  
**Governed by:** `designs/DESIGN-RULES.md`  
**Fundamental authority:** MS-FUNDAMENTAL-VISION-001  
**Depends on:** MS-PROT-026, 027, 043, 044, 046, 047, 048, 053, 056, 057, 059, 062, 063, 064, 065, 067, 069, 070, 072, 073, 075, 083, 085 and 086  
**Addresses:** The deferred product semantics identified for `marketing-and-campaign-engine.md`  
**Purpose:** Define bounded merchant marketing campaigns, audience definitions, recipient eligibility, approval, scheduling, execution handoff and outcome evidence without creating a CRM, advertising platform, consent authority, notification subsystem or autonomous AI marketer.

---

# 1. Decision Summary

Main Street SHALL support merchant-authorised, purpose-bound marketing campaigns that can coordinate accepted business facts into governed public or direct outreach.

Marketing owns:

- campaign purpose;
- exact campaign revision;
- campaign content composition;
- audience-definition semantics;
- campaign-specific recipient eligibility;
- campaign approval and activation authority;
- campaign scheduling meaning;
- campaign occurrence/run evidence;
- campaign-specific suppression decisions; and
- campaign outcome evidence before analytical interpretation.

Marketing does not own:

- CustomerContext or customer identity;
- marketing consent or lawful-basis truth;
- contact endpoints;
- Notification dispatch or delivery truth;
- Publication or Announcement truth;
- Conversation continuity;
- offers, prices, discounts, inventory or bookings;
- Business Recommendations;
- analytical attribution or causal claims;
- provider connection/readiness;
- Merchant Attention handling;
- payment or advertising-spend authority; or
- source business facts used for targeting.

Canonical:

```text
accepted merchant campaign purpose
        +
approved exact campaign revision
        +
registered audience definition
        +
current recipient eligibility
        ↓
Campaign Occurrence
        ↓
Publication instruction under MS-PROT-046
and/or
Notification Intent under MS-PROT-075
```

---

# 2. Problem

The lower-authority Marketing and Campaign PRD correctly identifies a need for simple retention, announcement, promotion and re-engagement activity.

It does not establish sufficient authority for:

- what a Campaign is;
- how Campaign differs from Publication or Notification;
- who is eligible to receive direct marketing;
- how consent, preference and suppression interact;
- how scheduled campaigns revalidate changing conditions;
- how duplicate recipients are prevented;
- how AI may assist without selecting or contacting customers autonomously;
- how provider uncertainty affects retry;
- how content changes affect approval;
- how campaign evidence becomes analytical evidence; or
- how Main Street avoids ungrounded conversion and revenue-attribution claims.

Without a bounded authority, implementation could invent materially inconsistent semantics.

---

# 3. Fundamental Vision Conformance

**Outcome:** `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`

The capability satisfies:

- the **Representation Test**, because target merchants legitimately communicate offers, news and reasons to return;
- the **Coordination Test**, because outreach may depend on accepted customer, offering, inventory, booking, availability and publication facts; and
- the **Administrative-Compression Test**, because Main Street can safely absorb recipient revalidation, suppression, deduplication, scheduling and provider coordination.

The additional permission, currentness and evidence machinery is justified by privacy, customer trust, provider uncertainty and irreversible external communication.

Main Street SHALL absorb that complexity internally.

The merchant-facing experience SHOULD remain equivalent to:

```text
What would you like customers to know?
Who is it relevant to?
When should it go out?
Review and approve.
```

Merchants and ordinary staff MUST NOT be required to administer:

- workflow graphs;
- provider-specific campaign objects;
- identity-resolution rules;
- consent-state machines;
- arbitrary query languages;
- attribution models; or
- enterprise marketing funnels.

---

# 4. Scope

MS-PROT-087 governs:

- merchant marketing Campaigns;
- immutable Campaign Revisions;
- governed Audience Definitions;
- candidate-recipient derivation;
- campaign-specific Recipient Eligibility Assessments;
- exact merchant approval;
- bounded automated activation;
- Campaign Occurrences;
- scheduling and recurrence meaning;
- suppression and current-state revalidation;
- public and direct-outreach handoff;
- campaign outcome evidence;
- provider-neutral execution composition; and
- campaign history.

It supports bounded purposes such as:

- merchant announcements;
- new or returning product/service awareness;
- seasonal communication;
- customer appreciation;
- re-engagement;
- event promotion;
- accepted offer communication; and
- review or feedback requests where separately permitted.

---

# 5. Explicit Non-Goals

MS-PROT-087 does not establish:

- a universal CRM;
- customer lifetime-value authority;
- automatic “VIP” or “high-value customer” classifications;
- arbitrary behavioural profiling;
- a generic customer data platform;
- loyalty-points or membership-programme semantics;
- pricing, discount or offer authority;
- advertising auctions;
- advertising budgets or spend;
- external advertising-platform optimisation;
- lead scoring;
- sales pipelines;
- multi-stage marketing funnels;
- generic workflow automation;
- autonomous AI campaign execution;
- statutory marketing-law interpretation;
- complete customer-history claims; or
- causal campaign attribution.

---

# 6. Ownership Boundaries

| Meaning | Authoritative owner |
|---|---|
| Campaign purpose, revision, audience and campaign eligibility | Marketing / MS-PROT-087 |
| CustomerContext and customer reconciliation | MS-PROT-043 |
| Offering, product, service, price or promotion source truth | Applicable source owner, including MS-PROT-044 |
| Public content and Announcement publication | MS-PROT-046 |
| Notification intent, channel resolution and delivery evidence | MS-PROT-075 |
| Conversation and inbound reply continuity | MS-PROT-086 |
| Marketing permission, protected-data use and retention authority | MS-PROT-053 plus applicable jurisdiction/regulatory authority |
| Scheduling and durable background work | MS-PROT-065 |
| AI assistance | MS-PROT-057 |
| Analytical measures, recommendations and attribution | MS-PROT-083 |
| Provider fulfilment/readiness | MS-PROT-048 and related provider authorities |
| External-effect uncertainty and reconciliation | MS-PROT-069 |
| Merchant Attention | MS-PROT-085 |
| Entitlement and eligibility | MS-PROT-056 |
| Audit and intervention evidence | MS-PROT-064 |

---

# 7. Hard Distinctions

```text
Campaign
    ≠ Publication
    ≠ Announcement
    ≠ Notification
    ≠ Conversation
    ≠ CustomerContext
    ≠ Business Recommendation
    ≠ provider campaign object
```

```text
customer relationship
    ≠ marketing permission
```

```text
contact endpoint
    ≠ recipient identity
    ≠ authority to contact
```

```text
Notification Preference
    ≠ marketing consent
```

```text
campaign approval
    ≠ provider delivery
    ≠ customer engagement
    ≠ commercial conversion
```

```text
purchase after campaign
    ≠ purchase caused by campaign
```

---

# 8. Marketing Campaign

A **Marketing Campaign** is a Merchant-scoped definition of one coherent marketing purpose whose execution occurs only through approved Campaign Revisions and Campaign Occurrences.

Conceptually:

```text
MarketingCampaign
{
    campaignIdentity
    MerchantScope
    campaignPurpose
    businessSubjectReferences
        where applicable
    permittedOutreachFamilies
    createdBy
    createdAt
    provenance
}
```

Campaign identity SHALL remain stable across revisions.

A Campaign does not itself prove:

- that content is approved;
- that any recipient is eligible;
- that communication is currently permitted;
- that an offer remains valid;
- that a scheduled occurrence will execute; or
- that external delivery occurred.

---

# 9. Campaign Revision

A **Campaign Revision** is an immutable version of the exact campaign meaning proposed for approval or execution.

Conceptually:

```text
CampaignRevision
{
    campaignRevisionIdentity
    campaignIdentity
    revisionNumber

    purposeStatement
    contentPackage
    AudienceDefinitionReference
    outreachFamilySet
    sourceBusinessReferences
    scheduleSemantics
    personalisationSemantics
    suppressionSemantics

    authoredBy
    createdAt
    provenance
}
```

Material revision includes change to:

- recipient audience;
- purpose;
- message meaning;
- call to action;
- referenced offer;
- publication destination;
- direct channel family;
- personalisation;
- schedule or recurrence;
- suppression behaviour; or
- other externally material content.

An accepted Campaign Revision SHALL NOT be destructively edited.

---

# 10. Content Package

A Campaign Revision SHALL identify the exact content or content-generation contract intended for each outreach family.

Content may include:

- subject/title;
- body;
- call to action;
- referenced images or media;
- destination link;
- source business references;
- required legal or merchant identity content; and
- bounded personalisation fields.

Campaign content MUST NOT manufacture source truth.

Canonical:

```text
campaign says:
"20% off the accepted Spring Service Offer"

        requires

an applicable accepted offer/price authority
```

Rejected:

```text
AI writes "20% off"
        ↓
discount becomes commercially valid
```

Marketing communicates accepted business meaning. It does not create pricing, contractual or fulfilment authority through copy.

---

# 11. Audience Definition

An **Audience Definition** is a Merchant-scoped, purpose-bound, versioned definition of the evidence required to identify candidate recipients.

Conceptually:

```text
AudienceDefinition
{
    audienceDefinitionIdentity
    version
    MerchantScope
    campaignPurposeFamily

    permittedSourceFacts
    inclusionSemantics
    exclusionSemantics
    identityAndDeduplicationRequirements
    minimumEvidenceRequirements
    unresolvedOutcomeSemantics

    approvedBy
    approvedAt
    provenance
}
```

Audience Definitions SHALL use accepted, explainable and registered semantics.

Examples may include:

```text
customers with an accepted completed booking
within the preceding defined period
```

```text
customers associated with an accepted purchase
of an applicable product family
```

```text
customers for whom an accepted relationship fact
shows no qualifying activity during a defined period
```

Descriptions such as:

- “best customers”;
- “likely spenders”;
- “people like these customers”;
- “probably affluent”;
- “seems interested”; or
- “AI-selected audience”

are insufficient unless resolved into accepted governed semantics.

---

# 12. Audience Definition Is Not a Stored Customer Label

An Audience Definition evaluates applicable current or historical evidence for one purpose.

It MUST NOT silently create permanent CustomerContext classifications.

Canonical:

```text
eligible for Campaign X at evaluation time
    ≠ permanently an "inactive customer"
```

Where a durable classification is independently required, its owning capability must establish it separately.

---

# 13. Sensitive and Proxy Targeting

Audience selection MUST NOT use sensitive information or material proxy inference merely because the data exists or AI can infer it.

Examples requiring separately accepted authority include targeting based on actual or inferred:

- health;
- disability;
- religion;
- ethnicity;
- sexual orientation;
- political opinion;
- financial distress;
- precise private location;
- vulnerability;
- children; or
- another specially protected or materially sensitive condition.

AI confidence does not make such targeting acceptable.

Where the permissibility of a targeting attribute cannot be established:

```text
audience attribute use = PROHIBITED / UNRESOLVED
```

---

# 14. Candidate Recipient

A **Candidate Recipient** is a subject considered under an Audience Definition.

Candidate status does not grant communication permission.

```text
matches audience criteria
    ≠ eligible to receive campaign
```

Candidate derivation SHALL preserve:

- Merchant Scope;
- Audience Definition version;
- source-evidence references;
- evaluation time;
- data-coverage qualification;
- recipient/identity basis; and
- provenance.

---

# 15. Marketing Recipient Eligibility Assessment

A **Marketing Recipient Eligibility Assessment** decides whether one candidate may participate in one exact Campaign Occurrence through one exact outreach family.

Conceptually:

```text
MarketingRecipientEligibilityAssessment
{
    assessmentIdentity
    MerchantScope
    campaignOccurrenceIdentity
    campaignRevisionIdentity
    candidateRecipientReference
    intendedEndpointOrPublicationScope
    outreachFamily

    audienceResult
    permissionAuthorityResult
    preferenceResult
    suppressionResult
    exposureResult
    entitlementResult
    sourceCurrentnessResult
    identityAndDeduplicationResult

    outcome
        ELIGIBLE
        INELIGIBLE
        UNRESOLVED

    evaluatedAt
    evidenceReferences
    provenance
}
```

Only `ELIGIBLE` may proceed toward externalisation.

`UNRESOLVED` MUST fail closed for that recipient/outreach path.

---

# 16. Permission and Lawful Communication Boundary

MS-PROT-087 does not create statutory, regulatory or lawful-basis truth.

Campaign eligibility SHALL consume applicable accepted authority establishing whether the intended:

- merchant;
- purpose;
- recipient;
- channel;
- content class;
- jurisdiction;
- timing; and
- use of protected information

are permitted.

A historical permission record does not automatically establish current permission.

Where the applicable authority cannot establish permission:

```text
direct marketing eligibility = UNRESOLVED
```

---

# 17. Relationship and Contact Do Not Establish Permission

Main Street MUST NOT infer direct-marketing permission solely because:

- a person submitted an Enquiry;
- a CustomerContext exists;
- a purchase or booking occurred;
- an email address or telephone number is known;
- a person follows the merchant;
- the merchant imported a list;
- the merchant has previously contacted the person;
- a customer replied to an operational communication; or
- AI predicts that contact would be welcome.

A contact list without sufficient source, scope and permission provenance MUST NOT become an eligible marketing audience.

---

# 18. Transactional-Label Bypass Is Prohibited

A merchant, worker, template or AI system MUST NOT bypass marketing restrictions by describing promotional content as:

- transactional;
- operational;
- a reminder;
- customer service;
- an order update; or
- another non-marketing classification.

Communication classification SHALL follow its actual meaning and purpose.

Mixed content SHALL satisfy every applicable authority or be separated into independently governed communications.

---

# 19. Preference and Suppression

Notification Preference may rank or constrain an otherwise permitted channel.

It does not create marketing permission.

A **Campaign Suppression Result** represents campaign-side evidence that a candidate must not proceed.

Suppression may arise from:

- applicable opt-out;
- invalid or withdrawn permission;
- campaign-frequency policy;
- duplicate-recipient protection;
- merchant exclusion;
- source-state change;
- invalid endpoint;
- unresolved identity;
- protection or abuse controls;
- provider restriction; or
- another accepted campaign rule.

Suppression scope SHALL remain exact.

Canonical:

```text
marketing email opt-out
    ≠ suppression of required security notice
```

```text
Campaign X exclusion
    ≠ universal deletion of CustomerContext
```

---

# 20. Current-State Revalidation

Audience inclusion at scheduling time does not reserve future authority to communicate.

Immediately before externalisation, Main Street SHALL revalidate every mutable condition required by the Campaign Revision and applicable authorities.

This includes, where relevant:

- current permission;
- current preference;
- current suppression;
- endpoint validity;
- current Audience Definition applicability;
- current source-business applicability;
- current entitlement;
- current provider readiness;
- current Exposure/access authority; and
- campaign pause or cancellation.

Canonical:

```text
Monday: recipient eligible
Tuesday: recipient opts out
Wednesday: campaign becomes due
        ↓
current eligibility = INELIGIBLE
        ↓
no direct marketing dispatch
```

---

# 21. Identity, Reconciliation and Deduplication

Marketing MUST NOT create a separate master customer identity.

CustomerContext and applicable identity/reconciliation authorities remain authoritative.

One candidate appearing through multiple source relationships MUST NOT automatically produce multiple communications.

An accepted deduplication basis SHALL be purpose-qualified and preserve:

- source references;
- recipient evidence;
- endpoint evidence;
- reconciliation confidence or authority;
- Campaign Occurrence identity; and
- the reason multiple candidate appearances are considered one outreach responsibility.

Contact equality alone MUST NOT destructively merge CustomerContexts or prove person identity.

Where duplicate risk or recipient identity cannot be resolved sufficiently:

```text
direct outreach = UNRESOLVED
```

rather than multiple speculative sends.

---

# 22. Shared Endpoints

An email address, telephone number or provider account may be shared.

The endpoint alone MUST NOT establish:

- which person will receive the communication;
- that every related CustomerContext is equivalent;
- that protected personalisation is safe;
- or that person-specific campaign content may be disclosed.

Campaigns using a shared or unresolved endpoint SHALL limit content to what the applicable recipient and Exposure authority permit.

Where person-specific disclosure cannot be established:

```text
personalised direct outreach = PROHIBITED / UNRESOLVED
```

---

# 23. Campaign Approval

A **Campaign Approval** is merchant-authorised evidence permitting one exact Campaign Revision to be activated within an exact scope.

Conceptually:

```text
CampaignApproval
{
    approvalIdentity
    MerchantScope
    campaignRevisionIdentity
    approvedAudienceDefinitionVersion
    approvedOutreachFamilies
    approvedScheduleOrActivationScope
    approvedPersonalisationScope
    approvingActor
    authorityEvidence
    approvedAt
    expiryOrInvalidationConditions
    provenance
}
```

Approval SHALL bind the exact externally material revision.

A materially changed revision requires new approval unless an already accepted bounded automation contract explicitly permits that exact class of change.

---

# 24. AI Assistance

AI MAY:

- draft copy;
- suggest titles;
- adapt tone;
- suggest source-grounded calls to action;
- propose a registered Audience Definition;
- summarise expected recipients;
- identify possible campaign opportunities;
- propose send timing;
- create merchant-review material; and
- explain eligibility or suppression outcomes.

AI MUST NOT independently:

- create commercial offers;
- invent recipient permission;
- create targeting attributes;
- infer sensitive targeting authority;
- approve a Campaign Revision;
- activate a campaign;
- override suppression;
- select an unregistered audience;
- send communication;
- claim conversion or causation; or
- reinterpret provider evidence as customer action.

AI output remains proposal or assistance until accepted through deterministic authority.

---

# 25. Automated Campaign Contract

Recurring or source-triggered campaign execution requires an accepted **Automated Campaign Contract**.

Conceptually:

```text
AutomatedCampaignContract
{
    automationContractIdentity
    MerchantScope
    permittedCampaignPurpose
    permittedCampaignRevisionOrTemplate
    permittedTriggerFamily
    permittedAudienceDefinition
    permittedOutreachFamilies

    sourcePreconditions
    currentnessRequirements
    recurrenceAndFrequencyLimits
    approvalScope
    stopAndInvalidationConditions
    failureAndEscalationSemantics

    approvedBy
    approvedAt
    provenance
}
```

Automation is not AI autonomy.

The merchant approves the bounded operating rule. Main Street subsequently evaluates exact accepted conditions.

An automation contract MUST NOT authorise arbitrary future AI-generated content, audiences, offers or channels.

---

# 26. Trigger Semantics

A Campaign Occurrence may be initiated by:

- current merchant instruction;
- an accepted schedule;
- an accepted recurrence rule; or
- an accepted source-event trigger under an Automated Campaign Contract.

A source event remains owned by its source capability.

Canonical:

```text
accepted inventory availability event
        ↓
registered campaign trigger applies
        ↓
candidate Campaign Occurrence
        ↓
current campaign and recipient validation
```

The event does not itself send communication.

---

# 27. Scheduling

Campaign scheduling meaning belongs to Marketing.

MS-PROT-065 owns durable wake-up, retry and background execution.

A schedule SHALL preserve:

- exact Campaign Revision;
- applicable Audience Definition version;
- intended due time or recurrence;
- business timezone;
- permitted execution window;
- expiry;
- frequency limits;
- approval affinity;
- invalidation conditions; and
- provenance.

A scheduled Campaign Occurrence MUST revalidate current authority when due.

---

# 28. Campaign Occurrence

A **Campaign Occurrence** represents one exact intended execution of an approved Campaign Revision.

Conceptually:

```text
CampaignOccurrence
{
    campaignOccurrenceIdentity
    MerchantScope
    campaignIdentity
    campaignRevisionIdentity
    approvalOrAutomationContractReference
    triggerOrScheduleReference
    intendedOutreachFamilies
    dueAt
    createdAt
    provenance
}
```

Retry SHALL converge on the same logical Campaign Occurrence.

A Campaign Occurrence does not itself prove that any communication was externalised.

---

# 29. Public Outreach

Where a Campaign uses a public website, storefront, announcement or public-provider destination:

```text
Campaign Occurrence
        ↓
MS-PROT-046 Publication/Announcement instruction
        ↓
Publication-owned revision and lifecycle
```

Marketing does not own public publication truth.

A Campaign may coordinate multiple Publications, but those Publications retain independent identity and authority.

Publication failure or withdrawal does not silently rewrite Campaign history.

---

# 30. Direct Outreach

Where a Campaign communicates with identified recipients:

```text
Campaign Occurrence
        ↓
current eligible recipients
        ↓
eligible communication responsibilities
        ↓
Notification Intents under MS-PROT-075
```

MS-PROT-075 owns:

- Notification Intent;
- recipient/channel resolution;
- dispatch;
- Delivery Attempt;
- Delivery Evidence; and
- provider transport outcomes.

Marketing owns why the campaign communication exists and why the recipient was eligible.

---

# 31. Reply and Conversation Boundary

A recipient reply does not mutate Campaign truth.

Where an accepted Conversation Creation Contract applies:

```text
campaign communication reply
        ↓
MS-PROT-086 inbound Message
        ↓
create/reuse Conversation as authorised
```

A reply MUST NOT automatically:

- accept an offer;
- create a booking;
- create an order;
- execute an unsubscribe outside the accepted suppression/permission path;
- create a refund;
- create a customer relationship; or
- mark the Campaign successful.

---

# 32. Pause, Cancellation and Completion

Campaign pause or cancellation SHALL prevent future Campaign externalisation within its effective scope.

It cannot retract communication already accepted by an external provider or already delivered.

Where an external effect may already have occurred:

```text
campaign stopped internally
        +
provider outcome uncertain
        ↓
preserve uncertainty and reconcile
```

Campaign completion means the defined occurrence no longer has pending campaign-owned work.

It does not mean:

- every communication was delivered;
- every recipient read it;
- the business goal was achieved; or
- the Campaign caused commercial outcomes.

---

# 33. Entitlement and Capability Change

Loss or reduction of entitlement MAY prevent future campaign activation or externalisation.

It MUST NOT:

- delete Campaign history;
- rewrite approvals;
- erase delivery evidence;
- erase permission or suppression history;
- rewrite CustomerContext;
- revoke independent Publication truth; or
- redefine previous provider effects.

Residual management, cancellation and evidence access SHALL follow accepted entitlement and lifecycle authority.

---

# 34. Provider Neutrality and Uncertainty

Provider campaign, list, audience or message identifiers are external correlation evidence.

They MUST NOT become canonical Campaign, Campaign Revision, Campaign Occurrence or recipient identity.

Provider timeout, partial batch response or network failure MUST NOT be interpreted as “nothing happened”.

Retry requires stable logical identities, idempotency and MS-PROT-069 reconciliation semantics.

Main Street MUST NOT create duplicate direct outreach merely because a provider result was unavailable.

---

# 35. Campaign Outcome Evidence

A **Campaign Outcome Observation** records campaign-related evidence without claiming analytical meaning beyond the evidence.

Conceptually:

```text
CampaignOutcomeObservation
{
    observationIdentity
    MerchantScope
    campaignOccurrenceIdentity
    campaignRevisionIdentity
    recipientOrPublicationScope
    observationKind
    sourceAuthority
    observedAt
    evidenceCoverage
    provenance
}
```

Possible observations may include:

- Publication accepted or exposed;
- Notification delivery evidence;
- provider-reported open or interaction;
- destination visit evidence;
- unsubscribe or complaint evidence;
- qualifying source-operation occurrence; or
- another registered observation.

Each observation retains its actual authority and limitations.

---

# 36. Delivery and Engagement Distinctions

```text
provider accepted
    ≠ delivered
```

```text
delivered
    ≠ read
```

```text
provider-reported open
    ≠ proven human reading
```

```text
link interaction
    ≠ purchase
```

```text
purchase after interaction
    ≠ campaign-caused purchase
```

Provider-reported engagement MAY be retained as provider-qualified evidence. It MUST NOT be silently promoted to stronger customer or business truth.

---

# 37. Analytics and Attribution

Campaign measures and performance claims SHALL be governed through MS-PROT-083 Analytical Measure Definitions.

A material measure SHALL define:

- exact campaign population;
- qualifying occurrence scope;
- time window;
- numerator and denominator;
- delivery and interaction semantics;
- identity/reconciliation basis;
- source-operation relationship;
- evidence coverage;
- known exclusions;
- currency treatment where monetary;
- attribution method; and
- uncertainty semantics.

Terms such as:

- “bookings generated”;
- “orders generated”;
- “revenue generated”;
- “retention improved”;
- “best-performing campaign”; or
- “likely business impact”

MUST NOT be used without accepted analytical semantics supporting the claim.

Correlation alone does not establish causation.

---

# 38. Recommendations

MS-PROT-083 may produce a Business Recommendation suggesting that the merchant consider a Campaign.

Canonical:

```text
Business Recommendation
        ↓
merchant considers campaign
        ↓
merchant instruction or accepted bounded automation
        ↓
Campaign Revision and approval
```

Rejected:

```text
Business Recommendation
        ↓
automatic Campaign activation
```

A recommendation expires or becomes inapplicable according to its own authority and does not reserve future campaign execution authority.

---

# 39. Coverage and Uncertainty

Campaign audience and performance claims SHALL preserve coverage.

Main Street MUST NOT claim:

- all customers;
- all inactive customers;
- all repeat customers;
- all recipients;
- complete delivery;
- complete engagement;
- complete revenue impact; or
- whole-business retention effect

merely because all Main Street-controlled sources were queried.

Where a campaign depends on incomplete customer, permission, endpoint or source-business coverage:

```text
eligibility or aggregate result
    =
PARTIAL / UNKNOWN / UNRESOLVED
```

as governed by the applicable authority.

Missing evidence is not negative evidence.

---

# 40. Data Protection and Retention

Campaign processing SHALL be purpose-bound and data-minimised.

Main Street SHALL preserve:

- the purpose for which protected data was used;
- the Audience Definition and Campaign Revision;
- permission and suppression evidence;
- source and recipient provenance;
- externalisation evidence;
- access restrictions;
- retention/disposition requirements; and
- required historical accountability.

Campaign history MUST NOT become an unlimited customer dossier.

Deletion of a merchant-visible campaign draft does not automatically erase evidence that must remain for security, permission, suppression, audit or external-effect reconciliation.

---

# 41. Operational Protection

Bulk or repeated marketing work SHALL remain subject to:

- capacity protection;
- provider rate limits;
- abuse protection;
- frequency policy;
- quiet-hour policy where applicable;
- suppression;
- degradation;
- fair-use or entitlement constraints; and
- independent transactional/operational priority.

Low-priority marketing MUST NOT crowd out security-critical, transactional or required operational communication.

---

# 42. Deferred Decisions

## 42.1 MS-PROT-087-DQ-001 — Initial Campaign Purpose and Outreach Portfolio

**Status:** DEFERRED — ACTIVE BEFORE PRODUCTION MARKETING CAMPAIGNS  
**Owner:** Marketing semantic design composed with Publication, Notifications, Customer Communication and data-protection authority  
**Revisit condition:** Before any production Campaign family is activated.

Resolution SHALL select the initial campaign purposes, public/direct outreach families, content requirements and prohibited combinations. Example lists do not activate a Campaign family.

## 42.2 MS-PROT-087-DQ-002 — Initial Audience Definition and Attribute Portfolio

**Status:** DEFERRED — ACTIVE BEFORE PRODUCTION AUDIENCE TARGETING  
**Owner:** Marketing composed with CustomerContext, source-capability, analytics and data-protection authority  
**Revisit condition:** Before production recipient targeting is enabled.

Resolution SHALL register the exact audience definitions, permitted source facts, evidence requirements, time semantics, exclusions, deduplication and unresolved outcomes.

## 42.3 MS-PROT-087-DQ-003 — Marketing Permission, Suppression and Contact Policy Portfolio

**Status:** DEFERRED — ACTIVE BEFORE PRODUCTION DIRECT MARKETING  
**Owner:** Data Protection and applicable regulatory authority composed with Marketing and Notifications  
**Revisit condition:** Before production direct marketing occurs for an applicable channel/jurisdiction.

Resolution SHALL establish the exact permission authorities, scope, evidence, withdrawal, unsubscribe, suppression, frequency, quiet-hour and retention semantics. Generic “consented” booleans are insufficient.

## 42.4 MS-PROT-087-DQ-004 — Automated Campaign Trigger and Recurrence Portfolio

**Status:** DEFERRED — ACTIVE BEFORE PRODUCTION AUTOMATED CAMPAIGNS  
**Owner:** Marketing composed with each trigger source, MS-PROT-065, AI and protection authority  
**Revisit condition:** Before a production Campaign is activated without a new contemporaneous merchant instruction.

Resolution SHALL select exact trigger families, approved templates/revisions, recurrence limits, currentness requirements, approval scope and termination behaviour.

## 42.5 MS-PROT-087-DQ-005 — Campaign Measure and Attribution Portfolio

**Status:** DEFERRED — ACTIVE BEFORE PRODUCTION CAMPAIGN PERFORMANCE CLAIMS  
**Owner:** MS-PROT-083 analytical authority composed with Marketing and applicable source owners  
**Revisit condition:** Before Main Street presents production campaign-effectiveness, conversion, retention or revenue-impact claims.

Resolution SHALL define exact measures, evidence coverage, identity basis, time windows, attribution methods, uncertainty and permissible causal language.

## 42.6 MS-PROT-087-DQ-006 — Paid Advertising, Spend and External Optimisation Authority

**Status:** DEFERRED — ACTIVE BEFORE PAID ADVERTISING OR EXTERNAL CAMPAIGN SPEND  
**Owner:** Future marketing/financial/external-provider design  
**Revisit condition:** Before Main Street commits advertising spend, participates in auctions, controls external advertising budgets or performs external ad optimisation.

MS-PROT-087 v1.0 does not authorise those operations.

---

# 43. Hard Invariants

### INV-087-001 — Exact Merchant Scope

Every Campaign, revision, audience, eligibility assessment, approval, occurrence and outcome observation SHALL belong to one exact Merchant Scope.

### INV-087-002 — Campaign Is Not Publication

Campaign coordination MUST NOT replace MS-PROT-046 Publication or Announcement authority.

### INV-087-003 — Campaign Is Not Notification

Campaign meaning and recipient eligibility MUST remain separate from Notification dispatch and delivery evidence.

### INV-087-004 — Campaign Is Not Conversation

Campaign execution or response MUST NOT silently redefine Conversation continuity.

### INV-087-005 — Relationship Is Not Permission

CustomerContext, purchase, booking, Enquiry or prior contact MUST NOT independently grant marketing permission.

### INV-087-006 — Endpoint Is Not Recipient Authority

An email address, telephone number or provider identity MUST NOT independently establish recipient identity or authority to contact.

### INV-087-007 — Preference Is Not Consent

Notification Preference MUST NOT create marketing permission or lawful communication authority.

### INV-087-008 — Permission Is Purpose- and Scope-Qualified

Permission evidence SHALL apply only within its accepted merchant, recipient, purpose, channel, jurisdiction and time scope.

### INV-087-009 — Marketing Classification Cannot Be Bypassed

Promotional meaning MUST NOT be relabelled as transactional, operational or customer service to bypass marketing restrictions.

### INV-087-010 — Current Eligibility Is Required

Mutable eligibility, permission, preference and suppression conditions SHALL be revalidated before externalisation.

### INV-087-011 — Unresolved Eligibility Fails Closed

An unresolved material eligibility condition MUST NOT produce marketing outreach.

### INV-087-012 — Audience Semantics Are Registered

Production audience selection SHALL use accepted, versioned and explainable Audience Definitions.

### INV-087-013 — Audience Eligibility Is Not a Permanent Customer Label

Campaign-specific eligibility MUST NOT silently create durable CustomerContext classification.

### INV-087-014 — Sensitive Inference Is Not Targeting Authority

Sensitive or proxy AI inference MUST NOT create campaign targeting authority.

### INV-087-015 — AI Is Non-Authoritative

AI MUST NOT independently approve, activate, address or send a Campaign.

### INV-087-016 — Approval Has Exact Revision Affinity

Campaign approval SHALL bind the exact externally material Campaign Revision.

### INV-087-017 — Material Change Invalidates Approval

A material content, audience, channel, personalisation or scheduling change requires new applicable approval.

### INV-087-018 — Automation Is Contract-Bounded

Automated campaign execution SHALL require an accepted bounded Automated Campaign Contract.

### INV-087-019 — Source Facts Retain Ownership

Campaign targeting and content MUST NOT rewrite the business facts they consume.

### INV-087-020 — Scheduling Does Not Reserve Future Authority

Scheduled eligibility MUST be revalidated when the Campaign Occurrence becomes due.

### INV-087-021 — Campaign Copy Cannot Create an Offer

Promotional wording MUST NOT create price, discount, availability, booking or contractual truth.

### INV-087-022 — Duplicate Candidates Do Not Multiply Outreach

Multiple source appearances MUST NOT create multiple communications without independently established recipient independence.

### INV-087-023 — Contact Equality Does Not Merge Customers

Endpoint equality MUST NOT destructively merge CustomerContexts or prove person identity.

### INV-087-024 — Retry Preserves Logical Identity

Campaign and Notification retry SHALL converge on stable logical identities and MUST NOT create speculative duplicate external effects.

### INV-087-025 — Provider Uncertainty Is Preserved

Unknown provider outcome MUST remain unknown until reconciled.

### INV-087-026 — Entitlement Does Not Rewrite History

Entitlement change MUST NOT erase or reinterpret prior Campaign, permission, delivery or outcome evidence.

### INV-087-027 — Public and Direct Outreach Retain Their Owners

Public outreach SHALL use Publication authority; direct-recipient outreach SHALL use Notification authority.

### INV-087-028 — Replies Require Conversation Authority

A campaign reply may enter Customer Communication only through an accepted Conversation Creation Contract.

### INV-087-029 — Outcome Observation Is Not Attribution

Campaign-related evidence MUST NOT independently establish campaign effectiveness or causation.

### INV-087-030 — Delivery Is Not Conversion

Delivery, open, read, click or visit evidence MUST NOT be represented as an order, booking, revenue or retention outcome.

### INV-087-031 — Correlation Is Not Causation

A later business outcome MUST NOT be described as caused by a Campaign without applicable MS-PROT-083 authority.

### INV-087-032 — Coverage Is Explicit

Audience and campaign-performance claims SHALL preserve source and identity coverage.

### INV-087-033 — Marketing Is Data-Minimised

Campaign history and derived audience evidence MUST NOT become an unrestricted customer dossier.

### INV-087-034 — Operational Communication Retains Priority

Marketing work MUST NOT impair required security, transactional or operational communication.

### INV-087-035 — No Enterprise Marketing Suite

Marketing SHALL remain a bounded merchant-outreach capability and MUST NOT become a generic CRM, customer-data platform, funnel builder or advertising suite.

### INV-087-036 — Paid Advertising Is Not Authorised

Campaign authority MUST NOT create advertising-spend, auction or external optimisation authority.

---

# 44. Alternatives and Trade-Offs

## 44.1 Rejected — Generic Marketing Engine Owns Everything

Rejected because customer identity, consent, delivery, publication, conversation, offers and analytics already have distinct owners.

## 44.2 Rejected — CustomerContext `marketingEligible` Flag

Rejected because eligibility varies by merchant, purpose, channel, jurisdiction, time, permission, suppression and exact campaign.

## 44.3 Rejected — Notification Preference as Consent

Rejected because a channel preference can constrain an already permitted communication but cannot create permission.

## 44.4 Rejected — AI-Selected Audience and Automatic Send

Rejected because AI inference cannot establish permission, currentness, sensitive-data acceptability, merchant approval or external-effect authority.

## 44.5 Rejected — Provider Campaign as Canonical Campaign

Rejected because provider objects are replaceable external fulfilment mechanisms.

## 44.6 Rejected — Complete Funnel Attribution

Rejected because provider and Main Street evidence rarely establishes complete customer behaviour or causation without explicit analytical authority.

## 44.7 Rejected — Enterprise Segment Builder

Rejected because arbitrary query builders expose internal software structure and create excessive configuration, privacy and training burden.

## 44.8 Accepted Trade-Off

The design adds internal revision, eligibility, approval, suppression and reconciliation precision. That complexity is necessary to make simple merchant outreach trustworthy and low-administration.

---

# 45. Falsification

## 45.1 First Falsification

Initial hypothesis:

```text
merchant chooses audience
        +
AI writes message
        +
schedule sends through provider
```

**Outcome:** FAIL.

It permitted:

1. CustomerContext to imply consent;
2. contact equality to imply recipient identity;
3. opt-out after scheduling to be ignored;
4. duplicate sends from reconciled customer evidence;
5. AI-created sensitive or proxy targeting;
6. provider retry after an uncertain external effect;
7. edited content to retain stale approval;
8. recommendations to become execution;
9. promotional copy to manufacture an offer;
10. provider clicks to become conversion claims; and
11. a sole trader to face enterprise segmentation and automation administration.

The model was revised with exact Campaign Revisions, registered Audience Definitions, current Recipient Eligibility Assessments, revision-bound approval, bounded automation, suppression, deduplication, owner-separated execution and MS-PROT-083 attribution.

## 45.2 Second Falsification

The revised model was tested against:

- a shared family email address;
- one person represented by multiple reconciled CustomerContexts;
- permission withdrawn after scheduling;
- a purchased contact list without sufficient provenance;
- an AI-proposed financial-distress audience;
- a provider timeout after possible acceptance;
- a content edit after approval;
- a source offer expiring before execution;
- entitlement loss during a Campaign;
- one Campaign using public Announcement and direct email;
- a customer replying “book me in”;
- an order following a campaign interaction;
- partial customer-history coverage;
- high-volume marketing during transactional delivery pressure; and
- a sole trader launching a simple seasonal message without configuring workflows.

**Outcome:** PASS WITH THREE RESIDUAL AMBIGUITIES.

Residual risks were:

1. promotional content could be relabelled transactional;
2. duplicate protection could be misread as permission to merge customers by endpoint; and
3. Campaign completion could be misread as business-goal completion.

Targeted rules were added to Sections 18, 21, 22 and 32 and invariants INV-087-009, INV-087-023 and INV-087-029–031.

## 45.3 Final Falsification

The targeted model preserves:

- exact semantic ownership;
- current permission and suppression;
- customer and endpoint ambiguity;
- immutable approval affinity;
- source-business currentness;
- provider uncertainty;
- truthful outcome evidence;
- analytical uncertainty;
- AI non-authority;
- low merchant administration; and
- bounded capability depth.

**Final falsification outcome:** PASS.

---

# 46. Corpus Conformance

MS-PROT-087 composes without changing the architecture:

- CustomerContext remains with MS-PROT-043.
- Publication remains with MS-PROT-046.
- Notification delivery remains with MS-PROT-075.
- Conversation remains with MS-PROT-086.
- analytics and recommendations remain with MS-PROT-083.
- data protection and retention remain with MS-PROT-053.
- provider fulfilment and uncertainty remain with MS-PROT-048 and MS-PROT-069.
- durable work remains with MS-PROT-065.
- AI remains non-authoritative under MS-PROT-057.
- Merchant Attention remains with MS-PROT-085.

The lower-authority `docs/development/PRD/marketing-and-campaign-engine.md` remains product-intent evidence only. Its automatic CRM segmentation, AI optimisation, campaign automation and “revenue generated” language do not govern except where supported by this authority and its deferred decisions.

**Architecture change:** NOT REQUIRED  
**New cross-cutting engine:** NOT REQUIRED  
**Implementation-rules amendment:** NOT REQUIRED  
**Current implementation sequence:** UNCHANGED  
**MS-PROT-084 incomplete-base blocker:** UNAFFECTED  
**Corpus conformance:** PASS

---

# 47. Acceptance Criteria

MS-PROT-087 is acceptable only if:

- Campaign remains distinct from Publication, Notification, Conversation and CustomerContext;
- recipient eligibility requires accepted current audience, permission, suppression and identity evidence;
- relationship and contact evidence cannot independently grant marketing permission;
- marketing cannot be relabelled to bypass applicable restrictions;
- audiences use registered explainable definitions;
- sensitive or proxy AI inference cannot create targeting authority;
- approval binds the exact Campaign Revision;
- material changes invalidate approval;
- automated execution requires a bounded approved contract;
- schedules revalidate current authority;
- source business facts retain ownership;
- duplicate candidates cannot create speculative duplicate sends;
- provider uncertainty is preserved;
- public and direct outreach use their accepted owners;
- replies require Customer Communication authority;
- campaign evidence cannot become conversion or causation without MS-PROT-083;
- marketing remains data-minimised and operationally subordinate;
- paid advertising remains outside v1.0;
- all six production decisions remain deferred until their gates; and
- the current implementation programme remains unchanged.

---

# 48. Governance Outcome

**Fundamental Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY  
**Feature Admission:** PASS  
**Design completeness:** PASS  
**First falsification:** FAIL → REVISED  
**Second falsification:** PASS WITH THREE RESIDUAL AMBIGUITIES  
**Targeted revision:** COMPLETED  
**Final falsification:** PASS  
**Cross-capability ownership:** PASS  
**Permission and suppression separation:** PASS  
**Audience-governance proportionality:** PASS  
**Approval affinity:** PASS  
**AI non-authority:** PASS  
**Provider neutrality:** PASS  
**Outcome and attribution discipline:** PASS  
**Data minimisation:** PASS  
**Anti-CRM / anti-marketing-suite proportionality:** PASS  
**Corpus conformance:** PASS  
**Recommendation:** ACCEPT  
**Manual approval:** GRANTED — 8 September 2026  
**Repository formalisation:** AUTHORISED

---

# 49. Recommendation

**RECOMMENDATION: ACCEPT**

MS-PROT-087 closes the generic Campaign semantic gap without introducing a CRM, marketing-automation platform, advertising system or new architectural layer. It gives Main Street enough authority to coordinate simple, trustworthy outreach for small merchants while leaving providers, legal permission, publications, delivery, conversations, business facts and analytics with their accepted owners.

Acceptance establishes semantic/design authority only. It does not activate a production Campaign family, audience definition, direct-marketing policy, automated trigger, performance claim, paid-advertising operation, production code, migration, provider or deployment. Implementation proceeds only through `designs/IMPLEMENTATION-RULES.md` after the applicable deferred-decision and sequencing gates are satisfied.
