create table notification_intent (
    intent_identity text primary key,
    notification_type_identifier text not null,
    owner_scope_kind_identifier text not null,
    owner_scope_identifier text not null,
    source_fact_or_process_reference text not null,
    recipient_resolution_basis_reference text not null,
    correlation_identifier text,
    causation_identifier text,
    created_at timestamptz not null,
    unique (
        intent_identity,
        owner_scope_kind_identifier,
        owner_scope_identifier
    )
);

create table notification_dispatch (
    dispatch_identity text primary key,
    intent_identity text not null,
    owner_scope_kind_identifier text not null,
    owner_scope_identifier text not null,
    recipient_identity text not null,
    recipient_kind_identifier text not null,
    recipient_semantic_reference text not null,
    recipient_resolution_basis text not null
        check (recipient_resolution_basis in ('COMMITMENT_BOUND', 'CURRENT_RELATIONSHIP')),
    channel text not null check (channel in ('IN_APP', 'EMAIL', 'PUSH', 'SMS')),
    endpoint_reference text not null,
    content_projection_reference text not null,
    created_at timestamptz not null,
    unique (
        dispatch_identity,
        owner_scope_kind_identifier,
        owner_scope_identifier
    ),
    foreign key (
        intent_identity,
        owner_scope_kind_identifier,
        owner_scope_identifier
    ) references notification_intent (
        intent_identity,
        owner_scope_kind_identifier,
        owner_scope_identifier
    )
);

create index notification_dispatch_intent_idx
    on notification_dispatch (intent_identity, created_at, dispatch_identity);

create table notification_delivery_attempt (
    attempt_identity text primary key,
    dispatch_identity text not null,
    owner_scope_kind_identifier text not null,
    owner_scope_identifier text not null,
    attempted_at timestamptz not null,
    channel_provider_context_reference text not null,
    provider_idempotency_reference text not null,
    outcome text not null check (
        outcome in (
            'STARTED',
            'KNOWN_ACCEPTED',
            'KNOWN_REJECTED',
            'KNOWN_FAILED_BEFORE_PROVIDER_EFFECT',
            'EXECUTION_UNCERTAIN'
        )
    ),
    failure_reference text,
    unique (
        attempt_identity,
        dispatch_identity,
        owner_scope_kind_identifier,
        owner_scope_identifier
    ),
    foreign key (
        dispatch_identity,
        owner_scope_kind_identifier,
        owner_scope_identifier
    ) references notification_dispatch (
        dispatch_identity,
        owner_scope_kind_identifier,
        owner_scope_identifier
    )
);

create index notification_delivery_attempt_dispatch_idx
    on notification_delivery_attempt (dispatch_identity, attempted_at, attempt_identity);

create table notification_delivery_evidence (
    evidence_identity text primary key,
    dispatch_identity text not null,
    owner_scope_kind_identifier text not null,
    owner_scope_identifier text not null,
    attempt_identity text,
    evidence_type_identifier text not null,
    observed_at timestamptz not null,
    provider_reference text,
    provenance_identifier text not null,
    foreign key (
        dispatch_identity,
        owner_scope_kind_identifier,
        owner_scope_identifier
    ) references notification_dispatch (
        dispatch_identity,
        owner_scope_kind_identifier,
        owner_scope_identifier
    ),
    foreign key (
        attempt_identity,
        dispatch_identity,
        owner_scope_kind_identifier,
        owner_scope_identifier
    ) references notification_delivery_attempt (
        attempt_identity,
        dispatch_identity,
        owner_scope_kind_identifier,
        owner_scope_identifier
    )
);

create index notification_delivery_evidence_dispatch_idx
    on notification_delivery_evidence (dispatch_identity, observed_at, evidence_identity);
