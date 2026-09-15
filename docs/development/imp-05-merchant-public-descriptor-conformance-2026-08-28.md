# IMP-05 Merchant Public Descriptor Semantic Primitive Conformance — 2026-08-28

**Programme:** MS-IMP-001  
**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Merchant Public Descriptor semantic primitive  
**Status:** CONFORMING_COMPLETE for this semantic primitive only  
**Macro status:** IMP-05 remains PARTIALLY_CONFORMING

## Accepted authority

This child is governed by:

- `designs/MS-IMP-001.md`;
- `designs/IMPLEMENTATION-RULES.md`;
- `designs/MS-PROT-051 — Merchant Profile, Public Business Information & Presence Model.md`;
- `designs/MS-PROT-032 —Backend Module, Bounded Context & Dependency Architecture.md`.

MS-PROT-051 separates merchant-authored or merchant-approved public descriptive information from legal identity, verification/trust claims, business classification and capability activation. It also permits the conceptual profile model to be realised through smaller implementation-owned structures rather than requiring one monolithic aggregate or table.

## Implemented contract

`mainstreet.merchantprofile.MerchantPublicDescriptor` now represents the smallest accepted public-descriptor primitive with:

- explicit `MerchantScope`;
- required non-blank public `displayName`;
- optional `tagline`;
- optional `shortSummary`;
- optional `approvedDescription`.

The implementation deliberately contains no executable capability/configuration authority and no trust or legal-identity authority.

## RED evidence

Commit:

`14600eb29bcff319e682ccd8de89d5c900a9d201`

`test: require merchant public descriptor semantic primitive`

Maven Tests #1207 (`33180131213`) failed during test compilation only because `MerchantPublicDescriptor` did not yet exist. Production compilation completed first with 557 source files. The RED test therefore represented the missing accepted primitive rather than an unrelated build failure.

## GREEN implementation

Commit:

`6eed6dc744f32262e24332dd5614ac90ac40ea75`

`feat: add merchant public descriptor semantic primitive`

Production change:

- added `src/main/java/mainstreet/merchantprofile/MerchantPublicDescriptor.java`;
- added no persistence schema or migration;
- added no Spring, provider, compiler, capability, exposure, AI or runtime coupling;
- depends only on the existing `MerchantScope` authority plus Java value types.

## Verification

Maven Tests #1208 (`33180244430`) ran against exact head:

`6eed6dc744f32262e24332dd5614ac90ac40ea75`

Result:

- production sources compiled: 558;
- test sources compiled: 200;
- unit tests: 598;
- PostgreSQL integration tests: 183;
- total tests: 781;
- failures: 0;
- errors: 0;
- skipped: 0;
- Flyway migrations validated/applied: 31;
- `MerchantPublicDescriptorTest`: 3/3 passing;
- architecture/corpus/conformance tests: passing;
- BUILD SUCCESS.

## Explicit non-claims

This evidence does **not** claim completion of the complete Merchant Profile or IMP-05.

The following remain outside this child and require their own dependency classification and implementation evidence where applicable:

- durable descriptor/profile persistence and revision history;
- descriptor provenance/edit evidence;
- contact points;
- physical locations, service areas and location exposure policy;
- operating status and Business Hours composition;
- media/profile presentation references;
- editorial validation and public projection;
- trust/verification claims;
- legal identity;
- business classification;
- onboarding case and structured onboarding answers;
- AI proposal/merchant-approval boundary;
- Initial Configuration Intent and Configuration Revision;
- compilation, activation, entitlement and runtime applicability/eligibility.

Merchant Public Descriptor therefore establishes one dependency-complete semantic primitive for IMP-05 without pre-empting later persistence, workflow, AI, exposure or configuration decisions.
