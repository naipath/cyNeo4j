package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.*;
import org.cytoscape.model.CyNetwork;

import java.util.List;

public class ImportGraphToNetwork implements GraphVisitor {

    private final CyNetwork network;
    private final ImportGraphStrategy importGraphStrategy;

    public ImportGraphToNetwork(CyNetwork network, ImportGraphStrategy importGraphStrategy) {
        this.network = network;
        this.importGraphStrategy = importGraphStrategy;
    }

    public void importGraph(List<GraphObject> list) {
        importGraphStrategy.createTables(network);
        list.forEach(item -> item.accept(this));
    }

    @Override
    public void visit(GraphNode graphNode) {
        importGraphStrategy.handleNode(network, graphNode);
    }

    @Override
    public void visit(GraphEdge graphEdge) {
        importGraphStrategy.handleEdge(network, graphEdge);
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
}
