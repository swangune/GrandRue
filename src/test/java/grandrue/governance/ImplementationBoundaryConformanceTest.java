package grandrue.governance;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Mechanically protects accepted production and repository-navigation
 * boundaries from superseded prototype implementations or stale claims.
 */
class ImplementationBoundaryConformanceTest {

    private static final Path MAIN = Path.of("src", "main", "java");
    private static final Path TEST = Path.of("src", "test", "java");

    @Test
    void canonical_configuration_compiler_is_the_only_production_compiler_path() {
        assertTrue(Files.isRegularFile(MAIN.resolve(
                "mainstreet/semantic/compiler/ConfigurationCompiler.java"
        )));

        for (String rejectedPath : List.of(
                "mainstreet/semantic/capability/CapabilityCompiler.java",
                "mainstreet/semantic/capability/OperationComposition.java",
                "mainstreet/semantic/capability/CapabilityCompositionDefinition.java"
        )) {
            assertFalse(
                    Files.exists(MAIN.resolve(rejectedPath)),
                    () -> "Superseded prototype semantic path returned to production: "
                            + rejectedPath
            );
            assertTrue(
                    Files.isRegularFile(TEST.resolve(rejectedPath)),
                    () -> "Historical prototype evidence is not quarantined in test scope: "
                            + rejectedPath
            );
        }
    }

    @Test
    void booking_does_not_own_a_parallel_production_notification_delivery_stack() {
        for (String rejectedPath : List.of(
                "mainstreet/booking/BookingNotificationGateway.java",
                "mainstreet/booking/BookingNotificationDelivery.java",
                "mainstreet/booking/NotificationDeliveryException.java"
        )) {
            assertFalse(
                    Files.exists(MAIN.resolve(rejectedPath)),
                    () -> "Booking-specific notification delivery returned to production: "
                            + rejectedPath
            );
            assertTrue(
                    Files.isRegularFile(TEST.resolve(rejectedPath)),
                    () -> "Historical Booking notification evidence is not quarantined: "
                            + rejectedPath
            );
        }

        assertTrue(Files.isRegularFile(MAIN.resolve(
                "mainstreet/notification/NotificationDeliveryCoordinator.java"
        )));
    }

    @Test
    void repository_navigation_does_not_reassert_obsolete_stack_or_authority_claims()
            throws IOException {
        String readme = Files.readString(Path.of("README"));
        assertTrue(readme.contains("Java 25"));
        assertTrue(readme.contains("PostgreSQL"));
        assertFalse(readme.contains("H2 is currently test-only"));

        String workflowTree = Files.readString(Path.of("workflow-tree.md"));
        assertTrue(workflowTree.contains("Non-authoritative"));
        assertFalse(workflowTree.contains("No feature should exist outside this hierarchy"));

        String lifecycle = Files.readString(Path.of("lifecycle.md"));
        assertTrue(lifecycle.contains("Non-authoritative"));

        String packageBoundary = Files.readString(
                Path.of("build_configuration", "package_boundary.md")
        );
        assertTrue(packageBoundary.contains("test-scope"));
        assertTrue(packageBoundary.contains("ConfigurationCompiler"));
    }
}
