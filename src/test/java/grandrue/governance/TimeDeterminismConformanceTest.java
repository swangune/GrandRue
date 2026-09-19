package grandrue.governance;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Protects IMPLEMENTATION-RULES §§31-32: deterministic business-time logic
 * must use an explicit controllable Clock rather than hidden wall-clock calls.
 */
class TimeDeterminismConformanceTest {

    private static final Path MAIN = Path.of("src", "main", "java", "grandrue");

    private static final Map<String, String> FORBIDDEN_WALL_CLOCK_CALLS = Map.of(
            "Instant.now(", "Instant.now",
            "LocalDate.now(", "LocalDate.now",
            "LocalDateTime.now(", "LocalDateTime.now",
            "OffsetDateTime.now(", "OffsetDateTime.now",
            "ZonedDateTime.now(", "ZonedDateTime.now",
            "System.currentTimeMillis(", "System.currentTimeMillis"
    );

    @Test
    void production_code_does_not_read_hidden_wall_clock_time() throws IOException {
        List<String> violations = new ArrayList<>();

        for (Path source : productionSources()) {
            String content = Files.readString(source);
            for (Map.Entry<String, String> forbidden : FORBIDDEN_WALL_CLOCK_CALLS.entrySet()) {
                if (content.contains(forbidden.getKey())) {
                    violations.add(relative(source) + " -> " + forbidden.getValue());
                }
            }
        }

        assertTrue(
                violations.isEmpty(),
                () -> "Business-time logic must receive a controllable Clock; hidden wall-clock reads found: "
                        + violations
        );
    }

    private static List<Path> productionSources() throws IOException {
        try (Stream<Path> paths = Files.walk(MAIN)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".java"))
                    .toList();
        }
    }

    private static String relative(Path source) {
        return MAIN.relativize(source).toString().replace('\\', '/');
    }
}
