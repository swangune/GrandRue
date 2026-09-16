# MS-PROT-080 v1.4 — Workforce Compensation & Payroll Commercial Access Contracts Amendment

**Document ID:** MS-PROT-080  
**Version:** 1.4  
**Status:** ACCEPTED  
**Approved:** 15 September 2026 by explicit manual approval in ChatGPT  
**Design node:** `MS-PROT-080-V11-DQ-016 — Commercial packaging and entitlement`  
**Authority type:** Workforce Compensation / Payroll commercial-access classification amendment  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-080 through v1.3 within commercial-access classification only  
**Depends on:** Composite MS-PROT-056 through v1.9; composite MS-PROT-055; composite MS-PROT-062; composite MS-PROT-063; composite MS-PROT-069; composite MS-PROT-074; composite MS-PROT-075; composite MS-PROT-080 through v1.3; MS-PROT-081 v1.4 where pre-existing Scheduled Work Commitment resolution produces compensation evidence  
**Preserves:** Workforce Compensation, Payroll, Money, Payment, Regulatory Administration, Workforce Scheduling, Notification, Exposure, Identity, Membership and provider ownership boundaries  
**Resolves:** `MS-PROT-080-V11-DQ-016`  
**Does not resolve:** `MS-PROT-056-V17-DQ-001`; any MS-PROT-091 commercial classification; any other unresolved MS-PROT-080 DQ  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY

**Product identity note:** inherited `Main Street` references identify the product currently named **GrandRue**. Stable `MS-*` and `mainstreet.*` identifiers remain unchanged pending separately governed migration.

---

# 1. Governing Decision

Composite MS-PROT-080 SHALL define exactly two initially protected commercial purposes:

```text
MAINTAIN_WORKFORCE_COMPENSATION_TERMS

ADMINISTER_WORKFORCE_COMPENSATION
```

They SHALL be represented through the following owner-qualified access contracts.

| Exact access-contract identity | Commercial classification | Standard allocation |
|---|---|---|
| `workforce-compensation/terms-authoring-access@1` | `MAINTAIN_WORKFORCE_COMPENSATION_TERMS` | BUSINESS + GROWTH |
| `workforce-compensation/compensation-administration-access@1` | `ADMINISTER_WORKFORCE_COMPENSATION` | BUSINESS + GROWTH |
| `workforce-compensation/merchant-preparation-access@1` | No Commercial Entitlement | plan-independent bounded exemption |
| `workforce-compensation/merchant-observation-access@1` | No Commercial Entitlement | plan-independent bounded exemption |
| `workforce-compensation/personal-observation-access@1` | No Commercial Entitlement | plan-independent bounded exemption |
| `workforce-compensation/existing-terms-proposal-resolution-access@1` | No Commercial Entitlement | plan-independent bounded exemption |
| `workforce-compensation/existing-obligation-resolution-access@1` | No Commercial Entitlement | plan-independent bounded residual access |

For the two protected contracts, the Commercial binding target family is:

```text
OPERATION_ACCESS
```

FREE SHALL NOT receive either protected purpose through the standard plan catalogue.

This amendment does **not** create the concrete `CommercialEntitlementIdentity` values.

Those remain Commercial-owned manifest work under:

```text
MS-PROT-056-V17-DQ-001
```

An explicit no-entitlement classification means only:

> no Commercial Entitlement is required for that exact bounded owner-qualified operation.

It does not waive any other applicable semantic, security, privacy, jurisdictional, operational or provider requirement.

Missing classification is not an exemption.

---

# 2. Ownership Boundary

Composite MS-PROT-080 continues to own the meaning of:

```text
CompensationRelationship
CompensationTermsRevision
CompensationTermsEvidence
CompensationAmount
Compensation approval
Jurisdiction Pay Treatment affinity
PayrollInputSnapshot
PayrollCalculationSnapshot
Payroll approval
Compensation Ledger facts
Compensation Document provenance
Payroll / Non-Payroll compensation consequences
```

Commercial continues to own:

```text
CommercialEntitlementIdentity
CommercialEntitlementDefinition
Commercial Access Binding
grant provenance
effective commercial permission
plan-revision grant sets
catalogue publication
```

This amendment classifies commercial access to existing MS-PROT-080 semantics.

It does not create:

```text
new compensation lifecycle states
new Payroll lifecycle states
new payment authority
new filing authority
new jurisdiction rules
new document semantics
new authentication mechanisms
new Payee representative authority
new provider readiness
new worker authority
```

---

# 3. No Tier-Owned Compensation Semantics

Rejected:

```text
if plan == BUSINESS
    enable payroll
```

Rejected:

```text
BUSINESS_PAYROLL
    → all Workforce Compensation behaviour
```

Rejected:

```text
workforce-compensation/*
```

Canonical:

```text
exact MS-PROT-080 access contract
        ↓
commercial classification
        ↓
exact Commercial Access Binding
        ↓
effective grant source
        ↓
runtime composition with
all independent predicates
```

BUSINESS and GROWTH are packaging.

They are not Compensation or Payroll authorities.

A merchant possessing a BUSINESS grant does not thereby prove:

```text
a Compensation Relationship exists
a Payee is eligible
a jurisdiction treatment is resolved
a calculation is ready
a Payroll snapshot is current
a regulatory obligation may be filed
a payment may be executed
a document may be exposed
```

---

# 4. Merchant Preparation

## 4.1 Contract

```text
workforce-compensation/merchant-preparation-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for bounded, non-authoritative preparation.

## 4.2 Permitted preparation

Where otherwise authorised, preparation MAY include:

```text
structuring candidate Compensation terms
from merchant-supplied facts

validating candidate values

identifying missing required facts

explaining existing supported compensation mechanisms

preparing a candidate relationship/terms proposal

previewing the business consequences
of candidate terms
```

This preserves GrandRue onboarding and business-to-software translation without requiring a merchant to buy a plan merely to understand what configuration would be proposed.

## 4.3 Preparation boundary

Preparation MUST NOT itself:

```text
establish a CompensationRelationship
establish a CompensationTermsRevision
make candidate terms authoritative
establish a CompensationAmount
approve an amount payable
create a PayrollInputSnapshot
create or approve a PayrollCalculationSnapshot
dispatch regulatory execution
execute Payee payment
assert funds settlement
generate a legally operative compensation agreement
create a required pay document
```

AI assistance, document extraction, provider retrieval or jurisdiction-specific determination remains separately governed.

Candidate preparation is not durable permission for a later authoritative operation.

Commercial permission must be re-evaluated when a protected effect is requested.

---

# 5. Protected Compensation-Terms Authoring

## 5.1 Contract

```text
workforce-compensation/terms-authoring-access@1
```

requires:

```text
MAINTAIN_WORKFORCE_COMPENSATION_TERMS
```

## 5.2 Bounded scope

Where otherwise semantically valid, this contract covers authoritative activity that establishes or materially changes the prospective compensation relationship, including:

```text
establishing a CompensationRelationship

establishing an initial
CompensationTermsRevision

establishing a material prospective
Compensation Terms variation

authoritatively approving/adopting
candidate structured terms

issuing a durable compensation agreement
intended to participate in establishment
of governing Compensation Terms

Payee acceptance/acknowledgement
where that action establishes accepted
Compensation Terms
```

The commercial purpose protects creation or material alteration of future compensation capability.

## 5.3 Agreement acceptance

A compensation agreement generated while protected access existed does not create perpetual future authority to accept it after the relevant commercial permission ends.

Where Payee acceptance would establish authoritative Compensation Terms:

```text
Payee ACCEPT / ACKNOWLEDGE
        ↓
terms-authoring-access@1
        ↓
MAINTAIN_WORKFORCE_COMPENSATION_TERMS
```

must be satisfied for the Merchant Scope at the authoritative acceptance boundary.

The Payee does not require a personal GrandRue subscription.

The merchant's commercial permission is the relevant commercial predicate.

## 5.4 Exclusions

This purpose does not grant or establish:

```text
Merchant Membership
Workforce Scheduling Arrangement
professional qualification
right-to-work authority
Jurisdiction Pay Treatment
Payroll treatment
work evidence
Compensation Amount
Payment
regulatory acceptance
```

Those remain independently governed.

---

# 6. Existing Terms-Proposal Resolution

## 6.1 Contract

```text
workforce-compensation/existing-terms-proposal-resolution-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for bounded resolution of an exact already-existing unaccepted compensation-terms proposal.

## 6.2 Permitted resolution

Where existing MS-PROT-080 semantics permit them, this classification may cover:

```text
Payee rejection
Payee dispute
merchant withdrawal
explicit abandonment
passive expiry becoming effective
```

of the exact existing proposal.

A merchant or Payee must not be forced to retain an unwanted pending compensation proposal merely because the commercial grant ended.

## 6.3 Not acceptance or variation

The exemption MUST NOT authorise:

```text
accepting the proposal

changing its pay rate

changing its compensation mechanism

changing future effective terms

replacing it with a materially different proposal

using rejection followed by silent mutation
as a substitute for new terms authoring
```

A materially different proposal or authoritative acceptance returns to Section 5.

Passive expiry, where already governed by owner semantics, does not require a commercial grant merely to become true.

This amendment does not create an expiry mechanism where none exists.

---

# 7. Protected Compensation Administration

## 7.1 Contract

```text
workforce-compensation/compensation-administration-access@1
```

requires:

```text
ADMINISTER_WORKFORCE_COMPENSATION
```

unless the exact operation qualifies for the residual classification in Section 8.

## 7.2 Bounded scope

For new compensation activity, the protected purpose includes, where otherwise valid under composite MS-PROT-080:

```text
establishing or recording
a new CompensationAmount

deriving a CompensationAmount
from current governed evidence

merchant approval of a new
amount as payable

establishing applicable
PayrollInputSnapshot evidence

performing deterministic
Payroll calculation

merchant approval of a
PayrollCalculationSnapshot

initiating applicable
Payroll or Non-Payroll
regulatory execution

initiating applicable
Payee payment/remittance execution

recording an external payment
as compensation evidence

generating applicable
Payroll or Non-Payroll
compensation documents
```

The list classifies existing semantic operations.

It does not create an operation absent from current authority.

## 7.3 Payroll and Non-Payroll

The commercial classification does not depend on whether the exact relationship is:

```text
PAYROLL

or

NON_PAYROLL
```

Both are routes within Workforce Compensation.

A merchant SHALL NOT be moved between standard tiers merely because jurisdiction-qualified treatment chooses one route rather than the other.

## 7.4 No compensation inference from commercial permission

Possession of:

```text
ADMINISTER_WORKFORCE_COMPENSATION
```

does not establish:

```text
amount owed
rate
payable time
employment status
Payroll treatment
deduction
regulatory liability
payment readiness
```

All source predicates remain independently authoritative.

---

# 8. Existing Compensation-Obligation Resolution

## 8.1 Contract

```text
workforce-compensation/existing-obligation-resolution-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

only where the operation satisfies the exact residual-resolution predicate below.

This is the MS-PROT-080 owner classification required by the residual-access rule in MS-PROT-056.

## 8.2 Residual-resolution predicate

An operation qualifies only when all applicable conditions are established:

```text
1. an exact bounded authoritative
   compensation/source commitment
   already exists;

2. the anchor was validly established
   while its required commercial
   permission existed,
   OR an accepted source owner explicitly
   classifies its continued resolution
   as residual;

3. required CompensationRelationship
   and CompensationTermsRevision affinity
   already exists where applicable;

4. the requested operation's sole business
   purpose is to fulfil, correct, reconcile,
   document, close or recover that exact
   pre-existing obligation;

5. any newly created fact is necessary
   evidence or consequence of resolving
   that existing obligation;

6. the operation does not create materially
   new prospective compensation capacity;

7. exact source and historical provenance
   remain preserved.
```

If these conditions cannot be established:

```text
existing-obligation-resolution-access@1
```

MUST NOT be assumed.

Missing evidence fails closed into the protected classification or another applicable failure result.

## 8.3 Qualifying residual anchors

Examples of exact anchors may include, where already governed:

```text
an established CompensationAmount

an approved payable CompensationAmount

an exact bounded Payroll cycle/input snapshot

an exact PayrollCalculationSnapshot

an existing Regulatory Obligation

an existing Payee-payment/remittance obligation

an existing required compensation-document obligation

an exact pre-existing ScheduledWorkCommitment
whose continued evidence capture is already
commercially residual under MS-PROT-081 v1.4
and whose Compensation Relationship / Terms
affinity was already established
```

Another capability's source fact may qualify only where that source owner's accepted authority explicitly establishes the relevant residual continuation and supplies the exact affinity required by MS-PROT-080.

A generic:

```text
CompensationRelationship
```

or:

```text
CompensationTermsRevision
```

by itself is **not** an indefinite residual anchor for all future compensation.

## 8.4 Permitted residual operations

For a qualifying exact obligation, residual resolution MAY include, where otherwise valid:

```text
establishing the amount required
to resolve the qualifying source commitment

approving the qualifying amount payable

calculating/recalculating
the exact bounded Payroll result

approving the exact bounded
PayrollCalculationSnapshot

performing required filing/reporting

performing required remittance

performing required Payee payment

recording external payment evidence

reconciling provider/authority outcomes

resolving uncertain external execution

performing an authorised correction
or replacement

generating/regenerating a required
payslip/payment/deduction document

delivering the required document

recovering the exact committed result
after acknowledgement loss
```

Every operation retains its existing source semantics, authorisation, jurisdiction, readiness, idempotency, correction and reconciliation rules.

## 8.5 Why approval may remain residual

Commercial loss MUST NOT force a merchant to leave already-earned or otherwise already-bounded compensation unresolved merely because the final approval step occurs later.

Therefore, where an exact pre-existing residual anchor proves that the compensation activity was already validly committed:

```text
amount approval
Payroll approval
filing
payment
document issuance
```

may remain residual-resolution operations.

Approval remains authoritative business approval.

The exemption changes only its Commercial predicate.

## 8.6 Workforce Scheduling composition

Example:

```text
ScheduledWorkCommitment C17
established while BUSINESS existed
        ↓
merchant later downgrades
        ↓
MS-PROT-081 v1.4 permits truthful
time evidence for C17
        ↓
Approved Worked-Time Evidence
retains exact Arrangement /
Compensation Relationship affinity
        ↓
MS-PROT-080 calculates and resolves
compensation attributable to C17
```

The resulting bounded compensation resolution MAY use:

```text
existing-obligation-resolution-access@1
```

Commercial downgrade therefore does not make truthful performance of an already-existing work commitment impossible to compensate.

## 8.7 No indefinite free Payroll

Rejected:

```text
CompensationRelationship exists
        ↓
merchant downgrades
        ↓
all future Payroll periods
remain free forever
```

Rejected:

```text
employee remains employed
        ↓
every future GrandRue Payroll cycle
is residual
```

Rejected:

```text
old Compensation Terms exist
        ↓
new unrelated post-downgrade work
may always be processed without entitlement
```

A new compensation period, new work event, new negotiated amount or other new compensation activity that lacks a qualifying bounded residual anchor requires current protected commercial permission.

## 8.8 Corrections

An authoritative correction to a qualifying existing obligation MAY change the corrected monetary result without becoming new paid activity merely because the corrected amount differs.

However, correction MUST NOT be used to introduce:

```text
a new future pay rate
a new prospective allowance
a new Compensation Relationship
a materially new future Terms Revision
unrelated new work
```

A prospective terms change is Section 5 authoring.

## 8.9 Existing relationship is not sufficient

This is a hard invariant:

> **Residual compensation access attaches to an exact bounded obligation or independently residual source commitment—not merely to the continued existence of a Compensation Relationship.**

---

# 9. Merchant Observation

## 9.1 Contract

```text
workforce-compensation/merchant-observation-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for otherwise-authorised inspection of retained MS-PROT-080 information.

This may include, subject to existing authority:

```text
Compensation Relationships
Compensation Terms history
Compensation Amounts
Compensation Ledger projections
Jurisdiction Pay Treatment evidence
PayrollInputSnapshots
PayrollCalculationSnapshots
Payroll approval evidence
regulatory execution evidence
payment/remittance evidence
Compensation Documents
corrections/replacements
retained historical compensation records
```

## 9.2 Observation boundary

Observation does not grant:

```text
new Compensation Terms
new Compensation Amounts
new Payroll cycles
new filings
new payments
new document generation
bulk export
cross-merchant access
another Payee's private information
```

Commercial loss MUST NOT itself erase the merchant's legitimate access to retained business records.

Retention and permission to observe remain separate.

---

# 10. Personal Payee Observation

## 10.1 Contract

```text
workforce-compensation/personal-observation-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for otherwise-authorised observation of the Payee's own permitted compensation information.

Potential existing scope includes:

```text
own accepted Compensation terms
own pay history
own payslips
own payment statements
own deduction statements
own permitted Compensation Documents
```

subject to existing MS-PROT-080 authority.

## 10.2 Related-Payee requirement

The exemption does not bypass:

```text
workforce-compensation /
related-payee-compensation
```

or any applicable:

```text
trusted execution context
authentication
Actor Authorisation
privacy
Exposure
retention
account restrictions
```

## 10.3 Current and former workforce participants

Ending Merchant Membership does not itself destroy historical Payee eligibility.

Commercial downgrade likewise MUST NOT destroy legitimate historical Payee eligibility.

Exact authentication/API/Surface/Audience mechanics for:

```text
former Payees
Payees without Merchant Membership
legal-entity representatives
```

remain under:

```text
MS-PROT-080-V11-DQ-013
```

This amendment does not resolve them.

## 10.4 No personal subscription

A Payee SHALL NOT require a separate personal GrandRue subscription merely to exercise otherwise-authorised own-subject compensation access.

Commercial permission is merchant-scoped where a protected merchant service is involved.

The bounded personal-observation contract itself requires no Commercial Entitlement.

---

# 11. Payee Participation in Protected Operations

A Payee may participate in an operation whose merchant-side commercial purpose is protected.

Examples include:

```text
accepting authoritative Compensation Terms

providing required setup information
for new prospective compensation administration
```

where already semantically supported.

In such a case:

```text
merchant scope
        ↓
required protected Commercial permission
```

is evaluated independently from:

```text
Payee authentication
related-Payee relationship
personal authority
privacy
Exposure
```

The Payee does not purchase or inherit the merchant's plan.

Where the participation instead exists solely to resolve an exact qualifying existing obligation, Section 8 may govern the Commercial predicate.

---

# 12. Standard Tier Allocation

Both protected commercial purposes belong to BUSINESS:

```text
MAINTAIN_WORKFORCE_COMPENSATION_TERMS

ADMINISTER_WORKFORCE_COMPENSATION
```

Therefore a standard catalogue shall satisfy:

```text
FREE
    → neither protected purpose

BUSINESS
    → both protected purposes

GROWTH
    → both protected purposes
       through monotonic inclusion
```

This preserves:

```text
FREE grants ⊆ BUSINESS grants ⊆ GROWTH grants
```

The two protected purposes are separate because:

```text
prospective compensation relationship/terms authority
        ≠
actual compensation/payroll administration
```

but the current standard commercial policy places both within BUSINESS.

No separate GROWTH premium is introduced for Payroll.

No Payroll-versus-Non-Payroll tier difference is introduced.

No merchant-size, Payee-count, pay-frequency or jurisdiction surcharge is introduced.

Prices and quantitative allowances remain outside this amendment.

---

# 13. Trial and Other Grant Sources

This amendment changes no existing grant-source model.

When exact entitlement identities are later established, the same protected purposes may be satisfied by independently valid sources such as:

```text
initial full-experience trial
paid Merchant Commercial Agreement
commercial remediation
other accepted future grant source
```

according to Commercial authority.

The Standing FREE baseline does not receive the two protected purposes merely because the underlying semantic capability is present in the platform.

A trial ending does not erase facts created while it was valid.

Qualifying residual access continues according to Sections 6, 8, 9 and 10.

---

# 14. Required Supporting Services

A commercially included Compensation workflow MUST NOT be fragmented by turning necessary supporting mechanics into unexplained higher-tier barriers.

Accordingly, BUSINESS placement of the protected Compensation purposes does not by itself create separate higher-tier requirements for mandatory supporting steps such as:

```text
jurisdiction-qualified deterministic calculation
required Payroll document generation
required regulatory filing
required remittance coordination
required Payment participation
required Notification delivery
required Exposure/presentation path
```

where those steps are necessary to complete the included compensation service.

This does **not** mean those supporting authorities become commercially owned by MS-PROT-080.

Every supporting service still retains its own:

```text
semantic applicability
Actor Authorisation
jurisdiction requirements
provider readiness
resource protection
security
privacy
execution approval
failure semantics
```

A future independently valuable optional service may receive its own commercial classification only through separately accepted authority.

---

# 15. Jurisdiction Boundary

Commercial permission never proves jurisdiction support.

The following remain distinct:

```text
Commercial Entitlement
        ≠
Jurisdiction Pay Treatment
        ≠
Jurisdiction support
        ≠
Calculation Readiness
        ≠
Regulatory Execution Readiness
```

A BUSINESS merchant may possess both protected commercial purposes while a requested jurisdiction-specific operation remains unsupported or unresolved.

The platform MUST return the applicable jurisdiction/support/readiness result rather than misreporting that state as a commercial denial.

This amendment does not resolve:

```text
MS-PROT-080-V11-DQ-001
first jurisdiction rollout

MS-PROT-080-V11-DQ-002
treatment-resolution mechanism

MS-PROT-080-V11-DQ-003
jurisdiction fact/evidence schema

MS-PROT-080-V11-DQ-007
calculation engines/providers

MS-PROT-080-V11-DQ-008
regulatory APIs/certification

MS-PROT-080-V11-DQ-010
jurisdiction correction/off-cycle rules

MS-PROT-080-V11-DQ-011
cross-border treatment
```

or another jurisdiction-owned deferred decision.

---

# 16. Document Boundary

Commercial classification does not resolve document technology or legal content.

The following remain independently deferred:

```text
MS-PROT-080-V11-DQ-018
digital/e-signature technology
and assurance

MS-PROT-080-V11-DQ-019
document rendering/storage technology

MS-PROT-080-V11-DQ-020
jurisdiction document templates
and legally required clauses/content
```

Commercial rules are:

```text
prospective durable agreement generation
as part of new Terms establishment
    → terms-authoring-access@1

required document generation for
a qualifying existing obligation
    → existing-obligation-resolution-access@1

observation/download of an already-existing
permitted document
    → merchant-observation-access@1
       or personal-observation-access@1
```

A document's commercial classification does not alter its source-of-truth boundary.

---

# 17. Commercial Loss Does Not Rewrite Compensation Truth

Loss of BUSINESS/GROWTH permission SHALL NOT itself:

```text
delete a CompensationRelationship

delete Compensation Terms

change a pay rate

change a Jurisdiction Pay Treatment

cancel an established CompensationAmount

invalidate an approved Payroll snapshot

erase regulatory obligations

mark a payment unpaid or paid

delete Compensation Documents

end Merchant Membership

cancel Scheduled Work Commitments

rewrite historical provenance
```

Commercial loss changes permission for protected future use.

It does not rewrite business history.

---

# 18. Retry and Recovery

Recovery of an already-committed Compensation result is not new compensation authoring merely because the client did not observe the original response.

Where an exact logical request already committed:

```text
retry / committed-result recovery
```

MAY return the existing authoritative result without requiring a newly acquired protected entitlement solely for recovery.

This does not waive:

```text
authentication
scope
authorisation
privacy
retention
```

A retry that would create a new authoritative effect because no prior commit exists MUST satisfy the commercial classification applicable at actual execution time.

A lost acknowledgement cannot be exploited to duplicate:

```text
Compensation Amounts
Payroll approval
filing
remittance
payment
documents
```

---

# 19. Failure Semantics

Commercial rejection MUST remain distinguishable from:

```text
semantic inapplicability

authentication rejection

Actor Authorisation rejection

related-Payee relationship failure

treatment unresolved

unsupported jurisdiction

stale Compensation Terms

stale Payroll calculation

operational ineligibility

provider/authority failure

uncertain external outcome

payment failure

document-generation failure

document-delivery failure

retention/privacy restriction

technical failure
```

Unavailable evidence MUST NOT be reported as:

```text
COMMERCIAL_DENIED
```

unless the Commercial predicate was actually evaluated and denied.

Likewise:

```text
Commercial permitted
```

MUST NOT be reported as operational success.

---

# 20. AI Boundary

AI may continue to perform only the assistive roles already accepted by composite MS-PROT-080.

Commercial permission does not expand AI authority.

AI MUST NOT:

```text
create a Compensation Relationship autonomously

approve Compensation Terms

approve a Compensation Amount

determine authoritative jurisdiction treatment

perform statutory calculation

approve Payroll

convert residual access into new paid activity

invent a residual anchor

mark filing/remittance/payment complete

alter commercial classification
```

Live AI availability is not required for deterministic Payroll correctness or truthful observation of retained compensation records.

---

# 21. Low-Software-Capacity Merchant Behaviour

The ordinary merchant experience SHOULD remain business-native.

A merchant should encounter concepts resembling:

```text
Add someone you pay

How are they paid?

Review the terms

Review this pay cycle

Approve

Needs attention

Paid / outstanding
```

The merchant SHOULD NOT be required to understand:

```text
CommercialEntitlementIdentity
OPERATION_ACCESS
residual-access predicates
PayrollInputSnapshot
Commercial Access Binding
grant provenance
```

Those are internal governance and execution concerns.

Where the merchant loses protected commercial access, the product SHOULD explain the business consequence rather than expose entitlement internals.

Example:

```text
You can still view previous pay records
and finish payments already in progress.

Starting a new pay cycle requires Business.
```

The precise UI wording remains presentation authority, not semantic authority.

---

# 22. Falsification Review

## F-01 — New Compensation Relationship on FREE

Merchant on FREE attempts to establish a new Compensation Relationship.

Required result:

```text
terms-authoring-access@1
→ MAINTAIN_WORKFORCE_COMPENSATION_TERMS
→ commercial denial
```

assuming no independent valid grant source.

**PASS.**

## F-02 — Existing terms proposal after downgrade

Merchant created a proposed agreement while BUSINESS was valid, then downgraded before Payee acceptance.

Payee attempts acceptance.

Required result:

```text
acceptance would establish Terms
→ protected terms authoring
→ current permission required
```

The old proposal does not create perpetual entitlement.

**PASS.**

## F-03 — Payee rejects old proposal after downgrade

Required result:

```text
existing-terms-proposal-resolution-access@1
→ no Commercial Entitlement
```

The proposal can be declined without repurchasing BUSINESS.

**PASS.**

## F-04 — New Payroll period after downgrade

An employment/Compensation Relationship remains active, but no qualifying bounded pre-loss compensation anchor exists for the new period.

Required result:

```text
compensation-administration-access@1
→ ADMINISTER_WORKFORCE_COMPENSATION
→ commercial denial
```

Existing relationship alone is insufficient.

**PASS.**

## F-05 — Existing Scheduled Work Commitment crosses downgrade boundary

Work was scheduled while BUSINESS permission existed.

Commercial permission later ends.

MS-PROT-081 permits truthful time evidence for the existing commitment.

Required result:

```text
exact existing ScheduledWorkCommitment
+
existing Compensation affinity
+
approved work evidence
→ bounded compensation resolution
→ no Commercial Entitlement
```

The merchant can pay work already committed.

**PASS.**

## F-06 — Old Payroll calculation awaits merchant approval

Exact PayrollCalculationSnapshot was validly established before downgrade.

Required result:

```text
existing-obligation-resolution-access@1
```

may permit approval and required downstream resolution, subject to stale-input revalidation and every other predicate.

**PASS.**

## F-07 — Regulatory filing remains due

A pre-existing Payroll event created an exact Regulatory Obligation before downgrade.

Required result:

```text
filing/remittance resolution
→ residual
```

The merchant is not forced to retain paid access solely to satisfy an already-existing obligation.

**PASS.**

## F-08 — Merchant corrects historical underpayment

A qualifying historical obligation is corrected from £800 to £850.

Required result:

```text
authorised correction of exact old obligation
→ residual
```

The £50 correction does not become new paid functionality merely because the corrected value changed.

**PASS.**

## F-09 — Merchant disguises new bonus as correction

A new £500 discretionary future bonus is added after downgrade with no qualifying existing obligation.

Required result:

```text
not residual
→ protected compensation administration
```

**PASS.**

## F-10 — Existing payslip download after downgrade

Otherwise-authorised Payee requests their retained payslip.

Required result:

```text
personal-observation-access@1
→ no Commercial Entitlement
```

**PASS.**

## F-11 — Former staff member

Merchant Membership ended.

Historical Compensation record remains retained.

Required result:

Commercial downgrade and ended Membership do not by themselves destroy eligibility, but exact authentication/API/Exposure mechanics remain governed by DQ-013.

**PASS.**

## F-12 — Legal-entity Payee

A human claims to represent a contractor company and requests its compensation documents.

Required result:

```text
no automatic personal-observation authority
```

Representative authority remains unresolved under DQ-013.

**PASS.**

## F-13 — Payroll versus contractor treatment

One Payee is PAYROLL; another is NON_PAYROLL.

Both are under BUSINESS.

Required result:

Commercial tier does not determine treatment and treatment does not determine tier.

**PASS.**

## F-14 — Provider outage

Merchant has required commercial permission but payroll/regulatory provider is unavailable.

Required result:

```text
provider/readiness failure
≠ commercial denial
```

Authoritative compensation truth survives.

**PASS.**

## F-15 — AI unavailable

Deterministic Payroll operation otherwise has authoritative inputs.

Required result:

AI outage does not remove the commercial permission or become calculation authority.

**PASS.**

## F-16 — Construction merchant records a new negotiated amount after downgrade

The merchant has an old general Compensation Relationship but the newly negotiated amount relates to new post-downgrade work and no exact bounded residual anchor.

Required result:

```text
ADMINISTER_WORKFORCE_COMPENSATION required
```

**PASS.**

## F-17 — Construction merchant resolves an amount already established before downgrade

The exact Compensation Amount already exists and merely requires payment/reconciliation.

Required result:

```text
existing-obligation-resolution-access@1
```

may apply.

**PASS.**

## F-18 — External payment

Merchant pays an exact existing compensation obligation outside GrandRue and records the result.

Required result:

Residual classification MAY permit the record/reconciliation.

The merchant-entered record still does not prove bank settlement.

**PASS.**

## F-19 — Trial expires during an existing bounded compensation obligation

The trial grant expires after an exact qualifying obligation was validly established.

Required result:

The trial no longer permits new compensation activity.

Existing bounded resolution remains possible.

**PASS.**

## F-20 — Merchant observation on FREE after downgrade

Merchant legitimately retains compensation history from a former BUSINESS period.

Required result:

Otherwise-authorised observation remains available without new Commercial Entitlement.

No new Payroll activity follows.

**PASS.**

---

# 23. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

## Business-to-Software Translation

The merchant chooses understandable commercial value.

GrandRue internally composes:

```text
commercial permission
+
Compensation semantics
+
jurisdiction
+
Payee relationship
+
Payroll / Non-Payroll route
+
readiness
+
provider execution
```

without making the merchant configure those architectural dimensions.

## Administrative Compression

The proposal avoids:

```text
separate payroll add-on switches
per-document entitlements
per-deduction entitlements
per-provider entitlements
Payroll-vs-contractor plan selection
merchant-managed residual rules
```

Two protected purposes are sufficient for the current portfolio.

## Ordinary-Staff Training

Workers and Payees do not need to understand plan grants.

They perform role-native actions such as:

```text
review terms
accept terms
view payslip
view pay history
```

## Exception-Driven Operation

The commercial boundary supports the accepted Compensation model's intended exception-driven merchant operation rather than turning entitlement administration into a routine merchant task.

## Target-Market Proportionality

The internal residual-resolution rule is more detailed than the merchant experience.

That complexity is justified because the system must simultaneously prevent:

```text
legal/pay obligation lockout
```

and:

```text
indefinite unpaid commercial use
```

This is intrinsic correctness/commercial-boundary complexity, not merchant-facing configurability.

---

# 24. Alternatives and Trade-Offs

## Alternative A — One blanket `WORKFORCE_COMPENSATION` entitlement

**Rejected.**

It cannot distinguish:

```text
new paid activity
from
required resolution of old obligations
```

and would either lock history/pay obligations or permit excessive residual use.

## Alternative B — Separate entitlement for every Payroll step

Examples:

```text
CALCULATE_PAYROLL
APPROVE_PAYROLL
FILE_PAYROLL
PAY_PAYROLL
GENERATE_PAYSLIP
```

**Rejected for the initial commercial model.**

These are required parts of a coherent compensation workflow rather than separate merchant value products.

Fragmenting them would undermine Administrative Compression and BUSINESS workflow coherence.

## Alternative C — Existing Compensation Relationship grants perpetual residual Payroll

**Rejected.**

A long-lived relationship is not a bounded pre-existing commitment.

This would permit indefinite free future compensation administration after downgrade.

## Alternative D — Commercial loss blocks all Compensation operations

**Rejected.**

It could strand already-earned pay, regulatory obligations, required documents, corrections and truthful historical access.

## Alternative E — Payroll and Non-Payroll use different standard tiers

**Rejected.**

Jurisdiction treatment is not a commercial value-level classification.

## Chosen trade-off

The initial model uses:

```text
2 protected commercial purposes

+

5 explicitly bounded
no-entitlement contracts
```

This is the smallest current portfolio that preserves:

```text
new-use protection
historical access
negative resolution
legal/pay obligation completion
future catalogue exactness
```

without converting plan identity into Compensation semantics.

---

# 25. Hard Invariants

1. BUSINESS/GROWTH packaging MUST NOT determine Payroll or Non-Payroll treatment.
2. Commercial permission MUST NOT establish a Compensation Relationship or Compensation Terms.
3. FREE receives neither protected Compensation purpose through the standard catalogue.
4. GROWTH includes both protected purposes through standard-plan monotonicity.
5. A Payee requires no personal subscription merely to participate in an otherwise-authorised merchant Compensation operation.
6. Personal observation of permitted own Compensation information requires no Commercial Entitlement.
7. Merchant observation of retained Compensation information requires no Commercial Entitlement.
8. Rejection/dispute of an existing unaccepted terms proposal requires no Commercial Entitlement.
9. Acceptance that establishes authoritative Compensation Terms remains protected new use.
10. Residual compensation access requires an exact bounded residual anchor.
11. A Compensation Relationship alone is not a residual anchor for all future compensation.
12. A Compensation Terms Revision alone is not a residual anchor for all future compensation.
13. Corrections may resolve existing obligations but MUST NOT manufacture new prospective terms.
14. Commercial loss MUST NOT rewrite or delete Compensation truth.
15. Commercial permission MUST remain distinct from jurisdiction, treatment, authorisation, readiness, provider and Exposure predicates.
16. Required supporting services MUST NOT become unstated higher-tier obstacles to completing the included BUSINESS compensation workflow.
17. No concrete Commercial Entitlement identity is created by this amendment.
18. Missing commercial classification MUST NOT be treated as no-entitlement access.
19. Exact committed-result recovery MUST NOT create duplicate Compensation effects.
20. This amendment MUST NOT activate implementation.

---

# 26. Deferred-Decision Effect

Upon approval:

```text
MS-PROT-080-V11-DQ-016
```

becomes:

```text
RESOLVED
```

with the disposition:

> Composite MS-PROT-080 defines two protected owner-qualified commercial purposes—`MAINTAIN_WORKFORCE_COMPENSATION_TERMS` and `ADMINISTER_WORKFORCE_COMPENSATION`—allocated to BUSINESS and GROWTH, plus bounded no-Commercial-Entitlement contracts for preparation, merchant observation, personal observation, negative resolution of existing terms proposals and resolution of exact existing compensation obligations. Residual access attaches to a bounded obligation or separately owner-qualified residual source commitment, not merely to an existing Compensation Relationship or Compensation Terms Revision. Concrete Commercial Entitlement identities and final plan grant sets remain under `MS-PROT-056-V17-DQ-001`.

All other MS-PROT-080 deferred decisions retain their current state unless separately resolved by accepted authority.

---

# 27. MS-PROT-056 Boundary

This amendment supplies owner-local classification required for eventual catalogue assembly.

It does **not**:

```text
select concrete entitlement identities

publish a catalogue generation

establish exact FREE/BUSINESS/GROWTH
plan revision identities

change the trial

change Standing Free

select prices

select quotas

activate Workforce Compensation

complete C3
```

`MS-PROT-056-V17-DQ-001` therefore remains OPEN.

---

# 28. MS-PROT-091 Boundary

This amendment does not classify MS-PROT-091 Rota operations.

The accepted BUSINESS allocation includes:

```text
Workforce Scheduling
Timekeeping
Leave
Rota
```

but MS-PROT-091 remains the owner of its own:

```text
RotaPeriod
Shift work requirement
OPEN_SELECTION ShiftClaim
ScheduleExclusion
conditional WorkSite
rota publication/revision
```

Commercial classification for those MS-PROT-091-owned operations must be established separately before a complete standard catalogue can bind them.

MS-PROT-081 commercial classifications MUST NOT be silently extended to MS-PROT-091 merely because both participate in workforce scheduling.

---

# 29. Recommendation

```text
RECOMMENDATION: ACCEPT
```

The design:

```text
preserves owner-local semantics
preserves BUSINESS allocation
avoids wildcard grants
keeps Payroll/Non-Payroll treatment independent
protects new commercial use
preserves bounded residual obligations
avoids historical data lockout
avoids indefinite free Payroll
preserves merchant/payee simplicity
leaves concrete catalogue construction to Commercial
```

No unresolved ambiguity found in this bounded commercial-classification scope requires deferral.

---

# 30. Manual Approval Boundary

```text
RECOMMENDATION: ACCEPT
        ≠
MANUAL APPROVAL: GRANTED
        ≠
STATUS: ACCEPTED
```

Current state:

```text
RECOMMENDATION: ACCEPT
MANUAL APPROVAL: GRANTED
STATUS: ACCEPTED
IMPLEMENTATION ACTIVATION: NONE
```

Approval authorises repository formalisation and governance-navigation synchronisation.

---

# 31. Acceptance Statement

> **GrandRue may commercially protect the establishment and ongoing administration of new workforce compensation activity without making commercial downgrade a barrier to truthful records, Payee access or the bounded completion of already-existing compensation obligations. Commercial access therefore distinguishes prospective Compensation Terms authoring and new Compensation administration from preparation, observation, negative proposal resolution and exact existing-obligation resolution. Residual access attaches to a bounded authoritative obligation or independently owner-qualified residual commitment—not merely to the existence of a Compensation Relationship—and neither commercial permission nor plan identity may determine Payroll treatment, compensation truth, actor authority, jurisdiction support or execution readiness.**
