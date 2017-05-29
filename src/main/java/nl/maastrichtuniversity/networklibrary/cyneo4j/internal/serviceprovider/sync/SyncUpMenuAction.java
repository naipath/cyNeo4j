package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SyncUpMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Sync Up";
    private final static String MENU_LOC = "Apps.cyNeo4j";
    private final CyApplicationManager cyApplicationManager;
    private final Neo4jRESTServer neo4jRESTServer;


    public SyncUpMenuAction(CyApplicationManager cyApplicationManager, Neo4jRESTServer neo4jRESTServer) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        setMenuGravity(0.1f);
        this.cyApplicationManager = cyApplicationManager;
        this.neo4jRESTServer = neo4jRESTServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!neo4jRESTServer.isConnected()) {
            JOptionPane.showMessageDialog(null, "Not connected to any remote instance");
        } else {
            neo4jRESTServer.syncUp(true, cyApplicationManager.getCurrentNetwork());
        }
    }
}
