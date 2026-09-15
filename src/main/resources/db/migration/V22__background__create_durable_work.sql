create table durable_work_instruction (
    work_identifier text primary key,
    owner_context_identifier text not null,
    execution_scope text not null,
    merchant_identifier text,
    due_at timestamptz not null,
    next_attempt_at timestamptz not null,
    responsibility_identifier text not null,
    correlation_identifier text not null,
    causation_identifier text,
    semantic_provenance_reference text,
    retry_policy_reference text not null,
    overdue_handling text not null,
    created_at timestamptz not null,
    claimed_by text,
    claim_expires_at timestamptz,
    final_classification text,
    finalised_at timestamptz,
    constraint ck_background_execution_scope
        check (execution_scope in ('PLATFORM', 'MERCHANT')),
    constraint ck_background_scope_affinity
        check (
            (execution_scope = 'PLATFORM' and merchant_identifier is null)
            or
            (execution_scope = 'MERCHANT' and merchant_identifier is not null)
        ),
    constraint ck_background_overdue_handling
        check (overdue_handling in (
            'EXECUTE_WHEN_OVERDUE',
            'RE_EVALUATE_CURRENT_STATE',
            'EXPIRE_WITHOUT_EXECUTION',
            'ESCALATE'
        )),
    constraint ck_background_claim_pair
        check (
            (claimed_by is null and claim_expires_at is null)
            or
            (claimed_by is not null and claim_expires_at is not null)
        ),
    constraint ck_background_final_pair
        check (
            (final_classification is null and finalised_at is null)
            or
            (final_classification is not null and finalised_at is not null)
        ),
    constraint ck_background_final_classification
        check (final_classification is null or final_classification in (
            'RECONCILIATION_REQUIRED',
            'TERMINAL_FAILURE',
            'MANUAL_INTERVENTION_REQUIRED',
            'NO_LONGER_APPLICABLE',
            'SUCCESS'
        )),
    constraint fk_background_merchant
        foreign key (merchant_identifier)
        references merchant_account (merchant_identifier)
);

create index durable_work_due_idx
    on durable_work_instruction (next_attempt_at, work_identifier)
    where finalised_at is null;

create table durable_work_attempt (
    attempt_identifier text primary key,
    work_identifier text not null,
    attempted_at timestamptz not null,
    principal_reference text not null,
    result_classification text not null,
    evidence_reference text,
    constraint fk_background_attempt_work
        foreign key (work_identifier)
        references durable_work_instruction (work_identifier),
    constraint ck_background_attempt_classification
        check (result_classification in (
            'RETRY_SAFE',
            'RECONCILIATION_REQUIRED',
            'TERMINAL_FAILURE',
            'MANUAL_INTERVENTION_REQUIRED',
            'NO_LONGER_APPLICABLE',
            'SUCCESS'
        ))
);

create index durable_work_attempt_work_idx
    on durable_work_attempt (work_identifier, attempted_at, attempt_identifier);
