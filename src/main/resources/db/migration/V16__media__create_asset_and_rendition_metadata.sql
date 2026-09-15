create table media_asset (
    asset_identity text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    media_kind text not null check (media_kind in ('IMAGE', 'VIDEO', 'DOCUMENT')),
    canonical_source_reference text not null,
    source_digest text not null,
    created_at timestamptz not null,
    validation_state text not null check (
        validation_state in (
            'PENDING_VALIDATION',
            'VALIDATED',
            'QUARANTINED',
            'REJECTED',
            'LOGICALLY_UNAVAILABLE'
        )
    ),
    unique (asset_identity, merchant_identifier),
    check (asset_identity <> canonical_source_reference)
);

create table media_rendition (
    rendition_identity text primary key,
    merchant_identifier text not null,
    source_asset_identity text not null,
    profile_identity text not null,
    profile_version integer not null check (profile_version > 0),
    storage_reference text not null,
    rendition_digest text not null,
    generated_at timestamptz not null,
    processing_outcome text not null check (
        processing_outcome in ('PENDING', 'SUCCEEDED', 'FAILED')
    ),
    unique (
        rendition_identity,
        source_asset_identity,
        merchant_identifier
    ),
    foreign key (source_asset_identity, merchant_identifier)
        references media_asset (asset_identity, merchant_identifier),
    check (rendition_identity <> storage_reference)
);

create index media_rendition_source_profile_idx
    on media_rendition (
        source_asset_identity,
        merchant_identifier,
        profile_identity,
        profile_version,
        generated_at
    );
