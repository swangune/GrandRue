# Main Street Executable Architecture Recommendation Falsification — 25 August 2026

**Status:** FALSIFICATION COMPLETE — REVISED RECOMMENDATIONS AWAIT MANUAL APPROVAL — **NOT IMPLEMENTATION AUTHORITY**  
**Authority class:** Design-review / falsification evidence  
**Governed by:** `designs/DESIGN-RULES.md`, `designs/DOCUMENT-GOVERNANCE.md`  
**Falsifies:** EAC-001, EAC-002 and EAC-003 recommendations from `docs/development/executable-architecture-completeness-review-2026-08-25.md`  
**Branch:** `development`  
**Purpose:** Attempt to disprove or materially weaken the three executable-architecture recommendations before any manual approval or formalisation.

This document is evidence only. It does not create accepted semantics, reserve a permanent MS-PROT/TAS/ADR identifier, authorise implementation, or modify an accepted authority. The recommendations below become implementation authority only after the applicable `DESIGN-RULES.md` lifecycle, explicit manual approval, formalisation, navigation updates and corpus conformance.

---

# 1. Falsification objective

`DESIGN-RULES.md` requires falsification to seek credible failure conditions rather than prove that a preferred design can work.

The test applied here is:

> **Can the recommendation preserve Main Street's accepted ownership, release affinity, historical commitments, failure semantics, business-type neutrality and simplicity when subjected to restart, deployment, partial failure, cross-capability execution, stale projections, privacy revocation and materially different merchant domains?**

A recommendation that fails a credible case is revised or rejected. The original wording is not defended merely because its general direction appears useful.

---

# 2. Accepted constraints used as falsification boundaries

The following accepted constraints are treated as non-negotiable:

```text
MS-PROT-054
    published Semantic Registry Release is immutable
    application deployment != semantic release
    compatibility is reference-scoped, not one release-global boolean
    historical execution/interpretation support may be required
    platform safety may restrict obsolete unsafe semantics

MS-PROT-040 / MS-PROT-022
    one Configuration Revision is pinned to one exact release
    one invocation binds to one exact applicable RCP
    historical RCP may be retained OR reconstructed from immutable inputs

ADR-010
    Semantic/Surface/Fulfilment snapshots form one coherent immutable
    SemanticReleaseAssembly when such an assembly is required

MS-PROT-027
    projections are derived and non-authoritative
    projection lag may be valid where safe
    stale projection data cannot authorise mutation

MS-PROT-049
    surface inclusion != interaction availability != backend authority
    required residual management surfaces must not disappear merely because
    another runtime dependency is degraded

MS-PROT-070
    stale reads are allowed only under the owning read contract
    privacy/security/invariants outrank apparent availability
    explicit bounded unavailability is preferable to false certainty
```

The falsification therefore does not reopen those accepted decisions.

---

# 3. Representative hostile scenario

All three proposals are tested against the same difficult but valid runtime state:

```text
Merchant A
    active Configuration C18 @ R8
    Booking disabled for new activity
    outstanding Booking B100 retains R8 affinity

Merchant B
    active Configuration C27 @ R9
    new Booking activity allowed

Deployment D12
    newer than the deployment that first served R8

Runtime conditions
    rolling restart / multiple application instances
    one instance temporarily lacks a required static release definition
    one provider is degraded
    one projection consumer is lagging
    one public Exposure permission is revoked during projection lag
    one durable R8 background instruction wakes after deployment
```

A valid architecture must preserve Merchant A's R8 obligation, Merchant B's R9 new activity, current privacy/security authority and non-authoritative projection semantics without requiring a separate full Main Street deployment for every historical release.

---

# 4. EAC-001 — attack the original recommendation

## 4.1 Original recommendation under test

The completeness review recommended an immutable published semantic-release artefact catalogue, exact bootstrap by release identifier, no `latest` substitution, and retention while active configurations, historical commitments, interpretation obligations or recovery requirements still need the release.

The core intention is sound, but the original wording overreaches in several cases.

## 4.2 Counterexample A — persisted exact RCP makes full historical release material unnecessary for some residual execution

Assume:

```text
B100 created under C18 @ R8
exact immutable RCP(C18,R8) retained
R8-compatible Booking execution support retained
```

The process restarts.

For a residual operation whose accepted semantics are completely represented by the retained RCP plus compatible capability-owned execution support, Main Street may not need to reconstruct the *entire* R8 Semantic Registry Release or its complete Surface/Fulfilment registries merely to manage B100.

Therefore this original claim is too strong:

```text
outstanding historical commitment
    => retain/load complete release artefact
```

MS-PROT-040 explicitly permits retention of the exact historical RCP rather than mandatory reconstruction from the complete source registry.

**Original recommendation result: FAIL / OVER-CONSTRAINED.**

## 4.3 Counterexample B — one physical artefact is not semantically required

A valid immutable release representation could be:

```text
one packaged resource
multiple immutable files
content-addressed objects
immutable database records
build-produced embedded descriptors
another reproducible representation
```

Requiring literally one file/blob/package would elevate packaging topology into semantic architecture.

What matters is one **coherent immutable published definition set**, not one physical object.

**Original recommendation result: FAIL / WORDING TOO PHYSICAL.**

## 4.4 Counterexample C — startup need not eagerly load every historical release

Suppose Main Street has published R1..R40 but the current process serves:

```text
active merchants: R38, R39, R40
residual obligations: R36
historical records requiring read interpretation: R12
```

It is unnecessary to require eager materialisation of R1..R40 into every process merely because they exist.

Exact lazy/on-demand resolution can preserve semantics while using less memory and startup time.

**Original recommendation result: FAIL if interpreted as eager-global bootstrap.**

## 4.5 Counterexample D — partial/corrupt publication

Suppose publication writes:

```text
Semantic snapshot R10     complete
Surface snapshot R10      complete
Fulfilment snapshot R10   missing/corrupt
```

ADR-010 rejects a mismatched in-process assembly, but publication itself still needs to prevent a partially published release from being considered valid.

The original recommendation said immutable/reproducible but did not explicitly require **coherent publication completion and integrity evidence**.

**Original recommendation result: INCOMPLETE.**

## 4.6 Counterexample E — defective published release

R10 is published and later found to contain a semantic defect.

Immutability means Main Street cannot silently edit R10 in place.

Valid responses may include:

```text
publish corrected R11
prohibit R10 for new configuration/new activity
migrate compatible merchants
retain narrowly required R10 evidence/support for safe residual/history
```

The release representation must therefore survive withdrawal from new use without confusing `published` with `currently permitted for every support dimension`.

**Original recommendation requires refinement.**

## 4.7 Counterexample F — recovery bundle must not become a second semantic representation

MS-TAS-RECOVERY-001 contains a Recovery Release Bundle. If DR captures a differently reconstructed form of R8 than ordinary runtime publication, Main Street could have two competing representations of `R8`.

The original recommendation correctly rejected this in principle, but falsification strengthens the requirement: recovery must reference, contain or verifiably reproduce the same immutable published definition set/evidence, not independently reinterpret it.

**Core recommendation survives.**

## 4.8 Counterexample G — release definitions must not contain merchant operational authority

If an engineer solves publication by serialising Merchant Configuration, provider credentials, live Provider Readiness or merchant operational data into a semantic-release package, the package becomes a cross-authority snapshot.

That violates the static/dynamic and ownership boundaries.

**Original recommendation needs an explicit exclusion.**

---

# 5. EAC-001 revised recommendation

The original recommendation is narrowed to the following:

> **The Semantic Registry authority shall preserve one coherent immutable published definition set for each published Semantic Registry Release. That definition set is a logical release artefact, not a mandated physical file. It must be sufficient to reproduce or verify the registered static definitions required for the support activity that claims to resolve that release. Runtime resolution is exact by release identity and may be eager or lazy. A process must never recreate an old release by relabelling current definitions.**

Additional constraints:

1. Publication of a release definition set must become usable only as a coherent completed release; partial constituent publication must not create a valid release.
2. Integrity/provenance must be sufficient to detect substitution or corruption of the published definition set.
3. Static release content excludes Merchant Configuration, credentials, Provider Readiness and mutable operational state.
4. Exact RCP retention may satisfy some historical execution needs without requiring full release materialisation on every request.
5. Full release-definition retention is required only while an accepted support activity actually requires that definition set or reconstruction from it; existing-commitment support may alternatively be satisfied by retained immutable RCP/evidence plus compatible executable support where accepted authority permits.
6. A release withdrawn from new configuration/new business activity remains immutable; it is not edited in place.
7. Missing/corrupt required release material fails the affected resolution path rather than substituting `latest`.
8. Ordinary runtime may load only required releases; no all-release eager bootstrap is mandated.
9. Recovery representation must reference, contain or verifiably reproduce the same immutable release definition set/evidence rather than create DR-only semantic meaning.
10. Physical storage, packaging and distribution technology remain downstream implementation decisions.

### Authority-layer finding

This revised recommendation does **not** need a new business-semantic model. MS-PROT-054 already owns release immutability and support dimensions. The unresolved choice is the implementation architecture that realises exact published definition-set materialisation/bootstrap beneath MS-PROT-054 and ADR-010.

**Recommended formalisation layer if manually approved:** narrow ADR/TAS-level implementation architecture, not a new capability semantic model.

---

# 6. Re-falsification of revised EAC-001

| Scenario | Result |
|---|---|
| Restart with persisted exact RCP and no need to rebuild entire R8 | PASS — full R8 need not be eagerly materialised |
| Restart requiring compilation/reconstruction against R8 | PASS — exact R8 definition set must resolve |
| Multiple physical files for one release | PASS — logical coherence, not one-file topology |
| Partial constituent publication | PASS — release cannot become usable until coherent |
| Corrupt/substituted artefact | PASS — integrity/provenance required |
| R10 withdrawn from new use but old residual evidence remains | PASS — support dimensions remain separate |
| Process only needs R38/R39/R40 | PASS — eager all-release loading not required |
| Recovery of R8 | PASS — DR cannot invent a competing R8 representation |
| Provider/merchant operational state changes | PASS — excluded from static release definition set |
| Information publisher with no Booking | PASS — no Booking-specific release machinery required |

No surviving case requires a mutable global `current semantics` catalogue or dedicated semantic-registry microservice.

### EAC-001 falsification verdict

```text
ORIGINAL RECOMMENDATION: REVISE
REVISED RECOMMENDATION: ACCEPT FOR MANUAL-APPROVAL REVIEW
```

`ACCEPT` here is a design recommendation only, not formal authority.

---

# 7. EAC-002 — attack the original recommendation

## 7.1 Original recommendation under test

The completeness review recommended explicit executable semantic-support compatibility, allowing one current implementation to serve multiple releases only where compatibility is established, with narrow historical adapters when behaviour differs.

The direction survives, but a coarse release→handler interpretation fails.

## 7.2 Counterexample A — release-global compatibility is already rejected by MS-PROT-054

Suppose R9 changes only Payment while Booking semantics remain identical to R8.

A model such as:

```text
CurrentBookingHandler supports R8 = true
CurrentBookingHandler supports R9 = true
```

is not necessarily wrong, but making the *release* the fundamental compatibility unit is too coarse. MS-PROT-054 explicitly requires compatibility to be reference-scoped because merchants use different semantic subsets.

Executable support must therefore be bound to the **affected semantic execution contract/coherent operation scope**, not one global release boolean.

**Original recommendation result: FAIL if implemented release-globally.**

## 7.3 Counterexample B — one handler can legitimately support several semantic versions through the exact RCP

Suppose `booking.confirm` differs between R8 and R9 only in registered configuration/effect data fully represented in the bound executable model, while the Java handler is deliberately generic and interprets that exact model.

Requiring a separate historical adapter merely because release identifiers differ would duplicate implementation without semantic need.

Valid:

```text
one implementation
    + exact bound semantic contract/RCP
    + conformance evidence
    -> supports several semantic contexts
```

**Original recommendation survives only if it permits data-driven multi-release support.**

## 7.4 Counterexample C — same operation identifier, materially different business behaviour

Suppose both releases contain:

```text
booking / confirm-booking
```

but R9 adds a materially different accepted effect/precondition.

Routing by:

```text
operation identifier
Java class name
Spring bean name
```

cannot prove R8 compatibility.

**Core recommendation survives.**

## 7.5 Counterexample D — cross-capability operation support

A Booking confirmation may require coordinated accepted effects in Booking, Appointment and Allocation contexts.

If Booking's handler claims R8 support but a required Allocation participant only implements an incompatible later contract, the overall operation is not safely executable.

Executable-support validation must cover the **required participant/effect graph** for the invoked contract, not merely the first capability handler.

**Original recommendation incomplete.**

## 7.6 Counterexample E — rolling deployment

Two instances serve traffic:

```text
Instance A supports required R8 residual execution + R9
Instance B supports R9 only
```

If the load balancer sends B100's R8 operation to Instance B, exact RCP binding alone does not help.

A serving instance/process must not accept an execution context for which required executable support is absent. The architecture may later use compatibility-aware routing, but the simpler initial rule may require every serving instance in one homogeneous deployment pool to support every semantic execution context that pool may receive.

**Original recommendation needs deployment-admission consequence.**

## 7.7 Counterexample F — durable work wakes after deployment

An R8 durable instruction is created, the application deploys D12, then the instruction wakes.

If the work item loses the applicable semantic/configuration affinity and simply invokes the latest handler, deployment becomes implicit migration authority.

Existing durable-work authority already requires current revalidation; executable-support resolution must additionally preserve/recover the semantic context required by the owning operation.

**Core recommendation survives.**

## 7.8 Counterexample G — security/platform invariant makes old adapter unsafe

Suppose a historical R8 execution path has a now-prohibited security behaviour.

A simplistic rule:

```text
outstanding R8 commitment
    => execute old R8 adapter forever
```

would conflict with MS-PROT-054's platform-required safety authority.

Historical support is an obligation to honour commitments **safely**, not a command to retain unsafe executable code unchanged forever. Valid handling may require intent-preserving migration, bounded restriction, remediation or manual intervention.

**Original recommendation needs explicit safety precedence.**

## 7.9 Counterexample H — implementation declaration is not semantic compatibility evidence

An annotation such as:

```text
@Supports("R8")
```

can lie.

Likewise passing unit tests do not turn application code into semantic authority. A support declaration is implementation/conformance evidence that must be validated against accepted semantic contracts; it does not itself create semantic equivalence.

**Original recommendation needs an authority distinction.**

---

# 8. EAC-002 revised recommendation

> **Before executing a configuration-dependent operation, Main Street must bind both (a) the exact accepted semantic/RCP context and (b) an executable implementation path whose declared and verified support covers the affected semantic execution contract and all required participant/effect contracts. Deployment/version/class identity does not establish support.**

Additional constraints:

1. Executable support is reference-/contract-scoped rather than one coarse `releaseCompatible=true` flag.
2. One implementation may support multiple semantic contexts where the relevant behaviour is equivalent or is correctly parameterised by exact bound semantic/RCP data and conformance evidence proves the contract.
3. A material semantic difference requires an implementation/adaptation path capable of the older contract while that path remains safely required.
4. Required cross-capability participants/effects must also have compatible executable support; support cannot stop at the first handler.
5. A serving process/instance must not execute a semantic context for which required support is absent. Missing support fails closed for the affected operation.
6. Deployment admission/readiness must prevent foreseeable required active/residual execution contexts from being routed to an incapable process, unless a separately accepted compatibility-aware routing architecture exists.
7. Durable/background execution must preserve or recover the exact semantic/configuration affinity required by the owning operation before executable-support resolution.
8. Support declarations/registrations are implementation-conformance evidence; they do not create semantic equivalence or migration authority.
9. Java class names, bean names, package names and deployment versions are not semantic compatibility evidence.
10. Platform/security/legal/integrity authority may require safe migration/restriction instead of indefinitely executing an unsafe historical implementation.
11. Full application deployment cloning per semantic release is not required; support should remain at the smallest coherent execution scope practical.
12. Exact implementation representation—registry, factory, manifest, generated binding table or equivalent—remains downstream until this contract is approved.

### Authority-layer finding

MS-PROT-054 already establishes semantic compatibility, historical-support obligations and the rule that deployment is not migration. EAC-002 concerns how the application proves and composes executable conformance to those semantics.

**Recommended formalisation layer if manually approved:** narrow ADR-level runtime/deployment implementation architecture subordinate to MS-PROT-054, MS-PROT-040, MS-PROT-023 and cross-capability execution authorities.

---

# 9. Re-falsification of revised EAC-002

| Scenario | Result |
|---|---|
| Payment changes but Booking does not | PASS — support is reference-scoped |
| Generic handler correctly interprets R8/R9 RCP data | PASS — one implementation may support both |
| Same operation ID but changed semantics | PASS — identifier/class alone is insufficient |
| Booking requires incompatible Allocation participant | PASS — participant/effect support graph must validate |
| Rolling deployment contains R9-only instance | PASS — incapable instance cannot serve R8 context |
| R8 durable work wakes after D12 deployment | PASS — semantic affinity is recovered before support binding |
| R8 adapter becomes unsafe | PASS — platform safety may force migration/restriction/remediation |
| Information-only merchant uses no Booking | PASS — no irrelevant Booking compatibility requirement |
| Full old application binary is the only safe exceptional path | PASS — not prohibited, merely not universal/default |
| Unknown support declaration | PASS — absence/unproven support fails closed |

### EAC-002 falsification verdict

```text
ORIGINAL RECOMMENDATION: REVISE
REVISED RECOMMENDATION: ACCEPT FOR MANUAL-APPROVAL REVIEW
```

---

# 10. EAC-003 — attack the original recommendation

## 10.1 Original recommendation under test

The completeness review recommended an explicit projection/read-model contract describing audience/use case, authoritative sources, permitted freshness/staleness, failure/unavailability behaviour, rebuildability and provenance.

The direction survives, but several common implementations would still be wrong.

## 10.2 Counterexample A — wall-clock TTL does not prove freshness

A projection updated one second ago may still be missing an earlier event or source version.

Conversely, a merchant description last updated one hour ago may still be semantically current.

Therefore:

```text
freshness = age < N seconds
```

cannot be the universal contract.

Freshness must be a projection-specific **determinable predicate/evidence** such as an as-of source version, watermark, source observation, bounded age where age is actually sufficient, or another accepted criterion.

**Original recommendation incomplete if interpreted as TTL-centric.**

## 10.3 Counterexample B — composite dashboard has mixed freshness

A merchant dashboard can combine:

```text
merchant profile       current enough
booking count          20 seconds behind
payment state          unavailable
calendar availability  current
```

A single page-wide `STALE=true` loses useful distinctions and may cause either over-blocking or unsafe display.

Projection contracts must remain scoped to the smallest material read model/component rather than creating one global page freshness state.

**Original recommendation requires scoping refinement.**

## 10.4 Counterexample C — mandatory residual workspace with unavailable projection

Booking is disabled for new activity but seven Booking-owned obligations remain. MS-PROT-049 requires minimum residual management surface.

If the Booking projection is temporarily unavailable, this is invalid:

```text
projection unavailable
    -> remove Bookings workspace
```

The workspace may need to remain present with bounded unavailable/degraded content even though the projection itself cannot currently be served.

Projection availability therefore cannot override higher-priority residual-surface inclusion authority.

**Original recommendation needs explicit precedence.**

## 10.5 Counterexample D — public privacy revocation during cache lag

A public media/profile projection is cached. A governing Exposure/personal-data-use permission is revoked while the projection consumer is lagging.

Serving the stale cached item merely because it is within an ordinary TTL would violate MS-PROT-070/MS-PROT-053/Exposure authority.

A projection contract must specify when current security/privacy/Exposure evidence is required independently of ordinary content freshness.

**Core recommendation survives and is strengthened.**

## 10.6 Counterexample E — projection need not be asynchronously materialised

A projection is a read representation, not necessarily a read table/cache.

A query handler may synchronously derive a projection from authoritative repositories through accepted application/query boundaries.

Therefore EAC-003 must not imply:

```text
projection contract
    => materialized projection store
```

Direct UI/controller SQL access to capability write tables remains invalid, but a legitimate application query path may derive a projection directly from authoritative sources.

**Original recommendation needs an explicit storage-neutral rule.**

## 10.7 Counterexample F — command succeeds before projection catches up

`ConfirmBooking` commits successfully and returns an authoritative operation result. The Bookings projection remains 500 ms behind.

The UI may legitimately show the command acknowledgement/result while the normal projection converges, provided that acknowledgement is not silently turned into a replacement projection authority for unrelated reads.

A projection contract must not force command responses to wait for asynchronous projection convergence unless the owning use case requires it.

**Original recommendation must preserve command/result separation.**

## 10.8 Counterexample G — some projections are not fully rebuildable from current state

An analytical/historical projection may depend on retained events/evidence or external observations that are not reproducible from the current aggregate snapshot alone.

Therefore `projection = always rebuildable` is false.

The valid rule is:

```text
if contract claims rebuildability
    -> authoritative/reproducible source evidence must actually be sufficient
```

otherwise recovery semantics must state the limitation.

**Original recommendation already allowed this, survives.**

## 10.9 Counterexample H — stale availability can be safe to display but never safe to allocate from

Public motel projection says `rooms available` based on bounded-stale data.

It may be acceptable as an informational summary if the owning projection contract permits it, while `AllocateRoom` still revalidates authoritative capacity.

This proves that:

```text
safe stale read
    != write authority
```

and that strict `never show stale availability` would be unnecessarily restrictive.

**Core recommendation survives.**

## 10.10 Counterexample I — no universal availability enum is yet justified

Different projections need materially different evidence:

```text
CURRENT
STALE_WITHIN_CONTRACT
UNKNOWN
PARTIALLY_AVAILABLE
REBUILDING
WITHHELD_FOR_EXPOSURE
```

Freezing one universal enum now risks mixing technical freshness, privacy withholding, partial source availability and presentation language.

The architecture needs a contract/predicate boundary before it needs a universal state algebra.

**Original recommendation correctly avoided a DSL, but dedicated design must also avoid premature enum freezing.**

---

# 11. EAC-003 revised recommendation

> **Each projection/read model whose freshness, caching, asynchronous materialisation, cross-boundary reference or failure behaviour can materially affect a user-facing/API decision must have one explicit projection contract with one declared projection owner. The contract determines what evidence is sufficient to serve that projection in the requested context and what bounded consequence follows when that evidence is absent. It does not own the underlying business facts, Exposure authority or mutation authority.**

Additional constraints:

1. Freshness is a projection-specific determinable predicate/evidence, not a mandatory global TTL.
2. Wall-clock age may be one valid input only where the projection contract proves age is sufficient for that use case.
3. Composite surfaces preserve material per-projection/subprojection freshness rather than collapsing everything into a page-wide boolean.
4. Projection unavailability cannot override an accepted higher-priority Surface inclusion invariant such as mandatory residual management; presentation may retain the workspace while its data is unavailable/degraded.
5. Public/customer cached data must still obey current security/privacy/Exposure requirements; revocation may require withholding even otherwise-tolerable stale content.
6. Projection contracts are storage-neutral: synchronous derivation, PostgreSQL read model, cache, search index or another representation may conform.
7. UI/transport code must not bypass application/query ownership and read authoritative write tables directly merely because a projection is unavailable.
8. Authoritative commands continue to revalidate authoritative state. A command result/receipt may be shown before projection convergence without becoming a general projection authority.
9. Rebuildability must be explicitly supportable by retained authoritative/provenance evidence where claimed; not every projection is assumed universally rebuildable.
10. Where serving stale/degraded data would materially mislead the consumer, the projection/API contract must convey sufficient freshness/degradation provenance for presentation to communicate the distinction.
11. Stable projection identity/reference is required only when another registered contract must refer to that projection; no generic projection registry/DSL is implied.
12. No universal projection availability enum or global cache policy is frozen by this recommendation.
13. Source business facts retain their existing capability owners; a cross-capability projection may have one declared read/projection owner without acquiring mutation authority over those sources.

### Authority-layer finding

This recommendation changes the normative meaning of when a derived read representation may be served and how projection availability composes with Surface/Exposure. That is broader than cache/storage implementation mechanics.

**Recommended formalisation layer if manually approved:** a narrow MS-PROT-027 projection/read-contract amendment, composed with MS-PROT-049 and MS-PROT-070. Storage/query technology remains downstream.

---

# 12. Re-falsification of revised EAC-003

| Scenario | Result |
|---|---|
| Static merchant description one hour old but unchanged | PASS — contract may permit it |
| Payment projection updated recently but missing source sequence | PASS — age alone does not prove freshness |
| Dashboard combines fresh and stale subviews | PASS — per-projection evidence preserved |
| Residual Bookings workspace projection unavailable | PASS — workspace need not disappear |
| Public media permission revoked during cache lag | PASS — Exposure/privacy override stale availability |
| Synchronous query-derived projection | PASS — materialised storage not required |
| Command commits before async projection catches up | PASS — result and projection remain distinct |
| Motel availability summary is bounded-stale | PASS — may be displayed if contract permits; allocation still revalidates |
| Projection not reconstructable from current aggregate alone | PASS — rebuildability must be claimed/proven, not assumed |
| Information publisher uses long-lived public content | PASS — no commerce-specific freshness model imposed |
| Customer-specific secure link revoked | PASS — current authority may require withholding regardless of cache age |

### EAC-003 falsification verdict

```text
ORIGINAL RECOMMENDATION: REVISE
REVISED RECOMMENDATION: ACCEPT FOR MANUAL-APPROVAL REVIEW
```

---

# 13. Cross-recommendation attacks

## 13.1 Can EAC-001 and EAC-002 be collapsed into one authority?

Tempting model:

```text
SemanticReleasePackage
    contains definitions + executable handlers
```

**Rejected.**

A semantic release is not an application deployment. Coupling code artefacts directly into semantic identity would recreate the equivalence MS-PROT-054 rejects.

Required separation remains:

```text
immutable semantic definition context
        !=
executable implementation support
```

They may be packaged together operationally in a deployment/recovery bundle where useful, but neither acquires the other's identity or authority.

## 13.2 Can EAC-002 and EAC-003 be collapsed into one runtime availability model?

Tempting model:

```text
runtime component available = true/false
```

**Rejected.**

Executable support answers whether Main Street can safely perform an operation under a semantic context. Projection contract answers whether a derived read can be served and with what freshness/degradation semantics. These have different owners, authority and failure consequences.

## 13.3 Can one global version/support/freshness registry solve all three?

**Rejected.**

It would combine:

```text
semantic release identity
implementation conformance
read-model freshness
```

into a generic registry whose abstractions have not been justified. The three contracts should compose through exact typed references, not be collapsed.

---

# 14. Cross-domain falsification

## 14.1 Information publisher

```text
Publication + Opportunity + Enquiry
```

- EAC-001 must not require commerce-specific release data.
- EAC-002 may require only Publication/Enquiry execution support actually referenced.
- EAC-003 permits long-lived content projection staleness where safe while deadline-sensitive Opportunity data may require stricter evidence.

**PASS.**

## 14.2 Online consultant

```text
Scheduling + Booking + Appointment + optional external calendar
```

- historical appointment/booking commitments test exact semantic support;
- provider readiness remains separate from executable semantic compatibility;
- calendar projections can degrade independently from business commitment truth.

**PASS.**

## 14.3 Motel

```text
Booking + Allocation + room/resource capacity
```

- old reservation commitments require exact historical execution support;
- cross-capability participant support is required;
- stale room availability may be informational but cannot authorise allocation.

**PASS.**

## 14.4 Retailer

```text
Product + Inventory + Order + Payment
```

- Product description may tolerate greater projection staleness;
- Inventory/payment require stronger freshness/current-authority checks;
- changing Payment semantics must not force unrelated Product handler duplication.

**PASS.**

## 14.5 Hybrid merchant

Mixed capabilities remain governed by referenced semantic scope rather than a business-category release/handler/projection template.

**PASS.**

No revised recommendation requires `if salon`, `if motel`, `if retailer`, `if consultant` or `if publisher` branching.

---

# 15. Upgrade, downgrade and historical-support falsification

## New release published

```text
R8 -> R9
```

Publishing R9 does not mutate R8 definition evidence or existing C18/R8 meaning.

**PASS.**

## Merchant migrates active configuration

```text
C18@R8 -> C19@R9
```

Existing B100 may retain R8 affinity. Revised EAC-002 still resolves R8 execution support where safely required.

**PASS.**

## R8 disallowed for new activity

R8 definition/support may remain available only for residual/history dimensions. Revised EAC-001 does not equate retained evidence with permission for new use.

**PASS.**

## Application deployment rolls backward

Deployment rollback does not itself change semantic release authority. The rolled-back process must still pass executable-support admission for the semantic contexts it may receive.

**PASS.**

## Semantic support removed accidentally

An instance lacking required support must not silently invoke another semantic version. Affected execution fails closed / deployment admission fails according to the implementation architecture.

**PASS.**

---

# 16. Concurrency, retry and partial-failure falsification

## Duplicate semantic release publication attempt

A second materially different definition set under the same published release identity must be rejected; immutable identity cannot be overwritten.

**PASS under revised EAC-001.**

## Two instances load same release through different physical caches

Both must resolve equivalent immutable published definition evidence. Cache locality does not create semantic variants.

**PASS.**

## Provider failure during R8 operation

Executable semantic compatibility does not imply Provider Readiness. Existing fulfilment/readiness/runtime authority still applies independently.

**PASS.**

## Projection consumer retries duplicate event

Projection idempotency/rebuild mechanics remain owned by the projection implementation; the freshness contract does not create business mutation authority.

**PASS.**

## Projection unavailable while command remains executable

Command availability is re-evaluated from authoritative runtime requirements; read-projection failure must not automatically redefine operation authority.

**PASS.**

---

# 17. Simplicity falsification

The revised recommendations were tested for speculative machinery.

Rejected as unnecessary:

```text
dedicated semantic-registry microservice
one deployment per semantic release
one universal handler-version matrix by full release
one generic release/execution/projection registry framework
universal projection DSL
one global projection TTL
one global page freshness status
semantic-aware service mesh/routing as initial requirement
full release eager loading in every process
```

The minimum surviving architecture is:

```text
EAC-001
coherent immutable published semantic-definition evidence
+ exact resolution when required

EAC-002
exact semantic/RCP binding
+ explicit executable support for affected execution contracts

EAC-003
projection-specific serviceability/freshness contract
+ source authority and Surface/Exposure separation
```

This remains compatible with the initial modular monolith.

---

# 18. Falsification outcome matrix

| Gap | Original recommendation | Main failure found | Revised result |
|---|---|---|---|
| EAC-001 | Immutable published release artefact retained/bootstrapped exactly | Over-required full release material for every historical obligation; physical/eager interpretation too strong; publication atomicity/integrity missing | **ACCEPT revised** |
| EAC-002 | Explicit executable support compatibility with historical adapters where required | Release-global support too coarse; transitive participants/deployment admission/safety precedence missing | **ACCEPT revised** |
| EAC-003 | Explicit projection freshness/availability contract | TTL/page-global interpretations unsafe; residual-surface precedence and synchronous projection path needed | **ACCEPT revised** |

No recommendation survived unchanged. None was falsified so completely that the underlying gap disappeared.

---

# 19. Recommended sequence after falsification

The sequence remains:

```text
EAC-001
Published semantic-definition materialisation/bootstrap architecture
        ↓
EAC-002
Executable semantic-support resolution/deployment compatibility
        ↓
EAC-003
Projection serviceability/freshness contract
        ↓
repeat end-to-end architecture falsification
        ↓
consider implementation resumption
```

The order still matters:

1. establish what exact static semantic definition evidence can be resolved;
2. establish what executable code may safely honour the bound semantic contract;
3. establish how resulting authoritative truth may be represented/served as a read projection.

---

# 20. DESIGN-RULES lifecycle status after this work

For the **revised** recommendations:

```text
PROPOSE                 complete
REVIEW                  complete against current accepted corpus
FALSIFICATION           complete, including revision + re-falsification
RECOMMENDATION          ACCEPT revised direction
MANUAL APPROVAL         NOT YET GIVEN
FORMALISATION           NOT STARTED
IMPLEMENTATION          NOT AUTHORISED
```

Because falsification materially narrowed all three recommendations, any eventual formal authority must use the **revised** contracts in this document rather than copying the earlier completeness-review wording verbatim.

---

# 21. Governance impact

This falsification requires only a work-queue status update:

- EAC-001, EAC-002 and EAC-003 remain promoted/unresolved;
- their original recommendations are superseded as recommendations by the revised forms in this evidence document;
- none becomes accepted authority without manual approval;
- `AUTHORITY-INDEX.md` must not index the revised answers yet;
- `CANONICAL-SEMANTIC-LEXICON.md` requires no change yet;
- `IMPLEMENTATION-RULES.md` requires no change;
- implementation remains paused.

---

# 22. Final falsification verdict

The attempt to disprove the three recommendations **did not justify abandoning the three gaps**, but it materially improved their boundaries.

The strongest corrections are:

```text
EAC-001
retain exact semantic evidence required by the support activity;
do not require full release materialisation for every historical commitment.

EAC-002
bind executable support to the affected semantic execution contract/participant graph;
do not use a coarse release-to-handler boolean.

EAC-003
freshness is evidence/predicate-specific and Surface/Exposure authority has precedence;
do not use a global TTL or hide mandatory residual surfaces on projection failure.
```

Accordingly:

> **Recommend manual approval of the revised EAC-001, EAC-002 and EAC-003 directions, not the original wording.**
