package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.maastrichtuniversity.networklibrary.cyneo4j.HttpResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IdListHandler extends HttpResponseHandler<List<Long>> {

    @Override
    public List<Long> handle(int responseCode, InputStream content) throws IOException {
        List<Long> ids = null;
        if (responseCode >= 200 && responseCode < 300) {

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> wrapper = mapper.readValue(content, Map.class);
            List<List<Integer>> queryRes = (List<List<Integer>>) wrapper.get("data");
            ids = new ArrayList<>();

            for (List<Integer> id : queryRes) {
                ids.add(id.get(0).longValue());
            }
        }
        return ids;
    }

}
