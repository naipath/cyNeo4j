package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.template;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.RetrieveDataFromQueryTemplateTask;
import org.cytoscape.application.swing.AbstractCyAction;

import java.awt.event.ActionEvent;

public class CypherQueryTemplateMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Cypher Query Templates";
    private final static String MENU_LOC = "Apps.cyNeo4j";
    private final Services services;

    public static CypherQueryTemplateMenuAction create(Services services) {
        return new CypherQueryTemplateMenuAction(services);
    }

    private CypherQueryTemplateMenuAction(Services services) {
        super(MENU_TITLE);
        setPreferredMenu(MENU_LOC);
        this.services =services;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SelectTemplateDialog dialog = new SelectTemplateDialog(services.getCySwingApplication().getJFrame(), services);
        dialog.showDialog();
        if(dialog.isOk()) {
            CypherQueryTemplate query = dialog.getCypherQueryTemplate();
            String networkName = dialog.getNetworkName();
            String visualStyle = dialog.getVisualStyle();

            ParameterDialog parameterDialog = new ParameterDialog(services.getCySwingApplication().getJFrame(), query);
            parameterDialog.showDialog();
            if (parameterDialog.isOk()) {
                query.setParameters(parameterDialog.getParameters());
                RetrieveDataFromQueryTemplateTask retrieveDataTask =
                        services.getCommandFactory().createRetrieveDataFromQueryTemplateTask(
                                networkName,
                                query,
                                visualStyle
                        );
                services.getCommandRunner().execute(retrieveDataTask);
            }
        }
    }
}