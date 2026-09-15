# IMP-06 Read, Exposure & Transport Spine — Fine-Grained Conformance Baseline

**Date:** 30 August 2026
**Macro authority:** `designs/MS-IMP-001.md`, IMP-06
**Implementation governance:** `designs/IMPLEMENTATION-RULES.md`
**Status:** **PARTIALLY_CONFORMING**; **E1, P1, P2, S1, R1, T1A, T1B1-T1B3, T2A-T2C, T3A-T3B AND T4A COMPLETE; E3 CONTEXT, TYPED EXTENSIONS, ADMISSION AND API BINDING GREEN; EXPOSURE RESULT SHAPE BLOCKED DESIGN**
**Starting committed frontier:** `ae350ad`
**Approved design resolutions:** `35ec5c1` — MS-PROT-027 v1.6; `31652bc` — MS-PROT-027 v1.7; `f5f6477` — MS-PROT-027 v1.8; `7875bf1` — MS-PROT-027 v1.9

This document is implementation evidence and dependency-graph navigation only.
It does not create semantic or architectural authority.

### Current branch verification note — 1 September 2026

The currently implementable E3 responsibilities reached a verified GREEN baseline at
`c9b60f8fdcc5d9e122f5689ef78efaa7d190b02e` in GitHub Maven Tests #1333 /
run `33492591779`:

```text
production Java sources compiled         867
test Java sources compiled               271
unit / conformance tests                 766 PASS
PostgreSQL integration tests             298 PASS
total Maven tests                        1064 PASS
Flyway migrations                        51 validated / V51 current
failures / errors / skipped              0 / 0 / 0
result                                   BUILD SUCCESS
```

E3 now covers trusted request establishment; closed Public, Customer-principal,
Customer-contextual and Merchant-interactive contexts; authentication currentness;
one invocation-bound admission; exact-release typed contribution definitions and
runtime bindings; contribution validation; and opaque API-context binding. The
remaining API/internal Exposure-result boundary is `BLOCKED_DESIGN`: v1.9 names
`ApiExposedElementSet` but neither defines its representation/invariants nor
points to an existing implementation. E4, T1b4 and T4b remain blocked; raw
contextual credentials, HTTP, continuation and semantic-bundle encoding are not
claimed.

---

## 1. Macro eligibility

IMP-05 is `CONFORMING_COMPLETE`, so the IMP-05 HARD dependency declared by
MS-IMP-001 is satisfied and IMP-06 is the current eligible macro target.

IMP-06 requires:

```text
Projection Contract infrastructure
Projection Serviceability
Exposure resolution
Surface Contributions
Audience Observation Context
Resource Protection
API Contract infrastructure
trusted transport-context resolution
problem/outcome mapping foundation
bounded query/pagination infrastructure
```

No broad endpoint catalogue is required at this stage.

---

## 2. Existing-code assessment

Repository inspection found reusable production foundations for:

- immutable Surface Contribution definitions and exact-release registry
  snapshots;
- static PUBLIC/CUSTOMER/MERCHANT composition and contextual Surface
  eligibility;
- Projection Serviceability, public/customer Exposure and Resource Protection
  ports used by existing application paths;
- cross-capability application outcome classification; and
- high-assurance authentication/session infrastructure from IMP-04.

Those foundations do not by themselves prove IMP-06 complete. E1 and P1 now
provide the exact-release Exposure Element Contract and Projection Contract
registries, and P2 provides deterministic exact-release Projection
Serviceability evaluation. The repository now contains the governed E3
request/context/admission, typed-extension and API-context-binding spine, but
still does not contain a production Exposure resolver, a governed
`ApiExposedElementSet`, a complete production query definition or bounded
cursor/query execution infrastructure.

Prototype controllers remain prototype/delivery evidence rather than the
production transport spine required by IMP-06.

---

## 3. Fine-grained dependency graph

```text
IMP-06
  ├── P. Projection
  │     ├── P1 exact Projection Contract Java/registration
  │     │      COMPLETE — MS-PROT-027 v1.7 + focused RED → GREEN
  │     └── P2 production Projection Serviceability evaluation
  │            COMPLETE — MS-PROT-027 v1.8 + focused RED → GREEN
  │
  ├── S. Surface
  │     ├── S1 Surface Contribution infrastructure assessment
  │     │      COMPLETE — existing responsibilities classified and verified
  │     ├── S2 final PUBLIC/CUSTOMER Surface assembly
  │     │      BLOCKED_DEPENDENCY on exact P2 evidence orchestration and E4
  │     └── S3 production Public Interaction Binding projection
  │            BLOCKED_DEPENDENCY on capability-owned participation sources and E4
  │
  ├── E. Exposure
  │     ├── E1 exact Exposure Element Contract Java/registration
  │     │      COMPLETE — MS-PROT-027 v1.6 + focused RED → GREEN
  │     ├── E2 exposure semantic-bundle encoding/materialisation
  │     │      BLOCKED_DESIGN — MS-PROT-027-V15-DQ-002
  │     ├── E3 server-established Audience Observation Context
  │     │      PARTIALLY_IMPLEMENTED — context/extensions/admission/API binding GREEN;
  │     │      result boundary BLOCKED_DESIGN on undefined ApiExposedElementSet
  │     └── E4 deterministic production Exposure resolver
  │            BLOCKED_DEPENDENCY on E2/E3 result shape and owner-policy/evaluator wiring
  │
  ├── R. Resource Protection
  │     └── R1 production authority assessment
  │            COMPLETE — existing bounded-window PostgreSQL authority verified
  │
  └── T. API / transport / bounded query
        ├── T1 API Contract infrastructure
        │     ├── T1a common logical registration spine
        │     │      COMPLETE — focused RED → GREEN
        │     └── T1b kind-specific contract definitions
        │            ├── T1b1 command definition
        │            │      COMPLETE — focused RED → GREEN
        │            ├── T1b2 callback definition
        │            │      COMPLETE — focused RED → GREEN
        │            ├── T1b3 media-transfer definition
        │            │      COMPLETE — focused RED → GREEN
        │            └── T1b4 query definition
        │                   BLOCKED_DEPENDENCY on E3/E4
        ├── T2 trusted transport-context resolution
        │     ├── T2a exact surface/scope establishment authority spine
        │     │      COMPLETE — focused RED → GREEN
        │     ├── T2b session/Controller merchant-context establishment
        │     │      COMPLETE — existing current-authority path verified
        │     ├── T2c staff operational context establishment
        │     │      COMPLETE — existing membership/device path verified
        │     └── T2d concrete public/customer/integration/platform adapters
        │            PENDING T2a and concrete source/portfolio dependencies
        ├── T3 problem/outcome mapping
        │     ├── T3a transport outcome/problem semantic vocabulary
        │     │      COMPLETE — exact closed vocabularies + focused RED → GREEN
        │     ├── T3b owner-qualified safe problem mapping authority spine
        │     │      COMPLETE — exact owner/evidence affinity + focused RED → GREEN
        │     └── T3c concrete owner/adapter mappings
        │            BLOCKED_DEPENDENCY on each real API adapter/owner source
        └── T4 bounded cursor/query infrastructure
              ├── T4a bounded collection contract definition
              │      COMPLETE — static responsibilities + focused RED → GREEN
              ├── T4b continuation affinity/validation authority
              │      BLOCKED_DEPENDENCY on T1b4 and E3 Audience context
              └── T4c concrete opaque encoding/adapter
                     PENDING T4b and a real query adapter
```

Graph refinement does not change the IMP-06 macro scope or HARD dependency.

---

## 4. Completed node E1

### Authority

- MS-PROT-027 v1.2–v1.5;
- approved MS-PROT-027 v1.6;
- ADR-010, ADR-011 and ADR-013 exact-release architecture; and
- MS-IMP-001 / IMPLEMENTATION-RULES.

### Implemented contract

The node adds:

- `ExposureElementContractIdentity`;
- `ExposableElementReference`;
- `MerchantExposureChoiceSourceReference`;
- `ExposureRequirementReference`;
- immutable `ExposureElementContract`;
- immutable exact-release `ExposureElementContractRegistrySnapshot`; and
- the nine-contract initial PUBLIC Merchant Presence portfolio.

The snapshot rejects duplicate contract identities, retains immutable values,
requires an exact requested release and never falls forward to another release.
An absent/mismatched lookup remains unresolved for later fail-closed resolver
handling.

No resolver, database, codec, API, cache, policy DSL or merchant-policy copy was
introduced.

### RED → GREEN

RED was established by the focused test failing compilation on the seven absent
production types.

GREEN:

```text
mainstreet.surface.ExposureElementContractRegistrySnapshotTest
tests:     5
failures:  0
errors:    0
skipped:   0
result:    BUILD SUCCESS
```

The tests prove:

- exact release plus exact identity lookup;
- wrong-release and missing-contract non-resolution;
- duplicate-registration rejection;
- immutable contract/registry collections;
- non-blank owner-qualified identifiers; and
- the exact nine-contract initial PUBLIC portfolio, baselines and Profile
  owner-policy references.

---

## 5. Completed node P1

### Authority

- MS-PROT-027 v1.1, v1.3 and v1.4;
- approved MS-PROT-027 v1.7;
- MS-PROT-041 v1.1, MS-PROT-050 and MS-PROT-051 v1.1 source ownership;
- ADR-010, ADR-011 and ADR-013 exact-release architecture; and
- MS-IMP-001 / IMPLEMENTATION-RULES.

### Implemented contract

The node adds:

- `ProjectionContractIdentity`;
- `ProjectionSourceDependencyReference`;
- the closed `ProjectionContractApplicabilityTrigger` vocabulary;
- the closed `ProjectionMaterialisationKind` vocabulary;
- `ProjectionPolicyReference`;
- `ProjectionReadUseIdentity` and `ProjectionReadUseContract`;
- immutable `ProjectionContractDefinition`;
- immutable exact-release `ProjectionContractRegistrySnapshot`; and
- `InitialProjectionContractPortfolio` with exactly Merchant Presence and Main
  Street Calendar.

The representation retains owner-qualified identities, trigger/source sets,
request-scoped materialisation, freshness/read-use policy references and
optional revocation/rebuild policy references. Construction rejects empty
required sets, duplicate read-use identities and duplicate contract identities.
Lookup is exact-release and exact-identity only.

The portfolio preserves Merchant Presence reduced truthful serviceability and
Calendar's committed-work versus availability-oriented distinction as static
policy references. It does not evaluate those policies.

No evaluator, projection store, cache, Redis dependency, checkpoint, worker,
API, executable DSL, Exposure outcome or command authority was introduced.

### RED → GREEN

RED was established by focused test compilation failing on the ten absent
production types.

GREEN:

```text
mainstreet.surface.ProjectionContractRegistrySnapshotTest
tests:     5
failures:  0
errors:    0
skipped:   0
result:    BUILD SUCCESS
```

The tests prove:

- exact release plus exact identity lookup;
- wrong-release and missing-contract non-resolution;
- duplicate contract and duplicate read-use rejection;
- non-blank owner-qualified identities;
- immutable definition/registry collections;
- structurally non-empty trigger/source/read-use sets; and
- the exact initial Merchant Presence and Calendar identities, triggers,
  source cardinalities, materialisation and read-use identities.

The combined focused Projection, Exposure, design-corpus and programme gate
suite passed 15 tests with zero failures, errors or skips.

---

## 6. Completed node P2

### Authority

- composite MS-PROT-027 through approved v1.8;
- MS-PROT-041 v1.1, MS-PROT-050 and MS-PROT-051 v1.1 source ownership;
- ADR-010, ADR-011 and ADR-013 exact-release architecture; and
- MS-IMP-001 / IMPLEMENTATION-RULES.

### Implemented contract

The node adds:

- closed policy-category, source availability/completeness/revocation,
  serviceability-outcome and reason-code vocabularies;
- immutable versioned evaluator identities and exact category-qualified policy
  bindings;
- an immutable exact-release evaluator registry without latest/default lookup;
- server-established source evidence retaining exact progress identifiers,
  evidence identity and observation time rather than caller verdicts;
- immutable evaluation requests, policy assessments and structured results;
- deterministic fail-closed evaluation with exact contract/read-use/evidence
  set validation and conservative policy composition; and
- exact initial Merchant Presence, Calendar committed-work and Calendar
  availability evaluators.

The result retains every consumed exact policy/evaluator identity, the complete
source-evidence set, stable reasons and omitted sources. Missing contract,
read-use, evaluator or exact evidence fails closed. Merchant Presence requires
a current descriptor and may truthfully omit other unproven elements. Calendar
may preserve independently current commitments during optional-source failure,
but current availability requires every applicable source to be current.

No source query orchestration, projection store, cache, Redis dependency,
checkpoint, worker, API mapping, Exposure resolver, Surface authority or
command authority was introduced.

### RED → GREEN

RED was established by focused test compilation failing on the absent P2
evaluator, evidence, request, result and registry types.

GREEN:

```text
mainstreet.surface.ProjectionServiceabilityEvaluationEngineTest
tests:     6
failures:  0
errors:    0
skipped:   0
result:    BUILD SUCCESS
```

The tests prove:

- exact release/category/policy evaluator lookup and duplicate rejection;
- currentness derived from exact progress/evidence values rather than caller
  booleans or elapsed time;
- fail-closed wrong-release, unresolved-evaluator and evidence-set mismatch;
- full, reduced and unserviceable Merchant Presence behavior;
- Calendar committed-work survival without invented availability;
- trusted `NOT_APPLICABLE` conditional-source handling; and
- deterministic immutable exact evaluator/evidence provenance.

The combined focused Projection, Exposure, design-corpus and programme gate
suite passed 21 tests with zero failures, errors or skips.

---

## 7. Canonical verification

The current full repository gate ran from an isolated clean export of committed
`development` at `7a58fd1` plus exactly the two scoped T4a production/test
files. The six unrelated
user-owned worktree changes were not copied into the verification directory.

Before launch, process and database checks found no competing MainStreet
Maven/JVM and no non-idle `mainstreet_test` session.

```text
mvn --batch-mode clean verify -Ppostgres-it

production Java sources compiled         830
test Java sources compiled               262
unit / conformance tests                 720 PASS
PostgreSQL integration tests             298 PASS
total tests                              1018 PASS
Flyway migrations                        51 validated / V51 current
failures / errors / skipped              0 / 0 / 0
result                                   BUILD SUCCESS
```

The canonical run used PostgreSQL 18.6 through the dedicated
`mainstreet_test` database. Preflight confirmed that the database existed, no
non-idle test session or competing MainStreet Maven/JVM was present, and the
applied V50 checksum matched the resolved local checksum `-2019768493`.

The canonical invocation explicitly supplied the three required
`MAINSTREET_TEST_POSTGRES_*` process variables. No migration was added or
modified by T4a.

---

## 8. Completed node S1

### Authority

- composite MS-PROT-049 v1.0-v1.3;
- composite MS-PROT-027 through v1.8;
- MS-PROT-036 and MS-PROT-037 composition ownership;
- MS-PROT-040 residual-management authority; and
- MS-IMP-001 / IMPLEMENTATION-RULES.

### Existing conforming responsibilities

Repository inspection and focused verification establish that the existing
production Surface implementation already provides:

- immutable capability-qualified contribution identities/definitions and an
  exact Semantic Registry Release-affined registry snapshot;
- exact-release materialisation from the executable merchant model, including
  rejection of absent operation references and release mismatch;
- deterministic static grouping by audience and registered composition target
  without merging contribution semantics;
- MERCHANT contextual filtering from active-capability, actor-authority and
  owner-routed residual-obligation evidence;
- contribution-local provider-readiness interaction availability that does not
  erase an otherwise eligible workspace or grant actor/execution authority;
- owner-qualified CUSTOMER eligibility-requirement registration and fail-closed
  current trusted-context evaluation; and
- structural PUBLIC/CUSTOMER candidate resolution that preserves membership,
  Projection Serviceability, Exposure and execution as separate decisions.

The focused existing-code suite passed:

```text
StaticSurfaceContributionComposerTest                 3 PASS
ContextualSurfaceResolverTest                         4 PASS
ContextualSurfaceInteractionAvailabilityTest          6 PASS
PublicContextualSurfaceResolverTest                    5 PASS
CustomerContextualSurfaceResolverTest                  5 PASS
SurfaceContributionRegistryCustomerEligibilityTest    2 PASS
CompositeResidualSurfaceObligationAuthorityTest        5 PASS
CompositeCustomerSurfaceEligibilityAuthorityTest       4 PASS
total                                                  34 PASS
failures / errors / skipped                             0 / 0 / 0
```

The unchanged production implementation also participated in the current
isolated canonical 988-test PostgreSQL-backed verification recorded in section
7.

### Boundaries found by the assessment

The existing PUBLIC/CUSTOMER resolver ports are deliberately older structural
seams, not final IMP-06 production authority. In particular:

- `ProjectionServiceabilityAuthority` returns an optional boolean and does not
  retain the exact contract/read-use/evaluator/source-evidence provenance now
  required by MS-PROT-027 v1.8 and implemented by P2;
- the public/customer Exposure ports consume contribution-level decisions but
  are not the production exact-contract Exposure resolver required by E4;
- `PublicSurfaceResolutionContext` is not the governed server-established
  Audience Observation Context required by E3; and
- subject-level Public Interaction Binding exists only in the prototype path;
  no production projector currently composes exact capability-owned
  participation with serviceability and Exposure.

Those seams fail closed, so they are safe reusable structure, but caller
booleans and prototype bindings cannot be promoted as completion evidence.
S1 therefore completes the required assessment without claiming final Surface
assembly.

The assessment refines the Surface branch without changing semantic ownership:

```text
S1 assessment                                      COMPLETE
S2 final PUBLIC/CUSTOMER Surface assembly          BLOCKED_DEPENDENCY
    requires exact P2 evidence acquisition/orchestration
    + E4 production Exposure resolution
S3 production Public Interaction Binding projection BLOCKED_DEPENDENCY
    requires capability-owned participation sources
    + exact serviceability/Exposure composition
```

No production code was changed by S1, and no missing authority was inferred
from the existing boolean ports.

---

## 9. Completed node R1

### Authority

- MS-PROT-073 v1.0;
- MS-PROT-062 v1.1;
- MS-IMP-001 / IMPLEMENTATION-RULES; and
- immutable Flyway migration V13.

### Existing conforming responsibilities

The existing production model contains the six initial Resource Protection
concepts authorised by MS-PROT-073 and no competing business/account authority:

```text
ProtectionTarget
ProtectionSubject
ProtectionPolicy
ProtectionConsumptionState
ProtectionAdmissionDecision
TemporaryProtectiveRestriction
```

The PostgreSQL/jOOQ authority additionally proves:

- only explicitly registered ProtectionTargets can be admitted or restricted;
- policy identity/version, subject scope, measurement basis, bounded capacity,
  deterministic window and policy-specific state-failure behaviour remain
  explicit;
- admission and consumption accounting share one transaction and serialize at
  the smallest subject/target scope requiring the invariant;
- a stable measurement-basis consumption identity prevents double consumption
  for retry of the same logical work;
- concurrent distinct candidates cannot both consume one remaining unit;
- `DEFER` is returned only when both policy and owning execution permit it;
- bounded restrictions affect only their exact subject/target and expire at the
  defined instant; and
- protection state and evidence remain separate from merchant configuration,
  entitlement, actor authority, provider readiness and business commitments.

Focused verification ran only after confirming no competing MainStreet
Maven/JVM, an existing `mainstreet_test` database, zero non-idle competing test
sessions and valid Flyway V13/V50/V51 history:

```text
ResourceProtectionModelTest                 7 PASS
JooqResourceProtectionAuthorityIT           7 PASS
total                                      14 PASS
failures / errors / skipped                  0 / 0 / 0
PostgreSQL                                  18.6
Flyway                                     51 migrations / V51 current
result                                     BUILD SUCCESS
```

The same unchanged production implementation participated in the current
isolated canonical 988-test PostgreSQL-backed verification recorded in section
7.

R1 does not claim a numerical policy catalogue, ingress integration, generic
multi-scope orchestration, leased-capacity implementation or public error
mapping. MS-PROT-073 leaves those choices to concrete downstream need, and
MS-IMP-001 explicitly does not require a broad endpoint catalogue in IMP-06.
The existing admission authority is the production Resource Protection spine;
later bounded use cases may consume it without changing its ownership.

No production code or migration was changed by R1.

---

## 10. Completed node T1a

### Authority

- composite MS-PROT-035 v1.0-v1.1;
- MS-IMP-001 / IMPLEMENTATION-RULES; and
- the accepted boundary that IMP-06 does not require a broad endpoint
  catalogue.

### Implemented contract

T1 inspection found no production API-contract package, so the node was split
before implementation. T1a adds only the common registered-contract spine:

- owner-qualified logical `ApiContractIdentity`, independent from URI,
  controller and HTTP method;
- the six closed initial transport surface classes;
- the four closed initial logical contract kinds;
- an owner-qualified accepted operation/query/callback/media responsibility
  reference;
- a stable trusted scope-establishment rule reference; and
- an immutable registry snapshot that rejects duplicate logical identities and
  resolves only exact registered identities.

The representation contains no URI, HTTP method, DTO, header, dynamic
dispatcher, endpoint catalogue, executable scope logic, business authority or
compatibility policy. Those omissions are deliberate boundaries, not defaults.

### RED → GREEN

RED was established by focused test compilation failing on the absent T1a
types. GREEN:

```text
ApiContractRegistrySnapshotTest
tests                                      5
failures / errors / skipped                0 / 0 / 0
result                                     BUILD SUCCESS
```

The test proves exact owner-qualified lookup, the complete six-surface and
four-kind vocabularies, duplicate-identity rejection, immutable registry
contents and non-blank logical authority/scope references.

The isolated canonical result is recorded in section 7.

---

## 11. Completed node T1b1

### Authority

- composite MS-PROT-035 v1.0-v1.1;
- the completed T1a common logical registration spine; and
- MS-IMP-001 / IMPLEMENTATION-RULES.

### Implemented contract

T1b was refined before implementation because the four accepted API Contract
kinds do not share the same governing fields or dependencies. T1b1 adds only
the immutable static command-definition responsibility:

- a T1a registration whose kind is exactly `COMMAND`;
- one or more eligible principal-class references;
- a transport-input contract reference;
- optional logical-retry and optimistic-concurrency-precondition references;
- completion-mode and safe result/rejection representation references;
- an optional typed Resource Protection target; and
- a data-protection classification reference.

The definition validates its static contract data but does not execute,
authorise, route or expose a command. Caller booleans, URIs, HTTP methods and
controller-specific DTOs are not introduced as authority.

### RED → GREEN

RED was established by focused test compilation failing on the absent command
definition type. GREEN:

```text
ApiCommandContractDefinitionTest          4 PASS
ApiContractRegistrySnapshotTest           5 PASS
failures / errors / skipped                0 / 0 / 0
result                                     BUILD SUCCESS
```

The tests prove command-kind enforcement, immutable eligible-principal data,
complete required static responsibility, optional retry/concurrency/protection
representation and rejection of blank references.

The isolated canonical result is recorded in section 7.

---

## 12. Completed node T1b2

### Authority

- composite MS-PROT-035 v1.0-v1.1, especially sections 9 and 53-59;
- the completed T1a common logical registration spine; and
- MS-IMP-001 / IMPLEMENTATION-RULES.

### Implemented contract

T1b2 adds only the immutable static callback-definition responsibility:

- a T1a registration whose kind is exactly `CALLBACK` and whose surface is
  exactly `INTEGRATION_INGRESS`;
- provider/integration role and source-authentication method references;
- correlation requirements and optional historical ProviderConnection/binding
  context;
- duplicate/replay identity and payload-validation rule references;
- evidence-owner and processing-mode references; and
- acknowledgement and safe-rejection semantics references.

The definition registers evidence-ingress responsibilities. It does not
authenticate a request, accept provider claims as business truth, select scope
from callback-body identifiers, execute provider-specific processing or create
authoritative consequences.

### RED → GREEN

RED was established by focused test compilation failing on the absent callback
definition type. GREEN:

```text
ApiCallbackContractDefinitionTest         4 PASS
ApiCommandContractDefinitionTest          4 PASS
ApiContractRegistrySnapshotTest           5 PASS
failures / errors / skipped                0 / 0 / 0
result                                     BUILD SUCCESS
```

The tests prove exact kind/surface admission, complete required static
responsibility, explicit optional historical context and rejection of blank
authority references.

The isolated canonical result is recorded in section 7.

---

## 13. Completed node T1b3

### Authority

- composite MS-PROT-035 v1.0-v1.1, especially sections 60-65;
- composite MS-PROT-066 v1.1-v1.2;
- the completed T1a common logical registration spine; and
- MS-IMP-001 / IMPLEMENTATION-RULES.

### Implemented contract

The authority assessment confirmed that Target 20 is design-closed and that
media transfer is delegated to composite MS-PROT-066. T1b3 adds one immutable
media-transfer definition with a closed choice between:

- bounded upload authority retaining trusted Merchant/actor scope, permitted
  role/context, size/type/profile constraints, transfer identity,
  expiry/lifetime, storage-destination authority, source confirmation/
  validation and the upload-completion separation rule; and
- protected download authority retaining current principal/context, Merchant
  Scope, optional applicable business relationship, Exposure/access authority
  and an optional bounded delivery-reference policy.

The optional delivery-reference policy retains its authority boundary,
optional required lifetime, Exposure compatibility and separation from
MediaAsset identity. The definition contains no URL, storage protocol, codec,
rendition dimension, attachment mutation or executable Exposure behavior.

### RED → GREEN

RED was established by focused test compilation failing on the absent
media-transfer definition type. GREEN:

```text
ApiMediaTransferContractDefinitionTest    4 PASS
ApiCallbackContractDefinitionTest         4 PASS
ApiCommandContractDefinitionTest          4 PASS
ApiContractRegistrySnapshotTest           5 PASS
failures / errors / skipped                0 / 0 / 0
result                                     BUILD SUCCESS
```

The tests prove exact kind admission, the closed upload/download authority
classes, complete required upload authority, protected-download inputs,
optional relationship/delivery-reference handling and non-blank references.

The isolated canonical result is recorded in section 7.

---

## 14. Completed node T2 assessment

### Authority

- composite MS-PROT-035 v1.0-v1.1, especially surface sections 2-9 and
  trusted-context sections 15-17;
- composite MS-PROT-063 v1.0-v1.1;
- MS-PROT-031 Merchant Scope ownership;
- existing IMP-04 closure evidence; and
- MS-IMP-001 / IMPLEMENTATION-RULES.

### Existing conforming responsibilities

Repository inspection and focused verification establish that the existing
production trust foundation already provides:

- WebAuthn-authenticated Identity mapping into a fresh Main Street-owned opaque
  Session without importing framework roles or Merchant Scope;
- current durable session resolution with expiry, revocation and Identity
  security-generation invalidation;
- host-bound privileged-cookie request extraction that rejects headers,
  parameters, missing, blank and ambiguous credentials as alternate authority;
- merchant-context establishment over a scope explicitly documented as already
  trusted, followed by current scoped-principal resolution;
- current Controller relationship re-read rather than Session-carried merchant
  authority; and
- staff operational context that independently revalidates active Membership
  and Merchant Operational Device Authorisation and carries trusted device
  context without absorbing role/privilege authority.

Focused verification:

```text
PrivilegedSessionRequestResolverTest                    4 PASS
SpringWebAuthnSessionBridgeTest                         4 PASS
MerchantControllerScopedExecutionPrincipalResolverTest 3 PASS
SessionCredentialResolverTest                          8 PASS
SessionTrustedExecutionContextEstablisherTest           2 PASS
StaffOperationalTrustedExecutionContextEstablisherTest  2 PASS
TrustedExecutionContextTest                            1 PASS
total                                                  24 PASS
failures / errors / skipped                             0 / 0 / 0
```

### Residual boundary

The verified merchant-context establishers intentionally require an already
trusted `MerchantScope`; they do not resolve transport routing or client
locators. No production API-layer registry currently binds a T1a
`scopeEstablishmentRuleReference` to one exact surface-affined establishment
authority. The existing runtime `TrustedExecutionContext` is merchant-scoped,
so it must not be reused to manufacture a fake Merchant Scope for
`PLATFORM_IDENTITY_BOOTSTRAP` or `PLATFORM_ADMINISTRATIVE`.

T2 is therefore split before further implementation. T2a owns only the common
exact rule/surface registry and a closed Merchant-versus-Platform scope result.
It will not authenticate requests, resolve roles, evaluate runtime authority,
or implement storefront/provider adapters. T2b and T2c are complete existing
children. Concrete surface adapters remain T2d and must consume their real
trusted routing/session/context/integration sources.

No production code or migration was changed by this assessment.

---

## 15. Completed node T2a

### Authority

- composite MS-PROT-035 v1.0-v1.1, especially sections 2-17;
- composite MS-PROT-063 v1.0-v1.1;
- MS-PROT-031 Merchant Scope ownership;
- the completed T1a contract registration spine; and
- MS-IMP-001 / IMPLEMENTATION-RULES.

### Implemented contract

T2a adds the common exact scope-establishment authority spine:

- a closed `ApiTransportScope` distinction between a real `MerchantScope` and
  explicit platform context that cannot fabricate a merchant;
- an `ApiTransportScopeEvidence` marker whose contract explicitly states that
  implementing the marker does not make evidence trusted;
- one authority port that declares its exact rule reference, transport surface
  and accepted evidence type, and independently validates that evidence before
  establishing scope; and
- an immutable registry snapshot that selects only the exact
  `scopeEstablishmentRuleReference` retained by T1a and enforces surface,
  evidence and result affinity.

PUBLIC, CUSTOMER_CONTEXTUAL and MERCHANT_OPERATIONAL require an established
Merchant Scope. PLATFORM_IDENTITY_BOOTSTRAP and PLATFORM_ADMINISTRATIVE require
explicit Platform scope. INTEGRATION_INGRESS may establish merchant or
platform/process scope through its registered concrete authority. The registry
does not authenticate, resolve principals/relationships, inspect transport
framework state or evaluate business/runtime authority.

### RED → GREEN

RED was established by focused test compilation failing on the absent T2a
types. GREEN:

```text
ApiTransportScopeAuthorityRegistrySnapshotTest       4 PASS
ApiContractRegistrySnapshotTest                       5 PASS
SessionTrustedExecutionContextEstablisherTest         2 PASS
StaffOperationalTrustedExecutionContextEstablisherTest 2 PASS
total                                                 13 PASS
failures / errors / skipped                            0 / 0 / 0
result                                                BUILD SUCCESS
```

The tests prove exact rule selection, immutable duplicate rejection,
surface/evidence affinity, fail-closed absent/null/mismatched authority results,
real Merchant Scope for merchant-facing surfaces and explicit Platform scope
for platform surfaces.

The isolated canonical result is recorded in section 7.

---

## 16. Refreshed graph frontier

S1, R1, T1a, T1b1-T1b3 and T2a-T2c are complete. Surface final-assembly dependants remain isolated
behind P2/E4 and production participation-source prerequisites, the existing
Resource Protection authority is evidence-backed, and production API contracts
now have a logical registration spine plus static command, callback and
media-transfer responsibilities without a speculative endpoint catalogue.

The smallest independent READY node is now:

```text
T3 — problem/outcome mapping authority/existing-code assessment

Required proof:
    classify existing application outcome and authentication/runtime failure
    categories against composite MS-PROT-035, identify the minimum missing
    transport-safe mapping responsibility, preserve owner meaning and security
    disclosure boundaries, and keep HTTP status outside semantic authority
```

The query definition remains blocked on the exact production Audience
Observation Context result boundary and Exposure resolver represented by E3/E4. E2 remains
a separate blocked design branch; E3 is implemented through API-context binding but its
result shape remains blocked on the undefined `ApiExposedElementSet`. S1 did not encode Exposure
definitions into semantic bundles, establish Audience Observation Context,
replace the exact P2 evaluator with a boolean or claim production Public
Interaction Binding resolution.

---

## 17. Completed T3 authority/existing-code assessment

### Authority and existing implementation

Composite MS-PROT-035 v1.1 fixes four transport/execution outcome classes:
`COMPLETED`, `ACCEPTED_PENDING`, `REJECTED` and `OUTCOME_UNCERTAIN`. It also
fixes the closed minimum client-relevant problem-category vocabulary in section
33, permits only safe owner-qualified detail, permits security-driven collapse
to `NOT_FOUND_OR_NOT_ACCESSIBLE`, and makes HTTP status a secondary adapter
mapping rather than semantic authority.

The repository already preserves related but differently scoped owner truth:

- `ApplicationOutcomeClassification` expresses MS-PROT-072 cross-capability
  progression and additionally owns `CONFLICT`, `EXECUTION_UNCERTAIN` and
  `MANUAL_INTERVENTION_REQUIRED`; it is not the MS-PROT-035 transport outcome
  vocabulary and must not be renamed or exported directly;
- `AuthenticationFailureCategory`, `AuthorizationException` and
  `UnsatisfiedOperationRequirementsException` preserve runtime trust,
  authorisation and requirement evidence;
- capability failure categories preserve capability-owned conflict,
  idempotency, not-found and technical distinctions; and
- Projection Serviceability, Exposure, Resource Protection and commercial
  access decisions remain separate authorities rather than transport problems.

No production type currently represents the exact MS-PROT-035 outcome or
problem vocabulary, and no transport-safe owner-qualified mapping authority
exists. A global Java-exception-to-problem switch would violate sections 34 and
36 because exception classes are not API contracts and transport must not
invent owner reasons.

### Refined implementation boundary

T3 is therefore refined without changing macro scope:

```text
T3a  exact transport outcome/problem semantic vocabulary
     no HTTP status and no owner lifecycle mutation

T3b  explicit owner-qualified safe mapping authority/registry
     exact owner evidence only; immutable duplicate rejection; fail closed

T3c  concrete owner and delivery-adapter mappings
     implemented with each real API surface and its accepted owner evidence
```

T3a is complete and T3b is now the smallest critical READY node. T3c is not a
licence for a speculative universal exception
translator or broad endpoint catalogue; each mapping remains coupled to its
real owner and adapter implementation.

---

## 18. Completed node T3a

### Implemented contract

T3a adds only the exact transport-neutral semantic vocabulary accepted by
MS-PROT-035 v1.1:

- `ApiCommandOutcomeClass` distinguishes `COMPLETED`, `ACCEPTED_PENDING`,
  `REJECTED` and `OUTCOME_UNCERTAIN`; and
- `ApiProblemCategory` preserves all sixteen stable top-level categories from
  section 33.

The types do not contain HTTP status, Java exception, retry-policy or business
lifecycle semantics. Existing MS-PROT-072 application outcomes remain
unchanged and are not treated as an interchangeable API enum.

### RED → GREEN and canonical proof

RED was established by focused test compilation failing because both types
were absent. GREEN:

```text
ApiTransportSemanticVocabularyTest       2 PASS
failures / errors / skipped              0 / 0 / 0
result                                   BUILD SUCCESS
```

The isolated canonical PostgreSQL proof for T3a was superseded by T3b's current
result in section 7. T3b defines the explicit owner-qualified safe mapping
authority; concrete owner and delivery mappings remain T3c work with their real
sources.

---

## 19. Completed node T3b

### Implemented contract

T3b adds an immutable, transport-neutral owner mapping spine:

- `ApiProblemEvidence` marks accepted mapping evidence without making it
  trusted or safe by declaration alone;
- `ApiOwnerProblemDetail` binds a safe code to the exact owning contract;
- `ApiProblem` combines the stable top-level category with optional safe owner
  detail;
- `ApiProblemMappingAuthority` declares one governed mapping rule, exact owner
  and exact evidence type; and
- `ApiProblemMappingRegistrySnapshot` selects by exact rule, rejects duplicate
  rules, enforces registration-owner and exact evidence affinity, rejects Java
  exceptions as evidence, fails closed on absent/null mappings and rejects
  detail attributed to another owner.

The registry assigns neither HTTP status nor generic retry policy. It permits a
mapping to omit owner detail, including the accepted security-safe collapse to
`NOT_FOUND_OR_NOT_ACCESSIBLE`.

### RED → GREEN and canonical proof

RED was established by focused test compilation failing on the absent T3b
types. GREEN:

```text
ApiProblemMappingRegistrySnapshotTest    4 PASS
ApiTransportSemanticVocabularyTest       2 PASS
total                                     6 PASS
failures / errors / skipped               0 / 0 / 0
result                                    BUILD SUCCESS
```

The isolated canonical PostgreSQL result is recorded in section 7. T3c remains
blocked until a real owner/adapter pair supplies accepted evidence and its safe
representation rule. The next independent READY work is T4's bounded
cursor/query authority and existing-code assessment.

---

## 20. Completed T4 authority/existing-code assessment

### Authority and existing implementation

Composite MS-PROT-035 requires every potentially growing collection query to
declare a maximum bounded page size, stable ordering basis, permitted filters,
continuation semantics and scope/query affinity. It prohibits an arbitrary
query DSL. Where continuation tokens are used, they must be opaque and bound to
the applicable query plus Merchant/Audience scope; structural validity cannot
permit cross-scope replay. Exact token encoding, cursor-versus-offset mechanics
and numeric defaults/maximums remain implementation choices.

Repository inspection found no production pagination, cursor, continuation
token or bounded collection contract type. `AllocationConflictQuery` is a
capability query port, not transport pagination infrastructure. T1b4's complete
query definition remains blocked because its required Audience Observation
Context and Exposure resolver are E3/E4 work.

### Refined implementation boundary

T4 is refined without selecting a deferred encoding choice or inventing
Audience authority:

```text
T4a  immutable bounded collection contract definition
     positive maximum, stable order, closed filter set, continuation and
     scope/query-affinity rule references

T4b  continuation issuance/resolution affinity authority
     exact query + trusted Merchant/Audience context; blocked on T1b4/E3

T4c  concrete opaque token encoding and delivery adapter
     implementation choice only after T4b and a real query adapter exist
```

T4a is the smallest critical READY node. It can be registered statically and
later composed into T1b4 without claiming to issue secure tokens. A generic
string cursor that accepts caller scope, or a token implementation bound only
to Merchant identity while ignoring applicable Audience context, would cross
the accepted authority boundary and is not implemented.

---

## 21. Completed node T4a and next governance gate

### Implemented contract

`ApiBoundedCollectionContract` retains one exact query-contract identity, a
positive maximum page size, a stable ordering-basis reference, an immutable
closed set of permitted filter references, continuation semantics and the
scope/query-affinity rule. It accepts an empty filter set, which means no
filters are registered; it does not expose a dynamic predicate language.

The type neither issues nor resolves continuation tokens and does not accept
caller-supplied scope as authority.

### RED → GREEN and canonical proof

RED was established by focused test compilation failing because the contract
type was absent. GREEN:

```text
ApiBoundedCollectionContractTest         3 PASS
failures / errors / skipped              0 / 0 / 0
result                                   BUILD SUCCESS
```

The isolated canonical PostgreSQL result is recorded in section 7.

### Accepted design frontier

MS-PROT-027 v1.9 resolved `IMP-06-E3-DG-001` after iterative falsification,
DESIGN-RULES-governed recommendation and explicit manual approval. The governed
request/context core, subjects, typed extensions, current admission and opaque
API-context binding have now passed RED → GREEN implementation.

The remaining E3 output boundary is not READY. Section 15 names
`ApiExposureResolution`, `ApiExposedElementSet` and a distinct
`InternalExposureResolution`, but the accepted corpus does not define the
element-set representation or invariants and the repository contains no such
implementation. Defining those semantics here would cross the manual design gate
and pre-empt E4. E4, T1b4 and T4b therefore remain blocked.

The separate `MS-PROT-027-V15-DQ-002` semantic-bundle encoding decision remains
deferred for E2/E4. E3 also stops before raw contextual credential encoding,
capability Exposure evaluators, continuation, HTTP mapping or bundle encoding.

---

## 22. E3 implementation evidence

### Governing authority

- MS-PROT-027 v1.9, accepted at `7875bf1`;
- MS-IMP-001; and
- IMPLEMENTATION-RULES v1.4.

### Verified RED → GREEN slices

1. Trusted observation-request establishment:
   RED `bd6a733` / Maven Tests #1313; GREEN `9a0473a` / #1314.
2. Closed initial observation subjects and opaque context:
   RED `6c5e45b` / #1315; GREEN `466d7f1` / #1316.
3. Authentication-session currentness:
   RED `315ece5` / #1317; GREEN `8b7b803` / #1318.
4. Current audience admission:
   RED `c03a95f` / #1320; GREEN `cea4470` / #1321.
5. Typed contribution definition/runtime-binding registries:
   RED `48392f3` plus fixture-only correction `3fe60b1` / #1324;
   GREEN `3b9a7b8` / #1325.
6. Contextual-access proof infrastructure and Customer-contextual subject:
   RED `fcdc7bb` plus boundary test `3b702f0` / #1326 and #1327;
   candidate `1c0d418` exposed an implementation-only assembly defect in #1328;
   fix `ce8c8f7` / #1329 GREEN without weakening tests.
7. Contribution validation/admission into opaque contexts:
   RED `3c083a1` / #1330; GREEN `dcee262` / #1331.
8. Opaque API Audience Observation Context binding:
   RED `726c6b0` / #1332; GREEN `c9b60f8` / #1333.

The GREEN implementation provides exact Active Release/request provenance,
closed and contextual subjects, exact Merchant Scope/authentication/proof
validation, immutable exact-release extension registries, contribution
validation, fail-closed current admission and a public API-bound context that
exposes only contract identity and surface.

Full E3 conformance is not claimed. `ApiExposedElementSet` requires a governed
representation before the distinct opaque API/internal Exposure result wrappers
can be implemented. See
`imp-06-e3-audience-observation-context-foundation-2026-09-01.md`.
