CREATE TABLE merchant_current_configuration_activation (
    merchant_identifier VARCHAR(200) PRIMARY KEY,
    activation_request_identifier VARCHAR(200) NOT NULL UNIQUE,
    configuration_revision_identifier VARCHAR(200) NOT NULL,
    release_identifier VARCHAR(200) NOT NULL,
    CONSTRAINT merchant_current_configuration_activation_evidence_fk
        FOREIGN KEY (activation_request_identifier)
        REFERENCES configuration_activation (activation_request_identifier)
);
