package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;

import javax.swing.*;
import java.util.List;

import static java.util.Collections.singletonList;

public class CypherExtExec implements ExtensionExecutor {

    private String query;
    private CyNetwork currNet;
    private String url;
    private final CyApplicationManager cyApplicationManager;
    private final CySwingApplication cySwingApplication;
    private final CyNetworkFactory cyNetworkFactory;
    private final CyNetworkManager cyNetworkManager;

    CypherExtExec(String url, CyApplicationManager cyApplicationManager, CySwingApplication cySwingApplication, CyNetworkFactory cyNetworkFactory, CyNetworkManager cyNetworkManager) {
        this.url = url;
        this.cyApplicationManager = cyApplicationManager;
        this.cySwingApplication = cySwingApplication;
        this.cyNetworkFactory = cyNetworkFactory;
        this.cyNetworkManager = cyNetworkManager;
    }

    @Override
    public boolean collectParameters() {

        currNet = cyApplicationManager.getCurrentNetwork();

        query = JOptionPane.showInputDialog(cySwingApplication.getJFrame(), "Cypher Query", "match (n)-[r]->(m) return n,r,m");
        query = query.replaceAll("\"", "\\\\\"");

        return !query.isEmpty();
    }

    @Override
    public void processCallResponse(Neo4jCall call, Object callRetValue) {
        if (currNet == null) {
            currNet = cyNetworkFactory.createNetwork();
            currNet.getRow(currNet).set(CyNetwork.NAME, query);
            cyNetworkManager.addNetwork(currNet);
        }
        CypherResultParser cypherResParser = new CypherResultParser(currNet);
        cypherResParser.parseRetVal(callRetValue);

    }

    @Override
    public List<Neo4jCall> buildExtensionCalls() {
        return singletonList(new Neo4jCall(url, "{\"query\" : \"" + query + "\",\"params\" : {}}"));
    }
}
