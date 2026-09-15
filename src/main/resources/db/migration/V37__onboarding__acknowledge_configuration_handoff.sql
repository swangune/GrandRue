-- MS-PROT-052 v1.2: configuration-bootstrap acceptance completes the source
-- Onboarding Case through an exact append-only revision.

alter table onboarding_case_revision
    drop constraint ck_onboarding_case_mutation_kind;

alter table onboarding_case_revision
    add constraint ck_onboarding_case_mutation_kind
        check (mutation_kind in (
            'START',
            'ANSWER',
            'SUBMIT',
            'COMPLETE'
        ));

alter table merchant_configuration_revision
    add column onboarding_completion_revision text not null,
    add constraint ck_configuration_onboarding_completion_revision
        check (btrim(onboarding_completion_revision) <> '');
