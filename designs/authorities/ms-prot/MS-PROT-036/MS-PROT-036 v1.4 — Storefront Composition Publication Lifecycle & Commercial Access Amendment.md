# MS-PROT-036 — Storefront Composition Publication Lifecycle & Commercial Access Amendment

**Document ID:** MS-PROT-036
**Version:** 1.4
**Status:** ACCEPTED
**Approved:** 14 September 2026 — explicit manual approval of the complete revised amendment and limited formalisation scope
**Authority type:** Storefront-owned presentation lifecycle and access contracts
**Governed by:** DESIGN-RULES v2.3; DOCUMENT-GOVERNANCE v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** Composite MS-PROT-036 within composition preparation, publication selection, withdrawal and commercial classification; v1.3 §4 within current published-composition resolution
**Depends on:** MS-PROT-094 §§3.3, 4, 6, 10–18, 26; MS-PROT-036 v1.3; MS-PROT-031; composite MS-PROT-027, MS-PROT-037, MS-PROT-040 through v1.8, MS-PROT-049, MS-PROT-053, MS-PROT-062 and MS-PROT-063; MS-PROT-057 v1.2 §§5–10, 15–30; MS-PROT-076 §§3–9, 15–17; MS-PROT-056 v1.2, v1.7 §§4–6, 10 and v1.9 §§3–6
**Preserves:** Capability-owned facts, configuration authority, Projection/Exposure, namespace authority, merchant authority and shared rendering
**Implementation activation:** NONE
**Purpose:** Establish a hostname-independent composition-publication lifecycle that supports business evolution without making merchants routine website-release administrators.

## 1. Governing decision

Storefront SHALL own selection of the composition used for its published presentation.

Four exact access contracts SHALL govern these responsibilities:

| Access contract | Commercial classification |
|---|---|
| `storefront/composition-preparation-access@1` | No independent Commercial Entitlement |
| `storefront/composition-publication-access@1` | Requires `PUBLISH_STOREFRONT_COMPOSITION` |
| `storefront/platform-presentation-maintenance-access@1` | No independent Commercial Entitlement |
| `storefront/composition-withdrawal-access@1` | No independent Commercial Entitlement |

Each contract is identified by its exact owner, identifier and revision. Its meaning SHALL remain immutable.

These classifications do not confer actor authority, source access, AI-inference permission, hostname use or website delivery.

`PUBLISH_STOREFRONT_COMPOSITION` SHALL belong to FREE and the explicit BUSINESS and GROWTH supersets. Its exact entitlement identity and Commercial Access Binding remain catalogue work.

## 2. Problem, scope and exclusions

MS-PROT-094 establishes immutable compositions, deterministic validation, central improvement and rollback. This amendment supplies the selection operations and their authority, concurrency and commercial boundaries.

It covers:

- private, non-publishing composition preparation;
- merchant-requested initial publication and replacement;
- platform-managed recomposition and composition rollback;
- merchant-requested withdrawal;
- selection provenance and retry behaviour;
- the handoff from independently authorised business changes to presentation.

It does not establish:

- additional storefronts or their provisioning;
- domain allocation, hostname binding or cutover;
- publication or mutation of capability-owned content;
- public preview links;
- staff website-administration privileges;
- arbitrary website code, templates or a universal presentation language;
- business-capability activation;
- schedules, prices, quotas or an AI-inference portfolio.

“Publication” here means selecting Storefront presentation. It is not publication of an MS-PROT-046 PublishedContent, Opportunity or Announcement.

## 3. Canonical state and ownership

A **Storefront Publication Selection** is an immutable Storefront-owned fact identifying:

- its Merchant Scope and existing storefront identity;
- its own selection identity;
- its predecessor selection identity, or `NO_PREDECESSOR`;
- one exact Storefront Composition Revision, or `NO_COMPOSITION`;
- the originating logical request;
- the execution principal and authority mode;
- the evidence supporting the selection;
- its authoritative commit instant in UTC.

Each storefront SHALL have at most one current selection.

Before its first selection, publication state is `NO_SELECTION`. This state permits no published composition.

`NO_COMPOSITION` means the storefront has no selected composition for public delivery. It does not mean that compositions, content, bindings or business facts have been deleted.

Selection identity, not composition identity alone, SHALL determine concurrency affinity. Selecting revision A, then B, then A creates distinct selection facts; the last selection does not revive the first selection’s authority.

Composition Revisions retain MS-PROT-094’s immutable, presentation-only meaning.

## 4. Preparation and private review

The preparation contract covers non-publishing production and inspection of a candidate arrangement using permitted presentation inputs.

For the initial merchant-facing portfolio, the caller SHALL be the current ACTIVE Merchant Controller, established through trusted execution context for the exact Merchant Scope. The account SHALL be OPEN without an effective account-wide Suspension.

A registered platform-maintenance principal MAY prepare candidates only within Section 7’s maintenance responsibility.

Preparation SHALL:

- use source-owner-authorised material and presentation inputs;
- distinguish material visible to a merchant from material authorised for public presentation;
- identify unresolved inputs or validation failures without fabricating facts;
- preserve merchant-specific composition and the prohibition on templates;
- remain independent of commercial permission to publish or serve the website.

Preparation SHALL NOT change the current selection, publish source content, establish a hostname or create customer-operation authority.

This amendment introduces no durable draft lifecycle. Retained preparation material remains non-published and subject to its data-use and retention authority.

Repeated preparation is non-publishing and may produce different candidates as permitted inputs change. AI-generated candidates remain untrusted until validated by the publication path.

## 5. Merchant publication authority

A merchant-requested selection SHALL require:

1. trusted execution context for the exact Merchant Scope;
2. an OPEN Merchant Account without an effective account-wide Suspension;
3. the current ACTIVE Merchant Controller;
4. current `PUBLISH_STOREFRONT_COMPOSITION` permission;
5. an explicit instruction to publish the exact candidate and its presented consequence;
6. the validation and concurrency conditions below.

The instruction MAY be the same user action that confirms a fully presented candidate. A second ceremonial confirmation is not required.

Approval of an idea, inferred intent, a previous composition or a previous Controller Relationship SHALL NOT authorise publication of the current candidate.

Publication SHALL revalidate the current Controller Relationship. A former Controller’s session or approval cannot authorise a new selection after transfer.

The merchant path MAY:

- make the first selection;
- replace the current composition;
- select a previously published revision after current validation;
- publish again after withdrawal.

These are new selections, not revival of historical permission.

No staff, provider, customer or unrestricted AI publication authority is introduced.

## 6. Publication operation

`PublishStorefrontComposition` SHALL accept:

- a logical request identity;
- trusted Merchant Scope and existing storefront identity;
- the exact immutable candidate revision and its content identity;
- the expected current selection identity, or `NO_SELECTION`;
- merchant-publication or platform-maintenance authority mode;
- the evidence required for that mode;
- trusted execution context.

Before committing, Storefront SHALL establish:

- exact candidate/storefront/scope affinity;
- conformity to MS-PROT-094’s presentation-only boundary;
- deterministic structural and accessibility/conformance validation;
- compatibility with a supported rendering path;
- source-reference and presentation-input authority;
- the authority required for the selected mode;
- the expected current publication selection.

Validation evidence SHALL identify the exact candidate and governing validation and compatibility context. Evidence for another candidate or an invalidated context SHALL NOT suffice.

Revalidation SHALL distinguish changed presentation requirements from independently refreshed business projections. A changed projected price, stock quantity or other business value does not itself require recomposition.

A successful operation SHALL atomically:

1. establish or verify the exact immutable Composition Revision;
2. append the new Publication Selection;
3. make that selection current;
4. record the logical request’s committed result and provenance.

An existing revision identity with different content SHALL be rejected.

Where the exact revision is already current and all authority checks pass, a new logical request SHALL return `ALREADY_CURRENT` without creating another selection. Its result SHALL still be recorded for retry resolution.

Success means the composition was selected. It does not assert successful hostname provisioning, public delivery, source publication or customer-operation availability.

## 7. Platform-managed improvement and rollback

Platform maintenance SHALL use an attributable trusted system principal with registered authority for the exact Storefront maintenance responsibility. Being a system process is insufficient.

The maintenance path SHALL require an existing current selection containing a composition. It SHALL NOT perform initial publication or republish a withdrawn storefront.

For this admitted path, the account SHALL be OPEN without an effective account-wide Suspension. This amendment establishes no suspension-safe maintenance exception.

Platform-managed recomposition SHALL satisfy MS-PROT-094 §15, preserving:

- merchant-authored or approved content;
- authoritative business facts;
- supported brand inputs and explicit presentation preferences;
- customer-operation meaning and authority;
- merchant policy;
- legal, security and accessibility constraints.

The maintenance decision SHALL carry evidence identifying the current selection and the inputs and constraints being preserved. An unsupported or unresolved preservation claim SHALL block automatic publication.

A change requiring alteration of merchant-owned content, explicit presentation choice or business meaning SHALL return to the applicable owner/approval path. It cannot be relabelled “maintenance.”

Composition rollback MAY select a previously published revision only where:

- it is currently compatible and passes required validation;
- it satisfies the preservation conditions above;
- it is conditional on the exact current selection.

Historical publication alone does not establish rollback eligibility.

Maintenance and rollback require no independent Commercial Entitlement. This exemption does not grant paid AI inference, website delivery or namespace use.

Rendering-engine evolution that does not change the Composition Revision remains governed by MS-PROT-094. It does not require a fabricated composition-selection transition.

## 8. Withdrawal operation

`WithdrawStorefrontComposition` SHALL accept:

- a logical request identity;
- trusted Merchant Scope and storefront identity;
- the expected current selection identity, or `NO_SELECTION`;
- an explicit withdrawal instruction;
- trusted execution context.

The caller SHALL be the current ACTIVE Merchant Controller of an OPEN or CLOSING account, without an effective account-wide Suspension.

No Commercial Entitlement is required. Commercial-grant loss SHALL NOT prevent this admitted withdrawal path.

The operation SHALL atomically append a `NO_COMPOSITION` selection, make it current and record the committed request result.

If there is already no selected composition, the operation SHALL return `ALREADY_WITHDRAWN` without another selection transition, while recording its result.

Withdrawal SHALL NOT:

- withdraw capability-owned Publications;
- delete compositions or source facts;
- release hostnames or domain rights;
- cancel customer commitments;
- rewrite Merchant Configuration.

Suspension-specific restrictions and legal or security takedown remain independently governed by MS-PROT-076 and applicable source/Exposure authority. No generic platform takedown privilege is created here.

## 9. Concurrency, retries and lost acknowledgements

Publication, maintenance, rollback and withdrawal SHALL serialize against the same current-selection boundary.

At most one competing transition from an expected selection may commit.

A stale expected selection SHALL produce a conflict. The caller SHALL NOT silently substitute a newer selection and retry a materially different decision.

This prevents delayed maintenance or rollback from overwriting a merchant change or reviving a withdrawn website.

Logical request identity SHALL be unique within the Merchant Scope and storefront. Its recorded payload includes the operation, candidate where present, expected selection and authority mode.

- Same identity and same payload: return the committed result without repeating the effect.
- Same identity and different payload: reject request-identity reuse.
- Commit succeeded but acknowledgement was lost: resolve the recorded result.
- Commit outcome remains unknown: report uncertainty and resolve or retry using the same identity.

Replaying a receipt does not make its selection current again. Responses SHALL distinguish the original committed result from current publication state.

Receipt disclosure remains subject to current actor authority. Historical participation does not grant a former Controller continuing access.

## 10. Delivery and source boundaries

MS-PROT-036 v1.3’s delivery contract SHALL resolve the current Publication Selection.

`NO_SELECTION` or a current `NO_COMPOSITION` SHALL supply no composition for ordinary public website delivery.

Materialised or cached delivery SHALL preserve exact storefront, scope and selection affinity. Cache possession SHALL NOT authorise serving a selection known to have been superseded or withdrawn. If required current-selection authority cannot be established, delivery SHALL fail safely.

A selection does not override:

- source lifecycle or Exposure;
- source-specific commercial permission;
- customer-operation authorisation;
- hostname authority;
- `SERVE_CUSTOMER_WEBSITE`;
- the applicable namespace-use purpose.

A previously published composition or rollback SHALL NOT restore withdrawn source material or obsolete customer permissions.

## 11. Failure and external effects

Results SHALL distinguish:

- inaccessible or wrong-scope target;
- actor-authorisation rejection;
- commercial rejection;
- account-lifecycle or Suspension rejection;
- invalid candidate or evidence;
- compatibility failure;
- selection conflict;
- technical failure or uncertain commit outcome.

Pre-commit rejection SHALL leave the current selection unchanged.

CDN work, materialisation and provider effects SHALL NOT be part of the authoritative selection transaction. Their failure SHALL NOT fabricate a different committed selection or mutate source business truth.

Consumers performing those effects SHALL be repeat-safe and selection-affined. This amendment defines no new domain-event family or durable-work schedule. An implementation requiring either must resolve its governing contract before activation.

Composition or AI failure SHALL preserve the current valid selection. Rollback remains a separately authorised transition, not silent history rewriting.

## 12. Data protection and history

Storefront owns the minimum selection and request evidence needed for:

- current publication resolution;
- immutable revision integrity;
- concurrency and duplicate prevention;
- attribution and authorised recovery.

Evidence SHALL NOT contain unnecessary copies of source content, customer information or AI context.

Composition payloads, source references, actor identifiers and minimal request/selection evidence SHALL receive separately qualified handling and retention under composite MS-PROT-053.

Retaining a historical composition does not authorise rendering it or retaining referenced media indefinitely.

Where disposition removes historical payloads, implementations SHALL preserve only the minimum independently authorised evidence needed to prevent identifier reuse or duplicate execution. If safe historical resolution cannot be established, the affected retry or rollback SHALL fail explicitly.

Concrete retention requirements and disposition support remain mandatory before production persistence activation. No universal duration is established here.

## 13. Architecture and merchant experience

These responsibilities SHALL use the existing shared Storefront architecture and application orchestration. They create no merchant-specific application, universal workflow engine or plan-specific rendering model.

Merchants SHALL interact through business-facing preparation, preview, publish and withdrawal actions. They SHALL NOT need to understand selection identities, concurrency tokens, rendering versions or composition internals.

Deterministic publication and withdrawal SHALL remain usable without AI. Optional AI generation remains separately governed.

Website preparation SHALL NOT require irrelevant Products, Booking, Payment, a gallery or physical premises. Only genuine requirements for the intended presentation or operation may block that scope.

## 14. Business evolution and mixed service/product merchants

### 14.1 No category-driven replacement

Adding a supported line of business SHALL NOT, solely because the business now combines services and products, require:

- another Merchant Account;
- replacement of the existing Merchant Scope;
- a separate website;
- a business-category-specific dashboard;
- a different rendering architecture;
- a template or predefined site family.

Existing services and commitments remain governed by their owners. Product expansion SHALL NOT itself cancel appointments, migrate historical records or remove existing service presentation.

### 14.2 Classify the requested change

The owner-qualified application path SHALL distinguish:

1. **Ordinary business-data change:** creating or updating a product within already-active supported semantics.
2. **Business-configuration change:** introducing or changing operating behaviour requiring the MS-PROT-040 lifecycle.
3. **Presentation change:** changing how already-authorised material or interactions are arranged.

These responsibilities may participate in one merchant journey, but SHALL NOT become one shared mutation authority.

Routine product creation SHALL use the owning capability’s operation. A Configuration Revision SHALL NOT be created merely because a product was added unless the governing configuration authority requires a semantic configuration change.

Likewise, a Composition Revision SHALL NOT be created merely because collection contents changed when the existing presentation arrangement already accommodates those contents.

### 14.3 Clarify material business intent

A request such as “We want to sell beauty products” SHALL NOT be interpreted as automatic authority for every sales channel or operating policy.

Unresolved material choices SHALL be clarified through the relevant capability or reconfiguration responsibility—for example, whether the merchant intends product presentation only or Main Street order execution.

Main Street SHALL NOT infer payment collection, stock policy, collection, shipping or delivery merely from the words “sell products.”

Existing explicit, applicable merchant choices SHALL be reused rather than requested again without need.

### 14.4 Preserve configuration authority

Where new operating behaviour requires Configuration change, composite MS-PROT-040 governs validation, impact review, current approval and activation.

Website publication, a composition candidate or an AI suggestion SHALL NOT activate that behaviour.

The merchant’s supported business model SHALL NOT be narrowed merely because a commercial grant is absent. Commercial permission governs protected use independently under MS-PROT-056.

Product and service presentation remains within FREE’s defined allocation. Applicable Ordering, Payment and Inventory use remains within BUSINESS’s allocation, subject to exact admitted bindings and all other owner requirements.

These allocations establish neither new operational contracts nor executable grants.

### 14.5 Dashboard consequence

The merchant dashboard remains a Main Street operational surface under composite MS-PROT-037 and MS-PROT-049.

It SHALL adapt through applicable capabilities, current work, commercial access and actor authority—not a new “salon-and-retail” application type.

Adding products SHALL NOT grant staff product, stock, payment or configuration privileges automatically.

The website’s composition or publication state SHALL NOT become dashboard business authority. A failed website update SHALL NOT disable independently authorised merchant operations.

### 14.6 Website consequence

The existing website MAY present services and products together using the same merchant identity and shared rendering architecture.

A new section, navigation arrangement or other material presentation change SHALL create a new Composition Revision.

Changes limited to authorised collection contents, prices or stock projections SHALL NOT require recomposition solely because those values changed.

Once operating changes have independently become authoritative, Storefront MAY re-evaluate affected presentation. Automatic publication is permitted only when Section 7’s preservation conditions hold; otherwise Section 5’s merchant-publication path applies.

Approval of business expansion SHALL NOT be treated as blanket approval of every resulting aesthetic or content change.

### 14.7 Handoff and failure isolation

A candidate prepared against proposed or earlier business context SHALL NOT expose an operation merely because that operation appeared in the proposal.

Publication and customer execution SHALL establish their independently required current authority.

If Configuration activation succeeds but website recomposition fails:

- the Configuration activation remains an authoritative fact;
- the current valid composition remains selected;
- rendered material and interactions remain subject to current source and operation authority;
- Main Street SHALL NOT report the website update as completed.

If Configuration activation fails, website preparation or publication SHALL NOT compensate by inventing the intended business behaviour.

No distributed all-or-nothing transaction across Configuration, source capabilities and Storefront is introduced.

## 15. Review, alternatives and falsification

**Vision result: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY.**

The design satisfies the Administrative-Compression and Coordination tests. Main Street manages presentation mechanics while preserving merchant intent and source ownership. Selection identities and evidence remain internal safeguards.

External hosting remains replaceable infrastructure. A provider cannot own Main Street’s composition selection or merchant approval.

| Falsification case | Required result |
|---|---|
| Low-software-capacity salon adds product display | Same account and website; no compulsory commerce setup or technical administration |
| Salon already has applicable product semantics and adds shampoo | Owner product operation; no automatic Configuration Revision |
| Salon introduces online product orders | Required operating choices and configuration authority precede protected order execution |
| Existing arrangement already presents products | New authorised collection contents do not alone require recomposition |
| New product section changes navigation | New Composition Revision |
| Salon receptionist lacks stock privileges | No automatic privilege grant |
| Website update fails after sales configuration activates | Configuration persists; existing valid presentation remains; current operation checks still govern |
| Information publisher has no physical premises or commerce | No invented location, Booking or Payment prerequisite |
| Retailer’s price changes during preparation | No website-owned price snapshot becomes authoritative |
| Platform improves rendering accessibility | No merchant approval for every compatible engine release |
| “Improvement” changes an approved merchant message | Owner approval required |
| Controller transfer precedes publication commit | Former Controller’s operation rejected |
| Two candidates race from one selection | At most one transition commits |
| Withdrawal races delayed rollback | Stale rollback cannot republish |
| Old composition references withdrawn media | Historical publication supplies no access |
| Publication commits but acknowledgement is lost | Same request resolves the original result |
| Composition publishes but hostname provisioning fails | No claim that delivery succeeded |
| AI is unavailable | Deterministic operations and existing valid delivery remain independent |

**Rejected alternatives:** approval for every routine improvement; unrestricted automatic first publication; last-write-wins selection; hostname-specific publication lifecycles; charging for withdrawal; category-driven business replacement; treating website changes as business activation.

**Accepted trade-off:** additional internal selection and evidence handling in exchange for merchant authority, safe automation, failure isolation and recoverable publication.

**Ambiguity review:** business-data mutation, Configuration activation, Composition Revision creation, publication selection, Exposure, commercial permission and delivery remain distinct. Neither a tier label nor a business-category label establishes any of them.

The inspected prototype Storefront code supplies surface projections and a prototype read endpoint. It does not establish this lifecycle. The scenarios support the design conclusion, not an implementation-conformance claim.

## 16. Amendment effect and remaining gates

Composite MS-PROT-036 and MS-PROT-094 previously described publication, validation and rollback without this exact selection-operation contract.

This amendment supplies that bounded lifecycle and the presentation handoff for business evolution. It does not replace MS-PROT-094’s composition freedom, automatic-improvement conditions, shared engines or business-data non-ownership.

It does not amend Configuration approval/activation or dashboard semantic ownership.

`MS-PROT-056-V17-DQ-001` remains OPEN.

Outstanding work includes namespace provisioning and cutover, remaining source/supporting classifications, exact entitlement bindings and the complete catalogue. Prices and quantitative allowances remain separately unresolved.

Acceptance authorises no production activation, C3 completion or programme promotion.

Implementation must demonstrate the authority, isolation, validation, concurrency, retry, withdrawal, source-access, retention, business-evolution and delivery-affinity invariants under IMPLEMENTATION-RULES.
