alter table merchant_account
    add column lifecycle text not null default 'OPEN'
        check (lifecycle in ('OPEN', 'CLOSING', 'CLOSED'));

create table merchant_controller_transfer_request (
    logical_request_identity text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    expected_controller_relationship_identifier text not null,
    initiating_controller_identity text not null,
    receiving_identity text not null,
    replacement_controller_relationship_identifier text not null,
    authentication_assurance_evidence_identity text not null,
    recipient_acceptance_evidence_identity text not null,
    transferred_at timestamptz not null,
    foreign key (
        merchant_identifier,
        expected_controller_relationship_identifier
    ) references merchant_controller_relationship (
        merchant_identifier,
        controller_relationship_identifier
    ),
    foreign key (
        merchant_identifier,
        replacement_controller_relationship_identifier
    ) references merchant_controller_relationship (
        merchant_identifier,
        controller_relationship_identifier
    )
);

create table merchant_account_suspension (
    suspension_identity text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    source_authority_identifier text not null,
    reason_class_identifier text not null,
    established_at timestamptz not null,
    established_by_identifier text not null,
    release_authority_identifier text not null,
    provenance_identifier text not null,
    released_at timestamptz,
    released_by_identifier text,
    release_evidence_identifier text,
    check (
        (released_at is null
            and released_by_identifier is null
            and release_evidence_identifier is null)
        or
        (released_at is not null
            and released_by_identifier is not null
            and release_evidence_identifier is not null
            and released_at >= established_at)
    )
);

create index merchant_account_effective_suspension_idx
    on merchant_account_suspension (merchant_identifier)
    where released_at is null;

create table merchant_account_suspension_request (
    logical_request_identity text primary key,
    suspension_identity text not null unique
        references merchant_account_suspension (suspension_identity),
    merchant_identifier text not null
        references merchant_account (merchant_identifier)
);

create table merchant_account_suspension_release_request (
    logical_request_identity text primary key,
    suspension_identity text not null
        references merchant_account_suspension (suspension_identity),
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    releasing_authority_identifier text not null,
    released_at timestamptz not null,
    released_by_identifier text not null,
    release_evidence_identifier text not null
);

create table merchant_account_closure_begin_request (
    logical_request_identity text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    expected_controller_relationship_identifier text not null,
    initiating_controller_identity text not null,
    authentication_assurance_evidence_identity text not null,
    requested_at timestamptz not null
);

create table merchant_account_closure_finalize_request (
    logical_request_identity text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    expected_controller_relationship_identifier text not null,
    closure_readiness_evidence_identity text not null,
    acting_authority_identifier text not null,
    closed_at timestamptz not null
);
