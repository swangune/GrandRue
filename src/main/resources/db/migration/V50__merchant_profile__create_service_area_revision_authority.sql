-- Composite MS-PROT-051 through v1.3: immutable Service Area authority.

create table merchant_service_area_revision (
    revision_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    service_area_identifier text not null,
    revision_number bigint not null,
    predecessor_revision_identifier text,
    operation_kind text not null,
    lifecycle text not null,
    geography_schema_identifier text not null,
    geography_kind text not null,
    country_code text,
    area_name text,
    merchant_location_identifier text,
    merchant_location_revision_identifier text,
    radius_metres bigint,
    public_description text not null,
    exposure_choice text not null,
    logical_request_identifier text not null unique,
    provenance_reference text not null,
    acting_principal_identifier text not null,
    controller_relationship_identifier text not null,
    committed_at timestamptz not null,

    constraint uq_merchant_service_area_revision_number
        unique (merchant_identifier, service_area_identifier, revision_number),
    constraint uq_merchant_service_area_revision_identity
        unique (
            merchant_identifier,
            service_area_identifier,
            revision_identifier
        ),
    constraint uq_merchant_service_area_revision_scope
        unique (
            merchant_identifier,
            service_area_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ),
    constraint fk_merchant_service_area_predecessor
        foreign key (
            merchant_identifier,
            service_area_identifier,
            predecessor_revision_identifier
        ) references merchant_service_area_revision (
            merchant_identifier,
            service_area_identifier,
            revision_identifier
        ),
    constraint fk_merchant_service_area_controller
        foreign key (merchant_identifier, controller_relationship_identifier)
        references merchant_controller_relationship (
            merchant_identifier,
            controller_relationship_identifier
        ),
    constraint fk_merchant_service_area_location_revision
        foreign key (
            merchant_identifier,
            merchant_location_identifier,
            merchant_location_revision_identifier
        ) references merchant_location_revision (
            merchant_identifier,
            location_identifier,
            revision_identifier
        ),
    constraint ck_merchant_service_area_identifier
        check (btrim(service_area_identifier) <> ''),
    constraint ck_merchant_service_area_revision_positive
        check (revision_number > 0),
    constraint ck_merchant_service_area_predecessor_shape
        check (
            (revision_number = 1 and predecessor_revision_identifier is null)
            or
            (revision_number > 1 and predecessor_revision_identifier is not null)
        ),
    constraint ck_merchant_service_area_operation
        check (operation_kind in ('CREATE', 'UPDATE', 'RETIRE')),
    constraint ck_merchant_service_area_operation_sequence
        check (
            (revision_number = 1 and operation_kind = 'CREATE')
            or
            (revision_number > 1 and operation_kind in ('UPDATE', 'RETIRE'))
        ),
    constraint ck_merchant_service_area_lifecycle
        check (lifecycle in ('ACTIVE', 'RETIRED')),
    constraint ck_merchant_service_area_operation_lifecycle
        check (
            (operation_kind in ('CREATE', 'UPDATE') and lifecycle = 'ACTIVE')
            or
            (operation_kind = 'RETIRE' and lifecycle = 'RETIRED')
        ),
    constraint ck_merchant_service_area_schema
        check (geography_schema_identifier = 'MS_SERVICE_AREA_GEOGRAPHY_V1'),
    constraint ck_merchant_service_area_geography_kind
        check (geography_kind in (
            'NAMED_AREA',
            'MERCHANT_LOCATION_RADIUS',
            'COUNTRY_WIDE',
            'REMOTE_COUNTRIES'
        )),
    constraint ck_merchant_service_area_country
        check (country_code is null or country_code ~ '^[A-Z]{2}$'),
    constraint ck_merchant_service_area_geography_shape
        check (
            (
                geography_kind = 'NAMED_AREA'
                and country_code is not null
                and area_name is not null
                and btrim(area_name) <> ''
                and merchant_location_identifier is null
                and merchant_location_revision_identifier is null
                and radius_metres is null
            )
            or
            (
                geography_kind = 'MERCHANT_LOCATION_RADIUS'
                and country_code is null
                and area_name is null
                and merchant_location_identifier is not null
                and btrim(merchant_location_identifier) <> ''
                and merchant_location_revision_identifier is not null
                and btrim(merchant_location_revision_identifier) <> ''
                and radius_metres is not null
                and radius_metres > 0
            )
            or
            (
                geography_kind = 'COUNTRY_WIDE'
                and country_code is not null
                and area_name is null
                and merchant_location_identifier is null
                and merchant_location_revision_identifier is null
                and radius_metres is null
            )
            or
            (
                geography_kind = 'REMOTE_COUNTRIES'
                and country_code is null
                and area_name is null
                and merchant_location_identifier is null
                and merchant_location_revision_identifier is null
                and radius_metres is null
            )
        ),
    constraint ck_merchant_service_area_description
        check (btrim(public_description) <> ''),
    constraint ck_merchant_service_area_exposure
        check (exposure_choice in ('PRIVATE_INTERNAL', 'PUBLIC')),
    constraint ck_merchant_service_area_request
        check (btrim(logical_request_identifier) <> ''),
    constraint ck_merchant_service_area_provenance
        check (btrim(provenance_reference) <> ''),
    constraint ck_merchant_service_area_actor
        check (btrim(acting_principal_identifier) <> '')
);

create table merchant_service_area_remote_country (
    revision_identifier text not null
        references merchant_service_area_revision (revision_identifier),
    country_ordinal integer not null,
    country_code text not null,
    primary key (revision_identifier, country_ordinal),
    constraint uq_merchant_service_area_remote_country
        unique (revision_identifier, country_code),
    constraint ck_merchant_service_area_remote_country_ordinal
        check (country_ordinal >= 0),
    constraint ck_merchant_service_area_remote_country_code
        check (country_code ~ '^[A-Z]{2}$')
);

create table current_merchant_service_area (
    current_pointer_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    service_area_identifier text not null,
    revision_identifier text not null unique,
    revision_number bigint not null,
    lifecycle text not null,

    constraint uq_current_merchant_service_area
        unique (merchant_identifier, service_area_identifier),
    constraint fk_current_merchant_service_area_revision
        foreign key (
            merchant_identifier,
            service_area_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ) references merchant_service_area_revision (
            merchant_identifier,
            service_area_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ),
    constraint ck_current_merchant_service_area_identifier
        check (btrim(service_area_identifier) <> ''),
    constraint ck_current_merchant_service_area_revision
        check (revision_number > 0),
    constraint ck_current_merchant_service_area_lifecycle
        check (lifecycle in ('ACTIVE', 'RETIRED'))
);

create index ix_merchant_service_area_history
    on merchant_service_area_revision (
        merchant_identifier,
        service_area_identifier,
        revision_number desc
    );

create index ix_merchant_service_area_location_affinity
    on merchant_service_area_revision (
        merchant_identifier,
        merchant_location_identifier,
        merchant_location_revision_identifier
    ) where geography_kind = 'MERCHANT_LOCATION_RADIUS';
