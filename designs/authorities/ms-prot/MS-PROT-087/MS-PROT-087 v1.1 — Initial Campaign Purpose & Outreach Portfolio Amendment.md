# MS-PROT-087 v1.1 — Initial Campaign Purpose & Outreach Portfolio Amendment

**Document ID:** MS-PROT-087  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** 9 September 2026 by explicit manual approval  
**Authority type:** Marketing Campaign production-portfolio semantic/design amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `designs/DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** MS-PROT-087 v1.0 only within the initial Campaign-purpose and outreach-family portfolio  
**Depends on:** Composite MS-PROT-027; composite MS-PROT-043; composite MS-PROT-046; MS-PROT-048; composite MS-PROT-053; MS-PROT-056; composite MS-PROT-057; MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; composite MS-PROT-065; MS-PROT-067; composite MS-PROT-069; MS-PROT-070; MS-PROT-073; composite MS-PROT-075; MS-PROT-083; composite MS-PROT-085; composite MS-PROT-086  
**Resolves:** `MS-PROT-087-DQ-001` — Initial Campaign Purpose and Outreach Portfolio  
**Implementation activation:** NONE  
**Purpose:** Select the minimum initial Marketing Campaign purposes and outreach families required for low-administration merchant awareness, customer appreciation and repeat-business communication without creating a CRM, cold-outreach system, generic publication wrapper, social-media manager or advertising platform.

---

# 1. Decision

The initial production-semantic Marketing Campaign portfolio SHALL contain exactly four Campaign purpose families:

```text
marketing/merchant-news-awareness@1
marketing/offering-awareness@1
marketing/customer-appreciation@1
marketing/customer-reengagement@1
```

The initial Campaign outreach portfolio SHALL contain exactly two outreach families:

```text
WEBSITE_ANNOUNCEMENT_V1
DIRECT_EMAIL_MARKETING_V1
```

No other Campaign purpose or outreach family is activated by this amendment.

In particular, this amendment does not activate SMS, push, in-app marketing, WhatsApp, social-network publication, generic external mailing lists, paid advertising or external advertising optimisation.

---

# 2. Governing Principle

The initial Marketing capability SHALL help a micro or small merchant remain visible and invite legitimate existing customer relationships back to the business without requiring the merchant to become a marketer, CRM administrator or campaign-automation specialist.

Canonical merchant interaction is conceptually:

```text
What do you want customers to know?
        ↓
Who is this relevant to?
        ↓
Main Street resolves the governed campaign semantics
        ↓
merchant reviews exact content and scope
        ↓
merchant approves
        ↓
Publication and/or Notification execute through their own authority
```

The merchant SHALL NOT be required to choose provider objects, construct database queries, configure segmentation expressions, understand consent-state machinery or administer marketing workflows.

---

# 3. Initial Campaign Purpose Portfolio

## 3.1 `marketing/merchant-news-awareness@1`

This purpose communicates non-transaction-specific merchant news whose purpose is awareness rather than fulfilment of an existing customer obligation.

Applicable subject matter MAY include current merchant events, business milestones, general seasonal or holiday messages, newly public business information and other current merchant-authored news.

This family SHALL NOT be used to disguise operational or transactional communication as marketing.

Therefore an Appointment cancellation, security notice, Order update, payment requirement or similar source-owned communication remains with its owning capability even when the merchant believes the information is commercially useful.

Permitted initial outreach families are:

```text
WEBSITE_ANNOUNCEMENT_V1
DIRECT_EMAIL_MARKETING_V1
```

subject to the gates in this amendment.

## 3.2 `marketing/offering-awareness@1`

This purpose communicates awareness of a currently authoritative and customer-exposable Product, Service, Offering or other accepted merchant commercial subject.

It MAY communicate an independently authoritative price, promotion or commercial term where an applicable source authority already establishes that meaning.

It SHALL NOT create that meaning.

Canonical:

```text
authoritative offering / commercial fact
        ↓
Campaign communicates it
```

Prohibited:

```text
Campaign copy says "20% off"
        ↓
20% discount becomes authoritative
```

Permitted initial outreach families are:

```text
WEBSITE_ANNOUNCEMENT_V1
DIRECT_EMAIL_MARKETING_V1
```

## 3.3 `marketing/customer-appreciation@1`

This purpose permits a merchant to send a non-transaction-specific appreciation message to an independently eligible existing customer audience.

The purpose itself SHALL NOT classify a recipient as:

```text
VIP
high value
loyal
best customer
premium customer
```

unless another accepted authority independently establishes the applicable classification.

An appreciation Campaign SHALL NOT manufacture a reward, discount, loyalty entitlement or commercial benefit.

The initial permitted outreach family is:

```text
DIRECT_EMAIL_MARKETING_V1
```

## 3.4 `marketing/customer-reengagement@1`

This purpose permits a merchant to invite independently eligible previous customers to return to the business.

Re-engagement eligibility SHALL derive only from an accepted Audience Definition under the future resolution of `MS-PROT-087-DQ-002`.

The Campaign purpose does not itself define:

```text
inactive customer
lapsed customer
likely to return
high churn risk
```

and SHALL NOT persist those labels into CustomerContext.

The initial permitted outreach family is:

```text
DIRECT_EMAIL_MARKETING_V1
```

---

# 4. Initial Outreach Family — `WEBSITE_ANNOUNCEMENT_V1`

`WEBSITE_ANNOUNCEMENT_V1` is a public Campaign outreach family that delegates external publication to MS-PROT-046.

Conceptually:

```text
CampaignOccurrence
        ↓
Campaign-owned public-outreach instruction
        ↓
MS-PROT-046 Announcement / Publication authority
        ↓
merchant-owned public website/storefront exposure
```

Marketing owns the Campaign purpose and coordination.

Publication owns the published object, revision, exposure and publication lifecycle.

A Marketing Campaign SHALL NOT be required merely to create one ordinary public Announcement.

Hard rule:

```text
single standalone website announcement
        ↓
MS-PROT-046 directly
```

not:

```text
single standalone website announcement
        ↓
create MarketingCampaign
        ↓
create CampaignOccurrence
        ↓
create Announcement
```

Within the initial portfolio, `WEBSITE_ANNOUNCEMENT_V1` SHALL participate in a Marketing Campaign only where the same exact Campaign Revision also coordinates `DIRECT_EMAIL_MARKETING_V1`.

This prevents Marketing from becoming an unnecessary wrapper around Publication.

No social-network or third-party public destination is included in `WEBSITE_ANNOUNCEMENT_V1`.

---

# 5. Initial Outreach Family — `DIRECT_EMAIL_MARKETING_V1`

`DIRECT_EMAIL_MARKETING_V1` is the initial identified-recipient Marketing outreach family.

It executes through MS-PROT-075 Notification authority using the canonical `EMAIL` channel.

Conceptually:

```text
CampaignOccurrence
        ↓
current eligible recipient
        ↓
one logical Marketing communication responsibility
        ↓
NotificationIntent
        ↓
EMAIL NotificationDispatch
        ↓
provider execution
```

Marketing owns why the recipient is included in the Campaign.

Notifications owns recipient/channel resolution, Notification Intent, Dispatch, Delivery Attempt and Delivery Evidence.

One independently resolved recipient SHALL create at most one logical `DIRECT_EMAIL_MARKETING_V1` communication responsibility for one Campaign Occurrence unless a separately accepted future contract explicitly establishes otherwise.

Retry SHALL converge on that existing logical responsibility rather than creating another marketing communication.

---

# 6. Relationship-Based Initial Direct-Marketing Boundary

The initial direct Campaign portfolio is relationship-based.

It does not establish a customer-acquisition or prospecting engine.

The following are outside the initial direct-marketing portfolio:

```text
purchased or rented mailing lists
scraped contact details
arbitrary imported prospect lists
social-media followers merely because they follow the merchant
Enquiry contacts merely because an Enquiry occurred
lookalike audiences
AI-inferred prospects
public-directory contacts
cold outreach
```

A known email address does not change this rule.

Future `MS-PROT-087-DQ-002` resolution SHALL select the exact supported Audience Definitions within this bounded portfolio.

Future `MS-PROT-087-DQ-003` resolution SHALL establish the exact permission, suppression, contact and jurisdiction policy required before any direct Campaign externalisation.

No relationship fact independently grants marketing permission.

---

# 7. Campaign Content Requirements

Every Campaign Revision in the initial portfolio SHALL bind the exact Campaign purpose family, exact outreach family set, exact externally material content for each outreach family, exact source-business references required for material factual claims, applicable destination or call-to-action references, approved personalisation semantics and the merchant identity/sender presentation.

Campaign content may contain ordinary expressive language.

Material factual claims concerning Products, Services, prices, availability, discounts, events, merchant information or other authoritative business facts SHALL remain grounded in their applicable source authority.

A merchant's approval of Campaign copy does not cause an otherwise non-authoritative source claim to become source truth.

Where a material source fact becomes invalid or insufficiently current before externalisation, Main Street SHALL NOT silently rewrite the approved Campaign copy.

The affected outreach path SHALL fail closed, or a materially revised Campaign Revision SHALL require new approval.

---

# 8. Initial Personalisation Boundary

Initial direct-email personalisation SHALL be deliberately shallow.

The only person-specific presentation permitted by this amendment is an optional current recipient-facing name or equivalent ordinary salutation where current recipient identity and Exposure authority make that disclosure safe.

The initial portfolio does not permit content personalisation from:

```text
purchase history
booking history
specific prior services
specific prior products
spend amount
visit frequency
absence duration
inferred preferences
location history
customer-value scoring
sensitive attributes
proxy-sensitive attributes
```

Audience eligibility may later consume source facts under an accepted Audience Definition without making those facts permissible Campaign content.

Therefore:

```text
selected because qualifying activity is absent
        ≠
email may disclose the recipient's activity history
```

A re-engagement message may say:

```text
"We'd love to see you again."
```

It SHALL NOT automatically say:

```text
"You haven't visited us for 93 days."
```

merely because the Audience Definition used that evidence.

---

# 9. Direct Email and Conversation Boundary

`DIRECT_EMAIL_MARKETING_V1` is a Notification email.

It is not the `EMAIL_REPLY_CONTINUE_V1` Conversation-Bound Email contract established by MS-PROT-086.

Therefore:

```text
marketing email
    ≠ existing Conversation
```

and:

```text
recipient email reply
    ≠ automatically authorised Conversation creation
```

A Main Street Conversation may be created or reused from a Campaign response only through a separately accepted Conversation Creation Contract.

Until such a contract exists, the Campaign experience SHALL NOT claim that replying to a marketing email provides Main Street customer-service or Conversation continuity.

Where appropriate, Campaign content MAY direct the recipient to an independently available merchant website, booking, ordering or website-messaging surface.

A Campaign call to action does not execute the underlying operation.

---

# 10. Prohibited Initial Combinations

The initial portfolio SHALL NOT combine promotional Marketing content into the same logical direct communication as security-critical, transactional or operational communication.

Therefore an Appointment reminder, Booking confirmation, Order update, Payment notice or security communication SHALL NOT gain promotional content merely because the same technical EMAIL channel is available.

Initial Campaigns also SHALL NOT include review/feedback solicitation, birthday marketing, loyalty-programme messaging, abandoned-cart workflows, automatically generated discounts, prospective-customer acquisition, social posting, SMS, push, WhatsApp, in-app marketing, paid advertising or external advertising optimisation.

These exclusions are portfolio boundaries, not claims that every excluded capability can never be supported.

---

# 11. Merchant Approval and Initial Activation

Every initial Campaign execution SHALL use an exact approved Campaign Revision.

For the portfolio selected by this amendment, ordinary manual execution authorises exactly one Campaign Occurrence.

Conceptually:

```text
exact Campaign Revision
        ↓
merchant review
        ↓
exact approval
        ↓
explicit activation instruction
        ↓
one Campaign Occurrence
```

An approval SHALL NOT become an unrestricted standing authority for future Campaigns.

A material change to purpose, content, audience definition, outreach family, call to action, source commercial meaning or personalisation requires a new applicable Campaign Revision and approval.

---

# 12. Automation and Scheduling Boundary

This amendment does not resolve `MS-PROT-087-DQ-004`.

Accordingly, it does not activate recurring, event-triggered or other standing automated Campaign execution.

Any Campaign execution that relies on an earlier standing authorisation rather than the merchant's exact single-occurrence activation remains gated by `MS-PROT-087-DQ-004`.

That later authority must determine the permitted trigger, schedule, recurrence, template/revision, currentness and termination semantics.

---

# 13. Direct-Marketing Activation Gate

Selection of `DIRECT_EMAIL_MARKETING_V1` in this semantic portfolio does not make direct marketing executable.

Before production direct email may externalise, all applicable prerequisites SHALL be satisfied, including:

```text
accepted DQ-002 Audience Definition portfolio
        +
accepted DQ-003 permission/suppression/contact policy
        +
exact current Campaign approval
        +
current recipient eligibility
        +
current source-business applicability
        +
current entitlement
        +
current Exposure/access
        +
Notification channel/provider readiness
        +
protection and rate-limit admission
```

Any unresolved material prerequisite fails closed for the affected recipient.

---

# 14. Performance and Attribution Boundary

This amendment does not resolve `MS-PROT-087-DQ-005`.

Campaign history MAY present direct factual evidence such as Campaign occurrence state and Notification delivery evidence using the exact limitations already established by their owners.

Claims such as:

```text
campaign generated bookings
campaign generated orders
campaign generated revenue
campaign improved retention
best-performing campaign
```

remain prohibited until an applicable accepted MS-PROT-083 analytical contract and `MS-PROT-087-DQ-005` resolution support the claim.

---

# 15. Paid Advertising Boundary

This amendment does not resolve `MS-PROT-087-DQ-006`.

Nothing in the initial portfolio authorises:

```text
advertising spend
external ad budgets
ad auctions
paid-media purchase
external audience optimisation
lookalike advertising
automatic bid changes
```

---

# 16. AI Boundary

AI MAY assist the merchant in translating ordinary language into one of the four registered purpose families, draft copy, suggest a source-grounded call to action and prepare the Campaign Revision for review.

AI SHALL NOT create another Campaign purpose, another outreach family, a recipient audience, recipient permission, a commercial offer, a material source fact, Campaign approval or Campaign activation merely through inference.

AI-generated material that becomes externally material SHALL be incorporated into the exact Campaign Revision reviewed by the merchant.

No post-approval generative step may materially rewrite approved Campaign meaning without a new applicable approval.

---

# 17. Low-Administration Merchant Experience

The four internal purpose identities SHALL NOT require the merchant to learn those identities.

A conforming merchant-facing experience MAY use ordinary business language equivalent to:

```text
Tell customers what's new
Tell customers about a product or service
Thank customers
Invite previous customers back
```

Main Street SHALL perform the translation into registered Campaign semantics.

The merchant SHALL NOT be presented with a generic CRM segment builder or arbitrary query language as the ordinary path for using the initial portfolio.

---

# 18. Fundamental Vision Conformance

**Outcome:** `VISION-CONFORMING`

This amendment satisfies the Coordination and Administrative-Compression tests.

It coordinates Marketing purpose with Publication and Notification without duplicating their ownership.

It compresses merchant administration by selecting four ordinary business purposes, preserving source-owned truth and hiding recipient, provider, retry, suppression and delivery machinery.

The initial relationship-based scope deliberately avoids cold-outreach and enterprise CRM complexity.

The Campaign-wrapper restriction prevents a merchant from needing to manage Marketing merely to publish an ordinary website Announcement.

---

# 19. Falsification

The proposed portfolio was tested against a sole trader posting a bank-holiday notice; a barber announcing a new service through website plus email; an imported list of ten thousand addresses; an Enquiry-only contact; an AI-invented discount; customer re-engagement based on inactivity; a shared household email address; a marketing recipient replying "book me in"; an Appointment reminder with promotional content appended; a merchant requesting SMS; a future scheduled Christmas Campaign; an opt-out occurring before externalisation; a provider timeout after possible email acceptance; and a low-software-capacity merchant launching a simple message.

The initial formulation failed in two places.

First, it allowed Marketing Campaign to become a redundant wrapper around standalone Publication.

Second, it left enough scope for direct Campaigns to drift into generic prospect-list marketing.

The proposal was revised so that standalone website Announcements remain directly MS-PROT-046-owned and the initial direct portfolio is relationship-based rather than prospecting-based.

The revised proposal also keeps direct email separate from Conversation-Bound Email, prevents mixed operational/promotional communications, restricts personalisation, and leaves audience, permission, automation and attribution semantics behind their explicit existing DQs.

**Final falsification outcome:** PASS.

---

# 20. Deferred-Decision Consequence

`MS-PROT-087-DQ-001` is resolved by this amendment.

The following remain unresolved and are not implicitly narrowed except where this amendment establishes the initial portfolio boundary:

```text
MS-PROT-087-DQ-002
Initial Audience Definition and Attribute Portfolio

MS-PROT-087-DQ-003
Marketing Permission, Suppression and Contact Policy Portfolio

MS-PROT-087-DQ-004
Automated Campaign Trigger and Recurrence Portfolio

MS-PROT-087-DQ-005
Campaign Measure and Attribution Portfolio

MS-PROT-087-DQ-006
Paid Advertising, Spend and External Optimisation Authority
```

DQ-002 and DQ-003 remain mandatory prerequisites before `DIRECT_EMAIL_MARKETING_V1` externalisation.

DQ-004 remains mandatory before standing, scheduled, recurring or source-triggered Campaign execution that is not the exact manually activated single occurrence established by this amendment.

DQ-005 remains mandatory before production campaign-effectiveness or impact claims.

DQ-006 remains mandatory before paid advertising or external Campaign spend.

---

# 21. Additional Hard Invariants

### INV-087-V11-001 — Initial Purpose Portfolio Is Closed

Production Campaign purpose under this amendment SHALL be exactly one of the four registered v1 families.

### INV-087-V11-002 — Initial Outreach Portfolio Is Closed

Production Campaign outreach under this amendment SHALL use only `WEBSITE_ANNOUNCEMENT_V1` and `DIRECT_EMAIL_MARKETING_V1`.

### INV-087-V11-003 — Standalone Publication Does Not Require Campaign

Main Street SHALL NOT require a Marketing Campaign merely to create one ordinary website Announcement.

### INV-087-V11-004 — Initial Direct Marketing Is Not Prospecting

The initial direct Campaign portfolio SHALL NOT perform cold outreach, arbitrary prospect-list messaging, lookalike targeting or AI-generated prospecting.

### INV-087-V11-005 — Direct Email Is Notification, Not Conversation

`DIRECT_EMAIL_MARKETING_V1` SHALL execute through Notification authority and SHALL NOT reuse Conversation-Bound Email semantics as marketing transport authority.

### INV-087-V11-006 — Direct Marketing Remains Gated

Selection of `DIRECT_EMAIL_MARKETING_V1` SHALL NOT permit externalisation before DQ-002 and DQ-003 are accepted and current recipient eligibility is established.

### INV-087-V11-007 — No Behavioural Disclosure by Audience Selection

A source fact used to establish audience eligibility SHALL NOT automatically become permissible recipient-facing Campaign content.

### INV-087-V11-008 — No Mixed Marketing Bypass

The initial direct Campaign portfolio SHALL NOT append promotional content to security-critical, transactional or operational communications.

### INV-087-V11-009 — One Manual Activation, One Occurrence

Initial ordinary manual Campaign activation SHALL create one logical Campaign Occurrence and SHALL NOT establish standing future execution authority.

### INV-087-V11-010 — AI Cannot Expand the Portfolio

AI SHALL NOT create an unregistered Campaign purpose, outreach family or execution authority.

---

# 22. Acceptance Criteria

This amendment is acceptable only if the initial portfolio remains small and relationship-focused; standalone Publication remains outside unnecessary Campaign wrapping; only website Announcement plus direct email are selected; public and direct externalisation retain MS-PROT-046 and MS-PROT-075 ownership respectively; direct marketing remains blocked by DQ-002 and DQ-003; recurring or standing automation remains blocked by DQ-004; campaign performance claims remain blocked by DQ-005; paid advertising remains blocked by DQ-006; Campaign content cannot manufacture business or commercial truth; initial personalisation remains shallow; Conversation-Bound Email is not reused as marketing authority; AI remains proposal-only; and no implementation or merchant activation is authorised by acceptance of this semantic portfolio.

**Architecture change:** NOT REQUIRED  
**New generic marketing engine:** NOT REQUIRED  
**Implementation activation:** NONE  
**Current implementation programme:** UNCHANGED  
**MS-PROT-084 incomplete-base blocker:** UNAFFECTED  
**Recommendation:** ACCEPT
