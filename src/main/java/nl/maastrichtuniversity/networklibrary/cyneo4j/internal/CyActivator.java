package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.connect.ConnectInstanceMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.CypherMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.bolt.Neo4jBoltClient;
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

    Services services = new Services();
    private CypherMenuAction cypherMenuAction;

    @Override
    public void start(BundleContext context) throws Exception {

        services.setCySwingApplication(getService(context, CySwingApplication.class));
        services.setCyApplicationManager(getService(context, CyApplicationManager.class));
        services.setCyNetworkFactory(getService(context, CyNetworkFactory.class));
        services.setCyNetworkManager(getService(context, CyNetworkManager.class));
        services.setCyNetworkViewManager(getService(context, CyNetworkViewManager.class));
        services.setDialogTaskManager(getService(context, DialogTaskManager.class));
        services.setCyNetworkViewFactory(getService(context, CyNetworkViewFactory.class));
        services.setCyLayoutAlgorithmManager(getService(context, CyLayoutAlgorithmManager.class));
        services.setVisualMappingManager(getService(context, VisualMappingManager.class));
        services.setNeo4jClient(new Neo4jBoltClient());

        CommandFactory commandFactory = CommandFactory.create(services);
        services.setCommandFactory(commandFactory);

        CommandRunner commandRunner = CommandRunner.create(services);
        services.setCommandRunner(commandRunner);

        cypherMenuAction = CypherMenuAction.create(services);

        ConnectInstanceMenuAction connectAction = ConnectInstanceMenuAction.create(services);
        RetrieveDataMenuAction syncDownAction = RetrieveDataMenuAction.create(services);

        registerAllServices(context, connectAction, new Properties());
        registerAllServices(context, syncDownAction, new Properties());

        services.getCySwingApplication().addAction(cypherMenuAction);
    }

    @Override
    public void shutDown() {
        services.getCySwingApplication().removeAction(cypherMenuAction);
    }
}
