create table credential_binding (
    binding_identifier text primary key,
    binding_scope text not null,
    merchant_identifier text,
    responsibility_reference text not null,
    external_context_reference text not null,
    technical_purpose_identifier text not null,
    established_at timestamptz not null,
    constraint ck_credential_binding_scope
        check (binding_scope in ('PLATFORM', 'MERCHANT')),
    constraint ck_credential_binding_scope_affinity
        check (
            (binding_scope = 'PLATFORM' and merchant_identifier is null)
            or
            (binding_scope = 'MERCHANT' and merchant_identifier is not null)
        ),
    constraint fk_credential_binding_merchant
        foreign key (merchant_identifier)
        references merchant_account (merchant_identifier)
);

create unique index credential_binding_exact_context_idx
    on credential_binding (
        binding_scope,
        coalesce(merchant_identifier, ''),
        responsibility_reference,
        external_context_reference,
        technical_purpose_identifier
    );

create table credential_generation (
    generation_identifier text primary key,
    binding_identifier text not null,
    protected_material_reference text not null unique,
    registered_at timestamptz not null,
    constraint fk_credential_generation_binding
        foreign key (binding_identifier)
        references credential_binding (binding_identifier)
);

create index credential_generation_binding_idx
    on credential_generation (binding_identifier, registered_at, generation_identifier);

create table credential_generation_policy (
    policy_identifier text primary key,
    generation_identifier text not null,
    policy_sequence bigint not null,
    state text not null,
    allow_new_execution boolean not null,
    allow_existing_obligation boolean not null,
    allow_verification boolean not null,
    effective_at timestamptz not null,
    evidence_reference text,
    unique (generation_identifier, policy_sequence),
    constraint fk_credential_policy_generation
        foreign key (generation_identifier)
        references credential_generation (generation_identifier),
    constraint ck_credential_policy_sequence
        check (policy_sequence > 0),
    constraint ck_credential_generation_state
        check (state in ('USABLE', 'RETIRING', 'EXPIRED', 'REVOKED', 'COMPROMISED')),
    constraint ck_credential_terminal_use
        check (
            state not in ('EXPIRED', 'REVOKED', 'COMPROMISED')
            or not (allow_new_execution or allow_existing_obligation or allow_verification)
        ),
    constraint ck_credential_retiring_new_use
        check (state <> 'RETIRING' or not allow_new_execution),
    constraint ck_credential_nonterminal_use
        check (
            state in ('EXPIRED', 'REVOKED', 'COMPROMISED')
            or allow_new_execution or allow_existing_obligation or allow_verification
        )
);

create index credential_generation_policy_current_idx
    on credential_generation_policy (
        generation_identifier,
        effective_at desc,
        policy_sequence desc
    );
