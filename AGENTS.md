# Main Street repository agent instructions

**Status:** NON-AUTHORITATIVE OPERATIONAL ADAPTER

This file is a concise execution and navigation adapter for repository coding agents. It does not create, amend, supersede, summarise into new meaning, or resolve Main Street product, semantic, architecture, programme, or implementation authority.

If this file conflicts with accepted repository authority, the accepted authority governs and this file is defective.

## Authority direction

- `designs/AUTHORITY-INDEX.md` determines **which** accepted authority currently governs.
- The applicable accepted authority determines **what** the substantive meaning is.
- `designs/IMPLEMENTATION-RULES.md` determines **how** accepted authority is implemented.
- `designs/authorities/programme/MS-IMP-001/MS-IMP-001.md` plus its current accepted composition governs the macro implementation programme when applicable.
- Tests, production code, implementation evidence, conversation memory, agent judgement, framework behaviour, and this file are not semantic authority.

Never fill a material semantic gap from plausibility, convention, adjacent code, or remembered context.

## Before material repository work

1. Establish the current branch and repository HEAD.
2. Classify the task:
   - **A — local read-only / explanatory work**;
   - **B — implementation of already accepted authority**;
   - **C — cross-capability / cross-boundary implementation**;
   - **D — material design / architecture / governance work**.
3. If classification is materially uncertain, use the broader applicable class.
4. Resolve current governing authority through `designs/AUTHORITY-INDEX.md` whenever a semantic, architecture, programme, or implementation-governance claim matters.
5. Read the exact applicable accepted constituent provisions and their required dependencies; do not infer current meaning from highest version, filename, date, code, or tests.
6. Inspect current repository/evidence sufficient for the claim or change being made.

Do not ritualistically reread unchanged sources without a freshness reason. Reuse currently inspected authority/evidence only while its source remains unchanged, task scope remains established, no widening trigger has appeared, and the exact governing provision can still be identified.

## Task-scoped loading

### A — Local read-only / explanatory work

Inspect the bounded current code/tests/evidence. Resolve governing authority where a semantic or architectural claim is made. Widen only when the discovered scope requires it.

### B — Accepted-authority implementation

Resolve the implementation target/node and applicable programme state; use `AUTHORITY-INDEX.md` to resolve the governing semantic/architecture authority; read the exact accepted constituent provisions, relevant evidence, affected code, and tests; then follow `IMPLEMENTATION-RULES.md`.

### C — Cross-capability / cross-boundary implementation

Read all participating semantic owners, accepted cross-capability contracts, applicable TAS/ADR architecture, relevant implementation evidence, and affected code/tests. Traverse discovered ownership and contract relationships until the material boundary is closed.

### D — Material design / architecture / governance

Do not use narrow implementation context. Follow the complete current `designs/DESIGN-RULES.md` lifecycle and load the broad applicable governance/design corpus, current authority navigation, terminology/deferred-decision state, and all materially applicable substantive authority.

## Agent execution profile

Agent/model selection and reasoning-effort settings are operational execution choices. They are not Main Street semantic, architectural, programme, implementation, acceptance, verification or approval authority.

Where the execution environment exposes selectable model capability or reasoning effort:

- bounded Class A work may use the environment's ordinary execution profile;
- Class B implementation should use an implementation-capable profile suitable for repository-scale code, tests and exact authority traceability;
- Class C work should use greater available reasoning capability than ordinary Class B work where the execution environment provides that distinction; and
- Class D work should use the strongest suitable reasoning capability available while following the complete current `designs/DESIGN-RULES.md` lifecycle.

A model or reasoning-profile change does not satisfy a mandatory-widening trigger. Required authority and repository evidence must still be retrieved under this file's context-loading and widening rules.

A higher-capability model, additional reasoning effort, larger context or separate agent invocation does not acquire semantic authority and must not resolve a material design gap by inference.

Falsification, verification, escalation, approval and completion remain governed exclusively by their applicable accepted Main Street authority and the existing provisions of this file that route agents to that authority.

Specific commercial model names and product-specific reasoning labels should not be encoded here. They may change without changing Main Street authority or repository governance.

## Mandatory widening

Widen current context when any of these appears:

- unknown or multiple plausible semantic owners;
- cross-capability command/query/event contract or a newly affected capability;
- task expansion beyond the initially inspected boundary;
- unresolved authority reference or base/amendment/supersession uncertainty;
- relevant OPEN/PROMOTED deferred decision;
- material canonical-terminology ambiguity;
- transaction, concurrency, retry/idempotency, historical-affinity, security/authorisation/trust, Exposure/projection, provider-responsibility, or business-truth persistence consequence;
- accepted-authority conflict with implementation or tests;
- repository evidence contradicting assumed scope; or
- required behaviour not explicitly determined by current accepted authority.

Widen by retrieving additional current authority/evidence. Never choose a merely plausible answer to avoid context expansion.

Consult the current `designs/DEFERRED-DECISION-REGISTER.md` when authority/evidence references a deferred decision, a design gap appears, affected concepts plausibly match an unresolved/promoted question, or implementation needs behaviour not explicitly determined by accepted authority.

Consult the relevant current `designs/CANONICAL-SEMANTIC-LEXICON.md` entry when introducing/changing semantic names, using a high-risk overloaded term, terminology qualification affects ownership, or materially different concepts could share ordinary-language wording.

## Stop conditions

If authority remains materially ambiguous, contradictory, or underspecified after appropriate widening, or there is a missing invariant, contract, failure behaviour, ownership rule, consistency rule, or architectural fit, stop the affected implementation path as `DESIGN_ESCALATION` under current `IMPLEMENTATION-RULES.md` / `DESIGN-RULES.md`. Independent READY work may continue where its authority remains sufficient.

## Freshness / invalidation

Reread affected current sources when the repository branch changes; a governing authority changes; the applicable `AUTHORITY-INDEX.md` composition changes; programme state changes eligibility; intervening work changes relevant evidence; task scope expands; an assumption is contradicted; or the exact governing provision can no longer be identified confidently.

Context loss or compaction is not itself semantic permission. If the exact material rule can no longer be established, reread it. If the required rule cannot be identified, widen. If authority remains uncertain, escalate.

Before completing a material implementation cycle, verify that the instructed branch is still active, applicable authority/evidence is fresh, every discovered widening trigger was resolved, no unresolved design gap was crossed, exact authority traceability remains valid, and required verification passed.

## Implementation kernel

- Preserve capability/domain ownership and accepted composite architecture.
- Tests protect accepted behaviour; they do not define missing semantics.
- Write/adjust tests first where accepted behaviour can be specified, and never weaken a valid test merely to make implementation pass.
- Prefer the smallest conforming implementation; do not introduce speculative machinery.
- Current repository evidence outranks remembered repository state.
- Use exact stable authority pointers for material traceability as required by `IMPLEMENTATION-RULES.md`.
- Run applicable targeted, integration/contract, architecture/conformance, static, and full verification required by current implementation governance.
- Actively falsify material completion claims.
- Synchronise implementation graph/evidence/status and commit cycle-closing state as required by current `IMPLEMENTATION-RULES.md`.

## Manual file-edit traceability

When an agent asks the user to create or modify repository files manually by copy/paste, the instruction MUST be self-contained and auditable:

- state the exact repository path for every file;
- provide the **complete resulting file content** for every file being created or replaced;
- do not use partial replacement fragments, hidden unchanged sections, ellipses, line-only substitutions, or instructions whose result depends on an unshown local edit;
- keep one stated resulting file state per instruction so the user can compare it directly with `git diff`;
- follow the manual edit with the applicable `git diff --check`, `git status --short`, and targeted verification command before widening the change; and
- if the complete resulting content cannot be supplied safely, stop or use an authorised repository mutation mechanism rather than directing an untraceable partial edit.

This section is an operational traceability rule only. It does not make this adapter semantic authority and does not permit a manual edit that would otherwise violate accepted design or `IMPLEMENTATION-RULES.md`.

## Git control

Use the currently instructed branch. **Do not create any Git branch unless the user explicitly authorises branch creation in the active chat.** `proceed`, `continue`, `implement`, permission to commit, or automatic READY-node execution do not authorise branch creation.

## Standard full gate

For the current Java backend programme, the repository full verification gate is:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

Use narrower tests during the implementation loop where appropriate, but do not substitute them for a required full completion gate.

## Adapter boundaries

Do not create a repository `AGENTS.override.md` or nested `AGENTS.md` without separately approved need. Do not maintain an independent authority catalogue or resolver here. `docs/development/implementation-programme-state.json` may assist implementation-node/dependency/evidence navigation but cannot determine semantic meaning or supersession.