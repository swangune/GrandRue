package grandrue.governance;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Structural checks for the governed design corpus.
 *
 * <p>This test intentionally does not attempt semantic reasoning over prose. It implements the
 * mechanically reliable subset accepted by DESIGN-CORPUS-CONFORMANCE.md.
 */
class DesignCorpusConformanceTest {

    private static final Path DESIGNS = Path.of("designs");

    private static final List<String> REQUIRED_GOVERNANCE_ARTIFACTS = List.of(
            "DESIGN-RULES.md",
            "DOCUMENT-GOVERNANCE.md",
            "AUTHORITY-INDEX.md",
            "CANONICAL-SEMANTIC-LEXICON.md",
            "DEFERRED-DECISION-REGISTER.md",
            "DESIGN-CORPUS-CONFORMANCE.md",
            "IMPLEMENTATION-RULES.md");

    private static final Pattern DOCUMENT_ID = Pattern.compile("MS-PROT-(\\d{3})");
    private static final Pattern VERSION = Pattern.compile("(?im)^\\*\\*Version:\\*\\*\\s*([^\\r\\n]+)");
    private static final Pattern CLOSES_DDR = Pattern.compile("DDR-OD-\\d{3}");

    @Test
    void requiredGovernanceArtifactsExist() {
        for (String file : REQUIRED_GOVERNANCE_ARTIFACTS) {
            assertTrue(Files.isRegularFile(DESIGNS.resolve(file)),
                    () -> "Missing required governance artifact: designs/" + file);
        }
    }

    @Test
    void everyAcceptedModernMsProtIdentifierAppearsInAuthorityIndex() throws IOException {
        String authorityIndex = Files.readString(DESIGNS.resolve("AUTHORITY-INDEX.md"));

        for (DesignDocument document : acceptedModernDocuments()) {
            assertTrue(authorityIndex.contains("MS-PROT-" + document.id()),
                    () -> "Accepted authority MS-PROT-" + document.id()
                            + " is missing from designs/AUTHORITY-INDEX.md (source: "
                            + document.path().getFileName() + ")");
        }
    }

    @Test
    void noTwoAcceptedDocumentsClaimSameIdentifierAndExplicitVersion() throws IOException {
        Map<String, List<Path>> claims = new HashMap<>();

        for (DesignDocument document : acceptedModernDocuments()) {
            if (document.version().isEmpty()) {
                continue;
            }
            String key = document.id() + "@" + document.version().get();
            claims.computeIfAbsent(key, ignored -> new ArrayList<>()).add(document.path());
        }

        List<String> duplicates = claims.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(entry -> entry.getKey() + " -> " + entry.getValue())
                .toList();

        assertTrue(duplicates.isEmpty(),
                () -> "Duplicate ACCEPTED MS-PROT identifier/version claims: " + duplicates);
    }

    @Test
    void explicitDdrClosuresAreResolvedInCurrentRegister() throws IOException {
        String currentDdr = Files.readString(DESIGNS.resolve("DEFERRED-DECISION-REGISTER.md"));

        for (DesignDocument document : acceptedModernDocuments()) {
            Matcher matcher = CLOSES_DDR.matcher(document.content());
            while (matcher.find()) {
                String decisionId = matcher.group();
                Pattern resolvedRow = Pattern.compile(
                        "(?m)^\\|\\s*" + Pattern.quote(decisionId)
                                + "\\s*\\|\\s*\\*\\*RESOLVED\\*\\*");
                assertTrue(resolvedRow.matcher(currentDdr).find(),
                        () -> decisionId + " is explicitly closed by " + document.path().getFileName()
                                + " but is not RESOLVED in the current DDR view");
            }
        }
    }

    private static List<DesignDocument> acceptedModernDocuments() throws IOException {
        try (Stream<Path> files = Files.list(DESIGNS)) {
            List<DesignDocument> documents = new ArrayList<>();
            for (Path path : files.filter(Files::isRegularFile).toList()) {
                Matcher fileId = DOCUMENT_ID.matcher(path.getFileName().toString());
                if (!fileId.find()) {
                    continue;
                }

                int numericId = Integer.parseInt(fileId.group(1));
                if (numericId < 20) {
                    continue;
                }

                String content = Files.readString(path);
                if (!isAccepted(content)) {
                    continue;
                }

                Matcher contentId = DOCUMENT_ID.matcher(content);
                String id = contentId.find() ? contentId.group(1) : fileId.group(1);
                Optional<String> version = explicitVersion(content);
                documents.add(new DesignDocument(id, version, path, content));
            }
            return documents;
        }
    }

    private static boolean isAccepted(String content) {
        String upper = content.toUpperCase();
        int status = upper.indexOf("STATUS:");
        if (status < 0) {
            return false;
        }
        int lineEnd = upper.indexOf('\n', status);
        String statusLine = lineEnd < 0 ? upper.substring(status) : upper.substring(status, lineEnd);
        return statusLine.contains("ACCEPTED");
    }

    private static Optional<String> explicitVersion(String content) {
        Matcher matcher = VERSION.matcher(content);
        return matcher.find() ? Optional.of(matcher.group(1).trim()) : Optional.empty();
    }

    private record DesignDocument(
            String id,
            Optional<String> version,
            Path path,
            String content) {
    }
}
