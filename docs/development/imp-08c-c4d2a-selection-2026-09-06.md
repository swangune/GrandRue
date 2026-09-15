# IMP-08C-C4D2A — Exact committed-baseline recovery selection
Date: 6 September 2026. Rules: MS-IMPLEMENTATION-RULES-001 v1.7, `designs/IMPLEMENTATION-RULES.md`, §30.1 — Exact Authority Pointer Contract; §30.3 — Clean-Code-First Rule; §47 — Implementation Evidence; §52.2 — Ready-node rule; §52.18 — Counterevidence Before Completion; §52.19 — Evidence Locality Rule.

## Baseline and authority
C4D1 closing baseline: `0270329fccdb680f5577ef471c86e44dc6aa8215`, run `34010162333` — SUCCESS — 1113 unit/governance + 387 PostgreSQL integration tests, zero failures/errors/skips.
Prerequisite evidence: `docs/development/imp-08c-c4d1-v17-conformance-2026-09-06.md`. C4C v1.7 evidence and the untouched historical v1.6 record retain their scoped claims.
Exact semantic authority: MS-PROT-056 v1.6, `designs/MS-PROT-056 v1.6 — Standing Free Baseline Temporal Anchor Amendment.md`, §3 — Standing Free baseline fact; §5 — Idempotency and duplicate delivery; §7 — Runtime behaviour during propagation gap.
MS-PROT-026 v1.1, `designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md`, §24 — Downstream Idempotency Remains Separate.

## Selected scope
C4D2A is a bounded child of C4D2: recover Commercial's already-committed baseline using its owner store before requiring catalogue availability or another candidate identity. Match merchant, originating establishment and original time exactly. First materialisation still requires historical FREE revision authority; the database owner remains responsible for concurrent establishment.
The currently live handler always asks the catalogue before resolving duplicates. This is an IMPLEMENTATION_GAP in lost-acknowledgement recovery availability; it does not invalidate C4C's static registration or C4D1's persistence scope.
No source trust, principal trust, worker dispatch, acknowledgement composition, historical backfill or overall C4D completion is claimed by this child.

## Tests-first falsification
Require committed replay to succeed when catalogue and identity factory would fail if called. Reject differing origin/time and a faulty owner port returning another merchant's baseline. Preserve first-materialisation validation and the existing PostgreSQL concurrent-convergence test. Add database-backed store-recreation recovery with the catalogue unavailable.
Update the existing duplicate integration assertion from two candidate identities to one: candidate generation on replay is not an accepted semantic requirement. Keep exact committed identity/history assertions.
