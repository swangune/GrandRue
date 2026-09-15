# MS-PROT-027 v1.9 — Audience Observation Context Establishment & Surface-Bound Exposure Amendment

**Document ID:** MS-PROT-027  
**Version:** 1.9  
**Status:** **ACCEPTED after iterative proposal, review, falsification, ambiguity review, recommendation and explicit manual approval**  
**Approved:** 1 September 2026  
**Amends:** composite MS-PROT-027 through v1.8 only within Audience Observation Context establishment, audience admission, typed extension boundaries and API-surface-bound Exposure resolution  
**Closes:** `IMP-06-E3-DG-001`  
**Depends on:** MS-PROT-027 v1.1–v1.8; MS-PROT-028; MS-PROT-041; MS-PROT-043; MS-PROT-063; MS-PROT-071; ADR-010; ADR-011; ADR-013; ADR-014  
**Governed by:** MS-DESIGN-RULES-001; MS-IMPLEMENTATION-RULES-001 v1.4; MS-IMP-001  
**Purpose:** Establish the minimum trusted, exact-release and request-scoped Audience Observation Context required by E3 while preserving capability ownership, current evaluation, contextual guest access, audience/transport separation and opaque surface-bound Exposure results.

---

## 1. Governing decision

Main Street SHALL use a hybrid Audience Observation Context consisting of:

1. a small, closed and trusted context core;
2. optional typed capability-owned contributions;
3. current owner evaluation during Exposure resolution;
4. strict separation between observer audience, Exposure target audience and API surface;
5. contextual customer support without putting raw credentials in the context; and
6. opaque API-bound contexts and results without public internal unwrapping.

The context records trusted request facts. It is not a permission, relationship
verdict, policy snapshot, transport credential or continuation token.

Canonical:

```text
trusted request boundary
        ↓
exact API or internal execution scope
        ↓
current Active Configuration Release
        +
exact Semantic Registry Release
        ↓
closed observation subject
        +
validated typed owner contributions
        ↓
opaque Audience Observation Context
        ↓
surface binding, when API-originated
        ↓
one current audience-admission evaluation
        ↓
Projection Serviceability
        ↓
element-level Exposure
        ↓
opaque API-bound result or distinct internal result
```

---

## 2. Context meaning

`AudienceObservationContext` SHALL be opaque, immutable and request-scoped.

It proves:

- the exact captured `ActiveRelease`;
- the Semantic Registry Release derived from that Active Release;
- the exact `MerchantScope`;
- one established observation subject;
- one request binding; and
- zero or more validated capability-owned contributions.

It SHALL NOT contain:

- an Exposure verdict;
- a cached authorisation or relationship decision;
- a raw session, guest link, token or credential;
- a generic claims map;
- complete query or continuation affinity;
- an HTTP route;
- a caller-supplied scope, release or audience; or
- a public context-unwrapping operation.

A context established before a configuration switch remains evidence of that
bounded request capture. It does not assert that the captured release remains
current after the request boundary.

---

## 3. Trusted API establishment

The conceptual API entry point is:

```text
establishForApi(
    ApiContractIdentity,
    ApiTransportScopeEvidence
)
```

It SHALL:

1. resolve the exact `ApiContractRegistration`;
2. require `ApiContractKind.QUERY`;
3. invoke the registered transport-scope authority internally;
4. require a `MerchantApiTransportScope`;
5. resolve the merchant's current `ActiveRelease` internally;
6. require exact Merchant Scope equality;
7. derive the Semantic Registry Release from
   `ConfigurationRelease.semanticRegistryVersion()`;
8. require that release to be materialised for serving;
9. require Projection and Exposure registry snapshots for that exact release;
   and
10. retain the resolved API contract, surface and scope-rule provenance
    privately.

Callers cannot supply Merchant Scope, Active Release or Semantic Registry
Release as authority.

Exact registry identity is required. This amendment does not claim a digest for
code-registered definitions while `MS-PROT-027-V15-DQ-002` remains deferred.

---

## 4. Trusted internal establishment

The conceptual internal entry point is:

```text
establishForExecution(TrustedExecutionContext)
```

It SHALL:

- derive Merchant Scope from the Trusted Execution Context;
- resolve the current Active Release internally; and
- perform the same exact-release and registry checks as the API path.

A public or caller-facing `establishForActiveRelease` operation is prohibited.

Principal-free internal observation is unsupported until a concrete trusted
scope source is governed.

---

## 5. Established request

`EstablishedObservationRequest` SHALL be sealed and opaque.

Its package-private final implementation retains:

```text
ActiveRelease
MerchantScope
ObservationRequestBinding
derived Semantic Registry Release
optional resolved API provenance
```

The request binding proves common establishment within one bounded request. It
does not authorise cross-request reuse.

Established requests and contexts SHALL be non-serializable and SHALL NOT be
retained in persistence, caches, static fields or singleton state.

---

## 6. Closed subject hierarchy

The initial subject hierarchy is:

| Subject | Audience | Required evidence |
|---|---|---|
| Public | `PUBLIC` | Established request; no Trusted Execution Context |
| Customer principal | `CUSTOMER` | Established request, Trusted Execution Context and authentication provenance |
| Customer contextual | `CUSTOMER` | Established request, Trusted Execution Context and contextual-access proof; authentication optional |
| Merchant interactive | `MERCHANT` | Established request, Trusted Execution Context and authentication provenance |

Audience derives only from the subject subtype.

Merchant system/service observation is initially unsupported.

For an unauthenticated contextual guest, the trusted contextual-access boundary
establishes an attributable, non-credential `ExecutionPrincipal`. No
production guest route is enabled until an owning contextual-proof authority is
separately registered.

The current `ExecutionPrincipal` carries an identifier rather than a governed
principal-category vocabulary. E3 does not invent a category field.

---

## 7. Principal and authentication binding

For every authenticated subject:

```text
AuthenticationProvenance.identityIdentifier
    ==
TrustedExecutionContext.principal.identifier
```

The current `SessionRecord` SHALL also satisfy:

```text
SessionRecord.sessionIdentity
    ==
AuthenticationProvenance.sessionIdentifier

SessionRecord.identityReference
    ==
AuthenticationProvenance.identityIdentifier

SessionRecord.establishedAt
    ==
AuthenticationProvenance.authenticatedAt
```

Subject and request Merchant Scope SHALL match exactly.

Any mismatch rejects context establishment.

This equality governs ordinary interactive customer and merchant subjects.
Future delegation must explicitly represent:

- acting principal;
- represented subject; and
- current delegation authority.

Delegation must not weaken ordinary principal equality.

---

## 8. Contextual customer access

`EstablishedContextualAccessProof` SHALL be a public non-sealed
owner-extension interface.

Each owner supplies a final package-private implementation validated through an
exact runtime binding.

The proof carries conceptually:

```text
ObservationRequestBinding
MerchantScope
ExecutionPrincipal identifier
owner-qualified contextual-access kind
stable non-secret access binding
```

It carries no raw credential, token or guest link.

For authenticated contextual access, its principal must also match
authentication provenance. For a guest, it matches the trusted guest
`ExecutionPrincipal`.

The proof is valid only inside `CustomerContextualObservationSubject`. It is
not a general contribution.

E4 later asks the owning capability to revalidate current access and the exact
candidate relationship.

No initial production contextual-proof kind or raw encoding is authorised by
E3.

---

## 9. Typed capability contributions

`EstablishedObservationContribution` SHALL be a public owner-extension
interface exposing only:

```text
owner-qualified contribution kind
ObservationRequestBinding
MerchantScope
```

For each exact Semantic Registry Release, an immutable definition snapshot
declares:

- owner-qualified kind;
- permitted audiences; and
- cardinality, initially `SINGLE`.

The semantic definition SHALL NOT contain:

- a Java implementation class;
- evaluator;
- expression;
- relationship value;
- permission; or
- Exposure verdict.

A separate immutable runtime-binding snapshot maps an exact contribution kind
to an exact Java contract class.

Implementations are final and package-private to the owner.

Context establishment validates:

- exact definition;
- exact runtime binding;
- owner;
- implementation class;
- audience;
- request binding;
- Merchant Scope; and
- cardinality.

Contribution presence never satisfies an Exposure requirement. The owning E4
evaluator revalidates current authority.

The initial contribution registry may be empty. Code registration is permitted;
semantic-bundle encoding is not claimed.

---

## 10. Audience separation

Three independent facts remain distinct:

```text
Observation subject
    → observer audience

ExposureElementContract.audience
    → permitted target audience

ApiContractRegistration.surface
    → transport/trust surface
```

None substitutes for another.

Exact observer/element audience mismatch produces `WITHHOLD`.

API surface does not manufacture audience.

---

## 11. Current audience admission

Before evaluating one candidate set, Exposure SHALL perform one
audience-admission evaluation at the start of that resolution invocation.

It SHALL be:

- current;
- invocation-bound;
- performed once for the candidate set;
- not repeated per field;
- not stored in the context; and
- not cached across requests.

Initial requirements are:

| Subject | Current admission |
|---|---|
| Public | Applicable surface-wide platform protection |
| Customer principal | Authentication currentness and applicable surface-wide protection |
| Customer contextual | Authentication currentness when present, current contextual-owner global authority, and applicable surface-wide protection |
| Merchant interactive | Authentication currentness, matching current Controller relationship or active Merchant Membership, and applicable surface-wide protection |

Controller or Membership evidence must match the exact subject principal and
Merchant Scope.

Device, privilege, Controller-only and candidate-specific relationship
requirements remain element-specific unless separately governed as
surface-wide.

The evaluation instant is captured once at the start of the invocation.

---

## 12. Authentication currentness

Runtime/authentication owns:

```text
AuthenticationSessionCurrentnessAuthority
    .evaluate(AuthenticationProvenance)
```

It uses:

- `SessionRecordStore.sessionByIdentity`;
- `IdentitySecurityGenerationAuthority`; and
- `Clock`.

Closed results are:

```text
CURRENT
NOT_CURRENT
UNRESOLVED
```

`CURRENT` requires:

- exact session identity;
- exact identity;
- exact establishment time;
- non-revoked session;
- non-expired session; and
- equality with the current Identity security generation.

Unknown or unavailable currentness never admits.

---

## 13. Admission result

`AudienceObservationAdmissionResult` SHALL be sealed:

```text
Admitted
    invocation binding
    evaluatedAt

Rejected
    invocation binding
    evaluatedAt
    exact failure
```

Failures include:

- authentication not current;
- authentication currentness unresolved;
- merchant association unsatisfied;
- merchant association unresolved;
- contextual authority unavailable or unresolved;
- platform protection unsatisfied or unresolved; and
- unsupported audience mode.

Admission rejection is distinct from:

- context-establishment failure;
- Projection Serviceability; and
- element-level `WITHHOLD`.

---

## 14. API-surface binding

An API-established context privately retains exact API provenance.

`ApiAudienceObservationContext` SHALL be opaque and sealed, exposing only:

```text
ApiContractIdentity
ApiSurfaceClass
```

It SHALL NOT expose `context()` or any generic unwrap operation.

The binder SHALL:

1. re-resolve the exact API registration;
2. require `QUERY`;
3. require exact provenance and Merchant Scope equality; and
4. enforce:

| Surface | Permitted subject |
|---|---|
| `PUBLIC` | Public |
| `CUSTOMER_CONTEXTUAL` | Customer principal or customer contextual |
| `MERCHANT_OPERATIONAL` | Merchant interactive |

An internally established context cannot be rebound as an API context.

The bound implementation, binder and API resolver SHALL be colocated or
otherwise encapsulated so the resolver can inspect the private internal context
without creating a public unwrap path.

---

## 15. API and internal Exposure outputs

The API order is:

```text
registered query
    → trusted transport scope
    → established request/context
    → API-surface binding
    → current audience admission
    → Projection Serviceability
    → element-level Exposure
    → opaque API-bound result
    → representation
```

`ApiExposureResolution` SHALL expose only:

- exact API contract identity;
- exact API surface; and
- `ApiExposedElementSet`.

It SHALL expose no:

- raw Audience Observation Context;
- raw admission result; or
- internal Exposure result.

Its private implementation validates that context, admission and result refer
to the same request and invocation.

`InternalExposureResolution` is distinct. API packages and representation code
SHALL NOT depend on it.

The wrapper remains shallow: one internal Exposure computation is privately
bound to the appropriate output. Exposure logic is not duplicated.

Physical controller/endpoint-to-contract mapping remains downstream API
implementation and architecture-test work.

---

## 16. Candidate observation

E4 receives `ExposureCandidateObservation` containing:

- `ExposableElementReference`; and
- optional owner-qualified candidate instance reference.

If a resource-specific requirement needs an instance and the reference is
absent:

```text
UNRESOLVED
    → WITHHOLD
```

A candidate reference cannot manufacture relationship authority.

---

## 17. Continuation boundary

The context exposes only a stable `ObservationSubjectBinding`:

| Subject | Stable binding |
|---|---|
| Public | Public audience |
| Customer principal | Principal identifier |
| Customer contextual | Principal identifier plus owner/access binding |
| Merchant interactive | Principal identifier |

It is affinity evidence, not authority.

T4b owns complete continuation affinity:

```text
query contract
+ canonical query parameters, filters and sort
+ Merchant Scope
+ audience
+ ObservationSubjectBinding
```

Request binding is excluded. Every continuation request establishes a new
context and repeats all current evaluations.

A contextual path without a stable non-secret binding cannot issue
continuation tokens.

---

## 18. Establishment outcomes

Sealed exact results SHALL govern:

- observation-request establishment;
- Audience Observation Context establishment; and
- API-surface binding.

Failure categories include:

- API contract absent or not a query;
- scope authority absent or failed;
- non-merchant API scope;
- active configuration unresolved;
- Merchant Scope mismatch;
- semantic release not materialised;
- Projection or Exposure registry release mismatch;
- internal execution scope unresolved;
- subject scope mismatch;
- authentication/principal/provenance mismatch;
- contextual proof owner, kind, class, request, scope or principal mismatch;
- contribution definition, binding, audience, request, scope or duplicate
  failure; and
- API provenance, contract, surface, scope or audience mismatch.

Establishment failure is not `WITHHOLD` and is not Projection Serviceability.

---

## 19. Construction and architecture enforcement

Main Street-owned closed contracts SHALL use public sealed interfaces with
final package-private implementations.

Capability-extension interfaces are non-sealed only where owner extension is
required and are validated through exact immutable runtime bindings.

There SHALL be no public authoritative constructors or deserializers.

Architecture tests SHALL prohibit:

- request/context caching or persistence;
- static/singleton context retention;
- direct construction outside trusted factories;
- public generic unwrapping;
- API dependencies on internal Exposure outputs;
- representation accepting internal Exposure results; and
- authority establishment from caller-supplied scope, audience, release, class
  or verdict.

---

## 20. Currentness and ownership

The applicable owner remains responsible for current evaluation of:

- session and security generation;
- Controller relationship;
- Merchant Membership;
- contextual access;
- privacy and Resource Protection;
- merchant Exposure choice; and
- candidate-specific capability relationship.

The context never converts these into cached facts.

---

## 21. Explicit non-goals

E3 introduces no:

- database or cache;
- raw token, link or credential encoding;
- continuation implementation;
- HTTP route, controller or representation mapping;
- semantic-bundle encoding;
- generic rules engine;
- E4 capability evaluator;
- Exposure telemetry;
- delegation;
- merchant system/service observation; or
- command authority.

`MS-PROT-027-V15-DQ-002` remains deferred.

Raw contextual credential encoding remains under the inactive
`ADR-014-DQ-011` decision.

---

## 22. Hard invariants

1. The context is opaque, immutable and request-scoped.
2. Scope and Active Release are established internally.
3. Semantic release derives from the exact Active Release.
4. Projection and Exposure registries match that release exactly.
5. API establishment requires an exact registered query and trusted scope.
6. Audience derives only from the closed subject subtype.
7. Subject and request Merchant Scope match exactly.
8. Authenticated subject, provenance and current session identities match.
9. Contextual proof is distinct from contributions and raw credentials.
10. Owner extensions are typed, owner-qualified and exact-bound.
11. Contribution presence never becomes authority.
12. Subject audience, element audience and API surface remain distinct.
13. Admission is current, fail-closed and once per invocation.
14. Controller or Membership evidence matches principal and Merchant Scope.
15. API-bound context and result have no public internal unwrap.
16. API and internal Exposure outputs remain distinct.
17. Missing required candidate identity fails closed.
18. Subject binding is continuation affinity evidence, not authority.
19. Every continuation request establishes a new context.
20. Contexts are not serialized, persisted or cached.
21. Unknown or unavailable authority never admits or exposes.
22. E3 does not implement E4, tokens, continuation, HTTP or bundles.

---

## 23. Complete falsification record

The earlier proposals did not pass every attack. They were revised into the
accepted hybrid.

| # | Attack | Earlier finding | Final controlling boundary | Result |
|---:|---|---|---|---|
| 1 | Monolithic closed context | Central context would absorb capability facts and grow indefinitely | Closed core plus typed owner extensions | **PASS** |
| 2 | Open claims map | Arbitrary keys/classes could become undeclared authority | Owner-qualified definition and exact runtime-binding snapshots | **PASS** |
| 3 | Closed core with no extension | Legitimate contextual and future capability evidence could not be represented | Narrow non-sealed owner interfaces | **PASS** |
| 4 | Audience/owner collision | Same local contribution name could collide across capabilities | Owner-qualified kind identity | **PASS** |
| 5 | Semantic definition embeds Java class | Runtime class would become semantic meaning | Semantic definition and runtime binding are separate | **PASS** |
| 6 | Java sealed hierarchy across capability packages | Capability implementations could not legally extend the closed hierarchy | Only core hierarchy sealed; extensions non-sealed and exact-bound | **PASS** |
| 7 | Raw contextual token/reference in context | Failed secrecy, transport separation and replay boundaries | Only stable non-secret contextual binding enters proof | **PASS** |
| 8 | Secure guest without authentication | Authenticated-only model excluded valid contextual guests | Trusted guest principal plus owner proof; authentication optional | **PASS** |
| 9 | Guest path fabricates authentication | Optional authentication could be misrepresented as a session | Trusted Execution Context supports non-session principals; no fabricated provenance | **PASS** |
| 10 | Contextual proof treated as ordinary contribution | Presence could incorrectly satisfy access | Dedicated proof type valid only in contextual subject | **PASS** |
| 11 | Forged Merchant Scope | Caller could target another merchant | Scope established by registered transport authority or Trusted Execution Context | **PASS** |
| 12 | Forged API registration/surface | Caller could assert query or surface identity | Exact registry resolution at establishment and binding | **PASS** |
| 13 | Forged Active Release | Caller could select an arbitrary configuration | Current activation resolves Active Release internally | **PASS** |
| 14 | Semantic release downgrade | Caller could select an older materialised release | Release derived from current exact Active Release | **PASS** |
| 15 | Public route without trusted merchant context | Public audience could be mistaken for merchant scope | Public API still requires registered merchant-scope authority | **PASS** |
| 16 | Active configuration changes during request | Re-resolving current could mix configurations | Exact Active Release captured once per request | **PASS** |
| 17 | Configuration changes but semantic release stays the same | Release identifier alone would miss configuration change | Context retains exact Active Release, not only semantic version | **PASS** |
| 18 | Stale Controller relationship | Captured merchant authority could survive transfer | Current admission rechecks matching Controller or Membership | **PASS** |
| 19 | Stale Merchant Membership | Removed staff could remain admitted | Membership rechecked at invocation | **PASS** |
| 20 | Revoked or expired session | Context-created authentication could become stale | Session currentness re-evaluated at invocation | **PASS** |
| 21 | Identity security generation rotates | Existing session might survive credential/security reset | Current generation equality is mandatory | **PASS** |
| 22 | Principal/provenance mismatch | One principal could present another identity's session | Exact principal, provenance and Session Record equality | **PASS** |
| 23 | Hypothetical delegation weakens identity equality | Generic exception could permit impersonation | Delegation excluded; future model must represent actor and subject explicitly | **PASS** |
| 24 | Current checks repeated per field | Correct but unnecessarily expensive and inconsistent within one result | One captured evaluation instant and one admission per invocation | **PASS** |
| 25 | Current admission cached across requests | Stale relationship/session could be reused | Admission is invocation-bound and non-cacheable | **PASS** |
| 26 | Contribution presence becomes permission | Typed evidence could be mistaken for satisfied relationship | Only current owner evaluator decides E4 requirement | **PASS** |
| 27 | Owner authority unavailable | Availability failure might default permissively | `UNRESOLVED` fails closed to rejection or withhold | **PASS** |
| 28 | Subject audience inferred from route | Transport could manufacture semantic audience | Audience derives only from subject subtype | **PASS** |
| 29 | Element audience ignored | Correct subject could receive wrong-target element | Exact audience mismatch yields `WITHHOLD` | **PASS** |
| 30 | API wrapper exposes `context()` | Representation could bypass resolver boundary | No public unwrap method | **PASS** |
| 31 | Internal Exposure result leaks into API | API could serialize unbounded internal evidence | Separate opaque API result; architecture dependency prohibition | **PASS** |
| 32 | Wrapper duplicates Exposure logic | API/internal behavior could diverge | One private computation, shallow output binding | **PASS** |
| 33 | Missing candidate instance reference | Resource-specific requirement could be evaluated generically | `UNRESOLVED → WITHHOLD` | **PASS** |
| 34 | Subject binding reused as continuation token | It lacks query and current authority | T4b owns complete query/scope/audience affinity | **PASS** |
| 35 | Request binding placed in continuation | New request could never match, or old authority could be replayed | Request binding excluded; new context established | **PASS** |
| 36 | Context serialized or cached | Old authority facts could be replayed | Non-serializable types and architecture tests | **PASS** |
| 37 | Semantic-bundle encoding implicitly claimed | Code registration could improperly resolve E2 | Explicit non-claim; DQ-002 remains deferred | **PASS** |
| 38 | Contextual-access encoding implicitly claimed | E3 infrastructure could invent guest-token design | No initial proof kind; ADR-014-DQ-011 remains inactive | **PASS** |
| 39 | Merchant service principal disguised as interactive merchant | System execution could inherit human admission | Service/system observation unsupported | **PASS** |
| 40 | E3 becomes command authority | Read context might leak into mutation eligibility | Explicit read-only/non-authoritative boundary | **PASS** |

**Falsification verdict:** **PASS**

No unresolved counterexample remains inside E3's accepted scope. Deferred
questions are excluded rather than guessed.

---

## 24. Mandatory ambiguity review

| DESIGN-RULES check | Result |
|---|---|
| Normative statements constrain materially different implementations | **PASS** |
| Each authoritative fact has one owner | **PASS** |
| Overloaded terms are qualified | **PASS** |
| Audience, surface, admission, serviceability and Exposure are separated | **PASS** |
| Conditions are determinable | **PASS** |
| References and pronouns are unambiguous | **PASS** |
| Material cardinalities are explicit | **PASS** |
| Identity and equality rules are explicit | **PASS** |
| Transaction boundary is correctly non-applicable to E3 | **PASS** |
| Retry/replay boundary is explicit | **PASS** |
| Establishment failure and withholding are distinguishable | **PASS** |
| Configuration, runtime and projection remain separate | **PASS** |
| Applicability, authorisation, readiness and Exposure remain separate | **PASS** |
| Provider facts do not become business truth | **PASS / not applicable** |
| Merchant policy and platform protection remain separate | **PASS** |
| AI inference is not involved | **PASS / not applicable** |
| Examples remain subordinate to rules | **PASS** |
| Negative ownership boundaries are explicit | **PASS** |
| Cross-document references resolve without chat | **PASS** |
| Amendment scope is explicit | **PASS** |
| Open questions are resolved or safely deferred | **PASS** |
| Invariants are testable | **PASS** |
| Another engineer can implement E3 without inventing a material rule | **PASS** |

**Ambiguity review:** **PASS**

---

## 25. Alternatives, trade-offs and accepted consequences

| Alternative | Reason rejected |
|---|---|
| Monolithic complete context | Centralises capability authority and becomes stale |
| Fully open extension map | Cannot constrain ownership, type or meaning |
| Closed core only | Cannot support legitimate contextual/capability evidence |
| Store current verdicts in context | Converts request facts into stale permissions |
| Authenticate every customer | Excludes legitimate secure guest/contextual access |
| Put raw token in context | Leaks credential and transport concerns |
| Infer audience from route | Conflates transport and semantics |
| Give API representation internal results | Weakens encapsulation and authority boundaries |
| Run global admission per field | Adds cost and inconsistent evaluation instants |
| Implement bundle/token/continuation together | Crosses independent deferred decisions |

Accepted costs:

- more small types and package boundaries;
- exact registration infrastructure;
- current session and relationship reads;
- fail-closed behaviour during authority outages;
- an initially empty contribution/proof registry; and
- separate API/internal wrappers.

These costs are accepted because they localise trust and prevent stale, forged
or transport-derived authority.

Residual risks:

- additional current authority reads may affect latency;
- fail-closed outages may reduce availability;
- future delegation requires a separate design;
- merchant system execution requires a separate design;
- contextual guest operation remains inactive until an owner proof and raw
  encoding are governed; and
- semantic-bundle encoding remains deferred.

None requires guessing inside the accepted E3 implementation boundary.

---

## 26. Corpus-conformance finding

This amendment:

- preserves MS-PROT-027's separation of Projection Serviceability and Exposure;
- uses exact v1.6 Exposure and v1.7 Projection registries;
- does not alter v1.8 serviceability evaluation;
- preserves configuration/runtime separation;
- preserves capability ownership;
- preserves current relationship authority;
- does not make API transport semantic authority;
- does not invent E2 bundle encoding;
- does not implement E4;
- remains inside the IMP-06 E3 node; and
- provides testable construction, release-affinity, currentness and
  package-dependency invariants.

**Corpus conformance:** **PASS**

---

## 27. Implementation consequence

`IMP-06-E3-DG-001` is resolved. E3 becomes `READY`, not complete.

The first RED→GREEN E3 implementation node may cover:

- opaque request/context/subject contracts;
- trusted API/internal factories;
- exact scope, release and provenance validation;
- authentication-currentness and audience-admission contracts;
- empty-capable typed contribution/runtime-binding infrastructure;
- contextual-proof infrastructure without an initial registered proof kind;
- API-surface binding;
- opaque API/internal result boundaries; and
- architecture and authority tests.

Implementation SHALL stop before:

- raw contextual credentials;
- E4 evaluators;
- actual Exposure decisions;
- continuation;
- HTTP mapping;
- semantic-bundle encoding;
- delegation; or
- merchant system execution.

E4, T1b4 and T4b remain blocked until E3 implementation and their other
dependencies are complete.

This amendment closes a design gate. It does not claim E3 production
implementation or IMP-06 completion.

---

## 28. Amendment effect

This amendment:

1. preserves composite MS-PROT-027 Projection and Exposure authority;
2. resolves only the E3 Audience Observation Context design gate;
3. establishes the trusted closed-core plus typed owner-extension hybrid;
4. preserves contextual guest access without deciding raw credential encoding;
5. requires current audience admission and owner evaluation;
6. binds API contexts/results to exact registered query surfaces without public
   unwrapping;
7. preserves E2, E4, T1b4 and T4b boundaries and dependencies; and
8. leaves all unrelated deferred decisions unchanged.

---

## 29. Acceptance statement

> **Audience Observation Context is a trusted, exact-release, request-scoped
> observation fact: Main Street establishes scope and release internally,
> derives audience from a closed subject, validates only exact typed
> owner-contributions, re-evaluates current admission and capability authority
> at Exposure time, and exposes API results only through exact surface-bound
> opaque wrappers. It is never a caller assertion, raw credential, cached
> permission, transport shortcut or continuation token.**

**Review:** PASS  
**Falsification:** PASS  
**Ambiguity review:** PASS  
**Corpus conformance:** PASS  
**Recommendation:** ACCEPT — approved by the user  
**Manual approval:** GRANTED — 1 September 2026  
**Governance verdict:** **ACCEPTED**
