-- Legacy instructions retain absent affinity: never infer registration from opaque provenance.
alter table durable_work_instruction
    add column contract_owner_identifier text,
    add column contract_identifier text,
    add column contract_semantic_release text,
    add constraint durable_work_contract_affinity_complete check (
        (contract_owner_identifier is null and contract_identifier is null and contract_semantic_release is null)
        or
        (contract_owner_identifier is not null and contract_identifier is not null and contract_semantic_release is not null
         and length(btrim(contract_owner_identifier)) > 0
         and length(btrim(contract_identifier)) > 0
         and length(btrim(contract_semantic_release)) > 0
         and contract_owner_identifier = owner_context_identifier)
    );
