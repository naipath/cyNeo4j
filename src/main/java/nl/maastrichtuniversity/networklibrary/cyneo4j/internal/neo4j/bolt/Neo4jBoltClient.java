package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.bolt;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.ConnectionParameter;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jGraph;
import org.neo4j.driver.v1.*;

import java.util.function.Function;

public class Neo4jBoltClient implements Neo4jClient {

    private Driver driver;


    public void execute(String query) {
        try (Session session = driver.session()) {
            StatementResult statementResult = session.run(query);
            statementResult.forEachRemaining(record -> record.asMap());
        }
    }

    @Override
    public void connect(ConnectionParameter connectionParameter) {
        Config noSSL = Config.build().withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig();
        driver = GraphDatabase.driver(
                connectionParameter.getBoltUrl(),
                AuthTokens.basic(
                        connectionParameter.getUsername(),
                        connectionParameter.getPasswordAsString()
                ),noSSL
        );
    }

    @Override
    public <T> T executeQuery(CypherQuery query, Function<Object, T> converter) {
        return null;
    }

    @Override
    public Neo4jGraph retrieveData() {
        return null;
    }

    @Override
    public Neo4jGraph executeQuery(CypherQuery cypherQuery) {
        return null;
    }

    @Override
    public boolean checkConnectionParameter(ConnectionParameter connectionParameter) {
        return false;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void syncDown() {

    }
}
