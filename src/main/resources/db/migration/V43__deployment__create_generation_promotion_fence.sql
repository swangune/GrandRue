CREATE TABLE ordinary_serving_generation_promotion (
    transition_identifier text PRIMARY KEY,
    cohort_identifier text NOT NULL CHECK (cohort_identifier = 'ORDINARY'),
    prior_generation_identifier text NOT NULL REFERENCES serving_deployment_admission_snapshot(generation_identifier),
    target_generation_identifier text NOT NULL REFERENCES serving_deployment_admission_snapshot(generation_identifier),
    promotion_status text NOT NULL CHECK (promotion_status IN ('PROMOTING', 'FINALIZED', 'ABORTED')),
    promotion_epoch bigint NOT NULL CHECK (promotion_epoch > 0),
    prepared_at timestamp with time zone NOT NULL,
    reconciled_at timestamp with time zone,
    CHECK (prior_generation_identifier <> target_generation_identifier),
    CHECK ((promotion_status = 'PROMOTING' AND reconciled_at IS NULL)
        OR (promotion_status IN ('FINALIZED', 'ABORTED') AND reconciled_at IS NOT NULL))
);

CREATE UNIQUE INDEX ordinary_serving_generation_one_open_promotion
    ON ordinary_serving_generation_promotion (cohort_identifier)
    WHERE promotion_status = 'PROMOTING';

CREATE TABLE ordinary_serving_admission_control (
    cohort_identifier text PRIMARY KEY CHECK (cohort_identifier = 'ORDINARY'),
    lifecycle_state text NOT NULL CHECK (lifecycle_state IN ('STABLE', 'PROMOTING')),
    promotion_epoch bigint NOT NULL CHECK (promotion_epoch >= 0),
    current_generation_identifier text NOT NULL REFERENCES serving_deployment_admission_snapshot(generation_identifier),
    target_generation_identifier text REFERENCES serving_deployment_admission_snapshot(generation_identifier),
    promotion_transition_identifier text REFERENCES ordinary_serving_generation_promotion(transition_identifier),
    last_transition_at timestamp with time zone NOT NULL,
    CHECK ((lifecycle_state = 'STABLE' AND target_generation_identifier IS NULL AND promotion_transition_identifier IS NULL)
        OR (lifecycle_state = 'PROMOTING' AND target_generation_identifier IS NOT NULL AND promotion_transition_identifier IS NOT NULL))
);
