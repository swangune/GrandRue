# IMP-07 T1B Conformance — Trusted PUBLIC Query Execution and Delivery

**Date:** 5 September 2026

**Node:** IMP-07-T1B — Trusted PUBLIC query execution and delivery

**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI; completes T1 with T1A

Implementation evidence only; no semantic authority is created.

## Authority and prerequisite

MS-IMP-001 v1.0 + v1.1 and IMPLEMENTATION-RULES v1.6 govern execution. Composite MS-PROT-035 v1.1 §§3, 33–43 and 113 governs trusted public routing, transport-safe outcomes, query/DTO separation and implementation-owned HTTP naming/status mapping. Composite MS-PROT-027 governs exact-release request establishment, serviceability, audience admission and same-read Exposure. Composite MS-PROT-046, including v1.2/v1.3, governs current published Opportunity material, exposure-window evidence and original revision affinity.

T1A closure `7ef3aa3b46f5188ff6749b6b0bbd378bd90f530f`, run `33988912106`, passed 1043 unit/governance + 355 PostgreSQL integration tests, zero failures/errors/skips. This closes the remaining execution/delivery child from `imp-07-t1-decomposition-2026-09-05.md`.

## Concrete path

The opt-in `publication-public-api` Spring profile registers the controller and concrete query composition. Its GET path is `/api/public/storefronts/{locator}/opportunities/{opportunity}`. An explicit immutable server-configured public locator map implements the T1A scope rule. It has no fallback from a locator to Merchant ID and accepts no body/header scope authority. Query parameters are rejected because this single-resource contract permits no filters, sorting or client scope selection.

PublicOpportunityQuery.create installs the T1A API registration, exact-release Publication Projection Contract/policy evaluators, P4 Exposure contracts and the authoritative Publication window/material-affinity requirement evaluator. It composes ObservationRequestEstablisher, current configuration and serving registry, supplied audience establishment/admission authorities, P5 material acquisition and the T1A response adapter.

PublicBoundedQueryExecution contains only shared request/P2/E4 mechanics. The owner acquisition callback receives trusted Merchant Scope and the opaque request inside one bounded invocation. A returned read must have the same request binding. Capability-owned material-affinity contributions are constructed through the existing trusted boundary, public audience/API context is established, current admission is evaluated and real Projection Serviceability precedes Exposure. No public request unwrap, generic capability dispatcher or new authorization shortcut is introduced.

The Publication serviceability portfolio requires complete/current exact published-material progress for all four freshness/serviceability/missing/stale policy bindings. It has no stale fallback or materialized cache. P5 establishes coherent published material at acquisition, and P4 revalidates exact material affinity and publication/window state before Exposure; P2 does not replace that live owner check.

T1A maps only the selected values. Missing/withdrawn/foreign material and empty positive Exposure use NOT_FOUND_OR_NOT_ACCESSIBLE/404. Invalid query parameters use INVALID_REQUEST/400; rejected current admission uses NOT_AUTHORISED/403; unavailable request/runtime/material evaluation uses REPRESENTATION_UNAVAILABLE/503. No exception detail or owner evidence reaches the response. All controller responses use Cache-Control: no-store. GET has no business mutation, read acknowledgement or relationship-creation dependency.

## Verification trace

Initial RED was test compilation on missing T1B types. The first production compile identified an omitted evaluator-version argument; it was corrected to the required versioned identity. All eight initial targeted tests passed after that implementation correction. A ninth test then proved the opt-in Spring configuration actually constructs the registered query/controller with supplied authorities.

Local full verification passed **1052 tests, zero failures/errors/skips**, using Java 26 and the Java 25 target.

Nine unit/composition tests prove Spring profile wiring, trusted locator → real P2/Exposure → JSON HTTP delivery, preserved public temporal values, no internal evidence in the body, rejected client scope/filter input, unregistered locator rejection before material access, cross-merchant absence, unavailable active configuration before owner reads, withdrawal after bounded acquisition, current protection on each request, safe owner failures, and real serviceability rejection of stale progress.

Two PostgreSQL tests use the durable Publication authority through the concrete HTTP query pipeline. One proves successful published representation with unchanged durable current state and Publication History. The other proves identical safe bodies for foreign and durably withdrawn resources.

Implementation commit: `5cd020b5d60c2ff15241eeb128b3d22868b647af`.

Full Java 25/PostgreSQL CI run `33989663158`: SUCCESS — 1052 unit/governance + 357 PostgreSQL integration tests, zero failures/errors/skips.

Required command: `mvn --batch-mode clean verify -Ppostgres-it`.

## Structure, deployment limits and closure

Publication owns route/contract composition, source serviceability and DTO delivery. Generic Surface code contains no Publication dependency; domain and persistence code are unchanged. No migrations, mutable projection database, execution authority, public interaction grant or semantic amendment were added.

The profile is implemented and tested, not deployed by this cycle. Deployment must provide its registered locator map, current configuration activation, serving Semantic Registry, durable Publication owner, correctly registered audience contribution bindings and current admission/protection authorities. Missing providers cannot acquire permissive defaults. Tests use deterministic active-release/serving-registry/protection fixtures, with the real P2 engine, audience/admission/Exposure composition and HTTP serialization; PostgreSQL proofs replace the Publication fixture with durable state.

Evidence, graph, status and programme gate close together on successful synchronization CI. T1B plus T1A complete T1. Select T2 next for public Enquiry submission delivery over E2/E3. T3 and V1 remain incomplete, and IMP-07 stays IN_PROGRESS.
