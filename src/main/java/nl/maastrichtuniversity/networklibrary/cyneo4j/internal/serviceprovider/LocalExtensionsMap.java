package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtParam;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.ExtensionLocationsHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.ExtensionParametersResponseHandler;
import org.apache.http.client.fluent.Request;
import org.cytoscape.application.swing.AbstractCyAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.singletonList;

/**
 * Created by steven on 27-05-17.
 */
public class LocalExtensions {

    private final Map<String, AbstractCyAction> localExtensions;
    private final Neo4jRESTServer neo4jRESTServer;
    private final Plugin plugin;

    public LocalExtensions(Map<String, AbstractCyAction> localExtensions, Neo4jRESTServer neo4jRESTServer, Plugin plugin) {
        this.localExtensions = localExtensions;
        this.neo4jRESTServer = neo4jRESTServer;
        this.plugin = plugin;
    }

    private void registerExtension() {
        for (Extension ext : getExtensions()) {
            plugin.registerAction(localExtensions.get(ext.getName()));
        }
    }

    public Extension supportsExtension(String name) {
        List<Extension> extensions = getExtensions();

        for (Extension extension : extensions) {
            if (extension.getName().equals(name)) {
                return extension;
            }
        }
        return null;
    }

    private List<Extension> getExtensions() {
        List<Extension> res = new ArrayList<>();

        Extension cypherExt = new Neo4jExtension();
        cypherExt.setName("cypher");
        cypherExt.setEndpoint(neo4jRESTServer.getCypherURL());

        cypherExt.setParameters(singletonList(new Neo4jExtParam("cypherQuery", "Cypher Endpoint", false, String.class)));

        if (localExtensions.containsKey("cypher")) {
            res.add(cypherExt);
        }
        try {
            Set<String> extNames = Request.Get(neo4jRESTServer.getInstanceLocation() + Neo4jRESTServer.EXT_URL).execute().handleResponse(new ExtensionLocationsHandler());

            for (String extName : extNames) {
                List<Extension> serverSupportedExt = Request.Get(neo4jRESTServer.getInstanceLocation() + Neo4jRESTServer.EXT_URL + extName).execute().handleResponse(new ExtensionParametersResponseHandler(neo4jRESTServer.getInstanceLocation() + Neo4jRESTServer.EXT_URL + extName));

                for (Extension ext : serverSupportedExt) {
                    if (localExtensions.containsKey(ext.getName())) {
                        res.add(ext);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return res;
    }



}
