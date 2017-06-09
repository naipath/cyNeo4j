package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.maastrichtuniversity.networklibrary.cyneo4j.HttpResponseHandler;

import java.io.IOException;
import java.io.InputStream;

public class PassThroughResponseHandler extends HttpResponseHandler<Object> {

    @Override
    public Object handle(int responseCode, InputStream content) throws IOException {

        if (responseCode >= 200 && responseCode < 300) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(content, Object.class);//FIXME: implicit Map
        }
        System.out.println("ERROR " + responseCode); //TODO
        return null;
    }

}
