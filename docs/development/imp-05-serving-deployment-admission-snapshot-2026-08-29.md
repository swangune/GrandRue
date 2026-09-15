# IMP-05 — Serving-Deployment Admission Snapshot Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** D5c2 — Immutable serving-generation snapshot/support evidence
**Date:** 29 August 2026
**Status:** **CONFORMING_COMPLETE**

## 1. Governing authority

This slice implements Sections 5 through 8 of accepted
`MS-PROT-040 v1.4`, composed with ADR-012 executable-support meaning and
ADR-013 packaged semantic materialisation.

One ordinary serving generation must retain an immutable normalized snapshot,
one or more exact release/bundle-digest rows, zero or more exact
implementation-path manifests, and exact release/contract tuples under each
manifest. Booleans, opaque JSON and mutable aliases are insufficient.

## 2. Implemented production boundary

The implementation adds:

- an exact `ORDINARY` cohort snapshot model with non-empty materialisation;
- materialisation evidence derived from the validated immutable
  `DeploymentSemanticMaterialisationSet`, retaining ADR-013 bundle digests;
- existing ADR-012 `ExecutableSupportManifest` evidence grouped by exact
  implementation path;
- exact materialisation and complete requirement-coverage queries;
- an append-only authority with exact idempotent retry and changed-intent
  generation identity conflict; and
- four normalized PostgreSQL relations for snapshot headers, materialised
  releases, manifests and supported release/contract tuples.

Current serving-generation state is deliberately absent. It belongs to the
separate D5c3 admission-control record.

## 3. RED → GREEN evidence

RED failed compilation because no serving snapshot or cohort model existed.

GREEN proved:

- exact release plus bundle-digest materialisation matching;
- complete participant/effect support on one exact path;
- normalized durable reconstruction;
- exact immutable retry;
- changed-intent conflict for a reused generation identity; and
- PostgreSQL rejection of an orphan supported-contract tuple without its
  exact generation/path manifest.

Focused verification passed one semantic contract and four PostgreSQL
integration contracts with Flyway at 42 migrations.

## 4. Canonical verification

```text
mvn --batch-mode -Ppostgres-it clean verify

Flyway migrations validated:           42
production Java sources compiled:      677
test Java sources compiled:            239
unit / conformance tests:              668 PASS
PostgreSQL integration tests:          249 PASS
total Maven tests:                     917 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

The shared target directory and PostgreSQL schema were isolated before the
run. Editor Java autobuild was restored afterward.

## 5. Explicit non-claims and next node

D5c2 records immutable generation facts; it does not declare one generation
current, coordinate activation, prepare promotion, switch external serving
state or reconcile an uncertain outcome.

D5c3 bidirectional promotion state machine and generation fence is now the
smallest critical **READY** node. D5c4 remains blocked by
`MS-PROT-040-V12-DQ-001`.
