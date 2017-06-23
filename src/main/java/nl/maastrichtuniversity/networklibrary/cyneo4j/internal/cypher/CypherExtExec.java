package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jGraph;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;

class CypherExtExec {

    private final ServiceLocator serviceLocator;

    CypherExtExec(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    void processCallResponse(Neo4jGraph graph) {
        CyNetwork currNet = serviceLocator.getCyApplicationManager().getCurrentNetwork();

        if (currNet == null) {
            currNet = serviceLocator.getCyNetworkFactory().createNetwork();
            currNet.getRow(currNet).set(CyNetwork.NAME, "Network");
            serviceLocator.getCyNetworkManager().addNetwork(currNet);
        }
        new CypherResultParser(currNet).parseRetVal(graph);
    }
}
