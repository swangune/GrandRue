create table webauthn_credential_record (
    credential_id bytea primary key,
    user_entity_user_id bytea not null,
    credential_type text,
    public_key_cose bytea not null,
    signature_count bigint not null,
    uv_initialized boolean not null,
    transports text[] not null,
    backup_eligible boolean not null,
    backup_state boolean not null,
    attestation_object bytea,
    attestation_client_data_json bytea,
    created_at timestamptz not null,
    last_used_at timestamptz not null,
    label text not null,
    constraint ck_webauthn_credential_id_nonempty
        check (octet_length(credential_id) > 0),
    constraint ck_webauthn_user_handle_size
        check (octet_length(user_entity_user_id) between 1 and 64),
    constraint ck_webauthn_public_key_nonempty
        check (octet_length(public_key_cose) > 0),
    constraint ck_webauthn_signature_count
        check (signature_count >= 0)
);

create index webauthn_credential_record_user_idx
    on webauthn_credential_record (user_entity_user_id, created_at, credential_id);
