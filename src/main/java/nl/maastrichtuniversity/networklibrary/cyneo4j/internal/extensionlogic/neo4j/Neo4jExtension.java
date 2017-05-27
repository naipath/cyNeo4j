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

    public List<Neo4jExtParam> getParameters() {
        return parameters;
    }

    public void addParameter(Neo4jExtParam param) {
        getParameters().add(param);
    }

    public String toString() {
        StringBuilder strbuff = new StringBuilder();
        strbuff.append("name: ").append(getName()).append(" endpoint: ").append(getEndpoint()).append(" of type: ").append(getType()).append("\n");
        strbuff.append("\nrequired parameters: \n");

        for (Neo4jExtParam param : getParameters()) {
            strbuff.append("\tparameter: ").append(param.getName()).append(" is optional? ").append(param.isOptional()).append("\n");
        }

        return strbuff.toString();
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
