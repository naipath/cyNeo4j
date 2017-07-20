package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.mapping;

import org.cytoscape.model.CyEdge;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphToCytoscapeMapping {
    private List<NodeLabelMapping> nodeLabelMapping;
    private Map<String, NodeMapping<?>> nodeProperyMapping;
    private Map<String, EdgeMapping<?>> edgeMapping;
    private EdgeMapping edgeReferenceIdMapping;
    private EdgeMapping edgeTypeMapping;
    private NodeMapping nodeReferenceIdMapping;
    private CyEdge.Type interaction;

    public GraphToCytoscapeMapping(
            NodeMapping<?> nodeReferenceIdMapping,
            Map<String, NodeMapping<?>> nodePropertyMapping,
            List<NodeLabelMapping> nodeLabelMapping,
            EdgeMapping<?> edgeReferenceIdMapping,
            EdgeMapping<?> edgeLinkTypeMapping,
            CyEdge.Type interaction,
            Map<String, EdgeMapping<?>> edgePropertyMapping) {
        this.nodeReferenceIdMapping = nodeReferenceIdMapping;
        this.nodeProperyMapping = nodePropertyMapping;
        this.nodeLabelMapping = nodeLabelMapping;//TODO
        this.edgeReferenceIdMapping = edgeReferenceIdMapping;
        this.edgeTypeMapping = edgeLinkTypeMapping;
        this.edgeMapping = edgePropertyMapping;
        this.interaction = interaction;
    }

    public Collection<NodeMapping<?>> getNodeMapping() {
        return nodeProperyMapping.values();
    }

    public Collection<NodeLabelMapping> getNodeLabelMapping() {
        return nodeLabelMapping;
    }

    public Collection<EdgeMapping<?>> getEdgeMapping() {
        return edgeMapping.values();
    }

    public NodeMapping getNodeMappingByPropertyKey(String key) {
        return nodeProperyMapping.get(key);
    }

    public EdgeMapping getEdgeMappingByPropertyKey(String key) {
        return edgeMapping.get(key);
    }

    public EdgeMapping getEdgeReferenceIdMapping() {
        return edgeReferenceIdMapping;
    }

    public EdgeMapping getEdgeTypeMapping() {
        return edgeTypeMapping;
    }

    public NodeMapping getNodeReferenceIdMapping() {
        return nodeReferenceIdMapping;
    }

    public CyEdge.Type getInteraction() {
        return interaction;
    }
}
