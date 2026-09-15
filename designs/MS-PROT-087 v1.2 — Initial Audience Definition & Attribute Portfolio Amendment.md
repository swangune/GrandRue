# MS-PROT-087 v1.2 — Initial Audience Definition & Attribute Portfolio Amendment

**Document ID:** MS-PROT-087  
**Version:** 1.2  
**Status:** **ACCEPTED by explicit manual approval on 9 September 2026**  
**Approved:** Explicit manual approval on 9 September 2026 after governed node selection, Fundamental Vision Conformance, ownership review, falsification and ambiguity review  
**Authority type:** Marketing Campaign production-portfolio semantic/design amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `designs/DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-087 v1.0 + v1.1 only within the initial Audience Definition and targeting-attribute portfolio  
**Resolves:** `MS-PROT-087-DQ-002 — Initial Audience Definition and Attribute Portfolio`  
**Implementation activation:** NONE

---

## 1. Governing Decision

The initial Marketing audience portfolio SHALL contain exactly three registered Audience Definition families:

```text
marketing-audience/existing-customer@1

marketing-audience/recent-customer-relationship@1

marketing-audience/previous-customer-reengagement@1
```

These definitions determine only:

```text
who is a candidate recipient
```

They do **not** determine:

```text
whether that candidate may legally or permissibly be contacted

which email endpoint may be used

whether the candidate has opted out

whether the candidate is currently contactable

whether Notification may dispatch

whether a Campaign occurrence is approved
```

Those remain downstream eligibility questions, including the authority still deferred by `MS-PROT-087-DQ-003`.

Canonical:

```text
Audience Definition match
        ↓
Candidate Recipient

NOT

Audience Definition match
        ↓
permission to send
```

---

## 2. Reason for Selecting DQ-002 Next

Composite MS-PROT-087 already distinguishes:

```text
candidate audience
        ≠
recipient eligibility
```

and requires a registered Audience Definition before candidate targeting.

MS-PROT-087 v1.1 further makes `marketing/customer-reengagement@1` explicitly dependent on the future resolution of DQ-002.

DQ-002 is therefore logically upstream of DQ-003:

```text
Who may be considered?
        ↓
DQ-002

May this particular candidate be contacted
for this purpose through this channel now?
        ↓
DQ-003
```

DQ-003 MUST NOT manufacture audience membership, and DQ-002 MUST NOT manufacture permission.

---

## 3. Fundamental Vision Conformance

**Outcome:** `VISION-CONFORMING`

### Representation Test

Micro and small merchants legitimately need simple distinctions equivalent to:

```text
my existing customers

customers who dealt with me recently

previous customers I have not dealt with recently
```

without maintaining CRM segments manually.

### Coordination Test

Audience evaluation requires coordination across:

```text
CustomerContext
+
capability-owned business relationship/activity facts
+
Marketing purpose
```

without transferring ownership of those facts to Marketing.

### Administrative-Compression Test

Main Street can determine the applicable audience internally from authoritative business facts.

The merchant SHOULD experience choices equivalent to:

```text
All existing customers

Recent customers

Previous customers to invite back
```

The merchant SHALL NOT need to construct queries such as:

```text
last_order_at < 90 days
AND lifetime_value > ...
AND segment = ...
```

### Proportionality

The initial portfolio deliberately excludes:

```text
CRM segmentation
behavioural scoring
lead scoring
customer-value tiers
arbitrary query builders
AI-selected audiences
sensitive targeting
lookalike targeting
prospecting
```

---

## 4. Ownership Boundary

Ownership remains:

```text
CustomerContext
    → customer relationship context

Ordering
    → Order truth

Booking
    → Booking truth

Appointment
    → Appointment truth

other source capability
    → its own business facts

Marketing
    → Audience Definition
      and campaign-specific evaluation of those facts
```

Marketing MUST NOT create:

```text
Customer lifecycle state

ACTIVE customer

INACTIVE customer

LAPSED customer

LOYAL customer

VIP customer

HIGH-VALUE customer
```

as durable CustomerContext truth.

Audience membership is a purpose-bound evaluation result.

---

## 5. Candidate Identity Boundary

Initial Audience Definitions SHALL consider only merchant-scoped durable customer relationships represented through accepted `CustomerContext` authority.

An Enquiry alone does not create candidate eligibility.

A Conversation alone does not create candidate eligibility.

A known email address alone does not create candidate eligibility.

A CustomerAccount alone does not create candidate eligibility.

A name, phone number, payment-provider identity or frontend session does not create candidate eligibility.

This preserves:

```text
customer relationship
    ≠ contact endpoint
    ≠ authenticated account
    ≠ enquiry
    ≠ marketing permission
```

---

# Audience Family 1

## 6. `marketing-audience/existing-customer@1`

This Audience Definition identifies merchant-scoped CustomerContexts representing an accepted durable customer relationship with the merchant.

Candidate derivation requires:

```text
current MerchantScope
+
accepted CustomerContext
+
CustomerContext not invalidated by
applicable authoritative correction
```

This definition does not require a particular Order, Booking or Appointment to exist.

Rationale:

`CustomerContext` already represents the merchant-scoped durable customer relationship.

Marketing MUST NOT recreate a second definition of “customer” from transaction tables.

### 6.1 Exclusions

The following do not independently qualify:

```text
Enquiry without CustomerContext

Conversation without CustomerContext

visitor activity

newsletter-style contact record

imported email address

CustomerAccount without merchant CustomerContext

weak contact match

AI-inferred likely customer
```

### 6.2 Relationship Does Not Mean Permission

Membership in:

```text
marketing-audience/existing-customer@1
```

does not establish that any marketing email may be sent.

DQ-003 remains mandatory.

---

# Audience Family 2

## 7. `marketing-audience/recent-customer-relationship@1`

This Audience Definition identifies an existing CustomerContext for which at least one accepted initial qualifying customer-business commitment was established within a bounded Campaign-defined lookback period.

Initial qualifying source families are exactly:

```text
Order commitment

Booking commitment

Appointment commitment
```

where the source capability authoritatively relates the commitment to that CustomerContext within the same MerchantScope.

Marketing consumes those facts.

It does not own or reproduce them.

---

## 8. Recent Lookback Parameter

The Audience Definition SHALL contain an explicit bounded:

```text
lookbackPeriod
```

rather than a hard-coded universal concept of “recent”.

The period MUST:

```text
be expressed using accepted duration/time semantics

be fixed in the exact Audience Definition version
used by the Campaign Revision

be reviewable by the merchant in ordinary language

remain bounded by platform policy
```

The initial authority does not prescribe one universal value such as:

```text
30 days
90 days
6 months
```

for every merchant.

A conforming merchant experience may offer a small controlled set such as:

```text
last 30 days
last 90 days
last 6 months
```

where subsequently established by configuration/product authority.

It SHALL NOT expose a general customer-query language.

---

## 9. Qualifying Activity Timestamp

For the initial portfolio, qualifying activity is based on the authoritative establishment time of the applicable business commitment:

```text
Order committedAt

Booking committedAt

Appointment committedAt
```

or the semantically equivalent accepted source timestamp.

Marketing SHALL NOT create its own generic:

```text
customer.lastActivityAt
```

authority.

---

## 10. Cancellation, Release and Later Change

A later:

```text
Order release

Booking cancellation

Appointment cancellation

refund

return

payment reversal
```

does not erase the historical fact that the qualifying commitment was established.

Therefore, for this initial portfolio:

```text
qualifying commitment established within lookback
    → activity occurred for audience-recency purposes
```

even when the commitment was later cancelled, released or otherwise changed.

This is deliberate.

Marketing MUST NOT reinterpret cross-capability lifecycle semantics into a universal concept of:

```text
completed visit

successful purchase

satisfied customer
```

without separate authority.

Audience copy SHALL therefore not claim that such an activity was completed merely because it contributed to audience evaluation.

---

# Audience Family 3

## 11. `marketing-audience/previous-customer-reengagement@1`

This Audience Definition identifies:

```text
existing CustomerContext
+
evidence of at least one qualifying historical
customer-business commitment
+
no qualifying commitment established
within the configured re-engagement quiet period
```

Initial qualifying source families are again exactly:

```text
Order

Booking

Appointment
```

No other capability contributes qualifying re-engagement activity under v1.2.

---

## 12. Re-engagement Algebra

For CustomerContext `C` at evaluation time `T`:

```text
HAS_HISTORY(C)
=
there exists at least one authoritative
qualifying Order, Booking or Appointment
related to C
```

```text
HAS_RECENT_ACTIVITY(C, period)
=
there exists at least one qualifying
Order, Booking or Appointment
related to C
whose authoritative commitment time
falls within period before T
```

Then:

```text
previous-customer-reengagement
=
HAS_HISTORY(C)
AND
NOT HAS_RECENT_ACTIVITY(C, quietPeriod)
```

provided required evidence coverage is sufficient.

---

## 13. Re-engagement Is Not an Inactive-Customer State

The result MUST remain:

```text
eligible candidate for this exact
Audience Definition evaluation
```

not:

```text
CustomerContext.status = INACTIVE
```

or:

```text
CustomerContext.segment = LAPSED
```

A customer may simultaneously match different Campaign audience evaluations for different purposes and times without acquiring durable CRM labels.

---

## 14. No Negative Inference From Missing Data

Absence of observed recent activity may be used only where Main Street has sufficient authoritative evidence coverage to evaluate the selected source families.

Canonical:

```text
complete required coverage
+
no qualifying recent activity
    → may satisfy absence predicate
```

Rejected:

```text
data source unavailable

or

relationship history incomplete

or

required evaluator unavailable
        ↓
assume no recent activity
```

Where sufficient evidence cannot be established:

```text
audience result = UNRESOLVED
```

The candidate MUST NOT silently enter the re-engagement audience.

---

## 15. Coverage Qualification

Every Audience evaluation SHALL preserve sufficient evidence to determine:

```text
which source families were required

which source families were evaluated

the relevant evaluation interval

whether required evidence was complete enough

which authoritative source facts matched

evaluation time

Audience Definition version
```

Marketing may cache or project these results for performance only under applicable projection authority.

Such a representation does not become source customer truth.

---

# Initial Attribute Portfolio

## 16. Permitted Audience Attributes

The initial Audience Definition portfolio may use only the following classes of targeting evidence:

```text
MerchantScope

CustomerContext identity

authoritative CustomerContext relationship existence

authoritative Order → CustomerContext relationship

authoritative Booking → CustomerContext relationship

authoritative Appointment → CustomerContext relationship

authoritative commitment establishment timestamp

bounded Campaign-defined time interval

Audience Definition identity/version

purpose-family compatibility
```

No other customer attribute is enabled by this amendment.

---

## 17. Explicitly Prohibited Initial Attributes

The initial portfolio SHALL NOT target using:

```text
customer spend

order value

average order value

number of purchases

number of bookings

number of appointments

visit frequency scoring

refund history

return history

complaint history

Conversation contents

Enquiry contents

message sentiment

customer-service sentiment

review scores

location history

precise location

postcode-derived affluence

age

birthday

gender inference

occupation

income

financial condition

health information

disability

religion

ethnicity

sexual orientation

political opinion

vulnerability

children/minor status

AI propensity score

AI churn score

AI purchase probability

AI-inferred preference

"best customer"

"high-value customer"

"VIP"

"loyal customer"

lookalike similarity
```

Future inclusion of any additional attribute requires separate governed admission.

---

## 18. Subject/Product-Specific Targeting Is Not Initially Enabled

Although MS-PROT-087 v1.0 permits Audience Definitions conceptually to use applicable purchase or offering facts, the initial portfolio SHALL NOT activate targeting such as:

```text
customers who bought Product X

customers who booked Service Y

customers interested in Category Z

customers who bought similar items
```

Reason:

This would materially widen targeting semantics into product-history segmentation and would require additional decisions concerning:

```text
subject classification

historical product affinity

replacements/versioning

privacy

personalisation boundaries

cross-capability exposure
```

It is not necessary for the four initial Campaign purpose families.

---

## 19. Payment Is Not Audience Activity

Payment facts SHALL NOT independently establish audience membership.

Therefore:

```text
Payment Application

payment success

payment failure

Refund

settlement

payment amount
```

do not independently qualify or disqualify a CustomerContext for the initial audience families.

Where an Order, Booking or Appointment exists, its owning capability supplies the relationship/activity fact.

Money remains Money-owned.

---

## 20. Enquiry and Conversation Are Not Audience Activity

Initial re-engagement SHALL NOT treat:

```text
Enquiry submission

Message sent

Conversation activity

website chat

customer-service interaction
```

as qualifying customer-business activity.

This prevents:

```text
someone asks one question
        ↓
becomes re-engagement marketing target
```

and prevents Marketing from becoming a support-activity CRM.

---

## 21. Reconciliation Boundary

Marketing SHALL use accepted CustomerContext reconciliation authority where needed for candidate resolution and duplicate protection.

It SHALL NOT destructively merge CustomerContexts.

Where:

```text
C2 RECONCILED_TO C1
```

Marketing MAY evaluate the governed reconciled customer relationship according to accepted reconciliation projection semantics.

Historical source relationships remain attached to their original contexts.

Marketing SHALL preserve enough evidence to explain which source contexts contributed to the candidate evaluation.

A shared email endpoint does not establish reconciliation.

---

## 22. Deduplication Boundary

One governed customer relationship appearing through multiple qualifying source facts MUST NOT create multiple candidate-recipient identities for the same Campaign Occurrence merely because:

```text
3 Orders

2 Bookings

1 Appointment
```

Those facts establish evidence.

They do not create six customers.

Conversely:

```text
same email endpoint
```

MUST NOT cause two unresolved CustomerContexts to be silently merged into one customer identity.

Endpoint deduplication and actual dispatch eligibility remain subject to the downstream DQ-003/contact-resolution authority.

---

## 23. Campaign Purpose Compatibility

Initial audience families may participate as follows:

```text
marketing/merchant-news-awareness@1
    → existing-customer
    → recent-customer-relationship
    → previous-customer-reengagement

marketing/offering-awareness@1
    → existing-customer
    → recent-customer-relationship
    → previous-customer-reengagement

marketing/customer-appreciation@1
    → existing-customer
    → recent-customer-relationship

marketing/customer-reengagement@1
    → previous-customer-reengagement
```

`customer-appreciation` SHALL NOT use the re-engagement audience in v1.2 because doing so would blur:

```text
thank existing customers
```

with:

```text
invite previous customers back
```

without a material benefit.

---

## 24. Audience Definition Version Affinity

Every Campaign Revision SHALL identify the exact:

```text
AudienceDefinition identity
+
AudienceDefinition version
+
bounded parameter values
```

it uses.

A later change to:

```text
lookback period

quiet period

qualifying source family

inclusion rule

exclusion rule
```

creates a materially different Audience Definition version or Campaign Revision as applicable.

It SHALL NOT silently change the recipient meaning of an already approved Campaign Revision.

---

## 25. Evaluation Currentness

Candidate derivation MAY occur before Campaign activation for preview purposes.

However:

```text
preview audience
    ≠ reserved recipient set
```

Before externalisation, MS-PROT-087 current-state revalidation remains mandatory.

Where current source evidence causes the candidate no longer to match:

```text
candidate excluded
```

Where required evidence becomes unresolved:

```text
audience result = UNRESOLVED
```

No stale audience snapshot may create dispatch authority.

---

## 26. Merchant Experience

The internal definitions SHALL NOT require merchant understanding of source-capability semantics.

A conforming experience may present:

```text
Who should receive this?

○ All existing customers

○ Customers you've dealt with recently

○ Previous customers you haven't dealt with recently
```

where purpose compatibility permits.

For bounded time windows:

```text
How far back should Main Street look?

○ 30 days
○ 90 days
○ 6 months
```

or another later accepted controlled set.

The ordinary merchant path SHALL NOT expose:

```text
SQL

boolean expression trees

CRM filters

analytics dimensions

source-table names

CustomerContext IDs

Order lifecycle internals
```

---

## 27. AI Boundary

AI MAY:

```text
interpret merchant language

suggest one registered Audience Definition

explain the selected audience in ordinary language

explain why a particular candidate matched
using authoritative evidence
```

AI SHALL NOT:

```text
invent another audience family

add another targeting attribute

infer protected/sensitive attributes

invent activity evidence

infer marketing permission

rank people by propensity

create candidate eligibility from probability

override UNRESOLVED evidence
```

Canonical:

```text
AI proposal
        ↓
registered deterministic Audience Definition
        ↓
authoritative evaluation
```

not:

```text
AI thinks these people are likely buyers
        ↓
audience
```

---

# 28. Falsification

The proposal was tested against the following cases.

### Case A — One-off Enquiry

A visitor submits an Enquiry and provides an email address.

No CustomerContext exists.

**Result:**

```text
not in initial direct-marketing audience
```

PASS.

---

### Case B — Existing customer with no recent transaction

A valid CustomerContext exists and an old Booking exists.

No qualifying recent Order, Booking or Appointment exists in the quiet period.

**Result:**

```text
existing-customer = MATCH

previous-customer-reengagement = MATCH
```

subject to evidence coverage and later DQ-003 recipient eligibility.

PASS.

---

### Case C — Customer booked yesterday

CustomerContext has a Booking committed yesterday.

**Result:**

```text
recent-customer-relationship = MATCH

previous-customer-reengagement = NO MATCH
```

PASS.

---

### Case D — Appointment made then cancelled

Appointment was validly committed 20 days ago and later cancelled.

For a 30-day recency definition:

```text
recent-customer-relationship = MATCH
```

The audience may know a customer relationship interaction occurred.

The Campaign MUST NOT infer:

```text
appointment completed
```

PASS.

---

### Case E — Payment without customer-business commitment

A Payment fact exists but no qualifying Order, Booking or Appointment can establish the required relationship activity.

**Result:**

```text
payment does not qualify candidate
```

PASS.

---

### Case F — Old Conversation activity

A previous customer sends a support message yesterday but has had no qualifying Order, Booking or Appointment within the re-engagement period.

Conversation activity does not reset the re-engagement clock.

**Result:**

```text
Conversation ≠ qualifying business activity
```

PASS.

This prevents customer-service interaction from silently becoming Marketing CRM activity.

---

### Case G — Reconciled CustomerContexts

C2 is authoritatively reconciled to C1.

Historical Order belongs to C2.

Marketing may use the accepted reconciliation projection to evaluate the governed relationship without rewriting the Order.

**Result:** PASS.

---

### Case H — Same shared family email

Two distinct CustomerContexts contain the same email endpoint.

**Result:**

```text
Audience evaluation does not merge them
```

DQ-003/contact-resolution authority must determine whether/how dispatch can occur without unsafe duplication.

PASS.

---

### Case I — Missing historical source evidence

Required Booking evidence cannot presently be established.

Marketing cannot prove absence of recent qualifying activity.

**Result:**

```text
reengagement audience = UNRESOLVED
```

not:

```text
assume inactive
```

PASS.

---

### Case J — Merchant asks for “best customers”

The phrase cannot resolve to an initial Audience Definition.

**Result:**

Main Street must not translate it into spend, frequency or AI-propensity scoring.

It may offer the bounded supported alternatives.

PASS.

---

### Case K — Merchant asks for “customers who bought shampoo”

Product-specific historical segmentation is not in v1.2.

**Result:**

not supported by this initial portfolio.

PASS.

This keeps the initial capability below CRM/CDP complexity.

---

### Case L — AI selects “customers likely to return”

No registered deterministic definition exists.

**Result:**

prohibited.

PASS.

---

## 29. Ambiguity Review

### Ambiguity 1 — Does cancellation erase activity?

Resolved:

```text
NO
```

The accepted commitment remains historical evidence that a customer-business interaction occurred.

Marketing makes no claim about completion.

### Ambiguity 2 — Does CustomerContext alone prove prior transaction?

No.

It proves an accepted durable merchant-customer relationship.

That is enough for:

```text
existing-customer
```

but not enough for:

```text
recent-customer-relationship

previous-customer-reengagement
```

which require qualifying source activity.

### Ambiguity 3 — Does “no recent activity” mean Main Street checked everything the customer may have done?

No.

It means only:

```text
no qualifying activity
within the exact source portfolio
defined by this Audience Definition
with sufficient evidence coverage
```

No complete-human-history claim is permitted.

### Ambiguity 4 — Is candidate deduplication based on email?

No.

Customer relationship/identity authority remains upstream.

Email is a contact endpoint and remains downstream contact-resolution scope.

---

## 30. Corpus Conformance

This amendment preserves existing authority:

```text
MS-PROT-043
    retains CustomerContext
    and reconciliation truth

MS-PROT-042
    retains Booking/Appointment truth

MS-PROT-077
    retains Order truth

MS-PROT-053
    retains data-protection authority

MS-PROT-083
    retains analytical classification
    and recommendation authority

MS-PROT-087
    owns only purpose-bound
    Audience Definition semantics
```

No architecture change is required.

No CRM subsystem is introduced.

No generic customer-activity service is introduced.

No universal `lastActivityAt` authority is introduced.

No implementation activation is authorised.

`MS-PROT-087-DQ-003` remains active and mandatory before direct-email marketing externalisation.

---

## 31. Explicitly Unresolved After This Amendment

Approval of v1.2 leaves:

```text
MS-PROT-087-DQ-003
    direct-marketing permission,
    suppression,
    contact and jurisdiction policy

MS-PROT-087-DQ-004
    automated/standing Campaign execution

MS-PROT-087-DQ-005
    Campaign performance and attribution

MS-PROT-087-DQ-006
    paid advertising
```

unresolved.

No direct Campaign becomes executable merely because DQ-002 is resolved.

---

## 32. Governance Outcome

**Node selection:** PASS  
**Representation Test:** PASS  
**Coordination Test:** PASS  
**Administrative-Compression Test:** PASS  
**Fundamental Vision Conformance:** PASS  
**Cross-capability ownership:** PASS  
**Anti-CRM proportionality:** PASS  
**CustomerContext ownership:** PASS  
**Source-business ownership:** PASS  
**Negative-evidence safety:** PASS  
**Sensitive/proxy targeting exclusion:** PASS  
**AI non-authority:** PASS  
**Falsification:** PASS  
**Ambiguity review:** PASS  
**Architecture change required:** NO  
**Implementation activation:** NONE

---

## 33. Recommendation

**RECOMMENDATION: ACCEPT**

This amendment closes `MS-PROT-087-DQ-002` with the smallest useful production Audience portfolio:

```text
existing customers

recent customer relationships

previous customers eligible for
re-engagement consideration
```

It deliberately stops before CRM segmentation, spend/value scoring, product-history targeting, behavioural profiling and AI-selected audiences.

The core design boundary is:

```text
source capability owns what happened

CustomerContext owns the merchant-customer relationship

Marketing decides whether those accepted facts
match one registered Campaign audience

DQ-003 separately decides whether
the resulting candidate may actually be contacted
```

**Manual approval:** GRANTED — 9 September 2026  
**Repository formalisation:** AUTHORISED
