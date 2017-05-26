package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.util.Map;

public class Neo4jPingHandler implements ResponseHandler<Boolean> {

    @Override
    public Boolean handleResponse(HttpResponse response) throws IOException {

        int responseCode = response.getStatusLine().getStatusCode();
        if (responseCode >= 200 && responseCode < 300) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> instanceResp = mapper.readValue(response.getEntity().getContent(), Map.class);
            if (instanceResp.containsKey("data"))
                return true;
        }

        return false;
    }

}