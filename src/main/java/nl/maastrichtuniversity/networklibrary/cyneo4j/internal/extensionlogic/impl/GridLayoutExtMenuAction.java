package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.LocalExtensions;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
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
    private final LocalExtensions localExtensions;

    public GridLayoutExtMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin, Neo4jRESTServer neo4jRESTServer, LocalExtensions localExtensions) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        this.neo4jRESTServer = neo4jRESTServer;
        this.localExtensions = localExtensions;
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        this.plugin = plugin;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Extension gridLayoutExt = localExtensions.supportsExtension("gridlayout");

        ExtensionExecutor exec = new SimpleLayoutExtExec();

        exec.setPlugin(plugin);
        exec.setExtension(gridLayoutExt);

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

    protected Plugin getPlugin() {
        return plugin;
    }
}