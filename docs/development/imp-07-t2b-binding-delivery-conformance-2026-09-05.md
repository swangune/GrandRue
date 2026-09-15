# IMP-07 T2B Conformance — Opportunity Binding Carry-Forward and Submission

**Date:** 5 September 2026

**Node:** IMP-07-T2B — Opportunity binding carry-forward and submission

**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI

Implementation evidence only; no semantic authority is created.

## Authority and prerequisite

MS-IMP-001 v1.0 + v1.1 and IMPLEMENTATION-RULES v1.6 govern execution. Composite MS-PROT-035 governs explicit PUBLIC query/command contracts, trusted locator scope and safe command outcomes. Composite MS-PROT-043 v1.2/v1.4 governs structured subject carry-forward, untrusted browser locators, fresh authoritative revalidation, immutable provenance, contextual owner requirements and supplied contact without identity inference. MS-PROT-059 governs logical-request retry. MS-PROT-027 and MS-PROT-049 govern exact selected material and owner-qualified interaction participation.

T2A closure `c092b10944281f5582f484331c0a4ce988a644fd`, run `33991054297`, passed 1061 unit/governance + 361 PostgreSQL integration tests, zero failures/errors/skips. The existing T2 decomposition selects this binding-aware child after T2A; no further graph decomposition or semantic amendment was needed.

## Implementation

The opt-in `enquiry-opportunity-public-api` profile explicitly registers two owner API contracts:
- GET `/api/public/storefronts/{locator}/opportunities/{opportunity}/enquiry-binding`.
- POST `/api/public/storefronts/{locator}/enquiries/opportunity` with Idempotency-Key.

The binding query establishes the registered trusted public locator scope and executes real P5 acquisition, admission, P2 serviceability and P4/E4 Exposure. A generic server-only selected-projection callback receives the executable model and static contribution catalogue from the exact immutable release captured at request establishment. It does not unwrap request authority to clients or reacquire activation. Publication's concrete acquisition is reused under the distinct registered binding query.

The owner adapter feeds the exact selected assembly into I2 with the I1 Opportunity participation source. Only the single matching binding can produce a locator. Its subject and revision derive from the selected typed P5 fragment and its exact published-material affinity. No later lookup substitutes a current revision. Binding issuance creates no Enquiry or receipt and grants no execution authority.

The response contains only the stable Opportunity identity and an opaque locator. The locator uses standard JDK AES-256-GCM with a random 96-bit nonce, a 128-bit authentication tag and purpose/version/key-ID authenticated data. Its encrypted payload retains merchant, Opportunity and exact revision identities; the subject type is fixed by this owner path. A bounded versioned encoding and an explicitly supplied immutable keyring reject malformed/tampered/unknown-key values. Encryption protects the internal revision meaning omitted by T1's DTO. This is a transport implementation of the accepted untrusted binding locator, not a new authority, access token or semantic expiry rule.

Submission establishes command scope and current admission independently on every attempt. It decodes the exact subject meaning before calling E2; token randomness, encoding and key rotation are not part of logical intent. Thus different issued locators for the same subject revision replay the same request, while a newly published revision conflicts with an already used key. E2 remains unchanged and reconciles a committed request before fresh E3 preparation. New attempts revalidate exact Publication binding, participation, Exposure, current interaction and required owner preparation inside the existing transaction. A stale binding fails closed without becoming a general enquiry.

The strict request carries binding, question and optional supplied contact only. Raw subject revision, scope, CustomerContext and unknown fields are rejected, including null unknown values. Existing text limits are retained; binding input is bounded to 32,768 characters. The operation/merchant-qualified retry identity separates general and subject-bound commands. No CustomerContext is inferred from supplied contact.

Success and replay return only `{"outcome":"COMPLETED"}`. Known pre-application failure, conflict or fresh applicability rejection returns a safe REJECTED problem; unexpected application failures return OUTCOME_UNCERTAIN. Query failures return safe query problems. Responses use Cache-Control: no-store and do not echo submitted values, revision evidence or internal exceptions.

## Verification trace

The initial tests-first RED was compilation on absent T2B production types; the new fixture also used a Jackson 2 import and was corrected to the repository's Jackson 3 namespace. A production exception import was corrected on the first compile. These compile corrections are not described as runtime failures. Fourteen initial targeted tests passed, including the existing nine T1B query regression tests. Eight final T2B tests and the full local suite passed: **1069 tests, zero failures/errors/skips**, on Java 26 targeting Java 25.

Eight unit/composition tests cover actual I2 binding issuance and E3 submission, exact subject and supplied contact, absent interaction/Exposure, tampering and foreign scope, strict unknown-field rejection, fresh admission and stale binding rejection, distinct reissued/rotated encodings with identical logical intent, captured-release affinity versus fresh configuration change, explicit key requirements, and actual Spring profile wiring for both routes.

Five PostgreSQL tests compose HTTP binding/submission with real Publication state/material storage and lock, real E2 transaction/receipt and E1 storage. They verify:
- Original retry after withdrawal and disabled interaction, with no fresh preparation or duplicate.
- Equivalent reissued locator replay, changed-content conflict and intentional repeat with a new key.
- Lost result after actual commit, OUTCOME_UNCERTAIN and recovery after withdrawal.
- New published revision conflicts with the old request key; old intent still replays; a fresh stale request rejects and a fresh current request stores the new exact revision.
- Stale rejection leaves neither submission nor retry receipt; explicit republish permits a subsequent attempt.

The integration fixture uses deterministic configuration, clocks, current admission and remaining requirements. Its Publication interfaces delegate to real PostgreSQL owner storage and locking; they are not mocked persistence outcomes. During review the fixture's withdrawn-to-published transition was corrected to use the required explicit republish operation before CI.

Implementation commit: `b9702d030b2a3dc9af181e97bf8bdd8ffa073fd2`.

Full Java 25/PostgreSQL CI run `33992025633`: SUCCESS — 1069 unit/governance + 366 PostgreSQL integration tests, zero failures/errors/skips.

Required command: `mvn --batch-mode clean verify -Ppostgres-it`.

## Structure, limits and closure

Generic Surface execution remains owner-neutral. All binding encoding, response mapping and command orchestration are Enquiry-owned, composed with existing Publication and I2/E3 authority. No migration, receipt representation change, semantic expiry, generic form language, communication side effect, CustomerContext creation or stored observation grant was added.

Deployment must provide trusted route registries, active configuration/registry, current query and command admission, durable Publication/E2 providers, contextual Enquiry-owned remaining requirements and persistent binding keys. Old decoding keys must be retained for the retry interval supported by the deployment; key removal invalidates the old locator before reconciliation. No default key or permissive provider exists. The profile is implemented and tested; this cycle does not deploy it or claim a browser UI.

Evidence, current graph, implementation status and programme gate close together on successful synchronization CI. T2B and T2 then conform with T2A. T3 is the next READY node; V1 still requires its merchant query adapter and integrated path proof. IMP-07 remains IN_PROGRESS.
