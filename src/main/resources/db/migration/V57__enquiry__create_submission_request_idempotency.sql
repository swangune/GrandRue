-- MS-PROT-043 v1.4 / MS-PROT-059: one durable result per logical submission request.
-- Immutable Enquiry evidence supplies exact intent comparison without a second copy of contact data.
create table enquiry_submission_application_request (
    application_request_identity text not null check (btrim(application_request_identity) <> ''),
    merchant_identifier text not null check (btrim(merchant_identifier) <> ''),
    enquiry_identity text not null check (btrim(enquiry_identity) <> ''),
    primary key (application_request_identity),
    unique (merchant_identifier, enquiry_identity),
    foreign key (merchant_identifier, enquiry_identity)
        references enquiry_submission (merchant_identifier, enquiry_identity)
);
