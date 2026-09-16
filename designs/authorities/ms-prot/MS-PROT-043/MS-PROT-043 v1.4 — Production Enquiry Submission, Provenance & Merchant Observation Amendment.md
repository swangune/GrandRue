# MS-PROT-043 v1.4 — Production Enquiry Submission, Provenance & Merchant Observation Amendment

**Document ID:** MS-PROT-043  
**Version:** 1.4  
**Status:** **ACCEPTED by manual approval on 27 August 2026**  
**Amends:** MS-PROT-043 v1.2–v1.3 within Enquiry creation/read/runtime scope  
**Closes jointly with MS-PROT-046 v1.2:** MS-PROT-079 Target 10 — Publication / Enquiry  
**Approved:** Manual approval on 27 August 2026 after Target-10 authority trace, implementation-evidence review, falsification, corpus-conformance review and recommendation  
**Purpose:** Make public and merchant-assisted Enquiry creation production-executable without turning Enquiry into a ticket workflow, CustomerContext factory, Conversation lifecycle or cross-capability mutation engine.

---

# 1. Governing decision

An Enquiry is the durable fact:

> **A customer asked this merchant this question/request in this authoritative context.**

MS-PROT-043 v1.3 remains authoritative that:

```text
Enquiry truth
≠ Merchant Attention
≠ Conversation state
≠ CustomerContext lifecycle
```

Target 10 adds the exact creation and observation contract.

---

# 2. Canonical submission flow

```text
PUBLIC / MERCHANT-ASSISTED INTERACTION
        ↓
current Merchant Scope
        +
current legitimate Enquiry interaction
        +
optional current subject binding
        +
known structured context
        +
applicable Enquiry requirements
        ↓
resolve unresolved customer input
        ↓
submit Enquiry command
        ↓
authoritative revalidation
        ↓
idempotent Enquiry creation
        ↓
optional communication consequences
        +
Merchant Attention projection
```

---

# 3. Public merchant-general Enquiry

The public `enquiry / send-enquiry` interaction MAY create an Enquiry with:

```text
SUBJECT = none
```

where the merchant-level Enquiry interaction is legitimately active.

This preserves the accepted rule that not every Enquiry needs a specific subject.

---

# 4. Subject-specific Enquiry

Where an Enquiry is initiated from an exact Public Interaction Binding:

```text
Opportunity O1
    → send-enquiry
```

the submission SHALL carry that exact semantic subject identity structurally.

The customer SHALL NOT have to restate it.

This preserves Context-Carry-Forward.

---

# 5. Subject references are untrusted locators

A browser-provided subject/binding reference SHALL be revalidated against:

```text
Merchant Scope
subject existence
subject type/owner
current public participation
current Exposure
current interaction applicability
```

before Enquiry creation.

Possession of an old or forged identifier is not authority.

---

# 6. Stale subject binding fails closed

If a specific subject ceased to participate before submission:

```text
stale subject-specific Enquiry
    → reject
```

Main Street SHALL NOT silently convert:

```text
"Enquire about withdrawn Opportunity O1"
```

into:

```text
general merchant Enquiry
```

because that changes customer meaning.

The customer may independently initiate a new general Enquiry.

---

# 7. Requirements remain Enquiry-owned semantics

Target 10 SHALL NOT introduce a generic form-builder DSL.

Enquiry preparation continues to resolve:

```text
Applicable Enquiry requirements
+
known structured context
+
known legitimate customer information
        ↓
unresolved requirements
```

Only unresolved required information is requested.

This preserves the accepted model in MS-PROT-043 v1.2.

---

# 8. Submitted contact data is not authenticated identity

If the customer supplies:

```text
name
email
telephone
```

the authoritative meaning is:

> these values were supplied for this Enquiry.

Not:

> Main Street has authenticated this person's identity.

The distinction remains explicit.

---

# 9. Enquiry does not automatically create CustomerContext

A one-off Enquiry MAY remain:

```text
Enquiry
+
submitted contact data
```

without creating:

```text
CustomerContext
CustomerAccount
```

This remains important for low-friction information publishers.

---

# 10. Existing reliable CustomerContext may be reused

Where Main Street has trusted current association:

```text
authenticated/authoritative context
    → CustomerContext C1
```

the new Enquiry MAY reference C1 directly.

This is association, not reconciliation.

Contact-value similarity alone remains insufficient.

---

# 11. Enquiry submission evidence is immutable

The initial customer request and its submission provenance SHALL be preserved as immutable submission evidence.

Later activity SHALL NOT rewrite what the customer originally asked.

Conceptually:

```text
Enquiry E1

submitted question
submitted contact
submitted subject
submission provenance
submission context evidence
```

remain historically interpretable.

---

# 12. Follow-up communication does not edit the original Enquiry

Customer follow-up:

```text
"Actually, I meant the September intake."
```

SHALL normally become:

```text
Message / communication evidence
```

rather than rewriting the original submitted Enquiry text.

Explicit correction semantics may be introduced later only if materially required.

---

# 13. Exact subject provenance

Every subject-specific Enquiry SHALL preserve:

```text
subject semantic identity
+
sufficient submission-time semantic provenance
```

Where the subject authority supports exact revision identity, Enquiry submission SHOULD retain that exact revision reference.

For Publication under MS-PROT-046 v1.2:

```text
Enquiry E1
    SUBJECT → Opportunity O1
    observed publication revision → R7
```

This allows later reconstruction even if O1 becomes R8.

---

# 14. Fallback provenance where subject revision is unavailable

If an owning subject does not provide a stable reconstructible revision identity, Enquiry may retain the **minimum immutable context values** required to interpret the customer request accurately.

It SHALL NOT snapshot the entire subject indiscriminately.

Canonical:

```text
exact revision reference where available
        else
minimum material submission snapshot
```

This closes the semantic gap left implementation-deferred by MS-PROT-043 v1.2 without choosing a database representation.

---

# 15. Enquiry creation is idempotent

The same logical submitted Enquiry delivered repeatedly due to:

```text
browser retry
network timeout
client retry
integration retry
```

SHALL result in one Enquiry.

Retry returns/reconciles with the original result.

---

# 16. Similar Enquiries are not automatically duplicates

Two independent submissions containing:

```text
same email
same subject
similar question
```

are not automatically the same logical Enquiry.

Main Street SHALL distinguish:

```text
technical retry
≠ repeated human intent
```

MS-PROT-059 remains authoritative.

---

# 17. Enquiry creation does not create another commitment

Successful submission SHALL NOT create:

```text
Booking
Appointment
Order
Payment
Allocation
```

A request such as:

> “Please reserve this for me.”

is still an Enquiry until the applicable capability-owned operation independently establishes the commitment.

---

# 18. Enquiry does not convert into another object

If an Enquiry later leads to Appointment A1:

```text
Enquiry E1
    ↓ relationship/provenance
Appointment A1
```

E1 remains an Enquiry.

There is no:

```text
ENQUIRY_CONVERTED
```

universal lifecycle state.

---

# 19. Conversation is optional, not mandatory

Submitting an Enquiry SHALL NOT require Main Street to manufacture a durable Conversation in every case.

Valid minimal case:

```text
Enquiry
+
email reply contact
```

Where durable Main Street messaging is supported:

```text
Enquiry
    ↔ Conversation
```

may be established through the accepted communication relationship.

Conversation still owns communication continuity.

---

# 20. Merchant Attention is a derived consequence

A newly submitted Enquiry MAY create:

```text
requires-attention
unread inbound
```

within the Merchant Attention responsibility.

It SHALL NOT mutate Enquiry into:

```text
OPEN
UNREAD
WAITING
RESPONDED
CLOSED
```

MS-PROT-043 v1.3's separation remains authoritative.

---

# 21. Merchant notification is post-commit

If Main Street notifies a merchant about a new Enquiry:

```text
Enquiry commits
    ↓
Notification Intent
    ↓
provider delivery
```

Delivery failure does not erase the Enquiry.

Target 16 governs concrete notification-provider completion.

---

# 22. Initial Enquiry merchant read model

The initial merchant-facing Enquiry read shall be request-scoped.

It may compose:

```text
Enquiry authoritative facts
submitted contact
SUBJECT identity
submission-time subject provenance
current permitted subject projection
known CustomerContext reference
communication summary
Merchant Attention summary
```

No separate CRM/inbox database is required to close Target 10.

---

# 23. Current subject and submission-time subject are distinguishable

Merchant-facing projection may show both where materially useful:

```text
At submission:
deadline = 31 October

Current:
deadline = 15 November
```

It SHALL NOT overwrite submission history with current subject truth.

---

# 24. Initial Enquiry Exposure portfolio

Target 10 SHALL register the initial MERCHANT Exposure contract families:

```text
enquiry / merchant-submission-content

enquiry / merchant-submitted-contact

enquiry / merchant-subject-context

enquiry / merchant-communication-summary
```

These require legitimate merchant observation context and applicable Actor Authorisation.

They SHALL NOT imply PUBLIC observation of stored Enquiry data.

---

# 25. Public submitter does not automatically receive an Enquiry history surface

Successful public submission may return:

```text
safe acknowledgement
public-safe correlation/reference
```

but that does not establish:

```text
CUSTOMER Enquiry history
CustomerAccount access
Conversation access
```

Those require their own relationship/access/Exposure contracts.

---

# 26. Resource protection may reject submission

Rate limiting, abuse protection and platform fairness may refuse attempted Enquiry submission before authoritative creation.

That decision remains Resource Protection Admission.

It SHALL NOT create:

```text
Enquiry status = SPAM
```

unless a later accepted Enquiry semantic explicitly requires such business classification.

---

# 27. AI may assist, never authorise

AI may:

```text
summarise an Enquiry
identify likely intent
suggest a response
suggest subject/customer matches
propose a resulting business operation
```

It SHALL NOT:

```text
rewrite original submission truth
silently reconcile CustomerContexts
create another commitment
determine Actor Authorisation
manufacture subject participation
```

---

# 28. Hard invariants

1. Enquiry remains durable business truth, not merchant work state.
2. General merchant Enquiry may have no subject.
3. Subject-specific Enquiry requires current authoritative participation.
4. Structured subject context outranks free-text inference.
5. Stale subject bindings fail closed.
6. Stale subject Enquiry is not silently converted to general Enquiry.
7. Enquiry requirements remain capability-owned; no generic form language is introduced.
8. Submitted contact information is not authenticated identity.
9. Enquiry does not automatically create CustomerContext.
10. Reliable existing CustomerContext association may be reused.
11. Initial submission evidence remains immutable.
12. Follow-up communication does not rewrite the original request.
13. Subject-specific Enquiry retains sufficient submission-time provenance.
14. Exact subject revision is preferred where available.
15. Whole-subject snapshots are prohibited without necessity.
16. Technical retries are idempotent.
17. Similar human submissions are not automatically duplicates.
18. Enquiry does not establish Booking, Appointment, Order or Payment.
19. Enquiry does not transform into another business object.
20. Conversation remains independently optional.
21. Merchant Attention remains separate.
22. Notifications are post-commit consequences.
23. Initial Enquiry merchant reads are request-scoped.
24. Merchant observation uses explicit Exposure contracts.
25. Public submission does not automatically grant stored-Enquiry read access.
26. AI remains assistive rather than authoritative.

---

# 29. Deferred questions

| ID | Status | Deferred question | Future owner |
|---|---|---|---|
| `MS-PROT-043-V14-DQ-001` | DEFERRED — INACTIVE | Exact Enquiry persistence representation | Enquiry implementation |
| `MS-PROT-043-V14-DQ-002` | DEFERRED — INACTIVE | Exact logical idempotency-key representation | Enquiry/API implementation |
| `MS-PROT-043-V14-DQ-003` | DEFERRED — INACTIVE | Exact subject-provenance encoding | Enquiry persistence implementation |
| `MS-PROT-043-V14-DQ-004` | DEFERRED — INACTIVE | Exact Enquiry API/form transport representation | Target 20 |
| `MS-PROT-043-V14-DQ-005` | DEFERRED — INACTIVE | Exact Merchant Attention persistence/model beyond minimal projection | Later merchant-attention implementation |
| `MS-PROT-043-V14-DQ-006` | DEFERRED — INACTIVE | Exact durable Conversation creation policy/channel mapping | Communication implementation |
| `MS-PROT-043-V14-DQ-007` | DEFERRED — INACTIVE | Enquiry/communication retention periods | Target 17 |
| `MS-PROT-043-V14-DQ-008` | DEFERRED — INACTIVE | Enquiry/attention telemetry | Target 19 |

---

# 30. Target-10 cross-authority composition

The accepted production path is:

```text
PUBLICATION
    owns information truth
    revisions
    lifecycle
    exposure-window evidence
    subject participation where registered

        ↓

REQUEST-SCOPED PUBLIC PROJECTION

        ↓

EXPOSURE
    determines audience observation

        ↓

PUBLIC INTERACTION BINDING
    derives legitimate Opportunity → Enquiry participation

        ↓

ENQUIRY PREPARATION
    carries forward merchant + subject context
    resolves only missing requirements

        ↓

ENQUIRY SUBMISSION
    revalidates current participation
    creates one idempotent durable Enquiry

        ↓

COMMUNICATION / MERCHANT ATTENTION
    optional independent consequences
```

The ownership boundaries remain:

```text
Publication
    ≠ Exposure

Publication
    ≠ Enquiry

Enquiry
    ≠ Conversation

Enquiry
    ≠ Merchant Attention

Public Interaction Binding
    ≠ participation authority

Projection
    ≠ mutation authority

Notification
    ≠ Enquiry truth
```

---

# 31. Target-10 falsification record

### Scholarship publisher

Publishes Opportunity O1; O1 is visible before applications open; customers can ask a question; no Offering, Booking or Payment is required.

**PASS**

### Information-only merchant

Has general Enquiry but no subject-specific publications. A visitor can contact the merchant without CustomerAccount or CustomerContext creation.

**PASS**

### Withdrawn scholarship

A browser holds an old Enquiry binding after O1 is withdrawn. New subject-specific submission is rejected rather than creating a stale Enquiry.

**PASS**

### Scheduled Announcement

PUBLISHED now, `publishFrom` tomorrow. No timer mutates lifecycle; PUBLIC Exposure WITHHOLDs until tomorrow.

**PASS**

### Expired announcement window

Remains historically PUBLISHED but is WITHHOLD after `publishUntil`; no artificial `EXPIRED` state.

**PASS**

### Deadline changes after enquiry

E1 preserves O1 revision observed at submission while current merchant projection may also show the corrected current deadline.

**PASS**

### Double-click Send

The same logical submission produces one Enquiry.

**PASS**

### Customer asks twice intentionally

Two separate logical submissions can produce two Enquiries.

**PASS**

### Enquiry leads to Appointment

Appointment is separately created by Appointment authority; E1 remains unchanged.

**PASS**

### Notification provider outage

Enquiry commits and remains visible to the merchant even if alert delivery fails.

**PASS**

### AI drafts scholarship

Draft may be reviewed; AI cannot publish it automatically.

**PASS**

### Merchant has no Enquiry capability

Exposed Opportunity alone does not manufacture an Enquiry binding.

**PASS**

---

# 32. Programme effect

Together with accepted MS-PROT-046 v1.2, this amendment Design-Closes MS-PROT-079 Target 10 — Publication / Enquiry and activates Target 11 — Booking / Appointment / Scheduling through the canonical Deferred Decision Register.

It does not authorise production implementation, API/UI work, migrations, provider execution or prototype work.

---

# 33. Governing principle

> **Enquiry preserves the durable fact of what a customer asked and in what authoritative context. Public submission revalidates current interaction/subject authority, preserves immutable submission provenance and is idempotent, while Publication, Exposure, Public Interaction Binding, Conversation, Merchant Attention, CustomerContext, Notification and downstream commitments remain independently owned.**
