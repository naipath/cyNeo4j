package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.connect;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.configuration.AppConfiguration;
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
    private final AppConfiguration appConfiguration;

    public static ConnectInstanceMenuAction create(Services services) {
        return new ConnectInstanceMenuAction(
                services.getNeo4jClient(),
                services.getCySwingApplication(),
                services.getAppConfiguration());
    }

    private ConnectInstanceMenuAction(Neo4jClient neo4jClient, CySwingApplication cySwingApplication, AppConfiguration appConfiguration) {
        super(MENU_TITLE);
        this.neo4jClient = neo4jClient;
        this.cySwingApplication = cySwingApplication;
        this.appConfiguration = appConfiguration;
        setPreferredMenu(MENU_LOC);
        setMenuGravity(0.0f);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        ConnectDialog connectDialog = new ConnectDialog(cySwingApplication.getJFrame(), neo4jClient::connect,
                appConfiguration.getNeo4jHost(),
                appConfiguration.getNeo4jUsername()
        );
        connectDialog.showConnectDialog();
        if(connectDialog.isOk()) {
            appConfiguration.setConnectionParameters(connectDialog.getHostname(), connectDialog.getUsername());
            appConfiguration.save();
            JOptionPane.showMessageDialog(this.cySwingApplication.getJFrame(), "Connected");
        }
    }

}
