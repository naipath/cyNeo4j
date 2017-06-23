package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import org.cytoscape.application.swing.AbstractCyAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CypherMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Cypher Query";
    private final static String MENU_LOC = "Apps.cyNeo4j";
    private final static String INITIAL_QUERY = "match (n)-[r]->(m) return n,r,m";

    private final Services services;

    public static CypherMenuAction create(Services services) {
        return new CypherMenuAction(services);
    }

    private CypherMenuAction(Services services) {
        super(MENU_TITLE);
        this.services = services;
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        setMenuGravity(0.5f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CypherExtExec exec = new CypherExtExec(services);

        String query = JOptionPane.showInputDialog( services.getCySwingApplication().getJFrame(), MENU_TITLE, INITIAL_QUERY)
            .replaceAll("\"", "\\\\\"");

        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(services.getCySwingApplication().getJFrame(), "Failed to collect parameters for ");
            return;
        }
        CypherQuery cypherQuery = CypherQuery.builder().query(query).build();
        exec.processCallResponse(services.getNeo4jClient().executeQueryResultObject(cypherQuery));
    }
}
