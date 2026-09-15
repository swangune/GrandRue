# IMP-08C C2B Assessment and C4A Selection
**Date:** 6 September 2026. Navigation/evidence under MS-IMP-001 and IMPLEMENTATION-RULES v1.6.
C2A closing head `2a3304325ca2f6c1755e4d227da518e06f8ea77a`, run `33997178701`, passed 1089 unit/governance + 377 PostgreSQL integration tests.

## Assessment before production changes
C2B execution composition remains BLOCKED_DEPENDENCY: no data-lifecycle evaluation application operation exists in the current source inventory. NotificationDeliveryCoordinator exists, but invoking its external delivery would require owner/provider/principal and downstream progression dependencies; a generic background callback cannot establish them. This is an implementation dependency, not a request to invent or approve new owner semantics.
C2B assessment is complete; executable C2B is not complete.

## Selected independent node
C4A registered Event Contract definitions and exact historical affinity: READY, selected.
MS-PROT-026 v1.1 §§3–6 requires owner-qualified registered Event Contracts and exact original meaning. The existing semantic.DomainEvent is a historical fact value; owner-specific publication outboxes are already present. Neither supplies this missing explicit contract registry.
Implement mandatory owner-declared fact meaning, authority-scope, subject, provenance, payload and evolution references; exact release lookup with no latest fallback; immutable registry with duplicate identity rejection. Scope and payload remain owner contract references, not new universal policy or schema.
C4B durable event occurrence/publication composition follows C4A plus concrete owner publication mapping.
C4C reaction registration/identity follows C4A; execution/acknowledgement requires its own owner composition.
C3 remains blocked on executable C2B and downstream intent. C5 remains blocked on required children.
C2 and C4, and IMP-08C, remain IN_PROGRESS. IMP-08A/B remain eligible. Provider/financial prerequisites and separate P6 blocker remain unchanged. No branch creation.
