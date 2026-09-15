create table webauthn_authentication_subject (
    user_handle bytea primary key,
    identity_reference text not null unique,
    username text not null unique,
    display_name text,
    constraint ck_webauthn_subject_handle_size
        check (octet_length(user_handle) between 1 and 64),
    constraint ck_webauthn_subject_identity_reference_nonblank
        check (btrim(identity_reference) <> ''),
    constraint ck_webauthn_subject_username_nonblank
        check (btrim(username) <> '')
);

create index webauthn_authentication_subject_identity_idx
    on webauthn_authentication_subject (identity_reference);
