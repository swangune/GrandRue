# IMP-08C C2 Decomposition — Durable Contract Affinity
Date: 5 September 2026. Navigation/evidence under MS-IMP-001 and IMPLEMENTATION-RULES v1.6.
C1 final head `4e3d22dc928bdee675b619c085aa14ef43d8b240`, run `33996398540`, passed 1084 unit/governance + 374 PostgreSQL integration tests.

## Selection before production changes
- C1: CONFORMING_COMPLETE.
- C2A exact registered-contract affinity in durable instruction/recovery: READY, selected. MS-PROT-065 v1.1 §§5–8 and 33–34 require exact registered responsibility and historical semantic affinity. Add explicit contract identity/release, persist/reconstruct it, and reject mismatched static resolution.
- C2B bounded execution binding/current revalidation: BLOCKED_DEPENDENCY on C2A and explicit first owner composition. Lifecycle reevaluation is an accepted candidate under MS-PROT-065 §§43–44 and composite MS-PROT-053; a concrete composition must be inspected before selection. No owner handler or default lifecycle policy is added by C2A.
- C3 recovery: BLOCKED_DEPENDENCY on executable C2B and downstream intent semantics.
- C4 event/reaction work: ELIGIBLE FOR DECOMPOSITION.
- C5 integration: BLOCKED_DEPENDENCY on required children.
IMP-08C stays IN_PROGRESS. IMP-08A/B stay eligible; provider/financial prerequisites and separate P6 block remain.

## Compatibility and limits
Existing instructions remain readable with explicit absent contract affinity; migration must not guess a contract or release. Absence cannot resolve as registered. Existing opaque configuration provenance remains separate and preserved.
Static resolution checks exact identity/release and instruction owner, scope, target reference, retry and overdue affinity. Resolution is not execution admission or current authority. Work persistence/technical claims remain infrastructure evidence. No production responsibility is made executable by this change.
