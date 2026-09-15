create table standing_free_baseline (
    baseline_identifier text primary key,
    merchant_identifier text not null unique
        references merchant_account (merchant_identifier),
    originating_establishment_identity text not null unique,
    effective_from timestamptz not null,
    free_plan_revision_identifier text not null,
    constraint uq_standing_free_baseline_affinity
        unique (baseline_identifier, merchant_identifier),
    constraint fk_standing_free_originating_establishment
        foreign key (merchant_identifier, originating_establishment_identity)
        references merchant_account_establishment (
            merchant_identifier,
            establishment_identity
        )
);

create table standing_free_baseline_entitlement (
    baseline_identifier text not null,
    merchant_identifier text not null,
    entitlement_identifier text not null,
    primary key (baseline_identifier, entitlement_identifier),
    constraint fk_standing_free_entitlement_baseline
        foreign key (baseline_identifier, merchant_identifier)
        references standing_free_baseline (
            baseline_identifier,
            merchant_identifier
        )
);

create index standing_free_baseline_entitlement_lookup_idx
    on standing_free_baseline_entitlement (
        merchant_identifier,
        entitlement_identifier,
        baseline_identifier
    );
