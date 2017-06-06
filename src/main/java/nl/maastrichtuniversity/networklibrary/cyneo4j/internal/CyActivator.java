package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.*;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.generallogic.ConnectInstanceMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4JExtensionRegister;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4JExtensions;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncDownMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncUpMenuAction;
import org.cytoscape.application.CyApplicationManager;
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

import java.util.Properties;

public class CyActivator extends AbstractCyActivator {


    private ActionRegister actionRegister;

    @Override
    public void start(BundleContext context) throws Exception {

        ServiceLocator serviceLocator = new ServiceLocator();

        CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);
        CySwingApplication cySwingApplication = getService(context, CySwingApplication.class);

        serviceLocator.register(getService(context, CyApplicationManager.class));
        serviceLocator.register(getService(context, CySwingApplication.class));
        serviceLocator.register(getService(context, CyNetworkFactory.class));
        serviceLocator.register(getService(context, CyNetworkManager.class));
        serviceLocator.register(getService(context, CyNetworkViewManager.class));
        serviceLocator.register(getService(context, DialogTaskManager.class));
        serviceLocator.register(getService(context, CyNetworkViewFactory.class));
        serviceLocator.register(getService(context, CyLayoutAlgorithmManager.class));
        serviceLocator.register(getService(context, VisualMappingManager.class));

        actionRegister = ActionRegister.create(serviceLocator);
        Neo4jRESTServer neo4jRESTServer = Neo4jRESTServer.create(serviceLocator);

        Neo4JExtensions neo4JExtensions = new Neo4JExtensions(neo4jRESTServer);
        Neo4JExtensionRegister neo4JExtensionRegister = new Neo4JExtensionRegister(neo4JExtensions, actionRegister);

        serviceLocator.register(neo4JExtensions);
        serviceLocator.register(neo4jRESTServer);

        neo4JExtensionRegister.put("neonetworkanalyzer", NeoNetworkAnalyzerAction.create(serviceLocator));
        neo4JExtensionRegister.put("forceatlas2", ForceAtlas2LayoutExtMenuAction.create(serviceLocator));
        neo4JExtensionRegister.put("circlelayout", CircularLayoutExtMenuAction.create(serviceLocator));
        neo4JExtensionRegister.put("gridlayout", GridLayoutExtMenuAction.create(serviceLocator));
        neo4JExtensionRegister.put("cypher", CypherMenuAction.create(serviceLocator));


        ConnectInstanceMenuAction connectAction = ConnectInstanceMenuAction.create(serviceLocator);
        SyncUpMenuAction syncUpAction = SyncUpMenuAction.create(serviceLocator);
        SyncDownMenuAction syncDownAction = SyncDownMenuAction.create(serviceLocator);

        registerAllServices(context, connectAction, new Properties());
        registerAllServices(context, syncUpAction, new Properties());
        registerAllServices(context, syncDownAction, new Properties());
        

    }

    @Override
    public void shutDown() {
        if(actionRegister != null) {
            actionRegister.cleanUp();
        }
    }


}
