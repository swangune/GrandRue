# MS-PROT-027 v1.2 — Exposure Policy & Audience Observation Amendment

**Document ID:** MS-PROT-027  
**Version:** 1.2  
**Status:** **ACCEPTED after graph-aware falsification and manual approval**  
**Amends:** MS-PROT-027 v1.1  
**Closes:** DDR-OD-008 — Exposure policy vocabulary and rule boundary  
**Depends on:** MS-PROT-027 v1.1, MS-PROT-049, MS-PROT-053 and applicable capability/configuration authorities  
**Purpose:** Define the minimum reusable exposure-policy semantics by which Main Street determines whether a particular audience may observe a particular semantic element in context, while preserving separation from lifecycle, authorisation, notification, surface composition, discoverability, entitlement and projection formatting.

---

# 1. Governing decision

Exposure is a deterministic audience-observation decision.

> **Exposure answers whether a particular exposable semantic element may be observed by a particular audience in a particular authoritative context.**

The minimum exposure decision vocabulary is:

```text
EXPOSE
WITHHOLD
```

Exposure is not a domain object lifecycle, operation authorisation, notification rule, commercial entitlement, discovery/indexing policy, surface-composition rule or presentation transformation.

---

# 2. Exposure is not a visibility status on Operational Objects

Main Street shall not require domain objects to carry universal states such as:

```text
PUBLIC
PRIVATE
CUSTOMER
MERCHANT
STAFF_ONLY
LINK_ONLY
HIDDEN
```

as authoritative business state.

A single object may expose different semantic elements to different audiences and contexts.

Example:

```text
Booking B1

related customer:
    service          EXPOSE
    time             EXPOSE
    internal notes   WITHHOLD

merchant owner:
    service          EXPOSE
    time             EXPOSE
    internal notes   EXPOSE where otherwise permitted

public:
    all customer-specific booking facts WITHHOLD
```

Therefore exposure is evaluated against an exposable semantic element and audience context rather than one coarse object-level visibility status.

---

# 3. Exposure decision inputs

Conceptually:

```text
ExposureDecision = evaluate(
    exposable semantic element,
    authoritative subject/context,
    audience/actor context,
    merchant scope,
    relationship/context evidence,
    governing restrictions,
    capability-owned exposure constraints,
    merchant-configured exposure policy
)
```

The exact implementation representation remains implementation-deferred provided the observable semantics conform to this contract.

---

# 4. Governing precedence

Merchant exposure configuration operates only inside Main Street-governed bounds.

Effective exposure must respect, as applicable:

```text
authoritative capability/lifecycle eligibility
        ↓
data-protection / sensitivity / purpose restrictions
        ↓
merchant scope / tenant isolation
        ↓
relationship and contextual authority
        ↓
capability-owned exposure constraints
        ↓
merchant-configured exposure choices
        ↓
deterministic exposure decision
```

Merchant configuration shall not bypass platform, privacy, lifecycle, tenant, capability or legally required restrictions.

---

# 5. Exposure and projection remain distinct

Projection determines the audience-safe representation of authoritative facts.

Exposure determines whether the resulting semantic element may be observed by the audience in context.

Therefore:

```text
source fact
    ↓
projection transformation
    ↓
audience-facing element
    ↓
EXPOSE / WITHHOLD
```

A protected source fact may legitimately participate in deriving an audience-safe projection without itself becoming exposed.

Example:

```text
exact available stock = 2
        ↓
derive
"Low stock"

public exact stock       WITHHOLD
public "Low stock"      EXPOSE
```

Permission to use a fact in projection derivation does not imply permission to reveal the source fact.

---

# 6. Redaction and masking are not exposure verdicts

Main Street shall not expand the core exposure decision to include generic outcomes such as:

```text
MASK
REDACT
SUMMARISE
```

Masking, redaction, anonymisation, summarisation and audience-friendly wording are projection/data-handling transformations governed by their appropriate authorities.

The transformed element is then independently evaluated for exposure.

Example:

```text
payment/provider data
    ↓
audience-safe payment projection
"Card ending 4242"
    ↓
EXPOSE
```

while the protected source representation remains withheld.

---

# 7. Surface membership is not exposure

Exposure shall not create page, workspace, route, navigation or storefront membership.

Surface contribution and projection composition first determine which semantic elements are candidates for a surface.

Exposure then filters those candidates for the current audience/context.

Conceptually:

```text
Capability surface contribution
        +
Projection composition
        ↓
Candidate semantic elements
        ↓
Exposure evaluation
        ↓
Audience-visible representation
```

An internally existing Product, Booking or Publication shall not become publicly surfaced merely because some of its fields have permissive exposure configuration.

---

# 8. Authorisation is not exposure

Exposure answers:

```text
May this actor/audience observe this element?
```

Authorisation answers:

```text
May this actor perform this operation?
```

The same actor/context evidence may participate in both decisions, but one shall not substitute for the other.

Frontend visibility never grants write authority.

---

# 9. Notification is not exposure

Notification governs proactive communication/delivery.

Exposure governs audience observation through a projection/surface.

A fact may be exposed without producing a notification, and a notification may be generated without making the underlying object generally queryable or public.

---

# 10. Discoverability and indexing are not exposure

Search-engine indexing, public discovery, recommendations and search inclusion are separate policies from audience observation.

A page may be legitimately viewable by a public audience while configured not to be indexed or surfaced through discovery mechanisms.

Main Street shall not create combined states such as:

```text
PUBLIC_NO_INDEX
```

inside exposure semantics.

---

# 11. Secure links and authentication are context evidence

Secure contextual links, authenticated sessions and CustomerAccounts may establish evidence relevant to exposure evaluation.

They are not exposure states.

Example:

```text
request
    +
valid secure Booking context
        ↓
RELATED CUSTOMER CONTEXT established
        ↓
Booking.date        EXPOSE
Booking.internal    WITHHOLD
```

A generic authenticated-user condition is insufficient for relationship-bound customer data where stronger subject relationship is required.

---

# 12. Merchant-configurable exposure

Where governing capability semantics permit merchant choice, the merchant may configure exposure using registered declarative options.

Examples may include:

```text
show/hide public exact address
show/hide Offering price
show/hide exact stock quantity
show customer appointment history
show public availability summary
```

Main Street shall not prescribe one commercial visibility policy where merchant business practice legitimately varies.

Capabilities may provide registered exposure groups/defaults to avoid field-by-field configuration explosion.

Such groups/defaults remain declarative definitions and do not become business objects or unrestricted rule systems.

---

# 13. Bounded predicates, not a general rules DSL

Exposure policies may use only registered bounded predicates/context dimensions supported by Main Street.

Examples may include:

```text
PUBLIC audience
MERCHANT audience
RELATED_CUSTOMER context
AUTHORISED_STAFF context
merchant scope
secure contextual authority
capability-defined relationship predicates
```

Main Street shall not expose a generic merchant-authored boolean expression language such as:

```text
IF actor.role == X
AND customer.city != Y
AND booking.state == Z
THEN expose field
```

unless those predicates are individually registered and the composition itself is part of accepted deterministic semantics.

Unsupported audience segmentation remains unsupported rather than becoming AI/merchant-authored executable logic.

---

# 14. AI boundary

AI specialists may:

- interpret merchant exposure intent;
- infer candidate mappings to registered exposure semantics;
- explain consequences;
- identify ambiguity;
- propose supported exposure configuration.

AI specialists shall not:

- create exposure predicates;
- invent semantic relationships;
- bypass privacy/capability restrictions;
- directly alter authoritative exposure configuration without the applicable approval/validation flow; or
- become runtime exposure authority.

Specialists infer; deterministic Main Street semantics decide.

---

# 15. Subscription entitlement remains separate

Commercial plan entitlement shall not be encoded into exposure semantics.

The appropriate ordering is:

```text
merchant configuration
    +
commercial entitlement/residual-right rules
        ↓
surface/function eligibility
        ↓
projection candidate
        ↓
exposure evaluation
```

Plan names such as Free, Business or Growth shall not become exposure predicates.

Residual customer/merchant access required to fulfil or manage existing commitments remains governed by the accepted entitlement-continuity authorities before exposure is evaluated.

---

# 16. Lifecycle and publication timing remain separate

Exposure does not make an otherwise ineligible object operationally publishable.

Examples:

```text
DRAFT Publication
DISCONTINUED Product
withdrawn Listing
future embargoed Opportunity
```

remain subject to their capability-owned lifecycle/effective-date rules.

Exposure applies only after the applicable capability determines what projection candidates are legitimate in the current context.

---

# 17. Data-protection boundary

MS-PROT-053 remains authoritative for protection classification, purpose, retention, erasure, anonymisation and redaction constraints.

A handling classification such as `STANDARD` does not automatically mean `PUBLIC`.

Likewise merchant exposure preference cannot make `PERSONAL`, `RESTRICTED` or `SECRET` information publicly observable where governing restrictions prohibit it.

Data-protection authority constrains exposure without becoming the owner of the underlying business semantics.

---

# 18. Falsification record

The model was tested against:

- public merchant content;
- public object with private/internal fields;
- customer-specific Booking/Appointment projections;
- restricted merchant staff actors;
- secure contextual guest links;
- authenticated customers;
- embargoed/future publications;
- merchant operational-location privacy;
- field masking/redaction;
- derived low-stock/availability projections;
- public visibility versus search indexing;
- subscription downgrade and residual commitments;
- customer portal exposure choices;
- discontinued capability-owned objects; and
- AI-assisted exposure configuration.

The following candidate abstractions were rejected:

```text
universal VisibilityStatus on domain objects
Exposure state machine
PUBLIC / PRIVATE / CUSTOMER / STAFF / HIDDEN object state
MASK as a core ExposureDecision
LINK_ONLY exposure state
PUBLIC_NO_INDEX exposure state
subscription-plan conditions inside exposure
AUTHENTICATED as sufficient customer visibility evidence
merchant-authored general boolean rules DSL
Exposure owning storefront/surface composition
```

No material contradiction remained after separation of neighbouring concerns.

---

# 19. Validation matrix

| Constraint | Result |
|---|---|
| Exposure remains distinct from lifecycle | PASS |
| Exposure remains distinct from authorisation | PASS |
| Exposure remains distinct from notification | PASS |
| Exposure remains distinct from surface membership | PASS |
| Exposure remains distinct from discoverability/indexing | PASS |
| Exposure remains distinct from commercial entitlement | PASS |
| Field/fact-level audience differences supported | PASS |
| Projection redaction/masking remains separate | PASS |
| Data-protection restrictions constrain exposure | PASS |
| Merchant configurability preserved within registered bounds | PASS |
| Secure links/authentication treated as context evidence | PASS |
| Derived projections do not leak protected source facts | PASS |
| No arbitrary exposure rules DSL introduced | PASS |
| AI inference does not create semantics or authority | PASS |
| Composite architecture/programming model preserved | PASS |

---

# 20. Accepted result

DDR-OD-008 is closed by the following semantic contract:

```text
EXPOSABLE SEMANTIC ELEMENT
        +
AUDIENCE / CONTEXT
        +
GOVERNING RESTRICTIONS
        +
REGISTERED MERCHANT POLICY
        ↓
DETERMINISTIC EXPOSURE DECISION
        ↓
EXPOSE | WITHHOLD
```

> **Main Street shall keep exposure deliberately small: it decides whether an audience may observe an already-legitimate semantic element in context. Projection determines representation; authorisation determines operations; surface composition determines placement; discovery determines findability; entitlement determines commercial availability; and data-protection authority constrains what may be handled or revealed at all.**
