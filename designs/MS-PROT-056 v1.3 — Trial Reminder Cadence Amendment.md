# MS-PROT-056 v1.3 — Trial Reminder Cadence Amendment

**Document ID:** MS-PROT-056  
**Version:** 1.3  
**Status:** **ACCEPTED after product-policy review and manual approval**  
**Amends:** MS-PROT-056 reminder provisions, including the prior Day-15-through-expiry daily reminder eligibility rule  
**Depends on:** MS-PROT-056 v1.0, MS-PROT-056 v1.1, MS-PROT-056 v1.2  
**Purpose:** Replace potentially excessive daily subscription reminders during the second half of the initial 30-day full-experience period with a measured milestone cadence that prevents surprise entitlement loss while preserving merchant trust, customer invisibility and merchant-specific impact communication.

---

# 1. Governing amendment

The initial 30-day full-experience entitlement remains unchanged. This amendment changes only the normal reminder cadence before that entitlement expires.

The previous policy permitting a routine subscription reminder on every applicable day from Day 15 through expiry is superseded by the following default milestone cadence:

```text
Day 1–14
    no scheduled subscription-expiry reminder

Day 15
    first reminder
    15 days remaining

Day 21
    reminder
    9 days remaining

Day 25
    reminder
    5 days remaining

Day 28
    reminder
    2 days remaining

Day 29
    reminder
    trial ends tomorrow

Day 30
    expiry-day reminder
```

The merchant dashboard or equivalent merchant-owned Main Street surface may continuously display the remaining full-experience duration without that persistent status indicator counting as a scheduled reminder communication.

---

# 2. Governing objective

The reminder policy shall satisfy both of the following:

1. a merchant shall receive sufficient notice to understand the commercial-access consequences of trial expiry and make an informed plan decision; and
2. Main Street shall not turn a substantial portion of the full-experience period into repetitive subscription sales pressure.

Therefore:

> **Trial-expiry communication shall be timely, progressively salient and merchant-specific, but shall not default to daily outbound reminders merely because the trial has entered its second half.**

---

# 3. Reminder communication is not semantic authority

A trial reminder is a commercial communication consequence of the full-experience lifecycle.

It shall not:

- alter merchant semantic configuration;
- activate or deactivate a capability;
- change the merchant's standing plan;
- change effective entitlement before the applicable entitlement boundary;
- manufacture a subscription decision; or
- become an input into deterministic business semantics.

Canonical separation:

```text
FULL-EXPERIENCE LIFECYCLE
        ↓
REMINDER ELIGIBILITY
        ↓
MERCHANT-SPECIFIC IMPACT PROJECTION
        ↓
COMMERCIAL COMMUNICATION
```

not:

```text
REMINDER
    ↓
CAPABILITY STATE CHANGE
```

---

# 4. Merchant-specific impact is mandatory where determinable

A reminder should explain the effect of expiry using the merchant's configured operating model and the post-trial entitlement position known at the time of communication.

Conceptually:

```text
resolved merchant operating model
        +
current full-experience entitlements
        +
post-trial standing/selected-plan entitlements
        ↓
merchant-relevant entitlement difference
        ↓
expiry reminder content
```

The reminder should distinguish, in business-facing language, functionality that will remain available from functionality for which new activity will no longer be commercially entitled.

It shall not advertise irrelevant capabilities merely because they exist elsewhere in Main Street.

---

# 5. Existing commitments must not be represented as lost

Reminder content must conform to the continuity rules of MS-PROT-056.

Where the merchant has valid commitments created while the full-experience entitlement was active, the reminder shall not imply that those commitments will be deleted, invalidated or held hostage after expiry.

For example, if new online Booking activity will cease to be entitled under the merchant's post-trial plan while future bookings already exist, the reminder must distinguish:

```text
NEW ONLINE BOOKING ACTIVITY
    affected by entitlement expiry

EXISTING VALID BOOKINGS
    preserved and manageable to the extent required for fulfilment
```

---

# 6. Merchant-customer separation

Subscription-expiry reminders are merchant-facing Main Street communications.

Main Street shall not place subscription-upgrade prompts, trial-expiry sales messages or Main Street commercial pressure into the merchant's customer journey merely because merchant functionality will change after expiry.

Customer-facing surfaces shall instead degrade according to the accepted post-trial product policy and applicable capability/surface rules.

---

# 7. Persistent dashboard status

During the full-experience period, Main Street may continuously expose a non-intrusive status such as:

```text
Full experience: 9 days remaining
```

within the merchant dashboard or account/subscription surface.

This status:

- may remain visible between milestone reminders;
- does not constitute an outbound reminder;
- should provide access to the merchant's relevant plan comparison or subscription-management surface; and
- shall not obstruct ordinary merchant operation.

---

# 8. Consolidation and duplicate suppression

Main Street shall avoid multiple subscription-expiry communications for the same milestone merely because several merchant capabilities will be affected.

The normal rule is one consolidated reminder for the merchant at each scheduled milestone.

Where multiple functions are affected, the reminder may summarise the most material merchant-specific consequences and provide access to a fuller comparison.

Duplicate notification delivery caused by retries, multiple channels or concurrent reminder evaluation must be controlled by the notification/application implementation.

---

# 9. Early subscription

If the merchant validly completes the required subscription decision before a future reminder milestone, routine trial-conversion reminders that are no longer relevant shall cease.

This does not shorten the remaining full-experience entitlement. The entitlement and first-renewal rules of MS-PROT-056 remain authoritative.

A merchant who has subscribed early may still see the remaining full-experience duration where useful, but Main Street shall not continue prompting that merchant to choose a subscription they have already selected.

---

# 10. Exceptional communications

The milestone cadence governs normal trial-expiry reminders. It does not prohibit a separate communication required because of a materially different event, such as:

- payment or subscription acceptance failure;
- a merchant-initiated plan change requiring attention;
- a legally required notice;
- an account/security event; or
- another independently governed operational condition.

Such communications must identify their own cause and shall not be disguised as additional routine trial-expiry reminders.

---

# 11. Cadence is commercial policy, not capability semantics

The exact reminder days are versioned Main Street product/commercial policy.

A future evidence-based cadence change shall not require changing merchant capability semantics, configuration compilation, domain state machines or entitlement meaning.

Therefore:

```text
REMINDER CADENCE CHANGE
        ≠
SEMANTIC MIGRATION
```

Any future cadence amendment remains subject to Main Street design governance and explicit approval.

---

# 12. Superseded rule

The following previous default is superseded:

```text
Day 15 through expiry
    routine daily subscription reminder eligibility
```

It is replaced by the milestone schedule in Section 1.

All other MS-PROT-056 rules concerning the 30-day full-experience period, merchant-specific impact explanation, standing Free plan, paid subscription, entitlement continuity, early subscription, upgrade, downgrade, cancellation, existing commitments and commercial availability remain authoritative unless separately amended.

---

# 13. Validation

The amendment has been checked against the governing Main Street constraints:

| Constraint | Result |
|---|---|
| Preserves 30-day full-experience entitlement | PASS |
| Prevents surprise expiry | PASS |
| Reduces repetitive commercial pressure | PASS |
| Preserves merchant semantic configuration | PASS |
| Preserves entitlement authority separation | PASS |
| Supports merchant-specific plan impact | PASS |
| Preserves existing-commitment continuity | PASS |
| Preserves customer invisibility from Main Street commercial prompts | PASS |
| Does not impose a new architectural/programming paradigm | PASS |
| Cadence remains independently evolvable commercial policy | PASS |

No new domain capability, universal extension mechanism, compiler responsibility or runtime semantic authority is introduced by this amendment.

---

# 14. Accepted result

The accepted default Main Street trial reminder policy is therefore:

```text
30-DAY FULL EXPERIENCE
        ↓
Days 1–14: no scheduled expiry reminders
        ↓
Day 15: first reminder
        ↓
Day 21: 9-day reminder
        ↓
Day 25: 5-day reminder
        ↓
Day 28: 2-day reminder
        ↓
Day 29: tomorrow reminder
        ↓
Day 30: expiry-day reminder
```

with a continuously available, non-intrusive dashboard countdown permitted throughout the experience period.

> **Main Street shall make trial expiry difficult to miss without making subscription solicitation a daily burden.**
