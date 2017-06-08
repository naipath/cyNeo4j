package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.PassThroughResponseHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.general.Neo4jPingHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncDownTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncUpTask;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Request;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.swing.DialogTaskManager;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class Neo4jRESTServer {

    private static final String DATA_URL = "/db/data/";
    private static final String CYPHER_URL = DATA_URL + "cypher";
    static final String EXT_URL = DATA_URL + "ext/";

    private String instanceLocation = null;
    protected ExecutorService threadpool;
    protected Async async;
    private CyNetworkManager cyNetworkManager;
    private CyNetworkFactory cyNetworkFactory;
    private CyNetworkViewManager cyNetViewMgr;
    private CyNetworkViewFactory cyNetworkViewFactory;
    private CyLayoutAlgorithmManager cyLayoutAlgorithmManager;
    private VisualMappingManager visualMappingManager;
    private CyApplicationManager cyApplicationManager;
    private DialogTaskManager dialogTaskManager;

    public static Neo4jRESTServer create(ServiceLocator serviceLocator) {
        Neo4jRESTServer neo4jRESTServer = new Neo4jRESTServer();
        neo4jRESTServer.cyApplicationManager = serviceLocator.getService(CyApplicationManager.class);
        neo4jRESTServer.cyNetworkFactory = serviceLocator.getService(CyNetworkFactory.class);
        neo4jRESTServer.cyNetViewMgr = serviceLocator.getService(CyNetworkViewManager.class);
        neo4jRESTServer.cyNetworkViewFactory = serviceLocator.getService(CyNetworkViewFactory.class);
        neo4jRESTServer.cyLayoutAlgorithmManager = serviceLocator.getService(CyLayoutAlgorithmManager.class);
        neo4jRESTServer.visualMappingManager = serviceLocator.getService(VisualMappingManager.class);
        neo4jRESTServer.cyApplicationManager = serviceLocator.getService(CyApplicationManager.class);
        neo4jRESTServer.dialogTaskManager = serviceLocator.getService(DialogTaskManager.class);
        return neo4jRESTServer;

    }


    private Neo4jRESTServer() {
        
    }

    public void connect(String instanceLocation) {

        if (isConnected()) {
            disconnect();
        }

        if (validateConnection(instanceLocation)) {
            setInstanceLocation(instanceLocation);
        }
        isConnected();
    }


    public void disconnect() {
        instanceLocation = null;
//        FIXME: move to disconnect call
//        unregisterExtensions();
    }

//    private void unregisterExtensions() {
//        getPlugin().unregisterActions();
//    }

    public boolean isConnected() {
        return validateConnection(getInstanceLocation());
    }

    public String getInstanceLocation() {
        return instanceLocation;
    }

    protected void setInstanceLocation(String instanceLocation) {
        this.instanceLocation = instanceLocation;
    }

    public void syncDown(boolean mergeInCurrent) {

        TaskIterator it = new TaskIterator( new SyncDownTask(
                mergeInCurrent,
                getCypherURL(),
                getInstanceLocation(),
                getCyNetworkFactory(),
                getCyNetworkManager(),
                getCyNetViewMgr(),
                getCyNetworkViewFactory(),
                getCyLayoutAlgorithmManager(),
                getVisualMappingManager()
        ));

        getDialogTaskManager().execute(it);

    }
    
    public void syncUp(boolean b, CyNetwork currentNetwork) {
        TaskIterator it = new TaskIterator( new SyncUpTask(b, getCypherURL(), currentNetwork));
        getDialogTaskManager().execute(it);
    }


    String getCypherURL() {
        return instanceLocation + CYPHER_URL;
    }

    protected void setupAsync() {
        threadpool = Executors.newFixedThreadPool(2);
        async = Async.newInstance().use(threadpool);
    }

    public Object executeExtensionCall(Neo4jCall call, boolean doAsync) {
        Object retVal = null;

        if (doAsync) {
            setupAsync();

            String url = call.getUrlFragment();
            Request req = Request.Post(url).bodyString(call.getPayload(), APPLICATION_JSON);

            async.execute(req);
        } else {
            try {
                String url = call.getUrlFragment();
                retVal = Request.Post(url).bodyString(call.getPayload(), APPLICATION_JSON).execute().handleResponse(new PassThroughResponseHandler());

            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public CyNetworkManager getCyNetworkManager() {
        return cyNetworkManager;
    }

    public CyNetworkFactory getCyNetworkFactory() {
        return cyNetworkFactory;
    }

    public CyNetworkViewManager getCyNetViewMgr() {
        return cyNetViewMgr;
    }

    public CyNetworkViewFactory getCyNetworkViewFactory() {
        return cyNetworkViewFactory;
    }

    public CyLayoutAlgorithmManager getCyLayoutAlgorithmManager() {
        return cyLayoutAlgorithmManager;
    }


    public VisualMappingManager getVisualMappingManager() {
        return visualMappingManager;
    }
    
    public CyApplicationManager getCyApplicationManager() {
        return cyApplicationManager;
    }

    public DialogTaskManager getDialogTaskManager() {
        return dialogTaskManager;
    }
}
