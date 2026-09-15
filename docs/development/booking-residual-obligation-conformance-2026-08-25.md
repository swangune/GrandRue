# Booking Residual Obligation Conformance — 25 August 2026

**Status:** VERIFIED implementation evidence  
**Authority:** MS-PROT-040, MS-PROT-042 v1.2-v1.4, MS-PROT-049  
**Branch:** `development`  
**Verified implementation head:** `d97a464f61d0fd2b35d0c140bac88826a11d79de`  
**GitHub Actions run:** `32892012535`

This note records implementation evidence only. It does not create semantic authority.

## Accepted design resolution

Manual approval on 25 August 2026 accepted `MS-PROT-042 v1.4 — Booking Residual Obligation & Discharge Amendment`.

The accepted boundary is:

```text
Booking historical existence
        ≠
Booking-owned outstanding operational obligation
```

For residual-obligation purposes, Booking owns the narrow reservation-commitment distinction:

```text
IN_FORCE
RELEASED
```

This is not a universal Booking lifecycle or fulfilment-outcome model. Payment, refund, fulfilment/no-show, Notification and Provider state remain separately owned facts.

Time passage beyond the reservation interval is not universal discharge authority. A Booking remains outstanding while its authoritative reservation commitment remains `IN_FORCE`; discharge requires Booking-owned authoritative evidence under registered semantics.

## TDD evidence

RED commit:

```text
2ab393b5231ab390a25748b90d9a9bea214c0a6a
test(booking): define residual obligation persistence contract
```

The RED build failed only because `JooqBookingResidualObligationAuthority` did not yet exist.

Production commit:

```text
a385ab70e2b33d2b50254cf8dba1ae92f0bd1357
feat(booking): persist residual reservation obligation state
```

It introduced:

- `BookingResidualObligationAuthority` as a Booking-owned query boundary;
- `JooqBookingResidualObligationAuthority` as the PostgreSQL implementation;
- Flyway V26 adding constrained `reservation_commitment_state` values `IN_FORCE` / `RELEASED` to `booking_booking`.

The first production CI run exposed only a test-fixture JDBC typing defect: raw SQL bound Java `Instant` values without explicit PostgreSQL `timestamp with time zone` casts. Production sources compiled, V26 applied, all unit tests passed and all pre-existing PostgreSQL integration tests remained green.

Fixture correction:

```text
d97a464f61d0fd2b35d0c140bac88826a11d79de
test(booking): bind residual obligation timestamps
```

No production semantics changed in that correction.

## Verified behavior

Executable evidence proves:

1. An `IN_FORCE` Booking remains an outstanding Booking-owned obligation even when its reservation interval is already in the past.
2. A `RELEASED` Booking no longer requires Booking residual management.
3. Residual-obligation lookup is merchant-scoped.
4. The query depends on Booking-owned reservation-commitment state, not reservation timestamps.
5. No cancellation, completion, no-show, refund, fulfilment or provider-state semantics were invented by this slice.

## Full repository gate

GitHub Actions run `32892012535` executed:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

Measured result:

```text
production Java sources             417
test Java sources                   148
unit tests                          434 PASS
PostgreSQL integration tests        146 PASS
total tests                         580 PASS
failures / errors / skipped         0 / 0 / 0
Flyway migrations                   26
PostgreSQL                          18.6
result                              BUILD SUCCESS
```

## Current implementation stop

The next integration seam is not yet implemented:

```text
BookingResidualObligationAuthority
        ↓
?
        ↓
ResidualSurfaceObligationAuthority
        ↓
ContextualSurfaceResolver
```

`ContextualSurfaceResolver` consumes a generic capability-qualified residual-obligation authority:

```text
hasOutstandingObligations(
    MerchantScope,
    capabilityIdentifier
)
```

The accepted corpus establishes that each owning capability remains authoritative for its own outstanding obligations and that Surface composition consumes those facts. It does not yet establish the concrete composition/routing mechanism by which multiple capability-owned authorities are exposed behind the generic Surface boundary.

Implementation is therefore paused at this seam rather than introducing capability-name branching, a speculative generic authority registry, or another unapproved routing mechanism.
