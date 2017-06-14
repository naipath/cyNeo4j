package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTClient;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CypherMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Cypher Query";
    private final static String MENU_LOC = "Apps.cyNeo4j";

    private final Neo4jRESTClient neo4JRESTClient;
    private final CyApplicationManager cyApplicationManager;
    private final CySwingApplication cySwingApplication;
    private final CyNetworkFactory cyNetworkFactory;
    private final CyNetworkManager cyNetworkManager;

    public static CypherMenuAction create(ServiceLocator serviceLocator) {
        return new CypherMenuAction(
            serviceLocator.getService(CyApplicationManager.class),
            serviceLocator.getService(Neo4jRESTClient.class),
            serviceLocator.getService(CySwingApplication.class),
            serviceLocator.getService(CyNetworkFactory.class),
            serviceLocator.getService(CyNetworkManager.class)
        );
    }

    private CypherMenuAction(CyApplicationManager cyApplicationManager,
                             Neo4jRESTClient neo4JRESTClient,
                             CySwingApplication cySwingApplication,
                             CyNetworkFactory cyNetworkFactory,
                             CyNetworkManager cyNetworkManager) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        this.neo4JRESTClient = neo4JRESTClient;
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
        String url = neo4JRESTClient.getCypherURL();
        CypherExtExec exec = new CypherExtExec(cyApplicationManager, cyNetworkFactory, cyNetworkManager);

        String query = JOptionPane.showInputDialog(cySwingApplication.getJFrame(), "Cypher Query", "match (n)-[r]->(m) return n,r,m")
            .replaceAll("\"", "\\\\\"");

        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(cySwingApplication.getJFrame(), "Failed to collect parameters for ");
            return;
        }
        Object callRetValue = neo4JRESTClient.executeExtensionCall(url, exec.buildExtensionCalls(query));
        exec.processCallResponse(callRetValue);
    }
}
