-- Enquiry owns immutable initial submission evidence. References do not transfer ownership.
-- Seconds plus nanoseconds preserve the exact Java Instant without database timestamp rounding.
CREATE TABLE enquiry_submission (
    merchant_identifier text NOT NULL CHECK (btrim(merchant_identifier) <> ''),
    enquiry_identity text NOT NULL CHECK (btrim(enquiry_identity) <> ''),
    submitted_epoch_second bigint NOT NULL,
    submitted_nano integer NOT NULL CHECK (submitted_nano BETWEEN 0 AND 999999999),
    question text NOT NULL CHECK (btrim(question) <> ''),
    submitted_name text,
    submitted_email text,
    submitted_telephone text,
    semantic_registry_release_identifier text NOT NULL CHECK (btrim(semantic_registry_release_identifier) <> ''),
    resolved_model_identifier text NOT NULL CHECK (btrim(resolved_model_identifier) <> ''),
    resolved_model_version bigint NOT NULL,
    subject_owner_capability text,
    subject_object_type text,
    subject_identity text,
    subject_revision_identity text,
    customer_context_identity text CHECK (btrim(customer_context_identity) <> ''),
    PRIMARY KEY (merchant_identifier, enquiry_identity),
    CONSTRAINT enquiry_submission_complete_subject_revision CHECK (
        (subject_owner_capability IS NULL AND subject_object_type IS NULL
            AND subject_identity IS NULL AND subject_revision_identity IS NULL)
        OR
        (subject_owner_capability IS NOT NULL AND btrim(subject_owner_capability) <> ''
            AND subject_object_type IS NOT NULL AND btrim(subject_object_type) <> ''
            AND subject_identity IS NOT NULL AND btrim(subject_identity) <> ''
            AND subject_revision_identity IS NOT NULL AND btrim(subject_revision_identity) <> '')
    )
);
