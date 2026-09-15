-- Booking outbox completion is technical publication evidence, not global consumer acknowledgement.
-- Authority: MS-PROT-026 v1.1,
-- designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md,
-- §11 — Event Publication Is Not Reaction Completion; §12 — Publication Responsibility;
-- §17 — Per-Reaction Acknowledgement.

drop index if exists booking_outbox_pending_idx;

alter table booking_outbox
    rename column acknowledged_at to published_at;

create index booking_outbox_pending_idx
    on booking_outbox (merchant_identifier, occurred_at, event_identifier)
    where published_at is null;
