# Main Street Architecture Watch List

**Document ID:** MS-ARCH-WATCH-001  
**Version:** 1.2  
**Status:** ACTIVE persistent architecture surveillance record  
**Established:** 2 September 2026  
**Governed by:** `DESIGN-RULES.md`; `DOCUMENT-GOVERNANCE.md`; applicable accepted authorities resolved through `AUTHORITY-INDEX.md`  
**Purpose:** Track persistent architectural failure modes that remain materially possible after a local design decision or implementation mitigation has been accepted.

---

## 1. Governing distinction

This watch list is not the Deferred Decision Register.

```text
DEFERRED-DECISION-REGISTER.md
    → unresolved/deferred/promoted decisions whose answer may block or constrain work

watch-list.md
    → persistent failure modes that remain under surveillance even when the current local decision is resolved
```

A watch item MAY remain active indefinitely.

Mitigation does not mean elimination.

A resolved DDR gate SHALL NOT automatically remove a related watch item.

A watch item becomes a design gate only when observed evidence crosses its escalation criteria or another accepted governance rule requires promotion.

---

## 2. Watch-state vocabulary

| State | Meaning |
|---|---|
| **WATCHING** | Persistent risk is currently mitigated sufficiently to proceed but requires continued observation. |
| **ELEVATED** | Concrete evidence shows the risk is increasing; focused falsification/review is required before materially expanding the affected boundary. |
| **PROMOTE_TO_DESIGN_GATE** | Evidence crosses a declared escalation criterion; implementation that depends on the unresolved consequence must stop and the issue enters the DDR/design lifecycle. |
| **RETIRED** | Strong evidence shows the failure mode is no longer materially applicable to Main Street. Retirement requires explicit justification; ordinary mitigation is insufficient. |

---

## 3. MS-WATCH-001 — Semantic Gravity / Composition-Boundary Pressure

**State:** **WATCHING — PERSISTENT**  
**Initial evidence:** MS-PROT-027 / IMP-06 E3 exposure-result boundary; `IMP-06-E3-DG-002`  
**Current mitigation authority:** composite MS-PROT-027 through v1.10  
**Scope:** Platform-wide; especially high-fan-in composition, resolver, context, registry, projection/read and API boundaries

### 3.1 Failure mode

A component that legitimately combines inputs from several independently owned authorities can gradually become the semantic centre of the system.

The pressure commonly follows this path:

```text
many legitimate upstream authorities
        ↓
one convenient composition boundary
        ↓
more context/evidence added for convenience
        ↓
composition begins interpreting upstream semantics
        ↓
resolver/context/result/registry acquires policy and acquisition responsibilities
        ↓
semantic gravity
        ↓
god boundary + change amplification + hidden coupling
```

The failure may occur without an explicit architectural decision. It often enters through convenience, performance optimisation, generic extension mechanisms or repeated adjacent amendments.

### 3.2 Why mitigation does not close the watch

MS-PROT-027 v1.10 narrows the current Exposure-result boundary, but it does not make semantic gravity impossible.

Future capabilities, high read fan-in, performance pressure, mixed-capability merchants, analytics, search, dashboards, notifications and AI-derived operational views can recreate the same pressure at other boundaries.

Therefore:

> **Semantic gravity remains on watch even when every current design gate caused by it is resolved.**

### 3.3 Current protective invariants

The following are current mitigation patterns, not reasons to retire the watch:

1. **Adjacency is not ownership.** Participation in a request path does not transfer semantic authority.
2. **Composition does not transfer ownership.** A composer may consume owner-established conclusions without redefining them.
3. **Exposure consumes established inputs.** It does not discover, acquire or schedule upstream semantic dependencies.
4. **Contexts remain purpose-bounded.** `AudienceObservationContext` is not a generic runtime/world-state container.
5. **Registries bind definitions/evaluators.** They do not decide live business truth.
6. **Results remain results.** They do not become policy engines, caches, query plans, authorisation grants or transport contracts.
7. **Optimisation preserves ownership.** Batching, memoisation and projection do not transfer semantic authority.
8. **Purpose-specific composition is preferred over a universal composition engine** unless concrete repeated evidence proves a shared runtime abstraction necessary.

### 3.4 Surveillance indicators

The watch SHALL be reviewed whenever any of the following appears:

- a resolver branches on merchant type or capability/business vocabulary;
- a context accumulates provider state, capacity, availability, configuration truth, permissions or arbitrary fact bags outside its declared purpose;
- a registry begins reading live merchant/provider/actor state to produce business outcomes;
- a result object begins carrying execution authority, readiness, pagination, caching, retry, continuation or transport policy;
- one layer begins discovering which semantic owners must be consulted rather than consuming declared/established inputs;
- generic `Map<String, Object>`, claims bags, extension bags or untyped metadata become necessary for cross-owner composition;
- package-private/internal inspection helpers proliferate into de facto public semantic backdoors;
- adding one capability/exposable element requires coordinated edits through several otherwise independent owners;
- tests require increasingly large registry/context/release fixture graphs for a narrow semantic change;
- one request performs repeated cross-registry/per-element lookups that create N+1/fan-out pressure;
- performance optimisation proposes a universal `WorldState`, `ExposureContext`, `QueryContext`, `SemanticContext` or equivalent mega-context;
- a protocol/document repeatedly attracts adjacent concerns because they occur nearby in the request path;
- an API/representation layer begins re-acquiring underlying business facts based solely on result membership;
- failure reasons from independent authorities are normalised into one universal policy/error taxonomy without a demonstrated cross-platform semantic need.

### 3.5 Exposure-specific tripwires

For composite MS-PROT-027, the watch becomes **ELEVATED** immediately if any production implementation requires Exposure to:

```text
fetch projections
schedule source acquisition
interpret Provider Readiness
derive Actor Authorisation
branch on Product / Service / Booking / Publication semantics
own business-specific relationship rules
own generic ordering/pagination/caching
retain a request-wide operational world state
```

These are not merely code smells; they are evidence that the accepted boundary may no longer hold.

### 3.6 Promotion criteria

Promote this watch item to a governed design gate when either condition is met:

**Criterion A — direct boundary breach**

One accepted/necessary implementation cannot satisfy a real use case without transferring an upstream semantic authority into a composition boundary such as Exposure, a generic context, registry, result or API layer.

**Criterion B — repeated missing read-composition mechanism**

At least two materially different capability/use-case implementations demonstrate runtime semantic dependency graphs that cannot be represented through existing purpose-specific mechanisms — including contextual resolution, Projection source/serviceability contracts, capability-owned derivation and narrow cross-capability orchestration — without duplicated orchestration or responsibility leakage.

When Criterion B is met, a read-planning architecture such as the previously falsified full-spine alternative MAY be reconsidered. It is not pre-approved.

### 3.7 Required response when promoted

If promoted:

1. stop implementation that depends on the unresolved boundary;
2. create/promote an exact DDR design gate;
3. identify which current owner boundaries are failing;
4. falsify the smallest repair first;
5. falsify any proposed generic planner/composition engine for semantic gravity before acceptance;
6. preserve already-correct predecessor decisions unless new authority genuinely supersedes them; and
7. record whether the problem is runtime semantic gravity, document/programme gravity, performance gravity, or a combination.

### 3.8 Current evidence and status

The IMP-06 E3 episode demonstrated:

- high semantic fan-in around Projection/Exposure/Audience/API boundaries;
- temptation to broaden `AudienceObservationContext` into operational state;
- temptation to make Exposure own more semantics than its element decision;
- temptation to introduce a universal composition/read-planning abstraction;
- the importance of distinguishing candidate-local `UNRESOLVED → WITHHOLD` from structural resolution failure; and
- document/programme gravity in the dense MS-PROT-027 amendment chain even though the runtime architecture remains substantially decomposed.

The current mitigation is intentionally narrow: MS-PROT-027 v1.10 defines a bounded result contract and explicitly prohibits acquisition/planning/authority transfer.

**Current decision:** Continue implementation under the accepted narrow boundary while retaining this item as **WATCHING — PERSISTENT**.

---

## 4. MS-WATCH-002 — Public Interaction Participation-Source / Programme Dependency Deadlock

**State:** **WATCHING — PROGRAMME/ARCHITECTURE DEPENDENCY**  
**Initial evidence:** IMP-06 S3 `Public Interaction Binding` remained `BLOCKED_DEPENDENCY` on capability-owned participation sources after Exposure resolution became otherwise available.  
**Current mitigation authority:** MS-PROT-049 v1.4, preserving MS-PROT-049 v1.0-v1.3; MS-PROT-046 v1.2 where applicable; composite MS-PROT-027; `MS-IMP-001.md`  
**Scope:** IMP-06 completion, Public Interaction Binding projection, and any later capability implementation expected to establish authoritative subject-interaction participation

### 4.1 Failure mode

Public Interaction Binding is correctly prohibited from inferring subject-interaction participation from Surface applicability, Exposure, merchant type, subject type, route, label or UI convention.

It therefore depends on authoritative participation facts owned by the capability that owns the interaction.

The programme risk appears if those production participation sources are scheduled only inside macro targets that themselves cannot become eligible until IMP-06 is complete:

```text
IMP-06 completion
        ↓
requires S3 Public Interaction Binding projection
        ↓
requires capability-owned participation sources
        ↓
participation sources implemented only by later capability macro target(s)
        ↓
later macro target(s) blocked HARD on IMP-06 completion
        ↓
programme dependency cycle / deadlock
```

The local S3 architecture may therefore be semantically correct while the macro implementation programme is impossible to complete in its present ordering.

### 4.2 Why this remains a watch after the design-gate resolution

The previously hypothetical cycle became concrete during the post-T4b IMP-06 macro applicability review.

Accepted MS-PROT-046 v1.2 requires the first production owner-qualified subject-interaction participation definition:

```text
Opportunity
    → enquiry / send-enquiry
```

That concrete production participation belongs to the Publication → Enquiry vertical slice under IMP-07, while `MS-IMP-001.md` HARD-blocks IMP-07 on complete IMP-06. Requiring that concrete source as a prerequisite for generic S3 completion therefore satisfied Criterion A below.

`IMP-06-S3-DG-001` was resolved by MS-PROT-049 v1.4 using the smallest repair: a generic owner-routed participation-source contract plus a generic fail-closed S3 projector for which an empty production-source registry is a valid state. Concrete positive participation sources remain with their owning capability vertical slices. The IMP-06 → IMP-07 HARD edge remains unchanged.

This resolves the current deadlock but does not eliminate the failure mode. A later interaction family could recreate the same pressure if generic infrastructure is again made dependent on positive evidence that only a HARD-blocked downstream capability may establish.

Therefore the item remains under surveillance.

### 4.3 Protective invariants

The watch SHALL NOT be resolved by weakening the semantic separation that caused it.

In particular:

1. **Exposure is not participation.** PUBLIC exposure cannot imply BOOK, ORDER, SEND_ENQUIRY or any other interaction.
2. **Surface is not participation authority.** S3 may project an owner-established participation fact but may not manufacture it.
3. **Merchant or subject classification is not participation authority.** Business category, resource type, route shape or presentation metadata must not become a substitute source.
4. **Programme pressure does not transfer semantic ownership.** A dependency cycle cannot be broken by moving Booking, Ordering, Enquiry or other capability semantics into generic Surface/Exposure code.
5. **Equivalent-existing implementation requires proof.** Existing code counts only where accepted authority, implementation, tests and conformance establish semantic equivalence under `IMPLEMENTATION-RULES.md`.
6. **Generic infrastructure completeness is not concrete participation-portfolio completeness.** Zero registered production sources may be valid only where the accepted generic source/projector boundary fails closed and does not pretend a concrete capability source exists.
7. **Macro sequencing may be repaired without reopening correct domain semantics.** If the failure is purely programme ordering, the repair should remain at the smallest responsible boundary unless falsification proves a semantic defect.

### 4.4 Surveillance questions

Re-evaluate this watch whenever S3, IMP-06 completion or a capability-owned participation source is traversed. The review SHALL answer:

- Which exact interaction families must S3 support for IMP-06 to be considered complete?
- Which accepted authority owns each required participation fact?
- Does a production source for that fact already exist?
- If not, which fine-grained or macro target is authorised to implement it?
- Is that target eligible before IMP-06 completes?
- Would implementing the minimum source inside IMP-06 preserve capability ownership, or would it pull later capability semantics forward artificially?
- Can generic S3 remain conforming with zero registered production participation sources under MS-PROT-049 v1.4?
- Is a proposed positive source actually capability-owned, or is Surface/Exposure inferring it?
- Does a future programme graph recreate a hidden cycle between generic infrastructure completion and later capability participation?

### 4.5 Escalation indicators

Set this watch to **ELEVATED** if any of the following is observed:

- another generic infrastructure node becomes blocked on a positive participation source owned only by a HARD-blocked later target;
- a concrete source is proposed inside a generic macro solely to satisfy sequencing rather than because its semantic owner belongs there;
- an implementation proposal attempts to infer interaction participation merely to unblock a programme gate;
- the same circular dependency recurs for another capability-owned interaction;
- a later capability cannot begin because an upstream macro is incomplete, while that macro cannot complete because the capability has not yet established participation truth;
- engineers propose duplicating capability participation semantics in Surface, Exposure or transport to escape programme ordering;
- generic S3 begins treating an empty production-source registry as authority for positive participation rather than as a fail-closed empty state.

### 4.6 Promotion criteria

Promote this watch item to a governed design/programme gate when either condition is proved:

**Criterion A — actual hard dependency cycle**

At least one participation source required for mandatory generic-spine completion can only be implemented under a macro target that is HARD-blocked by the incomplete upstream macro.

**Criterion B — generic-spine completion contradiction**

Accepted authority requires a generic spine to be complete before capability execution work begins, while simultaneously requiring the generic spine to contain capability-owned positive participation evidence that no eligible upstream target is authorised to establish.

Either criterion proves that the current programme graph cannot be executed without changing sequencing, completion semantics or target placement.

### 4.7 Required response when promoted

If promoted:

1. stop any attempt to break the cycle through semantic inference or ownership transfer;
2. create/promote an exact DDR/design-programme gate;
3. trace each required participation source to its semantic owner and implementation target;
4. determine whether the smallest repair is:
   - recognition of already-satisfied authoritative implementation,
   - a generic fail-closed owner-routed source boundary where semantically valid,
   - fine-grained target relocation while preserving macro authority,
   - capability-triggered/on-demand completion semantics,
   - macro dependency amendment, or
   - a justified change to macro completion criteria;
5. falsify each repair against `MS-IMP-001`, `IMPLEMENTATION-RULES.md`, MS-PROT-049 and capability ownership;
6. reject any repair that makes generic Surface/Exposure infer capability participation; and
7. refresh the fine-grained and macro dependency graphs after acceptance.

### 4.8 Current evidence and status

The post-T4b review established:

```text
S2 final PUBLIC/CUSTOMER Surface assembly
    CONFORMING_COMPLETE

S3 production Public Interaction Binding projection
    remaining generic Surface foundation

MS-PROT-046 v1.2
    requires first concrete production participation source:
    Opportunity → enquiry / send-enquiry

MS-IMP-001
    IMP-06 HARD → IMP-07

IMP-07
    first Publication → Enquiry production vertical slice
```

Therefore requiring the IMP-07 concrete participation source before generic S3/IMP-06 completion created an actual HARD programme cycle and satisfied Criterion A.

MS-PROT-049 v1.4 resolves `IMP-06-S3-DG-001` by establishing:

```text
capability-owned participation sources
        ↓
generic owner-routed S3 participation boundary
        ↓
zero production sources is valid and fail-closed
        ↓
zero positive bindings
```

The generic S3 infrastructure can now be implemented without importing Publication/Enquiry/Booking/Appointment/Ordering semantics into IMP-06. Concrete positive sources remain with their owning vertical slices and must be separately proven when introduced.

**Current decision:** The immediate S3 programme deadlock is mitigated by MS-PROT-049 v1.4. Keep `MS-WATCH-002` **WATCHING — PROGRAMME/ARCHITECTURE DEPENDENCY**. Verify during S3 implementation that the zero-source state remains genuinely fail-closed, and re-evaluate this watch whenever a concrete participation source or a similar cross-macro dependency is introduced.

---

## 5. Adding future watch items

A new watch item SHOULD include:

- stable `MS-WATCH-NNN` identity;
- persistent failure mode;
- current state;
- affected scope;
- evidence that caused the watch;
- current mitigations;
- observable indicators;
- explicit escalation/promotion criteria; and
- relationship to any DDR/design gates.

Watch items should describe failure mechanisms rather than temporary symptoms.

Examples of appropriate future watch subjects may include recurring transaction-boundary leakage, release-version combinatorics, cross-capability orchestration gravity, provider-coupling creep or tenancy-lifecycle amplification when concrete evidence justifies them.

---

## 6. Review invariant

The watch list SHALL be consulted during:

- architecture falsification of a watched boundary;
- implementation-level conformance review of that boundary;
- any redesign proposed because a watched failure has recurred; and
- closure of a related design gate.

Closing a design gate must answer:

> **Was the immediate decision resolved, or was the persistent failure mode actually eliminated?**

Unless evidence establishes the latter, the watch item remains active.
