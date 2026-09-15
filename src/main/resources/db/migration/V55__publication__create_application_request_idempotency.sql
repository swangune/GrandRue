-- MS-PROT-046 v1.2/v1.3 + MS-PROT-059 / IMP-07-P3:
-- durable logical Publication application request/result reconciliation.
-- Transport idempotency headers and public API representation remain outside this migration.

create table opportunity_publication_application_request (
    application_request_identity text not null,
    operation_kind text not null,
    merchant_identifier text not null,
    opportunity_identity text not null,
    expected_revision_identity text,
    material_revision_identity text,
    result_current_revision_identity text not null,
    result_lifecycle text not null,
    result_published_revision_identity text,

    constraint pk_opportunity_publication_application_request
        primary key (application_request_identity),
    constraint ck_opportunity_publication_application_request_identity
        check (btrim(application_request_identity) <> ''),
    constraint ck_opportunity_publication_application_operation
        check (operation_kind in (
            'ESTABLISH_DRAFT',
            'REVISE',
            'PUBLISH',
            'WITHDRAW',
            'REPUBLISH'
        )),
    constraint ck_opportunity_publication_application_merchant
        check (btrim(merchant_identifier) <> ''),
    constraint ck_opportunity_publication_application_opportunity
        check (btrim(opportunity_identity) <> ''),
    constraint ck_opportunity_publication_application_expected_revision
        check (
            expected_revision_identity is null
            or btrim(expected_revision_identity) <> ''
        ),
    constraint ck_opportunity_publication_application_material_revision
        check (
            material_revision_identity is null
            or btrim(material_revision_identity) <> ''
        ),
    constraint ck_opportunity_publication_application_result_revision
        check (btrim(result_current_revision_identity) <> ''),
    constraint ck_opportunity_publication_application_result_lifecycle
        check (result_lifecycle in ('DRAFT', 'PUBLISHED', 'WITHDRAWN')),
    constraint ck_opportunity_publication_application_result_published_revision
        check (
            result_published_revision_identity is null
            or btrim(result_published_revision_identity) <> ''
        ),
    constraint ck_opportunity_publication_application_result_evidence
        check (
            (result_lifecycle = 'DRAFT' and result_published_revision_identity is null)
            or
            (result_lifecycle in ('PUBLISHED', 'WITHDRAWN')
                and result_published_revision_identity is not null)
        ),
    constraint ck_opportunity_publication_application_shape
        check (
            (operation_kind = 'ESTABLISH_DRAFT'
                and expected_revision_identity is null
                and material_revision_identity is not null)
            or
            (operation_kind = 'REVISE'
                and expected_revision_identity is not null
                and material_revision_identity is not null)
            or
            (operation_kind in ('PUBLISH', 'WITHDRAW', 'REPUBLISH')
                and expected_revision_identity is not null
                and material_revision_identity is null)
        ),
    constraint fk_opportunity_publication_application_result_current
        foreign key (
            merchant_identifier,
            opportunity_identity,
            result_current_revision_identity
        )
        references opportunity_publication_revision_identity (
            merchant_identifier,
            opportunity_identity,
            revision_identity
        ),
    constraint fk_opportunity_publication_application_result_published
        foreign key (
            merchant_identifier,
            opportunity_identity,
            result_published_revision_identity
        )
        references opportunity_publication_revision_identity (
            merchant_identifier,
            opportunity_identity,
            revision_identity
        )
);
