# MS-PROT-088 — Website Binding Selection, Disconnection & Commercial Access Amendment

**Document ID:** MS-PROT-088
**Version:** 1.2
**Status:** ACCEPTED
**Approved:** 14 September 2026 — explicit manual approval of the complete revised authority and limited formalisation scope
**Authority type:** Merchant Brand Infrastructure binding lifecycle and access contracts
**Governed by:** DESIGN-RULES v2.4; DOCUMENT-GOVERNANCE v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** MS-PROT-088 v1.0 §§22–27, 46, 48 within Main Street website-binding selection, disconnection and commercial composition; supplements v1.1 without changing its exact namespace-use contract identities or meanings
**Depends on:** MS-PROT-088 v1.0 §§6–11, 20–28, 44–58 and v1.1 §§1–8; MS-PROT-036 v1.3 §§1–10 and v1.4 §§3–10; MS-PROT-094 §§6, 10–18; MS-PROT-025 §§1–6, 10, 14; MS-PROT-031 §§1–12; composite MS-PROT-053 and MS-PROT-062; MS-PROT-063 §§3–4, 12–14, 16–24; MS-PROT-072 §§1–9; MS-PROT-076 §§3–9, 16–24; MS-PROT-056 v1.7 §§4–6, 10–12 and v1.9 §§4–6
**Closes:** No complete deferred decision; partially addresses MS-PROT-056-V17-DQ-001
**Implementation activation:** NONE
**Purpose:** Establish explicit, recoverable Main Street hostname-binding transitions without treating commercial permission, provider success or historical approval as current routing authority.

## 1. Governing decision

Merchant Brand Infrastructure SHALL own the current website-binding selection for each Merchant Scope and namespace family.

The admitted families remain:

- `PLATFORM_DELEGATED_NAMESPACE`;
- `MERCHANT_CONTROLLED_DOMAIN`.

Each family SHALL have an independent selection boundary. Changing or disconnecting one SHALL NOT implicitly change the other.

Three exact owner-qualified contracts govern this amendment:

| Contract | Responsibility |
|---|---|
| `merchant-brand-infrastructure/inspect-website-binding@1` | Authorised inspection and non-committing preparation |
| `merchant-brand-infrastructure/select-website-binding@1` | Initial selection or replacement |
| `merchant-brand-infrastructure/disconnect-website-binding@1` | Removal of the selected binding |

Their identities and meanings SHALL be immutable.

## 2. Scope and non-goals

This amendment governs Main Street’s authoritative association between authorised hostnames and an existing storefront.

It covers initial selection, replacement, disconnection, concurrency, retry evidence and commercial requirements.

It does not:

- create a Merchant Account or storefront;
- allocate, reserve, rename or release a platform namespace;
- register, renew or transfer a domain;
- establish domain control merely from supplied text;
- authorise DNS or certificate-provider mutations;
- activate business email or privileged authentication origins;
- publish or withdraw Storefront compositions;
- introduce staff domain-management privileges;
- define prices, allowances, schedules or a generic infrastructure workflow engine.

Existing namespace assignment and purpose-qualified control are prerequisites, not effects of binding selection.

## 3. Website Binding Selection

A **Website Binding Selection** is an immutable Merchant Brand Infrastructure fact containing:

- selection identity;
- Merchant Scope and namespace family;
- predecessor selection identity, or `NO_SELECTION`;
- an exact hostname portfolio and existing storefront identity, or `NO_BINDING`;
- references to the namespace and authority evidence supporting the decision;
- originating operation and logical request identity;
- acting principal and Controller Relationship reference;
- authoritative UTC commit instant.

There SHALL be at most one current selection per Merchant Scope and namespace family.

Before any selection, the state is `NO_SELECTION`. Neither `NO_SELECTION` nor a current `NO_BINDING` authorises routing.

A new selection SHALL NOT mutate an earlier selection.

Recorded evidence explains the historical decision. It does not freeze domain control, commercial permission or technical readiness for future use.

## 4. Hostname portfolio and isolation

A platform-family selection SHALL contain exactly one already-assigned platform hostname.

A custom-domain selection SHALL contain:

- exactly one primary hostname;
- optionally one equivalent canonical redirect alias within the accepted v1.0 portfolio.

Both custom-domain members SHALL belong to the same Merchant Scope and storefront. An alias SHALL redirect only to that selection’s primary hostname.

An exact active hostname SHALL have at most one authoritative Merchant Scope/storefront routing target.

The accepted prohibition on conflicting primary namespace claims across Merchant Scopes remains binding.

Platform and custom-domain selections MAY coexist for the same storefront. This does not introduce a cross-family redirect or fallback. Each address independently requires its own current authority.

Hostname comparisons SHALL use validated canonical hostname identities from the owning namespace authority, not unvalidated client strings.

## 5. Inspection and preparation

`inspect-website-binding@1` requires trusted scope and the current ACTIVE Merchant Controller.

For an OPEN account without effective account-wide Suspension, it MAY:

- inspect current selections and authorised retained receipts;
- inspect an exact proposed hostname portfolio;
- identify missing prerequisites and conflicts;
- present the consequences of replacing an existing selection.

For a CLOSING account without effective account-wide Suspension, inspection is limited to existing state and evidence needed for governed disconnection or closure work.

No Commercial Entitlement is required for this bounded contract.

Inspection SHALL NOT allocate names, verify control by declaration, change selections, modify provider infrastructure or publish a website.

Preparation creates no durable approval or mutation permission. Any retained preparation information remains subject to applicable data-handling authority.

## 6. Selection authority and commercial requirements

Selection requires:

1. trusted context for the exact Merchant Scope;
2. an OPEN account without effective account-wide Suspension;
3. the current ACTIVE Merchant Controller;
4. explicit approval of the exact proposed portfolio, storefront and replacement consequences;
5. the applicable current namespace-use commercial permission;
6. the validation and consistency conditions in §7.

Commercial requirements are:

| Family | Required existing purpose |
|---|---|
| Platform | `USE_PLATFORM_WEBSITE_NAMESPACE` through `merchant-brand-infrastructure/platform-website-namespace-use@1` |
| Custom domain | `USE_MERCHANT_CONTROLLED_WEBSITE_DOMAIN` through `merchant-brand-infrastructure/custom-website-domain-use@1` |

No independent setup entitlement is introduced.

Selection consumes the commercial component of those existing purposes for the proposed destination. It SHALL NOT require the destination binding to exist already merely to evaluate that component.

The existing namespace-use contracts still govern actual use of a current binding. Their control, routing and readiness requirements are not bypassed.

A former Controller’s approval SHALL NOT authorise selection after the Controller Relationship changes.

No provider, staff, customer or unrestricted system-selection authority is introduced.

## 7. Selection operation and atomicity

`select-website-binding@1` SHALL accept:

- logical request identity;
- trusted Merchant Scope;
- namespace family;
- exact proposed hostname portfolio;
- existing storefront identity;
- expected current selection identity, or `NO_SELECTION`;
- exact approval and supporting authority references;
- trusted execution context.

Before committing, Merchant Brand Infrastructure SHALL establish:

- namespace, hostname, storefront and scope affinity;
- current platform assignment or purpose-qualified domain-control authority;
- compliance with the accepted portfolio;
- absence of conflicting hostname and primary namespace claims;
- technical conditions required to admit the exact local routing target safely;
- current actor, account and commercial authority;
- the expected current selection.

The operation SHALL atomically:

1. append the new selection;
2. make it current;
3. update the corresponding authoritative hostname associations;
4. remove associations displaced within that family;
5. record the committed logical-request result.

Uniqueness checks and routing changes SHALL share the consistency boundary required to prevent conflicting committed associations.

Success means **Main Street’s binding selection committed**. It does not assert that public DNS has changed, a composition is published, or customers can successfully use the website.

## 8. Disconnection operation

`disconnect-website-binding@1` SHALL accept:

- logical request identity;
- trusted Merchant Scope and namespace family;
- expected current selection identity, or `NO_SELECTION`;
- explicit disconnection instruction;
- trusted execution context.

The caller SHALL be the current ACTIVE Merchant Controller of an OPEN or CLOSING account without effective account-wide Suspension.

No Commercial Entitlement is required.

The operation SHALL atomically append a new `NO_BINDING` selection, make it current, remove that family’s active routing associations and record the request result.

A new logical disconnection request SHALL establish this revision even when no binding is currently selected. This prevents an earlier request based on the previous selection state from subsequently reconnecting the address.

Disconnection SHALL NOT delete or release:

- domain ownership or platform namespace assignment;
- another namespace family’s selection;
- business-email bindings;
- Storefront compositions;
- capability-owned content or business history.

External cleanup remains independently governed. Local disconnection SHALL NOT wait for DNS cleanup before ceasing to authorise routing.

## 9. Concurrency, retries and recovery

Selection and disconnection within a family SHALL serialize against the same current-selection boundary.

At most one competing transition from the same expected selection may commit. A stale request SHALL fail with a selection conflict; it SHALL NOT silently substitute the new current selection.

Global hostname and namespace-claim invariants SHALL remain protected across different Merchant Scopes and family boundaries.

Logical request identity SHALL be unique within the Merchant Scope and namespace family. Its recorded input SHALL include the operation, expected selection, proposed portfolio where applicable and approval affinity.

- Same identity and identical input: return the original committed result.
- Same identity and different input: reject identity reuse.
- Lost acknowledgement: resolve the retained receipt or retry identically.
- Uncertain commit: report uncertainty; do not invent another logical request.

Receipt disclosure requires current authorised inspection access. Returning an old receipt SHALL NOT make its selection current again.

A retry that only returns a committed receipt is not a new binding operation and SHALL NOT be blocked solely because the commercial grant has since ended.

## 10. Provider work and maintenance

Selecting a binding does not authorise registrar, DNS or certificate-provider effects.

Those effects require their own accepted responsibility, approval, fulfilment binding, current readiness and durable external-effect identity.

Provider success SHALL NOT directly write or restore a Website Binding Selection.

Where external work is uncertain:

- Main Street SHALL distinguish uncertainty from known failure;
- provider evidence SHALL not be fabricated;
- blind duplicate effects SHALL not be used as recovery;
- historical approval SHALL not replace current selection authority.

Maintenance that preserves the exact selected portfolio does not require a new binding selection merely because technical evidence or infrastructure is refreshed. It does not acquire an independent binding-maintenance entitlement.

Maintenance SHALL NOT change hostnames, Merchant Scope, storefront association or alias roles. Those changes require selection.

After disconnection, a late DNS update, renewed certificate or provider callback SHALL NOT restore routing authority.

Any implementation needing an ungoverned asynchronous continuation, external-effect contract or cleanup action SHALL stop that path for design resolution.

## 11. Delivery and commercial change

Actual delivery continues to require:

- the exact current binding selection;
- current namespace-use permission;
- `SERVE_CUSTOMER_WEBSITE`;
- the current published Storefront composition;
- current source, operation, security and applicable readiness conditions.

Cached routing SHALL preserve exact selection affinity. A known superseded or disconnected selection SHALL not authorise delivery.

If required current authority cannot be established, the affected path SHALL fail safely.

Loss of custom-domain permission SHALL not alter an independently authorised platform-family selection. Continued platform delivery is independent existing authority—not an invented fallback.

Neither grant loss nor a provider outage SHALL automatically change selections, domain rights, business facts or compositions.

Both families retain MS-PROT-094’s shared rendering architecture and merchant-specific composition model.

## 12. Failures, evidence and retention

Results SHALL distinguish:

- inaccessible or wrong-scope target;
- actor or account-lifecycle rejection;
- commercial rejection;
- missing namespace/control authority;
- invalid portfolio;
- hostname or namespace conflict;
- stale selection;
- unavailable technical evidence;
- technical failure or uncertain commit.

Pre-commit rejection SHALL leave the current selection unchanged.

Selection and receipt evidence SHALL be limited to what is necessary for routing authority, historical interpretation, attribution, concurrency and duplicate prevention.

Credentials and unnecessary copies of provider or merchant data SHALL not be embedded in selections.

Concrete data-lifecycle qualification, retention and disposition support under composite MS-PROT-053 are required before production persistence activation. This amendment establishes no universal retention duration.

Unavailable retained evidence SHALL cause explicit failure of the affected historical operation, not reconstruction from guessed current state.

## 13. Vision, comparative justification and falsification

**Vision result: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY.**

The merchant chooses addresses and approves material consequences. Internal revisions, uniqueness coordination and receipt recovery remain platform responsibilities.

The concrete-operation approach is preferred over classification-only contracts because it places commercial rules beside the mutations they constrain. Reusing the existing namespace-use purposes avoids another subscription component.

Independent family selections preserve an existing platform address when custom-domain use becomes unavailable. Their additional coordination cost is bounded by two admitted families, not a generic multi-domain management framework.

Design falsification requires these outcomes:

| Challenge | Required outcome |
|---|---|
| Low-software-capacity information publisher connects an address | No fabricated product, Booking or Payment prerequisite; no revision-token administration |
| Salon adds beauty products | Same Merchant Scope and storefront; no category-driven address replacement |
| Online retailer changes its custom primary hostname | Exact replacement approval; platform-family selection remains unchanged |
| Receptionist can manage bookings | No resulting domain-management authority |
| Controller transfers before selection commits | Former Controller cannot complete the new selection |
| Two merchants claim the same active hostname | No conflicting committed routing |
| Two replacements use the same predecessor | At most one commits |
| Initial disconnection races a delayed initial selection | The disconnection revision invalidates the stale selection |
| Selection commits but acknowledgement is lost | Identical retry returns the original result |
| Custom-domain permission ends | That address fails its current permission check; independent platform authority is preserved |
| DNS still points at Main Street after disconnection | No routing from the removed binding |
| Certificate renewal finishes after disconnection | No automatic reconnection |
| Provider work succeeds but binding validation fails | No fabricated binding success or distributed rollback claim |
| AI is unavailable | Deterministic binding operations and independently valid website delivery remain usable |

These are design-review scenarios, not executed implementation tests.

## 14. Amendment effect and remaining gates

This amendment establishes the local binding-selection and disconnection lifecycle and their exact supporting commercial requirements.

It does not claim complete initial namespace provisioning.

The following remain separately required where applicable:

- platform namespace allocation and release authority;
- complete external DNS/certificate execution and continuation contracts;
- production retention qualification;
- remaining catalogue supporting classifications;
- exact entitlement definitions and the complete three-tier manifest;
- production implementation and verification.

MS-PROT-056-V17-DQ-001 remains OPEN. Prices and reserved-service decisions remain unchanged.

Acceptance does not activate production, promote an implementation node or complete C3.
