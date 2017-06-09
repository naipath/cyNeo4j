package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;

import java.util.Arrays;
import java.util.List;

public class Neo4JExtensions {

    private final Neo4jRESTServer neo4jRESTServer;

    public Neo4JExtensions(Neo4jRESTServer neo4jRESTServer) {
        this.neo4jRESTServer = neo4jRESTServer;
    }

    List<Neo4jExtension> getExtensions() {
        return Arrays.asList(new Neo4jExtension("cypher", neo4jRESTServer.getCypherURL()));
    }
}
