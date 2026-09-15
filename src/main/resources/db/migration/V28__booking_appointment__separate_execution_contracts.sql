drop table booking_handled_command;
drop table booking_appointment;

alter table booking_booking
    rename column scheduled_operation_identifier to booked_subject_reference;

alter table booking_booking
    drop column capacity_subject_identifier;

alter table booking_booking
    rename column capacity_starts_at to reservation_starts_at;

alter table booking_booking
    rename column capacity_ends_at to reservation_ends_at;

create table booking_handled_command (
    merchant_identifier text not null,
    command_identifier text not null,
    booking_identifier text not null,
    customer_context_identifier text not null,
    booked_subject_reference text not null,
    allocation_subject_identifier text not null,
    reservation_starts_at timestamptz not null,
    reservation_ends_at timestamptz not null,
    confirmed_at timestamptz not null,
    allocation_identifier text not null,
    event_identifier text not null,
    primary key (merchant_identifier, command_identifier),
    unique (merchant_identifier, booking_identifier),
    unique (merchant_identifier, allocation_identifier),
    unique (merchant_identifier, event_identifier),
    foreign key (merchant_identifier, booking_identifier)
        references booking_booking (merchant_identifier, booking_identifier),
    foreign key (merchant_identifier, allocation_identifier)
        references booking_allocation_claim (merchant_identifier, claim_identifier),
    foreign key (merchant_identifier, event_identifier)
        references booking_outbox (merchant_identifier, event_identifier),
    constraint ck_booking_handled_command_interval_v2
        check (reservation_starts_at < reservation_ends_at)
);

create table appointment_appointment (
    merchant_identifier text not null,
    appointment_identifier text not null,
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
    unique (merchant_identifier, allocation_identifier),
    constraint ck_appointment_interval
        check (capacity_starts_at < capacity_ends_at),
    constraint ck_appointment_revision
        check (appointment_revision > 0)
);

create table appointment_allocation_claim (
    merchant_identifier text not null,
    claim_identifier text not null,
    capacity_subject_identifier text not null,
    capacity_starts_at timestamptz not null,
    capacity_ends_at timestamptz not null,
    use_identifier text not null,
    claimed_at timestamptz not null,
    primary key (merchant_identifier, claim_identifier),
    unique (merchant_identifier, use_identifier),
    constraint ck_appointment_allocation_interval
        check (capacity_starts_at < capacity_ends_at)
);

alter table appointment_appointment
    add foreign key (merchant_identifier, allocation_identifier)
        references appointment_allocation_claim (merchant_identifier, claim_identifier);

create index appointment_allocation_scope_idx
    on appointment_allocation_claim (
        merchant_identifier,
        capacity_subject_identifier,
        capacity_starts_at,
        capacity_ends_at
    );

create table appointment_outbox (
    merchant_identifier text not null,
    event_identifier text not null,
    fact_identifier text not null,
    subject_identifier text not null,
    causation_identifier text not null,
    occurred_at timestamptz not null,
    acknowledged_at timestamptz,
    primary key (merchant_identifier, event_identifier)
);

create table appointment_handled_command (
    merchant_identifier text not null,
    command_identifier text not null,
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
    unique (merchant_identifier, appointment_identifier),
    unique (merchant_identifier, allocation_identifier),
    unique (merchant_identifier, event_identifier),
    foreign key (merchant_identifier, appointment_identifier)
        references appointment_appointment (merchant_identifier, appointment_identifier),
    foreign key (merchant_identifier, allocation_identifier)
        references appointment_allocation_claim (merchant_identifier, claim_identifier),
    foreign key (merchant_identifier, event_identifier)
        references appointment_outbox (merchant_identifier, event_identifier),
    constraint ck_appointment_handled_command_interval
        check (capacity_starts_at < capacity_ends_at)
);
