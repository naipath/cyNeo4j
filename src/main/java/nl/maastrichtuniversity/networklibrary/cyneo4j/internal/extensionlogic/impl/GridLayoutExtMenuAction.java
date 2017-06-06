package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4JExtensions;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GridLayoutExtMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Grid Layout";
    private final static String MENU_LOC = "Apps.cyNeo4j.Layouts";

    private final Neo4jRESTServer neo4jRESTServer;
    private final Neo4JExtensions neo4JExtensions;
    private final CyApplicationManager cyApplicationManager;
    private final CyNetworkViewManager cyNetViewMgr;
    private final VisualMappingManager visualMappingManager;
    private final CySwingApplication cySwingApplication;

    public static GridLayoutExtMenuAction create(ServiceLocator serviceLocator) {
        return new GridLayoutExtMenuAction(
                serviceLocator.getService(CyApplicationManager.class),
                serviceLocator.getService(Neo4jRESTServer.class),
                serviceLocator.getService(Neo4JExtensions.class),
                serviceLocator.getService(CyNetworkViewManager.class),
                serviceLocator.getService(VisualMappingManager.class),
                serviceLocator.getService(CySwingApplication.class)
        );
    }

    private GridLayoutExtMenuAction(
            CyApplicationManager cyApplicationManager,
            Neo4jRESTServer neo4jRESTServer,
            Neo4JExtensions neo4JExtensions,
            CyNetworkViewManager cyNetViewMgr,
            VisualMappingManager visualMappingManager,
            CySwingApplication cySwingApplication
    ) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        this.cyApplicationManager = cyApplicationManager;
        this.neo4jRESTServer = neo4jRESTServer;
        this.neo4JExtensions = neo4JExtensions;
        this.cyNetViewMgr = cyNetViewMgr;
        this.visualMappingManager = visualMappingManager;
        this.cySwingApplication = cySwingApplication;
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Neo4jExtension gridLayoutExt = neo4JExtensions.supportsExtension("gridlayout");

        ExtensionExecutor exec = new SimpleLayoutExtExec(cyApplicationManager, cyNetViewMgr, visualMappingManager,  gridLayoutExt);

        if (!exec.collectParameters()) {
            JOptionPane.showMessageDialog(cySwingApplication.getJFrame(), "Failed to collect parameters for " + gridLayoutExt.getName());
            return;
        }

        List<Neo4jCall> calls = exec.buildExtensionCalls();

        for (Neo4jCall call : calls) {
            Object callRetValue = neo4jRESTServer.executeExtensionCall(call, false);
            exec.processCallResponse(call, callRetValue);
        }
    }
}