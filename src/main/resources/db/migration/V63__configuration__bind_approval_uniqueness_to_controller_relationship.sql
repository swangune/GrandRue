-- Authority: approved MS-PROT-040 v1.6.
--
-- One Identity may later regain Merchant Controller authority through a
-- different Controller Relationship. Historical approval under the old
-- relationship must neither revive nor prevent a new exact approval under
-- the new current relationship.
--
-- V40 keyed the exact approval uniqueness partly by approving principal.
-- Add exact Controller Relationship affinity to that uniqueness boundary.

alter table configuration_revision_approval
    drop constraint uk_configuration_approval_exact_fact;

alter table configuration_revision_approval
    add constraint uk_configuration_approval_exact_fact
        unique (
            merchant_identifier,
            configuration_revision_identifier,
            validation_evidence_identifier,
            impact_review_evidence_identifier,
            approving_principal_identifier,
            controller_relationship_identifier
        );