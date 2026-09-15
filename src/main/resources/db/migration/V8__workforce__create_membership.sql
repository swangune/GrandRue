create table workforce_merchant_membership (
    membership_identifier text primary key,
    merchant_identifier text not null references merchant_account (merchant_identifier),
    identity_reference text not null,
    lifecycle text not null check (lifecycle in ('ACTIVE', 'SUSPENDED', 'ENDED')),
    established_at timestamptz not null,
    ended_at timestamptz,
    unique (merchant_identifier, membership_identifier),
    check (
        (lifecycle = 'ENDED' and ended_at is not null)
        or
        (lifecycle <> 'ENDED' and ended_at is null)
    ),
    check (ended_at is null or ended_at >= established_at)
);

create unique index workforce_membership_one_current_per_identity
    on workforce_merchant_membership (merchant_identifier, identity_reference)
    where lifecycle in ('ACTIVE', 'SUSPENDED');
