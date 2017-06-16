package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.connect;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.rest.Neo4jRESTClient;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ConnectInstanceMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Connect to Instance";
    private final static String MENU_LOC = "Apps.cyNeo4j";

    private final Neo4jRESTClient neo4JRESTClient;
    private final CySwingApplication cySwingApplication;

    public static ConnectInstanceMenuAction create(ServiceLocator serviceLocator) {
        return new ConnectInstanceMenuAction(
                serviceLocator.getService(Neo4jRESTClient.class),
                serviceLocator.getService(CySwingApplication.class));
    }

    private ConnectInstanceMenuAction(Neo4jRESTClient neo4JRESTClient, CySwingApplication cySwingApplication) {
        super(MENU_TITLE);
        this.cySwingApplication = cySwingApplication;
        setPreferredMenu(MENU_LOC);
        setMenuGravity(0.0f);
        this.neo4JRESTClient = neo4JRESTClient;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        ConnectDialog connectDialog = new ConnectDialog(cySwingApplication.getJFrame(), neo4JRESTClient::checkConnectionParameter );
        connectDialog.showConnectDialog();
        if(connectDialog.isOk()) {
            neo4JRESTClient.connect(connectDialog.getParameters());
        } else {
            JOptionPane.showInputDialog("Invalid connection parameters");
        }
    }

}
