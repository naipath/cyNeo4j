package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualMappingManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SimpleLayoutExtExec implements ExtensionExecutor {

    private final Neo4jExtension extension;
    private final CyApplicationManager cyApplicationManager;
    private final CyNetworkViewManager cyNetViewMgr;
    private final VisualMappingManager visualMappingManager;
    private CyNetwork currNet;

    SimpleLayoutExtExec(CyApplicationManager cyApplicationManager, CyNetworkViewManager cyNetViewMgr, VisualMappingManager visualMappingManager, Neo4jExtension extension) {
        this.cyApplicationManager = cyApplicationManager;
        this.cyNetViewMgr = cyNetViewMgr;
        this.visualMappingManager = visualMappingManager;
        this.extension = extension;
    }

    @Override
    public boolean collectParameters() {
        currNet = cyApplicationManager.getCurrentNetwork();
        return true;
    }

    @Override
    public void processCallResponse(Neo4jCall call, Object callRetValue) {

        List<Double> values = (List<Double>) callRetValue;

        CyTable defNodeTab = currNet.getDefaultNodeTable();
        CyNetworkView networkView = cyNetViewMgr.getNetworkViews(currNet).iterator().next();

        for (int i = 0; i < (values.size() / 3); ++i) {
            Long neoid = values.get(i * 3).longValue();
            Double x = values.get(i * 3 + 1);
            Double y = values.get(i * 3 + 2);


            Set<CyNode> nodeSet = CyUtils.getNodesWithValue(currNet, defNodeTab, "neoid", neoid);
            CyNode n = nodeSet.iterator().next();

            View<CyNode> nodeView = networkView.getNodeView(n);
            nodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, x);
            nodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, y);

            CyUtils.updateVisualStyle(visualMappingManager, networkView);
        }
    }

    @Override
    public List<Neo4jCall> buildExtensionCalls() {
        List<Neo4jCall> calls = new ArrayList<>();

        String urlFragment = extension.getEndpoint();
        String payload = "{\"saveInGraph\":false}";

        calls.add(new Neo4jCall(urlFragment, payload, false));

        return calls;
    }
}
