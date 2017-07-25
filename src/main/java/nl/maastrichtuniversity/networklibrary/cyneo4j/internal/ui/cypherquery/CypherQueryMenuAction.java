package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.cypherquery;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.ImportQueryTemplateTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.retrievedata.ExecuteCypherQueryTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.view.vizmap.VisualStyle;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.stream.Collectors;

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

        CypherQueryDialog cypherQueryDialog = new CypherQueryDialog(services.getCySwingApplication().getJFrame(), getAllVisualStyleTitle());
        cypherQueryDialog.showDialog();
        if(!cypherQueryDialog.isExecuteQuery()) {
            return;
        }
        String query = cypherQueryDialog.getCypherQuery();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(services.getCySwingApplication().getJFrame(), "Query is empty");
            return;
        }

        ExecuteCypherQueryTask executeCypherQueryTask  =
                services.getCommandFactory().createExecuteCypherQueryTask(
                        cypherQueryDialog.getNetwork(),
                        CypherQuery.builder().query(query).build(),
                        cypherQueryDialog.getVisualStyleTitle()
                );
        services.getCommandRunner().execute(executeCypherQueryTask);
    }

    private String[] getAllVisualStyleTitle() {
        return services.getVisualMappingManager()
                .getAllVisualStyles().stream()
                .map(VisualStyle::getTitle)
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }
}
