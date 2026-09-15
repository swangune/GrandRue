# IMP-07 T1A Conformance — PUBLIC Opportunity Query Response Mapping

**Date:** 5 September 2026

**Node:** IMP-07-T1A — Query contract + bounded safe response mapping

**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI

This is implementation evidence only. It creates no semantic authority.

## Authority and selection

MS-IMP-001 v1.0 + v1.1 and IMPLEMENTATION-RULES v1.6 govern execution. Composite MS-PROT-035 v1.1 §§39–43, 113 governs query registration, consuming Projection Serviceability/Exposure, safe withholding and implementation-owned DTO naming. Composite MS-PROT-046, including v1.3, governs Opportunity material and its typed temporal boundaries. Composite MS-PROT-027 and the completed P5/G0 spine govern exact same-read selection.

M2 closure `a3c919ed67e69695d5412c5af42e12318a31ce9d`, run `33988065345`, passed 1035 unit/governance + 355 PostgreSQL integration tests, zero failures/errors/skips. T1 was selected for the required V1 adapter path.

Inspection found a bounded Merchant Presence handoff but no complete Publication request/HTTP execution composition. `imp-07-t1-decomposition-2026-09-05.md` refines T1 before implementation: T1A is contract/response mapping; T1B is trusted request/query execution and delivery. T1 remains IN_PROGRESS.

## Implementation and structural review

PublicOpportunityQueryContract defines publication/public-opportunity-query as PUBLIC QUERY, owned by the exact P5 public-opportunity-representation projection. It specifies the public route scope rule, public audience, no customer relationship, published-material serviceability, exact PUBLIC Exposure family, no client filtering/sorting, a single-resource bound, safe DTO and absent/unserviceable response references. The definition is registrable in ApiContractRegistrySnapshot; deployment installation and referenced runtime bindings remain T1B.

PublicOpportunityQueryResponseAdapter accepts server-established bounded read, P2 result and opaque API Exposure. It rejects wrong API identity/surface, wrong P5 projection/read-use and non-single/exact Opportunity material. It delegates all P2/E4 binding, progress, positive membership and serviceability selection to the existing PublicCustomerProjectionAssemblyService. Only selected values reach mapping; withheld material produces Optional.empty. Nonserviceable or structurally mismatched material cannot produce a successful DTO.

The adapter has no repository, mutation or policy dependency. It cannot reacquire values, decide publication eligibility or create scope from the requested identifier. Internal callers must supply the trusted request pipeline; this signature is not a client input contract.

PublicOpportunityResponse owns explicit transport values and nested link/temporal DTOs. It copies immutable lists and contains no domain object, revision identity, merchant scope, source evidence or observation binding. Calendar dates remain dates plus their explicit zone; exact instants retain ISO instant meaning and precision. No actionability, interaction execution grant or application-link authority is inferred from displaying role-qualified navigation facts.

No persistence, migrations, schema, domain authority, generic selector behavior or semantic amendment changed.

## Verification trace

Initial RED was test compilation on absent T1A types. The first targeted run passed seven tests and found an adversarial fixture that the API binder rejected before the adapter. After correcting that fixture, the same test found a second malformed fixture whose Projection owner/read-use mismatch was rejected during bounded-read construction. Both were fixture errors, not pre-existing production defects. The corrected fixtures reach the intended adapter guards, with precise structural exception assertions.

Local full verification passed **1043 tests, zero failures/errors/skips**, including all eight T1A tests, using Java 26 and the Java 25 target.

The tests cover exact PUBLIC registration, positive published material, no owner reload during mapping, immutable output, JSON serialization excluding internal evidence, preserved date/zone and nanosecond instant meaning, withdrawal after acquisition withholding the response, nonserviceable material, different read/request/resource, wrong API/surface/projection, and missing source material. They use the real P5 read port, Publication window/material-affinity evaluator, generic Exposure resolver, binder and selector with a deterministic owner authority and P2/admission fixtures.

No new PostgreSQL test is added for this pure response boundary; the full existing persistence/integration suite remains mandatory.

Implementation commit: `fade9d97cecf4542b968c2145293ef28ce5e9859`.

Full Java 25/PostgreSQL CI run `33988676029`: SUCCESS — 1043 unit/governance + 355 PostgreSQL integration tests, zero failures/errors/skips.

Required command: `mvn --batch-mode clean verify -Ppostgres-it`.

## Closure and limits

Evidence, graph, status and programme gate close together only on successful synchronization CI. T1A does not claim installed runtime registration, trusted route resolution, live P2/admission composition, unavailable HTTP outcome mapping or an HTTP endpoint. Those remain T1B. T1 and IMP-07 remain IN_PROGRESS; V1 still requires the complete T1/T2/T3 adapters.
