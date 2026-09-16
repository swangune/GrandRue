# MS-PROT-051 v1.3 — Versioned Service Area Geography Representation Amendment

**Document ID:** MS-PROT-051
**Version:** 1.3
**Status:** **ACCEPTED after implementation-gap review, falsification and manual approval**
**Approved:** 30 August 2026
**Amends:** composite MS-PROT-051 through v1.2 only within authoritative Merchant Service Area Descriptor geography representation and retained revision/provenance scope
**Closes:** `IMP-05-A3B-DG-001`
**Depends on:** MS-PROT-027 v1.5; MS-PROT-031; MS-PROT-051 v1.1; MS-PROT-051 v1.2; MS-PROT-053; MS-PROT-059; MS-PROT-063; MS-PROT-069; MS-PROT-071; MS-PROT-076
**Purpose:** Establish the minimum versioned, provider-neutral and non-executable geography representation for authoritative Merchant Service Area Descriptor revisions without turning descriptive public-profile geography into serviceability, delivery, Scheduling or other runtime eligibility authority.

---

## 1. Governing decision

Every active authoritative Merchant Service Area Descriptor revision SHALL
retain one exact `ServiceAreaGeographyV1` value from the approved closed
variant set:

```text
NAMED_AREA
MERCHANT_LOCATION_RADIUS
COUNTRY_WIDE
REMOTE_COUNTRIES
```

The descriptor also retains a merchant-approved public description and explicit
merchant-owned exposure choice.

```text
ServiceAreaGeographyV1
        = descriptive Merchant Profile fact
        ≠ executable geographic eligibility
        ≠ delivery/serviceability policy
        ≠ provider coverage claim
```

---

## 2. Amendment scope and unchanged authority

This amendment governs only:

- the initial closed geography variant set and minimum payloads;
- deterministic structural normalization;
- exact Location-revision affinity for Location-radius geography;
- immutable revision/currentness/provenance evidence; and
- the non-executable boundary.

MS-PROT-051 v1.1 continues unchanged for Merchant Profile ownership, stable
merchant-scoped fact identity, explicit owner operations, immutable revisions,
currentness, optimistic concurrency, logical retry, current actor authority,
Profile/Configuration separation, Exposure and projection separation.

This amendment does not define runtime serviceability, route calculation,
delivery zones, supported postcodes, GIS polygons, geocoding, provider
coverage, transport contracts or audience-specific Exposure resolution.

---

## 3. Canonical ServiceAreaGeographyV1 value

The canonical initial tagged union is:

```text
ServiceAreaGeographyV1
{
    NAMED_AREA
    {
        countryCode
        areaName
    }

    MERCHANT_LOCATION_RADIUS
    {
        MerchantLocation identity
        exact MerchantLocation revision identity
        radiusMetres
    }

    COUNTRY_WIDE
    {
        countryCode
    }

    REMOTE_COUNTRIES
    {
        countryCodes[]
    }
}
```

`countryCode` uses an uppercase ISO 3166-1 alpha-2 value. `areaName` is an exact
non-blank merchant-approved label. `radiusMetres` is a positive whole number.
`countryCodes` is a non-empty set represented canonically as distinct uppercase
codes in lexicographic order.

The variant tag and normalized payload form one immutable geography value.
Missing fields SHALL NOT be represented with blank strings, zero radii,
`UNKNOWN`, `N/A` or provider guesses.

---

## 4. Public description and exposure

Each Merchant Service Area Descriptor revision additionally retains:

```text
publicDescription
merchantOwnedExposureChoice
```

The public description is required, non-blank merchant-approved wording. It
does not replace the structured/bounded geography value.

The initial owner choice is explicit:

```text
PRIVATE_INTERNAL
PUBLIC
```

This retained choice is source fact, not the complete audience-observation
decision governed by MS-PROT-027 v1.5. Fact existence or a `PUBLIC` choice does
not itself publish a projection.

---

## 5. Named-area geography

`NAMED_AREA` represents one merchant-described geographic area qualified by one
country code.

Examples include a city, locality, administrative area or commonly understood
regional name. Main Street does not infer a boundary polygon, postcode set or
runtime serviceability rule from the name.

```text
"Swansea and surrounding areas"
        may describe NAMED_AREA(GB, Swansea)

but

NAMED_AREA(GB, Swansea)
        ≠ executable Swansea boundary
```

---

## 6. Merchant-Location-radius geography

`MERCHANT_LOCATION_RADIUS` is accepted only against one current, same-merchant,
`ACTIVE` Merchant Location revision at the Service Area mutation boundary.

The Service Area revision retains both:

```text
MerchantLocation identity
exact consumed MerchantLocation revision identity
```

plus the positive whole-metre radius. Caller-supplied coordinates, a mutable
current-Location alias or a provider place identifier cannot replace that exact
affinity.

A later Location correction or retirement does not rewrite an accepted Service
Area revision. A new Service Area revision is required to change its retained
Location/radius meaning. Historical affinity remains available even when the
referenced Location is no longer current or active.

The retained radius is descriptive profile geography. It SHALL NOT be evaluated
as executable eligibility unless a future accepted capability contract creates
an explicit binding and owns the runtime rule.

---

## 7. Country-wide and remote-country geography

`COUNTRY_WIDE` describes merchant-approved service throughout one named country.

`REMOTE_COUNTRIES` describes remote service across one or more named countries.
It does not imply physical travel, delivery, legal eligibility, tax treatment,
language support, fulfilment capability or provider readiness.

Country-code equality provides deterministic value identity only. It is not a
current geopolitical, legal or operational decision.

---

## 8. Structural normalization and validity

The initial schema identity is:

```text
MS_SERVICE_AREA_GEOGRAPHY_V1
```

Normalization SHALL:

1. normalize text to Unicode NFC;
2. trim leading and trailing Unicode whitespace;
3. uppercase country codes;
4. reject blank present values;
5. require exactly the payload belonging to the selected variant; and
6. sort and deduplicate `REMOTE_COUNTRIES` country codes.

Normalization SHALL NOT infer a different variant, expand an area into a
polygon/postcode list, geocode a name, choose a Merchant Location, convert a
radius to a provider zone, or rewrite the merchant's public description.

Country codes must be structurally two ASCII letters for this initial boundary.
Postal-address registry validation and geocoder/provider governance are not
transferred into Service Area ownership.

---

## 9. Stable identity, revision and lifecycle

Every Merchant Service Area Descriptor has stable merchant-scoped descriptor
identity independent of geography value or public wording.

Owner-specific operations are:

```text
CreateMerchantServiceArea
UpdateMerchantServiceArea
RetireMerchantServiceArea
```

Each successful material mutation appends one immutable exact revision and
advances one current pointer for that merchant/descriptor identity. Existing
facts require exact expected-current-revision identity. `RETIRED` is terminal
for the initial descriptor identity; a later materially new service area uses a
new identity.

Unrelated Profile facts do not share one global Profile revision or transaction
boundary.

---

## 10. Retry, concurrency and actor authority

Every externally retryable mutation retains one logical request identity.
Exact replay returns the originally committed Service Area revision even after
later revisions or referenced-Location retirement. Reuse of the request
identity with materially different operation, scope, expected revision,
geography, description, exposure, provenance, actor or commit-time intent
conflicts.

Ordinary direct mutation requires the accepted trusted execution context,
resolved Merchant Scope, current actor authority and eligible Merchant Account
lifecycle. The initial implementation may restrict mutation to the authenticated
current Merchant Controller until delegated Profile privileges are registered.

Same-descriptor concurrent mutation commits at most one successor for one
expected current revision.

---

## 11. Required persistent evidence

Every authoritative Service Area revision must establish:

```text
Merchant Scope
stable Service Area descriptor identity
exact revision identity and scope-local revision number
predecessor revision identity where applicable
operation and current lifecycle
ServiceAreaGeographyV1 schema identity
exact normalized variant and payload
public description
merchant-owned exposure choice
exact Merchant Location identity/revision affinity where applicable
logical request identity
source/provenance
actor/origin and current authority provenance
committed time
```

The exact PostgreSQL table/index layout remains an implementation decision under
`MS-PROT-051-V11-DQ-001`; it cannot weaken this evidence contract.

---

## 12. Projection, Exposure and executable boundaries

Committing a Service Area revision does not itself:

- publish or refresh a storefront;
- decide final audience Exposure;
- authorize service, delivery, collection, Booking or Scheduling;
- mutate or recompile Merchant Configuration;
- establish coordinates, routes, polygons or postcodes;
- establish provider coverage or readiness; or
- prove that the merchant can legally or operationally serve the area.

An accepted capability may later bind exact Service Area evidence through its
own explicit registered contract. Without that contract:

> **Descriptive Service Area geography SHALL NOT silently become executable
> geographic eligibility.**

---

## 13. Distinguishable outcomes

The existing MS-PROT-051 v1.1 failure semantics remain. Service-Area-specific
validation must preserve distinguishable recovery for:

```text
UNSUPPORTED_SERVICE_AREA_GEOGRAPHY_SCHEMA
UNSUPPORTED_SERVICE_AREA_GEOGRAPHY_KIND
INVALID_SERVICE_AREA_GEOGRAPHY
LOCATION_NOT_FOUND
LOCATION_RETIRED
PROFILE_FACT_NOT_FOUND
PROFILE_FACT_RETIRED
PROFILE_REVISION_CONFLICT
REQUEST_IDENTITY_CONFLICT
AUTHENTICATION_REQUIRED
AUTHORISATION_REJECTION
MERCHANT_ACCOUNT_OPERATION_RESTRICTED
```

---

## 14. Falsification record

### Mobile tradesperson using a home base

A radius can reference a private active Merchant Location revision without
making the Location public or executable. The public description can remain the
audience-facing wording. **PASS**

### Online consultant serving several countries

`REMOTE_COUNTRIES` represents the declared geographic context without implying
travel, provider readiness, tax or legal eligibility. **PASS**

### Merchant says "South Wales"

`NAMED_AREA` retains the country and exact area name without inventing a GIS
boundary. **PASS**

### Location corrected after radius creation

The old Service Area revision retains the exact consumed Location revision. A
new Service Area revision is required to adopt the corrected current Location
meaning. **PASS**

### Delivery code attempts to use the profile radius

The operation is rejected as an authority violation unless an accepted
capability contract explicitly binds the same evidence. **PASS**

### Retry after later update or Location retirement

The original logical request returns its exact committed revision before
current admission is reevaluated. **PASS**

---

## 15. Rejected alternatives

- public description as the sole authoritative geography representation;
- provider/GIS polygons, postcode sets or route zones in the minimum v1;
- caller booleans asserting Location validity;
- a mutable current-Location alias without exact revision provenance;
- geocoder/provider output as automatic authority;
- Service Area geography as automatic delivery/serviceability policy;
- one universal Business Profile aggregate/version;
- fact existence or `PUBLIC` choice as publication authority; and
- rewriting historical Service Area revisions after Location change.

---

## 16. Hard invariants

1. Every active Service Area revision retains exactly one supported
   `ServiceAreaGeographyV1` variant.
2. Stable Service Area identity is independent of geography value and public
   wording.
3. Merchant-Location radius retains exact same-merchant active Location
   revision affinity at mutation time.
4. Historical Location affinity is immutable.
5. Country and named-area values remain provider-neutral descriptive facts.
6. Profile geography does not become executable eligibility without a separate
   accepted explicit binding.
7. Public description does not replace structured/bounded geography.
8. Source exposure choice, projection and final audience Exposure remain
   separate.
9. Material mutations append exact revisions and use expected-current
   concurrency plus logical retry identity.
10. Caller booleans, mutable aliases and provider results are not authority.
11. Service Area mutation does not compile, activate or publish Merchant
    Configuration.
12. Existing MS-PROT-051 v1.1 ownership and authority boundaries continue to
    govern.

---

## 17. Deferred-decision and implementation consequence

`IMP-05-A3B-DG-001` is resolved by this amendment.

IMP-05 A3b may now implement the minimum relational Service Area authority with
the exact evidence above. It must not pull runtime serviceability, GIS/provider,
Projection, Exposure or transport decisions into the node.

`MS-PROT-051-V11-DQ-001` remains an implementation-detail classification for
exact relational layout. No existing deferred question is silently resolved by
this amendment.

---

## 18. Acceptance statement

> **A Merchant Service Area Descriptor retains one exact versioned descriptive
> geography variant, merchant-approved public wording and explicit source
> exposure choice per immutable revision. Location-radius values retain exact
> active Location revision affinity, while no Service Area fact becomes
> executable geographic eligibility, provider coverage or publication
> authority merely by existing.**

**Review:** PASS
**Falsification:** PASS
**Ambiguity review:** PASS
**Manual approval:** GRANTED — 30 August 2026
**Governance verdict:** **ACCEPTED**
