-- MS-PROT-026 v1.1, designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md,
-- §17 — Per-Reaction Acknowledgement; §23 — Reaction Deduplication; §32 — Historical Affinity.
-- Release is pinned evidence, not a new logical responsibility on replay.
create table merchant_event_reaction (
    event_identity text not null check (btrim(event_identity) <> ''),
    reaction_owner_identifier text not null check (btrim(reaction_owner_identifier) <> ''),
    reaction_contract_identifier text not null check (btrim(reaction_contract_identifier) <> ''),
    reaction_semantic_release text not null check (btrim(reaction_semantic_release) <> ''),
    source_owner_identifier text not null check (btrim(source_owner_identifier) <> ''),
    source_contract_identifier text not null check (btrim(source_contract_identifier) <> ''),
    source_semantic_release text not null check (btrim(source_semantic_release) <> ''),
    merchant_identifier text not null references merchant_account (merchant_identifier),
    downstream_intent_reference text not null check (btrim(downstream_intent_reference) <> ''),
    accepted_at timestamptz not null,
    outcome_reference text,
    acknowledged_at timestamptz,
    primary key (event_identity, reaction_owner_identifier, reaction_contract_identifier),
    check ((outcome_reference is null and acknowledged_at is null)
        or (outcome_reference is not null and btrim(outcome_reference) <> '' and acknowledged_at is not null))
);

create index merchant_event_reaction_pending on merchant_event_reaction
    (reaction_owner_identifier, reaction_contract_identifier, reaction_semantic_release, accepted_at, event_identity)
    where acknowledged_at is null;
