# MS-PROT-080 v1.1 — Workforce Compensation, Jurisdiction Pay Treatment, Payroll & Compensation Document Model

**Document ID:** MS-PROT-080  
**Version:** 1.1  
**Status:** **ACCEPTED by manual approval on 5 September 2026**  
**Approved:** Manual approval of the exact consolidated v1.1 presented in ChatGPT on 5 September 2026  
**Authority type:** Semantic / application-architecture authority  
**Governed by:** MS-DESIGN-RULES-001, MS-AVS-001  
**Supersedes:** MS-PROT-080 v1.0 in full within the MS-PROT-080 authority scope  
**Depends on:** MS-PROT-025, MS-PROT-027, composite MS-PROT-048, composite MS-PROT-053, composite MS-PROT-055, MS-PROT-059, composite MS-PROT-062, composite MS-PROT-063, MS-PROT-064, composite MS-PROT-065, MS-PROT-067, composite MS-PROT-069, MS-PROT-070, MS-PROT-072, MS-PROT-074, composite MS-PROT-075  
**Related:** MS-PROT-029, MS-PROT-037, composite MS-PROT-057, MS-PROT-079  
**Purpose:** Define how Main Street represents and automates compensation for people and organisations performing work for merchants; determines jurisdiction-qualified Payroll or Non-Payroll treatment; supports calculated, negotiated, time-based, project-based and progress-based compensation; automates applicable statutory execution; generates agreements, payslips and other compensation documents; and prevents merchant labels, AI, providers, generated documents or business categories from becoming competing semantic authority.

---

# 1. Governing Decision

Main Street SHALL support **Workforce Compensation** as the generic merchant-facing capability for determining, approving, administering, documenting and tracking amounts owed to Payees for work performed for a merchant.

Employer Payroll is one possible execution route.

Non-Payroll compensation is another.

The canonical model is:

```text
Payee
   +
Compensation Relationship
   +
Compensation Terms
   +
actual work/progress/pay evidence
        ↓
Amount established
        ↓
Jurisdiction Pay Treatment
        ↓
┌──────────────────────┬──────────────────────┐
│                      │                      │
PAYROLL              NON-PAYROLL          UNRESOLVED
│                      │
│                      ├── ordinary payment
│                      │
│                      └── regulated payer
│                          obligations where required
│
└── deterministic payroll calculation
    deductions
    employer liabilities
    filing
    remittance
        ↓
Merchant approval
        ↓
Payment / regulatory execution
        ↓
Documents
        ↓
Reconciliation / historical ledger
```

Main Street SHALL automate supported jurisdiction complexity without requiring ordinary merchants to become payroll, employment-status, tax or regulatory-protocol specialists.

---

# 2. Product Principle

The merchant-facing problem is:

> **Who performed work, what have we agreed they should be paid, what became payable, what statutory treatment applies, what has been paid, and what remains outstanding?**

Main Street SHOULD reduce ordinary merchant action to:

```text
identify the Payee
describe/record the commercial arrangement
record or approve work/progress/pay evidence
review exceptions
approve the amount/result
```

Main Street SHOULD automate the remaining supported mechanics.

---

# 3. Explicit Non-Goals

This authority SHALL NOT establish:

1. a universal employment-law ontology;
2. a universal HR platform;
3. one global `ContractType`;
4. one universal meaning of `employee`;
5. one universal meaning of `worker`;
6. one universal meaning of `self-employed`;
7. one universal meaning of `contractor`;
8. a universal zero-hours employment classification;
9. one universal statutory-scheme hierarchy;
10. one universal government payroll API;
11. a requirement that every person performing work enters Payroll;
12. a rule that every Non-Payroll relationship has no payer-side statutory obligations;
13. employment/pay treatment from merchant labels alone;
14. authoritative legal classification through AI;
15. business-type-specific compensation semantics;
16. professional/licensing eligibility;
17. right-to-work authority;
18. customer Payment ownership of Workforce Compensation;
19. bank settlement authority merely from merchant data entry; or
20. initial-MVP sequencing.

---

# 4. Mandatory Semantic Separation

Main Street MUST preserve:

```text
Identity
    ≠ Merchant Membership
    ≠ Compensation Relationship
    ≠ Compensation Terms
    ≠ employment-law status
    ≠ tax/pay treatment
    ≠ Schedule
    ≠ Attendance
    ≠ Worked-Time Evidence
    ≠ Approved Payable Time
    ≠ Project Progress
    ≠ Amount Agreed
    ≠ Amount Approved Payable
    ≠ Payroll Participation
    ≠ Payroll Calculation
    ≠ Worker Deduction
    ≠ Employer Liability
    ≠ Non-Payroll Withholding
    ≠ Merchant Approval
    ≠ Regulatory Filing
    ≠ Regulatory Acceptance
    ≠ Statutory Remittance
    ≠ Payee Payment
    ≠ Funds Settlement
    ≠ Generated Agreement
    ≠ Payslip / Payment Statement
```

No convenience projection, generated document, provider response or country adapter may collapse these distinctions.

---

# 5. Canonical Terminology

## 5.1 Payee

A **Payee** is the person or legal payee receiving compensation under one Compensation Relationship.

`Payee` does not imply employment status.

---

## 5.2 Compensation Relationship

A **Compensation Relationship** is the merchant-scoped relationship under which Main Street may determine, administer and track compensation owed to a Payee.

It is not a complete employment or services contract.

---

## 5.3 Compensation Terms Revision

A **Compensation Terms Revision** is an effective-dated authoritative Main Street record describing how compensation for one Compensation Relationship is established.

A Compensation Terms Revision may reference accepted agreement evidence.

---

## 5.4 Compensation Amount

A **Compensation Amount** is an amount established for one bounded work/progress/pay event or period.

Its establishment mechanism MUST be preserved.

---

## 5.5 Jurisdiction Pay Treatment Revision

A **Jurisdiction Pay Treatment Revision** is the effective-dated Main Street operational determination of how compensation under one Compensation Relationship must be administered under one explicit supported jurisdiction.

It is not a universal legal-status classification.

---

## 5.6 Payroll Route

The **Payroll Route** means compensation is administered through supported employer Payroll.

---

## 5.7 Non-Payroll Route

The **Non-Payroll Route** means compensation is not administered through ordinary employer Payroll.

Non-Payroll may still produce payer-side withholding, contribution, reporting, remittance or documentation obligations.

---

## 5.8 Compensation Document

A **Compensation Document** is a generated human-readable representation or evidence artefact derived from governed Compensation facts.

Examples include agreements, variations, payslips, payment statements and deduction statements.

A Compensation Document MUST NOT silently become a competing source of authoritative calculation truth.

---

# 6. Semantic Ownership

## 6.1 Workforce Compensation owns

```text
CompensationRelationship
CompensationTermsRevision
CompensationTermsEvidence
CompensationAmount
merchant Compensation Approval
Compensation Ledger facts
jurisdiction-treatment affinity
Compensation Document provenance
```

## 6.2 Payroll owns within the Payroll Route

```text
PayrollRun
PayrollInputSnapshot
PayrollCalculationSnapshot
PayrollApproval
Payroll correction/replacement relationships
Payroll regulatory execution evidence
Payroll document production facts
```

## 6.3 Workforce Compensation does not own

```text
Identity
Merchant Membership
general HR truth
complete employment contract
professional qualification/licence
right-to-work truth
Schedule authority
Attendance authority
customer Payment
bank truth
regulatory-authority truth
provider credentials
AuditRecord
```

---

# 7. Compensation Relationship Identity and Scope

Every Compensation Relationship MUST have a durable Main Street-generated identity.

One Compensation Relationship belongs to exactly one Merchant Scope.

The identity MUST NOT be inferred solely from:

```text
merchant + Payee
```

because one Payee MAY have multiple materially distinct Compensation Relationships with the same merchant where supported.

A Merchant Membership MUST NOT automatically create a Compensation Relationship.

A Compensation Relationship MUST NOT automatically create a Merchant Membership or operational authority.

---

# 8. Compensation Terms

Compensation Terms MAY establish compensation through materially different mechanisms.

Examples include:

```text
fixed periodic amount
hourly rate
day rate
piece/job amount
commission
bonus
allowance
approved reimbursement
fixed project amount
milestone amount
progress formula
separately negotiated progress amount
other registered supported mechanism
```

These are compensation mechanisms, not universal legal classifications.

---

# 9. Calculated and Negotiated Compensation

Main Street MUST support both:

```text
CALCULATED COMPENSATION
```

and:

```text
NEGOTIATED / RECORDED COMPENSATION
```

Calculated example:

```text
120 approved payable hours
×
£20/hour
=
£2,400
```

Negotiated example:

```text
Foundation stage reached
+
merchant and contractor agree £3,000
=
£3,000 Compensation Amount
```

Main Street MUST preserve which mechanism established each Compensation Amount.

---

# 10. Compensation Terms Evidence

Compensation terms MAY originate from:

```text
merchant-recorded verbal agreement
Payee-acknowledged terms
written document
generated Main Street agreement
invoice
accepted variation
other jurisdiction/permitted evidence
```

Main Street MUST retain provenance.

Merchant recording:

```text
"We verbally agreed £3,000 after foundation stage"
```

MUST NOT be represented as independently Payee-confirmed unless Payee confirmation or equivalent evidence exists.

---

# 11. Verbal Agreements

Main Street MUST NOT require a formal written contract merely to track compensation where the applicable jurisdiction permits the commercial relationship to exist on another basis.

Main Street MAY convert merchant-recorded verbal terms into a candidate structured Compensation Terms Revision.

The merchant MUST review/approve material structured terms before authoritative use.

AI MAY assist transcription/structuring.

AI MUST NOT fabricate missing terms.

---

# 12. Main Street Generated Agreements

Main Street MAY generate relationship/compensation agreements from governed facts.

Conceptual flow:

```text
candidate relationship facts
        +
candidate Compensation Terms
        +
Jurisdiction Pay Treatment
        +
jurisdiction-required clauses
        ↓
generated agreement
        ↓
required merchant approval
        ↓
required Payee acceptance/acknowledgement
        ↓
accepted Compensation Terms Revision
```

A generated agreement MUST NOT create an unsupported jurisdiction treatment merely through its wording.

---

# 13. Agreement Status Is Not One Generic Domain Status

Agreement lifecycle facts MAY include:

```text
generated
merchant approved
sent to Payee
Payee acknowledged
Payee rejected/disputed
superseded by variation
```

Presentation MAY derive labels such as `Draft`, `Awaiting Signature` or `Accepted`.

The UI label itself MUST NOT become separate authority.

---

# 14. Agreement Acceptance

Where the applicable relationship requires mutual acknowledgement for Main Street's intended evidence strength, Main Street MUST record:

```text
exact document revision
merchant principal
Payee principal
acceptance time
acceptance method
document hash/version
```

Duplicate acceptance MUST NOT create multiple Compensation Terms revisions.

---

# 15. Agreement Variation

A material change to accepted Compensation Terms MUST NOT destructively rewrite the historical agreement.

Example:

```text
Original:
Foundation supervision £3,000

Variation:
Additional retaining-wall supervision +£900
```

The variation MUST create new effective agreement/terms evidence.

Historical calculations remain affined to the terms in force when they were established.

---

# 16. Generated Agreement Is Not Independent Truth

Hard invariant:

> **The generated document represents governed relationship and compensation facts; editing or replacing the rendered document MUST NOT silently mutate those facts.**

A semantic change requires the governed Compensation Terms mutation path.

---

# 17. Compensation Basis Does Not Determine Legal Status

Main Street MUST NOT infer:

```text
hourly → employee

monthly → employee

invoice → self-employed

project pay → contractor

commission → employee

no guaranteed hours → self-employed
```

The applicable Jurisdiction Pay Treatment resolver governs.

---

# 18. No Universal Zero-Hours Type

`Zero-hours` MUST NOT become a universal employment or Payroll classification.

Where Main Street needs the underlying business meaning, it SHOULD represent jurisdiction-safe facts such as:

```text
NO GUARANTEED WORKING HOURS
```

subject to the applicable jurisdiction contract.

No guaranteed hours alone does not establish Payroll or Non-Payroll treatment.

---

# 19. Effective-Dated Terms

Every material compensation-term change MUST create a new effective period/revision.

Historical terms MUST NOT be overwritten.

A prior approved Compensation Amount or Payroll Run MUST remain attributable to the exact Compensation Terms Revision used.

---

# 20. Project Progress Separation

Main Street MUST preserve:

```text
Project Progress
    ≠ Compensation Amount
```

A project reaching 50% completion MUST NOT automatically mean 50% payment unless the applicable Compensation Terms explicitly define that calculation.

Project/progress evidence MAY be consumed by Workforce Compensation.

Project management remains the semantic owner of project-progress truth where such a capability exists.

---

# 21. Milestone Compensation

Compensation Terms MAY define deterministic milestones:

```text
Foundation complete → £3,000
Structure complete → £4,000
Roof complete       → £3,000
```

When the required milestone evidence is accepted, Main Street MAY derive the corresponding Compensation Amount.

---

# 22. Negotiated Progress Compensation

Compensation Terms MAY establish that amounts are negotiated separately at defined or ad hoc project stages.

Example:

```text
Foundation completed
        ↓
merchant + contractor negotiate
        ↓
£2,500 recorded/accepted
        ↓
Compensation Amount £2,500
```

Main Street MUST NOT invent the amount from progress percentage.

---

# 23. Scheduling and Timekeeping Separation

Main Street MUST preserve:

```text
Scheduled Assignment
        ≠
Attendance
        ≠
Worked-Time Evidence
        ≠
Approved Payable Time
```

A scheduled 12-hour shift MUST NOT automatically create 12 payable hours.

Approved Payable Time MAY feed compensation calculation where Compensation Terms are time-based.

---

# 24. Time-Based Compensation

A Compensation Amount MAY be calculated from:

```text
Approved Payable Time
        ×
effective rate
        =
gross Compensation Amount
```

This is valid under either Payroll or Non-Payroll treatment.

How much the merchant owes and how the jurisdiction requires that amount to be administered remain separate questions.

---

# 25. Pay Schedule

Compensation MAY use registered schedules such as:

```text
weekly
fortnightly
four-weekly
monthly
project-stage
ad hoc approved payment
other jurisdiction-supported cadence
```

`FOUR_WEEKLY` MUST NOT mean `MONTHLY`.

Jurisdiction calculation must respect the actual configured period.

---

# 26. Explicit Jurisdiction

Every jurisdiction-qualified treatment MUST resolve an explicit Compensation/Payroll Jurisdiction.

Main Street MUST NOT rely solely on:

```text
merchant address
Payee nationality
website domain
currency
```

where those facts are insufficient.

A merchant MAY operate Compensation Relationships under multiple jurisdictions.

---

# 27. Jurisdiction Pay Treatment Resolution

Before Main Street chooses Payroll or Non-Payroll execution, the Compensation Relationship MUST have an applicable Jurisdiction Pay Treatment Revision.

Resolution conceptually uses:

```text
merchant-supplied facts
+
Payee-supplied facts
+
authoritative external evidence
+
accepted operational evidence
        ↓
Jurisdiction Pay Treatment Resolver
        ↓
PAYROLL or NON_PAYROLL
```

Main Street MUST NOT create one universal classification algorithm.

---

# 28. Treatment Resolution Is Relationship-Scoped

Treatment attaches to the Compensation Relationship.

It MUST NOT attach globally to the Identity.

The same person MAY simultaneously be:

```text
PAYROLL with Merchant A

NON_PAYROLL with Merchant B
```

where valid.

---

# 29. Merchant Labels Are Not Authority

Merchant descriptions such as:

```text
employee
self-employed
contractor
freelancer
consultant
zero-hours
```

MAY seed onboarding questions.

They MUST NOT determine treatment by themselves.

Actual relationship facts and jurisdiction rules govern.

---

# 30. Treatment Unresolved

If the required jurisdiction facts/evidence are insufficient, no effective Jurisdiction Pay Treatment Revision exists for the unresolved period.

`Treatment unresolved` is a derived workflow condition.

Main Street MUST NOT silently route the relationship merely to make payment easier.

---

# 31. Payroll Route

`PAYROLL` means employer Payroll applies under the active jurisdiction treatment.

The route MAY include:

```text
gross compensation
worker deductions
net pay
employer liabilities/contributions
statutory filing
statutory remittance
payslip
worker payment
correction
```

as required.

---

# 32. Non-Payroll Route

`NON_PAYROLL` means ordinary employer Payroll does not own the compensation execution.

It MUST NOT mean:

```text
no statutory obligation
no withholding
no reporting
no payer contribution
no remittance
no document requirement
```

---

# 33. Regulated Non-Payroll Compensation

A Non-Payroll relationship MAY produce payer obligations such as:

```text
Payee verification
withholding
reporting
remittance
payer contribution
payment/deduction statement
record retention
```

The active jurisdiction contract determines them.

Generic Workforce Compensation MUST NOT embed one country's contractor scheme.

---

# 34. Same Merchant, Different Treatments

The same merchant MAY simultaneously have:

```text
employee → PAYROLL

independent specialist → NON_PAYROLL

third Payee → treatment unresolved
```

Merchant business type MUST NOT force one treatment across all Payees.

---

# 35. New Relationship Bootstrap

A new Compensation Relationship MUST NOT universally depend on previous-employer documentation.

A jurisdiction MAY use:

```text
regulatory-authority instruction
Payee declaration
recognised prior evidence
statutory default/fallback
qualified provider evidence
```

where legally permitted.

Where no lawful fallback exists, Main Street MUST fail closed.

---

# 36. Changing Jobs and Concurrent Relationships

Main Street MUST NOT assume:

```text
new relationship = previous relationship ended
```

Concurrent relationships are allowed.

A new merchant relationship establishes independently scoped treatment and state.

Main Street MUST NOT copy another merchant's deductions or private compensation history merely because the Identity is the same.

---

# 37. Cross-Merchant Privacy

Merchant B MUST NOT automatically receive Merchant A's:

```text
pay rates
Compensation Terms
deductions
tax instructions
Payroll Runs
payment history
classification evidence
documents
```

Cross-relationship continuity requires an explicit jurisdiction/privacy/authorisation basis.

---

# 38. Compensation Amount Approval

A Compensation Amount may be:

```text
derived from terms/evidence
or
explicitly recorded as negotiated
```

The merchant MAY then approve the amount as payable.

`Amount established` and `Amount approved payable` MUST remain distinct where merchant approval is required.

---

# 39. Monetary Truth Separation

Main Street MUST distinguish:

```text
agreed amount
    ≠
approved payable amount
    ≠
gross compensation
    ≠
statutory withholding
    ≠
net amount payable to Payee
    ≠
amount remitted to authority
    ≠
actual funds settled
```

This distinction is mandatory for Compensation Ledger accuracy.

---

# 40. Compensation Ledger

Main Street SHALL maintain a merchant-scoped Compensation Ledger projection derived from authoritative Compensation, regulatory and payment facts.

The ledger SHOULD support views by:

```text
Payee
project/reference
Compensation Relationship
date/pay period
gross amount
withholding/deduction
net amount
amount paid
amount outstanding
regulatory remittance
```

The ledger is a projection and MUST NOT become independent mutation authority.

---

# 41. External Payments

Main Street compensation tracking MUST NOT require Main Street to execute the payment.

A merchant MAY record an externally executed payment.

The record MUST distinguish:

```text
merchant-reported payment
        ≠
provider-confirmed payment
        ≠
bank-confirmed settlement
```

A merchant-entered payment record MUST NOT become independent bank settlement truth.

---

# 42. Payroll Input Snapshot

Before statutory Payroll calculation, Main Street MUST freeze an immutable PayrollInputSnapshot including or referencing:

```text
Merchant Scope
Compensation Relationship
Payee
jurisdiction
pay period
pay date
exact Compensation Terms Revision
exact approved Compensation Amounts/inputs
exact jurisdiction payroll state/instructions
required continuity facts
exact calculation/rule version
currency
```

---

# 43. Deterministic Payroll Calculation

Payroll statutory calculation MUST be deterministic.

For identical:

```text
PayrollInputSnapshot
+
jurisdiction calculation/rule version
```

a conforming implementation MUST produce materially identical results.

AI MUST NOT participate in authoritative payroll calculation.

---

# 44. Payroll Calculation Correctness

Payroll Calculation Correctness means conformity with:

```text
approved Compensation inputs
+
applicable jurisdiction state
+
effective jurisdiction rules
+
exact calculation version
```

It does not guarantee the Payee's ultimate personal annual tax outcome where external facts subsequently change.

Later authoritative changes use governed correction/supersession semantics.

---

# 45. Payroll Calculation Snapshot

A PayrollCalculationSnapshot MUST distinguish:

```text
gross earnings
worker deductions
net pay
employer-only costs/liabilities
regulatory liabilities
```

Jurisdiction-specific components MUST retain:

```text
component identity
jurisdiction
amount
economic side/responsibility
calculation provenance
```

---

# 46. No Generic Statutory Scheme Ontology

`StatutoryPayrollScheme` SHALL NOT be required as a universal Main Street Payroll concept.

The generic core MUST NOT need universal domain objects for:

```text
National Insurance
Social Security
health insurance
pension insurance
unemployment insurance
Medicare
or equivalent schemes
```

Those meanings remain jurisdiction-qualified unless later evidence proves a generic semantic distinction necessary.

---

# 47. Versioned Jurisdiction Rules

Every statutory calculation MUST retain the exact jurisdiction calculation/rule version used.

A later rule update MUST NOT silently alter historical results.

Retroactive changes use explicit correction/replacement semantics.

---

# 48. Merchant Payroll Approval

Payroll approval applies to one exact PayrollCalculationSnapshot.

Approval MUST NOT mean:

```text
merchant calculated statutory deductions
merchant chose tax law
filing succeeded
authority accepted filing
remittance succeeded
funds settled
```

---

# 49. Stale Calculation Protection

Before approval commits, Main Street MUST verify that the applicable:

```text
Compensation Terms Revision
approved Compensation inputs
Jurisdiction Pay Treatment Revision
jurisdiction payroll state
calculation/rule version
```

still match the calculation.

If any applicable item changed, the stale calculation MUST NOT be approved.

Recalculation is required.

The stale-check and approval boundary MUST be atomically protected or equivalently serialized.

---

# 50. Approved History Is Immutable

After approval, Main Street MUST NOT silently rewrite:

```text
inputs
calculation
treatment revision
terms revision
approval evidence
```

Corrections use explicit linked correction/replacement operations.

---

# 51. Regulatory Obligations

Payroll and Non-Payroll compensation MAY generate zero or more jurisdiction-qualified Regulatory Obligations.

Each obligation MUST identify:

```text
logical obligation identity
source Compensation/Payroll event
jurisdiction
required action
fulfilment/authority binding
required evidence of satisfaction
```

---

# 52. Provider and Authority Boundary

Regulatory or payroll execution MAY use:

```text
Main Street internal implementation
regulatory-authority adapter
qualified provider
approved library/service
bank/payment rail
```

Provider choice MUST NOT redefine Main Street Compensation semantics.

---

# 53. Direct Regulatory Integration

Main Street MUST NOT require:

```text
Main Street → Payroll Provider → Government
```

for every jurisdiction.

Direct authority integration, provider-mediated fulfilment and mixed fulfilment are all permitted where validated.

---

# 54. Regulatory Success

Main Street MUST distinguish:

```text
prepared
≠ dispatched
≠ transport acknowledged
≠ authority received
≠ authority accepted
≠ obligation satisfied
```

Transport success MUST NOT be treated as regulatory success unless the active jurisdiction contract explicitly identifies that evidence as final.

---

# 55. Retry and Idempotency

Every uncertainty-sensitive external operation MUST have a stable logical identity.

This includes:

```text
Payroll filing
Non-Payroll reporting
correction
statutory remittance
withheld-funds remittance
Payee payment
```

If outcome is unknown after dispatch, Main Street MUST reconcile before repeating unless the external contract guarantees idempotent repetition for the same logical identity.

---

# 56. Payroll Payslip Generation

For applicable Payroll relationships, Main Street SHALL generate a jurisdiction-conforming payslip or equivalent payroll document where required by the supported jurisdiction.

The document MUST be generated from the authoritative approved PayrollCalculationSnapshot and associated facts.

Conceptual flow:

```text
approved PayrollCalculationSnapshot
        ↓
jurisdiction document rules
        ↓
Payslip
```

The payslip MUST NOT be manually editable in a way that changes Payroll truth.

A Payroll correction requires correction of the underlying authoritative Payroll facts followed by document regeneration.

---

# 57. Non-Payroll Payment Documents

For Non-Payroll relationships, Main Street MAY generate applicable documents such as:

```text
payment statement
remittance advice
deduction statement
contractor statement
jurisdiction-specific regulatory statement
```

The active jurisdiction contract determines required terminology, content, timing and legal effect.

Main Street MUST NOT call every Non-Payroll document a `Payslip`.

---

# 58. Compensation Document Content

A generated Compensation Document MUST derive all authoritative monetary/business values from governed source facts.

A document MAY add presentation text, explanatory labels and jurisdiction-required prose.

Presentation text MUST NOT silently create:

```text
new Compensation Terms
new deductions
new legal classification
new regulatory obligations
new payment truth
```

---

# 59. Compensation Document Identity

Every durable generated Compensation Document MUST retain:

```text
document identity
document type
Merchant Scope
Payee / relationship reference
source authority references
document revision
generation time
jurisdiction
template/rule version where material
content hash or equivalent immutable affinity
```

where applicable.

---

# 60. Document Regeneration

A new document generation caused by an authoritative correction MUST create a new document revision or replacement relation.

The earlier document MUST remain historically attributable where retention requirements require it.

Main Street MUST NOT silently replace historical documents without traceability.

---

# 61. Document Delivery and Exposure

Generated Compensation Documents MUST follow purpose-bound Exposure.

A Payee MAY receive only their permitted documents.

Merchant administrators require separate Actor Authorisation for compensation/payroll document access.

Possession of a document file MUST NOT imply permission to expose unrelated Payroll or merchant data.

---

# 62. Worker/Payee Self-Service

Main Street MAY expose bounded self-service allowing a Payee to:

```text
complete required compensation/payroll setup
review proposed agreement
accept/acknowledge agreement
review variations
view own payslips
view own payment/deduction statements
view own pay history
download permitted own documents
```

This self-service MUST NOT expose:

```text
another Payee's compensation
merchant-wide payroll totals without authority
customer data
orders
bookings
inventory
unrelated merchant operational data
```

---

# 63. Document Generation Does Not Determine Treatment

Hard invariant:

> **Main Street MUST determine the supported Jurisdiction Pay Treatment before generating a document whose legal meaning depends on that treatment.**

Rejected:

```text
merchant selects "self-employed"
        ↓
Main Street generates contractor agreement
        ↓
contract wording becomes classification evidence
```

Accepted:

```text
relationship facts
        ↓
jurisdiction treatment resolution
        ↓
appropriate supported agreement/document
```

---

# 64. Merchant Experience

Ordinary merchant setup SHOULD resemble:

```text
Add Payee
        ↓
What work will they do?
        ↓
How will the amount be agreed/calculated?
        ↓
Record existing verbal/written terms
or generate an agreement
        ↓
Main Street/Payee complete jurisdiction facts
        ↓
Main Street resolves treatment
        ↓
Work occurs
        ↓
amount becomes payable
        ↓
Main Street calculates applicable deductions/obligations
        ↓
merchant approves
        ↓
documents + filing + payment/reconciliation
```

The merchant SHOULD NOT normally choose statutory categories that Main Street can deterministically resolve.

---

# 65. Exception-Driven Operation

Merchant Compensation operation SHOULD be exception-driven.

Example:

```text
Current payment cycle

12 Payees

✓ 8 ready
! 2 need payable-time approval
! 1 agreement variation awaiting acceptance
! 1 jurisdiction treatment requires evidence
```

Such `ready` labels are projections only.

Underlying predicates remain explicit.

---

# 66. Professional and Regulatory Eligibility Is Separate

Professional/licensing eligibility MUST NOT be inferred from pay treatment.

For example:

```text
security licence
engineering registration
trade qualification
right-to-work evidence
```

may affect work eligibility.

They do not by themselves determine:

```text
PAYROLL
NON_PAYROLL
amount owed
pay rate
payable time
```

Likewise, Payroll participation does not prove permission to perform regulated work.

---

# 67. Security & Facility Management Falsification

Scenario:

```text
security/facilities merchant
employees described as self-employed
no guaranteed hours
scheduled shifts
pay from approved time worked
four-weekly pay
```

Required behaviour:

```text
public website/enquiry
        → independent

schedule
        → work-allocation fact

actual time
        → time evidence

approved payable time
        → compensation input

"self-employed"
        → merchant description only

relationship facts
        ↓
Jurisdiction Pay Treatment
        ↓
PAYROLL or NON_PAYROLL
```

The merchant label MUST NOT override the resolved treatment.

---

# 68. Construction Contractor Falsification

Scenario:

```text
construction merchant
engineers / trades / contractors
project-based work
progress-based compensation
mostly negotiated verbally
merchant wants cumulative payment tracking
```

Required behaviour:

```text
Compensation Relationship
        ↓
Compensation Terms

either:
    milestone/formula
or:
    separately negotiated progress amount
        ↓
merchant-approved Compensation Amount
        ↓
Jurisdiction Pay Treatment
        ↓
PAYROLL or NON-PAYROLL
        ↓
applicable withholding/reporting
        ↓
payment
        ↓
Compensation Ledger
```

Main Street MUST support this without creating a construction-specific compensation aggregate.

---

# 69. Agreement Generation in the Construction Case

For merchant-recorded verbal terms:

```text
Project: Riverside Apartments
Payee: Ade Engineering
Terms: £3,000 after foundation completion
Source: verbal agreement
```

Main Street MAY generate an agreement reflecting those terms.

If Payee acknowledgement is obtained:

```text
merchant-recorded evidence
        ↓
generated agreement
        ↓
Payee acknowledgement
        ↓
stronger agreement evidence
```

The generated agreement does not retroactively prove facts not acknowledged.

---

# 70. Payment Statement in the Construction Case

Where a Non-Payroll regulatory regime requires payer-side deductions, Main Street SHALL distinguish:

```text
gross amount
deduction basis where different
statutory deduction
net amount
regulatory liability/remittance
```

and generate the applicable jurisdiction document from those authoritative facts.

---

# 71. Supported-Jurisdiction Requirement

This architecture applies to every jurisdiction Main Street deliberately activates for Workforce Compensation.

Potential markets include:

```text
United Kingdom
European jurisdictions
United States
Canada
Australia
New Zealand
Japan
South Korea
other deliberately supported jurisdictions
```

Those examples do not create one executable geographic category.

---

# 72. Jurisdiction Support Gate

A jurisdiction MUST NOT be presented as fully supported for Workforce Compensation unless the declared support scope validates:

```text
relationship-treatment resolution
supported Payroll relationships
supported Non-Payroll relationships
payer-side Non-Payroll obligations
new-relationship bootstrap
concurrent/changing relationship behaviour
supported compensation mechanisms
supported pay cadences
calculation rules
regulatory reporting
acknowledgement/rejection
correction/replacement
remittance where supported
payment where supported
agreement generation requirements
payslip requirements
Non-Payroll payment/deduction documents
document-delivery requirements
privacy
historical reconstruction
merchant-facing simplicity
```

Partial jurisdiction support MUST be represented explicitly as partial.

---

# 73. Country-Specific Rules Remain Country-Specific

Differences such as:

```text
employment-status tests
contractor tests
social-security rules
authority ruling mechanisms
contractor withholding
pensions
insurance
government APIs
required declarations
mandatory agreement clauses
payslip contents
document deadlines
```

remain inside jurisdiction-specific contracts.

They MUST NOT continually expand the generic Workforce Compensation core.

---

# 74. Payroll Precision Gate

Before Payroll activation in a jurisdiction, Main Street MUST validate:

```text
authoritative rule sources
effective-date semantics
official/certified test vectors where available
rounding
currency
pay frequencies
starter/new-hire rules
leaver rules
concurrent-employment cases where material
year boundaries
corrections
authority updates
historical reproducibility
required software recognition/certification
```

---

# 75. Non-Payroll Precision Gate

Before regulated Non-Payroll support is activated, Main Street MUST validate:

```text
which relationships qualify
payer-side withholding
reporting
contributions
remittance
Payee declarations
exemptions
authority instructions
required statements/documents
corrections
payment evidence
```

`NON_PAYROLL` MUST NOT default to:

```text
pay gross amount and do nothing else
```

unless the jurisdiction contract explicitly establishes that treatment.

---

# 76. Compensation Document Precision Gate

Before compensation-document generation is production-supported in a jurisdiction, Main Street MUST validate where applicable:

```text
document type required
mandatory contents
who must receive it
issue timing
electronic-delivery legality
acceptance/signature semantics
correction/reissue rules
retention period
language/localisation requirements
jurisdiction-required explanatory text
template/rule versioning
```

---

# 77. AI Boundary

AI MAY:

```text
interpret merchant descriptions
structure candidate Compensation Terms
explain jurisdiction questions
explain agreement clauses
summarise payroll/payment documents
summarise regulatory errors
identify missing facts
```

AI MUST NOT:

```text
authoritatively classify employment/pay treatment
fabricate agreement terms
calculate statutory deductions
fabricate time/progress evidence
alter rates autonomously
approve compensation
approve Payroll
mark filing accepted
mark remittance complete
mark funds settled
```

---

# 78. Historical Reproducibility

For every approved compensation execution, Main Street MUST retain enough evidence to establish:

```text
Merchant Scope
Payee
Compensation Relationship
Compensation Terms Revision
terms provenance
agreement/document revision where relevant
Jurisdiction Pay Treatment Revision
work/progress/pay evidence
Compensation Amount
payable approval
calculation version
Payroll calculation where applicable
regulatory execution
payment evidence
generated pay documents
corrections/replacements
```

A later rules/software/template update MUST NOT make the historical basis unknowable.

---

# 79. Failure Semantics

Main Street MUST distinguish:

```text
BUSINESS REJECTION
VALIDATION REJECTION
TREATMENT UNRESOLVED
AUTHORISATION REJECTION
ENTITLEMENT REJECTION
OPERATIONAL INELIGIBILITY
PROVIDER / AUTHORITY FAILURE
UNCERTAIN EXTERNAL OUTCOME
DOCUMENT GENERATION FAILURE
DOCUMENT DELIVERY FAILURE
```

Document-generation or delivery failure MUST NOT retroactively invalidate an already committed compensation/payment fact unless the jurisdiction makes document issuance a prerequisite to that exact operation.

---

# 80. Controlled Degradation

Provider, government or document-service outage MUST NOT rewrite authoritative Compensation truth.

If calculation is still safe:

```text
calculation MAY continue
external execution/document generation may remain pending
```

If required authoritative inputs are unavailable and no lawful fallback exists:

```text
affected operation MUST fail closed
```

---

# 81. Cross-Capability Boundaries

```text
Identity
    → principal/person reference

MS-PROT-074 Workforce Access
    → merchant membership/access
    → not compensation/employment status

Scheduling
    → planned work

future Timekeeping
    → worked-time / payable-time evidence

Project/Work Progress
    → project-progress evidence

Workforce Compensation
    → amount owed / payable

Jurisdiction Pay Treatment
    → administration route

Payroll
    → employer-payroll execution

Provider Fulfilment
    → implementation mechanism

Money
    → currency-qualified value

Audit
    → audit evidence

Privacy
    → personal-data lifecycle

Credentials
    → provider/authority secrets

Notification
    → delivery

Projection / Exposure
    → presentation and audience access
```

No edge creates shared mutable authority.

---

# 82. Entitlement and Readiness Separation

Main Street MUST distinguish:

```text
Semantic Applicability
Commercial Entitlement
Actor Authorisation
Treatment Resolution
Calculation Readiness
Provider Readiness
Regulatory Execution Readiness
Payment Readiness
Document Generation Readiness
Document Delivery Readiness
Surface Exposure
```

No single `enabled`, `active`, `available` or `ready` flag may collapse those dimensions.

---

# 83. Falsification Review

| Hypothesis | Result |
|---|---|
| Everyone performing work belongs in Payroll | **REJECTED** |
| Merchant selecting `self-employed` is sufficient | **REJECTED** |
| Contract wording alone establishes treatment | **REJECTED** |
| Hourly pay proves employee status | **REJECTED** |
| Invoice proves self-employment | **REJECTED** |
| No guaranteed hours proves self-employment | **REJECTED** |
| Scheduling alone proves employment | **REJECTED** |
| Same merchant must treat all Payees identically | **REJECTED** |
| Same person has one global status | **REJECTED** |
| Non-Payroll means no payer obligations | **REJECTED** |
| Compensation must be salary/hour formula | **REJECTED** |
| Project progress automatically determines payment | **REJECTED** |
| Verbal terms cannot be tracked | **REJECTED** |
| Negotiated progress amounts cannot be authoritative | **REJECTED** |
| Payment must pass through Main Street to be tracked | **REJECTED** |
| Merchant-recorded payment proves bank settlement | **REJECTED** |
| Generated contract can manufacture self-employed status | **REJECTED** |
| Generated agreement itself owns Compensation truth | **REJECTED** |
| Payslip may be manually edited to alter Payroll | **REJECTED** |
| Every Non-Payroll Payee receives a payslip | **REJECTED** |
| Universal statutory-scheme ontology is required | **REJECTED** |
| Payroll provider must own country calculation | **REJECTED** |
| Country support can mean employee Payroll only | **REJECTED for full Workforce Compensation support** |

The design survives the security/facilities, construction-project, hourly, salary, milestone, negotiated, Payroll and regulated Non-Payroll cases without business-type-specific compensation semantics.

---

# 84. Trade-Off Decisions

## 84.1 Payroll-only versus Workforce Compensation

**Rejected:** Payroll as the universal parent capability.

**Chosen:** Workforce Compensation above Payroll and Non-Payroll execution.

**Accepted consequence:** broader product responsibility, substantially better fit for real small-business work arrangements.

---

## 84.2 Universal employment ontology versus jurisdiction treatment

**Rejected:** global employee/self-employed/contractor model.

**Chosen:** relationship facts + jurisdiction-qualified treatment.

**Accepted consequence:** more country-specific work, lower false-abstraction and misclassification risk.

---

## 84.3 Formula-only compensation versus calculated + negotiated amounts

**Rejected:** all compensation must derive from a predefined formula.

**Chosen:** deterministic and explicitly negotiated compensation are both first-class.

**Accepted consequence:** Main Street must retain establishment provenance.

---

## 84.4 Written-contract-only versus evidence-aware agreements

**Rejected:** formal written contract required before tracking compensation.

**Chosen:** verbal/written/generated terms are representable with explicit evidence strength.

**Accepted consequence:** Main Street must distinguish merchant-recorded terms from mutually acknowledged terms.

---

## 84.5 Document as authority versus document as governed representation

**Rejected:** PDF/document becomes separate business truth.

**Chosen:** documents derive from authoritative facts; agreement acceptance may establish the governed terms through the accepted mutation path.

**Accepted consequence:** corrections regenerate documents rather than editing rendered outputs.

---

## 84.6 Generic statutory ontology versus jurisdiction isolation

**Rejected:** universal tax/social-insurance/pension hierarchy.

**Chosen:** jurisdiction-qualified calculation components and obligations.

**Accepted consequence:** controlled jurisdiction-specific duplication is preferable to false semantic reuse.

---

# 85. Deferred Decisions

The following remain deferred until the applicable implementation slice requires them:

```text
MS-PROT-080-V11-DQ-001
First Workforce Compensation jurisdiction rollout.

MS-PROT-080-V11-DQ-002
Exact jurisdiction treatment-resolution mechanism.

MS-PROT-080-V11-DQ-003
Exact jurisdiction-specific fact/evidence schema.

MS-PROT-080-V11-DQ-004
Exact Java/persistence representation.

MS-PROT-080-V11-DQ-005
Exact Timekeeping and payable-time authority.

MS-PROT-080-V11-DQ-006
Exact project/work-progress owner and internal contract.

MS-PROT-080-V11-DQ-007
Exact calculation engines/libraries/providers.

MS-PROT-080-V11-DQ-008
Exact regulatory APIs, credentials and certification.

MS-PROT-080-V11-DQ-009
Exact payment/remittance rails.

MS-PROT-080-V11-DQ-010
Exact correction/off-cycle rules per jurisdiction.

MS-PROT-080-V11-DQ-011
Exact cross-border/treaty/social-security treatment.

MS-PROT-080-V11-DQ-012
Exact jurisdiction retention/privacy policies.

MS-PROT-080-V11-DQ-013
Exact personal self-service API/authentication model.

MS-PROT-080-V11-DQ-014
Exact merchant/Payee onboarding wording.

MS-PROT-080-V11-DQ-015
Exact generic professional/licensing capability.

MS-PROT-080-V11-DQ-016
Commercial packaging and entitlement.

MS-PROT-080-V11-DQ-017
Exact product semantics for partial versus full jurisdiction support.

MS-PROT-080-V11-DQ-018
Exact digital-signature/e-signature technology and assurance requirements per jurisdiction.

MS-PROT-080-V11-DQ-019
Exact document rendering/storage technology.

MS-PROT-080-V11-DQ-020
Exact jurisdiction document templates and legally required clauses/content.
```

No implementation MAY silently fill these semantic gaps.

---

# 86. Supersession Effect

MS-PROT-080 v1.1 completely supersedes MS-PROT-080 v1.0.

The following v1.0 principles survive:

```text
workforce access ≠ compensation/payroll
Schedule ≠ payable time
compensation terms are effective-dated
Payroll is jurisdiction-qualified
providers do not own Main Street semantics
direct authority integration is permitted
calculation ≠ approval ≠ filing ≠ settlement
transport success ≠ regulatory success
personal pay data requires bounded privacy
personal self-service remains bounded
Payroll remains post-MVP
historical evidence is not destructively rewritten
```

The following v1.0 generic architecture is withdrawn:

```text
generic Payroll-owned WorkforceEngagement
generic universal working-time classification
generic StatutoryPayrollScheme
generic statutory-scheme applicability graph
PayrollParticipant as root relationship for all compensated people
```

They are replaced by:

```text
CompensationRelationship
CompensationTermsRevision
CompensationAmount
JurisdictionPayTreatmentRevision
Payroll Route
Non-Payroll Route
jurisdiction-qualified Regulatory Obligations
Compensation Documents
```

---

# 87. Implementation-Readiness Boundary

Approval of MS-PROT-080 v1.1 establishes the generic architecture only.

It does NOT by itself authorise production implementation.

A concrete jurisdiction slice remains blocked until:

```text
jurisdiction selected
        ↓
declared support scope established
        ↓
treatment-resolution contract completed
        ↓
Payroll and Non-Payroll obligations validated
        ↓
compensation mechanisms validated
        ↓
agreement/document requirements validated
        ↓
calculation implementations validated
        ↓
regulatory execution designed
        ↓
privacy/security reviewed
        ↓
failure/correction behaviour validated
        ↓
implementation sequencing explicitly promotes the slice
```

Payroll/Workforce Compensation remains post-MVP unless later sequencing authority changes that scope.

---

# 88. Conformance Conditions

A conforming implementation MUST demonstrate:

```text
merchant labels do not determine treatment
same person can have different treatment across relationships
Payroll and Non-Payroll remain distinct
Non-Payroll can retain payer obligations
calculated and negotiated compensation are supported
verbal-agreement evidence is representable
merchant-recorded terms are distinguished from Payee-confirmed terms
project progress does not automatically equal payable compensation
time-based compensation works independently of Payroll treatment
scheduled time does not silently become payable time
four-weekly does not collapse into monthly
statutory calculation is deterministic
AI has no classification/calculation authority
exact historical terms/rule affinity is retained
stale calculations cannot be approved
providers do not own Compensation semantics
external retries cannot silently duplicate legal/monetary effect
generated agreements do not manufacture classification
payslips derive from approved Payroll truth
Non-Payroll documents derive from authoritative Compensation/regulatory facts
document editing cannot mutate underlying authority
cross-merchant compensation data does not leak
professional eligibility remains separate
country support is not claimed beyond validated scope
```

---

# 89. Final Decision

> **Main Street Workforce Compensation owns the merchant-facing problem of determining, documenting, approving, administering and tracking what a merchant owes people or organisations that perform work. It does not assume that every Payee is an employee or belongs in Payroll.**

> **Compensation may arise from salary, time, commission, project milestones, project progress, explicit negotiated amounts or other registered supported mechanisms. Main Street preserves how each amount was established.**

> **Each Compensation Relationship is resolved under an explicit supported jurisdiction into a Payroll or Non-Payroll operational treatment using jurisdiction-qualified facts, evidence and determination mechanisms. Main Street then automates the applicable calculation, withholding, contribution, regulatory execution, payment evidence and reconciliation without requiring the merchant to become a tax or employment-status specialist.**

> **Main Street may generate agreements, variations, payslips, payment statements and other jurisdiction-qualified Compensation Documents from authoritative facts. Generated documents do not become competing business truth; where agreement acceptance establishes terms, it does so through the governed Compensation Terms mutation path.**

> **How much a Payee is owed, how that amount must legally be administered, what was actually paid, and what document represents those facts are separate semantic questions. Main Street automates all four without collapsing them.**

---

## Recommendation / Approval Record

**RECOMMENDATION: ACCEPT**

**Manual approval of this exact consolidated v1.1:** GRANTED on 5 September 2026.

### End of accepted MS-PROT-080 v1.1
