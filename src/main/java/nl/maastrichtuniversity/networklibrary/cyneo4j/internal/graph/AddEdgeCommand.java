package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph;

import java.util.Map;

public class AddEdgeCommand {
    private Map<String, Object> edgeProperties;
    private Long startId;
    private Long endId;
    private boolean directed;
    private String relationship;

    public void setEdgeProperties(Map<String, Object> edgeProperties) {
        this.edgeProperties = edgeProperties;
    }

    public Map<String, Object> getEdgeProperties() {
        return edgeProperties;
    }

    public void setStartId(Long startId) {
        this.startId = startId;
    }

    public Long getStartId() {
        return startId;
    }

    public void setEndId(Long endId) {
        this.endId = endId;
    }

    public Long getEndId() {
        return endId;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public boolean isDirected() {
        return directed;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRelationship() {
        return relationship;
    }
}
