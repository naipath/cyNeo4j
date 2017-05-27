package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import org.cytoscape.model.CyNetwork;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CypherExtExec implements ExtensionExecutor {

    private Plugin plugin;
    private Neo4jExtension extension;
    private String query;
    private CyNetwork currNet;

    @Override
    public boolean collectParameters() {

        query = JOptionPane.showInputDialog(plugin.getCySwingApplication().getJFrame(), "Cypher Query", "match (n)-[r]->(m) return n,r,m");
//		query = "match (n)-[r]->(m) return n,r,m";

        currNet = getPlugin().getCyApplicationManager().getCurrentNetwork();

        query = query.replaceAll("\"", "\\\\\"");

        return query != null && !query.isEmpty();
    }

    @Override
    public void processCallResponse(Neo4jCall call, Object callRetValue) {
        if (currNet == null) {
            currNet = getPlugin().getCyNetworkFactory().createNetwork();
            currNet.getRow(currNet).set(CyNetwork.NAME, query);
            getPlugin().getCyNetworkManager().addNetwork(currNet);
        }
        CypherResultParser cypherResParser = new CypherResultParser(currNet);
        cypherResParser.parseRetVal(callRetValue);

    }

    @Override
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void setExtension(Neo4jExtension extension) {
        this.extension = extension;
    }

    @Override
    public List<Neo4jCall> buildExtensionCalls() {
        List<Neo4jCall> calls = new ArrayList<>();

        String urlFragment = extension.getEndpoint();
        String payload = "{\"query\" : \"" + query + "\",\"params\" : {}}";

        calls.add(new Neo4jCall(urlFragment, payload, false));

        return calls;
    }

    protected Plugin getPlugin() {
        return plugin;
    }

}
