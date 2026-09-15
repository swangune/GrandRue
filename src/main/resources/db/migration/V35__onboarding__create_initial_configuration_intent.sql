-- MS-PROT-052 v1.2: immutable, exact-review-affined Initial Configuration
-- Intent and atomic Onboarding Case submission evidence.

alter table onboarding_case_revision
    drop constraint ck_onboarding_case_mutation_kind;

alter table onboarding_case_revision
    add constraint ck_onboarding_case_mutation_kind
        check (mutation_kind in ('START', 'ANSWER', 'SUBMIT'));

create table initial_configuration_intent (
    intent_identity text primary key,
    submission_request_identifier text not null unique,
    merchant_identifier text not null,
    source_onboarding_case_identity text not null unique,
    source_onboarding_case_revision text not null,
    submitted_case_revision text not null,
    submitted_by text not null,
    origin_identifier text,
    submitted_at timestamptz not null,

    constraint fk_initial_intent_source_revision
        foreign key (
            source_onboarding_case_identity,
            source_onboarding_case_revision
        ) references onboarding_case_revision (
            onboarding_case_identity,
            revision_identity
        ),
    constraint fk_initial_intent_submitted_revision
        foreign key (
            source_onboarding_case_identity,
            submitted_case_revision
        ) references onboarding_case_revision (
            onboarding_case_identity,
            revision_identity
        ),
    constraint ck_initial_intent_identity
        check (btrim(intent_identity) <> ''),
    constraint ck_initial_intent_request
        check (btrim(submission_request_identifier) <> ''),
    constraint ck_initial_intent_merchant
        check (btrim(merchant_identifier) <> ''),
    constraint ck_initial_intent_source_case
        check (btrim(source_onboarding_case_identity) <> ''),
    constraint ck_initial_intent_source_revision
        check (btrim(source_onboarding_case_revision) <> ''),
    constraint ck_initial_intent_submitted_revision
        check (btrim(submitted_case_revision) <> ''),
    constraint ck_initial_intent_revision_advance
        check (
            source_onboarding_case_revision <> submitted_case_revision
        ),
    constraint ck_initial_intent_submitted_by
        check (btrim(submitted_by) <> ''),
    constraint ck_initial_intent_origin
        check (
            origin_identifier is null
            or btrim(origin_identifier) <> ''
        )
);

create table initial_configuration_intent_semantic_seed (
    intent_identity text not null
        references initial_configuration_intent(intent_identity),
    seed_namespace text not null,
    seed_identifier text not null,

    constraint pk_initial_intent_semantic_seed
        primary key (intent_identity, seed_namespace, seed_identifier),
    constraint ck_initial_intent_seed_namespace
        check (btrim(seed_namespace) <> ''),
    constraint ck_initial_intent_seed_identifier
        check (btrim(seed_identifier) <> '')
);

create table initial_configuration_intent_provenance (
    intent_identity text not null
        references initial_configuration_intent(intent_identity),
    provenance_sequence integer not null,
    provenance_reference text not null,

    constraint pk_initial_intent_provenance
        primary key (intent_identity, provenance_sequence),
    constraint uk_initial_intent_provenance_reference
        unique (intent_identity, provenance_reference),
    constraint ck_initial_intent_provenance_sequence
        check (provenance_sequence >= 0),
    constraint ck_initial_intent_provenance_reference
        check (btrim(provenance_reference) <> '')
);

create table initial_configuration_intent_unresolved_prompt (
    intent_identity text not null
        references initial_configuration_intent(intent_identity),
    prompt_sequence integer not null,
    question_namespace text not null,
    question_identifier text not null,
    context_scope_reference text,

    constraint pk_initial_intent_unresolved_prompt
        primary key (intent_identity, prompt_sequence),
    constraint ck_initial_intent_prompt_sequence
        check (prompt_sequence >= 0),
    constraint ck_initial_intent_question_namespace
        check (btrim(question_namespace) <> ''),
    constraint ck_initial_intent_question_identifier
        check (btrim(question_identifier) <> ''),
    constraint ck_initial_intent_context
        check (
            context_scope_reference is null
            or btrim(context_scope_reference) <> ''
        )
);

create unique index uk_initial_intent_unresolved_prompt
    on initial_configuration_intent_unresolved_prompt (
        intent_identity,
        question_namespace,
        question_identifier,
        context_scope_reference
    ) nulls not distinct;
