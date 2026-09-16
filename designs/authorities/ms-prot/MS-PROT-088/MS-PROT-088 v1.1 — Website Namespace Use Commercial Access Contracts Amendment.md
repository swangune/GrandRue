# MS-PROT-088 — Website Namespace Use Commercial Access Contracts Amendment

**Document ID:** MS-PROT-088
**Version:** 1.1
**Status:** ACCEPTED
**Approved:** 14 September 2026 — explicit manual approval of the complete revised pair and limited formalisation scope
**Authority type:** Merchant Brand Infrastructure access contracts
**Governed by:** DESIGN-RULES v2.3; DOCUMENT-GOVERNANCE v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** MS-PROT-088 v1.0 §§6–7, 24–25, 46 within website namespace commercial-use classification
**Depends on:** MS-PROT-088 v1.0 §§6–7, 11, 20–28, 46–48; MS-PROT-094 v1.0 §§1, 6, 13–18; MS-PROT-056 v1.7 §§4–6, 10 and v1.9 §§4–6; composite MS-PROT-062
**Implementation activation:** NONE
**Purpose:** Define address-specific commercial permission independently of website composition, rendering and publication.

## 1. Governing decision

Merchant Brand Infrastructure SHALL define exactly:

| Access contract | Protected purpose |
|---|---|
| `merchant-brand-infrastructure/platform-website-namespace-use@1` | `USE_PLATFORM_WEBSITE_NAMESPACE` |
| `merchant-brand-infrastructure/custom-website-domain-use@1` | `USE_MERCHANT_CONTROLLED_WEBSITE_DOMAIN` |

Each contract requires its exact Commercial Access Binding. Missing classification or binding SHALL NOT be treated as exemption.

These contracts govern use of existing authorised namespaces and Website Hostname Bindings. They do not establish them.

## 2. Identity and ownership

Each contract is identified by its exact owner, identifier and revision. Its meaning SHALL remain immutable. Future families or revisions SHALL NOT inherit permission automatically.

Merchant Brand Infrastructure owns namespace and hostname-binding authority. Commercial owns entitlement definitions and grant evaluation. Storefront owns presentation and delivery.

A hostname is an authoritatively resolved locator. It is not Merchant Scope identity, entitlement evidence or composition identity.

## 3. Platform-delegated website namespace

`merchant-brand-infrastructure/platform-website-namespace-use@1` applies only to existing authorised `PLATFORM_DELEGATED_NAMESPACE` website use.

Use requires current namespace assignment, current Website Hostname Binding, exact hostname/namespace/Merchant Scope affinity, website-purpose authority, the required purpose grant, and applicable HTTPS, routing and security requirements.

This contract grants no namespace allocation, binding establishment, merchant ownership of the platform namespace or external transfer right.

## 4. Merchant-controlled website domain

`merchant-brand-infrastructure/custom-website-domain-use@1` applies to existing authorised `MERCHANT_CONTROLLED_DOMAIN` website bindings within the initial portfolio in MS-PROT-088 v1.0 §7.

Use requires current purpose-qualified domain control, a current binding, exact hostname/namespace/Merchant Scope affinity, required approved website-binding and cutover authority, the required purpose grant, and applicable HTTPS, uniqueness and routing requirements.

Domain verification, a DNS pointer or registrar credentials SHALL NOT establish a commercial grant or website-binding authority.

This contract grants no registration, renewal, transfer, DNS modification or cutover operation.

## 5. Routing and presentation independence

An exact active hostname SHALL resolve to at most one Merchant Scope under MS-PROT-088 v1.0 §22. Unbound, conflicting or wrong-scope routing SHALL fail safely without falling back to another merchant.

Neither namespace family determines templates, Presentation Profiles, a separate website application, a separate rendering architecture, required Main Street visual identity or reduced compositional freedom.

A namespace change does not itself create a new Merchant Scope or composition.

No automatic concurrent-address, redirect-alias or replacement-binding authority is created. Such behaviour requires its own governing owner authority.

## 6. Commercial placement

`USE_PLATFORM_WEBSITE_NAMESPACE` belongs to FREE. `USE_MERCHANT_CONTROLLED_WEBSITE_DOMAIN` belongs to BUSINESS and its explicit GROWTH superset under MS-PROT-056 v1.7 §§5–6.

This amendment establishes no prices, quantitative allowances or executable grants. Permission derives from independently valid grant sources, not a tier label.

A namespace-use grant permits only the governed address use. It does not satisfy website-delivery permission.

## 7. Entitlement loss and rights

Loss of required namespace permission SHALL deny the affected hostname use. It SHALL NOT by itself:

- release or reassign a platform namespace;
- transfer merchant domain rights;
- cancel domain registration or separately contracted renewal;
- prevent legitimate transfer out;
- delete registrar or binding history;
- delete website or source facts.

An independently authorised address may continue under its own current authority. No automatic fallback, redirect, reassignment or grace period is established.

## 8. Failure and exclusions

Commercial denial, invalid binding, scope conflict, unavailable domain-control authority and technical failure SHALL remain distinct. Cached evidence SHALL NOT replace required current validation.

Access evaluation performs no authoritative mutation or provider effect. Repeated evaluation SHALL NOT allocate a namespace or establish a binding.

This amendment grants no email, mailbox, sender, merchant-authentication-origin or website-publication authority.

Initial allocation, naming, reassignment, release and termination remain separately required decisions wherever accepted authority is insufficient.

No namespace, binding, executable grant or production service is established by acceptance.

## 9. Review and falsification

**Vision result: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY.** A merchant has one website whose address can evolve. Internal permissions do not impose separate website products or merchant-administered rendering architectures.

The paired MS-PROT-036 v1.3 review applies to this amendment. In particular, domain verification without approved cutover does not authorise cutover; a common website grant without custom-domain permission does not authorise custom-domain use; and loss of namespace permission does not release namespaces or remove domain rights.

**Alternative rejected:** the earlier unapproved platform-address-specific website-delivery contract unnecessarily coupled delivery identity to hostname family. This revised pair replaces that unapproved proposal in full.

**Ambiguity review:** delivery, address use, assignment, cutover, composition and customer-operation permissions remain separate. MS-PROT-094 continues to govern presentation and shared rendering.

Evidence consists of accepted-authority inspection and the paired scenarios. It is not proof of implementation or performance.
