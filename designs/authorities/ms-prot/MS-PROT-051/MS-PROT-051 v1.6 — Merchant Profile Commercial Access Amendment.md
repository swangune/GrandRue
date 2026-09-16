# MS-PROT-051 — Merchant Profile Commercial Access Amendment

**Document ID:** MS-PROT-051
**Version:** 1.6
**Status:** ACCEPTED
**Approved:** 15 September 2026 — explicit manual approval of the complete amendment and three-file formalisation scope
**Authority type:** Merchant Profile commercial access classification
**Governed by:** DESIGN-RULES.md v2.4; DOCUMENT-GOVERNANCE.md v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** Composite MS-PROT-051 through v1.5 within commercial access classification only
**Depends on:** MS-PROT-051 v1.0 §§3–48; v1.1 §§1–45; v1.2 §§1–13; v1.3 §§1–13; v1.4 §§1–11; v1.5 §§1–24; MS-PROT-027 v1.4 §§4–16 and v1.5 §§31–45, composed with its accepted later amendments; composite MS-PROT-031, MS-PROT-053, MS-PROT-059, MS-PROT-062 and MS-PROT-063; MS-PROT-076; MS-PROT-056 v1.1 §§12–16, v1.7 §§4–5 and 10–12, and v1.9 §§4–6
**Closes:** The bounded Merchant Profile commercial-classification gap within MS-PROT-056-V17-DQ-001; not the complete deferred decision
**Implementation activation:** NONE
**Purpose:** Define exact commercial access classifications for existing Merchant Profile preparation, observation, authoring, retirement and privacy restriction without transferring source ownership or broadening public disclosure.

### 1. Governing decision

The following exact owner-qualified access contracts SHALL govern the commercial classification of the bounded operations defined in this amendment.

| Access contract | Commercial requirement |
|---|---|
| `profile/public-source-observation-access@1` | No Commercial Entitlement for §3 |
| `profile/merchant-preparation-access@1` | No Commercial Entitlement for §4 |
| `profile/merchant-observation-access@1` | No Commercial Entitlement for §5 |
| `profile/merchant-presence-authoring-access@1` | `MAINTAIN_MERCHANT_PRESENCE` for §6 |
| `profile/merchant-retirement-access@1` | No Commercial Entitlement for §7 |
| `profile/privacy-restriction-access@1` | No Commercial Entitlement for §8 |

Each contract identity consists of its owner, identity and revision. Its meaning SHALL NOT be broadened to cover future Profile families or operations without separately accepted authority.

These contracts classify access to existing owner operations. They do not establish new transport endpoints, actor privileges or lifecycle transitions.

### 2. Scope and ownership

The source portfolio is exactly:

1. Merchant Public Descriptor;
2. Merchant Contact Point;
3. Merchant Location;
4. Merchant Service Area Descriptor;
5. Merchant External Presence Link;
6. Merchant Classification Entry; and
7. the subordinate Merchant Location Exposure Choice governed by v1.5.

Merchant Profile retains ownership of these facts, revisions, current pointers and owner choices.

Commercial retains ownership of entitlement definitions, bindings, grants and commercial-source provenance.

This amendment does not classify or grant:

- Public Business Hours;
- Product, Offering or Listing operations;
- Publication operations;
- branding, media or Storefront composition;
- website delivery or namespace use;
- operational capability bindings;
- AI inference or external-provider execution;
- external profile synchronisation;
- discovery, search, tracking or analytics.

Their appearance in a Business Profile surface does not transfer ownership or commercial permission to Profile.

### 3. Public source observation

`profile/public-source-observation-access@1` requires no Commercial Entitlement for the Profile-owned portion of the initial PUBLIC Merchant Presence portfolio:

```text
profile/public-display-name
profile/public-tagline
profile/public-short-summary
profile/public-approved-description
profile/public-contact-point
profile/public-merchant-location
profile/public-service-area
profile/public-external-presence-link
```

Observation SHALL retain the exact registered contract and release affinity required by composite MS-PROT-027.

The exemption SHALL NOT waive:

- exact Merchant Scope;
- current source and lifecycle evidence;
- legitimate candidate formation;
- Projection Serviceability;
- owner-qualified merchant choices;
- current audience Exposure;
- security, privacy or resource-protection requirements.

It does not expose raw coordinates, internal provenance, Controller identity, private values or retained history.

Merchant Classification Entries remain excluded from the initial PUBLIC Merchant Presence portfolio. A stored `PUBLIC` classification choice does not create its missing public projection or Exposure contract.

Public Business Hours remain separately owned and are not classified by this contract.

This exemption is not website-delivery permission and creates no new public browsing, export or historical-read operation.

### 4. Non-committing preparation

`profile/merchant-preparation-access@1` requires no Commercial Entitlement for preparing or validating candidates for the existing Profile operations in scope.

Preparation SHALL NOT itself commit:

- a source fact or revision;
- a current pointer;
- a lifecycle transition;
- a merchant Exposure choice;
- a configuration binding;
- a publication selection; or
- an external effect.

Preparation may consume only information independently authorised for that preparation purpose.

This contract does not authorise AI inference, provider retrieval, geocoding or import execution. Those require their own accepted contracts.

Prepared content and prior validation SHALL NOT become durable permission for a later mutation.

### 5. Authorised merchant observation

`profile/merchant-observation-access@1` requires no Commercial Entitlement for otherwise-authorised merchant inspection of the in-scope facts, owner choices, retained revisions and logical mutation receipts.

Observation SHALL preserve current actor authority, exact Merchant Scope, account-lifecycle restrictions and applicable data-protection limits under the accepted access and source authorities.

Possession of a fact identity, revision identity or receipt identity SHALL NOT confer access.

This contract does not create:

- public access to merchant-only information;
- cross-merchant access;
- a new staff privilege;
- bulk export;
- access to disposed data; or
- an unrestricted historical-retention obligation.

A recovered receipt SHALL remain distinguishable from current source state.

### 6. Protected presence authoring

`profile/merchant-presence-authoring-access@1` requires current permission for:

```text
MAINTAIN_MERCHANT_PRESENCE
```

for the following existing operations:

1. establishing or updating Merchant Public Descriptor content;
2. creating or updating Merchant Contact Points;
3. creating or correcting Merchant Locations;
4. creating or updating Merchant Service Area Descriptors;
5. creating or updating Merchant External Presence Links;
6. creating or updating Merchant Classification Entries; and
7. establishing or changing an existing owner-defined Exposure choice, except for the narrowly exempt restriction in §8.

The purpose covers maintenance of the accepted source facts whether their owner choice is public or private. It does not require a merchant to disclose a private fact to obtain source-maintenance access.

The operation SHALL also satisfy its existing actor, account, approval, value-validation, lifecycle, reference and concurrency requirements.

In particular:

- Classification Entry mutation retains v1.4’s current-Controller requirement.
- Delegated staff authority SHALL exist only where the applicable accepted operation permits it and the required privilege has been registered.
- Onboarding adoption SHALL retain exact reviewed-content affinity and protection against overwriting newer direct edits.
- Location Exposure-choice mutation SHALL preserve its independent revision stream and ACTIVE-Location requirement.

Commercial permission SHALL be revalidated at the authoritative mutation boundary. Candidate preparation or an earlier permission result is insufficient.

This classification does not create an empty Profile during Merchant Account establishment or make Profile authoring a prerequisite for establishing the account.

### 7. Retirement

`profile/merchant-retirement-access@1` requires no Commercial Entitlement for the existing retirement operations for:

- Merchant Contact Points;
- Merchant Locations;
- Merchant Service Area Descriptors;
- Merchant External Presence Links; and
- Merchant Classification Entries.

The exemption does not bypass existing actor authority, account-lifecycle restrictions, expected-revision checks or active-reference constraints.

Retirement SHALL NOT:

- erase retained history;
- rewrite a consuming capability’s commitments;
- mutate configuration bindings;
- reactivate a retired identity;
- retire another owner’s object; or
- authorise external-provider cleanup.

This contract creates no Merchant Public Descriptor retirement operation and no new closure or post-closure remediation path.

### 8. Privacy-only restriction

`profile/privacy-restriction-access@1` requires no Commercial Entitlement when the sole requested business change is to establish `PRIVATE_INTERNAL` through an existing owner-choice operation for:

- an existing Merchant Contact Point;
- an existing Merchant Service Area Descriptor;
- an existing Merchant Classification Entry; or
- an existing ACTIVE Merchant Location’s Exposure Choice.

All other source values SHALL remain unchanged. New revision, request and provenance evidence required by the existing operation is permitted.

An update that also changes a telephone value, description, label, geography or another source value is not covered by this exemption. The additional authoring requires §6.

Setting `PUBLIC` is not covered by this exemption.

The operation SHALL preserve its existing authority, account-lifecycle, validation, idempotency and concurrency requirements. The exemption neither grants staff privileges nor creates a new operation for a retired or nonexistent fact.

Existing revocation-sensitive Projection and Exposure rules govern the disclosure consequence. An older projection SHALL NOT override the accepted restriction.

Other privacy, correction, erasure and remediation operations remain governed by their existing authorities; this contract does not replace them.

### 9. Commercial allocation and support

The `MAINTAIN_MERCHANT_PRESENCE` purpose SHALL be allocated to FREE, with explicit inclusion in BUSINESS and GROWTH.

Its binding target family SHALL be:

```text
OPERATION_ACCESS
```

Its exact target SHALL be:

```text
profile/merchant-presence-authoring-access@1
```

The concrete entitlement identity, plan revision identities and grant sets remain subject to separate catalogue approval.

Catalogue assembly SHALL retain exact classification authority for the exempt contracts when those contracts are included as supporting access requirements. Absence of binding information SHALL NOT be treated as an exemption.

Neither descriptive Location count nor the presence of private or public Location records creates an additional tier or scale charge. MS-PROT-056 v1.1 §§12–16 remains unchanged.

Profile maintenance does not satisfy commercial requirements for Booking, Ordering, Inventory, Scheduling or another consuming capability.

No runtime permission may be inferred from a merchant category or tier-name string.

### 10. Lifecycle, history and recovery

This amendment changes no accepted Profile identity, revision, retirement, correction-versus-relocation or transaction semantics.

Existing per-fact concurrency boundaries SHALL remain. No global Business Profile revision or transaction is introduced.

Exact logical retries SHALL retain their existing committed-result recovery semantics. Recovery of an already-committed result does not constitute new authoring and SHALL NOT be denied solely because its former authoring grant is no longer available.

Recovery still requires the independently applicable actor, scope, account and data-access authority.

A retry requiring a new authoritative effect SHALL satisfy the classification governing that effect at execution.

Commercial loss SHALL NOT itself delete, retire, rewrite or make Profile facts public or private. Public observation follows §3; website delivery follows its own accepted contracts.

### 11. Failure and neighbouring boundaries

Commercial rejection SHALL remain distinguishable from:

- authentication or authorisation rejection;
- account-operation restriction;
- invalid or unsupported source values;
- stale revision or request-identity conflict;
- missing or retired facts;
- active-reference conflict;
- unavailable authoritative evidence;
- technical failure; and
- uncertain execution.

Unavailable evidence SHALL NOT be reported as a proven commercial denial, successful mutation or empty historical result.

Profile commits do not constitute Storefront publication, namespace binding, external synchronisation or operational capability activation.

Classification metadata does not determine business architecture. Descriptive service geography does not establish executable serviceability.

AI proposals, imported values and provider verification remain distinct from merchant-approved source truth. This amendment creates no provider dependency for ordinary deterministic Profile maintenance or observation.

Existing retention and data-use authority remains unchanged. No universal retention duration or permission to reuse private data is introduced.

### 12. Trade-offs, falsification and acceptance boundary

One authoring purpose is selected because the in-scope operations maintain one commercially included presence service while preserving independently owned facts.

Separate commercial permissions per field, descriptive Location or UI section are rejected as unnecessary packaging complexity.

Website-delivery permission is not reused because maintaining source facts and delivering a website are different operations.

Preparation, authorised observation, retirement and privacy-only restriction are explicitly exempt so they do not become accidental paid barriers or depend on new-authoring permission.

Required implementation falsification SHALL cover:

- an information-only merchant without premises;
- private premises with public service-area presentation;
- mixed product/service business evolution without category-driven activation;
- multiple descriptive Locations without invented scale charges;
- operation-specific Controller and staff restrictions;
- privacy-only restriction versus combined authoring;
- stale onboarding and concurrent source edits;
- exact retry after subsequent revision or retirement;
- exclusion of classification metadata, raw coordinates and private history from public access;
- active-reference protection during retirement; and
- AI/provider evidence that has not received required approval.

These scenarios establish design conformance conditions, not evidence that production execution has already passed them.

Acceptance establishes only the bounded commercial classifications in this amendment.

It does not:

- select a concrete entitlement identity;
- approve or publish the complete catalogue;
- close MS-PROT-056-V17-DQ-001;
- close existing Profile deferred decisions;
- activate a production endpoint, provider or worker;
- complete C3; or
- authorise a push before verified C3 completion.
