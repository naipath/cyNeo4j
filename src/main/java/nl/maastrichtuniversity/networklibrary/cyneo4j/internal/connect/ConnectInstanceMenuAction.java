package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.connect;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ConnectInstanceMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Connect to Instance";
    private final static String MENU_LOC = "Apps.cyNeo4j";

    private final CySwingApplication cySwingApplication;
    private final Neo4jRESTServer neo4jRESTServer;

    public static ConnectInstanceMenuAction create(ServiceLocator serviceLocator) {
        return new ConnectInstanceMenuAction(
                serviceLocator.getService(CyApplicationManager.class),
                serviceLocator.getService(CySwingApplication.class),
                serviceLocator.getService(Neo4jRESTServer.class)
        );
    }

    private ConnectInstanceMenuAction(CyApplicationManager cyApplicationManager, CySwingApplication cySwingApplication, Neo4jRESTServer neo4jRESTServer) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        setPreferredMenu(MENU_LOC);
        setMenuGravity(0.0f);
        this.cySwingApplication = cySwingApplication;
        this.neo4jRESTServer = neo4jRESTServer;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        ConnectDialog connectDialog = new ConnectDialog(cySwingApplication.getJFrame(), neo4jRESTServer::checkConnectionParameter );
        connectDialog.showConnectDialog();
        if(connectDialog.isOk()) {
            neo4jRESTServer.connect(connectDialog.getParameters());
        }
    }

}
