-- Preserve unknown historical affinity; do not reinterpret pre-migration events.
alter table merchant_account_establishment_publication_intent
    add column event_owner_identifier text,
    add column event_contract_identifier text,
    add column event_semantic_release text,
    add constraint merchant_establishment_event_affinity_complete check (
        (event_owner_identifier is null and event_contract_identifier is null and event_semantic_release is null)
        or
        (event_owner_identifier is not null and event_contract_identifier is not null and event_semantic_release is not null
         and length(btrim(event_owner_identifier)) > 0
         and length(btrim(event_contract_identifier)) > 0
         and length(btrim(event_semantic_release)) > 0)
    );
