# MS-PROT-046 v1.3 — Opportunity Material Revision & Publication History Amendment

**Document ID:** MS-PROT-046  
**Version:** 1.3  
**Status:** **ACCEPTED by manual approval on 4 September 2026**  
**Approved:** Manual approval on 4 September 2026 after implementation-discovered design-gap review, falsification, mandatory ambiguity review and complete-authority presentation in ChatGPT  
**Authority type:** Semantic/design amendment  
**Governed by:** `designs/DESIGN-RULES.md` and `designs/DOCUMENT-GOVERNANCE.md`  
**Amends:** Composite MS-PROT-046 v1.1 + v1.2 within Opportunity material-revision, temporal-boundary and Publication-history scope only  
**Depends on:** Composite MS-PROT-045 v1.0 + v1.1; composite MS-PROT-046 v1.1 + v1.2; MS-PROT-053 where retention later applies  
**Resolves:** MS-PROT-045 deferred Opportunity-schema scope required by IMP-07-P2B  
**Closes for implementation:** The semantic design gap preventing `IMP-07-P2B — Reconstructible material revisions + lifecycle history` from becoming implementation-ready  
**Purpose:** Define the minimum typed Opportunity material schema, immutable revision reconstruction contract and append-only Publication history required for production Publication persistence without introducing arbitrary payloads, business-type templates, Exposure ownership, application orchestration or retry-idempotency semantics.

---

## 1. Governing Decision

Main Street SHALL represent each material Opportunity revision as immutable Publication-owned evidence bound to:

```text
exact Merchant Scope
+
stable Opportunity identity
+
immutable logical revision identity
+
exact immutable Opportunity schema identity/version
+
exact material Opportunity values
+
exact Publication-owned exposure-window evidence
```

Main Street SHALL preserve sufficient immutable evidence to reconstruct the material representation of every retained Opportunity revision.

Publication SHALL additionally preserve append-only ordered history for every successful operation that changes the exact revision authorised for publication or changes the Opportunity between `PUBLISHED` and `WITHDRAWN`.

Canonical:

```text
Opportunity
    stable identity
        │
        ├── Material Revision R1
        ├── Material Revision R2
        └── Material Revision R3

Currentness
    current revision   → R3
    lifecycle          → PUBLISHED
    published revision → R2

Publication History
    1 PUBLISH    current=R1 published=R1
    2 PUBLISH    current=R2 published=R2
    3 WITHDRAW   current=R3 published=R2
    4 REPUBLISH  current=R3 published=R3
```

Material revision history and Publication history are distinct authoritative evidence.

---

## 2. Scope

This amendment governs only:

```text
Opportunity material schema v1
Opportunity material-revision identity and reconstruction
Publication-owned temporal-boundary representation used by Opportunity
immutable historical schema affinity
Opportunity Publication history
publish / withdraw / republish history evidence
P2B persistence-facing semantic contracts
```

It does not redefine other Publication families.

---

## 3. Explicit Non-Goals

This amendment SHALL NOT define or implement:

```text
logical-operation retry idempotency
application-service orchestration
HTTP/API transport
Public Exposure contracts/evaluation
storefront projection
Opportunity → Enquiry participation
Enquiry creation
Search/indexing
Notification delivery
social distribution
AI publication authority
numeric retention duration
generic arbitrary structured-data storage
universal JSON/value patching
merchant-created executable fields
business-category templates
```

Those responsibilities remain governed by their existing authorities and implementation nodes.

---

## 4. Canonical Terms

### 4.1 Opportunity Material Revision

An **Opportunity Material Revision** is immutable Publication-owned evidence recording the exact material business representation of one Opportunity logical revision.

Its durable identity is:

```text
Merchant Scope
+
Opportunity identity
+
Revision identity
```

Attribute similarity does not establish revision identity.

### 4.2 Opportunity Schema Identity

The initial schema identity is:

```text
owner capability: publication
schema:           opportunity
version:          1
```

Canonical shorthand:

```text
publication / opportunity@1
```

The shorthand is not a database, Java or transport identifier.

### 4.3 Opportunity Publication History Entry

An **Opportunity Publication History Entry** is immutable append-only Publication-owned evidence recording one successful:

```text
PUBLISH
WITHDRAW
REPUBLISH
```

operation relevant to historical publication truth.

It is not an Audit Record, Domain Event, projection or transport log.

### 4.4 Opportunity Temporal Boundary

An **Opportunity Temporal Boundary** preserves the exact granularity required by Opportunity business data.

It has exactly one of:

```text
CALENDAR_DATE
    LocalDate
    +
    interpretation ZoneId

EXACT_INSTANT
    Instant
```

A client clock is never authoritative.

---

## 5. Opportunity Schema v1

`publication / opportunity@1` contains the following material fields.

| Field identifier | Cardinality | Meaning |
|---|---:|---|
| `title` | exactly 1 | Merchant-approved human-readable Opportunity title |
| `description` | 0..1 | Merchant-approved descriptive content |
| `eligibility-information` | 0..1 | Merchant-approved information describing eligibility or qualification |
| `external-provider-name` | 0..1 | Descriptive name of an external provider where the Opportunity is provided by another organisation/person |
| `source-name` | 0..1 | Descriptive provenance label identifying where the merchant obtained the information |
| `external-link` | 0..N | Role-qualified external navigation reference |
| `applications-open` | 0..1 | Structured lower temporal boundary for Opportunity application actionability |
| `application-deadline` | 0..1 | Structured upper temporal boundary for Opportunity application actionability |

`title` MUST contain non-blank text.

The other fields are optional because valid Opportunities need not have an external provider, source, external application mechanism, eligibility restriction or temporal application window.

Absence of an optional field means only that the corresponding structured fact is not recorded in that revision.

Absence MUST NOT be converted into a fabricated value.

---

## 6. External Link Contract

Each `external-link` has:

```text
role
uri
label?
```

Initial role vocabulary:

```text
OFFICIAL_APPLICATION
PROVIDER
SOURCE
```

`uri` MUST be an absolute URI accepted by the applicable security/input-validation boundary.

`label` is optional descriptive text and carries no executable authority.

The same Opportunity MAY carry more than one external link, including more than one link with the same role.

An external link:

```text
does not create Enquiry
does not create Application
does not create Booking
does not create Order
does not establish provider fulfilment
does not establish external provider identity
```

It is Publication data used for audience-facing navigation unless another accepted capability establishes additional semantics.

---

## 7. Provider and Source Semantics

The publishing Merchant Scope remains the owner of the Opportunity representation.

`external-provider-name` records descriptive external-provider evidence only.

Its absence means:

```text
no external-provider name is recorded
```

and SHALL NOT by itself mean:

```text
merchant is provider
```

or:

```text
provider does not exist
```

`source-name` and `SOURCE` links record provenance only.

Provider and Source remain semantically distinct.

Neither field establishes a cross-merchant identity relationship.

---

## 8. Temporal Boundary Semantics

### 8.1 Calendar-date boundary

`CALENDAR_DATE` contains:

```text
LocalDate
+
IANA ZoneId
```

The ZoneId is part of authoritative boundary interpretation and SHALL NOT be silently supplied by client clock, browser locale or server deployment timezone.

For a lower boundary such as `applications-open`:

```text
effective lower instant
    =
start of that calendar date
in the recorded ZoneId
under the applicable timezone rules
```

For an upper deadline boundary:

```text
effective cutoff
    =
start of the following calendar date
in the recorded ZoneId
under the applicable timezone rules
```

Therefore a date-only application deadline is inclusive of the entire recorded calendar date.

### 8.2 Exact-instant boundary

`EXACT_INSTANT` contains one absolute `Instant`.

For an application opening:

```text
current trusted instant < boundary
    → opening boundary not yet reached
```

For an application deadline:

```text
current trusted instant >= boundary
    → deadline has been reached/passed
```

### 8.3 Input/inference boundary

Presentation, ingestion or AI MAY propose an interpretation ZoneId.

The ZoneId SHALL become authoritative only through the same merchant/owner-qualified approval path as the Opportunity material.

Main Street SHALL NOT infer an authoritative timezone merely from browser locale or deployment location.

This amendment defines temporal material truth; final Opportunity actionability composition remains owned by the later P6 implementation node and existing MS-PROT-046 actionability authority.

---

## 9. Publication Exposure Window in a Material Revision

An Opportunity Material Revision SHALL also retain the Publication-owned exposure-window evidence effective for that revision:

```text
publishFrom?
publishUntil?
```

Each boundary uses the same temporal-boundary representation defined in this amendment.

The semantics remain those of MS-PROT-046 v1.2:

```text
no publishFrom
    → no lower Exposure time bound

no publishUntil
    → no upper Exposure time bound
```

For a calendar-date `publishFrom`, the effective lower instant is the start of the recorded date in its ZoneId.

For a calendar-date `publishUntil`, the effective upper cutoff is the start of the following date in its ZoneId.

Changing either exposure boundary is a material Publication mutation and therefore creates a new logical Opportunity revision when the authoritative value changes.

Exposure resolution remains owned by MS-PROT-027.

---

## 10. Revision Establishment and Immutability

For every Opportunity revision participating in production currentness after this contract applies, exactly one Opportunity Material Revision SHALL exist for the same:

```text
Merchant Scope
Opportunity identity
Revision identity
```

Once recorded, an Opportunity Material Revision SHALL NOT be:

```text
updated
rebound to different material
rebound to a different schema version
reassigned to another Opportunity
reassigned to another Merchant Scope
```

Attempting to reuse an already-recorded revision identity for different material SHALL fail without altering previously committed material.

A later revision MAY contain values identical to an earlier historical revision, but creation of a new logical revision MUST correspond to a material mutation intent; retry/no-op suppression belongs to P3 application/idempotency orchestration.

---

## 11. Historical Schema Affinity

Every Opportunity Material Revision SHALL retain its exact immutable Opportunity schema identity/version.

Historical reconstruction MUST resolve the revision using that exact historical schema affinity.

Rejected:

```text
R1 created under opportunity@1
        ↓
opportunity@2 becomes current
        ↓
reinterpret R1 as opportunity@2
```

Required:

```text
R1 → opportunity@1
R8 → opportunity@2
```

Schema evolution SHALL NOT silently reinterpret old material.

---

## 12. Material Reconstruction Contract

Given:

```text
Merchant Scope
Opportunity identity
Revision identity
```

Publication authority SHALL be able, while the revision remains retained under applicable data-protection policy, to reconstruct the exact Opportunity Material Revision committed for that identity.

Reconstruction SHALL preserve:

```text
exact schema identity/version
exact recorded field presence/absence
exact recorded field values
external-link roles and values
exact Opportunity temporal boundaries
exact Publication exposure-window evidence
```

Reconstruction SHALL NOT:

```text
substitute current values
substitute the latest schema
re-run AI extraction
re-fetch an external provider/source
resolve through current storefront projection
infer missing values
```

---

## 13. Current and Published Revision Resolution

Current material is determined only by:

```text
OpportunityPublicationState.currentRevisionIdentity
        ↓
exact Opportunity Material Revision
```

Published material is determined only when a published revision exists:

```text
OpportunityPublicationState.publishedRevisionIdentity
        ↓
exact Opportunity Material Revision
```

Current material and published material MAY therefore differ.

Canonical:

```text
current revision   = R3
published revision = R2
```

means:

```text
merchant has material R3 as current truth
public publication remains bound to R2
until an explicit publication operation changes that binding
```

No projection may collapse these identities.

---

## 14. Publication History

Publication SHALL maintain a totally ordered append-only history for each merchant-scoped Opportunity.

Each history entry contains at minimum:

```text
Merchant Scope
Opportunity identity
history order
operation kind
current revision identity at successful operation
published revision identity relevant after the operation
resulting Publication lifecycle
```

`history order` establishes durable per-Opportunity ordering.

Its exact database numeric representation remains an implementation detail.

History entries SHALL be immutable.

---

## 15. PUBLISH History Semantics

A successful `PUBLISH` SHALL append one `PUBLISH` history entry.

This includes:

```text
DRAFT → PUBLISHED
```

and a successful publication of a newer current revision while the Opportunity already remains:

```text
PUBLISHED → PUBLISHED
```

Example:

```text
before:
    lifecycle          PUBLISHED
    current            R2
    published          R1

publish R2

after:
    lifecycle          PUBLISHED
    current            R2
    published          R2
```

The second publication is material historical evidence even though lifecycle did not change.

---

## 16. WITHDRAW History Semantics

A successful `WITHDRAW` SHALL append one `WITHDRAW` history entry.

The entry MUST retain:

```text
current revision identity at withdrawal
+
the exact previously published revision identity being withdrawn
+
resulting lifecycle = WITHDRAWN
```

These identities may differ.

Example:

```text
current   = R3
published = R2
        ↓
withdraw
        ↓
history records:
    current   R3
    published R2
```

Withdrawal does not rewrite R2 or R3.

---

## 17. REPUBLISH History Semantics

A successful explicit `REPUBLISH` from `WITHDRAWN` SHALL append one `REPUBLISH` history entry.

Republish binds the exact current revision as the newly published revision.

Example:

```text
WITHDRAWN
current = R3
        ↓
REPUBLISH
        ↓
PUBLISHED
published = R3
```

Earlier `PUBLISH` and `WITHDRAW` entries remain unchanged.

---

## 18. Revision Versus Publication History

The following operations create an Opportunity Material Revision but do not by themselves create Publication History:

```text
initial DRAFT material establishment
material edit while DRAFT
material edit while PUBLISHED
material edit while WITHDRAWN
```

The following successful operations create Publication History:

```text
PUBLISH
WITHDRAW
REPUBLISH
```

Therefore:

```text
Material Revision History
    ≠
Publication History
```

A material edit while `PUBLISHED` does not silently publish the new revision.

---

## 19. Persistence and Transaction Boundary

P2B persistence SHALL be capable of recording and reading already-valid:

```text
Opportunity Material Revisions
Opportunity Publication History Entries
```

without becoming the semantic owner of application commands.

P2B SHALL NOT introduce logical-operation retry identity.

The later P3 application mutation boundary SHALL compose, where one authoritative operation requires them:

```text
currentness CAS
+
new material revision
+
Publication History append
```

within the transaction required to prevent partial authoritative effects.

P2B does not waive that downstream atomicity requirement merely because its individual stores are implemented first.

---

## 20. Concurrency and Duplicate Evidence

Revision identity uniqueness remains merchant-and-Opportunity scoped.

Publication History ordering for one Opportunity SHALL be serialized sufficiently that two committed entries have an unambiguous durable order.

P2B SHALL NOT implement logical-command retry idempotency.

Expected-current conflict remains distinct from logical-operation duplicate detection.

A duplicate persistence attempt that would redefine immutable revision or history evidence SHALL fail rather than overwrite existing evidence.

---

## 21. AI and External-Ingestion Boundary

AI/external extraction MAY produce candidate Opportunity material.

Candidate material is not an Opportunity Material Revision merely because it has the same shape.

Required:

```text
candidate data
    ↓
applicable merchant/owner-qualified approval
    ↓
Publication-owned authoritative mutation
    ↓
Opportunity Material Revision
```

P2B persistence SHALL NOT convert unapproved candidate content into authoritative history.

---

## 22. Projection, Exposure and Enquiry Boundaries

Opportunity Material Revisions are authoritative Publication data.

They are not automatically public.

```text
material exists
    ≠ PUBLISHED
    ≠ EXPOSED
    ≠ actionable
```

Publication lifecycle determines legitimate publication candidacy.

MS-PROT-027 determines public Exposure.

Opportunity actionability remains a separate derivation.

MS-PROT-049 determines Public Interaction participation.

MS-PROT-043 owns Enquiry.

No P2B implementation may move those responsibilities into revision persistence.

---

## 23. Retention Boundary

This amendment defines what must be retained while historical Opportunity revision evidence remains within its applicable retention lifecycle.

It SHALL NOT define a universal numeric retention period.

`MS-PROT-046-V12-DQ-008` remains deferred under the applicable MS-PROT-053 data-protection/legal-policy implementation boundary.

P2B SHALL introduce no autonomous revision deletion or expiry rule.

---

## 24. Hard Invariants

1. Opportunity identity and revision identity are distinct.
2. Opportunity Material Revision identity is Merchant Scope + Opportunity identity + revision identity.
3. Every retained material revision is immutable.
4. A revision binds one exact immutable Opportunity schema version.
5. Historical revisions are not reinterpreted using the current schema.
6. `publication / opportunity@1` is capability-owned, not merchant-created.
7. Arbitrary JSON/key-value material is not Opportunity semantic authority.
8. `title` is required; other v1 fields are optional unless later schema authority says otherwise.
9. Provider and Source remain distinct from Publisher.
10. External-provider absence does not fabricate provider identity.
11. External links are role-qualified navigation facts, not execution authority.
12. Opportunity temporal boundaries preserve exact temporal granularity and interpretation.
13. Client time/browser locale is not authoritative temporal evidence.
14. Publication exposure-window evidence remains Publication-owned.
15. Changing material Opportunity data or exposure-window evidence creates a new logical revision.
16. Current revision and published revision may differ.
17. A material edit while PUBLISHED does not implicitly publish the new revision.
18. Publication History is append-only and totally ordered per merchant-scoped Opportunity.
19. Publishing a new revision while remaining PUBLISHED is historical publication evidence.
20. Withdrawal preserves the exact revision that had actually been published.
21. Explicit republish preserves earlier publication/withdrawal history.
22. Material Revision History and Publication History are distinct.
23. P2B does not own logical-operation retry idempotency.
24. P2B does not own Exposure, actionability, interaction participation, Enquiry or transport.
25. No numeric historical-retention duration is introduced by this authority.

---

## 25. Falsification Results

The model was tested against:

```text
external scholarship publisher
merchant's own job/vacancy
grant without deadline
multiple external links
date-only deadline
exact-instant deadline
PUBLISHED edit without publication
PUBLISHED revision rebinding
withdraw after unpublished edit
withdraw → edit → republish
revision-identity reuse
future Opportunity schema version
merchant/provider/source distinction
```

The initial proposal failed two cases:

```text
unqualified generic links
date-only deadlines without timezone interpretation
```

The final proposal resolves them through:

```text
role-qualified external links
+
typed temporal boundaries with explicit ZoneId for calendar-date interpretation
```

No tested case requires an Offering, commerce model, merchant-specific template or arbitrary payload.

---

## 26. Trade-Offs

### Chosen approach

A minimal typed Opportunity schema plus Publication-specific immutable history.

Benefits:

```text
bounded implementation scope
strong historical reconstruction
no arbitrary payload
no scholarship/job/grant template branching
precise temporal evidence
direct path to P2B persistence
preserves later schema evolution
```

Costs:

```text
initial Opportunity schema deliberately limited
future materially new Opportunity fields require schema evolution
calendar-date boundaries require explicit interpretation ZoneId
Publication persistence acquires additional immutable historical rows
```

These costs are accepted because they preserve semantic correctness and historical interpretability.

### Rejected: arbitrary JSON/value map

Lower short-term implementation cost, but destroys registered field authority and permits uncontrolled semantic growth.

**Rejected.**

### Rejected: vertical-specific Opportunity classes

Simple for one publisher category but accumulates category/template architecture.

**Rejected.**

### Rejected for this node: universal generic runtime value engine

Potentially reusable but expands P2B into a broader schema/runtime infrastructure programme not required to prove this vertical slice.

**Deferred unless later implementation evidence justifies it.**

---

## 27. Deferred Scope

This amendment does not resolve:

```text
generic cross-capability semantic-value runtime
bounded rich-text format
future Opportunity schema versions/fields
classification portfolio
media fields
exact Java class representation
exact PostgreSQL table/index representation
exact API/JSON representation
logical-operation retry/idempotency mechanism
numeric retention duration
Search/index architecture
```

Existing `MS-PROT-046-V12-DQ-001`, `DQ-002`, `DQ-003`, `DQ-004` through `DQ-008` remain governed according to their existing scopes except that the semantic content required to implement the initial Opportunity material revision is now defined.

Exact Java and PostgreSQL choices remain implementation details only insofar as they preserve this amendment.

---

## 28. Implementation Consequence

After this authority is accepted and governance-formalised, IMP-07-P2B becomes implementation-ready.

The implementation sequence is:

```text
accepted composite Publication authority
        ↓
derive exact P2B contracts
        ↓
write failing semantic/persistence tests
        ↓
minimum typed Opportunity material representation
        ↓
minimum immutable material-revision persistence
        ↓
minimum append-only Publication-history persistence
        ↓
targeted verification
        ↓
full PostgreSQL verification
        ↓
P2B conformance evidence
        ↓
graph refresh
        ↓
implementation-status synchronisation
        ↓
cycle-closing commit/CI
```

P3 remains blocked until P2B is `CONFORMING_COMPLETE`.

---

## 29. Amendment Effect

This amendment adds implementation-constraining meaning to the surviving composite MS-PROT-046 v1.1 + v1.2 authority only within:

```text
initial Opportunity schema
material-revision reconstruction
temporal-boundary material representation
Publication-history evidence
```

All unrelated surviving MS-PROT-046 v1.1/v1.2 rules remain unchanged.

MS-PROT-045 remains the owner of generic DataConcept/Schema/Field semantics.

MS-PROT-046 remains the owner of Opportunity and Publication truth.

---

## 30. Acceptance Statement

> **Main Street shall preserve each Opportunity's authoritative information as immutable schema-affined material revisions and shall preserve ordered append-only evidence of every successful publish, withdrawal and republish operation that changes historical publication truth. The initial `publication / opportunity@1` schema provides a bounded, business-type-neutral representation of title, descriptive and eligibility information, optional external provider/source provenance, role-qualified external navigation links and typed application temporal boundaries. Publication-owned exposure-window evidence is revision-affined but remains distinct from Exposure. Current and published revision identities may differ, historical schema affinity is immutable, arbitrary payloads and merchant-created executable fields remain prohibited, and P2B persistence does not acquire application idempotency, Exposure, actionability, Enquiry or transport ownership.**
