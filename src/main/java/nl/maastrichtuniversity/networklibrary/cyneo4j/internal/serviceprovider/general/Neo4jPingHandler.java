package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.maastrichtuniversity.networklibrary.cyneo4j.MyHttpResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class Neo4jPingHandler extends MyHttpResponseHandler<Boolean> {

    @Override
    public Boolean handle(int responseCode, InputStream content) throws IOException {
        if (responseCode >= 200 && responseCode < 300) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> instanceResp = mapper.readValue(content, Map.class);
            if (instanceResp.containsKey("data")) {
                return true;
            }
        }
        return false;
    }
}