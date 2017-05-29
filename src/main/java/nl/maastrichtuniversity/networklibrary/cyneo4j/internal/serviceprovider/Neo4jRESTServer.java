package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtParam;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.ExtensionLocationsHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.ExtensionParametersResponseHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.PassThroughResponseHandlerMy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.general.Neo4jPingHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncDownTaskFactory;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncUpTaskFactory;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Collections.singletonList;

public class Neo4jRESTServer {

    private static final String DATA_URL = "/db/data/";
    private static final String CYPHER_URL = DATA_URL + "cypher";
    static final String EXT_URL = DATA_URL + "ext/";

    protected String instanceLocation = null;

    private final Plugin plugin;

    protected ExecutorService threadpool;
    protected Async async;

    public Neo4jRESTServer(Plugin plugin) {
        this.plugin = plugin;
    }

    public void connect(String instanceLocation) {

        if (isConnected()) {
            disconnect();
        }

        if (validateConnection(instanceLocation)) {
            setInstanceLocation(instanceLocation);
//            registerExtension();
        }
        isConnected();
    }
    
    public void disconnect() {
        instanceLocation = null;
        unregisterExtensions();
    }

    private void unregisterExtensions() {
        getPlugin().unregisterActions();
    }

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

        TaskIterator it = new SyncDownTaskFactory(getPlugin().getCyNetworkManager(),
            mergeInCurrent,
            getPlugin().getCyNetworkFactory(),
            getInstanceLocation(),
            getCypherURL(),
            getPlugin().getCyNetViewMgr(),
            getPlugin().getCyNetworkViewFactory(),
            getPlugin().getCyLayoutAlgorithmManager(),
            getPlugin().getVisualMappingManager()
        ).createTaskIterator();

        plugin.getDialogTaskManager().execute(it);

    }

    public void syncUp(boolean wipeRemote, CyNetwork curr) {
        TaskIterator it = new SyncUpTaskFactory(wipeRemote, getCypherURL(), getPlugin().getCyApplicationManager().getCurrentNetwork()).createTaskIterator();
        plugin.getDialogTaskManager().execute(it);

    }

    String getCypherURL() {
        return getInstanceLocation() + CYPHER_URL;
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
            Request req = Request.Post(url).bodyString(call.getPayload(), ContentType.APPLICATION_JSON);

            async.execute(req);
        } else {


            try {
                String url = call.getUrlFragment();
                retVal = Request.Post(url).bodyString(call.getPayload(), ContentType.APPLICATION_JSON).execute().handleResponse(new PassThroughResponseHandlerMy());

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
        }
        // TODO fix error messages | show exceptions? does the user understand the error messages?
        return false;
    }

    protected Plugin getPlugin() {
        return plugin;
    }

}
