package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphEdge;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;
import org.cytoscape.model.CyNetwork;

public interface CopyCyNetworkStrategy {
    void createTables(CyNetwork network);
    void addPropertiesNode(CyNetwork network, GraphNode graphNode);
    void addPropertiesEdge(CyNetwork network, GraphEdge graphEdge);
}
