create table merchant_account_establishment_request (
    logical_establishment_request_identity text primary key,
    merchant_identifier text not null,
    initial_controller_relationship_identifier text not null,
    constraint fk_merchant_account_establishment_bootstrap
        foreign key (
            merchant_identifier,
            initial_controller_relationship_identifier
        )
        references merchant_controller_relationship (
            merchant_identifier,
            controller_relationship_identifier
        )
);
