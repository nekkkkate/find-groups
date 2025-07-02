package nekkkkate;

import java.util.*;

public class GroupBuilder {
    private final List<String> rows;
    private final Map<Integer, Map<String, List<Integer>>> indexToValueToRows = new HashMap<>();
    private final Map<Integer, Set<Integer>> graph = new HashMap<>();

    public GroupBuilder(List<String> rows) {
        this.rows = rows;
    }

    public List<Set<String>> buildGroups() {
        indexValues();
        buildGraph();
        return findGroups();
    }

    private void indexValues() {
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            String[] columns = rows.get(rowIndex).split(";", -1);
            for (int colIndex = 0; colIndex < columns.length; colIndex++) {
                String value = columns[colIndex].trim();
                if (value.isEmpty()) continue;

                indexToValueToRows
                        .computeIfAbsent(colIndex, k -> new HashMap<>())
                        .computeIfAbsent(value, k -> new ArrayList<>())
                        .add(rowIndex);
            }
        }
    }

    private void buildGraph() {
        for (Map<String, List<Integer>> valueToRows : indexToValueToRows.values()) {
            for (List<Integer> indices : valueToRows.values()) {
                if (indices.size() > 1) {
                    for (int i = 0; i < indices.size(); i++) {
                        for (int j = i + 1; j < indices.size(); j++) {
                            connect(indices.get(i), indices.get(j));
                        }
                    }
                }
            }
        }
    }

    private void connect(int a, int b) {
        graph.computeIfAbsent(a, k -> new HashSet<>()).add(b);
        graph.computeIfAbsent(b, k -> new HashSet<>()).add(a);
    }

    private List<Set<String>> findGroups() {
        Set<Integer> visited = new HashSet<>();
        List<Set<String>> groups = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            if (!visited.contains(i)) {
                Set<Integer> component = new HashSet<>();
                dfs(i, component, visited);

                Set<String> group = new LinkedHashSet<>();
                for (int index : component) {
                    group.add(rows.get(index));
                }

                if (!group.isEmpty()) {
                    groups.add(group);
                }
            }
        }

        return groups;
    }

    private void dfs(int current, Set<Integer> component, Set<Integer> visited) {
        if (visited.contains(current)) return;

        visited.add(current);
        component.add(current);

        Set<Integer> neighbors = graph.getOrDefault(current, Collections.emptySet());
        for (int neighbor : neighbors) {
            dfs(neighbor, component, visited);
        }
    }
}