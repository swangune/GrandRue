CREATE TABLE configuration_activation_publication_intent (
    publication_intent_identifier VARCHAR(200) PRIMARY KEY,
    activation_request_identifier VARCHAR(200) NOT NULL UNIQUE
        REFERENCES configuration_activation (activation_request_identifier),
    merchant_identifier VARCHAR(200) NOT NULL,
    configuration_revision_identifier VARCHAR(200) NOT NULL,
    release_identifier VARCHAR(200) NOT NULL,
    occurred_at TIMESTAMPTZ NOT NULL,
    published_at TIMESTAMPTZ
);

CREATE INDEX configuration_activation_publication_pending_idx
    ON configuration_activation_publication_intent (occurred_at, publication_intent_identifier)
    WHERE published_at IS NULL;
