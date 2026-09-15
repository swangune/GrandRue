# MS-PROT-082 — Jurisdiction, Regulatory Knowledge & Regulatory Administration Model

**Document ID:** MS-PROT-082  
**Version:** 1.0  
**Status:** ACCEPTED  
**Approved:** 8 September 2026  
**Authority type:** Cross-capability semantic/design authority  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `designs/DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Depends on:** composite MS-PROT-040; composite MS-PROT-048 through v1.5; composite MS-PROT-051; composite MS-PROT-053 through v1.2; composite MS-PROT-055; composite MS-PROT-057 through v1.2; applicable accepted authorisation, projection/exposure and audit authorities  
**Amends:** None  
**Supersedes:** None  
**Closes:** Establishes previously absent jurisdiction/regulatory substrate required for governed multi-jurisdiction business administration; does not close Business Health/Financial Intelligence semantics  
**Purpose:** Define how Main Street represents jurisdictions, maintains versioned regulatory knowledge, resolves purpose-specific jurisdictional context, evaluates jurisdiction-dependent rules, tracks regulatory administrative requirements, composes specialist providers, and determines jurisdiction-specific capability support without requiring micro and small merchants to become tax, regulatory, software or analytics experts.

---

# 0. Fundamental Vision Conformance

## 0.1 Outcome

**VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

The proposal introduces substantial internal complexity because jurisdiction, effective dates, regulatory rules, external authorities, provider execution, historical affinity and failure uncertainty are intrinsically complex.

That complexity SHALL remain primarily internal to Main Street.

The merchant-facing objective remains:

```text
merchant supplies ordinary business reality
        ↓
Main Street resolves jurisdiction
        ↓
Main Street resolves applicable supported rules
        ↓
Main Street calculates / coordinates / explains
        ↓
routine work is handled where authorised
        ↓
merchant sees decisions or exceptions only
```

The architecture MUST NOT transfer regulatory complexity to merchants merely because Main Street requires that complexity internally.

## 0.2 Business-to-Software Translation

This authority directly supports the Fundamental Vision because a merchant SHALL NOT be required to identify:

```text
tax rates
statutory thresholds
jurisdiction codes
regulatory formulas
provider-specific classifications
rule effective dates
filing-system terminology
```

where Main Street can derive those matters from accepted business facts, supported regulatory knowledge or bounded specialist providers.

## 0.3 Administrative Compression

The model reduces:

- duplicate country-specific configuration;
- merchant lookup of regulatory rates;
- repeated entry of stable facts;
- manual deadline tracking;
- manual cross-system reconciliation;
- duplicated jurisdiction logic across Main Street capabilities; and
- routine professional administration where reliable software/provider fulfilment is available.

## 0.4 Target-Market Proportionality

The initial target remains micro and small businesses.

MS-PROT-082 SHALL NOT require Main Street to model:

- multinational corporate tax optimisation;
- enterprise transfer pricing;
- complex corporate-group consolidation;
- bespoke cross-border legal structuring; or
- regulatory scenarios outside an explicitly supported jurisdiction-purpose boundary.

Such cases SHALL be classified as unsupported or escalation-required rather than approximated.

## 0.5 Ownership Versus Integration

Main Street requires native ownership of:

```text
jurisdiction reference semantics
regulatory-rule provenance/versioning
jurisdiction-purpose support declarations
regulatory determination provenance
regulatory administrative requirement tracking
```

Main Street does NOT require native ownership of every statutory calculation or government execution mechanism.

Qualified external providers MAY fulfil bounded calculation, filing, submission or related responsibilities through composite MS-PROT-048.

## 0.6 Feature Admission

The proposal satisfies:

**Representation Test** — jurisdiction-dependent operation cannot be represented correctly without explicit jurisdiction and temporal applicability.

**Coordination Test** — Payroll, future finance, invoicing, merchant administration and other capabilities require a common governed regulatory substrate.

**Administrative-Compression Test** — without this substrate, merchants or individual Main Street capabilities must repeatedly determine rules manually.

---

# 1. Governing Decision

Main Street SHALL implement a **jurisdiction-aware regulatory substrate** that separates universal Main Street business semantics from jurisdiction-specific regulatory fulfilment.

Canonical:

```text
Main Street business semantics
        ↓
owner-qualified Regulatory Purpose
        ↓
authoritative business facts
        ↓
purpose-qualified Jurisdiction Context
        ↓
versioned Regulatory Rule Set Release
        ↓
native deterministic evaluation
        OR
provider-backed fulfilment
        ↓
Regulatory Determination
        ↓
Regulatory Administrative Requirement
where required
        ↓
authorised fulfilment / escalation
        ↓
evidence
```

Main Street SHALL NOT implement jurisdiction support as:

```text
if country == X
    use X behaviour
else if country == Y
    use Y behaviour
```

Main Street SHALL NOT maintain a universal mutable `countrySettings` object containing unrelated tax, payroll, payment, privacy, invoicing and business semantics.

Jurisdiction affects how an owner-qualified business or administrative responsibility is fulfilled.

Jurisdiction does not become the owner of that underlying business meaning.

---

# 2. Problem and Governed Scope

Main Street intends to operate across multiple countries and sub-country jurisdictions.

Material rules MAY vary by:

```text
country
constituent nation
state
province
territory
county
municipality
special regulatory area
transaction context
worker context
business-establishment context
```

A country identifier alone is therefore insufficient.

The architecture must support cases where:

- one rule applies nationally;
- another differs between subdivisions;
- another depends on a local authority;
- another depends on transaction destination;
- another depends on worker location;
- another depends on business structure;
- another applies only after a threshold or registration fact;
- an external provider performs the actual statutory calculation;
- a rule changes at a future effective date; or
- Main Street does not yet support the regulatory purpose in that jurisdiction.

MS-PROT-082 governs:

1. jurisdiction reference identity and relationships;
2. purpose-qualified jurisdiction resolution;
3. Regulatory Purpose integration contracts;
4. Regulatory Source Evidence;
5. Regulatory Rule Set Releases;
6. Jurisdiction Pack Releases;
7. jurisdiction-purpose support declarations;
8. native versus provider-backed regulatory evaluation;
9. immutable Regulatory Determinations;
10. Regulatory Administrative Requirement tracking;
11. regulatory fulfilment evidence;
12. historical rule affinity;
13. jurisdiction-support/serviceability distinctions;
14. launch-jurisdiction readiness boundaries; and
15. AI/merchant interaction boundaries for this scope.

---

# 3. Explicit Non-Goals

MS-PROT-082 does NOT establish a universal:

```text
tax engine
accounting ledger
general ledger
accounts-payable system
accounts-receivable system
financial-health model
business-health score
payroll engine
payment engine
invoice owner
legal-advice engine
government filing network
enterprise compliance suite
```

MS-PROT-082 does NOT own:

- Merchant Location;
- Merchant Configuration;
- Orders;
- Bookings/Appointments;
- Payment Obligations;
- Payroll semantics;
- Workforce semantics;
- customer commitments;
- invoice semantics;
- financial statements;
- merchant profitability;
- business-health interpretation; or
- provider business semantics.

Those truths remain with their accepted owning authorities.

A future Business Health / Financial Intelligence authority MAY consume MS-PROT-082 outputs but MUST NOT be silently created by this authority.

---

# 4. Architectural Boundary

MS-PROT-082 establishes the **Jurisdiction & Regulatory Administration Context**, abbreviated in explanatory diagrams as `JRA`.

JRA OWNS only the truths defined by this authority.

Canonical ownership relationship:

```text
Capability Owner
    OWNS business facts
          │
          │ owner-qualified references
          ▼
JRA
    OWNS regulatory knowledge,
         determinations and
         regulatory-administration tracking
          │
          ├── may request Provider Fulfilment
          │
          └── may publish derived evidence/projections
```

JRA MUST NOT become a universal business-facts database.

---

# 5. Canonical Terminology

## 5.1 Jurisdiction

A **Jurisdiction** is a stable Main Street reference to a governmental, statutory or regulatory territorial authority boundary that MAY participate in a registered Regulatory Purpose.

A Jurisdiction SHALL have:

- a stable `JurisdictionId`;
- a jurisdiction kind;
- a canonical display name;
- zero or more external standard/authority codes where available;
- explicit parent/relationship references where represented; and
- lifecycle/provenance sufficient to prevent identity reuse.

A Jurisdiction's parent relationship MUST NOT by itself establish regulatory applicability.

Example:

```text
Country
  └── subdivision
        └── local authority
```

is navigational/reference structure.

It does NOT mean every parent rule automatically applies to every child or vice versa.

## 5.2 Regulatory Purpose

A **Regulatory Purpose** is an owner-qualified contract identifying why jurisdiction-dependent regulatory evaluation is required.

Examples are explanatory only:

```text
indirect-tax treatment
payroll statutory calculation
invoice statutory content
business-registration requirement
filing deadline calculation
```

A Regulatory Purpose SHALL be semantically owned by the capability that requires the regulatory answer.

JRA SHALL index and evaluate registered Regulatory Purpose contracts but SHALL NOT redefine their business meaning.

Every Regulatory Purpose contract MUST define:

1. stable owner-qualified purpose identity;
2. owning capability;
3. regulatory subject type;
4. required authoritative inputs;
5. governing-time semantics;
6. expected typed output contract;
7. whether a Regulatory Administrative Requirement may result;
8. requirement-business-key semantics if one may result;
9. permitted support classes;
10. provider participation where permitted;
11. evidence requirements;
12. historical-affinity rules not already owned elsewhere; and
13. prohibited fallback behaviour.

Arbitrary untyped `Map<String,Object>` regulatory outputs are prohibited.

## 5.3 Regulatory Subject Reference

A **Regulatory Subject Reference** identifies an existing authoritative business object or actor relevant to a Regulatory Purpose.

It is a reference, not a new owner.

Examples MAY include references to:

```text
merchant
merchant location
transaction
worker/payee
pay period
business establishment
```

JRA MUST NOT infer identity from similar names, addresses or attributes.

## 5.4 Jurisdiction Context

A **Jurisdiction Context** is an immutable DERIVED result identifying the exact set of Jurisdictions relevant to one Regulatory Purpose, Regulatory Subject Reference and governing-time context.

A Jurisdiction Context MUST contain or reference:

- Regulatory Purpose identity;
- Regulatory Subject Reference;
- Merchant Scope;
- exact source-fact revisions;
- governing-time value;
- resolver version/release;
- resolved Jurisdiction identifiers; and
- resolution evidence/provenance.

Jurisdiction Context is purpose-qualified.

There is no universal authoritative:

```text
merchant.taxJurisdiction
```

that all capabilities may reuse without regard to purpose.

## 5.5 Regulatory Source Evidence

**Regulatory Source Evidence** is immutable evidence supporting the content or applicability of a Regulatory Rule Set Release.

Source evidence SHALL identify, as applicable:

- issuing authority/provider;
- source identifier;
- retrieved/published version;
- retrieval time;
- source effective information;
- integrity/hash evidence where available;
- reviewer/promotion provenance; and
- purpose for which the source was accepted.

Search-engine results, AI model memory and unattributed prose MUST NOT constitute authoritative Regulatory Source Evidence.

## 5.6 Regulatory Rule Set Release

A **Regulatory Rule Set Release** is an immutable, versioned and source-backed representation of regulatory rules applicable to one or more registered Regulatory Purposes and explicit jurisdiction scopes.

A release MUST identify:

- stable release identity;
- release version;
- supported Regulatory Purpose identities;
- explicit jurisdiction applicability scopes;
- effective interval;
- legal/temporal basis;
- source evidence;
- evaluation binding;
- output-schema versions; and
- regulatory-assurance boundary.

A released rule set MUST NOT be mutated in place.

## 5.7 Jurisdiction Pack Release

A **Jurisdiction Pack Release** is an immutable platform deployment bundle containing a coherent set of:

```text
Jurisdiction definitions
Regulatory Rule Set Releases
Jurisdiction Support Declarations
native evaluator bindings
provider-fulfilment bindings/references
source provenance
conformance evidence
```

for a defined deployment scope.

A Jurisdiction Pack Release is NOT:

- Merchant Configuration;
- a business capability;
- a legal entity;
- a universal country behaviour object; or
- an assertion that every Main Street capability is supported in that country.

## 5.8 Jurisdiction Support Declaration

A **Jurisdiction Support Declaration** is an authoritative platform statement describing the level of support Main Street offers for one:

```text
Regulatory Purpose
+
explicit Jurisdiction scope
+
Jurisdiction Pack Release
+
effective support interval
```

Support is purpose-specific.

A country SHALL NOT have one universal boolean `supported`.

## 5.9 Regulatory Determination

A **Regulatory Determination** is an immutable DERIVED record of the result of evaluating one Regulatory Purpose against:

```text
exact authoritative input revisions
+
exact Jurisdiction Context
+
exact governing-time context
+
exact Regulatory Rule Set Release
+
exact evaluator/provider evidence
```

A Regulatory Determination is Main Street's traceable evaluation result.

It is NOT itself legislation, legal advice or a replacement for an external authority's ultimate legal powers.

## 5.10 Regulatory Administrative Requirement

A **Regulatory Administrative Requirement** is a JRA-owned merchant-specific tracking record establishing that, according to an accepted Regulatory Determination, an administrative regulatory action must be performed, evidenced or resolved.

The qualified term MUST be used.

`Regulatory Administrative Requirement` MUST NOT be shortened normatively to generic `Obligation`, because accepted authorities already own domain-specific concepts including Payment Obligation and Booking residual obligation.

A Regulatory Administrative Requirement MUST NOT re-own:

- a Payment Obligation;
- a Payroll obligation;
- a Booking/Appointment commitment;
- an Order;
- a customer commitment; or
- another capability-owned business obligation.

## 5.11 Regulatory Fulfilment Evidence

**Regulatory Fulfilment Evidence** is immutable evidence concerning an attempt or outcome associated with a Regulatory Administrative Requirement.

Provider evidence is evidence.

Provider evidence does not automatically equal successful requirement satisfaction unless the applicable Regulatory Purpose contract defines a deterministic acceptance predicate that is satisfied.

---

# 6. Jurisdiction Identity and Hierarchy

## 6.1 Identity

Jurisdiction identity SHALL use a stable Main Street `JurisdictionId`.

External codes MAY be recorded as attributes.

External code change MUST NOT silently create or merge Main Street jurisdiction identity.

## 6.2 Hierarchy

A Jurisdiction MAY have zero, one or more governed structural relationships where the external reality requires them.

The architecture MUST NOT assume all regulatory systems form a simple tree.

## 6.3 Applicability

Rule applicability SHALL be explicit.

The following is prohibited:

```text
rule applies to parent
therefore
rule automatically applies to every descendant
```

unless the Regulatory Rule Set Release explicitly defines that applicability.

## 6.4 Special Regulatory Areas

Special regulatory treatment MAY be represented by an explicit Jurisdiction or explicit Rule Applicability Scope.

Business-type hard-coding SHALL NOT substitute for jurisdiction semantics.

---

# 7. Purpose-Specific Jurisdiction Resolution

## 7.1 Governing Rule

Jurisdiction SHALL be resolved for a named Regulatory Purpose.

Canonical:

```text
Regulatory Purpose
        +
Regulatory Subject
        +
authoritative business facts
        +
governing time
        ↓
Jurisdiction Context
```

## 7.2 Source Facts

JRA SHALL reference facts from accepted semantic owners.

JRA MUST NOT create an arbitrary merchant regulatory-facts bag to bypass missing semantic ownership.

If a required material fact has no accepted owner, the affected Regulatory Purpose is not implementation-ready for that scope.

## 7.3 Multiple Jurisdictions

A Jurisdiction Context MAY contain multiple Jurisdictions.

The model MUST support a purpose requiring national and sub-national context simultaneously.

## 7.4 Ambiguity

If materially different jurisdiction resolutions remain possible from the available facts, JRA MUST return an explicit incomplete/ambiguous resolution result.

JRA MUST NOT:

- choose the most likely jurisdiction using AI confidence;
- assume a jurisdiction from IP address;
- silently choose a default state/region; or
- fabricate missing registration/location facts.

---

# 8. Merchant-Fact Acquisition Rule

## 8.1 Governing Principle

> **The merchant supplies business reality; Main Street supplies regulatory interpretation.**

Main Street MAY ask a merchant for unresolved primary business facts that the merchant can reasonably know.

Examples MAY include:

```text
where the business operates
legal/business structure
whether workers are employed
a known registration identifier/status
a recurring business expense
```

subject to the applicable owning capability.

## 8.2 Derived-Fact Prohibition

Main Street MUST NOT ask a merchant to supply a derived regulatory fact that Main Street can reliably determine from:

- authoritative business facts;
- active regulatory knowledge; or
- supported provider fulfilment.

Examples of prohibited configuration questions where Main Street can determine the answer include:

```text
"What VAT/sales-tax rate applies?"
"What statutory percentage should we use?"
"What is the current government threshold?"
"Which formula should payroll use?"
```

## 8.3 Missing Facts

Where evaluation requires a missing primary fact:

```text
JRA identifies missing owner-qualified fact
        ↓
owning capability obtains/confirms fact
        ↓
JRA re-evaluates
```

JRA MUST NOT become the mutation owner merely for convenience.

---

# 9. Regulatory Rule Release Model

## 9.1 Immutable Releases

A Regulatory Rule Set Release SHALL be immutable after activation.

A change produces a new release.

## 9.2 Effective Interval

Each rule set MUST define an exact effective interval.

Unless a governing source requires another representation, intervals SHALL be interpreted as:

```text
effectiveFrom = inclusive
effectiveUntil = exclusive
```

The applicable temporal basis and timezone/civil-date interpretation MUST be explicit where material.

## 9.3 Governing Time

Rule selection MUST use the Regulatory Purpose's defined governing time.

Server wall-clock `now` MUST NOT substitute for the business/legal event time unless the Regulatory Purpose explicitly defines current time as governing.

## 9.4 Source Requirement

No production Regulatory Rule Set Release may become active without accepted Regulatory Source Evidence.

## 9.5 Overlap

Two active rule releases MUST NOT produce ambiguous authority for the same:

```text
Regulatory Purpose
+
Rule Applicability Scope
+
governing time
```

If legitimate legal overlap exists, the rule contract MUST define deterministic composition or precedence before activation.

## 9.6 AI Extraction

AI MAY:

- locate candidate changes;
- extract candidate rule material;
- compare source versions;
- draft explanations; and
- identify potentially affected rules.

AI MUST NOT:

- activate a rule;
- choose legal precedence;
- fabricate missing rule content;
- promote a candidate source to authority; or
- silently amend an active release.

---

# 10. Regulatory Assurance Boundary

A rule release may have an unknown legal end date while Main Street's assurance that the release remains current is time-bounded.

Each production support declaration MUST therefore identify an `assuranceUntil` boundary or an accepted equivalent freshness contract.

After assurance expires:

- the support declaration remains historical evidence;
- existing Regulatory Determinations remain historical evidence;
- new automatic determinations MUST NOT treat the expired assurance as current serviceability; and
- the affected purpose MUST be revalidated, provider-backed, assisted, escalated or made unsupported.

This prevents “no known effective end date” from becoming “Main Street assumes the rule remains correct forever.”

---

# 11. Regulatory Evaluation Modes

A Regulatory Purpose may be fulfilled through one of the following supported mechanisms.

## 11.1 Native Deterministic Evaluation

Main Street MAY evaluate a purpose natively when:

- an active applicable rule release exists;
- an accepted deterministic evaluator is bound to that release;
- all required authoritative facts are available;
- regulatory assurance remains valid; and
- no accepted authority requires external provider determination.

AI MUST NOT perform the authoritative calculation.

## 11.2 Provider-Backed Evaluation

Main Street MAY delegate bounded regulatory calculation or statutory fulfilment through composite MS-PROT-048.

The provider SHALL return evidence.

JRA SHALL validate the returned evidence against the Regulatory Purpose contract before creating or updating Main Street regulatory records.

Provider output MUST NOT mutate source business facts.

## 11.3 Assisted Handling

Main Street MAY collect facts, prepare evidence and explain the issue where full determination is unavailable.

`ASSISTED` support MUST NOT be represented to the merchant as a completed authoritative determination.

## 11.4 Professional Escalation

Where reliable automation is inappropriate, Main Street MAY identify the relevant problem, assemble available evidence and require specialist/human resolution.

Escalation is an explicit supported outcome.

Guessing is not.

---

# 12. Jurisdiction Support Classes

The canonical `JurisdictionSupportClass` vocabulary is:

```text
NATIVE
PROVIDER_BACKED
ASSISTED
ESCALATION_REQUIRED
UNSUPPORTED
```

## 12.1 NATIVE

`NATIVE` means Main Street possesses an active, source-backed, deterministic evaluation path for the exact Regulatory Purpose and jurisdiction scope.

## 12.2 PROVIDER_BACKED

`PROVIDER_BACKED` means Main Street supports the exact Regulatory Purpose through a registered bounded Provider Fulfilment responsibility.

Provider operational readiness remains a separate runtime dimension.

## 12.3 ASSISTED

`ASSISTED` means Main Street can provide a bounded preparatory workflow but MUST NOT claim completion of the authoritative regulatory determination or fulfilment.

## 12.4 ESCALATION_REQUIRED

`ESCALATION_REQUIRED` means the scope is recognised but requires an authorised specialist/human resolution before the affected regulatory responsibility can be treated as resolved.

## 12.5 UNSUPPORTED

`UNSUPPORTED` means Main Street does not support the Regulatory Purpose for that jurisdiction scope.

Main Street MUST fail explicitly rather than fabricate an answer.

---

# 13. Support-Class Separation

`JurisdictionSupportClass` MUST remain distinct from:

```text
Semantic Applicability
Commercial Entitlement
Actor Authorisation
Operational Eligibility
Provider Readiness
Input Completeness
Surface Exposure
```

Example:

```text
Purpose support = PROVIDER_BACKED
Provider readiness = unavailable
```

means:

```text
the platform supports the purpose in principle
but this operation cannot currently be fulfilled
```

It does NOT mean the jurisdiction has become unsupported.

Likewise:

```text
Purpose support = NATIVE
Merchant entitlement = absent
```

does not change regulatory support semantics.

---

# 14. Regulatory Determination Contract

Every Regulatory Determination MUST identify:

- `RegulatoryDeterminationId`;
- Merchant Scope;
- Regulatory Purpose;
- Regulatory Subject Reference;
- exact Jurisdiction Context;
- exact source-fact revisions;
- governing-time value;
- exact Regulatory Rule Set Release;
- evaluator identity/version or provider evidence;
- typed output schema/version;
- typed result;
- creation time;
- provenance; and
- determination fingerprint.

A Regulatory Determination MUST be immutable.

---

# 15. Determination Identity and Duplicate Prevention

A durable Regulatory Determination has its own identity.

Repeated evaluation of the exact same logical input SHALL be duplicate-safe.

The canonical determination fingerprint MUST include at least:

```text
Merchant Scope
Regulatory Purpose
Regulatory Subject Reference
governing-time key
source-fact revision vector
Jurisdiction Context identity/fingerprint
Rule Set Release
evaluator/provider-result identity
output-schema version
```

Equivalent retry MUST NOT create materially different authoritative results solely because the request arrived twice.

A changed source fact, governing period, rule release or accepted provider outcome produces a new determination.

---

# 16. Determination Result Classes

Evaluation MUST distinguish at least:

```text
DETERMINED
INPUT_INCOMPLETE
JURISDICTION_AMBIGUOUS
PURPOSE_UNSUPPORTED
ESCALATION_REQUIRED
PROVIDER_OR_TECHNICAL_FAILURE
```

These are operation-result classifications, not one overloaded business `status`.

`PROVIDER_OR_TECHNICAL_FAILURE` MUST NOT be interpreted as:

```text
rule does not apply
```

`INPUT_INCOMPLETE` MUST NOT be interpreted as:

```text
merchant is exempt
```

---

# 17. Regulatory Administrative Requirement Model

## 17.1 Establishment

A Regulatory Administrative Requirement MAY be established only where a valid Regulatory Determination and Regulatory Purpose contract establish that a merchant-specific administrative action is required.

## 17.2 Identity

Every requirement SHALL have:

- stable `RegulatoryAdministrativeRequirementId`;
- Merchant Scope;
- Regulatory Purpose;
- Regulatory Subject Reference;
- owner-qualified Requirement Business Key;
- source Regulatory Determination;
- current immutable revision; and
- provenance.

## 17.3 Requirement Business Key

The Regulatory Purpose owner MUST define the business key required to recognise the same logical regulatory requirement across recalculation/retry.

JRA MUST NOT invent a universal key from dates or descriptions.

## 17.4 Requirement Revision

Material changes such as:

- due-date changes;
- revised required action;
- changed evidence requirement; or
- changed applicable amount where the purpose owns such output

SHALL create a new immutable requirement revision.

Historical revisions remain retained subject to composite MS-PROT-053.

---

# 18. Requirement Lifecycle

The authoritative lifecycle SHALL be represented through explicit facts/revisions rather than one mutable generic `status`.

At minimum:

```text
Requirement Established
        ↓
zero or more revisions
        ↓
zero or more Fulfilment Attempts
        ↓
Requirement Satisfied
        OR
Requirement Withdrawn
```

## 18.1 Open

`OPEN` is a DERIVED lifecycle interpretation where the current requirement revision has neither an accepted Satisfaction fact nor a permitted Withdrawal fact.

## 18.2 Overdue

`OVERDUE` is DERIVED from:

```text
OPEN
+
current time exceeds exact due boundary
```

Overdue is not a separate authoritative requirement identity.

## 18.3 Satisfaction

A requirement becomes satisfied only through accepted Regulatory Fulfilment Evidence satisfying the purpose-defined evidence predicate.

A submitted request is not satisfaction.

## 18.4 Withdrawal

Withdrawal is permitted only when an accepted re-determination or authority outcome establishes that the unresolved requirement no longer applies.

A satisfied historical requirement MUST NOT be withdrawn merely to erase history.

## 18.5 Late Satisfaction

A requirement MAY be satisfied after its due time.

Late satisfaction does not erase historical evidence that the due boundary was missed.

---

# 19. Regulatory Fulfilment Authority

Each Regulatory Purpose capable of producing a Regulatory Administrative Requirement MUST identify the permitted fulfilment-authority class.

Canonical classes are:

```text
MERCHANT_ACTION_REQUIRED
INSTANCE_APPROVAL_REQUIRED
STANDING_MERCHANT_AUTHORITY_PERMITTED
PROFESSIONAL_ESCALATION_REQUIRED
```

## 19.1 Merchant Action Required

Main Street surfaces the requirement and cannot fulfil it on the merchant's behalf.

## 19.2 Instance Approval Required

Main Street MAY prepare the action but SHALL require explicit authorised approval for that requirement instance.

## 19.3 Standing Merchant Authority Permitted

An accepted capability MAY permit the merchant to grant bounded standing authority for recurring routine fulfilment.

Standing authority MUST:

- be explicit;
- be revocable prospectively;
- be scope-qualified;
- be auditable; and
- use an accepted authorisation/configuration path.

MS-PROT-082 does not itself create such authority grants.

## 19.4 Professional Escalation Required

Main Street SHALL NOT automate final resolution where the governing Regulatory Purpose requires professional judgement.

Main Street MAY assemble evidence and minimise professional handling effort.

---

# 20. Funds-Movement Boundary

A Regulatory Administrative Requirement requiring money to be paid does NOT itself authorise funds movement.

Canonical:

```text
Regulatory Requirement:
"£X must be paid"
        ≠
authority to transfer £X
```

Any actual payment/funds-movement semantics remain with their accepted owner/provider boundary.

Filing approval MUST NOT silently imply payment approval.

Payment approval MUST NOT silently imply filing approval.

---

# 21. Provider Fulfilment Boundary

Where a provider fulfils calculation, filing, submission or another regulatory responsibility:

```text
JRA / purpose owner
    OWNS Main Street meaning
            ↓ request
Provider Fulfilment
    performs bounded external responsibility
            ↓ evidence
JRA
    validates evidence
            ↓
determination / requirement evidence
```

Provider terminology MUST NOT replace Main Street terminology.

Provider failure MUST NOT:

- rewrite authoritative merchant facts;
- mark a requirement satisfied;
- fabricate a regulatory determination;
- silently change Support Class; or
- cause duplicate external execution through blind retry.

---

# 22. Provider Failure, Retry and Uncertain Outcome

## 22.1 Pre-Submission Failure

If provider execution is known not to have been submitted, a retry MAY occur according to the accepted provider contract.

## 22.2 Post-Submission Known Failure

If the provider confirms rejection/failure, JRA records failure evidence and follows the purpose-specific recovery path.

## 22.3 Uncertain Outcome

If Main Street cannot determine whether an externally material action occurred:

```text
outcome = uncertain
```

The Regulatory Administrative Requirement remains unsatisfied unless accepted completion evidence exists.

Main Street MUST reconcile before retry where duplicate external effect would be material.

## 22.4 Lost Acknowledgement

Loss of acknowledgement after a potentially committed external action MUST NOT trigger blind re-submission.

Stable provider idempotency identity or provider reconciliation evidence SHALL be used where available.

---

# 23. Concurrency

Concurrent evaluation or requirement establishment MUST NOT create duplicate logical regulatory requirements.

At the appropriate atomic boundary Main Street MUST guarantee that one Requirement Business Key within one Merchant Scope and Regulatory Purpose cannot acquire conflicting current revisions.

Concurrent evidence acceptance MUST NOT satisfy one requirement twice or attach one exclusive provider result to incompatible requirement identities.

The exact database mechanism remains an implementation detail provided these invariants hold.

---

# 24. Historical Affinity

Historical correctness is mandatory.

Every Regulatory Determination SHALL retain exact affinity to:

```text
source fact revisions
Jurisdiction Context
Rule Set Release
evaluator/provider evidence
governing time
```

A later rule release MUST NOT silently rewrite an earlier determination.

## 24.1 Future Rule Change

A future-effective rule MAY be installed before its effective time.

The old rule remains applicable to governing times within its accepted interval.

The new rule applies only according to its explicit effective semantics.

## 24.2 Retroactive Correction

A correction affecting an earlier period SHALL be represented by new corrective regulatory evidence/release and, where required, a new determination.

Whether an existing domain commitment may be adjusted remains the responsibility of the capability that owns that commitment.

JRA MUST NOT retroactively rewrite capability-owned commitments.

---

# 25. Jurisdiction Change and Business Evolution

A merchant may:

- move premises;
- open another operating location;
- employ someone elsewhere;
- begin selling in another supported area;
- change business structure; or
- become subject to a previously irrelevant regulatory purpose.

Such changes SHALL trigger re-evaluation only for affected Regulatory Purposes.

Main Street MUST NOT require the merchant to rebuild their complete operating configuration because jurisdictional context changed.

Unrelated capabilities SHALL remain unaffected where their serviceability and invariants remain satisfied.

---

# 26. Multi-Location and Multi-Jurisdiction Businesses

The architecture SHALL permit one Merchant Scope to participate in multiple Jurisdiction Contexts.

A merchant SHALL NOT be reduced to one permanent jurisdiction identifier.

Example pattern:

```text
Merchant
 ├── Location A → Context for Purpose X
 ├── Location B → Context for Purpose X
 └── Worker C   → Context for Purpose Y
```

The exact multi-location product administration UX remains outside this authority.

---

# 27. Jurisdiction Pack Activation

A Jurisdiction Pack Release MAY be activated only after:

1. all referenced rule/source artifacts resolve;
2. mandatory source evidence exists;
3. evaluator bindings are exact and versioned;
4. referenced provider roles exist where required;
5. support declarations are internally consistent;
6. effective intervals are non-ambiguous;
7. regulatory assurance boundaries exist;
8. required test vectors pass;
9. authorised platform promotion occurs; and
10. corpus/runtime compatibility validation passes.

Activation SHALL be atomic with respect to the exact immutable pack release.

Partial activation of an internally inconsistent pack is prohibited.

---

# 28. Capability-Specific Jurisdiction Serviceability

A capability requiring one or more Regulatory Purposes SHALL declare those requirements explicitly.

For a merchant operation:

```text
Semantic Applicability
        +
Regulatory Purpose requirements
        +
Jurisdiction Context
        +
Jurisdiction Support Declaration
        +
Rule assurance
        +
Provider Readiness where required
        +
Input Completeness
        ↓
Jurisdiction Purpose Serviceability
```

This serviceability decision MAY participate in the capability's operational admission.

It MUST NOT mutate Merchant Configuration merely because serviceability temporarily fails.

---

# 29. Loss of Serviceability

If an already-active merchant loses jurisdiction-purpose serviceability because:

- provider readiness fails;
- regulatory assurance expires;
- an external integration becomes unavailable; or
- support is prospectively withdrawn,

then:

1. existing authoritative business facts remain true;
2. historical determinations remain retained;
3. existing regulatory requirements remain visible;
4. unsupported new affected operations MUST fail closed or escalate according to their owner;
5. unrelated capabilities MUST remain available where safe; and
6. Main Street MUST NOT fabricate continuity.

Loss of serviceability SHALL NOT silently cancel existing commitments.

---

# 30. Launch-Jurisdiction Readiness

Main Street SHALL NOT declare a whole country universally supported merely because:

- payments work there;
- websites can be displayed there;
- a currency is supported; or
- one regulatory provider operates there.

Before commercial launch for an offered merchant slice, Main Street MUST establish a **Jurisdiction Readiness Matrix** containing:

```text
offered capabilities
        ×
required Regulatory Purposes
        ×
exact jurisdiction scopes
        ×
accepted Support Classes
```

Each mandatory cell MUST have a conforming support path.

---

# 31. Partial Country Support

Main Street MAY launch only in selected subdivisions of a country.

Example:

```text
Country A
    Region 1 → supported
    Region 2 → supported
    Region 3 → not yet supported
```

The merchant onboarding flow MUST reject or appropriately limit affected capabilities outside the declared scope.

Marketing/product claims MUST NOT represent partial territorial support as complete national support.

---

# 32. Jurisdiction Readiness Gate

For each initial launch jurisdiction scope, the deployment evidence MUST confirm:

```text
[ ] jurisdiction identities and mappings validated
[ ] address/location inputs available from accepted owners
[ ] offered business structures can be represented
[ ] mandatory regulatory purposes identified
[ ] rule sources established
[ ] effective-date behaviour tested
[ ] support class declared per mandatory purpose
[ ] provider roles/bindings ready where required
[ ] failure and uncertain-outcome paths tested
[ ] regulatory assurance boundaries defined
[ ] merchant-language explanations prepared
[ ] data-protection classification complete
[ ] escalation path exists where required
[ ] unsupported cases fail explicitly
```

A failed mandatory item blocks the affected jurisdiction-purpose launch scope.

It does not automatically block unrelated Main Street capabilities.

---

# 33. Universal Core / Localised Edge Rule

Main Street SHALL preserve universal semantic ownership wherever the business meaning is universal.

Canonical:

```text
UNIVERSAL MAIN STREET CORE
    Customer
    Order
    Booking
    Resource
    Workforce
    Payment
    Merchant Configuration
    etc.
           │
           │ owner-qualified regulatory request
           ▼
JURISDICTION / REGULATORY EDGE
    Jurisdiction Context
    Rule Release
    Regulatory Determination
    Provider Fulfilment
    Regulatory Requirement
```

Main Street MUST NOT create:

```text
UKOrder
USOrder
FrenchBooking
ScottishPayrollCore
CaliforniaCustomer
```

merely because regulatory fulfilment differs.

A jurisdiction-specific semantic type is justified only if the underlying business meaning itself is materially different and survives separate governed design.

---

# 34. AI Interpretation and Explanation

AI MAY:

- translate merchant language into candidate fact requests;
- explain a Regulatory Determination in ordinary language;
- explain why Main Street needs a missing primary fact;
- summarise a Regulatory Administrative Requirement;
- prepare bounded submissions;
- explain provider evidence;
- compare current and prior determinations; and
- draft merchant-facing guidance.

AI MUST NOT:

- invent a tax/rate/threshold;
- select a rule from memory;
- decide an ambiguous jurisdiction;
- alter a rule release;
- mark a requirement satisfied;
- approve a regulatory filing;
- grant standing authority;
- invent provider success;
- present `ASSISTED` or `UNSUPPORTED` scope as determined; or
- override an accepted deterministic result.

Canonical:

```text
deterministic/provider-backed result
        ↓
AI explanation
```

not:

```text
raw merchant data
        ↓
LLM guesses regulatory answer
```

---

# 35. Merchant-Facing Explanation Contract

A material regulatory explanation SHOULD communicate, in business language:

```text
WHAT
What Main Street believes is required or calculated

WHY
Which business facts/rule category caused it

WHEN
The relevant period/deadline

ACTION
What Main Street will do or what the merchant must do

CERTAINTY/BOUNDARY
Whether it is determined, awaiting information,
provider-backed, or requires specialist escalation
```

Technical detail MAY be progressively disclosed.

Main Street MUST NOT require merchants to understand the internal concepts defined in MS-PROT-082.

---

# 36. Example Merchant Interaction

Explanatory only:

```text
Main Street

You are likely to need to set aside £X
for this obligation.

Why?
We used:
• your current business structure
• your recorded sales for the period
• the rule currently in force for your jurisdiction

We still need:
• confirmation of one expense

[Confirm expense]
```

The merchant does not select:

```text
tax code
percentage
formula
jurisdiction adapter
regulatory-rule version
```

---

# 37. Privacy and Data Protection

Regulatory evaluation MAY process sensitive commercial or personal information.

All such processing SHALL conform to composite MS-PROT-053 and applicable accepted privacy/security authority.

JRA MUST:

- use purpose-qualified minimum-necessary facts;
- retain provenance;
- prevent possession from implying Exposure permission;
- restrict worker/payroll-related regulatory data by actor authority;
- avoid placing sensitive amounts in ordinary notifications without accepted justification; and
- preserve applicable retention/disposition evidence.

Public regulatory rules and private merchant inputs MUST NOT be treated as the same data class.

---

# 38. Authorisation

Merchant-facing regulatory operations SHALL use accepted actor-authorisation authority.

Platform operations that:

- activate rule releases;
- promote source evidence;
- activate Jurisdiction Pack Releases;
- change support declarations; or
- accept exceptional/manual regulatory evidence

MUST require explicit platform-authorised principals.

Merchant authority MUST NOT authorise platform rule mutation.

Platform authority MUST NOT silently substitute merchant business facts.

---

# 39. Projection and Exposure

Regulatory Determinations and Regulatory Administrative Requirements MAY contribute to:

- merchant dashboard projections;
- attention/exception surfaces;
- notifications;
- future financial/business-health projections;
- authorised reports; and
- AI context.

Projection MUST NOT become authoritative regulatory state.

A dashboard label such as:

```text
Tax: needs attention
```

does not own or mutate the underlying determination or requirement.

---

# 40. Cross-Capability Coordination

MS-PROT-082 permits cross-capability coordination through explicit contracts.

Examples:

```text
Merchant Location
    -- authoritative fact reference -->
JRA Jurisdiction Resolution
```

```text
Payroll
    -- Regulatory Purpose request -->
JRA
    -- determination/provider evidence -->
Payroll
```

```text
JRA Regulatory Requirement
    -- projection -->
Merchant Attention Surface
```

```text
JRA Determination
    -- derived evidence -->
Future Business Health
```

No edge transfers source semantic ownership.

---

# 41. Business Health / Financial Intelligence Boundary

MS-PROT-082 deliberately does NOT define:

```text
profitability
cash runway
gross margin
business-health score
financial-health score
customer health
capacity health
business diagnosis
business recommendation ranking
```

A future authority SHALL govern those semantics.

That authority MAY consume:

- regulatory determinations;
- estimated statutory amounts;
- regulatory deadlines;
- current open regulatory requirements; and
- provenance/confidence boundaries

as input evidence.

It MUST retain those values as DERIVED evidence rather than re-owning regulatory truth.

---

# 42. Tax and Professional-Expertise Boundary

Main Street MAY automate routine calculation, preparation and administration where:

- the Regulatory Purpose is explicitly supported;
- facts are sufficient;
- the rule path is governed;
- provider obligations are satisfied where required; and
- the required actor/merchant authority exists.

Main Street MUST escalate rather than improvise where:

- material legal interpretation remains unresolved;
- facts are ambiguous;
- jurisdiction is unsupported;
- an accepted rule requires professional judgement; or
- provider/authority evidence cannot establish a reliable outcome.

The objective is:

> reduce avoidable professional and administrative cost without representing uncertainty as certainty.

---

# 43. Architecture Diagram

The following edge labels are normative.

```text
CAPABILITY OWNERS
(authoritative business facts)
        │
        │ fact references
        ▼
┌───────────────────────────────┐
│ Jurisdiction Context Resolver │
└───────────────────────────────┘
        │ DERIVES
        ▼
   Jurisdiction Context
        │
        │ input
        ▼
┌───────────────────────────────┐
│ Regulatory Evaluation        │
│                               │
│  Rule Set Release ─────────┐  │
│                            │  │
│  Native Evaluator          │  │
│       OR                   │  │
│  Provider Fulfilment ──────┘  │
└───────────────────────────────┘
        │ DERIVES
        ▼
 Regulatory Determination
        │
        ├── may ESTABLISH ──► Regulatory Administrative Requirement
        │                            │
        │                            │ request/evidence
        │                            ▼
        │                     Provider / Merchant /
        │                     Specialist Fulfilment
        │
        ├── projection ─────► Merchant Attention
        │
        ├── derived input ──► Future Business Health
        │
        └── context ────────► AI Explanation

AI has no authoritative mutation edge.
```

---

# 44. Hard Invariants

The following are mandatory invariants.

### INV-082-001 — No Country-Wide Behaviour Assumption

A country identifier alone MUST NOT determine all regulatory treatment.

### INV-082-002 — Purpose Qualification

Every regulatory evaluation MUST identify an owner-qualified Regulatory Purpose.

### INV-082-003 — Source-Fact Ownership

JRA MUST NOT maintain competing authoritative copies of capability-owned business facts.

### INV-082-004 — Rule Provenance

Every production regulatory rule path MUST be source-backed or provider-backed.

### INV-082-005 — Immutable Rule Releases

An active Regulatory Rule Set Release MUST NOT be mutated in place.

### INV-082-006 — Historical Affinity

Every Regulatory Determination MUST retain the exact rule/source/input affinity used to create it.

### INV-082-007 — No AI Regulatory Authority

AI MUST NOT create authoritative rates, rules, jurisdiction resolutions, determinations or satisfaction facts.

### INV-082-008 — Support-Dimension Separation

Jurisdiction Support Class MUST NOT collapse applicability, entitlement, authorisation, provider readiness or exposure.

### INV-082-009 — No Generic Obligation Takeover

Regulatory Administrative Requirement MUST NOT re-own capability-specific obligation semantics.

### INV-082-010 — Explicit Unsupported Outcome

Unsupported jurisdiction-purpose scope MUST fail explicitly.

### INV-082-011 — No Blind External Retry

An uncertain externally material outcome MUST be reconciled before potentially duplicating the action.

### INV-082-012 — Merchant Expertise Boundary

Main Street MUST NOT request a regulatory rate/formula/threshold from the merchant where an active supported path can derive it.

### INV-082-013 — Rule Assurance

An expired regulatory assurance boundary MUST NOT be treated as current support for new automatic determinations.

### INV-082-014 — No Silent Commitment Rewrite

A regulatory rule update MUST NOT silently rewrite another capability's existing authoritative commitments.

### INV-082-015 — Capability-Scoped Failure

Failure of one jurisdiction-purpose path MUST NOT disable unrelated capabilities without an explicit dependency.

---

# 45. Failure Model

The architecture MUST distinguish:

```text
INPUT INCOMPLETE
JURISDICTION AMBIGUITY
REGULATORY PURPOSE UNSUPPORTED
REGULATORY ASSURANCE EXPIRED
AUTHORISATION REJECTION
VALIDATION REJECTION
CONFLICT
PROVIDER NOT READY
PROVIDER FAILURE
EXTERNAL OUTCOME UNCERTAIN
RULE/PACK INTEGRITY FAILURE
```

A generic `regulatory failure` result is insufficient where caller recovery differs.

---

# 46. Falsification Evidence

## 46.1 Low-Software-Capacity Merchant

**Scenario:** A sole proprietor understands their business but does not know the statutory percentage/rate applicable to an ordinary supported transaction.

**Failure sought:** Main Street requires the merchant to configure the rate.

**Result:** PASS.

The merchant supplies primary business facts only. Rule selection/calculation is Main Street/provider responsibility.

## 46.2 Sub-Country Difference

**Scenario:** A rule differs between subdivisions within one country.

**Failure sought:** Country-wide hard-coded rate.

**Result:** PASS.

Purpose-qualified Jurisdiction Context resolves explicit subdivision scope.

## 46.3 National Rule Despite Subdivision

**Scenario:** Another rule is nationally uniform.

**Failure sought:** architecture unnecessarily forks the rule by subdivision.

**Result:** PASS.

Explicit Rule Applicability Scope may cover the national jurisdiction directly.

## 46.4 Local US-Style Layering

**Scenario:** A purpose depends on country + state + local jurisdiction.

**Failure sought:** one `stateTaxRate` property or global US branch.

**Result:** PASS.

Jurisdiction Context may contain multiple explicit jurisdictions and rule applicability is explicit.

## 46.5 Multi-Location Merchant

**Scenario:** One merchant operates in two regulatory areas.

**Failure sought:** one mutable merchant `taxJurisdiction`.

**Result:** PASS.

Jurisdiction Context is purpose- and subject-qualified.

## 46.6 Future Rule Change

**Scenario:** A rule changes on a known future date.

**Failure sought:** new rule rewrites historic calculation.

**Result:** PASS.

Immutable releases and governing-time affinity preserve historical correctness.

## 46.7 Retroactive Correction

**Scenario:** an authority later corrects an earlier rule interpretation.

**Failure sought:** historical database rows silently mutate.

**Result:** PASS.

New corrective release/determination is required; original evidence remains.

## 46.8 Provider Timeout After Submission

**Scenario:** provider filing may have succeeded but Main Street loses the response.

**Failure sought:** automatic duplicate filing.

**Result:** PASS.

Requirement remains unsatisfied/uncertain until reconciliation; blind retry is prohibited.

## 46.9 AI Hallucinated Rule

**Scenario:** AI states a plausible but incorrect rate.

**Failure sought:** model answer becomes calculation authority.

**Result:** PASS.

AI has no rule-promotion or determination authority.

## 46.10 Support Loss

**Scenario:** a provider becomes unavailable.

**Failure sought:** provider outage deletes regulatory support or existing business truth.

**Result:** PASS.

Support Class and Provider Readiness remain separate; existing truth survives.

## 46.11 Partial Country Coverage

**Scenario:** Main Street supports only selected subdivisions.

**Failure sought:** whole-country `supported=true`.

**Result:** PASS.

Support declarations are jurisdiction-purpose scoped.

## 46.12 Enterprise Complexity

**Scenario:** merchant requests complex multinational optimisation.

**Failure sought:** Main Street invents a generic enterprise rule engine.

**Result:** PASS.

Unsupported/escalation boundary remains valid.

## 46.13 Obligation Collision

**Scenario:** a tax filing requirement and Payment Obligation both exist.

**Failure sought:** generic `Obligation` entity becomes owner of both.

**Result:** PASS.

The new canonical term is `Regulatory Administrative Requirement`; existing domain owners remain authoritative.

## 46.14 Merchant Location Changes

**Scenario:** merchant changes authoritative operating location.

**Failure sought:** old jurisdiction silently remains current.

**Result:** PASS.

New source-fact revision causes new Jurisdiction Context and affected re-determinations while historical contexts remain.

## 46.15 Expired Regulatory Knowledge

**Scenario:** Main Street has no evidence that a previously accepted rule remains current after its assurance boundary.

**Failure sought:** indefinite use of stale rule because no explicit end date existed.

**Result:** PASS.

New automatic determination is blocked until assurance is restored or another support path is used.

---

# 47. Alternatives and Trade-Offs

## 47.1 Country-Specific Application Forks

**Rejected**

```text
UK implementation
US implementation
Canada implementation
...
```

Advantages:

- superficially simple initial implementation.

Rejected because:

- duplicates business semantics;
- creates divergence;
- makes expansion expensive;
- encourages country/business-type hard-coding.

## 47.2 One Universal Regulatory DSL

**Rejected for initial authority**

Advantages:

- theoretical ability to encode all rules uniformly.

Rejected because:

- speculative abstraction;
- high semantic gravity;
- tax, payroll, invoicing and other purposes may require materially different typed semantics;
- likely to become an accidental programming platform.

Chosen approach:

> owner-qualified typed Regulatory Purpose contracts + versioned evaluator/provider bindings.

A rule DSL MAY be proposed later if evidence demonstrates repeated structure requiring one.

## 47.3 External Providers for Everything

**Rejected as universal rule**

Advantages:

- reduces internal statutory implementation.

Disadvantages:

- provider lock-in;
- availability/cost risk;
- weak portability;
- unnecessary delegation of simple deterministic rules.

Chosen approach:

> hybrid native/provider-backed fulfilment.

## 47.4 Native Main Street Engine for Everything

**Rejected**

Advantages:

- maximal control.

Disadvantages:

- excessive regulatory maintenance;
- inappropriate legal/statutory breadth;
- slows jurisdiction expansion;
- duplicates mature specialist infrastructure.

## 47.5 Merchant-Entered Rates

**Rejected**

Advantages:

- easiest implementation.

Disadvantages:

- violates Administrative Compression;
- transfers expert work to merchants;
- causes stale/incorrect configuration;
- undermines the product mission.

## 47.6 Current-Rule-Only Storage

**Rejected**

Advantages:

- simpler data model.

Disadvantages:

- destroys historical correctness;
- makes audits/reconciliation unreliable;
- causes rule changes to alter past interpretation.

## 47.7 One Universal Merchant Jurisdiction

**Rejected**

Advantages:

- simple lookup.

Disadvantages:

- invalid for multi-location, worker-specific, transaction-specific and sub-country rules.

---

# 48. Accepted Trade-Off

Main Street accepts:

```text
more internal regulatory architecture
+
versioning/provenance overhead
+
jurisdiction-pack maintenance
+
provider integration complexity
```

in exchange for:

```text
low merchant regulatory burden
+
historical correctness
+
international extensibility
+
provider replaceability
+
explicit unsupported boundaries
+
cross-capability regulatory consistency
```

This is justified complexity under the Fundamental Vision.

---

# 49. Implementation Constraints

MS-PROT-082 does NOT prescribe:

- programming language;
- relational table layout;
- specific rule-engine framework;
- specific provider;
- specific government API;
- specific cache;
- specific workflow engine; or
- microservice deployment.

Initial implementation SHOULD conform to Main Street's accepted modular-monolith/composite architecture.

The JRA context SHOULD remain a bounded module with explicit ports for:

- source-fact queries;
- rule-release repository;
- evaluator bindings;
- provider fulfilment;
- audit/evidence;
- projections; and
- authorised platform administration.

A separate microservice is not justified by this authority.

---

# 50. MVP Boundary

The MVP does NOT need:

```text
worldwide tax support
worldwide payroll
all government filing
all local tax systems
enterprise accounting
international optimisation
```

The MVP architecture DOES need the governed primitives required to avoid first-country hard-coding:

```text
Jurisdiction
Regulatory Purpose
Jurisdiction Context
Regulatory Source Evidence
Regulatory Rule Set Release
Jurisdiction Pack Release
Jurisdiction Support Declaration
Regulatory Determination
Regulatory Administrative Requirement
Regulatory Fulfilment Evidence
```

The first production jurisdiction pack MAY contain a deliberately narrow Regulatory Purpose portfolio.

---

# 51. Expansion Rule

Adding a new jurisdiction SHALL primarily require:

```text
new/extended jurisdiction references
+
new source-backed rule releases
+
new support declarations
+
new evaluator/provider bindings where required
+
new conformance evidence
```

Adding a jurisdiction MUST NOT normally require:

- forking universal capability semantics;
- adding business-type-specific application code; or
- duplicating merchant configuration.

If a new jurisdiction reveals materially new business meaning, that meaning requires a separate governed semantic review rather than being hidden inside a Jurisdiction Pack.

---

# 52. Deferred Decisions

The following decisions are deliberately deferred and MUST NOT be guessed during implementation.

## MS-PROT-082-DQ-001 — Initial Commercial Jurisdiction Portfolio

**Question:** Which exact country/sub-country scopes constitute Main Street's first commercial launch?

**State:** DEFERRED — ACTIVE BEFORE PUBLIC JURISDICTION LAUNCH.

**Safe boundary:** Core JRA architecture may be implemented independently. No jurisdiction may be advertised/activated without its readiness matrix.

## MS-PROT-082-DQ-002 — Initial Regulatory Purpose Portfolio

**Question:** Which exact purposes—tax, invoice, filing, registration or others—are included in the first production pack?

**State:** DEFERRED — ACTIVE BEFORE FIRST JURISDICTION PACK ACTIVATION.

## MS-PROT-082-DQ-003 — Rule Artifact Physical Representation

**Question:** Exact persistence/serialization representation of rule releases and evaluator bindings.

**State:** DEFERRED — implementation detail only after semantic conformance proves materially equivalent behaviour.

A universal rule DSL is NOT authorised by this deferral.

## MS-PROT-082-DQ-004 — Initial Provider Portfolio

**Question:** Which external tax, payroll, filing or regulatory providers fulfil each provider-backed purpose?

**State:** DEFERRED — ACTIVE BEFORE corresponding `PROVIDER_BACKED` support declaration.

## MS-PROT-082-DQ-005 — Regulatory Source Monitoring Automation

**Question:** Exact mechanism/frequency for detecting source changes.

**State:** DEFERRED — ACTIVE BEFORE production support; each pack must nonetheless possess a concrete assurance boundary.

## MS-PROT-082-DQ-006 — Professional Escalation Network

**Question:** Whether Main Street integrates directly with accountants/tax professionals/legal specialists or provides evidence for merchant-selected professionals.

**State:** DEFERRED.

**Safe boundary:** `ESCALATION_REQUIRED` remains valid without an integrated professional marketplace.

## MS-PROT-082-DQ-007 — Business Health & Financial Intelligence Semantics

**Question:** Canonical metric identities, lineage, financial-health model, business-health model, diagnosis and merchant-facing interpretation.

**State:** DEFERRED TO SEPARATE MATERIAL AUTHORITY.

**Safe boundary:** MS-PROT-082 exposes provenance-preserving regulatory evidence but MUST NOT invent health semantics.

---

# 53. Conformance Test Obligations

A conforming implementation MUST provide evidence for at least the following.

### JRA-C01
Same exact facts + purpose + governing time + rule release produce duplicate-safe equivalent determination.

### JRA-C02
Two subdivisions can resolve different rules without forking the owning capability.

### JRA-C03
A national rule can apply identically without duplicated subdivision copies.

### JRA-C04
Missing source fact produces `INPUT_INCOMPLETE`, not fabricated default.

### JRA-C05
Ambiguous jurisdiction produces `JURISDICTION_AMBIGUOUS`.

### JRA-C06
Unsupported purpose produces explicit unsupported result.

### JRA-C07
AI cannot activate rule releases or create authoritative determination.

### JRA-C08
Rule update does not mutate old determination.

### JRA-C09
Provider outage does not mark requirement satisfied.

### JRA-C10
Uncertain external outcome prevents unsafe duplicate submission.

### JRA-C11
Duplicate provider callback cannot create duplicate satisfaction.

### JRA-C12
One Requirement Business Key cannot create conflicting current requirements under concurrency.

### JRA-C13
Expired assurance blocks new automatic determination.

### JRA-C14
Provider Readiness does not change Jurisdiction Support Class.

### JRA-C15
Commercial Entitlement does not change Jurisdiction Support Class.

### JRA-C16
Merchant Location change re-evaluates affected purposes without rewriting historic determinations.

### JRA-C17
Partial territorial support cannot resolve as universal country support.

### JRA-C18
A Regulatory Administrative Requirement cannot mutate Payment Obligation, Payroll, Booking or Order authority.

### JRA-C19
A merchant-facing workflow can obtain a supported rate/determination without asking the merchant to provide the rate itself.

### JRA-C20
An unrelated capability remains operational when another Regulatory Purpose loses serviceability.

---

# 54. Ambiguity Review

The proposal resolves the following material ambiguities.

| Potential ambiguity | Resolution |
|---|---|
| Country vs jurisdiction | Country is only one possible Jurisdiction scope |
| Merchant location vs regulatory jurisdiction | Merchant Location is source evidence; Jurisdiction Context is purpose-qualified derived context |
| Support vs provider availability | Separate dimensions |
| Support vs entitlement | Separate dimensions |
| Regulatory calculation vs AI explanation | Deterministic/provider-backed calculation precedes AI |
| Rule validity vs rule freshness | Effective interval and regulatory assurance are distinct |
| Provider result vs regulatory truth | Provider output is evidence evaluated through the purpose contract |
| Generic obligation | Rejected; `Regulatory Administrative Requirement` is explicitly qualified |
| Current rules vs historical facts | Exact release affinity is retained |
| Jurisdiction hierarchy vs legal applicability | Hierarchy does not automatically establish rule applicability |
| Merchant fact vs derived professional fact | Merchant provides primary facts; Main Street derives supported regulatory interpretations |
| Regulatory requirement vs funds movement | Separate authority |
| Country launch vs capability launch | Support is jurisdiction-purpose/capability specific |

No material unresolved ambiguity is knowingly left inside the governed semantic scope.

---

# 55. Corpus-Conformance Consequences After Approval

If MS-PROT-082 receives explicit manual approval, repository formalisation SHALL include review of stale lower-authority/product drafts that still imply:

```text
Main Street is only a Digital Presence Operating System
Main Street does not handle taxation in any form
Main Street provides technology but not business administration
```

Such statements SHALL be reconciled with:

- `MS-FUNDAMENTAL-VISION-001`; and
- accepted MS-PROT-082 scope

without implying that Main Street becomes a universal tax bureau, legal adviser or enterprise ERP.

The following areas SHALL also be reviewed for references/traceability:

- `AUTHORITY-INDEX.md`;
- `DEFERRED-DECISION-REGISTER.md`;
- `CANONICAL-SEMANTIC-LEXICON.md`;
- PRD jurisdiction/international-expansion material;
- Platform Integration documentation;
- Analytics/Business Intelligence boundary documentation;
- Merchant Dashboard/attention documentation; and
- `IMPLEMENTATION-RULES.md` impact.

No such repository mutation is authorised before explicit approval.

---

# 56. Acceptance Boundary

Approval of MS-PROT-082 SHALL establish:

1. jurisdiction as explicit platform semantics rather than country hard-coding;
2. purpose-qualified Jurisdiction Context;
3. owner-qualified Regulatory Purpose contracts;
4. versioned/source-backed regulatory knowledge;
5. immutable/effective-dated rule releases;
6. hybrid native/provider-backed regulatory evaluation;
7. capability-specific jurisdiction support declarations;
8. explicit partial/unsupported/escalation outcomes;
9. immutable Regulatory Determination provenance;
10. qualified Regulatory Administrative Requirement semantics;
11. safe fulfilment/retry/uncertainty handling;
12. historical regulatory affinity;
13. merchant primary-fact versus derived-regulatory-fact separation;
14. AI as explanation/inference rather than regulatory authority;
15. jurisdiction launch readiness as a matrix rather than a country boolean; and
16. a clean foundation for future Business Health and Financial Intelligence without transferring their ownership to JRA.

Approval SHALL NOT:

- choose the first launch country;
- authorise a specific tax provider;
- define actual tax rates;
- define payroll law;
- define financial-health metrics;
- authorise implementation beyond normal implementation governance;
- make Main Street a legal/tax adviser;
- establish a general ERP capability; or
- permit unsupported regulatory answers to be approximated.

---

# 57. Final Governing Statement

> **Main Street business semantics are global where the underlying business meaning is global. Jurisdiction determines how registered regulatory responsibilities are interpreted and fulfilled; it does not redefine the business itself.**

> **For supported regulatory purposes, merchants provide ordinary business facts. Main Street resolves the relevant jurisdiction, applies source-backed deterministic rules or bounded specialist providers, preserves evidence and historical affinity, handles authorised routine administration, and surfaces only the information, decision or exception the merchant needs.**

> **Main Street MUST know when it does not know. Unsupported, ambiguous, stale or professionally judgement-dependent regulatory scope must fail explicitly or escalate rather than being guessed.**

---

# 58. Design Review Outcome

**Fundamental Vision Conformance:** `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`

**Falsification:** PASSED for the governed architecture subject to the explicit deferred boundaries above.

**Trade-off outcome:** ACCEPT the additional internal jurisdiction/regulatory architecture because it materially reduces merchant administration while preserving capability ownership, provider replaceability, historical correctness and international extensibility.

**RECOMMENDATION:** `ACCEPT`

This recommendation is a design-review conclusion only.

The authority became ACCEPTED through explicit manual approval on 8 September 2026 and conforming repository formalisation. Implementation remains governed separately by `IMPLEMENTATION-RULES.md` and the accepted implementation programme.