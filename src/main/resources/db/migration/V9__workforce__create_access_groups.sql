create table workforce_access_group (
    group_identifier text primary key,
    merchant_identifier text not null references merchant_account (merchant_identifier),
    display_name text not null,
    unique (merchant_identifier, group_identifier)
);

create table workforce_group_membership (
    group_membership_identifier text primary key,
    merchant_identifier text not null,
    membership_identifier text not null,
    group_identifier text not null,
    lifecycle text not null check (lifecycle in ('ACTIVE', 'ENDED')),
    foreign key (merchant_identifier, membership_identifier)
        references workforce_merchant_membership (merchant_identifier, membership_identifier),
    foreign key (merchant_identifier, group_identifier)
        references workforce_access_group (merchant_identifier, group_identifier)
);

create unique index workforce_group_membership_one_active_per_pair
    on workforce_group_membership (
        merchant_identifier,
        membership_identifier,
        group_identifier
    )
    where lifecycle = 'ACTIVE';
