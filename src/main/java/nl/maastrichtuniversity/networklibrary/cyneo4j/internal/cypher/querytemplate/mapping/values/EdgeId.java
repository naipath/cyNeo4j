package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphEdge;

public class EdgeId implements ValueExpression<GraphEdge,Long> {
    @Override
    public Long eval(GraphEdge val) {
        return val.getId();
    }

    @Override
    public void accept(ValueExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
