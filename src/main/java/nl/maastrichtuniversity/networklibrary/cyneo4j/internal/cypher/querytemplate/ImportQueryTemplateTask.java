package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.ImportGraphToNetwork;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphObject;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImportQueryTemplateTask extends AbstractTask {

    private final Services services;
    private final String networkName;
    private final String visualStyleTitle;
    private final CypherQueryTemplate cypherQueryTemplate;

    public ImportQueryTemplateTask(Services services, String networkName, String visualStyleTitle, CypherQueryTemplate queryTemplate) {
        this.services = services;
        this.networkName = networkName;
        this.visualStyleTitle = visualStyleTitle;
        this.cypherQueryTemplate = queryTemplate;
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        try {
            taskMonitor.setTitle("Importing the Neo4j Graph " + networkName);
            // setup network
            CyNetwork network = services.getCyNetworkFactory().createNetwork();
            network.getRow(network).set(CyNetwork.NAME, networkName);
            services.getCyNetworkManager().addNetwork(network);

            ImportGraphToNetwork cypherParser = new ImportGraphToNetwork(network, new MapToNetworkStrategy(cypherQueryTemplate.getMapping()));
            taskMonitor.setStatusMessage("Downloading graph");
            CypherQuery cypherQuery = cypherQueryTemplate.createQuery();
            cypherParser.importGraph(executeQuery(cypherQuery));

            CyEventHelper cyEventHelper = services.getCyEventHelper();
            cyEventHelper.flushPayloadEvents();

            taskMonitor.setStatusMessage("Creating View");
            CyNetworkView networkView = services.getCyNetworkViewFactory().createNetworkView(network);
            services.getCyNetworkViewManager().addNetworkView(networkView);

            taskMonitor.setStatusMessage("Applying Layout");
            Set<View<CyNode>> nodes = new HashSet<>();
            CyLayoutAlgorithm layout = services.getCyLayoutAlgorithmManager().getLayout("force-directed");
            insertTasksAfterCurrentTask(layout.createTaskIterator(networkView, layout.createLayoutContext(), nodes, null));

            VisualStyle visualStyle = services.getVisualMappingManager().getAllVisualStyles().stream()
                    .filter(vs -> vs.getTitle().equals(visualStyleTitle))
                    .findFirst().orElseGet(() -> services.getVisualMappingManager().getDefaultVisualStyle());
            visualStyle.apply(networkView);
            networkView.updateView();

        } catch (Exception e) {
            taskMonitor.showMessage(TaskMonitor.Level.ERROR, e.getMessage());
        }
    }

    private List<GraphObject> executeQuery(CypherQuery query) {
        return services.getNeo4jClient().executeQuery(query);
    }

}
