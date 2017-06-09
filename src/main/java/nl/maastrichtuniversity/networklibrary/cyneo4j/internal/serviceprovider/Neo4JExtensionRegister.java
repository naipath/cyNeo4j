package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ActionRegister;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import org.cytoscape.application.swing.AbstractCyAction;

import java.util.HashMap;
import java.util.Map;

public class Neo4JExtensionRegister {

    private final Map<String, AbstractCyAction> stringAbstractCyActionMap;
    private final ActionRegister actionRegister;
    private final Neo4JExtensions neo4JExtensions;

    public Neo4JExtensionRegister(Neo4JExtensions neo4JExtensions, ActionRegister actionRegister) {
        this.stringAbstractCyActionMap = new HashMap<>();
        this.actionRegister = actionRegister;
        this.neo4JExtensions = neo4JExtensions;
    }

    public void registerExtension() {
        for (Neo4jExtension ext : neo4JExtensions.getExtensions()) {
            System.out.println("Register ext " + ext.getName() + " contains : " + stringAbstractCyActionMap.containsKey(ext.getName()));
            actionRegister.registerAction(stringAbstractCyActionMap.get(ext.getName()));
        }
    }

    public void put(String name, AbstractCyAction cyAction) {
        stringAbstractCyActionMap.put(name, cyAction);
    }
}
