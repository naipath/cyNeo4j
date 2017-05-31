package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.maastrichtuniversity.MyHttpResponseHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionTarget;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ExtensionParametersResponseHandler extends MyHttpResponseHandler<List<Neo4jExtension>> {

    private String extName;

    public ExtensionParametersResponseHandler(String extName) {
        this.extName = extName;
    }

    @Override
    public List<Neo4jExtension> handle(int responseCode, InputStream content) throws IOException {
        List<Neo4jExtension> res = new ArrayList<>();
        if (responseCode >= 200 && responseCode < 300) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> targets = mapper.readValue(content, Map.class);

            for (Entry<String, Object> target : targets.entrySet()) {

                Map<String, Object> extensions = (Map<String, Object>) target.getValue();

                for (Entry<String, Object> extension : extensions.entrySet()) {

                    Map<String, Object> extensionDesc = (Map<String, Object>) extension.getValue();
                    String name = (String) extensionDesc.get("name");
                    ExtensionTarget type = ExtensionTarget.map(((String) extensionDesc.get("extends")));

                    res.add(new Neo4jExtension(type, name, extName));
                }
            }
        } else {
            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> error = mapper.readValue(content, Map.class);
            System.out.println(error); // TODO
        }
        return res;
    }

}
