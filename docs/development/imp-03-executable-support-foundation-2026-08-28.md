# IMP-03 Executable Support Foundation — Implementation Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro-node:** IMP-03 — Semantic Runtime & Configuration Execution  
**Child scope:** ADR-012 contract-scoped executable support foundation  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child scope; IMP-03 remains PARTIALLY_CONFORMING**

---

## 1. Governing Authority

This implementation slice is governed by:

- `designs/IMPLEMENTATION-RULES.md`;
- `designs/MS-IMP-001.md`;
- ADR-012 — Runtime Semantic Execution & Deployment Compatibility;
- the accepted semantic/configuration authority already embodied by the production Semantic Registry, Configuration Compiler and Resolved Configuration Package.

No new business semantic, authorisation, entitlement, provider-readiness, transaction, public-API or customer-visible decision was introduced by this slice.

---

## 2. Implementation Trace

Implementation commit:

```text
30f9376cfa551bce2d7095e7c30997737a9dc6e0
feat: add ADR-012 contract-scoped executable support foundation
```

Verified merge into `development`:

```text
f9e7b5f52454d3696d7039b83cbac3bb57fd6793
IMP-03: contract-scoped executable support foundation
```

Pull request:

```text
#22 — IMP-03: contract-scoped executable support foundation
```

---

## 3. Production Implementation Added

The child adds an immutable technical executable-support model under:

```text
src/main/java/mainstreet/semantic/execution/
```

with:

- `SemanticExecutionContractReference` — exact Semantic Registry Release + execution-contract reference;
- `ExecutableSupportRequirement` — affected execution contract plus required participant/effect contracts;
- `ExecutableSupportManifest` — immutable evidence of contracts proven supportable by one implementation path;
- `ExecutableSupportRegistry` — immutable deterministic support/admission registry;
- `UnsupportedExecutableSupportException` — explicit non-execution when a selected path lacks support;
- `ExecutableSupportCoverageException` — explicit failure when a serving plan would orphan still-required execution contracts.

The registry is deliberately technical evidence only. It does not own semantic applicability, merchant configuration, actor authority, commercial entitlement, provider readiness or operational eligibility.

---

## 4. Conformance Proven by Tests

`ExecutableSupportRegistryTest` proves the following ADR-012 constraints within this child scope:

1. an operation identifier match alone does not establish support across different Semantic Registry Releases;
2. one implementation path may explicitly prove support for more than one exact semantic context;
3. materially different historical contracts may resolve to distinct compatible paths;
4. cross-capability participant/effect contracts must all be covered before admission succeeds;
5. a process/path rejects a semantic context absent from its support manifest;
6. deployment coverage rejects removal of the last safe path for a still-required contract;
7. unexpected support gaps remain scoped to the uncovered requirements rather than becoming release-global failure.

The implementation also preserves the ADR-012 rule that support metadata and tests are evidence rather than semantic authority.

---

## 5. Verification Evidence

### Pull-request verification

GitHub Actions run:

```text
33132963889
Maven Tests #1101
head: 30f9376cfa551bce2d7095e7c30997737a9dc6e0
result: SUCCESS
```

The workflow executed the repository-wide verification command:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

with JDK 25 and PostgreSQL integration-test infrastructure.

### Post-merge verification

GitHub Actions run:

```text
33133075367
Maven Tests #1102
head: f9e7b5f52454d3696d7039b83cbac3bb57fd6793
result: SUCCESS
```

The same full Maven/PostgreSQL verification path passed on the actual `development` merge commit.

---

## 6. Explicit Non-Claims

This evidence does **not** claim that IMP-03 as a whole is complete.

The following remain separate IMP-03 work:

- binding executable-support admission into the actual scoped operation-dispatch path;
- deterministic derivation/registration of exact execution requirements from the accepted semantic/RCP context where required;
- proving durable-work semantic/configuration affinity is recovered before support resolution;
- proving current security/privacy/legal/integrity restrictions remain authoritative over historical support;
- completing the remaining exact semantic/configuration-affinity and registered-operation execution conformance required by MS-IMP-001;
- whole-node full-suite and conformance closure.

No later child may infer missing participant/effect contract identity or other semantic meaning merely from implementation convenience. If accepted authority does not state the required contract identity, implementation must stop at that boundary and escalate through design governance.

---

## 7. Child Verdict

> **The ADR-012 contract-scoped executable-support foundation is CONFORMING_COMPLETE for its defined child scope. IMP-03 remains PARTIALLY_CONFORMING and may continue automatically only through further READY child work that requires no new semantic or architectural decision.**
