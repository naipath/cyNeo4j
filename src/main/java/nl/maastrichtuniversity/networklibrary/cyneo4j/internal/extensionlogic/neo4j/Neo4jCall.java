package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j;

public class Neo4jCall {
    private String urlFragment;
    private String payload;
    private boolean async;

    public String getUrlFragment() {
        return urlFragment;
    }

    public String getPayload() {
        return payload;
    }

    public Neo4jCall(String urlFragment, String payload, boolean async) {
        this.urlFragment = urlFragment;
        this.payload = payload;
        this.async = async;
    }

    public String toString() {
        return "Neo4jCall [urlFragment=" + urlFragment + ", payload=" + payload + "]";
    }

    public boolean isAsync() {
        return async;
    }


}
