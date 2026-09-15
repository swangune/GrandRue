# IMP-05 — Merchant Definition, Configuration & Activation — Conformance Baseline

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation  
**Date:** 28–30 August 2026
**Original repository head inspected:** `276cc08bdfe8310c401875d8ddc049d8ca0eb0fe`
**Extended local verification:** C4/C5 working slice, 29 August 2026
**Status:** **CONFORMING_COMPLETE**

## 1. Governing scope

MS-IMP-001 requires:

```text
Merchant Profile
Location
Business Hours configuration foundation
Onboarding Case
structured onboarding answers
AI proposal boundary where required
Initial Configuration Intent
Configuration Revision
approval
compilation
activation
entitlement
runtime applicability / eligibility
```

The completion milestone is a merchant that can be established, defined, configured, compiled and activated without collapsing profile truth, onboarding evidence, configuration authority, commercial entitlement or runtime eligibility into one owner.

## 2. Current conformance map

| Required child | Classification | Repository evidence / residual |
|---|---|---|
| Merchant Public Descriptor primitive | **CONFORMING_COMPLETE** | `MerchantPublicDescriptor` and focused unit evidence |
| Merchant Profile authoritative persistence/revisions | **CONFORMING_COMPLETE foundation** | Descriptor, Contact Point, Service Area, External Presence and Classification Entry immutable revision/currentness/concurrency/retry authorities conform |
| Merchant Location | **CONFORMING_COMPLETE foundation** | Version-affined `PostalAddressV1`, merchant-scoped identity, immutable exact revisions/currentness, terminal retirement, concurrency, retry and current-Controller mutation authority conform |
| Contact points, service areas, external presence, classification | **CONFORMING_COMPLETE** | Durable independently revisioned Contact Point, versioned Service Area, External Presence and non-executable Classification Entry authorities conform |
| Business Hours configuration | **CONFORMING_COMPLETE foundation** | Merchant- and Location-scope immutable stable weekly-hours revisions, currentness, concurrency, exact retry and exact consumed active-Location revision affinity conform |
| Onboarding question/answer semantics | **CONFORMING_COMPLETE foundation** | Immutable evidence, exact definition affinity, registered answer constraints, deterministic candidate interpretation and branch pruning now conform |
| Onboarding Case lifecycle | **CONFORMING_COMPLETE** | Durable initial-case creation, one-current-case enforcement, exact revisions, optimistic answer mutation, resume, retry identity, atomic submission and exact configuration-handoff completion now conform |
| Deterministic prompt recomputation | **CONFORMING_COMPLETE** | Revision-affined durable evidence reconstruction, registered mappings/applicability, branch pruning, scoped prompts and deterministic frontier ordering |
| AI proposal boundary | **CONDITION_NOT_ACTIVE for IMP-05** | No production AI path exists and deterministic onboarding is independently operable; production merchant-assistance/inference implementation remains owned by IMP-08A after the IMP-07 programme gate |
| Final review and Initial Configuration Intent | **CONFORMING_COMPLETE** | Current exact evidence is transactionally recomputed and re-authorized; immutable source-affined intent creation and `SUBMITTED` transition are atomic, retry-safe and concurrency-safe |
| Configuration Revision | **PARTIALLY_CONFORMING** | Immutable first-revision materialisation and authoritative persistence from one exact Initial Configuration Intent now conform; later revision/change-set mutation remains outside this slice |
| Compilation / Resolved Configuration Package | **CONFORMING_COMPLETE** | D2a IMP-03 compiler/RCP model, D2b durable exact validation/package evidence and D2c immutable business-facing impact-review evidence conform |
| Publication / exact release | **CONFORMING_COMPLETE** | Registry-validated release publication and exact lookup |
| Approval | **CONFORMING_COMPLETE** | Append-only exact revision/validation/package/impact approval by the current authenticated Merchant Controller, with retry, concurrency and transfer-applicability evidence |
| Activation | **CONFORMING_COMPLETE** | PostgreSQL persistence, concurrency, idempotency, compatibility, outbox, exact D3 approval admission, deterministic exact-RCP requirements, immutable serving-generation evidence, promotion fencing and activation-side exact release-purpose/stable-generation admission conform |
| Commercial entitlement | **CONFORMING_COMPLETE foundation** | Independent provenance-bearing durable grant authorities and effective decision resolution |
| Runtime applicability / eligibility composition | **CONFORMING_COMPLETE** | Exact active-model applicability, executable-support admission and durable Commercial entitlement compose through one captured operation; the complete PostgreSQL establishment-to-active proof preserves their separate authority |

Existing code is preserved as evidence. These classifications do not promote partial primitives to completed child authorities.

## 3. Fine-grained dependency graph

```text
Merchant Account / Scope / Controller authority (IMP-04 COMPLETE)
        │
        ├── A. Merchant definition authority
        │     ├── A1 Descriptor primitive COMPLETE
        │     ├── A2 Durable descriptor revisions COMPLETE
        │     ├── A3 Contact/service/external facts COMPLETE
        │     │     ├── A3a Merchant Contact Point authority COMPLETE
        │     │     ├── A3b Service Area Descriptor authority COMPLETE
        │     │     ├── A3c External Presence Link authority COMPLETE
        │     │     └── A3d Classification metadata authority COMPLETE
        │     └── A4 Merchant Location COMPLETE
        │           ├── A4a PostalAddressV1 value/normalization COMPLETE
        │           └── A4b Durable Location revision/lifecycle authority COMPLETE
        │
        ├── B. Business Hours
        │     ├── B1 Weekly/scope primitive COMPLETE
        │     └── B2 Durable revision authority COMPLETE
        │           ├── B2a Merchant-scope revisions COMPLETE
        │           └── B2b Location-scope integration COMPLETE
        │
        └── C. Onboarding authority
              ├── C1 Semantic primitives COMPLETE
              ├── C2 Durable case/evidence store COMPLETE
              ├── C3 Deterministic recomputation COMPLETE
              ├── C4 Exact final review/submission COMPLETE
              └── C5 Initial Configuration Intent COMPLETE
                                      │
                                      ▼
D. Configuration lifecycle
    ├── D1 Initial revision materialisation/persistence COMPLETE
    ├── D2a Deterministic compiler/RCP model COMPLETE
    ├── D2b Durable exact validation/package evidence COMPLETE
    ├── D2c Immutable business-facing impact-review evidence COMPLETE
    ├── D3 Current-Controller exact approval authority/persistence COMPLETE
    ├── D4 Publication COMPLETE
    └── D5 Activation
          ├── D5a Persistence/concurrency/outbox foundation COMPLETE
          ├── D5b Ordinary first-activation exact admission integration COMPLETE
          └── D5c Serving-deployment admission/evidence/generation fence
                  ├── D5c1 Deterministic RCP-affined requirement-set evidence COMPLETE
                  ├── D5c2 Immutable serving-generation snapshot/support evidence COMPLETE
                  ├── D5c3 Bidirectional promotion state machine/fence COMPLETE
                  └── D5c4 Activation-side serving admission integration COMPLETE
                                      │
                                      ▼
E. Entitlement + runtime eligibility integration COMPLETE
                                      │
                                      ▼
F. Establishment → definition → configuration → activation proof COMPLETE
```

All fine-grained nodes A through F are now CONFORMING_COMPLETE. E preserves the accepted separation between semantic applicability, exact executable-support admission and current Commercial entitlement. F composes the real PostgreSQL-backed establishment, definition, onboarding, Configuration, serving-admission, activation, trial-grant and runtime-dispatch authorities end to end without introducing a parallel aggregate or caller-supplied authority. Detailed child evidence remains authoritative for each lower-level invariant; `imp-05-establishment-to-active-runtime-closure-2026-08-30.md` records the final composition proof.

## 4. Selected node

```text
A2 — Durable Merchant Public Descriptor revision authority
    = CONFORMING_COMPLETE

C2 — Durable Onboarding Case / evidence store
    = CONFORMING_COMPLETE

C3 — Deterministic onboarding recomputation
    = CONFORMING_COMPLETE

C4 — Exact final review / submission
    = CONFORMING_COMPLETE

C5 — Initial Configuration Intent
    = CONFORMING_COMPLETE

D1 — Configuration Revision materialisation / persistence
    = CONFORMING_COMPLETE

D2a — Deterministic compiler / Resolved Configuration Package model
    = CONFORMING_COMPLETE

D2b — Durable exact validation / package evidence
    = CONFORMING_COMPLETE

D2c — Immutable business-facing impact-review evidence
    = CONFORMING_COMPLETE

D3 — Current-Controller exact approval authority / persistence
    = CONFORMING_COMPLETE

D5a — Activation persistence / concurrency / outbox foundation
    = CONFORMING_COMPLETE

D5b — Ordinary first-activation exact admission integration
    = CONFORMING_COMPLETE

D5c1 — Deterministic RCP-affined requirement-set evidence
    = CONFORMING_COMPLETE

D5c2 — Immutable serving-generation snapshot/support evidence
    = CONFORMING_COMPLETE

D5c3 — Bidirectional promotion state machine / generation fence
    = CONFORMING_COMPLETE

D5c4 — Activation-side serving admission integration
    = CONFORMING_COMPLETE

A4 — Merchant Location authority
    = CONFORMING_COMPLETE

A4a — PostalAddressV1 value, validation and normalization
    = CONFORMING_COMPLETE — governed by composite MS-PROT-051 through v1.2

A4b — Durable Merchant Location revision/lifecycle authority
    = CONFORMING_COMPLETE

B2 — Durable Business Hours revision authority
    = CONFORMING_COMPLETE

B2a — Merchant-scope stable weekly-hours revision authority
    = CONFORMING_COMPLETE — governed by composite MS-PROT-050 through v1.4

B2b — Merchant-location stable weekly-hours revision integration
    = CONFORMING_COMPLETE

A3a — Merchant Contact Point authority
    = CONFORMING_COMPLETE

A3c — External Presence Link authority
    = CONFORMING_COMPLETE

A3b — Service Area Descriptor authority
    = CONFORMING_COMPLETE — governed by composite MS-PROT-051 through v1.3

A3d — Classification metadata authority
    = CONFORMING_COMPLETE — governed by composite MS-PROT-051 through v1.4

C1 — Onboarding semantic primitives
    = CONFORMING_COMPLETE — existing-code satisfaction under composite MS-PROT-052 through v1.2

E — Entitlement + runtime eligibility integration
    = CONFORMING_COMPLETE — existing-code satisfaction through exact active-model, executable-support and durable Commercial-authority composition

F — Establishment → definition → configuration → activation proof
    = CONFORMING_COMPLETE — PostgreSQL end-to-end composition evidence
```

Governing requirements:

- prompt and discovery-mapping registry remains separate from Semantic Registry authority;
- recomputation binds to one exact Onboarding Case revision and its reconstructed effective evidence;
- only exact registered question-version/option mappings propose candidate semantic seeds;
- historical definitions are reusable only through an explicit registered interpretation contract;
- inapplicable branch answers remain history but cease candidate influence;
- an independently valid semantic dependency preserves a shared branch;
- prompt eligibility respects exact context scope;
- priority classes precede registered information-gain ordering with a stable deterministic tie-break;
- contradictory option combinations and answer-form cardinality remain unresolved rather than influencing the candidate;
- raw answers and semantic seeds remain non-executable;
- deterministic recomputation remains fully operable without AI;
- submission revalidates exact current evidence, completeness and current authority inside the owning transaction;
- Initial Configuration Intent creation, the append-only submission revision and lifecycle transition are atomic;
- retry returns the original intent only for the same exact request payload; and
- the intent does not select a semantic release or create executable Merchant Configuration;
- D1 alone selects the platform-owned ordinary new-configuration semantic release;
- one source intent can produce at most one immutable, merchant-scoped version-1 Configuration Revision;
- retry returns the original pinned revision even after the ordinary release advances;
- Configuration acceptance and the exact append-only Onboarding Case completion revision commit atomically;
- materialisation does not imply validation, approval, compilation, publication or activation;
- successful validation evidence must bind one exact stored Configuration Revision, semantic release and retained immutable Resolved Configuration Package provenance;
- callers cannot replace exact validation/package evidence with a boolean or generic status assertion;
- impact review must be immutable, revision-affined and business-facing, and must preserve the exact reviewed effects and warnings; and
- a pre-revision onboarding review is not Configuration Revision impact-review evidence and cannot authorize approval;
- approval must resolve the exact successful validation/package and impact-review evidence inside the owning transaction;
- approval must reject blocking impact and require an authenticated current Merchant Controller at commit;
- request retry must return only the same committed intent, while concurrent duplicate delivery commits at most one logical approval;
- Controller transfer preserves immutable approval history but invalidates the former Controller's current applicability; and
- approval must not activate, publish or change serving-deployment state;
- ordinary first activation must consume a then-current applicable D3 approval rather than the weaker general approval abstraction;
- first-activation approval affinity must match the candidate merchant, immutable revision and semantic release; and
- activation admission must share the Controller-transfer fence so stale Controller authority cannot commit.
- requirement derivation must consume the exact validated RCP and exact release-affined mappings rather than caller-supplied requirements or completeness booleans;
- every executable operation must resolve to one grouped affected-contract requirement with all participant/effect contracts retained;
- requirement-set identity must use the accepted versioned, length-prefixed unsigned-UTF-8 canonical encoding; and
- durable requirement evidence must preserve exact revision, release and D2b package affinity through normalized immutable PostgreSQL rows.
- serving-generation materialisation evidence must retain the exact semantic release and ADR-013 packaged-bundle digest;
- executable support must remain grouped by implementation path as exact release/contract tuples rather than booleans or opaque JSON; and
- snapshot correction or support change must use a new generation identity rather than mutate existing evidence.
- release-purpose decisions must be append-only exact facts with separate current pointers for each release/purpose pair;
- ordinary release-reference advancement must atomically prove both current purposes admitted and retain the exact decision identities;
- activation must evaluate its revision-pinned release rather than require equality with the current ordinary reference;
- activation must acquire the shared ordinary-cohort control-row lock and fail closed under `PROMOTING` or uncertain epoch;
- activation must retain exact current purpose-decision identities, package/requirement-set affinity, snapshot/generation epoch and materialised bundle digest; and
- caller booleans, serving snapshots and mutable aliases are not release-purpose authority.

The completed C3 slice remains deterministic onboarding proposal authority only. C4/C5 bind merchant review to one exact current recomputation revision and provide a retry-safe atomic handoff, without moving Configuration Revision authority into onboarding. D1 accepts that handoff under a separate Configuration authority, pins the ordinary platform semantic release and durably records the first revision. D2a compiles the deterministic package; D2b retains exact successful validation/package provenance for the stored revision and release; D2c separately retains the ordered business-facing effects and all classified findings. D3 establishes exact current-Controller approval against both immutable evidence identities without activation side effects. D5b consumes that then-current applicable approval for ordinary first activation and rejects the earlier weaker abstraction. D5c1 retains the exact deterministic support requirement set for that validated package. D5c2 and D5c3 establish immutable serving-generation evidence and its durable promotion fence. MS-PROT-040 v1.5 supplies exact current release-purpose authority; D5c4 composes it with the D5c3 shared stable-generation fence and persists all consumed provenance atomically with activation.

## 5. Stop conditions

Implementation must stop the affected node if it requires:

- a universal `BusinessProfile` aggregate/version;
- Location storage before the structured-address choice is resolved;
- profile mutation to recompile or activate Merchant Configuration;
- AI-generated text without exact merchant approval evidence;
- exposure/publication inferred merely from stored profile data; or
- onboarding evidence to overwrite newer direct profile revisions.

## 6. Verification consequence

All IMP-05 child nodes passed their required RED → GREEN loops or satisfy the MS-IMP-001 existing-code rule with retained child evidence. The isolated canonical result is 674 unit/conformance tests plus 298 PostgreSQL integration tests, all passing against 51 Flyway migrations in the dedicated `mainstreet_test` database. E and F close through the exact end-to-end proof recorded in `imp-05-establishment-to-active-runtime-closure-2026-08-30.md`. IMP-05 is **CONFORMING_COMPLETE** and its completion promotes IMP-06 to READY under the canonical dependency graph.
