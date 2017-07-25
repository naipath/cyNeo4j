package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.querytemplate;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.ImportQueryTemplateTask;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.view.vizmap.VisualStyle;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.stream.Collectors;

public class QueryTemplateMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Cypher Query Templates";
    private final static String MENU_LOC = "Apps.cyNeo4j";
    private final Services services;

    public static QueryTemplateMenuAction create(Services services) {
        return new QueryTemplateMenuAction(services);
    }

    private QueryTemplateMenuAction(Services services) {
        super(MENU_TITLE);
        setPreferredMenu(MENU_LOC);
        this.services =services;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(!services.getNeo4jClient().isConnected()) {
            JOptionPane.showMessageDialog(services.getCySwingApplication().getJFrame(), "Not connected");;
            return;
        }
        SelectTemplateDialog dialog = new SelectTemplateDialog(
                services.getCySwingApplication().getJFrame(),
                getAllVisualStyleTitle(),
                services.getAppConfiguration().getTemplateDirectory(),
                templateDir -> {
                    services.getAppConfiguration().setTemplateDirectory(templateDir);
                    services.getAppConfiguration().save();
                });
        dialog.showDialog();

        if(dialog.isOk()) {
            CypherQueryTemplate query = dialog.getCypherQueryTemplate();
            if(query != null) {
                String networkName = dialog.getNetworkName();
                String visualStyle = dialog.getVisualStyle();
                ParameterDialog parameterDialog = new ParameterDialog(services.getCySwingApplication().getJFrame(), query.getParameterTypes());
                parameterDialog.showDialog();
                if (parameterDialog.isOk()) {
                    query.setParameters(parameterDialog.getParameters());
                    ImportQueryTemplateTask retrieveDataTask =
                            services.getCommandFactory().createRetrieveDataFromQueryTemplateTask(
                                    networkName,
                                    query,
                                    visualStyle
                            );
                    services.getCommandRunner().execute(retrieveDataTask);
                }
            } else {
                JOptionPane.showMessageDialog(services.getCySwingApplication().getJFrame(), "Query not found");
            }
        }
    }

    private String[] getAllVisualStyleTitle() {
        return services.getVisualMappingManager()
                .getAllVisualStyles().stream()
                .map(VisualStyle::getTitle)
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }
}