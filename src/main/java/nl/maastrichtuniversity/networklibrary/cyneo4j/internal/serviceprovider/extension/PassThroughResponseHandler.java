package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.util.Map;

public class PassThroughResponseHandler implements ResponseHandler<Object> {

    @Override
    public Object handleResponse(HttpResponse response) throws IOException {
        int responseCode = response.getStatusLine().getStatusCode();

        if (responseCode >= 200 && responseCode < 300) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getEntity().getContent(), Object.class);
        }
        System.out.println("ERROR " + responseCode); //TODO
        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> error = mapper.readValue(response.getEntity().getContent(), Map.class);
        System.out.println(error); //TODO
        return null;
    }

}
