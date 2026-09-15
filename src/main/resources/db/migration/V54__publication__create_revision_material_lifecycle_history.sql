-- MS-PROT-045 v1.1 + MS-PROT-046 v1.1/v1.2 / IMP-07-P2B:
-- reconstructible Opportunity material revisions and ordered Publication lifecycle evidence.
-- Logical-operation retry idempotency, application orchestration, Exposure and Enquiry remain
-- outside this migration.

create table opportunity_publication_revision_material (
    merchant_identifier text not null,
    opportunity_identity text not null,
    revision_identity text not null,
    schema_identifier text not null,
    schema_version bigint not null,

    constraint pk_opportunity_publication_revision_material
        primary key (
            merchant_identifier,
            opportunity_identity,
            revision_identity
        ),
    constraint ck_opportunity_publication_material_merchant
        check (btrim(merchant_identifier) <> ''),
    constraint ck_opportunity_publication_material_opportunity
        check (btrim(opportunity_identity) <> ''),
    constraint ck_opportunity_publication_material_revision
        check (btrim(revision_identity) <> ''),
    constraint ck_opportunity_publication_material_schema
        check (btrim(schema_identifier) <> ''),
    constraint ck_opportunity_publication_material_schema_version
        check (schema_version > 0),
    constraint fk_opportunity_publication_material_revision
        foreign key (
            merchant_identifier,
            opportunity_identity,
            revision_identity
        )
        references opportunity_publication_revision_identity (
            merchant_identifier,
            opportunity_identity,
            revision_identity
        )
        deferrable initially deferred
);

create table opportunity_publication_revision_field_value (
    merchant_identifier text not null,
    opportunity_identity text not null,
    revision_identity text not null,
    field_identifier text not null,
    canonical_value text not null,

    constraint pk_opportunity_publication_revision_field_value
        primary key (
            merchant_identifier,
            opportunity_identity,
            revision_identity,
            field_identifier
        ),
    constraint ck_opportunity_publication_field_merchant
        check (btrim(merchant_identifier) <> ''),
    constraint ck_opportunity_publication_field_opportunity
        check (btrim(opportunity_identity) <> ''),
    constraint ck_opportunity_publication_field_revision
        check (btrim(revision_identity) <> ''),
    constraint ck_opportunity_publication_field_identifier
        check (btrim(field_identifier) <> ''),
    constraint fk_opportunity_publication_field_material
        foreign key (
            merchant_identifier,
            opportunity_identity,
            revision_identity
        )
        references opportunity_publication_revision_material (
            merchant_identifier,
            opportunity_identity,
            revision_identity
        )
        deferrable initially deferred
);

create table opportunity_publication_lifecycle_history (
    merchant_identifier text not null,
    opportunity_identity text not null,
    transition_sequence bigint not null,
    transition_kind text not null,
    previous_lifecycle text,
    lifecycle text not null,
    current_revision_identity text not null,
    published_revision_identity text,

    constraint pk_opportunity_publication_lifecycle_history
        primary key (
            merchant_identifier,
            opportunity_identity,
            transition_sequence
        ),
    constraint ck_opportunity_publication_history_merchant
        check (btrim(merchant_identifier) <> ''),
    constraint ck_opportunity_publication_history_opportunity
        check (btrim(opportunity_identity) <> ''),
    constraint ck_opportunity_publication_history_sequence
        check (transition_sequence > 0),
    constraint ck_opportunity_publication_history_kind
        check (transition_kind in ('ESTABLISH', 'PUBLISH', 'WITHDRAW', 'REPUBLISH')),
    constraint ck_opportunity_publication_history_previous_lifecycle
        check (
            previous_lifecycle is null
            or previous_lifecycle in ('DRAFT', 'PUBLISHED', 'WITHDRAWN')
        ),
    constraint ck_opportunity_publication_history_lifecycle
        check (lifecycle in ('DRAFT', 'PUBLISHED', 'WITHDRAWN')),
    constraint ck_opportunity_publication_history_current_revision
        check (btrim(current_revision_identity) <> ''),
    constraint ck_opportunity_publication_history_published_revision
        check (
            published_revision_identity is null
            or btrim(published_revision_identity) <> ''
        ),
    constraint ck_opportunity_publication_history_lifecycle_evidence
        check (
            (lifecycle = 'DRAFT' and published_revision_identity is null)
            or
            (lifecycle in ('PUBLISHED', 'WITHDRAWN')
                and published_revision_identity is not null)
        ),
    constraint ck_opportunity_publication_history_transition_shape
        check (
            (transition_kind = 'ESTABLISH' and previous_lifecycle is null)
            or
            (transition_kind = 'PUBLISH'
                and previous_lifecycle in ('DRAFT', 'PUBLISHED')
                and lifecycle = 'PUBLISHED'
                and published_revision_identity = current_revision_identity)
            or
            (transition_kind = 'WITHDRAW'
                and previous_lifecycle = 'PUBLISHED'
                and lifecycle = 'WITHDRAWN'
                and published_revision_identity is not null)
            or
            (transition_kind = 'REPUBLISH'
                and previous_lifecycle = 'WITHDRAWN'
                and lifecycle = 'PUBLISHED'
                and published_revision_identity = current_revision_identity)
        ),
    constraint fk_opportunity_publication_history_current_revision
        foreign key (
            merchant_identifier,
            opportunity_identity,
            current_revision_identity
        )
        references opportunity_publication_revision_identity (
            merchant_identifier,
            opportunity_identity,
            revision_identity
        )
        deferrable initially deferred,
    constraint fk_opportunity_publication_history_published_revision
        foreign key (
            merchant_identifier,
            opportunity_identity,
            published_revision_identity
        )
        references opportunity_publication_revision_identity (
            merchant_identifier,
            opportunity_identity,
            revision_identity
        )
        deferrable initially deferred
);
