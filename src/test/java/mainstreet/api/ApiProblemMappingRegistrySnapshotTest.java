package mainstreet.api;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApiProblemMappingRegistrySnapshotTest {

    private static final ApiOwnerContractReference ORDER_CREATE =
            new ApiOwnerContractReference("ordering", "order-create");

    @Test
    void maps_only_through_the_exact_rule_owner_and_evidence_contract() {
        TestAuthority authority = new TestAuthority(
                "public-order-create-rejection",
                ORDER_CREATE,
                new ApiProblem(
                        ApiProblemCategory.CONFLICT,
                        Optional.of(new ApiOwnerProblemDetail(
                                ORDER_CREATE,
                                "order-no-longer-amendable"
                        ))
                )
        );
        ApiProblemMappingRegistrySnapshot registry =
                new ApiProblemMappingRegistrySnapshot(List.of(authority));

        ApiProblem problem = registry.map(
                registration(ORDER_CREATE),
                "public-order-create-rejection",
                new OrderRejectionEvidence("ORDER_NO_LONGER_AMENDABLE")
        );

        assertEquals(ApiProblemCategory.CONFLICT, problem.category());
        assertEquals(
                "order-no-longer-amendable",
                problem.ownerDetail().orElseThrow().safeCode()
        );
        assertEquals(1, authority.calls);
    }

    @Test
    void permits_security_safe_collapse_without_exposing_owner_detail() {
        ApiProblemMappingRegistrySnapshot registry =
                new ApiProblemMappingRegistrySnapshot(List.of(new TestAuthority(
                        "protected-order-lookup-rejection",
                        ORDER_CREATE,
                        new ApiProblem(
                                ApiProblemCategory.NOT_FOUND_OR_NOT_ACCESSIBLE,
                                Optional.empty()
                        )
                )));

        ApiProblem problem = registry.map(
                registration(ORDER_CREATE),
                "protected-order-lookup-rejection",
                new OrderRejectionEvidence("EXISTS_BUT_NOT_ACCESSIBLE")
        );

        assertEquals(
                ApiProblemCategory.NOT_FOUND_OR_NOT_ACCESSIBLE,
                problem.category()
        );
        assertEquals(Optional.empty(), problem.ownerDetail());
    }

    @Test
    void rejects_duplicate_missing_wrong_owner_and_wrong_evidence_mappings() {
        TestAuthority authority = new TestAuthority(
                "public-order-create-rejection",
                ORDER_CREATE,
                new ApiProblem(ApiProblemCategory.CONFLICT, Optional.empty())
        );
        ApiProblemMappingRegistrySnapshot registry =
                new ApiProblemMappingRegistrySnapshot(List.of(authority));

        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiProblemMappingRegistrySnapshot(List.of(
                        authority,
                        new TestAuthority(
                                "public-order-create-rejection",
                                ORDER_CREATE,
                                new ApiProblem(
                                        ApiProblemCategory.INVALID_REQUEST,
                                        Optional.empty()
                                )
                        )
                ))
        );
        assertThrows(
                IllegalStateException.class,
                () -> registry.map(
                        registration(ORDER_CREATE),
                        "missing-rule",
                        new OrderRejectionEvidence("CONFLICT")
                )
        );
        assertThrows(
                IllegalStateException.class,
                () -> registry.map(
                        registration(new ApiOwnerContractReference(
                                "scheduling",
                                "appointment-create"
                        )),
                        "public-order-create-rejection",
                        new OrderRejectionEvidence("CONFLICT")
                )
        );
        assertThrows(
                IllegalStateException.class,
                () -> registry.map(
                        registration(ORDER_CREATE),
                        "public-order-create-rejection",
                        new OtherEvidence("CONFLICT")
                )
        );
    }

    @Test
    void rejects_internal_exceptions_null_results_and_foreign_owner_detail() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiProblemMappingRegistrySnapshot(List.of(
                        new ThrowableAuthority()
                ))
        );

        ApiProblemMappingRegistrySnapshot nullResultRegistry =
                new ApiProblemMappingRegistrySnapshot(List.of(new TestAuthority(
                        "null-result",
                        ORDER_CREATE,
                        null
                )));
        assertThrows(
                IllegalStateException.class,
                () -> nullResultRegistry.map(
                        registration(ORDER_CREATE),
                        "null-result",
                        new OrderRejectionEvidence("CONFLICT")
                )
        );

        ApiProblemMappingRegistrySnapshot foreignDetailRegistry =
                new ApiProblemMappingRegistrySnapshot(List.of(new TestAuthority(
                        "foreign-detail",
                        ORDER_CREATE,
                        new ApiProblem(
                                ApiProblemCategory.CONFLICT,
                                Optional.of(new ApiOwnerProblemDetail(
                                        new ApiOwnerContractReference(
                                                "inventory",
                                                "stock-claim"
                                        ),
                                        "insufficient-quantity"
                                ))
                        )
                )));
        assertThrows(
                IllegalStateException.class,
                () -> foreignDetailRegistry.map(
                        registration(ORDER_CREATE),
                        "foreign-detail",
                        new OrderRejectionEvidence("CONFLICT")
                )
        );
    }

    private static ApiContractRegistration registration(
            ApiOwnerContractReference owner
    ) {
        return new ApiContractRegistration(
                new ApiContractIdentity(
                        owner.ownerIdentifier(),
                        "test-contract"
                ),
                ApiSurfaceClass.PUBLIC,
                ApiContractKind.COMMAND,
                owner,
                "trusted-public-merchant-scope"
        );
    }

    private record OrderRejectionEvidence(String category)
            implements ApiProblemEvidence {
    }

    private record OtherEvidence(String category) implements ApiProblemEvidence {
    }

    private static final class TestAuthority implements ApiProblemMappingAuthority {

        private final String ruleReference;
        private final ApiOwnerContractReference owner;
        private final ApiProblem result;
        private int calls;

        private TestAuthority(
                String ruleReference,
                ApiOwnerContractReference owner,
                ApiProblem result
        ) {
            this.ruleReference = ruleReference;
            this.owner = owner;
            this.result = result;
        }

        @Override
        public String mappingRuleReference() {
            return ruleReference;
        }

        @Override
        public ApiOwnerContractReference ownerContractReference() {
            return owner;
        }

        @Override
        public Class<? extends ApiProblemEvidence> evidenceType() {
            return OrderRejectionEvidence.class;
        }

        @Override
        public ApiProblem map(ApiProblemEvidence evidence) {
            calls++;
            return result;
        }
    }

    private static final class ThrowableAuthority
            implements ApiProblemMappingAuthority {

        @Override
        public String mappingRuleReference() {
            return "exception-mapping";
        }

        @Override
        public ApiOwnerContractReference ownerContractReference() {
            return ORDER_CREATE;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Class<? extends ApiProblemEvidence> evidenceType() {
            return (Class<? extends ApiProblemEvidence>) (Class<?>) InternalFailure.class;
        }

        @Override
        public ApiProblem map(ApiProblemEvidence evidence) {
            return new ApiProblem(ApiProblemCategory.INTERNAL_FAILURE, Optional.empty());
        }
    }

    private static final class InternalFailure extends RuntimeException
            implements ApiProblemEvidence {
    }
}
