package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.retrievedata;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.CypherResultParser;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.ResultObject;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jGraph;
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

    private final Services services;

    public RetrieveDataTask(Services services) {
        this.services = services;
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        try {
            CypherQuery nodeIdQuery = CypherQuery.builder()
                    .query("MATCH (n) RETURN id(n)")
                    .build();

            CypherQuery edgeIdQuery =
                    CypherQuery.builder()
                            .query("MATCH ()-[r]->() RETURN id(r)")
                            .build();

            List<Long> nodeIds = executeQueryNodeId(nodeIdQuery).getData();
            List<Long> edgeIds = executeQueryNodeId(edgeIdQuery).getData();

            int numQueries = nodeIds.size() + edgeIds.size();

            int chunkSize = 500;
            double progress_increment = (0.7 / (double) numQueries) * (double) chunkSize;
            double progress = 0.1;

            taskMonitor.setProgress(progress);

            if (nodeIds.size() > 0) {

                taskMonitor.setTitle("Importing the Neo4j Graph");

                // setup network
                CyNetwork network = services.getCyNetworkFactory().createNetwork();
                network.getRow(network).set(CyNetwork.NAME, "neo4j network");

                CypherResultParser cypherParser = new CypherResultParser(network);

                taskMonitor.setStatusMessage("Downloading nodes");
                for (int i = 0; i < nodeIds.size(); i += chunkSize) {
                    int end = i + chunkSize;

                    if (end > nodeIds.size())
                        end = nodeIds.size();
                    CypherQuery query = CypherQuery.builder()
                            .query("MATCH (n) where id(n) in {toget} RETURN n")
                            .params("toget", nodeIds.subList(i, end))
                            .build();

                    cypherParser.parseRetVal(executeQuery(query));

                    progress += progress_increment;
                    taskMonitor.setProgress(progress);
                }

                services.getCyNetworkManager().addNetwork(network);

                cypherParser = new CypherResultParser(network);
                taskMonitor.setStatusMessage("Downloading edges");
                for (int i = 0; i < edgeIds.size(); i += chunkSize) {
                    int end = i + chunkSize;

                    if (end > edgeIds.size())
                        end = edgeIds.size();

                    CypherQuery query = CypherQuery.builder()
                            .query("MATCH ()-[r]->() where id(r) in {toget} RETURN r")
                            .params("toget", edgeIds.subList(i, end))
                            .build();

                    cypherParser.parseRetVal(executeQuery(query));

                    progress += progress_increment;
                    taskMonitor.setProgress(progress);
                }

                taskMonitor.setStatusMessage("Creating View");
                taskMonitor.setProgress(0.8);

                Collection<CyNetworkView> views = services.getCyNetworkViewManager().getNetworkViews(network);
                CyNetworkView view;
                if (!views.isEmpty()) {
                    view = views.iterator().next();
                } else {
                    view = services.getCyNetworkViewFactory().createNetworkView(network);
                    services.getCyNetworkViewManager().addNetworkView(view);
                }

                taskMonitor.setStatusMessage("Applying Layout");
                taskMonitor.setProgress(0.9);

                Set<View<CyNode>> nodes = new HashSet<>();
                CyLayoutAlgorithm layout = services.getCyLayoutAlgorithmManager().getLayout("force-directed");
                insertTasksAfterCurrentTask(layout.createTaskIterator(view, layout.createLayoutContext(), nodes, null));

                VisualStyle vs = services.getVisualMappingManager().getDefaultVisualStyle();
                services.getVisualMappingManager().setVisualStyle(vs, view);
                vs.apply(view);
                view.updateView();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private Neo4jGraph<Long> executeQueryNodeId(CypherQuery query) {
        return services.getNeo4jClient().executeQueryIdList(query);
    }

    private  Neo4jGraph<ResultObject> executeQuery(CypherQuery query) {
        return services.getNeo4jClient().executeQueryResultObject(query);
    }

}
