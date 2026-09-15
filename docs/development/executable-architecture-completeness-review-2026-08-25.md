# Main Street Executable Architecture Completeness Review — 25 August 2026

**Status:** DESIGN REVIEW COMPLETE — RECOMMENDATIONS AWAIT MANUAL APPROVAL — **NOT IMPLEMENTATION AUTHORITY**  
**Authority class:** Design-review evidence  
**Governed by:** `designs/DESIGN-RULES.md`, `designs/DOCUMENT-GOVERNANCE.md`  
**Branch:** `development`  
**Purpose:** Test whether accepted Main Street authority is complete enough to trace one merchant from registered semantics through release, configuration, runtime execution, projection and delivery without an implementer inventing material architectural rules.

This document is review evidence only. It does not create accepted semantics, reserve a permanent MS-PROT/TAS/ADR number, authorise implementation, or make any recommendation below authoritative before manual approval and formalisation through the governed design lifecycle.

---

## 1. Why this review exists

Recent implementation work repeatedly reached narrow but consequential architecture seams only after production work had begun. The pattern was useful for falsifying earlier architecture, but continuing indefinitely would make implementation the primary mechanism for discovering design omissions.

The project therefore deliberately entered a temporary design-only phase to test **architectural completeness rather than product completeness**.

The review asks whether the accepted corpus supports this complete path:

```text
Accepted semantic definitions
        ↓
Semantic release construction
        ↓
Release retention / retrieval
        ↓
Merchant Configuration Revision
        ↓
Deterministic compilation
        ↓
Resolved Configuration Package
        ↓
Atomic activation
        ↓
Runtime package binding
        ↓
Capability-owned execution authority
        ↓
Authoritative persistence / external fulfilment
        ↓
Projection
        ↓
API contract
        ↓
Merchant / Customer / Public surface
```

The target is not to pre-design every class, endpoint, cache, provider or screen. The target is that an implementer can cross every material authority boundary without inventing business meaning, ownership, release affinity, failure policy or historical-support rules.

---

## 2. Review method required by DESIGN-RULES

Every boundary was tested against the following questions:

```text
IDENTITY
    What identifies the thing or contract?

OWNERSHIP
    Which authority owns its meaning and mutation?

LIFECYCLE
    How is it created, retained, superseded or retired?

COMPOSITION
    How does it participate in the running application without transferring authority?

PERSISTENCE / RECONSTRUCTION
    Does it survive process restart and, if so, how is equivalent authority recovered?

VERSION AFFINITY
    Which exact semantic/configuration/provider context governs it?

FAILURE
    What happens when required evidence or support is absent?

HISTORY
    Can outstanding commitments and historical interpretation retain their accepted affinity?
```

Ambiguities were classified using `DESIGN-RULES.md`:

```text
A CONTRADICTION
B SEMANTIC AMBIGUITY
C AUTHORITY AMBIGUITY
D OWNERSHIP OVERLAP
E TERMINOLOGY DRIFT
F STALE / SUPERSEDED CLAIM
G UNDERSPECIFICATION
H SAFE OVERLAP
```

Only A–G material gaps are candidates for deliberate DDR promotion. Deliberately deferred implementation choices remain unpromoted.

---

## 3. Accepted authority traced

The review traced, at minimum, the following accepted authorities:

```text
MS-PROT-022 v1.5
    Resolved Configuration Package and static/contextual boundary

MS-PROT-027 v1.1 + v1.2
    Projection/read model and Exposure boundaries

MS-PROT-029 v1.1
    modular backend and separate delivery surfaces

MS-PROT-030
    Java/Spring backend and delivery/application/domain dependency direction

MS-PROT-033
    relational persistence, repository ownership and non-authoritative projections

MS-PROT-035
    use-case/projection-oriented API contracts

MS-PROT-040 v1.0 + v1.1
    configuration revision, package, activation and runtime binding

MS-PROT-042 through v1.4
    Booking/Appointment and Booking residual-obligation authority

MS-PROT-048 through v1.3
    fulfilment roles, requirements, bindings and provider separation

MS-PROT-049 v1.0 + v1.1
    static/contextual surface contribution composition

MS-PROT-054
    immutable semantic releases, compatibility, migration and historical support

MS-PROT-069
    execution uncertainty

MS-PROT-070
    controlled degradation and projection-specific stale-read requirement

MS-PROT-072
    cross-capability orchestration and restart-safe durable progression

MS-TAS-RECOVERY-001
    production restore/DR, Recovery Release Bundles and derived-state rebuilding

ADR-009
    capability-owned residual authority composition

ADR-010
    coherent Semantic Release Assembly and exact release lookup
```

Prototype, PRD and unindexed historical TAS material were treated as evidence only and were not allowed to override accepted authority.

---

## 4. Representative end-to-end falsification scenario

The architecture was tested against a deliberately difficult but ordinary evolution case.

```text
Merchant A
    active Configuration C18 @ Semantic Release R8
    Booking capability later disabled for new activity
    outstanding Booking B100 created under C18 @ R8

Merchant B
    active Configuration C27 @ Semantic Release R9
    new Booking activity permitted

Current software deployment
    newer than the deployment that first served R8

Then:
    application process restarts
    provider readiness for one external dependency is DEGRADED
    some read projection is stale or temporarily unavailable
```

After restart Main Street must be able to:

1. recover the exact active Configuration Revision for each merchant;
2. resolve or reconstruct the exact applicable Resolved Configuration Package;
3. obtain the exact release-affined semantic, Surface and Fulfilment definitions;
4. prove that the deployed executable implementation can still honour R8 where B100 requires it;
5. execute Merchant B's R9 new activity without silently applying R8 semantics;
6. execute Merchant A's residual R8 commitment without switching it to R9 because R9 is newer;
7. resolve live Provider Readiness outside the immutable package;
8. avoid using stale projection state as mutation authority;
9. determine whether the stale/unavailable projection may still be served and how its surface behaves;
10. refresh/rebuild projection state from authoritative truth where required; and
11. serve the resulting use-case/projection contract through the correct Merchant/Customer/Public boundary.

The accepted corpus successfully governs most of this path, but three points still require an implementer to choose material architecture or failure semantics.

---

# 5. Review matrix

| Review area | Current result | Classification | DDR promotion |
|---|---|---:|---:|
| Semantic release construction / affinity | Coherent runtime assembly accepted by ADR-010 | H / governed | No |
| Semantic release publication, durable retention and ordinary bootstrap | Material source/retention/bootstrap rule missing | **G** | **Yes — EAC-001** |
| Runtime semantic execution support across deployments/releases | Historical support obligation exists but executable binding/compatibility mechanism is deferred | **G + C** | **Yes — EAC-002** |
| Configuration Revision → RCP → activation → runtime package binding | Governed by MS-PROT-022/040 | H / governed | No |
| RCP persistence versus deterministic reconstruction | Explicit accepted alternative; storage choice remains implementation | H | No |
| Capability-owned authoritative persistence/repositories | Governed by MS-PROT-025/033 and capability authorities | H / governed | No |
| Application composition-root mechanics / Spring bean layout | Existing authority determines dependency direction; exact framework wiring is implementation | H | No |
| Projection authority and non-authoritative read semantics | Governed at semantic level | H / governed | No |
| Projection freshness, staleness, availability and Surface consequence | Required by MS-PROT-070/049 but no reusable contract is yet defined | **G** | **Yes — EAC-003** |
| Merchant/Customer/Public/Integration API architecture | Governed by MS-PROT-029/035 | H / governed | No |
| REST URI/OpenAPI/JSON/pagination/real-time transport | Intentionally deferred implementation detail | H | No |
| Final UI routes/components/layout/design system | Intentionally outside current completeness target | H / future | No |
| Concrete provider adapters/readiness implementations | Capability/provider-specific future implementation | H / future | No |

The result is deliberately narrower than a general architecture backlog. Missing technology selection alone is not a design gap.

---

# 6. EAC-001 — Semantic Release Publication, Retention & Bootstrap Architecture

## 6.1 Classification

```text
G — UNDERSPECIFICATION
```

There is no contradiction in accepted authority. The missing information is how the accepted immutable semantic release becomes a durable, reproducible platform artefact that an ordinary application process can obtain after restart.

## 6.2 Existing authority

MS-PROT-054 already establishes:

- a published Semantic Registry Release is immutable;
- deployment version is not semantic release identity;
- old commitments may retain old release affinity;
- historical execution/interpretation support must be preserved while required;
- exact implementation representation is deferred.

ADR-010 establishes:

```text
SemanticReleaseAssembly
├── SemanticRegistrySnapshot
├── SurfaceContributionRegistrySnapshot
└── FulfilmentContractRegistrySnapshot
```

and exact release lookup, but deliberately does not define where concrete release definitions come from or how they are retained across process lifetimes.

MS-TAS-RECOVERY-001 additionally requires a `RecoveryReleaseBundle` containing an application artefact, migration chain, semantic registry release and deployment specification for the operational recovery window. This proves that semantic release state must be recoverable, but it is a disaster-recovery contract, not the ordinary runtime publication/bootstrap architecture and not necessarily sufficient for commitments whose support lifetime exceeds the PITR window.

## 6.3 Why the omission is material

Without an accepted rule an implementer could legitimately choose materially different behaviours:

```text
A. reconstruct R8 from whatever current Java definitions happen to register
B. store mutable "current" semantic definitions in PostgreSQL
C. package immutable release artefacts and load exact R8/R9 definitions
D. use a separate semantic-registry service
```

A can silently reinterpret R8 after code changes. B can accidentally make mutable operational persistence the owner of semantic meaning. C and D can preserve immutability but have materially different publication/retention boundaries.

This is therefore not ordinary Spring wiring.

## 6.4 Options reviewed

### Option A — Current-code reconstruction only

At startup, run current registration code and label its result with historical release identifiers.

**Rejected in review.** A later deployment could change the meaning produced for R8 while preserving the same identifier, violating MS-PROT-054.

### Option B — One mutable runtime semantic catalogue

Maintain current semantic definitions as ordinary mutable runtime rows/state.

**Rejected as the governing model.** A storage technology may later hold immutable release artefacts, but semantic release mutation must not inherit ordinary mutable-configuration semantics.

### Option C — Immutable published release artefact catalogue

Publish each accepted semantic release as an immutable reproducible platform artefact. Ordinary application bootstrap resolves exact artefacts and constructs ADR-010 `SemanticReleaseAssembly` instances from them. Physical storage may be packaged resources, protected artefact storage, immutable database representation or another implementation, provided the logical publication/immutability/retention contract is preserved.

**Recommended.**

### Option D — Dedicated network semantic-registry service

A separate service publishes and serves immutable releases.

**Not justified initially.** It adds a distributed dependency without evidence that the modular monolith requires it.

## 6.5 Review recommendation — awaiting manual approval

Recommend a narrow authority establishing:

1. **Semantic release publication is a Main Street platform authority**, not a merchant or application-deployment side effect.
2. A published release has one immutable reproducible **release artefact** sufficient to reconstruct its accepted static definitions and ADR-010 assembly.
3. The runtime never reconstructs an old release by relabelling current definitions.
4. Ordinary bootstrap resolves releases by exact identifier; no `latest` substitution is permitted for a pinned Configuration/RCP/commitment.
5. Release artefacts required by active configurations, outstanding historical commitments, historical interpretation obligations or accepted recovery requirements must remain retrievable.
6. Automatic release retirement is prohibited unless Main Street can prove the release is no longer required for any governing support dimension.
7. Physical artefact storage and build tooling remain downstream implementation choices provided immutability, integrity, reproducibility and retention are preserved.
8. The accepted Recovery Release Bundle must reference or contain the same semantic release artefact rather than defining a competing DR-only semantic representation.

A later governed review may define safe release retirement. The initial safe rule may simply retain published release artefacts rather than invent premature garbage collection.

---

# 7. EAC-002 — Runtime Semantic Execution Support & Deployment Compatibility

## 7.1 Classification

```text
G — UNDERSPECIFICATION
C — AUTHORITY AMBIGUITY at the implementation boundary
```

MS-PROT-054 owns the semantic obligation to preserve historical execution support, but no accepted implementation architecture currently determines how a deployed runtime proves that a capability handler can execute a particular historical semantic contract without reinterpreting it.

## 7.2 Existing authority

MS-PROT-054 explicitly states:

```text
APPLICATION DEPLOYMENT VERSION
    ≠
SEMANTIC REGISTRY RELEASE
```

and requires enough execution/interpretation support for outstanding historical commitments. It deliberately leaves the mechanism open, listing examples such as historical definitions, compatible handlers, historical execution adapters and reconstructable packages.

MS-PROT-040 requires each configuration-dependent invocation to bind one exact RCP before execution. ADR-010 provides exact static release definitions. Neither proves that the Java handler reached after binding still implements the semantics of that release.

## 7.3 Falsification

Assume:

```text
R8 booking.confirm
    semantics S8

R9 booking.confirm
    semantics S9

current Java handler
    implements S9

B100 historical commitment
    retains R8 affinity
```

If runtime performs only:

```text
bind R8 package
    ↓
call current Booking handler
```

then the package affinity is correct while the executed behaviour may still be wrong.

The opposite extreme—retaining an entire application deployment for every semantic release—preserves behaviour but creates unnecessary operational duplication where one handler is genuinely semantics-compatible across releases.

## 7.4 Options reviewed

### Option A — Always use the current handler

**Rejected.** Deployment would become implicit semantic migration authority.

### Option B — One full application deployment/runtime per semantic release

**Rejected as the universal model.** It preserves isolation but duplicates the whole platform even when only one operation changed.

### Option C — Explicit executable semantic-support compatibility with narrow historical adapters where required

The running application proves which semantic contracts/releases its capability-owned implementations support. A handler may serve multiple releases only where compatibility is explicit; a material semantic change requires an appropriate release-specific implementation/adaptation path for old commitments that still require execution.

**Recommended.**

### Option D — Dynamically start arbitrary old application binaries on demand

**Deferred/rejected initially.** It may be a future operational technique for extreme migration cases but is unnecessary as the default modular-monolith execution model.

## 7.5 Review recommendation — awaiting manual approval

Recommend a narrow semantic/runtime-support authority establishing:

1. Binding an invocation to an RCP/release is necessary but not sufficient; the runtime must also resolve **executable support compatible with that semantic context**.
2. Application deployment must not silently make the newest capability implementation authoritative for every historical release.
3. One capability implementation may support multiple semantic releases only where accepted compatibility evidence establishes equivalent required behaviour for the relevant contract.
4. Where behaviour is not compatible, Main Street retains a narrow historical implementation/adapter path for the affected semantic scope while obligations require it.
5. Execution-support binding remains capability/operation scoped where practical; it must not require cloning the entire application per release.
6. Missing required execution support fails closed for the affected operation and must be surfaced as a deployment/runtime-support defect rather than silently substituting another release.
7. New deployment must not intentionally retire execution support still required for active configurations or outstanding commitments.
8. Support dimensions already identified by MS-PROT-054—new configuration validation, new business activity, existing commitment execution and historical interpretation—remain distinct; an older release may be prohibited for new activity while still supported for residual execution/history.
9. Java class names, Spring bean names and deployment version strings do not themselves constitute semantic compatibility evidence.

The exact Java registry/composite/manifest representation should remain implementation-deferred until this semantic/runtime contract is approved.

---

# 8. EAC-003 — Projection Contract, Freshness & Availability Boundary

## 8.1 Classification

```text
G — UNDERSPECIFICATION
```

Projection authority is conceptually clear, but the accepted corpus intentionally leaves projection storage/query details downstream. That becomes material at the Surface/API boundary because two later accepted rules now require a projection-specific runtime contract.

## 8.2 Existing authority

MS-PROT-027 establishes:

- authoritative state is business truth;
- projections are audience-specific derived representations;
- projections may be eventually consistent where safe;
- stale projection data cannot authorise mutation.

MS-PROT-049 establishes that a Surface Contribution may identify projection requirements and that contextual eligibility may depend on projection availability.

MS-PROT-070 establishes the stronger resilience rule:

> Reads may remain available during dependency failure only under the **owning projection/read model's freshness and staleness contract**.

No accepted generic projection contract currently defines how those requirements are identified, owned and evaluated.

## 8.3 Falsification

Assume the merchant Bookings workspace is semantically eligible and the actor is authorised, but its read projection is unavailable or delayed.

Possible implementations could choose:

```text
A. hide the workspace entirely
B. show the workspace with a temporary-unavailable state
C. serve cached data regardless of age
D. serve stale data only within a projection-specific tolerance
E. bypass the projection and read authoritative write tables directly from UI transport
```

These choices have materially different correctness, privacy and user-experience consequences. E is architecturally invalid; C may violate MS-PROT-070; A can wrongly make a capability appear absent; B/D may be valid depending on the projection contract.

A page-wide global TTL cannot safely govern both merchant description and live availability/payment state.

## 8.4 Options reviewed

### Option A — One global projection freshness rule

**Rejected.** Staleness tolerance is data/use-case specific.

### Option B — Surface/page decides ad hoc

**Rejected.** Presentation would become hidden read-consistency authority.

### Option C — Explicit projection/read-model contract owned at the projection/application boundary

Each material projection declares enough contract to determine audience/context, authoritative sources, permitted staleness/freshness, failure/unavailability behaviour, rebuildability and provenance. Surface/API consume that contract without becoming its semantic owner.

**Recommended.**

### Option D — Create a universal projection registry and DSL immediately

**Rejected.** Evidence requires a contract boundary, not a general expression/registry framework.

## 8.5 Review recommendation — awaiting manual approval

Recommend a narrow Projection Contract authority establishing:

1. Every material read model/projection that may be cached, asynchronously rebuilt or used by Surface eligibility has an explicit owning projection contract.
2. The contract identifies the audience/use-case context and authoritative source semantics without transferring source-fact ownership.
3. Freshness/staleness tolerance is projection-specific; no global page/system TTL becomes semantic authority.
4. The contract determines whether stale data is permitted, must be labelled/degraded, or must be withheld when current truth cannot be established.
5. Projection unavailability does not automatically deactivate a capability or erase its workspace; the owning Surface/Projection contract determines the bounded presentation consequence.
6. Projection state never authorises mutation; commands revalidate authoritative state.
7. Exposure/privacy/security restrictions outrank stale-cache availability.
8. Derived projections are rebuildable from authoritative truth/provenance where their contract claims rebuildability.
9. A stable projection identity/reference is required only where another registered contract (for example a Surface Contribution) must reference that projection; this does not justify a general projection DSL or global registry by itself.
10. Storage technology, query language, cache product, materialisation mechanism and API DTO shape remain implementation/use-case decisions.

The exact availability/freshness vocabulary should be resolved in the dedicated governed review rather than frozen by this completeness audit.

---

# 9. Deliberately NOT promoted as design gaps

## 9.1 RCP persistence versus reconstruction

MS-PROT-040 v1.1 already accepts either:

```text
retain exact historical RCP
```

or:

```text
retain all immutable authoritative inputs required to reconstruct an equivalent RCP deterministically
```

MS-PROT-022 v1.5 supplies the deterministic package boundary. Choosing persistence, reconstruction or a bounded hybrid is therefore an implementation architecture choice unless later evidence shows a semantic difference.

**Classification: H — safe downstream choice.**

## 9.2 Spring composition-root mechanics

ADR-009 requires explicit immutable residual-authority composition. ADR-010 requires exact release assembly lookup. MS-PROT-030 requires Spring to wrap rather than infect the domain and fixes dependency direction.

Whether these are constructed through `@Configuration`, explicit factories or another ordinary Spring composition mechanism does not change authority if the accepted invariants are preserved.

**Classification: H — implementation detail.**

## 9.3 Projection storage technology

PostgreSQL read tables, in-memory projections, cache-backed projections or another technology may be valid depending on the future projection contract. Technology does not need promotion before the semantic freshness/availability contract is accepted.

**Classification: H — implementation detail after EAC-003.**

## 9.4 HTTP/API mechanics

MS-PROT-029 and MS-PROT-035 already govern audience separation, trusted MerchantScope, use-case-oriented commands/queries and boundary DTOs. Exact URI conventions, OpenAPI tooling, pagination mechanics, JSON configuration, CORS/CSRF and real-time transport remain explicitly deferred.

**Classification: H — intentionally deferred.**

## 9.5 Final UI architecture

MS-PROT-036, MS-PROT-037 and MS-PROT-049 already establish composition boundaries, but final components, routes, navigation and visual design are outside this review. UI work should begin only after the projection/read-contract path can support a real vertical slice without inventing semantics.

**Classification: H — future delivery design, not a current executable-architecture blocker.**

## 9.6 Concrete provider adapters

Provider-specific adapters/readiness mappings are intentionally downstream of MS-PROT-048/049/067/070 and should be introduced by real capability/provider needs rather than a universal provider framework.

**Classification: H — future implementation.**

---

# 10. Cross-domain falsification

The three promoted gaps were tested against materially different merchants.

## Information publisher

Needs Publication/Opportunity/Enquiry projection and public delivery but may have no Booking, Inventory or Payment.

- EAC-001 still matters because its active configuration is pinned to a semantic release.
- EAC-002 matters if publication semantics change while old published/historical data remains interpretable.
- EAC-003 matters because public content may tolerate bounded staleness differently from deadline-sensitive Opportunity data.

**PASS — gaps remain domain-neutral.**

## Online consultant

Needs Scheduling/Booking/Appointments and may bind an external calendar provider.

- historical Booking/Appointment commitments expose EAC-001/002 directly;
- provider degradation remains contextual under existing authority;
- calendar/booking projections expose EAC-003.

**PASS.**

## Motel / bookable resource

High consequence of stale availability and historical reservation affinity.

- exact historical release support is mandatory;
- stale public availability cannot authorise room allocation;
- projection freshness must be stricter than static merchant content.

**PASS.**

## Retailer

Inventory and payment data demonstrate that a global stale-read policy would be unsafe, while product description/images may tolerate more staleness.

**PASS.**

## Hybrid merchant

Multiple capability projections and releases must compose without a business-category-specific runtime or UI template.

**PASS.**

No promoted gap requires a Salon, Motel, Retail, Consultant or Publisher-specific architecture.

---

# 11. Failed assumptions removed by the review

The following candidate conclusions were rejected:

```text
"Every missing implementation choice needs another ADR."              ✗

"RCP must be persisted because restart exists."                        ✗
    Accepted authority already permits exact retention OR reconstruction.

"Spring needs a new service-locator architecture."                     ✗
    Existing composition authority is sufficient.

"A generic projection registry should be designed before any projection." ✗
    Evidence requires an explicit contract, not a generic registry/DSL.

"Latest semantic release is good enough after restart."                ✗

"Pinned RCP proves the current Java handler is historically compatible." ✗

"Stale read behaviour can be a global cache setting."                  ✗

"Projection failure should simply hide the affected capability."       ✗
```

---

# 12. Recommended design sequence

The review recommends resolving the promoted gaps in this order:

```text
EAC-001
Semantic Release Publication, Retention & Bootstrap
        ↓
EAC-002
Runtime Semantic Execution Support & Deployment Compatibility
        ↓
EAC-003
Projection Contract, Freshness & Availability
```

Reasoning:

- EAC-001 establishes what exact static release artefact exists after restart.
- EAC-002 establishes how executable code safely honours that release, including historical commitments.
- EAC-003 establishes how authoritative results become safely available read contracts for APIs/surfaces.

After those are accepted, the architecture should be re-run through the representative vertical slice before implementation resumes.

---

# 13. DESIGN-RULES lifecycle status

For the three promoted gaps, this completeness review has completed:

```text
PROPOSE                 ✓
REVIEW                  ✓
INITIAL FALSIFICATION   ✓
RECOMMENDATION          ✓
```

It has **not** completed:

```text
MANUAL APPROVAL                         ✗
FORMALISE ACCEPTED AUTHORITY            ✗
UPDATE AUTHORITY INDEX FOR NEW ANSWER   ✗
IMPLEMENTATION AUTHORISATION            ✗
```

The recommendations in Sections 6–8 must therefore be discussed/approved before any substantive new authority is created.

---

# 14. Implementation-readiness verdict

The accepted architecture is strong through configuration compilation/activation and authoritative execution foundations, but the complete release-to-delivery path is **not yet implementation-ready as a whole**.

Current verdict:

```text
Semantic core / ownership                    READY
Configuration → RCP → activation             READY
Authoritative persistence / transaction       READY FOUNDATION
Static semantic release coherence             READY
Residual authority composition                READY

Release publication / retention / bootstrap   DESIGN GAP — EAC-001
Historical executable support across deploys  DESIGN GAP — EAC-002
Projection freshness / availability contract  DESIGN GAP — EAC-003

HTTP delivery architecture                    READY FOUNDATION
Final frontend implementation                 INTENTIONALLY NOT STARTED
```

Implementation should remain paused while the three promoted material gaps are governed.

---

# 15. IMPLEMENTATION-RULES impact review

No change to `designs/IMPLEMENTATION-RULES.md` is recommended by this review.

The existing rule already requires implementation to pause when accepted authority does not determine a material semantic/architectural choice. The present design-only phase is an application of that rule, not evidence that the rule is insufficient.

---

# 16. Governance actions required from this review

This review requires the following navigation/work-queue changes only:

1. repair `AUTHORITY-INDEX.md` so already accepted MS-PROT-042 v1.4, ADR-009 and ADR-010 are discoverable;
2. promote EAC-001, EAC-002 and EAC-003 into the canonical `DEFERRED-DECISION-REGISTER.md`;
3. do **not** add this review or its recommendations to `AUTHORITY-INDEX.md` as accepted substantive authority;
4. do **not** modify `CANONICAL-SEMANTIC-LEXICON.md` because no new canonical term has yet been approved;
5. run repository design-corpus/governance conformance after the governance updates.

---

# 17. Review conclusion

The temporary design-only pivot is justified, but it should remain bounded.

The completeness sweep does **not** reveal a need to redesign Main Street broadly. It reveals three precise seams where existing accepted authority stops just before an implementer would otherwise have to invent consequential behaviour:

```text
1. How an immutable semantic release exists durably and re-enters a process.

2. How deployed executable code proves it still implements the exact semantic
   context bound to current and historical commitments.

3. How derived projection freshness/unavailability is governed before APIs and
   surfaces consume it.
```

Resolve those three through DESIGN-RULES, rerun the end-to-end falsification, and only then reconsider implementation.
