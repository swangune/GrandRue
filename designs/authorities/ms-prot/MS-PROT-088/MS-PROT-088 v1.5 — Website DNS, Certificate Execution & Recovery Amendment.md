# MS-PROT-088 — Website DNS, Certificate Execution & Recovery Amendment

**Document ID:** MS-PROT-088
**Version:** 1.5
**Status:** ACCEPTED
**Approved:** 15 September 2026 — explicit manual approval of the complete amendment and stated formalisation scope
**Authority type:** Merchant Brand Infrastructure fulfilment, external execution and recovery contracts
**Governed by:** DESIGN-RULES v2.4; DOCUMENT-GOVERNANCE v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** MS-PROT-088 v1.0 §§9–11, 20–28, 44–58 within bounded website DNS/certificate execution; supplies external-execution composition retained by v1.2 §10, v1.3 §§13–15 and v1.4 §§7, 11, 16. Existing connection, assignment, binding and commercial-use contract identities retain their meanings.
**Depends on:** Composite MS-PROT-088 through v1.4; MS-PROT-025 §§3–6, 14; MS-PROT-031; composite MS-PROT-048, MS-PROT-053 and MS-PROT-062; MS-PROT-063 §§3–4, 12–14, 16–24; MS-PROT-064 §§7–13, 17–20, 23–29, 37; MS-PROT-065 v1.1 §§2–14, 23–37, 45–47; MS-PROT-067 §§6–18; MS-PROT-069 v1.1 §§2–28; MS-PROT-070 §§1–14; MS-PROT-072 §§3–9; MS-PROT-073; MS-PROT-076 §§16–30; MS-PROT-056 v1.9 §§4–6; ADR-012 §§6–8.
**Closes:** Bounded website DNS/certificate execution, maintenance and recovery semantics; no complete catalogue deferred decision.
**Implementation activation:** NONE
**Purpose:** Automate safe website infrastructure work without granting blanket DNS authority, confusing external evidence with local routing authority, or leaving retry and cleanup behaviour undefined.

## 1. Governing decision

Merchant Brand Infrastructure SHALL own the purpose, authorisation, progression and interpretation of website infrastructure work.

DNS and certificate fulfillers SHALL execute only the exact responsibilities admitted through their registered contracts and bindings.

The following SHALL remain distinct:

- approved infrastructure work;
- a Website Connection Request;
- a Website Binding Selection;
- an external effect and its execution certainty;
- current DNS observations;
- current certificate usability;
- public website delivery.

No single infrastructure-success or domain-active flag SHALL replace these facts.

## 2. Scope and exclusions

This amendment covers infrastructure for:

- an existing authorised platform website assignment; or
- a merchant-controlled website namespace with adequate current purpose-qualified control evidence.

It covers HTTPS preparation, approved website DNS cutover, bounded maintenance, evidence receipt, reconciliation and safe cleanup.

It does not establish initial domain-control proof mechanisms, registrar purchase or transfer, business email, blanket managed DNS, nameserver delegation, DNS security-policy changes, additional hostname portfolios, privileged authentication origins or Storefront publication.

Provider selection, concrete proof protocols and production security qualification remain governed admission work. Their absence SHALL not be concealed by assumed readiness.

## 3. Website Infrastructure Plan

A **Website Infrastructure Plan** is an immutable Merchant Brand Infrastructure record defining one bounded set of intended technical effects.

It SHALL identify:

- plan identity and predecessor plan where applicable;
- exact Merchant Scope, namespace family and hostname portfolio;
- originating connection request or current binding selection;
- purpose: `CONNECTION_PREPARATION`, `WEBSITE_CUTOVER`, `MAINTENANCE` or `CLEANUP`;
- exact changes or the bounded certificate-challenge form allowed by §7;
- required ordering and expected resource state;
- approval or registered maintenance/cleanup authority;
- exact fulfilment routing and governing contract provenance;
- logical acceptance identity, principal attribution and UTC acceptance instant.

The approved content SHALL not be edited in place.

A separate, attributable progression record SHALL establish whether further effects are permitted, stopped or complete. Stopping a plan SHALL not rewrite its content or erase unresolved effects.

At most one plan may authorise forward changes to an overlapping resource set at a time. A replacement SHALL stop its predecessor’s future dispatch authority.

A conflicting replacement SHALL not proceed while an earlier effect could still change the same resources, unless authoritative evidence establishes that the overlap is safe.

## 4. Exact application contracts

All following identities use `merchant-brand-infrastructure/`:

| Contract | Responsibility |
|---|---|
| `inspect-website-infrastructure@1` | Inspect authorised state, prepare a non-executing plan and explain prerequisites. |
| `approve-website-infrastructure-plan@1` | Accept exact Controller-approved infrastructure work. |
| `cancel-website-infrastructure-plan@1` | Stop future dispatch under an exact plan. |
| `execute-website-dns-change@1` | Execute an admitted, exact DNS effect. |
| `prepare-website-https@1` | Prepare and install usable HTTPS infrastructure without creating routing authority. |
| `maintain-website-infrastructure@1` | Establish bounded maintenance work for the exact current binding. |
| `cleanup-website-infrastructure@1` | Execute the narrowly authorised cleanup in §12. |
| `receive-website-infrastructure-evidence@1` | Authenticate, correlate and retain infrastructure evidence. |
| `reconcile-website-infrastructure-effect@1` | Resolve an identified historical uncertainty or discrepancy. |

These contracts SHALL not be interpreted as arbitrary provider-operation dispatch.

## 5. Actor and commercial authority

Controller inspection, approval and cancellation require trusted exact Merchant Scope and the current ACTIVE Merchant Controller.

New preparation and cutover require an OPEN account without effective account-wide Suspension. Controller cancellation and cleanup may also operate during CLOSING, subject to their bounded conditions.

Preparation, cutover and maintenance require the applicable existing namespace-use commercial purpose:

- platform namespace: `USE_PLATFORM_WEBSITE_NAMESPACE`;
- custom domain: `USE_MERCHANT_CONTROLLED_WEBSITE_DOMAIN`.

No independent DNS, certificate-preparation or ordinary certificate-renewal entitlement is introduced.

Inspection, cancellation, evidence receipt, historical reconciliation and qualified cleanup require no Commercial Entitlement. This exemption SHALL not authorise new website use.

Background execution requires a registered principal bounded to the exact plan, effect or maintenance responsibility. Staff, customers, AI and provider credentials acquire no independent approval authority.

Maintenance does not require the original connecting Controller to remain Controller. It preserves current service under current authority rather than completing an old Controller’s unfinished decision.

## 6. Fulfilment roles and routing

Two exact Merchant Brand Infrastructure fulfilment roles SHALL govern automated execution:

| Role | Required obligations |
|---|---|
| `website-dns-record-management@1` | Observe qualified records; apply an exact conditional change; identify and inspect the resulting effect. |
| `website-certificate-management@1` | Prepare certificate material for authorised hostnames; validate and install it; replace it safely; remove unused managed material; identify and inspect effects. |

The owner-qualified role prefix is `merchant-brand-infrastructure/`.

For these responsibilities:

- platform-assignment DNS uses `PLATFORM_ROUTED` fulfilment;
- automated management through a merchant’s DNS provider context uses `MERCHANT_ROUTED` fulfilment;
- Main Street-supplied certificate infrastructure uses `PLATFORM_ROUTED` fulfilment.

Merchant routing SHALL retain the exact configuration-affined binding required by MS-PROT-048 v1.2. Platform routing SHALL retain the exact serving-affined binding required by v1.5.

Required role, context and obligation support SHALL be registered and validated under composite MS-PROT-048 before the path is admitted. This amendment introduces no alternative applicability language or mutable global provider pointer.

A plan SHALL not select or change a provider merely by naming one. A provider change requires its existing routing-authority process.

Provider adoption remains subject to the open-library, open-software, then justified paid-service evaluation order.

## 7. DNS change boundaries

Every automated DNS effect SHALL identify:

- exact provider connection and zone/resource context;
- exact record owner name and type;
- intended value change;
- expected existing state or provider concurrency token;
- the Main Street responsibility requiring the change.

Changes SHALL preserve unrelated records and unrelated values within a shared record set.

A certificate challenge may use a bounded generated value rather than a value known at initial approval only when:

- the approved plan identifies the exact permitted challenge-name/type boundary;
- the value comes from the authenticated, correlated certificate operation;
- the change is limited to adding or removing that operation’s challenge value;
- the exact instantiated payload and expected state are durably recorded before dispatch.

This is not a general expression language or authority for provider-supplied arbitrary DNS changes.

Automatic mutation requires evidence that concurrent changes cannot be silently overwritten. A read followed by an unconditional write is insufficient where another actor may modify the resource.

An adapter that cannot satisfy the required protection SHALL report the automated path unsupported. Main Street SHALL not weaken the invariant to accommodate it.

Changes to nameserver delegation, unrelated email records, security policy or another service are outside this contract.

## 8. Approval and planned manual DNS work

A material website cutover SHALL require explicit Controller approval of the destination and the existing website consequences.

Approval MAY cover the connection request and associated plans in one merchant interaction when all material effects are presented.

Technical challenge values and routine certificate administration SHALL not require ceremonial individual confirmations within the approved bounds.

When safe automation is unavailable, Main Street MAY provide the minimum exact manual DNS instructions and observe their results through an admitted manual path.

A planned manual DNS step is external preparation—not, by itself, exceptional operational intervention under v1.4 §10. The request may remain pending while that planned step is performed.

An actual intervention-required condition retains v1.4’s stopping and fresh-approval requirements.

A merchant’s “done” acknowledgement SHALL not prove DNS state or certificate readiness. Required evidence must still be established.

Manual execution SHALL not be reported as a Main Street provider effect.

## 9. Preparation and cutover ordering

For a new connection:

1. Current namespace/control authority and exact approval must exist.
2. HTTPS preparation SHALL establish the required certificate and endpoint readiness without redirecting existing website traffic.
3. Local binding completion SHALL occur only through v1.4’s bounded completion contract or the separately authorised direct selection contract.
4. Website DNS cutover SHALL require the exact resulting selection to remain current.

Pre-binding preparation MAY install technical material, but SHALL not permit customer content delivery without the existing binding and delivery predicates.

A preparation path requiring premature replacement of the existing website is not admitted by this amendment. It must use another conforming proof/preparation path or report the limitation.

Local binding readiness SHALL not require public DNS convergence merely to authorise the subsequent DNS cutover. It SHALL require safe TLS, endpoint isolation and the other existing local-routing predicates.

If DNS already points to Main Street, local selection may make the approved route usable immediately; that possibility SHALL be included in the approval consequences.

Partial DNS progress SHALL remain explicit. Neither zero downtime nor instantaneous public convergence is guaranteed.

## 10. Certificate preparation and usability

Certificate preparation SHALL be affined to the exact authorised hostname set and connection request or current binding.

Before certificate material becomes usable for the target, the fulfiller SHALL establish the required hostname coverage, validity, trusted chain, key association, security usability and deployment affinity.

Certificate possession SHALL not establish namespace authority.

Private keys and provider credentials SHALL remain behind MS-PROT-067’s security boundary. Work records, plans, events, audit, logs and AI context SHALL contain only appropriate references and non-secret evidence.

Installing or renewing certificate material SHALL not create, replace or restore a Website Binding Selection.

Replacement SHALL preserve a currently usable certificate until the replacement is validated and usable, unless security authority requires the old material to become unusable sooner.

Failure of certificate-management access SHALL not automatically make an independently valid deployed certificate unusable. Conversely, management success SHALL not conceal an expired, revoked, mismatched or otherwise unusable serving certificate.

## 11. Maintenance

Registered maintenance may preserve the exact current hostname portfolio, Merchant Scope, storefront and alias roles without another routine Controller approval.

It may:

- renew or replace certificate material;
- refresh bounded challenge records;
- update already-managed website records to another admitted Main Street endpoint serving the same authorised target.

It SHALL not:

- replace an external website without approval;
- expand the hostname portfolio;
- change storefront or alias meaning;
- change registrar or merchant provider routing;
- overwrite externally modified records;
- establish a new binding or revive a disconnected one.

Maintenance requires current account, namespace-use, control, binding, security and applicable provider authority.

A business-content change—such as a salon adding products—does not itself require infrastructure maintenance or reconnection.

## 12. Cancellation, disconnection and cleanup

Plan cancellation SHALL atomically stop further dispatch admission and retain its receipt. It SHALL not undo a committed local binding.

Disconnection, assignment retirement and incompatible binding replacement SHALL also stop conflicting future plan execution.

Operations already admitted for external dispatch may complete later. Main Street SHALL preserve their uncertainty and reconciliation responsibility; it SHALL not claim instantaneous external cancellation.

Automatic residual cleanup is limited to:

- exact challenge values created for the plan and no longer required;
- unused managed certificate material with no remaining authorised dependency.

Website traffic-record removal requires an exact Controller-approved cleanup plan. It SHALL not occur merely because a commercial grant ended.

All cleanup requires identifiable Main Street provenance, current authority to access the resource, matching expected state and absence of conflicting dependencies.

If a record was changed or repurposed, automatic deletion SHALL stop. Shared technical resources SHALL not be removed while another authorised use depends on them.

Cleanup SHALL not restore old third-party records automatically. Such restoration requires a separately approved exact change.

Local disconnection SHALL not wait for cleanup. Unbound hostnames continue to fail safely.

## 13. External-effect identity and dispatch

Each duplicate-sensitive external effect SHALL have a durable identity distinct from its plan, application command and WorkAttempt.

Before dispatch, Main Street SHALL durably retain:

- exact effect identity and immutable payload;
- plan and subject affinity;
- expected resource state;
- historical fulfilment binding, provider and connection;
- provider idempotency/correlation identity where supported;
- execution authority and attempt evidence.

Dispatch admission SHALL revalidate current plan permission, stage, account, commercial requirements where applicable, namespace/control, binding, security and exact obligation readiness.

A plan cancellation and dispatch admission SHALL have one authoritative local order. There is no distributed transaction promise between that order and provider execution.

No new dispatch may be admitted after cancellation. Already-admitted effects remain subject to late-result handling.

An uncertain overlapping effect SHALL not be bypassed by inventing another provider request, switching provider or allocating another WorkAttempt.

## 14. Evidence and reconciliation

`receive-website-infrastructure-evidence@1` SHALL validate the source and correlate evidence to the exact historical effect and provider context.

Verified origin is not permission to mutate arbitrary state. Provider-native status strings SHALL be interpreted through the registered evidence contract.

The owner-qualified ReconciliationContract is:

`merchant-brand-infrastructure/website-infrastructure-effect-reconciliation@1`

Its subject is one exact effect and its historical execution affinity.

Admissible evidence comprises retained Main Street effect records, authenticated provider queries or callbacks, and qualified technical observations appropriate to the question being resolved.

Reconciliation SHALL distinguish:

- `KNOWN_EXECUTED`;
- `KNOWN_NOT_EXECUTED`;
- `EXECUTION_UNCERTAIN`.

Observing the desired DNS state may establish current technical readiness. It does not, by itself, prove that Main Street performed the change or owns the record for cleanup.

Conflicting evidence SHALL not be resolved by “last callback wins”.

Reconciliation SHALL prefer non-mutating evidence acquisition. A subsequent write is a separately authorised effect, not an observation.

Completion requires sufficient evidence and the corresponding owner interpretation to be committed. Age, retry exhaustion or operator preference SHALL not fabricate certainty.

## 15. Residual authority and intervention

Historical evidence receipt and non-mutating reconciliation may continue for exact existing effects during OPEN or CLOSING, including where new commercial use is unavailable.

Effective Suspension does not itself authorise these operations. Only the registered, narrowly scoped historical-evidence path is permitted, and current security restrictions may prohibit it.

Qualified residual cleanup requires the conditions in §12 and an OPEN or CLOSING account without effective account-wide Suspension.

Residual credential use must be explicitly permitted for the exact historical responsibility. Revoked or compromised credentials SHALL not be used merely to finish cleanup.

This amendment introduces no post-CLOSED execution or privileged bypass. Closure must account for outstanding responsibilities under MS-PROT-076 rather than declaring them complete.

Insufficient evidence, conflicting ownership, unsupported safe mutation or exhausted safe recovery SHALL produce an explicit intervention requirement.

Intervention may inspect and reacquire admissible evidence or submit a newly authorised plan through the normal contracts. It SHALL not force success, edit authoritative tables directly or resume stopped authority by changing a status field.

## 16. Durable progression

Two BackgroundWorkContracts are established:

- `merchant-brand-infrastructure/website-infrastructure-plan-progression@1`;
- `merchant-brand-infrastructure/website-infrastructure-effect-reconciliation-work@1`.

The first is triggered by committed plan acceptance or an authorised maintenance/cleanup responsibility. It evaluates only the next permitted effect for that exact plan.

The second is triggered by an identified uncertain or discrepant effect and invokes the reconciliation contract in §14.

Both SHALL preserve exact semantic, subject and historical provider affinity. Attempts SHALL reconstruct the same downstream logical intent.

Due execution means reevaluation, not irrevocable permission. Overdue work SHALL not bypass stage or current-authority checks.

Retry is permitted only where independently proven safe. Exhaustion preserves the underlying responsibility and yields the appropriate stopped, uncertain or intervention-required outcome.

Routine renewal scheduling SHALL derive from the admitted certificate contract and current certificate evidence. This amendment establishes no universal renewal interval or numerical retry schedule.

## 17. Failures, audit and data lifecycle

Results SHALL distinguish authority rejection, unsupported safe execution, stale resource state, provider unreadiness, unavailable evidence, known rejection, partial progress, execution uncertainty, cancellation and intervention-required outcomes.

No `DEGRADED` result authorises a write that cannot satisfy its invariants.

Required plan approval, cancellation, dispatch and reconciliation attribution SHALL be durable. Audit propagation may follow through a durable post-commit path; it SHALL not be silently lost.

Audit evidence SHALL identify exact subjects, actions, authority and outcomes without raw credentials, unnecessary full DNS-zone copies or unrelated personal data.

Concrete production lifecycle contracts are required for plans, effects, challenge evidence, certificates, security references and reconciliation records. No universal retention period or indefinite full-payload retention is established.

Resource-protection admission remains separate from commercial permission and semantic authority.

## 18. Conformance and acceptance boundary

The design SHALL be challenged against:

- preservation of existing website and email infrastructure;
- planned manual DNS work;
- conflicting external record edits;
- partial multi-record progress;
- certificate preparation before local binding;
- cancellation before and after dispatch;
- disconnection before a late provider result;
- Controller transfer before pending cutover;
- downgrade during preparation or maintenance;
- credential revocation;
- provider changes during historical reconciliation;
- certificate-management outage with a still-valid deployed certificate;
- shared-resource cleanup;
- lost acknowledgements and duplicate callbacks;
- materially different merchant businesses and low-software-capacity users.

Production admission requires exact registered fulfilment/evidence contracts, routing and credentials, lifecycle qualification, executable support, recovery proof and applicable implementation verification.

Acceptance does not select a vendor, activate website infrastructure, publish the commercial catalogue, complete C3 or authorise a push.

`MS-PROT-056-V17-DQ-001` remains OPEN.
