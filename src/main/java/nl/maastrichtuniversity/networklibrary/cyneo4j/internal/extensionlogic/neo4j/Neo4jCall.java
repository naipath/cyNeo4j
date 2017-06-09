package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j;

public class Neo4jCall {
    private String urlFragment;
    private String payload;

    public String getUrlFragment() {
        return urlFragment;
    }

    public String getPayload() {
        return payload;
    }

    public Neo4jCall(String urlFragment, String payload) {
        this.urlFragment = urlFragment;
        this.payload = payload;
    }

}
