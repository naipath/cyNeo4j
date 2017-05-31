package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4JExtensions;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GridLayoutExtMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Grid Layout";
    private final static String MENU_LOC = "Apps.cyNeo4j.Layouts";

    private Plugin plugin;
    private final Neo4jRESTServer neo4jRESTServer;
    private final Neo4JExtensions neo4JExtensions;

    public GridLayoutExtMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin, Neo4jRESTServer neo4jRESTServer, Neo4JExtensions neo4JExtensions) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        this.neo4jRESTServer = neo4jRESTServer;
        this.neo4JExtensions = neo4JExtensions;
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        this.plugin = plugin;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Neo4jExtension gridLayoutExt = neo4JExtensions.supportsExtension("gridlayout");

        ExtensionExecutor exec = new SimpleLayoutExtExec(plugin, gridLayoutExt);

        if (!exec.collectParameters()) {
            JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Failed to collect parameters for " + gridLayoutExt.getName());
            return;
        }

        List<Neo4jCall> calls = exec.buildExtensionCalls();

        for (Neo4jCall call : calls) {
            Object callRetValue = neo4jRESTServer.executeExtensionCall(call, false);
            exec.processCallResponse(call, callRetValue);
        }
    }
}