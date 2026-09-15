package mainstreet.governance;

import mainstreet.semantic.release.SemanticReleaseAssemblyRepository;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Protects ADR-010/011/013 from collapsing into ADR-012 execution authority or
 * database-schema state.
 */
class SemanticReleaseBoundaryConformanceTest {

    private static final Path RELEASE_PACKAGE = Path.of(
            "src", "main", "java", "mainstreet", "semantic", "release"
    );

    @Test
    void materialisation_package_does_not_own_execution_or_configuration_authority()
            throws IOException {
        List<String> violations = forbiddenReferences(List.of(
                "import mainstreet.semantic.executable.",
                "import mainstreet.runtime.",
                "import mainstreet.semantic.configuration."
        ));

        assertTrue(
                violations.isEmpty(),
                () -> "Semantic materialisation must not become execution/configuration authority: "
                        + violations
        );
    }

    @Test
    void database_schema_state_does_not_become_semantic_release_authority()
            throws IOException {
        List<String> violations = forbiddenReferences(List.of(
                "import mainstreet.infrastructure.",
                "import org.flywaydb.",
                "flyway_schema_history",
                "schemaVersion",
                "migrationVersion"
        ));

        assertTrue(
                violations.isEmpty(),
                () -> "Semantic release identity must remain independent of database schema state: "
                        + violations
        );
    }

    @Test
    void semantic_release_repository_exposes_exact_lookup_only() {
        Set<String> declaredMethods = Arrays.stream(
                        SemanticReleaseAssemblyRepository.class.getDeclaredMethods()
                )
                .map(method -> method.getName() + "/" + method.getParameterCount())
                .collect(java.util.stream.Collectors.toUnmodifiableSet());

        assertEquals(Set.of("release/1"), declaredMethods);
        assertTrue(declaredMethods.stream().noneMatch(method ->
                method.startsWith("latest/") || method.startsWith("current/")
        ));
    }

    private static List<String> forbiddenReferences(List<String> forbidden)
            throws IOException {
        List<String> violations = new ArrayList<>();
        try (Stream<Path> sources = Files.walk(RELEASE_PACKAGE)) {
            for (Path source : sources
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".java"))
                    .toList()) {
                String content = Files.readString(source);
                for (String reference : forbidden) {
                    if (content.contains(reference)) {
                        violations.add(source.getFileName() + " -> " + reference);
                    }
                }
            }
        }
        return violations;
    }
}
