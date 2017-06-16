package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.retrievedata;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RetrieveDataMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Retrieve data";
    private final static String MENU_LOC = "Apps.cyNeo4j";
    private final Neo4jClient neo4JClient;

    public static RetrieveDataMenuAction create(ServiceLocator serviceLocator) {
        return new RetrieveDataMenuAction(
                serviceLocator.getService(CyApplicationManager.class),
                serviceLocator.getService(Neo4jClient.class)
        );
    }


    private RetrieveDataMenuAction(CyApplicationManager cyApplicationManager, Neo4jClient neo4jClient) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        setMenuGravity(0.1f);
        this.neo4JClient = neo4jClient;

    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!neo4JClient.isConnected()) {
            JOptionPane.showMessageDialog(null, "Not connected to any remote instance");
            return;
        }
       neo4JClient.syncDown();
    }

}
