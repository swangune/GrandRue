package mainstreet.api;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApiTransportScopeAuthorityRegistrySnapshotTest {

    @Test
    void resolves_only_through_exact_rule_and_surface_affined_authority() {
        TestAuthority authority = new TestAuthority(
                "trusted-public-storefront-route",
                ApiSurfaceClass.PUBLIC,
                new MerchantApiTransportScope(new MerchantScope("merchant-a"))
        );
        ApiTransportScopeAuthorityRegistrySnapshot registry =
                new ApiTransportScopeAuthorityRegistrySnapshot(List.of(authority));

        ApiTransportScope resolved = registry.establish(
                registration(ApiSurfaceClass.PUBLIC, "trusted-public-storefront-route"),
                new RouteEvidence("shop.example")
        );

        MerchantApiTransportScope merchant = assertInstanceOf(
                MerchantApiTransportScope.class,
                resolved
        );
        assertEquals("merchant-a", merchant.merchantScope().merchantIdentifier());
        assertEquals(1, authority.calls);
    }

    @Test
    void represents_platform_scope_without_fabricating_a_merchant() {
        PlatformApiTransportScope platform = new PlatformApiTransportScope(
                "identity-bootstrap"
        );
        ApiTransportScopeAuthorityRegistrySnapshot registry =
                new ApiTransportScopeAuthorityRegistrySnapshot(List.of(
                        new TestAuthority(
                                "trusted-bootstrap-origin",
                                ApiSurfaceClass.PLATFORM_IDENTITY_BOOTSTRAP,
                                platform
                        )
                ));

        ApiTransportScope resolved = registry.establish(
                registration(
                        ApiSurfaceClass.PLATFORM_IDENTITY_BOOTSTRAP,
                        "trusted-bootstrap-origin"
                ),
                new RouteEvidence("identity.example")
        );

        assertEquals(platform, resolved);
        assertInstanceOf(PlatformApiTransportScope.class, resolved);
    }

    @Test
    void rejects_missing_duplicate_surface_mismatched_and_wrong_evidence_authorities() {
        TestAuthority publicAuthority = new TestAuthority(
                "public-route",
                ApiSurfaceClass.PUBLIC,
                new MerchantApiTransportScope(new MerchantScope("merchant-a"))
        );
        ApiTransportScopeAuthorityRegistrySnapshot registry =
                new ApiTransportScopeAuthorityRegistrySnapshot(List.of(publicAuthority));

        assertThrows(
                IllegalStateException.class,
                () -> registry.establish(
                        registration(ApiSurfaceClass.PUBLIC, "missing-rule"),
                        new RouteEvidence("shop.example")
                )
        );
        assertThrows(
                IllegalStateException.class,
                () -> registry.establish(
                        registration(ApiSurfaceClass.MERCHANT_OPERATIONAL, "public-route"),
                        new RouteEvidence("shop.example")
                )
        );
        assertThrows(
                IllegalStateException.class,
                () -> registry.establish(
                        registration(ApiSurfaceClass.PUBLIC, "public-route"),
                        new SessionEvidence("opaque-session")
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiTransportScopeAuthorityRegistrySnapshot(List.of(
                        publicAuthority,
                        new TestAuthority(
                                "public-route",
                                ApiSurfaceClass.PUBLIC,
                                new MerchantApiTransportScope(new MerchantScope("merchant-b"))
                        )
                ))
        );
    }

    @Test
    void fails_closed_when_authority_returns_absent_or_surface_invalid_scope() {
        ApiTransportScopeAuthorityRegistrySnapshot nullResultRegistry =
                new ApiTransportScopeAuthorityRegistrySnapshot(List.of(
                        new TestAuthority("public-route", ApiSurfaceClass.PUBLIC, null)
                ));
        assertThrows(
                IllegalStateException.class,
                () -> nullResultRegistry.establish(
                        registration(ApiSurfaceClass.PUBLIC, "public-route"),
                        new RouteEvidence("shop.example")
                )
        );

        ApiTransportScopeAuthorityRegistrySnapshot fakeMerchantForPlatform =
                new ApiTransportScopeAuthorityRegistrySnapshot(List.of(
                        new TestAuthority(
                                "admin-scope",
                                ApiSurfaceClass.PLATFORM_ADMINISTRATIVE,
                                new MerchantApiTransportScope(new MerchantScope("fake-platform"))
                        )
                ));
        assertThrows(
                IllegalStateException.class,
                () -> fakeMerchantForPlatform.establish(
                        registration(ApiSurfaceClass.PLATFORM_ADMINISTRATIVE, "admin-scope"),
                        new RouteEvidence("admin.example")
                )
        );

        ApiTransportScopeAuthorityRegistrySnapshot platformForPublic =
                new ApiTransportScopeAuthorityRegistrySnapshot(List.of(
                        new TestAuthority(
                                "public-route",
                                ApiSurfaceClass.PUBLIC,
                                new PlatformApiTransportScope("not-public-merchant-scope")
                        )
                ));
        assertThrows(
                IllegalStateException.class,
                () -> platformForPublic.establish(
                        registration(ApiSurfaceClass.PUBLIC, "public-route"),
                        new RouteEvidence("shop.example")
                )
        );
    }

    private static ApiContractRegistration registration(
            ApiSurfaceClass surface,
            String scopeRule
    ) {
        return new ApiContractRegistration(
                new ApiContractIdentity("test-owner", "test-contract"),
                surface,
                ApiContractKind.COMMAND,
                new ApiOwnerContractReference("test-owner", "test-operation"),
                scopeRule
        );
    }

    private record RouteEvidence(String route) implements ApiTransportScopeEvidence {
    }

    private record SessionEvidence(String credential) implements ApiTransportScopeEvidence {
    }

    private static final class TestAuthority
            implements ApiTransportScopeEstablishmentAuthority {

        private final String ruleReference;
        private final ApiSurfaceClass surface;
        private final ApiTransportScope result;
        private int calls;

        private TestAuthority(
                String ruleReference,
                ApiSurfaceClass surface,
                ApiTransportScope result
        ) {
            this.ruleReference = ruleReference;
            this.surface = surface;
            this.result = result;
        }

        @Override
        public String ruleReference() {
            return ruleReference;
        }

        @Override
        public ApiSurfaceClass surface() {
            return surface;
        }

        @Override
        public Class<? extends ApiTransportScopeEvidence> evidenceType() {
            return RouteEvidence.class;
        }

        @Override
        public ApiTransportScope establish(ApiTransportScopeEvidence evidence) {
            assertInstanceOf(RouteEvidence.class, evidence);
            calls++;
            return result;
        }
    }
}
