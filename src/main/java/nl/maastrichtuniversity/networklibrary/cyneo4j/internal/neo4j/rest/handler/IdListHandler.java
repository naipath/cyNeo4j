package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.maastrichtuniversity.networklibrary.cyneo4j.HttpResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class IdListHandler extends HttpResponseHandler<List<Long>> {

    @Override
    public List<Long> handle(int responseCode, InputStream content) throws IOException {
        if (responseCode >= 200 && responseCode < 300) {

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> wrapper = mapper.readValue(content, Map.class);
            List<List<Integer>> queryRes = (List<List<Integer>>) wrapper.get("data");

            return queryRes.stream()
                .map(ids -> ids.get(0))
                .map(Integer::longValue)
                .collect(toList());
        }
        return null;
    }
}
