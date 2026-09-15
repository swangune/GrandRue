# MS-PROT-040 v1.2 — Initial Merchant Configuration Bootstrap & Serving-Deployment Admission Amendment

**Document ID:** MS-PROT-040  
**Version:** 1.2  
**Status:** **ACCEPTED after governed review, falsification, ambiguity review and manual approval**  
**Approved:** Manual approval on 26 August 2026  
**Amends:** MS-PROT-040 v1.0 and v1.1 within the scope defined below  
**Authority type:** Merchant Configuration lifecycle / activation design authority  
**Governed by:** `DESIGN-RULES.md` v2.1 and `DOCUMENT-GOVERNANCE.md`  
**Depends on:** MS-PROT-021 v1.3; MS-PROT-022 v1.5; MS-PROT-040 v1.0 + v1.1; MS-PROT-054; MS-PROT-056 v1.2; MS-PROT-062; MS-PROT-071 v1.1; MS-PROT-072; MS-PROT-076; ADR-012; ADR-013; ADR-014  
**Closes:** MS-PROT-079 Target 3 — Initial merchant configuration bootstrap  
**Purpose:** Complete the production path from an established Merchant Account and complete merchant-authorised initial configuration intent to the first authoritative active Merchant Configuration, while proving that the exact semantic/RCP context is admissible by the current serving deployment before activation commits.

---

## 1. Governing Decision

Main Street SHALL establish the first active Merchant Configuration through:

```text
established OPEN Merchant Account
        +
current Merchant Controller
        ↓
complete Initial Configuration Intent
        ↓
exact ordinary new-configuration Semantic Registry Release
        ↓
immutable first Configuration Revision
    baseRevision = ABSENT
        ↓
deterministic validation
        ↓
exact Resolved Configuration Package
        ↓
impact analysis
        ↓
exact merchant approval
        ↓
SERVING-DEPLOYMENT ADMISSION
        ↓
atomic first activation
        ↓
post-commit reactions
```

The new hard rule is:

> **A valid and approved Configuration Revision MUST NOT become active until Main Street proves that the current ordinary serving deployment can materialise its exact Semantic Registry Release and can execute every new-activity semantic execution contract required by its exact Resolved Configuration Package.**

This evidence does not become Merchant Configuration authority.

---

## 2. Why MS-PROT-040 Owns This Decision

MS-PROT-040 already owns:

```text
Configuration Revision
approval
activation
current active revision
supersession
first-activation concurrency
```

ADR-013 owns semantic-definition materialisation evidence.

ADR-012 owns executable-support evidence.

Therefore:

```text
ADR-013 evidence
        +
ADR-012 evidence
        ↓
MS-PROT-040 activation precondition
```

is accepted.

Rejected:

```text
new ADR owns whether configuration becomes active
```

because that would create competing activation authority.

---

## 3. Explicit Non-Goals

This amendment does not design:

```text
onboarding questions
onboarding sequencing
AI inference
business-description interpretation
merchant-profile capture
Merchant Location
frontend screens
dashboard behaviour
commercial pricing
trial UI
provider-health resolution
production API shape
```

Targets 4–6 remain responsible for onboarding, Merchant Profile/Location and AI inference.

This amendment consumes a complete configuration intent; it does not decide how that intent was discovered.

---

## 4. Initial Configuration Intent

An **Initial Configuration Intent** is:

> A complete candidate expression of the merchant's supported operating needs supplied by a governed upstream source for creation of the first Merchant Configuration Revision.

It is not authoritative configuration.

Possible future upstream sources include:

```text
structured onboarding
merchant correction
AI-assisted inference
administrative migration path
```

subject to their own accepted authority.

The Initial Configuration Intent MUST NOT itself:

```text
activate capabilities
govern runtime
establish commercial entitlement
publish a storefront
become semantic authority
```

---

## 5. No Automatic Placeholder Configuration

Merchant Account establishment does not automatically create an active Merchant Configuration.

The following is rejected:

```text
Merchant Account created
        ↓
automatically activate
"basic/default/empty merchant"
```

merely to make the account executable.

MS-PROT-071 already keeps Merchant Account establishment distinct from Merchant Configuration.

A Merchant Account may therefore validly exist as:

```text
Merchant Account = ESTABLISHED
Active Configuration = ABSENT
```

until a valid first configuration is ready.

---

## 6. Initial Configuration Must Represent the Complete Supported Model

The first configuration MUST represent the merchant's complete supported operating model to the extent Main Street currently supports it.

It MUST NOT be deliberately reduced because:

```text
merchant is on Free tier
trial has not started
merchant has not paid
merchant may later upgrade
```

MS-PROT-056 expressly makes complete business configuration precede commercial gating.

Therefore:

```text
merchant business need
        ↓
complete supported configuration
        ↓
commercial entitlement evaluated separately
```

remains mandatory.

A genuinely minimal merchant may still have a minimal configuration.

Minimal does not mean placeholder.

---

## 7. Ordinary New-Configuration Semantic Release

For initial production, Main Street SHALL maintain one exact:

**Ordinary New-Configuration Semantic Release Reference**

for ordinary new merchant configuration.

It identifies one immutable Semantic Registry Release.

Example:

```text
Ordinary New-Configuration Release
    = R21
```

It MUST NOT mean:

```text
latest
highest version
newest deployed
whatever registry is in memory
```

---

## 8. Ownership of the Release Reference

The Ordinary New-Configuration Semantic Release Reference is a **platform semantic-release administration fact**.

It does not transfer semantic ownership to Merchant Configuration.

MS-PROT-054 remains authoritative over Semantic Registry Release identity and the support dimensions:

```text
NEW_CONFIGURATION_VALIDATION
NEW_BUSINESS_ACTIVITY
EXISTING_COMMITMENT_EXECUTION
HISTORICAL_INTERPRETATION
```

The ordinary reference merely identifies which exact currently admitted release is used for new ordinary configuration creation.

---

## 9. Release Admission Requirements

A release may be the ordinary new-configuration target only while it is permitted for both:

```text
NEW_CONFIGURATION_VALIDATION
AND
NEW_BUSINESS_ACTIVITY
```

A release that remains available solely for:

```text
EXISTING_COMMITMENT_EXECUTION
or
HISTORICAL_INTERPRETATION
```

MUST NOT be used for a new merchant configuration.

---

## 10. Merchant and AI Cannot Select Semantic Release Authority

For the ordinary initial configuration path:

```text
merchant
AI
onboarding category
frontend
```

MUST NOT nominate arbitrary semantic releases.

They provide configuration intent within the semantic space supplied by the platform's exact ordinary new-configuration release.

This prevents:

```text
merchant asks for R14
because an old feature worked differently
```

from becoming semantic-release authority.

---

## 11. Point at Which Release Affinity Becomes Immutable

An onboarding or inference draft may evolve while non-authoritative.

When Main Street materialises the first Configuration Revision for deterministic validation, that revision SHALL pin the exact Semantic Registry Release.

After that:

```text
C1 @ R21
```

remains:

```text
C1 @ R21
```

even if the ordinary platform reference later advances to R22.

MS-PROT-040 v1.1's revision immutability survives unchanged.

---

## 12. Platform Reference Advances During Onboarding

If:

```text
onboarding begins while ordinary release = R21

later

ordinary release = R22
```

and no immutable Configuration Revision has yet been established, the upstream onboarding/configuration-proposal process may construct its eventual revision against R22 according to its governing authority.

This amendment does not make an onboarding draft a release-affined authoritative object.

---

## 13. Platform Reference Advances After Revision Creation

If:

```text
C1 @ R21
```

already exists and the platform moves ordinary new configuration to R22, C1 MUST NOT silently mutate.

At activation, R21 is re-evaluated.

If R21 remains permitted for:

```text
NEW_CONFIGURATION_VALIDATION
+
NEW_BUSINESS_ACTIVITY
```

C1 may continue toward activation.

If R21 no longer satisfies those predicates:

```text
C1 cannot activate
```

A new candidate/migration must follow accepted authority.

---

## 14. First Configuration Revision

The first authoritative candidate revision SHALL satisfy:

```text
merchantIdentifier = exact established Merchant Account
baseRevision = ABSENT
semanticRegistryRelease = exact selected release
```

The revision remains subject to all existing MS-PROT-021/MS-PROT-040 rules.

No special mutable “bootstrap configuration” object is introduced.

---

## 15. Deterministic Validation and Package Production

The first Configuration Revision enters the ordinary deterministic pipeline:

```text
exact immutable revision
        +
exact pinned Semantic Registry Release
        ↓
static validation/resolution
        ↓
exact immutable RCP
```

The RCP remains derived state.

It does not become Merchant Configuration authority.

MS-PROT-022 already defines that static/runtime distinction.

---

## 16. No AI in Compilation

Initial configuration bootstrap MUST NOT introduce AI reasoning into deterministic compilation.

Therefore:

```text
AI proposes configuration
        ↓
immutable revision
        ↓
deterministic compiler
```

is valid.

Rejected:

```text
compiler encounters missing value
        ↓
ask AI what merchant probably means
        ↓
compile guessed answer
```

---

## 17. New-Activity Execution Requirement Set

Every activation candidate SHALL have a deterministically derivable:

**Configuration New-Activity Execution Requirement Set**

derived from its exact RCP.

The set represents the semantic execution contracts that the activated configuration may require for **new activity**.

---

## 18. Requirement-Set Contents

The requirement set includes, where applicable:

```text
statically applicable new-activity operations
required operation effects
required cross-capability participant contracts
required relationship/effect contracts
other ADR-012 execution contracts
necessary for those configured operations
```

Example:

```text
Order configuration
        ↓
ordering.commit contract
+
required Inventory claim contract
+
required Money obligation contract
```

must be covered as a coherent execution requirement where those participants are required by accepted semantics.

ADR-012 already requires participant/effect support, not merely entry-handler support.

---

## 19. What the Requirement Set Excludes

The requirement set MUST NOT encode live facts such as:

```text
current actor privileges
current Commercial Entitlement
current Provider Readiness
current provider health
current inventory quantity
current bookings
current appointments
current customer identity
current session
projection freshness
current Surface Exposure
```

Those remain contextual authorities.

This preserves MS-PROT-022 v1.5's static/dynamic boundary.

---

## 20. Why Commercial Entitlement Does Not Reduce the Requirement Set

A configured operation cannot be omitted from executable-support validation solely because today's plan does not commercially permit its use.

Commercial Entitlement can change independently without configuration recompilation.

Therefore:

```text
semantically applicable in active RCP
+
currently not entitled
```

does not justify activating a configuration whose implementation cannot execute that operation if entitlement later becomes effective.

Commercial packaging does not define the merchant model.

---

## 21. Serving Deployment Admission Snapshot

The production platform SHALL expose an immutable technical view called a:

**Serving Deployment Admission Snapshot**

for one ordinary serving generation.

Conceptually:

```text
ServingDeploymentAdmissionSnapshot
{
    generationIdentifier
    semanticMaterialisationEvidence
    executableSupportEvidence
}
```

This is technical deployment evidence.

It does not own semantic meaning, merchant configuration, business state or capability authority.

---

## 22. Generation Identity

Every serving admission snapshot SHALL have a stable immutable generation identity.

Example:

```text
serving-generation-1042
```

The generation identity allows Configuration Activation to prove:

> The deployment state I validated immediately before commit is still the deployment state against which this activation commits.

Exact persistence/control-plane representation remains downstream.

---

## 23. Initial Ordinary Serving Topology

ADR-013 establishes a uniform ordinary serving cohort for initial production.

Therefore Target 3 assumes:

```text
ordinary merchant traffic
        ↓
one current ordinary Serving Admission Generation
        ↓
homogeneous semantic materialisation contract
```

Compatibility-aware multi-cohort routing remains future scope.

---

## 24. Serving-Deployment Admission Predicate

Before activation may commit, Main Street SHALL establish all of:

```text
A. Candidate's exact Semantic Registry Release remains
   permitted for NEW_CONFIGURATION_VALIDATION.

B. Candidate's exact Semantic Registry Release remains
   permitted for NEW_BUSINESS_ACTIVITY.

C. Candidate's exact Semantic Registry Release is present
   in the current serving generation's ADR-013
   Deployment Semantic Materialisation Set.

D. Current serving generation has valid ADR-012
   Executable Support Evidence covering every contract
   in the candidate's Configuration New-Activity
   Execution Requirement Set.

E. The serving-generation evidence is still current
   at the activation commit boundary.
```

All five are mandatory.

---

## 25. Materialisation Does Not Prove Execution

The following remains invalid:

```text
R21 bundle exists
        ↓
therefore C1 @ R21 can activate
```

Correct:

```text
R21 materialisable
        +
all required exact execution contracts supported
        ↓
deployment admission may succeed
```

ADR-013 explicitly separates these predicates.

---

## 26. Release-Wide `supported=true` Is Insufficient

The serving admission gate MUST NOT accept:

```text
R21 supported = true
```

as sufficient evidence where one configured contract is unsupported.

Example:

```text
R21 Publication supported
R21 Booking supported
R21 Ordering handler supported

BUT

required Inventory participant contract unsupported
```

Result:

```text
configuration requiring that Ordering path
    → NOT ADMISSIBLE
```

Support remains ADR-012 contract-scoped.

---

## 27. Provider Readiness Is Not an Activation Predicate

Serving-deployment admission MUST NOT collapse with Provider Readiness.

A configuration may have:

```text
valid registered fulfilment binding
+
provider currently unavailable
```

without becoming semantically invalid.

Static binding validity is RCP responsibility.

Current provider health/readiness is contextual runtime authority.

Therefore:

```text
Provider temporarily offline
    ≠ automatically reject Merchant Configuration activation
```

unless a separate accepted capability-specific authority establishes a stronger requirement.

---

## 28. Merchant Account Preconditions for Ordinary First Activation

Ordinary first activation requires:

```text
Merchant Account exists
Merchant Account lifecycle = OPEN
no applicable effective merchant-wide Suspension
current Merchant Controller exists
```

A `CLOSING` Merchant Account cannot originate ordinary new activity, and a `CLOSED` account cannot perform ordinary Configuration activation.

An effective account-wide suspension prevents ordinary privileged merchant mutation.

---

## 29. First-Configuration Approval Authority

For the ordinary first configuration, merchant approval SHALL come from the **current Merchant Controller**.

This intentionally narrows the more general later Configuration approval rule.

Rationale:

```text
first configuration
    establishes the merchant's initial
    complete Main Street operating model
```

At this point Main Street MUST NOT depend on later workforce delegation merely to establish the merchant's first authoritative configuration.

Future delegation of first-configuration approval would require explicit accepted authority.

---

## 30. Controller Change Before Activation

If Controller A approves the first configuration and Controller authority transfers to B before first activation:

```text
A's historical approval
    remains historical evidence

BUT

first activation requires approval
from current Controller B
```

The exact immutable revision need not change merely because Controller identity changed.

A new current-Controller approval may bind to that same revision if it is otherwise still valid.

---

## 31. Authentication Assurance for First Activation

Ordinary first activation is a high-impact Controller operation.

The activating Controller SHALL satisfy ADR-014's recent strong authentication requirement applicable to high-impact Controller operations.

The first configuration may therefore require a recent phishing-resistant authentication/step-up rather than relying solely on an old long-lived session.

Authentication remains distinct from configuration authority.

---

## 32. Approval Is Not Activation

Even if a later UI expresses both in one user gesture:

```text
"Approve and go live"
```

the backend contract remains:

```text
merchant approval commits
        ≠
configuration activation commits
```

A technical deployment-admission failure MUST NOT fabricate activation success.

Likewise an exact already-committed approval need not be discarded merely because one technical activation attempt failed, provided the revision and governing approval remain valid.

---

## 33. Complete First-Activation Preconditions

MS-PROT-040 v1.1's activation contract is amended for ordinary first activation.

`ActivateConfigurationRevision` may commit the first activation only if:

```text
1. Merchant Account exists.

2. Merchant Account lifecycle permits ordinary new activity.

3. No applicable account-wide Suspension blocks the operation.

4. Revision belongs to that Merchant Account.

5. baseRevision = ABSENT.

6. currentActiveRevision = ABSENT.

7. Revision is immutable.

8. Exact pinned Semantic Registry Release remains admitted
   for NEW_CONFIGURATION_VALIDATION.

9. Same release remains admitted for NEW_BUSINESS_ACTIVITY.

10. Deterministic validation succeeded.

11. Exact RCP exists and matches revision + semantic release.

12. No applicable blocking impact finding remains.

13. Current Merchant Controller approved this exact revision.

14. Initiating principal is the current Merchant Controller.

15. Applicable recent high-assurance authentication is satisfied.

16. Current Serving Deployment Admission Snapshot
    materialises the exact semantic release.

17. ADR-012 support covers the complete
    Configuration New-Activity Execution Requirement Set.

18. Serving-generation concurrency/fencing predicate holds.

19. Logical activation-request idempotency predicate holds.
```

No undefined `ready` flag may replace these conditions.

---

## 34. Deployment-Admission Check Applies Beyond First Activation

Although Target 3 is the initial-configuration target, the deployment-support invariant is not unique to first activation.

Therefore all later Configuration activation/reinstatement must also consume the applicable serving-deployment admission predicate.

Otherwise a later configuration could introduce unsupported semantics after startup and violate ADR-013 for exactly the same reason.

Existing MS-PROT-040 replacement/reinstatement rules otherwise survive.

---

## 35. First-Activation Atomicity

The existing first-activation CAS remains:

```text
candidate.baseRevision = ABSENT
AND
currentActiveRevision = ABSENT
```

Both predicates must hold at commit.

The new admission predicates do not weaken this rule.

Two competing first configurations cannot both become current.

---

## 36. Serving-Generation Concurrency Problem

A precheck alone is insufficient.

Rejected:

```text
check deployment generation G1
    → supports candidate

deployment switches to G2
    → does not support candidate

configuration activation commits
```

This would produce an active but unsupported configuration.

---

## 37. Serving-Generation Fence

Activation must bind its admission evaluation to one exact Serving Deployment Admission Generation.

At commit:

```text
evaluated generation = current generation
```

must still hold.

If not:

```text
DEPLOYMENT_ADMISSION_CONFLICT
```

and activation MUST NOT commit.

The request may be retried against the current generation.

---

## 38. Deployment Promotion Must Observe Active Configuration

The invariant is bidirectional.

Configuration activation must not outrun deployment support.

Likewise deployment promotion must not invalidate configurations already admitted for ordinary new activity.

Before a new ordinary serving generation becomes current, deployment admission SHALL establish:

```text
for every currently active Merchant Configuration
whose semantic context is still permitted for new activity

candidate serving generation
    materialises required exact release

AND

candidate serving generation
    supports required new-activity execution contracts
```

ADR-012's residual/historical no-orphan requirements remain additional obligations.

---

## 39. Governing Bidirectional Invariant

The core cross-boundary invariant is:

```text
CONFIGURATION → DEPLOYMENT

No configuration becomes active
without safe serving support.

DEPLOYMENT → CONFIGURATION

No serving generation becomes ordinary-current
if it would strand an active required configuration.
```

Neither side becomes semantic owner of the other.

---

## 40. Exact Fencing Mechanism Is Not Semantic Authority

The observable concurrency invariant is mandatory.

The implementation MAY later satisfy it using:

```text
database row/version
CAS
transactional control record
deployment-generation lock
serialisable coordination
another proven equivalent
```

The exact mechanism is implementation architecture, provided no unsupported committed interval becomes observable.

---

## 41. Activation Evidence

A successful Configuration Activation SHALL preserve enough provenance to establish at least:

```text
merchantIdentifier
configurationRevisionIdentifier
configurationRelease / RCP provenance
semanticRegistryReleaseIdentifier
activationRequestIdentifier
activating principal
activatedAt
replacedRevisionIdentifier?
servingDeploymentAdmissionGenerationIdentifier
deploymentAdmissionEvidenceReference
```

The exact storage representation is not mandated.

---

## 42. Deployment Admission Evidence Is Not Merchant Authority

The activation's deployment evidence means:

> The serving environment satisfied the required technical execution predicates when activation committed.

It does not mean:

```text
merchant chose this configuration
merchant was commercially entitled
provider was healthy
operation was authorised for every actor
```

Those remain separately owned facts.

---

## 43. Semantic-Release Admission Changes After Activation

A release may later cease to be permitted for new business activity under MS-PROT-054.

That does not rewrite or delete the Configuration Revision.

It may instead constrain future runtime execution according to the applicable semantic-support authority while existing commitments retain their required affinity.

Configuration history remains immutable.

---

## 44. Commercial and Trial Consequences

Successful first Configuration activation may produce post-commit facts/events consumed by the accepted commercial/trial architecture.

Existing implementation already treats first activation as evidence available to Commercial rather than Commercial ownership of Configuration.

Therefore:

```text
Configuration activation commits
        ↓
post-commit commercial/trial reaction
```

is valid.

Rejected:

```text
trial establishment must commit
inside first Configuration activation transaction
```

unless a future accepted invariant explicitly requires it.

---

## 45. Publication, Projection and Surface Consequences

Likewise:

```text
configuration activation
        ↓
post-commit projection/surface recomposition
```

does not make projection or storefront publication part of Configuration authority.

Projection refresh failure MUST NOT retroactively make the Configuration Activation uncommitted.

Existing MS-PROT-040 post-commit semantics survive.

---

## 46. Retry and Lost Acknowledgement

The existing activation idempotency rule remains.

If activation commits but acknowledgement is lost:

```text
same logical activation request
        ↓
return original committed activation
```

It MUST NOT:

```text
activate again
re-evaluate into a different historical result
rebind the original activation to a newer deployment generation
```

The committed outcome retains its original deployment-admission provenance.

---

## 47. Retry After Admission Conflict

If no activation committed because the serving generation changed:

```text
request
    → DEPLOYMENT_ADMISSION_CONFLICT
```

a retry may evaluate the same still-valid revision against the new serving generation.

This is not a new merchant configuration intent.

A new approval is unnecessary solely because the deployment generation changed, provided the exact revision and applicable approval remain valid.

---

## 48. Request Identity Cannot Change Intent

One logical activation request identity MUST NOT be reused for materially different:

```text
Configuration Revision
Configuration Release
Merchant Account
expected-current revision
```

or a conflicting already-committed activation intent.

Existing MS-PROT-059/MS-PROT-040 idempotency semantics survive.

---

## 49. Failure and Rejection Semantics

Target 3 adds the following explicit activation outcomes where recovery differs:

| Outcome | Meaning |
|---|---|
| `SEMANTIC_RELEASE_NOT_ADMITTED_FOR_NEW_CONFIGURATION` | Exact pinned release is no longer eligible to introduce a new Configuration |
| `SEMANTIC_RELEASE_NOT_ADMITTED_FOR_NEW_BUSINESS_ACTIVITY` | Release may exist historically but cannot establish new operational activity |
| `SEMANTIC_RELEASE_NOT_MATERIALISED_BY_SERVING_DEPLOYMENT` | Exact release is not available in current serving materialisation set |
| `EXECUTABLE_SUPPORT_INCOMPLETE` | One or more required new-activity contracts lack proven ADR-012 support |
| `DEPLOYMENT_ADMISSION_CONFLICT` | Serving admission generation changed across evaluation/commit fence |

Existing outcomes remain:

```text
VALIDATION_REJECTION
APPROVAL_REQUIRED
AUTHORISATION_REJECTION
ACTIVATION_CONFLICT
PACKAGE_MISMATCH
TECHNICAL_FAILURE_BEFORE_ACTIVATION_COMMIT
EXECUTION_UNCERTAIN
SUCCESS
```

These distinctions MUST NOT collapse into generic `FAILED` where caller recovery differs.

---

## 50. Failure Does Not Create Partial Authority

For every pre-commit failure:

```text
candidate may remain
approval may remain where still valid
RCP may remain
```

but:

```text
currentActiveRevision
```

remains unchanged.

For first activation:

```text
currentActiveRevision = ABSENT
```

remains true.

---

## 51. Technical Admission Is Not Business Validation

An activation candidate may be:

```text
semantically valid
merchant approved
```

yet fail because:

```text
current deployment cannot execute it
```

That is not evidence that the merchant's intent is semantically invalid.

Conversely, a technically capable deployment does not make an invalid merchant configuration valid.

---

## 52. Current Implementation Evidence

The current implementation already represents much of the accepted base contract:

```text
ConfigurationCompiler
ResolvedConfigurationPackage
ConfigurationPublication
ConfigurationReleaseActivation
approval authority
activation authorisation
base-revision CAS
semantic compatibility
activation idempotency
```

It does not yet represent the new serving-deployment admission gate.

Therefore current implementation is evidence of the gap, not authority for the missing answer.

---

## 53. Falsification

### 53.1 Merchant Account exists with no configuration

```text
Merchant exists
Active Configuration = NONE
```

Result: valid.

No placeholder configuration is manufactured.

**PASS**

### 53.2 Incomplete onboarding draft

The draft cannot produce a complete valid revision/RCP.

Result: no activation.

**PASS**

### 53.3 Platform advances from R21 to R22 mid-onboarding

No immutable revision yet exists.

Result: eventual candidate may use R22 under upstream authority.

**PASS**

### 53.4 Platform advances after C1 @ R21 exists

C1 remains R21.

If R21 is still admitted, it may proceed.

If not, C1 cannot activate and is not silently rewritten.

**PASS**

### 53.5 Release exists historically but is not in serving deployment

R17 is reconstructable from archive but not materialised by current ordinary serving cohort.

Result:

```text
SEMANTIC_RELEASE_NOT_MATERIALISED_BY_SERVING_DEPLOYMENT
```

No activation.

**PASS**

### 53.6 Release materialises but one configured contract is unsupported

R21 bundle exists, but required Inventory participant contract is unsupported.

Result:

```text
EXECUTABLE_SUPPORT_INCOMPLETE
```

No activation.

**PASS**

### 53.7 Provider is temporarily unhealthy

Static binding remains valid.

Result: Configuration may activate; Provider Readiness governs actual operation later.

**PASS**

### 53.8 Merchant has no paid entitlement

The complete business configuration remains valid.

Result: commercial gating does not shrink configuration.

**PASS**

### 53.9 Deployment changes after admission precheck

G1 supports candidate.

G2 replaces G1 before commit.

Result: generation fence detects change and returns `DEPLOYMENT_ADMISSION_CONFLICT`.

**PASS**

### 53.10 Two competing first configurations

Both have `baseRevision = ABSENT`.

One commits first.

Second observes a current revision and gets `ACTIVATION_CONFLICT`.

**PASS**

### 53.11 Approval exists but technical admission fails

Merchant approval remains valid evidence for unchanged revision.

Configuration does not activate.

**PASS**

### 53.12 Merchant suspended between approval and activation

Current account-wide restriction is re-evaluated.

Ordinary activation is rejected.

**PASS**

### 53.13 Merchant enters CLOSING

Ordinary new business activity is prohibited.

Initial activation does not proceed.

**PASS**

### 53.14 Release supports historical interpretation only

The release is available but not admitted for new business.

Result:

```text
SEMANTIC_RELEASE_NOT_ADMITTED_FOR_NEW_BUSINESS_ACTIVITY
```

**PASS**

### 53.15 Release-global support flag hides incompatible participant

`R21 supported=true` exists, but exact participant contract has no evidence.

Result: global flag is insufficient.

**PASS**

### 53.16 Trial post-commit handler fails

Configuration Activation already committed.

Result: Configuration remains active; downstream recovery handles required consequence.

**PASS**

### 53.17 Deployment attempts to drop support required by active configuration

Candidate deployment would strand an active configuration.

Result: serving-generation promotion is rejected.

**PASS**

### 53.18 AI supplies an arbitrary semantic-release identifier

AI cannot select release authority.

Result: ordinary platform release reference governs.

**PASS**

No falsifier requires:

```text
latest semantic release lookup
commercial gating inside compilation
Provider Readiness inside activation
AI inside deterministic compilation
RCP becoming merchant authority
deployment becoming semantic authority
```

---

## 54. Alternatives Considered

### A. Activation ignores deployment support

Rejected.

Creates active configurations the system cannot execute.

### B. Check only semantic bundle presence

Rejected.

Materialisability does not prove executable support.

### C. Check only release-wide executable support

Rejected.

ADR-012 support is contract-scoped.

### D. Check support only at first invocation

Rejected as the sole control.

It permits Main Street to knowingly activate an unusable merchant operating model.

Invocation-time checks remain defence-in-depth/runtime enforcement.

### E. Allow arbitrary merchant release selection

Rejected for ordinary bootstrap.

Semantic-release administration is platform authority.

### F. Always compile against `latest`

Rejected.

`latest` is not stable semantic identity.

### G. Put deployment checks inside the compiler

Rejected.

The compiler owns deterministic static configuration resolution; current deployment support is operational context.

### H. Put Provider Readiness into activation

Rejected.

Provider health changes independently of Configuration.

### I. Create a new activation ADR

Rejected.

Would duplicate MS-PROT-040 ownership.

---

## 55. Trade-Offs

The accepted proposal deliberately adds one production admission step before activation.

Cost:

```text
additional support evidence
deployment/configuration coordination
generation fencing
```

Benefit:

```text
no active-but-unexecutable merchant model
no semantic latest fallback
safe rolling deployment
explicit provenance
stronger production determinism
```

This is the correct trade for Main Street because Configuration Activation is the moment a merchant model becomes authoritative for new activity.

---

## 56. Deferred Question Catalogue

The following retained questions SHALL be catalogued in the canonical DDR with these stable identifiers.

| ID | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|
| `MS-PROT-040-V12-DQ-001` | Exact persistence/control-plane representation of the Ordinary New-Configuration Semantic Release Reference | Release/platform implementation architecture | Before production semantic-release administration is implemented |
| `MS-PROT-040-V12-DQ-002` | Exact storage/representation of Serving Deployment Admission Snapshots | Deployment architecture | Before production admission-control implementation |
| `MS-PROT-040-V12-DQ-003` | Exact executable-support manifest/evidence encoding | ADR-012 implementation architecture | Before production support evidence is materialised |
| `MS-PROT-040-V12-DQ-004` | Exact canonical identity/digest representation for a Configuration New-Activity Execution Requirement Set | Implementation architecture | During deterministic admission implementation |
| `MS-PROT-040-V12-DQ-005` | Exact database/control-plane locking, CAS or fencing mechanism between activation and deployment promotion | Concurrency implementation architecture | Before production implementation of the cross-boundary fence |
| `MS-PROT-040-V12-DQ-006` | Exact operator/automation workflow for advancing the ordinary new-configuration release | Release operations | Before routine production semantic-release promotion |
| `MS-PROT-040-V12-DQ-007` | Staged rollout with multiple new-configuration semantic targets or compatibility-aware serving cohorts | Future deployment architecture | When one homogeneous ordinary serving cohort becomes materially restrictive |
| `MS-PROT-040-V12-DQ-008` | Maximum age/revalidation policy for an approved but not-yet-activated revision where semantics have not changed | Security/operational policy | If long-lived pending configuration approvals become operationally material |
| `MS-PROT-040-V12-DQ-009` | Exact transport/API representation of the new deployment-admission rejection classes | Target 20 / API implementation | During Production API contract design |
| `MS-PROT-040-V12-DQ-010` | Future delegation of ordinary first-configuration approval/activation to non-Controller merchant administrators | Semantic/access design | Only if merchant evidence shows Controller-only bootstrap authority is materially inadequate |
| `MS-PROT-040-V12-DQ-011` | Exact retention period/storage strategy for deployment-admission provenance | Data protection/operations implementation | During Target 17 and production persistence implementation |
| `MS-PROT-040-V12-DQ-012` | Future merchant-selectable semantic release/channel | Product/semantic architecture candidate | Only if an explicit need arises for merchants to choose between simultaneously supported semantic release channels |

Traceability rules:

```text
identifier remains stable

DDR records current status

material semantic/architecture promotion
    → DESIGN-RULES

implementation detail
    → IMPLEMENTATION-RULES where appropriate

resolution records authority/evidence

no deferred item may weaken
the accepted activation invariant silently
```

---

## 57. Implementation Constraints

Later implementation MUST preserve:

```text
Merchant Account may exist without active Configuration

no automatic placeholder Configuration

complete supported model before commercial gating

exact semantic release, never latest

one immutable first Configuration Revision

baseRevision = ABSENT

deterministic exact RCP

merchant approval ≠ activation

current Controller authority for ordinary first activation

exact new-configuration/new-business release admission

RCP-derived new-activity execution requirement set

ADR-013 exact semantic materialisation

ADR-012 contract-scoped executable support

provider health outside static activation admission

commercial entitlement outside semantic activation

serving-generation fence

bidirectional configuration/deployment safety

atomic first activation

idempotent activation request

no partial authority on failure
```

---

## 58. Conformance Gate

Target 3 is Design-Closed only if the accepted corpus now guarantees:

```text
[ ] Merchant Account can exist without Configuration
[ ] no placeholder/default configuration is automatically activated
[ ] initial configuration represents complete supported business need
[ ] commercial entitlement does not determine semantic completeness
[ ] ordinary new configuration pins one exact release
[ ] latest/newest is not semantic authority
[ ] merchant/AI cannot arbitrarily select release identity
[ ] first revision has no base revision
[ ] revision remains immutable after validation submission
[ ] deterministic exact RCP is produced
[ ] RCP remains derived state
[ ] new-activity execution requirements are deterministically derivable
[ ] materialisation and executable support remain distinct
[ ] every required execution contract is proven before activation
[ ] Provider Readiness remains separate
[ ] first approval belongs to current Merchant Controller
[ ] first activation respects account OPEN/suspension constraints
[ ] first activation retains existing CAS rule
[ ] deployment support is revalidated against an exact generation
[ ] deployment change cannot race activation into unsupported authority
[ ] new serving generation cannot strand active required configurations
[ ] activation stores deployment-admission provenance
[ ] failure does not create partial Configuration authority
[ ] retries preserve logical intent
[ ] downstream trial/projection reactions remain post-commit
[ ] deferred questions have stable traceability identifiers
```

---

## 59. Amendment Effect

MS-PROT-040 v1.2 SHALL:

1. establish the complete ordinary first-configuration bootstrap envelope;
2. prohibit automatic placeholder activation;
3. establish exact ordinary new-configuration semantic-release selection;
4. require releases to remain permitted for both new configuration and new business activity;
5. establish the Configuration New-Activity Execution Requirement Set;
6. establish Serving Deployment Admission Snapshot / generation semantics;
7. make ADR-013 materialisation evidence an activation precondition;
8. make ADR-012 contract-scoped executable support an activation precondition;
9. require generation fencing across activation and deployment promotion;
10. establish the bidirectional active-configuration / serving-deployment support invariant;
11. require current Merchant Controller authority for ordinary first activation;
12. preserve commercial, provider, projection and runtime-access independence;
13. add explicit deployment-admission failure semantics;
14. catalogue `MS-PROT-040-V12-DQ-001` through `MS-PROT-040-V12-DQ-012`;
15. close **MS-PROT-079 Target 3** after formalisation and conformance; and
16. activate:

```text
Target 4
Onboarding engine
```

---

## 60. Acceptance Statement

The governing principle is:

> **Main Street does not make a merchant configuration authoritative merely because it is valid and approved. The first and every later activation must bind one exact configuration revision and RCP to an exact admitted semantic release, prove that the current serving deployment can materialise and execute the configuration's required new-activity contracts, and commit only while that deployment evidence remains current.**

This creates the production chain:

```text
merchant intent
        ↓
exact semantics
        ↓
immutable revision
        ↓
deterministic RCP
        ↓
merchant authority
        ↓
deployment capability proof
        ↓
atomic activation
```

**Review:** PASS  
**Falsification:** PASS  
**Ambiguity review:** PASS  
**Recommendation:** `ACCEPT`  
**Manual approval:** GRANTED on 26 August 2026
