package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.maastrichtuniversity.networklibrary.cyneo4j.MyHttpResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public class ExtensionLocationsHandler extends MyHttpResponseHandler<Set<String>> {

    @Override
    public Set<String> handle(int responseCode, InputStream content) throws IOException {
        Set<String> res = null;
        if (responseCode >= 200 && responseCode < 300) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> instanceResp = mapper.readValue(content, Map.class);
            res = instanceResp.keySet();
        }
        return res;
    }
}