package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.rest;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.ConnectionParameter;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jGraph;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.util.function.Function;

public class Neo4jRESTClient implements Neo4jClient {

    private static final String DATA_URL = "/db/data/";
    private static final String CYPHER_URL = DATA_URL + "cypher";

    private String instanceLocation = null;
    private Neo4jPingHandler neo4jPingHandler = new Neo4jPingHandler();
    private PassThroughResponseHandler passThroughResponseHandler = new PassThroughResponseHandler();

    public static Neo4jRESTClient create() {
        return new Neo4jRESTClient();
    }

    private Neo4jRESTClient() {
    }


    public boolean checkConnectionParameter(ConnectionParameter connectionParameter) {
        return validateConnection(connectionParameter.getHttpUrl());
    }

    public void connect(ConnectionParameter parameters) {
        connect(parameters.getHttpUrl());
    }

    @Override
    public <T> T executeQuery(CypherQuery query, Function<Object, T> converter) throws IOException {
        return executeQuery(query.toJsonString(), converter);
    }

    private <T> T executeQuery(String query, Function<Object, T> converter) throws IOException {
        return converter.apply(Request.Post(getCypherURL()).bodyString(query, ContentType.APPLICATION_JSON).execute().handleResponse(passThroughResponseHandler));
    }

    @Override
    public Neo4jGraph retrieveData() {
        return null;
    }

    @Override
    public Neo4jGraph executeQuery(CypherQuery cypherQuery) {
        return null;
    }

    public void connect(String instanceLocation) {
        if (validateConnection(this.instanceLocation)) {
            this.instanceLocation = null;
        }
        if (validateConnection(instanceLocation)) {
            this.instanceLocation = instanceLocation;
        }
    }

    public boolean isConnected() {
        return validateConnection(getInstanceLocation());
    }

    public String getInstanceLocation() {
        return instanceLocation;
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
