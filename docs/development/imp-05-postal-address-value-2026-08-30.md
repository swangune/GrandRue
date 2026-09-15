# IMP-05 — PostalAddressV1 Value and Normalization

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** A4a — PostalAddressV1 value, validation and normalization
**Date:** 30 August 2026
**Status:** **CONFORMING_COMPLETE**

## Governing authority

Composite MS-PROT-051 through accepted v1.2 governs this node. MS-PROT-051
v1.2 resolves `MS-PROT-051-V11-DQ-002`; it does not activate the separately
deferred geocoder/provider, UI, Exposure or transport questions.

## Implemented boundary

The implementation retains exact original structured input and a deterministic
normalized `PostalAddressV1` with explicit schema, normalization-profile and
country-registry identity. It validates ISO alpha-2 country membership, ordered
nonblank address lines, nonblank optional components and control-character
absence without requiring universal locality, region or postal-code fields.

Normalization performs Unicode NFC, boundary trimming and country-code
uppercasing while preserving line order, internal characters, case, punctuation
and spacing. Optional accepted coordinates retain source and acceptance
provenance and enforce latitude/longitude bounds.

## RED → GREEN and canonical evidence

RED failed compilation because the structured input, normalized value,
evidence, validation failure and accepted-coordinate contracts did not exist.

GREEN passed four focused contracts covering deterministic minimal
normalization/original evidence, international optionality, invalid structure
and country rejection, and coordinate range/provenance.

```text
mvn --batch-mode -Ppostgres-it clean verify

Flyway migrations validated:           45
production Java sources compiled:      722
test Java sources compiled:            244
unit / conformance tests:              674 PASS
PostgreSQL integration tests:          264 PASS
total Maven tests:                     938 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

The canonical run used the isolated `mainstreet_test` PostgreSQL database and
no competing MainStreet Maven process. Editor Java autobuild was restored.

## Non-claims and next node

A4a does not create Merchant Location identity, revisions, lifecycle,
persistence, Exposure, geocoding, projection, UI or transport behavior. A4b
durable Merchant Location authority is now the smallest critical READY node.
