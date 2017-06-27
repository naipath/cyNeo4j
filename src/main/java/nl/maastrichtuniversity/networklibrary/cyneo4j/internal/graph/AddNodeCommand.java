package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph;

import java.util.Map;

public class AddNodeCommand {
    private Map<String, Object> nodeProperties;
    private Long nodeId;

    public void setNodeProperties(Map<String, Object> nodeProperties) {
        this.nodeProperties = nodeProperties;
    }

    public Map<String, Object> getNodeProperties() {
        return nodeProperties;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getNodeId() {
        return nodeId;
    }
}
