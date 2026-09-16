# MS-PROT-066 — Media Asset, Optimisation, Rendition & Access Lifecycle Model

**Document ID:** MS-PROT-066  
**Version:** 1.1  
**Status:** Accepted  
**Depends on:** MS-PROT-027, MS-PROT-035, MS-PROT-049, MS-PROT-053, MS-PROT-057, MS-PROT-059, MS-PROT-063, MS-PROT-065  
**Purpose:** Define media identity, attachment, validation, optimisation, rendition, access and lifecycle semantics while keeping capabilities authoritative for business meaning.

---

## 1. Governing Principle

> **Capabilities own why media exists and what it means; Media owns asset identity and technical lifecycle; specialist AI infers registered optimisation intent; deterministic processors execute it.**

Main Street SHALL optimise website media for fast delivery while preserving canonical merchant source assets and merchant brand/content authority.

---

## 2. Canonical Separation

```text
Capability / Merchant Profile / Publication / Interaction
    owns semantic relationship and purpose

MediaAsset
    owns Main Street media identity and source lifecycle

MediaRendition
    owns derived technical representation

RenditionProfile
    defines registered processing requirements

Media Specialist AI
    selects/infers registered profile where needed

Deterministic Processor
    performs transformation

Storage Adapter
    stores/retrieves bytes

Exposure / Projection
    determines observation/use
```

MediaAsset identity MUST NOT be a provider URL or object-storage key. Upload completion MUST NOT imply business attachment.

---

## 3. Canonical Source and Renditions

Main Street SHALL preserve the canonical source asset without destructive replacement while it remains within its governing retention lifecycle.

A derived rendition is not a new business asset. Renditions SHOULD retain provenance to source asset, registered processing profile/version, media type, dimensions where applicable, byte size, storage reference and processing outcome.

Derived renditions are normally regenerable technical artefacts.

---

## 4. Image Optimisation

Generated image renditions SHALL use lossless encoding. Website performance SHALL primarily use right-sized responsive renditions, caching/CDN delivery where implemented, lazy loading for non-critical media, priority handling for critical media and bounded surface-aware composition.

Lossless delivery does not require sending the full-resolution source to every client. Derived dimensions MAY be reduced while the canonical source remains preserved.

---

## 5. Media Roles and Registered Profiles

Initial roles include, where applicable:

```text
LOGO
PRODUCT_IMAGE
HERO_IMAGE
GALLERY_IMAGE
THUMBNAIL
PUBLICATION_IMAGE
DOCUMENT
PRODUCT_VIDEO
PROFILE_VIDEO
INFORMATION_VIDEO
```

Processing SHALL use registered purpose-oriented profiles rather than arbitrary per-request transformations. Examples include `MERCHANT_LOGO`, `PRODUCT_CARD`, `PRODUCT_DETAIL`, `PUBLIC_HERO`, `PUBLIC_GALLERY`, `PUBLIC_THUMBNAIL`, `DOCUMENT_PREVIEW`, `PRODUCT_VIDEO_SHORT` and `PROFILE_VIDEO_SHORT`.

Explicit capability/media-role context outranks AI inference.

---

## 6. Logo Integrity

Logo is a first-class media role. Merchant Profile owns the fact that an asset is the merchant's logo; Media owns the asset/renditions.

Ordinary optimisation MAY validate/sanitise, resize, losslessly encode, preserve transparency and generate deterministic fallbacks. It MUST NOT silently recolour, change typography/shapes/proportions/wording, add/remove backgrounds, crop meaningful brand content or redesign the logo.

Merchants MAY provide `PRIMARY`, `LIGHT_SURFACE`, `DARK_SURFACE` and `COMPACT_MARK` variants where supported. Main Street MUST NOT fabricate these by recolouring the primary logo.

Valid vector sources MAY remain canonical and be delivered where safe; SVG MUST be sanitised/validated. Raster fallback generation MUST NOT replace the canonical vector merely for implementation convenience.

---

## 7. Photo/Image Optimisation Specialist

Main Street SHALL support a dedicated Photo/Image Optimisation Specialist under MS-PROT-057. It MAY infer/select registered processing profiles using source and surface context but MUST NOT invent media roles, rendition profiles, arbitrary quality settings, destructive crops, brand redesign or business relationships.

Known-profile processing MUST remain available without AI. The specialist MAY diagnose unsuitable logos and present supported merchant choices but MUST NOT autonomously redesign them.

---

## 8. Video Optimisation Specialist

Main Street SHALL support a dedicated Video Optimisation Specialist under MS-PROT-057. It MAY infer/select registered video profiles from duration, resolution, frame rate, codec/container, orientation, audio presence, target surface, declared role and poster requirements.

It MAY propose registered technical operations such as rendition selection, poster generation, resizing/transcoding and streaming preparation. It MUST NOT autonomously trim merchant content, remove meaningful audio, rewrite scenes or otherwise change semantic content.

Known-profile processing MUST remain available without AI.

---

## 9. Video Source Fidelity and Delivery Optimisation

The lossless-image rendition rule does NOT apply to video delivery renditions.

Main Street SHALL preserve the canonical merchant video source without destructive replacement. Derived video delivery renditions MAY use controlled lossy encoding for practical website performance only when:

1. lossy processing applies solely to derived delivery renditions;
2. canonical source remains preserved;
3. rendition remains traceable to source and processing profile/version;
4. transformation is deterministic under a registered profile;
5. transformation does not become editorial/content modification.

> **Preserve video source fidelity; optimise video delivery bandwidth.**

The architecture SHOULD support multiple video delivery renditions and MAY support adaptive delivery. Exact codec, bitrate, container, adaptive protocol, CDN and transcoder remain implementation choices.

---

## 10. Role-Specific Video Duration

Main Street SHALL use role-specific video duration constraints rather than one universal video limit.

The initial standard short-form storefront profiles SHALL permit up to **30 seconds** for:

```text
PRODUCT_VIDEO
PROFILE_VIDEO / short promotional storefront video
```

Other registered roles MAY define different duration constraints when separately justified and accepted.

The 30-second limit is a profile/platform constraint, not a universal semantic property of `VIDEO` or `MediaAsset`.

An over-duration source MUST NOT be automatically truncated. Main Street MAY reject it for that role with an actionable explanation, invite a compliant upload, allow another supported role/profile, or route an explicit editing request through separately authorised editing semantics if such capability later exists.

The Video Optimisation Specialist MUST NOT silently cut video to satisfy duration constraints.

---

## 11. Optimisation Is Not Editing

Automatic technical optimisation MAY include registered transcoding, resolution reduction, bitrate adaptation, streaming segmentation, poster generation, container conversion and safe metadata treatment.

Editorial/content changes include trimming scenes, removing meaningful audio, changing merchant speech, rewriting visual content, reframing/cropping away meaningful subjects, appearance-changing AI enhancement, background replacement/removal and invented narrative content.

Editorial changes MUST NOT occur merely as optimisation; they require separate explicit merchant intent and supported semantics.

---

## 12. AI Is Not the Processor

```text
media context
    ↓
specialist inference where useful
    ↓
registered processing plan
    ↓
deterministic validation
    ↓
deterministic processor
    ↓
MediaRenditions
```

Photo/Video agents infer; processors execute. AI unavailability MUST NOT block known-profile deterministic processing.

---

## 13. Validation, Access and Exposure

Stored bytes do not automatically become usable media. Client-declared MIME/content metadata MUST NOT be blindly trusted. Media MUST distinguish validation rejection, quarantine, processing failure, logical unavailability and physical cleanup where those distinctions affect recovery or exposure.

`assetId ≠ permission`. Access MUST follow applicable Trusted Execution Context, merchant scope, business relationship and Exposure authority. Object-storage public accessibility MUST NOT substitute for Main Street exposure semantics.

Public delivery URLs are infrastructure references, not MediaAsset identity. Storage/CDN migration MUST be possible without rewriting business semantics.

---

## 14. Direct Upload and Background Processing

Direct client-to-storage upload MAY be used only through bounded Main Street upload authority followed by Main Street validation/confirmation and capability-owned attachment.

Expensive processing MAY execute asynchronously under MS-PROT-065. Rendition failure MUST NOT automatically invalidate or delete an otherwise valid canonical source. Retry remains idempotent and attributable.

---

## 15. Retention, Detachment and Deletion

One MediaAsset MAY support multiple authorised references. Removing one business relationship MUST NOT physically delete the source while legitimate references or retention obligations remain.

Media retention/erasure composes with MS-PROT-053. Logical unavailability and physical deletion are distinct; failed physical cleanup MUST NOT restore exposure.

Derived renditions MAY be purged/regenerated independently of canonical source where no governing requirement requires their retention. Obsolete renditions SHOULD NOT accumulate without bound.

---

## 16. Hard Invariants

1. MediaAsset identity MUST NOT equal a storage URL/key.
2. Upload completion MUST NOT imply business attachment.
3. Stored bytes MUST NOT automatically become usable media.
4. Asset identifier possession MUST NOT grant access.
5. Media MUST NOT own Product/Profile/Publication business semantics.
6. Canonical sources MUST remain preserved during their governing retention lifecycle unless another accepted authority requires removal.
7. Generated image renditions MUST use the accepted lossless policy.
8. Website performance MUST use suitable renditions rather than require full-resolution delivery.
9. Derived rendition MUST NOT become a new business asset merely because encoding/dimensions differ.
10. Logo optimisation MUST preserve brand integrity and required transparency.
11. Main Street MUST NOT silently redesign/recolour/crop merchant logos.
12. Explicit merchant/capability media role MUST outrank AI inference.
13. Photo and Video specialists MUST select/infer registered semantics only.
14. AI MUST NOT perform authoritative media transformation directly.
15. Known-profile deterministic processing MUST remain operable without AI.
16. Editorial/content modification MUST NOT be disguised as optimisation.
17. Canonical video source MUST be preserved without destructive replacement during its retention lifecycle.
18. Controlled lossy video encoding MAY apply only to derived delivery renditions under registered profiles.
19. Initial PRODUCT_VIDEO and short PROFILE/PROMOTIONAL storefront profiles SHALL permit up to 30 seconds.
20. The 30-second limit MUST NOT become a universal VIDEO or MediaAsset constraint.
21. Over-duration video MUST NOT be silently truncated.
22. Video duration constraints MUST remain role/profile-specific.
23. Exact video codec, bitrate, adaptive protocol and transcoder MUST remain implementation choices.
24. Business detachment MUST NOT equal immediate physical deletion.
25. Logical deletion MUST NOT depend on immediate physical cleanup success.
26. Storage/CDN provider migration MUST NOT rewrite business semantics.
27. Sensitive/private assets MUST NOT inherit public exposure from storage configuration.
28. Media processing MUST preserve traceability to source and processing profile/version.
29. Direct storage upload MUST remain bounded by Main Street authority.

---

## 17. Explicit Non-Responsibilities

This authority does NOT select object-storage provider, CDN provider, image-processing library, malware scanner, exact image codec implementation beyond accepted losslessness, exact image rendition dimensions, cache TTLs, signed-URL technology, multipart-upload library, video codec, bitrate, streaming protocol, transcoder, concrete Java classes/interfaces, or merchant/admin UI.

---

## 18. Acceptance Statement

Main Street's media model preserves source fidelity and merchant content authority while enabling fast responsive image delivery and controlled short-form video delivery.

> **Preserve the source; infer only registered optimisation intent; transform deterministically; deliver the smallest suitable authorised rendition; never let optimisation silently rewrite merchant meaning.**
