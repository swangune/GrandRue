# MS-PROT-051 v1.5 — Merchant Location Public Exposure Choice Revision & Persistence Amendment

**Document ID:** MS-PROT-051  
**Version:** 1.5  
**Status:** **ACCEPTED by explicit manual approval on 2 September 2026**  
**Approved:** 2 September 2026  
**Authority type:** Production Merchant Profile semantic/persistence amendment  
**Governed by:** MS-DESIGN-RULES-001; MS-IMPLEMENTATION-RULES-001; MS-IMP-001  
**Amends:** composite MS-PROT-051 through v1.4 only within Merchant Location public-observation choice ownership, revision, persistence, mutation concurrency, lifecycle interaction and onboarding adoption  
**Depends on:** composite MS-PROT-051 through v1.4; composite MS-PROT-027 through v1.10; MS-PROT-033; MS-PROT-052; MS-PROT-053; MS-PROT-062  
**Closes if conformingly implemented:** `MS-PROT-027-V15-DQ-003`  
**Purpose:** Establish the exact Profile-owned representation and PostgreSQL persistence of Merchant Location public Exposure choice without changing Merchant Location revision identity, manufacturing historical merchant intent, creating a generic Exposure-policy aggregate or allowing Exposure to own merchant policy.

---

## 1. Governing decision

Merchant Location public-observation choice SHALL be an independently revisioned subordinate Profile fact scoped to one stable Merchant Location identity.

Canonical:

```text
MerchantLocation L1
    ├── immutable Location revisions
    │      address / label / coordinates / lifecycle evidence
    │
    └── immutable Location Exposure Choice revisions
           PRIVATE_INTERNAL | PUBLIC
```

Both authorities remain owned by Merchant Profile.

Exposure consumes the current choice.

Exposure does not persist or mutate it.

A Location Exposure-choice mutation SHALL NOT, by itself, create a new `MerchantLocationRevision`.

---

## 2. Why Location differs from Contact Point

Existing Contact Point public choice remains part of its Contact Point revision.

That model SHALL remain unchanged.

Merchant Location is different because exact Merchant Location revisions may be retained by other capabilities as historical/current provenance.

Therefore:

```text
Contact Point
    value + public choice
    may share one fact revision

Merchant Location
    Location fact revision
    ≠
    Location public-choice revision
```

No generic rule requiring all Profile facts to use one policy-storage pattern is introduced.

---

## 3. Merchant Location Exposure Choice vocabulary

The owner vocabulary SHALL be exactly:

```text
MerchantLocationExposure
{
    PRIVATE_INTERNAL
    PUBLIC
}
```

Meaning:

```text
PRIVATE_INTERNAL
    merchant does not permit ordinary public observation
    of the current eligible Location representation

PUBLIC
    merchant permits ordinary public observation
    subject to all other Exposure restrictions
```

Neither value is a final Exposure verdict.

Therefore:

```text
MerchantLocationExposure.PUBLIC
    ≠
ExposureDecision.EXPOSE
```

Exposure remains responsible for the final audience decision.

---

## 4. Absence is a legitimate state

Main Street SHALL distinguish:

```text
no recorded Merchant Location Exposure choice
```

from:

```text
MerchantLocationExposure.PRIVATE_INTERNAL
```

Absence means:

> No explicit current merchant choice has been established.

It SHALL evaluate downstream as:

```text
choice absent
    ↓
merchant-choice evaluation = UNRESOLVED
    ↓
Exposure = WITHHOLD
```

Main Street SHALL NOT manufacture `PRIVATE_INTERNAL` merely to obtain the same fail-closed result.

---

## 5. Stable policy scope

One Merchant Location Exposure Choice stream is identified by:

```text
MerchantScope
+
MerchantLocationIdentity
```

It is not identified by:

```text
postal address
coordinates
MerchantLocationRevisionIdentity
provider place identifier
projection revision
frontend component
```

The choice governs the continuing Merchant Location identity.

---

## 6. Same-place corrections retain merchant choice

MS-PROT-051 already distinguishes same-place correction from relocation.

Therefore:

```text
Location L1 / revision 8
choice = PUBLIC

same-place correction
        ↓

Location L1 / revision 9
choice remains PUBLIC
```

A correction may include:

```text
postcode correction
address formatting correction
building-name correction
coordinate correction
public label correction
```

provided the operation remains a valid same-place correction under Merchant Location authority.

The choice SHALL NOT be duplicated into the Location revision merely to preserve this relationship.

---

## 7. Relocation does not inherit choice

A materially different merchant place requires a new Merchant Location identity.

Therefore:

```text
L1
choice = PUBLIC

merchant relocates
        ↓

L1 retired
L2 created
L2 choice = absent
```

unless the merchant separately establishes a choice for L2.

Main Street SHALL NOT copy L1's public-choice state to L2 automatically.

---

## 8. Choice revision model

Conceptually:

```text
MerchantLocationExposureChoiceRevision
{
    revisionIdentity

    MerchantScope
    MerchantLocationIdentity

    revisionNumber
    predecessorRevisionIdentity?

    exposure:
        PRIVATE_INTERNAL | PUBLIC

    logicalRequestIdentity
    provenanceReference
    actingPrincipalIdentity
    controllerRelationshipIdentity
    committedAt
}
```

Every explicit choice change SHALL produce a new immutable choice revision.

Previous revisions remain historical evidence subject to ordinary Data Lifecycle authority.

---

## 9. No explicit choice-clearing operation

Once an explicit merchant choice exists, ordinary merchant mutation changes it between:

```text
PRIVATE_INTERNAL
PUBLIC
```

This amendment introduces no semantic operation:

```text
CLEAR_TO_UNKNOWN
```

Absence represents only a choice not yet established under the authoritative stream.

A later need to deliberately erase established policy meaning would require separate lifecycle/data-governance authority.

---

## 10. Exact PostgreSQL representation

Production persistence SHALL use two Profile-owned relations.

### 10.1 Revision relation

```text
merchant_location_exposure_choice_revision
```

shall contain at least:

```text
revision_identifier              text primary key
merchant_identifier              text not null
location_identifier              text not null
revision_number                  bigint not null
predecessor_revision_identifier  text null
exposure_choice                  text not null
logical_request_identifier       text not null unique
provenance_reference             text not null
acting_principal_identifier      text not null
controller_relationship_identifier text not null
committed_at                     timestamptz not null
```

with:

```text
unique (
    merchant_identifier,
    location_identifier,
    revision_number
)
```

and:

```text
exposure_choice in (
    'PRIVATE_INTERNAL',
    'PUBLIC'
)
```

Revision/predecessor coherence SHALL preserve:

```text
revision 1
    → no predecessor

revision N > 1
    → exact predecessor required
```

### 10.2 Current-pointer relation

```text
current_merchant_location_exposure_choice
```

shall contain at least:

```text
current_pointer_identifier   text primary key
merchant_identifier          text not null
location_identifier          text not null
revision_identifier          text not null unique
revision_number              bigint not null
```

with:

```text
unique (
    merchant_identifier,
    location_identifier
)
```

`revision_identifier` SHALL reference the exact authoritative choice revision.

No row in the current-pointer relation means:

```text
current choice absent
```

It does not mean `PRIVATE_INTERNAL`.

---

## 11. No legacy backfill

Existing Merchant Locations for which no explicit historical Location Exposure choice exists SHALL receive no fabricated choice revision.

Migration SHALL NOT infer `PUBLIC` or `PRIVATE_INTERNAL` from:

```text
public label
postal address
coordinates
Google Business evidence
verification status
Service Area
Business Hours
existing projection behaviour
merchant category
```

Legacy absence remains absence.

---

## 12. Choice mutation operation

Profile SHALL own a bounded operation conceptually equivalent to:

```text
SetMerchantLocationExposureChoice
{
    MerchantScope
    MerchantLocationIdentity

    expectedCurrentChoice:
        ABSENT
        |
        exact ChoiceRevisionIdentity

    replacementExposure:
        PRIVATE_INTERNAL | PUBLIC

    logicalRequestIdentity
    provenanceReference
    actingPrincipalIdentity
    committedAt
}
```

`ABSENT` is an explicit optimistic-concurrency expectation.

It does not mean the caller omitted concurrency information.

---

## 13. Optimistic concurrency

Choice mutations SHALL use compare-and-set semantics.

Example:

```text
current choice = C4 PUBLIC

device A expects C4
device B expects C4

A → PRIVATE_INTERNAL
    commits C5

B → PUBLIC
    using stale C4
    ↓
PROFILE_REVISION_CONFLICT
```

First-choice establishment is also concurrency protected:

```text
current choice = absent

A expects ABSENT
B expects ABSENT

only one may establish revision 1
```

Blind last-write-wins is prohibited.

---

## 14. Idempotency

Externally retryable Location Exposure-choice mutation SHALL retain stable logical request identity.

If the same request commits and is retried with identical intent:

```text
return / reconcile the same committed logical effect
```

It SHALL NOT create another choice revision.

The same logical request identity with materially different intent SHALL be rejected.

---

## 15. Location lifecycle validation

A Location Exposure choice may be established or changed only for a current:

```text
Merchant Location exists
+
lifecycle = ACTIVE
```

A retired or nonexistent Location cannot receive a new choice mutation.

Choice history MAY remain after Location retirement.

Historical:

```text
choice = PUBLIC
```

does not make a retired Location a current Exposure candidate.

---

## 16. Retirement race protection

Location retirement and Location Exposure-choice mutation SHALL participate in one compatible Merchant/Location serialization discipline.

The implementation SHALL prevent:

```text
choice operation validates ACTIVE
        ↓
concurrent retirement commits
        ↓
choice commits afterward as though ACTIVE
```

A conforming outcome is:

```text
choice commits first
    → retirement may then commit
    → retired Location ceases to be a candidate

or

retirement commits first
    → later choice mutation rejects
```

No new global Profile lock is introduced.

---

## 17. Same-place correction and choice mutation

Location correction and Location Exposure choice are distinct same-owner facts.

They SHALL NOT conflict merely because both change concurrently.

The shared stable Location identity permits:

```text
Location correction
    → Location revision CAS

Exposure choice update
    → choice revision CAS
```

Lifecycle crossing remains protected as required by Section 16.

No global `BusinessProfile.version` is introduced.

---

## 18. Current owner evidence supplied to Exposure

For candidate:

```text
profile / public-merchant-location
+
MerchantLocationIdentity L1
```

the Profile-owned merchant-choice evaluator SHALL establish current evidence by verifying at least:

```text
same MerchantScope
+
Location L1 currently exists
+
Location L1 is ACTIVE
+
current Location Exposure choice exists
```

and return:

```text
PUBLIC
    → merchant-choice result EXPOSE

PRIVATE_INTERNAL
    → merchant-choice result WITHHOLD

choice absent
    → merchant-choice result UNRESOLVED

Location unavailable / no longer current
    → UNRESOLVED
```

The evaluator does not create the final Exposure verdict.

---

## 19. Candidate instance is not authority

Possession of:

```text
MerchantLocationIdentity L1
```

does not prove:

```text
L1 exists
L1 belongs to this merchant
L1 is active
merchant chose PUBLIC
```

The Profile evaluator SHALL establish those facts from Profile authority.

A candidate instance reference is a locator/evaluation identity only.

---

## 20. Contact Point authority remains unchanged

Existing `MerchantContactPointExposure` continues to use:

```text
PRIVATE_INTERNAL
PUBLIC
```

inside the immutable Contact Point revision.

No Contact Point migration to the new Location choice tables occurs.

Current Contact Point value and Contact Point Exposure choice remain one coherent Contact Point revision.

---

## 21. DQ-003 completion boundary

`MS-PROT-027-V15-DQ-003` is complete when production evidence establishes:

```text
Contact Point
    current public choice
    → exact revision-affined PostgreSQL representation

Merchant Location
    current public choice
    → exact independent Profile-owned choice revision/pointer representation
```

This amendment does not require all future Profile facts to use either pattern.

---

## 22. Onboarding adoption

Onboarding may establish a Location Exposure choice only when the exact reviewed onboarding evidence explicitly contains the merchant's choice.

It SHALL NOT infer public choice from:

```text
merchant supplied an address
merchant supplied a map pin
business has a physical site
Google Business verification exists
business category usually displays addresses
```

If the submitted reviewed onboarding case contains no explicit Location public-choice intent:

```text
authoritative Location may be created
+
Location Exposure choice remains absent
```

If choice adoption is retried, ordinary Profile idempotency and concurrency rules apply.

This amendment does not define the external API/transport representation used to collect that choice.

---

## 23. DQ-004 remains open

This amendment SHALL NOT resolve:

```text
MS-PROT-027-V15-DQ-004
```

Exact transport/application representation exposed externally for changing Profile Exposure choices remains downstream API scope.

A Profile-owned internal/application mutation contract may exist without deciding its HTTP/API representation.

---

## 24. No generic Exposure policy aggregate

The following remains prohibited:

```text
MerchantExposurePolicy
ProfileVisibility
MerchantVisibilitySettings
Map<Field, Boolean>
```

as a universal authoritative store.

The new persistence belongs only to Merchant Location public choice.

Other owner policies remain where their applicable owning authorities place them.

---

## 25. Required implementation falsification tests

Implementation SHALL prove at least:

1. new Location may exist with no choice;
2. absent choice is not persisted as `PRIVATE_INTERNAL`;
3. first explicit choice creates choice revision 1;
4. PUBLIC → PRIVATE_INTERNAL creates a new choice revision;
5. PRIVATE_INTERNAL → PUBLIC creates a new choice revision;
6. choice-only mutation does not create a new Location revision;
7. same-place Location correction preserves current choice;
8. relocation to new Location identity does not inherit old choice;
9. two concurrent first-choice mutations cannot both succeed;
10. stale expected choice revision conflicts;
11. same logical request replay is idempotent;
12. same request with different intent is rejected;
13. retired Location rejects new choice mutation;
14. retirement/choice race cannot commit choice after retirement;
15. historical PUBLIC choice does not resurrect retired Location;
16. unknown/absent legacy choice evaluates unresolved;
17. Contact Point persistence behaviour remains unchanged;
18. unrelated Profile fact mutation does not conflict with Location choice merely because it is concurrent.

---

## 26. Non-goals

This amendment introduces no:

```text
global Profile transaction
generic policy database
Exposure resolver
Exposure cache
rule DSL
public API DTO
HTTP route
frontend setting schema
provider dependency
Projection Serviceability rule
business-type switch
```

---

## 27. Acceptance consequence

If approved and conformingly implemented:

```text
Merchant Location public choice
    → design-resolved

MS-PROT-027-V15-DQ-003
    → closed by conforming implementation evidence

E4 Profile merchant-choice evaluator
    → has an authoritative persistence boundary
```
