-- Authority: MS-PROT-040 v1.1,
-- designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md,
-- §3 Configuration Revision identity and immutability.
-- Binding authority: designs/MS-PROT-048 v1.2 — Merchant Fulfilment Binding Set Revision & Activation Affinity Amendment.md,
-- §4 Exact revision reference.
-- Replacement rows can carry bindings; SQL UNKNOWN must not admit half a reference.
alter table merchant_configuration_revision
    drop constraint ck_configuration_revision_binding_pair,
    add constraint ck_configuration_revision_binding_pair check (
        (fulfilment_binding_set_identifier is null and fulfilment_binding_set_revision is null)
        or (
            fulfilment_binding_set_identifier is not null
            and fulfilment_binding_set_revision is not null
            and btrim(fulfilment_binding_set_identifier) <> ''
            and fulfilment_binding_set_revision > 0
        )
    );
