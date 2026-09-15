# MS-PROT-046 v1.1 — Final Semantic Resolution

**Status:** **ACCEPTED**  
**Supersedes:** MS-PROT-046 v1.0  
**Reason for revision:** Resolve every material ambiguity identified during sequential review.

The decisions already approved remain:

```text
046-A  Independent PublishedContent / Opportunity / Announcement
046-B  Two-layer classification
046-C  Merchant-scoped Opportunity + optional external provenance
046-D  Opportunity lifecycle + derived actionability
046-E  Lightweight Announcement Operational Object
046-F  Bounded Subscription scopes
046-G  Merchant-scoped SubscriberContext
046-H  Shared publication-state contract
046-I  Minimal Subscription lifecycle
```

The remaining decisions are now resolved as follows.

---

## Decision 046-J — Resubscription

**ACCEPTED**

A cancelled Subscription remains historical and terminal:

```text
Subscription S1
    ACTIVE
       ↓
    CANCELLED
```

If the participant subscribes again:

```text
SubscriberContext SC1

Subscription S1
    CANCELLED

Subscription S2
    ACTIVE
```

Main Street shall **not reactivate S1**.

This preserves distinct periods of expressed intent and avoids rewriting cancellation history.

Canonical rule:

> **Renewed subscription intent creates a new Subscription instance. A cancelled Subscription remains cancelled.**

The merchant does not need to understand this distinction; the platform may simply show:

```text
Subscribed
Unsubscribed
Subscribed again
```

---

# Decision 046-K — Publication revisions

A stable publication identity and its published revisions must be distinguished.

**ACCEPTED**

Example:

```text
Opportunity O100
        │
        ├── Revision R1
        │      published
        │
        ├── Revision R2
        │      correction
        │
        └── Revision R3
               current
```

The merchant still experiences:

```text
Edit Opportunity
Save
Publish
```

not manual version management.

### Governing rule

> **Published informational objects retain stable identity while Main Street preserves sufficient immutable revision evidence to reconstruct materially relevant previously published representations.**

This is particularly important for:

```text
deadlines
eligibility
application links
provider information
announcements
Enquiry provenance
```

---

# Decision 046-L — Editing published content

Editing a currently published object shall not require creating a completely new Opportunity, Article or Announcement.

**ACCEPTED**

Example:

```text
Opportunity O100
deadline = 14 October
```

Merchant corrects:

```text
deadline = 21 October
```

Identity remains:

```text
O100
```

Main Street records a new publication revision.

Therefore:

```text
object identity
    remains stable

authoritative current data
    changes

material publication history
    remains reconstructible
```

This follows the authoritative effect model:

```text
MUTATE_DATA
```

rather than:

```text
delete old object
create new object
```

---

# Decision 046-M — Archive semantics

`ARCHIVED` shall **not** become a universal publication lifecycle state.

**ACCEPTED**

Shared lifecycle remains:

```text
DRAFT
PUBLISHED
WITHDRAWN
```

Historical or old material may be treated as archived by:

```text
read model
retention policy
merchant presentation
historical discovery policy
```

where required.

Therefore:

```text
ARCHIVED
≠ universal publication state
```

If a future capability proves that archival has independent operational consequences, that capability may introduce the semantic explicitly.

---

# Decision 046-N — Scheduled publication

Scheduled publication is supported without introducing a `SCHEDULED` lifecycle state.

**ACCEPTED**

Publication state and exposure timing remain orthogonal.

Example:

```text
Opportunity O1

publication state:
    PUBLISHED

exposure:
    publishFrom = 1 September
```

Before 1 September:

```text
authorised for publication
but
not publicly exposed
```

From 1 September:

```text
publicly exposed
```

Canonical model:

```text
Publication state
    DRAFT
    PUBLISHED
    WITHDRAWN

Exposure window
    publishFrom?
    publishUntil?
```

This model applies wherever scheduled exposure is supported.

The merchant experience can simply be:

```text
Publish now

or

Publish:
1 September 2026, 09:00
```

The platform handles the distinction internally.

---

# Decision 046-O — Announcement expiry

Announcement expiry uses exposure timing rather than a new lifecycle state.

**ACCEPTED**

Example:

```text
Announcement:
"Closed on Bank Holiday Monday"

publishFrom:
Friday

publishUntil:
Tuesday
```

After Tuesday:

```text
Announcement remains historically PUBLISHED
but is no longer actively exposed
```

It does not transition to:

```text
EXPIRED
```

unless a later proven operational requirement requires such semantics.

---

# Decision 046-P — Opportunity dates versus publication exposure

These are separate.

**ACCEPTED**

Consider:

```text
Opportunity

publishFrom:
1 August

applicationsOpen:
1 September

applicationDeadline:
31 October
```

Then:

```text
1–31 August
    Opportunity may be publicly visible
    actionability = NOT_YET_OPEN

1 September–31 October
    actionability = OPEN

after 31 October
    actionability = CLOSED_BY_DEADLINE
```

The merchant therefore can advertise an Opportunity before applications open.

This prevents the architecture from confusing:

```text
Can customers see this?
```

with:

```text
Can customers act on this opportunity now?
```

---

# Decision 046-Q — Duplicate Opportunity detection

Duplicate detection is **assistive**, never authoritative.

**ACCEPTED**

If a merchant starts creating:

```text
Commonwealth Master's Scholarship
```

and Main Street detects a similar existing merchant Opportunity, it may say:

```text
You may already have a similar Opportunity.
```

The merchant can inspect it.

Main Street shall not automatically:

```text
merge
replace
reject
link
```

the objects based merely on similarity.

Cross-merchant duplicate detection shall likewise never collapse merchant ownership.

Canonical rule:

> **Similarity may assist merchants; it does not establish semantic identity.**

AI may participate in detection, but not in automatic identity decisions.

---

# Decision 046-R — Provider, source and author

These meanings remain distinct.

**ACCEPTED**

For an Opportunity:

```text
Merchant
    publishes representation

Provider
    actually provides the opportunity

Source
    where the merchant obtained information
```

For general content:

```text
Merchant
    owns publication

Author
    person/organisation credited with authorship

Source
    external provenance where applicable
```

Main Street shall not force every merchant to populate all of them.

Canonical merchant workflow:

```text
Ask only what is relevant.
```

If:

```text
merchant is also provider
```

Main Street should not make the merchant redundantly identify itself again.

If:

```text
provider is external
```

the supported interface may ask:

```text
Provider
Official link
```

and expose additional provenance only where needed.

---

# Decision 046-S — External ingestion

External content import may be supported, but imported data does not become authoritative automatically.

**ACCEPTED**

Conceptually:

```text
External source
      ↓
candidate imported data
      ↓
validation / merchant review where required
      ↓
merchant-owned publication representation
```

AI or external extraction may reduce manual entry.

It may not silently establish merchant-published facts without the applicable authority path.

This directly supports the overarching principle:

> **Simplify merchant work without surrendering semantic control.**

---

# Decision 046-T — Classification semantics

The previously approved two-layer model is tightened.

```text
SEMANTIC CLASSIFICATION
    platform/capability-owned

DESCRIPTIVE ORGANISATION
    merchant-owned
```

Example:

```text
Opportunity O1

Semantic:
    educationLevel = POSTGRADUATE
    fundingType = FULL

Merchant organisation:
    "Featured Scholarships"
    "Engineering Opportunities"
```

Semantic classification may drive:

```text
typed filtering
Requirements
Subscription matching
Notification matching
```

Merchant categories may drive:

```text
navigation
display grouping
subscriber-selected descriptive interests
```

Merchant categories do **not** gain hidden behavioural meaning.

---

# Decision 046-U — Opportunity types

MS-PROT-046 shall **not** establish a closed global Opportunity-type enum such as:

```text
SCHOLARSHIP
GRANT
JOB
BURSARY
COMPETITION
TRAINING
...
```

as universal runtime architecture.

Instead, Opportunity type/classification is capability-owned structured data.

This means Main Street can support new information businesses without modifying the semantic kernel simply because another kind of Opportunity appears.

Where particular types require different executable behaviour, that behaviour must be modelled explicitly rather than inferred from a merchant label.

---

# Decision 046-V — Search architecture boundary

Search implementation is explicitly delegated to the subsequent Search/Discovery specification.

This is **not an unresolved ambiguity in 046**.

MS-PROT-046 establishes only:

```text
published informational subjects
    may participate in search/discovery projections

semantic type must survive indexing

structured classifications may be searchable/filterable

search remains derived
```

The next specification may determine:

```text
indexing
query model
ranking
filter composition
merchant-site scope
```

without changing Publication semantics.

---

# Decision 046-W — Notification frequency

Exact delivery frequencies are delegated to the Notification specification.

MS-PROT-046 establishes only that Subscription may express supported delivery preferences.

For example, a future Notification capability could support:

```text
IMMEDIATE
DAILY_DIGEST
WEEKLY_DIGEST
```

but 046 does not own those values.

This is a capability boundary, not ambiguity.

---

# Decision 046-X — Deadline reminders

Deadline reminders belong to composition between:

```text
Opportunity
+
Subscription
+
Notification
```

MS-PROT-046 establishes:

```text
authoritative Opportunity deadline
        ↓
registered reminder policy
        ↓
matching Subscription
        ↓
Notification
```

But Notification owns delivery behaviour.

AI does not independently decide when to send reminders.

---

# Decision 046-Y — Social distribution

Social publishing remains an integration reaction to Main Street publication facts.

Canonical:

```text
AnnouncementPublished
        ↓
Configured Social Distribution
        ↓
external provider
```

Main Street remains authoritative.

No external provider controls:

```text
Announcement identity
Publication lifecycle
merchant content ownership
```

---

# Decision 046-Z — SEO

SEO remains a **storefront projection concern**, not Publication semantics.

Publication objects may provide authoritative structured information from which Main Street generates:

```text
page titles
descriptions
canonical routes
structured metadata
internal navigation
search-engine-readable content
```

where appropriate.

There shall be no:

```text
50-mile SEO geofence
```

or other arbitrary operational restriction introduced merely for ranking purposes.

Google Business Profile remains optional and independent of information-publication eligibility.

---

# Cross-cutting merchant-experience invariant

The principle you established is now normative across Main Street:

> **Main Street shall simplify merchant operation without artificially limiting what merchants can represent or do within supported platform semantics.**

Its architectural interpretation is:

```text
Rich internal semantic model
        ↓
context-aware projection
        ↓
merchant sees only what is needed
```

not:

```text
simpler merchant UI
        =
weaker semantic architecture
```

Nor:

```text
powerful architecture
        =
merchant must configure everything
```

Instead:

```text
SUPPORTED POWER
      +
PROGRESSIVE DISCLOSURE
      +
CONTEXTUAL DEFAULTS
      +
INFERENCE
      +
MINIMUM NECESSARY QUESTIONS
```

The merchant interface therefore exposes complexity **only when the merchant's actual operation requires it**.

---

# Final MS-PROT-046 semantic model

```text
PUBLICATION CAPABILITY
│
├── PublishedContent
│      ├── stable identity
│      ├── content schema
│      ├── publication state
│      ├── exposure
│      └── revision history
│
├── Opportunity
│      ├── stable identity
│      ├── structured Opportunity data
│      ├── provider/source provenance where relevant
│      ├── publication state
│      ├── exposure
│      ├── derived actionability
│      └── revision history
│
└── Announcement
       ├── lightweight update data
       ├── publication state
       ├── exposure window
       └── revision history where materially relevant
```

Supporting semantics:

```text
Classification
    ├── semantic
    └── descriptive

SubscriberContext
        ↓
Subscription
    ACTIVE → CANCELLED

Publication
        ↓
Domain Event
        ↓
Notification / Social Distribution

Published Subject
        ↓
Search / Discovery projection

Published Subject
        ↓
Enquiry SUBJECT relationship
```

---

# MS-PROT-046 governance result

All material semantic questions identified during review are now resolved.

Remaining implementation-specific matters are explicitly owned by later specifications rather than left as ambiguous semantic choices.

Therefore:

```text
PROPOSE
   ↓
REVIEW / FALSIFY
   ↓
DECISION RESOLUTION
   ↓
REVISE
   ↓
VALIDATE
   ↓
ACCEPT
```

## Final status

**MS-PROT-046 v1.1 — ACCEPTED**

We can now proceed sequentially to **MS-PROT-047**.
