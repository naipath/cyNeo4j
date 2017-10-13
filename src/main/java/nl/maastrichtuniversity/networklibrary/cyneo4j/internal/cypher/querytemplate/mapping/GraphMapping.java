package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.MappingStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.MappingStrategyVisitor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values.ValueExpression;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphEdge;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphMapping implements MappingStrategy {

    private final String nodeReferenceIdColumn;
    private final String edgeReferenceIdColumn;
    private List<NodeColumnMapping> nodeColumnMapping;
    private List<EdgeColumnMapping> edgeColumnMapping;

    public GraphMapping(
            List<NodeColumnMapping> nodeColumnMapping,
            List<EdgeColumnMapping> edgeColumnMapping,
            String nodeReferenceIdColumn,
            String edgeReferenceIdColumn) {
        this.nodeColumnMapping = nodeColumnMapping;
        this.edgeColumnMapping = edgeColumnMapping;
        this.nodeReferenceIdColumn = nodeReferenceIdColumn;
        this.edgeReferenceIdColumn = edgeReferenceIdColumn;
    }

    public List<NodeColumnMapping> getNodeColumnMapping() {
        return nodeColumnMapping;
    }

    public List<EdgeColumnMapping> getEdgeColumnMapping() {
        return edgeColumnMapping;
    }

    public String getNodeReferenceIdColumn() {
        return nodeReferenceIdColumn;
    }

    public String getEdgeReferenceIdColumn() {
        return edgeReferenceIdColumn;
    }

    @Override
    public void accept(MappingStrategyVisitor visitor) {
        visitor.visit(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<NodeColumnMapping> nodeColumnMapping = new ArrayList<>();
        private List<EdgeColumnMapping> edgeColumnMapping = new ArrayList<>();
        private String nodeReferenceIdColumn;
        private String edgeReferenceIdColumn;


        public <T> Builder addNodeColumnMapping(String columnName, Class<T> type, ValueExpression<GraphNode, T> valueExpression) {
            nodeColumnMapping.add(new NodeColumnMapping(columnName, type, valueExpression));
            return this;
        }

        public <T> Builder addEdgeColumnMapping(String columnName, Class<T> type, ValueExpression<GraphEdge, T> valueExpression) {
            edgeColumnMapping.add(new EdgeColumnMapping(columnName, type, valueExpression));
            return this;
        }

        public Builder setNodeReferenceIdColumn(String nodeReferenceIdColumn) {
            this.nodeReferenceIdColumn = nodeReferenceIdColumn;
            return this;
        }
        public Builder setEdgeReferenceIdColumn(String edgeReferenceIdColumn) {
            this.edgeReferenceIdColumn = edgeReferenceIdColumn;
            return this;
        }

        public GraphMapping build() {
            return new GraphMapping(
                    nodeColumnMapping,
                    edgeColumnMapping,
                    nodeReferenceIdColumn,
                    edgeReferenceIdColumn
            );
        }
    }
}
