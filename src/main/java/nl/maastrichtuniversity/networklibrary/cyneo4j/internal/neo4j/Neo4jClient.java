package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphObject;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.AuthenticationException;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;

import java.util.List;

public class Neo4jClient {

    private Driver driver;
    private Neo4jGraphFactory neo4JGraphFactory = new Neo4jGraphFactory();

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
    public List<GraphObject> executeQuery(CypherQuery cypherQuery) {
        try (Session session = driver.session()) {
            StatementResult statementResult = session.run(cypherQuery.getQuery(), cypherQuery.getParams());
            return statementResult.list( neo4JGraphFactory::create);
        }
    }

    public boolean isConnected() {
        return driver != null && driver.session().isOpen();
    }

}
