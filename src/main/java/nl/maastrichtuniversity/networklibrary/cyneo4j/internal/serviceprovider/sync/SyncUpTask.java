package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.swing.*;
import java.io.IOException;

public class SyncUpTask extends AbstractTask {

    private boolean wipeRemote;
    private String cypherURL;
    private CyNetwork currNet;

    public SyncUpTask(boolean wipeRemote, String cypherURL, CyNetwork currNet) {
        this.wipeRemote = wipeRemote;
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
            boolean wiped = false;
            if (wipeRemote) {
                taskMonitor.setStatusMessage("wiping remote network");
                String wipeQuery = "{ \"query\" : \"MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r\",\"params\" : {}}";
                progress = 0.1;
                taskMonitor.setProgress(progress);

                wiped = Request.Post(cypherURL)
                    .bodyString(wipeQuery, ContentType.APPLICATION_JSON)
                    .execute()
                    .handleResponse(this::hasCorrectResponseCode);
            }

            if (wiped == wipeRemote) {
                CyTable defNodeTab = currNet.getDefaultNodeTable();
                if (defNodeTab.getColumn("neoid") == null) {
                    defNodeTab.createColumn("neoid", Long.class, false);
                }

                double steps = currNet.getNodeList().size() + currNet.getEdgeList().size();
                double stepSize = 0.9 / steps;
                taskMonitor.setStatusMessage("uploading nodes");
                for (CyNode node : currNet.getNodeList()) {

                    String params = CyUtils.convertCyAttributesToJson(node, defNodeTab);
                    String cypher = "{ \"query\" : \"CREATE (n { props }) return id(n)\", \"params\" : {   \"props\" : [ " + params + " ] } }";

                    Long neoid = Request.Post(cypherURL).bodyString(cypher, ContentType.APPLICATION_JSON).execute().handleResponse(new CreateIdReturnResponseHandler());
                    defNodeTab.getRow(node.getSUID()).set("neoid", neoid);

                    progress = progress + stepSize;
                    taskMonitor.setProgress(progress);
                }

                CyTable defEdgeTab = currNet.getDefaultEdgeTable();
                if (defEdgeTab.getColumn("neoid") == null) {
                    defEdgeTab.createColumn("neoid", Long.class, false);
                }

                for (CyEdge edge : currNet.getEdgeList()) {
                    taskMonitor.setStatusMessage("uploading edges");
                    String from = edge.getSource().getSUID().toString();
                    String to = edge.getTarget().getSUID().toString();

                    String rparams = CyUtils.convertCyAttributesToJson(edge, defEdgeTab);

                    String rtype = defEdgeTab.getRow(edge.getSUID()).get(CyEdge.INTERACTION, String.class);

                    String cypher = "{\"query\" : \"MATCH (from { SUID: {fname}}),(to { SUID: {tname}}) CREATE (from)-[r:" + rtype + " { rprops } ]->(to) return id(r)\", \"params\" : { \"fname\" : " + from + ", \"tname\" : " + to + ", \"rprops\" : " + rparams + " }}";

                    Long neoid = Request.Post(cypherURL).bodyString(cypher, ContentType.APPLICATION_JSON).execute().handleResponse(new CreateIdReturnResponseHandler());
                    defEdgeTab.getRow(edge.getSUID()).set("neoid", neoid);

                    progress = progress + stepSize;
                    taskMonitor.setProgress(progress);
                }

            } else {
                System.out.println("could not wipe the instance! aborting syncUp");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasCorrectResponseCode(HttpResponse response) {
        int responseCode = response.getStatusLine().getStatusCode();
        return responseCode >= 200 && responseCode < 300;
    }
}
