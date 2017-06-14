package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.connect;

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
        return "http://" + username + ":" +  new String(password) + "@" + host + ":7474";
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password;
    }

    public String getBoltUrl() {
        return "bolt://" + host;
    }

    public String getPasswordAsString() {
        return new String(password);
    }
}
