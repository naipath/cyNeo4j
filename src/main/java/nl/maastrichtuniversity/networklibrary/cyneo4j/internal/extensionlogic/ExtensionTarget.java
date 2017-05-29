package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

public enum ExtensionTarget {
    NODE, RELATIONSHIP, GRAPHDB;

    public static ExtensionTarget map(String target) {
        switch (target) {
            case "graphdb":
                return GRAPHDB;
            case "node":
                return NODE;
            case "relationship":
                return RELATIONSHIP;
            default:
                return null;
        }
    }

    public boolean isGraphdb() {
        return this == GRAPHDB;
    }
}