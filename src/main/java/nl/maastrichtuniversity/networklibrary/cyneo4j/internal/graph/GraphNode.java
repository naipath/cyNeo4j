package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphNode extends GraphObject {

    private Map<String, Object> properties;
    private List<String> labels = new ArrayList<>();
    private long id;

    @Override
    public void accept(GraphVisitor graphVisitor) {
        graphVisitor.visit(this);
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void addLabel(String label) {
        labels.add(label);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
