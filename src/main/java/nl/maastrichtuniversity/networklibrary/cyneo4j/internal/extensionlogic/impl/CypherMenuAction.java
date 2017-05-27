package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CypherMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Cypher Query";
    private final static String MENU_LOC = "Apps.cyNeo4j";

    private Plugin plugin;

    public CypherMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        setMenuGravity(0.5f);
        this.plugin = plugin;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Neo4jExtension cypherExt = plugin.getInteractor().supportsExtension("cypher");

        ExtensionExecutor exec = new CypherExtExec(plugin, cypherExt);

        if (!exec.collectParameters()) {
            JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Failed to collect parameters for " + cypherExt.getName());
            return;
        }

        List<Neo4jCall> calls = exec.buildExtensionCalls();

        for (Neo4jCall call : calls) {
            Object callRetValue = plugin.getInteractor().executeExtensionCall(call, false);
            exec.processCallResponse(call, callRetValue);
        }
    }
}
