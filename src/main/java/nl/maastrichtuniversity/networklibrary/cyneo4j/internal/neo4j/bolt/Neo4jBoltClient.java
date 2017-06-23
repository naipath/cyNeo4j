package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.bolt;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.ConnectionParameter;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jGraph;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.AuthenticationException;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;
import org.neo4j.driver.v1.types.MapAccessor;
import org.neo4j.driver.v1.util.Pair;

import static java.util.stream.Collectors.toList;

public class Neo4jBoltClient implements Neo4jClient {

    private Driver driver;

    @Override
    public boolean connect(ConnectionParameter connectionParameter) {
        try {
            driver = GraphDatabase.driver(
                connectionParameter.getBoltUrl(),
                AuthTokens.basic(
                    connectionParameter.getUsername(),
                    connectionParameter.getPasswordAsString()
                ),
                Config.build().withoutEncryption().toConfig()
            );
            return true;
        } catch (AuthenticationException | ServiceUnavailableException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Neo4jGraph retrieveData() {
        return null;
    }

    @Override
    public Neo4jGraph executeQuery(CypherQuery cypherQuery) {
        try (Session session = driver.session()) {
            StatementResult statementResult = session.run(cypherQuery.getQuery());
            new Neo4jGraph(
                statementResult.list(record -> record.fields()
                    .stream()
                    .map(Pair::value)
                    .map(MapAccessor::asMap)
                    .collect(toList()))
            );
        }
        throw new IllegalStateException("There are no results");
    }

    @Override
    public boolean isConnected() {
        return driver != null && driver.session().isOpen();
    }

}
