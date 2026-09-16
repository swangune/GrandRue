# MS-PROT-046 — Merchant Publication Authoring & Commercial Access Amendment

**Document ID:** MS-PROT-046
**Version:** 1.5
**Status:** ACCEPTED
**Approved:** 15 September 2026 — explicit manual approval of the complete amendment and stated formalisation scope
**Authority type:** Publication-owned commercial-access classification
**Governed by:** DESIGN-RULES v2.4; DOCUMENT-GOVERNANCE v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** Composite MS-PROT-046 through v1.4 within merchant preparation, observation, authoring and withdrawal commercial classification only.
**Depends on:** MS-PROT-046 v1.1 Decisions 046-K–P and 046-R–S; v1.2 §§1–12, 20–24; v1.3 §§2–3, 10–23; v1.4 §§1–12; composite MS-PROT-027, MS-PROT-053 and MS-PROT-062; MS-PROT-031; MS-PROT-059; MS-PROT-063; MS-PROT-076; MS-PROT-056 v1.7 §§4–5, 10–12 and v1.9 §§4–6, 14, 17.
**Closes:** The bounded merchant Publication commercial-classification gap retained by MS-PROT-046 v1.4 §12; no complete catalogue deferred decision.
**Implementation activation:** NONE
**Purpose:** Make the accepted FREE Publication allocation bindable without treating public observation as administration, fragmenting ordinary authoring into separate paid components, or changing Publication lifecycle authority.

## 1. Governing decision

Publication SHALL establish four exact access contracts:

| Contract | Commercial classification |
|---|---|
| `publication/merchant-preparation-access@1` | No independent Commercial Entitlement for the bounded non-committing preparation in §3. |
| `publication/merchant-observation-access@1` | No Commercial Entitlement for the bounded authorised observation in §4. |
| `publication/merchant-authoring-access@1` | Requires `AUTHOR_AND_PUBLISH_INFORMATION` for the exact operations in §5. |
| `publication/merchant-withdrawal-access@1` | No Commercial Entitlement for the existing withdrawal operation in §6. |

Their owner, identity, revision and meaning SHALL be immutable.

These are commercial-access contracts, not new transport endpoints, actor privileges or mutation lifecycles.

## 2. Exact portfolio and ownership

The contracts apply only to the accepted Publication families:

- `PublishedContent`;
- `Opportunity`;
- `Announcement`.

They do not grant access to arbitrary future Publication families or operations.

Publication retains its object, material, revision, lifecycle and publication-history ownership. Commercial owns entitlement definitions and grant sources. Exposure, Storefront, Enquiry, Notification, Marketing and providers retain their independent authority.

This amendment does not establish subscriber enrolment, Publication subscription management, social distribution, notification delivery, search, bulk export or AI/provider execution.

Publication subscriptions SHALL remain distinct from Commercial subscription plans and agreements.

## 3. Non-committing preparation

`merchant-preparation-access@1` covers preparation and validation of candidate material for the §2 portfolio without committing a Publication object, material revision or lifecycle transition.

It may consume only information the actor is independently authorised to access.

Preparation SHALL NOT:

- establish an authoritative draft;
- save a material revision;
- publish or republish;
- change exposure-window facts;
- establish subscriber intent;
- execute an external effect;
- treat AI or imported material as approved Publication truth.

The exemption applies to preparation itself. It SHALL not waive independently governed source access or authorise AI, ingestion or provider services.

A prepared candidate creates no durable permission for later mutation. The committing operation must perform its current checks.

## 4. Merchant observation

`merchant-observation-access@1` covers authorised merchant-side inspection of existing Publication objects, their current and published material, retained revision/publication history and retained logical-operation results.

Observation requires:

- trusted exact Merchant Scope;
- current actor authority for the requested information;
- applicable account-lifecycle conditions;
- exact object and revision affinity where relevant;
- applicable data-use, retention and Exposure restrictions.

Commercial exemption SHALL not make private material public, grant cross-merchant access, create a general export service or revive disposed data.

This contract does not grant public observation. The existing v1.4 public-source contract remains independently governing.

Receipt recovery SHALL distinguish the historical committed result from current Publication state.

## 5. Protected authoring

`merchant-authoring-access@1` protects exactly:

1. initial authoritative DRAFT establishment;
2. material revision of an existing object;
3. publication of the exact valid current revision;
4. explicit republication from WITHDRAWN.

Changing Publication-owned exposure-window evidence is included where the accepted owner contract treats it as a material revision.

Each operation requires current `AUTHOR_AND_PUBLISH_INFORMATION` permission through its exact Commercial Access Binding, together with all applicable semantic, actor, account, scope, approval and authoritative-state checks.

The commercial grant SHALL NOT itself establish any of those other conditions.

A new committing operation must revalidate commercial permission at the authoritative execution boundary. A prepared candidate or earlier successful check is insufficient.

The grant does not authorise new fields, schemas, object families or operation semantics absent from accepted Publication authority.

## 6. Withdrawal

`merchant-withdrawal-access@1` classifies the existing Publication withdrawal operation as requiring no Commercial Entitlement.

Withdrawal still requires its independently established actor, scope, account, lifecycle, concurrency and execution authority.

The exemption SHALL NOT:

- introduce a new privileged or post-closure actor path;
- erase material or publication history;
- withdraw a Storefront composition;
- disconnect a hostname;
- cancel an Enquiry;
- retract an already-delivered notification or external effect.

Republishing withdrawn material remains protected by §5.

## 7. FREE allocation and exact binding

`AUTHOR_AND_PUBLISH_INFORMATION` SHALL be allocated to FREE, with explicit inclusion in BUSINESS and GROWTH grant sets.

Its target family is `OPERATION_ACCESS`; its exact target is `publication/merchant-authoring-access@1`.

This amendment does not choose its Commercial Entitlement identity or publish a grant.

Catalogue admission SHALL retain the precise binding, source authority and necessary supporting classifications under MS-PROT-056 v1.9. The exemptions in §§3, 4 and 6 SHALL not be interpreted as missing classifications or additional paid requirements.

No runtime branch may derive permission from a tier label.

## 8. Lifecycle, history and concurrency preservation

The existing Publication lifecycle remains `DRAFT`, `PUBLISHED` and `WITHDRAWN`.

Material edits preserve stable object identity and the required immutable revision evidence. Editing a published object SHALL not silently publish its newer revision.

Publication and republication bind the exact current revision through their existing owner contracts. Withdrawal preserves the required historical evidence.

Existing expected-current concurrency, logical-operation idempotency and transaction boundaries remain controlling. This amendment introduces no replacement mechanism.

An authorised retry recovering an already-committed result is observation, not a new authoring effect. It SHALL not be blocked solely because the authoring grant subsequently ended.

A retry that would perform a new effect remains subject to §5.

## 9. Exposure, delivery and supporting services

Publication state, public Exposure, Opportunity actionability and Enquiry participation remain distinct.

Authoring permission SHALL not grant:

- website delivery or namespace use;
- Storefront composition publication;
- Enquiry submission or observation;
- Marketing Campaign execution;
- subscriber or recipient authority;
- Notification or social-provider execution.

Conversely, local Publication does not require a notification or social-distribution provider merely to become a legitimate public candidate under MS-PROT-046 v1.2.

Publication exposure windows retain their existing runtime interpretation. This amendment introduces no scheduled-publication worker or new lifecycle state.

A failed post-commit delivery consequence SHALL not undo Publication truth.

## 10. Commercial loss and failure handling

Loss of authoring permission SHALL block new protected authoring effects. It SHALL not automatically delete, withdraw or rewrite existing Publication objects or history.

Current public observation and website delivery continue to follow their own contracts; retained data or an earlier publication does not bypass those checks.

Results SHALL distinguish commercial rejection, actor or account rejection, invalid material, stale revision, lifecycle rejection, unavailable evidence, technical failure and uncertain execution where applicable.

Technical failure SHALL not be represented as commercial denial or a successful Publication effect.

## 11. Security, AI and retention

Commercial permission does not grant staff privileges, Controller authority, unrestricted background execution or AI approval authority.

AI and imported material remain candidates until the applicable owner-qualified approval and mutation path establishes authoritative content.

The observation exemption SHALL not waive privacy, source ownership or data-use restrictions.

Existing Publication retention and historical-schema requirements remain controlling. This amendment establishes no numeric retention period, universal historical-access right or new production data-lifecycle qualification.

## 12. Trade-offs, conformance and acceptance boundary

One bounded authoring purpose is used because the four protected operations support the same accepted FREE information-publication service. Separate entitlements for saving a draft, revising and publishing would add commercial machinery without an established product distinction.

Preparation, authorised observation and withdrawal are explicitly classified separately to prevent a commercial gate from becoming an unintended inspection or removal barrier.

Required falsifiers include:

- an information-only merchant with no Offering, Booking or Order;
- editing published material without publishing the new revision;
- withdrawal followed by explicit republication;
- lost acknowledgement after successful publication;
- an actor without publication privileges;
- a foreign-scope or private-history read;
- AI-generated or imported unapproved material;
- notification-provider failure;
- a Marketing Campaign attempting to rely solely on FREE Publication permission;
- a low-software-capacity merchant and an ordinary staff member.

These are design-level conformance obligations. Implementation must demonstrate the applicable outcomes before claiming completion.

This amendment supplies commercial classifications only. It does not establish concrete entitlement identities, a complete manifest, production availability or C3 completion.

`MS-PROT-056-V17-DQ-001` remains OPEN. Existing Publication deferred decisions remain unchanged.
