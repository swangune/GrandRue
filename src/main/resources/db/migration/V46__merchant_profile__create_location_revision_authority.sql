-- Composite MS-PROT-051 through v1.2: immutable Merchant Location authority.

create table merchant_location_revision (
    revision_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    location_identifier text not null,
    revision_number bigint not null,
    predecessor_revision_identifier text
        references merchant_location_revision (revision_identifier),
    operation_kind text not null,
    lifecycle text not null,
    public_label text,

    address_schema_identifier text not null,
    normalization_profile_identifier text not null,
    country_registry_identifier text not null,
    original_country_code text not null,
    normalized_country_code text not null,
    original_dependent_locality text,
    normalized_dependent_locality text,
    original_locality text,
    normalized_locality text,
    original_administrative_area text,
    normalized_administrative_area text,
    original_postal_code text,
    normalized_postal_code text,
    original_sorting_code text,
    normalized_sorting_code text,

    latitude numeric,
    longitude numeric,
    coordinate_source_kind text,
    coordinate_source_reference text,
    coordinates_accepted_by_actor_identifier text,
    coordinates_accepted_at timestamptz,

    logical_request_identifier text not null unique,
    provenance_reference text not null,
    acting_principal_identifier text not null,
    controller_relationship_identifier text not null,
    committed_at timestamptz not null,

    constraint uq_merchant_location_revision_number
        unique (merchant_identifier, location_identifier, revision_number),
    constraint uq_merchant_location_revision_scope
        unique (
            merchant_identifier,
            location_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ),
    constraint fk_merchant_location_controller
        foreign key (merchant_identifier, controller_relationship_identifier)
        references merchant_controller_relationship (
            merchant_identifier,
            controller_relationship_identifier
        ),
    constraint ck_merchant_location_identifier
        check (btrim(location_identifier) <> ''),
    constraint ck_merchant_location_revision_positive
        check (revision_number > 0),
    constraint ck_merchant_location_predecessor_shape
        check (
            (revision_number = 1 and predecessor_revision_identifier is null)
            or
            (revision_number > 1 and predecessor_revision_identifier is not null)
        ),
    constraint ck_merchant_location_operation
        check (operation_kind in ('CREATE', 'CORRECT', 'RETIRE')),
    constraint ck_merchant_location_lifecycle
        check (lifecycle in ('ACTIVE', 'RETIRED')),
    constraint ck_merchant_location_operation_lifecycle
        check (
            (operation_kind in ('CREATE', 'CORRECT') and lifecycle = 'ACTIVE')
            or
            (operation_kind = 'RETIRE' and lifecycle = 'RETIRED')
        ),
    constraint ck_merchant_location_public_label
        check (public_label is null or btrim(public_label) <> ''),
    constraint ck_merchant_location_address_schema
        check (address_schema_identifier = 'POSTAL_ADDRESS_V1'),
    constraint ck_merchant_location_normalization
        check (
            normalization_profile_identifier
                = 'MS_POSTAL_ADDRESS_NORMALIZATION_V1'
        ),
    constraint ck_merchant_location_country_registry
        check (
            country_registry_identifier
                = 'ISO_3166_1_ALPHA_2_2026_08_30'
        ),
    constraint ck_merchant_location_normalized_country
        check (normalized_country_code ~ '^[A-Z]{2}$'),
    constraint ck_merchant_location_request
        check (btrim(logical_request_identifier) <> ''),
    constraint ck_merchant_location_provenance
        check (btrim(provenance_reference) <> ''),
    constraint ck_merchant_location_actor
        check (btrim(acting_principal_identifier) <> ''),
    constraint ck_merchant_location_coordinate_shape
        check (
            (
                latitude is null
                and longitude is null
                and coordinate_source_kind is null
                and coordinate_source_reference is null
                and coordinates_accepted_by_actor_identifier is null
                and coordinates_accepted_at is null
            )
            or
            (
                latitude between -90 and 90
                and longitude between -180 and 180
                and btrim(coordinate_source_kind) <> ''
                and btrim(coordinates_accepted_by_actor_identifier) <> ''
                and coordinates_accepted_at is not null
            )
        )
);

create table merchant_location_original_address_line (
    revision_identifier text not null
        references merchant_location_revision (revision_identifier),
    line_sequence integer not null,
    line_value text not null,
    primary key (revision_identifier, line_sequence),
    constraint ck_merchant_location_original_line_sequence
        check (line_sequence >= 0),
    constraint ck_merchant_location_original_line_value
        check (btrim(line_value) <> '')
);

create table merchant_location_normalized_address_line (
    revision_identifier text not null
        references merchant_location_revision (revision_identifier),
    line_sequence integer not null,
    line_value text not null,
    primary key (revision_identifier, line_sequence),
    constraint ck_merchant_location_normalized_line_sequence
        check (line_sequence >= 0),
    constraint ck_merchant_location_normalized_line_value
        check (btrim(line_value) <> '')
);

create table current_merchant_location (
    current_pointer_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    location_identifier text not null,
    revision_identifier text not null unique,
    revision_number bigint not null,
    lifecycle text not null,

    constraint uq_current_merchant_location
        unique (merchant_identifier, location_identifier),
    constraint fk_current_merchant_location_revision
        foreign key (
            merchant_identifier,
            location_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ) references merchant_location_revision (
            merchant_identifier,
            location_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ),
    constraint ck_current_merchant_location_identifier
        check (btrim(location_identifier) <> ''),
    constraint ck_current_merchant_location_revision
        check (revision_number > 0),
    constraint ck_current_merchant_location_lifecycle
        check (lifecycle in ('ACTIVE', 'RETIRED'))
);

create index ix_merchant_location_scope_history
    on merchant_location_revision (
        merchant_identifier,
        location_identifier,
        revision_number desc
    );
