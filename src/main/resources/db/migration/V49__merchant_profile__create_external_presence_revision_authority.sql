-- Composite MS-PROT-051 through v1.2: independent External Presence facts.

create table merchant_external_presence_link_revision (
    revision_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    presence_identifier text not null,
    revision_number bigint not null,
    predecessor_revision_identifier text,
    operation_kind text not null,
    lifecycle text not null,
    platform_kind text not null,
    public_url text not null,
    exposure_choice text not null,
    logical_request_identifier text not null unique,
    provenance_reference text not null,
    acting_principal_identifier text not null,
    controller_relationship_identifier text not null,
    committed_at timestamptz not null,

    constraint uq_merchant_external_presence_revision_number
        unique (merchant_identifier, presence_identifier, revision_number),
    constraint uq_merchant_external_presence_revision_identity
        unique (
            merchant_identifier,
            presence_identifier,
            revision_identifier
        ),
    constraint uq_merchant_external_presence_revision_scope
        unique (
            merchant_identifier,
            presence_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ),
    constraint fk_merchant_external_presence_predecessor
        foreign key (
            merchant_identifier,
            presence_identifier,
            predecessor_revision_identifier
        ) references merchant_external_presence_link_revision (
            merchant_identifier,
            presence_identifier,
            revision_identifier
        ),
    constraint fk_merchant_external_presence_controller
        foreign key (merchant_identifier, controller_relationship_identifier)
        references merchant_controller_relationship (
            merchant_identifier,
            controller_relationship_identifier
        ),
    constraint ck_merchant_external_presence_identifier
        check (btrim(presence_identifier) <> ''),
    constraint ck_merchant_external_presence_revision_positive
        check (revision_number > 0),
    constraint ck_merchant_external_presence_predecessor_shape
        check (
            (revision_number = 1 and predecessor_revision_identifier is null)
            or
            (revision_number > 1 and predecessor_revision_identifier is not null)
        ),
    constraint ck_merchant_external_presence_operation
        check (operation_kind in ('CREATE', 'UPDATE', 'RETIRE')),
    constraint ck_merchant_external_presence_operation_sequence
        check (
            (revision_number = 1 and operation_kind = 'CREATE')
            or
            (revision_number > 1 and operation_kind in ('UPDATE', 'RETIRE'))
        ),
    constraint ck_merchant_external_presence_lifecycle
        check (lifecycle in ('ACTIVE', 'RETIRED')),
    constraint ck_merchant_external_presence_operation_lifecycle
        check (
            (operation_kind in ('CREATE', 'UPDATE') and lifecycle = 'ACTIVE')
            or
            (operation_kind = 'RETIRE' and lifecycle = 'RETIRED')
        ),
    constraint ck_merchant_external_presence_platform
        check (btrim(platform_kind) <> ''),
    constraint ck_merchant_external_presence_url
        check (btrim(public_url) <> ''),
    constraint ck_merchant_external_presence_exposure
        check (exposure_choice in ('PRIVATE_INTERNAL', 'PUBLIC')),
    constraint ck_merchant_external_presence_request
        check (btrim(logical_request_identifier) <> ''),
    constraint ck_merchant_external_presence_provenance
        check (btrim(provenance_reference) <> ''),
    constraint ck_merchant_external_presence_actor
        check (btrim(acting_principal_identifier) <> '')
);

create table current_merchant_external_presence_link (
    current_pointer_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    presence_identifier text not null,
    revision_identifier text not null unique,
    revision_number bigint not null,
    lifecycle text not null,

    constraint uq_current_merchant_external_presence
        unique (merchant_identifier, presence_identifier),
    constraint fk_current_merchant_external_presence_revision
        foreign key (
            merchant_identifier,
            presence_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ) references merchant_external_presence_link_revision (
            merchant_identifier,
            presence_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ),
    constraint ck_current_merchant_external_presence_identifier
        check (btrim(presence_identifier) <> ''),
    constraint ck_current_merchant_external_presence_revision
        check (revision_number > 0),
    constraint ck_current_merchant_external_presence_lifecycle
        check (lifecycle in ('ACTIVE', 'RETIRED'))
);

create index ix_merchant_external_presence_history
    on merchant_external_presence_link_revision (
        merchant_identifier,
        presence_identifier,
        revision_number desc
    );

create index ix_merchant_external_presence_platform
    on merchant_external_presence_link_revision (
        merchant_identifier,
        platform_kind,
        lifecycle
    );
