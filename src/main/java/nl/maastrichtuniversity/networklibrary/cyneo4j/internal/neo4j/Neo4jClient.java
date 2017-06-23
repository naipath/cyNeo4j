package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.ResultObject;

import java.util.Map;
import java.util.function.Function;

public interface Neo4jClient {

    boolean connect(ConnectionParameter connectionParameter);
    <T> Neo4jGraph<T> executeQuery(CypherQuery cypherQuery, Function<Map<String,Object>,T> fn);
    Neo4jGraph<Long> executeQueryIdList(CypherQuery cypherQuery);
    Neo4jGraph<ResultObject> executeQueryResultObject(CypherQuery cypherQuery);
    boolean isConnected();
}
