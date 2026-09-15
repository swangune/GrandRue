# MS-PROT-082 v1.1 — Initial Commercial Jurisdiction, Regulatory Purpose & Filing Scope Portfolio Amendment

**Status:** ACCEPTED by manual approval on 14 September 2026  
**Parent authority:** MS-PROT-082 v1.0 — Jurisdiction, Regulatory Knowledge & Regulatory Administration Model  
**Execution envelope:** JRA-GRP-SCOPE-01 — Initial Jurisdiction & Regulatory Purpose Portfolio  
**Deferred decisions addressed:**  
- MS-PROT-082-DQ-001 — Initial commercial jurisdiction portfolio
- MS-PROT-082-DQ-002 — Initial Regulatory Purpose / FilingScopeReference subtype portfolio

**Implementation activation:** NONE  
**Authority type:** Additive semantic amendment  
**Supersedes:** Previous unapproved MS-PROT-082 v1.1 proposal presented in ChatGPT  
**Does not supersede:** MS-PROT-082 v1.0

---

# 1. Executive Decision

This amendment proposes the first bounded semantic vocabulary with which Main Street may represent jurisdiction-scoped regulatory administration.

It proposes:

## 1.1 Initial CommercialJurisdiction portfolio

Exactly:

- **England**
- **Wales**

These are independently addressable Main Street `CommercialJurisdiction` scope identities.

They are semantic regulatory-applicability scopes within Main Street. Their existence SHALL NOT be interpreted as a constitutional, sovereignty or universal legal-jurisdiction classification.

## 1.2 Initial RegulatoryPurpose portfolio

Exactly:

1. **Commercial Transaction Regulatory Administration**
2. **Workforce Regulatory Administration**
3. **Business Authorisation Regulatory Administration**

These classify **why** regulatory administration is being performed.

They do not define the actual law, obligation, programme, filing, regulator, deadline, liability, provider or compliance outcome.

## 1.3 Initial FilingScopeReference subtype portfolio

Exactly:

1. **Merchant Filing Scope Reference**
2. **Operating Location Filing Scope Reference**
3. **Workforce Filing Scope Reference**
4. **Commercial Activity Filing Scope Reference**

These classify **which source-owned business scope the regulatory administration concerns**.

They do not transfer ownership of the referenced business facts to Regulatory Administration.

## 1.4 Core safety rule

Acceptance of any vocabulary item above creates **zero regulatory-support claim by itself**.

The following SHALL remain distinct:

```text
available semantic vocabulary
        ≠
configured jurisdiction pack
        ≠
applicable regulatory obligation
        ≠
provider-backed support
        ≠
production regulatory support
        ≠
merchant compliance
```

This amendment therefore establishes vocabulary and scope boundaries without activating regulatory operation.

---

# 2. Authority Basis

This proposal derives from, and SHALL remain subordinate to:

- `MS-FUNDAMENTAL-VISION-001`;
- `designs/DESIGN-RULES.md`;
- accepted MS-PROT-082 v1.0;
- `designs/DEFERRED-DECISION-REGISTER.md`;
- `designs/AUTHORITY-INDEX.md`;
- `designs/CANONICAL-SEMANTIC-LEXICON.md`;
- the accepted Lossless Deferred-Design Grouping Overlay in `SEQUENCE.md`;
- applicable accepted capability authorities referenced by MS-PROT-082.

MS-PROT-082 v1.0 already owns the generic regulatory substrate, including accepted jurisdiction policy, `CommercialJurisdiction`, `RegulatoryPurpose`, source-backed regulatory-rule provenance, conservative effective-rule selection, regulatory programmes and obligations, filing evidence, regulatory effects, support dimensions, support manifest/history, support eligibility and regulatory exception/attention semantics.

This amendment SHALL NOT redesign those accepted concepts.

Its authority is narrower:

```text
MS-PROT-082 v1.0
    defines the regulatory semantic machinery

MS-PROT-082 v1.1
    selects the first bounded vocabulary
    for two deliberately deferred parts of that machinery
```

---

# 3. Deferred-Decision Fidelity

## 3.1 MS-PROT-082-DQ-001

The material question is:

> What is the initial concrete `CommercialJurisdiction` set that Main Street is authorised to represent before a jurisdiction-specific regulated capability is publicly launched?

The existing deferred status means the abstraction exists but the initial concrete portfolio was deliberately absent.

This amendment answers that question only.

## 3.2 MS-PROT-082-DQ-002

The material question contains **two coupled vocabulary decisions**:

1. the initial concrete `RegulatoryPurpose` portfolio; and
2. the initial target-specific `FilingScopeReference` subtype vocabulary.

Neither part may be silently omitted.

This amendment therefore treats DQ-002 as unresolved unless **both** vocabularies receive a complete normative answer.

## 3.3 DQ-003 through DQ-007 remain outside this amendment

This amendment SHALL NOT answer:

- **MS-PROT-082-DQ-003** — exact Java/persistence representation for programmes, rules, deadlines, obligations and filing evidence;
- **MS-PROT-082-DQ-004** — initial regulatory provider portfolio;
- **MS-PROT-082-DQ-005** — regulatory-source change-monitoring mechanism and review frequency;
- **MS-PROT-082-DQ-006** — professional escalation integration;
- **MS-PROT-082-DQ-007** — complete Financial Health / Financial Intelligence operating model.

Any design consequence that attempts to decide those matters during formalisation SHALL be rejected as post-approval semantic expansion.

---

# 4. Why DQ-001 and DQ-002 May Share One Lifecycle

`JRA-GRP-SCOPE-01` is a **lossless execution envelope**, not a merged decision.

The grouping is legitimate because the two questions share a material safety problem:

> Main Street must not recognise a geography in a way that accidentally implies unspecified regulatory coverage, and it must not recognise a regulatory purpose without a bounded jurisdictional context in which that purpose could later be used.

Their shared reasoning includes:

- regulatory-support claim honesty;
- jurisdiction applicability;
- purpose qualification;
- unsupported-scope behaviour;
- business-type neutrality;
- source ownership;
- provider independence;
- AI authority boundaries;
- merchant-visible Administrative Compression.

The grouping remains lossless because:

- DQ-001 retains its identifier;
- DQ-002 retains its identifier;
- each receives an independent recommendation;
- either could be revised or rejected without automatically changing the other;
- approval must contain the complete result for each;
- DDR resolution occurs independently for each DQ;
- no group identifier acquires semantic authority.

If later evidence invalidates the shared assumptions, the group SHALL split rather than force a common outcome.

---

# 5. Problem Statement

MS-PROT-082 v1.0 deliberately provides generic regulatory infrastructure without claiming that Main Street already supports a particular jurisdiction or a particular regulatory programme.

That restraint is necessary, but the abstraction eventually requires bounded concrete vocabulary.

Without DQ-001 resolution, Main Street cannot truthfully say which jurisdictional scopes its regulatory substrate is capable of representing.

Without DQ-002 resolution, Main Street cannot consistently express:

- why a regulatory programme or obligation exists; or
- which represented business scope a filing/obligation concerns.

Selecting these vocabularies too broadly would create a compliance-platform abstraction larger than Main Street needs.

Selecting them too narrowly or business-specifically would create special cases when materially different local businesses are introduced.

The design problem is therefore:

> Establish the minimum sufficiently expressive initial jurisdiction, purpose and filing-scope vocabulary that can support materially different Main Street businesses without implying regulatory coverage that has not actually been designed and accepted.

---

# 6. Design Forces

The proposal must simultaneously satisfy the following forces.

## 6.1 Expressiveness

It must represent enough difference to avoid immediate business-specific exceptions.

## 6.2 Boundedness

It must not become a general ontology of world law, taxation, employment regulation or licensing.

## 6.3 Claim honesty

Semantic vocabulary must not masquerade as operational or legal support.

## 6.4 Business-type neutrality

A salon, shop, restaurant, motel, tradesperson or professional service should use the same generic regulatory concepts where materially equivalent.

## 6.5 Ownership preservation

Regulatory Administration may reference facts owned elsewhere but may not duplicate ownership of those facts.

## 6.6 Progressive activation

No merchant should configure regulatory concepts that are irrelevant to the merchant's current operation.

## 6.7 Administrative Compression

The internal precision needed for regulatory correctness must not become a software-administration task for the merchant.

## 6.8 Deterministic authority

AI, provider output and document extraction may assist evidence acquisition but may not create regulatory truth.

## 6.9 Provider replaceability

No provider-specific jurisdiction or filing vocabulary may become Main Street semantic authority.

## 6.10 Deferred-decision integrity

The amendment must not use DQ-001/DQ-002 as a vehicle for prematurely solving DQ-003 through DQ-007.

---

# 7. Feature Admission

# 7.1 Group-level admission

**Outcome: ADMITTED**

The combined design envelope satisfies the admission gate because it directly improves:

- **Business Representation** — Main Street can represent jurisdiction-qualified regulatory concerns;
- **Coordination** — regulatory semantics can reference source-owned commerce, workforce, location and merchant scopes without merging ownership;
- **Administrative Compression** — regulatory complexity can remain internal while merchants answer ordinary business questions.

It also satisfies the remaining Fundamental Vision checks:

- role simplicity;
- target-market proportionality;
- ownership necessity.

---

# 7.2 MS-PROT-082-DQ-001 Feature Admission

**Outcome: ADMITTED**

### Representation

PASS.

A business may operate in a regulatory context materially affected by geography. An explicit jurisdiction scope is therefore a genuine business-representation need.

### Administrative Compression

PASS.

A bounded jurisdiction portfolio lets Main Street infer and coordinate regulatory context without asking merchants to understand internal jurisdiction architecture.

### Coordination

PASS.

Jurisdiction scope coordinates facts originating in merchant identity, premises/location, commerce, workforce and other capabilities without taking ownership of them.

### Role Simplicity

PASS.

Merchants should normally state ordinary facts such as where they operate. They should not configure a legal ontology.

### Target-market Proportionality

PASS.

England and Wales are sufficiently bounded for an initial UK-weighted Main Street scope while avoiding speculative global coverage.

### Ownership Necessity

PASS.

The generic notion of a regulatory applicability jurisdiction belongs to the Regulatory Administration authority because it is used to qualify regulatory knowledge and support.

---

# 7.3 MS-PROT-082-DQ-002 Feature Admission

**Outcome: ADMITTED**

### Representation

PASS.

A regulatory concern must distinguish why administration exists and what business scope it concerns.

### Administrative Compression

PASS.

Generic purpose and filing-scope semantics allow Main Street to derive internal configuration from business context instead of exposing regulator-specific setup machinery.

### Coordination

PASS.

`FilingScopeReference` provides a controlled bridge to source-owned business scope without copying business truth into Regulatory Administration.

### Role Simplicity

PASS.

No merchant-facing taxonomy-management requirement is introduced.

### Target-market Proportionality

PASS.

Three generic purposes and four generic filing-scope-reference subtypes are materially smaller than a universal legal taxonomy.

### Ownership Necessity

PASS.

The regulatory capability must own the meaning of its purpose and filing-scope reference while the referenced business facts remain owner-controlled elsewhere.

---

# 8. Rejected Admission Alternatives

The following alternatives were considered and rejected.

## 8.1 `United Kingdom` as the only initial CommercialJurisdiction

**REJECTED.**

An umbrella value would be too coarse to preserve material geographic divergence when later accepted regulatory authority differs beneath that umbrella.

It would also tempt public wording such as “UK regulatory support” before exact territorial support existed.

## 8.2 Global `Other` jurisdiction

**REJECTED.**

`Other` would convert unsupported geography into apparently supported semantic state.

Unsupported jurisdiction must remain explicitly unsupported rather than hidden behind a generic fallback.

## 8.3 Entire global jurisdiction catalogue

**REJECTED.**

That would create speculative breadth with no current business need and would move Main Street toward a general compliance platform.

## 8.4 Generic `Compliance` RegulatoryPurpose

**REJECTED.**

It conveys virtually no semantic information and could collapse materially different administrative reasons into one universal bucket.

## 8.5 Accommodation/guest reporting as an initial RegulatoryPurpose

**REJECTED AFTER FALSIFICATION.**

Although relevant to some motels/accommodation businesses, it is not sufficiently generic to justify inclusion in the initial cross-business portfolio.

If a future concrete jurisdiction pack requires a materially distinct accommodation-specific regulatory purpose, it must pass fresh Feature Admission.

## 8.6 Provider-derived purpose categories

**REJECTED.**

Provider taxonomy is replaceable implementation/external representation, not Main Street semantic authority.

## 8.7 Java enum or persistence codes

**REJECTED FROM THIS AUTHORITY.**

Exact physical representation belongs to DQ-003.

---

# 9. Canonical Semantic Separation

The following concepts SHALL remain distinct.

```text
CommercialJurisdiction
    answers:
    "Within which accepted geographic regulatory-applicability
     scope is the regulatory question being evaluated?"

RegulatoryPurpose
    answers:
    "Why is regulatory administration being performed?"

FilingScopeReference
    answers:
    "Which source-owned represented business scope
     does this regulatory administration concern?"

RegulatoryAdministrationProgramme / RegulatoryObligation
    answers:
    "What accepted regulatory administration or obligation exists?"

source-backed regulatory authority
    answers:
    "On what accepted authority is that regulatory meaning based?"

support policy / manifest / history
    answers:
    "What does Main Street currently claim and operationally support?"
```

No one concept SHALL substitute for another.

---

# 10. Vocabulary Admission Is Not Support Activation

The mere existence of a value in an accepted vocabulary SHALL mean only:

> Main Street's semantic model is authorised to represent this category when some separately accepted regulatory authority legitimately requires it.

It SHALL NOT mean:

- a jurisdiction pack exists;
- a rule exists;
- a regulatory programme exists;
- an obligation applies;
- a provider is configured;
- an external source is being monitored;
- Main Street offers regulatory support publicly;
- a merchant is compliant.

Formally:

```text
VocabularyMembership(x)
    ↛ ProductionSupport(x)
```

and:

```text
CommercialJurisdictionPortfolio
    × RegulatoryPurposePortfolio
    × FilingScopeReferenceSubtypePortfolio
```

SHALL NOT be interpreted as an automatically supported Cartesian product.

Only explicitly accepted bindings may become meaningful regulatory configurations.

---

# 11. DQ-001 — Initial CommercialJurisdiction Portfolio

The initial `CommercialJurisdiction` portfolio SHALL contain exactly:

- **England**
- **Wales**

No third concrete jurisdiction is admitted by this amendment.

---

# 12. Meaning of England and Wales

Within Main Street, **England** and **Wales** are independently addressable `CommercialJurisdiction` scope identities.

This is a product-semantic regulatory-applicability distinction.

It SHALL NOT assert:

- that each is a sovereign state;
- that they have completely separate bodies of law;
- that every rule differs;
- that every rule is shared;
- that Main Street has independently established the constitutional status of either;
- that either is supported for every RegulatoryPurpose.

The separation exists so accepted regulatory authority can:

- converge where the same treatment applies; and
- diverge where materially different treatment applies,

without changing the identity of the underlying commercial-jurisdiction scopes.

---

# 13. Why Both England and Wales Are Included

A one-jurisdiction portfolio would be smaller, but it would not exercise the coordination problem that caused a generic `CommercialJurisdiction` abstraction to be necessary.

Including England and Wales provides the smallest proposed portfolio that can represent:

- one-jurisdiction operation;
- multi-jurisdiction operation;
- shared treatment across scopes;
- divergent treatment across scopes;
- migration between scopes;
- jurisdiction-qualified support claims.

This is sufficiently expressive without admitting Scotland, Northern Ireland or wider global scope speculatively.

Acceptance of both values SHALL NOT require Main Street to launch regulatory functionality publicly in both.

---

# 14. Unsupported Jurisdictions

Every commercial jurisdiction not explicitly admitted remains unsupported by this portfolio.

This includes, without claiming an exhaustive world list:

- Scotland;
- Northern Ireland;
- Crown Dependencies;
- Republic of Ireland;
- United States jurisdictions;
- all other unadmitted jurisdictions.

An unsupported jurisdiction SHALL NOT be:

- silently converted to England;
- silently converted to Wales;
- represented as an `Other` supported jurisdiction;
- mapped to a “closest” jurisdiction;
- treated as supported because a provider returns data for it;
- treated as supported because an AI model believes its rules are similar;
- treated as supported because some rule text happens to match.

The safe state is explicit unsupportedness.

---

# 15. Unknown Jurisdiction Is Not Unsupported Jurisdiction

The model SHALL preserve the distinction between:

```text
known jurisdiction outside accepted portfolio
```

and:

```text
insufficient evidence to determine applicable jurisdiction
```

For example:

- a merchant conclusively operating only in Scotland is outside the admitted portfolio;
- a merchant whose operational geography cannot yet be established has unresolved applicability.

Main Street SHALL NOT convert uncertainty into a jurisdiction default.

---

# 16. Jurisdiction Applicability

A postal, billing or registered address SHALL NOT universally determine regulatory applicability.

Jurisdiction applicability may require facts owned by different capabilities, including where relevant and accepted:

- merchant/business identity facts;
- operating location;
- premises location;
- location where regulated activity occurs;
- workforce location;
- commercial-activity context;
- fulfilment context;
- other explicitly admitted source-owned facts.

The exact applicability rule SHALL be defined only by the applicable accepted regulatory authority.

MS-PROT-082 MAY evaluate those facts for regulatory meaning.

It SHALL NOT take source ownership of them.

---

# 17. Multi-Jurisdiction Operation

A Main Street merchant may legitimately have:

- zero currently determinable applicable admitted jurisdictions;
- one applicable admitted jurisdiction;
- multiple applicable admitted jurisdictions.

The model SHALL NOT encode:

```text
Merchant
    → exactly one CommercialJurisdiction
```

as a universal invariant.

Jurisdiction may be relevant to:

- the whole merchant;
- a location;
- a workforce context;
- a commercial activity;
- another accepted filing scope.

---

# 18. Temporal Jurisdiction Integrity

A merchant's present address, premises or operating footprint SHALL NOT silently rewrite the jurisdictional context of historical regulatory evidence or previously established regulatory effects.

If jurisdiction applicability changes over time, accepted historical regulatory records SHALL remain interpretable in the context under which they were established.

This amendment defines that semantic invariant only.

It does not decide the exact persistence mechanism, which remains within DQ-003 and applicable accepted historical-record authority.

---

# 19. DQ-002A — Initial RegulatoryPurpose Portfolio

The initial `RegulatoryPurpose` portfolio SHALL contain exactly:

1. **Commercial Transaction Regulatory Administration**
2. **Workforce Regulatory Administration**
3. **Business Authorisation Regulatory Administration**

No generic `Other`, `Compliance`, `Miscellaneous` or provider-defined purpose is admitted.

---

# 20. RegulatoryPurpose General Semantics

A `RegulatoryPurpose` classifies the material **reason for regulatory administration**.

It SHALL NOT independently establish:

- the existence of law;
- merchant applicability;
- an obligation;
- a programme;
- a filing;
- a deadline;
- a liability;
- a government authority;
- a reporting destination;
- a provider;
- evidence sufficiency;
- merchant compliance.

A purpose is therefore descriptive semantic qualification, not regulatory authority by itself.

---

# 21. Commercial Transaction Regulatory Administration

**Definition**

Regulatory administration whose material reason arises from represented commercial transactions or commercial activity conducted by the merchant.

It is intended to support source-backed regulatory administration that is materially connected to commerce without creating a tax or accounting subsystem inside MS-PROT-082.

It MAY qualify an accepted regulatory programme where commercial-transaction facts are relevant.

It SHALL NOT by itself determine:

- whether a transaction is taxable;
- tax rate;
- tax liability;
- revenue recognition;
- accounting treatment;
- invoice treatment;
- payment-provider treatment;
- filing requirement;
- filing interval;
- regulatory destination.

Commerce truth remains source-owned.

Financial/accounting semantics remain with their applicable owners.

---

# 22. Workforce Regulatory Administration

**Definition**

Regulatory administration whose material reason arises from represented workforce operation.

It MAY qualify accepted regulatory programmes where workforce facts are relevant.

It SHALL NOT transfer ownership of:

- worker identity;
- employment relationship;
- shifts;
- attendance;
- leave;
- compensation;
- working-time facts;
- workforce scheduling;
- workforce evidence.

Those remain owned by applicable Workforce authorities.

MS-PROT-082 owns only the regulatory interpretation established by accepted regulatory authority.

---

# 23. Business Authorisation Regulatory Administration

**Definition**

Regulatory administration whose material reason concerns a licence, registration, permit, approval or materially equivalent regulatory authorisation associated with conducting an admitted business activity.

The purpose SHALL NOT by itself determine:

- whether authorisation is legally required;
- the issuing body;
- merchant eligibility;
- required evidence;
- fees;
- conditions;
- validity period;
- renewal timing;
- enforcement consequence.

Those require separately accepted regulatory authority.

---

# 24. Purpose Non-Exclusivity

The portfolio SHALL NOT imply that the three purposes are mutually exclusive in the real business world.

A material regulatory matter may intersect more than one concern.

This amendment does not alter any cardinality already established by MS-PROT-082 v1.0.

Where a concrete accepted programme or obligation requires purpose association, only admitted purposes may be used, and the exact accepted regulatory authority determines the applicable association.

No “pick the nearest purpose” rule is permitted.

---

# 25. Future Purpose Expansion

A future regulatory concern SHALL NOT be squeezed into an existing purpose merely to avoid expanding the portfolio.

A new purpose is admissible only where:

1. an actual intended regulatory programme exposes a material semantic difference;
2. the existing portfolio cannot represent that difference without distortion;
3. the new purpose passes Feature Admission;
4. ownership boundaries remain intact;
5. the expansion does not create business-type-specific architecture without sufficient cause.

Example:

A future accommodation-specific programme MAY demonstrate that accommodation/guest administration is materially distinct from the initial three purposes.

If so, it requires fresh accepted authority.

It is not pre-authorised here.

---

# 26. DQ-002B — Initial FilingScopeReference Subtype Portfolio

The initial target-specific `FilingScopeReference` subtype vocabulary SHALL contain exactly:

1. **Merchant Filing Scope Reference**
2. **Operating Location Filing Scope Reference**
3. **Workforce Filing Scope Reference**
4. **Commercial Activity Filing Scope Reference**

These are semantic subtype categories.

Their exact Java classes, records, persistence representation, identifiers or serialization forms are NOT defined by this amendment.

---

# 27. FilingScopeReference General Semantics

A `FilingScopeReference` identifies the source-owned represented business scope to which a regulatory filing, obligation or applicable regulatory administration refers.

It SHALL be a reference boundary, not a duplicated domain aggregate.

Its semantics are:

```text
Regulatory authority
      │
      └── refers to
            source-owned business scope
```

not:

```text
Regulatory authority
      │
      └── becomes owner of
            copied business state
```

---

# 28. Merchant Filing Scope Reference

**Meaning**

References the merchant-level business scope where regulatory administration applies to the merchant as a represented operating entity rather than to one narrower source-owned scope.

It SHALL NOT copy or redefine merchant identity.

It SHALL NOT imply that every obligation is merchant-wide.

---

# 29. Operating Location Filing Scope Reference

**Meaning**

References a source-owned represented operating location or premises where that location is materially relevant to accepted regulatory administration.

It SHALL NOT create an independent Regulatory Administration-owned premises model.

Examples of potentially relevant source facts MAY include a represented shop, salon premises or other operating location, but the subtype itself remains business-type neutral.

---

# 30. Workforce Filing Scope Reference

**Meaning**

References the applicable source-owned workforce scope where accepted regulatory administration is materially concerned with workforce activity.

It SHALL NOT duplicate:

- staff records;
- worker records;
- timekeeping;
- rota;
- leave;
- compensation;
- workforce eligibility state.

Those remain source-owned.

---

# 31. Commercial Activity Filing Scope Reference

**Meaning**

References a bounded source-owned commercial-activity scope where regulatory administration materially concerns represented commercial activity rather than the entire merchant or a physical location.

It SHALL NOT itself own:

- orders;
- bookings;
- appointments;
- sales;
- payments;
- invoices;
- transaction amounts;
- customer commitments.

Those remain owned by the applicable operational capabilities.

The reference supplies regulatory context only.

---

# 32. Filing Scope Is Not Purpose

The following distinction is authoritative:

```text
RegulatoryPurpose
    = why regulatory administration exists

FilingScopeReference
    = what represented business scope it concerns
```

Example:

```text
Purpose:
    Workforce Regulatory Administration

Scope:
    Workforce Filing Scope Reference
```

is semantically possible, but this amendment does not declare any concrete obligation from that combination.

Similarly:

```text
Purpose:
    Business Authorisation Regulatory Administration

Scope:
    Operating Location Filing Scope Reference
```

does not mean every operating location requires a licence.

Concrete applicability remains source-backed and authority-qualified.

---

# 33. No Automatic Cross-Product

The model SHALL NOT infer regulatory support from every combination of:

- accepted jurisdiction;
- accepted purpose;
- accepted filing-scope subtype.

Conceptually:

```text
PotentialVocabularySpace
    =
    Jurisdiction
    × Purpose
    × FilingScopeSubtype
```

but:

```text
AcceptedRegulatoryBindings
    ⊂
    PotentialVocabularySpace
```

and only separately accepted regulatory authority may establish membership in `AcceptedRegulatoryBindings`.

The earlier proposal's full England/Wales × purpose matrix is therefore explicitly withdrawn.

---

# 34. Jurisdiction-Pack Boundary

A jurisdiction pack MAY later select and bind relevant accepted vocabulary.

Such a pack SHALL specify only combinations actually supported by accepted authority.

A pack SHALL NOT inherit support merely because a vocabulary item exists.

For example:

```text
England
```

being admitted does not imply that all three Regulatory Purposes are operationally supported for England.

Likewise:

```text
Workforce Regulatory Administration
```

being admitted does not imply any workforce-regulatory programme exists in Wales.

---

# 35. Public-Support Claim Boundary

Main Street SHALL NOT derive a public support statement directly from DQ-001 or DQ-002 resolution.

Prohibited inference:

```text
England is in CommercialJurisdictionPortfolio
        therefore
"Main Street handles regulatory compliance in England"
```

Prohibited inference:

```text
Workforce Regulatory Administration is an admitted purpose
        therefore
"Main Street handles employment compliance"
```

Any public support declaration must remain governed by MS-PROT-082 v1.0's accepted support-policy, support-manifest, support-history and operational-support semantics plus any still-required deferred decisions.

---

# 36. Regulatory Support Is Purpose-Qualified and Authority-Qualified

Where Main Street eventually makes a support claim, it must be bounded sufficiently to distinguish:

- jurisdiction;
- applicable regulatory concern;
- accepted programme/rule authority;
- support state;
- material limitations where required.

This amendment does not invent a new support-state ladder.

It preserves the support dimensions already established by MS-PROT-082 v1.0.

---

# 37. Provider Boundary

An external provider MAY supply:

- source material;
- regulatory data;
- filing transport;
- calculation support;
- evidence;
- other services accepted by later authority.

A provider SHALL NOT:

- create a Main Street `CommercialJurisdiction`;
- create a `RegulatoryPurpose`;
- redefine `FilingScopeReference`;
- make an unsupported jurisdiction supported;
- redefine source-owned business facts;
- establish Main Street support merely through provider availability.

The initial provider portfolio remains MS-PROT-082-DQ-004.

---

# 38. Regulatory-Source Monitoring Boundary

This amendment admits no source-monitoring cadence, polling mechanism, review frequency, source-change SLA or change-detection technology.

Before production regulatory support depends on external regulatory sources, MS-PROT-082-DQ-005 remains independently active according to its existing trigger.

Vocabulary acceptance SHALL NOT bypass that gate.

---

# 39. Professional Escalation Boundary

This amendment does not define:

- accountant escalation;
- bookkeeper escalation;
- lawyer/legal adviser escalation;
- regulated adviser workflow;
- professional approval;
- professional override.

That remains MS-PROT-082-DQ-006.

A `RegulatoryPurpose` SHALL NOT implicitly determine a professional escalation route.

---

# 40. Financial Health Boundary

Nothing in:

- Commercial Transaction Regulatory Administration;
- Commercial Activity Filing Scope Reference;
- regulatory evidence;

SHALL be interpreted as resolving the end-to-end Financial Health / Financial Intelligence problem.

MS-PROT-082-DQ-007 remains unresolved.

The incomplete MS-PROT-084 composition SHALL NOT be laundered into accepted authority through this amendment.

---

# 41. Source-Fact Ownership

Regulatory Administration MAY consume qualified facts from source capabilities.

It SHALL NOT become canonical owner of facts merely because those facts participate in a regulatory rule.

Conceptually:

```text
Source capability
    owns business fact
          ↓
MS-PROT-082
    consumes qualified fact
          ↓
accepted regulatory authority
    derives regulatory meaning
```

Examples include:

- merchant identity;
- operating location;
- workforce facts;
- commercial transactions;
- payments;
- bookings;
- appointments;
- inventory;
- document evidence.

The applicable existing capability retains business truth.

---

# 42. Evidence Extraction Boundary

Where a document participates in regulatory evidence:

```text
document/media
    ↓
Document Intake
    ↓
non-authoritative extraction candidate
    ↓
owner-qualified validation/evidence
    ↓
MS-PROT-082 regulatory interpretation
```

Document content, OCR output, model extraction or provider interpretation SHALL NOT automatically become regulatory truth.

MS-PROT-092's generic evidence boundary remains intact.

---

# 43. AI Authority Boundary

AI MAY:

- explain an accepted regulatory concept in ordinary language;
- ask the merchant for missing operational facts;
- classify candidate information for review;
- suggest a potentially relevant admitted jurisdiction or purpose;
- assist with document extraction under applicable evidence/security authority;
- identify conflicting or missing evidence;
- present accepted regulatory state in simpler language.

AI SHALL NOT:

- invent a jurisdiction;
- expand the accepted jurisdiction portfolio;
- invent a RegulatoryPurpose;
- select a nearest unsupported jurisdiction;
- invent regulatory law;
- invent a filing requirement;
- declare an obligation solely from model probability;
- convert extracted document text directly into regulatory truth;
- declare a merchant compliant;
- override a deterministic accepted rule;
- silently create semantic authority.

AI output remains assistance, not authority.

---

# 44. Merchant Experience

The merchant-facing experience SHALL be business-first, not taxonomy-first.

A merchant SHOULD NOT normally be asked:

> Select a `CommercialJurisdiction`.

or:

> Choose your `RegulatoryPurpose`.

or:

> Select a `FilingScopeReference` subtype.

Instead Main Street SHOULD collect ordinary operational facts where necessary and deterministically project accepted semantic configuration behind the interface.

Examples of ordinary questions MAY concern:

- where the business operates;
- whether it employs people;
- where a relevant premises is located;
- what business activity is conducted.

Internal semantic precision SHALL NOT become merchant-visible configuration burden.

---

# 45. Staff Experience

This amendment introduces no staff-facing regulatory-administration configuration responsibility.

Ordinary staff SHALL NOT need to understand:

- jurisdiction taxonomy;
- regulatory-purpose taxonomy;
- filing-scope taxonomy;
- support manifests;
- regulatory-rule provenance.

Where a future accepted workflow requires staff participation, the staff experience must remain role-native and task-specific.

---

# 46. Progressive Configuration

No merchant SHALL receive configuration or questions for all admitted Regulatory Purposes merely because the vocabulary exists.

Activation SHALL be based on represented business need and accepted regulatory authority.

Conceptually:

```text
admitted vocabulary
        ↓
relevant represented business reality
        ↓
accepted applicability rules
        ↓
only required regulatory configuration
```

not:

```text
merchant signs up
        ↓
configure all regulation
```

---

# 47. Exception-Driven Operation

This amendment does not create a regulatory dashboard requiring constant merchant supervision.

Where accepted regulatory administration later produces actionability, existing Merchant Attention and notification authorities may surface relevant exceptions or required decisions.

They consume regulatory meaning.

They SHALL NOT recreate or reinterpret it.

---

# 48. Expansion Rule

Expansion of any of the three portfolios requires deliberate accepted authority.

## CommercialJurisdiction expansion

Requires evidence that a new jurisdiction scope is actually needed.

## RegulatoryPurpose expansion

Requires evidence that an actual regulatory programme cannot be represented without semantic distortion.

## FilingScopeReference expansion

Requires evidence that an accepted regulatory matter refers to a materially different source-owned business scope.

No expansion may occur merely because:

- a provider offers another value;
- a database schema contains another code;
- AI suggests another category;
- a hypothetical future business might need it;
- a general compliance taxonomy contains it.

---

# 49. Fundamental Vision Conformance Review

## 49.1 Micro/small-business beneficiary

**PASS**

The proposal exists to let Main Street absorb jurisdiction/regulatory complexity for small local businesses rather than requiring enterprise compliance administration.

## 49.2 Sophisticated infrastructure without software-admin capacity

**PASS**

Internal regulatory semantics can be precise while merchant interaction remains ordinary-language and business-centred.

## 49.3 Business as organising abstraction

**PASS**

The model references merchant, operating location, workforce and commercial activity—the business realities to which regulation may relate.

It does not organise the merchant around regulator software modules.

## 49.4 Administrative Compression

**PASS**

Jurisdiction, purpose and filing-scope precision remain internal. Merchants need not administer taxonomies or integration mappings.

## 49.5 Staff learn the job, not Main Street

**PASS**

No general staff regulatory configuration role is introduced.

## 49.6 Merchant learns operating decisions, not architecture

**PASS**

The merchant may need to understand a real regulatory requirement when actionable, but not `CommercialJurisdiction`, `RegulatoryPurpose` or `FilingScopeReference` internals.

## 49.7 Progressive configuration

**PASS**

Vocabulary admission does not cause universal merchant activation.

## 49.8 Minimum sufficiently expressive capability

**PASS**

Two jurisdictions, three purposes and four reference subtypes are deliberately bounded.

Global jurisdiction catalogues, generic compliance engines and speculative vertical categories are excluded.

## 49.9 Capability breadth rather than maximal feature depth

**PASS**

The proposal establishes cross-business regulatory representation without attempting to become a complete accounting, employment-law, licensing or filing product.

## 49.10 Integration instead of conquest

**PASS**

Providers and source capabilities remain independent authorities for their own concerns.

MS-PROT-082 coordinates rather than absorbs them.

## 49.11 Ownership separation

**PASS**

`FilingScopeReference` is explicitly reference-only. It does not copy ownership of Merchant, Location, Workforce or Commerce state.

## 49.12 Cross-capability coordination

**PASS**

Regulatory applicability may consume facts across multiple capability owners through explicit semantic boundaries.

## 49.13 Exception-driven operation

**PASS**

Nothing creates a merchant obligation to supervise regulatory machinery continuously.

## 49.14 AI simplifies; deterministic Main Street operates

**PASS**

AI may assist explanation and candidate interpretation but cannot create regulatory authority.

## 49.15 Business-type neutrality

**PASS AFTER REVISION**

The accommodation-specific purpose from the earlier draft was removed because it would make the generic initial vocabulary reflect one vertical prematurely.

## 49.16 Provider replaceability

**PASS**

No provider vocabulary or identifier is semantic authority.

## 49.17 Low merchant-visible complexity

**PASS**

No generic regulatory setup screen is required by this design.

## 49.18 Target-market proportionality

**PASS**

The proposal is materially narrower than UK-wide or global compliance infrastructure while providing a useful first bounded scope.

## 49.19 No ERP drift

**PASS**

The design does not create accounting, HR, licensing, tax, document or compliance suites inside Regulatory Administration.

## 49.20 Vision result

**FUNDAMENTAL VISION CONFORMANCE: VISION-CONFORMING**

No Fundamental Vision exception or waiver is required.

---

# 50. Architecture Review

## 50.1 Business-model-first configuration

**PASS**

Regulatory configuration derives from represented business facts rather than a generic software-module selection process.

## 50.2 Generic composition

**PASS**

The same jurisdiction/purpose/scope model can serve materially different businesses.

## 50.3 Deterministic semantic authority

**PASS**

Vocabulary and rule authority remain explicit and accepted.

## 50.4 Capability ownership

**PASS**

Regulatory Administration references but does not absorb Merchant, Workforce, Location or Commerce truth.

## 50.5 Provenance

**PASS**

The proposal does not weaken MS-PROT-082's accepted source-backed rule/provenance model.

## 50.6 Progressive activation

**PASS**

Admitted vocabulary is not automatically active.

## 50.7 Role-native projection

**PASS**

Internal semantics need not surface as user-facing taxonomy.

## 50.8 Provider replaceability

**PASS**

Provider taxonomy remains external/replaceable.

## 50.9 Cross-capability coordination

**PASS**

`FilingScopeReference` establishes the necessary semantic bridge without shared ownership.

## 50.10 Semantic duplication

**PASS**

No duplicate Merchant, Location, Workforce or Commerce aggregate is introduced.

## 50.11 Coupling

**PASS**

References depend on source-owned identities/contracts, not source internal models.

## 50.12 Business-specific exception accumulation

**PASS AFTER REVISION**

Accommodation-specific purpose removed.

## 50.13 Speculative machinery

**PASS**

No rules engine, global legal ontology, filing infrastructure or generic compliance suite is introduced.

## 50.14 DQ-003 implementation leakage

**PASS**

No Java, persistence, serialization or storage representation is defined.

## 50.15 DQ-004 provider leakage

**PASS**

No regulatory provider is selected.

## 50.16 DQ-005 monitoring leakage

**PASS**

No monitoring mechanism/frequency is selected.

## 50.17 DQ-006 escalation leakage

**PASS**

No professional escalation integration is selected.

## 50.18 DQ-007 Financial Health leakage

**PASS**

Financial Health remains explicitly outside scope.

## 50.19 Public-support overclaim

**PASS AFTER REVISION**

The earlier cross-product matrix was removed and replaced by the explicit no-automatic-binding invariant.

## 50.20 Architecture-review result

**ARCHITECTURE REVIEW: PASS**

No authority collision, unnecessary cross-capability ownership or speculative platform expansion remains identified within the proposed scope.

---

# 51. Ambiguity Review Method

The ambiguity review asks a different question from falsification.

It does not ask whether the design survives an adversarial scenario.

It asks:

> Could two reasonable implementers, designers or reviewers read this authority and derive materially different semantic behaviour?

An ambiguity is considered closed only where the authority either:

1. gives one normative interpretation; or
2. explicitly preserves the question for another accepted authority rather than permitting implicit invention.

---

# 52. Ambiguity Review

| # | Ambiguity | Normative resolution |
|---|---|---|
| A01 | Does `CommercialJurisdiction` mean sovereign state? | No. It is a Main Street regulatory-applicability scope identity. |
| A02 | Does separating England and Wales make a constitutional claim? | No. The separation is product-semantic only. |
| A03 | Is `United Kingdom` implicitly a supported CommercialJurisdiction? | No. It is not in this initial portfolio. |
| A04 | Does portfolio membership mean public launch? | No. Vocabulary admission and launch are distinct. |
| A05 | Does portfolio membership mean regulatory support? | No. It creates zero support claim. |
| A06 | Does support for one purpose mean support for all purposes in that jurisdiction? | No. There is no automatic cross-product. |
| A07 | Does `RegulatoryPurpose` state a legal obligation? | No. It states why accepted regulatory administration exists. |
| A08 | Is `RegulatoryPurpose` a regulator classification? | Not necessarily. It is Main Street semantic vocabulary. |
| A09 | Is purpose the same as source-backed rule authority? | No. Rule/source authority remains separate. |
| A10 | Is `FilingScopeReference` another name for purpose? | No. Purpose = why; scope reference = what represented business scope is concerned. |
| A11 | Does a scope reference transfer ownership? | No. It is reference-only. |
| A12 | Does Merchant Filing Scope mean every merchant obligation is merchant-wide? | No. It is used only where accepted authority requires merchant-level scope. |
| A13 | Does Operating Location Filing Scope create a premises aggregate? | No. It references the source-owned location. |
| A14 | Does Workforce Filing Scope own staff records? | No. Workforce retains ownership. |
| A15 | Does Commercial Activity Filing Scope own transactions/orders? | No. Commerce owners retain them. |
| A16 | Does postal address universally establish jurisdiction? | No. |
| A17 | Does registered-office address universally establish jurisdiction? | No. |
| A18 | Does operating location always establish jurisdiction? | No. It is one possible source fact under accepted rules. |
| A19 | Does customer location automatically establish merchant jurisdiction? | No universal rule is admitted. |
| A20 | Does an online transaction default to merchant address jurisdiction? | No. Applicable accepted authority must decide. |
| A21 | Can a merchant have multiple applicable jurisdictions? | Yes. No one-merchant/one-jurisdiction invariant exists. |
| A22 | What happens when jurisdiction cannot be determined? | Applicability remains unresolved; no default jurisdiction is invented. |
| A23 | What happens when jurisdiction is known but unsupported? | It remains unsupported; no fallback mapping occurs. |
| A24 | Is `Other` a legal fallback? | No. No `Other` jurisdiction or purpose is admitted. |
| A25 | Can provider coverage make a jurisdiction supported? | No. |
| A26 | Can AI confidence make a jurisdiction applicable? | No. |
| A27 | Can OCR/document extraction create regulatory truth? | No. |
| A28 | Can merchant-entered text itself create regulatory authority? | No. It may supply source facts/evidence subject to applicable qualification. |
| A29 | Does the purpose portfolio create a tax subsystem? | No. |
| A30 | Does Workforce Regulatory Administration redefine Workforce semantics? | No. |
| A31 | Does Business Authorisation purpose mean a licence is required? | No. |
| A32 | Is accommodation/guest reporting silently included in Business Authorisation? | No. A materially distinct future purpose requires admission rather than semantic squeezing. |
| A33 | Must one regulatory matter have exactly one purpose? | This amendment does not change parent cardinality; it forbids invented nearest-purpose mapping. |
| A34 | Can one filing concern more than one source-owned business scope? | This amendment does not impose a universal cardinality; accepted programme authority must determine the required references. |
| A35 | Does external jurisdiction coding become semantic identity? | No. Physical/external representation is separate from semantic identity. |
| A36 | Are Java enum names established by the English semantic labels? | No. DQ-003 remains open. |
| A37 | Does accepting England and Wales require both to launch simultaneously? | No. |
| A38 | Can a current address rewrite historical regulatory context? | No. |
| A39 | Does a regulatory-purpose value create a Merchant Attention item? | No. Actionability must arise through accepted regulatory semantics and applicable attention authority. |
| A40 | Does BI derive authoritative regulatory state? | No. BI may analyse source state but not originate it. |
| A41 | Does this amendment resolve provider selection? | No; DQ-004 remains open. |
| A42 | Does this amendment resolve regulatory-source freshness? | No; DQ-005 remains open. |
| A43 | Does this amendment resolve accountant/adviser integration? | No; DQ-006 remains open. |
| A44 | Does this amendment resolve Financial Health? | No; DQ-007 remains open. |
| A45 | Can new vocabulary be added during implementation? | No. Material semantic expansion requires accepted design authority. |
| A46 | Can new vocabulary be added during post-approval formalisation? | No. Formalisation may record only approved consequences. |

## 52.1 Ambiguity-review result

**AMBIGUITY REVIEW: PASS**

No identified ambiguity is left available for an implementer to answer by invention.

Questions intentionally outside this amendment are explicitly routed to their existing owner or deferred decision rather than silently left unspecified.

---

# 53. Falsification Method

Falsification SHALL attempt to demonstrate that this authority is materially wrong, not merely confirm that normal examples work.

The proposal is considered falsified if a tested scenario demonstrates that it:

1. requires a business-type-specific exception in the generic base model;
2. creates an unearned public regulatory-support claim;
3. cannot preserve materially different jurisdiction applicability;
4. assumes one merchant has exactly one jurisdiction;
5. allows unsupported geography to masquerade as supported;
6. allows uncertainty to become a regulatory default;
7. permits AI/provider/document output to create semantic authority;
8. duplicates canonical business facts inside Regulatory Administration;
9. requires merchants or ordinary staff to administer internal regulatory architecture;
10. conflates purpose, filing scope and obligation;
11. prematurely answers DQ-003 through DQ-007;
12. creates a general compliance/ERP platform;
13. forces grouped DQs to receive the same recommendation;
14. loses historical meaning when business context changes;
15. cannot refuse safely when regulatory evidence is insufficient.

Falsification outcomes are:

- **SURVIVES** — the proposed invariant handles the attack without additional semantic rule;
- **BOUNDED REFUSAL** — safe behaviour is to withhold applicability/support rather than invent an answer;
- **REVISION INCORPORATED** — the attack exposed a defect in the earlier draft and this proposal was changed before presentation;
- **FALSIFIED** — the current proposal cannot safely represent the scenario without material redesign.

---

# 54. Falsification Matrix

| # | Attack | Required safe behaviour | Result |
|---|---|---|---|
| F01 | England salon with a future transaction-related obligation | Reuse generic Commerce purpose/scope; no salon branch | SURVIVES |
| F02 | Welsh retail shop with equivalent regulatory concern | Same generic vocabulary with Wales jurisdiction | SURVIVES |
| F03 | Restaurant needing future business authorisation administration | Business Authorisation purpose; no restaurant-specific base type | SURVIVES |
| F04 | Motel needs materially distinct guest-reporting administration | Do not squeeze into existing purpose; require fresh Feature Admission | **REVISION INCORPORATED** — accommodation purpose removed |
| F05 | Tradesperson works at customer locations across England and Wales | No one-address/one-jurisdiction assumption | SURVIVES |
| F06 | Professional service with no currently relevant regulatory programme | No regulatory configuration merely because vocabulary exists | SURVIVES |
| F07 | Merchant operates only in England | England may be applicable without Wales activation | SURVIVES |
| F08 | Merchant operates only in Wales | Wales may be applicable without England activation | SURVIVES |
| F09 | Merchant materially operates in both | Multiple jurisdiction applicability permitted | SURVIVES |
| F10 | Registered address England, relevant operation Wales | Address does not universally decide applicability | SURVIVES |
| F11 | Multiple premises on both sides of jurisdiction boundary | Operating-location references preserve relevant source scopes | SURVIVES |
| F12 | Merchant operates only in Scotland | No fallback to England/Wales | BOUNDED REFUSAL |
| F13 | Merchant operates only in Northern Ireland | No fallback to England/Wales | BOUNDED REFUSAL |
| F14 | Crown Dependency merchant | No UK-like approximation | BOUNDED REFUSAL |
| F15 | Operational location unknown | Applicability remains unresolved | BOUNDED REFUSAL |
| F16 | Two authoritative business facts conflict about location | No default jurisdiction; conflict must remain unresolved/qualified | BOUNDED REFUSAL |
| F17 | English merchant sells online to customer elsewhere | Customer location does not create universal rule | SURVIVES |
| F18 | Mobile business performs work in both accepted jurisdictions | Jurisdiction may be activity-qualified | SURVIVES |
| F19 | Workforce distributed across accepted jurisdictions | Workforce scope can remain source-qualified; no merchant-wide shortcut | SURVIVES |
| F20 | Merchant relocates from Wales to England | Current state does not rewrite historical regulatory context | SURVIVES |
| F21 | Commercial transaction regulation appears | Generic Commercial Transaction purpose available | SURVIVES |
| F22 | Workforce-related regulatory programme appears | Generic Workforce purpose available | SURVIVES |
| F23 | Business licence/registration programme appears | Generic Business Authorisation purpose available | SURVIVES |
| F24 | Developer wants to classify everything as `Compliance` | No such admitted purpose exists | SURVIVES |
| F25 | New accommodation-specific regulatory requirement appears | Requires fresh admission if materially distinct | SURVIVES |
| F26 | One regulatory programme touches commerce and workforce | No forced nearest-purpose mapping; concrete authority must qualify properly | SURVIVES |
| F27 | Scope of a filing cannot be established from available evidence | Do not invent Merchant scope as default | BOUNDED REFUSAL |
| F28 | Engineer treats vocabulary membership as production support | Explicit invariant forbids it | **REVISION INCORPORATED** — prior matrix removed |
| F29 | Only a Wales-specific concrete pack exists | England vocabulary does not become active automatically | SURVIVES |
| F30 | Provider supports England but no accepted Main Street programme exists | Provider availability creates no semantic support | SURVIVES |
| F31 | Internal authority exists but provider-backed support was never accepted | No provider-backed claim; DQ-004 gate remains | SURVIVES |
| F32 | Regulatory source may have changed and monitoring is unresolved | Production claim cannot bypass DQ-005 | BOUNDED REFUSAL |
| F33 | Provider declares a merchant compliant | Provider statement does not become Main Street semantic authority automatically | SURVIVES |
| F34 | Previously supported regulatory programme is withdrawn | Existing MS-PROT-082 support/history authority governs; vocabulary survives independently | SURVIVES |
| F35 | OCR extracts a filing identifier | Candidate remains non-authoritative until qualified | SURVIVES |
| F36 | Malicious document tells AI to mark merchant compliant | Document content has no command/semantic authority | SURVIVES |
| F37 | Merchant corrects extracted document text | Correction is evidence/input under owner rules, not universal regulatory authority | SURVIVES |
| F38 | Two documents conflict | No model-confidence shortcut to authoritative regulatory truth | BOUNDED REFUSAL |
| F39 | AI predicts Wales with 98% confidence | Prediction cannot become regulatory applicability solely from confidence | SURVIVES |
| F40 | AI invents a plausible fourth purpose | Purpose remains unadmitted | SURVIVES |
| F41 | AI answers “you are compliant” without accepted deterministic basis | Prohibited | SURVIVES |
| F42 | Low-software-capacity merchant onboards | Merchant need not understand internal taxonomy | SURVIVES |
| F43 | Frontline staff uses Main Street | No regulatory-admin taxonomy knowledge required | SURVIVES |
| F44 | Merchant has no current regulated capability | No unnecessary regulatory setup | SURVIVES |
| F45 | Merchant asks “Do you support Wales?” | Main Street cannot infer blanket yes from portfolio membership | SURVIVES |
| F46 | External regulation changes | Vocabulary remains stable; source-change handling remains DQ-005/parent authority | SURVIVES |
| F47 | Business adds a second location later | Progressive configuration can evaluate newly relevant scope | SURVIVES |
| F48 | Historical filing evidence is inspected after relocation | Historical context must remain interpretable | SURVIVES |
| F49 | BI computes a “compliance score” and attempts to write regulatory state | BI has no authority to originate regulatory truth | SURVIVES |
| F50 | Merchant Attention displays a regulatory exception | Attention may surface but not reinterpret source regulatory state | SURVIVES |
| F51 | Workforce Compensation calculation depends on jurisdiction | Compensation remains source owner; MS-PROT-082 does not absorb it | SURVIVES |
| F52 | Financial Health wants regulatory obligations as input | Consumption does not resolve MS-PROT-082-DQ-007 or blocked MS-PROT-084 authority | SURVIVES |
| F53 | Developer asks which Java enum represents England | DQ-003 remains unresolved | BOUNDED REFUSAL |
| F54 | Provider jurisdiction ID is convenient as database primary key | Convenience cannot make provider identity semantic authority | SURVIVES |
| F55 | DQ-001 proves acceptable but later evidence forces revision of DQ-002 | Group permits independent recommendation/status | SURVIVES |
| F56 | Marketing wants “UK compliance supported” after acceptance | Neither UK nor blanket compliance support is authorised | SURVIVES |
| F57 | Merchant has Wales address but England-only regulated activity | No universal address rule | SURVIVES |
| F58 | Merchant is registered in England but premises/business activity span England and Wales | Applicability remains fact/rule-qualified | SURVIVES |
| F59 | One filing refers to merchant and one operating location | Authority may use appropriate references; no global cardinality invented | SURVIVES |
| F60 | A future filing targets a resource type not covered by four references | Do not misuse nearest subtype; fresh Feature Admission required | SURVIVES |
| F61 | Provider introduces its own `OTHER_REGION` value | Cannot enter semantic portfolio implicitly | SURVIVES |
| F62 | An engineer preloads Scotland “for future use” | Material semantic expansion prohibited | SURVIVES |
| F63 | A merchant-facing settings screen exposes all purposes | Violates Administrative Compression; not authorised by this proposal | SURVIVES |
| F64 | Regulatory team asks MS-PROT-082 to store its own copy of every order | Ownership invariant prohibits it | SURVIVES |
| F65 | One regulation applies to only one historical transaction population | Commercial Activity reference can point to source-owned bounded scope without moving transaction ownership | SURVIVES |
| F66 | A licence is specific to one premises | Operating Location reference can represent scope without creating a licence because concrete authority is still required | SURVIVES |
| F67 | Merchant's regulator uses different geographic terminology | External terminology may be mapped at boundary but cannot rewrite Main Street semantic identity without accepted authority | SURVIVES |
| F68 | Team uses accepted England/Wales vocabulary to skip source provenance | Parent source-backed authority remains mandatory | SURVIVES |
| F69 | Team claims accepted purpose is enough to derive a filing deadline | Purpose does not create concrete rule/obligation | SURVIVES |
| F70 | A new jurisdiction can reuse identical rules | Rule similarity does not automatically admit the jurisdiction | SURVIVES |

---

# 55. Falsification Findings

The falsification exercise materially changed the proposal before approval.

## Finding 1 — Business-type leakage

The earlier draft admitted **Accommodation and Guest Reporting** as an initial purpose.

Testing against the Fundamental Vision's business-type-neutrality rule showed that this prematurely elevated a motel/accommodation concern into generic cross-business vocabulary.

**Correction:** removed from the initial portfolio.

## Finding 2 — False support through cross-product presentation

The earlier draft showed a complete England/Wales × purpose matrix labelled “admitted for representation”.

Although accompanied by caveats, that representation could still be interpreted by later designers or implementers as a pre-authorised set of jurisdiction-purpose combinations.

**Correction:** the matrix is removed.

The revised authority establishes:

```text
no automatic Cartesian-product support
```

## Finding 3 — DQ-002 detail loss

The earlier draft treated DQ-002 primarily as a `RegulatoryPurpose` question and omitted its explicit `FilingScopeReference` subtype responsibility.

**Correction:** four target-specific filing-scope-reference subtypes are now defined.

## Finding 4 — Incorrect deferred-tail description

The earlier draft incorrectly described DQ-003 through DQ-006 as different semantic questions.

**Correction:** this proposal preserves the canonical DDR concerns exactly:

- DQ-003 — physical Java/persistence representation;
- DQ-004 — provider portfolio;
- DQ-005 — regulatory-source monitoring;
- DQ-006 — professional escalation;
- DQ-007 — Financial Health / Financial Intelligence.

## 55.1 Falsification conclusion

**RESULT: NOT FALSIFIED WITHIN THE TESTED SCOPE**

This result is not a claim that the design is universally correct.

It means the current proposal survived the listed attempts to demonstrate:

- false support;
- business-specific leakage;
- authority collision;
- ownership collapse;
- unsafe defaults;
- merchant complexity;
- AI/provider authority substitution;
- deferred-decision laundering;
- historical-context loss.

Future counter-evidence remains capable of forcing REVISE or REJECT through the normal design lifecycle.

---

# 56. Independent Recommendation — MS-PROT-082-DQ-001

## Recommendation

**ACCEPT**

Resolve the initial concrete `CommercialJurisdiction` portfolio as:

```text
{
    England,
    Wales
}
```

subject to all boundaries and semantics in this amendment.

## Resolution effect

If approved:

```text
MS-PROT-082-DQ-001
    OPEN
        →
    RESOLVED BY MS-PROT-082 v1.1
```

This does not itself activate a jurisdiction-specific public launch.

---

# 57. Independent Recommendation — MS-PROT-082-DQ-002

## Recommendation

**ACCEPT**

Resolve the initial `RegulatoryPurpose` portfolio as:

```text
{
    Commercial Transaction Regulatory Administration,
    Workforce Regulatory Administration,
    Business Authorisation Regulatory Administration
}
```

and the initial target-specific `FilingScopeReference` subtype vocabulary as:

```text
{
    Merchant Filing Scope Reference,
    Operating Location Filing Scope Reference,
    Workforce Filing Scope Reference,
    Commercial Activity Filing Scope Reference
}
```

subject to all boundaries and semantics in this amendment.

## Resolution effect

If approved:

```text
MS-PROT-082-DQ-002
    OPEN
        →
    RESOLVED BY MS-PROT-082 v1.1
```

No concrete jurisdiction pack becomes active merely from this resolution.

---

# 58. Group Recommendation Integrity

The fact that both DQs currently receive **ACCEPT** is evidence-derived, not imposed by the group.

The accepted grouping rule remains:

```text
shared reasoning
    ≠
shared decision identity
```

If DQ-002 were revised while DQ-001 remained acceptable, DQ-001 could still be accepted independently.

Approval of `JRA-GRP-SCOPE-01` as a label alone SHALL NOT resolve either DQ.

Only approval of the complete normative resolutions contained in this amendment does so.

---

# 59. Remaining MS-PROT-082 Deferred Tail

Acceptance of v1.1 SHALL leave the following unchanged.

## MS-PROT-082-DQ-003

**Concern:** Exact Java/persistence representation for programmes, rules, deadlines, obligations and filing evidence.

**Status after v1.1:** OPEN / existing trigger unchanged.

## MS-PROT-082-DQ-004

**Concern:** Initial regulatory provider portfolio.

**Status after v1.1:** OPEN / existing provider-backed-support trigger unchanged.

## MS-PROT-082-DQ-005

**Concern:** Regulatory-source change-monitoring mechanism and review frequency.

**Status after v1.1:** OPEN / existing production-regulatory-support trigger unchanged.

## MS-PROT-082-DQ-006

**Concern:** Professional escalation integration.

**Status after v1.1:** OPEN / existing trigger unchanged.

## MS-PROT-082-DQ-007

**Concern:** Complete Financial Health / Financial Intelligence operating model.

**Status after v1.1:** OPEN / existing prerequisite trigger unchanged.

No sequencing statement in this amendment automatically promotes any of them.

---

# 60. Implementation-Rules Impact Review

## 60.1 Semantic implementation authorisation

**NONE**

This authority selects semantic vocabulary only.

It SHALL NOT be interpreted as approval to add:

- Java enums;
- Java records/classes;
- database tables;
- columns;
- migrations;
- API schemas;
- persistence identifiers;
- provider adapters;
- government integrations;
- regulatory-source crawlers;
- regulatory dashboards;
- merchant configuration screens;
- production jurisdiction packs.

## 60.2 DQ-003 protection

The implementation team cannot infer exact code representation from this document.

For example:

```text
semantic term: England
```

does not imply:

```java
CommercialJurisdiction.ENGLAND
```

or any particular identifier, enum, table or serialization.

Those remain implementation/design work under the applicable DQ and IMPLEMENTATION-RULES.

## 60.3 Current implementation impact

**IMPLEMENTATION-RULES IMPACT: NONE**

No implementation rule requires amendment as a consequence of accepting this semantic scope alone.

---

# 61. Pre-Approval Corpus Conformance Review

This is the **pre-approval design-corpus review** required to determine whether the proposal is safe to present for manual approval.

It is not a substitute for the formal post-write corpus-conformance check required after approved authority is persisted.

## 61.1 MS-FUNDAMENTAL-VISION-001

**PASS**

The proposal strengthens Administrative Compression, deterministic authority, business-type neutrality, progressive configuration, provider replaceability and bounded ownership.

## 61.2 DESIGN-RULES

**PASS**

The proposal has:

- explicit Feature Admission;
- Fundamental Vision Conformance;
- architecture review;
- ambiguity review;
- falsification;
- independent recommendations;
- complete proposed normative authority;
- no pre-approval repository mutation.

## 61.3 MS-PROT-082 v1.0

**PASS — ADDITIVE**

The proposal selects concrete portfolio values for abstractions deliberately left open by v1.0.

It does not redefine:

- jurisdiction policy;
- source-backed rule authority;
- programme semantics;
- obligation semantics;
- filing-evidence semantics;
- regulatory-effect semantics;
- support dimensions;
- support manifest/history;
- support eligibility;
- regulatory exception semantics.

## 61.4 DEFERRED-DECISION-REGISTER

**PASS**

DQ-001 is answered directly.

DQ-002 is answered completely, including `FilingScopeReference`.

DQ-003 through DQ-007 retain their canonical concerns and statuses unless separately governed later.

## 61.5 SEQUENCE — Lossless Grouping Overlay

**PASS**

The proposal uses JRA-GRP-SCOPE-01 only as a shared execution envelope.

Individual DQ identity and recommendations remain intact.

No registration or approval of the group promotes unrelated work.

## 61.6 AUTHORITY-INDEX

**PASS — NO PRE-APPROVAL CHANGE**

MS-PROT-082 remains indexed at its currently accepted version until manual approval and persistence.

## 61.7 CANONICAL-SEMANTIC-LEXICON

**PASS WITH POST-APPROVAL UPDATE REQUIRED**

The following become candidate canonical terms only if the proposal is approved:

- England as an MS-PROT-082 `CommercialJurisdiction`;
- Wales as an MS-PROT-082 `CommercialJurisdiction`;
- Commercial Transaction Regulatory Administration;
- Workforce Regulatory Administration;
- Business Authorisation Regulatory Administration;
- Merchant Filing Scope Reference;
- Operating Location Filing Scope Reference;
- Workforce Filing Scope Reference;
- Commercial Activity Filing Scope Reference.

Formalisation SHALL first check for existing canonical equivalents and SHALL reuse rather than duplicate them where semantics are identical.

No lexicon term is authoritative merely because it appears in this proposal.

## 61.8 MS-PROT-080 Workforce Compensation

**PASS**

Workforce Compensation retains compensation and workforce-specific jurisdiction treatment ownership.

MS-PROT-082 provides regulatory-jurisdiction semantics only.

No compensation calculation or jurisdiction-treatment rule is added here.

## 61.9 Workforce authorities

**PASS**

Workforce Filing Scope Reference does not transfer staff, scheduling, leave, timekeeping or compensation ownership.

## 61.10 Commerce authorities

**PASS**

Commercial Transaction Regulatory Administration and Commercial Activity Filing Scope Reference do not transfer order, appointment, booking, payment or transaction ownership.

## 61.11 MS-PROT-083 Business Intelligence

**PASS**

BI may later observe accepted regulatory facts but does not create regulatory authority.

No analytical measure or Business Health authority is added.

## 61.12 MS-PROT-085 Merchant Attention

**PASS**

Attention may surface regulatory actionability but remains a consumer of source-owned regulatory state.

## 61.13 MS-PROT-092 Document Intake / Evidence Extraction

**PASS**

Document extraction remains non-authoritative until qualified by the relevant owner/authority.

## 61.14 MS-PROT-084 blocked Financial Operations composition

**PASS**

No blocked Financial Operations or Financial Health decision is imported or treated as resolved.

## 61.15 Provider authority

**PASS**

No provider is made semantic owner and DQ-004 remains unresolved.

## 61.16 Regulatory source-monitoring authority

**PASS**

No monitoring cadence/mechanism is introduced and DQ-005 remains unresolved.

## 61.17 IMPLEMENTATION-RULES

**PASS**

No implementation decision is smuggled into semantic authority.

## 61.18 Pre-approval corpus result

**PRE-APPROVAL CORPUS CONFORMANCE: PASS**

No known accepted-authority collision remains after the revisions described in the falsification findings.

Formal post-approval corpus conformance remains mandatory after repository formalisation.

---

# 62. Formalisation Consequences if Approved

Manual approval of this complete proposal SHALL authorise only the following post-approval consequences.

## 62.1 Persist accepted authority

Write this approved amendment as MS-PROT-082 v1.1 in the accepted design corpus.

No additional semantic rule may be added while converting this proposal into repository form.

## 62.2 Authority Index

Update MS-PROT-082 to indicate accepted composite authority through v1.1.

## 62.3 Deferred Decision Register

Update individually:

```text
MS-PROT-082-DQ-001
    → RESOLVED BY MS-PROT-082 v1.1

MS-PROT-082-DQ-002
    → RESOLVED BY MS-PROT-082 v1.1
```

No other MS-PROT-082 DQ status may change as a consequence of this approval.

## 62.4 Canonical Semantic Lexicon

Add or align only the semantic terms made authoritative by this amendment.

Existing equivalent canonical terms SHALL be reused rather than duplicated.

## 62.5 SEQUENCE

Reconcile JRA-GRP-SCOPE-01 to record the two independently resolved member outcomes.

The sequence update SHALL NOT automatically promote DQ-003 or another design node.

## 62.6 IMPLEMENTATION-RULES review

Record:

```text
Implementation-rule amendment required:
    NONE
```

unless the post-approval conformance review discovers an actual accepted-authority consequence already contained in this proposal.

Such review SHALL NOT invent a new design rule.

## 62.7 Formal corpus conformance

Run formal `DESIGN-CORPUS-CONFORMANCE` against the persisted authority and all consequential governance changes.

If a material contradiction is discovered, formalisation SHALL stop and return to design rather than silently editing approved semantics.

## 62.8 Commit

Commit only:

- the approved authority;
- required Authority Index consequence;
- required DDR consequences;
- required Lexicon consequence;
- required SEQUENCE consequence;
- required corpus-conformance record;
- any non-semantic bookkeeping directly required by existing governance.

No implementation code is authorised.

---

# 63. Post-Approval Prohibition

Formalisation SHALL NOT introduce:

- Scotland;
- Northern Ireland;
- `United Kingdom` as an MS-PROT-082 CommercialJurisdiction;
- an `Other` jurisdiction;
- additional Regulatory Purposes;
- additional FilingScopeReference subtypes;
- Java representations;
- provider selections;
- source-monitoring policy;
- professional escalation;
- Financial Health semantics;
- business-specific accommodation regulation;
- government filing APIs;
- tax calculations;
- merchant compliance conclusions.

Any such requirement discovered during formalisation returns to the applicable design lifecycle.

---

# 64. Canonical Result

If this complete proposal is approved, the accepted result is:

```text
MS-PROT-082 v1.1

CommercialJurisdiction portfolio
    =
    {
        England,
        Wales
    }

RegulatoryPurpose portfolio
    =
    {
        Commercial Transaction Regulatory Administration,
        Workforce Regulatory Administration,
        Business Authorisation Regulatory Administration
    }

FilingScopeReference subtype portfolio
    =
    {
        Merchant Filing Scope Reference,
        Operating Location Filing Scope Reference,
        Workforce Filing Scope Reference,
        Commercial Activity Filing Scope Reference
    }
```

with the following mandatory invariants:

```text
CommercialJurisdiction
    = regulatory-applicability scope identity
    ≠ constitutional claim
    ≠ universal support claim

RegulatoryPurpose
    = why regulatory administration exists
    ≠ regulatory obligation
    ≠ regulatory rule
    ≠ compliance result

FilingScopeReference
    = reference to source-owned business scope
    ≠ ownership transfer
    ≠ duplicated aggregate

vocabulary admission
    ≠ jurisdiction-pack activation

vocabulary admission
    ≠ public support

provider availability
    ≠ semantic authority

AI confidence
    ≠ semantic authority

document extraction
    ≠ regulatory truth

unsupported jurisdiction
    ≠ nearest supported jurisdiction

unknown applicability
    ≠ default applicability

current merchant state
    ≠ authority to rewrite historical regulatory context
```

and:

```text
MS-PROT-082-DQ-001
    → ACCEPT / RESOLVE BY v1.1

MS-PROT-082-DQ-002
    → ACCEPT / RESOLVE BY v1.1

MS-PROT-082-DQ-003
    → unchanged

MS-PROT-082-DQ-004
    → unchanged

MS-PROT-082-DQ-005
    → unchanged

MS-PROT-082-DQ-006
    → unchanged

MS-PROT-082-DQ-007
    → unchanged

Implementation activation
    → NONE
```

---

# 65. Final Review Outcomes

**Feature Admission:** PASS  
**Fundamental Vision Conformance:** VISION-CONFORMING  
**Architecture Review:** PASS  
**Ambiguity Review:** PASS  
**Falsification:** NOT FALSIFIED WITHIN TESTED SCOPE  
**Pre-Approval Corpus Conformance:** PASS  
**Implementation-Rules Impact:** NONE  

**MS-PROT-082-DQ-001 Recommendation:** **ACCEPT**  
**MS-PROT-082-DQ-002 Recommendation:** **ACCEPT**

---

# 66. Approval Boundary

Approval applies only to the complete proposal above.

Approval of a summary, group name, earlier draft or selected section SHALL NOT substitute for approval of this complete authority.

Until explicit approval:

- no repository authority shall be written;
- no DDR status shall change;
- no Authority Index entry shall change;
- no Lexicon term shall become authoritative;
- no implementation shall be activated.

Upon explicit approval, formalisation may perform only the consequences enumerated in Section 62.