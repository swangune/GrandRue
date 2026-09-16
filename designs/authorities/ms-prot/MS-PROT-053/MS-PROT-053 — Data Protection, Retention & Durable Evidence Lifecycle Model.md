# MS-PROT-053 — Data Protection, Retention & Durable Evidence Lifecycle Model

**Document ID:** MS-PROT-053  
**Version:** 1.0  
**Status:** **ACCEPTED after deferred-decision audit, cross-contract review and manual approval**  
**Depends on:** MS-PROT-025, MS-PROT-026, MS-PROT-027, MS-PROT-028 v1.3, MS-PROT-031, MS-PROT-033, MS-PROT-040, MS-PROT-043 v1.2, MS-PROT-045 v1.1, MS-PROT-046 v1.1, MS-PROT-048 v1.1, MS-PROT-051 v1.0  
**Closes:** DDR-OD-001 — Data protection, sensitivity, retention, erasure and durable-evidence lifecycle  
**Purpose:** Define the cross-cutting authority model by which Main Street classifies data-handling sensitivity, limits use and exposure, governs retention and erasure, preserves minimum historically necessary evidence, and prevents privacy requirements from being collapsed into business lifecycle, soft deletion, projection visibility or provider state.

---

# 1. Governing decision

Main Street shall distinguish the lifecycle and meaning of a business fact from the lifecycle and permitted handling of data carried by or associated with that fact.

> **Business semantics determine what a fact means. Data-protection and retention authority determine how much identifying or sensitive information may continue to be stored, used or exposed. Erasure shall not rewrite legitimate business history, while historical business truth shall not justify indefinite retention of personal or sensitive data.**

Canonical separation:

```text
BUSINESS LIFECYCLE
        ≠
DATA-PROTECTION CLASSIFICATION
        ≠
PURPOSE OF USE
        ≠
RETENTION REQUIREMENT
        ≠
EXPOSURE / AUDIENCE VISIBILITY
        ≠
ERASURE / ANONYMISATION / REDACTION
        ≠
HISTORICAL EVIDENCE
```

Example:

```text
Appointment A100
    lifecycle = COMPLETED
```

shall not itself mean either:

```text
DELETE ALL DATA NOW
```

or:

```text
RETAIN ALL CUSTOMER DATA FOREVER
```

The applicable protection, purpose and retention contracts determine the permitted data disposition.

---

# 2. Scope

MS-PROT-053 governs the cross-cutting architectural boundary for:

```text
data-handling classification
personal/sensitive business data
security secrets
purpose-limited use
retention requirements
retention triggers and end conditions
erasure
anonymisation
redaction
restricted use
minimal durable evidence
historical snapshots/provenance
Domain Event data minimisation
AuditRecord data minimisation
projection/cache/search convergence after disposition
provider/trust evidence minimisation
merchant-scoped customer/contact data
private merchant/profile data
```

It does not define:

```text
universal statutory retention durations
jurisdiction-specific legal advice
one universal data-deletion operation
physical SQL deletion strategy
backup implementation
cryptographic algorithms
secret-store product
privacy-policy copy
cookie-consent implementation
marketing-law workflow
subject-access-request UI
specific regulator integrations
```

Those require applicable implementation, policy, legal or future product contracts.

---

# 3. Existing authority remains intact

MS-PROT-053 does not transfer semantic ownership of business data into a generic privacy subsystem.

Existing authorities remain authoritative for what data means:

```text
Capability / domain owner
    owns current business meaning and lifecycle

MS-PROT-045 FieldDefinition
    owns contextual field meaning

MS-PROT-027
    owns audience-safe projection principles

MS-PROT-028
    owns trust requirement / claim semantics

MS-PROT-031
    owns Merchant Scope isolation

MS-PROT-040
    owns configuration history and activation lifecycle

MS-PROT-043
    owns Enquiry / CustomerContext business semantics

MS-PROT-046
    owns Publication revision/history semantics

MS-PROT-048
    owns provider authority and evidence boundaries

MS-PROT-051
    owns merchant-profile/presence facts and exposure distinctions
```

MS-PROT-053 adds the handling and retention contract across those owners.

Hard rule:

> **Data-protection authority constrains use, exposure and retention; it does not become a second owner of the underlying business semantics.**

---

# 4. Protection classification is contextual

A reusable `DataConcept` shall not automatically determine one universal handling classification.

Example:

```text
EMAIL_ADDRESS
        ↓
CustomerContext.email
        → PERSONAL

MerchantContactPoint.email
        → classification follows its actual merchant/business context

ProviderOAuthToken
        → SECRET
```

This follows MS-PROT-045:

```text
DataConcept
    supplies reusable semantic meaning

FieldDefinition / evidence binding
    supplies contextual business meaning
```

Protection classification therefore attaches to the relevant contextual field, evidence contract, secret reference or explicitly defined data subject.

Rejected:

```text
EMAIL_ADDRESS = always public
EMAIL_ADDRESS = always restricted
```

without contextual authority.

---

# 5. Initial handling-class vocabulary

Main Street adopts a deliberately small initial platform handling vocabulary:

```text
STANDARD
PERSONAL
RESTRICTED
SECRET
```

These are **Main Street engineering/governance classifications**, not claims that Main Street has reproduced every jurisdiction's statutory data category.

## 5.1 STANDARD

Ordinary business information for which no identified elevated handling requirement applies.

Examples may include:

```text
public merchant description
public business hours
public Offering title
published Opportunity title
```

`STANDARD` does not automatically mean `PUBLIC`; exposure remains separately governed.

## 5.2 PERSONAL

Information relating to, identifying, or reasonably linkable to a natural person in the applicable Main Street context.

Examples may include:

```text
customer name
customer email
customer telephone
customer service address
staff contact details
controller contact details
```

A field may be `PERSONAL` while legitimately visible to a narrowly authorised merchant actor.

## 5.3 RESTRICTED

Information requiring materially tighter handling because the accepted platform/capability/trust/legal/security context establishes elevated sensitivity, misuse risk or bounded disclosure.

Examples may include, where actually required:

```text
identity-verification evidence/reference
regulated evidence
sensitive merchant/private operational information
high-risk account-recovery evidence
certain customer data where a capability establishes elevated protection
```

Main Street shall not classify broad categories as `RESTRICTED` merely because doing so seems safer; the elevated handling requirement must be explainable.

## 5.4 SECRET

Credentials and equivalent security material whose disclosure would permit or materially assist unauthorised access or impersonation.

Examples include:

```text
OAuth access/refresh tokens
API secrets
password hashes or credential material
recovery secrets
signing/private keys
webhook secrets
```

`SECRET` receives the strongest default non-projection/non-propagation boundary.

---

# 6. Handling classification does not define business authority

Classification shall not grant operational permission.

```text
PERSONAL
```

does not mean:

```text
merchant actor may read it
```

and:

```text
STANDARD
```

does not mean:

```text
publish it publicly
```

Correct evaluation remains:

```text
business ownership
    +
actor authority
    +
audience/exposure contract
    +
protection classification
    +
purpose
        ↓
permitted access/use/projection
```

Hard invariant:

> **Protection classification constrains handling; it never substitutes for actor authorisation or exposure policy.**

---

# 7. Purpose is explicit

Possession of data does not itself justify every future use.

Main Street shall preserve a distinction between data availability and permitted purpose.

Initial purpose categories sufficient for current architecture include:

```text
OPERATIONAL_NEED
BUSINESS_COMMITMENT_EVIDENCE
SECURITY_AUDIT
EXTERNAL_RECONCILIATION
LEGAL_OR_REGULATORY_OBLIGATION
```

Additional purposes may be introduced only where a concrete accepted requirement needs them.

Example:

```text
customer email supplied for Enquiry response
```

may support:

```text
OPERATIONAL_NEED = respond to Enquiry
```

but shall not automatically become:

```text
marketing permission
subscription consent
unbounded customer profiling
```

MS-PROT-043 remains authoritative that Enquiry contact is not marketing consent.

---

# 8. RetentionRequirement

A `RetentionRequirement` expresses why a defined data scope may or must continue to be retained after its immediate creation/use.

Conceptually:

```text
RetentionRequirement
{
    requirementIdentity
    affectedDataScope
    purpose
    authoritySource
    startTrigger
    endOrReviewCondition
    permittedUseWhileRetained
    terminalDisposition
}
```

This is conceptual and does not mandate one Java type/table.

A retention requirement may originate from:

```text
platform security contract
capability/business commitment contract
external reconciliation obligation
accepted legal/regulatory rule
merchant-owned legitimate business requirement where platform policy permits
```

It shall not originate merely from:

```text
storage is cheap
might be useful later
analytics may want it
AI may find it interesting
```

---

# 9. No unexplained universal retention duration

MS-PROT-053 does not establish a universal platform constant such as:

```text
retain everything for 7 years
```

or:

```text
delete everything after 30 days
```

because different data/facts have different purposes and authorities.

Instead:

```text
RetentionRequirement
        ↓
applicable policy/jurisdiction/business rule
        ↓
concrete duration or end condition
```

Exact durations may therefore remain policy/implementation data once the governing authority and retention subject are explicit.

Hard rule:

> **A numeric retention duration without an identified subject, purpose and authority is not a sufficient Main Street retention contract.**

---

# 10. Retention subject is not automatically the whole Operational Object

One Operational Object may contain fields with different long-term retention needs.

Example:

```text
Appointment
    identity
    merchant
    service context
    scheduled interval
    lifecycle evidence
    customer name
    customer email
    free-text notes
```

A future disposition may legitimately preserve:

```text
appointment existed
service context
historical interval
completion/cancellation fact
```

while removing or anonymising:

```text
customer-identifying fields
free-text data no longer required
```

Therefore:

> **Retention and erasure may operate at object, field, relationship, evidence or projection scope according to the accepted contract.**

Rejected universal model:

```text
Object.deleted = true|false
```

as the complete privacy/retention architecture.

---

# 11. Distinct disposition outcomes

Main Street shall not collapse every data end-of-life outcome into physical deletion.

Initial conceptual dispositions are:

```text
DELETE
ANONYMISE
REDACT
RESTRICT_USE
RETAIN_MINIMAL_EVIDENCE
RETAIN_WITH_EXPLICIT_JUSTIFICATION
```

## DELETE

Remove the governed data when no continuing authorised need remains and deletion does not violate an applicable evidence/obligation requirement.

## ANONYMISE

Irreversibly remove or transform identifying association such that the retained business/statistical fact no longer represents an identifiable person under the applicable Main Street contract.

## REDACT

Remove a bounded sensitive component while retaining the surrounding record/evidence.

## RESTRICT_USE

Retain data because an obligation/evidence boundary requires it while preventing ordinary operational reuse beyond the allowed purpose.

## RETAIN_MINIMAL_EVIDENCE

Preserve only the minimum facts/references necessary to prove or interpret a historically material event, commitment, configuration, trust claim or provider interaction.

## RETAIN_WITH_EXPLICIT_JUSTIFICATION

Continue retention where an identified current requirement authorises it and other dispositions would not satisfy that requirement.

The physical implementation of each disposition remains downstream.

---

# 12. Erasure does not rewrite business history

Suppose Main Street historically had:

```text
Appointment A100
CustomerContext C100
```

and the customer-identifying information later has no continuing authorised retention purpose.

Permitted result may be conceptually:

```text
Appointment A100
    lifecycle = COMPLETED
    occurredAt = ...
    service = ...
    customer relationship = anonymised/minimised reference
```

Main Street need not rewrite history to say:

```text
Appointment never existed
```

unless the underlying record itself has no continuing legitimate retention purpose and may be deleted entirely.

Hard invariant:

> **Privacy disposition may minimise identity without falsifying a materially retained business fact.**

---

# 13. Historical evidence does not justify full payload retention

The opposite error is also prohibited.

If Main Street needs evidence that:

```text
customer accepted a policy
trust claim was satisfied
publication had a particular deadline
a payment provider returned a particular external fact
```

Main Street shall preserve only enough evidence to establish the materially relevant historical fact.

It shall not automatically retain:

```text
complete customer profile
complete external-provider payload
complete identity document
complete mutable subject snapshot
unrelated free-text notes
```

merely because some evidence is required.

Hard invariant:

> **Evidence retention preserves the minimum evidence necessary for the historically material claim; it does not create a licence for indefinite source-payload retention.**

---

# 14. Historical snapshots and provenance

MS-PROT-025/033 distinguish current authoritative data from historical/contextual snapshots. MS-PROT-043 requires sufficient submission-time provenance for materially changing Enquiry subjects. MS-PROT-046 requires reconstructible material publication revisions.

MS-PROT-053 adds:

```text
historical snapshot/provenance
        ↓
identify materially required fields
        ↓
apply purpose + retention + protection rules
        ↓
retain only required evidence
```

No indiscriminate immutable object snapshot is authorised by the word `history`.

---

# 15. Publication revision evidence

A published Opportunity or Announcement may require material revision history so a prior published representation can be reconstructed.

This does not imply every revision must retain every personal/sensitive field forever.

Correct rule:

```text
materially relevant publication representation
    → preserve sufficient revision evidence

unrelated personal/sensitive data
    → retain only under separate requirement
```

Publication revision history remains MS-PROT-046 semantics; MS-PROT-053 constrains the data retained within that evidence.

---

# 16. Trust and verification evidence

Where Main Street relies on a TrustClaim, MS-PROT-028 requires provenance sufficient to answer what claim was relied on, for which subject, from which authority and at what time.

MS-PROT-053 therefore prefers:

```text
claim type
subject reference
issuer/authority
evidence/reference identity
status/time/validity provenance
```

rather than automatically storing:

```text
full passport image
full regulator document
full provider KYC payload
```

A full source artefact may be retained only if a concrete applicable requirement justifies that specific retention.

---

# 17. Provider data and external authority

Main Street cannot erase or retain an external provider's authoritative copy merely by changing Main Street state.

Therefore:

```text
Main Street disposition
        ≠
provider-side disposition
```

Where provider-side deletion/restriction is required and supported, it must occur through the provider/integration contract.

Main Street shall still minimise its own:

```text
provider payloads
provider identifiers
reconciliation evidence
cached provider data
```

according to the applicable retention requirement.

---

# 18. Domain Events are not a permanent PII archive

MS-PROT-026 remains authoritative that event payloads contain only purpose-justified information.

MS-PROT-053 adds:

> **Domain Events and Integration Events shall not be designed as indefinite copies of mutable personal or restricted object state.**

Prefer:

```text
stable subject reference
minimal event-specific facts
non-sensitive correlation
```

where consumers can query authorised current state when needed.

If an event must retain personal/restricted information for historical evidence, that field remains subject to an explicit retention requirement.

Event immutability shall not be used to bypass erasure/minimisation authority.

The implementation may use redaction, cryptographic separation, payload minimisation, tombstoning or another safe mechanism; MS-PROT-053 does not mandate the mechanism.

---

# 19. AuditRecord is not unrestricted evidence storage

Audit may need durable records of:

```text
actor
operation
result
time
scope
target
security/admin action
```

but an audit record shall not copy full business objects or secret/customer payloads by default.

Audit evidence receives its own purpose and retention requirement.

Hard rule:

> **Audit need is not permission to duplicate every sensitive value involved in an audited action.**

---

# 20. SECRET handling invariant

`SECRET` material shall not enter ordinary:

```text
Domain Event payloads
Integration Event payloads
merchant/customer/public projections
analytics/search indexes
business-readable audit payloads
AI prompts/context
logs/telemetry
```

except where a narrowly defined security/integration mechanism explicitly requires access and preserves equivalent protection.

Prefer references to secure secret storage over secret-value duplication.

Hard invariant:

> **A Main Street business semantic shall never require exposing a credential merely because the credential participates in fulfilment.**

---

# 21. Projections and caches do not gain independent retention authority

Derived copies include:

```text
storefront projection
merchant dashboard projection
customer history projection
search index
cache
analytics representation
notification preparation record
```

They may improve usability/performance but remain derived.

Therefore:

```text
authoritative disposition changes
        ↓
derived copies must converge
```

A derived representation shall not preserve personal/restricted data indefinitely merely because it is stored separately.

Projection refresh/deletion may be asynchronous, but there must be a bounded convergence/reconciliation mechanism where the underlying disposition requires removal or anonymisation.

---

# 22. Backups and disaster recovery

Backup media may have technical retention characteristics different from live operational data.

MS-PROT-053 does not mandate the backup implementation but requires:

```text
backup retention must be governed
backup access must be restricted
expired live data must not become ordinary operational data again merely because a backup is restored
restore procedures must reapply applicable disposition/retention state
```

Exact backup duration, encryption and restoration mechanics remain implementation/security policy decisions.

---

# 23. Merchant Scope and customer relationships remain isolated

MS-PROT-031/043 remain authoritative.

Example:

```text
CustomerAccount X
    ├── relationship with Merchant A
    └── relationship with Merchant B
```

A disposition affecting Merchant A's CustomerContext shall not automatically destroy independently legitimate Merchant B data.

Likewise:

```text
same email address
```

across merchants does not create one global erasure object by itself.

A global CustomerAccount or platform identity may have its own platform-scoped disposition contract, distinct from each merchant-scoped business relationship.

---

# 24. Enquiry and one-off contact minimisation

MS-PROT-043 already permits an Enquiry to exist without creating a durable CustomerContext.

MS-PROT-053 reinforces that design:

```text
one-off Enquiry
        ↓
retain contact/message while operationally required
        ↓
when no applicable purpose remains
        ↓
delete / anonymise / retain minimal evidence
according to contract
```

Main Street shall not create durable customer records merely to make retention easier.

---

# 25. Contact reuse remains purpose-bound

Where a reliable CustomerContext exists, Main Street may reuse legitimately available contact information to reduce repetition.

But:

```text
information exists
        ≠
all purposes permitted
```

Example:

```text
email retained to administer existing Booking
```

shall not automatically authorise unrelated bulk marketing.

Purpose and subscription/marketing semantics remain distinct.

---

# 26. Merchant Profile privacy

A Merchant Location or contact point may be private even though Main Street stores it.

MS-PROT-051 remains authoritative for profile exposure.

MS-PROT-053 adds that private merchant/profile fields may carry `PERSONAL` or `RESTRICTED` handling where appropriate.

Example:

```text
home-based tradesperson
    private home address
```

shall not become public merely because the merchant publishes a service area.

Protection classification and exposure are separate but jointly constrain projection.

---

# 27. Relationship removal and erasure are different

A semantic relationship may end while evidence remains legitimately retained.

Example:

```text
StaffRelationship → INACTIVE
```

is not equivalent to:

```text
erase every audit reference to that actor
```

Likewise:

```text
CustomerContext association removed/corrected
```

is not automatically permission to rewrite a historical Order/Appointment commitment.

Relationship lifecycle and data disposition remain separate decisions.

---

# 28. Deletion request is not itself authority to violate obligations

A requested erasure/deletion action must be evaluated against current applicable requirements.

Conceptually:

```text
requested disposition
        +
subject/scope
        +
current retention requirements
        +
current legal/security/business obligations
        ↓
permitted disposition plan
```

Possible result:

```text
some fields deleted
some anonymised
some restricted
minimal evidence retained
```

rather than one undifferentiated success/failure flag.

This specification does not define customer-facing legal-right request handling; it defines the internal authority boundary any such workflow must obey.

---

# 29. Retention expiry requires action

A retention requirement reaching its end condition shall not silently turn into indefinite storage.

The system must be capable of reaching a terminal disposition such as:

```text
DELETE
ANONYMISE
REDACT
RESTRICT_USE
RETAIN_MINIMAL_EVIDENCE under another still-active requirement
```

If another valid requirement continues, its scope/purpose must be explicit.

Hard invariant:

> **Expired purpose does not become a default perpetual retention purpose.**

---

# 30. Multiple retention requirements may coexist

The same field/evidence may be subject to more than one legitimate requirement.

Example:

```text
payment-related record
    operational need
    + reconciliation evidence
    + applicable legal requirement
```

Disposition is evaluated against the set of still-active requirements.

One expired requirement does not force deletion while another valid requirement remains.

Conversely, one broad requirement may not be used to retain fields outside its defined affected data scope.

---

# 31. Data minimisation applies at creation time

Retention is not only an end-of-life problem.

Main Street should avoid collecting/storing data that is unnecessary for the operation.

Examples:

```text
Visitor browsing
    → no CustomerContext merely for browsing

Enquiry from known Listing
    → do not ask customer to re-enter Listing address

Trust claim from provider
    → prefer bounded claim/reference over duplicate identity document

Event
    → do not copy whole aggregate
```

Hard rule:

> **The cheapest personal/sensitive field to protect and erase is the field Main Street never needed to collect.**

---

# 32. AI boundary

AI access to personal/restricted data is not justified merely because AI could improve a workflow.

AI may receive only data that is:

```text
necessary for the approved AI use
within permitted purpose
within actor/platform authority
consistent with protection classification
minimised for the task
```

`SECRET` data is prohibited from ordinary AI context.

AI-generated summaries or extracted values containing personal/restricted information inherit the relevant handling/retention constraints; summarisation does not make sensitive data non-sensitive.

---

# 33. Cross-domain falsification

## 33.1 Scholarship information publisher

Customer submits one Enquiry with email and question.

Valid model:

```text
Enquiry contact retained while needed for response/history
no durable CustomerContext required merely for one enquiry
later disposition may delete/anonymise identifying/contact data
minimal non-identifying operational evidence may remain if justified
```

No commerce/identity-verification retention policy is imposed merely because the merchant exists.

**PASS**

## 33.2 Online consultant

A guest creates an Appointment.

Valid model:

```text
Appointment commitment retained while operationally required
contact data retained only under applicable purpose
completed Appointment history may later retain minimal business evidence
unnecessary identifying fields may be anonymised/removed
```

**PASS**

## 33.3 Salon

Customer attends a haircut Appointment.

Main Street may preserve:

```text
that appointment occurred
service/time/business evidence where justified
```

without requiring permanent retention of:

```text
email
telephone
free-text notes
```

where no continuing purpose exists.

**PASS**

## 33.4 Retail order

Order may require historical commercial/fulfilment evidence.

Correct model distinguishes:

```text
order identity / commercial facts
        ≠
current customer profile
        ≠
delivery/contact snapshot
        ≠
provider payment evidence
```

Each retained field/evidence is governed by its purpose rather than one `keep order forever` flag.

**PASS**

## 33.5 Motel Booking

A Booking can preserve reservation and allocation history while guest-identifying data follows separate retention requirements.

Cancellation/release semantics remain Booking/Allocation authority; privacy disposition does not manufacture capacity changes.

**PASS**

## 33.6 Realtor Enquiry

Enquiry `SUBJECT → Listing` remains durable where needed, while submission-time Listing provenance retains only materially relevant context.

The Enquiry shall not snapshot the entire Property/Listing merely for history.

**PASS**

## 33.7 Home-based tradesperson

Merchant records a home address as a private Merchant Location and exposes only a service-area descriptor.

Protection/exposure rules prevent the private location from becoming public while preserving the merchant's valid public storefront.

**PASS**

## 33.8 Trust provider

External provider establishes a bounded trust claim.

Main Street retains claim provenance/reference sufficient for historical interpretation but does not automatically retain full source identity documents/provider payloads.

**PASS**

## 33.9 Configuration history

A superseded configuration revision remains historically meaningful under MS-PROT-040.

MS-PROT-053 permits removal/redaction of personal/sensitive data not required to interpret that configuration while preserving configuration semantics/provenance.

**PASS**

## 33.10 Domain Events and projections

A customer email changes or is erased/anonymised.

Current projections/search/cache must converge to the authorised current disposition; old events do not become a permanent customer-data copy merely because events are historically durable.

**PASS**

---

# 34. Rejected alternatives

The following approaches are rejected:

1. one universal hard-delete rule;
2. one universal `deleted` Boolean as the privacy lifecycle;
3. object lifecycle state determining retention automatically;
4. indefinite retention merely because data may be useful later;
5. one universal retention duration for all Main Street data;
6. DataConcept alone determining privacy classification;
7. `STANDARD` meaning automatically public;
8. `PERSONAL` meaning automatically inaccessible;
9. audit/event history as justification for copying entire sensitive objects;
10. immutable events as an escape hatch from erasure/minimisation;
11. provider/trust evidence automatically retaining full external payloads;
12. derived projection/cache/index copies acquiring independent retention authority;
13. secret values in business events, analytics, AI context or ordinary logs;
14. customer contact supplied for one purpose becoming universal consent;
15. cross-merchant erasure based merely on matching email/telephone/name;
16. deleting business history solely to remove personal identifiers where anonymisation/minimisation satisfies the legitimate retained fact;
17. retaining personal identifiers solely because business history must remain interpretable when minimal non-identifying evidence would suffice;
18. privacy subsystem becoming a second semantic owner of capability data.

---

# 35. Accepted invariants

1. Business lifecycle and data-protection/retention lifecycle are distinct.
2. Data protection constrains handling but does not transfer semantic ownership.
3. Protection classification is contextual and does not belong universally to a reusable DataConcept.
4. Initial platform handling classes are `STANDARD`, `PERSONAL`, `RESTRICTED`, and `SECRET`.
5. Handling classes are Main Street governance categories, not a replacement for jurisdiction-specific law.
6. Protection classification does not replace actor authority or exposure policy.
7. Possession of data does not authorise every purpose.
8. Retention requires an identified subject/scope, purpose, authority and end/review condition.
9. No unexplained universal retention duration is accepted.
10. Retention may apply at field, object, relationship, evidence or projection scope.
11. Erasure is not synonymous with physical deletion in every case.
12. Supported conceptual dispositions include delete, anonymise, redact, restrict use, retain minimal evidence and explicitly justified retention.
13. Privacy disposition shall not falsify legitimately retained business history.
14. Historical business evidence shall not justify indefinite retention of full personal/sensitive payloads.
15. Evidence retention is minimised to what the materially relevant claim requires.
16. Domain Events are not permanent unrestricted personal-data snapshots.
17. AuditRecords are not unrestricted evidence stores.
18. `SECRET` values are excluded from ordinary business events, projections, analytics, AI context, search and logs.
19. Derived projections/caches/indexes must converge after authoritative disposition changes.
20. Backup/restore processes must preserve current retention/disposition authority rather than resurrect expired operational data indefinitely.
21. Merchant Scope isolation remains authoritative for customer/business data disposition.
22. One-off Enquiry does not require durable CustomerContext merely for retention convenience.
23. Contact reuse remains purpose-bound and does not imply marketing/subscription consent.
24. Private merchant/profile data remains distinct from public exposure.
25. Relationship termination and privacy disposition are distinct.
26. Requested erasure/deletion is evaluated against current applicable retention obligations.
27. Retention expiry requires a terminal disposition or explicit continuing requirement.
28. Multiple retention requirements may coexist but only within their defined data scopes.
29. Data minimisation applies at collection/creation time, not only at deletion time.
30. AI use of personal/restricted data requires purpose, necessity, authority and minimisation; secret data is excluded from ordinary AI context.
31. External-provider authority does not relieve Main Street of minimising its own retained copy.
32. Main Street disposition does not by itself prove provider-side disposition.
33. Configuration, publication and trust history may retain minimal interpretive evidence without retaining unrelated personal/sensitive data.

---

# 36. Deferred implementation and policy details

The following remain intentionally downstream after this semantic closure:

```text
exact SQL representation of classifications/retention requirements
physical delete/anonymisation/redaction mechanisms
retention scheduler/job implementation
backup technology and exact backup duration
secret-store technology
at-rest/in-transit encryption implementation
log-redaction implementation
search/cache purge mechanism
Domain Event redaction/tombstone implementation
exact policy durations for each jurisdiction/use case
subject-rights workflow/UI
privacy notice generation
regulator-specific/legal integrations
provider-specific deletion API orchestration
exact AI data-isolation implementation
```

These must conform to MS-PROT-053; they do not reopen DDR-OD-001 unless implementation evidence exposes a semantic contradiction.

---

# 37. Continuous-improvement checkpoint

Question:

> **What could we have done better?**

A tempting design would have introduced a generic object-level privacy flag or universal `deleted` lifecycle. That would have been simpler superficially but would conflict with Main Street's existing distinctions among current business state, historical evidence, projections, events, audit and external authority.

The stronger design is purpose- and scope-aware:

```text
Capability-owned fact
        +
contextual protection classification
        +
purpose-scoped retention requirement
        ↓
authoritative disposition evaluation
        ↓
retain / restrict / minimise /
anonymise / redact / delete
```

This preserves business truth without turning historical evidence into indefinite sensitive-data retention.

A second improvement is making data minimisation a creation-time rule. Avoiding unnecessary collection removes later storage, access, breach and erasure complexity without weakening merchant operations.

A third improvement is treating events, audit and provider evidence as independent retention subjects rather than assuming `history` means immutable full payloads forever.

No further material cross-cutting ambiguity remains within DDR-OD-001 after the approved boundary and representative-domain falsification.

---

# 38. Governance verdict

**ACCEPTED.**

MS-PROT-053 closes DDR-OD-001 at the architecture/design level.

It establishes the governing separation between business lifecycle and data-protection/retention lifecycle; contextual handling classifications; purpose-scoped retention requirements; minimal evidence retention; bounded erasure/anonymisation/redaction; secret-data handling; and propagation of authorised disposition into derived representations.

Exact legal durations, persistence mechanisms and operational tooling remain downstream policy/implementation decisions under the accepted authority above.
