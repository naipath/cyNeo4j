package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ServiceLocator;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4JExtensions;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.swing.DialogTaskManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ForceAtlas2LayoutExtMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Force Atlas2 Layout";
    private final static String MENU_LOC = "Apps.cyNeo4j.Layouts";

    private final CyApplicationManager cyApplicationManager;
    private final CySwingApplication cySwingApplication;
    private final CyNetworkViewManager cyNetViewMgr;
    private final VisualMappingManager visualMappingManager;
    private final DialogTaskManager dialogTaskManager;
    private final Neo4jRESTServer neo4jRESTServer;
    private final Neo4JExtensions neo4JExtensions;

    public static ForceAtlas2LayoutExtMenuAction create(ServiceLocator serviceLocator) {
        return new ForceAtlas2LayoutExtMenuAction(
                serviceLocator.getService(CyApplicationManager.class),
                serviceLocator.getService(CySwingApplication.class),
                serviceLocator.getService(CyNetworkViewManager.class),
                serviceLocator.getService(VisualMappingManager.class),
                serviceLocator.getService(DialogTaskManager.class),
                serviceLocator.getService(Neo4jRESTServer.class),
                serviceLocator.getService(Neo4JExtensions.class)
        );
    }

    private ForceAtlas2LayoutExtMenuAction(
            CyApplicationManager cyApplicationManager,
            CySwingApplication cySwingApplication,
            CyNetworkViewManager cyNetViewMgr,
            VisualMappingManager visualMappingManager,
            DialogTaskManager dialogTaskManager,
            Neo4jRESTServer neo4jRESTServer,
            Neo4JExtensions neo4JExtensions
    ) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        this.neo4jRESTServer = neo4jRESTServer;
        this.neo4JExtensions = neo4JExtensions;
        this.cySwingApplication = cySwingApplication;
        this.cyNetViewMgr = cyNetViewMgr;
        this.visualMappingManager = visualMappingManager;
        this.cyApplicationManager = cyApplicationManager;
        this.dialogTaskManager = dialogTaskManager;
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Neo4jExtension forceAtlas2LayoutExt = neo4JExtensions.supportsExtension("forceatlas2");

        ForceAtlas2LayoutExtExec exec = new ForceAtlas2LayoutExtExec(cyApplicationManager, cySwingApplication, cyNetViewMgr, visualMappingManager);
        exec.setExtension(forceAtlas2LayoutExt);

        ForceAtlas2ExecutionTaskFactory factory = new ForceAtlas2ExecutionTaskFactory(exec);
        do {
            if (!exec.collectParameters()) {
                JOptionPane.showMessageDialog(cySwingApplication.getJFrame(), "Failed to collect parameters for " + forceAtlas2LayoutExt.getName());
                return;
            }

            TaskIterator it = factory.createTaskIterator();
            dialogTaskManager.execute(it);

        } while (exec.doContinue());
    }

    protected class ForceAtlas2ExecutionTask extends AbstractTask {

        ForceAtlas2LayoutExtExec exec;

        ForceAtlas2ExecutionTask(ForceAtlas2LayoutExtExec exec) {
            this.exec = exec;
        }

        @Override
        public void run(TaskMonitor monitor) throws Exception {
            monitor.setStatusMessage("Executing ForceAtlas2 layout");

            List<Neo4jCall> calls = exec.buildExtensionCalls();

            double progress = 0.0;
            for (Neo4jCall call : calls) {
                Object callRetValue = neo4jRESTServer.executeExtensionCall(call, false);
                exec.processCallResponse(callRetValue);
                ++progress;
                monitor.setProgress(progress / ((double) calls.size()));

            }

        }

    }

    protected class ForceAtlas2ExecutionTaskFactory extends AbstractTaskFactory {

        protected ForceAtlas2LayoutExtExec exec;

        public ForceAtlas2ExecutionTaskFactory(ForceAtlas2LayoutExtExec exec) {
            this.exec = exec;
        }

        @Override
        public TaskIterator createTaskIterator() {
            return new TaskIterator(new ForceAtlas2ExecutionTask(exec));
        }

    }
}