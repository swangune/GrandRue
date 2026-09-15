# MS-PROT-056 v1.4 — Initial Full-Experience Trial Establishment Amendment

**Document ID:** MS-PROT-056  
**Version:** 1.4  
**Status:** **ACCEPTED after governed vertical-slice review, falsification and manual approval**  
**Amends:** MS-PROT-056 v1.0, v1.1, v1.2 and v1.3  
**Depends on:** MS-PROT-040 v1.0 + v1.1, MS-PROT-056 v1.0–v1.3, MS-PROT-065 v1.0, MS-PROT-069 v1.0, MS-PROT-071 v1.0  
**Purpose:** Remove ambiguity over when Main Street's automatic initial 30-day full-experience trial begins, which authority owns that fact, how long it lasts, and how duplicate delivery, later configuration changes, paid-plan changes and infrastructure failure affect it.

---

## 1. Governing decision

> **A Merchant Account's automatic initial 30-day full-experience trial is established exactly once from that Merchant Account's first successfully committed Configuration Revision activation. Its commercial interval begins at that activation instant, lasts exactly 720 elapsed hours, is not restarted by later configuration or commercial changes, and remains independent of configuration, publication, trust, provider readiness and standing-plan authority.**

Canonical separation:

```text
Merchant Configuration authority
    owns first committed configuration activation

Commercial authority
    observes first activation fact
    owns initial trial establishment, duration and grants
```

Configuration activation does not itself become commercial state.

---

## 2. Trial origin

The authoritative trial-origin event is the first successfully committed Configuration Revision activation for the Merchant Account as governed by MS-PROT-040 v1.0 + v1.1.

The trial start MUST NOT be derived from:

```text
Merchant Account establishment
registration timestamp
onboarding completion
merchant approval
compiler completion
storefront publication
first login
first customer interaction
first protected feature use
notification time
consumer retry time
```

The authoritative `startsAt` is the activation instant of that first committed activation.

---

## 3. Why account creation is not the anchor

Merchant Account establishment precedes authoritative executable Merchant Configuration. Starting the 30-day period at account establishment could consume commercial trial duration while the merchant is still completing onboarding and before Main Street has established the merchant's usable supported operating model.

The accepted product meaning is a 30-day full experience of the merchant's supported Main Street operating model. Therefore the first committed configuration activation is the earliest valid commercial anchor.

---

## 4. Trial identity and cardinality

Conceptually:

```text
InitialFullExperienceTrial
{
    trialIdentity
    merchantIdentifier
    originConfigurationRevisionIdentifier
    startsAt
    expiresAt
    establishmentProvenance
}
```

This structure is conceptual and does not mandate one Java type.

For one Merchant Account:

```text
automaticInitialTrialCount <= 1
```

Later configuration replacement, reinstatement, migration, provider reconnection, publication change, plan change or return from inactivity MUST NOT automatically create another initial trial.

Cross-Merchant-Account repeat-trial eligibility or abuse prevention is outside this amendment and requires separately accepted commercial/abuse authority.

---

## 5. Duration and boundary semantics

The trial duration is exactly:

```text
30 × 24 hours = 720 elapsed hours
```

Canonical interval:

```text
startsAt <= evaluationInstant < expiresAt
```

where:

```text
expiresAt = startsAt + 720 hours
```

At exactly `expiresAt`, the full-experience entitlement source is no longer effective.

The commercial duration MUST NOT depend on merchant-local calendar-day boundaries, daylight-saving transitions, storefront timezone or reminder-delivery timing.

---

## 6. Establishment boundary

Configuration activation and trial establishment remain separate authoritative transactions.

Rejected:

```text
configuration activation transaction
    + create trial
    + create commercial grants
```

Accepted:

```text
Configuration Revision activation commits
        ↓
post-commit authoritative activation fact
        ↓
commercial application reaction
        ↓
EstablishInitialFullExperienceTrial
```

Commercial authority owns the trial record and commercial entitlement consequences.

---

## 7. Idempotency and duplicate delivery

The commercial reaction may be delivered more than once.

Repeated processing of the same originating first-activation identity MUST resolve to the same trial and MUST NOT create another trial or extend the expiry instant.

Concurrent establishment attempts MUST atomically preserve:

```text
automaticInitialTrialCount(merchantIdentifier) <= 1
```

A duplicate loser resolves the already-established trial.

---

## 8. Lost acknowledgement and execution uncertainty

If commercial trial establishment commits but acknowledgement is lost, retry MUST resolve the existing committed trial under MS-PROT-069 semantics.

If commit outcome is uncertain, the system MUST reconcile the authoritative outcome before another side-effecting establishment attempt.

Transport failure alone is not evidence that no trial exists.

---

## 9. Delayed commercial processing

If configuration activation commits at time `T` but the Commercial reaction is processed later, successful trial establishment MUST retain:

```text
startsAt = T
```

not the retry or consumer-processing time.

Main Street infrastructure delay MUST NOT silently redefine the authoritative origin instant.

Where Main Street's own failure materially prevents the merchant from receiving the promised full-experience access after `T`, any duration remediation or compensating commercial adjustment MUST be owned by Commercial authority and MUST NOT mutate Configuration history.

---

## 10. Trial clock does not pause for independent conditions

Once established, the trial MUST NOT pause because of:

```text
merchant inactivity
merchant choice not to publish
missing optional provider connection
provider outage
incomplete trust requirement for a particular operation
weekends or closed business hours
configuration replacement
temporary removal of a capability
```

Those conditions retain their own authorities.

The full-experience grant removes the applicable commercial barrier only; it does not bypass semantic, actor, trust, provider, security or operational requirements.

---

## 11. Later configuration changes

The trial remains anchored to the original first activation.

If merchant-relevant supported functionality is configured later during the active trial, applicable full-experience entitlement may apply to that functionality only for the remainder of the original trial interval.

Rejected:

```text
new capability configured on day 12
    → new 30-day trial for that capability
```

unless a future separately accepted feature-specific trial policy explicitly authorises that commercial product.

---

## 12. Paid plans during the trial

Standing-plan grants, paid-plan grants and active full-experience grants are independent commercial sources.

Accepting a paid plan during the active initial trial MUST NOT automatically shorten the initial trial.

Cancelling or downgrading a paid plan during the still-active initial trial MUST NOT automatically cancel the initial trial.

At trial expiry, the full-experience source becomes ineffective and other still-effective commercial grants remain.

---

## 13. Standing Free plan remains independent

The initial full-experience trial does not replace or mutate the standing Free-plan baseline established by earlier MS-PROT-056 authority.

During the trial:

```text
effective commercial grants
    = standing-plan grants
      ∪ active full-experience grants
      ∪ other valid grants
```

At expiry, only the full-experience source ceases to contribute.

No semantic recompilation or conversion of the merchant into a different merchant type occurs.

---

## 14. Expiry is time-derived commercial effectiveness

Commercial correctness MUST NOT depend on a background worker mutating a stored status exactly at expiry.

Trial effectiveness is derived from authoritative interval data and current evaluation time.

Background work may support reminders, projections, communications or analytics, but failure or delay of such work MUST NOT extend the entitlement beyond `expiresAt`.

---

## 15. Reminder interpretation

Earlier MS-PROT-056 reminder policy remains authoritative.

For elapsed-duration semantics, day-15 reminder eligibility begins at:

```text
startsAt + 14 × 24 hours
```

Reminder delivery is not trial authority and does not define expiry.

---

## 16. Failure semantics

Trial establishment MUST distinguish at least:

```text
ESTABLISHED
ALREADY_ESTABLISHED
INELIGIBLE
INVALID_ORIGIN
TECHNICAL_FAILURE_BEFORE_COMMIT
EXECUTION_UNCERTAIN
```

`INVALID_ORIGIN` applies when the supplied activation is not the Merchant Account's first authoritative configuration activation.

`INELIGIBLE` may be produced only by a separately accepted eligibility rule; this amendment does not invent cross-account anti-abuse identity.

---

## 17. Explicit non-ownership

This amendment does not define or own:

```text
Merchant Account identity
controller-to-account cardinality
cross-account repeat-trial abuse policy
Merchant Configuration semantics
configuration activation semantics
plan catalogue semantics
payment evidence
trust requirements
provider readiness
publication state
merchant suspension or closure
feature-specific promotional trials
commercial remediation policy details
```

Each remains with existing or future accepted authority.

---

## 18. Falsification results

The accepted rule survives the reviewed cases:

```text
account created; onboarding takes many days
    → trial not yet started

first configuration activates
    → trial starts exactly then

storefront never published
    → trial still runs

provider connected later
    → original expiry unchanged

configuration replaced or reinstated
    → no restart

new relevant capability added during trial
    → access only through original expiry

paid plan accepted during trial
    → trial remains active through original expiry

duplicate activation reaction delivered
    → same trial resolved

expiry worker delayed or unavailable
    → entitlement still expires by authoritative time
```

---

## 19. Acceptance statement

MS-PROT-056 v1.0–v1.3 remain authoritative outside the scope narrowed by this amendment.

> **One Merchant Account, one automatic initial full-experience trial, first committed configuration activation as the authoritative commercial origin, exactly 720 elapsed hours, and no coupling of commercial duration to later configuration, publication, provider or plan changes.**
