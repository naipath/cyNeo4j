package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.genepathquery;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.gff.RetrieveGenomeDataTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery.GenePathQuery;
import org.cytoscape.application.swing.AbstractCyAction;

import java.awt.event.ActionEvent;

public class GenePathQueryMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Gene Path";
    private final static String MENU_LOC = "Apps.cyNeo4j";
    private final Services services;

    public GenePathQueryMenuAction(Services services) {
        super(MENU_TITLE);
        setPreferredMenu(MENU_LOC);
        this.services =services;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GenePathQueryDialog dialog = new GenePathQueryDialog(services.getCySwingApplication().getJFrame());
        dialog.showDialog();
        if(dialog.isOk()) {
            GenePathQuery query = dialog.getGenePathQuery();
            RetrieveGenomeDataTask retrieveGenomeDataTask = services.getCommandFactory().createRetrieveGenomeDataTask("gff path", query, null);
            services.getCommandRunner().execute(retrieveGenomeDataTask);
        }
    }

    public static GenePathQueryMenuAction create(Services services) {
        return new GenePathQueryMenuAction(services);
    }
}
