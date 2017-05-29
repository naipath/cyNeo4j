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
public class LocalExtensionsMap {

    private final Map<String, AbstractCyAction> localExtensions;
    private final Neo4jRESTServer neo4jRESTServer;
    private final Plugin plugin;
    private final LocalExtensions localExtensionsX;

    public LocalExtensionsMap(Map<String, AbstractCyAction> localExtensions, Neo4jRESTServer neo4jRESTServer, Plugin plugin) {
        this.localExtensions = localExtensions;
        this.neo4jRESTServer = neo4jRESTServer;
        this.plugin = plugin;
        this.localExtensionsX = new LocalExtensions(localExtensions.keySet(), neo4jRESTServer);
    }

    public void registerExtension() {
        for (Extension ext : localExtensionsX.getExtensions()) {
            plugin.registerAction(localExtensions.get(ext.getName()));
        }
    }

}
