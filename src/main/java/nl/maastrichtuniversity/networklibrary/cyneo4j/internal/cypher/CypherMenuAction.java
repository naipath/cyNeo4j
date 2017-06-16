package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
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

    private final Neo4jClient neo4JClient;
    private final CyApplicationManager cyApplicationManager;
    private final CySwingApplication cySwingApplication;
    private final CyNetworkFactory cyNetworkFactory;
    private final CyNetworkManager cyNetworkManager;

    public static CypherMenuAction create(ServiceLocator serviceLocator) {
        return new CypherMenuAction(
            serviceLocator.getService(CyApplicationManager.class),
            serviceLocator.getService(Neo4jClient.class),
            serviceLocator.getService(CySwingApplication.class),
            serviceLocator.getService(CyNetworkFactory.class),
            serviceLocator.getService(CyNetworkManager.class)
        );
    }

    private CypherMenuAction(CyApplicationManager cyApplicationManager,
                             Neo4jClient neo4JClient,
                             CySwingApplication cySwingApplication,
                             CyNetworkFactory cyNetworkFactory,
                             CyNetworkManager cyNetworkManager) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        this.neo4JClient = neo4JClient;
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
        CypherExtExec exec = new CypherExtExec(cyApplicationManager, cyNetworkFactory, cyNetworkManager);

        String query = JOptionPane.showInputDialog(cySwingApplication.getJFrame(), "Cypher Query", "match (n)-[r]->(m) return n,r,m")
            .replaceAll("\"", "\\\\\"");

        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(cySwingApplication.getJFrame(), "Failed to collect parameters for ");
            return;
        }
        CypherQuery cypherQuery = CypherQuery.builder().query(query).build();
        Object callRetValue = neo4JClient.executeQuery(cypherQuery);
        exec.processCallResponse(callRetValue);
    }
}
