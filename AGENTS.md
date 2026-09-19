# GrandRue repository agent instructions

**Status:** NON-AUTHORITATIVE OPERATIONAL ADAPTER

This file is the repository bootstrap and navigation map for coding agents.

It does not create, amend, supersede, reinterpret, or resolve GrandRue product, semantic, architecture, programme, implementation, acceptance, verification, or approval authority.

If this file conflicts with accepted repository authority, the accepted authority governs and this file is defective.

## Authority map

Use the repository authorities rather than this file for substantive rules:

- `designs/AUTHORITY-INDEX.md` — determines **which accepted authority currently governs**.
- Applicable accepted design authority — determines **what the required product, semantic, contract, and architecture meaning is**.
- `designs/DESIGN-RULES.md` — governs material design, architecture, governance, review, falsification, ambiguity resolution, and approval.
- `designs/IMPLEMENTATION-RULES.md` — governs **how accepted authority is implemented**, verified, evidenced, checkpointed, and controlled.
- `designs/authorities/programme/MS-IMP-001/MS-IMP-001.md` and its current accepted composition — govern the macro implementation programme where applicable.
- `/IMPLEMENTATION.md` — is the non-authoritative resumable implementation controller and must remain subordinate to the canonical graph, current evidence, and accepted authority.

Tests, production code, implementation evidence, conversation memory, framework behaviour, agent judgement, and this file are not semantic authority.

## Repository bootstrap

Before material repository work:

1. Establish the current repository branch and HEAD.
2. Identify whether the task is:
   - bounded read-only/explanatory work;
   - implementation of already accepted authority; or
   - material design, architecture, or governance work.
3. Resolve governing authority through `designs/AUTHORITY-INDEX.md` whenever semantic, architectural, programme, or implementation-governance meaning matters.
4. Inspect the exact applicable accepted provisions and sufficient current repository evidence for the task.
5. Do not infer current meaning from filenames, version numbers, code, tests, prior conversation, or plausibility.

Do not ritualistically reload unchanged material. Reuse inspected authority/evidence only while its source and relevant task scope remain unchanged and the exact governing rule can still be identified.

## Work routing

### Bounded read-only / explanatory work

Inspect the bounded current code, tests, and evidence needed for the question. Resolve accepted authority whenever the answer makes a semantic or architectural claim.

### Accepted-authority implementation

Resolve the applicable implementation target and current programme state, inspect `/IMPLEMENTATION.md` as an operational pointer where relevant, resolve governing accepted authority through `AUTHORITY-INDEX.md`, and follow `designs/IMPLEMENTATION-RULES.md`.

### Material design / architecture / governance

Follow the complete current `designs/DESIGN-RULES.md` lifecycle. Do not use implementation convenience, existing code, tests, or this adapter to create design meaning.

## Widening and escalation

Widen the inspected context whenever the current material is insufficient to establish the governing rule safely, including when:

- ownership or the governing authority is uncertain;
- work crosses a capability, contract, trust, persistence, transaction, concurrency, retry/idempotency, projection, provider, or other material boundary;
- task scope expands;
- accepted authority conflicts with implementation or tests;
- relevant terminology or deferred-decision state is unresolved; or
- required behaviour is not explicitly determined by accepted authority.

Retrieve additional current authority and evidence rather than choosing a merely plausible answer.

When relevant, consult:

- `designs/DEFERRED-DECISION-REGISTER.md` for unresolved or promoted design questions; and
- `designs/CANONICAL-SEMANTIC-LEXICON.md` for material terminology ownership or ambiguity.

If a material semantic, contract, ownership, consistency, failure, security/trust, or architectural question remains ambiguous, contradictory, or underspecified after appropriate widening, stop the affected path and escalate through the current `IMPLEMENTATION-RULES.md` / `DESIGN-RULES.md`.

Never fill a material design gap from plausibility, convention, adjacent code, tests, or remembered context.

## Adapter boundaries

This file must remain a thin router, not a second governance document.

Do not duplicate detailed rules already owned by design, implementation, programme, migration, verification, or other accepted authorities merely for convenience.

Do not maintain an independent authority catalogue or supersession resolver here.

Do not create `AGENTS.override.md` or nested `AGENTS.md` files without separately approved need.

Where repository procedures, commands, branch controls, verification gates, migration handoffs, implementation mechanics, or execution policies are required, load them from their current owning authority instead of encoding copies here.
