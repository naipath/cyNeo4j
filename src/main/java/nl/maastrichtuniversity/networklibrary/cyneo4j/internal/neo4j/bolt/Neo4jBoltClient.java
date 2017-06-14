package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.bolt;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.ConnectionParameter;
import org.neo4j.driver.v1.*;

public class Neo4jClient {

    private final ConnectionParameter connectionParameter;
    private Driver driver;

    public Neo4jClient(ConnectionParameter connectionParameter) {
        this.connectionParameter = connectionParameter;
    }

    public void connect() {
        Config noSSL = Config.build().withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig();
        driver = GraphDatabase.driver(
                connectionParameter.getBoltUrl(),
                AuthTokens.basic(
                        connectionParameter.getUsername(),
                        connectionParameter.getPasswordAsString()
                ),noSSL
        );
    }

    public void execute(String query) {
        try (Session session = driver.session()) {
            session.run(query);
        }
    }
}
