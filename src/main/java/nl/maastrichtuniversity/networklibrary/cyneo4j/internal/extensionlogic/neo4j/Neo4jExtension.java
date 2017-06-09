package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionTarget;

public class Neo4jExtension {

    private String name;

    public Neo4jExtension(String name, String location) {
        this.name = name;
    }

    public Neo4jExtension(ExtensionTarget type, String name, String extName) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}