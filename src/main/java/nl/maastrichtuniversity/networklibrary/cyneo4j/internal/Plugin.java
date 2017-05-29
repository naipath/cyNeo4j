package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.*;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.swing.DialogTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plugin {


    private CyApplicationManager cyApplicationManager = null;
    private List<AbstractCyAction> registeredActions = null;
    private CySwingApplication cySwingApplication = null;
    private CyNetworkFactory cyNetworkFactory = null;
    private CyNetworkManager cyNetMgr = null;
    private CyNetworkViewManager cyNetViewMgr = null;
    private DialogTaskManager diagTaskManager = null;
    private CyNetworkViewFactory cyNetworkViewFactory = null;
    private CyLayoutAlgorithmManager cyLayoutAlgorithmMgr = null;
    private VisualMappingManager visualMappingMgr = null;


    public static Neo4jRESTServer create(CyApplicationManager cyApplicationManager,
                                         CySwingApplication cySwingApplication,
                                         CyNetworkFactory cyNetworkFactory,
                                         CyNetworkManager cyNetMgr, CyNetworkViewManager cyNetViewMgr,
                                         DialogTaskManager diagTaskManager,
                                         CyNetworkViewFactory cyNetworkViewFactory,
                                         CyLayoutAlgorithmManager cyLayoutAlgorithmMgr,
                                         VisualMappingManager visualMappingMgr) {
		/*
		 * This should eventually be replaced by a more modular system. Each of the extensions
		 * is its own Cytoscape app and this app just serves as a entry point for them?
		 */

		/*
		 * DEV ENTRY POINT
		 * Link a name of a plugin on the server side with an action in the app!
		 * The linked action will be displayed in the cyNeo4j menu item if the plugin is available on the server
		 */
        Plugin plugin = new Plugin(cyApplicationManager, cySwingApplication, cyNetworkFactory, cyNetMgr, cyNetViewMgr, diagTaskManager, cyNetworkViewFactory, cyLayoutAlgorithmMgr, visualMappingMgr);
        
        Neo4jRESTServer interactor = new Neo4jRESTServer(plugin);
        return interactor;
    }



    public Plugin(CyApplicationManager cyApplicationManager,
                  CySwingApplication cySwingApplication,
                  CyNetworkFactory cyNetworkFactory,
                  CyNetworkManager cyNetMgr, CyNetworkViewManager cyNetViewMgr,
                  DialogTaskManager diagTaskManager,
                  CyNetworkViewFactory cyNetworkViewFactory,
                  CyLayoutAlgorithmManager cyLayoutAlgorithmMgr,
                  VisualMappingManager visualMappingMgr) {
		/*
		 * This should eventually be replaced by a more modular system. Each of the extensions
		 * is its own Cytoscape app and this app just serves as a entry point for them?
		 */
		
		/*
		 * DEV ENTRY POINT 
		 * Link a name of a plugin on the server side with an action in the app!
		 * The linked action will be displayed in the cyNeo4j menu item if the plugin is available on the server
		 */

        this.cyApplicationManager = cyApplicationManager;
        this.cySwingApplication = cySwingApplication;
        this.cyNetworkFactory = cyNetworkFactory;
        this.cyNetMgr = cyNetMgr;
        this.cyNetViewMgr = cyNetViewMgr;
        this.diagTaskManager = diagTaskManager;
        this.cyNetworkViewFactory = cyNetworkViewFactory;
        this.cyLayoutAlgorithmMgr = cyLayoutAlgorithmMgr;
        this.visualMappingMgr = visualMappingMgr;
        registeredActions = new ArrayList<>();
    }

    public CyNetworkFactory getCyNetworkFactory() {
        return cyNetworkFactory;
    }

    public CyNetworkManager getCyNetworkManager() {
        return cyNetMgr;
    }

    public CyNetworkViewManager getCyNetViewMgr() {
        return cyNetViewMgr;
    }

    public CyApplicationManager getCyApplicationManager() {
        return cyApplicationManager;
    }

    public CySwingApplication getCySwingApplication() {
        return cySwingApplication;
    }
    
    public DialogTaskManager getDialogTaskManager() {
        return diagTaskManager;
    }

    public CyNetworkViewFactory getCyNetworkViewFactory() {
        return cyNetworkViewFactory;
    }

    public CyLayoutAlgorithmManager getCyLayoutAlgorithmManager() {
        return cyLayoutAlgorithmMgr;
    }

    public VisualMappingManager getVisualMappingManager() {
        return visualMappingMgr;
    }

    void cleanUp() {
        //extension actions
        unregisterActions();
    }

    public void registerAction(AbstractCyAction action) {
        registeredActions.add(action);
        getCySwingApplication().addAction(action);
    }

    public void unregisterActions() {
        registeredActions.forEach(cyAction -> getCySwingApplication().removeAction(cyAction));
    }
}
