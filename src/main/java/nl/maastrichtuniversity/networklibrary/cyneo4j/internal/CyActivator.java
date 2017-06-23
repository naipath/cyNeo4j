package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.connect.ConnectInstanceMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.CypherMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.rest.Neo4jRESTClient;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.retrievedata.RetrieveDataMenuAction;
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

        ServiceLocator serviceLocator = new ServiceLocator();

        serviceLocator.setCySwingApplication(getService(context, CySwingApplication.class));
        serviceLocator.setCyApplicationManager(getService(context, CyApplicationManager.class));
        serviceLocator.setCyNetworkFactory(getService(context, CyNetworkFactory.class));
        serviceLocator.setCyNetworkManager(getService(context, CyNetworkManager.class));
        serviceLocator.setCyNetworkViewManager(getService(context, CyNetworkViewManager.class));
        serviceLocator.setDialogTaskManager(getService(context, DialogTaskManager.class));
        serviceLocator.setCyNetworkViewFactory(getService(context, CyNetworkViewFactory.class));
        serviceLocator.setCyLayoutAlgorithmManager(getService(context, CyLayoutAlgorithmManager.class));
        serviceLocator.setVisualMappingManager(getService(context, VisualMappingManager.class));

        Neo4jRESTClient neo4JRESTClient = Neo4jRESTClient.create();
        serviceLocator.setNeo4jClient(neo4JRESTClient);

        CommandFactory commandFactory = CommandFactory.create(serviceLocator);
        serviceLocator.setCommandFactory(commandFactory);

        CommandRunner commandRunner = CommandRunner.create(serviceLocator);
        serviceLocator.setCommandRunner(commandRunner);

        CypherMenuAction cypherMenuAction = CypherMenuAction.create(serviceLocator);

        ConnectInstanceMenuAction connectAction = ConnectInstanceMenuAction.create(serviceLocator);
        RetrieveDataMenuAction syncDownAction = RetrieveDataMenuAction.create(serviceLocator);

        registerAllServices(context, connectAction, new Properties());
        registerAllServices(context, syncDownAction, new Properties());

        serviceLocator.getCySwingApplication().addAction(cypherMenuAction);
    }

    @Override
    public void shutDown() {
//        cySwingApplication.removeAction(cypherMenuAction);
    }

}
