package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.retrievedata;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.CypherResultParser;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class RetrieveDataTask extends AbstractTask {

    private final ServiceLocator serviceLocator;

    public RetrieveDataTask(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        try {
            CypherQuery nodeIdQuery = CypherQuery.builder()
                    .query("MATCH (n) RETURN id(n)")
                    .build(); //"{ \"query\" : \"MATCH (n) RETURN id(n)\",\"params\" : {}}";

            CypherQuery edgeIdQuery =
                    CypherQuery.builder()
                    .query("MATCH ()-[r]->() RETURN id(r)")
                    .build();

//                    "{ \"query\" : \"MATCH ()-[r]->() RETURN id(r)\",\"params\" : {}}";

            List<Long> nodeIds = executeQuery(nodeIdQuery, this::toLongList);
            List<Long> edgeIds = executeQuery(edgeIdQuery, this::toLongList);

            int numQueries = nodeIds.size() + edgeIds.size();

            int chunkSize = 500;
            double progress_increment = (0.7 / (double) numQueries) * (double) chunkSize;
            double progress = 0.1;

            taskMonitor.setProgress(progress);

            if (nodeIds.size() > 0) {

                taskMonitor.setTitle("Importing the Neo4j Graph");

                // setup network
                CyNetwork network = serviceLocator.getCyNetworkFactory().createNetwork();
                network.getRow(network).set(CyNetwork.NAME, "neo4j network");

                CypherResultParser cypherParser = new CypherResultParser(network);

                taskMonitor.setStatusMessage("Downloading nodes");
                for (int i = 0; i < nodeIds.size(); i += chunkSize) {
                    int end = i + chunkSize;

                    if (end > nodeIds.size())
                        end = nodeIds.size();
                    String array = toJSONArray(nodeIds.subList(i, end));
                    CypherQuery query = CypherQuery.builder()
                            .query("MATCH (n) where id(n) in {toget} RETURN n")
                            .params("toget", array)
                            .build();

                    Object responseObj = executeQuery(query, this::identity);

                    if (responseObj == null) {
                        throw new IllegalArgumentException("query failed! " + query);
                    }

                    cypherParser.parseRetVal(responseObj);

                    progress += progress_increment;
                    taskMonitor.setProgress(progress);
                }

                serviceLocator.getCyNetworkManager().addNetwork(network);

                cypherParser = new CypherResultParser(network);
                taskMonitor.setStatusMessage("Downloading edges");
                for (int i = 0; i < edgeIds.size(); i += chunkSize) {
                    int end = i + chunkSize;

                    if (end > edgeIds.size())
                        end = edgeIds.size();

                    String array = toJSONArray(edgeIds.subList(i, end));
//                    String query = "{\"query\" : \"MATCH ()-[r]->() where id(r) in {toget} RETURN r\", \"params\" : { \"toget\" : " + array + "} }";

                    CypherQuery query = CypherQuery.builder()
                            .query("MATCH ()-[r]->() where id(r) in {toget} RETURN r")
                            .params("toget", array)
                            .build();

                    Object responseObj = executeQuery(query, this::identity);
                    cypherParser.parseRetVal(responseObj);

                    if (responseObj == null) {
                        throw new IllegalArgumentException("query failed! " + query);
                    }

                    progress += progress_increment;
                    taskMonitor.setProgress(progress);
                }

                taskMonitor.setStatusMessage("Creating View");
                taskMonitor.setProgress(0.8);

                Collection<CyNetworkView> views = serviceLocator.getCyNetworkViewManager().getNetworkViews(network);
                CyNetworkView view;
                if (!views.isEmpty()) {
                    view = views.iterator().next();
                } else {
                    view = serviceLocator.getCyNetworkViewFactory().createNetworkView(network);
                    serviceLocator.getCyNetworkViewManager().addNetworkView(view);
                }

                taskMonitor.setStatusMessage("Applying Layout");
                taskMonitor.setProgress(0.9);

                Set<View<CyNode>> nodes = new HashSet<>();
                CyLayoutAlgorithm layout = serviceLocator.getCyLayoutAlgorithmManager().getLayout("force-directed");
                insertTasksAfterCurrentTask(layout.createTaskIterator(view, layout.createLayoutContext(), nodes, null));

                VisualStyle vs = serviceLocator.getVisualMappingManager().getDefaultVisualStyle();
                serviceLocator.getVisualMappingManager().setVisualStyle(vs, view);
                vs.apply(view);
                view.updateView();
            }
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private Object identity(Object o) {
        return o;
    }

    private List<Long> toLongList(Object result) {
        if(result instanceof Map) {
            Map<String,Object> map = (Map<String, Object>) result;
            List<List<Integer>> queryRes = (List<List<Integer>>) map.get("data");

            return queryRes.stream()
                    .map(ids -> ids.get(0))
                    .map(Integer::longValue)
                    .collect(toList());

        }
        throw new IllegalStateException();
    }

    private <T> T executeQuery(CypherQuery query, Function<Object, T> converter) throws IOException {
        return serviceLocator.getNeo4jClient().executeQuery(query, converter);
    }

    private String toJSONArray(List<Long> ids) {
        return ids.toString();
    }
}
