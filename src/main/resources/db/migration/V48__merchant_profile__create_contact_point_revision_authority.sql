-- Composite MS-PROT-051 through v1.2: independent Merchant Contact Point facts.

create table merchant_contact_point_revision (
    revision_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    contact_point_identifier text not null,
    scope_kind text not null,
    merchant_location_identifier text,
    revision_number bigint not null,
    predecessor_revision_identifier text
        references merchant_contact_point_revision (revision_identifier),
    operation_kind text not null,
    lifecycle text not null,
    contact_kind text not null,
    contact_value text not null,
    exposure_choice text not null,
    contact_label text,
    merchant_location_revision_identifier text,
    logical_request_identifier text not null unique,
    provenance_reference text not null,
    acting_principal_identifier text not null,
    controller_relationship_identifier text not null,
    committed_at timestamptz not null,

    constraint uq_merchant_contact_point_revision_number
        unique (merchant_identifier, contact_point_identifier, revision_number),
    constraint uq_merchant_contact_point_revision_scope
        unique (
            merchant_identifier,
            contact_point_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ),
    constraint fk_merchant_contact_point_controller
        foreign key (merchant_identifier, controller_relationship_identifier)
        references merchant_controller_relationship (
            merchant_identifier,
            controller_relationship_identifier
        ),
    constraint fk_merchant_contact_point_location_revision
        foreign key (
            merchant_identifier,
            merchant_location_identifier,
            merchant_location_revision_identifier
        ) references merchant_location_revision (
            merchant_identifier,
            location_identifier,
            revision_identifier
        ),
    constraint ck_merchant_contact_point_identifier
        check (btrim(contact_point_identifier) <> ''),
    constraint ck_merchant_contact_point_scope
        check (scope_kind in ('MERCHANT', 'MERCHANT_LOCATION')),
    constraint ck_merchant_contact_point_scope_shape
        check (
            (
                scope_kind = 'MERCHANT'
                and merchant_location_identifier is null
                and merchant_location_revision_identifier is null
            )
            or
            (
                scope_kind = 'MERCHANT_LOCATION'
                and btrim(merchant_location_identifier) <> ''
                and btrim(merchant_location_revision_identifier) <> ''
            )
        ),
    constraint ck_merchant_contact_point_revision_positive
        check (revision_number > 0),
    constraint ck_merchant_contact_point_predecessor_shape
        check (
            (revision_number = 1 and predecessor_revision_identifier is null)
            or
            (revision_number > 1 and predecessor_revision_identifier is not null)
        ),
    constraint ck_merchant_contact_point_operation
        check (operation_kind in ('CREATE', 'UPDATE', 'RETIRE')),
    constraint ck_merchant_contact_point_lifecycle
        check (lifecycle in ('ACTIVE', 'RETIRED')),
    constraint ck_merchant_contact_point_operation_lifecycle
        check (
            (operation_kind in ('CREATE', 'UPDATE') and lifecycle = 'ACTIVE')
            or
            (operation_kind = 'RETIRE' and lifecycle = 'RETIRED')
        ),
    constraint ck_merchant_contact_point_kind
        check (contact_kind in ('TELEPHONE', 'EMAIL', 'MOBILE', 'WEB_LINK')),
    constraint ck_merchant_contact_point_value
        check (btrim(contact_value) <> ''),
    constraint ck_merchant_contact_point_exposure
        check (exposure_choice in ('PRIVATE_INTERNAL', 'PUBLIC')),
    constraint ck_merchant_contact_point_label
        check (contact_label is null or btrim(contact_label) <> ''),
    constraint ck_merchant_contact_point_request
        check (btrim(logical_request_identifier) <> ''),
    constraint ck_merchant_contact_point_provenance
        check (btrim(provenance_reference) <> ''),
    constraint ck_merchant_contact_point_actor
        check (btrim(acting_principal_identifier) <> '')
);

create table current_merchant_contact_point (
    current_pointer_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    contact_point_identifier text not null,
    revision_identifier text not null unique,
    revision_number bigint not null,
    lifecycle text not null,

    constraint uq_current_merchant_contact_point
        unique (merchant_identifier, contact_point_identifier),
    constraint fk_current_merchant_contact_point_revision
        foreign key (
            merchant_identifier,
            contact_point_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ) references merchant_contact_point_revision (
            merchant_identifier,
            contact_point_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ),
    constraint ck_current_merchant_contact_point_identifier
        check (btrim(contact_point_identifier) <> ''),
    constraint ck_current_merchant_contact_point_revision
        check (revision_number > 0),
    constraint ck_current_merchant_contact_point_lifecycle
        check (lifecycle in ('ACTIVE', 'RETIRED'))
);

create index ix_merchant_contact_point_scope_history
    on merchant_contact_point_revision (
        merchant_identifier,
        contact_point_identifier,
        revision_number desc
    );

create index ix_merchant_contact_point_location_scope
    on merchant_contact_point_revision (
        merchant_identifier,
        merchant_location_identifier,
        lifecycle
    )
    where scope_kind = 'MERCHANT_LOCATION';
