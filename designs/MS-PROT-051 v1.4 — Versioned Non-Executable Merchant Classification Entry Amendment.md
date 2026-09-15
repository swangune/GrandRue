# MS-PROT-051 v1.4 — Versioned Non-Executable Merchant Classification Entry Amendment

**Document ID:** MS-PROT-051
**Version:** 1.4
**Status:** **ACCEPTED after implementation-gap review, falsification and manual approval**
**Approved:** 30 August 2026
**Governed by:** `DESIGN-RULES.md`
**Amends:** composite MS-PROT-051 through v1.3 only within authoritative Merchant Classification Entry representation, mutation and retained revision/provenance scope
**Closes:** `IMP-05-A3D-DG-001`
**Depends on:** MS-PROT-027 v1.5; MS-PROT-031; MS-PROT-051 v1.1; MS-PROT-053; MS-PROT-057 v1.2; MS-PROT-059; MS-PROT-063; MS-PROT-069; MS-PROT-076
**Purpose:** Establish the minimum versioned, merchant-approved and non-executable representation for independent Merchant Classification Entry revisions without standardising a cross-product taxonomy or allowing classification metadata to determine Configuration, capability or runtime behaviour.

---

## 1. Governing decision

An authoritative Merchant Classification Entry is one independently revisioned
Merchant Profile fact with this minimum value:

```text
MerchantClassificationEntryV1
{
    stable merchant-scoped classificationIdentity
    kind:
        CATEGORY
        DISCOVERY_TAG
        CONTEXTUAL_DESCRIPTOR
    merchantApprovedLabel
    merchantOwnedExposureChoice:
        PRIVATE_INTERNAL
        PUBLIC
}
```

Each entry has its own stable identity, lifecycle, immutable revision history and
current pointer. It is not one field inside a merchant-wide Profile snapshot.

```text
Merchant Classification Entry
        = contextual Merchant Profile metadata
        != standardised taxonomy membership
        != executable Configuration
        != capability or runtime authority
```

---

## 2. Amendment scope and unchanged authority

This amendment governs only:

- the minimum `MerchantClassificationEntryV1` value;
- stable fact identity and allowed mutation;
- label normalization;
- immutable revision/currentness/retry evidence;
- current-Controller mutation authority;
- the source exposure boundary; and
- the hard non-executable boundary.

MS-PROT-051 v1.1 continues unchanged for Merchant Profile ownership, explicit
owner operations, immutable revisions, currentness, optimistic concurrency,
logical retry, actor authority, Profile/Configuration separation and
projection/Exposure separation.

This amendment does not define a taxonomy, registry, hierarchy, synonym model,
translation model, ranking, confidence score, primary category, search
algorithm, projection contract or audience Exposure resolution.

`MS-PROT-051-V11-DQ-012` remains **DEFERRED — INACTIVE**. This amendment does
not standardise classification across production features and does not resolve
that deferred decision.

---

## 3. Stable identity and independent facts

`classificationIdentity` is stable and merchant-scoped. Its identity is
independent of:

```text
kind
label
taxonomy
provider
projection
Configuration
```

Each entry is an independent Profile fact. Unrelated classification entries
and unrelated Profile facts do not share one aggregate revision, current
pointer or mandatory transaction boundary.

Equal labels under different stable identities are permitted. Main Street
shall not automatically merge them merely because their normalized labels are
equal.

---

## 4. Classification kind

The initial closed kind set is:

```text
CATEGORY
DISCOVERY_TAG
CONTEXTUAL_DESCRIPTOR
```

Kind is immutable for a stable classification identity. Changing kind requires
retiring the existing identity and creating a new identity. Kind does not imply
taxonomy membership, ranking, primacy or executable behaviour.

---

## 5. Merchant-approved label and normalization

Every active entry retains one required, non-blank merchant-approved label.
Normalization shall:

1. normalize the label to Unicode NFC; and
2. strip surrounding Unicode whitespace.

Normalization shall preserve case and the merchant's internal wording. It
shall not translate, synonym-expand, classify, rank, rewrite or map the label
to a taxonomy.

The initial label is intentionally free text. No taxonomy code, registry
identifier, hierarchy, synonym, translation, rank, confidence or primary-label
designation is required or implied.

---

## 6. Merchant-owned exposure choice

Each revision retains one explicit source choice:

```text
PRIVATE_INTERNAL
PUBLIC
```

`PUBLIC` is a Merchant Profile source fact only. It does not publish an entry,
create a projection, select a surface or replace the audience-specific Exposure
decision governed by MS-PROT-027 v1.5.

Label and exposure may be changed by an update. Kind may not.

---

## 7. Revision and lifecycle contract

Owner-specific operations are:

```text
CreateMerchantClassificationEntry
UpdateMerchantClassificationEntry
RetireMerchantClassificationEntry
```

Every successful material mutation appends one immutable exact `CREATE`,
`UPDATE` or `RETIRE` revision and advances one current pointer for the exact
merchant/classification identity.

Update and retire require the exact expected current revision identity.
Retirement is terminal for the initial classification identity. A later new
classification fact uses a new identity.

Historical revisions remain exact and are not rewritten after later label,
exposure, Controller or lifecycle change.

---

## 8. Retry, concurrency and actor authority

Every externally retryable mutation retains one logical request identity.
Exact replay returns the originally committed Classification Entry revision,
including after later update or retirement. Reuse of that request identity with
materially changed intent conflicts.

Changed intent includes a different operation, Merchant Scope,
classification identity, expected revision, kind, label, exposure, provenance
or actor intent.

Same-entry concurrent mutation commits at most one successor for one expected
current revision.

Ordinary direct mutation requires:

```text
authenticated trusted actor
+ resolved Merchant Scope
+ authenticated current Merchant Controller
+ eligible open and unsuspended Merchant Account
```

Caller assertions, caller booleans, onboarding state or a stale Controller
relationship are not mutation authority.

---

## 9. Required persistent evidence

Every authoritative Classification Entry revision must establish:

```text
Merchant Scope
stable classification identity
exact revision identity and scope-local revision number
predecessor revision identity where applicable
CREATE / UPDATE / RETIRE operation
current lifecycle
MerchantClassificationEntryV1 schema identity
exact kind
exact normalized merchant-approved label
merchant-owned exposure choice
logical request identity
source / provenance
actor / origin
exact current Controller relationship provenance
committed time
```

One durable current pointer identifies the effective revision for each exact
merchant/classification identity. The exact relational layout is an
implementation choice provided these invariants are enforced.

---

## 10. Onboarding and AI boundary

Onboarding and AI assistance may propose candidate classification values. A
proposal is not authoritative merely because it was inferred, stored or shown.

Authoritative mutation requires an exact reviewed, merchant-approved owner
operation through the same Classification Entry authority. AI confidence,
pre-revision onboarding review and caller booleans cannot substitute for that
operation.

---

## 11. Non-executable boundary

A Classification Entry revision shall never trigger, configure or prove:

- a Configuration Revision or compiler branch;
- capability inclusion, exclusion or composition;
- runtime applicability, eligibility or admission;
- dashboard or storefront functionality;
- verification or trust;
- Payment, Scheduling or other operational semantics;
- entitlement; or
- provider readiness.

Legacy labels such as `PRODUCT`, `SERVICE` or `HYBRID` may be retained as
contextual merchant-approved labels. They have no executable meaning and shall
not select business behaviour.

Committing an entry also does not publish it. Projection and final audience
Exposure remain separate authorities.

---

## 12. Falsification record

### Two merchants use the same category wording differently

Each merchant retains independently scoped facts and no shared taxonomy meaning
is inferred. **PASS**

### One merchant has two intentionally similar labels

Stable identities remain distinct and equal normalized labels are not
automatically merged. **PASS**

### A merchant corrects wording but keeps the category meaning

An update changes the label while preserving stable identity and immutable
kind. Exact history remains available. **PASS**

### A proposed tag should become a category

Kind cannot be rewritten. The old identity is retired and a new identity is
created, preventing historical reinterpretation. **PASS**

### AI infers that a merchant is a service business

The inference can be proposed but cannot create an authoritative entry without
an exact reviewed merchant-approved operation, and the resulting label cannot
activate Scheduling or any other capability. **PASS**

### A PUBLIC classification exists

The source choice alone neither publishes a projection nor authorizes an
audience observation. **PASS**

### Exact create is retried after later retirement

The logical request returns the original immutable revision before current
admission is reevaluated. **PASS**

---

## 13. Rejected alternatives

- one merchant-wide classification snapshot;
- standardising a taxonomy before production features require it;
- opaque JSON without a versioned typed value;
- classification-driven Configuration, compiler, capability or UI behaviour;
- mutable kind under one stable identity;
- automatic merging by equal label; and
- AI or onboarding review as direct mutation authority.

---

## 14. Hard invariants

1. Every active Classification Entry retains one exact
   `MerchantClassificationEntryV1` value.
2. Classification identity is stable, merchant-scoped and independent of kind,
   label, taxonomy, provider, projection and Configuration.
3. Kind is immutable for one stable identity.
4. Label and source exposure may change only through an appended exact update.
5. Labels are required, merchant-approved, Unicode NFC normalized and stripped
   only at their surrounding boundary.
6. Equal labels do not imply equal fact identity.
7. Material mutations append immutable revisions and use exact expected-current
   concurrency plus logical retry identity.
8. Retirement is terminal for the initial identity.
9. Ordinary mutation requires the authenticated current Controller and an
   eligible open, unsuspended Merchant Account.
10. AI, onboarding state and caller booleans are not mutation authority.
11. `PUBLIC` source choice is not publication or final audience Exposure.
12. Classification does not configure or prove executable merchant behaviour.
13. `MS-PROT-051-V11-DQ-012` remains deferred and inactive.

---

## 15. Deferred-decision and implementation consequence

`IMP-05-A3D-DG-001` is resolved by this amendment.

IMP-05 A3d may implement the minimum relational Classification Entry authority
with the exact evidence above. It must not introduce a shared taxonomy,
classification-driven Configuration, projection, transport or executable
capability decision.

`MS-PROT-051-V11-DQ-012` remains **DEFERRED — INACTIVE** until classification
must become standardised across production features. No other deferred question
is silently resolved by this amendment.

---

## 16. Acceptance statement

> **A Merchant Classification Entry is one stable merchant-scoped,
> independently revisioned and merchant-approved contextual Profile fact. Its
> kind is immutable, its label and source exposure may change through exact
> owner mutations, and neither the entry nor any legacy business-type wording
> can determine Configuration, capability, runtime, publication or provider
> behaviour.**

**Review:** PASS
**Falsification:** PASS
**Ambiguity review:** PASS
**Manual approval:** GRANTED — 30 August 2026
**Governance verdict:** **ACCEPTED**
