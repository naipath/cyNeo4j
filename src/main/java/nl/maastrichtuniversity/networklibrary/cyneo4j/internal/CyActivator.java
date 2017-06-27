package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.CommandMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.ConnectInstanceMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.CypherMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
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

public class CyActivator extends AbstractCyActivator  {


    @Override
    public void start(BundleContext context) throws Exception {
        Services services = new Services();
        services.setCySwingApplication(getService(context, CySwingApplication.class));
        services.setCyApplicationManager(getService(context, CyApplicationManager.class));
        services.setCyNetworkFactory(getService(context, CyNetworkFactory.class));
        services.setCyNetworkManager(getService(context, CyNetworkManager.class));
        services.setCyNetworkViewManager(getService(context, CyNetworkViewManager.class));
        services.setDialogTaskManager(getService(context, DialogTaskManager.class));
        services.setCyNetworkViewFactory(getService(context, CyNetworkViewFactory.class));
        services.setCyLayoutAlgorithmManager(getService(context, CyLayoutAlgorithmManager.class));
        services.setVisualMappingManager(getService(context, VisualMappingManager.class));
        services.setNeo4jClient(new Neo4jClient());
        services.setCommandFactory(CommandFactory.create(services));
        services.setCommandRunner(CommandRunner.create(services));

        CypherMenuAction cypherMenuAction = CypherMenuAction.create(services);

        ConnectInstanceMenuAction connectAction = ConnectInstanceMenuAction.create(services);
        CommandMenuAction retrieveDataMenuAction = CommandMenuAction.create("Retrieve Data",services, () -> services.getCommandFactory().createRetrieveDataTask());
        CommandMenuAction copyDataMenuAction = CommandMenuAction.create("Copy Data",services, () -> services.getCommandFactory().createCopyDataTask());

        registerAllServices(context, connectAction, new Properties());
        registerAllServices(context, retrieveDataMenuAction, new Properties());
        registerAllServices(context, copyDataMenuAction, new Properties());
        registerAllServices(context, cypherMenuAction, new Properties());

    }

}
