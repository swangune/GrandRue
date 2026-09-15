create table audit_record (
    audit_identifier text primary key,
    occurred_at timestamptz not null,
    principal_reference text not null,
    execution_scope text not null,
    merchant_identifier text,
    action_class text not null,
    action_identifier text not null,
    subject_type text,
    subject_reference text,
    outcome_identifier text not null,
    reason_category text,
    correlation_identifier text not null,
    causation_identifier text,
    origin_identifier text,
    evidence_reference text,
    constraint ck_audit_execution_scope
        check (execution_scope in ('PLATFORM', 'MERCHANT')),
    constraint ck_audit_scope_affinity
        check (
            (execution_scope = 'PLATFORM' and merchant_identifier is null)
            or
            (execution_scope = 'MERCHANT' and merchant_identifier is not null)
        ),
    constraint ck_audit_action_class
        check (action_class in (
            'EXECUTION_ACCEPTED',
            'EXECUTION_REJECTED',
            'AUTHENTICATION_SECURITY',
            'AUTHORISATION_SECURITY',
            'CONFIGURATION_CHANGE',
            'ADMINISTRATIVE_ACTION',
            'INTEGRATION_RECONCILIATION',
            'BREAK_GLASS_ACTION',
            'DATA_ACCESS',
            'DATA_EXPORT'
        )),
    constraint ck_audit_subject_pair
        check (
            (subject_type is null and subject_reference is null)
            or
            (subject_type is not null and subject_reference is not null)
        ),
    constraint fk_audit_merchant
        foreign key (merchant_identifier)
        references merchant_account (merchant_identifier)
);

create index audit_record_merchant_time_idx
    on audit_record (merchant_identifier, occurred_at, audit_identifier)
    where merchant_identifier is not null;

create index audit_record_correlation_idx
    on audit_record (correlation_identifier, occurred_at, audit_identifier);
