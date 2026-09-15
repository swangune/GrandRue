CREATE TABLE configuration_activation (
    activation_request_identifier VARCHAR(200) PRIMARY KEY,
    merchant_identifier VARCHAR(200) NOT NULL,
    configuration_revision_identifier VARCHAR(200) NOT NULL,
    release_identifier VARCHAR(200) NOT NULL,
    expected_current_configuration_identifier VARCHAR(200),
    initiating_principal_identifier VARCHAR(200) NOT NULL,
    activated_at TIMESTAMPTZ NOT NULL,
    replaced_configuration_revision_identifier VARCHAR(200),
    CONSTRAINT configuration_activation_replacement_affinity CHECK (
        (expected_current_configuration_identifier IS NULL
            AND replaced_configuration_revision_identifier IS NULL)
        OR (
            expected_current_configuration_identifier IS NOT NULL
            AND replaced_configuration_revision_identifier IS NOT NULL
            AND expected_current_configuration_identifier = replaced_configuration_revision_identifier
        )
    )
);

CREATE INDEX configuration_activation_merchant_history_idx
    ON configuration_activation (merchant_identifier, activated_at, activation_request_identifier);
