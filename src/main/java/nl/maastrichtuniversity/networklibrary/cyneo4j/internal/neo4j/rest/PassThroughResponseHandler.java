package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.rest;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

class PassThroughResponseHandler extends HttpResponseHandler<Object> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Object handle(int responseCode, InputStream content) throws IOException {
        if (responseCode >= 200 && responseCode < 300) {
            return mapper.readValue(content, Object.class);
        }
        return null;
    }

}
