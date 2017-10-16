package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphEdge;

public class EdgePropertyValue<T> implements ValueExpression<GraphEdge, T> {
    private final String key;
    private final Class<T> type;

    public EdgePropertyValue(String key, Class<T> type) {
        this.key = key;
        this.type = type;
    }

    @Override
    public T eval(GraphEdge val) {
        return val.getProperty(key, type).orElse(null);
    }

    @Override
    public void accept(ValueExpressionVisitor visitor) {
        visitor.visit(this);
    }

    public String getKey() {
        return key;
    }

    public Class<T> getType() {
        return type;
    }
}
