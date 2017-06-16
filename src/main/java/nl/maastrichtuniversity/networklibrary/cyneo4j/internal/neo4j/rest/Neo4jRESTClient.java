package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.rest;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.ConnectionParameter;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jGraph;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.retrievedata.RetrieveDataTask;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.swing.DialogTaskManager;

import java.io.IOException;
import java.util.function.Function;

public class Neo4jRESTClient implements Neo4jClient {

    private static final String DATA_URL = "/db/data/";
    private static final String CYPHER_URL = DATA_URL + "cypher";

    private String instanceLocation = null;
    private CyNetworkManager cyNetworkManager;
    private CyNetworkFactory cyNetworkFactory;
    private CyNetworkViewManager cyNetViewMgr;
    private CyNetworkViewFactory cyNetworkViewFactory;
    private CyLayoutAlgorithmManager cyLayoutAlgorithmManager;
    private VisualMappingManager visualMappingManager;
    private DialogTaskManager dialogTaskManager;
    private Neo4jPingHandler neo4jPingHandler = new Neo4jPingHandler();
    private PassThroughResponseHandler passThroughResponseHandler = new PassThroughResponseHandler();

    public static Neo4jRESTClient create(ServiceLocator serviceLocator) {
        Neo4jRESTClient neo4JRESTClient = new Neo4jRESTClient();
        neo4JRESTClient.cyNetworkManager = serviceLocator.getService(CyNetworkManager.class);
        neo4JRESTClient.cyNetworkFactory = serviceLocator.getService(CyNetworkFactory.class);
        neo4JRESTClient.cyNetViewMgr = serviceLocator.getService(CyNetworkViewManager.class);
        neo4JRESTClient.cyNetworkViewFactory = serviceLocator.getService(CyNetworkViewFactory.class);
        neo4JRESTClient.cyLayoutAlgorithmManager = serviceLocator.getService(CyLayoutAlgorithmManager.class);
        neo4JRESTClient.visualMappingManager = serviceLocator.getService(VisualMappingManager.class);
        neo4JRESTClient.dialogTaskManager = serviceLocator.getService(DialogTaskManager.class);
        return neo4JRESTClient;
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

    public void syncDown() {
        TaskIterator it = new TaskIterator(new RetrieveDataTask(
            getInstanceLocation(),
            this,
            cyNetworkFactory,
            cyNetworkManager,
            cyNetViewMgr,
            cyNetworkViewFactory,
            cyLayoutAlgorithmManager,
            visualMappingManager
        ));
        dialogTaskManager.execute(it);
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
