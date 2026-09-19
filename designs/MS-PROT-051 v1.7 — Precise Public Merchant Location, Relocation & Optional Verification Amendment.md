# MS-PROT-051 v1.7 — Precise Public Merchant Location, Relocation & Optional Verification Amendment

**Document ID:** MS-PROT-051  
**Version:** 1.7  
**Status:** **ACCEPTED by explicit manual approval on 18 September 2026; approval reconfirmed on 19 September 2026**  
**Approved:** 18 September 2026  
**Authority type:** Merchant Profile / Merchant Location semantic amendment  
**Governed by:** MS-DESIGN-RULES-001 v2.5; DOCUMENT-GOVERNANCE.md; MS-FUNDAMENTAL-VISION-001  
**Amends:** Composite MS-PROT-051 through v1.6 within public physical-location precision, customer navigation, merchant-facing address change/relocation, location-resolution evidence and verification separation  
**Depends on:** Composite MS-PROT-027; composite MS-PROT-028 through v1.3; composite MS-PROT-051 through v1.6; composite MS-PROT-052 through v1.2; MS-PROT-053  
**Implementation activation:** NONE  
**Deferred implementation dependency:** MS-PROT-051-V11-DQ-003 and applicable relocation UI/API scope under MS-PROT-051-V11-DQ-004  
**Purpose:** Ensure every publicly navigable Merchant Location resolves to a precise destination while allowing merchants to change or move their shop through ordinary business language, without exposing coordinates, internal location identities, map administration or mandatory premises verification.

---

## 1. Governing decision

GrandRue SHALL distinguish:

    ADDRESS / LOCATION RESOLUTION
            ≠
    PHYSICAL RELOCATION
            ≠
    LOCATION VERIFICATION

A merchant SHALL be able to change a shop address.

The ordinary merchant experience SHALL NOT require the merchant to understand whether GrandRue internally performs:

    same-place correction
            OR
    new Merchant Location identity + retirement of old identity

Canonical merchant interaction:

    Shop
    24 High Street

    [Change address]

If the proposed address represents a materially different physical place:

    Has this shop moved to this new address?

    [Yes, move this shop]
    [No, I'm correcting the address]

GrandRue SHALL handle the resulting location lifecycle internally.

---

## 2. Merchant language rule

Merchant-facing interaction SHALL use ordinary business concepts.

Preferred:

    Change shop address
    Move this shop
    Correct this address
    Where should customers find you?

Prohibited as ordinary merchant interaction:

    Create replacement MerchantLocationIdentity
    Retire location L1
    Rebind configuration to L2
    Edit latitude / longitude

Those are internal semantics.

---

## 3. Merchant Location identity remains internal

Composite MS-PROT-051 remains authoritative that a Merchant Location identity represents one continuing physical place.

Therefore:

    same physical place
    +
    address correction
            ↓
    same Merchant Location identity
    new immutable revision

while:

    shop physically moves
            ↓
    new Merchant Location identity
    old Merchant Location eventually retired

This historical distinction remains necessary even though the merchant experiences both through the same Business Profile.

The merchant SHALL NOT be required to manually create the replacement identity.

---

## 4. Canonical address-change operation

GrandRue SHALL support a merchant-facing operation conceptually equivalent to:

    ChangeShopAddress

The application layer SHALL resolve that intent into one of:

    CORRECT_EXISTING_PLACE

or:

    RELOCATE_SHOP

The operation SHALL NOT determine relocation solely from distance, postcode difference, coordinate difference, provider result or address-string comparison because those signals cannot always distinguish correction from relocation.

Where the distinction is materially ambiguous, GrandRue SHALL ask the merchant using ordinary business language.

---

## 5. Same-place correction

A merchant may correct information about the same premises.

Examples include:

    wrong postcode
    wrong building name
    misspelled street
    missing unit number
    incorrect coordinate
    provider placed entrance incorrectly

The merchant interaction may simply be:

    [Edit address]

A valid same-place correction:

- preserves the existing Merchant Location identity;
- creates a new immutable Location revision;
- preserves historical revisions;
- preserves the existing Location public-exposure choice;
- may replace the accepted navigation point where correctly resolved; and
- does not itself create a verification claim.

---

## 6. Physical relocation

Where the merchant has actually moved the shop to a materially different physical place, GrandRue SHALL treat this as relocation.

Merchant-facing:

    Old:
    24 High Street

    New:
    81 Market Road

    Has this shop moved here?

    [Yes, move this shop]

GrandRue internally performs the equivalent of:

    existing Merchant Location L1
            ↓
    create replacement Merchant Location L2
            ↓
    resolve L2 precisely
            ↓
    prepare affected current relationships
            ↓
    cut over new ordinary activity
            ↓
    retire L1 for new ordinary use

The merchant SHALL NOT need to understand L1 or L2.

---

## 7. Relocation must preserve history

A shop move SHALL NOT rewrite history.

If an Order, Appointment or other historical event occurred at 24 High Street and the merchant later moves to 81 Market Road, the historical record SHALL remain associated with the old location meaning.

Historical commitments retain the exact Location revision or immutable location meaning required by their owning capability.

---

## 8. Existing future commitments must not move silently

A shop relocation SHALL NOT automatically rewrite already-established future commitments.

If an Appointment or another commitment is already committed at old premises L1, the shop move SHALL NOT silently convert it to L2.

The owning capability determines whether the commitment remains at L1, is explicitly rescheduled, is cancelled/replaced, or can be moved through an accepted capability operation.

The relocation workflow MAY surface such affected commitments to the merchant.

It SHALL NOT invent their resolution.

---

## 9. Current bindings and dependencies

Where current configuration explicitly references the old Merchant Location, including an Offering, Business Hours or another accepted binding, GrandRue's relocation workflow MAY orchestrate the required transition, but semantic ownership remains with the owning capabilities.

Canonical:

    merchant says:
    "Move this shop"
            ↓
    GrandRue determines affected current relationships
            ↓
    prepare valid owner-specific changes
            ↓
    merchant resolves any material exceptions
            ↓
    owners commit their authorised changes
            ↓
    L2 becomes current destination
            ↓
    L1 retires for new ordinary use

GrandRue SHALL NOT bypass another capability's authority merely to make relocation convenient.

---

## 10. No unnecessary merchant reconfiguration

A shop move SHOULD preserve business settings that do not semantically change merely because the physical address changed.

GrandRue SHALL distinguish:

    business fact still valid after move

from:

    fact specifically bound to old physical place

The relocation workflow SHOULD reuse or prepare continuing information where accepted authority permits it.

The merchant SHOULD NOT be forced to recreate unrelated profile information, products, services or ordinary business configuration merely because the shop moved.

---

## 11. Relocation cutover

GrandRue SHALL avoid publishing the new location before it is sufficiently resolved.

Canonical sequence:

    old location L1 currently active
            ↓
    merchant supplies new shop address
            ↓
    GrandRue resolves L2
            ↓
    required dependent changes prepared
            ↓
    new destination ready
            ↓
    controlled cutover
            ↓
    L2 used for new public navigation/activity
    L1 no longer used for new ordinary activity

GrandRue SHALL NOT replace a known precise old destination with an unresolved new destination.

---

## 12. Failed relocation preparation

If the new address cannot yet be resolved precisely, GrandRue SHALL NOT retire L1 prematurely, publish an approximate L2, or route customers to a postcode/locality centroid.

The merchant may continue correcting the new address or use another supported resolution route.

Unrelated merchant capabilities remain unaffected.

---

## 13. Location Resolution

**Location Resolution** establishes where a physical Merchant Location is sufficiently precisely for customer navigation.

It answers:

> Where should customers be navigated?

It does not answer:

> Has GrandRue independently verified the merchant's right to represent this premises?

A precise accepted navigation point SHALL be provider-neutral domain state.

---

## 14. Coordinates remain internal

Coordinates SHALL remain invisible implementation information during ordinary merchant operation.

The merchant SHALL NOT be required to understand latitude, longitude, coordinate systems, GPS accuracy, geocoder confidence or provider place IDs.

The merchant interacts with ordinary business concepts such as shop, office, building, unit, address and business entrance.

GrandRue performs the translation.

---

## 15. Public Navigation Ready

PUBLIC_NAVIGATION_READY is a derived predicate, not an authoritative lifecycle status.

It requires:

    Merchant Location exists
    AND
    Merchant Location = ACTIVE
    AND
    current Location revision contains
        an accepted precise navigation point
    AND
    the applicable Location Resolution contract accepts it

Merchant public-exposure intent remains separate.

---

## 16. Public location precision

A Merchant Location exposed as a public customer destination MUST resolve to its accepted precise navigation point.

GrandRue SHALL NOT knowingly substitute a district centroid, postcode centroid, city centre, market centre, road midpoint or approximate landmark for the actual Merchant Location.

---

## 17. Address and navigation point remain distinct

GrandRue SHALL preserve:

    human-readable address
            ≠
    precise navigation point

A country may provide a very precise postal address. Another country may provide a different legitimate addressing mechanism.

The domain SHALL NOT require one national addressing model globally.

GrandRue SHALL NOT invent vague text such as "Near Bodija Market" and present it as though it were a precise premises address.

---

## 18. Unit and internal-premises information

Coordinates cannot identify Shop 14, Suite 3B, Unit 27 or Floor 4 inside a shared property.

Applicable unit/building information SHALL therefore remain part of the merchant-approved human-readable premises information.

Coordinate equality SHALL NOT merge different Merchant Locations.

---

## 19. No mandatory map manipulation

Moving a map pin SHALL NOT be required for ordinary location establishment or address change.

A map MAY be offered as optional confirmation, optional correction mechanism or advanced fallback, but GrandRue SHALL provide a non-map route wherever the platform otherwise supports resolution of that location.

---

## 20. Physical presence is optional

GrandRue MAY offer "I'm at the shop now" as one resolution route.

Physical presence SHALL NOT be mandatory when another accepted method can resolve the premises precisely.

Remote owners and multi-location merchants therefore remain supported.

---

## 21. Verification remains optional

Location Resolution and Location Verification remain separate.

A valid state is:

    Merchant Location = ACTIVE
    navigation point = precise
    public choice = PUBLIC
    verification claim = absent

GrandRue SHALL NOT describe such a location as independently verified.

Optional verification MAY be offered separately.

A separately accepted law, security, safety or provider requirement MAY require a specific Trust Claim for a bounded scope.

Such a requirement SHALL NOT silently become a universal merchant publication gate.

---

## 22. Changing address does not require re-verification by default

A same-place address correction SHALL NOT automatically require optional Location Verification to be performed again.

A physical relocation SHALL NOT automatically make Location Verification mandatory either.

Where an existing optional Trust Claim is inherently tied to the previous physical premises, that specific claim may cease to apply to the new location according to its own Trust semantics.

This SHALL NOT prevent GrandRue from maintaining a precise unverified public destination unless another accepted scoped rule requires verification.

---

## 23. External provider verification

External business-profile or mapping-provider verification belongs to that provider or the applicable Trust requirement.

Failure to obtain an external provider's business-profile verification SHALL ordinarily affect the relevant integration rather than GrandRue's entire merchant presence.

Provider verification does not own Merchant Location truth.

---

## 24. Provider-neutral location resolution

Candidate location evidence MAY originate from structured postal address, digital addressing, existing business/place record, device location, merchant-assisted selection or other accepted evidence.

No provider response becomes authoritative solely because it exists.

The governing Merchant Location operation accepts the resulting navigation point.

---

## 25. Ambiguous address changes fail safely

If a merchant enters a changed address and GrandRue cannot determine whether the merchant means correct current premises or move shop to different premises, GrandRue SHALL ask.

Example:

> Has your shop moved to this new address?

    [Yes, move my shop]
    [No, I'm correcting the current address]

GrandRue SHALL NOT silently choose based only on distance.

---

## 26. Multi-location merchants

Each location remains independently changeable.

A merchant can select one branch and choose Change address without affecting other branches.

A move of one branch does not create a new Merchant Scope.

---

## 27. Privacy

A precise Merchant Location may remain private.

Location Resolution SHALL NOT imply public Exposure.

A merchant may therefore maintain a precise internal location plus a PRIVATE_INTERNAL choice without exposing the address or coordinate publicly.

---

## 28. Retry and concurrency

Address correction and relocation SHALL preserve existing optimistic concurrency and logical-request identity requirements.

Repeated transport SHALL NOT create duplicate replacement locations, repeat retirement, duplicate revisions or multiply the merchant's single move.

If the merchant's current Location changes after the relocation workflow was reviewed but before commit, GrandRue SHALL detect the conflict rather than overwrite newer authoritative state.

---

## 29. Falsification

### 29.1 Same shop, postcode typo

A merchant corrects a postcode for the same premises.

Expected result:

    same Location identity
    new revision

**PASS**

### 29.2 Shop physically relocates

A merchant changes from 24 High Street to 81 Market Road and confirms the shop moved.

The merchant experiences one change-shop-address workflow.

Internally:

    L1 → historical/retired
    L2 → current

**PASS**

### 29.3 Merchant cannot use maps

The merchant types or selects the new address.

GrandRue resolves it without requiring map-pin manipulation.

**PASS**

### 29.4 New location unresolved

The merchant announces a move, but GrandRue cannot yet establish the exact destination.

The old authoritative destination is not silently replaced by an approximate one.

**PASS**

### 29.5 Existing historical appointment

An Appointment took place at old premises.

The shop later moves.

The historical Appointment still references old location meaning.

**PASS**

### 29.6 Future appointment at old premises

A future commitment explicitly references the old location.

The shop move does not silently rewrite it.

Applicable capability resolution is required.

**PASS**

### 29.7 Optional verification

The merchant moves shop and does not request verification.

The new location is precisely resolved and can be published when otherwise eligible.

GrandRue does not claim it is verified.

**PASS**

### 29.8 Multi-location merchant

Only one branch moves.

Other branches remain untouched.

**PASS**

---

## 30. Rejected alternatives

### 30.1 Merchant manually adds new branch and deletes old branch

Rejected for an ordinary shop move because it exposes internal lifecycle administration to the merchant.

### 30.2 Mutate old Merchant Location identity into any new physical place

Rejected because it corrupts historical location meaning.

### 30.3 Automatically infer relocation solely from distance

Rejected because distance cannot reliably distinguish correction from relocation.

### 30.4 Block address changes because historical records exist

Rejected because historical affinity exists specifically so current business facts can evolve without rewriting history.

### 30.5 Mandatory verification after every move

Rejected because location precision and premises verification are separate truths.

---

## 31. Hard invariants

1. A merchant can change the address of a shop.
2. The normal merchant interaction is Change address / Move shop, not internal Location lifecycle administration.
3. Same-place correction preserves Merchant Location identity.
4. Physical relocation creates a new internal Merchant Location identity.
5. The merchant SHALL NOT be required to create the replacement identity manually.
6. The old Location preserves historical meaning.
7. Existing commitments SHALL NOT be silently rewritten to the new premises.
8. GrandRue MAY orchestrate relocation across capabilities but SHALL NOT usurp their semantic ownership.
9. A new public destination must be precisely resolved before cutover.
10. An unresolved new destination SHALL NOT replace a known precise destination.
11. Coordinates remain internal implementation information.
12. Merchants SHALL NOT be required to manipulate coordinates or map pins.
13. Physical presence is optional.
14. Location Verification remains optional unless independently required by a scoped Trust authority.
15. Relocation does not automatically establish a verification claim.
16. Public navigation SHALL use the accepted precise destination.
17. Public Exposure remains independently governed.
18. Provider evidence remains provider evidence until accepted by Merchant Location authority.
19. Multi-location merchants can move one location without affecting others.
20. Merchant Location history SHALL NOT be rewritten merely because the business moves.

---

## 32. Vision Conformance

**Business-to-Software Translation:** PASS — the merchant expresses the real business event: "my shop moved."

**Administrative Compression:** PASS — GrandRue performs location lifecycle, history preservation and dependency coordination internally.

**Ordinary-Staff Training:** PASS — no architectural or geospatial concepts are exposed.

**Business Language:** PASS — the interaction uses shop, address, move and correction.

**Target-Market Proportionality:** PASS — a small-business owner does not have to administer internal branch identities.

**Ownership:** PASS — Merchant Location retains location authority while dependent capabilities retain their own business truth.

**Historical Correctness:** PASS — moving a shop changes current business reality without rewriting previous commitments.

**Vision-Conformance result:** VISION-CONFORMING

---

## 33. Acceptance boundary

This amendment establishes:

> **A GrandRue merchant may change a shop address through one ordinary merchant-facing workflow. A correction of the same physical premises creates a new revision of the existing Merchant Location; an actual shop move creates a new internal Merchant Location and preserves the previous location for historical truth. GrandRue manages that distinction and lifecycle internally. A public destination must be precisely resolved, while premises verification remains optional unless separately required by a bounded Trust rule.**

**Design review:** PASS  
**Falsification:** PASS  
**Ambiguity review:** PASS  
**Recommendation:** ACCEPT  
**Manual approval:** GRANTED — 18 September 2026; reconfirmed 19 September 2026  
**Governance verdict:** **ACCEPTED**  
**Implementation activation:** **NONE**
