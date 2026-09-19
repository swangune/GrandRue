package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.booking.BookingResidualObligationAuthority;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompositeResidualSurfaceObligationAuthorityTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");

    @Test
    void delegates_exactly_once_to_booking_owned_residual_authority() {
        AtomicInteger calls = new AtomicInteger();
        BookingResidualObligationAuthority bookingAuthority = scope -> {
            assertEquals(MERCHANT, scope);
            calls.incrementAndGet();
            return true;
        };

        CompositeResidualSurfaceObligationAuthority authority =
                new CompositeResidualSurfaceObligationAuthority(
                        registryWithResidualContribution("booking"),
                        List.of(new ResidualSurfaceObligationBinding(
                                "booking",
                                bookingAuthority::hasOutstandingBookingObligation
                        ))
                );

        assertTrue(authority.hasOutstandingObligations(MERCHANT, "booking"));
        assertEquals(1, calls.get());
    }

    @Test
    void rejects_duplicate_capability_bindings_during_assembly() {
        ResidualSurfaceObligationBinding first =
                new ResidualSurfaceObligationBinding("booking", scope -> true);
        ResidualSurfaceObligationBinding duplicate =
                new ResidualSurfaceObligationBinding("booking", scope -> false);

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompositeResidualSurfaceObligationAuthority(
                        registryWithResidualContribution("booking"),
                        List.of(first, duplicate)
                )
        );
    }

    @Test
    void rejects_missing_binding_for_registered_active_or_residual_contribution() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CompositeResidualSurfaceObligationAuthority(
                        registryWithResidualContribution("booking"),
                        List.of()
                )
        );
    }

    @Test
    void active_only_contribution_does_not_require_residual_binding() {
        assertDoesNotThrow(() -> new CompositeResidualSurfaceObligationAuthority(
                registryWithActiveOnlyContribution("booking"),
                List.of()
        ));
    }

    @Test
    void unbound_runtime_capability_lookup_fails_closed() {
        CompositeResidualSurfaceObligationAuthority authority =
                new CompositeResidualSurfaceObligationAuthority(
                        registryWithResidualContribution("booking"),
                        List.of(new ResidualSurfaceObligationBinding(
                                "booking",
                                scope -> false
                        ))
                );

        assertThrows(
                IllegalStateException.class,
                () -> authority.hasOutstandingObligations(MERCHANT, "appointment")
        );
    }

    private static SurfaceContributionRegistrySnapshot registryWithResidualContribution(
            String capabilityIdentifier
    ) {
        return new SurfaceContributionRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(new SurfaceContributionDefinition(
                        new SurfaceContributionIdentity(
                                capabilityIdentifier,
                                "manage-" + capabilityIdentifier
                        ),
                        SurfaceAudience.MERCHANT,
                        SurfaceContributionKind.WORKSPACE,
                        Optional.of("merchant/" + capabilityIdentifier),
                        Set.of(capabilityIdentifier + "-summary"),
                        Set.of(),
                        SurfaceEligibilityContract.activeOrResidual(
                                capabilityIdentifier + ".view"
                        )
                ))
        );
    }

    private static SurfaceContributionRegistrySnapshot registryWithActiveOnlyContribution(
            String capabilityIdentifier
    ) {
        return new SurfaceContributionRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(new SurfaceContributionDefinition(
                        new SurfaceContributionIdentity(
                                capabilityIdentifier,
                                "create-" + capabilityIdentifier
                        ),
                        SurfaceAudience.MERCHANT,
                        SurfaceContributionKind.ACTION,
                        Optional.of("merchant/" + capabilityIdentifier),
                        Set.of(),
                        Set.of(),
                        SurfaceEligibilityContract.activeOnly(
                                capabilityIdentifier + ".create"
                        )
                ))
        );
    }
}
