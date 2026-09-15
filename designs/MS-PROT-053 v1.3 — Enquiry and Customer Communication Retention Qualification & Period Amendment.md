# MS-PROT-053 v1.3 — Enquiry and Customer Communication Retention Qualification & Period Amendment

**Document ID:** MS-PROT-053  
**Version:** 1.3  
**Status:** ACCEPTED  
**Approved:** 8 September 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.2; `DOCUMENT-GOVERNANCE.md`  
**Parent authority:** MS-PROT-053 v1.2 — Production Data Lifecycle Evaluation, Disposition & Convergence Contract Amendment  
**Composes with:** MS-PROT-043, composite MS-PROT-053 through v1.2, MS-PROT-056, MS-PROT-086, MS-TAS-RECOVERY-001 and applicable source-capability authorities  
**Resolves:** MS-PROT-043-V14-DQ-007 — Enquiry/communication retention periods  
**Implementation activation:** NONE

---

# 1. Purpose

This amendment defines the production retention qualification and concrete baseline retention periods for Main Street Enquiry and Customer Communication data.

It resolves the previously deferred question of how long ordinary Enquiry and Customer Communication information remains retainable while preserving the existing architectural distinction between:

- durable acceptance and persistence;
- operational usefulness;
- business and evidentiary retention;
- merchant-visible history;
- commercial entitlement;
- data-use authorization;
- physical storage tier;
- disposition.

This amendment does not make all customer communication a permanent merchant business record.

---

# 2. Governing Principle

Main Street SHALL retain customer communication only while one or more continuing purposes or owner-qualified retention requirements justify retention.

The fact that communication was accepted, persisted, inexpensive to store, historically useful, visible to a merchant, associated with a registered customer, or available under a paid plan SHALL NOT by itself justify indefinite retention.

The governing distinction is:

```text
accepted and durably persisted
        ≠
retained indefinitely

retained
        ≠
merchant-visible indefinitely

retained
        ≠
usable for every purpose

retained
        ≠
AI-eligible

cheap to store
        ≠
authorized to retain
```

---

# 3. Authority Ownership

## 3.1 Data Lifecycle authority

MS-PROT-053 owns:

- retention qualification;
- retention requirements;
- retention periods defined by this amendment;
- disposition evaluation;
- `RETAIN`, `DISPOSITION_DUE` and `UNRESOLVED`;
- lifecycle convergence;
- minimum-evidence retention;
- use-versus-retention separation.

## 3.2 Enquiry authority

MS-PROT-043 continues to own:

- Enquiry meaning;
- Enquiry acceptance;
- Enquiry provenance;
- merchant observation semantics.

This amendment does not redefine Enquiry.

## 3.3 Customer Communication authority

MS-PROT-086 continues to own:

- Conversation;
- ConversationMessage;
- Message acceptance;
- continuity;
- channel semantics;
- participant semantics;
- provider-neutral communication semantics.

This amendment does not redefine Conversation or ConversationMessage.

## 3.4 Commercial authority

MS-PROT-056 continues to own:

- whether a plan is entitled to a capability;
- commercial availability;
- plan transitions;
- merchant-facing commercial packaging.

Subscription state SHALL NOT become retention authority merely because it controls commercial access to Messaging.

## 3.5 Backup and recovery authority

MS-TAS-RECOVERY-001 continues to own backup-generation retention and recovery windows.

This amendment SHALL NOT establish a competing Messaging-specific backup-retention period.

---

# 4. Definitions

## 4.1 Retention Qualification

**Retention Qualification** is the owner-qualified evaluation of which continuing purposes and retention requirements apply to communication data at a particular time.

Retention Qualification is not an importance score.

Multiple requirements MAY coexist.

## 4.2 Transitory Enquiry

A **Transitory Enquiry** is accepted Enquiry content which, at the point of evaluation, is not necessary as material context or evidence for:

- an authoritative business object;
- a material customer or merchant instruction;
- an agreement, approval, authorization or commitment;
- a transaction;
- a booking, order, quotation or fulfilment event;
- a cancellation or refund;
- a complaint or dispute;
- an applicable safety requirement;
- an applicable rights/privacy event;
- a regulatory or statutory obligation;
- a legal or preservation hold;
- an active incident;
- or another owner-qualified retention requirement.

A registered CustomerContext does not by itself prevent an Enquiry from being Transitory.

## 4.3 Ordinary Customer Communication

**Ordinary Customer Communication** is accepted ConversationMessage content retained for ordinary customer-service and communication continuity but not independently qualified for a longer owner-qualified business, evidentiary, regulatory or preservation requirement.

## 4.4 Qualified Business Evidence

**Qualified Business Evidence** is the minimum communication content and context materially required to evidence a business fact, event, instruction, decision, agreement, transaction, state transition or other source-capability matter.

Qualification does not transfer business truth from the owning source capability to Customer Communication.

## 4.5 Preserved Communication

**Preserved Communication** is communication whose otherwise applicable disposition is temporarily blocked or extended by an active owner-qualified preservation requirement, including an applicable dispute, investigation, legal/regulatory requirement or hold.

Preservation does not mean permanent retention.

## 4.6 Retention Scope

**Retention Scope** is the minimum communication extent necessary to satisfy a valid retention requirement.

A Retention Scope MAY be:

- a component of a Message;
- one Message;
- selected Messages;
- a Conversation segment;
- structured business evidence plus provenance;
- minimum residual evidence;
- or, exceptionally, an entire Conversation where lesser context cannot preserve the required meaning.

Whole-Conversation retention SHALL NOT be the default merely because one Message is material evidence.

## 4.7 Dead Storage

“Dead storage” is an engineering and economic diagnostic for information that consumes storage despite having no remaining valid retention purpose.

It is not a normative lifecycle state.

Data for which no valid retention requirement remains SHALL be evaluated through MS-PROT-053 and normally become `DISPOSITION_DUE`.

---

# 5. Concrete Retention Schedule

The following periods are normative Main Street baseline periods.

| Data scope | Baseline period | Retention clock |
|---|---:|---|
| Transitory Enquiry content | **12 calendar months** | Original accepted Enquiry submission |
| Ordinary ConversationMessage content | **24 calendar months** | Individual Message canonical acceptance |
| Temporary access/security information embedded in communication | **As soon as purpose ends; no later than 30 days after purpose end** | Related operational purpose completion |
| Successfully canonicalised raw provider payload | **30 days** | Successful canonical acceptance |
| Failed, quarantined or unresolved provider payload | **90 days** | Provider receipt/failure |
| Ordinary abuse/security diagnostic data | **90 days** | Relevant event |
| Delivery/bounce/correlation evidence | **Same period as related Message, capped at 24 months for ordinary communication** | Related canonical Message acceptance |
| Minimum disposition/replay/tombstone evidence | **12 calendar months after disposition** | Effective disposition |
| AI/search/analytics/cache-derived representation | **No longer than source absent independent owner-qualified requirement** | Source lifecycle |
| Qualified Business Evidence | **Longest applicable owner-qualified source/domain/jurisdiction requirement** | Trigger defined by that requirement |
| Active preserved communication | **Until preservation requirement ends, then immediate reevaluation** | No new retention clock is created merely by preservation |
| Backup copies | **Owned exclusively by MS-TAS-RECOVERY-001** | Recovery authority |

---

# 6. Time Semantics

A 12-month or 24-month period SHALL be calculated as a calendar-month interval from the authoritative lifecycle timestamp.

A 30-day or 90-day period SHALL be calculated from the authoritative event timestamp.

Migration time, import time, provider retry time, merchant observation time and later Conversation activity SHALL NOT replace the original authoritative timestamp unless the owning requirement explicitly defines another trigger.

An implementation SHALL use one deterministic time authority for lifecycle calculations.

---

# 7. Transitory Enquiry Retention

A Transitory Enquiry SHALL ordinarily remain retainable for 12 calendar months from its accepted submission.

This period provides limited continuity across normal and seasonal small-business interactions without converting every public question into indefinite merchant history.

Examples include:

- opening-hours questions;
- location questions;
- simple availability questions;
- generic product/service questions;
- routine introductory communication;
- ordinary acknowledgement.

The 12-month period is a maximum baseline entitlement to ordinary retention, not a requirement to preserve information that an earlier valid disposition requirement requires Main Street to erase, anonymise or restrict.

A later customer interaction SHALL NOT restart an expired or running Transitory Enquiry retention clock merely because the same person, endpoint or CustomerContext communicates again.

---

# 8. Ordinary Customer Communication Retention

Each Ordinary ConversationMessage SHALL ordinarily remain retainable for 24 calendar months from that Message's own canonical acceptance.

The retention clock is per Message.

It SHALL NOT be calculated as:

```text
24 months after latest Conversation activity
```

because that would allow one new Message to refresh years of unrelated history.

Therefore:

- later Messages do not reset earlier Message clocks;
- provider retries do not reset clocks;
- duplicate webhook delivery does not reset clocks;
- bounce events do not reset clocks;
- spam does not reset clocks;
- automatic replies do not reset clocks;
- merchant observation does not reset clocks;
- account login does not reset clocks.

A Message MAY remain longer only where another valid retention requirement independently applies.

---

# 9. Retention Requalification

Retention Qualification SHALL be reevaluable.

A Message initially treated as Transitory or Ordinary MAY later acquire a materially different retention requirement.

Relevant triggers include:

- creation or modification of an authoritative Booking, Order, Quote, Reservation or other business object;
- material instruction;
- acceptance or authorization;
- payment/refund event;
- cancellation;
- complaint;
- dispute;
- applicable safety event;
- rights request;
- regulatory requirement;
- legal/preservation hold;
- account closure;
- relevant policy/jurisdiction change;
- expiration of an operational purpose;
- restore or migration;
- scheduled lifecycle evaluation.

Requalification SHALL add, replace or remove requirements according to the owning authorities.

It SHALL NOT rewrite historical business truth.

---

# 10. Qualified Business Evidence

Where communication materially evidences a source-capability fact or event, Main Street SHALL determine the minimum Retention Scope required to preserve that evidence.

The source capability remains authoritative for the underlying business truth.

For example:

```text
ConversationMessage:
"Yes, go ahead with the £1,800 quotation."

             ↓

Quote authority:
quote = £1,800
accepted = true
acceptedAt = ...

             +

minimum necessary communication provenance/evidence
```

Main Street SHALL NOT retain an entire Conversation merely because one part of it supports a business record unless surrounding context is required to understand the evidence.

When several valid requirements apply, the effective retention SHALL preserve the required scope until the longest applicable requirement has expired.

If an applicable period cannot be deterministically resolved, Main Street SHALL return `UNRESOLVED`.

It SHALL NOT guess a shorter period and destructively dispose of the evidence.

---

# 11. Context Preservation

A Message SHALL NOT be treated as independently meaningful where the required evidence depends materially on surrounding communication.

For example:

```text
Merchant: The total is £1,800 including parts.
Customer: Okay, go ahead.
```

Retaining only:

```text
"Okay, go ahead."
```

would destroy evidentiary meaning.

Main Street SHALL therefore retain the minimum surrounding segment necessary to preserve context where required.

This does not authorize retention of unrelated earlier or later Conversation content.

---

# 12. Structured Business Fact Extraction

Where communication produces an authoritative structured business fact, Main Street SHOULD prefer that authoritative representation for ongoing business operation.

For example:

```text
"Book me Friday at 3pm."

          ↓

Booking
date = Friday
time = 15:00
```

Creation of the Booking does not automatically require the original Message to survive for the Booking's entire lifecycle.

The Message SHALL remain only where its own ordinary period or another qualified evidence requirement remains valid.

This prevents Customer Communication from becoming a permanent duplicate database of source-capability truth.

---

# 13. Temporary Access and Security Information

Temporary access/security information includes information such as:

- one-time entry codes;
- temporary gate codes;
- temporary lockbox codes;
- short-lived access instructions containing reusable secrets.

Where such information no longer has a valid operational or evidentiary purpose, Main Street SHALL redact or dispose of the sensitive component as soon as reasonably practicable and no later than 30 days after the related operational purpose ends.

The surrounding Message MAY continue under another valid lifecycle requirement.

Example:

```text
"Use gate code 8472 when you arrive tomorrow."
```

may become:

```text
"Use gate code [REDACTED] when you arrive tomorrow."
```

where the surrounding communication remains legitimately retainable but the secret no longer is.

A commercial plan SHALL NOT extend the life of an expired access secret merely to provide longer historical messaging.

---

# 14. Provider Data

## 14.1 Successfully canonicalised provider payload

Once a provider payload has successfully produced the canonical Main Street Message and required provenance, the raw provider payload SHALL ordinarily be disposed of within 30 days.

Raw provider HTML, redundant headers and other provider-specific content SHALL NOT become permanent duplicate communication storage merely because storage is inexpensive.

## 14.2 Failed or unresolved provider payload

A provider payload that cannot yet be safely canonicalised MAY remain for up to 90 days to support:

- deterministic retry;
- diagnosis;
- abuse handling;
- incident investigation;
- correlation recovery.

An active owner-qualified incident or unresolved lifecycle condition MAY justify longer retention.

Once successfully canonicalised, the raw payload SHALL transition to the successfully-canonicalised provider-payload rule.

---

# 15. Delivery and Correlation Evidence

Delivery, bounce and Conversation-correlation evidence SHALL ordinarily follow the lifecycle of the related canonical Message.

For Transitory Enquiry communication, this evidence SHALL not automatically survive to 24 months merely because the platform can store it.

For Ordinary Customer Communication, ordinary delivery/correlation evidence SHALL be capped at 24 months unless independently qualified as longer-term business evidence.

Minimal provider identifiers or digests necessary for replay/idempotency protection MAY remain as minimum tombstone evidence after content disposition.

---

# 16. Conversation Identity and Bindings

Conversation identity, participant bindings, channel bindings and reply-routing information SHALL be retained only while required for:

- retained communication interpretation;
- legitimate communication continuity;
- retained evidence;
- audit;
- another owner-qualified lifecycle purpose.

There SHALL be no automatic additional:

```text
+24 months
```

after the final retained Message.

When full participant/contact/channel information is no longer required, Main Street SHALL minimise or dispose of it independently even if an opaque Conversation identifier or minimum disposition record remains.

---

# 17. Subscription and Commercial Entitlement

Retention is not a commercial-plan status.

Once Main Street has canonically accepted an Enquiry or ConversationMessage, its lifecycle SHALL be governed by this amendment and other applicable lifecycle authorities rather than by subsequent subscription state.

Therefore:

- downgrade SHALL NOT restart or shorten a required retention period merely because a lower plan is active;
- downgrade SHALL NOT cause destructive deletion solely to conform to commercial entitlement;
- upgrade SHALL NOT resurrect lawfully disposed communication;
- paid entitlement SHALL NOT override mandatory minimisation or disposition;
- a Free or lower-tier merchant SHALL NOT lose legitimately required business evidence merely because of plan state;
- a higher plan SHALL NOT create indefinite retention merely because storage is commercially funded.

MS-PROT-056 MAY independently govern whether a merchant can initiate or use a capability and what ordinary presentation/access its plan provides.

Any future proposal to sell longer convenience-history retention as a commercial feature MUST separately satisfy MS-PROT-053 and MS-PROT-056. It is not implicitly authorized by this amendment.

---

# 18. Merchant-Visible History

Merchant-visible history is not identical to retained data.

A Message retained as restricted evidence MAY cease to appear in ordinary messaging history where its normal operational-use period has ended.

Conversely, merchant-facing deletion or disappearance SHALL NOT be represented as physical destruction where another valid retention requirement still exists.

Main Street SHALL preserve the distinction between:

```text
ordinary operational history
restricted retained evidence
disposed data
```

Access to retained evidence SHALL remain separately authorized.

---

# 19. Storage Tiers and Dead Storage

Physical HOT, WARM or COLD storage is an implementation concern.

Storage tier SHALL be selected only after retention qualification answers:

```text
Does this information still have authority to exist?
```

Low access frequency does not make retained evidence disposable.

Low storage cost does not make disposition-due information retainable.

Storage capacity pressure SHALL NOT cause oldest-first or quota-first destructive deletion without lifecycle qualification.

`DISPOSITION_DUE`, not “dead storage”, remains the normative lifecycle conclusion.

---

# 20. Derived Data

Communication lifecycle SHALL converge across materially derived representations.

This includes, where present:

- search indexes;
- analytics copies;
- AI summaries;
- embeddings;
- classifications;
- moderation artefacts;
- caches;
- operational replicas;
- exports controlled by Main Street.

A derived artefact SHALL NOT automatically survive its source merely because it is smaller or no longer visibly contains the original text.

Independent retention requires its own owner-qualified purpose.

Retention of source data SHALL NOT imply authorization to use it for AI.

---

# 21. Disposition Evidence

After content disposition, Main Street MAY retain minimum non-content disposition/replay/tombstone evidence for 12 calendar months.

Such evidence SHALL be limited to what is necessary to:

- prevent accidental resurrection;
- prevent prohibited replay/reprocessing;
- demonstrate lifecycle convergence;
- support disposition idempotency.

It SHALL NOT retain unnecessary Message content or identifying data.

Where another accepted authority requires longer minimum audit evidence, the narrowest representation satisfying that authority SHALL prevail.

---

# 22. Backups and Recovery

Backup-generation retention SHALL remain governed by MS-TAS-RECOVERY-001.

This amendment SHALL NOT create a competing 30-, 35-, 90-day or other backup-generation retention window.

When backup restoration reintroduces data that had already become disposed, redacted, anonymised or restricted in the authoritative post-backup timeline, Main Street SHALL apply lifecycle reconciliation before that restored data becomes ordinarily operational, searchable, visible or AI-usable.

Restore SHALL NOT become a mechanism for resurrecting disposed communication.

---

# 23. Holds and Preservation

A valid preservation requirement SHALL suspend otherwise applicable irreversible disposition for the required Retention Scope.

A hold SHALL:

- identify its owner and basis;
- apply only to the necessary scope;
- not create a permanent-retention presumption;
- be reevaluated when its basis ends.

When preservation ends, Main Street SHALL immediately reevaluate the original and currently applicable lifecycle requirements.

Ending a hold SHALL NOT start a fresh 12- or 24-month ordinary retention period.

---

# 24. Data-Subject and Merchant Closure Events

A data-subject erasure request SHALL trigger lifecycle evaluation.

It SHALL NOT automatically erase material evidence where another valid requirement lawfully continues to require retention.

Conversely, the existence of some retained evidence SHALL NOT justify retaining all customer communication.

Merchant-account closure SHALL similarly trigger lifecycle reevaluation.

Closure:

- may terminate ordinary operational use;
- does not automatically destroy required evidence;
- does not justify retaining ordinary communication indefinitely;
- does not create a new retention clock.

---

# 25. AI Classification

AI MAY assist Main Street in identifying potential retention-significance candidates.

AI SHALL NOT be the sole authority for an irreversible decision to:

- preserve communication beyond its otherwise valid lifecycle;
- delete communication;
- redact material evidence;
- override a hold;
- determine an unknown legal/regulatory requirement.

Where deterministic authority is unavailable and irreversible disposition could destroy material evidence, the result SHALL be `UNRESOLVED`.

---

# 26. Merchant Signals

A merchant action such as:

```text
mark important
star
bookmark
pin
```

MAY affect presentation or provide a qualification signal.

It SHALL NOT by itself create a legal, regulatory or indefinite retention requirement.

Merchant UX SHALL NOT make merchants responsible for selecting statutory retention periods that Main Street can determine from authoritative policy and merchant facts.

---

# 27. Concurrency and Race Safety

Before irreversible disposition, Main Street SHALL re-evaluate or version-check the relevant lifecycle state.

If a valid dispute, hold, business-evidence relationship or other retention requirement becomes effective concurrently with disposition evaluation, Main Street SHALL NOT destroy the newly protected scope.

Disposition operations SHALL be idempotent.

A repeated disposition command SHALL converge on the same permitted state rather than creating additional effects.

---

# 28. Partial Disposition Failure

A disposition SHALL NOT be considered fully converged merely because the canonical database row has changed.

Where deletion/redaction/anonymisation is required across controlled derived representations, failure of one representation SHALL remain an incomplete lifecycle operation and SHALL be retried according to the owning disposition/recovery contract.

Main Street SHALL NOT falsely report complete disposition while prohibited live derived copies remain ordinarily usable.

---

# 29. Migration and Backfill

Migration SHALL preserve original lifecycle timestamps.

Migration/import date SHALL NOT restart:

- 12-month Enquiry retention;
- 24-month Message retention;
- provider-payload retention;
- business-evidence retention.

Where legacy content lacks sufficient provenance to determine safe retention or disposition, Main Street SHALL classify the lifecycle result as `UNRESOLVED`.

Migration SHALL NOT perform mass destructive disposition based on guessed timestamps or guessed significance.

Backfill of retention qualification MAY subsequently resolve the uncertainty.

---

# 30. Duplicate Logical Representation

Where the same logical communication is represented through both Enquiry and Customer Communication semantics, Main Street SHALL NOT be required to store two physical payload copies merely to satisfy both authorities.

The effective lifecycle SHALL preserve the information for the union of all valid owner-qualified requirements.

Physical deduplication SHALL NOT erase semantic provenance.

---

# 31. Attachments

This amendment does not activate or define Message-attachment retention.

Message attachments remain outside the initial text-only Customer Messaging portfolio under MS-PROT-086-DQ-004.

If attachments are later activated, Media/Data Lifecycle authority SHALL define their retention requirements.

Attachment binaries SHALL NOT silently inherit 24-month retention merely because associated Message text does.

---

# 32. Observability and Audit

For each material lifecycle decision, Main Street SHALL be able to establish, at the level required by MS-PROT-053:

- scope evaluated;
- lifecycle policy/version;
- applicable requirement owner;
- authoritative trigger timestamp;
- decision;
- disposition action;
- unresolved reason where applicable;
- preservation requirement where applicable;
- convergence status.

This evidence SHALL itself be minimised according to its owning lifecycle requirements.

---

# 33. Examples

## 33.1 Simple public enquiry

```text
Customer:
"Are you open Sunday?"
```

No booking, transaction, instruction, dispute or other qualification emerges.

Result:

```text
Transitory Enquiry
→ 12 months from accepted submission
→ DISPOSITION_DUE
```

## 33.2 Enquiry becomes booking

```text
"Do you have space Friday?"
"Yes."
"Book me for 3pm."
```

Relevant content becomes related to Booking authority.

The Booking owns the authoritative booking state.

Only communication required for ordinary continuity or qualified evidence survives under its applicable requirements.

## 33.3 Quote acceptance

```text
Merchant:
"£1,800 including parts."

Customer:
"Okay, go ahead."
```

The acceptance and necessary context may become Qualified Business Evidence.

The unrelated remainder of the Conversation does not automatically inherit the extended period.

## 33.4 Gate code

```text
"Gate code is 8472 for tomorrow's delivery."
```

The code is operationally important before delivery but security-sensitive afterwards.

After purpose completion:

```text
dispose/redact as soon as practicable
and no later than 30 days
```

unless an independent requirement applies.

## 33.5 Old Conversation receives a new Message

A customer sends a new Message 23 months after an earlier Message.

The new Message receives its own 24-month ordinary clock.

The old Message's clock is not reset.

---

# 34. Falsification Review

| Scenario | Required result | Outcome |
|---|---|---|
| Simple “Are you open?” enquiry with no further business event | 12-month transitory lifecycle then disposition | PASS |
| Same customer returns after old enquiry was disposed | New interaction does not resurrect old data | PASS |
| Enquiry becomes Booking before expiry | Relevant scope requalified; Booking owns truth | PASS |
| Quote acceptance later disputed | Necessary acceptance/context must have qualified as business evidence | PASS |
| “Okay, go ahead” requires earlier price context | Minimum Conversation segment retained | PASS |
| Whole Conversation contains one evidentiary Message | Unrelated Messages do not automatically inherit evidence retention | PASS |
| Gate code after completed delivery | Secret disposed/redacted promptly; no later than 30 days absent another requirement | PASS |
| Paid merchant wants gate code retained forever | Commercial entitlement cannot override minimisation | PASS |
| New Message arrives near old Message expiry | Old clock is not reset | PASS |
| Provider retries webhook | No new retention clock | PASS |
| Spam/auto-reply occurs | No retention inflation | PASS |
| Registered customer asks trivial question | Registration does not create permanent history | PASS |
| Guest provides material authorization | Guest status does not prevent evidence qualification | PASS |
| Merchant downgrades | No destructive lifecycle rewrite solely because of plan | PASS |
| Merchant upgrades | Disposed history is not resurrected | PASS |
| Storage capacity becomes constrained | No oldest-first destruction bypassing lifecycle | PASS |
| Customer requests erasure during active dispute | Applicable requirements evaluated; no blind deletion | PASS |
| Merchant closes account | Ordinary use may end; evidence and disposition requirements continue correctly | PASS |
| Provider payload contains excess PII | Raw payload disposed on shorter provider-data schedule | PASS |
| Restored backup contains previously disposed Message | Reconciliation prevents operational resurrection | PASS |
| Hold races with scheduled deletion | Current lifecycle rechecked before irreversible disposition | PASS |
| Search index deletion fails | Disposition remains incomplete and converges by retry | PASS |
| Legacy migration lacks original timestamp | `UNRESOLVED`; no destructive guess | PASS |
| Same logical content exists as Enquiry and Message | Requirements compose without requiring duplicate payload retention | PASS |
| AI thinks a Message is “important” | AI alone cannot create indefinite retention | PASS |

**Falsification result: PASS.**

No falsification case requires reopening the central architecture.

---

# 35. Ambiguity Review

The following ambiguities are explicitly closed:

**“Important message”**  
Not a normative concept. Retention is reason-qualified.

**“Converted enquiry”**  
Does not require a universal conversion state. An Enquiry simply acquires additional owner-qualified retention requirements when material business facts arise.

**“24 months of conversation history”**  
Means each ordinary Message has its own 24-month lifecycle. It does not mean 24 months after last Conversation activity.

**“Business evidence”**  
Does not mean every Message associated with a business object. Only minimum material evidence/context qualifies.

**“Preserved”**  
Does not mean permanent. It means an active requirement currently blocks normal disposition.

**“Paid retention”**  
Plan entitlement is not lifecycle authority.

**“Cold storage”**  
Means retained information stored economically; it is not permission to retain data after its purpose ends.

**“Dead storage”**  
Is an engineering/economic diagnostic; normative disposition remains governed by MS-PROT-053.

**“Delete conversation”**  
May involve multiple lifecycle scopes. One evidentiary component can survive while unrelated content is disposed.

**“Customer history”**  
A CustomerContext does not become a permanent dossier of every communication.

**Ambiguity result: PASS.**

---

# 36. Fundamental Vision Conformance

**Result: PASS — VISION-CONFORMING**

The amendment:

- keeps records-management complexity inside Main Street;
- does not require micro/small-business merchants to know legal retention periods;
- permits jurisdiction-aware rules where requirements actually differ;
- avoids indefinite data accumulation;
- controls storage cost without destroying material business evidence;
- preserves provider neutrality;
- keeps source capabilities authoritative;
- supports automated lifecycle operation;
- avoids merchant-specific exceptions;
- keeps commercial packaging separate from semantic correctness.

---

# 37. Architecture Review

**Result: PASS**

Authority ownership is coherent:

```text
MS-PROT-043
    → owns Enquiry meaning

MS-PROT-086
    → owns Conversation/Message meaning

MS-PROT-053 v1.3
    → owns communication lifecycle qualification
      and baseline retention periods

source capability + jurisdiction/domain policy
    → owns applicable business-evidence requirement

MS-PROT-056
    → owns commercial entitlement

MS-TAS-RECOVERY-001
    → owns backup/recovery retention
```

No ownership collision is introduced.

---

# 38. Implementation-Rules Impact

**Result: NO IMPLEMENTATION ACTIVATION**

Acceptance of this amendment SHALL NOT:

- reprioritise implementation automatically;
- activate Customer Messaging implementation;
- activate guest browser continuation;
- activate attachments;
- activate autonomous customer-service responses;
- promote any post-baseline protocol into production implementation.

Any implementation promotion remains governed separately by IMPLEMENTATION-RULES.md.

---

# 39. Deferred-Decision Resolution

Upon acceptance and formalisation:

**MS-PROT-043-V14-DQ-007 — Enquiry/communication retention periods**

SHALL become:

```text
RESOLVED — MS-PROT-053 v1.3
```

The basic Enquiry/Customer Communication duration question SHALL no longer remain deferred.

Jurisdiction/domain-specific business-record periods are not a continuation of this DQ. They are runtime/policy requirements owned by the corresponding source/domain/jurisdiction authorities and SHALL be resolvable before destructive disposition where applicable.

---

# 40. Governance Consequences Upon Approval

Human acceptance of this complete proposal authorizes only formalisation of this approved meaning.

Formalisation SHALL include:

- create MS-PROT-053 v1.3 from this approved proposal;
- update AUTHORITY-INDEX.md;
- update DEFERRED-DECISION-REGISTER.md;
- mark MS-PROT-043-V14-DQ-007 resolved;
- update SEQUENCE.md to reflect removal of the retention prerequisite and recompute the next Customer Messaging candidate;
- review canonical lexicon impact;
- review IMPLEMENTATION-RULES impact;
- run DESIGN-CORPUS-CONFORMANCE;
- commit the resulting governance closure to `development`.

Formalisation SHALL NOT authorize production implementation.

---

# 41. Acceptance Boundary

Approval of this proposal means approval of the complete normative package, including:

- 12-month Transitory Enquiry retention;
- 24-month per-Message Ordinary Customer Communication retention;
- temporary access/security information disposal as soon as purpose ends and no later than 30 days;
- 30-day successfully canonicalised raw-provider retention;
- 90-day unresolved/quarantined provider retention;
- 90-day ordinary security/abuse diagnostics;
- delivery/correlation evidence following the related Message lifecycle;
- 12-month minimum disposition/tombstone period;
- business-evidence inheritance from applicable owner-qualified requirements;
- minimum-scope rather than whole-Conversation evidence retention;
- plan-neutral accepted-message lifecycle;
- no plan-driven destructive downgrade;
- no upgrade resurrection;
- independent use/AI authorization;
- recovery-authority ownership of backup retention;
- restore/disposition convergence;
- race-safe and idempotent disposition;
- migration clocks based on original authoritative timestamps;
- `UNRESOLVED` rather than destructive guessing;
- no attachment-retention activation;
- no implementation activation.

**ACCEPTED — 8 September 2026**
