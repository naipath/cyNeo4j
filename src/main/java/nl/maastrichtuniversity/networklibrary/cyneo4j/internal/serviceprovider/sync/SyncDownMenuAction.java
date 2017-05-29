package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class SyncDownMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Sync Down";
    private final static String MENU_LOC = "Apps.cyNeo4j";
    private final Neo4jRESTServer neo4jRESTServer;


    public SyncDownMenuAction(CyApplicationManager cyApplicationManager, Neo4jRESTServer neo4jRESTServer) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        setMenuGravity(0.1f);
        this.neo4jRESTServer = neo4jRESTServer;

    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!neo4jRESTServer.isConnected()) {
            JOptionPane.showMessageDialog(null, "Not connected to any remote instance");
            return;
        }
       neo4jRESTServer.syncDown(false);
    }

}
