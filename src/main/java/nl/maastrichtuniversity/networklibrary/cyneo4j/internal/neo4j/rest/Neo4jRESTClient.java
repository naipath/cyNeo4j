package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.rest;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.ConnectionParameter;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jGraph;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.util.List;

public class Neo4jRESTClient implements Neo4jClient {

    private static final String DATA_URL = "/db/data/";
    private static final String CYPHER_URL = DATA_URL + "cypher";

    private String instanceLocation = null;
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
    public Neo4jGraph executeQuery(CypherQuery query) {
        try {
            return new Neo4jGraph((List<List<Object>>) Request.Post(getCypherURL()).bodyString(query.toJsonString(), ContentType.APPLICATION_JSON).execute().handleResponse(passThroughResponseHandler));
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    private void connect(String instanceLocation) {
        if (validateConnection(this.instanceLocation)) {
            this.instanceLocation = null;
        }
        if (validateConnection(instanceLocation)) {
            this.instanceLocation = instanceLocation;
        }
    }

    public boolean isConnected() {
        return validateConnection(instanceLocation);
    }

    public String getCypherURL() {
        return instanceLocation + CYPHER_URL;
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
