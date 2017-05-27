package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils;

public class NeoUtils {
    public static Long extractID(String objUrl) {
        return Long.valueOf(objUrl.substring(objUrl.lastIndexOf('/') + 1));
    }
}
