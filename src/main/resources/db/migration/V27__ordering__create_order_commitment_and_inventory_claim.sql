create table inventory_stock_position (
    merchant_identifier text not null,
    subject_identifier text not null,
    stock_on_hand bigint not null,
    primary key (merchant_identifier, subject_identifier),
    constraint ck_inventory_stock_position_non_negative
        check (stock_on_hand >= 0)
);

create table inventory_quantity_claim (
    merchant_identifier text not null,
    claim_identifier text not null,
    subject_identifier text not null,
    quantity bigint not null,
    use_identifier text not null,
    claimed_at timestamptz not null,
    primary key (merchant_identifier, claim_identifier),
    foreign key (merchant_identifier, subject_identifier)
        references inventory_stock_position (merchant_identifier, subject_identifier),
    constraint ck_inventory_quantity_claim_positive
        check (quantity > 0)
);

create index inventory_quantity_claim_subject_idx
    on inventory_quantity_claim (
        merchant_identifier,
        subject_identifier,
        claimed_at,
        claim_identifier
    );

create table ordering_order (
    merchant_identifier text not null,
    order_identifier text not null,
    customer_context_identifier text,
    governing_release_identifier text not null,
    committed_at timestamptz not null,
    primary key (merchant_identifier, order_identifier)
);

create table ordering_order_portion (
    merchant_identifier text not null,
    order_identifier text not null,
    portion_identifier text not null,
    portion_ordinal integer not null,
    committed_subject_reference text not null,
    quantity_magnitude numeric not null,
    quantity_unit_identifier text not null,
    currency_identifier text not null,
    committed_unit_minor_amount numeric(38, 0) not null,
    commercial_terms_provenance_reference text not null,
    primary key (merchant_identifier, order_identifier, portion_identifier),
    unique (merchant_identifier, order_identifier, portion_ordinal),
    foreign key (merchant_identifier, order_identifier)
        references ordering_order (merchant_identifier, order_identifier),
    constraint ck_ordering_order_portion_ordinal
        check (portion_ordinal >= 0),
    constraint ck_ordering_order_portion_quantity
        check (quantity_magnitude > 0),
    constraint ck_ordering_order_portion_money
        check (committed_unit_minor_amount >= 0)
);

create table ordering_outbox (
    merchant_identifier text not null,
    event_identifier text not null,
    fact_identifier text not null,
    subject_identifier text not null,
    causation_identifier text not null,
    occurred_at timestamptz not null,
    acknowledged_at timestamptz,
    primary key (merchant_identifier, event_identifier)
);

create index ordering_outbox_pending_idx
    on ordering_outbox (
        merchant_identifier,
        acknowledged_at,
        occurred_at,
        event_identifier
    );

create table ordering_handled_command (
    merchant_identifier text not null,
    command_identifier text not null,
    order_identifier text not null,
    customer_context_identifier text,
    event_identifier text not null,
    primary key (merchant_identifier, command_identifier),
    unique (merchant_identifier, order_identifier),
    unique (merchant_identifier, event_identifier),
    foreign key (merchant_identifier, order_identifier)
        references ordering_order (merchant_identifier, order_identifier),
    foreign key (merchant_identifier, event_identifier)
        references ordering_outbox (merchant_identifier, event_identifier)
);

create table ordering_handled_requested_portion (
    merchant_identifier text not null,
    command_identifier text not null,
    portion_identifier text not null,
    portion_ordinal integer not null,
    subject_reference text not null,
    quantity_magnitude numeric not null,
    quantity_unit_identifier text not null,
    primary key (merchant_identifier, command_identifier, portion_identifier),
    unique (merchant_identifier, command_identifier, portion_ordinal),
    foreign key (merchant_identifier, command_identifier)
        references ordering_handled_command (merchant_identifier, command_identifier),
    constraint ck_ordering_handled_portion_ordinal
        check (portion_ordinal >= 0),
    constraint ck_ordering_handled_portion_quantity
        check (quantity_magnitude > 0)
);

create table ordering_handled_claim (
    merchant_identifier text not null,
    command_identifier text not null,
    claim_identifier text not null,
    claim_ordinal integer not null,
    primary key (merchant_identifier, command_identifier, claim_identifier),
    unique (merchant_identifier, command_identifier, claim_ordinal),
    foreign key (merchant_identifier, command_identifier)
        references ordering_handled_command (merchant_identifier, command_identifier),
    foreign key (merchant_identifier, claim_identifier)
        references inventory_quantity_claim (merchant_identifier, claim_identifier),
    constraint ck_ordering_handled_claim_ordinal
        check (claim_ordinal >= 0)
);
