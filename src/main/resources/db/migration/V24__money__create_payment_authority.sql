create table payment_obligation (
    obligation_identifier text primary key,
    merchant_identifier text not null,
    commercial_subject_reference text not null,
    currency_identifier text not null,
    minor_unit_amount numeric not null,
    source_commercial_commitment_reference text not null,
    due_condition_identifier text not null,
    provenance_reference text not null,
    established_at timestamptz not null,
    unique (merchant_identifier, obligation_identifier),
    constraint ck_payment_obligation_amount_non_negative
        check (minor_unit_amount >= 0 and minor_unit_amount = trunc(minor_unit_amount)),
    constraint fk_payment_obligation_merchant
        foreign key (merchant_identifier)
        references merchant_account (merchant_identifier)
);

create index payment_obligation_subject_idx
    on payment_obligation (merchant_identifier, commercial_subject_reference, established_at);

create table provider_payment_evidence (
    evidence_identifier text primary key,
    merchant_identifier text not null,
    provider_identifier text not null,
    provider_transaction_reference text not null,
    mainstreet_correlation_identifier text not null,
    currency_identifier text not null,
    minor_unit_amount numeric not null,
    payment_method_category text not null,
    provider_result_category text not null,
    observed_at timestamptz not null,
    receipt_evidence_reference text,
    unique (merchant_identifier, evidence_identifier),
    constraint ck_provider_payment_evidence_amount_non_negative
        check (minor_unit_amount >= 0 and minor_unit_amount = trunc(minor_unit_amount)),
    constraint fk_provider_payment_evidence_merchant
        foreign key (merchant_identifier)
        references merchant_account (merchant_identifier)
);

create index provider_payment_evidence_transaction_idx
    on provider_payment_evidence (
        merchant_identifier,
        provider_identifier,
        provider_transaction_reference,
        observed_at
    );

create table payment_application (
    application_identifier text primary key,
    merchant_identifier text not null,
    obligation_identifier text not null,
    payment_evidence_identifier text not null,
    currency_identifier text not null,
    minor_unit_amount numeric not null,
    applied_at timestamptz not null,
    provenance_reference text not null,
    constraint ck_payment_application_amount_non_negative
        check (minor_unit_amount >= 0 and minor_unit_amount = trunc(minor_unit_amount)),
    constraint fk_payment_application_obligation
        foreign key (merchant_identifier, obligation_identifier)
        references payment_obligation (merchant_identifier, obligation_identifier),
    constraint fk_payment_application_evidence
        foreign key (merchant_identifier, payment_evidence_identifier)
        references provider_payment_evidence (merchant_identifier, evidence_identifier)
);

create index payment_application_obligation_idx
    on payment_application (merchant_identifier, obligation_identifier, applied_at, application_identifier);

create index payment_application_evidence_idx
    on payment_application (merchant_identifier, payment_evidence_identifier, applied_at, application_identifier);
