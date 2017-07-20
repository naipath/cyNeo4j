package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.*;
import org.cytoscape.model.*;

import java.util.List;

public class CreateCyNetworkFromGraphObjectList implements GraphVisitor {

    private static final String COLUMN_REFERENCEID = "refid";
    private final CyNetwork currNet;
    private final CopyCyNetworkStrategy copyCyNetworkStrategy;

    public CreateCyNetworkFromGraphObjectList(CyNetwork network, CopyCyNetworkStrategy copyCyNetworkStrategy) {
        this.currNet = network;
        this.copyCyNetworkStrategy = copyCyNetworkStrategy;
    }

    public void importGraph(List<GraphObject> list) {
        copyCyNetworkStrategy.createTables(currNet);
        list.forEach(item -> item.accept(this));
    }

    @Override
    public void visit(GraphNode graphNode) {
        copyCyNetworkStrategy.addPropertiesNode(currNet, graphNode);
    }

    @Override
    public void visit(GraphEdge graphEdge) {
        copyCyNetworkStrategy.addPropertiesEdge(currNet, graphEdge);
    }

    @Override
    public void visit(GraphResult result) {
        result.getAll().forEach(graphObject -> graphObject.accept(this));
    }

    @Override
    public void visit(GraphLong graphLong) {

    }

    @Override
    public void visit(GraphUnspecifiedType unspecifiedType) {

    }

    @Override
    public void visit(GraphPath graphPath) {
        for(GraphNode node : graphPath.getNodes()) {
            node.accept(this);
        }
        for(GraphEdge edge : graphPath.getEdges()) {
            edge.accept(this);
        }
    }
}
