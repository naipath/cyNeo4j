package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtParam;

import java.util.Map;

public class NeoUtils {
    public static Long extractID(String objUrl) {
        return Long.valueOf(objUrl.substring(objUrl.lastIndexOf('/') + 1));
    }

    public static Neo4jExtParam parseExtParameter(Map<String, Object> json) {
        return new Neo4jExtParam((String) json.get("name"));
    }
}
