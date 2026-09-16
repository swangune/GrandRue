# MS-PROT-086 v1.4 — Customer Communication Commercial Access Classification Amendment

**Document ID:** MS-PROT-086  
**Version:** 1.4  
**Status:** ACCEPTED  
**Approved:** 16 September 2026 — explicit manual approval of the complete proposal  
**Authority type:** Customer Communication commercial-access classification amendment  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-086 through v1.3 within commercial-access classification only  
**Depends on:** Composite MS-PROT-086 through v1.3; composite MS-PROT-056 through v1.9; composite MS-PROT-085 through v1.4; composite MS-PROT-053; composite MS-PROT-057; composite MS-PROT-062; composite MS-PROT-063; composite MS-PROT-065; composite MS-PROT-069; composite MS-PROT-073; composite MS-PROT-074; composite MS-PROT-075; and applicable source-owner, Actor Authorisation, Provider Readiness and Projection/Exposure authorities  
**Preserves:** Conversation and Message ownership; channel/participant/access separation; Notification delivery ownership; Merchant Attention handling ownership; source-business ownership; AI non-authority; provider neutrality; current authority and data-lifecycle boundaries  
**Partially resolves:** `MS-PROT-056-V17-DQ-001` by supplying the current Customer Communication owner/supporting classifications  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY

---

# 0. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

GrandRue SHALL commercially protect the merchant's ability to conduct new Customer Communication without turning retained communication history, incomplete drafts or already-committed progression into separate subscription tolls.

Canonical:

```text
new customer communication activity
        ↓
BUSINESS commercial permission required

existing accepted Conversation / Message
        ↓
bounded observation
        ↓
no independent Customer Communication entitlement

already committed Message / assessment
        ↓
recovery / downstream progression
        ↓
no second commercial toll
```

Routine automated customer service is additional protected service use but remains part of the BUSINESS operating portfolio.

The merchant-facing commercial model remains:

```text
BUSINESS
    → communicate with customers
    → optionally use the admitted routine factual-response service
```

The merchant SHALL NOT be required to understand Conversation contracts, response-contract families, delivery providers, Attention contracts or entitlement identities.

Commercial complexity remains internal to GrandRue.

---

# 1. Governing Decision

Composite MS-PROT-086 SHALL define exactly two initially protected commercial purposes:

```text
CONDUCT_CUSTOMER_COMMUNICATION

AUTOMATE_ROUTINE_CUSTOMER_SERVICE
```

The exact protected access contracts SHALL be:

| Exact access contract | Protected commercial purpose | Standard allocation | Target family |
|---|---|---|---|
| `customer-communication/message-participation-access@1` | `CONDUCT_CUSTOMER_COMMUNICATION` | BUSINESS + GROWTH | `PLATFORM_SERVICE_ACCESS` |
| `customer-communication/automated-response-access@1` | `AUTOMATE_ROUTINE_CUSTOMER_SERVICE` | BUSINESS + GROWTH | `PLATFORM_SERVICE_ACCESS` |

FREE SHALL NOT receive either protected purpose through the standard catalogue.

The following exact bounded Customer Communication contracts SHALL require **NO INDEPENDENT COMMERCIAL ENTITLEMENT**:

```text
customer-communication/non-committing-human-preparation-access@1

customer-communication/existing-conversation-observation-access@1

customer-communication/committed-communication-progression-access@1
```

No `CommercialEntitlementIdentity` is minted by this amendment.

Missing classification is not an exemption.

---

# 2. Ownership Boundary

Customer Communication continues to own:

```text
Conversation identity

Conversation creation/reuse decisions

Conversation participant bindings

Guest Conversation Access Grants

Conversation channel bindings

Conversation-bound reply routes

immutable accepted ConversationMessages

CustomerServiceResponseAssessment

communication continuity projections

Customer Communication-owned
acceptance/recovery evidence
```

Commercial continues to own:

```text
CommercialEntitlementIdentity

CommercialEntitlementDefinition

Commercial Access Binding

grant provenance

effective commercial permission

plan-revision grant sets

catalogue publication
```

Merchant Attention continues to own:

```text
Attention occurrence

handling facts

human-handling coordination
```

Notification continues to own:

```text
dispatch

provider delivery attempt

delivery/read evidence
```

Every related Order, Booking, Appointment, Payment, Refund, Return or other business capability retains its own business truth and mutation authority.

Commercial permission SHALL NOT transfer any of those ownership boundaries.

---

# 3. Protected Customer Communication Participation

`customer-communication/message-participation-access@1` SHALL govern successful establishment of materially new Customer Communication activity.

It protects:

```text
new Conversation creation
+
new ConversationMessage acceptance
```

where those effects occur through the accepted Customer Communication portfolio.

The protected purpose is:

```text
CONDUCT_CUSTOMER_COMMUNICATION
```

This purpose applies regardless of whether the new Message author is:

```text
customer-side participant

merchant-side human participant

or
AUTOMATED_ASSISTANCE
```

where another applicable authority also permits that author basis.

Commercial permission does not establish participant identity, Conversation access, Message validity, customer identity, provider readiness, business-subject authority or actor authority.

---

# 4. Website Message Creation

`WEBSITE_MESSAGE_CREATE_V1` SHALL require current satisfaction of:

```text
CONDUCT_CUSTOMER_COMMUNICATION
```

for the exact Merchant Scope whose Customer Communication service is being used.

This remains true even though the initiating visitor may be public or unauthenticated.

The commercial permission belongs to the merchant's Customer Communication service.

It is not a customer subscription requirement.

Therefore:

```text
public customer
    may invoke permitted merchant surface

merchant's service
    must be commercially admitted

customer
    does not need a GrandRue plan
```

The permission is additional to:

- accepted merchant configuration/activation;
- exact trusted Merchant Scope;
- supported content;
- abuse/resource-protection admission;
- participant requirements;
- logical retry identity; and
- every other existing `WEBSITE_MESSAGE_CREATE_V1` predicate.

FREE Enquiry remains independently governed.

A FREE merchant's ability to receive an accepted Enquiry SHALL NOT be reinterpreted as FREE durable Customer Messaging.

---

# 5. Existing-Conversation Message Continuation

Acceptance of a new Message into an existing Conversation SHALL require:

```text
CONDUCT_CUSTOMER_COMMUNICATION
```

This includes applicable execution through:

```text
EMAIL_REPLY_CONTINUE_V1

CONVERSATION_BROWSER_CONTINUE_V1

merchant-authored Conversation response

other already-accepted Message-append path
within the current MS-PROT-086 portfolio
```

The rule applies to both customer-side and merchant-side new Message acceptance.

A customer-side credential, Guest Conversation Access Grant, participant relationship or valid Conversation-bound email route establishes only its independently accepted access/correlation authority.

It does not establish the merchant's commercial permission to continue operating the communication service.

Conversely, commercial permission does not establish participant or route authority.

---

# 6. No Free Existing-Conversation Continuation

This amendment deliberately does **not** classify arbitrary new Message continuation as residual no-entitlement activity merely because a Conversation already exists.

Canonical:

```text
existing Conversation
    ≠
unlimited free future communication
```

MS-PROT-086 deliberately has no universal:

```text
OPEN
CLOSED
RESOLVED
```

Conversation lifecycle.

Therefore a rule equivalent to:

```text
if Conversation already exists
    new Messages are always free
```

would create an effectively unbounded commercial bypass.

A merchant could establish one Conversation under BUSINESS and continue arbitrary new communication indefinitely after commercial permission ended.

That alternative is rejected.

---

# 7. Existing Conversation Observation

`customer-communication/existing-conversation-observation-access@1` SHALL require **NO INDEPENDENT COMMERCIAL ENTITLEMENT**.

It governs bounded observation of already-accepted Customer Communication material.

It MAY support, where independently authorised:

```text
merchant observation of an existing Conversation

merchant observation of existing Messages

CONVERSATION_BROWSER_VIEW_V1

REGISTERED_CONVERSATION_DISCOVERY_V1

bounded recovery/display of retained
existing communication history
```

Observation remains subject to every applicable:

```text
Merchant Scope

participant/customer relationship

Actor Authorisation

Guest Conversation Access Grant

Projection/Exposure

Data Protection / lifecycle

retention

redaction

serviceability

security

Resource Protection
```

requirement.

No-entitlement observation does not grant the ability to append another Message.

It does not reactivate a disabled Customer Communication service.

---

# 8. Why Historical Observation Is Not a FREE Messaging Grant

Existing Conversation observation is classified separately from new communication.

Canonical:

```text
observe retained communication history
    ≠
conduct new customer communication
```

A merchant whose BUSINESS entitlement ends may retain authorised visibility of existing communication without thereby receiving:

```text
new website Conversations

new customer Message append

new merchant Message send

new email continuation

new browser continuation writes

automated factual responses
```

This preserves history and operational honesty without converting the BUSINESS communication service into a FREE service.

---

# 9. Non-Committing Human Preparation

`customer-communication/non-committing-human-preparation-access@1` SHALL require **NO INDEPENDENT COMMERCIAL ENTITLEMENT**.

It MAY permit bounded human preparation that does not yet establish a ConversationMessage or external effect.

Examples include:

```text
typing a merchant response draft

editing an unsent human draft

reviewing content before explicit send

preparing a response through
an Attention Candidate Action
before Message acceptance
```

Commercial permission SHALL be revalidated before the protected Message-acceptance boundary.

Therefore:

```text
draft permitted
    ≠
Message send permitted
```

A draft surviving a commercial change does not preserve authority to send.

---

# 10. Automated Drafts Are Not Human Preparation

The no-entitlement human-preparation classification SHALL NOT be used to bypass the automated-response boundary.

A system-generated draft produced through the accepted MS-PROT-086 v1.3 Customer-Service Response Contract portfolio remains automated customer-service use.

Where the system creates:

```text
DRAFT_FOR_HUMAN_REVIEW
```

through that portfolio, the applicable automated-response commercial permission SHALL already have been satisfied.

Human review of that draft does not turn its generation retrospectively into free manual preparation.

Sending the reviewed draft as a Message additionally requires the ordinary Message-participation permission applicable at final acceptance.

---

# 11. Automated Customer-Service Commercial Boundary

`customer-communication/automated-response-access@1` SHALL protect:

```text
AUTOMATE_ROUTINE_CUSTOMER_SERVICE
```

for the accepted MS-PROT-086 v1.3 response portfolio.

The protected purpose includes applicable execution of:

```text
response-contract resolution

request-family assessment

ResponseEvidenceBundle establishment

coverage evaluation

automated clarification

AI-assisted or deterministic
system-generated response drafting

automated substantive response generation

deterministic response validation

automated-response final acceptance path
```

where those activities occur as part of the admitted automated customer-service service.

The exact seven response families remain unchanged.

No new response family is created by this amendment.

---

# 12. Automated Response Requires the Communication Substrate

An automated response that becomes a new `ConversationMessage` SHALL satisfy both applicable commercial boundaries:

```text
CONDUCT_CUSTOMER_COMMUNICATION
+
AUTOMATE_ROUTINE_CUSTOMER_SERVICE
```

The first permits new Customer Communication participation.

The second permits use of the automated factual-response service.

This is not duplicate semantic ownership.

Canonical:

```text
Customer Communication
    owns Message acceptance

Customer-Service Response Contract
    owns automated-response eligibility

Commercial
    independently evaluates both
protected purposes
```

Under the standard catalogue, BUSINESS and GROWTH contain both purposes.

No runtime tier-name comparison is permitted.

---

# 13. Human-Required Assessment and Attention Handoff

Where an authorised automated response assessment produces:

```text
DRAFT_FOR_HUMAN_REVIEW

HUMAN_RESPONSE_REQUIRED

or applicable UNRESOLVED
human-handoff outcome
```

the assessment itself remains historical Customer Communication evidence once committed.

Subsequent contribution to:

```text
customer-communication/human-response-required@1
```

and Merchant Attention establishment/recovery remain governed by MS-PROT-085 v1.4.

No additional Attention entitlement is introduced.

Commercial loss after the authoritative assessment SHALL NOT:

```text
delete the assessment

rewrite its outcome

erase an established Attention occurrence

prevent qualified post-assessment
Attention recovery
```

It may prevent a later new ConversationMessage if the applicable current Message-participation permission is no longer satisfied.

---

# 14. Committed Communication Progression

`customer-communication/committed-communication-progression-access@1` SHALL require **NO INDEPENDENT COMMERCIAL ENTITLEMENT**.

Its scope is strictly post-commit progression/recovery for a Customer Communication fact that already committed while its required authority was satisfied.

It MAY include:

```text
idempotent recovery of an accepted
Conversation / Message result

recovery after response acknowledgement loss

post-commit handoff of accepted Message
to Notification delivery

post-commit contribution of an accepted
response assessment to Merchant Attention

post-commit projection/reaction convergence

security/lifecycle revocation or cleanup
required to prevent continued unauthorised use
```

It SHALL NOT include acceptance of another customer or merchant Message.

It SHALL NOT establish another Conversation.

It SHALL NOT re-run automated customer service as new use.

---

# 15. Message Acceptance Before Commercial Loss

Where a Message committed while all required authority was valid:

```text
Message accepted
        ↓
commercial permission later changes
        ↓
delivery / projection / Attention reaction
still converges according to its own authority
```

Commercial change SHALL NOT roll back or delete the accepted Message.

Notification delivery may continue according to its independently governed supporting-service and delivery rules.

Commercial change does not turn:

```text
Message accepted
```

into:

```text
Message never existed
```

---

# 16. Commercial Loss Before Message Acceptance

Where entitlement is lost before final Message acceptance, the send/append operation SHALL fail current commercial revalidation.

This includes:

```text
merchant draft prepared earlier

automated response assessed earlier

browser composer left open

customer continuation prepared earlier

provider-side event received
but not yet accepted as a ConversationMessage
```

None of those facts alone preserves permission to create a new Message.

Where an external provider effect already occurred before local uncertainty was resolved, existing acknowledgement/reconciliation authority governs.

Commercial denial MUST NOT fabricate whether the external effect occurred.

---

# 17. Customer Browser Access After Commercial Change

For an existing Conversation:

```text
CONVERSATION_BROWSER_VIEW_V1
```

may continue under the no-independent-entitlement observation classification where its registered-participant or valid Guest Conversation Access authority and all lifecycle/protection requirements remain satisfied.

However:

```text
CONVERSATION_BROWSER_CONTINUE_V1
```

requires current `CONDUCT_CUSTOMER_COMMUNICATION` because it accepts new Message activity.

Therefore:

```text
valid Guest Conversation Access Grant
    ≠
commercial permission to append

commercial permission to append
    ≠
valid Guest Conversation Access Grant
```

The two predicates remain independent.

---

# 18. Conversation-Bound Email After Commercial Change

A retained Conversation-bound reply route does not preserve paid service permission.

If a customer email event arrives after the relevant Customer Communication permission is absent:

```text
valid provider event
+
valid route
+
valid correlation
```

is insufficient to append a new ConversationMessage.

Current `CONDUCT_CUSTOMER_COMMUNICATION` remains required at Message acceptance.

The provider event MAY be retained, rejected, quarantined or otherwise handled only according to applicable provider/security/lifecycle authority.

GrandRue SHALL NOT silently append the Message and call it residual access.

---

# 19. Merchant Human Response After Commercial Change

A merchant may continue to observe an existing Conversation where Section 7 permits observation.

The merchant may also perform bounded non-committing preparation under Section 9.

But final acceptance of a new merchant response through Customer Communication requires current:

```text
CONDUCT_CUSTOMER_COMMUNICATION
```

If that permission is absent, Merchant Attention may still permit:

```text
HANDLED_OUTSIDE_MAIN_STREET_RECORDED
```

where MS-PROT-085 v1.4's exact source-observation and actor requirements are satisfied.

This provides an honest escape path without silently granting perpetual free messaging.

---

# 20. Relationship to Residual Commitment Access

MS-PROT-056 v1.5 permits owner-qualified residual access required to resolve pre-existing authoritative business commitments.

This amendment does not create a wildcard Customer Communication residual-send permission.

A Conversation is communication continuity, not itself:

```text
Booking commitment

Appointment commitment

Order commitment

Payment obligation

Refund obligation

or other source-business commitment
```

If a future accepted source-owner contract requires a specific Customer Communication operation as unavoidable residual commitment resolution, that exact purpose may be classified separately through the normal design lifecycle.

Until then:

```text
existing source commitment
    ≠
unbounded free Conversation continuation
```

Source-owner residual operations remain independently available under their accepted authorities.

---

# 21. Routine Automated Response Downgrade

Loss of `AUTOMATE_ROUTINE_CUSTOMER_SERVICE` SHALL stop new protected automated-response use.

It SHALL NOT:

```text
delete historical automated Messages

rewrite response assessments

erase response provenance

convert automated Messages
into human-authored Messages

erase Attention occurrences

or manufacture NO_RESPONSE_REQUIRED
```

A previously accepted automated Message remains an accepted Message.

A previously committed human-handoff assessment may continue its qualified Attention progression under MS-PROT-085 v1.4.

Fresh automated evaluation, clarification, generated draft or substantive response requires current applicable automated-service permission.

---

# 22. Public Enquiry Boundary

FREE Enquiry and BUSINESS Customer Communication remain distinct.

Canonical:

```text
Enquiry
    = accepted request/contact truth
      under composite MS-PROT-043

Conversation / Message
    = durable two-way communication continuity
      under composite MS-PROT-086
```

FREE Enquiry SHALL NOT be silently upgraded into free durable Conversation infrastructure merely because both permit a customer to contact a merchant.

Conversely, BUSINESS Customer Communication does not redefine or replace Enquiry.

An information-only FREE merchant may still use the accepted FREE Enquiry portfolio without acquiring Customer Communication.

---

# 23. Notification and Delivery

Customer Communication commercial permission governs the Customer Communication service.

It does not create a separate Notification premium toll.

Once an outbound Message is authoritatively accepted:

```text
accepted Message
        ↓
Notification / dispatch
        ↓
provider attempt
        ↓
delivery evidence
```

remains governed by composite MS-PROT-075 and applicable supporting-service rules.

Notification success does not establish Customer Communication permission.

Notification failure does not erase Customer Communication truth.

---

# 24. Provider Boundary

No messaging or email provider acquires Commercial authority.

Provider:

```text
route possession

event delivery

provider account state

provider thread identity

provider message acceptance

provider delivery success
```

does not satisfy either:

```text
CONDUCT_CUSTOMER_COMMUNICATION

AUTOMATE_ROUTINE_CUSTOMER_SERVICE
```

Provider readiness remains independently required where applicable.

Commercial permission does not make an unavailable provider ready.

---

# 25. AI Boundary

AI does not acquire Commercial authority.

An AI model SHALL NOT:

```text
activate Customer Communication

grant Message participation

grant automated-response permission

infer BUSINESS/GROWTH from context

bypass a missing Commercial Access Binding

preserve send permission after commercial loss

or turn model confidence into entitlement
```

Live AI unavailability SHALL NOT prevent independently valid human Customer Communication.

Where deterministic/manual operation remains available, optional AI failure does not manufacture service unavailability.

---

# 26. Resource Protection

Commercial entitlement does not mean unbounded use.

Customer Communication remains independently subject to Resource Protection, abuse and fairness controls.

Applicable bounded controls MAY include:

```text
Message size

Conversation creation rate

Message rate

discovery page bounds

provider dispatch bounds

automated-response inference budget

clarification bounds

retry limits

concurrency
```

A Resource Protection denial SHALL remain distinct from commercial denial.

Resource pressure SHALL NOT automatically imply a higher commercial tier unless separately accepted under `MS-PROT-056-V17-DQ-003`.

---

# 27. Historical Preservation

Commercial change SHALL NOT delete or rewrite:

```text
Conversation identity

ConversationMessage

participant provenance

channel provenance

Guest Conversation Access Grant history

response assessment

automated-response provenance

Attention contribution provenance

delivery evidence

retained audit evidence
```

Retention and disposition remain governed by composite MS-PROT-053.

Commercial permission and data-retention authority remain distinct.

Retained history does not automatically remain externally visible; current observation still requires all applicable non-commercial predicates.

---

# 28. Exact Catalogue Consequence

For `MS-PROT-056-V17-DQ-001`, this amendment contributes exactly two protected Customer Communication purposes:

```text
customer-communication/message-participation-access@1
    → CONDUCT_CUSTOMER_COMMUNICATION
    → BUSINESS + GROWTH
    → PLATFORM_SERVICE_ACCESS

customer-communication/automated-response-access@1
    → AUTOMATE_ROUTINE_CUSTOMER_SERVICE
    → BUSINESS + GROWTH
    → PLATFORM_SERVICE_ACCESS
```

and exactly three no-independent-entitlement classifications:

```text
customer-communication/non-committing-human-preparation-access@1

customer-communication/existing-conversation-observation-access@1

customer-communication/committed-communication-progression-access@1
```

The final Commercial-owned catalogue SHALL assign stable `CommercialEntitlementIdentity` values to the protected target/purpose pairs.

This amendment SHALL NOT invent those identities.

The no-independent-entitlement contracts SHALL be represented explicitly rather than inferred from missing bindings.

---

# 29. No Channel-by-Channel Commercial Fragmentation

The current portfolio SHALL NOT create separate commercial purposes such as:

```text
USE_WEBSITE_CHAT

USE_EMAIL_REPLY

USE_GUEST_BROWSER_MESSAGES

USE_REGISTERED_BROWSER_MESSAGES
```

merely because different transport/access contracts exist.

Those routes currently deliver one commercial value:

```text
CONDUCT_CUSTOMER_COMMUNICATION
```

Channel/provider differences remain semantic, access, transport and operational concerns.

A future materially different channel or service MAY justify a new commercial classification only through the normal design lifecycle.

---

# 30. No Per-Response-Family Commercial Fragmentation

The seven accepted MS-PROT-086 v1.3 Customer-Service Response Contract families SHALL initially share:

```text
AUTOMATE_ROUTINE_CUSTOMER_SERVICE
```

GrandRue SHALL NOT create separate standard commercial purposes for:

```text
hours questions

offering questions

policy questions

Scheduling availability questions

Booking/Appointment questions

Order/Fulfilment/Shipment questions

Payment/Refund questions
```

merely because their source evidence differs.

Their semantic ownership, evidence and access requirements remain independently exact.

Commercial packaging need not mirror every semantic family.

---

# 31. Failure Honesty

The following outcomes SHALL remain distinguishable:

| Condition | Required interpretation |
|---|---|
| Required Customer Communication commercial purpose absent | Commercial access denial |
| Commercial decision cannot be established technically | Commercial evaluation unavailable, not authoritative denial |
| Message already committed but result acknowledgement lost | Recover committed result |
| Draft exists but send permission ended | Draft/history preserved; new Message denied |
| Provider event exists but Message acceptance is commercially denied | Provider evidence does not become ConversationMessage |
| Existing Conversation is observable but continuation denied | History access without new communication permission |
| Automated assessment committed but auto permission later ends | Assessment preserved; no fresh automated use |
| Message committed but Notification fails | Message remains; delivery failure is separate |
| Actor authority ended | Actor-authorisation rejection |
| Guest grant invalid/expired | Customer contextual-access rejection |
| Data lifecycle prohibits ordinary use | Data-use/lifecycle restriction |
| Resource limit rejects use | Resource Protection decision |

A failure in one predicate SHALL NOT be represented as another.

---

# 32. Concurrency and Idempotency

Existing MS-PROT-086 logical-operation identity, Conversation identity, Message identity and retry rules remain unchanged.

Commercial evaluation SHALL NOT replace those controls.

A retry of the same already-committed logical Message operation SHALL recover/converge on the committed result under the bounded progression classification.

It SHALL NOT require a second commercial purchase merely because acknowledgement was lost.

Reuse of the logical identity for materially different new communication remains conflict/rejection according to existing authority.

---

# 33. Falsification

| Challenge | Required result |
|---|---|
| FREE merchant uses ordinary Enquiry | Enquiry remains available; no Conversation/Message entitlement is manufactured |
| FREE merchant tries `WEBSITE_MESSAGE_CREATE_V1` without another valid grant | New Customer Communication denied |
| BUSINESS merchant receives website Message | `CONDUCT_CUSTOMER_COMMUNICATION` admits the commercial predicate; normal semantic/security predicates still apply |
| Customer replies to existing Conversation | New Message requires current communication permission |
| Merchant responds to existing Conversation | New Message requires current communication permission |
| BUSINESS ends after Conversation exists | Existing authorised history may remain observable; new Message activity is denied absent another valid grant |
| Guest grant remains valid after BUSINESS ends | View may remain if otherwise authorised; append still requires commercial permission |
| Registered customer discovers old Conversations after downgrade | Bounded retained discovery/observation may continue; discovery does not permit append |
| Merchant typed draft before downgrade | Draft survives; final send revalidates and may be denied |
| Provider webhook arrives after downgrade | Valid webhook/route does not by itself append a Message |
| Outbound Message commits before downgrade; delivery occurs later | Message remains and qualified delivery progression continues |
| Automated assessment commits; automated entitlement ends before send | No automatic send; committed assessment survives; qualified Attention handoff can converge |
| Human-response Attention occurrence exists after downgrade | Observation may continue if source observation is valid; human Message send still requires communication permission |
| Merchant handles customer by telephone | MS-PROT-085 outside-handling evidence may apply; no ConversationMessage is fabricated |
| AI is unavailable | Human communication can continue where authorised |
| Customer asks for refund in Message | Communication permission grants no Refund authority |
| Notification provider fails | Message truth survives; delivery failure remains Notification-owned |
| Runtime checks `plan == BUSINESS` | Rejected; exact commercial purposes/bindings are required |
| One Conversation established while BUSINESS then used indefinitely after downgrade | New continuation is denied; no free-continuation loophole |
| Automated public-hours response | Both communication substrate and automated-response permission are required; source evidence/validation remain independent |
| Missing final catalogue binding | Not interpreted as permission |

**Falsification result:** PASS.

---

# 34. Alternatives Considered

## 34.1 One Blanket Entitlement for Every Customer Communication Operation

**Rejected.**

It would make ordinary observation of retained history depend permanently on current paid service and would conflate new activity, observation, recovery and automated service use.

## 34.2 No Commercial Entitlement for Customer Communication

**Rejected.**

It contradicts MS-PROT-056 v1.7's BUSINESS allocation and would make durable Conversation/Message service effectively FREE.

## 34.3 Protect Merchant Writes but Allow Customer Writes Without Commercial Permission

**Rejected.**

Customer-authored Message acceptance still consumes the merchant's Customer Communication service.

A public or guest actor does not need a subscription, but the merchant-scoped service must be commercially authorised.

## 34.4 All Existing-Conversation Continuation Is Residual Free Access

**Rejected.**

MS-PROT-086 deliberately lacks a universal Conversation closure state.

Free continuation would therefore permit indefinite new communication after one historically paid Conversation.

## 34.5 Block Historical Observation After Downgrade

**Rejected.**

It would unnecessarily make current subscription state rewrite practical access to retained accepted communication history and would obstruct bounded handling/audit without protecting new Customer Communication activity.

## 34.6 Separate Entitlement Per Channel

**Rejected.**

Website, email and browser continuation currently implement one Customer Communication value proposition.

Provider/channel structure is not a commercial taxonomy.

## 34.7 Separate Entitlement Per Automated Response Family

**Rejected.**

The seven families differ semantically because their evidence owners differ, not because the merchant is buying seven separate customer-service products.

## 34.8 Direct BUSINESS/GROWTH Runtime Checks

**Rejected.**

Tier placement belongs to immutable Commercial catalogue grants.

Customer Communication consumes exact protected-purpose decisions.

## 34.9 Selected Alternative

**Selected:** protect new Customer Communication participation and automated-response use through two exact BUSINESS+GROWTH commercial purposes while explicitly classifying existing observation, human non-committing preparation and committed progression as no-independent-entitlement paths.

---

# 35. Hard Invariants

1. Customer Communication defines exactly two initially protected commercial purposes.
2. `CONDUCT_CUSTOMER_COMMUNICATION` is BUSINESS + GROWTH.
3. `AUTOMATE_ROUTINE_CUSTOMER_SERVICE` is BUSINESS + GROWTH.
4. FREE receives neither protected purpose through the standard catalogue.
5. No final `CommercialEntitlementIdentity` is minted by this amendment.
6. Runtime SHALL NOT branch on BUSINESS/GROWTH names.
7. `WEBSITE_MESSAGE_CREATE_V1` requires the communication purpose.
8. New Message continuation requires the communication purpose.
9. Customer-side authorship does not exempt the merchant-scoped communication service.
10. Customers never require a GrandRue subscription merely to invoke an authorised merchant surface.
11. Existing Conversation observation has no independent Commercial Entitlement.
12. Observation does not permit Message append.
13. Registered discovery has no independent Commercial Entitlement.
14. Guest/registered browser view does not imply browser continue authority.
15. Non-committing human preparation has no independent Commercial Entitlement.
16. Draft preparation does not preserve final send authority.
17. Automated draft generation is not disguised as free human preparation.
18. Automated-response service requires its exact protected purpose.
19. Automated Message acceptance also remains subject to the underlying communication-purpose requirement.
20. No per-channel commercial fragmentation is introduced.
21. No per-response-family commercial fragmentation is introduced.
22. Existing Conversation identity does not create unlimited free continuation.
23. An accepted Message is not rolled back by later commercial loss.
24. Committed recovery/progression has no second commercial toll.
25. Provider evidence does not establish commercial permission.
26. AI does not establish commercial permission.
27. Notification delivery remains independently owned.
28. Merchant Attention remains independently owned.
29. Source-business mutation authority remains independently owned.
30. Historical communication retention remains Data Protection-owned.
31. Resource Protection remains independent from commercial permission.
32. Missing binding information is not an exemption.
33. Commercial denial does not claim semantic inapplicability.
34. Commercial evaluation failure does not become authoritative denial.
35. DQ-004 Conversation attachments remain outside this commercial classification until semantically admitted.
36. `MS-PROT-056-V17-DQ-001` remains OPEN.
37. Acceptance authorises no implementation.

---

# 36. DQ-001 Effect

Upon acceptance, the current MS-PROT-086 owner classification would be complete for the already-accepted non-attachment Customer Communication portfolio.

The exact protected owner contracts would be:

```text
customer-communication/message-participation-access@1
    → CONDUCT_CUSTOMER_COMMUNICATION
    → BUSINESS + GROWTH

customer-communication/automated-response-access@1
    → AUTOMATE_ROUTINE_CUSTOMER_SERVICE
    → BUSINESS + GROWTH
```

The exact no-independent-entitlement contracts would be:

```text
customer-communication/non-committing-human-preparation-access@1

customer-communication/existing-conversation-observation-access@1

customer-communication/committed-communication-progression-access@1
```

`MS-PROT-056-V17-DQ-001` would remain **OPEN**.

Still outstanding would include:

```text
final CommercialEntitlementIdentity values

Commercial-owned exact bindings

remaining owner/supporting classifications
outside the now-complete Customer Communication slice

complete FREE grant set

complete BUSINESS grant set

complete GROWTH grant set

cross-binding consistency validation

complete Commercial Catalogue Manifest

explicit approval of that manifest
```

No `MS-PROT-086-DQ-*` row is reopened.

`MS-PROT-086-DQ-004` remains:

```text
DEFERRED — ACTIVE BEFORE PRODUCTION MESSAGE ATTACHMENTS
```

and future attachment commercial classification cannot be inferred from this amendment.

---

# 37. Fundamental Vision Result

**VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

The merchant sees a simple commercial proposition:

```text
BUSINESS
    → customers and the business can communicate
    → routine factual responses can be automated
```

A downgrade does not erase communication history.

Nor does one historical Conversation become a loophole for perpetual free messaging.

Customers do not need GrandRue accounts or subscriptions merely to interact with an authorised merchant surface.

Internal distinctions among Message participation, observation, automation, Attention, Notification, security and source-business authority remain hidden infrastructure necessary for correctness.

---

# 38. Governance Outcome

**Fundamental Vision review:** PASS.

**Architecture review:** PASS.

**Commercial-boundary review:** PASS.

**Residual-access review:** PASS.

**Supporting-service review:** PASS.

**Authority-separation review:** PASS.

**Falsification review:** PASS.

**Ambiguity review:** PASS.

The proposal distinguishes:

```text
existing Conversation
    ≠
permission for new Message

Conversation observation
    ≠
Conversation participation

customer access proof
    ≠
merchant commercial permission

Customer Communication permission
    ≠
automated customer-service permission

automated-response permission
    ≠
source-business access

Message accepted
    ≠
delivered

Attention occurrence
    ≠
Message-send authority

Commercial Entitlement
    ≠
Actor Authorisation

Commercial Entitlement
    ≠
semantic activation
```

**RECOMMENDATION:** ACCEPT.

**Manual approval:** GRANTED — 16 September 2026.

**Repository formalisation:** COMPLETE FOR THIS AUTHORITY DOCUMENT.

**Implementation promotion:** NONE.

Acceptance authorises formalisation of this exact Customer Communication commercial-access classification only.

It does not authorise final Commercial Entitlement identities, a concrete catalogue manifest, attachment support, merchant activation or production implementation.
