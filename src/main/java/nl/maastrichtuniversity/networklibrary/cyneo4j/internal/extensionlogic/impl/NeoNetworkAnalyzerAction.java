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

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class NeoNetworkAnalyzerAction extends AbstractCyAction {
    private final static String MENU_TITLE = "NeoNetworkAnalyzer";
    private final static String MENU_LOC = "Apps.cyNeo4j.Statistics";

    private final Neo4jRESTServer neo4jRESTServer;
    private final Neo4JExtensions neo4JExtensions;
    private final CyApplicationManager cyApplicationManager;
    private final CySwingApplication cySwingApplication;

    public static NeoNetworkAnalyzerAction create(ServiceLocator serviceLocator) {
        return new NeoNetworkAnalyzerAction(
                serviceLocator.getService(CyApplicationManager.class),
                serviceLocator.getService(CySwingApplication.class),
                serviceLocator.getService(Neo4jRESTServer.class),
                serviceLocator.getService(Neo4JExtensions.class)
                );
    }


    private NeoNetworkAnalyzerAction(CyApplicationManager cyApplicationManager, CySwingApplication cySwingApplication, Neo4jRESTServer neo4jRESTServer, Neo4JExtensions neo4JExtensions) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        this.neo4jRESTServer = neo4jRESTServer;
        this.neo4JExtensions = neo4JExtensions;
        this.cyApplicationManager = cyApplicationManager;
        this.cySwingApplication = cySwingApplication;
        setPreferredMenu(MENU_LOC);
        setEnabled(false);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Neo4jExtension neoAnalyzer = neo4JExtensions.supportsExtension("neonetworkanalyzer");

        ExtensionExecutor exec = new NeoNetworkAnalyzerExec(neoAnalyzer, cyApplicationManager, cySwingApplication);

        if (!exec.collectParameters()) {
            JOptionPane.showMessageDialog(cySwingApplication.getJFrame(), "Failed to collect parameters for " + neoAnalyzer.getName());
            return;
        }

        List<Neo4jCall> calls = exec.buildExtensionCalls();
        long time = System.currentTimeMillis();
        System.out.println("start time cyNeo4j: " + time);
        for (Neo4jCall call : calls) {
            Object callRetValue = neo4jRESTServer.executeExtensionCall(call, call.isAsync());
            exec.processCallResponse(call, callRetValue);
        }

        time = System.currentTimeMillis() - time;
        System.out.println("runtime time cyNeo4j: " + time);
    }

}
