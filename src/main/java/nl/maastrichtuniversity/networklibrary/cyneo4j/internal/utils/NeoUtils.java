package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils;

import java.util.Map;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtParam;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;

public class NeoUtils {
    public static Long extractID(String objUrl) {
        return Long.valueOf(objUrl.substring(objUrl.lastIndexOf('/') + 1));
    }

    public static Neo4jExtParam parseExtParameter(Map<String, Object> json) {
        return new Neo4jExtParam((String) json.get("name"), (String) json.get("description"), (Boolean) json.get("optional"), decideParameterType((String) json.get("type")));
    }

    public static Class<?> decideParameterType(String typeStr) {
        switch (typeStr) {
            case "string":
                return String.class;
            case "integer":
                return Integer.class;
            case "strings":
                return String[].class;
            case "node":
                return CyNode.class;
            case "relationship":
                return CyEdge.class;
            default:
                return null;
        }
    }
}
