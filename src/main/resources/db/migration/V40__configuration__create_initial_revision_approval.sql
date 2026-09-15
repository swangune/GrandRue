-- MS-PROT-040 v1.3: append-only exact current-Controller approval facts.
alter table configuration_impact_review_evidence
    add constraint uk_configuration_impact_review_evidence_affinity
        unique (
            merchant_identifier,
            impact_review_evidence_identifier,
            configuration_revision_identifier,
            semantic_registry_release_identifier,
            validation_evidence_identifier,
            resolved_package_evidence_identifier
        );

create table configuration_revision_approval (
    logical_approval_request_identifier text primary key,
    merchant_identifier text not null,
    configuration_revision_identifier text not null,
    semantic_registry_release_identifier text not null,
    validation_evidence_identifier text not null,
    impact_review_evidence_identifier text not null,
    resolved_package_evidence_identifier text not null,
    approving_principal_identifier text not null,
    controller_relationship_identifier text not null,
    approved_at timestamptz not null,
    constraint fk_configuration_approval_revision foreign key (merchant_identifier, configuration_revision_identifier, semantic_registry_release_identifier)
        references merchant_configuration_revision (merchant_identifier, configuration_revision_identifier, semantic_registry_release_identifier),
    constraint fk_configuration_approval_validation foreign key (merchant_identifier, validation_evidence_identifier, configuration_revision_identifier, semantic_registry_release_identifier, resolved_package_evidence_identifier)
        references configuration_validation_evidence (merchant_identifier, validation_evidence_identifier, configuration_revision_identifier, semantic_registry_release_identifier, resolved_package_evidence_identifier),
    constraint fk_configuration_approval_impact foreign key (
        merchant_identifier,
        impact_review_evidence_identifier,
        configuration_revision_identifier,
        semantic_registry_release_identifier,
        validation_evidence_identifier,
        resolved_package_evidence_identifier
    ) references configuration_impact_review_evidence (
        merchant_identifier,
        impact_review_evidence_identifier,
        configuration_revision_identifier,
        semantic_registry_release_identifier,
        validation_evidence_identifier,
        resolved_package_evidence_identifier
    ),
    constraint fk_configuration_approval_controller foreign key (merchant_identifier, controller_relationship_identifier)
        references merchant_controller_relationship (merchant_identifier, controller_relationship_identifier),
    constraint uk_configuration_approval_exact_fact unique (merchant_identifier, configuration_revision_identifier, validation_evidence_identifier, impact_review_evidence_identifier, approving_principal_identifier),
    constraint ck_configuration_approval_identities check (
        btrim(logical_approval_request_identifier) <> '' and btrim(approving_principal_identifier) <> ''
    )
);
