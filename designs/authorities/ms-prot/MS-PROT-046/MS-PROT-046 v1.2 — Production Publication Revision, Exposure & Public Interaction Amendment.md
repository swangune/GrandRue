# MS-PROT-046 v1.2 — Production Publication Revision, Exposure & Public Interaction Amendment

**Document ID:** MS-PROT-046  
**Version:** 1.2  
**Status:** **ACCEPTED by manual approval on 27 August 2026**  
**Amends:** MS-PROT-046 v1.1  
**Approved:** Manual approval on 27 August 2026 after Target-10 authority trace, implementation-evidence review, falsification, corpus-conformance review and recommendation  
**Purpose:** Make Publication production-executable without turning storefront presentation, Exposure, Enquiry, Search, Notifications or provider integrations into Publication authority.

---

# 1. Publication ownership remains unchanged

The Publication capability continues to own three distinct information-object families:

```text
PublishedContent
Opportunity
Announcement
```

They SHALL NOT be collapsed into:

```text
Offering
Listing
GenericContent
StorefrontItem
```

The Offering/Listing/Publication separation remains authoritative.

---

# 2. Stable object identity and immutable publication revisions

Every Publication object SHALL have:

```text
stable merchant-scoped identity
+
one current logical revision
+
immutable previous materially relevant revisions
```

A material edit creates a new logical revision.

Example:

```text
Opportunity O100
    Revision 7
        deadline = 31 October

merchant corrects deadline

Opportunity O100
    Revision 8
        deadline = 15 November
```

`O100` remains the same Opportunity.

Revision 7 remains reconstructible.

This makes the accepted MS-PROT-046 revision rule implementation-constraining rather than merely descriptive.

---

# 3. Publication mutation concurrency

A material Publication mutation SHALL be conditional on the expected current revision.

Conceptually:

```text
read O100@R7
        ↓
edit based on R7
        ↓
current still R7?
    YES → commit R8
    NO  → conflict
```

Rejected:

```text
silent last-write-wins
```

for concurrent material edits.

Unrelated Publication objects remain independently mutable.

---

# 4. Retry idempotency

Retrying the same logical Publication operation SHALL NOT create:

```text
two Publication objects
two revisions
two publish transitions
```

The channel-independent logical-operation/idempotency rules of MS-PROT-059 apply.

---

# 5. Publication lifecycle

The shared lifecycle remains:

```text
DRAFT
PUBLISHED
WITHDRAWN
```

`ARCHIVED`, `EXPIRED` and `SCHEDULED` SHALL NOT be added as universal states.

Initial production SHALL support:

```text
DRAFT → PUBLISHED

PUBLISHED → WITHDRAWN

WITHDRAWN → PUBLISHED
    only through an explicit republish operation
    over a valid current revision
```

Republishing SHALL preserve previous publication and withdrawal evidence.

Editing a `PUBLISHED` object does not itself withdraw it.

---

# 6. Publish binds an exact revision

Publishing SHALL establish exactly which current Publication revision became authoritative for publication.

Conceptually:

```text
Opportunity O100
current revision = R8
        ↓
publish
        ↓
O100 remains PUBLISHED
published representation based on R8
```

If another actor changes the object before the publish command commits, stale publication SHALL conflict rather than publishing an unintended revision.

---

# 7. AI cannot publish candidate content directly

AI may:

```text
draft
rewrite
classify
suggest
extract candidate structured data
```

but an AI candidate SHALL NOT become a published authoritative representation merely because the model produced it.

Required:

```text
AI candidate
    ↓
merchant-confirmed / owner-qualified content
    ↓
Publication mutation
    ↓
publish
```

This composes with MS-PROT-057.

---

# 8. Publication state and Exposure remain distinct

MS-PROT-046 v1.1 previously described:

```text
PUBLISHED
+
publishFrom / publishUntil
```

as scheduled exposure.

Target 10 SHALL now compose that meaning through the accepted production Exposure architecture rather than maintain a parallel visibility system.

Canonical:

```text
Publication lifecycle
        ↓
legitimate public candidate?
        ↓
Publication-owned exposure-window evidence
        ↓
MS-PROT-027 Exposure resolution
        ↓
EXPOSE | WITHHOLD
```

Exposure remains an independent deterministic observation decision.

---

# 9. DRAFT and WITHDRAWN are not PUBLIC candidates

For PUBLIC observation:

```text
DRAFT
    → not a legitimate public Publication candidate

WITHDRAWN
    → not a legitimate public Publication candidate
```

No Exposure verdict is required to resurrect or suppress an object whose Publication lifecycle already makes it ineligible.

---

# 10. PUBLISHED enters Exposure resolution

A `PUBLISHED` Publication may become a legitimate PUBLIC candidate.

Its actual observation is still subject to:

```text
current exposure window
data protection
merchant scope
applicable Exposure contract
other governing restrictions
```

Therefore:

```text
PUBLISHED
≠ automatically visible
```

---

# 11. Exposure window ownership

`publishFrom` and `publishUntil` remain Publication-owned facts.

They SHALL NOT become generic Exposure-database fields.

Changing either is a material Publication mutation and follows Publication revision/concurrency rules.

Current semantics are:

```text
publishFrom absent
    → no lower time bound

publishUntil absent
    → no upper time bound

current time < publishFrom
    → WITHHOLD

current time >= publishUntil
    → WITHHOLD

otherwise
    → time requirement satisfied
```

The clock used for runtime determination must be trusted server-side evidence.

Client clock values are not authority.

---

# 12. Scheduled publication requires no state-changing timer

Example:

```text
state = PUBLISHED
publishFrom = 1 September 09:00
```

Before that time:

```text
WITHHOLD
```

At/after that time:

```text
Exposure may resolve EXPOSE
```

No background worker needs to mutate:

```text
SCHEDULED → PUBLISHED
```

because no such lifecycle is introduced.

This keeps scheduled storefront publication cheap and deterministic.

---

# 13. Initial Publication Exposure contracts

Target 10 SHALL register the initial owner-qualified PUBLIC Exposure contract families:

```text
publication / public-published-content-representation

publication / public-opportunity-representation

publication / public-opportunity-actionability

publication / public-announcement-representation
```

They are release-affined Exposure Element Contracts under MS-PROT-027 v1.5.

They SHALL expose only the bounded public representation of each object.

They SHALL NOT automatically expose:

```text
revision identifiers
internal provenance
audit evidence
merchant administration metadata
AI inference provenance
internal moderation information
```

unless another explicit contract establishes that element as public.

---

# 14. Opportunity actionability remains distinct from Exposure

An Opportunity may be publicly visible while not currently actionable.

Example:

```text
publishFrom
    1 August

applicationsOpen
    1 September

applicationDeadline
    31 October
```

Then:

```text
August
    EXPOSE
    actionability = NOT_YET_OPEN

September–October
    EXPOSE
    actionability = OPEN

after deadline
    may remain EXPOSE
    actionability = CLOSED_BY_DEADLINE
```

Publication visibility and Opportunity actionability SHALL NOT be collapsed.

---

# 15. Publication public read classification

Initial Publication storefront reads SHALL use:

```text
authoritative Publication query
+
request-scoped derivation
+
current Exposure resolution
```

Classification:

```text
MS-PROT-027 Category B
    synchronous request-scoped projection/composition
```

No:

```text
Publication cache
Publication search index
Redis read model
static-site snapshot
```

is required to close Target 10.

If Search/Discovery or CDN/static generation later introduces an MS-PROT-027 v1.3 trigger, the appropriate Projection Contract becomes mandatory.

---

# 16. Public Enquiry participation must be explicit

The following remains prohibited:

```text
Publication exposed
+
Enquiry capability active
        ↓
automatically assume
Publication → send-enquiry
```

MS-PROT-049 v1.2 explicitly rejects that inference.

Target 10 SHALL instantiate concrete participation rather than leave it to frontend convention.

---

# 17. Initial Publication-to-Enquiry participation

The initial production portfolio SHALL register:

```text
Opportunity
    → enquiry / send-enquiry
```

through an owner-qualified registered subject-interaction participation definition.

This supports the information-publisher case directly:

```text
Scholarship Opportunity O1
        ↓
publicly exposed
        ↓
authoritative Opportunity → Enquiry participation
        ↓
Public Interaction Binding
        ↓
"Ask about this opportunity"
```

No fake Offering is required.

---

# 18. No universal enquiryability of all published content

Target 10 SHALL NOT assume:

```text
every Article
every Announcement
every Publication
    → Enquiry
```

A merchant may still expose a general merchant Enquiry interaction.

Additional subject families can acquire direct `send-enquiry` participation only through an explicit registered owner-qualified relationship.

This preserves the fail-closed rule from MS-PROT-049.

---

# 19. Presentation does not own participation

The website may choose where to present:

```text
Ask about this opportunity
```

but the component itself SHALL NOT create the relationship.

Likewise removing the button from one page does not necessarily mutate the underlying participation authority.

Presentation remains downstream.

---

# 20. Withdrawal invalidates new public bindings

Suppose:

```text
T1 Opportunity O1 PUBLISHED
T2 browser receives O1 → send-enquiry binding
T3 merchant WITHDRAWS O1
T4 browser submits stale binding
```

Backend revalidation SHALL reject the stale subject-specific interaction.

A stale public binding never creates a grandfathered right to initiate new activity.

---

# 21. External application links are not Enquiry execution

An Opportunity may expose:

```text
official application URL
provider URL
source URL
```

Following such a link does not create:

```text
Enquiry
Application
Booking
Order
```

inside Main Street.

It is an audience-facing Publication fact/navigation action unless another accepted capability establishes more semantics.

---

# 22. Social distribution is post-commit integration

Canonical:

```text
Publication commits
    ↓
Publication event
    ↓
configured social-distribution consequence
    ↓
provider fulfilment
```

Provider failure SHALL NOT undo the authoritative Publication.

MS-PROT-048 Provider Readiness applies only to the provider-dependent consequence, not to Main Street Publication truth.

---

# 23. Notification delivery remains separately owned

Subscription matching and notification delivery remain governed by Publication/Subscription and Notification authorities respectively.

Target 10 SHALL NOT require a notification provider to make a Publication public on the merchant's Main Street storefront.

---

# 24. Subscription remains valid accepted semantics

MS-PROT-046's accepted:

```text
SubscriberContext
Subscription
ACTIVE → CANCELLED
new Subscription on resubscribe
```

remain unchanged.

Target 10 does not pre-empt Target 16's provider-delivery design or Target 20's exact subscribe/cancel transport contract.

---

# 25. Hard invariants

1. Publication identity and revision identity remain distinct.
2. Material edits create immutable logical revisions.
3. Stale same-Publication mutations conflict.
4. Logical retries do not multiply Publication effects.
5. `DRAFT | PUBLISHED | WITHDRAWN` remains the shared lifecycle.
6. `ARCHIVED`, `EXPIRED` and `SCHEDULED` are not universal states.
7. Explicit republishing preserves historical publication evidence.
8. AI candidate content is not published authority.
9. Publication state is not Exposure.
10. Exposure timing remains Publication-owned evidence consumed by Exposure resolution.
11. Public observation uses exact registered Exposure Element Contracts.
12. Client time is not publication-time authority.
13. Opportunity actionability remains distinct from public Exposure.
14. Initial Publication public reads are request-scoped.
15. No dedicated Publication cache/index is required initially.
16. Public Enquiry participation is explicit and fail-closed.
17. Initial direct Publication subject participation registers Opportunity → Enquiry.
18. Every Publication type does not automatically become enquire-able.
19. Presentation does not create subject-interaction authority.
20. Provider/social-delivery failure does not roll back Publication.

---

# 26. Deferred questions

| ID | Status | Deferred question | Future owner |
|---|---|---|---|
| `MS-PROT-046-V12-DQ-001` | DEFERRED — INACTIVE | Exact Java representation of Publication identities/revisions | Publication implementation |
| `MS-PROT-046-V12-DQ-002` | DEFERRED — INACTIVE | Exact PostgreSQL layout/indexes | Persistence implementation |
| `MS-PROT-046-V12-DQ-003` | DEFERRED — INACTIVE | Exact transport contracts for create/revise/publish/withdraw/republish | Target 20 |
| `MS-PROT-046-V12-DQ-004` | DEFERRED — INACTIVE | Search/index architecture | When search/discovery is introduced |
| `MS-PROT-046-V12-DQ-005` | DEFERRED — INACTIVE | CDN/static-generation publication-revocation convergence | If introduced |
| `MS-PROT-046-V12-DQ-006` | DEFERRED — INACTIVE | Social-distribution worker/provider implementation | Target 18/provider implementation |
| `MS-PROT-046-V12-DQ-007` | DEFERRED — INACTIVE | Subscription public transport and delivery preference UX | Targets 16/20 |
| `MS-PROT-046-V12-DQ-008` | DEFERRED — INACTIVE | Exact retained historical Publication revision period | Target 17 |

---

# 27. Programme effect

This authority is one half of the accepted Target-10 closure package. Target 10 closes only when both MS-PROT-046 v1.2 and MS-PROT-043 v1.4 are accepted and formalised.

It does not authorise production implementation, API/UI work, migrations, provider execution or prototype work.

---

# 28. Governing principle

> **Publication owns durable information truth, revision history, publication lifecycle and publication-owned exposure-window evidence. Public observation remains governed by Exposure, public subject interaction remains explicitly owned and projected, and provider/social/notification consequences remain post-commit integrations rather than Publication authority.**
