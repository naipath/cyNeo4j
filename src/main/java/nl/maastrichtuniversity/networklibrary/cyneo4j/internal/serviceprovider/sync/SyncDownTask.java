package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.CypherResultParser;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.PassThroughResponseHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.View;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SyncDownTask extends AbstractTask {

    private String cypherURL;
    private String instanceLocation;
    private CyNetworkFactory cyNetworkFactory;
    private CyNetworkManager cyNetworkMgr;
    private CyNetworkViewManager cyNetworkViewMgr;
    private CyNetworkViewFactory cyNetworkViewFactory;
    private CyLayoutAlgorithmManager cyLayoutAlgorithmMgr;
    private VisualMappingManager visualMappingMgr;

    public SyncDownTask(String cypherURL,
                        String instanceLocation, CyNetworkFactory cyNetworkFactory,
                        CyNetworkManager cyNetworkMgr,
                        CyNetworkViewManager cyNetworkViewMgr,
                        CyNetworkViewFactory cyNetworkViewFactory,
                        CyLayoutAlgorithmManager cyLayoutAlgorithmMgr,
                        VisualMappingManager visualMappingMgr) {
        this.cypherURL = cypherURL;
        this.instanceLocation = instanceLocation;
        this.cyNetworkFactory = cyNetworkFactory;
        this.cyNetworkMgr = cyNetworkMgr;
        this.cyNetworkViewMgr = cyNetworkViewMgr;
        this.cyNetworkViewFactory = cyNetworkViewFactory;
        this.cyLayoutAlgorithmMgr = cyLayoutAlgorithmMgr;
        this.visualMappingMgr = visualMappingMgr;
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        try {

            String nodeIdQuery = "{ \"query\" : \"MATCH (n) RETURN id(n)\",\"params\" : {}}";
            String edgeIdQuery = "{ \"query\" : \"MATCH ()-[r]->() RETURN id(r)\",\"params\" : {}}";

            IdListHandler idListHandler = new IdListHandler();
            List<Long> nodeIds = Request.Post(cypherURL).bodyString(nodeIdQuery, ContentType.APPLICATION_JSON).execute().handleResponse(idListHandler);
            List<Long> edgeIds = Request.Post(cypherURL).bodyString(edgeIdQuery, ContentType.APPLICATION_JSON).execute().handleResponse(idListHandler);

            int numQueries = nodeIds.size() + edgeIds.size();

            int chunkSize = 500;
            double progress_increment = (0.7 / (double) numQueries) * (double) chunkSize;
            double progress = 0.1;

            taskMonitor.setProgress(progress);

            if (nodeIds.size() > 0) {

                taskMonitor.setTitle("Synchronizing the remote network DOWN");

                // setup network
                CyNetwork network = cyNetworkFactory.createNetwork();
                network.getRow(network).set(CyNetwork.NAME, instanceLocation);

                PassThroughResponseHandler passHandler = new PassThroughResponseHandler();
                CypherResultParser cypherParser = new CypherResultParser(network);

                taskMonitor.setStatusMessage("Downloading nodes");
                for (int i = 0; i < nodeIds.size(); i += chunkSize) {
                    int end = i + chunkSize;

                    if (end > nodeIds.size())
                        end = nodeIds.size();
                    String array = toJSONArray(nodeIds.subList(i, end));
                    String query = "{\"query\" : \"MATCH (n) where id(n) in {toget} RETURN n\", \"params\" : { \"toget\" : " + array + "} }";

                    Object responseObj = Request.Post(cypherURL).bodyString(query, ContentType.APPLICATION_JSON).execute().handleResponse(passHandler);

                    if (responseObj == null) {
                        throw new IllegalArgumentException("query failed! " + query);
                    }

                    cypherParser.parseRetVal(responseObj);

                    progress += progress_increment;
                    taskMonitor.setProgress(progress);
                }

                cyNetworkMgr.addNetwork(network);

                cypherParser = new CypherResultParser(network);
                taskMonitor.setStatusMessage("Downloading edges");
                for (int i = 0; i < edgeIds.size(); i += chunkSize) {
                    int end = i + chunkSize;

                    if (end > edgeIds.size())
                        end = edgeIds.size();

                    String array = toJSONArray(edgeIds.subList(i, end));
                    String query = "{\"query\" : \"MATCH ()-[r]->() where id(r) in {toget} RETURN r\", \"params\" : { \"toget\" : " + array + "} }";

                    Object responseObj = Request.Post(cypherURL).bodyString(query, ContentType.APPLICATION_JSON).execute().handleResponse(passHandler);
                    cypherParser.parseRetVal(responseObj);

                    if (responseObj == null) {
                        throw new IllegalArgumentException("query failed! " + query);
                    }

                    progress += progress_increment;
                    taskMonitor.setProgress(progress);
                }

                taskMonitor.setStatusMessage("Creating View");
                taskMonitor.setProgress(0.8);

                Collection<CyNetworkView> views = cyNetworkViewMgr.getNetworkViews(network);
                CyNetworkView view;
                if (!views.isEmpty()) {
                    view = views.iterator().next();
                } else {
                    view = cyNetworkViewFactory.createNetworkView(network);
                    cyNetworkViewMgr.addNetworkView(view);
                }

                taskMonitor.setStatusMessage("Applying Layout");
                taskMonitor.setProgress(0.9);

                Set<View<CyNode>> nodes = new HashSet<>();
                CyLayoutAlgorithm layout = cyLayoutAlgorithmMgr.getLayout("force-directed");
                insertTasksAfterCurrentTask(layout.createTaskIterator(view, layout.createLayoutContext(), nodes, null));

                CyUtils.updateVisualStyle(visualMappingMgr, view);
            }

        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private String toJSONArray(List<Long> ids) {
        return ids.toString();
    }

}
