package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionTarget;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.NeoUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionTarget.*;

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
                    ExtensionTarget type = decideExtensionType((String) extensionDesc.get("extends"));
                    String name = (String) extensionDesc.get("name");

                    List<Map<String, Object>> parameters = (List<Map<String, Object>>) extensionDesc.get("parameters");

                    Neo4jExtension currExt = new Neo4jExtension(type, name, extName);

                    for (Map<String, Object> parameter : parameters) {
                        currExt.addParameter(NeoUtils.parseExtParameter(parameter));
                    }
                    res.add(currExt);
                }
            }
        } else {
            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> error = mapper.readValue(content, Map.class);
            System.out.println(error); // TODO
        }

        return res;
    }

    protected ExtensionTarget decideExtensionType(String target) {
        switch (target) {
            case "graphdb":
                return GRAPHDB;
            case "node":
                return NODE;
            case "relationship":
                return RELATIONSHIP;
            default:
                return null;
        }
    }
}
