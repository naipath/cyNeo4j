package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;

public class NodeScriptExpression<T> extends ValueScriptExpression<GraphNode, T> {

    public NodeScriptExpression(String script, Class<T> type) {
        super(script, "node", type);
    }
}
