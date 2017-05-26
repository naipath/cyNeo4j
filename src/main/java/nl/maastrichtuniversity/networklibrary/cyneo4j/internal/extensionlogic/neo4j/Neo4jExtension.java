package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j;

import java.util.ArrayList;
import java.util.List;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionParameter;

public class Neo4jExtension implements Extension {

    public enum ExtensionTarget {NODE, RELATIONSHIP, GRAPHDB}

    private ExtensionTarget type;
    private String name;
    private String location;
    private String description;

    private List<ExtensionParameter> parameters = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEndpoint(String endpoint) {
        this.location = endpoint;
    }

    public List<ExtensionParameter> getParameters() {
        return parameters;
    }

    public void addParameter(Neo4jExtParam param) {
        getParameters().add(param);
    }

    public String toString() {
        StringBuilder strbuff = new StringBuilder();
        strbuff.append("name: ").append(getName()).append(" endpoint: ").append(getEndpoint()).append(" of type: ").append(getType()).append("\n");
        strbuff.append("\nrequired parameters: \n");

        for (ExtensionParameter param : getParameters()) {
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

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getEndpoint() {
        return location;
    }

    @Override
    public void setDescription(String desc) {
        this.description = desc;

    }

    @Override
    public void setParameters(List<ExtensionParameter> params) {
        this.parameters = params;

    }
}
