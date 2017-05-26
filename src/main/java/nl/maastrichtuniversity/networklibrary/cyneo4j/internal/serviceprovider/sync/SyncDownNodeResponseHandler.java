package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.CypherResultParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;

import java.io.IOException;
import java.util.Map;

public class SyncDownNodeResponseHandler implements ResponseHandler<CyNetwork> {

    private String instanceLocation;
    private CyNetworkFactory cyNetworkFactory;
    private CyNetworkManager cyNetworkMgr;
    private String errors = null;

    public SyncDownNodeResponseHandler(String instanceLocation,
                                       CyNetworkFactory cyNetworkFactory, CyNetworkManager cyNetworkMgr) {
        super();
        this.instanceLocation = instanceLocation;
        this.cyNetworkFactory = cyNetworkFactory;
        this.cyNetworkMgr = cyNetworkMgr;
    }

    protected String getInstanceLocation() {
        return instanceLocation;
    }

    protected CyNetworkFactory getCyNetworkFactory() {
        return cyNetworkFactory;
    }

    protected CyNetworkManager getCyNetworkMgr() {
        return cyNetworkMgr;
    }

    @Override
    public CyNetwork handleResponse(HttpResponse response) throws IOException {
        int responseCode = response.getStatusLine().getStatusCode();

        CyNetwork resNet = null;

        if (responseCode >= 200 && responseCode < 300) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> nodes = mapper.readValue(response.getEntity().getContent(), Map.class);


            CyNetwork myNet = getCyNetworkFactory().createNetwork();
            myNet.getRow(myNet).set(CyNetwork.NAME, getInstanceLocation());

            CypherResultParser cypherParser = new CypherResultParser(myNet);
            cypherParser.parseRetVal(nodes);

            getCyNetworkMgr().addNetwork(myNet);
            resNet = myNet;

        } else {
            errors = "ERROR " + responseCode;

            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> error = mapper.readValue(response.getEntity().getContent(), Map.class);
            errors = errors + "\n" + error.toString();
        }

        return resNet;
    }

    public String getErrors() {
        return errors;
    }
}