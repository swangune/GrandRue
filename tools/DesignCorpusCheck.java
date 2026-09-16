import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Deterministic structural checker for the GrandRue design-authority corpus.
 *
 * This tool checks repository structure and navigation only. It is not semantic authority
 * and does not claim to prove business/design coherence.
 */
public final class DesignCorpusCheck {
    private static final List<String> GOVERNANCE_FILES = List.of(
            "DESIGN-RULES.md",
            "DOCUMENT-GOVERNANCE.md",
            "AUTHORITY-INDEX.md",
            "CANONICAL-SEMANTIC-LEXICON.md",
            "DEFERRED-DECISION-REGISTER.md",
            "DESIGN-CORPUS-CONFORMANCE.md",
            "IMPLEMENTATION-RULES.md");

    private static final Pattern DOCUMENT_ID = Pattern.compile("^\\*\\*Document ID:\\*\\*\\s*`?([^`\\s]+)`?.*$");
    private static final Pattern VERSION = Pattern.compile("^\\*\\*Version:\\*\\*\\s*([^\\s]+).*$");
    private static final Pattern STATUS = Pattern.compile("^\\*\\*Status:\\*\\*\\s*(.+?)\\s*$");
    private static final Pattern INDEX_ROW = Pattern.compile("^\\|\\s*(MS-(?:PROT|IMP)-\\d{3})\\s*\\|");
    private static final Pattern MARKDOWN_LINK = Pattern.compile("\\[[^\\]]*]\\(([^)]+\\.md(?:#[^)\\s]+)?)\\)");

    private record Meta(Path path, String id, String version, String status, String content) {
        String key() { return id + "@" + version; }
        boolean accepted() { return normalizedStatus().contains("ACCEPTED"); }
        boolean proposed() { return normalizedStatus().contains("PROPOSED") || normalizedStatus().contains("DRAFT"); }
        String normalizedStatus() { return status == null ? "" : status.toUpperCase(Locale.ROOT); }
    }

    private static final class Result {
        int errors;
        int warnings;
        void error(String message) { errors++; System.err.println("ERROR: " + message); }
        void warning(String message) { warnings++; System.err.println("WARNING: " + message); }
        void info(String message) { System.out.println("INFO: " + message); }
    }

    public static void main(String[] args) throws Exception {
        Arguments a = Arguments.parse(args);
        if (a.help) {
            usage();
            return;
        }

        Path root = a.root.toAbsolutePath().normalize();
        Path designs = root.resolve("designs");
        Result result = new Result();

        if (!Files.isDirectory(designs)) {
            result.error("designs/ directory not found under " + root);
            finish(result);
            return;
        }

        List<Meta> metadata = scan(designs, result);
        if (a.preflightId != null) {
            runPreflight(root, designs, metadata, a, result);
        } else {
            runFull(root, designs, metadata, a, result);
        }
        finish(result);
    }

    private static void runPreflight(Path root, Path designs, List<Meta> metadata, Arguments a, Result result) throws IOException {
        String id = a.preflightId;
        String version = a.candidateVersion;
        if (version == null || version.isBlank()) {
            result.error("--candidate-version is required with --preflight");
            return;
        }

        Path index = designs.resolve("AUTHORITY-INDEX.md");
        String indexText = Files.exists(index) ? Files.readString(index, StandardCharsets.UTF_8) : "";
        List<Meta> matches = metadata.stream().filter(m -> id.equals(m.id)).sorted(Comparator.comparing(m -> m.version)).toList();

        System.out.println("PREFLIGHT");
        System.out.println("root=" + root);
        System.out.println("documentId=" + id);
        System.out.println("candidateVersion=" + version);
        System.out.println("canonicalDestination=" + canonicalDirectory(designs, id));
        System.out.println("indexed=" + indexText.contains("| " + id + " |"));

        if (matches.isEmpty()) {
            System.out.println("existingVersions=<none>");
        } else {
            System.out.println("existingVersions:");
            for (Meta m : matches) {
                System.out.println("  " + m.version + " | " + safe(m.status) + " | " + root.relativize(m.path));
            }
        }

        List<Meta> collision = matches.stream().filter(m -> version.equals(m.version)).toList();
        if (!collision.isEmpty()) {
            result.error("candidate collision for " + id + " version " + version + ": "
                    + collision.stream().map(m -> root.relativize(m.path).toString()).toList());
        } else {
            result.info("candidate identity is unoccupied: " + id + " v" + version);
        }
    }

    private static void runFull(Path root, Path designs, List<Meta> metadata, Arguments a, Result result) throws IOException {
        checkCanonicalGovernance(designs, result);
        checkDuplicateIdentities(root, metadata, result);
        checkAuthorityStore(root, designs, metadata, a, result);
        checkAuthorityIndex(root, designs, metadata, result);
        checkCanonicalDdr(designs, result);
        checkGovernanceCompanions(root, designs, result);
        checkMarkdownLinks(root, metadata, result);

        result.info("scanned " + metadata.size() + " metadata-bearing Markdown authorities recursively");
    }

    private static List<Meta> scan(Path designs, Result result) throws IOException {
        List<Meta> all = new ArrayList<>();
        try (Stream<Path> s = Files.walk(designs)) {
            for (Path path : s.filter(Files::isRegularFile).filter(p -> p.getFileName().toString().endsWith(".md")).toList()) {
                String content = Files.readString(path, StandardCharsets.UTF_8);
                String id = null, version = null, status = null;
                for (String line : content.lines().limit(40).toList()) {
                    Matcher m = DOCUMENT_ID.matcher(line);
                    if (m.matches()) id = m.group(1).trim();
                    m = VERSION.matcher(line);
                    if (m.matches()) version = m.group(1).trim();
                    m = STATUS.matcher(line);
                    if (m.matches()) status = stripMarkdown(m.group(1).trim());
                }
                if (id != null || version != null || status != null) {
                    if (id == null || version == null || status == null) {
                        result.warning("incomplete authority metadata: " + path);
                    }
                    if (id != null && version != null) {
                        all.add(new Meta(path.toAbsolutePath().normalize(), id, version, status, content));
                    }
                }
            }
        }
        return all;
    }

    private static void checkCanonicalGovernance(Path designs, Result result) {
        for (String f : GOVERNANCE_FILES) {
            if (!Files.isRegularFile(designs.resolve(f))) result.error("required canonical governance file absent: designs/" + f);
        }
    }

    private static void checkDuplicateIdentities(Path root, List<Meta> metadata, Result result) {
        Map<String, List<Meta>> byKey = new TreeMap<>();
        for (Meta m : metadata) byKey.computeIfAbsent(m.key(), ignored -> new ArrayList<>()).add(m);
        for (var e : byKey.entrySet()) {
            if (e.getValue().size() <= 1) continue;
            Set<String> distinct = new HashSet<>();
            for (Meta m : e.getValue()) distinct.add(m.content);
            String paths = e.getValue().stream().map(m -> root.relativize(m.path).toString()).toList().toString();
            if (distinct.size() > 1 || e.getValue().stream().anyMatch(Meta::accepted)) {
                result.error("duplicate/conflicting authority identity " + e.getKey() + " at " + paths);
            } else {
                result.warning("duplicate non-competing representation " + e.getKey() + " at " + paths);
            }
        }
    }

    private static void checkAuthorityStore(Path root, Path designs, List<Meta> metadata, Arguments a, Result result) {
        Path acceptedStore = designs.resolve("authorities").toAbsolutePath().normalize();
        for (Meta m : metadata) {
            boolean inStore = m.path.startsWith(acceptedStore);
            if (inStore && m.proposed()) {
                result.error("unapproved/proposed authority persisted in accepted authority store: " + root.relativize(m.path));
            }

            boolean governedSeries = (m.id.startsWith("MS-PROT-") && numericProt(m.id) >= 20)
                    || m.id.startsWith("MS-IMP-");
            if (governedSeries && m.accepted()) {
                boolean enforce = true;
                if (a.pilotId != null && !a.pilotId.equals(m.id) && !a.enforceLayout) {
                    enforce = true;
                }
                if (enforce) {
                    Path expected = canonicalDirectory(designs, m.id).toAbsolutePath().normalize();
                    if (!m.path.startsWith(expected)) {
                        result.error("accepted " + m.id + " is outside canonical directory " + root.relativize(expected)
                                + ": " + root.relativize(m.path));
                    }
                }
                if (inStore) {
                    Path expected = canonicalDirectory(designs, m.id).toAbsolutePath().normalize();
                    if (!m.path.startsWith(expected)) {
                        result.error("authority-directory ID mismatch for " + m.id + ": " + root.relativize(m.path));
                    }
                }
            }
        }
    }

    private static void checkAuthorityIndex(Path root, Path designs, List<Meta> metadata, Result result) throws IOException {
        Path index = designs.resolve("AUTHORITY-INDEX.md");
        if (!Files.isRegularFile(index)) return;
        String text = Files.readString(index, StandardCharsets.UTF_8);
        Set<String> indexed = new HashSet<>();
        for (String line : text.lines().toList()) {
            Matcher m = INDEX_ROW.matcher(line);
            if (m.find()) indexed.add(m.group(1));
        }
        Set<String> accepted = new TreeSet<>();
        for (Meta m : metadata) {
            if (!m.accepted()) continue;
            if (m.id.startsWith("MS-PROT-") && numericProt(m.id) >= 20) accepted.add(m.id);
            if (m.id.startsWith("MS-IMP-")) accepted.add(m.id);
        }
        for (String id : accepted) if (!indexed.contains(id)) result.error("accepted authority absent from Authority Index: " + id);
        for (String id : indexed) if (!accepted.contains(id)) result.error("Authority Index points to no accepted authority metadata: " + id);
    }

    private static void checkCanonicalDdr(Path designs, Result result) {
        if (!Files.isRegularFile(designs.resolve("DEFERRED-DECISION-REGISTER.md"))) {
            result.error("canonical DDR absent");
        }
    }

    private static void checkGovernanceCompanions(Path root, Path designs, Result result) throws IOException {
        try (Stream<Path> s = Files.walk(designs)) {
            for (Path p : s.filter(Files::isRegularFile).filter(x -> x.getFileName().toString().endsWith(".md")).toList()) {
                String name = p.getFileName().toString();
                for (String canonical : GOVERNANCE_FILES) {
                    String stem = canonical.substring(0, canonical.length() - 3);
                    if (!p.equals(designs.resolve(canonical)) && name.startsWith(stem)
                            && (name.contains("CURRENT") || name.contains("LATEST") || name.matches(".*v\\d.*"))) {
                        result.error("competing live governance/rule companion candidate: " + root.relativize(p));
                    }
                }
            }
        }
    }

    private static void checkMarkdownLinks(Path root, List<Meta> metadata, Result result) {
        for (Meta meta : metadata) {
            Matcher matcher = MARKDOWN_LINK.matcher(meta.content);
            while (matcher.find()) {
                String raw = matcher.group(1);
                String pathPart = raw.split("#", 2)[0].replace("%20", " ");
                if (pathPart.isBlank() || pathPart.startsWith("http://") || pathPart.startsWith("https://") || pathPart.startsWith("mailto:")) continue;
                Path resolved = meta.path.getParent().resolve(pathPart).normalize();
                if (!Files.exists(resolved)) result.warning("broken repository-relative Markdown link in "
                        + root.relativize(meta.path) + ": " + raw);
            }
        }
    }

    private static Path canonicalDirectory(Path designs, String id) {
        if (id.startsWith("MS-PROT-")) return designs.resolve("authorities/ms-prot").resolve(id);
        if (id.startsWith("MS-IMP-")) return designs.resolve("authorities/programme").resolve(id);
        return designs.resolve("authorities").resolve(id);
    }

    private static int numericProt(String id) {
        try { return Integer.parseInt(id.substring("MS-PROT-".length())); }
        catch (RuntimeException ignored) { return -1; }
    }

    private static String stripMarkdown(String s) {
        return s.replace("**", "").replace("`", "").trim();
    }

    private static String safe(String s) { return s == null ? "<missing>" : s; }

    private static void finish(Result result) {
        System.out.println("SUMMARY errors=" + result.errors + " warnings=" + result.warnings);
        if (result.errors > 0) System.exit(1);
    }

    private static void usage() {
        System.out.println("Usage:\n"
                + "  java tools/DesignCorpusCheck.java --full [--root PATH] [--pilot MS-PROT-NNN] [--enforce-layout]\n"
                + "  java tools/DesignCorpusCheck.java --preflight MS-PROT-NNN --candidate-version X.Y [--root PATH]");
    }

    private static final class Arguments {
        Path root = Path.of(".");
        boolean full;
        boolean help;
        boolean enforceLayout;
        String pilotId;
        String preflightId;
        String candidateVersion;

        static Arguments parse(String[] args) {
            Arguments a = new Arguments();
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--help", "-h" -> a.help = true;
                    case "--full" -> a.full = true;
                    case "--enforce-layout" -> a.enforceLayout = true;
                    case "--root" -> a.root = Path.of(requireValue(args, ++i, "--root"));
                    case "--pilot" -> a.pilotId = requireValue(args, ++i, "--pilot");
                    case "--preflight" -> a.preflightId = requireValue(args, ++i, "--preflight");
                    case "--candidate-version" -> a.candidateVersion = requireValue(args, ++i, "--candidate-version");
                    default -> throw new IllegalArgumentException("Unknown argument: " + args[i]);
                }
            }
            if (!a.help && a.preflightId == null && !a.full) a.full = true;
            return a;
        }

        private static String requireValue(String[] args, int i, String flag) {
            if (i >= args.length) throw new IllegalArgumentException("Missing value for " + flag);
            return args[i];
        }
    }
}