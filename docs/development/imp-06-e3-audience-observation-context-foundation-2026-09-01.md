# IMP-06 E3 Audience Observation Context — Implementation Evidence

**Date:** 1 September 2026  
**Macro:** IMP-06 — Read, Exposure & Transport Spine  
**Fine-grained node:** E3 — Audience Observation Context  
**Classification:** PARTIALLY_IMPLEMENTED; REQUEST/CONTEXT, TYPED EXTENSIONS, ADMISSION AND API BINDING GREEN; EXPOSURE RESULT SHAPE BLOCKED DESIGN  
**Authority:** MS-PROT-027 v1.9 at `7875bf19e4aeeb1e3b3cab45c3ba0fac4e9693b3`  
**Execution governance:** IMPLEMENTATION-RULES v1.4 at `c4b221f0231d7f640c458f3a54a43278167cd9fd`

This record is implementation evidence and dependency navigation only. It
creates no semantic or architectural authority.

## Implemented responsibilities

### Trusted request and closed context core

`ObservationRequestEstablisher` establishes a sealed request from trusted API
transport evidence or an existing trusted execution context. It re-resolves the
exact QUERY registration, establishes Merchant Scope through the registered
surface/rule authority, resolves the current Active Release and requires exact
serving Semantic, Projection and Exposure release affinity.

`AudienceObservationContextEstablisher` establishes sealed, immutable,
request-scoped contexts for Public, Customer principal, Customer contextual and
Merchant interactive subjects. Public carries no trusted execution context.
Authenticated subjects require exact principal, session provenance, request and
Merchant Scope correspondence.

No public method accepts caller-supplied scope, release, audience, class or
verdict as authority.

### Authentication and contextual proof

`DefaultAuthenticationSessionCurrentnessAuthority` evaluates exact session
identity, Identity, establishment time, revocation, absolute expiry and current
Identity security generation at one captured Clock instant.

Contextual proof is a distinct owner-qualified extension. Its runtime snapshot
binds an exact proof kind to an exact final implementation class. Establishment
validates request, Merchant Scope, principal, owner, kind, class and nonblank
stable access binding. Raw credentials are not represented. Production enables
no contextual proof kind by default, and current admission rejects contextual
subjects with unresolved owner authority.

### Typed contributions

The contribution definition snapshot is affined to one exact Semantic Registry
Release and retains only kind, permitted audiences and the closed `SINGLE`
cardinality. A separate runtime-binding snapshot maps each kind to one exact
final package-private implementation class.

Context establishment accepts zero or more contributions and validates exact
release, definition, runtime class, audience, request, Merchant Scope and
single-cardinality uniqueness. Contribution presence is evidence only and never
becomes authority.

### Current audience admission

`AudienceObservationAdmissionEvaluator` captures one Clock instant and creates
one opaque invocation binding per evaluation. It evaluates Public against
surface-wide platform protection; Customer principal against authentication
currentness and platform protection; and Merchant interactive against
authentication currentness, exact current Controller or active Membership, and
platform protection.

Customer contextual admission remains fail-closed until its owner authority is
provided. Unsatisfied and unresolved outcomes remain distinct. Admission is not
cached or stored in the context and grants no element-level Exposure.

### Opaque API-context binding

`ApiAudienceObservationContextBinder` re-resolves the exact registered QUERY,
requires trusted API provenance, checks exact scope-rule provenance and surface,
rechecks execution/request Merchant Scope, and enforces the surface/subject
matrix.

The public `ApiAudienceObservationContext` exposes only
`contractIdentity()` and `surface()`. It has no public internal-context
unwrap.

## Falsification results

| Slice | RED falsification | Revision / GREEN |
|---|---|---|
| Trusted request establishment | `bd6a73380f345d3ccbe06b46ecdc84eead4991e8`; Maven #1313 failed only on absent E3 contracts | `9a0473a38b4d2379e5e69b24e8df4b1806e56332`; #1314 SUCCESS |
| Closed initial subjects/context | `6c5e45bb9331af85cd012be00f9fd5b106266d65`; #1315 absent-contract RED | `466d7f143974badfecd7c0624ea8e236ad2cf0ed`; #1316 SUCCESS |
| Authentication currentness | `315ece50aa7e30c217bc7c5dbf6ff4b4817052c9`; #1317 absent-contract RED | `8b7b803547926e4e0e6ae1a4eba7f7a22e623ae3`; #1318 SUCCESS |
| Current audience admission | `c03a95fe40ad32ad61bce6425ae7d80c881922c2`; #1320 absent-contract RED | `cea4470b92be48736fb229c53cf446819cbfee4a`; #1321 SUCCESS |
| Contribution definition/runtime registries | `48392f3d50fb92e7113eec025cd74c56aee38d2e`; fixture visibility corrected by `3fe60b1b1bc32c492b0f2d59c7e0e8058c3a27d9`; #1324 absent-contract RED | `3b9a7b852c1e15c32d20a906ccd0cc97d6cb0663`; #1325 SUCCESS |
| Contextual proof and subject | `fcdc7bb822471b28b460c44b9eeeeb3b6fa80865` plus blank-binding case `3b702f0e94f8c64563bc4f76eac44e79a23b3980`; #1326/#1327 absent-contract RED | Candidate `1c0d41813c5d00563dec50adec57b17b1e22dab9` failed #1328 because a helper was assembled into the wrong class; `ce8c8f7cb678d2507cf73d1532b1f94ccd6ba87a` moved it without weakening tests; #1329 SUCCESS |
| Contribution validation in context | `3c083a12d4b08379cd365fb2554c05712cf31ba8`; #1330 absent-contract RED | `dcee262de195e2a50a1c7c9fdd4dbd8a48e1ec89`; #1331 SUCCESS |
| Opaque API-context binding | `726c6b060e70f36c1694574c51482e406ff3541e`; #1332 absent-contract RED | `c9b60f8fdcc5d9e122f5689ef78efaa7d190b02e`; #1333 SUCCESS |

## Canonical GREEN evidence

Maven Tests #1333 at `c9b60f8fdcc5d9e122f5689ef78efaa7d190b02e`:

```text
command:                                mvn --batch-mode clean verify -Ppostgres-it
production Java sources compiled:       867
test Java sources compiled:             271
unit / conformance tests:               766 PASS
PostgreSQL integration tests:           298 PASS
total Maven tests:                      1064 PASS
Flyway migrations:                      51 validated / V51 current
failures / errors / skipped:            0 / 0 / 0
result:                                 BUILD SUCCESS
```

Run: https://github.com/swangune/MainStreet/actions/runs/33492591779

## Boundary falsification and remaining blocker

The accepted corpus and current tree were searched for
`ApiExposedElementSet`, `ApiExposureResolution` and
`InternalExposureResolution`. MS-PROT-027 v1.9 section 15 names these output
contracts and requires opaque, distinct API/internal results, but it does not
define the contents or invariants of `ApiExposedElementSet`; earlier Exposure
amendments and production code contain no such representation.

Implementing that set would therefore require inventing E4-adjacent element
result semantics. Under DESIGN-RULES and IMPLEMENTATION-RULES v1.4 this is a
`BLOCKED_DESIGN` boundary, not an implementation default. No speculative
wrapper was added.

## Explicit non-claims

This implementation does not claim:

- a production contextual-proof kind or current contextual owner authority;
- concrete platform-protection policy/adapter wiring;
- `ApiExposedElementSet`, opaque API/internal Exposure results or E4 resolution;
- Projection Serviceability/Exposure orchestration;
- T1b4 query definition or T4b continuation affinity;
- raw contextual credential encoding;
- HTTP/controller/representation mapping;
- semantic-bundle encoding;
- delegation; or
- merchant system/service observation.

E4, T1b4 and T4b remain blocked. E3 may resume only after the Exposure element
set representation is governed through the manual design gate.
