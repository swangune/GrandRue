create table workforce_role_definition (
    role_identifier text primary key,
    merchant_identifier text not null references merchant_account (merchant_identifier),
    display_name text not null,
    unique (merchant_identifier, role_identifier)
);

create table workforce_role_privilege (
    role_identifier text not null,
    merchant_identifier text not null,
    privilege_identifier text not null,
    primary key (role_identifier, privilege_identifier),
    foreign key (merchant_identifier, role_identifier)
        references workforce_role_definition (merchant_identifier, role_identifier)
        on delete cascade
);

create table workforce_role_assignment (
    assignment_identifier text primary key,
    merchant_identifier text not null,
    membership_identifier text,
    group_identifier text,
    role_identifier text not null,
    effective_from timestamptz not null,
    effective_until_exclusive timestamptz,
    revoked_at timestamptz,
    foreign key (merchant_identifier, role_identifier)
        references workforce_role_definition (merchant_identifier, role_identifier),
    foreign key (merchant_identifier, membership_identifier)
        references workforce_merchant_membership (merchant_identifier, membership_identifier),
    foreign key (merchant_identifier, group_identifier)
        references workforce_access_group (merchant_identifier, group_identifier),
    check (
        (membership_identifier is not null and group_identifier is null)
        or
        (membership_identifier is null and group_identifier is not null)
    ),
    check (
        effective_until_exclusive is null
        or effective_until_exclusive > effective_from
    ),
    check (
        revoked_at is null
        or effective_until_exclusive is null
        or revoked_at < effective_until_exclusive
    )
);
