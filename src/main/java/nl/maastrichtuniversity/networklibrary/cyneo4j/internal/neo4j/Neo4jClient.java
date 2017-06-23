package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j;

public interface Neo4jClient {

    boolean connect(ConnectionParameter connectionParameter);
    Neo4jGraph executeQuery(CypherQuery cypherQuery);
    boolean isConnected();
}
