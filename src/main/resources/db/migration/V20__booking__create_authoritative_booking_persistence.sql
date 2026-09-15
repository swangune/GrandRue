create table booking_booking (
    merchant_identifier text not null,
    booking_identifier text not null,
    customer_context_identifier text not null,
    scheduled_operation_identifier text not null,
    capacity_subject_identifier text not null,
    capacity_starts_at timestamptz not null,
    capacity_ends_at timestamptz not null,
    governing_release_identifier text not null,
    confirmed_at timestamptz not null,
    primary key (merchant_identifier, booking_identifier),
    constraint ck_booking_capacity_interval
        check (capacity_starts_at < capacity_ends_at)
);

create table booking_allocation_claim (
    merchant_identifier text not null,
    claim_identifier text not null,
    capacity_subject_identifier text not null,
    capacity_starts_at timestamptz not null,
    capacity_ends_at timestamptz not null,
    use_identifier text not null,
    claimed_at timestamptz not null,
    primary key (merchant_identifier, claim_identifier),
    unique (merchant_identifier, use_identifier),
    constraint ck_booking_allocation_interval
        check (capacity_starts_at < capacity_ends_at)
);

create index booking_allocation_scope_idx
    on booking_allocation_claim (
        merchant_identifier,
        capacity_subject_identifier,
        capacity_starts_at,
        capacity_ends_at
    );

create table booking_appointment (
    merchant_identifier text not null,
    appointment_identifier text not null,
    booking_identifier text not null,
    customer_context_identifier text not null,
    scheduled_operation_identifier text not null,
    capacity_subject_identifier text not null,
    capacity_starts_at timestamptz not null,
    capacity_ends_at timestamptz not null,
    allocation_identifier text not null,
    governing_release_identifier text not null,
    appointment_revision bigint not null,
    confirmed_at timestamptz not null,
    primary key (merchant_identifier, appointment_identifier),
    unique (merchant_identifier, booking_identifier),
    unique (merchant_identifier, allocation_identifier),
    foreign key (merchant_identifier, booking_identifier)
        references booking_booking (merchant_identifier, booking_identifier),
    foreign key (merchant_identifier, allocation_identifier)
        references booking_allocation_claim (merchant_identifier, claim_identifier),
    constraint ck_booking_appointment_interval
        check (capacity_starts_at < capacity_ends_at),
    constraint ck_booking_appointment_revision
        check (appointment_revision > 0)
);

create table booking_outbox (
    merchant_identifier text not null,
    event_identifier text not null,
    fact_identifier text not null,
    subject_identifier text not null,
    causation_identifier text not null,
    occurred_at timestamptz not null,
    acknowledged_at timestamptz,
    primary key (merchant_identifier, event_identifier)
);

create index booking_outbox_pending_idx
    on booking_outbox (
        merchant_identifier,
        acknowledged_at,
        occurred_at,
        event_identifier
    );

create table booking_handled_command (
    merchant_identifier text not null,
    command_identifier text not null,
    booking_identifier text not null,
    appointment_identifier text not null,
    customer_context_identifier text not null,
    scheduled_operation_identifier text not null,
    capacity_subject_identifier text not null,
    capacity_starts_at timestamptz not null,
    capacity_ends_at timestamptz not null,
    confirmed_at timestamptz not null,
    allocation_identifier text not null,
    event_identifier text not null,
    primary key (merchant_identifier, command_identifier),
    unique (merchant_identifier, booking_identifier),
    unique (merchant_identifier, appointment_identifier),
    unique (merchant_identifier, allocation_identifier),
    unique (merchant_identifier, event_identifier),
    foreign key (merchant_identifier, booking_identifier)
        references booking_booking (merchant_identifier, booking_identifier),
    foreign key (merchant_identifier, appointment_identifier)
        references booking_appointment (merchant_identifier, appointment_identifier),
    foreign key (merchant_identifier, allocation_identifier)
        references booking_allocation_claim (merchant_identifier, claim_identifier),
    foreign key (merchant_identifier, event_identifier)
        references booking_outbox (merchant_identifier, event_identifier),
    constraint ck_booking_handled_command_interval
        check (capacity_starts_at < capacity_ends_at)
);
