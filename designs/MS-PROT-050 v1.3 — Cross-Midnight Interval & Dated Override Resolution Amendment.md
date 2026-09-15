# MS-PROT-050 v1.3 — Cross-Midnight Interval & Dated Override Resolution Amendment

**Document ID:** MS-PROT-050  
**Version:** 1.3  
**Status:** **ACCEPTED after graph-aware iterative falsification and manual approval**  
**Amends:** MS-PROT-050 v1.1 and MS-PROT-050 v1.2  
**Closes:** DDR-OD-005 — Business Hours overnight interval versus dated-override collision  
**Depends on:** MS-PROT-039 v1.2, MS-PROT-042 v1.2, MS-PROT-050 v1.1, MS-PROT-050 v1.2  
**Purpose:** Define deterministic authority when Business Hours intervals cross midnight and interact with dated Business Operating Overrides, without introducing merchant-authored precedence rules, business-category exceptions, Scheduling-owned reinterpretation, or hidden cross-scope behaviour.

---

# 1. Governing decision

Main Street shall resolve Business Hours cross-midnight behaviour using **effective local-civil-time interval occurrences anchored to an origin local date and one explicit Business Hours Scope**.

An effective interval may legitimately cross midnight into the following local date.

Two complementary authority rules govern such an occurrence:

> **Origin-date authority determines whether the cross-midnight operating occurrence exists.**

> **An explicit dated override on the entered local date determines whether the portion entering that date remains effective there.**

Canonical example:

```text
Weekly Friday:
20:00 → Saturday 02:00
```

The interval is one effective Friday-origin operating occurrence. If Friday's governing hours source removes that occurrence, no Saturday carry-over exists. If the occurrence exists but Saturday has an authoritative dated override, that Saturday override governs the Saturday portion.

---

# 2. Scope is mandatory

All resolution occurs within exactly one explicit Business Hours Scope as defined by MS-PROT-050 v1.2.

Conceptually:

```text
BusinessHoursScope
    +
origin local date
    +
governing hours source
        ↓
effective interval occurrence
        ↓
possible following-date carry-over
```

The rules in this amendment shall not create precedence between:

```text
MERCHANT
```

and:

```text
MERCHANT_LOCATION
```

Scopes remain independently authoritative for the contexts that explicitly reference them.

---

# 3. Origin-date authority

For local date `D`, Main Street first determines the hours source governing `D` at the applicable Business Hours Scope.

The governing source is:

```text
Dated Business Operating Override for D, when present
        otherwise
Standard weekly Business Hours for D
```

That source determines the effective operating interval occurrences originating on `D`.

Therefore a weekly cross-midnight interval does not automatically create following-date opening time if an origin-date override has replaced the weekly interval.

Example:

```text
Weekly Friday:
20:00 → Saturday 02:00

Friday override:
CLOSED
```

Required result:

```text
Friday weekly interval is replaced
        ↓
Friday-origin overnight occurrence does not exist
        ↓
Saturday receives no 00:00 → 02:00 carry-over
```

The following result is forbidden:

```text
Friday = CLOSED
Saturday 01:00 = OPEN
because weekly Friday hours were independently reconstructed
```

---

# 4. Cross-midnight carry-over

Where an effective origin-date interval legitimately crosses midnight, its post-midnight portion may contribute open time to the immediately following local date.

Example:

```text
Friday effective interval:
20:00 → Saturday 02:00

Saturday:
no dated override
```

The resulting effective operating time includes:

```text
Friday 20:00 → 24:00
Saturday 00:00 → 02:00
```

This internal civil-date interpretation does not require Main Street to expose two merchant-facing intervals. The merchant-facing meaning may remain one overnight period:

```text
Friday 20:00 → Saturday 02:00
```

---

# 5. Entered-date override authority

A dated Business Operating Override for local date `D` replaces the complete ordinary effective Business Hours slice for `D` at the same Business Hours Scope.

That replacement includes:

- standard weekly intervals originating on `D`;
- ordinary carry-over entering `D` from the preceding date; and
- any other ordinary Business Hours contribution that would otherwise form the effective operating-hours interpretation for `D`.

Example:

```text
Friday effective interval:
20:00 → Saturday 02:00

Saturday override:
CLOSED
```

Required result:

```text
Friday 23:59 = OPEN
Saturday 00:00 = CLOSED
Saturday 01:00 = CLOSED
```

The Friday-origin occurrence exists, but its Saturday portion is suppressed by Saturday's explicit authority.

---

# 6. Special-hours overrides may cross midnight

A dated Business Operating Override may itself contain a legitimate cross-midnight interval.

Example:

```text
Normal Friday:
20:00 → Saturday 02:00

Special Friday override:
21:00 → Saturday 04:00
```

If Saturday has no dated override, the special Friday occurrence may contribute:

```text
Saturday 00:00 → 04:00
```

If Saturday has:

```text
Saturday override:
CLOSED
```

then Saturday's override suppresses that carry-over.

If Saturday instead has:

```text
Saturday override:
10:00 → 14:00
```

then Saturday's effective Business Hours are:

```text
10:00 → 14:00
```

and not:

```text
00:00 → 04:00
10:00 → 14:00
```

No mandatory paired override records are required merely because a merchant-facing special interval crosses midnight.

---

# 7. Overrides are replacements, not patches

A dated override shall not be interpreted as a partial patch over inherited weekly or carry-over intervals.

If the merchant requires several intervals on an exceptional date, the authoritative override for that date must represent the intended effective intervals for that date.

Example:

```text
Ordinary Saturday effective hours:
00:00 → 02:00
10:00 → 14:00

Exceptional Saturday desired:
00:00 → 01:00
10:00 → 12:00
```

The Saturday override shall represent:

```text
00:00 → 01:00
10:00 → 12:00
```

Main Street shall not interpret an override as:

```text
change these fragments
+
inherit all unspecified ordinary fragments
```

The UX may pre-populate ordinary intervals as editing assistance. Such assistance does not create semantic inheritance or precedence.

---

# 8. Business-facing assistance shall not create precedence semantics

Where an entered date receives carry-over from the preceding date, Main Street may explain the consequence while the merchant edits a dated override.

Example business-facing assistance:

> Friday's normal hours continue until 02:00 Saturday. Should those hours remain open on this exceptional Saturday?

If the merchant chooses to retain them, Main Street shall produce an override representation consistent with the authoritative effective hours intended for Saturday.

Main Street shall not introduce semantic flags such as:

```text
preservePriorDayCarryOver
ignoreEnteredDateOverride
originDayWins
followingDayWins
```

The governing authority remains the deterministic contract in this amendment.

---

# 9. Effective-dated operating exceptions

A temporary or emergency change to operating time shall be represented through the established effective-dated Business Hours exception/override model rather than by mutating stable weekly hours or inventing a second temporal authority.

Where historical truth matters, the effective intervals should represent the actual intended operating period rather than retroactively implying that the merchant was closed for time during which it had legitimately operated.

Example:

```text
Ordinary carry-over:
Saturday 00:00 → 03:00

Emergency closure at 00:30
```

An effective exception may represent:

```text
Saturday 00:00 → 00:30
```

where that is the required authoritative interpretation.

---

# 10. Consumer rule

Business Hours remains the temporal authority for effective operating hours.

Consumers shall consume the resolved result and shall not independently reinterpret weekly hours, dated overrides or overnight collisions.

This applies to at least:

- storefront/public operating status;
- next-open calculation;
- Scheduling operating constraints;
- Appointment candidate-time generation;
- Enquiry acknowledgement that references open/closed state;
- merchant dashboard projections; and
- other registered consumers of effective Business Hours.

Canonical:

```text
Business Hours configuration
        ↓
Business Hours resolution
        ↓
Effective Business Hours
        ↓
consumer
```

Rejected:

```text
consumer
    ↓
re-read weekly hours
    ↓
apply its own overnight precedence
```

---

# 11. Scheduling and Appointment consequence

Scheduling shall use Effective Business Hours as an operating constraint.

Example:

```text
Friday 20:00 → Saturday 02:00
Saturday override CLOSED
```

At Saturday 01:00:

```text
Effective Business Hours = CLOSED
        ↓
Scheduling shall not offer 01:00
```

Scheduling shall not recover the time from Friday's underlying weekly definition.

Appointment and Booking semantics remain owned by their respective capabilities under MS-PROT-042. This amendment does not merge or redefine them.

A later Business Hours change shall not by itself erase a valid historical customer commitment. Commitment modification/cancellation remains governed by the owning lifecycle semantics.

---

# 12. Public status and next-open consequence

Public open/closed status and next-open calculation shall use the same resolved Effective Business Hours.

Example:

```text
Friday:
20:00 → Saturday 02:00

Saturday override:
CLOSED

Sunday:
10:00 → 16:00
```

At Saturday 00:30:

```text
status = CLOSED
next open = Sunday 10:00
```

A next-open resolver shall not rediscover discarded Saturday carry-over from the underlying Friday weekly pattern.

---

# 13. Enquiry remains independent

This amendment does not make Enquiry availability dependent on Business Hours.

A merchant may be operationally closed while its digital Enquiry surface remains available.

Business Hours may supply truthful context such as:

```text
currently closed
next open Sunday 10:00
```

but shall not disable Enquiry unless a separate accepted policy explicitly governs that behaviour.

---

# 14. Multi-location consequence

Resolution remains scope-local.

Example:

```text
Swansea location
Friday 20:00 → Saturday 02:00
Saturday override CLOSED

Cardiff location
Friday 20:00 → Saturday 02:00
Saturday no override
```

At Saturday 01:00:

```text
Swansea = CLOSED
Cardiff = OPEN
```

Main Street shall not infer an unscoped merchant-wide operating status from those results unless an explicit merchant-level Business Hours Scope independently governs the context.

---

# 15. Timezone and DST

Existing MS-PROT-050 timezone authority remains unchanged.

Business Hours are expressed in local civil time under an explicit IANA timezone.

Cross-midnight ownership and override resolution are determined in local civil-date terms before any consumer requiring a unique real instant performs instant resolution.

Therefore this amendment does not introduce a separate DST subsystem.

For authoritative scheduled commitments, existing Scheduling rules requiring an unambiguous real instant remain authoritative.

---

# 16. Deterministic resolution model

A conforming implementation shall preserve the following semantic ordering:

```text
1. Select explicit Business Hours Scope.
2. Determine applicable IANA timezone.
3. Determine origin local date D.
4. Select D's governing hours source:
       dated override when present,
       otherwise weekly hours.
5. Materialise effective interval occurrences originating on D.
6. Permit legitimate occurrences to cross into D+1.
7. When resolving D+1, determine whether D+1 has an authoritative dated override.
8. If no D+1 override exists, eligible incoming carry-over may contribute to D+1.
9. If a D+1 override exists, it replaces the complete ordinary D+1 effective slice, including incoming carry-over.
10. Publish one Effective Business Hours interpretation for the requested scope/date/context.
11. Consumers use that interpretation without independent precedence logic.
```

The implementation may use a different internal algorithm or data representation if and only if its externally observable semantics are equivalent to this contract.

---

# 17. Prohibited designs

The following are rejected:

- independently reconstructing following-date carry-over from weekly hours after the origin date has overridden that occurrence;
- allowing origin-date weekly hours to defeat an explicit entered-date override;
- treating dated overrides as implicit partial patches;
- merchant-authored overnight precedence flags;
- business-category-specific overnight rules;
- Scheduling-owned Business Hours precedence;
- storefront-owned Business Hours precedence;
- hidden merchant/location scope fallback;
- UTC-only storage semantics that erase merchant-facing local civil-time meaning; and
- requiring a new universal temporal framework solely to resolve this collision.

---

# 18. Graph-aware falsification record

The accepted rule was not obtained through a one-pass design decision.

The first candidate was:

```text
each local date independently derives its weekly date slice
+
entered-date override replaces that slice
```

Falsification exposed the following counterexample:

```text
Weekly Friday:
20:00 → Saturday 02:00

Friday override:
CLOSED

Naïve independent Saturday derivation:
00:00 → 02:00 OPEN
```

That result was invalid because the Friday-origin occurrence never existed.

The proposal therefore re-entered the design loop and was revised to the accepted **origin-anchored occurrence + entered-date override authority** model.

The revised model was then tested against:

- normal overnight operation;
- origin-date closure;
- entered-date closure;
- origin-date special overnight hours;
- entered-date special hours;
- partial exceptional intervals;
- effective emergency closure;
- Scheduling consumption;
- Appointment/Booking authority separation;
- storefront status;
- next-open calculation;
- Enquiry independence;
- merchant/location scope separation; and
- DST/local-civil-time interpretation.

No material contradiction was found within the declared scope.

---

# 19. Validation matrix

| Constraint | Result |
|---|---|
| Origin-date closure cannot resurrect overnight carry-over | PASS |
| Entered-date override governs entered-date slice | PASS |
| Legitimate overnight operation remains representable | PASS |
| Special dated overnight hours remain representable | PASS |
| Overrides remain replacements rather than patches | PASS |
| Scheduling consumes one authoritative result | PASS |
| Appointment/Booking ownership remains unchanged | PASS |
| Storefront and next-open use same authority | PASS |
| Enquiry remains digitally independent | PASS |
| Multi-location scope separation preserved | PASS |
| IANA/local-civil-time authority preserved | PASS |
| DST does not require new semantic ownership | PASS |
| No business-category exception introduced | PASS |
| No merchant-authored precedence language introduced | PASS |
| Composite architecture preserved | PASS |

---

# 20. Accepted result

DDR-OD-005 is closed by the following governing rule:

```text
ORIGIN DATE
    decides whether an effective overnight occurrence exists

        +

ENTERED DATE OVERRIDE
    decides whether the portion entering that date remains effective

        ↓

EFFECTIVE BUSINESS HOURS
    becomes the single result consumed downstream
```

> **Main Street shall preserve overnight intervals as genuine local-civil-time operating occurrences while giving an explicit dated override authoritative control over the local date it represents. No downstream consumer may reconstruct a competing interpretation.**
