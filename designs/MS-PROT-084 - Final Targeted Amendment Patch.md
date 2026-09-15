# MS-PROT-084 — Final Targeted Amendment Patch

**Document ID:** MS-PROT-084 (final targeted amendment; not the base authority)  
**Version:** 1.0  
**Status:** APPROVED standalone amendment; base-dependent conformance BLOCKED  
**Approved:** 8 September 2026 — explicit manual approval, followed by explicit authorisation to commit this standalone amendment with the missing base recorded as a conformance blocker.  
**Governed by:** DESIGN-RULES.md; DOCUMENT-GOVERNANCE.md; DESIGN-CORPUS-CONFORMANCE.md  
**Amends:** MS-PROT-084 v1.0 draft revised after second falsification — complete base text unavailable.  
**Supersedes:** Only the explicitly identified clauses below, once composed with the exact approved base. No complete supersession.  
**Depends on:** The exact approved MS-PROT-084 base and its accepted dependencies; base composition cannot currently be verified.  
**Closes:** No repository deferred-decision entries through this standalone record.

## Repository formalisation record

The user explicitly authorised committing the supplied text as a standalone approved amendment with the missing base document recorded as a conformance blocker.

**Conformance blocker:** The complete MS-PROT-084 v1.0 draft revised after second falsification is absent from the inspected development tree and was not recovered from the supplied handoff, available workspace, saved-document search or prior-context search. The handoff mentions prepared Git blobs but supplies no recoverable blob identifiers. Unlisted clauses, original invariants 001–037, earlier acceptance criteria and the complete sixteen deferred-decision boundaries cannot be reconstructed from this amendment.

**Result:** Standalone amendment preservation is authorised. Complete MS-PROT-084 repository formalisation, base/amendment composition, corpus-conformance completion and implementation readiness remain BLOCKED. The supplied PASS claims below are preserved approval-stage review statements, not a claim that this repository composition passed verification.

**Historical status text:** The supplied Section 91 and closing paragraph below retain their original PENDING / NOT AUTHORISED language verbatim. Those approval-stage statements are superseded solely as approval/recording status by the user's subsequent manual approval and explicit standalone-commit instruction recorded above. They do not revoke that instruction.

**Section 70 traceability:** The supplied Section 90 names Section 70, but no Section 70 replacement is supplied. This reference is preserved; no Section 70 wording has been invented or amended.

**Governance impact:** Existing accepted authority navigation remains effective. This record does not close MS-PROT-083-DQ-015 or MS-PROT-082-DQ-007, register reconstructed DQ definitions, promote financial terminology into independent authority, reconcile PRD scope as though the complete base were present, or authorise production implementation. IMPLEMENTATION-RULES.md requires no change for preservation of this blocked amendment.

The supplied amendment follows. Markdown spacing and code layout are normalised for readability; substantive wording is preserved.

---

The three residual failures can be closed without changing the architecture or adding a DQ. Below is the exact amendment patch; every unlisted MS-PROT-084 clause remains unchanged.

# MS-PROT-084 — Final Targeted Amendment Patch

**Applies to:** MS-PROT-084 v1.0 draft revised after second falsification  
**Purpose:** Close the three residual defects identified by the final falsification.  
**Effect:** Replace/add only the clauses below. All other MS-PROT-084 wording remains unchanged.

# Amendment 1 — Currency Affinity

## Replace Section 11 final portion with

# 11. Operating Cost Adjustment

An **Operating Cost Adjustment** is an immutable Financial-Operations-owned fact representing an authorised increase or decrease to an existing Operating Cost Occurrence.

Conceptually:

    OperatingCostAdjustment {
        adjustmentIdentity
        occurrenceIdentity
        direction INCREASE | DECREASE
        MonetaryAmount
        sourceAuthority
        provenance
        occurredAt
    }

Adjustment SHALL NOT destructively rewrite the original occurrence.

An Operating Cost Adjustment SHALL use the **same currency** as the Operating Cost Occurrence it adjusts.

Canonical:

    Operating Cost Occurrence
        1,800 GBP
    Operating Cost Adjustment
        DECREASE
        200 GBP

Permitted.

Rejected without separate conversion authority:

    Operating Cost Occurrence
        1,800 GBP
    Operating Cost Adjustment
        DECREASE
        200 EUR

Where an effective amount is required:

    effective occurrence amount = original occurrence amount + same-currency INCREASE adjustments - same-currency DECREASE adjustments

Invariant:

    effective occurrence amount >= 0

An adjustment set that would make the effective occurrence amount negative is invalid for that occurrence and MUST produce an explicit unresolved/discrepancy outcome rather than negative Money or implicit credit semantics.

A cross-currency source fact MAY contribute only where a separately accepted currency-normalisation/conversion contract establishes an authoritative converted `MonetaryAmount` in the occurrence currency.

Until such authority applies:

    cross-currency Operating Cost Adjustment = PROHIBITED / UNRESOLVED

## Replace the relevant arithmetic portion of Section 14 with

Each Finance-Native Payable Adjustment SHALL use the same currency as its source Finance-Native Payable.

Each Finance-Native Receivable Adjustment SHALL use the same currency as its source Finance-Native Receivable.

Therefore:

    effective payable amount = original payable amount + same-currency payable INCREASE adjustments - same-currency payable DECREASE adjustments

and:

    effective receivable amount = original receivable amount + same-currency receivable INCREASE adjustments - same-currency receivable DECREASE adjustments

Hard invariants:

    effective payable amount >= 0
    effective receivable amount >= 0

Cross-currency adjustments MUST NOT participate directly in these calculations.

Where a separately accepted currency-normalisation/conversion contract applies, that contract MAY establish an authoritative converted amount in the source Payable/Receivable currency.

The original foreign-currency evidence and conversion provenance SHALL remain preserved.

Until such authority applies:

    cross-currency adjustment = PROHIBITED / UNRESOLVED

An adjustment that would reduce either effective amount below zero MUST NOT create negative Money or silently manufacture a reverse obligation.

## Replace the arithmetic portion of Section 16 with

A discharge/satisfaction consequence requires accepted owner-qualified semantics establishing:

- exact Payable/Receivable reference;
- exact applied `MonetaryAmount`;
- currency affinity;
- source evidence;
- governing time;
- reconciliation consequence; and
- historical provenance.

An applied amount SHALL use the same currency as the Finance-Native Payable or Receivable to which it is applied.

For one Payable or Receivable:

    accepted applied amount = sum(
        accepted same-currency
        discharge / satisfaction applications
    )

Then:

    outstanding amount = max(
        0,
        effective amount
        -
        accepted applied amount
    )

Cross-currency transaction evidence SHALL NOT be directly subtracted from the source obligation.

Where an accepted currency-normalisation/conversion contract applies:

    foreign-currency evidence
        ↓ authoritative conversion with source/rate/time/provenance
        ↓ converted MonetaryAmount in source obligation currency
        ↓ possible discharge/satisfaction application

Until such conversion authority exists for that purpose:

    cross-currency discharge / satisfaction application = UNRESOLVED

Where:

    accepted applied amount > effective amount

derive:

    excess applied amount = accepted applied amount - effective amount

The excess is reconciliation evidence.

It is NOT automatically:

- credit;
- refund;
- new Payable;
- new Receivable;
- revenue;
- expense;
- owner capital.

A separately accepted semantic consequence is required.

# Amendment 2 — Economic Exposure Overlap

## Add new Section 36.1 immediately after Section 36

# 36.1 Economic Exposure Relationship and Analytical Non-Duplication

Different authoritative financial facts MAY legitimately represent different semantic aspects of the **same underlying economic exposure**.

Examples:

    Operating Cost Occurrence
        ≠ Finance-Native Payable

because:

    cost incidence
        ≠ amount owed

Yet both MAY concern the same £1,800 rent exposure.

Likewise:

    Financing Payment Schedule
        ≠ Finance-Native Payable

while both MAY concern one financing repayment.

Therefore:

**Semantic distinctness does not imply additive independence.**

Where one financial fact:

- originates from;
- establishes;
- schedules;
- realises;
- satisfies;
- discharges;
- adjusts; or
- otherwise materially concerns

the same economic exposure represented by another accepted fact, sufficient owner-qualified relationship/provenance SHALL be preserved so downstream analytics can identify the overlap.

Canonical:

    Operating Cost Occurrence
        £1,800 rent
        ↓ may establish / support
    Finance-Native Payable
        £1,800

This represents:

    one cost incidence + one payable meaning

not automatically:

    £3,600 economic exposure

MS-PROT-083 Analytical Measure Definitions consuming financial facts SHALL account for such relationships.

An Analytical Measure Definition MUST NOT add semantically overlapping financial facts as independent monetary exposure unless its accepted calculation semantics establish that the represented amounts are economically independent for that measure.

Rejected:

    Operating Cost Occurrence
        £1,800
    + Payable generated from that same occurrence
        £1,800
        ↓ financial pressure = £3,600

unless an accepted measure definition establishes a separate legitimate meaning for that sum.

Where overlap cannot be established or excluded with sufficient evidence:

    additive treatment = UNRESOLVED

The applicable measure SHALL preserve uncertainty rather than assume independence.

Hard distinction:

    same economic exposure represented through different semantic perspectives
        ≠ multiple independent economic exposures

This clause does not merge the source facts.

Each source fact retains its accepted semantic owner and independent meaning.

It only prohibits downstream double counting.

## Add to Section 39 — Evidence Coverage

Add:

Evidence coverage also includes knowledge of **economic-exposure overlap**.

Complete source coverage does not imply additive independence.

For example:

    all Operating Cost Occurrences known + all Finance-Native Payables known

does not permit naïve addition if some Payables arise from those same Cost Occurrences.

Where a financial measure depends on combining sources, the applicable analytical definition SHALL establish sufficient overlap/deduplication semantics.

## Add to Section 60 — Near-Term Commitment Pressure

Add:

Near-term commitment pressure MUST distinguish independent commitments from multiple semantic representations of the same underlying commitment.

For example:

    Operating Cost Occurrence
        + Payable arising from that same occurrence

MUST NOT be counted twice merely because both facts are authoritative.

The applicable MS-PROT-083 Analytical Measure Definition SHALL define the accepted source-selection, relationship or deduplication semantics.

If economic independence cannot be established:

    affected aggregate = UNRESOLVED

rather than an inflated monetary total.

# Amendment 3 — Mixed-Use Financial Accounts

## Replace Section 27.1 with

# 27.1 Account Applicability to Financial Purposes

Connection of a Financial Account Reference does not itself establish that all evidence associated with that account may participate in Financial Health or another financial analytical purpose.

Hard distinctions:

    connected account
        ≠ business-relevant account
        ≠ whole account position business-attributable
        ≠ Financial Health eligible whole-balance evidence

Purpose-qualified applicability SHALL establish not merely whether the account has some business relevance, but **which evidence scope is eligible for the exact purpose**.

Conceptually:

    FinancialAccountApplicability {
        accountReference
        MerchantScope
        analyticalPurpose
        eligibleEvidenceScope
        supportingEvidence
        provenance
    }

The exact implementation vocabulary remains deferred, but the semantics MUST distinguish outcomes equivalent to:

    FULL_ACCOUNT_POSITION_ELIGIBLE
    BOUNDED_EVIDENCE_ONLY
    NOT_APPLICABLE
    UNRESOLVED

### FULL_ACCOUNT_POSITION_ELIGIBLE

Sufficient accepted evidence establishes that the applicable whole-account position may legitimately participate in the named business-financial purpose.

### BOUNDED_EVIDENCE_ONLY

The account contains evidence relevant to the business, but Main Street has not established that the whole account position belongs to, or may be attributed to, the business for that purpose.

Examples may include mixed:

    business + personal

accounts.

Individual transactions or other bounded evidence MAY still participate where separately classified, authorised and relevant.

The complete account balance MUST NOT participate merely because some business transactions occur in the account.

### NOT_APPLICABLE

Sufficient evidence establishes that the account/evidence scope does not apply to the named business-financial purpose.

### UNRESOLVED

Main Street cannot establish sufficient applicability.

Hard rule:

    mixed-use account + business relevance
        ≠ whole account balance business cash

Main Street MUST NOT infer whole-account applicability solely because:

- the merchant connected the provider;
- the account uses the merchant's name;
- business transactions appear in the account;
- some business income is received there;
- some business expenses are paid there;
- AI believes the account is mostly business-related.

Purpose-qualified applicability MAY legitimately differ between purposes.

Example:

    mixed-use account
    specific classified business transaction
        → eligible for one bounded
          cost/reconciliation purpose
    whole account balance
        → NOT established as eligible
          for business cash-position purpose

Where whole-position eligibility cannot be established:

    whole account position MUST NOT contribute to whole-business observed cash position

## Replace the relevant part of Section 59 with

# 59. Observed Cash Position

Observed cash position SHALL be based only on accepted account-position evidence under defined analytical semantics.

It MUST preserve:

- represented account scope;
- purpose-qualified account applicability;
- eligible evidence scope;
- account-equivalence confidence/authority;
- exact balance observation kind;
- financial-position polarity where applicable;
- currency;
- observation/effective time;
- coverage qualification.

A whole account position MAY contribute only where the applicable purpose-qualified account contract establishes:

    FULL_ACCOUNT_POSITION_ELIGIBLE

or semantically equivalent accepted authority.

An account classified only for:

    BOUNDED_EVIDENCE_ONLY

MAY supply qualifying transaction/evidence inputs but MUST NOT contribute its entire balance to observed business cash position.

If insufficient fully eligible account-position evidence exists:

    Observed Cash Position = UNKNOWN

or another MS-PROT-083-governed insufficient-evidence outcome.

Observed cash position MUST NOT imply:

- all merchant cash;
- unrestricted cash;
- immediately available funds;
- solvency;
- future liquidity

unless those propositions are independently established.

# Amendment 4 — Hard Invariants

## Add the following to Section 86

### INV-084-038 — Adjustment Currency Affinity

An Operating Cost, Payable or Receivable Adjustment SHALL use the same currency as its source financial fact unless a separately accepted conversion contract establishes an authoritative converted amount in the source currency.

### INV-084-039 — Discharge Currency Affinity

A discharge/satisfaction amount SHALL use the same currency as its source Payable/Receivable unless a separately accepted conversion contract establishes an authoritative converted amount in the source currency.

### INV-084-040 — Semantic Distinctness Is Not Additive Independence

Different authoritative facts describing the same underlying economic exposure MUST NOT be counted as independent monetary exposure merely because their semantic meanings differ.

### INV-084-041 — Unresolved Exposure Overlap Prohibits Naïve Aggregation

Where material economic-exposure overlap cannot be established or excluded, the dependent aggregate MUST remain unresolved rather than assume additive independence.

### INV-084-042 — Business Relevance Is Not Whole-Balance Authority

Evidence that an account is partly used for business MUST NOT establish that its entire account position is attributable to the business.

### INV-084-043 — Account Applicability Is Evidence-Scope Qualified

Financial-account applicability SHALL identify the evidence scope eligible for the applicable purpose; connection or partial business relevance alone is insufficient.

# Amendment 5 — Acceptance and Governance Record

## Add to Section 89 — Acceptance Criteria

MS-PROT-084 is acceptable only if:

- cross-currency adjustments cannot enter source-fact arithmetic without accepted conversion authority;
- cross-currency discharge/satisfaction cannot enter source-fact arithmetic without accepted conversion authority;
- semantically different financial facts representing one economic exposure cannot be naïvely added;
- unresolved economic-exposure overlap produces uncertainty rather than inflated totals;
- mixed-use account business relevance cannot make the whole account balance business-attributable;
- account applicability identifies the eligible evidence scope for the exact analytical purpose.

All previous acceptance criteria remain unchanged.

## Replace the closing portion of Section 90 with

The final falsification identified three residual failures:

1. cross-currency adjustment/discharge arithmetic lacked explicit source-currency affinity;
2. semantically distinct financial facts could still double-count one underlying economic exposure;
3. mixed-use financial-account relevance could still be interpreted as whole-balance business applicability.

These residual defects are addressed through the final targeted amendments to:

- Sections 11, 14, 16 and 70 boundary semantics;
- new Section 36.1;
- Sections 39 and 60;
- Section 27.1;
- Section 59;
- Section 86 hard invariants;
- Section 89 acceptance criteria.

No new capability, provider, accounting formula, statutory responsibility or deferred decision has been introduced.

## Replace Section 91 with

# 91. Governance Outcome

**Fundamental Vision Conformance:** PASS  
**First fresh falsification:** FAIL → REVISED  
**Second falsification:** FAIL → TARGETED REVISION COMPLETED  
**Final falsification:** FAIL → THREE TARGETED AMENDMENTS COMPLETED  
**Cross-capability semantic ownership:** PASS  
**Payment/Finance ownership resolution:** PASS  
**Canonical Money conformance:** PASS  
**Currency-affinity semantics:** PASS  
**Finance-native lifecycle:** PASS  
**Financial evidence ownership/isolation:** PASS  
**Economic-exposure overlap protection:** PASS  
**Financial Account identity/equivalence:** PASS  
**Mixed-use account applicability:** PASS  
**Operating Cost temporal affinity:** PASS  
**Regulatory boundary:** PASS  
**Financial Health composition through MS-PROT-083:** PASS  
**AI non-authority:** PASS  
**Provider neutrality:** PASS  
**Anti-ERP proportionality:** PASS  
**Sixteen deferred decisions:** PRESERVED OPEN  
**Implementation non-promotion:** PASS  
**Corpus conformance check:** PASS  
**Recommendation:** ACCEPT  
**Manual approval:** PENDING  
**Repository formalisation:** NOT AUTHORISED UNTIL MANUAL APPROVAL AND EXPLICIT FORMALISATION INSTRUCTION

## Conformance check

The amended model now conforms to the accepted Payment discipline: monetary adjustments cannot silently cross currencies; existing Payment authority already requires adjustments to use the obligation currency. It also remains consistent with the existing Payment distinction between outstanding and currently due monetary amounts recorded in the repository register.

The overlap amendment does not create a new accounting layer. It simply gives MS-PROT-083 enough source relationship information to avoid mathematically treating two semantic views of one exposure as two exposures. Source ownership remains unchanged, and Financial Health remains an analytical projection rather than a second business-truth aggregate.

The mixed-use-account amendment likewise does not create accounting allocation rules. It takes the conservative position: Main Street may use independently established business transactions from a mixed account, but it cannot call the entire balance “business cash” without stronger evidence.

### Result

**Corpus conformance: PASS.**  
**Architectural recommendation: ACCEPT.**

At this point I would stop the falsification cycle. Further questions such as provider selection, initial indicator portfolio, capital classification, profitability definitions, statutory accounting, FX normalisation and outgoing payment execution are already correctly isolated behind the existing 16 deferred decisions. The remaining step is **manual approval of MS-PROT-084**; approval alone still does not write to the repository.
