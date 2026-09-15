# IMP-08C C2A Conformance — Durable Contract Affinity

**Date:** 5 September 2026
**Node:** IMP-08C-C2A
**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI
Implementation evidence only, not new semantic authority.

## Authority and selection
MS-IMP-001 v1.0 + v1.1; IMPLEMENTATION-RULES v1.6; composite MS-PROT-065 v1.1 §§5–8, 33–34 and 59 establish registered responsibility, historical affinity and restart recovery. Exact schema mechanics remain implementation detail (§78).
Selection was recorded before production changes in `imp-08c-c2-decomposition-2026-09-05.md`.
C1 final head `4e3d22dc928bdee675b619c085aa14ef43d8b240`, run `33996398540`, passed 1084 unit/governance + 374 PostgreSQL integration tests.

## Implementation and boundaries
BackgroundWorkContractAffinity preserves the exact owner-qualified contract identity and semantic registry release separately from existing opaque configuration provenance.
DurableWorkInstruction carries optional affinity, validates owner agreement and preserves legacy construction as explicitly unregistered. No release or contract is inferred for legacy data.
Static registeredContract resolution uses the exact registry release/identity and checks instruction scope, target identifier, retry reference and overdue mode against the declaration. Missing or mismatched registration returns empty.
Static resolution is not executable admission. It does not establish trusted Merchant Scope, due-condition satisfaction, current authority, principal bounds or downstream intent safety. The returned registered target retains its owner qualifier. No arbitrary executor, production handler, scheduler or owner policy is introduced.
V58 adds nullable contract owner/identity/release columns with all-or-none, nonblank and owner-equality constraints. The jOOQ adapter writes, reads and reconstructs affinity for direct retrieval and technical claims. Existing exact instruction equality makes replay conflict if affinity is removed, replaced or retroactively attached to already committed legacy work.
Configuration provenance, work identity, claim coordination and existing transaction/idempotency behavior are preserved. Existing technical store calls remain infrastructure evidence; they do not require or grant runtime admission.

## Tests and verification
Tests-first RED: new affinity types/methods absent at compilation.
Five unit tests prove exact resolution, unknown owner/identity/release refusal, scope/target/retry/overdue mismatch refusal, missing registration, malformed affinity and preservation of configuration provenance.
Three PostgreSQL tests prove restart/claim/replay reconstruction; conflicting contract/release/legacy replay; and SQL rejection of partial, blank or cross-owner affinity. Existing legacy, scope, concurrency, retry and attempt tests remain unchanged.
Local full suite: **1089 tests, zero failures/errors/skips** on Java 26 targeting Java 25.
Implementation head `8751a5e42775c25b92d66bc7eec50e2c98dc68ff`, full CI run `33996910427`: SUCCESS — 1089 unit/governance + 377 PostgreSQL integration tests, zero failures/errors/skips.
Required full gate: `mvn --batch-mode clean verify -Ppostgres-it` with Java 25/PostgreSQL 18.
The schema is verified through Flyway and actual PostgreSQL; no local PostgreSQL result is claimed.

## Remaining graph
C2A closes only with synchronized status, graph, evidence and executable programme gate on passing full CI.
C2 remains IN_PROGRESS. C2B bounded execution/current revalidation is next for owner-composition assessment; lifecycle reevaluation is an accepted candidate, not an implemented runtime binding.
C3 attempt/recovery, C4 event/reaction and C5 integration remain outstanding. IMP-08C stays IN_PROGRESS; IMP-08A/B eligibility and provider/financial hard dependencies are unchanged. P6 remains separately blocked.
