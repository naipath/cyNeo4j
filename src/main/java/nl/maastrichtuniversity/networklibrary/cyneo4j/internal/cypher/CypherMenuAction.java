package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import org.cytoscape.application.swing.AbstractCyAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CypherMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Cypher Query";
    private final static String MENU_LOC = "Apps.cyNeo4j";

    private final ServiceLocator serviceLocator;

    public static CypherMenuAction create(ServiceLocator serviceLocator) {
        return new CypherMenuAction(serviceLocator);
    }

    private CypherMenuAction(ServiceLocator serviceLocator) {
        super(MENU_TITLE);
        this.serviceLocator = serviceLocator;
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        setMenuGravity(0.5f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CypherExtExec exec = new CypherExtExec(serviceLocator);

        String query = JOptionPane.showInputDialog( serviceLocator.getCySwingApplication().getJFrame(), "Cypher Query", "match (n)-[r]->(m) return n,r,m")
            .replaceAll("\"", "\\\\\"");

        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(serviceLocator.getCySwingApplication().getJFrame(), "Failed to collect parameters for ");
            return;
        }
        CypherQuery cypherQuery = CypherQuery.builder().query(query).build();
        exec.processCallResponse(serviceLocator.getNeo4jClient().executeQuery(cypherQuery));
    }
}
