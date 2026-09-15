create table merchant_controller_relationship (
    controller_relationship_identifier text primary key,
    merchant_identifier text not null
        references merchant_account (merchant_identifier),
    identity_identifier text not null,
    lifecycle text not null,
    constraint ck_merchant_controller_relationship_lifecycle
        check (lifecycle in ('ACTIVE', 'ENDED')),
    constraint uq_merchant_controller_relationship_merchant_relation
        unique (merchant_identifier, controller_relationship_identifier)
);

create unique index uq_merchant_controller_one_active
    on merchant_controller_relationship (merchant_identifier)
    where lifecycle = 'ACTIVE';
