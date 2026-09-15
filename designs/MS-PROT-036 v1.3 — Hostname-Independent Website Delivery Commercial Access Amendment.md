# MS-PROT-036 — Hostname-Independent Website Delivery Commercial Access Amendment

**Document ID:** MS-PROT-036
**Version:** 1.3
**Status:** ACCEPTED
**Approved:** 14 September 2026 — explicit manual approval of the complete revised pair and limited formalisation scope
**Authority type:** Storefront delivery access contract
**Governed by:** DESIGN-RULES v2.3; DOCUMENT-GOVERNANCE v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** MS-PROT-036 v1.2 within customer-facing website-delivery commercial classification
**Depends on:** MS-PROT-036 v1.2 §§2, 11–14, 17–18; MS-PROT-094 v1.0 §§1, 3.3, 6, 10–18, 26; MS-PROT-088 v1.1; MS-PROT-056 v1.7 §§5, 10 and v1.9 §§4–6; composite MS-PROT-027 and MS-PROT-062
**Implementation activation:** NONE
**Purpose:** Define one website-delivery permission independently of the website address.

## 1. Governing decision

Storefront SHALL define:

```text
storefront/website-delivery-access@1
```

**Protected purpose:** `SERVE_CUSTOMER_WEBSITE`.

The contract governs delivery of an existing published customer-facing website through an independently authorised Website Hostname Binding. It does not create, compose or publish a website.

The same contract applies to both namespace families governed by MS-PROT-088 v1.1.

## 2. Identity and ownership

The contract is identified by its exact owner, identifier and revision. Its meaning SHALL remain immutable.

Storefront owns delivery. Merchant Brand Infrastructure owns hostname authority. Source owners retain business and operation truth. Commercial owns grant evaluation.

A hostname is not storefront, composition or merchant identity. This contract excludes privileged merchant clients, preview tools and arbitrary hosting.

## 3. Commercial composition

Delivery requires `SERVE_CUSTOMER_WEBSITE` and the namespace purpose applicable to the actual binding:

| Namespace family | Additional required purpose |
|---|---|
| `PLATFORM_DELEGATED_NAMESPACE` | `USE_PLATFORM_WEBSITE_NAMESPACE` |
| `MERCHANT_CONTROLLED_DOMAIN` | `USE_MERCHANT_CONTROLLED_WEBSITE_DOMAIN` |

Both required purposes SHALL be satisfied through their exact Commercial Access Bindings. An unknown namespace family SHALL fail without a default classification.

A valid hostname, namespace-use permission or prepared rendering result SHALL NOT satisfy the website-delivery grant.

## 4. Delivery evidence

Delivery requires:

- trusted exact current hostname-binding and Merchant Scope affinity;
- current namespace-use authority under MS-PROT-088 v1.1;
- the one current published Composition Revision for the selected storefront;
- compatible supported rendering;
- authorised projections and independently required source access;
- applicable security, Resource Protection and delivery requirements.

Changing the address SHALL NOT bypass these requirements.

## 5. MS-PROT-094 conformance

Both hostname families SHALL use merchant-specific Composition Revisions and authorised projections over a shared Rendering Engine.

Hostname-family choice SHALL NOT require a different website, template family, merchant-specific application or rendering architecture.

Changing hostname alone SHALL NOT require a new Composition Revision merely to switch commercial address families.

Where a change also changes merchant-approved arrangement, content or business meaning, the applicable owner approval rules govern.

Address-routing and delivery information remains owned by namespace and delivery responsibilities. This amendment does not authorise blanket rewriting of merchant content.

## 6. Source and operation boundaries

Website-delivery permission grants no private Profile, customer history, Enquiry, Booking, Order, Payment, Conversation or other independently protected purpose.

Every embedded read or operation remains governed by its owner. The exact Publication source-read and Enquiry preparation exemptions do not exempt website delivery.

A rendered interaction or displayed operation SHALL NOT confer durable execution authority.

## 7. Published presentation and AI

MS-PROT-094 validation, controlled rollout and rollback requirements remain applicable. A failed candidate SHALL NOT replace a valid published composition.

Serving an existing valid website SHALL NOT depend on live AI availability.

This amendment introduces no templates, Presentation Profiles, bespoke merchant code or runtime AI dependency. It grants no permission to change content, source facts or policy.

## 8. Tier placement and commercial changes

`SERVE_CUSTOMER_WEBSITE` belongs to FREE and the explicit paid-tier supersets.

The paid custom-domain distinction belongs to namespace use, not a separate website product or architecture.

Permission evaluation respects the union of independently valid grant sources. Cancellation of one source SHALL NOT negate another valid source.

Missing required permission SHALL deny the affected delivery path. It SHALL NOT by itself delete compositions, withdraw Publications, alter Configuration, release hostnames or cancel commitments.

No residual website-delivery right, grace period or automatic migration is established.

## 9. Failure, caching and repetition

Commercial denial, invalid hostname authority, incompatible rendering, source-access failure and technical delivery failure SHALL remain distinct.

Failure SHALL NOT disclose another merchant's website, unpublished material or protected diagnostics.

Repeated requests remain subject to current authority. Caches SHALL obey governing freshness and affinity requirements. Prior successful delivery is not permanent permission.

This amendment introduces no distributed transaction.

## 10. Catalogue and implementation consequences

Catalogue assembly SHALL include the common website-delivery binding, both conditional address-use bindings, their tier placements and independently required source and operation contracts.

No wildcard grant for embedded operations is permitted.

Downstream implementation SHALL demonstrate exact routing, enforcement of both required purposes, hostname-independent rendering, current published composition selection, source isolation, AI independence and non-destructive denial.

## 11. Closure and exclusions

This amendment resolves only commercial delivery of existing published websites through the two governed hostname families.

It does not resolve initial composition-publication access, namespace provisioning, cutover operations, prices, quantitative allowances or service-level guarantees.

`MS-PROT-056-V17-DQ-001` remains OPEN. Acceptance neither publishes nor activates a catalogue and authorises no C3 completion or programme promotion.

## 12. Paired review and falsification

**Vision result: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY.** A merchant has one website whose address can evolve. Internal permissions do not impose separate website products or merchant-administered rendering architectures.

| Challenge | Required outcome |
|---|---|
| Platform address changes to custom domain | Same delivery contract and rendering architecture; independent domain authority required |
| Domain is verified but cutover is unapproved | No cutover authority |
| Custom-domain grant is absent | Common website grant supplies no domain-use authority |
| Platform address remains independently authorised | Evaluate its own authority; fabricate no fallback |
| Hostname changes while arrangement remains unchanged | No recomposition solely for namespace-family change |
| Composition candidate is invalid | Preserve the valid published composition |
| AI is unavailable | Existing valid rendering remains independent |
| Enquiry origination grant is absent | Website permission supplies no submission authority |
| Hostname binding is ambiguous | Fail closed |
| Namespace permission ends | No automatic reassignment or loss of domain rights |

**Alternative rejected:** the earlier unapproved platform-address-specific delivery contract unnecessarily coupled delivery identity to hostname family. This pair replaces that unapproved proposal in full.

**Ambiguity review:** website delivery, namespace use, assignment, cutover, composition and customer-operation permission remain distinct. MS-PROT-094 governs presentation and shared rendering; these contracts do not supersede it.

Evidence consists of accepted-authority inspection and these scenarios, not implementation or performance proof.
