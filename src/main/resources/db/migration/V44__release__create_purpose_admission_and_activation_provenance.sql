-- MS-PROT-040 v1.5: append-only release-purpose authority and D5c4 provenance.

create table semantic_release_purpose_admission_decision (
    admission_decision_identifier text primary key,
    semantic_registry_release_identifier text not null,
    admission_purpose text not null,
    admission_disposition text not null,
    deciding_principal_identifier text not null,
    decision_provenance_reference text not null,
    decided_at timestamptz not null,
    constraint uk_semantic_release_purpose_decision_affinity unique (
        admission_decision_identifier,
        semantic_registry_release_identifier,
        admission_purpose
    ),
    constraint ck_semantic_release_admission_purpose check (
        admission_purpose in (
            'NEW_CONFIGURATION_VALIDATION',
            'NEW_BUSINESS_ACTIVITY'
        )
    ),
    constraint ck_semantic_release_admission_disposition check (
        admission_disposition in ('ADMITTED', 'WITHDRAWN')
    ),
    constraint ck_semantic_release_admission_identities check (
        btrim(admission_decision_identifier) <> '' and
        btrim(semantic_registry_release_identifier) <> '' and
        btrim(deciding_principal_identifier) <> '' and
        btrim(decision_provenance_reference) <> ''
    )
);

create table semantic_release_purpose_current_admission (
    semantic_registry_release_identifier text not null,
    admission_purpose text not null,
    admission_decision_identifier text not null,
    constraint pk_semantic_release_purpose_current_admission primary key (
        semantic_registry_release_identifier,
        admission_purpose
    ),
    constraint fk_semantic_release_purpose_current_decision foreign key (
        admission_decision_identifier,
        semantic_registry_release_identifier,
        admission_purpose
    ) references semantic_release_purpose_admission_decision (
        admission_decision_identifier,
        semantic_registry_release_identifier,
        admission_purpose
    )
);

create table ordinary_new_configuration_semantic_release_reference_revision (
    reference_revision_identifier text primary key,
    semantic_registry_release_identifier text not null,
    validation_admission_decision_identifier text not null,
    validation_admission_purpose text not null default 'NEW_CONFIGURATION_VALIDATION',
    business_activity_admission_decision_identifier text not null,
    business_activity_admission_purpose text not null default 'NEW_BUSINESS_ACTIVITY',
    reference_epoch bigint not null check (reference_epoch > 0),
    selecting_principal_identifier text not null,
    selection_provenance_reference text not null,
    selected_at timestamptz not null,
    constraint uk_ordinary_semantic_release_reference_epoch unique (reference_epoch),
    constraint ck_ordinary_reference_purposes check (
        validation_admission_purpose = 'NEW_CONFIGURATION_VALIDATION' and
        business_activity_admission_purpose = 'NEW_BUSINESS_ACTIVITY'
    ),
    constraint fk_ordinary_reference_validation_decision foreign key (
        validation_admission_decision_identifier,
        semantic_registry_release_identifier,
        validation_admission_purpose
    ) references semantic_release_purpose_admission_decision (
        admission_decision_identifier,
        semantic_registry_release_identifier,
        admission_purpose
    ),
    constraint fk_ordinary_reference_activity_decision foreign key (
        business_activity_admission_decision_identifier,
        semantic_registry_release_identifier,
        business_activity_admission_purpose
    ) references semantic_release_purpose_admission_decision (
        admission_decision_identifier,
        semantic_registry_release_identifier,
        admission_purpose
    ),
    constraint ck_ordinary_reference_identities check (
        btrim(reference_revision_identifier) <> '' and
        btrim(semantic_registry_release_identifier) <> '' and
        btrim(selecting_principal_identifier) <> '' and
        btrim(selection_provenance_reference) <> ''
    )
);

create table ordinary_new_configuration_semantic_release_pointer (
    pointer_identifier text primary key check (pointer_identifier = 'ORDINARY'),
    reference_epoch bigint not null,
    reference_revision_identifier text not null unique,
    constraint fk_ordinary_reference_pointer foreign key (
        reference_revision_identifier
    ) references ordinary_new_configuration_semantic_release_reference_revision (
        reference_revision_identifier
    )
);

alter table serving_deployment_materialised_release
    add constraint uk_serving_materialised_release_digest_affinity unique (
        generation_identifier,
        semantic_registry_release_identifier,
        packaged_bundle_content_digest
    );

alter table configuration_activation
    add column admission_contract_version integer,
    add column semantic_registry_release_identifier text,
    add column validation_admission_decision_identifier text,
    add column validation_admission_purpose text,
    add column business_activity_admission_decision_identifier text,
    add column business_activity_admission_purpose text,
    add column resolved_package_evidence_identifier text,
    add column requirement_set_identifier text,
    add column serving_generation_identifier text,
    add column serving_generation_epoch bigint,
    add column packaged_bundle_content_digest text;

alter table configuration_activation
    add constraint ck_configuration_activation_admission_evidence check (
        (admission_contract_version is null and
            semantic_registry_release_identifier is null and
            validation_admission_decision_identifier is null and
            validation_admission_purpose is null and
            business_activity_admission_decision_identifier is null and
            business_activity_admission_purpose is null and
            resolved_package_evidence_identifier is null and
            requirement_set_identifier is null and
            serving_generation_identifier is null and
            serving_generation_epoch is null and
            packaged_bundle_content_digest is null)
        or
        (admission_contract_version = 1 and
            semantic_registry_release_identifier is not null and
            validation_admission_decision_identifier is not null and
            validation_admission_purpose = 'NEW_CONFIGURATION_VALIDATION' and
            business_activity_admission_decision_identifier is not null and
            business_activity_admission_purpose = 'NEW_BUSINESS_ACTIVITY' and
            resolved_package_evidence_identifier is not null and
            requirement_set_identifier is not null and
            serving_generation_identifier is not null and
            serving_generation_epoch is not null and serving_generation_epoch >= 0 and
            packaged_bundle_content_digest is not null)
    ),
    add constraint fk_configuration_activation_validation_admission foreign key (
        validation_admission_decision_identifier,
        semantic_registry_release_identifier,
        validation_admission_purpose
    ) references semantic_release_purpose_admission_decision (
        admission_decision_identifier,
        semantic_registry_release_identifier,
        admission_purpose
    ),
    add constraint fk_configuration_activation_activity_admission foreign key (
        business_activity_admission_decision_identifier,
        semantic_registry_release_identifier,
        business_activity_admission_purpose
    ) references semantic_release_purpose_admission_decision (
        admission_decision_identifier,
        semantic_registry_release_identifier,
        admission_purpose
    );
