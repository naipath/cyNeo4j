package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jGraph;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;

class CypherExtExec {

    private final CyApplicationManager cyApplicationManager;
    private final CyNetworkFactory cyNetworkFactory;
    private final CyNetworkManager cyNetworkManager;

    CypherExtExec(CyApplicationManager cyApplicationManager, CyNetworkFactory cyNetworkFactory, CyNetworkManager cyNetworkManager) {
        this.cyApplicationManager = cyApplicationManager;
        this.cyNetworkFactory = cyNetworkFactory;
        this.cyNetworkManager = cyNetworkManager;
    }

    void processCallResponse(Neo4jGraph graph) {
        CyNetwork currNet = cyApplicationManager.getCurrentNetwork();

        if (currNet == null) {
            currNet = cyNetworkFactory.createNetwork();
            currNet.getRow(currNet).set(CyNetwork.NAME, "Network");
            cyNetworkManager.addNetwork(currNet);
        }
        new CypherResultParser(currNet).parseRetVal(graph);
    }
}
