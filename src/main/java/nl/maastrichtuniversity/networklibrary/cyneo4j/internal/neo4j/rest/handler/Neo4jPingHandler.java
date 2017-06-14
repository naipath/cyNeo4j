package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.maastrichtuniversity.networklibrary.cyneo4j.HttpResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class Neo4jPingHandler extends HttpResponseHandler<Boolean> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Boolean handle(int responseCode, InputStream content) throws IOException {
        if (responseCode >= 200 && responseCode < 300) {
            Map<String, String> instanceResp = mapper.readValue(content, Map.class);
            if (instanceResp.containsKey("data")) {
                return true;
            }
        }
        return false;
    }
}