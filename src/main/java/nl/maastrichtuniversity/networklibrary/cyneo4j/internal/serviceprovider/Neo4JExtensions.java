package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.ExtensionLocationsHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.ExtensionParametersResponseHandler;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by steven on 27-05-17.
 */
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

    public List<Neo4jExtension> getExtensions() {
        List<Neo4jExtension> res = new ArrayList<>();

        Neo4jExtension cypherExt = new Neo4jExtension("cypher", neo4jRESTServer.getCypherURL());

        //cypherExt.setParameters(singletonList(new Neo4jExtParam("cypherQuery", "Cypher Endpoint", false, String.class)));

        res.add(cypherExt);
        try {
            Set<String> extNames = Request.Get(neo4jRESTServer.getInstanceLocation() + Neo4jRESTServer.EXT_URL).execute().handleResponse(new ExtensionLocationsHandler());

            for (String extName : extNames) {
                List<Neo4jExtension> serverSupportedExt = Request.Get(neo4jRESTServer.getInstanceLocation() + Neo4jRESTServer.EXT_URL + extName).execute().handleResponse(new ExtensionParametersResponseHandler(neo4jRESTServer.getInstanceLocation() + Neo4jRESTServer.EXT_URL + extName));

                for (Neo4jExtension ext : serverSupportedExt) {
                    res.add(ext);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
