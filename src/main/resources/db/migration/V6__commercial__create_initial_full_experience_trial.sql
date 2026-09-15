CREATE TABLE initial_full_experience_trial (
    merchant_identifier VARCHAR(200) PRIMARY KEY,
    trial_identity VARCHAR(200) NOT NULL,
    origin_configuration_revision_identifier VARCHAR(200) NOT NULL,
    originating_first_activation_identity VARCHAR(200) NOT NULL,
    starts_at TIMESTAMPTZ NOT NULL
);
