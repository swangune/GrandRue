# MS-PROT-053 v1.1 — Purpose-Bound Personal Media Use Amendment

**Document ID:** MS-PROT-053  
**Version:** 1.1  
**Status:** ACCEPTED  
**Amends:** MS-PROT-053 v1.0  
**Depends on:** MS-PROT-027, MS-PROT-043, MS-PROT-053 v1.0, MS-PROT-057, MS-PROT-066 v1.2  
**Purpose:** Define how identifiable personal media is permitted for a bounded purpose and audience without collapsing personal-data authority into a Boolean consent flag, a CustomerAccount, a MediaAsset visibility field or merchant possession of the underlying bytes.

---

## 1. Governing Principle

> **Possession of personal media does not authorise every use of it. Personal-media use must be evaluated for the relevant subject/data scope, purpose, audience, authority/evidence basis and current Exposure context.**

Canonical separation:

```text
MediaAsset existence
    ≠ personal-data use authority
    ≠ consent
    ≠ Exposure
    ≠ retention
    ≠ subject identity/account
    ≠ guardian/legal relationship
```

This amendment is generic. It does not introduce nursery-, childcare-, staff-photo- or other business-category semantics.

---

## 2. PersonalDataUseBasis

Main Street accepts the concept `PersonalDataUseBasis` as the bounded authority/evidence record by which an applicable governed policy can establish that a defined scope of personal data may be used for a defined purpose and audience.

Conceptually:

```text
PersonalDataUseBasis
{
    basisIdentity
    affectedSubject / bounded subject scope
    affectedDataScope
    purpose
    intendedAudience / exposure scope
    authoritySource
    evidenceReference / provenance
    effectiveFrom
    optionalEndOrReviewCondition
    currentSatisfaction
}
```

This is a semantic contract, not a mandated Java class/table.

`PersonalDataUseBasis` records the basis/evidence Main Street relies upon under the applicable governed policy. It MUST NOT be interpreted as Main Street certifying legal compliance in every jurisdiction.

---

## 3. Consent Is Not Universal Authority

Main Street MUST NOT model personal-media permission as a universal:

```text
consent = true
```

or:

```text
parentConsent = true
```

Consent may be one possible authority/evidence source where applicable, but the platform semantic kernel MUST support other accepted bases/policies without rewriting Media or Data Protection architecture.

Therefore:

```text
PersonalDataUseBasis
    ≠ universal consent record
```

Exact jurisdictional lawful-basis mapping remains policy/legal scope outside this amendment.

---

## 4. Public Promotional Exposure Purpose

The proven cross-domain purpose:

```text
PUBLIC_PROMOTIONAL_EXPOSURE
```

is accepted into the MS-PROT-053 purpose vocabulary.

It covers public/promotional use such as:

```text
merchant website gallery
public prospectus imagery
public promotional video
merchant social distribution where separately supported
public team/profile imagery
```

Possession or operational use of an image MUST NOT automatically establish this purpose.

Example:

```text
photo retained for internal operational need
    ≠
photo authorised for public promotional exposure
```

---

## 5. Subject Reference Does Not Require Account Creation

An identifiable person depicted in personal media does not need a Main Street CustomerAccount or other global account merely for data-protection handling.

Main Street MAY use a bounded merchant/contextual subject reference sufficient to associate applicable personal-data use requirements and evidence.

Canonical distinction:

```text
DataSubjectReference
    ≠ Identity
    ≠ CustomerAccount
    ≠ CustomerContext
    ≠ Merchant Membership
```

The implementation SHOULD minimise identifying data and avoid manufacturing broader relationship semantics solely to support privacy controls.

---

## 6. Guardian / Representative Authority Is Separately Governed

Main Street MUST NOT infer legal guardian, parental responsibility, authorised representative or equivalent status from:

```text
same surname
same address
CustomerContext
self-assertion alone
merchant label
```

Where a use requires authority from a guardian/representative or another subject relationship, the applicable policy/authority must define how sufficient evidence is established.

Media consumes the resulting eligibility; Media does not decide legal relationship truth.

---

## 7. Purpose- and Audience-Bound Eligibility

For personal media, eligibility is contextual.

Conceptually:

```text
MediaAsset
    + semantic attachment
    + affected subject/data scope
    + requested purpose
    + requested audience
    + protection classification
    + applicable PersonalDataUseBasis
    + current Exposure policy
        ↓
USE / EXPOSURE ELIGIBILITY
```

A `MediaAsset` MUST NOT own one universal `PUBLIC|PRIVATE` flag as the complete authority for personal-media use.

The same asset may be eligible for one purpose/audience and ineligible for another.

---

## 8. Multiple Identifiable Subjects

Where an unchanged asset contains multiple identifiable subjects and the governing policy requires a use basis for each applicable subject, every required subject-use condition must be satisfied before that use/exposure is eligible.

Rejected:

```text
5 of 6 required subject conditions satisfied
    → publish anyway
```

If one required condition becomes unsatisfied, the unchanged asset becomes ineligible for that purpose/audience unless another separately accepted mechanism produces a compliant representation.

---

## 9. No Automatic Subject Removal or Alteration

Loss of eligibility for one depicted subject MUST NOT automatically authorise Main Street to:

```text
blur the subject
crop the subject out
AI-remove the subject
replace a face/body
change the scene
```

Those are editorial transformations and remain outside normal optimisation under MS-PROT-066.

The default semantic consequence is loss of eligibility for the unchanged asset on the affected purpose/audience.

A future separately authorised editing capability may establish a different asset under its own semantics.

---

## 10. Exposure Withdrawal Is Not Immediate Deletion

When a personal-media use basis becomes unsatisfied:

```text
use for affected purpose/audience
    → no longer eligible
```

This does not by itself mean:

```text
delete canonical source immediately
```

Other independently authorised purposes or retention requirements may remain valid.

Example:

```text
PUBLIC_PROMOTIONAL_EXPOSURE
    ✗

private authorised relationship use
    ✓

legal/regulatory retention
    ✓ where applicable
```

MS-PROT-053 v1.0 retention/erasure rules remain authoritative.

---

## 11. Projection and Cache Convergence

When public/personal-media use becomes ineligible, Main Street MUST stop authorising fresh public use and MUST cause affected public projections/delivery paths to converge toward the new restriction.

Conceptually:

```text
use basis becomes unsatisfied
        ↓
affected Exposure becomes ineffective
        ↓
public references removed / suppressed
        ↓
new delivery authorisation denied
        ↓
cache/CDN invalidation or equivalent convergence initiated
```

Main Street MUST NOT claim that copies already downloaded by external recipients have been technically recalled.

---

## 12. External Distribution Remains External Authority

If personal media has been distributed to an external provider/channel:

```text
Main Street-owned Exposure
    → governed immediately by Main Street

external provider copy
    → provider-side removal/reconciliation operation where supported
```

Main Street MUST preserve provider evidence semantics and MUST NOT claim external deletion until provider evidence supports that outcome.

---

## 13. AI Boundary

AI MAY assist by identifying candidate risk such as:

```text
people appear identifiable
multiple people appear in this image
personal-media handling may apply
```

AI MUST NOT independently establish:

```text
lawful/permission basis exists
guardian authority exists
public promotional use is permitted
safeguarding requirements are satisfied
```

The governing deterministic policy/authority decides use eligibility.

---

## 14. Staff and Other Adult Media Use the Same Model

The model is not child-specific.

Examples include:

```text
public employee/team photo
consultant profile image
event attendee photograph
customer testimonial video
```

Where personal-media handling requirements apply, the same purpose/audience/basis semantics are used.

No `ChildPhotoPermission`, `NurseryConsent` or business-category-specific privacy primitive is accepted.

---

## 15. Non-Personal Media Remains Simple

Main Street MUST NOT require fictitious subject/use-basis records for media where no applicable personal-data use requirement exists.

Example:

```text
empty classroom photograph
building exterior
product photograph
logo
```

may follow ordinary Media/Exposure rules where no personal-data condition applies.

This preserves progressive disclosure and avoids burdening every merchant asset with unnecessary privacy workflow.

---

## 16. Falsification

### 16.1 One identifiable child/person

Public promotional use is eligible only when the applicable use-basis requirement is satisfied.

**PASS**

### 16.2 Group photograph

Every applicable required subject-use condition must be satisfied for the unchanged public asset.

**PASS**

### 16.3 Basis becomes unsatisfied after publication

Affected public use ends; independent retention or private-purpose authority may survive.

**PASS**

### 16.4 No Main Street account

Bounded subject/use evidence can exist without inventing a CustomerAccount or global identity.

**PASS**

### 16.5 AI detects a face

AI may flag candidate personal-media handling but cannot authorise public use.

**PASS**

### 16.6 Externally distributed copy

Main Street revokes its own Exposure and separately reconciles provider-side removal with provider evidence.

**PASS**

### 16.7 Empty-room image

No unnecessary personal-data use record is required.

**PASS**

---

## 17. Rejected Alternatives

The following are rejected:

1. Universal `consent=true` or `parentConsent=true` as the platform permission model.
2. Treating possession of personal media as authority for public promotional use.
3. Requiring every depicted person to have a Main Street account.
4. Treating CustomerContext as proof of guardian/legal-representative authority.
5. Encoding all personal-media authority as `MediaAsset.visibility`.
6. Automatically editing/blur-removing subjects to preserve publication eligibility.
7. Immediate source deletion solely because one Exposure purpose ended.
8. Claiming already-downloaded or external-provider copies have been recalled without evidence.
9. Allowing AI to establish lawful/permission/guardian authority.
10. Nursery-/child-specific privacy primitives where generic personal-data semantics suffice.

---

## 18. Accepted Invariants Added by v1.1

1. Personal-media use is purpose- and audience-bound.
2. `PersonalDataUseBasis` is distinct from universal consent.
3. `PUBLIC_PROMOTIONAL_EXPOSURE` is an accepted data-use purpose.
4. Subject references need not create Main Street accounts or broader business relationships.
5. Guardian/representative authority must not be inferred from CustomerContext or labels.
6. Multiple-subject media must satisfy every applicable required subject-use condition for the unchanged use/exposure.
7. Loss of one use basis removes affected use eligibility but does not automatically delete the canonical source.
8. Personal-media use eligibility must compose with MS-PROT-027 Exposure.
9. Cache/projection delivery must converge after exposure withdrawal, without claiming recall of downloaded copies.
10. External-provider copies remain separate provider-state/evidence concerns.
11. AI may flag risk but may not establish use authority.
12. The model applies generically to children, staff and other identifiable persons.
13. Non-personal media must not be burdened with fictitious personal-data authority records.

---

## 19. Acceptance Statement

Main Street can support media-heavy and sensitive-subject merchants without turning privacy into a Boolean flag or a niche-specific feature.

> **Use personal media only for the bounded purpose and audience supported by current governed authority/evidence; withdraw affected exposure when that basis no longer holds; preserve independent retention and business truth.**
