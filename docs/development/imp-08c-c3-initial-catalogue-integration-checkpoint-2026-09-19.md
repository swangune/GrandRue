# IMP-08C-C3 Initial Standard Catalogue Integration Checkpoint — 19 September 2026

**Node:** `IMP-08C-C3 — Execution failures/outcome ledger`  
**State:** `IN_PROGRESS`  
**Branch:** `development`  
**Verified source head:** `c498398bb8bbb0ec8feaa24402e61a3f6e652c36`  
**Closure claim:** NONE

## Purpose

Implement the first dependency-complete C3 slice made READY by accepted MS-PROT-056 v1.10. This checkpoint is implementation evidence only; it does not publish the catalogue, activate a plan, establish pricing or close C3.

## Accepted authority implemented

- MS-PROT-056 v1.9 §§4–6, 13 — exact immutable bindings, supporting requirements and retained manifest evidence.
- MS-PROT-056 v1.10 §§1–18, 26–31, 43–48 — exact `standard-commercial-catalogue@1`, 36 bindings, explicit 13/34/36 snapshots, strict monotonicity and owner-qualified conditional supporting-commercial relationships.
- `designs/IMPLEMENTATION-RULES.md` v2.1 — tests first, minimum conforming implementation, source-rooted correction and proportional verification.

## RED evidence

GitHub Actions run `35464488430` / Maven Tests #896 failed at test compilation before the production implementation existed.

The failures were exactly the missing required production concepts:

- `CommercialConditionalSupportAlternative`;
- `CommercialConditionalSupportingAccessRequirement`; and
- `InitialStandardCommercialCatalogue`.

No unrelated migration/build defect was implicated.

## Implemented behaviour

The slice adds:

- `CommercialRequiredPurpose` — one exact supporting target/purpose pair;
- `CommercialConditionalSupportAlternative` — one source-owned condition value with zero or more required Commercial purposes;
- `CommercialConditionalSupportingAccessRequirement` — retained owner-qualified conditional support evidence;
- `CommercialAccessBinding.conditionalSupportingAccessRequirements` while retaining the existing constructor for unconditional callers;
- conditional manifest validation that:
  - resolves every protected alternative to an exact retained binding;
  - does not flatten alternatives into an AND requirement; and
  - requires each plan granting the root service to contain at least one commercially satisfiable alternative;
- manifest codec v2 retention of conditional support, with decoding compatibility for retained v1 content;
- `InitialStandardCommercialCatalogue.manifest()`, representing exactly the accepted v1.10 catalogue:
  - `standard-commercial-catalogue@1`;
  - `standard-plan/free@1` — 13 explicit grants;
  - `standard-plan/business@1` — 34 explicit grants;
  - `standard-plan/growth@1` — 36 explicit grants;
  - 36 unique entitlement/binding identities;
  - no wildcard, reservation, AI-brand, Notification or client-platform pseudo-entitlement;
  - website-delivery one-of namespace support;
  - automated-response / new-Message conditional Customer Communication support;
  - Invoice / Payment Obligation conditional support; and
  - Marketing / Publication conditional support for `WEBSITE_ANNOUNCEMENT_V1`.

Catalogue construction is stateless and repeatable.

## Verification

The final full gate ran automatically from source head `c498398bb8bbb0ec8feaa24402e61a3f6e652c36`:

```text
GitHub Actions run      35464853053
Maven Tests run number  907
command                 mvn --batch-mode clean verify -Ppostgres-it

production compilation  PASS — 1,098 sources
test compilation        PASS — 377 sources
unit/governance tests   PASS — 1,259 / 0 failures / 0 errors / 0 skipped
PostgreSQL integration  PASS — 445 / 0 failures / 0 errors / 0 skipped
result                  BUILD SUCCESS
total time              01:57
```

Relevant focused classes inside the full gate:

- `CommercialCatalogueManifestTest` — 24 GREEN;
- `InitialStandardCommercialCatalogueTest` — 4 GREEN;
- `CommercialCatalogueManifestCodecTest` — 5 GREEN.

## Non-claims / remaining C3 work

This slice does **not**:

- execute `PublishStandardCommercialCatalogue`;
- establish a catalogue publication instant;
- change Standing Free baselines or Merchant Commercial Agreements;
- establish a concrete production publishing principal;
- supply the final trusted publication-authorisation composition;
- activate the ordinary account path against the initial catalogue; or
- close C3.

The next dependency-complete C3 responsibility is trusted admission of the exact approved initial manifest, preserving the distinction between exact-content approval and platform publication authorisation.
