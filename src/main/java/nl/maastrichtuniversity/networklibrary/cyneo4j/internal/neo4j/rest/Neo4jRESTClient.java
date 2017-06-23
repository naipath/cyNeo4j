package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.rest;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.ResultObject;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.ConnectionParameter;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jGraph;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Neo4jRESTClient implements Neo4jClient {

    private static final String DATA_URL = "/db/data/";
    private static final String CYPHER_URL = DATA_URL + "cypher";

    private String httpUrl = null;
    private Neo4jPingHandler neo4jPingHandler = new Neo4jPingHandler();
    private PassThroughResponseHandler passThroughResponseHandler = new PassThroughResponseHandler();

    @Override
    public boolean connect(ConnectionParameter connectionParameter) {
        if (validateConnection(connectionParameter.getHttpUrl())) {
            connect(connectionParameter.getHttpUrl());
            return true;
        }
        return false;
    }

    @Override
    public <T> Neo4jGraph<T> executeQuery(CypherQuery query, Function<Map<String,Object>, T> fn) {
        try {
            return (Neo4jGraph)Request.Post(getCypherURL()).bodyString(query.toJsonString(), ContentType.APPLICATION_JSON)
                .execute().handleResponse(passThroughResponseHandler);
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    @Override
    public Neo4jGraph<Long> executeQueryIdList(CypherQuery cypherQuery) {
        return null;
    }

    @Override
    public Neo4jGraph<ResultObject> executeQueryResultObject(CypherQuery cypherQuery) {
        return null;
    }

    private void connect(String instanceLocation) {
        if (validateConnection(this.httpUrl)) {
            this.httpUrl = null;
        }
        if (validateConnection(instanceLocation)) {
            this.httpUrl = instanceLocation;
        }
    }

    public boolean isConnected() {
        return validateConnection(httpUrl);
    }

    public String getCypherURL() {
        return httpUrl + CYPHER_URL;
    }


    public boolean validateConnection(String instanceLocation) {
        try {
            return instanceLocation != null && Request.Get(instanceLocation).execute().handleResponse(neo4jPingHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO fix error messages | show exceptions? does the user understand the error messages?
        return false;
    }
}
