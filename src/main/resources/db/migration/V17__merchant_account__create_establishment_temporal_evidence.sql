create table merchant_account_establishment (
    establishment_identity text primary key,
    merchant_identifier text not null unique
        references merchant_account (merchant_identifier),
    logical_establishment_request_identity text not null unique
        references merchant_account_establishment_request (
            logical_establishment_request_identity
        ),
    established_at timestamptz not null,
    constraint uq_merchant_account_establishment_affinity
        unique (merchant_identifier, establishment_identity)
);

create table merchant_account_establishment_publication_intent (
    publication_intent_identifier text primary key,
    establishment_identity text not null unique,
    merchant_identifier text not null,
    logical_establishment_request_identity text not null,
    occurred_at timestamptz not null,
    published_at timestamptz,
    constraint fk_merchant_account_establishment_publication_fact
        foreign key (merchant_identifier, establishment_identity)
        references merchant_account_establishment (
            merchant_identifier,
            establishment_identity
        ),
    constraint fk_merchant_account_establishment_publication_request
        foreign key (logical_establishment_request_identity)
        references merchant_account_establishment_request (
            logical_establishment_request_identity
        )
);

create index merchant_account_establishment_publication_pending_idx
    on merchant_account_establishment_publication_intent (
        occurred_at,
        publication_intent_identifier
    )
    where published_at is null;
