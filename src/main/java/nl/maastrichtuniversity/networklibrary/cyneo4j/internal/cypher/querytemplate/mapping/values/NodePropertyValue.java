package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values.ValueExpression;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;

public class NodePropertyValue<T> implements ValueExpression<GraphNode, T> {
    private final String key;
    private final Class<T> type;

    public NodePropertyValue(String key, Class<T> type) {
        this.key = key;
        this.type = type;
    }

    @Override
    public T eval(GraphNode val) {
        return val.getProperty(key, type).orElse(null);
    }
}
