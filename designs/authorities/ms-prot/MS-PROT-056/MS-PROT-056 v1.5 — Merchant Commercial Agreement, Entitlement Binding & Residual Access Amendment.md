# MS-PROT-056 v1.5 — Merchant Commercial Agreement, Entitlement Binding & Residual Access Amendment

**Document ID:** MS-PROT-056  
**Version:** 1.5  
**Status:** **ACCEPTED after governed vertical-slice review, falsification and manual approval**  
**Amends:** MS-PROT-056 v1.0–v1.4  
**Depends on:** MS-PROT-055 v1.0, MS-PROT-056 v1.0–v1.4, MS-PROT-059 v1.0, MS-PROT-062 v1.0, MS-PROT-065 v1.0, MS-PROT-069 v1.0, MS-PROT-071 v1.0  
**Purpose:** Define the authoritative merchant commercial-agreement lifecycle, exact plan-revision binding, entitlement-definition authority, grant provenance, paid-plan transition semantics, and residual commercial access required to resolve pre-existing commitments without allowing plan identity to redefine Merchant Configuration or capability semantics.

---

## 1. Governing decision

Main Street SHALL distinguish:

```text
PLAN CATALOGUE REVISION
    ≠
MERCHANT COMMERCIAL AGREEMENT
    ≠
PAYMENT OBLIGATION
    ≠
PAYMENT EVIDENCE
    ≠
COMMERCIAL ENTITLEMENT GRANT
    ≠
EFFECTIVE COMMERCIAL ENTITLEMENT
    ≠
MERCHANT CONFIGURATION
```

> **A Merchant Commercial Agreement is the Commercial authority's durable record that a Merchant Account is governed by one accepted commercial offer/revision for a defined effective interval and billing basis. Payment evidence may satisfy a commercial condition but does not itself create or mutate the agreement, and entitlement grants derive commercial permission without creating semantic applicability.**

---

## 2. Merchant Commercial Agreement authority

The Commercial context exclusively owns Merchant Commercial Agreement truth.

Conceptually:

```text
MerchantCommercialAgreement
{
    commercialAgreementIdentity
    merchantIdentifier
    planRevisionIdentity
    billingCadence
    effectiveFrom
    effectiveUntilExclusive?
    acceptanceProvenance
}
```

This structure is conceptual and does not mandate one Java type.

A payment provider, payment-evidence record, Merchant Configuration, Runtime, Authentication or Merchant Account authority MUST NOT independently assert which paid commercial agreement governs the merchant.

---

## 3. Exact plan-revision binding

A Merchant Commercial Agreement MUST bind to one exact immutable subscription-plan revision.

Rejected:

```text
agreement -> latest BUSINESS
```

Accepted:

```text
agreement -> BUSINESS-v7
```

Publication of a later catalogue revision MUST NOT silently alter historical or currently effective agreement meaning.

Changing to a later plan revision requires an authoritative commercial transition under the applicable acceptance/change policy.

---

## 4. Standing Free baseline remains separate

The standing Free commercial baseline remains an independently governed Commercial grant source and does not require a fake paid-style agreement, invoice or payment obligation merely because its price is zero.

Conceptually:

```text
Merchant Account
    ↓
Standing FREE commercial baseline
```

This baseline may coexist with trial, paid-agreement and other valid grant sources.

---

## 5. Paid agreement establishment

Where governing commercial terms require successful initial payment, selecting a plan establishes commercial intent only.

Canonical flow:

```text
merchant selects exact plan revision
        ↓
commercial acceptance captured
        ↓
payment obligation established
        ↓
payment processing / provider participation
        ↓
trusted payment evidence
        ↓
Commercial determines required payment condition satisfied
        ↓
Merchant Commercial Agreement becomes effective
        ↓
agreement-derived commercial grants become effective
```

The payment provider does not own the final agreement transition.

---

## 6. Agreement identity and historical reproducibility

Each Merchant Commercial Agreement MUST have immutable identity.

Commercial history MUST remain reconstructable so that Main Street can determine the exact agreement and terms governing any historical instant.

Conceptually:

```text
Agreement A
BUSINESS-v7
Jan 1 <= T < Mar 1

Agreement B
GROWTH-v4
Mar 1 <= T
```

Whether implementation uses separate agreement instances, agreement revisions or agreement periods is downstream provided this invariant is preserved.

---

## 7. Effective interval semantics

Agreement effectiveness uses half-open intervals:

```text
effectiveFrom <= evaluationInstant < effectiveUntilExclusive
```

At a transition boundary `T`, the prior interval may end at `T` and the successor interval may begin at `T` without ambiguity or accidental gap.

Independent grant sources may validly overlap where separately authorised, such as an initial trial overlapping a paid agreement.

---

## 8. Upgrade semantics

An upgrade becomes effective when all conditions required by the accepted commercial/payment policy are authoritatively satisfied.

It MUST NOT become effective merely because:

```text
merchant clicked an upgrade control
provider checkout opened
payment request was submitted
browser redirect occurred
```

When an immediate upgrade becomes authoritative at `T`:

```text
prior paid agreement interval ends at T
successor paid agreement interval begins at T
```

and entitlement consequences follow the commercial transition.

---

## 9. Downgrade semantics

A downgrade scheduled for the current paid-period boundary does not immediately remove current paid-plan entitlement.

Canonical shape:

```text
downgrade requested at R
        ↓
current paid agreement remains authoritative
        ↓
scheduled commercial transition exists
        ↓
paid-period boundary T
        ↓
current interval ends
successor commercial state begins
```

A scheduled change is future commercial intent; it is not itself proof that the target agreement is already effective.

---

## 10. Cancellation semantics

Cancellation means that the paid agreement will not continue beyond the applicable already-paid boundary, subject to governing terms.

Cancellation MUST NOT:

```text
delete commercial history
erase Merchant Configuration
immediately remove independently valid trial grants
cancel existing customer commitments
```

At the effective cancellation boundary, paid-agreement-derived grants cease while standing Free, trial and other independently valid grants continue according to their own authorities.

---

## 11. Payment failure boundary

Payment failure or provider-declined evidence does not directly rewrite Merchant Configuration, the standing plan or entitlement state.

Commercial authority interprets trusted payment evidence under the applicable agreement/payment policy.

Possible consequences such as retry, grace interval, non-renewal, restriction or other delinquency treatment require the governing accepted policy.

This amendment does not invent a grace-period duration or delinquency lifecycle that is not already accepted elsewhere.

---

## 12. Commercial Entitlement Definition authority

The Commercial context owns the commercial meaning of a `CommercialEntitlementIdentity` through an accepted entitlement definition.

Conceptually:

```text
CommercialEntitlementDefinition
{
    entitlementIdentity
    protectedCommercialPurpose
    targetReference
}
```

An entitlement definition references an already-defined Main Street semantic/runtime/service access point.

It MUST NOT create, configure, activate or make that target semantically applicable.

Dependency direction:

```text
existing semantic/runtime/service access point
        ↑ referenced by
Commercial Entitlement Definition
```

not:

```text
Commercial Entitlement
        ↓ creates
semantic operation/capability
```

---

## 13. Entitlement identity stability

Entitlement identity remains stable independently of plan packaging.

The same entitlement may be referenced by:

```text
trial
BUSINESS plan revision
GROWTH plan revision
promotion
commercial remediation
other future accepted source
```

Plan revisions are grant sources; they do not own entitlement meaning.

---

## 14. Grant provenance

Every durable commercial entitlement grant MUST identify an authoritative commercial source sufficient to reconstruct why that merchant possessed that entitlement for that interval.

The architecture must be able to distinguish source classes such as:

```text
standing commercial baseline
initial full-experience trial
paid Merchant Commercial Agreement
commercial remediation
other future accepted source
```

This amendment does not require one enum or persistence representation.

A grant record that contains only merchant, entitlement and time window but cannot identify its authoritative commercial source is insufficient for the mature architecture.

---

## 15. Effective commercial entitlement remains derived

Effective commercial permission at evaluation time is derived from all independently valid grant sources.

Conceptually:

```text
standing baseline grants
+
active trial grants
+
paid-agreement grants
+
promotional/remediation/other accepted grants
+
current time
        ↓
Effective Commercial Entitlement
```

Runtime MUST NOT reduce this to one mutable `currentPlan` field that discards independent grant sources.

---

## 16. Reasoned Commercial Access Decision

Commercial authority SHOULD expose a decision capable of preserving why access was permitted or denied.

Conceptually:

```text
CommercialAccessDecision
{
    entitlementIdentity
    permitted
    effectiveGrantSources
    evaluatedAt
}
```

A Boolean may be a derived convenience but MUST NOT erase the authoritative distinction between semantic inapplicability and commercial denial.

MS-PROT-062 remains the final runtime-composition authority across commercial, semantic, actor, trust, operational and provider predicates.

---

## 17. Residual commercial access for pre-existing commitments

Loss of entitlement to originate new commercially protected activity MUST NOT, by itself, prohibit operations required to inspect, fulfil, reconcile, cancel, refund, communicate about, or otherwise resolve commitments validly established while the relevant commercial permission existed.

Canonical distinction:

```text
create new protected activity
    may be commercially denied

resolve pre-existing authoritative commitment
    may remain commercially permitted where governing authority classifies the operation as commitment-resolution
```

This is residual commitment access, not permanent entitlement to new activity.

---

## 18. Residual access is purpose-bound

Residual commercial permission MUST be tied to:

```text
existing authoritative commitment
+
operation purpose classified by accepted owning authority
```

It MUST NOT be inferred merely because the merchant once held an entitlement.

For example, a merchant may be denied creation of new Booking activity while remaining able to inspect or resolve an existing Booking commitment.

---

## 19. Capability ownership is preserved

Commercial authority does not own Booking, Order, Shipment, Return, Payment or other capability lifecycle semantics.

The owning capability determines whether an operation such as cancel, refund, complete, modify or reschedule is semantically valid for the commitment.

Commercial authority determines only the commercial permission required for the classified purpose.

Runtime then composes both decisions under MS-PROT-062.

---

## 20. Ambiguous operations require owning-authority classification

This amendment MUST NOT universally classify ambiguous operations such as rescheduling as residual commitment-resolution activity.

An operation that appears to modify an existing commitment may also create a materially new obligation or consume new scarce capacity.

Therefore:

> **Residual commercial access applies only to operation purposes explicitly classified as commitment-resolution purposes by accepted owning authority. Commercial MUST NOT infer that classification from operation names.**

---

## 21. Trial and paid agreement coexistence

Acceptance of a paid agreement during an active initial full-experience trial does not terminate the trial unless a separately accepted commercial policy explicitly says otherwise.

Standing Free, active trial, paid-agreement and other valid grant sources may coexist and contribute to the Effective Commercial Entitlement set.

---

## 22. Transaction boundary

Main Street MUST NOT require one distributed transaction spanning payment provider participation, payment evidence, Commercial agreement transition and all entitlement consumers.

Canonical decomposition:

```text
Payment authority
    commits payment evidence

        ↓ post-commit/application orchestration

Commercial authority
    atomically commits commercial-agreement transition
    and authoritative commercial-source facts

        ↓

Entitlement evaluation
    derives effective commercial permission
```

Entitlement grants may be persisted or deterministically derived from authoritative commercial sources provided provenance and historical reproducibility are preserved.

---

## 23. Commercial transition concurrency

A merchant commercial transition MUST be based on an expected current authoritative commercial state/revision.

If concurrent changes race:

```text
request A based on S7
request B based on S7
A commits S8
B finds current state != S7
```

B MUST fail with a commercial-transition conflict and re-evaluate rather than silently overwrite A.

---

## 24. Idempotency

Paid agreement establishment/change commands MUST have logical request identity appropriate to the application use case.

Replay of the same logical request after successful commit MUST resolve the committed transition rather than create duplicate agreements, commercial periods or billing intent.

Commercial idempotency does not replace independent Money/Payment idempotency for monetary side effects.

---

## 25. Failure semantics

The commercial agreement lifecycle MUST distinguish at least:

```text
OFFER_NOT_AVAILABLE
ACCEPTANCE_REQUIRED
PAYMENT_CONDITION_UNSATISFIED
COMMERCIAL_TRANSITION_CONFLICT
ALREADY_APPLIED
TECHNICAL_FAILURE_BEFORE_COMMIT
EXECUTION_UNCERTAIN
SUCCESS
```

Transport/provider timeout MUST NOT be interpreted as definitive payment failure without trusted reconciliation under MS-PROT-069/MS-PROT-055 semantics.

---

## 26. Explicit exclusions

This amendment does not define:

```text
exact grace/delinquency durations
cross-Merchant-Account repeat-trial abuse policy
business-specific classification of ambiguous residual operations
Merchant Configuration semantics
capability-owned business transitions
provider readiness
actor authorisation
trust satisfaction
```

Those remain governed by existing or future accepted authorities.

---

## 27. Acceptance statement

Main Street now has an explicit authoritative path from commercial catalogue and merchant acceptance to durable merchant commercial agreement, provenance-bearing entitlement sources and residual commitment access without making plan identity a semantic architecture.

> **Commercial packaging determines permission; authoritative merchant agreements explain why that permission exists; capability semantics remain independently owned.**
