alter table configuration_validation_evidence
    add column reinstatement_basis_activation_request_identifier text;

alter table configuration_impact_review_evidence
    add column reinstatement_basis_activation_request_identifier text;

alter table configuration_revision_approval
    add column reinstatement_basis_activation_request_identifier text;


alter table configuration_validation_evidence
    add constraint ck_configuration_validation_reinstatement_basis_nonblank
    check (
        reinstatement_basis_activation_request_identifier is null
        or btrim(reinstatement_basis_activation_request_identifier) <> ''
    );

alter table configuration_impact_review_evidence
    add constraint ck_configuration_impact_reinstatement_basis_nonblank
    check (
        reinstatement_basis_activation_request_identifier is null
        or btrim(reinstatement_basis_activation_request_identifier) <> ''
    );

alter table configuration_revision_approval
    add constraint ck_configuration_approval_reinstatement_basis_nonblank
    check (
        reinstatement_basis_activation_request_identifier is null
        or btrim(reinstatement_basis_activation_request_identifier) <> ''
    );


alter table configuration_activation
    add constraint uq_configuration_activation_merchant_request
    unique (
        merchant_identifier,
        activation_request_identifier
    );


alter table configuration_validation_evidence
    add constraint fk_configuration_validation_reinstatement_basis
    foreign key (
        merchant_identifier,
        reinstatement_basis_activation_request_identifier
    )
    references configuration_activation (
        merchant_identifier,
        activation_request_identifier
    );


alter table configuration_impact_review_evidence
    add constraint fk_configuration_impact_reinstatement_basis
    foreign key (
        merchant_identifier,
        reinstatement_basis_activation_request_identifier
    )
    references configuration_activation (
        merchant_identifier,
        activation_request_identifier
    );


alter table configuration_revision_approval
    add constraint fk_configuration_approval_reinstatement_basis
    foreign key (
        merchant_identifier,
        reinstatement_basis_activation_request_identifier
    )
    references configuration_activation (
        merchant_identifier,
        activation_request_identifier
    );


create index ix_configuration_validation_reinstatement_basis
    on configuration_validation_evidence (
        merchant_identifier,
        reinstatement_basis_activation_request_identifier
    )
    where reinstatement_basis_activation_request_identifier is not null;


create index ix_configuration_impact_reinstatement_basis
    on configuration_impact_review_evidence (
        merchant_identifier,
        reinstatement_basis_activation_request_identifier
    )
    where reinstatement_basis_activation_request_identifier is not null;


create index ix_configuration_approval_reinstatement_applicability
    on configuration_revision_approval (
        merchant_identifier,
        configuration_revision_identifier,
        reinstatement_basis_activation_request_identifier
    )
    where reinstatement_basis_activation_request_identifier is not null;