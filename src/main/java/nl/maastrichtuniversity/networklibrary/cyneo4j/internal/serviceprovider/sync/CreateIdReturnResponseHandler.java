package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.maastrichtuniversity.networklibrary.cyneo4j.MyHttpResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class CreateIdReturnResponseHandler extends MyHttpResponseHandler<Long> {

    @Override
    public Long handle(int responseCode, InputStream content) throws IOException {
        if (responseCode >= 200 && responseCode < 300) {

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> wrapper = mapper.readValue(content, Map.class);

            List<List<Integer>> queryRes = (List<List<Integer>>) wrapper.get("data");

            return queryRes.get(0).get(0).longValue();

        }

        System.out.println("ERROR " + responseCode);
        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> error = mapper.readValue(content, Map.class);
        System.out.println(error);
        return null;
    }

}
