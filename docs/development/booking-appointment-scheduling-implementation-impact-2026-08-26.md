# MS-PROT-042 v1.5 Implementation-Rules Impact Review

**Date:** 26 August 2026  
**Status:** Governance / implementation-planning evidence — not semantic authority  
**Accepted semantic authority:** `MS-PROT-042 v1.5 — Booking, Appointment & Scheduling Execution Contract Amendment`

## Result

**No change to `designs/IMPLEMENTATION-RULES.md` is required.**

The accepted amendment is fully implementable under the existing mandatory rules:

- accepted semantic authority is read before implementation;
- capability ownership remains explicit;
- business-type branching remains prohibited;
- test-first change is required;
- cross-capability atomicity is used only where an accepted invariant requires it;
- merchant scope and actor authority remain trusted runtime concerns;
- idempotency must preserve one logical commitment per logical command;
- persistence migrations remain forward-only;
- implementation must stop again if a new material semantic or architectural question is exposed.

## Required implementation consequences

The existing appointment-shaped Booking vertical slice is non-conforming within the new amendment's scope and must be migrated.

Required targets:

```text
booking.confirm
    → Booking only
    → booked subject + reservation scope
    → required Allocation where applicable
    → no synthetic Appointment

appointment.confirm
    → Appointment only
    → Scheduling revalidation seam
    → required Appointment capacity Allocation where applicable
    → no synthetic Booking
```

The consultant prototype must move from `booking.confirm` to `appointment.confirm`.

A new `prototype-motel` reference merchant may use `booking.confirm` with pooled Resource/capacity fixture semantics. It must not be selected through a motel/business-category conditional.

## Persistence migration boundary

The v1.5 authority explicitly permits disposable prototype data not to receive a production-grade semantic data migration. Schema evolution must nevertheless remain forward-only and deterministic.

The implementation should prefer a new migration that establishes the conforming durable command/result structures rather than rewriting an already-applied migration file.

## Lexicon impact review

No `CANONICAL-SEMANTIC-LEXICON.md` change is required in this cycle.

Reason:

- Booking and Appointment are already canonical distinct commitment terms;
- Booking Availability and Appointment Availability are already explicitly qualified;
- `ScheduleIntent` is already a specific accepted Scheduling concept rather than a new shared overloaded noun;
- Calendar remains explicitly governed by MS-PROT-041;
- the new operation identifiers are executable contract identifiers, not cross-domain vocabulary requiring lexicon ownership.

If later implementation introduces a generic bare `Schedule` domain object or another overloaded scheduling term, that must be reviewed separately rather than silently added here.

## Implementation classification

The code work is an accepted-design conformance migration and prototype extension. Once the test baseline is established, implementation/refactoring steps fall under the normal `AUTO_TEST`, `AUTO_FIX`, `AUTO_REFACTOR` and `AUTO_HARDEN` categories so long as they do not alter MS-PROT-042 v1.5 semantics.
