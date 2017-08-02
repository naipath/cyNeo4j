package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.writenetwork;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.AddEdgeCommand;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.AddNodeCommand;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class WriteNetworkToNeo4jDataTask extends AbstractTask {

    private final Services services;

    public WriteNetworkToNeo4jDataTask(Services services) {
        this.services = services;
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        try {
            taskMonitor.setStatusMessage("Export network to Neo4j");
            CyNetwork cyNetwork = services.getCyApplicationManager().getCurrentNetwork();

            if(cyNetwork == null) {
                taskMonitor.showMessage(TaskMonitor.Level.WARN, "No network selected");
            } else {
                taskMonitor.setStatusMessage("Exporting nodes");
                cyNetwork.getNodeList().forEach(node -> copyNodeToNeo4j(cyNetwork, node));
                taskMonitor.setStatusMessage("Exporting edges");
                cyNetwork.getEdgeList().forEach(edge -> copyEdgeToNeo4j(cyNetwork, edge));
            }

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
        services.getNeo4jClient().executeCommand(cmd);
    }
}
