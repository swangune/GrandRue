package grandrue.governance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Mechanically reliable checks for the non-authoritative repository agent adapter.
 *
 * <p>Authority: MS-IMPLEMENTATION-RULES-001 v2.0,
 * {@code designs/IMPLEMENTATION-RULES.md}, §55 — Agent Context-Efficient Authority Loading.
 * These checks prove adapter structure only; they do not attempt semantic reasoning over prose.
 */
class AgentInstructionsConformanceTest {

    private static final Path ROOT = Path.of(".").toAbsolutePath().normalize();
    private static final Path AGENTS = ROOT.resolve("AGENTS.md");
    private static final Path IMPLEMENTATION = ROOT.resolve("IMPLEMENTATION.md");
    private static final Path IMPLEMENTATION_GRAPH = ROOT.resolve("docs/development/implementation-programme-state.json");
    private static final Path OPERATIONAL_RULES = ROOT.resolve("operational-rules.md");
    private static final long MAX_AGENTS_BYTES = 12L * 1024L;
    private static final long MAX_IMPLEMENTATION_BYTES = 20L * 1024L;

    /**
     * Local/generated directory families excluded from repository governance discovery.
     * These correspond to repository-ignored dependency, build, coverage and IDE trees;
     * their contents are not controlled Main Street repository instructions.
     */
    private static final Set<String> UNCONTROLLED_LOCAL_DIRECTORIES = Set.of(
            ".git",
            "target",
            "node_modules",
            ".next",
            "coverage",
            ".idea",
            ".vscode",
            ".settings");

    private static final List<String> REQUIRED_CANONICAL_REFERENCES = List.of(
            "designs/AUTHORITY-INDEX.md",
            "designs/IMPLEMENTATION-RULES.md",
            "designs/DESIGN-RULES.md",
            "designs/DEFERRED-DECISION-REGISTER.md",
            "designs/CANONICAL-SEMANTIC-LEXICON.md");

    @Test
    void rootAgentAdapterExistsIsNonAuthoritativeAndStaysSmall() throws IOException {
        assertTrue(Files.isRegularFile(AGENTS), "Missing root AGENTS.md");

        byte[] bytes = Files.readAllBytes(AGENTS);
        String content = new String(bytes, StandardCharsets.UTF_8);

        assertTrue(bytes.length <= MAX_AGENTS_BYTES,
                () -> "AGENTS.md exceeds 12 KiB: " + bytes.length + " bytes");
        assertTrue(content.contains("NON-AUTHORITATIVE"),
                "AGENTS.md must declare itself non-authoritative");
        assertTrue(content.contains("AUTHORITY-INDEX.md") && content.contains("which"),
                "AGENTS.md must preserve Authority Index navigation ownership");
        assertTrue(content.contains("accepted authority") && content.contains("governs"),
                "AGENTS.md must preserve accepted-authority precedence");

        for (String reference : REQUIRED_CANONICAL_REFERENCES) {
            assertTrue(content.contains(reference),
                    () -> "AGENTS.md is missing canonical reference: " + reference);
            assertTrue(Files.isRegularFile(ROOT.resolve(reference)),
                    () -> "AGENTS.md references missing canonical file: " + reference);
        }
    }

    @Test
    void implementationControllerExistsIsNonAuthoritativeAndBounded() throws IOException {
        assertTrue(Files.isRegularFile(IMPLEMENTATION), "Missing root IMPLEMENTATION.md");
        assertTrue(Files.isRegularFile(IMPLEMENTATION_GRAPH), "Missing canonical implementation graph");

        byte[] bytes = Files.readAllBytes(IMPLEMENTATION);
        String content = new String(bytes, StandardCharsets.UTF_8);

        assertTrue(bytes.length <= MAX_IMPLEMENTATION_BYTES,
                () -> "IMPLEMENTATION.md exceeds 20 KiB: " + bytes.length + " bytes");
        assertTrue(content.contains("NON-AUTHORITATIVE OPERATIONAL CONTROLLER"));
        assertTrue(content.contains("designs/IMPLEMENTATION-RULES.md"));
        assertTrue(content.contains("docs/development/implementation-programme-state.json"));
        assertTrue(content.contains("grandrue-implementation-controller/v2"));
        assertFalse(content.contains("implementation-status.md remains a mandatory"));
    }

    @Test
    void noOverrideOrNestedAgentInstructionFilesExist() throws IOException {
        assertFalse(Files.exists(ROOT.resolve("AGENTS.override.md")),
                "Repository AGENTS.override.md is prohibited by current implementation authority");

        List<Path> nested = findNestedAgentInstructionFiles(ROOT);
        assertTrue(nested.isEmpty(), () -> "Nested AGENTS.md files are prohibited: " + nested);
    }

    @Test
    void generatedDependencyTreesAreExcludedWithoutWeakeningNestedAgentProhibition(@TempDir Path tempDir)
            throws IOException {
        Files.writeString(tempDir.resolve("AGENTS.md"), "root");

        Path sourceAgent = Files.createDirectories(tempDir.resolve("src/main/java/mainstreet/example"))
                .resolve("AGENTS.md");
        Files.writeString(sourceAgent, "prohibited repository instruction");

        Path dependencyAgent = Files.createDirectories(tempDir.resolve("storefront-web/node_modules/next"))
                .resolve("AGENTS.md");
        Files.writeString(dependencyAgent, "third-party package instruction");

        Path buildAgent = Files.createDirectories(tempDir.resolve("target/generated-test-fixtures"))
                .resolve("AGENTS.md");
        Files.writeString(buildAgent, "generated build instruction");

        List<Path> nested = findNestedAgentInstructionFiles(tempDir);

        assertTrue(nested.equals(List.of(sourceAgent)),
                () -> "Repository nested AGENTS.md must remain prohibited while generated trees are ignored: " + nested);
    }

    @Test
    void operationalRulesIsCompatibilityPointerOnly() throws IOException {
        assertTrue(Files.isRegularFile(OPERATIONAL_RULES), "Missing operational-rules.md");

        String content = Files.readString(OPERATIONAL_RULES);
        assertTrue(content.contains("NON-AUTHORITATIVE COMPATIBILITY POINTER"),
                "operational-rules.md must be compatibility-only");
        assertTrue(content.contains("designs/IMPLEMENTATION-RULES.md"));
        assertTrue(content.contains("/AGENTS.md"));
        assertFalse(content.contains("# 1. Mandatory Governance Sources"),
                "operational-rules.md must not retain the previous long-form operating corpus");
        assertFalse(content.contains("# 4. Mandatory Design Lifecycle"),
                "operational-rules.md must not duplicate the design lifecycle");
    }

    private static List<Path> findNestedAgentInstructionFiles(Path root) throws IOException {
        Path normalizedRoot = root.toAbsolutePath().normalize();
        Path rootAgent = normalizedRoot.resolve("AGENTS.md");
        List<Path> nested = new ArrayList<>();

        Files.walkFileTree(normalizedRoot, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) {
                if (!directory.equals(normalizedRoot)
                        && UNCONTROLLED_LOCAL_DIRECTORIES.contains(directory.getFileName().toString())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                Path normalizedFile = file.toAbsolutePath().normalize();
                if (normalizedFile.getFileName().toString().equals("AGENTS.md")
                        && !normalizedFile.equals(rootAgent)) {
                    nested.add(normalizedFile);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return List.copyOf(nested);
    }
}
