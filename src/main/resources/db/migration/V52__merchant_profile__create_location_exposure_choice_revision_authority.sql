-- MS-PROT-051 v1.5: independent Merchant Location public Exposure choice.

create table merchant_location_exposure_choice_revision (
    revision_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    location_identifier text not null,
    revision_number bigint not null,
    predecessor_revision_identifier text
        references merchant_location_exposure_choice_revision (revision_identifier),
    exposure_choice text not null,
    logical_request_identifier text not null unique,
    provenance_reference text not null,
    acting_principal_identifier text not null,
    controller_relationship_identifier text not null,
    committed_at timestamptz not null,

    constraint uq_merchant_location_exposure_choice_revision_number
        unique (merchant_identifier, location_identifier, revision_number),
    constraint uq_merchant_location_exposure_choice_revision_scope
        unique (
            merchant_identifier,
            location_identifier,
            revision_identifier,
            revision_number
        ),
    constraint fk_merchant_location_exposure_choice_controller
        foreign key (merchant_identifier, controller_relationship_identifier)
        references merchant_controller_relationship (
            merchant_identifier,
            controller_relationship_identifier
        ),
    constraint ck_merchant_location_exposure_choice_location
        check (btrim(location_identifier) <> ''),
    constraint ck_merchant_location_exposure_choice_revision_positive
        check (revision_number > 0),
    constraint ck_merchant_location_exposure_choice_predecessor_shape
        check (
            (revision_number = 1 and predecessor_revision_identifier is null)
            or
            (revision_number > 1 and predecessor_revision_identifier is not null)
        ),
    constraint ck_merchant_location_exposure_choice_value
        check (exposure_choice in ('PRIVATE_INTERNAL', 'PUBLIC')),
    constraint ck_merchant_location_exposure_choice_request
        check (btrim(logical_request_identifier) <> ''),
    constraint ck_merchant_location_exposure_choice_provenance
        check (btrim(provenance_reference) <> ''),
    constraint ck_merchant_location_exposure_choice_actor
        check (btrim(acting_principal_identifier) <> '')
);

create table current_merchant_location_exposure_choice (
    current_pointer_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    location_identifier text not null,
    revision_identifier text not null unique,
    revision_number bigint not null,

    constraint uq_current_merchant_location_exposure_choice
        unique (merchant_identifier, location_identifier),
    constraint fk_current_merchant_location_exposure_choice_revision
        foreign key (
            merchant_identifier,
            location_identifier,
            revision_identifier,
            revision_number
        ) references merchant_location_exposure_choice_revision (
            merchant_identifier,
            location_identifier,
            revision_identifier,
            revision_number
        ),
    constraint ck_current_merchant_location_exposure_choice_location
        check (btrim(location_identifier) <> ''),
    constraint ck_current_merchant_location_exposure_choice_revision_positive
        check (revision_number > 0)
);

create index ix_merchant_location_exposure_choice_history
    on merchant_location_exposure_choice_revision (
        merchant_identifier,
        location_identifier,
        revision_number desc
    );
