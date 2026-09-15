# IMP-03 Semantic Execution Spine — Closure Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro-node:** IMP-03 — Semantic Execution Spine  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE**

---

## 1. Governing Authority

This closure is governed by:

- `designs/IMPLEMENTATION-RULES.md`;
- `designs/MS-IMP-001.md`;
- accepted Semantic Registry / Capability / Configuration Compiler / Resolved Configuration Package authorities;
- ADR-012 — Runtime Semantic Execution & Deployment Compatibility;
- accepted semantic compatibility and configuration-activation authority.

This record is implementation evidence only. It creates no semantic or architectural authority.

---

## 2. MS-IMP-001 Scope Closure

MS-IMP-001 defines IMP-03 to cover:

```text
Semantic registries
Capability Registry
Capability composition infrastructure
Configuration Compiler
Resolved Configuration Package
Operation Runtime
exact semantic/configuration affinity
registered operation execution
semantic compatibility checks
```

The production implementation and executable evidence now cover this scope without reviving the superseded prototype compiler/composition path.

### Semantic registries

Production `SemanticRegistry` resolves exact immutable releases. `SemanticRegistrySnapshot` rejects conflicting semantic identities and invalid owned definitions. Exact-release semantics remain distinct from latest-release lookup.

### Capability registration/composition

Production semantic registration remains capability-owned through immutable registered semantic definitions. Historical `mainstreet.semantic.capability` compiler/composition types remain quarantined to test scope where superseded; `ConfigurationCompiler` is the canonical production compilation path.

### Configuration Compiler

`mainstreet.semantic.compiler.ConfigurationCompiler` deterministically resolves accepted registered semantics into the executable merchant model and rejects capabilities absent from the referenced exact semantic registry.

### Resolved Configuration Package

`ResolvedConfigurationPackage` binds the source Merchant Configuration revision, exact semantic-registry release, executable semantic model, resolved configuration, fulfilment plan, static surface catalogue and provenance. Construction rejects mismatched source/configuration provenance.

### Operation Runtime

`ActiveOperationResolver` resolves an operation from the exact active merchant release and captures one immutable `ApplicableOperation` snapshot. `ScopedOperationDispatcher` consumes that captured operation and establishes no semantic meaning of its own.

### Exact semantic/configuration affinity

`ApplicableOperation` preserves the exact active release/model snapshot for an invocation. Concurrent later activation does not rewrite work already bound to that captured operation.

### Registered operation execution

The end-to-end IMP-03 conformance test proves the production path:

```text
SemanticRegistry
    ↓
ConfigurationCompiler
    ↓
ResolvedConfigurationPackage
    ↓
ConfigurationRelease
    ↓
ConfigurationReleaseActivation
    ↓
ActiveOperationResolver
    ↓
ApplicableOperation
    ↓
ADR-012 ExecutableSupportAdmission
    ↓
ScopedOperationDispatcher
    ↓
capability-owned handler
```

### Semantic compatibility checks

Cross-semantic-release activation fails closed unless exact trusted semantic-compatibility evidence permits the transition. Evidence is bound to source/target release and reference scope; incompatible evidence cannot authorise activation.

---

## 3. ADR-012 Executable-Support Closure

The first IMP-03 implementation child established contract-scoped executable support:

```text
30f9376cfa551bce2d7095e7c30997737a9dc6e0
feat: add ADR-012 contract-scoped executable support foundation

merge:
f9e7b5f52454d3696d7039b83cbac3bb57fd6793
```

It added immutable technical support evidence under `mainstreet.semantic.execution`, including exact contract references, support requirements, manifests, deterministic path admission and execution-support orphaning protection.

It proves that:

- equal operation identifiers do not imply support across semantic releases;
- one deliberately generic path may support multiple exact contexts when explicitly proven;
- historical/incompatible contracts may use distinct paths;
- every required participant/effect contract must be covered;
- unsupported execution fails closed;
- deployment planning cannot knowingly remove the final safe path for a still-required contract.

This evidence remains technical compatibility evidence, not semantic authority.

---

## 4. Runtime Admission Closure

The second IMP-03 child bound ADR-012 support admission into the scoped runtime path:

```text
645d443247f28c6553b9e51e6f5a3c16d43c1381
fix/test-complete IMP-03 runtime support-admission child

merge:
a40fb95418b6d385faf85ebc459a8a8ea69e33e2
IMP-03: bind executable support to scoped runtime admission
```

Production additions:

```text
mainstreet.runtime.ExecutableSupportOperationExecutionGuard
mainstreet.semantic.execution.ExecutableSupportAdmission
mainstreet.semantic.execution.ExecutableSupportRequirementResolver
```

The guard consumes the same captured `ApplicableOperation` used by dispatch. It does not re-resolve latest configuration/semantic state mid-invocation.

Tests prove:

- supported exact contracts reach the handler;
- same operation identifier on an unproven semantic release does not execute;
- a support requirement from another semantic release is rejected before registry admission;
- compatible configuration activation during an earlier guard does not change the already-captured support affinity.

The development merge passed the full repository verification gate.

---

## 5. End-to-End Spine Proof

The final IMP-03 child added no production behaviour. It added one end-to-end conformance test proving that the already-existing production spine composes correctly:

```text
9fcf3b71d578d2af6ea51607c3730edecee17198
test: prove IMP-03 semantic execution spine end to end

PR #24
IMP-03: prove semantic execution spine end to end

merge:
5e635e484c367fbe27e241c0476f0e0a46441e73
```

The absence of additional production code is intentional: the conformance proof established that the remaining macro scope was already satisfied by existing accepted implementation plus the two ADR-012 children. No speculative refactor or duplicate semantic path was introduced merely to manufacture milestone activity.

---

## 6. Verification Evidence

### ADR-012 foundation

```text
PR run:       33132963889 — Maven Tests #1101 — SUCCESS
post-merge:   33133075367 — Maven Tests #1102 — SUCCESS
```

### Runtime support admission

```text
PR run:       33133426283 — Maven Tests #1105 — SUCCESS
post-merge:   33133532539 — Maven Tests #1106 — SUCCESS
```

### End-to-end semantic execution spine

```text
PR run:       33133674013 — Maven Tests #1107 — SUCCESS
post-merge:   33134132894 — Maven Tests #1108 — SUCCESS
head:         5e635e484c367fbe27e241c0476f0e0a46441e73
```

All gates execute the repository-wide Maven/PostgreSQL verification path:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

with JDK 25 and the PostgreSQL integration-test harness.

---

## 7. Important Boundary Decisions Preserved

IMP-03 closes without changing these separations:

```text
semantic applicability
    ≠ executable support

configuration activation
    ≠ semantic compatibility evidence

executable support
    ≠ actor authorisation

executable support
    ≠ commercial entitlement

executable support
    ≠ provider readiness

operation identifier equality
    ≠ semantic-contract equality

captured invocation affinity
    ≠ latest active configuration
```

The runtime remains fail-closed when the exact required semantic execution contract is not proven supportable.

---

## 8. Deferred Scope That Does Not Prevent IMP-03 Closure

The following are not reopened as IMP-03 deficiencies because they belong to later authorised macro-nodes or independent accepted responsibilities:

- durable background reaction/work execution — IMP-08C;
- authentication/session/trusted-browser mechanics — IMP-04;
- Merchant Profile/Location/onboarding and complete activation use case — IMP-05;
- API/exposure/transport — IMP-06 and later;
- provider readiness and provider execution — IMP-09+;
- deployment/operability automation beyond the ADR-012 support invariant — later production-operability scope.

Those later nodes must consume the exact semantic/configuration-affinity rules proven here and must not reinterpret them.

---

## 9. Closure Decision

IMP-03 completion criteria are satisfied:

```text
semantic registry production path                 PASS
capability-owned semantic registration            PASS
canonical configuration compiler                  PASS
exact immutable RCP                               PASS
scoped operation runtime                          PASS
exact semantic/configuration affinity             PASS
registered operation execution                    PASS
semantic compatibility enforcement                PASS
ADR-012 contract-scoped executable support        PASS
runtime support admission                         PASS
end-to-end semantic spine                         PASS
full repository verification                      PASS
```

> **IMP-03 — Semantic Execution Spine: CONFORMING_COMPLETE.**

Under `MS-IMP-001`, IMP-04 remains independently READY after IMP-02 and may continue automatically under `IMPLEMENTATION-RULES.md`. IMP-05 remains blocked until both IMP-03 and IMP-04 are conforming complete.
