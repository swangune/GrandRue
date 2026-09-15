create table workforce_operational_device_authorisation (
    authorisation_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    binding_reference text not null,
    authorised_by_identity_reference text not null,
    authorised_at timestamptz not null,
    lifecycle text not null
        check (lifecycle in ('ACTIVE', 'REVOKED')),
    revoked_at timestamptz,
    unique (merchant_identifier, authorisation_identifier),
    check (
        (lifecycle = 'ACTIVE' and revoked_at is null)
        or
        (lifecycle = 'REVOKED' and revoked_at is not null and revoked_at >= authorised_at)
    )
);

create unique index uq_workforce_one_active_operational_device_binding
    on workforce_operational_device_authorisation (
        merchant_identifier,
        binding_reference
    )
    where lifecycle = 'ACTIVE';

create table workforce_operational_device_authorisation_request (
    logical_request_identity text primary key,
    merchant_identifier text not null,
    binding_reference text not null,
    controller_identity_reference text not null,
    authorisation_identifier text not null,
    foreign key (merchant_identifier, authorisation_identifier)
        references workforce_operational_device_authorisation (
            merchant_identifier,
            authorisation_identifier
        )
);
