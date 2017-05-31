package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.NeoNetworkAnalyzerAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import org.cytoscape.application.swing.AbstractCyAction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steven on 27-05-17.
 */
public class Neo4JExtensionRegister {

    private final Map<String, AbstractCyAction> stringAbstractCyActionMap;
    private final Plugin plugin;
    private final Neo4JExtensions neo4JExtensions;

    public Neo4JExtensionRegister(Neo4JExtensions neo4JExtensions, Plugin plugin) {
        this.stringAbstractCyActionMap = new HashMap<>();
        this.plugin = plugin;
        this.neo4JExtensions = neo4JExtensions;
    }



    public void registerExtension() {
        for (Neo4jExtension ext : neo4JExtensions.getExtensions()) {
            plugin.registerAction(stringAbstractCyActionMap.get(ext.getName()));
        }
    }

    public void put(String name, AbstractCyAction cyAction) {
        stringAbstractCyActionMap.put(name, cyAction);
    }
}
