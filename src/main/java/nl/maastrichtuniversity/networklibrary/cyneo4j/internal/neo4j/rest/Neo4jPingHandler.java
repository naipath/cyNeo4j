package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.rest;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

class Neo4jPingHandler extends HttpResponseHandler<Boolean> {

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