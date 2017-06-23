package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jGraph;
import org.cytoscape.model.CyNetwork;

public class CypherExtExec {

    private final Services services;

    public CypherExtExec(Services services) {
        this.services = services;
    }

    public void processCallResponse(Neo4jGraph<ResultObject> graph) {
        CyNetwork currNet = services.getCyApplicationManager().getCurrentNetwork();

        if (currNet == null) {
            currNet = services.getCyNetworkFactory().createNetwork();
            currNet.getRow(currNet).set(CyNetwork.NAME, "Network");
            services.getCyNetworkManager().addNetwork(currNet);
        }
        new CypherResultParser(currNet).parseRetVal(graph);
    }
}
