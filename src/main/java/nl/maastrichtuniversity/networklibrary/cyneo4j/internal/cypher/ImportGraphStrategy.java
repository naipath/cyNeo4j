package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphEdge;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;
import org.cytoscape.model.CyNetwork;

public interface ImportGraphStrategy {
    void createTables(CyNetwork network);
    void handleNode(CyNetwork network, GraphNode graphNode);
    void handleEdge(CyNetwork network, GraphEdge graphEdge);
}
