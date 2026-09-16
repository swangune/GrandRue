# MS-PROT-088 — Website Connection Request & Bounded Completion Amendment

**Document ID:** MS-PROT-088
**Version:** 1.4
**Status:** ACCEPTED
**Approved:** 15 September 2026 — explicit manual confirmation of the complete proposal and stated formalisation scope
**Authority type:** Merchant Brand Infrastructure request lifecycle and bounded execution authority
**Governed by:** DESIGN-RULES v2.4; DOCUMENT-GOVERNANCE v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** MS-PROT-088 v1.2 §§6–10 only by adding separately identified deferred-completion authority and request invalidation at conflicting binding transitions; qualifies v1.0 §26’s preparation boundary without weakening domain-control or delivery requirements.
**Depends on:** MS-PROT-088 v1.0 §§9–11, 20–28, 44–58; v1.1 §§1–8; v1.2 §§3–12; v1.3 §§3–13; MS-PROT-025 §§3–6, 14; MS-PROT-031; composite MS-PROT-048, MS-PROT-053 and MS-PROT-062; MS-PROT-063 §§3–4, 12–14, 16–24; MS-PROT-065 v1.1 §§2–14, 23–37; MS-PROT-067 §§6–18; MS-PROT-069 v1.1 §§2–20, 24–28; MS-PROT-072 §§3–9; MS-PROT-076 §§3–9, 16–30; MS-PROT-056 v1.9 §§4–6.
**Closes:** The bounded deferred local-binding authorisation gap retained by MS-PROT-088 v1.2 §10; no complete deferred decision.
**Implementation activation:** NONE
**Purpose:** Allow an explicitly approved website connection to finish without requiring routine merchant attendance, while preventing stale requests, provider callbacks and retry machinery from acquiring independent routing authority.

## 1. Governing decision

Merchant Brand Infrastructure SHALL own a durable **Website Connection Request** representing approval to establish one exact Website Binding Selection once all required conditions are satisfied.

The request SHALL remain distinct from:

- namespace assignment or domain-control evidence;
- Website Binding Selection;
- external DNS or certificate effects;
- Storefront publication;
- commercial permission;
- public website availability.

Request acceptance SHALL NOT make a hostname routable.

## 2. Scope and exclusions

This amendment governs request acceptance, inspection, cancellation, supersession and bounded completion of the existing local binding transition.

It does not register DNS or certificate fulfilment operations, authorise external mutations, select providers, purchase or transfer domains, publish compositions, introduce authentication origins, or define cleanup and post-closure intervention authority.

It introduces no general workflow language or universal domain lifecycle.

## 3. Request identity and facts

A Website Connection Request SHALL contain:

- immutable request identity;
- exact Merchant Scope and namespace family;
- exact canonical hostname portfolio and existing storefront;
- platform assignment or purpose-qualified domain-control references;
- expected Website Binding Selection identity, or `NO_SELECTION`;
- predecessor connection-request identity, or `NO_CONNECTION_REQUEST`;
- exact approval and originating logical-command references;
- approving Controller Relationship and acting principal;
- governing contract/release provenance;
- authoritative UTC acceptance instant.

The approved content SHALL be immutable.

The request lifecycle SHALL be:

- `PENDING`: eligible for evaluation, not permission to bypass current checks;
- `SELECTED`: its exact local binding transition committed;
- `CANCELLED`: the Controller stopped the pending request;
- `SUPERSEDED`: a newer request or conflicting binding transition replaced its authority;
- `STOPPED`: completion requires fresh approval because its original authority is no longer sufficient or accepted progression requires intervention.

All states except `PENDING` SHALL be terminal for that request. Terminal transitions SHALL retain attributable reasons and evidence.

At most one request SHALL be `PENDING` per Merchant Scope and namespace family. The latest request identity SHALL remain available for concurrency checks after termination.

A request SHALL neither reserve a custom domain nor establish an exclusive hostname claim.

## 4. Exact contracts and commercial composition

All following identities use the owner prefix `merchant-brand-infrastructure/`:

| Contract | Authority and commercial requirement |
|---|---|
| `inspect-website-connection@1` | Current authorised Controller inspection; no Commercial Entitlement. |
| `request-website-connection@1` | Current Controller approval; applicable existing namespace-use commercial purpose. |
| `cancel-website-connection@1` | Current authorised Controller cancellation; no Commercial Entitlement. |
| `complete-website-connection@1` | Bounded registered completion principal plus the original approval and all current completion predicates; applicable existing namespace-use purpose. |

Platform requests require `USE_PLATFORM_WEBSITE_NAMESPACE`; custom-domain requests require `USE_MERCHANT_CONTROLLED_WEBSITE_DOMAIN`, through the existing v1.1 contracts.

No independent preparation or completion entitlement is introduced. Commercial evaluation SHALL consume the applicable commercial component without requiring the proposed binding to exist already.

The existing `select-website-binding@1` remains Controller-executed. Its identity SHALL NOT be reinterpreted as unrestricted background authority.

## 5. Inspection and non-committing preparation

Inspection requires trusted Merchant Scope and the current ACTIVE Merchant Controller.

For an OPEN account without effective account-wide Suspension, inspection MAY present:

- the proposed exact address portfolio and storefront;
- current binding and request state;
- missing prerequisites;
- replacement consequences;
- whether completion may occur later without another routine confirmation;
- cancellation and disconnection consequences.

For CLOSING accounts without effective Suspension, inspection is limited to existing requests, receipts and governed cancellation or closure needs.

Inspection creates no approval, assignment, reservation, control proof or external-effect permission.

## 6. Request acceptance

Acceptance requires:

1. trusted exact Merchant Scope;
2. an OPEN account without effective account-wide Suspension;
3. the current ACTIVE Merchant Controller;
4. explicit approval of the exact portfolio, storefront, replacement consequences and deferred local completion;
5. current applicable commercial permission;
6. an existing valid platform assignment or adequate purpose-qualified custom-domain control;
7. conformity with the existing hostname portfolio;
8. matching expected binding and latest-request identities;
9. an admitted execution path for the required preparation, or sufficient existing preparation evidence.

A missing external execution contract SHALL NOT be concealed by accepting an apparently executable setup request.

Acceptance SHALL atomically record the new request, supersede any previous pending request in that family, establish the latest-request reference, retain the command receipt and durably establish its completion-evaluation responsibility.

Acceptance SHALL leave the current binding unchanged.

Identical command retries SHALL return the original result. Reusing a command identity with different content SHALL be rejected.

## 7. Preparation authority is not routing authority

An accepted request supplies the merchant’s exact intended binding and approval for bounded local completion.

It MAY be referenced by a separately accepted certificate-preparation contract as evidence of intended binding authority before a current routing selection exists.

This qualification SHALL NOT:

- substitute for current purpose-qualified domain control;
- itself authorise certificate issuance or DNS changes;
- relax fulfilment, credential or provider-readiness requirements;
- permit customer delivery before the existing binding and delivery predicates hold.

A future external contract SHALL explicitly establish its allowed effects, approval requirements, ordering and recovery behaviour. Those requirements SHALL NOT be inferred from this request.

One merchant approval MAY cover several independently governed operations only when their material effects and consequences were fully presented. Missing approval SHALL NOT be supplied by implication.

## 8. Bounded local completion

`complete-website-connection@1` SHALL accept an exact request identity and trusted registered completion context. It SHALL accept no replacement hostname, storefront or approval payload.

Before committing, Merchant Brand Infrastructure SHALL establish:

- the request is the latest request and remains `PENDING`;
- its captured Controller Relationship remains the current ACTIVE Controller Relationship;
- the account remains OPEN without effective account-wide Suspension;
- current applicable commercial permission;
- the exact expected binding selection;
- current assignment or domain-control authority;
- current portfolio, uniqueness, scope and storefront validity;
- the technical conditions required by v1.2 §7 for safe local routing.

Completion SHALL atomically:

1. append the exact approved Website Binding Selection;
2. make it current and update the corresponding authoritative hostname associations;
3. record the completion receipt;
4. mark the request `SELECTED` with the resulting selection identity.

Both the approving Controller Relationship and the bounded executing principal SHALL remain attributable.

Provider callbacks SHALL NOT invoke a privileged bypass or write the selection directly.

Success means local binding selection—not DNS propagation, composition publication or successful public delivery.

## 9. Cancellation and conflicting transitions

Cancellation requires the current ACTIVE Controller of an OPEN or CLOSING account without effective account-wide Suspension.

It SHALL target an exact request identity.

For a pending request, cancellation SHALL atomically establish `CANCELLED` and retain the command receipt. Cancellation SHALL NOT alter the current binding.

If completion already committed, cancellation SHALL return that terminal outcome without undoing it. Removing the resulting binding requires the existing disconnection operation.

The following SHALL invalidate conflicting pending completion authority within their authoritative consistency boundaries:

- a new accepted request: supersedes the preceding pending request;
- direct binding selection: supersedes the pending request in that family;
- disconnection: supersedes pending connection authority, including when already unbound;
- retirement of the exact platform assignment: stops pending requests relying on it.

Cancellation or invalidation SHALL NOT claim that an external in-flight effect was cancelled. Any such effect retains its independently governed recovery responsibility.

## 10. Current authority and waiting

A changed Controller Relationship SHALL prevent completion under the former Controller’s request, even if the same Identity later becomes Controller through a different relationship.

Observed account, commercial or namespace-authority rejection SHALL stop forward completion and require a fresh approved request. Unavailable evidence SHALL remain distinguishable from a proven rejection.

Temporary technical unreadiness MAY leave a request pending under bounded safe progression. Waiting alone SHALL NOT create authority or fabricate failure.

There is no automatic expiry solely because time passes. The merchant SHALL be able to see and cancel pending work. If progression reaches a condition requiring manual intervention, automatic completion SHALL stop; a fresh approval is required to resume through a new request.

A stopped request SHALL NOT revive when a subscription, connection or account condition later improves.

## 11. Background completion contract

The registered BackgroundWorkContract SHALL be:

`merchant-brand-infrastructure/website-connection-completion@1`

Its owner is Merchant Brand Infrastructure; its subject is one exact Website Connection Request; its sole consequential target is `complete-website-connection@1`.

Its trigger is committed request acceptance. Subsequent attempts reevaluate that same responsibility rather than create another connection intention.

The bounded principal SHALL reconstruct the exact request and current authority. It SHALL NOT reuse a historical browser session as proof of current permission.

The downstream completion-command identity SHALL remain stable across WorkAttempts.

The contract SHALL:

- preserve request and governing-contract affinity;
- record required attempt evidence before consequential execution;
- recover an existing completion receipt after lost acknowledgement;
- permit retry only when repetition is established as safe;
- preserve uncertain outcomes until resolved;
- stop pending forward execution when cancellation, supersession or authority loss applies;
- retain only references needed for progression, not raw credentials.

No DNS write, certificate issuance, cleanup or provider substitution is authorised by this background contract.

## 12. Concurrency and recovery

Request acceptance, cancellation, completion and conflicting binding transitions SHALL coordinate through the same owner-controlled consistency boundary wherever their invariants overlap.

A cancellation/completion race SHALL have one authoritative order:

- cancellation first: no completion;
- completion first: cancellation reports the completed result and does not disconnect it.

Cross-merchant hostname uniqueness and assignment-retirement protections remain mandatory at completion.

Lost acknowledgement SHALL be resolved from the retained logical result. A retry SHALL not create another selection.

Recovery SHALL distinguish historical completion from current binding state. Returning an old receipt SHALL never restore a superseded selection.

## 13. Failure and evidence boundaries

Results SHALL distinguish:

- authentication, scope or Controller rejection;
- account-lifecycle rejection;
- commercial rejection;
- missing or conflicting namespace authority;
- stale binding/request expectations;
- missing admitted preparation support;
- technical unreadiness or unavailable evidence;
- request cancellation, supersession or stopping;
- committed completion;
- uncertain commit or integrity failure.

Evidence SHALL retain exact approval, request, transition and completion affinity without storing raw secrets.

Production persistence requires concrete MS-PROT-053 lifecycle qualification for these data scopes. This amendment establishes no universal retention duration or permission to retain complete personal payloads indefinitely.

## 14. Merchant experience and ownership

Main Street SHALL present business-facing outcomes such as “Connecting your website”, “Connected” or “Needs your approval”, without requiring ordinary merchants to understand request states or worker attempts.

Ordinary staff acquire no domain-management permission or additional routine administration.

A salon adding beauty products, a retailer changing its catalogue and a consultant changing services SHALL not need another connection request merely because their business content changes.

This amendment does not change merchant dashboards, Storefront content ownership, MS-PROT-094 composition/rendering, or the independent platform/custom-domain families.

## 15. Trade-offs and falsification boundaries

The additional durable request is justified by work that may outlive the originating interaction. It is not required for an already-ready, directly Controller-executed binding selection.

Accepted costs include request history, cancellation coordination and current-authority reevaluation.

Required falsifiers include:

- duplicate submission and lost acknowledgement;
- cancellation racing completion;
- disconnection before a late callback;
- replacement requests completing out of order;
- Controller transfer;
- grant loss;
- platform assignment retirement;
- competing merchants claiming a hostname;
- provider success without current merchant authority;
- low-software-capacity merchants and ordinary staff.

Implementation SHALL prove these invariants before claiming conformance. Scenario review is not executable proof.

## 16. Acceptance boundary

This amendment supplies bounded request and deferred local-completion authority only.

It does not close external DNS/certificate execution, maintenance, cleanup, reconciliation or concrete data-lifecycle qualification. It does not establish production providers, executable catalogue grants or the complete FREE/BUSINESS/GROWTH manifest.

`MS-PROT-056-V17-DQ-001` remains OPEN. C3 remains IN_PROGRESS. No production activation, programme promotion or push is authorised by this amendment.
