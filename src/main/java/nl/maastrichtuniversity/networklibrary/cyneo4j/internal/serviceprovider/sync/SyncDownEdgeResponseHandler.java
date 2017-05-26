package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.CypherResultParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.cytoscape.model.CyNetwork;

import java.io.IOException;
import java.util.Map;

public class SyncDownEdgeResponseHandler implements ResponseHandler<Long> {

    private CyNetwork network;
    private String errors;

    public SyncDownEdgeResponseHandler(CyNetwork network) {
        super();
        this.network = network;

        errors = null;
    }

    protected CyNetwork getNetwork() {
        return network;
    }

    @Override
    public Long handleResponse(HttpResponse response) throws IOException {
        int responseCode = response.getStatusLine().getStatusCode();

        Long resNet = null;
        if (responseCode >= 200 && responseCode < 300) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> nodes = mapper.readValue(response.getEntity().getContent(), Map.class);

            CypherResultParser cypherParser = new CypherResultParser(getNetwork());
            cypherParser.parseRetVal(nodes);

            resNet = cypherParser.edgesAdded();

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