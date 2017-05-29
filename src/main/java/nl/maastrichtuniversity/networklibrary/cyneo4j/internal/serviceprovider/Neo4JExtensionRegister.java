package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import org.cytoscape.application.swing.AbstractCyAction;
import java.util.Map;

/**
 * Created by steven on 27-05-17.
 */
public class LocalExtensionsMap {

    private final Map<String, AbstractCyAction> localExtensions;
    private final Plugin plugin;
    private final Neo4JExtensions neo4JExtensions;

    public LocalExtensionsMap(Neo4JExtensions neo4JExtensions, Map<String, AbstractCyAction> nameToCyActionMap, Plugin plugin) {
        this.localExtensions = nameToCyActionMap;
        this.plugin = plugin;
        this.neo4JExtensions = neo4JExtensions;
    }

    public void registerExtension() {
        for (Neo4jExtension ext : neo4JExtensions.getExtensions()) {
            plugin.registerAction(localExtensions.get(ext.getName()));
        }
    }

}
