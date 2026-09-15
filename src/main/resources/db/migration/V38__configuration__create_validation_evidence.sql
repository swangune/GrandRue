-- MS-PROT-040 v1.1/v1.3: immutable exact validation/package evidence.

alter table merchant_configuration_revision
    add constraint uk_configuration_revision_release_affinity
        unique (
            merchant_identifier,
            configuration_revision_identifier,
            semantic_registry_release_identifier
        );

create table configuration_validation_evidence (
    merchant_identifier text not null,
    validation_evidence_identifier text not null,
    configuration_revision_identifier text not null,
    semantic_registry_release_identifier text not null,
    resolved_package_evidence_identifier text not null,
    validation_outcome text not null,
    compiler_identifier text not null,
    package_generated_at timestamptz not null,
    evidence_produced_at timestamptz not null,

    constraint pk_configuration_validation_evidence
        primary key (
            merchant_identifier,
            validation_evidence_identifier
        ),
    constraint uk_configuration_resolved_package_evidence
        unique (
            merchant_identifier,
            resolved_package_evidence_identifier
        ),
    constraint fk_configuration_validation_revision_affinity
        foreign key (
            merchant_identifier,
            configuration_revision_identifier,
            semantic_registry_release_identifier
        ) references merchant_configuration_revision (
            merchant_identifier,
            configuration_revision_identifier,
            semantic_registry_release_identifier
        ),
    constraint ck_configuration_validation_evidence_identity
        check (btrim(validation_evidence_identifier) <> ''),
    constraint ck_configuration_validation_revision_identity
        check (btrim(configuration_revision_identifier) <> ''),
    constraint ck_configuration_validation_semantic_release
        check (btrim(semantic_registry_release_identifier) <> ''),
    constraint ck_configuration_validation_package_identity
        check (btrim(resolved_package_evidence_identifier) <> ''),
    constraint ck_configuration_validation_success
        check (validation_outcome = 'SUCCEEDED'),
    constraint ck_configuration_validation_compiler
        check (btrim(compiler_identifier) <> '')
);
