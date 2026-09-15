-- MS-PROT-040 v1.3: immutable exact business-facing impact-review evidence.

alter table configuration_validation_evidence
    add constraint uk_configuration_validation_evidence_affinity
        unique (
            merchant_identifier,
            validation_evidence_identifier,
            configuration_revision_identifier,
            semantic_registry_release_identifier,
            resolved_package_evidence_identifier
        );

create table configuration_impact_review_evidence (
    merchant_identifier text not null,
    impact_review_evidence_identifier text not null,
    configuration_revision_identifier text not null,
    semantic_registry_release_identifier text not null,
    validation_evidence_identifier text not null,
    resolved_package_evidence_identifier text not null,
    impact_analysis_completed_at timestamptz not null,

    constraint pk_configuration_impact_review_evidence
        primary key (
            merchant_identifier,
            impact_review_evidence_identifier
        ),
    constraint fk_configuration_impact_review_validation_affinity
        foreign key (
            merchant_identifier,
            validation_evidence_identifier,
            configuration_revision_identifier,
            semantic_registry_release_identifier,
            resolved_package_evidence_identifier
        ) references configuration_validation_evidence (
            merchant_identifier,
            validation_evidence_identifier,
            configuration_revision_identifier,
            semantic_registry_release_identifier,
            resolved_package_evidence_identifier
        ),
    constraint ck_configuration_impact_review_identity
        check (btrim(impact_review_evidence_identifier) <> ''),
    constraint ck_configuration_impact_review_revision
        check (btrim(configuration_revision_identifier) <> ''),
    constraint ck_configuration_impact_review_release
        check (btrim(semantic_registry_release_identifier) <> ''),
    constraint ck_configuration_impact_review_validation
        check (btrim(validation_evidence_identifier) <> ''),
    constraint ck_configuration_impact_review_package
        check (btrim(resolved_package_evidence_identifier) <> '')
);

create table configuration_impact_review_effect (
    merchant_identifier text not null,
    impact_review_evidence_identifier text not null,
    effect_sequence integer not null,
    business_facing_effect text not null,

    constraint pk_configuration_impact_review_effect
        primary key (
            merchant_identifier,
            impact_review_evidence_identifier,
            effect_sequence
        ),
    constraint fk_configuration_impact_review_effect
        foreign key (
            merchant_identifier,
            impact_review_evidence_identifier
        ) references configuration_impact_review_evidence (
            merchant_identifier,
            impact_review_evidence_identifier
        ),
    constraint ck_configuration_impact_review_effect_sequence
        check (effect_sequence >= 0),
    constraint ck_configuration_impact_review_effect_content
        check (btrim(business_facing_effect) <> '')
);

create table configuration_impact_review_finding (
    merchant_identifier text not null,
    impact_review_evidence_identifier text not null,
    finding_sequence integer not null,
    impact_classification text not null,
    business_facing_finding text not null,

    constraint pk_configuration_impact_review_finding
        primary key (
            merchant_identifier,
            impact_review_evidence_identifier,
            finding_sequence
        ),
    constraint fk_configuration_impact_review_finding
        foreign key (
            merchant_identifier,
            impact_review_evidence_identifier
        ) references configuration_impact_review_evidence (
            merchant_identifier,
            impact_review_evidence_identifier
        ),
    constraint ck_configuration_impact_review_finding_sequence
        check (finding_sequence >= 0),
    constraint ck_configuration_impact_review_classification
        check (impact_classification in (
            'BLOCKING',
            'CONSEQUENTIAL',
            'INFORMATIONAL',
            'EXISTING_COMMITMENT_CONFLICT'
        )),
    constraint ck_configuration_impact_review_finding_content
        check (btrim(business_facing_finding) <> '')
);
