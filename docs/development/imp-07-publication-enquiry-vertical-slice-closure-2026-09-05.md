# IMP-07 — Publication → Enquiry Vertical Slice Closure

**Date:** 5 September 2026

**Macro-node:** IMP-07 — First Complete Vertical Slice: Publication → Enquiry

**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI

This implementation closure is subordinate to MS-IMP-001 v1.0 + v1.1 §13 and IMPLEMENTATION-RULES v1.6. It creates no semantic authority.

## Closure basis

IMP-04, IMP-05 and IMP-06 conforming macro evidence remains retained. Required IMP-07 owner nodes G0/P1/P2A/P2B/P3/P4/P5/I1/I2/E1/E2/E3/M1/M2 and concrete adapters T1/T2/T3 conform.

T3 closure `6657a12b20218970312a1f0ace907e36d1db4d08`, run `33993321981`, passed the full gate. V1 proof `fccef99cf13789ac9302bd4efe3bec237c61eede`, run `33995174577`, passed 1079 unit/governance + 374 PostgreSQL integration tests, zero failures/errors/skips.

Detailed trace, initial fixture failure/correction and limits: `imp-07-v1-vertical-conformance-2026-09-05.md`. Current graph: `imp-07-graph-refresh-2026-09-05-v1-closure.md`.

## Required vertical path

| MS-IMP-001 §13 step | Integrated evidence |
| --- | --- |
| Merchant established | Real MerchantAccountEstablisher and PostgreSQL bootstrap |
| Merchant configured | Onboarding intent, compiled package, validation/review/approval, serving admission and durable activation |
| Merchant Profile exposed | Durable owner descriptor, real P2/PUBLIC Exposure, exact bounded Merchant Presence query |
| Merchant creates Publication | P3 durable application establishes draft and publishes exact material |
| Public serviceability and customer observation | Real T1 HTTP query and I2/T2B binding |
| Customer submits Enquiry | T2B HTTP submission, E3 revalidation, real atomic E2/E1 persistence |
| Merchant observes Enquiry | T3 HTTP query, durable session currentness, M1 family Exposure and exact M2 mapping |

The full suite covers domain invariants, durable persistence, application/transaction behavior, projection/Exposure, public and merchant APIs, authority and scope, concurrent same-request reconciliation and the complete path. Four new PostgreSQL integrated scenarios additionally verify changed-publication provenance, lost-response recovery, foreign-scope rejection and current admission/session/family authority.

The proof uses one merchant throughout, with no mocked persistence or activation. Its explicit authority/requirement and semantic-bundle fixtures are identified in V1 evidence. MockMvc exercises production controllers; Merchant Profile uses the accepted bounded query handoff. A deployed server, browser UI and complete downstream product are not claimed.

## Programme gate and remaining scope

IMP-07's mandatory programme gate is demonstrated on successful synchronization CI. IMP-08A/B/C become eligible for remaining-work graph refresh, preserving existing conforming implementation and selecting the smallest dependency-complete executable node. No alternate Git branch is created or authorized by this closure.

P6 Opportunity actionability remains separately BLOCKED_DEPENDENCY on authoritative Opportunity facts. It is not required to decide the Publication visibility or Enquiry interaction already proven and is not silently treated as complete. Optional current-subject, CustomerContext, communication and attention material remain outside the minimum read.

Provider/financial work still requires its own accepted hard dependencies, including IMP-08C where specified. This closure does not bypass them, alter master or deploy services.

The V1 evidence, macro closure, current graph, implementation status and executable programme-gate assertions close atomically as an implementation record when the cycle-closing head passes `mvn --batch-mode clean verify -Ppostgres-it`.
