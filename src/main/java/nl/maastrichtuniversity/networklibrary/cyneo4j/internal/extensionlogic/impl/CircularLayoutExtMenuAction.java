package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4JExtensions;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CircularLayoutExtMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Circle Layout";
    private final static String MENU_LOC = "Apps.cyNeo4j.Layouts";

    private final Plugin plugin;
    private final Neo4jRESTServer neo4jRESTServer;
    private final Neo4JExtensions neo4JExtensions;

    public CircularLayoutExtMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin, Neo4jRESTServer neo4jRESTServer, Neo4JExtensions neo4JExtensions) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        this.neo4jRESTServer = neo4jRESTServer;
        this.neo4JExtensions = neo4JExtensions;
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        this.plugin = plugin;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Neo4jExtension layoutExt = neo4JExtensions.supportsExtension("circlelayout");

        ExtensionExecutor exec = new SimpleLayoutExtExec(plugin, layoutExt);

        if (!exec.collectParameters()) {
            JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Failed to collect parameters for " + layoutExt.getName());
            return;
        }

        List<Neo4jCall> calls = exec.buildExtensionCalls();

        for (Neo4jCall call : calls) {
            Object callRetValue = neo4jRESTServer.executeExtensionCall(call, false);
            exec.processCallResponse(call, callRetValue);
        }
    }
}