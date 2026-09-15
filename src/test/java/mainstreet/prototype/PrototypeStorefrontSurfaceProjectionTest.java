package mainstreet.prototype;

import mainstreet.surface.ExposureDecision;
import mainstreet.surface.SurfaceContributionIdentity;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrototypeStorefrontSurfaceProjectionTest {

    private final PrototypeStorefrontSurfaceProjection projection =
            PrototypeStorefrontSurfaceProjection.standard(
                    PrototypeMerchantRuntime.standard()
            );

    @Test
    void same_trade_merchants_receive_different_public_surfaces_from_active_semantics() {
        PrototypeStorefrontSurfaceView showcase = projection.surface(
                "prototype-gardener-showcase"
        );
        PrototypeStorefrontSurfaceView bookable = projection.surface(
                "prototype-gardener-bookable"
        );

        assertEquals(
                Set.of("publication/browse-published-content"),
                contributionKeys(showcase)
        );
        assertEquals(
                Set.of(
                        "publication/browse-published-content",
                        "appointment/arrange-appointment"
                ),
                contributionKeys(bookable)
        );
        assertFalse(contributionKeys(showcase).contains(
                "appointment/arrange-appointment"
        ));

        PrototypeStorefrontSurfaceView.Contribution appointment = contribution(
                bookable,
                "appointment/arrange-appointment"
        );
        assertEquals(
                java.util.List.of(new PrototypePublicInteractionBinding(
                        "garden-maintenance",
                        "Garden maintenance visit"
                )),
                appointment.bindings()
        );
    }

    @Test
    void storefront_consumes_public_exposure_filter_after_static_membership() {
        SurfaceContributionIdentity appointment = new SurfaceContributionIdentity(
                "appointment",
                "arrange-appointment"
        );
        PrototypeStorefrontSurfaceProjection restricted =
                PrototypeStorefrontSurfaceProjection.withContextAuthorities(
                        PrototypeMerchantRuntime.standard(),
                        (scope, projectionRequirement, requestContext) -> Optional.empty(),
                        (scope, contribution, requestContext) -> Optional.of(
                                contribution.equals(appointment)
                                        ? ExposureDecision.WITHHOLD
                                        : ExposureDecision.EXPOSE
                        )
                );

        PrototypeStorefrontSurfaceView bookable = restricted.surface(
                "prototype-gardener-bookable"
        );

        assertEquals(
                Set.of("publication/browse-published-content"),
                contributionKeys(bookable)
        );
    }

    @Test
    void hidden_subject_removes_binding_without_manufacturing_contribution_state() {
        SurfaceContributionIdentity appointment = new SurfaceContributionIdentity(
                "appointment",
                "arrange-appointment"
        );
        PrototypeStorefrontSurfaceProjection restricted =
                PrototypeStorefrontSurfaceProjection.withContextAuthorities(
                        PrototypeMerchantRuntime.standard(),
                        (scope, projectionRequirement, requestContext) -> Optional.empty(),
                        (scope, contribution, requestContext) ->
                                Optional.of(ExposureDecision.EXPOSE),
                        (scope, contribution, subjectReference, requestContext) ->
                                Optional.of(
                                        contribution.equals(appointment)
                                                && subjectReference.equals("garden-maintenance")
                                                ? ExposureDecision.WITHHOLD
                                                : ExposureDecision.EXPOSE
                                )
                );

        PrototypeStorefrontSurfaceView bookable = restricted.surface(
                "prototype-gardener-bookable"
        );

        assertTrue(contributionKeys(bookable).contains(
                "appointment/arrange-appointment"
        ));
        assertEquals(
                java.util.List.of(),
                contribution(bookable, "appointment/arrange-appointment").bindings()
        );
    }

    @Test
    void booked_subject_binding_never_exposes_internal_allocation_subject() {
        PrototypeStorefrontSurfaceView motel = projection.surface(
                "prototype-motel"
        );

        PrototypeStorefrontSurfaceView.Contribution booking = contribution(
                motel,
                "booking/reserve-subject"
        );
        assertEquals(
                java.util.List.of(new PrototypePublicInteractionBinding(
                        "standard-room",
                        "Standard room"
                )),
                booking.bindings()
        );
        assertTrue(booking.bindings().stream().noneMatch(binding ->
                binding.subjectReference().contains("capacity")
        ));
    }

    @Test
    void ordering_binding_exposes_public_proposition_reference_not_internal_sku() {
        PrototypeStorefrontSurfaceView retailer = projection.surface(
                "prototype-retailer"
        );

        PrototypeStorefrontSurfaceView.Contribution ordering = contribution(
                retailer,
                "ordering/place-order"
        );
        assertEquals(
                java.util.List.of(new PrototypePublicInteractionBinding(
                        "milk-2l",
                        "Milk 2L"
                )),
                ordering.bindings()
        );
        assertTrue(ordering.bindings().stream().noneMatch(binding ->
                binding.subjectReference().equals("sku-1")
        ));
    }

    @Test
    void materially_different_merchants_share_registered_composition_targets() {
        PrototypeStorefrontSurfaceView motel = projection.surface(
                "prototype-motel"
        );
        PrototypeStorefrontSurfaceView retailer = projection.surface(
                "prototype-retailer"
        );

        assertEquals(
                Set.of("booking/reserve-subject"),
                contributionKeys(motel)
        );
        assertEquals(
                Set.of("ordering/place-order"),
                contributionKeys(retailer)
        );
        assertTrue(motel.groups().stream().anyMatch(group ->
                group.compositionTargetReference()
                        .equals("public/primary-actions")
        ));
        assertTrue(retailer.groups().stream().anyMatch(group ->
                group.compositionTargetReference()
                        .equals("public/primary-actions")
        ));
    }

    @Test
    void storefront_projection_never_exposes_inactive_operation_contributions() {
        PrototypeStorefrontSurfaceView publisher = projection.surface(
                "prototype-publisher"
        );

        assertEquals(
                Set.of("publication/browse-published-content"),
                contributionKeys(publisher)
        );
        assertTrue(publisher.groups().stream()
                .flatMap(group -> group.contributions().stream())
                .allMatch(contribution ->
                        contribution.supportedOperationReferences().isEmpty()
                                && contribution.bindings().isEmpty()
                ));
    }

    private static PrototypeStorefrontSurfaceView.Contribution contribution(
            PrototypeStorefrontSurfaceView view,
            String key
    ) {
        return view.groups().stream()
                .flatMap(group -> group.contributions().stream())
                .filter(contribution -> key.equals(
                        contribution.ownerCapabilityIdentifier()
                                + "/"
                                + contribution.contributionIdentifier()
                ))
                .findFirst()
                .orElseThrow();
    }

    private static Set<String> contributionKeys(
            PrototypeStorefrontSurfaceView view
    ) {
        return view.groups().stream()
                .flatMap(group -> group.contributions().stream())
                .map(contribution ->
                        contribution.ownerCapabilityIdentifier()
                                + "/"
                                + contribution.contributionIdentifier()
                )
                .collect(Collectors.toUnmodifiableSet());
    }
}
