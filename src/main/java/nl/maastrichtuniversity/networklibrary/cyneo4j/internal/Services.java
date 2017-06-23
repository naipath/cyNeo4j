package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.rest.Neo4jRESTClient;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.swing.DialogTaskManager;

public class Services {

    private CySwingApplication cySwingApplication;
    private CyApplicationManager cyApplicationManager;
    private CyNetworkFactory cyNetworkFactory;
    private CyNetworkManager cyNetworkManager;
    private CyNetworkViewManager cyNetworkViewManager;
    private DialogTaskManager dialogTaskManager;
    private CyNetworkViewFactory cyNetworkViewFactory;
    private CyLayoutAlgorithmManager cyLayoutAlgorithmManager;
    private VisualMappingManager visualMappingManager;
    private Neo4jRESTClient neo4jClient;
    private CommandFactory commandFactory;
    private CommandRunner commandRunner;

    public CySwingApplication getCySwingApplication() {
        return cySwingApplication;
    }

    public void setCySwingApplication(CySwingApplication cySwingApplication) {
        this.cySwingApplication = cySwingApplication;
    }

    public CyApplicationManager getCyApplicationManager() {
        return cyApplicationManager;
    }

    public void setCyApplicationManager(CyApplicationManager cyApplicationManager) {
        this.cyApplicationManager = cyApplicationManager;
    }

    public CyNetworkFactory getCyNetworkFactory() {
        return cyNetworkFactory;
    }

    public void setCyNetworkFactory(CyNetworkFactory cyNetworkFactory) {
        this.cyNetworkFactory = cyNetworkFactory;
    }

    public CyNetworkManager getCyNetworkManager() {
        return cyNetworkManager;
    }

    public void setCyNetworkManager(CyNetworkManager cyNetworkManager) {
        this.cyNetworkManager = cyNetworkManager;
    }

    public CyNetworkViewManager getCyNetworkViewManager() {
        return cyNetworkViewManager;
    }

    public void setCyNetworkViewManager(CyNetworkViewManager cyNetworkViewManager) {
        this.cyNetworkViewManager = cyNetworkViewManager;
    }

    public DialogTaskManager getDialogTaskManager() {
        return dialogTaskManager;
    }

    public void setDialogTaskManager(DialogTaskManager dialogTaskManager) {
        this.dialogTaskManager = dialogTaskManager;
    }

    public CyNetworkViewFactory getCyNetworkViewFactory() {
        return cyNetworkViewFactory;
    }

    public void setCyNetworkViewFactory(CyNetworkViewFactory cyNetworkViewFactory) {
        this.cyNetworkViewFactory = cyNetworkViewFactory;
    }

    public CyLayoutAlgorithmManager getCyLayoutAlgorithmManager() {
        return cyLayoutAlgorithmManager;
    }

    public void setCyLayoutAlgorithmManager(CyLayoutAlgorithmManager cyLayoutAlgorithmManager) {
        this.cyLayoutAlgorithmManager = cyLayoutAlgorithmManager;
    }

    public VisualMappingManager getVisualMappingManager() {
        return visualMappingManager;
    }

    public void setVisualMappingManager(VisualMappingManager visualMappingManager) {
        this.visualMappingManager = visualMappingManager;
    }

    public void setNeo4jClient(Neo4jRESTClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public Neo4jRESTClient getNeo4jClient() {
        return neo4jClient;
    }

    public void setCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    public void setCommandRunner(CommandRunner commandRunner) {
        this.commandRunner = commandRunner;
    }

    public CommandRunner getCommandRunner() {
        return commandRunner;
    }
}
