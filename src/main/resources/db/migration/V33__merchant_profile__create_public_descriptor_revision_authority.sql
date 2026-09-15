-- MS-PROT-051 v1.1: independently revisioned Merchant Public Descriptor.
-- Rows are immutable historical authority; current state is the highest
-- revision within one Merchant Scope.

create table merchant_public_descriptor_revision (
    merchant_identifier text not null,
    descriptor_revision bigint not null,
    display_name text not null,
    tagline text,
    short_summary text,
    approved_description text,
    mutation_request_identifier text not null,
    provenance_reference text not null,
    principal_reference text not null,
    origin_identifier text,
    committed_at timestamptz not null,

    constraint pk_merchant_public_descriptor_revision
        primary key (merchant_identifier, descriptor_revision),
    constraint uk_merchant_public_descriptor_mutation_request
        unique (mutation_request_identifier),
    constraint ck_merchant_public_descriptor_revision_positive
        check (descriptor_revision > 0),
    constraint ck_merchant_public_descriptor_display_name
        check (btrim(display_name) <> ''),
    constraint ck_merchant_public_descriptor_request
        check (btrim(mutation_request_identifier) <> ''),
    constraint ck_merchant_public_descriptor_provenance
        check (btrim(provenance_reference) <> ''),
    constraint ck_merchant_public_descriptor_principal
        check (btrim(principal_reference) <> ''),
    constraint ck_merchant_public_descriptor_origin
        check (origin_identifier is null or btrim(origin_identifier) <> '')
);

create index ix_merchant_public_descriptor_current
    on merchant_public_descriptor_revision (
        merchant_identifier,
        descriptor_revision desc
    );
