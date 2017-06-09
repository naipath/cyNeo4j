package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RetrieveDataMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Retrieve data";
    private final static String MENU_LOC = "Apps.cyNeo4j";
    private final Neo4jRESTClient neo4JRESTClient;

    public static RetrieveDataMenuAction create(ServiceLocator serviceLocator) {
        return new RetrieveDataMenuAction(
                serviceLocator.getService(CyApplicationManager.class),
                serviceLocator.getService(Neo4jRESTClient.class)
        );
    }


    private RetrieveDataMenuAction(CyApplicationManager cyApplicationManager, Neo4jRESTClient neo4JRESTClient) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        setMenuGravity(0.1f);
        this.neo4JRESTClient = neo4JRESTClient;

    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!neo4JRESTClient.isConnected()) {
            JOptionPane.showMessageDialog(null, "Not connected to any remote instance");
            return;
        }
       neo4JRESTClient.syncDown();
    }

}
