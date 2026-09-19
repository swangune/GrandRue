package grandrue.governance;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Completion evidence must not disappear when navigation narrows to one branch.
 * MS-IMP-001 v1.0, designs/authorities/programme/MS-IMP-001/MS-IMP-001.md, §33 — Target Completion Gate;
 * §36 — Graph Refinement; MS-IMPLEMENTATION-RULES-001 v1.7,
 * designs/IMPLEMENTATION-RULES.md, §52.9 — Existing implementation satisfaction.
 */
class ImplementationGraphIntegrityTest {
    private static final Path GRAPH = Path.of("docs/development/implementation-programme-state.json");
    private static final Path HISTORY = Path.of("docs/development",
            "implementation-status-history-2026-09-14-pre-c2b-closure.md");

    record Macro(String id, String state, List<String> evidence) {}
    record Edge(String from, String to, String type) {}
    record Gate(String target, List<String> allOf, List<String> oneOf, String qualification) {}
    record Family(String id, String state, String authority) {}
    record Node(String id, String macro, String state, List<String> requires, List<String> evidence, String reason) {}
    record Graph(String baseline, List<Macro> macros, List<Edge> edges, List<Gate> completionGates,
                 List<Edge> conditionalEdges, List<Family> families, List<Node> nodes,
                 List<String> inventorySources) {}

    private static Graph graph() throws Exception {
        return new ObjectMapper().readValue(Files.readString(GRAPH), Graph.class);
    }

    @Test
    void current_navigation_closes_configuration_only_with_general_lifecycle_proof() throws Exception {
        String controller = Files.readString(Path.of("IMPLEMENTATION.md"));
        String history = Files.readString(HISTORY);
        assertTrue(controller.contains("id: IMP-08C"));
        assertTrue(controller.contains("state: IN_PROGRESS"));
        assertTrue(controller.contains("canonical_graph: docs/development/implementation-programme-state.json"));
        assertTrue(controller.contains("historical_status_compatibility: docs/development/implementation-status.md"));
        String compatibility = Files.readString(Path.of("docs/development/implementation-status.md"));
        assertTrue(compatibility.contains(HISTORY.getFileName().toString()));
        assertTrue(history.contains("IMP-05") && history.contains("CONFORMING_COMPLETE"));
        assertTrue(history.contains("imp-programme-integrity-review-2026-09-06.md"));
        assertTrue(history.contains("imp-05-r3b-reinstatement-conformance-2026-09-12.md"));
        assertEquals("CONFORMING_COMPLETE", macros(graph()).get("IMP-05").state());
        assertEquals("CONFORMING_COMPLETE", macros(graph()).get("IMP-06").state());
        assertEquals("CONFORMING_COMPLETE", macros(graph()).get("IMP-07").state());
        assertEquals("IN_PROGRESS", macros(graph()).get("IMP-08C").state());
    }

    @Test
    void every_accepted_macro_and_explicit_hard_or_programme_edge_is_represented() throws Exception {
        var graph = graph();
        String authority = Files.readString(Path.of("designs/authorities/programme/MS-IMP-001/MS-IMP-001.md"));
        var expected = new HashSet<String>();
        var sections = Pattern.compile("(?m)^## \\d+\\. (IMP-\\d{2}[ABC]?) — ").matcher(authority);
        var starts = new ArrayList<Integer>();
        var ids = new ArrayList<String>();
        while (sections.find()) { expected.add(sections.group(1)); ids.add(sections.group(1)); starts.add(sections.start()); }
        expected.add("IMP-WF-01");
        assertEquals(expected, macros(graph).keySet());
        var expectedEdges = new HashSet<Edge>();
        for (int i = 0; i < ids.size(); i++) {
            String section = authority.substring(starts.get(i), i + 1 < ids.size() ? starts.get(i + 1) : authority.length());
            var depends = Pattern.compile("Depends:\\s*```text\\s*(.*?)```", Pattern.DOTALL).matcher(section);
            if (depends.find()) {
                var edges = Pattern.compile("(IMP-\\d{2}[ABC]?) (HARD|PROGRAMME_GATE)").matcher(depends.group(1));
                while (edges.find()) expectedEdges.add(new Edge(edges.group(1), ids.get(i), edges.group(2)));
            }
        }
        expectedEdges.add(new Edge("IMP-08B", "IMP-WF-01", "HARD"));
        assertEquals(expectedEdges, new HashSet<>(graph.edges()), "Do not omit or invent macro edges");
        assertEquals(expectedEdges.size(), graph.edges().size(), "Duplicate macro edge");
        assertEquals(graph.macros().size(), macros(graph).size(), "Duplicate macro identity");
    }

    @Test
    void readiness_is_dependency_closed_and_the_hard_graph_has_no_cycles() throws Exception {
        var graph = graph();
        validateReadiness(graph.macros(), graph.edges());
        for (var node : graph.macros()) visit(node.id(), graph.edges(), new HashSet<>(), new HashSet<>());
        var invalid = new ArrayList<>(graph.macros());
        invalid.removeIf(m -> m.id().equals("IMP-09"));
        invalid.add(new Macro("IMP-09", "READY", List.of()));
        assertThrows(AssertionError.class, () -> validateReadiness(invalid, graph.edges()),
                "A downstream target must not bypass a currently incomplete prerequisite");
    }

    @Test
    void completion_and_conditional_gates_preserve_the_workforce_and_payment_distinctions() throws Exception {
        var graph = graph();
        var hardening = graph.completionGates().stream().filter(g -> g.target().equals("IMP-16")).findFirst().orElseThrow();
        assertTrue(hardening.allOf().containsAll(List.of("IMP-08A", "IMP-08B", "IMP-08C", "IMP-10",
                "IMP-11", "IMP-12", "IMP-13", "IMP-14", "IMP-15", "IMP-WF-01")));
        var live = graph.completionGates().stream().filter(g -> g.target().equals("IMP-20")).findFirst().orElseThrow();
        var allBeforeLive = new HashSet<>(macros(graph).keySet());
        allBeforeLive.remove("IMP-20");
        assertEquals(allBeforeLive, new HashSet<>(live.allOf()));
        for (var gate : graph.completionGates()) {
            String state = macros(graph).get(gate.target()).state();
            boolean gateApplies = state.equals("CONFORMING_COMPLETE")
                    || (gate.target().equals("IMP-20") && Set.of("READY", "IN_PROGRESS").contains(state));
            if (gateApplies) for (String prerequisite : gate.allOf()) {
                assertEquals("CONFORMING_COMPLETE", macros(graph).get(prerequisite).state(), gate.target());
            }
        }
        var payment = graph.completionGates().stream().filter(g -> g.target().equals("IMP-13")).findFirst().orElseThrow();
        assertEquals(Set.of("IMP-11", "IMP-12"), new HashSet<>(payment.oneOf()));
        assertTrue(payment.qualification().contains("producer"));
        assertFalse(graph.edges().stream().anyMatch(e -> e.to().equals("IMP-13") && payment.oneOf().contains(e.from())));
        assertEquals("NOT_READY_APPROVED_DECOMPOSITION_REQUIRED", graph.families().getFirst().state());
        assertFalse(graph.edges().stream().anyMatch(e -> e.to().equals("IMP-WC")));
        assertEquals(4, graph.conditionalEdges().stream().filter(e -> e.to().equals("IMP-WF-01")).count());
        assertTrue(Files.readString(Path.of(
                "designs/authorities/ms-prot/MS-PROT-065/MS-PROT-065 v1.1 — Production Durable Background Work, Timer, Attempt & Retry Execution Contract Amendment.md"))
                .contains("Not every Event Reaction requires durable work."));
    }

    @Test
    void retained_children_and_known_corrections_have_resolvable_evidence_and_dependencies() throws Exception {
        var graph = graph();
        var ids = new HashSet<>(macros(graph).keySet());
        for (var node : graph.nodes()) assertTrue(ids.add(node.id()), "Duplicate node: " + node.id());
        for (String required : List.of("IMP-05-R1", "IMP-05-R2", "IMP-05-R3", "IMP-08C-C2B",
                "IMP-08C-C3", "IMP-08C-C4B2", "IMP-08C-C4E", "IMP-08C-C5", "IMP-10-N1",
                "IMP-06-T4c", "IMP-07-P6")) assertTrue(ids.contains(required), required);
        for (var node : graph.nodes()) {
            assertTrue(macros(graph).containsKey(node.macro()), node.id());
            assertTrue(ids.containsAll(node.requires()), node.id());
            assertFalse(node.evidence().isEmpty(), node.id());
            for (String evidence : node.evidence()) assertTrue(Files.isRegularFile(Path.of(evidence)), evidence);
            if (node.state().equals("READY")) {
                assertTrue(graph.edges().stream().filter(e -> e.to().equals(node.macro()))
                        .allMatch(e -> macros(graph).get(e.from()).state().equals("CONFORMING_COMPLETE")), node.id());
                for (String dependency : node.requires()) {
                    assertTrue(graph.nodes().stream().anyMatch(n -> n.id().equals(dependency)
                            && n.state().equals("CONFORMING_COMPLETE")), dependency);
                }
            }
        }
        for (var macro : graph.macros()) for (String evidence : macro.evidence()) {
            assertTrue(Files.isRegularFile(Path.of(evidence)), evidence);
        }
        for (String source : graph.inventorySources()) assertTrue(Files.isRegularFile(Path.of(source)), source);
    }

    @Test
    void declared_child_completion_requires_complete_dependencies_and_child_graph_is_acyclic() throws Exception {
        var graph = graph();
        validateChildCompletion(graph.nodes());
        var childEdges = new ArrayList<Edge>();
        for (var node : graph.nodes()) for (String prerequisite : node.requires()) {
            childEdges.add(new Edge(prerequisite, node.id(), "HARD"));
        }
        for (var node : graph.nodes()) visit(node.id(), childEdges, new HashSet<>(), new HashSet<>());
        var invalid = List.of(
                new Node("parent", "IMP-05", "CONFORMING_COMPLETE", List.of("child"), List.of(), "fixture"),
                new Node("child", "IMP-05", "IN_PROGRESS", List.of(), List.of(), "fixture"));
        assertThrows(AssertionError.class, () -> validateChildCompletion(invalid),
                "A completed parent cannot hide an unfinished declared prerequisite");
    }

    private static void validateChildCompletion(List<Node> nodes) {
        for (var node : nodes) {
            if (!Set.of("READY", "IN_PROGRESS", "CONFORMING_COMPLETE").contains(node.state())) continue;
            for (String dependency : node.requires()) {
                assertTrue(nodes.stream().anyMatch(n -> n.id().equals(dependency)
                        && n.state().equals("CONFORMING_COMPLETE")), node.id() + " requires " + dependency);
            }
        }
    }

    private static Map<String, Macro> macros(Graph graph) {
        var result = new HashMap<String, Macro>();
        for (var macro : graph.macros()) result.put(macro.id(), macro);
        return result;
    }

    private static void validateReadiness(List<Macro> macros, List<Edge> edges) {
        var states = new HashMap<String, String>();
        macros.forEach(m -> states.put(m.id(), m.state()));
        for (var edge : edges) {
            assertTrue(states.containsKey(edge.from()) && states.containsKey(edge.to()));
            if (Set.of("READY", "IN_PROGRESS", "CONFORMING_COMPLETE").contains(states.get(edge.to()))) {
                assertEquals("CONFORMING_COMPLETE", states.get(edge.from()), edge.to() + " requires " + edge.from());
            }
        }
    }

    private static void visit(String id, List<Edge> edges, Set<String> active, Set<String> visited) {
        if (visited.contains(id)) return;
        assertTrue(active.add(id), "Dependency cycle at " + id);
        for (var edge : edges) if (edge.to().equals(id)) visit(edge.from(), edges, active, visited);
        active.remove(id);
        visited.add(id);
    }
}
