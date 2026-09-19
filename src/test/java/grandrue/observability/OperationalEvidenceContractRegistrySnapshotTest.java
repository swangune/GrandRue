package grandrue.observability;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationalEvidenceContractRegistrySnapshotTest {

    private static final OperationalEvidenceContractIdentity IDENTITY =
            new OperationalEvidenceContractIdentity(
                    "background",
                    "durable-work-progression"
            );

    @Test
    void registry_resolves_only_the_exact_release_and_identity() {
        OperationalEvidenceContractDefinition definition =
                definition(IDENTITY);

        OperationalEvidenceContractRegistrySnapshot registry =
                new OperationalEvidenceContractRegistrySnapshot(
                        "operational-evidence@1",
                        List.of(definition)
                );

        assertEquals(
                Optional.of(definition),
                registry.contract(
                        "operational-evidence@1",
                        IDENTITY
                )
        );
        assertTrue(
                registry.contract(
                        "operational-evidence@2",
                        IDENTITY
                ).isEmpty()
        );
    }

    @Test
    void duplicate_contract_identity_is_rejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OperationalEvidenceContractRegistrySnapshot(
                        "operational-evidence@1",
                        List.of(
                                definition(IDENTITY),
                                definition(IDENTITY)
                        )
                )
        );
    }

    @Test
    void contract_requires_explicit_evidence_correlation_and_access_scope() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OperationalEvidenceContractDefinition(
                        IDENTITY,
                        "background/durable-work",
                        "MS-PROT-068-v1.1/8:background-work-evidence",
                        Set.of(),
                        Set.of(
                                OperationalCorrelationReference
                                        .DURABLE_WORK_INSTRUCTION_IDENTITY
                        ),
                        Optional.empty(),
                        "MS-PROT-068-v1.1/17:source-timestamp",
                        "MS-PROT-068-v1.1/8:outstanding-work",
                        Optional.empty(),
                        "MS-PROT-068-v1.1/22:bounded-identifiers",
                        "MS-PROT-053:purpose-scoped-operational-evidence",
                        Set.of("platform-operations")
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new OperationalEvidenceContractDefinition(
                        IDENTITY,
                        "background/durable-work",
                        "MS-PROT-068-v1.1/8:background-work-evidence",
                        Set.of(OperationalEvidenceFamily.OPERATIONAL_METRIC),
                        Set.of(),
                        Optional.empty(),
                        "MS-PROT-068-v1.1/17:source-timestamp",
                        "MS-PROT-068-v1.1/8:outstanding-work",
                        Optional.empty(),
                        "MS-PROT-068-v1.1/22:bounded-identifiers",
                        "MS-PROT-053:purpose-scoped-operational-evidence",
                        Set.of("platform-operations")
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new OperationalEvidenceContractDefinition(
                        IDENTITY,
                        "background/durable-work",
                        "MS-PROT-068-v1.1/8:background-work-evidence",
                        Set.of(OperationalEvidenceFamily.OPERATIONAL_METRIC),
                        Set.of(
                                OperationalCorrelationReference
                                        .DURABLE_WORK_INSTRUCTION_IDENTITY
                        ),
                        Optional.empty(),
                        "MS-PROT-068-v1.1/17:source-timestamp",
                        "MS-PROT-068-v1.1/8:outstanding-work",
                        Optional.empty(),
                        "MS-PROT-068-v1.1/22:bounded-identifiers",
                        "MS-PROT-053:purpose-scoped-operational-evidence",
                        Set.of()
                )
        );
    }

    private static OperationalEvidenceContractDefinition definition(
            OperationalEvidenceContractIdentity identity
    ) {
        return new OperationalEvidenceContractDefinition(
                identity,
                "background/durable-work",
                "MS-PROT-068-v1.1/8:background-work-evidence",
                Set.of(OperationalEvidenceFamily.OPERATIONAL_METRIC),
                Set.of(
                        OperationalCorrelationReference
                                .DURABLE_WORK_INSTRUCTION_IDENTITY,
                        OperationalCorrelationReference
                                .WORK_ATTEMPT_IDENTITY
                ),
                Optional.empty(),
                "MS-PROT-068-v1.1/17:source-timestamp",
                "MS-PROT-068-v1.1/8:outstanding-work",
                Optional.of(
                        "MS-PROT-068-v1.1/19:request-authorised-attention-only"
                ),
                "MS-PROT-068-v1.1/22:bounded-identifiers",
                "MS-PROT-053:purpose-scoped-operational-evidence",
                Set.of("platform-operations")
        );
    }
}
