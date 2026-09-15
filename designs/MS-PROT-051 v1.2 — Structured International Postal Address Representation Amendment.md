# MS-PROT-051 v1.2 — Structured International Postal Address Representation Amendment

**Document ID:** MS-PROT-051
**Version:** 1.2
**Status:** **ACCEPTED after deferred-decision review, falsification and manual approval**
**Approved:** 30 August 2026
**Amends:** composite MS-PROT-051 through v1.1 only within authoritative Merchant Location structured postal-address representation and retained normalization/provenance scope
**Closes:** `MS-PROT-051-V11-DQ-002`
**Depends on:** MS-PROT-031; MS-PROT-051 v1.1; MS-PROT-053; MS-PROT-059; MS-PROT-063; MS-PROT-069; MS-PROT-071; MS-PROT-076
**Purpose:** Establish one provider-neutral, international and historically reproducible structured postal-address value for Merchant Location revisions without identifying a Location by its address, treating formatting or geocoding as authority, or imposing one country's addressing conventions globally.

---

## 1. Governing decision

Every authoritative Merchant Location revision that represents an active
physical place SHALL retain one exact `PostalAddressV1` value.

```text
MerchantLocation identity
        ≠
PostalAddressV1 value

MerchantLocationRevision
        +
exact PostalAddressV1
        +
schema / normalization / country-registry affinity
        ↓
historically reproducible authoritative place description
```

The address is a revisioned value. It is not the Location identity, a tenant
boundary, an Exposure decision, a provider place identifier or proof of
deliverability.

---

## 2. Amendment scope and unchanged authority

This amendment governs only:

- the exact structured postal-address field set;
- minimum structural validity;
- deterministic normalization and retained policy affinity;
- original-input and normalized-value evidence; and
- optional accepted coordinate provenance.

MS-PROT-051 v1.1 continues unchanged for Merchant Location ownership, stable
merchant-scoped identity, `ACTIVE → RETIRED` lifecycle, correction versus
relocation, immutable revisions, currentness, concurrency, idempotency,
onboarding adoption, actor authority, configuration bindings, Exposure and
projection separation.

This amendment does not select a geocoding/map provider, transport contract,
merchant UI, projection format, public rendering convention or postal-delivery
verification service.

---

## 3. Canonical PostalAddressV1 value

The canonical initial structured value is:

```text
PostalAddressV1
{
    countryCode                         // required ISO 3166-1 alpha-2
    addressLines[]                      // required ordered non-empty collection
    dependentLocality?                  // optional
    locality?                           // optional
    administrativeArea?                 // optional
    postalCode?                         // optional
    sortingCode?                        // optional
}
```

`addressLines` preserves merchant-approved order. It may contain a building,
premises, street, thoroughfare, post-box or another country-appropriate line.
Main Street SHALL NOT assign universal semantic meaning to line positions.

Optional means genuine absence is permitted. Missing optional fields SHALL NOT
be filled with empty strings, `UNKNOWN`, `N/A`, guessed values or provider
defaults.

---

## 4. International representation boundary

`countryCode` is required and SHALL be validated against one identified
version of the platform's ISO 3166-1 alpha-2 registry.

The initial contract SHALL NOT globally require:

```text
state / province
postal code
city
street number
county
```

because those concepts and their mandatory status vary by country. The
country-registry version retained with the revision provides historical
validation affinity; later registry changes do not rewrite an accepted
historical address.

Country-specific enrichment or deliverability validation may later reject an
operation only through separately accepted policy. It does not silently alter
the canonical value stored here.

---

## 5. Original structured input and normalized authority

One accepted Location revision retains both:

```text
exact original structured address components
        +
exact normalized PostalAddressV1 components
```

The normalized value is the authoritative address value used for deterministic
equality, retry-intent comparison and current reads. Original structured input
is provenance and reconstruction evidence; it is not a second editable fact.

Both remain subject to MS-PROT-053 data-protection authority.

---

## 6. Initial normalization profile

The initial profile identity is:

```text
MS_POSTAL_ADDRESS_NORMALIZATION_V1
```

It performs only deterministic structural normalization:

1. normalize text to Unicode NFC;
2. remove leading and trailing Unicode whitespace from every component;
3. uppercase the two-letter country code;
4. preserve address-line order;
5. preserve internal characters, punctuation, case and spacing; and
6. represent absent optional components as absent, never as blank values.

It SHALL NOT:

- reorder address lines;
- expand or abbreviate words;
- translate or transliterate;
- infer a locality, administrative area or postal code;
- change country-specific case or punctuation;
- replace merchant input with a geocoder result; or
- claim postal deliverability.

Every retained revision identifies its address schema version, normalization
profile identity and country-registry version. A later normalization profile
requires a new Location revision to change authoritative current meaning; it
does not rewrite history.

---

## 7. Structural validity

A `PostalAddressV1` is structurally valid only when:

- `countryCode` resolves under the retained country registry;
- at least one normalized address line exists;
- every normalized address line is non-blank;
- every present optional component is non-blank; and
- no component contains a disallowed control character.

This is structural acceptance, not verification that mail can be delivered or
that the merchant occupies the place.

Implementation safety bounds for individual text/component size may be applied
consistently provided they do not introduce country-specific semantic fields
or rewrite accepted values.

---

## 8. Optional accepted coordinates

A Merchant Location revision MAY additionally retain an exact coordinate
pair:

```text
AcceptedLocationCoordinates
{
    latitude
    longitude
    sourceKind
    sourceReference?
    acceptedByActorIdentity
    acceptedAt
}
```

Latitude is within `[-90, 90]`; longitude is within `[-180, 180]`.

Coordinates may originate from merchant selection, geocoding, import or other
evidence, but become part of authoritative Location state only when accepted by
the governing Profile/Location operation. A provider response or place ID by
itself is candidate evidence, not Main Street authority.

Provider selection, confidence scoring and coordinate-normalization process
remain deferred under `MS-PROT-051-V11-DQ-003` until introduced.

---

## 9. Merchant Location revision evidence

In addition to the evidence already required by MS-PROT-051 v1.1, each
address-bearing revision must establish:

```text
Merchant Scope
Merchant Location identity
Location revision identity and scope-local revision number
predecessor revision identity where applicable
current lifecycle
exact original structured address
exact normalized PostalAddressV1
address schema version
normalization profile identity
country-registry version
optional accepted coordinates and their provenance
actor/origin
logical request identity
committed time
```

The exact revision/current pointer remains the concurrency boundary. Address
equality does not merge Location identities and does not deduplicate distinct
merchant-approved physical places.

---

## 10. Correction, relocation and retirement

A same-place correction may append a new revision for the same Location
identity using a new exact `PostalAddressV1`.

A material relocation SHALL create a new Merchant Location identity under
MS-PROT-051 v1.1. Matching or nearby coordinates, postal codes or normalized
strings SHALL NOT decide correction versus relocation automatically.

Retirement preserves the last exact address-bearing history. It does not erase
or anonymize the address merely through the semantic lifecycle transition;
retention/erasure remains governed by MS-PROT-053.

---

## 11. Retry and concurrency consequence

Location create/correct/retire operations retain the v1.1 expected-revision
and logical-request contracts.

Material retry intent for address-bearing operations includes at least:

```text
operation kind
Merchant Scope
Merchant Location identity where already assigned
expected current revision
normalized PostalAddressV1
schema / normalization / country-registry affinity
optional accepted coordinate value and provenance
```

Exact replay returns the original committed revision. Reuse of one request
identity with materially different address or coordinate intent conflicts.

---

## 12. Projection, Exposure and provider boundaries

An authoritative address commit does not itself:

- publish the address;
- decide `EXPOSE | WITHHOLD`;
- refresh a storefront synchronously;
- establish verification or trust;
- prove customer serviceability or delivery eligibility;
- bind a capability/configuration to the Location;
- make the Location a Resource; or
- make a geocoder/provider authoritative.

Caller-formatted display strings are presentation inputs/outputs and SHALL NOT
replace the structured authority. Projections may format the structured value
for an audience but cannot mutate or reinterpret the owner revision.

---

## 13. Distinguishable outcomes

The existing MS-PROT-051 v1.1 failure semantics remain. Address-specific
validation must preserve distinguishable recovery for:

```text
UNSUPPORTED_ADDRESS_SCHEMA
UNSUPPORTED_NORMALIZATION_PROFILE
UNSUPPORTED_COUNTRY_REGISTRY
INVALID_COUNTRY_CODE
INVALID_POSTAL_ADDRESS_STRUCTURE
INVALID_COORDINATES
```

These are validation rejections and do not imply provider failure or Location
revision conflict.

---

## 14. Falsification record

### Country without mandatory postal code

The address is accepted without inventing a postal code because the global
contract does not require one. **PASS**

### Non-Latin address

Unicode content survives NFC/boundary normalization without transliteration or
case rewriting. **PASS**

### Geocoder disagreement

A provider returns differently formatted lines and coordinates. Current Main
Street authority remains unchanged until an owner operation accepts a revision.
**PASS**

### Same-place typo correction

A postcode or building-name correction appends a revision to the same Location
identity. **PASS**

### Physical relocation

Changing to a materially different place does not mutate the old identity;
the application creates a new Location identity. **PASS**

### Historical normalization

A later profile version does not silently recalculate an old revision because
the old schema, normalization and country-registry identities remain retained.
**PASS**

### Duplicate formatted address

Two distinct Location identities may retain equal normalized address values;
value equality does not merge identity. **PASS**

---

## 15. Rejected alternatives

- one free-form address string as authoritative storage;
- mandatory US/UK-style city/state/postcode fields for every country;
- address or coordinates as Merchant Location identity;
- mutable normalization that rewrites old revisions;
- geocoder/provider output as automatic authority;
- provider place ID as the canonical Main Street location identity;
- country-rule inference without retained registry/version affinity;
- blank strings as absent optional components;
- public exposure inferred from address existence; and
- address correction automatically classified as physical relocation.

---

## 16. Hard invariants

1. `MerchantLocationIdentity` is not `PostalAddressV1`.
2. Every active authoritative Location revision retains one exact normalized
   `PostalAddressV1` and its original structured-input evidence.
3. Country, schema, normalization and country-registry affinity are explicit.
4. Address-line order is authoritative and preserved.
5. International optional components are not fabricated.
6. Normalization is structural and deterministic, not semantic inference.
7. Historical revisions are not silently renormalized.
8. Coordinates are optional accepted evidence, not provider authority.
9. Same-place correction may retain identity; material relocation creates a
   new identity.
10. Address existence does not imply Exposure, verification, configuration,
    serviceability, deliverability or Resource semantics.
11. Caller-formatted strings and unaccepted provider results are not authority.
12. Existing v1.1 revision, concurrency, retry, lifecycle and actor rules
    continue to govern.

---

## 17. Deferred-decision and implementation consequence

`MS-PROT-051-V11-DQ-002` is resolved by this amendment.

The first implementation may now create the minimum relational Merchant
Location authority required by IMP-05 A4. It must retain the exact evidence
above and must not pull DQ-003 geocoder/provider decisions, DQ-004 UI workflow,
DQ-006 Exposure resolution or DQ-009 transport contracts into A4.

After A4 conforms, B2b may integrate location-scoped stable weekly Business
Hours using the current same-merchant active Location identity required by
MS-PROT-050 v1.4.

---

## 18. Acceptance statement

> **A Merchant Location retains one version-affined, provider-neutral structured
> international postal address per authoritative active revision. Exact original
> structured input and deterministic normalized components preserve historical
> meaning without making the address, a formatted string, coordinates or a
> geocoder result the Location identity or public authority.**

**Review:** PASS
**Falsification:** PASS
**Ambiguity review:** PASS
**Manual approval:** GRANTED — 30 August 2026
**Governance verdict:** **ACCEPTED**
