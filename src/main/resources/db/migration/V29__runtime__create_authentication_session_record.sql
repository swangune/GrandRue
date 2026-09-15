create table authentication_session_record (
    session_identity varchar(160) primary key,
    identity_reference varchar(160) not null,
    credential_verifier varchar(160) not null unique,
    established_at timestamptz not null,
    authentication_assurance_reference varchar(160) not null,
    authentication_method_reference varchar(160) not null,
    absolute_expiry timestamptz not null,
    last_activity_at timestamptz not null,
    security_generation_reference varchar(160) not null,
    revoked_at timestamptz null,
    revocation_reason varchar(500) null,

    constraint authentication_session_absolute_expiry_ck
        check (absolute_expiry > established_at),
    constraint authentication_session_last_activity_ck
        check (
            last_activity_at >= established_at
            and last_activity_at <= absolute_expiry
        ),
    constraint authentication_session_revocation_pair_ck
        check (
            (revoked_at is null and revocation_reason is null)
            or
            (revoked_at is not null and revocation_reason is not null)
        ),
    constraint authentication_session_revocation_time_ck
        check (revoked_at is null or revoked_at >= last_activity_at)
);

create index authentication_session_identity_reference_idx
    on authentication_session_record (identity_reference);
