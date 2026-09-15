create table resource_protection_consumption_window (
    policy_identifier text not null,
    policy_version integer not null check (policy_version > 0),
    target_identifier text not null,
    subject_scope_identifier text not null,
    subject_identifier text not null,
    window_starts_at timestamptz not null,
    window_ends_at timestamptz not null,
    consumed_units bigint not null check (consumed_units >= 0),
    primary key (
        policy_identifier,
        policy_version,
        target_identifier,
        subject_scope_identifier,
        subject_identifier,
        window_starts_at
    ),
    check (window_ends_at > window_starts_at)
);

create table resource_protection_consumption_evidence (
    target_identifier text not null,
    subject_scope_identifier text not null,
    subject_identifier text not null,
    measurement_basis_identifier text not null,
    consumption_identity text not null,
    policy_identifier text not null,
    policy_version integer not null,
    window_starts_at timestamptz not null,
    admitted_at timestamptz not null,
    units bigint not null check (units > 0),
    primary key (
        target_identifier,
        subject_scope_identifier,
        subject_identifier,
        measurement_basis_identifier,
        consumption_identity
    ),
    foreign key (
        policy_identifier,
        policy_version,
        target_identifier,
        subject_scope_identifier,
        subject_identifier,
        window_starts_at
    ) references resource_protection_consumption_window (
        policy_identifier,
        policy_version,
        target_identifier,
        subject_scope_identifier,
        subject_identifier,
        window_starts_at
    )
);

create table temporary_protective_restriction (
    restriction_identity text primary key,
    subject_scope_identifier text not null,
    subject_identifier text not null,
    effective_from timestamptz not null,
    expires_at timestamptz not null,
    reason_class_identifier text not null,
    check (expires_at > effective_from)
);

create table temporary_protective_restriction_target (
    restriction_identity text not null
        references temporary_protective_restriction (restriction_identity)
        on delete cascade,
    target_identifier text not null,
    primary key (restriction_identity, target_identifier)
);

create index temporary_protective_restriction_lookup_idx
    on temporary_protective_restriction (
        subject_scope_identifier,
        subject_identifier,
        effective_from,
        expires_at
    );
