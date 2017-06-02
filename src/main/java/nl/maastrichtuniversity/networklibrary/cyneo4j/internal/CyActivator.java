package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.*;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.generallogic.ConnectInstanceMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4JExtensions;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4JExtensionRegister;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncDownMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncUpMenuAction;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

import java.util.*;

public class CyActivator extends AbstractCyActivator {

    protected Plugin plugin;

    @Override
    public void start(BundleContext context) throws Exception {

        CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);
        CySwingApplication cySwingApplication = getService(context, CySwingApplication.class);
        CyNetworkFactory cyNetworkFactory = getService(context, CyNetworkFactory.class);
        CyNetworkManager cyNetMgr = getService(context, CyNetworkManager.class);
        CyNetworkViewManager cyNetViewMgr = getService(context, CyNetworkViewManager.class);
        DialogTaskManager diagTaskManager = getService(context, DialogTaskManager.class);
        CyNetworkViewFactory cyNetworkViewFactory = getService(context, CyNetworkViewFactory.class);
        CyLayoutAlgorithmManager cyLayoutAlgorithmMgr = getService(context, CyLayoutAlgorithmManager.class);
        VisualMappingManager visualMappingMgr = getService(context, VisualMappingManager.class);


        Plugin plugin = new Plugin(cyApplicationManager, cySwingApplication, cyNetworkFactory, cyNetMgr, cyNetViewMgr, diagTaskManager, cyNetworkViewFactory, cyLayoutAlgorithmMgr, visualMappingMgr);
        Neo4jRESTServer neo4jRESTServer = Plugin.create(cyApplicationManager, cySwingApplication, cyNetworkFactory, cyNetMgr, cyNetViewMgr, diagTaskManager, cyNetworkViewFactory, cyLayoutAlgorithmMgr, visualMappingMgr);

        Neo4JExtensions neo4JExtensions = new Neo4JExtensions(neo4jRESTServer);
        Neo4JExtensionRegister neo4JExtensionRegister = new Neo4JExtensionRegister(neo4JExtensions, plugin);

        neo4JExtensionRegister.put("neonetworkanalyzer", new NeoNetworkAnalyzerAction(cyApplicationManager, plugin, neo4jRESTServer, neo4JExtensions));
        neo4JExtensionRegister.put("forceatlas2", new ForceAtlas2LayoutExtMenuAction(cyApplicationManager, plugin, neo4jRESTServer, neo4JExtensions));
        neo4JExtensionRegister.put("circlelayout", new CircularLayoutExtMenuAction(cyApplicationManager, plugin, neo4jRESTServer, neo4JExtensions));
        neo4JExtensionRegister.put("gridlayout", new GridLayoutExtMenuAction(cyApplicationManager, plugin, neo4jRESTServer, neo4JExtensions));
        neo4JExtensionRegister.put("cypher", new CypherMenuAction(cyApplicationManager, plugin, neo4jRESTServer, neo4JExtensions));


        ConnectInstanceMenuAction connectAction = new ConnectInstanceMenuAction(cyApplicationManager, cySwingApplication, neo4jRESTServer, neo4JExtensionRegister);
        SyncUpMenuAction syncUpAction = new SyncUpMenuAction(cyApplicationManager, neo4jRESTServer);
        SyncDownMenuAction syncDownAction = new SyncDownMenuAction(cyApplicationManager, neo4jRESTServer);

        registerAllServices(context, connectAction, new Properties());
        registerAllServices(context, syncUpAction, new Properties());
        registerAllServices(context, syncDownAction, new Properties());

    }

    @Override
    public void shutDown() {
        plugin.cleanUp();
    }


}
