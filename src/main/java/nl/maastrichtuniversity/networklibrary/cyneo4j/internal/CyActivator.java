package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.CypherMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.generallogic.ConnectInstanceMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncDownMenuAction;
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

    private CypherMenuAction cypherMenuAction;
    private CySwingApplication cySwingApplication;

    @Override
    public void start(BundleContext context) throws Exception {
        cySwingApplication = getService(context, CySwingApplication.class);

        ServiceLocator serviceLocator = new ServiceLocator();
        serviceLocator.register(CySwingApplication.class, cySwingApplication);
        serviceLocator.register(CyApplicationManager.class, getService(context, CyApplicationManager.class));
        serviceLocator.register(CyNetworkFactory.class, getService(context, CyNetworkFactory.class));
        serviceLocator.register(CyNetworkManager.class, getService(context, CyNetworkManager.class));
        serviceLocator.register(CyNetworkViewManager.class, getService(context, CyNetworkViewManager.class));
        serviceLocator.register(DialogTaskManager.class, getService(context, DialogTaskManager.class));
        serviceLocator.register(CyNetworkViewFactory.class, getService(context, CyNetworkViewFactory.class));
        serviceLocator.register(CyLayoutAlgorithmManager.class, getService(context, CyLayoutAlgorithmManager.class));
        serviceLocator.register(VisualMappingManager.class, getService(context, VisualMappingManager.class));

        Neo4jRESTServer neo4jRESTServer = Neo4jRESTServer.create(serviceLocator);
        serviceLocator.register(neo4jRESTServer);

        cypherMenuAction = CypherMenuAction.create(serviceLocator);

        ConnectInstanceMenuAction connectAction = ConnectInstanceMenuAction.create(serviceLocator);
        SyncDownMenuAction syncDownAction = SyncDownMenuAction.create(serviceLocator);

        registerAllServices(context, connectAction, new Properties());
        registerAllServices(context, syncDownAction, new Properties());

        cySwingApplication.addAction(cypherMenuAction);

    }

    @Override
    public void shutDown() {
        cySwingApplication.removeAction(cypherMenuAction);
    }
}
