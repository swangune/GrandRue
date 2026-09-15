-- MS-PROT-040 v1.4: immutable normalized serving-generation evidence.

create table serving_deployment_admission_snapshot (
    generation_identifier text primary key,
    cohort_identifier text not null,
    evidence_recorded_at timestamptz not null,
    constraint ck_serving_snapshot_generation
        check (btrim(generation_identifier) <> ''),
    constraint ck_serving_snapshot_ordinary_cohort
        check (cohort_identifier = 'ORDINARY')
);

create table serving_deployment_materialised_release (
    generation_identifier text not null,
    semantic_registry_release_identifier text not null,
    packaged_bundle_content_digest text not null,
    constraint pk_serving_materialised_release
        primary key (
            generation_identifier,
            semantic_registry_release_identifier
        ),
    constraint fk_serving_materialised_release_snapshot
        foreign key (generation_identifier)
        references serving_deployment_admission_snapshot (
            generation_identifier
        ),
    constraint ck_serving_materialised_release
        check (btrim(semantic_registry_release_identifier) <> ''),
    constraint ck_serving_materialised_digest
        check (btrim(packaged_bundle_content_digest) <> '')
);

create table serving_deployment_executable_support_manifest (
    generation_identifier text not null,
    implementation_path_identifier text not null,
    constraint pk_serving_executable_support_manifest
        primary key (
            generation_identifier,
            implementation_path_identifier
        ),
    constraint fk_serving_executable_manifest_snapshot
        foreign key (generation_identifier)
        references serving_deployment_admission_snapshot (
            generation_identifier
        ),
    constraint ck_serving_implementation_path
        check (btrim(implementation_path_identifier) <> '')
);

create table serving_deployment_executable_support_contract (
    generation_identifier text not null,
    implementation_path_identifier text not null,
    semantic_registry_release_identifier text not null,
    contract_identifier text not null,
    constraint pk_serving_executable_support_contract
        primary key (
            generation_identifier,
            implementation_path_identifier,
            semantic_registry_release_identifier,
            contract_identifier
        ),
    constraint fk_serving_executable_contract_manifest
        foreign key (
            generation_identifier,
            implementation_path_identifier
        ) references serving_deployment_executable_support_manifest (
            generation_identifier,
            implementation_path_identifier
        ),
    constraint ck_serving_support_release
        check (btrim(semantic_registry_release_identifier) <> ''),
    constraint ck_serving_support_contract
        check (btrim(contract_identifier) <> '')
);
