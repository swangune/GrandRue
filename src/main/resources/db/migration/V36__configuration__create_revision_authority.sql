-- MS-PROT-040 v1.1/v1.2: immutable Merchant Configuration Revision
-- authority and exact Initial Configuration Intent source affinity.

alter table initial_configuration_intent
    add constraint uk_initial_intent_configuration_handoff
        unique (
            intent_identity,
            merchant_identifier,
            source_onboarding_case_identity,
            source_onboarding_case_revision
        );

create table merchant_configuration_revision (
    merchant_identifier text not null
        references merchant_account(merchant_identifier),
    configuration_revision_identifier text not null,
    configuration_version bigint not null,
    semantic_registry_release_identifier text not null,
    base_configuration_revision_identifier text,
    fulfilment_binding_set_identifier text,
    fulfilment_binding_set_revision bigint,
    source_initial_configuration_intent_identity text not null unique,
    source_onboarding_case_identity text not null,
    source_onboarding_case_revision text not null,
    materialised_by text not null,
    origin_identifier text,
    materialised_at timestamptz not null,

    constraint pk_merchant_configuration_revision
        primary key (
            merchant_identifier,
            configuration_revision_identifier
        ),
    constraint uk_merchant_configuration_version
        unique (merchant_identifier, configuration_version),
    constraint fk_configuration_revision_initial_intent
        foreign key (
            source_initial_configuration_intent_identity,
            merchant_identifier,
            source_onboarding_case_identity,
            source_onboarding_case_revision
        ) references initial_configuration_intent (
            intent_identity,
            merchant_identifier,
            source_onboarding_case_identity,
            source_onboarding_case_revision
        ),
    constraint fk_configuration_revision_base
        foreign key (
            merchant_identifier,
            base_configuration_revision_identifier
        ) references merchant_configuration_revision (
            merchant_identifier,
            configuration_revision_identifier
        ),
    constraint ck_configuration_revision_identity
        check (btrim(configuration_revision_identifier) <> ''),
    constraint ck_configuration_revision_version
        check (configuration_version > 0),
    constraint ck_configuration_revision_semantic_release
        check (btrim(semantic_registry_release_identifier) <> ''),
    constraint ck_configuration_revision_not_own_base
        check (
            base_configuration_revision_identifier is null
            or base_configuration_revision_identifier
                <> configuration_revision_identifier
        ),
    constraint ck_configuration_revision_binding_pair
        check (
            (
                fulfilment_binding_set_identifier is null
                and fulfilment_binding_set_revision is null
            )
            or (
                btrim(fulfilment_binding_set_identifier) <> ''
                and fulfilment_binding_set_revision > 0
            )
        ),
    constraint ck_configuration_revision_materialised_by
        check (btrim(materialised_by) <> ''),
    constraint ck_configuration_revision_origin
        check (
            origin_identifier is null
            or btrim(origin_identifier) <> ''
        ),
    constraint ck_initial_configuration_revision_shape
        check (
            configuration_version = 1
            and base_configuration_revision_identifier is null
            and fulfilment_binding_set_identifier is null
            and fulfilment_binding_set_revision is null
        )
);

create table merchant_configuration_revision_capability (
    merchant_identifier text not null,
    configuration_revision_identifier text not null,
    capability_identifier text not null,

    constraint pk_merchant_configuration_revision_capability
        primary key (
            merchant_identifier,
            configuration_revision_identifier,
            capability_identifier
        ),
    constraint fk_configuration_capability_revision
        foreign key (
            merchant_identifier,
            configuration_revision_identifier
        ) references merchant_configuration_revision (
            merchant_identifier,
            configuration_revision_identifier
        ),
    constraint ck_configuration_capability_identifier
        check (btrim(capability_identifier) <> '')
);

create table merchant_configuration_revision_policy (
    merchant_identifier text not null,
    configuration_revision_identifier text not null,
    owner_capability_identifier text not null,
    policy_identifier text not null,
    selected_value text not null,

    constraint pk_merchant_configuration_revision_policy
        primary key (
            merchant_identifier,
            configuration_revision_identifier,
            owner_capability_identifier,
            policy_identifier
        ),
    constraint fk_configuration_policy_revision
        foreign key (
            merchant_identifier,
            configuration_revision_identifier
        ) references merchant_configuration_revision (
            merchant_identifier,
            configuration_revision_identifier
        ),
    constraint ck_configuration_policy_owner
        check (btrim(owner_capability_identifier) <> ''),
    constraint ck_configuration_policy_identifier
        check (btrim(policy_identifier) <> ''),
    constraint ck_configuration_policy_value
        check (btrim(selected_value) <> '')
);
