package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.PassThroughResponseHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.general.Neo4jPingHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncDownTask;
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

public class Neo4jRESTServer {

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

    public static Neo4jRESTServer create(ServiceLocator serviceLocator) {
        Neo4jRESTServer neo4jRESTServer = new Neo4jRESTServer();
        neo4jRESTServer.cyNetworkManager = serviceLocator.getService(CyNetworkManager.class);
        neo4jRESTServer.cyNetworkFactory = serviceLocator.getService(CyNetworkFactory.class);
        neo4jRESTServer.cyNetViewMgr = serviceLocator.getService(CyNetworkViewManager.class);
        neo4jRESTServer.cyNetworkViewFactory = serviceLocator.getService(CyNetworkViewFactory.class);
        neo4jRESTServer.cyLayoutAlgorithmManager = serviceLocator.getService(CyLayoutAlgorithmManager.class);
        neo4jRESTServer.visualMappingManager = serviceLocator.getService(VisualMappingManager.class);
        neo4jRESTServer.dialogTaskManager = serviceLocator.getService(DialogTaskManager.class);
        return neo4jRESTServer;
    }

    private Neo4jRESTServer() {
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
        TaskIterator it = new TaskIterator(new SyncDownTask(
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

    public Object executeExtensionCall(Neo4jCall call) {
        Object retVal = null;
        try {
            String url = call.getUrlFragment();
            retVal = Request.Post(url).bodyString(call.getPayload(), APPLICATION_JSON).execute().handleResponse(new PassThroughResponseHandler());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public boolean validateConnection(String instanceLocation) {
        try {
            return instanceLocation != null && Request.Get(instanceLocation).execute().handleResponse(new Neo4jPingHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO fix error messages | show exceptions? does the user understand the error messages?
        return false;
    }
}
