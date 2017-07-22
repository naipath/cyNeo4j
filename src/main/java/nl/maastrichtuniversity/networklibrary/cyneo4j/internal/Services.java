package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.configuration.AppConfiguration;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.provider.CypherQueryTemplateResourceProvider;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CySessionManager;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;
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
    private Neo4jClient neo4jClient;
    private CommandFactory commandFactory;
    private CommandRunner commandRunner;
    private CyEventHelper cyEventHelper;
    private VisualStyleFactory visualStyleFactory;
    private AppConfiguration appConfiguration;

    public CySwingApplication getCySwingApplication() {
        return cySwingApplication;
    }

    void setCySwingApplication(CySwingApplication cySwingApplication) {
        this.cySwingApplication = cySwingApplication;
    }

    public CyApplicationManager getCyApplicationManager() {
        return cyApplicationManager;
    }

    void setCyApplicationManager(CyApplicationManager cyApplicationManager) {
        this.cyApplicationManager = cyApplicationManager;
    }

    public CyNetworkFactory getCyNetworkFactory() {
        return cyNetworkFactory;
    }

    void setCyNetworkFactory(CyNetworkFactory cyNetworkFactory) {
        this.cyNetworkFactory = cyNetworkFactory;
    }

    public CyNetworkManager getCyNetworkManager() {
        return cyNetworkManager;
    }

    void setCyNetworkManager(CyNetworkManager cyNetworkManager) {
        this.cyNetworkManager = cyNetworkManager;
    }

    public CyNetworkViewManager getCyNetworkViewManager() {
        return cyNetworkViewManager;
    }

    void setCyNetworkViewManager(CyNetworkViewManager cyNetworkViewManager) {
        this.cyNetworkViewManager = cyNetworkViewManager;
    }

    DialogTaskManager getDialogTaskManager() {
        return dialogTaskManager;
    }

    void setDialogTaskManager(DialogTaskManager dialogTaskManager) {
        this.dialogTaskManager = dialogTaskManager;
    }

    public CyNetworkViewFactory getCyNetworkViewFactory() {
        return cyNetworkViewFactory;
    }

    void setCyNetworkViewFactory(CyNetworkViewFactory cyNetworkViewFactory) {
        this.cyNetworkViewFactory = cyNetworkViewFactory;
    }

    public CyLayoutAlgorithmManager getCyLayoutAlgorithmManager() {
        return cyLayoutAlgorithmManager;
    }

    void setCyLayoutAlgorithmManager(CyLayoutAlgorithmManager cyLayoutAlgorithmManager) {
        this.cyLayoutAlgorithmManager = cyLayoutAlgorithmManager;
    }

    public VisualMappingManager getVisualMappingManager() {
        return visualMappingManager;
    }

    void setVisualMappingManager(VisualMappingManager visualMappingManager) {
        this.visualMappingManager = visualMappingManager;
    }

    void setNeo4jClient(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public Neo4jClient getNeo4jClient() {
        return neo4jClient;
    }

    void setCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    void setCommandRunner(CommandRunner commandRunner) {
        this.commandRunner = commandRunner;
    }

    public CommandRunner getCommandRunner() {
        return commandRunner;
    }

    public void setCyEventHelper(CyEventHelper cyEventHelper) {
        this.cyEventHelper = cyEventHelper;
    }

    public CyEventHelper getCyEventHelper() {
        return cyEventHelper;
    }

    public VisualStyleFactory getVisualStyleFactory() {
        return visualStyleFactory;
    }

    public void setVisualStyleFactory(VisualStyleFactory visualStyleFactory) {
        this.visualStyleFactory = visualStyleFactory;
    }

    public void setAppConfiguration(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    public AppConfiguration getAppConfiguration() {
        return appConfiguration;
    }
}
