-- MS-PROT-051 v1.4: independent, non-executable Classification Entry facts.

create table merchant_classification_entry_revision (
    revision_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    classification_identifier text not null,
    revision_number bigint not null,
    predecessor_revision_identifier text,
    operation_kind text not null,
    lifecycle text not null,
    value_schema_identifier text not null,
    classification_kind text not null,
    merchant_approved_label text not null,
    exposure_choice text not null,
    logical_request_identifier text not null unique,
    provenance_reference text not null,
    acting_principal_identifier text not null,
    controller_relationship_identifier text not null,
    committed_at timestamptz not null,

    constraint uq_merchant_classification_revision_number
        unique (merchant_identifier, classification_identifier, revision_number),
    constraint uq_merchant_classification_revision_identity
        unique (
            merchant_identifier,
            classification_identifier,
            revision_identifier
        ),
    constraint uq_merchant_classification_revision_kind
        unique (
            merchant_identifier,
            classification_identifier,
            revision_identifier,
            classification_kind
        ),
    constraint uq_merchant_classification_revision_scope
        unique (
            merchant_identifier,
            classification_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ),
    constraint fk_merchant_classification_predecessor_kind
        foreign key (
            merchant_identifier,
            classification_identifier,
            predecessor_revision_identifier,
            classification_kind
        ) references merchant_classification_entry_revision (
            merchant_identifier,
            classification_identifier,
            revision_identifier,
            classification_kind
        ),
    constraint fk_merchant_classification_controller
        foreign key (merchant_identifier, controller_relationship_identifier)
        references merchant_controller_relationship (
            merchant_identifier,
            controller_relationship_identifier
        ),
    constraint ck_merchant_classification_identifier
        check (btrim(classification_identifier) <> ''),
    constraint ck_merchant_classification_revision_positive
        check (revision_number > 0),
    constraint ck_merchant_classification_predecessor_shape
        check (
            (revision_number = 1 and predecessor_revision_identifier is null)
            or
            (revision_number > 1 and predecessor_revision_identifier is not null)
        ),
    constraint ck_merchant_classification_operation
        check (operation_kind in ('CREATE', 'UPDATE', 'RETIRE')),
    constraint ck_merchant_classification_operation_sequence
        check (
            (revision_number = 1 and operation_kind = 'CREATE')
            or
            (revision_number > 1 and operation_kind in ('UPDATE', 'RETIRE'))
        ),
    constraint ck_merchant_classification_lifecycle
        check (lifecycle in ('ACTIVE', 'RETIRED')),
    constraint ck_merchant_classification_operation_lifecycle
        check (
            (operation_kind in ('CREATE', 'UPDATE') and lifecycle = 'ACTIVE')
            or
            (operation_kind = 'RETIRE' and lifecycle = 'RETIRED')
        ),
    constraint ck_merchant_classification_schema
        check (
            value_schema_identifier = 'MS_MERCHANT_CLASSIFICATION_ENTRY_V1'
        ),
    constraint ck_merchant_classification_kind
        check (classification_kind in (
            'CATEGORY',
            'DISCOVERY_TAG',
            'CONTEXTUAL_DESCRIPTOR'
        )),
    constraint ck_merchant_classification_label
        check (btrim(merchant_approved_label) <> ''),
    constraint ck_merchant_classification_exposure
        check (exposure_choice in ('PRIVATE_INTERNAL', 'PUBLIC')),
    constraint ck_merchant_classification_request
        check (btrim(logical_request_identifier) <> ''),
    constraint ck_merchant_classification_provenance
        check (btrim(provenance_reference) <> ''),
    constraint ck_merchant_classification_actor
        check (btrim(acting_principal_identifier) <> '')
);

create table current_merchant_classification_entry (
    current_pointer_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    classification_identifier text not null,
    revision_identifier text not null unique,
    revision_number bigint not null,
    lifecycle text not null,

    constraint uq_current_merchant_classification_entry
        unique (merchant_identifier, classification_identifier),
    constraint fk_current_merchant_classification_revision
        foreign key (
            merchant_identifier,
            classification_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ) references merchant_classification_entry_revision (
            merchant_identifier,
            classification_identifier,
            revision_identifier,
            revision_number,
            lifecycle
        ),
    constraint ck_current_merchant_classification_identifier
        check (btrim(classification_identifier) <> ''),
    constraint ck_current_merchant_classification_revision
        check (revision_number > 0),
    constraint ck_current_merchant_classification_lifecycle
        check (lifecycle in ('ACTIVE', 'RETIRED'))
);

create index ix_merchant_classification_entry_history
    on merchant_classification_entry_revision (
        merchant_identifier,
        classification_identifier,
        revision_number desc
    );
