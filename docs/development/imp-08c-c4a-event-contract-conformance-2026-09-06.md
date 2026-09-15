# IMP-08C C4A Conformance — Registered Event Contracts

**Date:** 6 September 2026
**Node:** IMP-08C-C4A
**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI
Implementation evidence only; no new semantic authority.

## Authority and selection
MS-IMP-001 v1.0 + v1.1; IMPLEMENTATION-RULES v1.6; MS-PROT-026 v1.0 and MS-PROT-026 v1.1 §§3–6 govern owner-defined event contracts and exact original meaning.
C2A closing head `2a3304325ca2f6c1755e4d227da518e06f8ea77a`, run `33997178701`, passed 1089 unit/governance + 377 PostgreSQL integration tests.
Before production changes, `imp-08c-c2b-assessment-c4a-selection-2026-09-06.md` recorded the C2B dependency assessment and independent C4A selection. No lifecycle-evaluation application operation was found; Notification external delivery cannot be adopted without its provider/principal/owner progression dependencies. C2B assessment completion is not runtime closure.

## Implementation
EventContractIdentity qualifies the fact contract by semantic owner. EventContractAffinity captures that identity and exact semantic registry release.
EventContractDefinition retains explicit fact-meaning, authority-scope, subject-reference, required-provenance, purpose-limited payload and compatibility/evolution contract references. References must be nonblank; provenance requirements must be explicit and immutable. This does not require every occurrence to carry every possible metadata field; the owner references define which evidence is required.
EventContractRegistrySnapshot copies declarations, rejects duplicate identities even for identical definitions, permits distinct owners' same-named contracts and resolves only the exact requested affinity. Empty registry, unknown identity/owner or different release yields no definition. No latest-release fallback.
Registration is not publication or reaction authority. These static references do not evaluate payloads, discover listeners, authorize consumers, establish a committed occurrence or provide acknowledgement.
The existing semantic.DomainEvent and owner-specific outboxes remain unchanged. New types live under semantic.event; no Background-owned event semantics, universal event envelope, broker or workflow engine is introduced.

## Verification
Tests-first RED: Event Contract types were absent at compilation.
Six unit tests cover declared semantics; malformed identity/required references; exact release/owner lookup; duplicate identity rejection; defensive copying and immutable enumeration; and empty/malformed affinity handling.
Local full suite: **1095 tests, zero failures/errors/skips** on Java 26 targeting Java 25.
Implementation head `41a4e0fabd7c6f029bbd23b5f9c7b321dca64366`, CI run `33997829375`: SUCCESS — 1095 unit/governance + 377 PostgreSQL integration tests, zero failures/errors/skips.
Required gate: `mvn --batch-mode clean verify -Ppostgres-it`, Java 25/PostgreSQL 18.
No persistence behavior changes, so no new PostgreSQL scenario or migration was necessary for static contract registration. The full existing integration suite remains regression evidence.

## Limits and progression
C4B owner occurrence/publication mapping is next for decomposition; C4C reaction registration is eligible after C4A. Neither durable event mapping nor reaction execution/acknowledgement is claimed.
C2B and C3 remain blocked on executable owner/current-authority and downstream intent dependencies. C5 remains blocked on required children.
C2/C4 and IMP-08C remain IN_PROGRESS. IMP-08A/B remain eligible; provider/financial and separate P6 prerequisites remain.
C4A closes only when evidence, current graph, status and executable programme gate are synchronized and the closing head passes full CI.
