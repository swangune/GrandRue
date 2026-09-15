package mainstreet.semantic.executable;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.testing.TestConfigurationReleases;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ActiveOperationResolverTest {

    @Test
    void binds_an_applicable_operation_to_one_captured_model_snapshot() {
        TestConfigurationReleases releases = new TestConfigurationReleases();
        ConfigurationReleaseActivation activation = releases.activation();
        ActiveOperationResolver resolver = new ActiveOperationResolver(activation);
        ExecutableMerchantModel first = model(
                "merchant-a",
                1,
                "BookingCreatedV1"
        );
        ExecutableMerchantModel second = model(
                "merchant-a",
                2,
                "BookingCreatedV2"
        );

        releases.activate(first);
        ApplicableOperation captured = resolver.resolve(
                new MerchantScope("merchant-a"),
                "booking.create"
        );
        releases.activate(second);
        ApplicableOperation current = resolver.resolve(
                new MerchantScope("merchant-a"),
                "booking.create"
        );

        assertSame(first, captured.model());
        assertSame(first.operation("booking.create").orElseThrow(),
                captured.operation());
        assertEquals(Set.of("BookingCreatedV1"),
                captured.operation().eventIdentifiers());
        assertSame(second, current.model());
        assertEquals(Set.of("BookingCreatedV2"),
                current.operation().eventIdentifiers());
    }

    @Test
    void rejects_an_operation_absent_from_the_active_merchant_model() {
        TestConfigurationReleases releases = new TestConfigurationReleases();
        ConfigurationReleaseActivation activation = releases.activation();
        releases.activate(model("merchant-a", 1, "BookingCreated"));
        ActiveOperationResolver resolver = new ActiveOperationResolver(activation);

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(
                        new MerchantScope("merchant-a"),
                        "payment.capture"
                )
        );
        assertThrows(
                IllegalStateException.class,
                () -> resolver.resolve(
                        new MerchantScope("merchant-b"),
                        "booking.create"
                )
        );
    }

    @Test
    void resolves_identical_operation_identifiers_only_within_scope() {
        TestConfigurationReleases releases = new TestConfigurationReleases();
        ConfigurationReleaseActivation activation = releases.activation();
        releases.activate(model("merchant-a", 1, "MerchantABooking"));
        releases.activate(model("merchant-b", 1, "MerchantBBooking"));
        ActiveOperationResolver resolver = new ActiveOperationResolver(activation);

        ApplicableOperation resolved = resolver.resolve(
                new MerchantScope("merchant-b"),
                "booking.create"
        );

        assertEquals("merchant-b", resolved.model().merchantIdentifier());
        assertEquals(
                Set.of("MerchantBBooking"),
                resolved.operation().eventIdentifiers()
        );
    }

    private static ExecutableMerchantModel model(
            String merchantIdentifier,
            long version,
            String eventIdentifier
    ) {
        ExecutableOperationalObjectTypeIdentity bookingType =
                new ExecutableOperationalObjectTypeIdentity(
                        "booking",
                        "booking"
                );
        ExecutableOperationDefinition create = new ExecutableOperationDefinition(
                "booking.create",
                List.of(new ExecutableObjectCreationEffect(
                        bookingType,
                        "requested"
                )),
                Set.of(eventIdentifier),
                "booking.create"
        );
        return new ExecutableMerchantModel(
                merchantIdentifier,
                "configuration-" + version,
                version,
                "semantic-registry-1.0",
                Set.of("booking"),
                List.of(new ExecutableOperationalObjectDefinition(
                        "booking",
                        "booking",
                        Set.of("requested"),
                        "requested"
                )),
                List.of(create)
        );
    }
}
