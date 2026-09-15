create table commercial_agreement (
    commercial_agreement_identity text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    plan_level text not null
        check (plan_level in ('BUSINESS', 'GROWTH')),
    plan_revision_identifier text not null,
    billing_cadence text not null
        check (billing_cadence in ('MONTHLY', 'ANNUAL')),
    effective_from timestamptz not null,
    effective_until_exclusive timestamptz,
    acceptance_provenance_identity text not null,
    unique (merchant_identifier, commercial_agreement_identity),
    check (
        effective_until_exclusive is null
        or effective_until_exclusive > effective_from
    )
);

create index commercial_agreement_effective_lookup_idx
    on commercial_agreement (
        merchant_identifier,
        effective_from,
        effective_until_exclusive
    );

create table commercial_agreement_entitlement (
    commercial_agreement_identity text not null,
    merchant_identifier text not null,
    entitlement_identity text not null,
    primary key (commercial_agreement_identity, entitlement_identity),
    foreign key (merchant_identifier, commercial_agreement_identity)
        references commercial_agreement (
            merchant_identifier,
            commercial_agreement_identity
        )
        on delete cascade
);

create table commercial_agreement_transition_head (
    merchant_identifier text primary key
        references merchant_account (merchant_identifier),
    commercial_agreement_identity text not null unique,
    foreign key (merchant_identifier, commercial_agreement_identity)
        references commercial_agreement (
            merchant_identifier,
            commercial_agreement_identity
        )
);

create table commercial_agreement_transition_request (
    logical_request_identity text primary key,
    merchant_identifier text not null,
    expected_current_agreement_identity text,
    commercial_agreement_identity text not null,
    intent_fingerprint text not null,
    foreign key (merchant_identifier, commercial_agreement_identity)
        references commercial_agreement (
            merchant_identifier,
            commercial_agreement_identity
        )
);
