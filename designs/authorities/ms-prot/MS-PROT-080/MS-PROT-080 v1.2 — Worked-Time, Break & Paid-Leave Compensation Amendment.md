# MS-PROT-080 v1.2 — Worked-Time, Break & Paid-Leave Compensation Amendment

**Document ID:** MS-PROT-080  
**Version:** 1.2  
**Status:** **ACCEPTED by manual approval on 5 September 2026**  
**Approved:** Manual approval of the exact final Workforce Scheduling, Timekeeping, Leave & Compensation authority package presented in ChatGPT on 5 September 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** MS-DESIGN-RULES-001, MS-AVS-001  
**Amends:** MS-PROT-080 v1.1 within worked-time, break and paid-leave compensation-input scope  
**Purpose:** Allow Workforce Compensation and Payroll to consume authoritative worked-time, paid-break and approved-leave evidence without transferring Workforce Scheduling, Timekeeping, Break or Leave ownership into Payroll.

---

# 1. Amendment Scope

MS-PROT-080 v1.1 survives except that Workforce Compensation SHALL consume authoritative time/leave evidence from MS-PROT-081 where those facts determine compensation.

MS-PROT-080 SHALL NOT own Workforce Scheduling, clock events, actual attendance, leave approval or Scheduled Break truth.

---

# 2. Compensation Input Boundary

Workforce Compensation MAY consume:

```text
Approved Worked-Time Evidence
Paid Break Entitlement
Approved Leave Evidence
approved overtime evidence
other accepted compensation inputs
```

according to Compensation Terms and jurisdiction.

---

# 3. Replace Ambiguous Approved Payable Time

Where MS-PROT-080 refers generically to `Approved Payable Time`, the more precise model is:

```text
Approved Worked-Time Evidence
        +
applicable non-work compensation entitlement
        ↓
Compensation Terms
        ↓
Compensation Amount
```

This prevents:

```text
paid holiday
```

from being falsely represented as worked time.

---

# 4. Time-Based Compensation

For compensation calculated directly from time:

```text
Approved Worked Time
×
effective rate
=
worked-time Compensation Amount
```

Actual break intervals do not form part of Approved Worked Time.

---

# 5. Excess Break Rule

For time-based compensation:

> **Excess Break Time MUST NOT contribute to the time-based Compensation Amount.**

This is a hard compensation invariant.

---

# 6. Paid Break Compensation

If applicable Compensation Terms/jurisdiction provide paid scheduled breaks:

```text
Paid Break Entitlement
```

MAY contribute separately to compensation.

Therefore:

```text
Paid Break Entitlement
    ≠ Worked Time
```

---

# 7. Salary Boundary

Fixed/salaried compensation MUST NOT be automatically reduced because Timekeeping identifies Excess Break Time.

Any consequence requires applicable Compensation Terms and jurisdiction rules.

---

# 8. Paid Leave / Holiday Compensation

Where approved leave is compensation-eligible:

```text
Approved Leave Evidence
+
Compensation Terms
+
Jurisdiction Pay Treatment/rules
        ↓
leave compensation
```

The applicable calculation MAY depend on jurisdiction-qualified rules and historical compensation evidence.

Main Street MUST NOT assume:

```text
holiday hours × ordinary current hourly rate
```

is universally correct.

---

# 9. Salaried Leave

For fixed/salaried compensation, approved paid leave MAY result in:

```text
normal salary continuing
```

rather than an additional holiday-payment component.

The resulting calculation must preserve jurisdiction/terms meaning without double-paying.

---

# 10. Unpaid Leave

Approved Leave MAY legitimately produce no compensation where the applicable relationship terms/jurisdiction classify it as unpaid.

---

# 11. Payroll Input Snapshot Amendment

Where applicable, PayrollInputSnapshot MUST identify/reference the exact:

```text
Approved Worked-Time Evidence
Approved Leave Evidence
Paid Break Entitlement evidence
other approved time-related Compensation inputs
```

used in calculation.

Later changes to those inputs before Payroll approval SHALL make the calculation stale under existing MS-PROT-080 rules.

---

# 12. Payroll Does Not Rewrite Time

Payroll MUST NOT:

```text
change CLOCK_IN
change BREAK_START/END
change CLOCK_OUT
approve Worked Time
approve leave
modify a shift
```

If Payroll reveals a suspected error, correction returns to the owning authority.
