package nl.maastrichtuniversity.networklibrary.cyneo4j;

import org.neo4j.driver.v1.*;

public class Neo4jClient {

    private final String url;
    private final String username;
    private final String password;
    private Driver driver;

    public Neo4jClient(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void connect() {
        Config noSSL = Config.build().withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig();
        driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j","test"),noSSL); // <password>
    }

    public void execute(String query) {
        try (Session session = driver.session()) {
            session.run(query);
        }
    }
}
