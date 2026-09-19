package grandrue.governance;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Mechanically enforces the subset of MS-PROT-032 module boundaries that is
 * expressible from the current package architecture without inventing a new
 * package convention or architecture framework.
 */
class ModuleDependencyConformanceTest {

    private static final Path MAIN = Path.of("src", "main", "java", "grandrue");
    private static final Pattern GRANDRUE_IMPORT = Pattern.compile(
            "^import\\s+grandrue\\.([a-zA-Z0-9_]+)\\.([a-zA-Z0-9_.$]+);$",
            Pattern.MULTILINE
    );

    private static final Set<String> OWNERSHIP_ROOTS = Set.of(
            "audit",
            "background",
            "booking",
            "businesshours",
            "commercial",
            "credential",
            "customer",
            "fulfilment",
            "inventory",
            "media",
            "merchantaccount",
            "merchantprofile",
            "money",
            "notification",
            "observability",
            "ordering",
            "privacy",
            "protection",
            "resilience",
            "scheduling",
            "surface",
            "workforce"
    );

    @Test
    void non_infrastructure_code_does_not_depend_on_infrastructure_implementations()
            throws IOException {
        List<String> violations = new ArrayList<>();

        for (Path source : productionSources()) {
            if (relative(source).startsWith("infrastructure/")) {
                continue;
            }

            String content = Files.readString(source);
            if (isSpringCompositionRoot(content)) {
                continue;
            }
            if (content.contains("import grandrue.infrastructure.")) {
                violations.add(relative(source));
            }
        }

        assertTrue(
                violations.isEmpty(),
                () -> "MS-PROT-032 requires infrastructure to point inward through ports; "
                        + "only Spring composition roots may wire concrete infrastructure: "
                        + violations
        );
    }

    @Test
    void database_technology_is_confined_to_infrastructure_or_composition_roots()
            throws IOException {
        List<String> violations = new ArrayList<>();

        for (Path source : productionSources()) {
            if (relative(source).startsWith("infrastructure/")) {
                continue;
            }

            String content = Files.readString(source);
            if (isSpringCompositionRoot(content)) {
                continue;
            }
            if (content.contains("import org.jooq.")
                    || content.contains("import org.springframework.jdbc.")
                    || content.contains("import javax.sql.")) {
                violations.add(relative(source));
            }
        }

        assertTrue(
                violations.isEmpty(),
                () -> "Database implementation technology leaked outside infrastructure/composition roots: "
                        + violations
        );
    }

    @Test
    void ownership_modules_do_not_import_another_modules_persistence_port()
            throws IOException {
        List<String> violations = new ArrayList<>();

        for (Path source : productionSources()) {
            String sourceRoot = ownershipRoot(source);
            if (sourceRoot == null) {
                continue;
            }

            Matcher matcher = GRANDRUE_IMPORT.matcher(Files.readString(source));
            while (matcher.find()) {
                String importedRoot = matcher.group(1);
                if (sourceRoot.equals(importedRoot) || !OWNERSHIP_ROOTS.contains(importedRoot)) {
                    continue;
                }

                String importedType = matcher.group(2);
                String simpleName = importedType.substring(importedType.lastIndexOf('.') + 1);
                if (simpleName.endsWith("Store")
                        || simpleName.endsWith("Repository")
                        || simpleName.startsWith("Jdbc")
                        || simpleName.startsWith("Jooq")) {
                    violations.add(
                            relative(source) + " -> grandrue." + importedRoot + "." + importedType
                    );
                }
            }
        }

        assertTrue(
                violations.isEmpty(),
                () -> "MS-PROT-032 prohibits direct cross-module persistence access; "
                        + "use a published capability/application contract instead: " + violations
        );
    }

    private static boolean isSpringCompositionRoot(String content) {
        return content.contains("@Configuration")
                && content.contains("org.springframework.context.annotation.Configuration");
    }

    private static List<Path> productionSources() throws IOException {
        try (Stream<Path> paths = Files.walk(MAIN)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".java"))
                    .toList();
        }
    }

    private static String ownershipRoot(Path source) {
        Path relative = MAIN.relativize(source);
        if (relative.getNameCount() < 2) {
            return null;
        }

        String root = relative.getName(0).toString();
        return OWNERSHIP_ROOTS.contains(root) ? root : null;
    }

    private static String relative(Path source) {
        return MAIN.relativize(source).toString().replace('\\', '/');
    }
}
