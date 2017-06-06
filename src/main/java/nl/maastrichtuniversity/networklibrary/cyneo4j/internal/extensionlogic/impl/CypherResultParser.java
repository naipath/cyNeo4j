package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class CypherResultParser {

    private static final String NODE_KEY = "outgoing_typed_relationships";
    private static final String EDGE_KEY = "type";

    private List<String> cols;
    private Map<String, ResType> colType = new HashMap<>();
    private CyNetwork currNet;

    public CypherResultParser(CyNetwork network) {
        this.currNet = network;
    }

    public void parseRetVal(Object callRetValue) {
        Map<String, Object> retVal = (Map<String, Object>) callRetValue;

        readColumns((List<String>) retVal.get("columns"));

        readResultTable((List<List<Object>>) retVal.get("data"));
    }

    private void readColumns(List<String> columns) {
        cols = columns;
        for (String col : cols) {
            colType.put(col, ResType.Unknown);
        }
    }

    private void readResultTable(List<List<Object>> rows) {
        for (List<Object> row : rows) {
            for (int i = 0; i < row.size(); ++i) {

                Object item = row.get(i);
                String col = cols.get(i);
                ResType type = duckTypeObject(item, col);

                switch (type) {
                    case Node:
                        parseNode(item);
                        break;

                    case Edge:
                        parseEdge(item);
                        break;

                    default:
                        break;

                }
            }
        }
    }

    private void parseNode(Object nodeObj) {

        CyTable defNodeTab = currNet.getDefaultNodeTable();
        if (defNodeTab.getColumn("neoid") == null) {
            defNodeTab.createColumn("neoid", Long.class, false);
        }

        Map<String, Object> node = (Map<String, Object>) nodeObj;

        Long self = extractID((String) node.get("self"));

        CyNode cyNode = CyUtils.getNodeByNeoId(currNet, self);

        if (cyNode == null) {
            cyNode = currNet.addNode();
            currNet.getRow(cyNode).set("neoid", self);
        }

        Map<String, Object> nodeProps = (Map<String, Object>) node.get("data");

        for (Entry<String, Object> obj : nodeProps.entrySet()) {
            if (defNodeTab.getColumn(obj.getKey()) == null) {
                if (obj.getValue().getClass() == ArrayList.class) {
                    defNodeTab.createListColumn(obj.getKey(), String.class, true);
                } else {
                    defNodeTab.createColumn(obj.getKey(), obj.getValue().getClass(), true);
                }
            }

            Object value = CyUtils.fixSpecialTypes(obj.getValue(), defNodeTab.getColumn(obj.getKey()).getType());
            defNodeTab.getRow(cyNode.getSUID()).set(obj.getKey(), value);
        }
    }

    private void parseEdge(Object edgeObj) {

        CyTable defEdgeTab = currNet.getDefaultEdgeTable();
        if (defEdgeTab.getColumn("neoid") == null) {
            defEdgeTab.createColumn("neoid", Long.class, false);
        }

        CyTable defNodeTab = currNet.getDefaultNodeTable();
        if (defNodeTab.getColumn("neoid") == null) {
            defNodeTab.createColumn("neoid", Long.class, false);
        }

        Map<Object, Object> edge = (Map<Object, Object>) edgeObj;

        String selfURL = (String) edge.get("self");
        Long self = extractID(selfURL);

        CyEdge cyEdge = CyUtils.getEdgeByNeoId(currNet, self);

        if (cyEdge == null) {

            String startUrl = (String) edge.get("start");
            Long start = extractID(startUrl);

            String endUrl = (String) edge.get("end");
            Long end = extractID(endUrl);

            String type = (String) edge.get("type");

            CyNode startNode = CyUtils.getNodeByNeoId(currNet, start);
            CyNode endNode = CyUtils.getNodeByNeoId(currNet, end);

            if (startNode == null) {
                startNode = currNet.addNode();
                currNet.getRow(startNode).set("neoid", start);
            }

            if (endNode == null) {
                endNode = currNet.addNode();
                currNet.getRow(endNode).set("neoid", end);
            }

            cyEdge = currNet.addEdge(startNode, endNode, true);

            currNet.getRow(cyEdge).set("neoid", self);
            currNet.getRow(cyEdge).set(CyEdge.INTERACTION, type);

            Map<String, Object> nodeProps = (Map<String, Object>) edge.get("data");

            for (Entry<String, Object> obj : nodeProps.entrySet()) {
                if (defEdgeTab.getColumn(obj.getKey()) == null) {
                    if (obj.getValue().getClass() == ArrayList.class) {
                        defEdgeTab.createListColumn(obj.getKey(), String.class, true);
                    } else {
                        defEdgeTab.createColumn(obj.getKey(), obj.getValue().getClass(), true);
                    }
                }

                Object value = CyUtils.fixSpecialTypes(obj.getValue(), defEdgeTab.getColumn(obj.getKey()).getType());
                defEdgeTab.getRow(cyEdge.getSUID()).set(obj.getKey(), value);

            }
        }
    }

    private ResType duckTypeObject(Object obj, String column) {

        ResType result = colType.get(column);

        if (result == ResType.Unknown) {
            if (isNodeType(obj)) {
                result = ResType.Node;
            } else if (isEdgeType(obj)) {
                result = ResType.Edge;
            } else { // this could / should be extended
                result = ResType.Ignore;
            }
            colType.put(column, result);
        }

        return result;
    }

    private boolean isNodeType(Object obj) {
        try {
            Map<String, Object> node = (Map<String, Object>) obj;
            return node.containsKey(NODE_KEY);

        } catch (ClassCastException e) {
            return false;
        }
    }

    private boolean isEdgeType(Object obj) {
        try {
            Map<String, Object> node = (Map<String, Object>) obj;
            return node.containsKey(EDGE_KEY);

        } catch (ClassCastException e) {
            return false;
        }
    }

    private Long extractID(String objUrl) {
        return Long.valueOf(objUrl.substring(objUrl.lastIndexOf('/') + 1));
    }
}
