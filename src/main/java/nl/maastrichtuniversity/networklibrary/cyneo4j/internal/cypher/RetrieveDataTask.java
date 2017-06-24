package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphObject;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RetrieveDataTask extends AbstractTask {

    private static final String NODE_QUERY = "MATCH (n) RETURN n";
    private static final String EDGE_QUERY = "MATCH ()-[r]->() RETURN r";

    private Services services;

    public RetrieveDataTask(Services services) {
        this.services = services;
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        try {
            taskMonitor.setTitle("Importing the Neo4j Graph");
            // setup network
            CyNetwork network = services.getCyNetworkFactory().createNetwork();
            network.getRow(network).set(CyNetwork.NAME, "neo4j network");
            CreateCyNetworkFromGraphObjectList cypherParser = new CreateCyNetworkFromGraphObjectList(network);
            taskMonitor.setStatusMessage("Downloading nodes");
            CypherQuery nodeQuery = CypherQuery.builder()
                    .query(NODE_QUERY)
                    .build();
            cypherParser.parseRetVal(executeQuery(nodeQuery));

            services.getCyNetworkManager().addNetwork(network);

            cypherParser = new CreateCyNetworkFromGraphObjectList(network);
            taskMonitor.setStatusMessage("Downloading edges");
            CypherQuery edgeQuery = CypherQuery.builder()
                    .query(EDGE_QUERY)
                    .build();
            cypherParser.parseRetVal(executeQuery(edgeQuery));

            taskMonitor.setStatusMessage("Creating View");

            Collection<CyNetworkView> views = services.getCyNetworkViewManager().getNetworkViews(network);
            CyNetworkView view;
            if (!views.isEmpty()) {
                view = views.iterator().next();
            } else {
                view = services.getCyNetworkViewFactory().createNetworkView(network);
                services.getCyNetworkViewManager().addNetworkView(view);
            }

            taskMonitor.setStatusMessage("Applying Layout");

            Set<View<CyNode>> nodes = new HashSet<>();
            CyLayoutAlgorithm layout = services.getCyLayoutAlgorithmManager().getLayout("force-directed");
            insertTasksAfterCurrentTask(layout.createTaskIterator(view, layout.createLayoutContext(), nodes, null));

            VisualStyle vs = services.getVisualMappingManager().getDefaultVisualStyle();
            services.getVisualMappingManager().setVisualStyle(vs, view);
            vs.apply(view);
            view.updateView();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private List<GraphObject> executeQuery(CypherQuery query) {
        return services.getNeo4jClient().executeQuery(query);
    }

}
