# IMP-08C-C3 Trusted Catalogue Admission Checkpoint — 19 September 2026

**Node:** `IMP-08C-C3 — Execution failures/outcome ledger`  
**State:** `IN_PROGRESS`  
**Branch:** `development`  
**Verified source head:** `cf032f4de2842db74b23fcfcbbb8ac192f0abeac`  
**Closure claim:** NONE

## Purpose

Implement the next dependency-complete C3 slice after the approved initial catalogue representation: trusted publication admission for exactly the accepted `standard-commercial-catalogue@1`. This checkpoint is implementation evidence only. It does not publish a catalogue generation, assign P0, enable ordinary account creation or close C3.

## Accepted authority implemented

- MS-PROT-056 v1.9 §§7–8, 12 — catalogue publication is platform-controlled; trusted attribution and explicit current publication authorisation are independent; new publication requires exact approved-manifest affinity; failure categories remain distinguishable.
- MS-PROT-056 v1.10 §§26–31 — exact approval provenance and exact approved initial manifest; approval is not publication and no runtime tier-name inference is introduced.
- MS-PROT-063 §§3.4, 4, 19–20 — trusted platform execution context is attribution, not authorisation.
- `designs/IMPLEMENTATION-RULES.md` v2.1 — tests first, minimum conforming implementation and proportional verification.

## RED evidence

GitHub Actions run `35465101602` / Maven Tests #911 failed at test compilation because the production admission types did not yet exist.

The missing concepts were exactly:

- `CommercialCataloguePublicationAuthorisationAuthority`; and
- `InitialStandardCommercialCataloguePublicationAdmission`.

No unrelated production, persistence, migration or schema defect was implicated.

## Implemented behaviour

The slice adds:

- `CommercialCataloguePublicationAuthorisationAuthority` — an injected current platform-authorisation predicate for catalogue publication;
- `InitialStandardCommercialCataloguePublicationAdmission` — the production `CommercialCataloguePublicationAdmission` implementation for the first approved manifest.

The admission implementation:

1. requires a non-null trusted platform execution context;
2. re-evaluates the injected publication-authorisation authority on every invocation, including retry paths;
3. rejects absent current permission with `AUTHORISATION_REJECTED`;
4. reconstructs the exact approved v1.10 manifest through `InitialStandardCommercialCatalogue.manifest()`;
5. admits only exact manifest equality, including catalogue/plan identities, bindings, conditional support, allocation evidence and approval provenance;
6. rejects structurally valid but unapproved content, or modified approval provenance, with `VALIDATION_REJECTED`;
7. does not publish the catalogue, establish the authorisation source, infer permission from principal identity, or grant merchant/runtime access.

## Verification

The final full gate ran automatically from source head `cf032f4de2842db74b23fcfcbbb8ac192f0abeac`:

```text
GitHub Actions run      35465196370
Maven Tests run number  912
command                 mvn --batch-mode clean verify -Ppostgres-it

unit/governance tests   PASS — 1,264 / 0 failures / 0 errors / 0 skipped
PostgreSQL integration  PASS — 445 / 0 failures / 0 errors / 0 skipped
result                  BUILD SUCCESS
```

The new `InitialStandardCommercialCataloguePublicationAdmissionTest` is GREEN inside that full gate.

## Falsification

The full gate proves the bounded admission behaviours requested by the RED tests:

- trusted platform attribution without explicit current publication authority is rejected;
- exact approved manifest + current authority is admitted;
- changed manifest content cannot reuse approval text to gain admission;
- structurally valid but unapproved catalogue content is rejected; and
- publication authority is re-evaluated rather than cached by the admission component.

## Non-claims / remaining C3 work

This slice does **not**:

- execute `PublishStandardCommercialCatalogue`;
- establish the initial authoritative publication instant `P0`;
- provide a merchant-facing or public publication endpoint;
- infer platform publication authority from Merchant Controller/staff/subscription/provider state;
- prove production rollout ordering before ordinary new-account establishment; or
- close C3.

The next dependency-complete C3 responsibility is production publication/rollout composition: the initial catalogue must be established through the trusted publication operation before the ordinary new-account path that depends on catalogue coverage is enabled.
