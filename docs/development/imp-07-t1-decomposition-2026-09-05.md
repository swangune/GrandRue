# IMP-07 T1 — Executable Decomposition

**Date:** 5 September 2026

Implementation navigation only, under MS-IMP-001 and IMPLEMENTATION-RULES. The M2 graph selects T1 for V1; this refines its executable children without changing authority or declaring the adapter complete.

Inspection found P5 bounded owner material and generic same-read assembly, plus a Merchant Presence bounded query handoff. It found no complete Publication request/HTTP execution composition. Composite MS-PROT-035 v1.1 §§39–43, 113 and MS-PROT-046 govern query registration, safe audience-specific response mapping and replaceable transport; exact DTO naming is implementation scope.

```text
T1   concrete PUBLIC Publication query adapter       IN_PROGRESS
T1A  query contract + bounded safe response mapping  READY — P5 + G0
T1B  trusted request/query execution + delivery      BLOCKED_DEPENDENCY — T1A
V1   full vertical path                             BLOCKED_DEPENDENCY — required complete adapters
```

T1A must register a PUBLIC, single-Opportunity query over the exact P5 projection, accept only matching serviceability/Exposure/read material, and map selected values into transport-owned DTOs preserving date/zone versus exact-instant meaning. It must not serialize fragments, revisions, scope, source evidence or bindings. Withheld material produces an absent response value. Nonserviceable or structurally mismatched inputs must not produce a successful DTO.

T1B remains responsible for trusted routing evidence, request/audience establishment, current serviceability and Exposure composition, unavailable outcome mapping and concrete delivery. T1A does not establish scope from a caller identifier, grant authority, acquire material or claim complete HTTP delivery.
