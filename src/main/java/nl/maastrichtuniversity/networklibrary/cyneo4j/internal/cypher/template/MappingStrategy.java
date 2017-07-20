package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.CopyCyNetworkStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.mapping.EdgeMapping;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.mapping.GraphToCytoscapeMapping;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.mapping.NodeLabelMapping;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.mapping.NodeMapping;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphEdge;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;
import org.cytoscape.model.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;
import java.util.Optional;

//TODO: add scripting to evaluate values: ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
public class MappingStrategy implements CopyCyNetworkStrategy {

    private final GraphToCytoscapeMapping graphToCytoscapeMapping;

    public MappingStrategy(GraphToCytoscapeMapping graphToCytoscapeMapping) {
        this.graphToCytoscapeMapping = graphToCytoscapeMapping;
    }

    @Override
    public void createTables(CyNetwork network) {
        CyTable nodeTable = network.getDefaultNodeTable();
        CyTable edgeTable = network.getDefaultEdgeTable();
        CyTable cyNetworkTable= network.getDefaultNetworkTable();

        for(NodeMapping nodeMappping : graphToCytoscapeMapping.getNodeMapping()) {
            nodeTable.createColumn(nodeMappping.getColumnName(), nodeMappping.getColumnType(), true);
        }

        for(NodeLabelMapping nodeLabelMapping : graphToCytoscapeMapping.getNodeLabelMapping()) {
            nodeTable.createColumn(nodeLabelMapping.getColumnName(), String.class, true);
        }

        NodeMapping<?> nodeReferenceIdMapping = graphToCytoscapeMapping.getNodeReferenceIdMapping();
        nodeTable.createColumn(nodeReferenceIdMapping.getColumnName(), nodeReferenceIdMapping.getColumnType(),true);

        for(EdgeMapping edgeMappping : graphToCytoscapeMapping.getEdgeMapping()) {
            edgeTable.createColumn(edgeMappping.getColumnName(), edgeMappping.getColumnType(), true);
        }
        EdgeMapping<?> edgeReferenceIdMapping = graphToCytoscapeMapping.getEdgeReferenceIdMapping();
        edgeTable.createColumn(edgeReferenceIdMapping.getColumnName(), edgeReferenceIdMapping.getColumnType(), true);

        EdgeMapping<?> edgeTypeMapping = graphToCytoscapeMapping.getEdgeTypeMapping();
        edgeTable.createColumn(edgeTypeMapping.getColumnName(), edgeTypeMapping.getColumnType(), true);

    }

    @Override
    public void addPropertiesNode(CyNetwork network, GraphNode graphNode) {
        CyNode cyNode = getNodeByIdOrElseCreate(network, graphNode.getId());
        CyRow cyRow = network.getRow(cyNode);

        for(Map.Entry<String, Object> property : graphNode.getProperties().entrySet()) {
            NodeMapping nodeMapping = graphToCytoscapeMapping.getNodeMappingByPropertyKey(property.getKey());
            cyRow.set(nodeMapping.getColumnName(), nodeMapping.asValue(property.getValue()));
        }

        for(String label : graphNode.getLabels()) {
            graphToCytoscapeMapping.getNodeLabelMapping().forEach(nodeLabelMapping -> {
                if(nodeLabelMapping.matches(label)) {
                    cyRow.set(nodeLabelMapping.getColumnName(), label);
                }
            });
        }
        //TODO: add mapping for name
        cyRow.set("name", graphNode.getLabels().stream().findFirst().orElse("?"));
    }

    @Override
    public void addPropertiesEdge(CyNetwork network, GraphEdge graphEdge) {

        Long start = graphEdge.getStart();
        Long end = graphEdge.getEnd();

        CyNode startNode = getNodeByIdOrElseCreate(network, start);
        CyNode endNode = getNodeByIdOrElseCreate(network, end);
        CyEdge cyEdge = network.addEdge(startNode, endNode, true);
        CyRow cyRow = network.getRow(cyEdge);

        for(Map.Entry<String, Object> property : graphEdge.getProperties().entrySet()) {
            EdgeMapping edgeMapping = graphToCytoscapeMapping.getEdgeMappingByPropertyKey(property.getKey());
            cyRow.set(edgeMapping.getColumnName(), edgeMapping.asValue(property.getValue()));
        }

        EdgeMapping referenceIdMapping = graphToCytoscapeMapping.getEdgeReferenceIdMapping();
        cyRow.set(referenceIdMapping.getColumnName(), graphEdge.getId());

        EdgeMapping typeMapping = graphToCytoscapeMapping.getEdgeTypeMapping();
        cyRow.set(typeMapping.getColumnName(), graphEdge.getType());

        //TODO: something with name and interaction
        cyRow.set(CyEdge.INTERACTION, graphEdge.getType());
        cyRow.set("name", graphEdge.getType());
    }

    private CyNode getNodeByIdOrElseCreate(CyNetwork currNet, Long id) {
        return getNodeById(currNet, id).orElseGet(() -> createNewNode(currNet, id));
    }

    private Optional<CyNode> getNodeById(CyNetwork network, Long id) {
        String primaryKeyColumnName =network.getDefaultNodeTable().getPrimaryKey().getName();
        NodeMapping referenceMapping = graphToCytoscapeMapping.getNodeReferenceIdMapping();
        return network
                .getDefaultNodeTable()
                .getMatchingRows(referenceMapping.getColumnName(), id)
                .stream()
                .findFirst()
                .map(row -> network.getNode(row.get(primaryKeyColumnName, Long.class)));
    }

    private CyNode createNewNode(CyNetwork currNet, Long id) {
        NodeMapping referenceMapping = graphToCytoscapeMapping.getNodeReferenceIdMapping();
        CyNode newNode = currNet.addNode();
        currNet.getRow(newNode).set(referenceMapping.getColumnName(), id);
        return newNode;
    }
}
