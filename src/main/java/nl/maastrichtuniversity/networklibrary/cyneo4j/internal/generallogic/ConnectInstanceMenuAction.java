package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.generallogic;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4JExtensionRegister;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ConnectInstanceMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Connect to Instance";
    private final static String MENU_LOC = "Apps.cyNeo4j";

    private final CySwingApplication cySwingApplication;
    private final Neo4jRESTServer neo4jRESTServer;
    private final Neo4JExtensionRegister localExtensions;

    public static ConnectInstanceMenuAction create(ServiceLocator serviceLocator) {
        return new ConnectInstanceMenuAction(
                serviceLocator.getService(CyApplicationManager.class),
                serviceLocator.getService(CySwingApplication.class),
                serviceLocator.getService(Neo4jRESTServer.class),
                serviceLocator.getService(Neo4JExtensionRegister.class)
        );
    }

    private ConnectInstanceMenuAction(CyApplicationManager cyApplicationManager, CySwingApplication cySwingApplication, Neo4jRESTServer neo4jRESTServer, Neo4JExtensionRegister localExtensions) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        this.localExtensions = localExtensions;
        setPreferredMenu(MENU_LOC);
        setMenuGravity(0.0f);
        this.cySwingApplication = cySwingApplication;
        this.neo4jRESTServer = neo4jRESTServer;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        JDialog dialog = new JDialog(cySwingApplication.getJFrame());
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ConnectPanel p = new ConnectPanel(dialog, neo4jRESTServer, localExtensions);
        p.setOpaque(true);
        locate(dialog);
        dialog.setModal(true);
        dialog.setContentPane(p);
        dialog.setResizable(false);

        dialog.pack();
        dialog.setVisible(true);
    }

    private void locate(JDialog dialog) {

        Point cyLocation = cySwingApplication.getJFrame().getLocation();
        int cyHeight = cySwingApplication.getJFrame().getHeight();
        int cyWidth = cySwingApplication.getJFrame().getWidth();

        Point middle = new Point(cyLocation.x + (cyWidth / 4), cyLocation.y + (cyHeight / 4));

        dialog.setLocation(middle);
    }

}
