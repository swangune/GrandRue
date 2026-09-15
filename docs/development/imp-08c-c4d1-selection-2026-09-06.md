# IMP-08C-C4D1 — Receipt and Acknowledgement Selection
Date: 6 September 2026. Target: C4D1, a bounded child of C4D. Rules: MS-IMPLEMENTATION-RULES-001 v1.7, `designs/IMPLEMENTATION-RULES.md`, §30.1 — Exact Authority Pointer Contract; §47 — Implementation Evidence; §52.18 — Counterevidence Before Completion; §52.19 — Evidence Locality Rule.

## Observed baseline and exact authority
Baseline `951cfe9e1ec908fab5e26f8af54679bd5fa652a5`, CI `34009153710`: 1110 unit/governance + 380 PostgreSQL integration tests, zero failures/errors/skips.
C4C prerequisite: `imp-08c-c4c-v17-impact-conformance-2026-09-06.md`. Its current evidence is carried forward; historical nodes are not re-audited.
MS-PROT-026 v1.1, `designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md`: §2 — Canonical Separation; §16 — Event Reaction Identity; §17 — Per-Reaction Acknowledgement; §23 — Reaction Deduplication; §24 — Downstream Idempotency Remains Separate; §32 — Historical Affinity; §45 — Consumer Failure.
MS-PROT-065 v1.1, `designs/MS-PROT-065 v1.1 — Production Durable Background Work, Timer, Attempt & Retry Execution Contract Amendment.md`: §11 — Attempt Evidence Before Consequential Execution; §59 — Restart Recovery. C4D1 supplies a durable receipt prerequisite, not a complete WorkAttempt implementation.

## Selected responsibility
READY: immutable merchant reaction receipt containing logical key, exact reaction/source affinities, Merchant Scope and downstream logical-intent reference; idempotent durable acceptance; independent immutable completion acknowledgement; bounded pending discovery for one exact reaction affinity.
Duplicate physical acceptance preserves first receipt time. Changed source, reaction release, scope or downstream intent under the same logical key conflicts. Acknowledgement for one reaction cannot complete another consumer/event. No business payload or credentials are stored.
The infrastructure store records supplied progression evidence; it does not establish source truth, principal trust, current eligibility or downstream outcome truth. C4D2 must bind the accepted Standing Free owner operation and verify those boundaries.

## Completion falsification planned before code
Test conflicting provenance, duplicate/concurrent acceptance, independent consumers on the same event, separate events, lost acknowledgement replay, conflicting acknowledgement, malformed SQL state and store recreation.
Do not infer downstream exactly-once effects, authority, lifecycle policy or complete C4D/C2/C3/C5 conformance from these persistence tests. No migration of historical event meaning.
