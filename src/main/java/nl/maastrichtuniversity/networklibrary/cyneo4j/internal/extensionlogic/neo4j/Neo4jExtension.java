package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionTarget;

import java.util.ArrayList;
import java.util.List;

public class Neo4jExtension {

    private ExtensionTarget type;
    private String name;
    private String location;

    private List<Neo4jExtParam> parameters = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEndpoint(String endpoint) {
        this.location = endpoint;
    }

    public void addParameter(Neo4jExtParam param) {
        parameters.add(param);
    }

    public void setType(ExtensionTarget type) {
        this.type = type;
    }

    public ExtensionTarget getType() {
        return type;
    }

    public String getEndpoint() {
        return location;
    }

    public void setParameters(List<Neo4jExtParam> params) {
        this.parameters = params;

    }
}
