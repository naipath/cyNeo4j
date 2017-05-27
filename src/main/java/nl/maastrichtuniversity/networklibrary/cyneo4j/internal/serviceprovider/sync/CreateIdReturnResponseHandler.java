package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CreateIdReturnResponseHandler implements ResponseHandler<Long> {

    @Override
    public Long handleResponse(HttpResponse response) throws IOException {
        int responseCode = response.getStatusLine().getStatusCode();

        if (responseCode >= 200 && responseCode < 300) {

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> wrapper = mapper.readValue(response.getEntity().getContent(), Map.class);

            List<List<Integer>> queryRes = (List<List<Integer>>) wrapper.get("data");

            return queryRes.get(0).get(0).longValue();

        }

        System.out.println("ERROR " + responseCode);
        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> error = mapper.readValue(response.getEntity().getContent(), Map.class);
        System.out.println(error);

        return null;
    }

}
