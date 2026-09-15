create table identity_security_generation (
    identity_reference text primary key,
    current_generation_reference text not null unique,
    concurrency_version bigint not null,
    established_at timestamptz not null,
    last_rotated_at timestamptz,
    constraint ck_identity_security_generation_version
        check (concurrency_version >= 1),
    constraint ck_identity_security_generation_rotation_time
        check (
            (concurrency_version = 1 and last_rotated_at is null)
            or
            (
                concurrency_version > 1
                and last_rotated_at is not null
                and last_rotated_at >= established_at
            )
        )
);
