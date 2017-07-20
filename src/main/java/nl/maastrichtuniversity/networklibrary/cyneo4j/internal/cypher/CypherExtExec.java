package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphObject;
import org.cytoscape.model.CyNetwork;

import java.util.List;

public class CypherExtExec {

    private final Services services;

    public CypherExtExec(Services services) {
        this.services = services;
    }

    public void processCallResponse(List<GraphObject> graph) {
        CyNetwork currNet = services.getCyApplicationManager().getCurrentNetwork();

        if (currNet == null) {
            currNet = services.getCyNetworkFactory().createNetwork();
            currNet.getRow(currNet).set(CyNetwork.NAME, "Network");
            services.getCyNetworkManager().addNetwork(currNet);
        }
        new CreateCyNetworkFromGraphObjectList(currNet, new BasicCopyCyNetworkStrategy()).importGraph(graph);
    }
}
