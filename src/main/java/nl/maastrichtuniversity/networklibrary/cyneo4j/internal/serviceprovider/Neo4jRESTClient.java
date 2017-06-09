package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.handler.Neo4jPingHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.handler.PassThroughResponseHandler;
import org.apache.http.client.fluent.Request;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.swing.DialogTaskManager;

import java.io.IOException;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class Neo4jRESTClient {

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

    public void connect(String instanceLocation) {
        if (validateConnection(this.instanceLocation)) {
            this.instanceLocation = null;
        }
        if (validateConnection(instanceLocation)) {
            this.instanceLocation = instanceLocation;
        }
    }

    boolean isConnected() {
        return validateConnection(getInstanceLocation());
    }

    public String getInstanceLocation() {
        return instanceLocation;
    }

    void syncDown() {
        TaskIterator it = new TaskIterator(new RetrieveDataTask(
            getCypherURL(),
            getInstanceLocation(),
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

    public Object executeExtensionCall(String url, String payload) {
        Object retVal = null;
        try {
            retVal = Request.Post(url).bodyString(payload, APPLICATION_JSON).execute().handleResponse(passThroughResponseHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return retVal;
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
