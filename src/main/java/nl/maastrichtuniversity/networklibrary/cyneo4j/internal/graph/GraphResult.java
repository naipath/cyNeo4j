package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph;

import java.util.*;

public class GraphResult extends GraphObject {

    private Map<String, GraphObject> results = new HashMap<>();

    public void add(String key, GraphObject graphObject) {
        results.put(key, graphObject);
    }

    @Override
    public void accept(GraphVisitor graphVisitor) {
        graphVisitor.visit(this);
    }

    public GraphObject get(String key) {
        return results.get(key);
    }

    public Collection<GraphObject> getAll() {
        return Collections.unmodifiableCollection(results.values());
    }
}
