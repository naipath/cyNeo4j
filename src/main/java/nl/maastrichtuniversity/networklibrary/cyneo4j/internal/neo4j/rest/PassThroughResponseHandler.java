package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.ResultObject;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jGraph;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

class PassThroughResponseHandler extends HttpResponseHandler<Object> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Object handle(int responseCode, InputStream content) throws IOException {
        if (responseCode >= 200 && responseCode < 300) {
            Map<String, Object> map = mapper.readValue(content, Map.class);
            return new Neo4jGraph((List<List<ResultObject>>) map.get("data"));
        }
        return null;
    }

}
