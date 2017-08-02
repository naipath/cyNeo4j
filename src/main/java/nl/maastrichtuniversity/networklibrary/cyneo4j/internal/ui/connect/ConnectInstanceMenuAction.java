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
    private final ConnectToNeo4j connectToNeo4j;

    public static ConnectInstanceMenuAction create(Services services) {
        return new ConnectInstanceMenuAction(ConnectToNeo4j.create(services));
    }

    private ConnectInstanceMenuAction(ConnectToNeo4j connectToNeo4j) {
        super(MENU_TITLE);
        this.connectToNeo4j = connectToNeo4j;
        setPreferredMenu(MENU_LOC);
        setMenuGravity(0.0f);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        connectToNeo4j.connect();
    }

}
