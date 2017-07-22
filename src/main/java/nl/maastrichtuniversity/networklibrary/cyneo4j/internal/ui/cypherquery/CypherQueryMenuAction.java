package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.cypherquery;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.retrievedata.ExecuteCypherQuery;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import org.cytoscape.application.swing.AbstractCyAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CypherQueryMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Cypher Query";
    private final static String MENU_LOC = "Apps.cyNeo4j";

    private final Services services;

    public static CypherQueryMenuAction create(Services services) {
        return new CypherQueryMenuAction(services);
    }

    private CypherQueryMenuAction(Services services) {
        super(MENU_TITLE);
        this.services = services;
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        setMenuGravity(0.5f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(!services.getNeo4jClient().isConnected()) {
            JOptionPane.showMessageDialog(services.getCySwingApplication().getJFrame(), "Not connected");;
            return;
        }

        CypherQueryDialog cypherQueryDialog = new CypherQueryDialog(services.getCySwingApplication().getJFrame());
        cypherQueryDialog.showDialog();
        if(!cypherQueryDialog.isExecuteQuery()) {
            return;
        }
        String query = cypherQueryDialog.getCypherQuery();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(services.getCySwingApplication().getJFrame(), "Failed to collect parameters for ");
            return;
        }
        ExecuteCypherQuery exec = new ExecuteCypherQuery(services);
        CypherQuery cypherQuery = CypherQuery.builder().query(query).build();
        exec.processCallResponse(services.getNeo4jClient().executeQuery(cypherQuery), "Network");
    }
}
