package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j;

public class Neo4jExtParam {

    private String name;
    private boolean optional;

    public Neo4jExtParam(String name, boolean optional) {
        this.name = name;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    boolean isOptional() {
        return optional;
    }


}
