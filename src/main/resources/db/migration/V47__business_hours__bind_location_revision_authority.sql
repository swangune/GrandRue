-- MS-PROT-050 v1.4 + MS-PROT-051 v1.2: exact Location-affined hours evidence.

alter table merchant_location_revision
    add constraint uq_merchant_location_revision_identity_scope
        unique (merchant_identifier, location_identifier, revision_identifier);

alter table standard_business_hours_revision
    add column merchant_location_revision_identifier text;

alter table standard_business_hours_revision
    add constraint fk_standard_business_hours_location_revision
        foreign key (
            merchant_identifier,
            merchant_location_identifier,
            merchant_location_revision_identifier
        ) references merchant_location_revision (
            merchant_identifier,
            location_identifier,
            revision_identifier
        );

alter table standard_business_hours_revision
    add constraint ck_standard_business_hours_location_revision_shape
        check (
            (
                scope_kind = 'MERCHANT'
                and merchant_location_revision_identifier is null
            )
            or
            (
                scope_kind = 'MERCHANT_LOCATION'
                and merchant_location_revision_identifier is not null
            )
        );
