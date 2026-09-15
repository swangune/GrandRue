-- Authority: MS-PROT-040 v1.0,
-- designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
-- §13 Change set; §14 Change-set provenance; §49 Configuration history.
-- Initial provenance remains mandatory only for initial revisions.
alter table merchant_configuration_revision
    alter column source_initial_configuration_intent_identity drop not null,
    alter column source_onboarding_case_identity drop not null,
    alter column source_onboarding_case_revision drop not null,
    alter column onboarding_completion_revision drop not null,
    drop constraint ck_initial_configuration_revision_shape,
    add column source_change_set_identifier text,
    add column change_origin text,
    add column change_source_identifier text,
    -- Canonical Instant text preserves the exact proposal instant, including nanoseconds.
    add column change_proposed_at text,
    add constraint uk_configuration_revision_change
        unique (merchant_identifier, source_change_set_identifier),
    add constraint ck_configuration_revision_provenance_shape check (
        (
            configuration_version = 1
            and base_configuration_revision_identifier is null
            and fulfilment_binding_set_identifier is null
            and fulfilment_binding_set_revision is null
            and source_initial_configuration_intent_identity is not null
            and source_onboarding_case_identity is not null
            and source_onboarding_case_revision is not null
            and onboarding_completion_revision is not null
            and source_change_set_identifier is null
            and change_origin is null
            and change_source_identifier is null
            and change_proposed_at is null
        ) or (
            configuration_version > 1
            and base_configuration_revision_identifier is not null
            and source_initial_configuration_intent_identity is null
            and source_onboarding_case_identity is null
            and source_onboarding_case_revision is null
            and onboarding_completion_revision is null
            and source_change_set_identifier is not null and btrim(source_change_set_identifier) <> ''
            and change_origin is not null and change_origin in (
                'MERCHANT_INITIATED', 'INFERENCE_PROPOSED', 'REGISTERED_DERIVATION', 'PLATFORM_REQUIRED')
            and change_source_identifier is not null and btrim(change_source_identifier) <> ''
            and change_proposed_at is not null and btrim(change_proposed_at) <> ''
        )
    );
