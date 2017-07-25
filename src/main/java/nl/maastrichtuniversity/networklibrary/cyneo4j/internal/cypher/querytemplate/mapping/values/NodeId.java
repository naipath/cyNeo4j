package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values.ValueExpression;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;

public class NodeId implements ValueExpression<GraphNode,Long> {
    @Override
    public Long eval(GraphNode val) {
        return val.getId();
    }
}
