package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.ExtensionLocationsHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.ExtensionParametersResponseHandler;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Neo4JExtensions {

    private final Neo4jRESTServer neo4jRESTServer;

    public Neo4JExtensions(Neo4jRESTServer neo4jRESTServer) {
        this.neo4jRESTServer = neo4jRESTServer;
    }

    public Neo4jExtension supportsExtension(String name) {
        List<Neo4jExtension> extensions = getExtensions();

        for (Neo4jExtension extension : extensions) {
            if (extension.getName().equals(name)) {
                return extension;
            }
        }
        return null;
    }

    List<Neo4jExtension> getExtensions() {
        return Arrays.asList(new Neo4jExtension("cypher", neo4jRESTServer.getCypherURL()));
    }
}
