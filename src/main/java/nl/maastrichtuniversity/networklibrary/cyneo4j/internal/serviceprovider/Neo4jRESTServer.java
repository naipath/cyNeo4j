package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtParam;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.ExtensionLocationsHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.ExtensionParametersResponseHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.PassThroughResponseHandler;
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
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Collections.singletonList;

public class Neo4jRESTServer {

    private static final String DATA_URL = "/db/data/";
    private static final String CYPHER_URL = DATA_URL + "cypher";
    private static final String EXT_URL = DATA_URL + "ext/";

    protected String instanceLocation = null;

    private Plugin plugin;
    private Map<String, AbstractCyAction> localExtensions;

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
            registerExtension();
        }
        isConnected();
    }

    protected void registerExtension() {
        for (Extension ext : getExtensions()) {
            getPlugin().registerAction(localExtensions.get(ext.getName()));
        }
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

    public List<Extension> getExtensions() {
        List<Extension> res = new ArrayList<>();

        Extension cypherExt = new Neo4jExtension();
        cypherExt.setName("cypher");
        cypherExt.setEndpoint(getCypherURL());

        cypherExt.setParameters(singletonList(new Neo4jExtParam("cypherQuery", "Cypher Endpoint", false, String.class)));

        if (localExtensions.containsKey("cypher")) {
            res.add(cypherExt);
        }
        try {
            Set<String> extNames = Request.Get(getInstanceLocation() + EXT_URL).execute().handleResponse(new ExtensionLocationsHandler());

            for (String extName : extNames) {
                List<Extension> serverSupportedExt = Request.Get(getInstanceLocation() + EXT_URL + extName).execute().handleResponse(new ExtensionParametersResponseHandler(getInstanceLocation() + EXT_URL + extName));

                for (Extension ext : serverSupportedExt) {
                    if (localExtensions.containsKey(ext.getName())) {
                        res.add(ext);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return res;
    }

    public void syncUp(boolean wipeRemote, CyNetwork curr) {
        TaskIterator it = new SyncUpTaskFactory(wipeRemote, getCypherURL(), getPlugin().getCyApplicationManager().getCurrentNetwork()).createTaskIterator();
        plugin.getDialogTaskManager().execute(it);

    }

    private String getCypherURL() {
        return getInstanceLocation() + CYPHER_URL;
    }

    protected void setupAsync() {
        threadpool = Executors.newFixedThreadPool(2);
        async = Async.newInstance().use(threadpool);
    }

    public Object executeExtensionCall(ExtensionCall call, boolean doAsync) {
        Object retVal = null;

        if (doAsync) {
            setupAsync();

            String url = call.getUrlFragment();
            Request req = Request.Post(url).bodyString(call.getPayload(), ContentType.APPLICATION_JSON);

            async.execute(req);
        } else {


            try {
                String url = call.getUrlFragment();
                retVal = Request.Post(url).bodyString(call.getPayload(), ContentType.APPLICATION_JSON).execute().handleResponse(new PassThroughResponseHandler());

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

    public Extension supportsExtension(String name) {
        List<Extension> extensions = getExtensions();

        for (Extension extension : extensions) {
            if (extension.getName().equals(name)) {
                return extension;
            }
        }
        return null;
    }

    public void setLocalSupportedExtension(Map<String, AbstractCyAction> localExtensions) {
        this.localExtensions = localExtensions;
    }
}
