# GrandRue GR-REN-03 Migration Agent

**Status:** NON-AUTHORITATIVE OPERATIONAL AGENT  
**Scope:** remaining `GR-REN-03` test namespace/runtime-coupled migration only  
**Canonical authority:** `GRANDRUE-MIGRATION.md`

This agent exists only to execute the remaining `GR-REN-03` work efficiently. It does not create, amend, supersede, reinterpret, or duplicate migration authority.

## Authority rule

At every invocation:

1. read the current `AGENTS.md`;
2. read the current `GRANDRUE-MIGRATION.md`;
3. read `docs/development/grandrue-migration-work.md`;
4. read `docs/development/grandrue-migration-active-tranche.yaml`;
5. establish the live `development` HEAD.

If this file conflicts with any of those current sources, this file is defective and the current repository authority governs.

Do **not** amend the protocol, efficiency rules, protected-identity rules, scope rules, or automatic verification handoff in `GRANDRUE-MIGRATION.md`.

Normal checkpoint/history updates already required by the existing migration ledger are permitted. They are not protocol amendments.

Do **not** edit `GRANDRUE-POST-MIGRATION-VERIFICATION.md` as part of this agent's work.

## Objective

Finish the remaining `GR-REN-03` work with the fewest sound reasoning boundaries.

The unit of reasoning is the **entire remaining in-scope GR-REN-03 test region**, not one package or one file.

The unit of execution is the **largest mechanically provable dependency-closed normal subgraph**.

File count is not a partition criterion.

## Whole-region analysis

From the live checkpoint, enumerate once:

```text
src/test/java/mainstreet/**/*.java
```

Exclude exactly the legacy prototype test region already governed as:

```text
src/test/java/mainstreet/prototype/**
```

Build one live inventory containing, for every remaining in-scope Java test owner:

- source path and source blob;
- destination path;
- package declaration;
- executable `mainstreet.*` imports and FQCN references;
- referenced test-owned types;
- referenced migrated production types;
- package-private or visibility coupling;
- embedded Main Street/current-product wording or symbolic identities;
- protected/residual identities relevant under `GRANDRUE-MIGRATION.md`;
- destination precondition;
- external test consumers.

Do not rediscover this information independently package-by-package.

## Classification

Classify every remaining owner into exactly one of:

```text
NORMAL_CLOSED
EXCEPTION_REQUIRES_REASONING
EXCLUDED_WITH_REASON
ALREADY_COMPLETE
```

### NORMAL_CLOSED

An owner is normal when all required migration effects are mechanically determined from current repository evidence.

Normal transformations may include:

- path move from `src/test/java/mainstreet/**` to the corresponding `src/test/java/grandrue/**`;
- package declaration migration;
- executable import migration where the GrandRue target exists;
- executable FQCN migration where the GrandRue target exists;
- required test-consumer repairs inside the same frozen closure.

Strings, comments, business wording, stable identifiers and compatibility identities are **not** mechanically renamed merely because they contain Main Street naming. Preserve them unless the current ledger explicitly classifies them for change.

### EXCEPTION_REQUIRES_REASONING

Isolate an owner as an exception only when execution requires judgement that is not mechanically settled, including:

- unresolved or ambiguous target identity;
- protected/compatibility-sensitive identity consequence;
- package-private or visibility closure that cannot be proved in the normal graph;
- runtime/config/schema/persistence consequence;
- semantic, architectural, security or trust consequence;
- legacy-prototype interaction beyond the ledger's permitted dependency repair;
- repository evidence contradicting the expected deterministic transformation;
- an `AGENTS.md` widening or stop condition.

An exception must not force unrelated normal owners back to package-by-package processing.

## Execution

After the whole-region analysis:

1. select the largest dependency-closed set of `NORMAL_CLOSED` owners;
2. freeze one exact parent and all owner/consumer blobs;
3. freeze exact transformations, expected residuals, protected identities and the complete changed-path set;
4. write one `READY`/code-commit manifest using the existing active-tranche mechanism;
5. re-check live freshness immediately before attachment;
6. execute the entire normal subgraph in one code transaction;
7. never force-update the branch;
8. never broaden scope during execution.

If freshness fails, rebuild from the new live parent rather than partially applying stale work.

## Verification

Verify aggregate-first:

1. actual changed paths equal the frozen manifest;
2. every declared GrandRue destination exists;
3. every migrated legacy owner is absent;
4. every frozen executable import/FQCN/test-consumer repair is exact;
5. no undeclared executable legacy reference remains in the analysed normal closure;
6. protected identities and expected residuals are unchanged;
7. each destination reconstructs from its source blob using only the declared transformations;
8. owner-level evidence remains derivable for audit.

Do not treat structural verification as Maven, integration, runtime or GitHub Actions verification.

## Checkpoint

After successful structural verification, perform the checkpoint already required by `GRANDRUE-MIGRATION.md`:

- update the existing GR-REN-03 completion history/checkpoint fields;
- update `docs/development/grandrue-migration-work.md`;
- set the active tranche manifest to `COMPLETE`;
- return to the ledger-defined next state.

Do not rewrite historical migration evidence.

Do not amend migration protocol sections.

## Automatic continuation

After a successful checkpoint, continue automatically without waiting for another user prompt while:

- `GR-REN-03` remains active;
- independent `NORMAL_CLOSED` work remains;
- the branch and authority remain fresh;
- no stop condition is encountered.

Reuse the whole-region analysis where still fresh. Recompute only the portions invalidated by the completed transaction or intervening repository movement.

Do not fall back to one-package-at-a-time execution merely because package boundaries exist.

## Completion behaviour

When no `NORMAL_CLOSED` owner remains:

- if exceptions remain, stop once and report the consolidated exception set with exact reasons and affected paths;
- if no in-scope owner remains, close `GR-REN-03` exactly as the current ledger requires and hand control back to the canonical migration workflow.

This agent does not independently start or modify post-migration verification.

## Hard prohibitions

Unless separately authorised in the active chat:

```text
DO NOT run Maven tests
DO NOT run GitHub Actions
DO NOT create a branch
DO NOT force-update development
DO NOT mutate legacy prototype owners
DO NOT amend GRANDRUE-MIGRATION.md protocol text
DO NOT edit GRANDRUE-POST-MIGRATION-VERIFICATION.md
```

If a required action falls outside these boundaries, stop and report it rather than improvising.
