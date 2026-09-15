# MS-PROT-066 v1.2 — Media Delivery Fidelity & Long-Form Information Amendment

**Document ID:** MS-PROT-066  
**Version:** 1.2  
**Status:** ACCEPTED  
**Amends:** MS-PROT-066 v1.1  
**Depends on:** MS-PROT-027, MS-PROT-053, MS-PROT-057, MS-PROT-066 v1.1  
**Purpose:** Refine media-delivery fidelity for image-heavy merchants and make long-form informational video explicit without changing MediaAsset identity, merchant content authority, Exposure authority or the distinction between optimisation and editorial modification.

---

## 1. Governing Principle

> **Preserve the canonical merchant source; derive only registered purpose-specific delivery renditions; allow delivery fidelity to follow the media role/profile rather than forcing one encoding rule onto every image or video.**

Canonical separation:

```text
Canonical Media Source
    ≠ Media Rendition
    ≠ Business Attachment
    ≠ Exposure
    ≠ Editorial Modification
```

This amendment is intentionally narrow. It does not introduce nursery-, daycare-, Montessori- or other business-category semantics.

---

## 2. Canonical Source Preservation

MS-PROT-066 v1.1 source-preservation semantics remain authoritative.

A canonical merchant source MUST NOT be destructively replaced merely because Main Street generates smaller or differently encoded delivery renditions.

Canonical source fidelity and delivery encoding fidelity are distinct concerns.

```text
source fidelity
    ≠
delivery encoding fidelity
```

---

## 3. Image Delivery Fidelity

The v1.1 universal rule requiring all generated image renditions to use lossless encoding is narrowed by this amendment.

Generated image delivery renditions SHALL follow the fidelity requirements of a registered role/profile.

Controlled lossy encoding MAY be used for photographic delivery renditions where the registered profile permits it and all of the following remain true:

1. the canonical source remains preserved under its governing retention lifecycle;
2. the rendition is derived deterministically under a registered profile/version;
3. the transformation remains technical delivery optimisation rather than editorial/content modification;
4. provenance from rendition to source and profile remains available;
5. the rendition remains appropriate for the authorised surface/purpose.

Lossless processing SHALL remain required where the registered role/profile requires fidelity, transparency, line/graphic integrity or brand integrity.

---

## 4. Role/Profile-Specific Behaviour

Examples of valid policy classes include:

```text
MERCHANT_LOGO
    → lossless / brand-preserving

PUBLIC_THUMBNAIL from photograph
    → controlled photographic delivery encoding permitted

PUBLIC_GALLERY from photograph
    → controlled photographic delivery encoding permitted

PUBLIC_HERO from photograph
    → controlled photographic delivery encoding permitted

transparency-sensitive graphic
    → lossless where profile requires
```

These examples do not mandate concrete codecs, quality factors, image dimensions or output formats.

Exact format selection, quality parameters, responsive dimensions, CDN strategy and cache policy remain downstream implementation/operations choices.

---

## 5. No Arbitrary Degradation

This amendment does not permit:

```text
destructive replacement of source
silent editorial crop
subject removal
face/body alteration
background replacement
AI-generated replacement content
merchant-content rewriting
arbitrary client-selected quality semantics
```

Optimisation MUST remain deterministic under a registered profile.

AI may infer/select a registered profile where permitted by MS-PROT-057/MS-PROT-066, but AI MUST NOT invent arbitrary quality/fidelity rules or perform authoritative media editing.

---

## 6. Galleries Remain Projection/Presentation

A collection of MediaAssets displayed as a gallery does not become a new primitive, capability or universal operational object merely because a merchant has many images.

Canonical model:

```text
MediaAssets
    +
semantic attachments / classifications
    +
Exposure
        ↓
Gallery / grid / carousel / lightbox projection
```

Presentation form does not transfer semantic ownership to Media.

---

## 7. Long-Form INFORMATION_VIDEO

The 30-second restriction accepted by MS-PROT-066 v1.1 remains limited to its short-form roles/profiles.

`INFORMATION_VIDEO` MAY use registered long-form delivery profiles.

An `INFORMATION_VIDEO` MUST NOT inherit the 30-second limit merely because the underlying media type is video.

Examples of legitimate informational video use include:

```text
virtual premises tour
long-form service explanation
educational/instructional information
merchant informational presentation
```

The exact duration ceiling, upload-size limits, codec, bitrate, adaptive protocol and transcoder are not established by this amendment.

Platform/operational limits may constrain supported processing/delivery, but such limits MUST NOT be misrepresented as the semantic meaning of `INFORMATION_VIDEO`.

---

## 8. Long-Form Video Does Not Authorise Editorial Modification

Long-form informational delivery remains subject to the same source-fidelity and editing boundary as other video.

Main Street MAY perform registered technical transformations such as:

```text
transcoding
resolution reduction
bitrate adaptation
streaming segmentation
poster generation
container conversion
```

It MUST NOT silently trim scenes, remove meaningful audio, rewrite speech, change depicted subjects or otherwise alter merchant meaning.

---

## 9. Data Protection and Exposure Remain Independent

Efficient rendition generation does not make an asset publicly usable.

```text
rendition exists
    ≠ Exposure permitted

rendition optimised
    ≠ personal-data use authorised
```

MS-PROT-027 remains authoritative for Exposure/projection. MS-PROT-053 and its accepted amendments remain authoritative for data handling, purpose and retention.

---

## 10. Falsification

### 10.1 Photo-heavy nursery website

Eighty photographic assets can use responsive controlled photographic renditions while canonical originals remain preserved.

**PASS**

### 10.2 Transparent merchant logo

Registered logo profile preserves transparency and brand integrity through lossless delivery.

**PASS**

### 10.3 Four-minute virtual tour

`INFORMATION_VIDEO` may use a long-form registered delivery profile and does not inherit the short promotional 30-second limit.

**PASS**

### 10.4 AI optimisation

AI may select a registered profile but cannot invent a crop, quality rule or editorial transformation.

**PASS**

### 10.5 Publicly ineligible personal image

Generation of a technically valid rendition does not bypass Exposure/data-protection rules.

**PASS**

---

## 11. Rejected Alternatives

The following are rejected:

1. Universal lossless encoding for every photographic delivery rendition.
2. Destructive replacement of canonical sources by delivery renditions.
3. A universal 30-second limit for all video roles.
4. Treating a virtual tour as `PROFILE_VIDEO_SHORT` solely to reuse implementation machinery.
5. Merchant- or AI-authored arbitrary encoding semantics.
6. Gallery as a new semantic primitive/capability merely because many assets are displayed together.

---

## 12. Accepted Invariants Added by v1.2

1. Canonical source fidelity and delivery encoding fidelity are distinct.
2. Image delivery fidelity is role/profile-specific.
3. Controlled lossy encoding is permitted only for derived photographic renditions under registered profiles.
4. Lossless delivery remains required where registered fidelity/brand/transparency requirements demand it.
5. Rendition optimisation remains deterministic and traceable to source/profile/version.
6. Optimisation cannot become editorial modification.
7. `INFORMATION_VIDEO` may use long-form registered delivery profiles.
8. The short-form 30-second restriction must not be inherited by `INFORMATION_VIDEO` automatically.
9. Exact codecs, quality factors, duration ceilings, dimensions and provider technologies remain downstream.
10. Media rendition existence never creates Exposure or personal-data-use authority.

---

## 13. Acceptance Statement

Main Street supports media-heavy merchants without weakening source fidelity or semantic control.

> **Preserve the source; optimise delivery according to a registered role/profile; keep editorial meaning, Exposure and data-use authority separate.**
