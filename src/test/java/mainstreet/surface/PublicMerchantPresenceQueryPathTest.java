package mainstreet.surface;

import mainstreet.api.ApiContractIdentity;
import mainstreet.api.ApiContractKind;
import mainstreet.api.ApiContractRegistration;
import mainstreet.api.ApiOwnerContractReference;
import mainstreet.api.ApiQueryContractDefinition;
import mainstreet.api.ApiSurfaceClass;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicMerchantPresenceQueryPathTest {

    @Test
    void defines_first_public_merchant_presence_query_from_accepted_projection_identity() {
        ApiQueryContractDefinition definition =
                PublicMerchantPresenceQueryPath.definition();

        assertEquals(
                new ApiContractIdentity("platform", "public-merchant-presence"),
                definition.registration().identity()
        );
        assertEquals(ApiSurfaceClass.PUBLIC, definition.registration().surface());
        assertEquals(ApiContractKind.QUERY, definition.registration().kind());
        assertEquals(
                new ApiOwnerContractReference("platform", "merchant-presence"),
                definition.registration().ownerContractReference()
        );
        assertEquals(
                "surface/established-observation-request-merchant-scope",
                definition.registration().scopeEstablishmentRuleReference()
        );
        assertEquals(
                "surface/public-audience-observation-context",
                definition.audienceObservationContextReference()
        );
        assertEquals(Optional.empty(), definition.relationshipRequirementReference());
        assertEquals(
                "platform/merchant-presence-truthful-serviceability",
                definition.projectionServiceabilityRequirementReference()
        );
        assertEquals(
                Set.of("exposure/current-observation-restrictions"),
                definition.exposureContractReferences()
        );
        assertEquals(
                "platform/public-merchant-presence-no-client-filter-or-sort",
                definition.filterSortContractReference()
        );
        assertEquals(
                "platform/public-merchant-presence-single-bounded-read",
                definition.paginationBoundRuleReference()
        );
        assertEquals(
                "surface/public-customer-projection-assembly",
                definition.safeResponseRepresentationReference()
        );
        assertEquals(
                "platform/merchant-presence-reduced-or-unserviceable",
                definition.unavailableRepresentationReference()
        );
    }

    @Test
    void binds_the_exact_s2_assembly_without_reread_or_transport_remapping() {
        PublicCustomerProjectionAssembly assembly = assembly();

        PublicMerchantPresenceBoundedQuery boundedQuery =
                new PublicMerchantPresenceQueryPath().bind(assembly);

        assertEquals(
                PublicMerchantPresenceQueryPath.definition(),
                boundedQuery.definition()
        );
        assertSame(assembly, boundedQuery.assembly());
        assertSame(
                assembly.boundedProjectionReadBinding(),
                boundedQuery.assembly().boundedProjectionReadBinding()
        );
    }

    @Test
    void query_definition_rejects_non_query_registration() {
        ApiQueryContractDefinition definition =
                PublicMerchantPresenceQueryPath.definition();

        assertThrows(
                IllegalArgumentException.class,
                () -> copyWithRegistration(
                        definition,
                        new ApiContractRegistration(
                                definition.registration().identity(),
                                definition.registration().surface(),
                                ApiContractKind.COMMAND,
                                definition.registration().ownerContractReference(),
                                definition.registration()
                                        .scopeEstablishmentRuleReference()
                        )
                )
        );
    }

    @Test
    void bounded_handoff_rejects_query_identity_or_projection_owner_laundering() {
        ApiQueryContractDefinition definition =
                PublicMerchantPresenceQueryPath.definition();

        ApiQueryContractDefinition wrongQuery = copyWithRegistration(
                definition,
                new ApiContractRegistration(
                        new ApiContractIdentity("platform", "other-query"),
                        ApiSurfaceClass.PUBLIC,
                        ApiContractKind.QUERY,
                        definition.registration().ownerContractReference(),
                        definition.registration().scopeEstablishmentRuleReference()
                )
        );
        ApiQueryContractDefinition wrongOwner = copyWithRegistration(
                definition,
                new ApiContractRegistration(
                        definition.registration().identity(),
                        ApiSurfaceClass.PUBLIC,
                        ApiContractKind.QUERY,
                        new ApiOwnerContractReference("profile", "descriptor"),
                        definition.registration().scopeEstablishmentRuleReference()
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new PublicMerchantPresenceBoundedQuery(wrongQuery, assembly())
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new PublicMerchantPresenceBoundedQuery(wrongOwner, assembly())
        );
    }

    @Test
    void query_definition_defensively_copies_exposure_contract_membership() {
        ApiQueryContractDefinition baseline =
                PublicMerchantPresenceQueryPath.definition();
        Set<String> mutable = new HashSet<>(baseline.exposureContractReferences());

        ApiQueryContractDefinition definition = new ApiQueryContractDefinition(
                baseline.registration(),
                baseline.audienceObservationContextReference(),
                baseline.relationshipRequirementReference(),
                baseline.projectionServiceabilityRequirementReference(),
                mutable,
                baseline.filterSortContractReference(),
                baseline.paginationBoundRuleReference(),
                baseline.safeResponseRepresentationReference(),
                baseline.unavailableRepresentationReference()
        );
        mutable.add("exposure/forged-later-membership");

        assertEquals(
                Set.of("exposure/current-observation-restrictions"),
                definition.exposureContractReferences()
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> definition.exposureContractReferences().add("exposure/forged")
        );
    }

    @Test
    void query_path_has_no_repository_provider_capability_payload_or_continuation_dependency() {
        assertNoForbiddenDependency(PublicMerchantPresenceQueryPath.class);
        assertNoForbiddenDependency(PublicMerchantPresenceBoundedQuery.class);

        assertTrue(
                PublicMerchantPresenceBoundedQuery.class.isRecord(),
                "bounded query handoff must remain immutable"
        );
    }

    private static ApiQueryContractDefinition copyWithRegistration(
            ApiQueryContractDefinition definition,
            ApiContractRegistration registration
    ) {
        return new ApiQueryContractDefinition(
                registration,
                definition.audienceObservationContextReference(),
                definition.relationshipRequirementReference(),
                definition.projectionServiceabilityRequirementReference(),
                definition.exposureContractReferences(),
                definition.filterSortContractReference(),
                definition.paginationBoundRuleReference(),
                definition.safeResponseRepresentationReference(),
                definition.unavailableRepresentationReference()
        );
    }

    private static PublicCustomerProjectionAssembly assembly() {
        return new PublicCustomerProjectionAssembly(
                new RuntimeBoundedProjectionReadBinding(),
                List.of()
        );
    }

    private static void assertNoForbiddenDependency(Class<?> type) {
        for (Field field : type.getDeclaredFields()) {
            assertAllowed(field.getType().getName());
        }
        for (Method method : type.getDeclaredMethods()) {
            assertAllowed(method.getReturnType().getName());
            for (Class<?> parameterType : method.getParameterTypes()) {
                assertAllowed(parameterType.getName());
            }
        }
    }

    private static void assertAllowed(String className) {
        String type = className.toLowerCase();
        assertFalse(type.contains("repository"));
        assertFalse(type.contains("provider"));
        assertFalse(type.contains("capability"));
        assertFalse(type.contains("continuation"));
        assertFalse(type.contains("jsonnode"));
        assertFalse(type.contains("java.util.map"));
        assertFalse(type.contains("merchantpublicdescriptor"));
    }
}
