package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.*;
import org.cytoscape.model.*;

import java.util.*;

public class CreateCyNetworkFromGraphObjectList implements GraphVisitor {

    private static final String COLUMN_REFERENCEID = "refid";
    private CyNetwork currNet;
    private CopyCyNetworkStrategy copyCyNetworkStrategy = new CopyCyNetworkStrategy();

    CreateCyNetworkFromGraphObjectList(CyNetwork network) {
        this.currNet = network;
    }

    void parseRetVal(List<GraphObject> list) {
        CyTable defNodeTab = currNet.getDefaultNodeTable();

        if (defNodeTab.getColumn(COLUMN_REFERENCEID) == null) {
            defNodeTab.createColumn(COLUMN_REFERENCEID, Long.class, false);
        }
        CyTable defEdgeTab = currNet.getDefaultEdgeTable();
        if (defEdgeTab.getColumn(COLUMN_REFERENCEID) == null) {
            defEdgeTab.createColumn(COLUMN_REFERENCEID, Long.class, false);
        }
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
    public void visit(GraphLong neo4jLong) {

    }

    @Override
    public void visit(GraphUnspecifiedType neo4jUnspecifiedType) {

    }
}
