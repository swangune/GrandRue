-- A WorkAttempt must be durably representable before its outcome is known.
-- Existing classified attempts remain unchanged; only the historical
-- requirement that every row already contain a result is relaxed.

alter table durable_work_attempt
    alter column result_classification drop not null;