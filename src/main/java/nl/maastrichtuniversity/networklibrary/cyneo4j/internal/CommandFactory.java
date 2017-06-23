package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.retrievedata.RetrieveDataTask;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;

public class CommandFactory {

    private final Neo4jClient neo4jClient;
    private final String instanceLocation;
    private final CyNetworkFactory cyNetworkFactory;
    private final CyNetworkManager cyNetworkMgr;
    private final CyNetworkViewManager cyNetworkViewMgr;
    private final CyNetworkViewFactory cyNetworkViewFactory;
    private final CyLayoutAlgorithmManager cyLayoutAlgorithmMgr;
    private final VisualMappingManager visualMappingMgr;


    private CommandFactory(Neo4jClient neo4jClient, String instanceLocation, CyNetworkFactory cyNetworkFactory, CyNetworkManager cyNetworkMgr, CyNetworkViewManager cyNetworkViewMgr, CyNetworkViewFactory cyNetworkViewFactory, CyLayoutAlgorithmManager cyLayoutAlgorithmMgr, VisualMappingManager visualMappingMgr) {
        this.neo4jClient = neo4jClient;
        this.instanceLocation = instanceLocation;
        this.cyNetworkFactory = cyNetworkFactory;
        this.cyNetworkMgr = cyNetworkMgr;
        this.cyNetworkViewMgr = cyNetworkViewMgr;
        this.cyNetworkViewFactory = cyNetworkViewFactory;
        this.cyLayoutAlgorithmMgr = cyLayoutAlgorithmMgr;
        this.visualMappingMgr = visualMappingMgr;
    }

    public RetrieveDataTask createRetrieveDataTask() {
        return new RetrieveDataTask(instanceLocation, neo4jClient, cyNetworkFactory, cyNetworkMgr, cyNetworkViewMgr, cyNetworkViewFactory, cyLayoutAlgorithmMgr, visualMappingMgr);
    }

    public static CommandFactory create(ServiceLocator serviceLocator) {
        return new CommandFactory(
                serviceLocator.getService(Neo4jClient.class),
                null,//TODO: instance location
                serviceLocator.getService(CyNetworkFactory.class),
                serviceLocator.getService(CyNetworkManager.class),
                serviceLocator.getService(CyNetworkViewManager.class),
                serviceLocator.getService(CyNetworkViewFactory.class),
                serviceLocator.getService(CyLayoutAlgorithmManager.class),
                serviceLocator.getService(VisualMappingManager.class)
        );
    }
}
