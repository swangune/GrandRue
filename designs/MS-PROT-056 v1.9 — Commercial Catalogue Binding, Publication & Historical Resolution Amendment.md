# MS-PROT-056 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment

**Document ID:** MS-PROT-056
**Version:** 1.9
**Status:** ACCEPTED
**Approved:** 14 September 2026 — explicit manual approval of the complete contract and limited formalisation scope
**Authority type:** Commercial catalogue authority
**Governed by:** `DESIGN-RULES.md` v2.3; `DOCUMENT-GOVERNANCE.md` v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** MS-PROT-056 v1.0 §§4–7, v1.1 §§6–8 and v1.5 §§12–13 within catalogue binding and publication scope; supplies the historical catalogue-resolution policy required by v1.6 §3
**Preserves:** Other applicable MS-PROT-056 provisions through v1.8
**Depends on:** Composite MS-PROT-062; MS-PROT-071 v1.2; MS-PROT-072
**Implementation activation:** NONE
**Purpose:** Define exact commercial binding requirements, immutable catalogue publication, initial effective start, historical selection and recovery without allowing packaging to create business semantics.

### 1. Governing decision

Commercial SHALL publish standard entitlement catalogues as immutable, explicitly approved generations.

Each generation SHALL contain exactly one FREE, one BUSINESS and one GROWTH plan revision, with explicit grant sets satisfying:

```text
FREE grants ⊆ BUSINESS grants ⊆ GROWTH grants
```

Catalogue publication SHALL NOT:

- activate a capability;
- establish a merchant agreement;
- grant actor privileges;
- establish provider readiness;
- migrate an existing Standing Free baseline;
- rewrite an existing agreement or historical grant.

### 2. Scope and exclusions

This amendment governs binding, publication and historical resolution of standard catalogue generations.

It does not select the complete production entitlement manifest, prices, quantitative allowances, reserved-service portfolios or implementation technology.

It introduces no:

- merchant-editable entitlement catalogue;
- regional catalogue partition;
- scheduled future publication;
- retroactive catalogue publication;
- automatic migration of existing commercial sources;
- provider-owned Commercial authority;
- new residual-access classification.

Pricing and agreement establishment remain independently governed. Publication of grant policy alone SHALL NOT make an incomplete paid proposition acceptable as a paid agreement.

### 3. Canonical terminology and ownership

**Commercial Access Binding** means the Commercial-owned association between one stable entitlement identity and one exact, already-governed access point for one protected commercial purpose.

**Commercial Catalogue Manifest** means the complete immutable content proposed for one standard catalogue generation, including definitions, bindings, explicit plan grants and approval provenance.

**Commercial Catalogue Publication** means the Commercial-owned committed fact that one exact manifest became the next published generation at its authoritative publication instant.

These concepts are distinct from Merchant Publication, Merchant Configuration and Semantic Registry Release publication.

Source owners retain their operations, analytical definitions, service contracts, applicability rules, actor authority and business invariants.

### 4. Exact binding requirements

Every Commercial Access Binding SHALL identify:

1. a stable Commercial Entitlement identity;
2. its target family under MS-PROT-056 v1.0 §7;
3. the owning capability or platform service;
4. the exact target identity and governing contract revision;
5. the protected commercial purpose;
6. the accepted authority defining that target and purpose;
7. supporting access requirements necessary to use the granted service;
8. the boundary between protected new use and any separately governed residual access.

A capability name, feature label, document-series number or tier name is insufficient as an executable target reference.

Bindings SHALL NOT use wildcards such as:

```text
all Business Intelligence
all future Booking operations
all AI services
everything under a capability namespace
```

An unresolved target, supporting dependency or purpose classification SHALL prevent that binding from admission.

### 5. Binding identity and satisfaction

Within the Commercial authority:

- one entitlement identity SHALL retain one immutable binding meaning;
- one exact owner-qualified target and protected-purpose pair SHALL have one canonical entitlement identity;
- different protected purposes at the same target MAY have different entitlement identities.

A target lookup SHALL NOT itself determine runtime permission.

The target’s governing access contract SHALL explicitly identify which commercial purposes apply. Where several commercial purposes are required, all required purposes SHALL be satisfied.

An access contract that requires no Commercial Entitlement SHALL state that classification explicitly. Missing binding information SHALL NOT be interpreted as an exemption.

Independent valid grant sources may satisfy the same entitlement identity. This preserves the grant-source composition governed by MS-PROT-056 v1.5 §§14–16.

A change to target meaning, protected purpose or commercial boundary requires a new entitlement identity. Mere repackaging of an unchanged entitlement does not.

### 6. Manifest completeness

A publishable manifest SHALL contain:

- one catalogue revision identity;
- three distinct plan revision identities, each assigned its exact standard level;
- the complete explicit entitlement set for each plan;
- the exact binding definitions required to interpret every grant;
- exact references to reused immutable definitions;
- evidence of conformance with accepted tier-allocation authority;
- retained approval provenance identifying the exact approved content.

Every referenced definition SHALL resolve before publication.

The manifest SHALL contain no unresolved reservation presented as an executable grant.

Every included supporting service SHALL be checked against MS-PROT-056 v1.7 §10 and v1.8 §6. A necessary supporting presentation path SHALL NOT impose an additional higher-tier requirement.

Publishing a catalogue does not establish that every granted service is implemented or usable. Merchant-facing availability claims remain subject to actual implementation, applicability and readiness.

### 7. Publication authority

Catalogue publication SHALL be a platform-controlled Commercial operation.

The publication caller SHALL have a trusted execution principal and explicit platform-authorisation for Commercial catalogue publication. Merchant Controller status, merchant staff privileges, subscription level and provider credentials SHALL NOT independently confer that authority.

The operation SHALL require approval provenance for the exact manifest. Approval of this amendment’s publication mechanism SHALL NOT substitute for approval of a concrete manifest.

Authentication and credential mechanisms remain governed by applicable security authority. Commercial owns validation of the manifest and the publication effect.

No public merchant publication endpoint or merchant-facing catalogue editor is introduced.

### 8. Publication operation

The operation `PublishStandardCommercialCatalogue` SHALL accept:

```text
logical publication request identity
exact manifest
expected predecessor catalogue identity
    or explicit NO_PREDECESSOR
approval provenance
trusted execution context
```

Before committing, Commercial SHALL validate:

- publication authority;
- exact approval affinity;
- definition and binding completeness;
- identity uniqueness;
- immutable-content consistency;
- plan-level cardinality;
- monotonic grant sets;
- conformity with accepted allocation rules;
- the expected predecessor.

Success SHALL atomically establish:

```text
immutable manifest evidence
+ exact plan revisions and binding evidence
+ publication request affinity
+ predecessor relationship
+ authoritative publication instant
+ new catalogue head
```

No partial generation may become resolvable.

The operation SHALL return the committed catalogue identity, plan revision identities and authoritative publication instant.

No merchant account, configuration, agreement, Standing Free baseline or provider state is mutated by publication.

### 9. Initial effective start and succession

The first successful publication establishes the initial catalogue start, `P0`.

`P0` SHALL be the authoritative UTC instant assigned to the successful Commercial publication within its consistency boundary. It SHALL be retained unchanged on replay.

It SHALL NOT be inferred from:

- deployment or installation time;
- a source-file timestamp;
- application startup;
- the first merchant login;
- the first background-worker execution;
- a fabricated epoch.

Each subsequent publication SHALL have an authoritative instant strictly later than its predecessor.

Publication is effective from that instant, inclusively. This initial policy permits neither caller-selected backdating nor scheduled future effectiveness.

For consecutive publications at `Pi` and `Pi+1`, historical selection uses:

```text
Pi ≤ selectionInstant < Pi+1
```

The latest generation has no committed successor boundary.

The predecessor’s immutable content is not edited. Its effective end is derived from the next committed publication.

Publication and effective-time resolution SHALL be coordinated so that a later publication cannot change a previously successful selection for an already-historical instant. If the implementation cannot preserve this invariant at its supported time precision, publication SHALL fail rather than backdate or silently reinterpret history.

### 10. Historical resolution

Commercial SHALL provide exact-generation resolution and effective-time resolution.

**Exact-generation resolution** accepts an exact catalogue identity and returns that generation’s retained immutable evidence. It SHALL NOT substitute the current generation.

**Effective-time resolution** accepts an authoritative UTC selection instant and returns the unique published generation governing that instant, including its exact plan revisions and grant snapshots.

A future selection instant SHALL not be resolved speculatively under this policy.

The following outcomes SHALL remain distinguishable:

| Condition | Outcome |
|---|---|
| A unique governing generation exists | Exact generation and snapshots |
| No catalogue has been published | Catalogue not established |
| Instant precedes the initial publication | Historical instant not covered |
| Instant is in the future | Future selection unsupported |
| Required retained evidence is unavailable | Technical resolution failure |
| Evidence is corrupt, conflicting or non-unique | Catalogue integrity failure |

None of these failure outcomes SHALL silently select the latest generation or return an invented empty FREE plan.

### 11. Standing Free and pre-existing accounts

Standing Free SHALL continue to use the immutable Merchant Account establishment instant governed by MS-PROT-071 v1.2 and MS-PROT-056 v1.6.

Delayed execution SHALL resolve the catalogue effective at that establishment instant, not at worker execution time.

When a baseline already exists, recovery SHALL preserve and validate the committed baseline before requiring a fresh catalogue lookup. Catalogue unavailability SHALL NOT erase an already-committed baseline or cause rebinding.

This amendment introduces no backfill for an establishment instant preceding `P0`.

Production rollout SHALL establish the initial catalogue before enabling the ordinary new-account path that depends on that catalogue. This sequencing requirement does not add a Commercial row to the Merchant Account transaction.

If an uncovered account already exists, the affected Standing Free path SHALL stop with the explicit coverage failure. It SHALL NOT fabricate history, change establishment time or delete the account. A migration policy would require separate accepted authority.

### 12. Idempotency, concurrency and failure

A successfully committed publication request identity SHALL remain bound to its exact input and result.

An authorised retry with the same identity and identical input SHALL return the original result, including the original publication instant.

Reuse of that committed identity with different input SHALL be rejected as an idempotency conflict.

A failed, uncommitted attempt establishes no catalogue generation.

Concurrent different requests naming the same expected predecessor SHALL NOT both append as its immediate successor. The losing request SHALL receive a predecessor conflict; the system SHALL NOT silently retarget it to the new head.

If acknowledgement is lost after commit, recovery SHALL inspect the committed request result or retry the identical request. It SHALL NOT generate another publication merely because success was not observed.

Validation rejection, authorisation rejection, predecessor conflict, identity conflict, integrity failure and temporary technical failure SHALL remain distinguishable.

No distributed transaction with Merchant Account, Payment, capability owners or providers is required.

### 13. Retention, recovery and runtime boundaries

Commercial SHALL retain enough immutable evidence to reproduce every published generation while a retained commercial source, historical interpretation obligation or recovery obligation refers to it.

A deployment SHALL NOT reconstruct an old generation by relabelling current definitions.

Corruption or unavailable historical evidence SHALL fail the affected resolution path explicitly.

Existing independently interpretable commercial sources may continue under their own accepted authority. Catalogue-storage failure SHALL NOT automatically become whole-platform failure.

Binding and grant caches SHALL preserve exact revision affinity. A cache SHALL NOT substitute for required authoritative revalidation.

Commercial permission remains separate from Semantic Applicability, Actor Authorisation, Operational Eligibility, Resource Protection Admission, Provider Readiness and Exposure under composite MS-PROT-062.

### 14. Financial and analytical allocation preservation

Manifest admission SHALL preserve MS-PROT-056 v1.7 and v1.8, including:

- BUSINESS allocation of the accepted initial financial, operational and customer-return analytical portfolios;
- GROWTH allocation of the accepted campaign measures;
- no separate higher-tier requirement for supporting analytical presentation;
- no conversion of monitoring vocabulary into scheduling or execution authority;
- no activation of taxation, invoicing, quotation, forecasting or agentic CRM reservations through a feature label.

This amendment adds no analytical family or business operation.

### 15. Fundamental Vision Conformance and trade-offs

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY.**

The merchant continues to choose understandable commercial value levels. Catalogue identities, temporal boundaries and binding evidence remain platform responsibilities.

The internal complexity is justified by reproducible permission, preserved history and safe recovery. No new merchant configuration or ordinary-staff training is required.

Rejected alternatives are:

- mutable live inheritance between tiers;
- deployment time as commercial history;
- latest-catalogue fallback for historical lookup;
- wildcard capability grants;
- publication through provider payment state;
- editing old catalogue content to repair new packaging.

The chosen append-only model makes corrections require a new approved generation. Scheduled publication and historical migration are excluded to keep the initial lifecycle bounded.

### 16. Falsification and required implementation proof

The design has been examined against these counterexamples:

| Challenge | Required result |
|---|---|
| Information-only merchant | No fabricated Booking, Order or Payment requirement |
| Salon upgrades from BUSINESS to GROWTH | Explicit monotonic grants; no configuration rewrite |
| Worker handles an old account event after catalogue replacement | Original establishment-time generation is selected |
| Publication commits but its acknowledgement is lost | Retry returns the same generation and instant |
| Two publishers use the same predecessor | One successor commits; the other conflicts |
| Reused entitlement identity changes its purpose | Publication rejected |
| One binding points to a missing target | No partial catalogue publication |
| Account predates the initial catalogue | Explicit coverage failure, not synthetic history |
| Historical catalogue store is unavailable but baseline exists | Committed baseline remains recoverable |
| Staff member has a paid subscription but lacks observation authority | Paid entitlement does not expose protected information |
| A new deployment changes registration code | Old published meaning remains independently reproducible |
| Publication races with historical selection | A successful historical selection is not retrospectively changed |

These are design-review scenarios, not executed implementation tests.

Downstream implementation SHALL prove the listed outcomes, exact transition-boundary selection, grant-set immutability, tenant-safe baseline materialisation and recovery after process failure. Verification remains governed by IMPLEMENTATION-RULES and the applicable implementation programme.

### 17. Deferred decisions and readiness

`MS-PROT-056-V17-DQ-002` is resolved **for catalogue-publication and temporal-selection policy** by this amendment.

That resolution does not mean an initial production catalogue exists.

`MS-PROT-056-V17-DQ-001` remains OPEN until the complete concrete manifest—including exact entitlement identities, owner-qualified target bindings and all three explicit grant sets—has been presented and approved.

DQ-003 and DQ-004 remain OPEN.

No production catalogue, scheduled worker or C3 completion is authorised by this amendment.

### 18. Amendment effect

This amendment makes catalogue binding, publication and historical selection explicit.

It does not change accepted tier allocations, trial timing, agreement establishment, paid-plan transition timing, residual-access ownership or existing Standing Free historical affinity.

Physical storage, identifier encoding and concurrency mechanisms remain implementation choices only insofar as they preserve every stated invariant.
