-- MS-PROT-046 v1.2 / IMP-07-P2A:
-- durable Opportunity Publication revision/currentness evidence foundation.
-- Complete material revision representation, lifecycle transition history and
-- logical-operation idempotency remain outside this migration.

create table opportunity_publication_revision_identity (
    merchant_identifier text not null,
    opportunity_identity text not null,
    revision_identity text not null,

    constraint pk_opportunity_publication_revision_identity
        primary key (
            merchant_identifier,
            opportunity_identity,
            revision_identity
        ),
    constraint ck_opportunity_publication_revision_merchant
        check (btrim(merchant_identifier) <> ''),
    constraint ck_opportunity_publication_revision_opportunity
        check (btrim(opportunity_identity) <> ''),
    constraint ck_opportunity_publication_revision_identity
        check (btrim(revision_identity) <> '')
);

create table opportunity_publication_current (
    merchant_identifier text not null,
    opportunity_identity text not null,
    current_revision_identity text not null,
    lifecycle text not null,
    published_revision_identity text,

    constraint pk_opportunity_publication_current
        primary key (merchant_identifier, opportunity_identity),
    constraint ck_opportunity_publication_current_merchant
        check (btrim(merchant_identifier) <> ''),
    constraint ck_opportunity_publication_current_opportunity
        check (btrim(opportunity_identity) <> ''),
    constraint ck_opportunity_publication_current_revision
        check (btrim(current_revision_identity) <> ''),
    constraint ck_opportunity_publication_current_lifecycle
        check (lifecycle in ('DRAFT', 'PUBLISHED', 'WITHDRAWN')),
    constraint ck_opportunity_publication_published_revision
        check (
            published_revision_identity is null
            or btrim(published_revision_identity) <> ''
        ),
    constraint ck_opportunity_publication_lifecycle_evidence
        check (
            (lifecycle = 'DRAFT' and published_revision_identity is null)
            or
            (lifecycle in ('PUBLISHED', 'WITHDRAWN')
                and published_revision_identity is not null)
        ),
    constraint fk_opportunity_publication_current_revision
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
    constraint fk_opportunity_publication_published_revision
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
