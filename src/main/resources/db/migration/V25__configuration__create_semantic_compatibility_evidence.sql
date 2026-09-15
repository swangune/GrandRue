create table semantic_compatibility_evidence (
    merchant_identifier text not null,
    source_release_identifier text not null,
    target_release_identifier text not null,
    source_semantic_registry_version text not null,
    target_semantic_registry_version text not null,
    reference_scope text[] not null,
    disposition text not null,
    migration_evidence_identifier text,
    decision_evidence_identifier text,
    primary key (
        merchant_identifier,
        source_release_identifier,
        target_release_identifier
    ),
    constraint ck_semantic_compatibility_distinct_releases
        check (source_release_identifier <> target_release_identifier),
    constraint ck_semantic_compatibility_registry_transition
        check (source_semantic_registry_version <> target_semantic_registry_version),
    constraint ck_semantic_compatibility_disposition
        check (disposition in (
            'UNAFFECTED',
            'SEMANTICALLY_EQUIVALENT',
            'MIGRATABLE_PRESERVING_INTENT',
            'MIGRATION_REQUIRES_DECISION',
            'INCOMPATIBLE'
        )),
    constraint ck_semantic_compatibility_migration_evidence
        check (
            disposition <> 'MIGRATABLE_PRESERVING_INTENT'
            or migration_evidence_identifier is not null
        ),
    constraint ck_semantic_compatibility_decision_evidence
        check (
            disposition <> 'MIGRATION_REQUIRES_DECISION'
            or (
                migration_evidence_identifier is not null
                and decision_evidence_identifier is not null
            )
        )
);

create index semantic_compatibility_evidence_target_idx
    on semantic_compatibility_evidence (
        merchant_identifier,
        target_release_identifier,
        source_release_identifier
    );
