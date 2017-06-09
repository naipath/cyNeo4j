package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.swing.*;
import java.io.IOException;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class SyncUpTask extends AbstractTask {

    private static final String WIPE_QUERY = "{ \"query\" : \"MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r\",\"params\" : {}}";
    private static final String CREATE_ID_QUERY = "{ \"query\" : \"CREATE (n { props }) return id(n)\", \"params\" : {   \"props\" : [ %s ] } }";
    private static final String CREATE_ID_ALTERNATIVE_QUERY = "{\"query\" : \"MATCH (from { SUID: {fname}}),(to { SUID: {tname}}) CREATE (from)-[r:%s { rprops } ]->(to) return id(r)\", \"params\" : { \"fname\" : %s, \"tname\" : %s, \"rprops\" : %s }}";
    private static final String NEOID = "neoid";

    private String cypherURL;
    private CyNetwork currNet;

    public SyncUpTask(String cypherURL, CyNetwork currNet) {
        this.cypherURL = cypherURL;
        this.currNet = currNet;
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {

        CyNetwork currNet = this.currNet;

        if (currNet == null) {
            JOptionPane.showMessageDialog(null, "No network selected!");
            return;
        }

        taskMonitor.setTitle("Synchronizing UP to the Server");
        double progress = 0.0;

        try {
            taskMonitor.setStatusMessage("wiping remote network");
            updateProgress(taskMonitor, progress, 0.1);

            boolean wiped = Request.Post(cypherURL)
                .bodyString(WIPE_QUERY, APPLICATION_JSON)
                .execute().handleResponse(this::hasCorrectResponseCode);

            if (wiped) {
                CyTable defNodeTab = currNet.getDefaultNodeTable();
                if (defNodeTab.getColumn(NEOID) == null) {
                    defNodeTab.createColumn(NEOID, Long.class, false);
                }

                double steps = currNet.getNodeList().size() + currNet.getEdgeList().size();
                double stepSize = 0.9 / steps;
                taskMonitor.setStatusMessage("uploading nodes");

                for (CyNode node : currNet.getNodeList()) {

                    String cypher = String.format(CREATE_ID_QUERY, CyUtils.convertCyAttributesToJson(node, defNodeTab));

                    defNodeTab.getRow(node.getSUID()).set(NEOID, retrieveNeoId(cypher));

                    updateProgress(taskMonitor, progress, stepSize);
                }

                CyTable defEdgeTab = currNet.getDefaultEdgeTable();
                if (defEdgeTab.getColumn(NEOID) == null) {
                    defEdgeTab.createColumn(NEOID, Long.class, false);
                }

                for (CyEdge edge : currNet.getEdgeList()) {
                    taskMonitor.setStatusMessage("uploading edges");

                    String cypher = String.format(
                        CREATE_ID_ALTERNATIVE_QUERY,
                        defEdgeTab.getRow(edge.getSUID()).get(CyEdge.INTERACTION, String.class),
                        edge.getSource().getSUID().toString(),
                        edge.getTarget().getSUID().toString(),
                        CyUtils.convertCyAttributesToJson(edge, defEdgeTab)
                    );

                    defEdgeTab.getRow(edge.getSUID()).set(NEOID, retrieveNeoId(cypher));

                    updateProgress(taskMonitor, progress, stepSize);
                }
            } else {
                System.out.println("could not wipe the instance! aborting syncUp");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProgress(TaskMonitor taskMonitor, double progress, double stepSize) {
        progress = progress + stepSize;
        taskMonitor.setProgress(progress);
    }

    private Long retrieveNeoId(String query) throws IOException {
        return Request.Post(cypherURL)
            .bodyString(query, APPLICATION_JSON)
            .execute().handleResponse(new CreateIdReturnResponseHandler());
    }

    private boolean hasCorrectResponseCode(HttpResponse response) {
        int responseCode = response.getStatusLine().getStatusCode();
        return responseCode >= 200 && responseCode < 300;
    }
}
