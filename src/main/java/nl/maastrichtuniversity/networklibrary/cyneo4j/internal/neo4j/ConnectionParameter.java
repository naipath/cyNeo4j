package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j;

public class ConnectionParameter {
    private final String host;
    private final String username;
    private final char[] password;

    public ConnectionParameter(String url, String username, char[] password) {
        this.host = url;
        this.username = username;
        this.password = password;
    }

    public String getHttpUrl() {
        return String.format("http://%s:%s@%s:7474", username, new String(password), host);
    }

    public String getBoltUrl() {
        return "bolt://" + host;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordAsString() {
        return new String(password);
    }
}
