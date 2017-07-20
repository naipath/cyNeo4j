package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.genedetailquery;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.gff.RetrieveGenomeDataTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery.GeneDetailsQuery;
import org.cytoscape.application.swing.AbstractCyAction;

import java.awt.event.ActionEvent;

public class GeneDetailsQueryMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Gene Details";
    private final static String MENU_LOC = "Apps.cyNeo4j";
    private final Services services;

    public GeneDetailsQueryMenuAction(Services services) {
        super(MENU_TITLE);
        setPreferredMenu(MENU_LOC);
        this.services =services;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GeneDetailsQueryDialog dialog = new GeneDetailsQueryDialog(services.getCySwingApplication().getJFrame());
        dialog.showDialog();
        if(dialog.isOk()) {
            GeneDetailsQuery query = dialog.getGeneDetailsQuery();
            RetrieveGenomeDataTask retrieveGenomeDataTask = services.getCommandFactory().createRetrieveGenomeDataTask("gff gene", query, null);
            services.getCommandRunner().execute(retrieveGenomeDataTask);
        }
    }

    public static GeneDetailsQueryMenuAction create(Services services) {
        return new GeneDetailsQueryMenuAction(services);
    }
}
