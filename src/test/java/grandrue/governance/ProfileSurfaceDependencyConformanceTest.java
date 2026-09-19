package grandrue.governance;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Protects the BR3 dependency direction required by MS-PROT-032: Profile may
 * publish read material through generic Surface contracts, while Surface must
 * remain unaware of the Profile owner.
 */
class ProfileSurfaceDependencyConformanceTest {

    private static final Path SURFACE = Path.of(
            "src", "main", "java", "mainstreet", "surface"
    );

    @Test
    void generic_surface_does_not_depend_on_merchant_profile_owner()
            throws IOException {
        List<String> violations;

        try (Stream<Path> paths = Files.walk(SURFACE)) {
            violations = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".java"))
                    .filter(path -> containsProfileDependency(path))
                    .map(path -> SURFACE.relativize(path).toString().replace('\\', '/'))
                    .toList();
        }

        assertTrue(
                violations.isEmpty(),
                () -> "Generic Surface must remain owner-blind; "
                        + "Profile-specific dependencies belong in Profile/application composition: "
                        + violations
        );
    }

    private static boolean containsProfileDependency(Path source) {
        try {
            return Files.readString(source).contains("mainstreet.merchantprofile.");
        } catch (IOException exception) {
            throw new IllegalStateException("Could not inspect " + source, exception);
        }
    }
}
