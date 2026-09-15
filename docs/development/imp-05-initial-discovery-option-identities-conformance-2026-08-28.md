# IMP-05 Initial Discovery Option Identities Conformance — 2026-08-28

**Macro node:** IMP-05 — Merchant Definition, Configuration & Activation  
**Child:** Initial Customer-Interaction Discovery Option Identities  
**Status:** `CONFORMING_COMPLETE`  
**Macro status:** `PARTIALLY_CONFORMING`

## Authority

MS-PROT-052 v1.1 makes the initial customer-interaction discovery option identity set authoritative and removes `VIEW_BUSINESS_INFORMATION` from new definitions because baseline Merchant Profile public presence is not itself an additional capability/discovery choice.

The authoritative v1.1 identities are:

```text
PUBLISH_INFORMATION
SEND_ENQUIRY
ARRANGE_APPOINTMENT
RESERVE_SUBJECT
PLACE_ORDER
SUBSCRIBE_UPDATES
NOTHING_ELSE_FOR_NOW
OTHER
```

## RED evidence

Commit:

`a4c98be483af164fd20a70547c4e68c705927d7a`

Message:

`test: require v1.1 initial discovery option identities`

GitHub Actions Maven Tests #1219 (`33182621114`) compiled 561 existing production sources and then failed during test compilation solely because `InitialCustomerInteractionDiscoveryOption` did not exist. This established the intended missing production contract.

## GREEN evidence

Commit:

`f8cd175e868184fcb552b03e1b324fde661d5b99`

Message:

`feat: add v1.1 initial discovery option identities`

GitHub Actions Maven Tests #1220 (`33182793660`) completed successfully on that exact head:

- production sources compiled: 562
- test sources compiled: 204
- unit tests: 603
- PostgreSQL integration tests: 183
- total tests: 786
- failures: 0
- errors: 0
- skipped: 0
- Flyway migrations validated/applied: 31
- result: `BUILD SUCCESS`

## Conformance claims

This child proves only that:

1. the v1.1 initial discovery option identities are explicit in production code;
2. the exact authoritative eight-value identity set is represented;
3. `VIEW_BUSINESS_INFORMATION` is not part of the new-definition identity set.

## Explicit non-claims

This child does **not** define or claim completion of:

- option display labels or localisation;
- selection cardinality or mutual-exclusion rules;
- interpretation of `OTHER` content;
- discovery mappings or semantic seeds;
- question-definition versioning;
- answer provenance or persistence;
- Onboarding Case lifecycle, revisioning, concurrency or submission;
- Initial Configuration Intent;
- Merchant Profile/Location adoption;
- configuration compilation or activation;
- IMP-05 as a whole.

The next implementation target must be selected from the refreshed IMP-05 graph against MS-PROT-052 v1.2 and the rest of the current accepted authority.
