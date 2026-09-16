# MS-PROT-088 — Platform Website Namespace Allocation & Retirement Amendment

**Document ID:** MS-PROT-088
**Version:** 1.3
**Status:** ACCEPTED
**Approved:** 14 September 2026 — explicit manual approval of the complete allocation-and-retirement authority and limited formalisation scope
**Authority type:** Merchant Brand Infrastructure namespace allocation lifecycle
**Governed by:** DESIGN-RULES v2.4; DOCUMENT-GOVERNANCE v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** MS-PROT-088 v1.0 §6.1 within platform website namespace assignment; supplies the allocation, naming and retirement authority retained by v1.1 §8 and v1.2 §§2, 14
**Preserves:** Existing namespace-use contracts, binding-selection authority, custom-domain rights, commercial allocation and Storefront architecture
**Depends on:** MS-PROT-088 v1.0 §§6, 22–28, 46–58; v1.1 §§1–8; v1.2 §§3–12; MS-PROT-025 §§1–6, 10, 14; MS-PROT-031 §§1–12; composite MS-PROT-053 and MS-PROT-062; MS-PROT-063 §§3–4, 16–24; MS-PROT-072 §§1–9; MS-PROT-073 §§1–6; MS-PROT-076 §§3–9, 16–30; MS-PROT-056 v1.7 §§4–6, 10–12 and v1.9 §§4–6
**Closes:** No complete deferred decision; partially addresses MS-PROT-056-V17-DQ-001
**Implementation activation:** NONE
**Purpose:** Allocate stable platform website addresses without namespace competition, accidental reassignment or coupling allocation to website publication.

## 1. Governing decision

Merchant Brand Infrastructure SHALL own platform website namespace assignment.

The initial portfolio SHALL permit at most one `ASSIGNED` Platform Website Namespace Assignment per Merchant Scope.

An assignment SHALL identify one exact hostname beneath an explicitly approved Main Street-controlled parent namespace.

Allocation SHALL NOT establish a Website Binding Selection, publish a composition or authorise delivery.

Permanent release under this amendment means **retirement**, not return to a reusable naming pool.

## 2. Scope and exclusions

This amendment governs:

- candidate preparation and inspection;
- initial allocation;
- allocation after a previous assignment has been retired;
- stable naming;
- permanent retirement;
- uniqueness, concurrency and retry evidence;
- commercial requirements for these operations.

It does not establish:

- merchant ownership or external portability of platform addresses;
- unsuffixed vanity-name claims;
- temporary reservations or name trading;
- in-place renaming;
- seamless address migration or automatic redirects;
- domain purchasing or registrar transfer;
- DNS or certificate execution;
- business-email or technical-subdomain assignment;
- privileged merchant authentication origins;
- production parent-domain selection;
- prices, quotas or unlimited-use promises.

Assignments created here are qualified for the website purpose only. They SHALL NOT silently become general-purpose brand-namespace allocations.

Admission of additional namespace uses requires their own accepted allocation and retirement composition.

## 3. Platform Website Namespace Assignment

A **Platform Website Namespace Assignment** is a Merchant Brand Infrastructure-owned fact containing:

- assignment identity;
- exact Merchant Scope;
- canonical hostname;
- exact parent namespace reference;
- non-reused allocation discriminator;
- naming-rule revision;
- website-purpose qualification;
- predecessor assignment identity, or `NO_ASSIGNMENT`;
- originating logical request and approval references;
- acting principal and Controller Relationship reference;
- authoritative allocation instant in UTC.

Its lifecycle is:

```text
ASSIGNED → RETIRED
```

`RETIRED` is terminal for that assignment.

The allocation fact and its original content SHALL remain immutable. Retirement SHALL retain its own attributable transition evidence.

Merchant Brand Infrastructure SHALL retain the latest assignment identity needed for concurrency, including when that assignment is retired.

An assignment is not Merchant Scope identity, domain ownership, binding authority or commercial permission.

## 4. Naming and parent authority

A generated hostname SHALL combine:

- readable address wording;
- a platform-generated, permanently non-reused allocation discriminator;
- the approved parent namespace.

Readable wording may be prepared from material authorised for that purpose or supplied for the merchant’s approval. It SHALL NOT become authoritative business-name or legal-identity evidence.

Preparation SHALL be deterministic without requiring live AI. Where readable wording cannot be represented safely, Main Street SHALL offer neutral wording rather than fabricate a business identity.

The exact candidate hostname SHALL be presented before allocation approval.

The discriminator:

- SHALL be independent of customer, credential and Controller information;
- SHALL NOT be treated as a secret or access proof;
- SHALL prevent naming collisions between businesses with equivalent wording;
- SHALL never be reissued by Main Street, including after retirement or abandoned preparation.

Encoding and hostname formatting remain implementation choices, provided the result is valid, canonical, stable and preserves these invariants.

The parent namespace SHALL be explicitly admitted through applicable platform infrastructure authority, with current evidence of Main Street control. A caller-supplied parent string is insufficient.

Changing the parent used for new allocations SHALL NOT migrate existing assignments.

Loss of parent control SHALL block affected allocation or use where current authority cannot be established. This amendment does not claim control over an external party’s later use of a domain.

## 5. Exact operation contracts

The following contracts are owned by Merchant Brand Infrastructure:

| Contract | Responsibility |
|---|---|
| `merchant-brand-infrastructure/inspect-platform-website-namespace@1` | Inspect assignments and receipts; prepare non-assigning candidates |
| `merchant-brand-infrastructure/allocate-platform-website-namespace@1` | Commit an exact approved assignment |
| `merchant-brand-infrastructure/retire-platform-website-namespace@1` | Permanently retire an exact assignment |

Each owner-qualified contract identity and revision SHALL retain immutable meaning.

No separate commercial classification layer or independent setup entitlement is introduced.

## 6. Inspection and candidate preparation

Inspection requires trusted Merchant Scope and the current ACTIVE Merchant Controller.

For an OPEN account without effective account-wide Suspension, inspection MAY return:

- the current or latest assignment;
- authorised retained receipts;
- an exact proposed hostname and its naming/parent references;
- the expected assignment predecessor;
- missing prerequisites or conflicts.

For a CLOSING account without effective account-wide Suspension, inspection is limited to existing assignments and evidence needed for retirement or closure work.

No Commercial Entitlement is required for this bounded inspection.

Candidate preparation SHALL create no namespace assignment, exclusive reservation, binding or provider effect. Technical identity issuance does not confer a right to use the proposed hostname.

Candidate evidence SHALL preserve exact hostname, scope and naming-context affinity. If that evidence cannot be resolved or validated, allocation SHALL require fresh preparation and approval.

If an assignment is already `ASSIGNED`, ordinary preparation SHALL surface that existing address rather than silently propose a replacement.

## 7. Allocation authority and commercial composition

Allocation requires:

1. trusted context for the exact Merchant Scope;
2. an OPEN account without effective account-wide Suspension;
3. the current ACTIVE Merchant Controller;
4. explicit approval of the exact candidate;
5. current `USE_PLATFORM_WEBSITE_NAMESPACE` permission through the existing `merchant-brand-infrastructure/platform-website-namespace-use@1` binding;
6. current parent, naming and uniqueness authority;
7. no existing `ASSIGNED` assignment;
8. the expected assignment predecessor.

The commercial component is evaluated before assignment exists. This SHALL NOT fabricate the other conditions needed for actual namespace use.

Permission may come from any independently valid grant source. It SHALL NOT be inferred from a tier label.

Allocation SHALL NOT be part of Merchant Account establishment or a prerequisite for Standing Free materialisation. It occurs through its own governed operation when an address is requested.

## 8. Allocation operation

`allocate-platform-website-namespace@1` SHALL accept:

- logical request identity;
- trusted Merchant Scope;
- exact candidate and its supporting evidence;
- expected latest assignment identity, or `NO_ASSIGNMENT`;
- explicit candidate approval;
- trusted execution context.

Before committing, the owner SHALL revalidate the §7 conditions and exact candidate affinity.

Success SHALL atomically establish:

- the immutable assignment;
- its `ASSIGNED` state;
- the new latest-assignment reference;
- authoritative uniqueness/non-reuse evidence;
- the logical request’s committed result and provenance.

A concurrent different allocation from the same predecessor SHALL NOT also succeed.

An already-assigned address SHALL NOT be replaced implicitly. The operation SHALL report that an assignment already exists.

Success means **the address is assigned**. It does not mean that the website is connected, published or reachable.

Allocation and binding selection may participate in one explicitly presented merchant journey. Their authoritative effects remain separate: failure to bind SHALL NOT erase a successful allocation or falsely report complete website setup.

## 9. Stability and disconnection

An existing assignment SHALL NOT change automatically because of:

- a business-name or branding change;
- addition of products or services;
- Controller transfer;
- subscription change;
- custom-domain connection;
- provider outage;
- website disconnection.

Ordinary disconnection remains governed by MS-PROT-088 v1.2. It preserves the assignment so the same address can later be selected again under current authority.

No automatic expiry, reclamation timer or inactivity-based reassignment is introduced.

## 10. Permanent retirement

Retirement requires:

- the current ACTIVE Merchant Controller;
- an OPEN or CLOSING account without effective account-wide Suspension;
- the exact current assignment identity;
- the expected current platform-family Website Binding Selection identity, or `NO_SELECTION`;
- explicit approval that the address will not be recoverable through reassignment;
- absence of an active platform-family binding using the assignment.

No Commercial Entitlement is required.

A merchant-facing retirement journey MAY first invoke the separately authorised disconnection operation. It SHALL not hide the permanent consequence or require two ceremonial confirmations when one explicit instruction fully presents both effects.

Retirement SHALL atomically:

1. establish the assignment’s terminal `RETIRED` state;
2. preserve the latest-assignment and non-reuse evidence;
3. record the logical request’s result, principal and UTC retirement instant.

Retirement SHALL NOT delete or mutate custom-domain bindings, compositions, source facts or external obligations.

An unresolved external effect remains an external reconciliation responsibility. Retirement SHALL neither fabricate its cancellation nor permit a late result to restore assignment or routing authority.

This amendment introduces no ordinary Controller operation after the account is CLOSED and no new privileged post-closure cleanup authority. Account closure does not itself manufacture retirement.

## 11. Non-reuse and subsequent allocation

Main Street SHALL NOT assign a retired hostname again:

- to another Merchant Scope;
- to the original Merchant Scope;
- after recreation of the real-world business as a new Merchant Account;
- after a deployment, storage restoration or naming-rule change.

A later allocation for an eligible Merchant Scope SHALL use a new assignment identity, discriminator and hostname.

Such allocation is not recovery of the retired address and creates no redirect from it.

Non-reuse must survive permitted data minimisation. It SHALL NOT depend on retaining all historical merchant names, approval payloads or personal data indefinitely.

Where recovery cannot establish safe allocator continuity or non-reuse, affected allocation SHALL fail explicitly rather than risk reissuing a discriminator.

## 12. Concurrency and retries

Allocation, retirement and binding admission SHALL coordinate wherever their invariants overlap.

A binding selection SHALL NOT commit against a retired assignment. Retirement SHALL NOT commit while a conflicting active binding remains.

The relevant current-assignment, binding and uniqueness checks SHALL be protected against concurrent change—not merely checked beforehand.

Logical mutation-request identity SHALL be scoped to the Merchant Scope and retained with the operation and exact input.

- Identical retry: return the original committed result.
- Same identity with different input: reject identity reuse.
- Lost acknowledgement: resolve the retained receipt or retry identically.
- Uncertain commit: report uncertainty without inventing another allocation or retirement.

An old allocation receipt SHALL NOT revive an assignment that is now retired. Responses SHALL distinguish the historical result from current assignment state.

Receipt disclosure requires current authorised inspection access. Returning a receipt is not new allocation and SHALL not require a still-current commercial grant solely for that reason.

## 13. Failures, resource protection and retention

The operation results SHALL distinguish:

- inaccessible or wrong-scope target;
- actor or account-lifecycle rejection;
- commercial rejection;
- unresolved parent-control authority;
- invalid or unavailable candidate evidence;
- existing-assignment or predecessor conflict;
- hostname/non-reuse conflict;
- active-binding conflict;
- Resource Protection rejection or deferral;
- technical failure, integrity failure or uncertain commit.

Resource Protection remains independent of Commercial. Its admission SHALL not create authority; its rejection SHALL not retire an assignment.

Pre-commit rejection SHALL leave authoritative assignment and binding state unchanged.

Candidate material, assignment facts, receipts, retirement evidence and non-reuse safeguards SHALL receive separately qualified handling under composite MS-PROT-053.

No indefinite retention of full identifying payloads, universal retention duration or claim of anonymisation is established.

Production activation requires qualified lifecycle/disposition support and recovery proof for non-reuse.

## 14. Vision and falsification

**Vision result: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY.**

Readable generated addresses reduce naming administration. Permanent non-reuse prevents Main Street from redirecting old address traffic into another merchant’s assignment.

The trade-offs are:

- generated addresses contain a discriminator rather than being pure vanity names;
- retirement is irreversible;
- changing an address through retirement and new allocation is not a seamless migration;
- Main Street must preserve safe allocator continuity.

Design falsification requires:

| Challenge | Required outcome |
|---|---|
| Two salons use equivalent names | Distinct addresses; no ownership contest over the readable wording |
| Information publisher has no products or premises | No fabricated commerce or location prerequisite |
| Online retailer changes its brand | Existing address remains stable |
| Merchant wording cannot be represented safely | Neutral candidate is presented for approval |
| Ordinary receptionist manages appointments | No allocation or retirement privilege follows |
| Low-software-capacity merchant requests a website | Business-facing candidate review; no namespace-token administration |
| Two allocation requests race | At most one assignment commits |
| Allocation commits but acknowledgement is lost | Retry returns that assignment |
| Binding fails after allocation | Assignment survives; setup is not falsely reported complete |
| Retirement races binding selection | No committed binding to a retired assignment |
| Subscription permission ends | No automatic retirement or reassignment |
| Old allocation receipt is replayed after retirement | No revival |
| Business returns under a new Merchant Account | No inheritance of the former platform address |
| Restore cannot prove allocator continuity | Allocation fails rather than risk reuse |
| A late provider result arrives after retirement | No restoration of assignment or routing authority |

These are design-review scenarios, not executed implementation tests.

## 15. Amendment effect and remaining gates

This amendment supplies the initial website-qualified platform allocation, naming and retirement lifecycle.

It preserves existing namespace-use meanings and the FREE allocation with explicit paid-tier inclusion.

It does not select a production parent domain, provision DNS/certificates, establish executable grants or activate a website.

Additional namespace purposes, seamless migrations and privileged post-closure cleanup require separately accepted authority before use.

MS-PROT-056-V17-DQ-001 remains OPEN pending the complete catalogue and remaining dependencies. Pricing and reserved-service decisions remain unchanged.

Acceptance authorises no production activation, implementation-node promotion or C3 completion.
