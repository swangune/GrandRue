-- MS-PROT-052 v1.2: durable merchant-owned Onboarding Case and
-- immutable answer evidence. Evidence remains non-executable.

create table onboarding_case (
    onboarding_case_identity text primary key,
    merchant_identifier text not null,
    purpose text not null,
    lifecycle text not null,
    current_revision text not null,
    started_at timestamptz not null,

    constraint ck_onboarding_case_identity
        check (btrim(onboarding_case_identity) <> ''),
    constraint ck_onboarding_case_merchant
        check (btrim(merchant_identifier) <> ''),
    constraint ck_onboarding_case_purpose
        check (purpose in ('INITIAL_CONFIGURATION')),
    constraint ck_onboarding_case_lifecycle
        check (lifecycle in (
            'IN_PROGRESS',
            'SUBMITTED',
            'COMPLETED',
            'ABANDONED'
        )),
    constraint ck_onboarding_case_current_revision
        check (btrim(current_revision) <> '')
);

create unique index uk_onboarding_one_current_initial_case
    on onboarding_case (merchant_identifier)
    where purpose = 'INITIAL_CONFIGURATION'
      and lifecycle in ('IN_PROGRESS', 'SUBMITTED');

create table onboarding_start_request (
    start_request_identifier text primary key,
    requested_case_identity text not null,
    merchant_identifier text not null,
    initial_revision text not null,
    principal_reference text not null,
    origin_identifier text,
    started_at timestamptz not null,
    resolved_case_identity text not null
        references onboarding_case(onboarding_case_identity),

    constraint ck_onboarding_start_request
        check (btrim(start_request_identifier) <> ''),
    constraint ck_onboarding_start_requested_case
        check (btrim(requested_case_identity) <> ''),
    constraint ck_onboarding_start_merchant
        check (btrim(merchant_identifier) <> ''),
    constraint ck_onboarding_start_revision
        check (btrim(initial_revision) <> ''),
    constraint ck_onboarding_start_principal
        check (btrim(principal_reference) <> ''),
    constraint ck_onboarding_start_origin
        check (origin_identifier is null or btrim(origin_identifier) <> '')
);

create table onboarding_case_revision (
    onboarding_case_identity text not null
        references onboarding_case(onboarding_case_identity),
    revision_identity text not null,
    prior_revision_identity text,
    mutation_request_identifier text not null unique,
    mutation_kind text not null,
    resulting_lifecycle text not null,
    principal_reference text not null,
    origin_identifier text,
    committed_at timestamptz not null,

    constraint pk_onboarding_case_revision
        primary key (onboarding_case_identity, revision_identity),
    constraint ck_onboarding_case_revision_identity
        check (btrim(revision_identity) <> ''),
    constraint ck_onboarding_case_prior_revision
        check (
            prior_revision_identity is null
            or btrim(prior_revision_identity) <> ''
        ),
    constraint ck_onboarding_case_mutation_request
        check (btrim(mutation_request_identifier) <> ''),
    constraint ck_onboarding_case_mutation_kind
        check (mutation_kind in ('START', 'ANSWER')),
    constraint ck_onboarding_case_revision_lifecycle
        check (resulting_lifecycle in (
            'IN_PROGRESS',
            'SUBMITTED',
            'COMPLETED',
            'ABANDONED'
        )),
    constraint ck_onboarding_case_revision_principal
        check (btrim(principal_reference) <> ''),
    constraint ck_onboarding_case_revision_origin
        check (origin_identifier is null or btrim(origin_identifier) <> '')
);

create table onboarding_answer_evidence (
    answer_sequence bigint generated always as identity primary key,
    answer_evidence_identifier text not null unique,
    onboarding_case_identity text not null,
    revision_identity text not null,
    question_namespace text not null,
    question_identifier text not null,
    question_definition_version text not null,
    answer_form text not null,
    answer_option_identifiers text[] not null,
    structured_value_reference text,
    context_scope_reference text,
    answer_origin text not null,
    answered_by text not null,
    answered_at timestamptz not null,
    semantic_registry_release text,
    supersedes_answer_evidence_identifier text
        references onboarding_answer_evidence(answer_evidence_identifier),

    constraint fk_onboarding_answer_case_revision
        foreign key (onboarding_case_identity, revision_identity)
        references onboarding_case_revision(
            onboarding_case_identity,
            revision_identity
        ),
    constraint uk_onboarding_one_answer_per_case_revision
        unique (onboarding_case_identity, revision_identity),
    constraint ck_onboarding_answer_evidence_identity
        check (btrim(answer_evidence_identifier) <> ''),
    constraint ck_onboarding_answer_question_namespace
        check (btrim(question_namespace) <> ''),
    constraint ck_onboarding_answer_question_identifier
        check (btrim(question_identifier) <> ''),
    constraint ck_onboarding_answer_question_version
        check (btrim(question_definition_version) <> ''),
    constraint ck_onboarding_answer_form
        check (answer_form in (
            'SINGLE_SELECT',
            'MULTI_SELECT',
            'BOOLEAN',
            'QUANTITY',
            'DURATION',
            'TEXT',
            'STRUCTURED_VALUE'
        )),
    constraint ck_onboarding_answer_representation
        check (
            (cardinality(answer_option_identifiers) > 0)
            <>
            (structured_value_reference is not null)
        ),
    constraint ck_onboarding_answer_structured_reference
        check (
            structured_value_reference is null
            or btrim(structured_value_reference) <> ''
        ),
    constraint ck_onboarding_answer_context
        check (
            context_scope_reference is null
            or btrim(context_scope_reference) <> ''
        ),
    constraint ck_onboarding_answer_origin
        check (answer_origin in (
            'MERCHANT_SELECTED',
            'MERCHANT_APPROVED_IN_REVIEW',
            'INFERRED_PROPOSAL',
            'DERIVED',
            'DEFAULTED',
            'IMPORTED_EVIDENCE'
        )),
    constraint ck_onboarding_answer_answered_by
        check (btrim(answered_by) <> ''),
    constraint ck_onboarding_answer_semantic_release
        check (
            semantic_registry_release is null
            or btrim(semantic_registry_release) <> ''
        )
);

create table onboarding_effective_answer (
    effective_answer_pointer_identity bigint
        generated always as identity primary key,
    onboarding_case_identity text not null
        references onboarding_case(onboarding_case_identity),
    question_namespace text not null,
    question_identifier text not null,
    context_scope_reference text,
    answer_evidence_identifier text not null unique
        references onboarding_answer_evidence(answer_evidence_identifier)
);

create unique index uk_onboarding_effective_answer_key
    on onboarding_effective_answer (
        onboarding_case_identity,
        question_namespace,
        question_identifier,
        context_scope_reference
    ) nulls not distinct;
