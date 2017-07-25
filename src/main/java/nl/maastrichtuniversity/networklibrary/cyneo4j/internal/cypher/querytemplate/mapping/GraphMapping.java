package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping;

import java.util.List;

public class GraphMapping {

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
}
