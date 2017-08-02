package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.AddEdgeCommand;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.AddNodeCommand;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
import org.cytoscape.model.*;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class CopyDataTask extends AbstractTask {

    private final Services services;

    public CopyDataTask(Services services) {
        this.services = services;
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        try {
            CyNetwork cyNetwork = services.getCyApplicationManager().getCurrentNetwork();
            cyNetwork.getNodeList().forEach(node -> copyNodeToNeo4j(cyNetwork, node));
            cyNetwork.getEdgeList().forEach(edge -> copyEdgeToNeo4j(cyNetwork, edge));

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void copyEdgeToNeo4j(CyNetwork cyNetwork, CyEdge cyEdge) {
        AddEdgeCommand cmd = new AddEdgeCommand();
        CyRow cyRow=cyNetwork.getDefaultNodeTable().getRow(cyEdge.getSUID());
        cmd.setEdgeProperties(cyRow.getAllValues());
        cmd.setStartId(cyEdge.getSource().getSUID());
        cmd.setEndId(cyEdge.getTarget().getSUID());
        cmd.setRelationship("links");
        cmd.setDirected(cyEdge.isDirected());
        services.getNeo4jClient().executeCommand(cmd);

    }

    private void copyNodeToNeo4j(CyNetwork cyNetwork, CyNode cyNode) {
        AddNodeCommand cmd = new AddNodeCommand();
        CyRow cyRow=cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID());
        cmd.setNodeProperties(cyRow.getAllValues());
        cmd.setNodeId(cyNode.getSUID());
        cmd.setNodeLabel(cyNetwork.getRow(cyNetwork).get(CyNetwork.NAME, String.class));
        services.getNeo4jClient().executeCommand(cmd);
    }

}
