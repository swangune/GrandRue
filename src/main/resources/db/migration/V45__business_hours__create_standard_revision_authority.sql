-- MS-PROT-050 v1.4: immutable scope-affined stable weekly Business Hours.

create table standard_business_hours_revision (
    revision_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    scope_kind text not null,
    merchant_location_identifier text,
    revision_number bigint not null,
    predecessor_revision_identifier text
        references standard_business_hours_revision (revision_identifier),
    disposition text not null,
    time_zone_identifier text,
    logical_request_identifier text not null unique,
    acting_principal_identifier text not null,
    controller_relationship_identifier text not null,
    committed_at timestamptz not null,

    constraint uq_standard_business_hours_scope_revision
        unique nulls not distinct (
            merchant_identifier,
            scope_kind,
            merchant_location_identifier,
            revision_number
        ),
    constraint fk_standard_business_hours_controller
        foreign key (merchant_identifier, controller_relationship_identifier)
        references merchant_controller_relationship (
            merchant_identifier,
            controller_relationship_identifier
        ),
    constraint ck_standard_business_hours_scope_kind
        check (scope_kind in ('MERCHANT', 'MERCHANT_LOCATION')),
    constraint ck_standard_business_hours_scope_shape
        check (
            (scope_kind = 'MERCHANT' and merchant_location_identifier is null)
            or
            (scope_kind = 'MERCHANT_LOCATION'
                and btrim(merchant_location_identifier) <> '')
        ),
    constraint ck_standard_business_hours_revision_positive
        check (revision_number > 0),
    constraint ck_standard_business_hours_predecessor_shape
        check (
            (revision_number = 1 and predecessor_revision_identifier is null)
            or
            (revision_number > 1 and predecessor_revision_identifier is not null)
        ),
    constraint ck_standard_business_hours_disposition
        check (disposition in ('CONFIGURED', 'WITHDRAWN')),
    constraint ck_standard_business_hours_value_shape
        check (
            (disposition = 'CONFIGURED' and btrim(time_zone_identifier) <> '')
            or
            (disposition = 'WITHDRAWN' and time_zone_identifier is null)
        ),
    constraint ck_standard_business_hours_request
        check (btrim(logical_request_identifier) <> ''),
    constraint ck_standard_business_hours_actor
        check (btrim(acting_principal_identifier) <> '')
);

create table standard_business_hours_interval (
    revision_identifier text not null
        references standard_business_hours_revision (revision_identifier),
    interval_sequence integer not null,
    start_day smallint not null,
    start_local_time time not null,
    end_local_time time not null,

    primary key (revision_identifier, interval_sequence),
    constraint ck_standard_business_hours_interval_sequence
        check (interval_sequence >= 0),
    constraint ck_standard_business_hours_start_day
        check (start_day between 1 and 7),
    constraint ck_standard_business_hours_nonzero_interval
        check (start_local_time <> end_local_time)
);

create table current_standard_business_hours (
    current_pointer_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    scope_kind text not null,
    merchant_location_identifier text,
    revision_identifier text not null unique
        references standard_business_hours_revision (revision_identifier),
    revision_number bigint not null,

    constraint uq_current_standard_business_hours_scope
        unique nulls not distinct (
            merchant_identifier,
            scope_kind,
            merchant_location_identifier
        ),
    constraint ck_current_standard_business_hours_scope_kind
        check (scope_kind in ('MERCHANT', 'MERCHANT_LOCATION')),
    constraint ck_current_standard_business_hours_scope_shape
        check (
            (scope_kind = 'MERCHANT' and merchant_location_identifier is null)
            or
            (scope_kind = 'MERCHANT_LOCATION'
                and btrim(merchant_location_identifier) <> '')
        ),
    constraint ck_current_standard_business_hours_revision_positive
        check (revision_number > 0)
);

create index ix_standard_business_hours_scope_history
    on standard_business_hours_revision (
        merchant_identifier,
        scope_kind,
        merchant_location_identifier,
        revision_number desc
    );
