create table personal_data_use_basis (
    basis_identity text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    subject_reference text not null,
    data_scope_identifier text not null,
    purpose text not null,
    audience_scope_identifier text not null,
    authority_source text not null,
    evidence_reference text not null,
    effective_from timestamptz not null,
    effective_until_exclusive timestamptz,
    constraint ck_personal_data_use_basis_interval
        check (
            effective_until_exclusive is null
            or effective_until_exclusive > effective_from
        )
);

create index personal_data_use_basis_effective_lookup_idx
    on personal_data_use_basis (
        merchant_identifier,
        subject_reference,
        data_scope_identifier,
        purpose,
        audience_scope_identifier,
        effective_from
    );
