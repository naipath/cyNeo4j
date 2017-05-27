package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionTarget;

import java.util.ArrayList;
import java.util.List;

import static nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionTarget.NODE;
import static nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionTarget.RELATIONSHIP;

public class Neo4jExtension {

    private ExtensionTarget type;
    private String name;
    private String location;
    private List<Neo4jExtParam> parameters = new ArrayList<>();

    public Neo4jExtension(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Neo4jExtension(ExtensionTarget type, String name, String extName) {
        this.type = type;
        this.name = name;
        this.location = buildEndpoint(extName);
    }

    public String getName() {
        return name;
    }

    public void addParameter(Neo4jExtParam param) {
        parameters.add(param);
    }

    public String getEndpoint() {
        return location;
    }

    public void setParameters(List<Neo4jExtParam> params) {
        this.parameters = params;
    }

    private String buildEndpoint(String extName) {
        String endpoint = extName + "/" + type.toString().toLowerCase() + "/";

        if (type == NODE || type == RELATIONSHIP) {
            endpoint = endpoint + "<IDHERE>/";
        }
        return endpoint + name;
    }
}
