# MS-PROT-086 v1.2 — Conversation Browser Access, Resume & View Contract Amendment

**Document ID:** MS-PROT-086  
**Version:** 1.2  
**Status:** **ACCEPTED after authority review, Fundamental Vision review, architecture review, falsification, ambiguity review and manual approval**  
**Approved:** Manual approval on 8 September 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.2; `DOCUMENT-GOVERNANCE.md`  
**Amends:** Composite MS-PROT-086 through v1.1 only within customer browser access, Conversation resume/view/continue authority, registered-customer participation access and guest purpose-bound access  
**Depends on:** MS-PROT-028; MS-PROT-031; composite MS-PROT-035; composite MS-PROT-043; composite MS-PROT-053 through v1.3; MS-PROT-056; MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; MS-PROT-067; MS-PROT-073; ADR-014  
**Resolves:** `MS-PROT-086-DQ-003` — production Guest Conversation Access mechanism  
**Implementation activation:** **NONE**

---

# 1. Purpose

This amendment defines how customers may securely return to, view and continue an existing Main Street Conversation through a merchant-branded browser surface.

It resolves two materially different access cases:

1. authenticated registered-customer access; and
2. guest possession-bound access.

It preserves the distinction:

```text
customer identity
    ≠
guest contextual access
    ≠
email endpoint
    ≠
Conversation identity
```

---

# 2. Governing Decision

Main Street SHALL support two browser-access authority paths:

```text
REGISTERED CUSTOMER

authenticated CustomerAccount/Identity context
        ↓
current Merchant Scope
        ↓
authoritatively associated CustomerContext
        ↓
exact Conversation participation
        ↓
customer-contextual read/write authority
```

and:

```text
GUEST

valid Guest Conversation Access Proof
        ↓
exact Guest Conversation Access Grant
        ↓
exact Merchant Scope
        ↓
exact Conversation
        ↓
bounded read/write authority
```

The two paths SHALL remain independent.

Neither path may infer authority from mutable contact-value equality.

---

# 3. Browser Access Is Not Conversation Identity

Conversation browser access does not create or replace Conversation identity.

The following remain distinct:

```text
Conversation
    durable communication continuity

ConversationMessage
    immutable accepted contribution

CustomerContext
    merchant-scoped customer relationship

CustomerAccount
    authenticated customer-access mechanism

Guest Conversation Access Grant
    narrow authorization to one Conversation

Guest Conversation Access Proof
    possession evidence for that Grant
```

A browser credential, URL, cookie, session or other delivery mechanism SHALL NOT become canonical Conversation identity.

---

# 4. Browser Access Is Not Public Access

A public merchant website may permit anonymous creation of a new Conversation.

That does not make an existing Conversation publicly readable.

Canonical:

```text
PUBLIC
    → may create new Conversation
      under WEBSITE_MESSAGE_CREATE_V1

CUSTOMER_CONTEXTUAL
    → may view/continue existing Conversation
      only after registered or guest authority
```

Knowledge of:

```text
Conversation ID
customer email
customer name
merchant name
message text
URL path
```

does not establish existing-Conversation access.

---

# 5. Canonical Browser Contracts

This amendment defines three logical Customer Messaging browser contracts.

## 5.1 CONVERSATION_BROWSER_VIEW_V1

Returns the bounded customer-visible projection of one existing Conversation.

Eligible authority basis:

```text
registered participant authority
or
Guest Conversation Access Grant
```

## 5.2 CONVERSATION_BROWSER_CONTINUE_V1

Accepts one supported customer Message into an existing Conversation.

Eligible authority basis:

```text
registered participant authority
or
Guest Conversation Access Grant
```

The Message remains governed by the existing ConversationMessage acceptance contract.

## 5.3 REGISTERED_CONVERSATION_DISCOVERY_V1

Returns a bounded merchant-scoped list of Conversations to which the currently authenticated registered customer has current participant-based access.

Guest access SHALL NOT support Conversation enumeration/discovery.

These are logical contracts. Physical URI, controller, cookie, DTO and database representation remain downstream.

---

# 6. Registered-Customer Access

Registered-customer browser access requires:

```text
valid authenticated customer execution context
+
exact Merchant Scope
+
authoritatively resolved CustomerContext
+
Conversation participant relationship
+
current read/write authorization
+
applicable runtime admission
```

Authentication alone is insufficient.

Therefore:

```text
logged in
    ≠
may read every Conversation with matching email
```

and:

```text
logged in
    ≠
may read every Conversation associated with the merchant
```

---

# 7. Registered Participant Identity

Where `WEBSITE_MESSAGE_CREATE_V1` was invoked by a signed-in registered customer under MS-PROT-086 v1.1, the bound authenticated CustomerContext remains the registered participant identity.

Browser continuation SHALL use that relationship.

Changing the customer's:

```text
email
phone
display name
```

does not break or recreate Conversation participant identity where CustomerContext remains authoritative.

---

# 8. Registered Conversation Discovery

A signed-in customer MAY receive a bounded Conversation list for the current Merchant Scope.

Discovery SHALL include only Conversations whose customer-side access is supported by an authoritative participant relationship available to that authenticated customer context.

It SHALL NOT provide:

```text
cross-merchant universal inbox
all Conversations with same email
all Conversations with same phone
all merchant CustomerContexts
all merchant communication history
```

The list SHALL expose only minimum useful customer-facing summary information.

The exact UI, pagination size and presentation wording remain downstream.

---

# 9. CustomerContext Reconciliation Boundary

Merchant-side CustomerContext reconciliation SHALL NOT automatically expand customer-side Conversation access.

Canonical:

```text
C2 RECONCILED_TO C1
```

may support an authorised merchant-facing unified-history projection.

It does not by itself mean:

```text
CustomerAccount associated with C1
    → automatically owns every Conversation historically attached to C2
```

Customer-facing access requires independently valid customer-side association/participation authority.

This prevents an incorrect merchant reconciliation from becoming a customer-data disclosure.

---

# 10. Guest Conversation Access Grant

A **Guest Conversation Access Grant** is a server-authoritative, purpose-bound authorization relationship permitting a guest contextual principal to access one Conversation.

It SHALL be bound to exactly:

```text
one Merchant Scope
one Conversation
one guest participant/continuity context
one permitted operation set
one grant lifecycle
```

The initial permitted operation set is exactly:

```text
VIEW
APPEND_TEXT_MESSAGE
```

The Grant does not authorize:

```text
CustomerContext access
CustomerAccount access
other Conversations
Booking/Order/Payment records
merchant operational data
provider administration
identity reconciliation
attachments
account creation
```

---

# 11. Guest Conversation Access Proof

A **Guest Conversation Access Proof** is the Conversation-specific specialization of the accepted MS-PROT-063 Contextual Access Proof.

Possession establishes only:

```text
proof of authority to invoke
the exact Guest Conversation Access Grant
```

It does not establish:

```text
legal identity
CustomerContext identity
email ownership
CustomerAccount identity
ownership of unrelated business objects
```

---

# 12. Guest Grant Establishment

For a guest invocation of `WEBSITE_MESSAGE_CREATE_V1`, where Conversation browser continuation is applicable, successful creation SHALL establish one Guest Conversation Access Grant associated with the newly created Conversation.

The logical acceptance boundary becomes:

```text
new Conversation
+
first ConversationMessage
+
guest participant/continuity binding
+
reply-email endpoint
+
Guest Conversation Access Grant
+
mandatory provenance/audit facts
```

The durable Grant relationship SHALL be established consistently with the accepted website-message creation result.

A registered signed-in customer SHALL NOT simultaneously receive a guest grant merely as a convenience fallback.

---

# 13. Grant and Credential Separation

Main Street SHALL distinguish:

```text
Guest Conversation Access Grant
    server-authoritative access relationship

Guest Conversation Access Proof/Credential
    secret possession evidence
```

Credential rotation or reissue SHALL NOT create:

```text
new Conversation
new participant
new CustomerContext
new Grant family
new absolute lifetime
```

unless an independently authorised new grant operation exists.

---

# 14. Guest Grant Lifetime

The initial Guest Conversation Access Grant SHALL use:

```text
idle expiry:
    90 days

absolute lifetime:
    12 calendar months
```

The absolute lifetime begins when the Grant is established.

The initial idle clock begins at Grant establishment.

A successful authorised browser:

```text
view
or
Message append
```

MAY advance the Grant's last-authorised-use time.

Failed, malformed, rejected or unauthorised attempts SHALL NOT extend it.

---

# 15. Absolute Lifetime Is a Hard Cap

Credential rotation, browser refresh, Message creation, merchant response and successful use SHALL NOT move the Grant-family absolute-expiry boundary.

Canonical:

```text
grantEstablishedAt
    + 12 calendar months
        =
maximum initial guest browser-access lifetime
```

At the absolute boundary, the Grant expires unless another separately accepted authority has established a new grant through a new trusted process.

---

# 16. Grant Expiry Does Not Delete the Conversation

Guest browser-access lifetime and communication retention are independent.

Therefore:

```text
Guest Grant expired
    ≠
Conversation deleted

Guest Grant expired
    ≠
Message disposed

Message retained
    ≠
Guest Grant remains valid
```

Communication retention remains governed by MS-PROT-053 v1.3.

---

# 17. Browser Use Does Not Extend Message Retention

Viewing, scrolling, refreshing or continuing a Conversation SHALL NOT reset historical Message retention clocks.

Example:

```text
Message M1
accepted 1 January 2027
ordinary retention clock applies

customer views M1
December 2028

M1 retention clock
does not restart
```

A newly accepted Message receives its own independently governed lifecycle.

---

# 18. Same-Browser Guest Continuity

The initial required guest browser experience is:

```text
guest sends website Message
        ↓
Main Street establishes bounded guest access
        ↓
same protected browser context returns later
        ↓
proof resolves
        ↓
same Conversation resumes
```

This SHALL NOT require CustomerAccount registration.

The customer should not need to understand tokens, identifiers or Main Street security concepts.

---

# 19. Cross-Device Guest Continuity

Initial production guest browser access SHALL NOT require Main Street to recover Conversation access merely from an email address, phone number or name.

If the guest loses the valid possession proof:

```text
browser transcript access is not recoverable
from contact-value equality alone
```

The guest may still:

```text
continue through Conversation-Bound Email
or
start a new Website Conversation
```

according to their independently valid channel authority.

A future stronger guest browser-recovery mechanism MAY be designed separately if product evidence justifies it.

---

# 20. Reply Email Is Not Browser Access Authority

The reply email established by MS-PROT-086 v1.1 remains:

```text
communication endpoint
```

and not:

```text
browser transcript credential
customer identity
authentication factor
```

Main Street SHALL NOT automatically email a reusable Guest Conversation Access Proof to an unverified reply endpoint merely because that endpoint was supplied during guest initiation.

This avoids a mistyped or maliciously supplied email address receiving transcript authority.

---

# 21. Email Route and Browser Grant Are Independent

The Conversation-Bound Email route and Guest Conversation Access Grant SHALL remain independent credentials/authorities.

Therefore:

```text
email reply route possession
    ≠ browser transcript authority

browser access proof possession
    ≠ email-provider route authority
```

Leakage or revocation of one SHALL NOT automatically authorize or invalidate the other unless an accepted security operation explicitly requires coordinated revocation.

---

# 22. Guest-to-Registered Transition

If a guest later creates or authenticates a CustomerAccount:

```text
same email
same phone
same name
same browser
```

SHALL NOT automatically convert the Guest Conversation into registered-customer history.

The Guest Conversation remains guest-authorised unless an independently governed customer-association/claim mechanism establishes otherwise.

This amendment does not introduce such a claim mechanism.

---

# 23. Simultaneous Registered and Guest Evidence

A browser may, in principle, possess:

```text
authenticated CustomerAccount context
+
Guest Conversation Access Proof
```

for different interactions.

Main Street SHALL NOT union those authorities.

Each Conversation request SHALL execute under one deterministically resolved authority basis.

A guest proof SHALL NOT acquire broader authority because the browser also contains an authenticated session.

An authenticated customer SHALL NOT acquire ownership of the guest Conversation merely because both proofs coexist.

---

# 24. Conversation Browser View

`CONVERSATION_BROWSER_VIEW_V1` SHALL expose only the customer-visible retained communication projection.

It MAY include:

```text
customer Messages
merchant Messages
supported timestamps
safe participant/presentation information
bounded continuity metadata
```

It SHALL NOT expose:

```text
Merchant Attention state
internal assignment
snooze state
internal audit records
provider IDs
provider headers
delivery infrastructure
security evidence
CustomerContext internals
source-capability records
restricted evidence
other Conversations
```

---

# 25. Retained Evidence Is Not Automatically Visible

MS-PROT-053 may require a Message or Conversation segment to remain retained beyond ordinary customer-visible operational history.

That does not authorize browser exposure.

Therefore:

```text
retained as qualified evidence
    ≠
ordinary transcript-visible
```

Evidence-only, restricted, redacted or otherwise non-usable material SHALL remain absent or appropriately redacted from ordinary browser view.

The browser SHALL NOT reveal hidden evidence merely by signalling that omitted protected content exists.

---

# 26. Disposed and Redacted Content

Browser view SHALL converge with accepted lifecycle disposition.

If content has been:

```text
deleted
anonymised
redacted
restricted from ordinary use
```

the customer transcript SHALL respect the effective lifecycle state.

A stale cache, search index, browser projection or restored backup SHALL NOT re-expose disposed content.

---

# 27. Conversation Browser Continue

`CONVERSATION_BROWSER_CONTINUE_V1` accepts supported text into exactly one existing Conversation.

Admission requires:

```text
exact Merchant Scope
+
exact Conversation
+
valid registered or guest access authority
+
current continuation eligibility
+
supported Message content
+
logical retry identity
+
applicable abuse/resource-protection admission
+
current lifecycle/use authority
```

Accepted execution creates one immutable ConversationMessage under the existing Message-acceptance authority.

---

# 28. No Direct Business Mutation

Browser continuation remains communication.

A customer writing:

```text
"Cancel my booking"
```

does not itself cancel the Booking.

Canonical:

```text
ConversationMessage accepted
        ↓
optional intent interpretation
        ↓
separately authorised business operation
        ↓
Booking authority
```

This amendment grants no additional source-capability mutation authority.

---

# 29. Idempotent Browser Retry

Browser Message continuation SHALL preserve logical idempotency.

A network retry of the same logical Message attempt SHALL NOT create duplicate Messages.

If the outcome is uncertain:

```text
same logical retry identity
    → recover established result
      or safely establish current outcome
```

A new logical customer Message requires a new logical operation identity.

---

# 30. Guest Creation Response Loss

If initial website Message acceptance succeeds but the client does not receive the resulting guest access credential because the response is lost:

- the accepted Message SHALL remain accepted;
- no duplicate Conversation SHALL be created on an idempotent retry;
- no duplicate first Message SHALL be created;
- the existing Guest Conversation Access Grant family SHALL remain authoritative.

A conforming security implementation MAY safely issue/rotate possession evidence for that same Grant family without restarting its absolute lifetime.

---

# 31. Revocation

A Guest Conversation Access Grant SHALL be independently revocable.

Revocation MAY occur for an authorised:

```text
security response
abuse response
merchant communication-access decision
platform protection action
other accepted access-control reason
```

Revocation:

```text
blocks subsequent browser authority
```

but does not:

```text
delete Conversation
delete Messages
rewrite participant history
revoke email route automatically
create CustomerContext
```

unless another independently accepted operation requires those effects.

---

# 32. Revocation and Accepted Messages

If revocation becomes effective after a Message has already crossed its atomic acceptance boundary:

```text
revocation
    SHALL NOT
roll back the accepted Message
```

If revocation becomes effective before Message acceptance:

```text
current access revalidation
    SHALL prevent
the new append
```

where the request no longer has valid authority.

---

# 33. Expiry and Concurrency

Grant validity SHALL use authoritative server time.

A Grant is invalid where:

```text
now >= idleExpiry
or
now >= absoluteExpiry
or
revoked
```

where those conditions apply.

Access-sensitive state SHALL be re-evaluated sufficiently close to the protected read/write boundary to prevent a stale preliminary `PASS` from defeating concurrent expiry or revocation.

---

# 34. Anti-Enumeration

An invalid, expired, revoked, wrong-merchant or wrong-Conversation guest proof SHALL NOT allow an attacker to discover whether the target Conversation exists.

Similarly, a registered customer lacking participant authority SHALL not receive existence information that would violate privacy.

External results SHOULD therefore use an appropriate safe result such as:

```text
NOT_FOUND_OR_NOT_ACCESSIBLE
```

where disclosure would otherwise leak protected existence.

---

# 35. Credential Security

Guest possession evidence SHALL be:

- cryptographically unpredictable;
- opaque;
- non-derivable from Conversation ID, email or timestamps;
- transported only over authenticated TLS;
- excluded from ordinary application logs;
- excluded from analytics payloads;
- excluded from provider notifications;
- excluded from referrer leakage;
- excluded from merchant-visible data.

The exact credential encoding, verifier form, cookie/session/bootstrap mechanism and storage representation remain security implementation architecture.

---

# 36. URL Bootstrap Boundary

If an implementation uses a URL-carried bootstrap proof, a long-lived reusable guest credential SHALL NOT remain exposed in ordinary navigational URLs longer than necessary to establish protected browser context.

It SHALL NOT subsequently leak through:

```text
browser referrers
analytics
third-party resources
merchant-visible logs
```

Exact exchange/rotation mechanics remain downstream security architecture.

---

# 37. Custom-Domain Isolation

A merchant-branded custom domain SHALL NOT receive Merchant Controller or staff operational credentials merely because it hosts Customer Messaging.

Guest/customer contextual access SHALL remain isolated from privileged merchant sessions.

The storefront route/domain establishes candidate Merchant Scope only through trusted Main Street scope resolution.

A credential valid for Merchant A SHALL fail for Merchant B.

---

# 38. Resource Protection and Abuse

Both browser view and append MAY be subject to applicable MS-PROT-073 Resource Protection policies.

Guest possession does not exempt requests from:

```text
rate limiting
abuse protection
bot/flood protection
security admission
```

Protection admission remains independent from Actor Authorisation and operational eligibility.

A valid proof may therefore still be temporarily rejected or constrained by accepted resource-protection authority.

---

# 39. Merchant Messaging Disablement

Disabling new public Website Messaging SHALL prevent new Conversation initiation where the governing configuration says so.

It SHALL NOT by itself manufacture:

```text
Conversation CLOSED
Guest Grant revoked
all existing Messages deleted
```

Existing browser read/write continuation remains subject to its independently applicable:

```text
access
commercial
communication
lifecycle
abuse
security
```

requirements.

This amendment does not invent a universal Conversation open/closed lifecycle.

---

# 40. Commercial Entitlement

Possession of a guest proof or authenticated customer session does not create Commercial Entitlement.

Likewise, loss of entitlement SHALL NOT itself delete retained Conversation data.

Commercial admission and residual-operation policy remain owned by MS-PROT-056 and composed through MS-PROT-062.

This amendment does not make legally/materially required retained evidence dependent on subscription tier.

---

# 41. Merchant Closure / Suspension

Where Merchant Scope becomes unavailable for customer operation because of authoritative merchant suspension, closure or another applicable lifecycle restriction:

```text
browser access SHALL fail closed
```

as required by the owning authority.

This does not imply destruction of retained evidence.

A guest credential SHALL NOT bypass merchant lifecycle authority merely because its nominal expiry has not yet arrived.

---

# 42. Registered Session Expiry

For registered customers:

```text
customer session expired/revoked
    → registered browser access no longer established
```

The customer must re-establish valid authenticated context according to applicable CustomerAccount authentication policy.

Session expiry does not modify:

```text
Conversation
Messages
CustomerContext
participant history
```

---

# 43. No Guest Fallback for Registered Conversation

A Conversation initiated and participant-bound as an authenticated registered-customer Conversation SHALL NOT automatically receive a weaker guest-access path merely because the customer later signs out.

Canonical:

```text
registered Conversation
        ↓
registered participant access
```

rather than:

```text
registered Conversation
        ↓
silent permanent guest bearer credential
```

This prevents guest capability from bypassing customer-account revocation or authentication requirements.

---

# 44. Browser Access and AI

Browser transcript access does not authorize AI use.

AI MAY assist Customer Messaging only where separately authorised by accepted AI/data-use authority.

The following remains invalid:

```text
customer may view Message
    therefore
AI may train/infer/profile from Message
```

DQ-002 remains unresolved and this amendment does not activate autonomous customer-service responses.

---

# 45. Attachments

Browser view and continuation are text-only under the initial Customer Messaging portfolio.

This amendment does not activate:

```text
upload
download
preview
retention
scanning
```

of Message attachments.

Those remain gated by MS-PROT-086-DQ-004.

---

# 46. Real-Time Behaviour

Conversation browser access does not imply:

```text
WebSocket
typing indicators
online presence
read receipts
delivery ticks
live-agent presence
instant push
```

A conforming implementation may use polling or another transport consistent with accepted API architecture.

Real-time presentation is not part of this DQ.

---

# 47. Guest Recovery Non-Goal

This amendment deliberately does not define:

```text
email magic-link transcript recovery
SMS recovery
support-assisted guest identity recovery
guest Conversation claim after registration
cross-device guest Conversation search
```

because each would require additional evidence and authority beyond possession of the current Guest Conversation Access Proof.

The absence of these mechanisms does not prevent Conversation-Bound Email continuity.

---

# 48. Registered Authentication Non-Goal

This amendment defines what registered-customer Conversation access requires after valid customer authentication has been established.

It does not resolve ADR-014's separate CustomerAccount-specific authentication-policy question.

Authentication mechanism selection remains owned by the authentication/security authority.

---

# 49. Guest Credential Representation Non-Goal

This amendment defines the semantic Guest Conversation Access Grant and its mandatory security properties.

It does not select the exact physical representation of the possession credential.

ADR-014's exact contextual-access credential representation remains the owning security-architecture decision.

Acceptance of this amendment SHALL make that implementation/security decision active before production Guest Conversation browser access.

---

# 50. Timing Summary

| Boundary | Selected value |
|---|---:|
| Guest Grant inactivity period | **90 days** |
| Guest Grant absolute lifetime | **12 calendar months** |
| Successful authorized view resets idle period | **Yes** |
| Successful accepted browser append resets idle period | **Yes** |
| Failed/denied attempt resets idle period | **No** |
| Credential rotation resets absolute lifetime | **No** |
| New Message resets old Message retention | **No** |
| Guest Grant expiry deletes Conversation | **No** |
| Email route lifetime tied to browser Grant | **No** |

---

# 51. Canonical Guest Flow

```text
guest visits merchant website
        ↓
submits first Message
        ↓
WEBSITE_MESSAGE_CREATE_V1
        ↓
Conversation + Message + guest bindings
+ Guest Conversation Access Grant
        ↓
protected possession proof established
        ↓
guest leaves website
        ↓
same browser returns
        ↓
proof validated
        ↓
exact Merchant + Conversation resolved
        ↓
customer-visible transcript
        ↓
guest appends text
        ↓
CONVERSATION_BROWSER_CONTINUE_V1
        ↓
canonical Message accepted
```

---

# 52. Canonical Registered Flow

```text
registered customer authenticates
        ↓
trusted customer session/context
        ↓
merchant website
        ↓
Merchant Scope resolved
        ↓
CustomerContext relationship resolved
        ↓
REGISTERED_CONVERSATION_DISCOVERY_V1
        ↓
exact participation-bound Conversations only
        ↓
customer selects Conversation
        ↓
CONVERSATION_BROWSER_VIEW_V1
        ↓
CONVERSATION_BROWSER_CONTINUE_V1
```

---

# 53. Falsification Review

| Scenario | Required result | Outcome |
|---|---|---|
| Two guests use same email | No cross-Conversation access | PASS |
| Attacker knows Conversation ID | No access | PASS |
| Registered account has same email as guest endpoint | No guest Conversation access without grant | PASS |
| Registered customer changes email | Existing CustomerContext participant relationship remains authoritative | PASS |
| Merchant incorrectly reconciles CustomerContexts | Does not automatically expose Conversation to customer | PASS |
| Guest proof leaked | Exposure bounded to one Conversation and permitted operations | PASS |
| Guest proof used against Merchant B | Reject without cross-merchant leakage | PASS |
| Email reply route leaked | Does not grant browser access | PASS |
| Browser proof leaked | Does not grant email-route authority | PASS |
| Guest loses browser state | No contact-value recovery; email continuity remains | PASS |
| Customer signs up after guest enquiry | No automatic Conversation claim | PASS |
| Customer signs out of registered Conversation | No silent guest fallback | PASS |
| Both account session and guest proof exist | Authorities do not merge | PASS |
| Grant unused for 90 days | Expires | PASS |
| Grant used regularly for 13 months | Absolute 12-month cap prevents continuation | PASS |
| Credential rotated at month 11 | Absolute lifetime remains unchanged | PASS |
| Old Message viewed at month 23 | Retention clock does not reset | PASS |
| New Message appended | Only new Message receives new retention clock | PASS |
| Evidence-retained Message no longer ordinarily visible | Browser does not expose it | PASS |
| Redacted Message exists in cache | Projection must converge to redaction | PASS |
| Merchant response email delivery fails after canonical acceptance | Browser may still show accepted merchant Message | PASS |
| Duplicate browser append retry | One canonical Message | PASS |
| Grant revoked concurrently with append | Current authority revalidation prevents post-revocation acceptance | PASS |
| Revocation happens after Message commit | Accepted Message remains | PASS |
| Request arrives exactly at expiry | `now >= expiry` rejects | PASS |
| Merchant disables new public messaging | New initiation blocked; no invented Conversation CLOSED state | PASS |
| Merchant closes | Browser access fails according to merchant lifecycle; evidence may remain | PASS |
| Guest repeatedly attacks invalid token | Resource Protection applies; no enumeration | PASS |
| Attachment supplied | Not admitted under DQ-004 boundary | PASS |
| AI attempts autonomous response | Not authorised by this amendment | PASS |

**Falsification result: PASS.**

---

# 54. Ambiguity Review

The following ambiguities are explicitly closed.

## “Resume”

Resume means:

```text
re-establish authorised browser interaction
with an existing Conversation
```

It does not mean reopening a Conversation lifecycle.

## “View”

View means:

```text
customer-visible currently usable transcript projection
```

not every retained evidence byte.

## “Guest”

Guest means a contextual principal where supported.

It does not mean public/no-authority access.

## “Same email”

Same email is contact correlation only.

It is not transcript authority.

## “Registered customer”

Registered access means valid authentication plus authorised CustomerContext participation.

It is not an email-match rule.

## “Browser remembers conversation”

A conforming possession proof may be retained in protected browser context.

Browser state without valid proof has no authority.

## “Lost guest access”

Loss of proof does not trigger identity inference or email-based transcript recovery.

## “CustomerContext reconciliation”

Merchant-side reconciliation does not automatically expand customer-side access.

## “Retention”

Retention does not imply browser visibility.

## “Conversation access expired”

Access expiry does not imply Conversation deletion.

## “Messaging disabled”

New initiation eligibility and existing-Conversation access are separate decisions.

## “Cross-device”

Registered customers can re-establish access through authentication.

Initial guest browser access guarantees same-browser continuity; email remains the initial cross-device guest continuation mechanism.

**Ambiguity result: PASS.**

---

# 55. Fundamental Vision Conformance

**Result: PASS — VISION-CONFORMING**

The design:

- keeps guest messaging account-optional;
- provides seamless same-browser continuation;
- avoids email-verification friction for ordinary public initiation;
- allows registered customers to continue across devices;
- hides security/token concepts from ordinary customers;
- does not require merchants to administer customer credentials;
- preserves merchant branding;
- keeps identity and privacy complexity inside Main Street;
- prevents merchant/customer matching heuristics from becoming authority;
- uses the minimum access depth necessary for initial Messaging;
- avoids building a universal customer portal or CRM.

---

# 56. Architecture Review

**Result: PASS**

Ownership remains bounded:

```text
MS-PROT-086
    owns Conversation browser-access semantics

MS-PROT-063
    owns trusted principal/contextual-proof establishment

MS-PROT-062
    owns runtime access/admission composition

MS-PROT-043
    owns CustomerContext relationships/reconciliation

MS-PROT-053
    owns retention/use/disposition

MS-PROT-035
    owns API surface/transport classification

MS-PROT-056
    owns commercial entitlement

MS-PROT-073
    owns resource protection

ADR-014
    owns physical browser credential/session security architecture
```

No universal authentication, CRM, permission or messaging engine is introduced.

---

# 57. Rejected Alternatives

The following designs are rejected.

### Email address as guest identity

Rejected because mutable/unverified contact equality cannot establish transcript authority.

### Automatic magic-link access sent to initial unverified email

Rejected because a mistyped endpoint could receive protected Conversation access.

### Conversation ID as access credential

Rejected because identifiers are locators, not authority.

### Never-expiring guest browser token

Rejected because compromise would create indefinite transcript authority.

### Guest Grant lifetime equal to full Message retention

Rejected because evidence retention and access authorization are independent.

### Automatic guest-to-account Conversation merge

Rejected because account creation/contact equality does not prove historical ownership.

### CustomerContext reconciliation automatically granting customer access

Rejected because merchant-side reconciliation could accidentally disclose protected customer history.

### Universal cross-merchant customer inbox

Rejected as unnecessary initial product depth and incompatible with merchant-scoped customer relationships.

### Mandatory CustomerAccount for messaging

Rejected because guest operation is an accepted product requirement.

### Browser proof granting source-object access

Rejected because Conversation authority must not bypass Booking, Order, Payment or other capability access.

### Whole retained evidence visible in transcript

Rejected because retention is not exposure authorization.

### Guest fallback on registered Conversation

Rejected because it could bypass registered-account security/revocation.

---

# 58. Remaining Separate Gates

Acceptance resolves:

```text
MS-PROT-086-DQ-003
```

It does not resolve:

```text
MS-PROT-086-DQ-002
    automated customer-service responses

MS-PROT-086-DQ-004
    Message attachments
```

It also does not itself resolve the physical security choices tracked under ADR-014.

In particular:

```text
ADR-014-DQ-010
    CustomerAccount-specific authentication policy

ADR-014-DQ-011
    exact guest contextual-access credential representation
```

remain separately owned security/implementation questions.

`ADR-014-DQ-011` becomes a concrete production prerequisite before implementing Guest Conversation browser access.

---

# 59. Implementation-Rules Impact

**Result: NO IMPLEMENTATION ACTIVATION**

Acceptance SHALL NOT:

- activate Customer Messaging implementation;
- activate customer-account production rollout;
- select credential libraries;
- create cookies/tokens;
- activate guest transcript access;
- activate autonomous customer service;
- activate attachments;
- reprioritise the existing production implementation graph.

Implementation promotion remains separately governed by `IMPLEMENTATION-RULES.md`.

---

# 60. Governance Consequences Upon Approval

Human acceptance of this complete proposal authorizes formalisation of this approved meaning only.

Formalisation SHALL include:

- create MS-PROT-086 v1.2;
- update `AUTHORITY-INDEX.md`;
- update `DEFERRED-DECISION-REGISTER.md`;
- mark `MS-PROT-086-DQ-003` RESOLVED;
- update `SEQUENCE.md`;
- update the canonical semantic lexicon where the new Conversation-specific access terms warrant registration;
- identify `ADR-014-DQ-011` as active before production Guest Conversation browser access;
- review `IMPLEMENTATION-RULES.md` impact;
- run `DESIGN-CORPUS-CONFORMANCE`;
- commit governance closure to `development`.

No production implementation is authorized.

---

# 61. Acceptance Boundary

Approval of this proposal means approval of the complete normative package, including:

- two non-interchangeable registered and guest access paths;
- registered access by authenticated CustomerContext participation;
- merchant-scoped registered Conversation discovery;
- no reconciliation-derived customer access;
- one-Conversation Guest Conversation Access Grant;
- `VIEW` + `APPEND_TEXT_MESSAGE` only;
- guest grant established with eligible guest Conversation creation;
- Grant/credential separation;
- 90-day guest inactivity expiry;
- 12-calendar-month absolute guest lifetime;
- no lifetime reset through credential rotation;
- same-browser guest continuity;
- no initial contact-value-based cross-device guest browser recovery;
- Conversation-Bound Email retained as the initial cross-device guest continuity mechanism;
- no automatic access proof sent to unverified reply email;
- no automatic guest-to-registered Conversation claim;
- no guest fallback for registered Conversations;
- retained evidence not automatically browser-visible;
- browser activity not resetting Message retention;
- independent email-route/browser-proof authority;
- anti-enumeration;
- current revocation/expiry revalidation;
- idempotent browser Message append;
- credential-leakage protections;
- separate commercial/resource-protection admission;
- no attachment activation;
- no autonomous-response activation;
- no implementation activation.

**ACCEPTED**
