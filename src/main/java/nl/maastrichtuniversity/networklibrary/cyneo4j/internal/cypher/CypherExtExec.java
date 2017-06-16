package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;

class CypherExtExec {

    private String query;
    private final CyApplicationManager cyApplicationManager;
    private final CyNetworkFactory cyNetworkFactory;
    private final CyNetworkManager cyNetworkManager;

    CypherExtExec(CyApplicationManager cyApplicationManager, CyNetworkFactory cyNetworkFactory, CyNetworkManager cyNetworkManager) {
        this.cyApplicationManager = cyApplicationManager;
        this.cyNetworkFactory = cyNetworkFactory;
        this.cyNetworkManager = cyNetworkManager;
    }

    void processCallResponse(Object callRetValue) {
        CyNetwork currNet = cyApplicationManager.getCurrentNetwork();

        if (currNet == null) {
            currNet = cyNetworkFactory.createNetwork();
            currNet.getRow(currNet).set(CyNetwork.NAME, query);
            cyNetworkManager.addNetwork(currNet);
        }
        new CypherResultParser(currNet).parseRetVal(callRetValue);
    }

    String buildExtensionCalls(String query) {
        this.query = query;
        return String.format("{\"query\" : \"%s\",\"params\" : {}}", query);
    }
}
