package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionTarget;

public class Neo4jExtension {

    private String name;
    private String location;

    public Neo4jExtension(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Neo4jExtension(ExtensionTarget type, String name, String extName) {
        this.name = name;
        this.location = buildEndpoint(extName, type);
    }

    public String getName() {
        return name;
    }

    public String getEndpoint() {
        return location;
    }

    private String buildEndpoint(String extName, ExtensionTarget type) {
        String endpoint = extName + "/" + type.toString().toLowerCase() + "/";

        if (!type.isGraphdb()) {
            endpoint = endpoint + "<IDHERE>/";
        }
        return endpoint + name;
    }
}