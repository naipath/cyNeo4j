package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;

public class CyUtils {
    public static Set<CyNode> getNodesWithValue(CyNetwork net, CyTable table, String colname, Object value) {
        Collection<CyRow> matchingRows = table.getMatchingRows(colname, value);
        Set<CyNode> nodes = new HashSet<>();
        String primaryKeyColname = table.getPrimaryKey().getName();
        for (CyRow row : matchingRows) {
            Long nodeId = row.get(primaryKeyColname, Long.class);
            if (nodeId == null)
                continue;
            CyNode node = net.getNode(nodeId);
            if (node == null)
                continue;
            nodes.add(node);
        }
        return nodes;
    }

    public static Set<CyEdge> getEdgeWithValue(CyNetwork net, CyTable table, String colname, Object value) {
        Collection<CyRow> matchingRows = table.getMatchingRows(colname, value);
        Set<CyEdge> edges = new HashSet<>();
        String primaryKeyColname = table.getPrimaryKey().getName();
        for (CyRow row : matchingRows) {
            Long edgeId = row.get(primaryKeyColname, Long.class);
            if (edgeId == null)
                continue;
            CyEdge edge = net.getEdge(edgeId);
            if (edge == null)
                continue;
            edges.add(edge);
        }
        return edges;
    }

    public static String convertCyAttributesToJson(CyIdentifiable item, CyTable tab) {
        StringBuilder params = new StringBuilder("{");
        for (CyColumn cyCol : tab.getColumns()) {
            String paramName = cyCol.getName();
            if (paramName.equals("neoid"))
                continue;

            Object paramValue = tab.getRow(item.getSUID()).get(cyCol.getName(), cyCol.getType());

            String paramValueStr;
            if (paramValue == null) {
                continue;
            } else {
                if (cyCol.getType() == String.class) {
                    paramValueStr = "\"" + paramValue.toString() + "\"";
                } else {
                    paramValueStr = paramValue.toString();
                }
            }

            String jsonParam = "\"" + paramName + "\" : " + paramValueStr + ",";
            params.append(jsonParam);
        }

        params = new StringBuilder(params.substring(0, params.length() - 1));
        params.append("}");

        return params.toString();
    }

    public static CyNode getNodeByNeoId(CyNetwork network, Long neoId) {
        Set<CyNode> res = CyUtils.getNodesWithValue(network, network.getDefaultNodeTable(), "neoid", neoId);
        if (res.size() > 1) {
            throw new IllegalArgumentException("more than one start node found! " + res.toString());
        }

        if (res.size() == 0) {
            return null;
        }
        return res.iterator().next();
    }

    public static CyEdge getEdgeByNeoId(CyNetwork network, Long neoId) {
        Set<CyEdge> res = CyUtils.getEdgeWithValue(network, network.getDefaultEdgeTable(), "neoid", neoId);
        if (res.size() > 1) {
            throw new IllegalArgumentException("more than one start node found! " + res.toString());
        }

        if (res.size() == 0) {
            return null;
        }
        return res.iterator().next();
    }

    public static Object fixSpecialTypes(Object val, Class<?> req) {
        if (val.getClass() == req) {
            return val;
        } else if (val.getClass() == Integer.class && req == Long.class) {
            return ((Integer) val).longValue();
        }
        return null;
    }

    public static void updateVisualStyle(VisualMappingManager visualMappingMgr, CyNetworkView view) {
        VisualStyle vs = visualMappingMgr.getDefaultVisualStyle();
        visualMappingMgr.setVisualStyle(vs, view);
        vs.apply(view);
        view.updateView();
    }


}
