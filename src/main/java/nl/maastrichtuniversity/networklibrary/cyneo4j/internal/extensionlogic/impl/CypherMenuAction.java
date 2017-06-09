package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CypherMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Cypher Query";
    private final static String MENU_LOC = "Apps.cyNeo4j";

    private final Neo4jRESTServer neo4jRESTServer;
    private final CyApplicationManager cyApplicationManager;
    private final CySwingApplication cySwingApplication;
    private final CyNetworkFactory cyNetworkFactory;
    private final CyNetworkManager cyNetworkManager;

    public static CypherMenuAction create(ServiceLocator serviceLocator) {
        return new CypherMenuAction(
                serviceLocator.getService(CyApplicationManager.class),
                serviceLocator.getService(Neo4jRESTServer.class),
                serviceLocator.getService(CySwingApplication.class),
                serviceLocator.getService(CyNetworkFactory.class),
                serviceLocator.getService(CyNetworkManager.class)
        );
    }

    private CypherMenuAction(
            CyApplicationManager cyApplicationManager,
            Neo4jRESTServer neo4jRESTServer,
            CySwingApplication cySwingApplication,
            CyNetworkFactory cyNetworkFactory,
            CyNetworkManager cyNetworkManager
    ) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        this.neo4jRESTServer = neo4jRESTServer;
        this.cyApplicationManager = cyApplicationManager;
        this.cySwingApplication = cySwingApplication;
        this.cyNetworkFactory = cyNetworkFactory;
        this.cyNetworkManager = cyNetworkManager;
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        setMenuGravity(0.5f);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url = neo4jRESTServer.getCypherURL();
        ExtensionExecutor exec = new CypherExtExec(url, cyApplicationManager,  cySwingApplication,  cyNetworkFactory,  cyNetworkManager);

        if (!exec.collectParameters()) {
            JOptionPane.showMessageDialog(cySwingApplication.getJFrame(), "Failed to collect parameters for ");
            return;
        }

        List<Neo4jCall> calls = exec.buildExtensionCalls();

        for (Neo4jCall call : calls) {
            Object callRetValue = neo4jRESTServer.executeExtensionCall(call, false);
            exec.processCallResponse(call, callRetValue);
        }
    }
}
