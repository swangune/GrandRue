# MS-PROT-053 v1.2 — Production Data Lifecycle Evaluation, Disposition & Convergence Contract Amendment

**Document ID:** MS-PROT-053  
**Version:** 1.2  
**Status:** ACCEPTED  
**Approved:** 27 August 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** composite MS-PROT-053 through v1.1 only within production data-lifecycle evaluation, retention/use separation, disposition execution, projection/provider convergence and lifecycle-review triggering  
**Preserves:** contextual protection classification; explicit purposes; `RetentionRequirement`; `PersonalDataUseBasis`; existing disposition vocabulary; business-owner authority; MS-PROT-027 Projection/Exposure authority; provider/external authority; Audit ownership; recovery authority; jurisdiction/legal-policy separation  
**Depends on:** MS-PROT-023; MS-PROT-025; MS-PROT-026; composite MS-PROT-027 through v1.5; MS-PROT-031; MS-PROT-043; MS-PROT-045; MS-PROT-046; composite MS-PROT-048 through v1.5; MS-PROT-051; composite MS-PROT-053 through v1.1; MS-PROT-055; composite MS-PROT-057; composite MS-PROT-060; composite MS-PROT-061; MS-PROT-064; composite MS-PROT-066; MS-PROT-067; MS-PROT-069; MS-PROT-070; MS-PROT-072; MS-PROT-075 v1.1; MS-PROT-076; MS-PROT-079; MS-TAS-RECOVERY-001  
**Closes:** MS-PROT-079 Target 17 — Data protection lifecycle  
**Purpose:** Make Main Street’s accepted protection, purpose, retention, erasure, minimisation and durable-evidence semantics production-executable without transferring business ownership into a generic Privacy subsystem, treating retained data as automatically reusable, allowing stale lifecycle evaluations to authorise irreversible disposal, or allowing projections/providers/backups to retain independent use authority.

---

## 1. Governing Decision

The accepted MS-PROT-053 separation remains authoritative:

```text
BUSINESS LIFECYCLE
    ≠ DATA-PROTECTION CLASSIFICATION
    ≠ PURPOSE OF USE
    ≠ RETENTION REQUIREMENT
    ≠ EXPOSURE
    ≠ DISPOSITION
    ≠ HISTORICAL EVIDENCE
```

Target 17 adds the production execution contract:

```text
authoritative business/data facts
        +
current use-basis evidence
        +
current retention requirements
        +
historical-evidence obligations
        ↓
Data Lifecycle Evaluation
        ↓
RETAIN
DISPOSITION_DUE
UNRESOLVED
        ↓
owner-safe disposition where due
        ↓
derived/external convergence
```

Hard invariant:

> **The fact that Main Street still stores data does not itself authorise continued operational use, AI use, projection, publication or external disclosure.**

---

# Ownership

## 2. Data Protection Is Not a Second Business Owner

MS-PROT-053 governs:

```text
how data may be handled
why it may be used
why it may remain stored
when a disposition is required
what disposition class is authorised
```

The owning capability/domain still governs:

```text
what the business fact means
whether the fact occurred
its lifecycle
its invariants
its authoritative relationships
```

Therefore:

```text
Data Protection
    constrains business-owned data

Data Protection
    does not replace business ownership
```

## 3. No Universal Privacy Aggregate

Target 17 SHALL NOT introduce a universal business-semantic object such as:

```text
PrivacyCase
CustomerPrivacyRecord
DeletionCase
GDPRCase
ConsentProfile
DataLifecycleStatus
```

A technical progression record may exist downstream for execution/retry.

It MUST NOT become the source of business or legal truth.

---

# Data Lifecycle Contract

## 4. Registered DataLifecycleContract

Every data scope requiring material lifecycle handling SHALL be governed by an accepted, registered **DataLifecycleContract**.

Conceptually:

```text
DataLifecycleContract
{
    contractIdentity
    semanticOwner

    affectedDataScope

    protectionClassificationSource

    applicablePurposeSources

    useBasisSources where applicable

    retentionRequirementSources

    permittedDispositionSet

    ownerDispositionContract

    convergenceObligations

    reviewTriggers
}
```

This is a semantic contract.

It does not mandate one Java type, table or service.

## 5. Owner Qualification

A DataLifecycleContract MUST identify the authority that owns the affected data’s business meaning.

Examples:

```text
Ordering
Payment
Appointment
CustomerContext
Audit
Media
Notification
Merchant Profile
Credential authority
```

A generic Data Protection implementation MUST NOT infer business ownership from:

```text
table name
package name
foreign key
JSON field name
frontend route
AI classification
```

## 6. Data Scope Is Explicit

A lifecycle contract may govern:

```text
whole Operational Object

specific field

relationship/reference

provider evidence

historical snapshot element

media attachment

Audit evidence field

Notification payload component

projection field
```

It MUST NOT assume that the whole aggregate shares one retention outcome.

Example:

```text
Order
    identity                retained
    committed quantity      retained
    commercial terms        retained
    customer email          may later be minimised
    free-text delivery note may later be deleted/redacted
```

---

# Purpose and Use

## 7. Use Eligibility Is Independent from Retention

Production Main Street SHALL evaluate separately:

```text
MAY THIS DATA STILL BE USED
```

and:

```text
MUST / MAY THIS DATA STILL BE RETAINED
```

Therefore:

```text
retained
    ≠ reusable

retained
    ≠ publishable

retained
    ≠ AI-eligible

retained
    ≠ provider-transmittable
```

## 8. Use Evaluation

Where a requested operation materially uses PERSONAL or RESTRICTED data, the applicable execution path MUST establish the required current purpose/use authority before use.

Conceptually:

```text
requested use
    +
data scope
    +
purpose
    +
actor/execution authority
    +
current use basis where required
    +
Exposure where relevant
        ↓
USE_PERMITTED
USE_NOT_PERMITTED
UNRESOLVED
```

This need not be one universal runtime enum if existing owner contracts already provide equivalent determinate evidence.

The semantic requirement is determinacy.

## 9. Use Uncertainty Fails Closed

If required use authority is:

```text
UNRESOLVED
```

Main Street MUST NOT treat uncertainty as permission.

This applies to:

```text
ordinary operational reuse
public Exposure
AI processing
external provider transmission
marketing or promotional use
```

where a governing use basis is required.

## 10. Retention Does Not Preserve Original Purpose

Suppose customer contact data was originally collected for:

```text
OPERATIONAL_NEED
    respond to Enquiry
```

and that purpose ends.

If a separate requirement still requires bounded retention for:

```text
BUSINESS_COMMITMENT_EVIDENCE
```

then:

```text
retention = required

ordinary Enquiry-use authority = ended
```

The data may require:

```text
RESTRICT_USE
```

rather than unrestricted retention.

---

# Retention Evaluation

## 11. DataLifecycleEvaluation

For each governed data scope when lifecycle review is required, the authoritative result is exactly one of:

```text
RETAIN

DISPOSITION_DUE

UNRESOLVED
```

## 12. RETAIN

`RETAIN` means:

> At least one currently valid RetentionRequirement requires the evaluated data scope, or an accepted retained representation of it, to continue to exist.

It does not imply:

```text
current operational use
public exposure
merchant read access
customer read access
AI use
provider transmission
```

Those remain separately governed.

## 13. DISPOSITION_DUE

`DISPOSITION_DUE` means:

> No current requirement authorises continuation of the evaluated scope in its present form, and the accepted DataLifecycleContract identifies a valid disposition that may now be executed.

It MUST identify one accepted disposition from composite MS-PROT-053:

```text
DELETE
ANONYMISE
REDACT
RESTRICT_USE
RETAIN_MINIMAL_EVIDENCE
RETAIN_WITH_EXPLICIT_JUSTIFICATION
```

where semantically applicable.

## 14. UNRESOLVED

`UNRESOLVED` means:

> Main Street cannot establish sufficient current authority/evidence to decide whether the existing data representation may safely be retained or irreversibly disposed.

`UNRESOLVED` MUST NOT authorise destructive disposition.

It also MUST NOT restore or extend ordinary use merely because deletion cannot be proven safe.

Canonical:

```text
UNRESOLVED
    ↓
preserve against irreversible destruction
    ↓
restrict use/exposure where required authority
cannot be established
    ↓
review / reconcile
```

This is deliberately asymmetric.

## 15. Multiple Retention Requirements

One data scope may have:

```text
0..n active RetentionRequirements
```

Disposition may proceed only when all requirements governing retention of the current representation have been resolved consistently.

Example:

```text
OPERATIONAL_NEED
    ended

SECURITY_AUDIT
    active

LEGAL_OR_REGULATORY_OBLIGATION
    active
```

Result:

```text
do not delete required evidence
```

but ordinary operational use may still end.

## 16. Requirement Precedence Is Not One Global Priority Table

Target 17 SHALL NOT define a universal:

```text
LEGAL > SECURITY > BUSINESS > OPERATIONAL
```

priority order that rewrites specific requirements.

Instead, the evaluator computes the compatible retained representation satisfying all currently applicable requirements.

Where no safe compatible representation can be determined:

```text
UNRESOLVED
```

rather than inventing precedence.

---

# Terminal Representation

## 17. Current Representation vs Minimum Required Representation

Retention is evaluated against the data representation actually required.

Example:

```text
full customer profile
```

may no longer be required even where:

```text
proof that customer-associated transaction occurred
```

must remain.

Therefore:

```text
current full representation
    may be DISPOSITION_DUE

minimal evidence representation
    may be RETAIN
```

This is the production meaning of `RETAIN_MINIMAL_EVIDENCE`.

## 18. No History Falsification

Disposition MUST NOT cause a retained historical fact to become false.

Rejected:

```text
delete customer relationship evidence
    ↓
pretend Booking never occurred
```

Accepted:

```text
Booking existed
    remains true

unnecessary customer identifying attributes
    removed/minimised
```

## 19. No Evidence Inflation

The need to prove:

```text
Order O1 existed
```

does not automatically justify retaining:

```text
full customer profile
all messages
full provider payload
every address ever observed
all AI context
```

The narrowest representation satisfying the accepted evidence requirement is preferred.

---

# Production Evaluation Operation

## 20. `data-lifecycle.evaluate`

Canonical conceptual operation:

```text
data-lifecycle.evaluate
```

**Owner:** Data Protection lifecycle authority.

It does not mutate business data.

## 21. Evaluation Inputs

The evaluator receives or resolves:

```text
DataLifecycleContract identity

exact affected subject/data scope

evaluation time

semantic/configuration affinity where relevant

current owner-qualified business facts

current applicable use-basis evidence

current RetentionRequirements

historical evidence requirements

provider/external obligations where relevant
```

Client-provided lifecycle conclusions are never authoritative.

## 22. Evaluation Reads

The evaluator reads each required fact from the authority that owns it.

Examples:

```text
Order history
    → Ordering

Refund/provider evidence
    → Payment

Notification DeliveryAttempt
    → Notification

personal media use basis
    → Data Protection authority

Audit evidence requirement
    → Audit

Merchant Account closure
    → Merchant Account authority
```

Data Protection does not replicate those business facts merely to evaluate them.

## 23. Evaluation Purity

`data-lifecycle.evaluate` is logically read-only with respect to the affected business facts.

It MAY record bounded evaluation/audit evidence where separately required.

It MUST NOT directly:

```text
delete
anonymise
redact
rewrite capability-owned state
```

---

# Disposition Execution

## 24. Owner-Safe Disposition

A disposition requiring mutation of capability-owned authoritative data MUST execute through the owning authority.

Canonical:

```text
DataLifecycleEvaluation
    = DISPOSITION_DUE
        ↓
owner-qualified disposition instruction
        ↓
owning capability/data authority
        ↓
authoritative revalidation
        ↓
owner-safe mutation
```

## 25. No Generic Cross-Domain Delete

Rejected:

```text
PrivacyService.deleteCustomerEverywhere(customerId)
```

where the service directly mutates:

```text
Orders
Bookings
Appointments
Payments
Audit
Notifications
Inventory
```

Such a service would become an illicit second semantic owner.

## 26. OwnerDispositionContract

Every lifecycle scope requiring authoritative mutation SHALL have an owner-defined disposition contract sufficient to establish:

```text
owner

affected data scope

accepted disposition(s)

required principal/execution authority

preconditions

current retention/lifecycle evidence required

fields/facts preserved

fields/facts transformed/removed

atomicity boundary

idempotency

concurrency behaviour

success evidence

failure classifications
```

The concrete operation name may remain capability-specific.

## 27. Disposition Execution Principal

Ordinary lifecycle disposition is a trusted platform execution responsibility acting under accepted Data Protection authority and the owner-specific disposition contract.

It MUST NOT acquire:

```text
merchant Controller authority

staff privilege

customer authority

universal admin override
```

merely because the platform is applying a privacy lifecycle rule.

Where merchant or legal approval is materially required, that requirement must be explicitly supplied by the governing contract.

## 28. Final Revalidation

A previously computed `DISPOSITION_DUE` result is not permanent mutation authority.

Immediately before irreversible disposition, the owning operation MUST re-establish all material current predicates.

At minimum:

```text
same affected data scope

same relevant business/evidence state
or compatible expected revision

no newly active conflicting RetentionRequirement

required historical evidence remains preservable

requested disposition is still permitted
```

## 29. Concurrency

Suppose:

```text
T1:
    evaluation says DELETE

T2:
    new legally/materially valid
    retention requirement is established

T3:
    stale delete worker executes
```

The delete MUST fail/re-evaluate rather than destroy data under stale authority.

Exact mechanism may be:

```text
row/version check
optimistic concurrency
locking
CAS
equivalent
```

provided the semantic invariant is satisfied.

## 30. Idempotency

Each consequential disposition execution MUST have stable logical identity.

Conceptually:

```text
DataDispositionExecution
{
    executionIdentity
    lifecycleContract
    affectedDataScope
    requestedDisposition
    causation/evaluation reference
}
```

Retry of the same logical operation:

```text
same intent
    → same already-established result
```

It MUST NOT multiply semantic effects.

## 31. Changed Intent

If the required disposition materially changes:

```text
ANONYMISE
```

to:

```text
DELETE
```

that is a new logical disposition decision.

It MUST NOT silently reuse the prior operation identity.

---

# Disposition Semantics

## 32. DELETE

`DELETE` is permitted only when:

```text
no current requirement needs the governed representation

deletion does not falsify retained business/evidence truth

owner contract permits complete removal
```

Physical storage implementation is downstream.

## 33. ANONYMISE

`ANONYMISE` must produce a representation that no longer preserves the prohibited identifying association under the applicable Main Street contract.

It MUST NOT be represented merely by:

```text
isAnonymous = true
```

while retaining trivially re-identifying data in the same accessible context.

Exact transformation mechanism remains implementation scope.

## 34. REDACT

`REDACT` removes a bounded sensitive component while preserving surrounding required evidence.

Examples may include:

```text
remove free-text personal note

remove unnecessary address line

remove provider payload fragment
```

where the retained surrounding record remains legitimate.

## 35. RESTRICT_USE

`RESTRICT_USE` means:

```text
data remains stored
because retention is still authorised/required

ordinary operational reuse
is no longer authorised
```

Systems consuming the data MUST respect the restricted-use state/evidence.

It MUST NOT be implemented only as UI hiding.

## 36. RETAIN_MINIMAL_EVIDENCE

This disposition preserves exactly the minimum historically necessary representation supported by accepted authority.

The preserved evidence may include:

```text
stable historical identity/reference
material business fact
material time
material authority/provenance
bounded relationship evidence
```

without retaining unrelated personal/sensitive payload.

## 37. RETAIN_WITH_EXPLICIT_JUSTIFICATION

This result may be used only while a concrete current RetentionRequirement explicitly authorises continuation of the existing representation.

It is not:

```text
keep it because maybe useful
```

and MUST retain traceable authority.

---

# CustomerContext and Business Records

## 38. CustomerContext Is Not a Universal Deletion Root

Deleting or minimising a CustomerContext MUST NOT automatically cascade physical destruction through unrelated capability history.

Canonical:

```text
CustomerContext
    current relationship/contact authority

Order
Booking
Appointment
Payment
etc.
    independent historical business authorities
```

Each owner determines the minimum retained relationship evidence it needs.

## 39. CustomerAccount Closure Is Separate

CustomerAccount/session/identity lifecycle does not automatically determine every merchant-scoped business-data disposition.

Conversely, data minimisation does not necessarily require destroying an active CustomerAccount.

Identity/account authority and retention authority remain distinct.

---

# Merchant Account Closure

## 40. Merchant Closure Does Not Mean Delete Everything

Merchant Account closure under MS-PROT-076 MAY end:

```text
new ordinary operations
public presence
merchant administrative access
```

without immediately ending every:

```text
business evidence requirement
security requirement
external reconciliation obligation
payment/refund requirement
audit requirement
legal/regulatory retention requirement
```

Target 17 therefore prohibits:

```text
MerchantAccount = CLOSED
    → DELETE ALL MERCHANT DATA
```

as a universal rule.

## 41. Closure Lifecycle Evaluation

Closure SHALL create or activate lifecycle review for affected merchant-scoped data.

Each affected DataLifecycleContract evaluates independently.

Results may legitimately differ:

```text
public profile
    → DELETE / minimise

historical Orders
    → retain minimal evidence

provider credentials
    → revoke/destroy secret material

Audit evidence
    → retain under bounded requirement

derived projections
    → remove/rebuild to closed state
```

---

# Notification Provider Data

## 42. Notification Business Evidence vs Payload Data

Composite MS-PROT-075 may require historical evidence that:

```text
NotificationIntent existed
Dispatch existed
DeliveryAttempt occurred
provider evidence was received
```

That does not automatically require indefinite retention of:

```text
full email body
full SMS body
customer email address
customer phone number
raw provider payload
```

Each data element follows its DataLifecycleContract.

## 43. Provider Payload Minimisation

Before externalising personal/restricted data through Notification or another provider path:

```text
purpose/use authority
+
necessary data scope
+
provider path authority
+
data minimisation
```

must be established.

Target 17 does not reopen Target-16 Provider Readiness semantics.

---

# External Providers

## 44. Local Disposition vs Provider Disposition

The existing rule survives:

```text
Main Street disposition
    ≠ provider-side disposition
```

When provider-side deletion/restriction is required and supported, Target 17 establishes a provider-convergence obligation.

## 45. ProviderDispositionObligation

Conceptually:

```text
ProviderDispositionObligation
{
    obligationIdentity
    affected provider/data context
    required provider-side outcome
    authoritySource
    correlation/provenance
}
```

This is a technical lifecycle obligation, not external-state truth.

## 46. Provider-Side Execution

Provider disposition executes through:

```text
accepted Provider Fulfilment / integration path
+
historical/current ProviderConnection as applicable
+
Provider Readiness where a new side effect is required
+
durable execution identity
+
MS-PROT-069 uncertainty semantics
```

The exact generic background mechanism remains Target 18.

## 47. No False Provider Deletion Claim

Until sufficient provider evidence establishes the required outcome:

```text
provider deletion/restriction
    = not proven complete
```

Local deletion MUST NOT be presented as proof of external deletion.

## 48. Unsupported Provider Deletion

If a provider contract cannot perform a disposition required by policy:

```text
PROVIDER_DISPOSITION_UNSUPPORTED
```

or equivalent must remain explicit.

The system MUST NOT silently classify the obligation as complete.

Product/legal/provider-selection consequences remain governed separately.

---

# Projection and Exposure Convergence

## 49. Authoritative Disposition Precedes Derived Convergence

Canonical:

```text
owner-safe authoritative disposition
        ↓
Projection/Exposure invalidation
        ↓
derived copies converge
```

A derived projection does not become the authority deciding whether source data may be disposed.

## 50. Exposure Fails Closed After Authority Loss

Once current source/use eligibility is no longer established:

```text
fresh Exposure
    MUST NOT continue
```

merely because a stale cache or projection still contains the prior value.

## 51. Derived Copies

Affected derived representations may include:

```text
projection tables
search indexes
caches
CDNs
media renditions
analytics projections
precomputed storefront representations
```

where those representations contain or expose the disposed/restricted data.

They must converge according to their Projection/Media/Exposure authority.

## 52. Convergence Does Not Mean Historical Recall

Target 17 does not claim technical recall of copies already:

```text
downloaded
emailed
screen-captured
exported
transmitted to independent external recipients
```

Main Street controls only the systems/copies under its authority and any provider disposition contract it can execute.

## 53. Projection Convergence Failure

Failure to converge a derived representation after authoritative restriction/disposition is an explicit operational condition.

It MUST NOT restore source use authority.

Conceptually:

```text
source disposition committed
projection convergence failed

source remains disposed/restricted
projection is stale/unsafe
```

Affected Exposure must fail closed where required.

---

# Audit

## 54. Disposition Auditability

Material lifecycle actions SHOULD be auditable where accountability/security/compliance requires.

Audit evidence may include:

```text
disposition execution identity
affected data-scope reference
disposition class
authority/requirement reference
time
execution outcome
```

Audit MUST NOT defeat the disposition by copying the removed data into the AuditRecord.

## 55. Audit Retention Is Independently Governed

Audit evidence follows its own RetentionRequirements.

An actor/data subject cannot selectively erase accountability evidence merely because related operational data was minimised, unless the governing lifecycle authority actually requires such disposition.

---

# Domain Events and Background Work

## 56. Domain Event Minimisation

Domain Events used to propagate lifecycle consequences MUST contain only the data needed for reaction.

Prefer:

```text
data-scope identity/reference
disposition execution identity
disposition class
correlation
```

rather than embedding the personal payload being removed.

## 57. Post-Commit Lifecycle Consequences

Where owner-safe disposition commits before all downstream convergence completes:

```text
authoritative disposition
    remains committed

projection/provider/background convergence
    becomes durable follow-on obligation
```

Consumer failure does not roll back the already-valid source disposition unless the owner contract explicitly requires atomic local consequences.

---

# Lifecycle Review Triggers

## 58. Lifecycle Review Is Triggered by Material Change

A DataLifecycleContract SHALL identify the material facts that require reevaluation.

Examples include:

```text
business commitment completion

CustomerContext relationship ending

merchant account closure

PersonalDataUseBasis becoming ineffective

retention review/end condition reached

external reconciliation completion

credential relationship ending

historical evidence obligation ending

provider disposition evidence arriving

manual/legal policy update where accepted
```

## 59. Time-Based Review

Where a RetentionRequirement contains a future:

```text
end condition
review time
expiry time
```

Main Street must be able to cause reevaluation when that condition becomes due.

Target 17 defines the semantic obligation.

Target 18 defines the generic durable scheduling/background mechanism.

## 60. Event-Based Review

Where an authoritative business event changes retention/use eligibility, the affected lifecycle scope MAY be queued for reevaluation.

The event does not itself dictate disposition unless its owning lifecycle contract makes that consequence deterministic.

## 61. Review Coalescing

Multiple triggers for the same data scope MAY be safely coalesced because final disposition authority always comes from current re-evaluation.

Therefore:

```text
review trigger
    ≠ pre-authorised disposition command
```

---

# Recovery / Backup

## 62. Backup Retention Is Not Business Retention

MS-TAS-RECOVERY-001 remains authoritative:

```text
backup retention
    serves infrastructure recovery

business retention
    serves data/business/evidentiary authority
```

Target 17 does not require immediate mutation of every immutable backup generation when live data is disposed.

## 63. Restored Data Does Not Regain Use Authority

A restored backup may contain data that was later:

```text
deleted
anonymised
redacted
restricted
exposure-withdrawn
```

Recovery MUST reconcile applicable post-target privacy/security state before affected use/Exposure resumes.

This does not rewrite the old backup artefact.

It governs promotion/use of restored state.

---

# AI

## 64. AI Use Is a Purpose

The existence of data in Main Street does not automatically authorise:

```text
AI training
AI fine-tuning
long-term profiling
AI memory
model evaluation
```

Those uses require an accepted purpose/use authority.

## 65. AI Cannot Create Retention Authority

AI MAY:

```text
identify candidate personal data
identify likely over-retention
prepare a lifecycle-review recommendation
summarise current lifecycle evidence
```

AI MUST NOT:

```text
invent RetentionRequirement
declare legal basis
extend retention
override disposition
declare provider deletion
infer irreversible anonymisation completed
```

---

# Failure Semantics

## 66. Required Failure Classes

Target 17 preserves at least:

```text
LIFECYCLE_CONTRACT_NOT_FOUND

LIFECYCLE_EVALUATION_UNRESOLVED

USE_NOT_PERMITTED

RETENTION_REQUIREMENT_CONFLICT

DISPOSITION_NOT_PERMITTED

DISPOSITION_STALE

DISPOSITION_IDENTITY_CONFLICT

OWNER_DISPOSITION_REJECTED

PROJECTION_CONVERGENCE_REQUIRED

PROVIDER_DISPOSITION_REQUIRED

PROVIDER_DISPOSITION_UNSUPPORTED

PROVIDER_NOT_READY

PROVIDER_EXECUTION_UNCERTAIN

AUTHORITATIVE_CONFLICT

TECHNICAL_FAILURE
```

Exact Java/HTTP representation remains downstream.

---

# Atomicity

## 67. No Universal Cross-Domain Privacy Transaction

Target 17 rejects one distributed transaction spanning:

```text
Ordering
Payment
Booking
Audit
Notification
provider APIs
search/cache
object storage
```

Lifecycle disposition uses the smallest accepted owner-local consistency boundary.

Cross-owner progression uses orchestration/durable work.

## 68. Required Local Atomicity

Where one owner disposition requires multiple local authoritative mutations to preserve an invariant, those mutations MUST be atomic.

Example:

```text
replace identifying relationship
+
establish minimal historical evidence
```

where committing one without the other would falsify owner history.

---

# Historical Affinity

## 69. Retention Authority Provenance

Where retention depends on historical policy, configuration, commitment or legal/evidentiary authority, the lifecycle evaluation must retain sufficient affinity to the authority that actually applied.

Current merchant configuration MUST NOT silently rewrite historical retention provenance.

## 70. DataLifecycleContract Version Affinity

A disposition executed under a particular accepted lifecycle contract must preserve enough provenance to determine which contract/rules justified it.

A later contract revision does not rewrite the fact that a prior authorised disposition occurred.

---

# Initial Production Read Architecture

## 71. No Privacy Read Platform by Default

Initial production SHALL use:

```text
owner queries
+
RetentionRequirement authority
+
PersonalDataUseBasis authority
+
request-scoped lifecycle evaluation
```

No initial requirement exists for:

```text
central customer privacy database
privacy data lake
Redis privacy cache
universal consent ledger
generic DataLifecycleStatus table
```

A later projection must satisfy MS-PROT-027.

---

# Falsification

## 72. Completed Order, Customer Identity No Longer Needed Operationally

Required:

```text
Order history survives
customer-identifying data may be minimised
ordinary use does not survive merely because evidence does
```

**PASS**

## 73. Active Legal/Evidence Retention, Operational Purpose Ended

Required:

```text
data retained
ordinary use prohibited
RESTRICT_USE or equivalent bounded representation
```

**PASS**

## 74. Unknown Retention Authority

Required:

```text
no irreversible delete
no renewed normal use/exposure
review required
```

**PASS**

## 75. New Retention Requirement Appears After Evaluation

Required:

```text
stale disposition fails final revalidation
```

**PASS**

## 76. Duplicate Disposition Execution

Required:

```text
same logical disposition
    → one semantic effect
```

**PASS**

## 77. CustomerContext Removed

Required:

```text
does not cascade-delete historical Order/Payment/Booking truth
```

**PASS**

## 78. Merchant Account Closed

Required:

```text
no universal DELETE ALL

each lifecycle contract evaluates independently
```

**PASS**

## 79. Payment Provider Evidence

Required:

```text
retain only evidence needed for Payment/reconciliation authority

do not retain full provider payload by default
```

**PASS**

## 80. Notification Provider Payload

Required:

```text
DeliveryAttempt history may survive

full message/endpoint/provider payload retained only
under applicable lifecycle requirement
```

**PASS**

## 81. Provider-Side Copy Exists

Required:

```text
local disposition
    ≠ external deletion proof

provider obligation remains until evidence resolves it
```

**PASS**

## 82. Provider Cannot Support Required Deletion

Required:

```text
explicit unsupported/reconciliation state
no false success
```

**PASS**

## 83. Personal Media Public Basis Ends

Required:

```text
public use ends
projection/cache converges
canonical source may survive under another valid purpose
```

**PASS**

## 84. AI Wants Historical Messages for Future Training

Required:

```text
AI usefulness alone creates no retention/use authority
```

**PASS**

## 85. Restored Backup Contains Previously Disposed Data

Required:

```text
restored bytes do not restore current use/exposure authority
post-target reconciliation occurs before protected/public use
```

**PASS**

## 86. Audit Requires Accountability

Required:

```text
bounded Audit evidence survives where required

removed personal payload is not copied wholesale
into Audit to defeat disposition
```

**PASS**

---

# Rejected Alternatives

## 87. Rejected Designs

Target 17 rejects:

```text
PrivacyService.deleteCustomerEverywhere()

one universal CustomerPrivacyRecord

universal consent=true

retained = reusable

stored = AI-eligible

merchant closed = delete everything

CustomerContext deletion = cascade business-history deletion

Audit = permanent copy of original payload

Domain Events = immutable PII archive

provider copy disappears because local row disappeared

stale projection = continued use authority

UNRESOLVED = permission to delete

UNRESOLVED = permission to continue use

one fixed platform retention period for all data

softDeleted boolean as complete privacy architecture

current merchant policy rewriting historical retention authority
```

---

# Deferred Scope

## 88. Deferred / Downstream

The following are deliberately not decided by Target 17:

```text
jurisdiction-specific statutory retention durations

formal legal advice / controller-processor classification

marketing-law implementation

cookie-consent mechanisms

subject-access-request UI/workflow

data portability/export API

exact deletion SQL

exact anonymisation algorithms

cryptographic erasure implementation

physical database partition/purge strategy

provider-specific deletion API/SDK

provider data-residency selection

international-transfer legal mechanisms

exact background scheduler/worker — Target 18

exact retry/backoff mechanics — Target 18

operational privacy dashboards — Target 19

reconciliation operator UI — Target 19

production privacy/API DTOs — Target 20

backup mechanics — existing MS-TAS-RECOVERY-001
```

---

# Conformance

## 89. Target-17 Conformance Criteria

A conforming implementation MUST prove:

```text
[ ] every materially governed lifecycle scope has
    explicit owner-qualified lifecycle authority

[ ] business ownership remains with the semantic owner

[ ] use eligibility and retention are evaluated separately

[ ] retention never automatically grants operational reuse

[ ] UNRESOLVED grants neither destructive authority nor
    ordinary use authority

[ ] disposition identifies an accepted MS-PROT-053 disposition

[ ] owner-safe mutation revalidates current authority

[ ] stale lifecycle evaluation cannot destroy data

[ ] dispositions are idempotent

[ ] concurrency cannot bypass newly established retention need

[ ] historical business facts are not falsified by minimisation

[ ] retained evidence is no broader than materially required

[ ] CustomerContext is not a universal cascade-deletion root

[ ] Merchant Account closure is not DELETE ALL

[ ] Notification/provider payloads follow separate
    lifecycle requirements from delivery evidence

[ ] local disposition does not claim provider deletion

[ ] provider-side obligations retain explicit completion evidence

[ ] Projection/Exposure converges after authoritative disposition

[ ] stale derived copies do not preserve use authority

[ ] Audit does not defeat minimisation

[ ] Domain Events do not become indefinite PII archives

[ ] backup recovery cannot resurrect current privacy authority

[ ] AI usefulness creates no use or retention authority

[ ] initial evaluation remains owner-query/request-scoped
    rather than requiring a speculative privacy read platform
```

---

# 90. Target-17 Closure Effect

If accepted:

```text
Protection classification
    → composite MS-PROT-053

Purpose / PersonalDataUseBasis
    → composite MS-PROT-053

RetentionRequirement
    → composite MS-PROT-053

Lifecycle evaluation
    → MS-PROT-053 v1.2

Business meaning
    → owning capability/domain

Disposition execution
    → owning capability/data authority

Projection/Exposure convergence
    → MS-PROT-053 v1.2
      + composite MS-PROT-027

Provider-side disposition
    → MS-PROT-053 v1.2
      + composite MS-PROT-048
      + MS-PROT-069

Audit evidence
    → MS-PROT-064
      constrained by composite MS-PROT-053

Backup/recovery
    → MS-TAS-RECOVERY-001
      constrained by composite MS-PROT-053

Lifecycle scheduling/retry machinery
    → Target 18
```

No Target-17 material semantic rule remains for implementation to invent.

Subject to governance propagation:

```text
Target 17 — Data protection lifecycle
    DESIGN-CLOSED

Target 18 — Events / background processes
    CURRENT ACTIVE TARGET
```

---

# 91. Governing Principle

> **Main Street shall treat data use, retention and disposition as independent governed questions. A business fact remains owned by the capability that gives it meaning, while Data Protection determines whether identifying or sensitive information may still be used, retained or must be minimised. Retention never grants unrestricted reuse; uncertainty grants neither destructive authority nor renewed use authority. Irreversible disposition must be revalidated and executed through the semantic owner without falsifying legitimate history. Derived projections and external providers must converge separately, and neither stale copies, backups, audit evidence nor AI convenience may manufacture continuing data authority.**

---

# 92. Acceptance Statement

MS-PROT-053 v1.2 closes the production execution gap left between the accepted cross-cutting privacy model and the now-complete Main Street backend domain authorities.

It does so without introducing a central privacy business owner, one universal deletion workflow, one universal consent record or business-category-specific privacy semantics.
