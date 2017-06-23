package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.connect;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ConnectInstanceMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Connect to Instance";
    private final static String MENU_LOC = "Apps.cyNeo4j";

    private final Neo4jClient neo4jClient;
    private final CySwingApplication cySwingApplication;

    public static ConnectInstanceMenuAction create(ServiceLocator serviceLocator) {
        return new ConnectInstanceMenuAction(
                serviceLocator.getService(Neo4jClient.class),
                serviceLocator.getService(CySwingApplication.class));
    }

    private ConnectInstanceMenuAction(Neo4jClient neo4jClient, CySwingApplication cySwingApplication) {
        super(MENU_TITLE);
        this.cySwingApplication = cySwingApplication;
        setPreferredMenu(MENU_LOC);
        setMenuGravity(0.0f);
        this.neo4jClient = neo4jClient;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        ConnectDialog connectDialog = new ConnectDialog(cySwingApplication.getJFrame(), neo4jClient::connect);
        connectDialog.showConnectDialog();
        if(!connectDialog.isOk()) {
            JOptionPane.showInputDialog("Invalid connection parameters");
        }
    }

}
