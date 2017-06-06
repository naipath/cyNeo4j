package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CypherExtExec implements ExtensionExecutor {

    private final Neo4jExtension extension;
    private String query;
    private CyNetwork currNet;
    private final CyApplicationManager cyApplicationManager;
    private final CySwingApplication cySwingApplication;
    private final CyNetworkFactory cyNetworkFactory;
    private final CyNetworkManager cyNetworkManager;

    CypherExtExec(Neo4jExtension extension, CyApplicationManager cyApplicationManager, CySwingApplication cySwingApplication, CyNetworkFactory cyNetworkFactory, CyNetworkManager cyNetworkManager) {
        this.extension = extension;
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
        List<Neo4jCall> calls = new ArrayList<>();

        String urlFragment = extension.getEndpoint();
        String payload = "{\"query\" : \"" + query + "\",\"params\" : {}}";

        calls.add(new Neo4jCall(urlFragment, payload, false));

        return calls;
    }
}
