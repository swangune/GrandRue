-- MS-PROT-056 v1.9 §§8–13: one atomic append-only publication chain.
-- The singleton is coordination state, not an initial catalogue generation.
create table commercial_catalogue_publication (
    catalogue_identifier text primary key,
    request_identifier text not null unique,
    manifest_content bytea not null,
    predecessor_identifier text unique references commercial_catalogue_publication (catalogue_identifier),
    publishing_principal_identifier text not null,
    published_at timestamp with time zone not null unique,
    check (length(btrim(catalogue_identifier)) > 0),
    check (length(btrim(request_identifier)) > 0),
    check (length(btrim(publishing_principal_identifier)) > 0),
    check (octet_length(manifest_content) > 0),
    check (predecessor_identifier is null or predecessor_identifier <> catalogue_identifier)
);

create unique index commercial_catalogue_one_root
    on commercial_catalogue_publication ((predecessor_identifier is null))
    where predecessor_identifier is null;

create table commercial_catalogue_head (
    singleton boolean primary key check (singleton),
    catalogue_identifier text references commercial_catalogue_publication (catalogue_identifier),
    -- Lossless Instant encoding preserves nanosecond read boundaries even though
    -- PostgreSQL publication timestamps have microsecond precision.
    last_selection_instant text
);

insert into commercial_catalogue_head (singleton) values (true);
