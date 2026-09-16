# MS-PROT-075 v1.2 — Notification Policy Portfolio Closure Amendment

**Document ID:** MS-PROT-075  
**Version:** 1.2  
**Status:** **ACCEPTED by explicit manual approval on 9 September 2026**  
**Approved:** Explicit manual approval on 9 September 2026 after governed review, falsification and complete proposal presentation in ChatGPT  
**Authority type:** Notification semantic/design amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `designs/DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-075 v1.0 + v1.1 within retained provider-selection/status-mapping, retry/backoff, rendering, Notification Preference, marketing-contact-policy boundary, reminder-timing ownership, batching/digest, quiet-hours and open/click tracking scope  
**Resolves:** All eight retained MS-PROT-075 Notification future/deferred decisions listed in the canonical Deferred Decision Register  
**Implementation activation:** NONE

---

# 1. Governing Decision

The Notification model is complete enough that the retained Notification scope SHALL no longer remain an open design queue.

The final responsibility split is:

```text
Originating capability/process
    owns:
        why communication exists
        whether it remains applicable
        business timing where material
        business consequence

Data Protection / Regulatory authority
    owns:
        lawful/permitted communication conditions
        protected-data use
        jurisdiction-qualified restrictions

Notifications
    owns:
        NotificationIntent
        recipient/channel resolution
        NotificationPreference application
        rendering
        NotificationDispatch
        DeliveryAttempt
        DeliveryEvidence
        provider execution coordination

MS-PROT-065
    owns:
        durable wake-up
        background retry scheduling

MS-PROT-048
    owns:
        provider fulfilment binding/readiness
```

No retained Notification decision may transfer those ownership boundaries.

After acceptance of v1.2:

```text
retained MS-PROT-075 Notification decisions = 0
```

A future Notification feature may still require a new governed design decision. That is not the same as keeping the eight current retained items unresolved.

---

# 2. Fulfilment Path, Not Universal External Provider

A Notification channel requires a compatible registered fulfilment path.

An external provider is required only where that path is externally fulfilled.

Canonical:

```text
Notification
    ↓
channel
    ↓
notification / message-delivery
    ↓
fulfiller
        ├── INTERNAL
        └── EXTERNAL PROVIDER
```

Initial channel families remain:

```text
EMAIL
SMS
PUSH
IN_APP
```

`IN_APP` MAY use an internal fulfiller and requires no fake external ProviderConnection.

EMAIL, SMS or PUSH MAY use external providers where their registered fulfilment path requires them.

The absence or failure of one external path does not make the entire Notification capability unavailable.

---

# 3. Provider Selection — Resolved

Specific provider names SHALL NOT become Notification semantic authority.

Rejected:

```text
EMAIL = SendGrid
SMS   = Twilio
PUSH  = Firebase
```

as canonical Notification meaning.

Accepted:

```text
Notification Channel
        ↓
notification / message-delivery
        ↓
exact release-affined Fulfilment Binding
        ↓
provider/internal fulfiller
```

Specific vendor selection is release/configuration authority under composite MS-PROT-048 rather than an unresolved semantic decision.

A deployment MAY change Provider A to Provider B without changing Notification meaning where the new exact fulfilment binding is accepted, compatible and currently admissible.

Absence of a compatible active fulfilment path yields:

```text
FULFILMENT_PATH_NOT_AVAILABLE
```

not arbitrary provider selection.

---

# 4. Provider Status Mapping — Resolved

Every external provider adapter SHALL register an exact versioned **Notification Provider Evidence Profile**.

Conceptually:

```text
NotificationProviderEvidenceProfile
{
    providerContractIdentity
    channel
    providerEventOrResult
    canonicalInterpretation
    evidenceStrength
    correlationRequirements
    duplicateIdentityRule
}
```

Initial canonical interpretations include:

```text
KNOWN_FAILED_BEFORE_PROVIDER_EFFECT
PROVIDER_ACCEPTED
PROVIDER_REJECTED
PROVIDER_REPORTED_DELIVERED
PROVIDER_REPORTED_BOUNCED
PROVIDER_REPORTED_COMPLAINT
EXECUTION_UNCERTAIN
UNINTERPRETED_PROVIDER_EVIDENCE
```

Tracking-dependent evidence remains constrained by §13.

An unknown provider status SHALL become `UNINTERPRETED_PROVIDER_EVIDENCE`. It SHALL NOT be guessed into `DELIVERED`, `FAILED`, `OPENED` or `CLICKED`.

Provider-native status strings are evidence inputs, not canonical Notification state.

---

# 5. Retry and Backoff — Resolved

There SHALL be no universal provider-independent numeric retry schedule.

Every delivery path that permits automatic retry SHALL register one exact immutable:

```text
NotificationDeliveryRetryProfile
```

containing at minimum:

```text
sameAttemptReplayPermitted
maximumSameAttemptReplays
exact replay delay sequence
maximum retry age
newAttempt eligibility rule
uncertainty reconciliation rule
exhaustion disposition
```

This is mandatory exact release data, not an open semantic decision.

Where an active delivery path has no valid Retry Profile:

```text
automatic retry = DISABLED
```

There is no implicit infinite/default retry loop.

Same-attempt replay is permitted only where the provider contract proves safe idempotent re-invocation and the same DeliveryAttempt, provider-effect identity, endpoint and materially exact payload are preserved while current delivery eligibility remains valid.

A new DeliveryAttempt is permitted only after accepted authority establishes that another physical effect is legitimate, such as known pre-effect failure, definitive rejection or separately authorised retry after definitive non-delivery.

Canonical uncertainty rule:

```text
possible prior external effect
        ↓
EXECUTION_UNCERTAIN
        ↓
no blind new attempt
no blind provider fallback
no blind channel fallback
        ↓
reconciliation
```

MS-PROT-065 performs durable retry scheduling. Notifications owns the retry-policy meaning. Worker/queue technology remains implementation detail.

---

# 6. Rendering — Resolved

The initial rendering contract is:

```text
notification-rendering/deterministic-versioned@1
```

Notification rendering SHALL be:

```text
server-controlled
version-affined
deterministic for the same inputs
source-grounded
data-minimised
channel-qualified
reproducible from retained provenance
```

No provider-side mutable template is canonical Notification content.

EMAIL SHALL support a plain-text representation and MAY additionally support HTML where both express materially equivalent meaning.

SMS uses bounded text appropriate to its registered contract.

PUSH may contain only structured fields allowed by its Notification Contract, such as title, bounded body and safe application action/deep-link.

IN_APP may contain title, body, authorised application action and presentation metadata without becoming a separate business authority.

The concrete rendering library is implementation choice. Provider templates MAY be used only as an optimisation where exact immutable affinity to approved rendered meaning is preserved.

AI MAY assist drafting before the applicable approval boundary. AI SHALL NOT dynamically rewrite materially significant Notification content at send time.

---

# 7. Notification Preference — Resolved

The initial Notification Preference vocabulary is:

```text
UNSPECIFIED
PREFERRED
DISABLED
```

scoped to an applicable:

```text
recipient/context
+
Notification Contract/class
+
channel
+
effective interval
```

`UNSPECIFIED` means no explicit recipient preference exists. It does not mean consented, opted in or preferred.

`PREFERRED` ranks an already independently permitted channel ahead of a non-preferred alternative where multiple channels are valid.

`DISABLED` prevents selection where the Notification Contract permits recipient preference control. Preference cannot invalidate a separately lawful mandatory communication path merely by relabelling it, and a Notification Contract cannot label communication required to bypass independent legal/privacy restrictions.

Preference can narrow or rank already-permitted communication choices. It cannot create communication authority.

---

# 8. Preference UI and Defaults — Resolved

There SHALL initially be no universal enterprise-style Notification Preference centre.

Main Street exposes a preference control only where an accepted Notification Contract says the recipient may control that delivery choice.

The ordinary interaction SHOULD be equivalent to:

```text
How would you prefer to receive these updates?
```

not provider/routing administration.

The UI MUST NOT expose NotificationContract IDs, provider identities, routing graphs, retry settings or delivery guarantees to ordinary users.

Absence of preference always means:

```text
UNSPECIFIED
```

There is no hidden default marketing opt-in and no hidden default that every optional channel is disabled.

Marketing permission remains an independent authority.

---

# 9. Marketing-Consent / Contact-Policy Decision — Resolved at the Notification Boundary

Notifications SHALL NOT own statutory marketing permission, lawful-basis truth or jurisdiction-specific direct-marketing policy.

The retained Notification question concerning marketing-consent law/policy is therefore closed by ownership:

```text
Data Protection + applicable Regulatory authority
    determine whether communication is permitted

Marketing
    determines campaign purpose and campaign-specific recipient eligibility

Notifications
    consumes the resulting current permission/contact-policy outcome
```

Notification Preference does not become consent.

Provider availability does not create contact permission.

A provider's own policy may narrow a technically available path but cannot manufacture statutory or business authority.

The exact Marketing permission/suppression/contact portfolio remains governed by `MS-PROT-087-DQ-003` until that separately owned authority receives explicit approval. Its continued existence does not reopen the MS-PROT-075 ownership question.

---

# 10. Reminder Timing — Resolved by Ownership

Notifications SHALL NOT define universal business reminder timing.

Canonical:

```text
Appointment owner
    determines Appointment reminder timing

Booking owner
    determines Booking reminder timing

another business process
    determines its reminder timing

MS-PROT-065
    wakes due work

Notifications
    delivers an already-due communication
```

Rejected:

```text
Notifications:
    all reminders = 24 hours before
```

because that would make delivery infrastructure a hidden business-policy owner.

Any unresolved timing for a specific capability belongs to that capability's governed design portfolio and MUST NOT remain catalogued as an MS-PROT-075 decision.

---

# 11. Batching and Digest — Resolved

The initial generic Notification portfolio provides:

```text
NO GENERIC BATCHING
NO GENERIC DIGEST
```

One Notification Intent remains one logical communication responsibility.

Notifications SHALL NOT combine unrelated Intents merely for convenience.

A future business owner may justify a specific digest through normal feature admission. That would be a new owner-qualified feature, not an unresolved MS-PROT-075 question.

---

# 12. Quiet Hours — Resolved

Initial platform quiet-hour semantics are:

```text
EMAIL
    → no generic Notification quiet-hour restriction

IN_APP
    → no generic Notification quiet-hour restriction

SMS
    → optional/deferrable communication:
      08:00–20:00 recipient-local time

PUSH
    → optional/deferrable communication:
      08:00–20:00 recipient-local time
```

The window is inclusive at 08:00 and exclusive at 20:00.

SMS/PUSH may bypass this quiet period only where the originating Notification Contract explicitly establishes `TIME_CRITICAL` and every applicable communication/privacy/security authority permits immediate delivery.

The channel itself never decides urgency.

For optional/deferrable SMS or PUSH, unresolved recipient-local timezone SHALL NOT be guessed. The quiet-hour-sensitive path is withheld until a safe window can be established; another independently authorised channel MAY be used if its own semantics permit.

---

# 13. Open/Click Analytics — Resolved

The initial platform policy is:

```text
tracking pixel injection = DISABLED

link rewriting solely for behavioural click tracking = DISABLED

open-rate collection = DISABLED

click-through behavioural tracking = DISABLED
```

Notification still records legitimate transport evidence such as provider acceptance/rejection, provider-reported delivery, bounce, complaint and uncertainty where supported and correctly correlated.

Provider-reported open/click evidence SHALL NOT automatically become retained Notification or Marketing analytical evidence.

Unless another accepted authority explicitly permits collection, such signals are discarded/minimised according to the applicable provider/data-lifecycle contract.

Open/click evidence is provider-dependent, privacy-sensitive and unreliable as proof of human attention. Campaign measurement remains separately governed by MS-PROT-087-DQ-005.

---

# 14. Fundamental Vision Conformance

**Outcome:** `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`

The internal model contains provider mapping, retry profiles, rendering affinity, preferences, quiet hours and evidence interpretation because safe cross-channel delivery is intrinsically complex.

That complexity remains internal to Main Street.

Merchants and ordinary staff SHALL NOT be required to administer provider-specific retry rules, evidence mappings, template systems, message brokers or delivery-routing graphs.

This satisfies:

- **Representation Test** — materially different notification channels and fulfilment paths can be represented without provider-specific domain semantics;
- **Coordination Test** — source-owned communication requirements can safely compose with providers, background work, regulatory authority and recipient preferences; and
- **Administrative-Compression Test** — Main Street absorbs transport, retry and provider complexity instead of exposing it to micro/small merchants.

---

# 15. Falsification

## 15.1 Internal notification

```text
IN_APP notification
merchant has no email/SMS/push provider
```

Expected:

```text
internal fulfilment path remains valid
no external provider required
```

**PASS**

## 15.2 Provider outage

```text
EMAIL provider NOT_READY
```

Expected:

```text
EMAIL fulfilment path unavailable/not ready
Notification capability itself remains valid
other independently authorised paths may continue
```

**PASS**

## 15.3 Unknown provider status

Expected:

```text
UNINTERPRETED_PROVIDER_EVIDENCE
```

not fabricated delivery/failure semantics.

**PASS**

## 15.4 Missing Retry Profile

Expected:

```text
no automatic retry
```

**PASS**

## 15.5 Uncertain provider effect

Expected:

```text
reconcile before another physical effect
```

**PASS**

## 15.6 Preference says EMAIL, marketing permission absent

Expected:

```text
no marketing provider attempt
```

**PASS**

## 15.7 Reminder timing

A capability requires a reminder but has not defined when it is due.

Expected:

```text
Notifications does not invent a timing
```

**PASS**

## 15.8 Optional SMS at 02:00 recipient-local time

Expected:

```text
defer until permissible window
unless owner-qualified TIME_CRITICAL authority applies
```

**PASS**

## 15.9 Provider exposes open tracking by default

Expected:

```text
Main Street does not activate or retain behavioural open/click tracking merely because provider supports it
```

**PASS**

---

# 16. Hard Invariants Added by v1.2

```text
INV-075-V12-001
A Notification channel requires a registered fulfilment path, not universally an external provider.

INV-075-V12-002
Provider identity is fulfilment configuration, not Notification meaning.

INV-075-V12-003
Unmapped provider status cannot manufacture Notification evidence.

INV-075-V12-004
No valid Retry Profile means no automatic retry.

INV-075-V12-005
Uncertain external effect blocks blind duplicate delivery/fallback.

INV-075-V12-006
Notification rendering is deterministic and version-affined.

INV-075-V12-007
Notification Preference defaults to UNSPECIFIED.

INV-075-V12-008
Preference cannot create communication authority.

INV-075-V12-009
Notifications do not own statutory marketing permission.

INV-075-V12-010
Notifications do not own business reminder timing.

INV-075-V12-011
No generic Notification digest/batching exists initially.

INV-075-V12-012
Optional SMS/PUSH obey 08:00–20:00 recipient-local quiet hours unless separately TIME_CRITICAL.

INV-075-V12-013
EMAIL/IN_APP have no generic Notification quiet-hour restriction.

INV-075-V12-014
Open/click behavioural tracking is disabled initially.
```

---

# 17. Rejected Alternatives

Rejected:

```text
provider-specific Notification domain types
one mandatory external provider for all Notifications
provider selected by worker/frontend
one universal retry count/backoff
provider status treated directly as canonical Notification state
mutable provider templates as canonical content
Notification Preference treated as consent
universal Reminder Engine timing
one default notification digest
quiet-hour guessing when timezone materially matters
tracking pixels enabled because provider offers them
provider open = proven customer reading
```

---

# 18. Deferred-Scope Outcome

The following are no longer MS-PROT-075 deferred decisions:

```text
specific provider selection
provider-status mapping semantics
retry/backoff policy representation
rendering model
Notification Preference semantics/UI/default boundary
marketing-consent law/policy ownership
reminder timing ownership
generic batching/digest policy
quiet-hours policy
open/click tracking policy
```

Specific vendor/library/configuration values designated by this authority as release or implementation choices do not remain semantic DQs.

New materially different Notification capabilities must pass normal feature admission rather than being treated as continuation of this closed list.

---

# 19. Implementation Boundary

Approval of this amendment does NOT itself activate:

```text
email provider selection
SMS provider selection
push provider selection
provider SDK integration
Notification preference UI
new API routes
new persistence
tracking
Marketing campaigns
```

Implementation remains governed by `designs/IMPLEMENTATION-RULES.md` and the current implementation programme.

---

# 20. Corpus Conformance and Recommendation

This amendment preserves:

```text
MS-PROT-048
    provider fulfilment/readiness

MS-PROT-053
    data protection/lifecycle

MS-PROT-065
    durable work/timing infrastructure

MS-PROT-069
    execution uncertainty/reconciliation

MS-PROT-075 v1.0 + v1.1
    Notification intent/dispatch/delivery semantics

MS-PROT-082
    regulatory/jurisdiction authority

MS-PROT-087
    Marketing campaign and recipient-eligibility ownership
```

No provider-specific Notification domain is created.

No generic consent engine is created.

No universal Reminder Engine is created.

No behavioural-tracking platform is created.

**Final falsification:** PASS  
**Fundamental Vision:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY  
**Recommendation:** ACCEPTED by explicit manual approval on 9 September 2026.
