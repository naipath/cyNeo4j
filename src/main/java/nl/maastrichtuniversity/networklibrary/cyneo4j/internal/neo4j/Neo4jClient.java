package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j;

import java.io.IOException;
import java.util.function.Function;

public interface Neo4jClient {

    void connect(ConnectionParameter connectionParameter);
    <T> T executeQuery(CypherQuery query, Function<Object, T> converter) throws IOException;
    Neo4jGraph retrieveData();
    Neo4jGraph executeQuery(CypherQuery cypherQuery);
    boolean checkConnectionParameter(ConnectionParameter connectionParameter);
    boolean isConnected();
}
