package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils;

import org.cytoscape.model.*;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

public class CyUtils {

    public static String convertCyAttributesToJson(CyIdentifiable item, CyTable tab) {
        StringBuilder params = new StringBuilder("{");
        for (CyColumn cyCol : tab.getColumns()) {
            String paramName = cyCol.getName();
            if (!paramName.equals("neoid")) {

                Object paramValue = tab.getRow(item.getSUID()).get(cyCol.getName(), cyCol.getType());

                if (paramValue != null) {
                    String paramValueStr = cyCol.getType() == String.class ? "\"" + paramValue.toString() + "\"" : paramValue.toString();
                    params.append("\"")
                        .append(paramName)
                        .append("\" : ")
                        .append(paramValueStr)
                        .append(",");
                }
            }
        }

        params = new StringBuilder(params.substring(0, params.length() - 1));
        params.append("}");

        return params.toString();
    }

    public static CyNode getNodeByNeoId(CyNetwork network, Long neoId) {
        Set<CyNode> res = getNodesWithValue(network, network.getDefaultNodeTable(), "neoid", neoId);
        if (res.size() > 1) {
            throw new IllegalArgumentException("more than one start node found! " + res.toString());
        }
        if (res.size() == 0) {
            return null;
        }
        return res.iterator().next();
    }

    public static CyEdge getEdgeByNeoId(CyNetwork network, Long neoId) {
        Set<CyEdge> res = getEdgeWithValue(network, network.getDefaultEdgeTable(), "neoid", neoId);
        if (res.size() > 1) {
            throw new IllegalArgumentException("more than one start node found! " + res.toString());
        }
        if (res.size() == 0) {
            return null;
        }
        return res.iterator().next();
    }

    public static Set<CyNode> getNodesWithValue(CyNetwork net, CyTable table, String colname, Object value) {
        String primaryKeyColname = table.getPrimaryKey().getName();
        return getValueFromRows(table.getMatchingRows(colname, value), primaryKeyColname, net::getNode);
    }

    private static Set<CyEdge> getEdgeWithValue(CyNetwork net, CyTable table, String colname, Object value) {
        String primaryKeyColname = table.getPrimaryKey().getName();
        return getValueFromRows(table.getMatchingRows(colname, value), primaryKeyColname, net::getEdge);
    }

    private static <T> Set<T> getValueFromRows(Collection<CyRow> matchingRows, String primaryKeyColname, Function<Long, T> mapper) {
        return matchingRows.stream()
            .map(cyRow -> cyRow.get(primaryKeyColname, Long.class))
            .filter(Objects::nonNull)
            .map(mapper)
            .filter(Objects::nonNull)
            .collect(toSet());
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
