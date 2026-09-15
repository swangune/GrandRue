-- MS-PROT-040 v1.4: immutable normalized exact RCP-affined requirements.

alter table configuration_validation_evidence
    add constraint uk_configuration_validation_package_affinity
        unique (
            merchant_identifier,
            configuration_revision_identifier,
            semantic_registry_release_identifier,
            resolved_package_evidence_identifier
        );

create table configuration_new_activity_requirement_set (
    merchant_identifier text not null,
    resolved_package_evidence_identifier text not null,
    configuration_revision_identifier text not null,
    semantic_registry_release_identifier text not null,
    canonicalization_version integer not null,
    requirement_set_identifier text not null,
    evidence_produced_at timestamptz not null,

    constraint pk_configuration_new_activity_requirement_set
        primary key (
            merchant_identifier,
            resolved_package_evidence_identifier
        ),
    constraint uk_cfg_new_activity_set_affinity
        unique (
            merchant_identifier,
            resolved_package_evidence_identifier,
            requirement_set_identifier,
            semantic_registry_release_identifier
        ),
    constraint fk_configuration_new_activity_package_affinity
        foreign key (
            merchant_identifier,
            configuration_revision_identifier,
            semantic_registry_release_identifier,
            resolved_package_evidence_identifier
        ) references configuration_validation_evidence (
            merchant_identifier,
            configuration_revision_identifier,
            semantic_registry_release_identifier,
            resolved_package_evidence_identifier
        ),
    constraint ck_configuration_new_activity_canonicalization
        check (canonicalization_version = 1),
    constraint ck_configuration_new_activity_identity
        check (
            requirement_set_identifier ~
            '^ms-reqset-v1:sha256:[0-9a-f]{64}$'
        )
);

create table configuration_new_activity_requirement (
    merchant_identifier text not null,
    resolved_package_evidence_identifier text not null,
    requirement_set_identifier text not null,
    semantic_registry_release_identifier text not null,
    affected_release_identifier text not null,
    affected_contract_identifier text not null,

    constraint pk_configuration_new_activity_requirement
        primary key (
            merchant_identifier,
            resolved_package_evidence_identifier,
            affected_release_identifier,
            affected_contract_identifier
        ),
    constraint fk_configuration_new_activity_requirement_set
        foreign key (
            merchant_identifier,
            resolved_package_evidence_identifier,
            requirement_set_identifier,
            semantic_registry_release_identifier
        ) references configuration_new_activity_requirement_set (
            merchant_identifier,
            resolved_package_evidence_identifier,
            requirement_set_identifier,
            semantic_registry_release_identifier
        ),
    constraint uk_cfg_new_activity_requirement_affinity
        unique (
            merchant_identifier,
            resolved_package_evidence_identifier,
            requirement_set_identifier,
            semantic_registry_release_identifier,
            affected_release_identifier,
            affected_contract_identifier
        ),
    constraint ck_configuration_new_activity_affected_release
        check (
            affected_release_identifier =
            semantic_registry_release_identifier
        ),
    constraint ck_configuration_new_activity_affected_contract
        check (btrim(affected_contract_identifier) <> '')
);

create table configuration_new_activity_required_contract (
    merchant_identifier text not null,
    resolved_package_evidence_identifier text not null,
    requirement_set_identifier text not null,
    semantic_registry_release_identifier text not null,
    affected_release_identifier text not null,
    affected_contract_identifier text not null,
    required_release_identifier text not null,
    required_contract_identifier text not null,

    constraint pk_configuration_new_activity_required_contract
        primary key (
            merchant_identifier,
            resolved_package_evidence_identifier,
            affected_release_identifier,
            affected_contract_identifier,
            required_release_identifier,
            required_contract_identifier
        ),
    constraint fk_configuration_new_activity_required_requirement
        foreign key (
            merchant_identifier,
            resolved_package_evidence_identifier,
            requirement_set_identifier,
            semantic_registry_release_identifier,
            affected_release_identifier,
            affected_contract_identifier
        ) references configuration_new_activity_requirement (
            merchant_identifier,
            resolved_package_evidence_identifier,
            requirement_set_identifier,
            semantic_registry_release_identifier,
            affected_release_identifier,
            affected_contract_identifier
        ),
    constraint ck_configuration_new_activity_required_release
        check (
            required_release_identifier =
            semantic_registry_release_identifier
        ),
    constraint ck_configuration_new_activity_required_contract
        check (btrim(required_contract_identifier) <> '')
);
