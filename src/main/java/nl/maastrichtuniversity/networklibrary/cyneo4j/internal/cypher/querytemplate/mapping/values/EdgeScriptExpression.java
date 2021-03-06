package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphEdge;

public class EdgeScriptExpression<V> extends ValueScriptExpression<GraphEdge, V> {

    public EdgeScriptExpression(String script, Class<V> type) {
        super(script, "edge", type);
    }

    @Override
    public void accept(ValueExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
