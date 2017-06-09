package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

import org.cytoscape.model.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;
import static nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ResType.Edge;
import static nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ResType.Node;


public class CypherResultParser {

    public static final String COLUMN_ID = "neoid";
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
        cols.forEach(s -> colType.put(s, ResType.Unknown));
    }

    private void readResultTable(List<List<Object>> rows) {
        for (List<Object> row : rows) {
            for (int i = 0; i < row.size(); ++i) {

                Object item = row.get(i);
                String col = cols.get(i);
                ResType type = duckTypeObject(item, col);

                if (Node.equals(type)) {
                    parseNode(item);
                }
                if (Edge.equals(type)) {
                    parseEdge(item);
                }
            }
        }
    }

    private void parseNode(Object nodeObj) {

        CyTable defNodeTab = currNet.getDefaultNodeTable();
        if (defNodeTab.getColumn(COLUMN_ID) == null) {
            defNodeTab.createColumn(COLUMN_ID, Long.class, false);
        }

        Map<String, Object> node = (Map<String, Object>) nodeObj;

        Long self = extractID((String) node.get("self"));

        CyNode cyNode = getNodeByNeoId(currNet, self);

        if (cyNode == null) {
            cyNode = currNet.addNode();
            currNet.getRow(cyNode).set(COLUMN_ID, self);
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

            Object value = fixSpecialTypes(obj.getValue(), defNodeTab.getColumn(obj.getKey()).getType());
            defNodeTab.getRow(cyNode.getSUID()).set(obj.getKey(), value);
        }
    }

    private void parseEdge(Object edgeObj) {

        CyTable defEdgeTab = currNet.getDefaultEdgeTable();
        if (defEdgeTab.getColumn(COLUMN_ID) == null) {
            defEdgeTab.createColumn(COLUMN_ID, Long.class, false);
        }

        CyTable defNodeTab = currNet.getDefaultNodeTable();
        if (defNodeTab.getColumn(COLUMN_ID) == null) {
            defNodeTab.createColumn(COLUMN_ID, Long.class, false);
        }

        Map<Object, Object> edge = (Map<Object, Object>) edgeObj;

        String selfURL = (String) edge.get("self");
        Long self = extractID(selfURL);

        CyEdge cyEdge = getEdgeByNeoId(currNet, self);

        if (cyEdge == null) {

            String startUrl = (String) edge.get("start");
            Long start = extractID(startUrl);

            String endUrl = (String) edge.get("end");
            Long end = extractID(endUrl);

            String type = (String) edge.get("type");

            CyNode startNode = getNodeByNeoId(currNet, start);
            CyNode endNode = getNodeByNeoId(currNet, end);

            if (startNode == null) {
                startNode = currNet.addNode();
                currNet.getRow(startNode).set(COLUMN_ID, start);
            }

            if (endNode == null) {
                endNode = currNet.addNode();
                currNet.getRow(endNode).set(COLUMN_ID, end);
            }

            cyEdge = currNet.addEdge(startNode, endNode, true);

            currNet.getRow(cyEdge).set(COLUMN_ID, self);
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

                Object value = fixSpecialTypes(obj.getValue(), defEdgeTab.getColumn(obj.getKey()).getType());
                defEdgeTab.getRow(cyEdge.getSUID()).set(obj.getKey(), value);

            }
        }
    }

    private ResType duckTypeObject(Object obj, String column) {

        ResType result = colType.get(column);

        if (result == ResType.Unknown) {
            if (isNodeType(obj)) {
                result = Node;
            } else if (isEdgeType(obj)) {
                result = Edge;
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


    private CyNode getNodeByNeoId(CyNetwork network, Long neoId) {
        Set<CyNode> res = getNodesWithValue(network, network.getDefaultNodeTable(), neoId);
        if (res.size() > 1) {
            throw new IllegalArgumentException("more than one start node found! " + res.toString());
        }
        if (res.size() == 0) {
            return null;
        }
        return res.iterator().next();
    }

    private CyEdge getEdgeByNeoId(CyNetwork network, Long neoId) {
        Set<CyEdge> res = getEdgeWithValue(network, network.getDefaultEdgeTable(), neoId);
        if (res.size() > 1) {
            throw new IllegalArgumentException("more than one start node found! " + res.toString());
        }
        if (res.size() == 0) {
            return null;
        }
        return res.iterator().next();
    }

    private Set<CyNode> getNodesWithValue(CyNetwork net, CyTable table, Object value) {
        String primaryKeyColname = table.getPrimaryKey().getName();
        return getValueFromRows(table.getMatchingRows(COLUMN_ID, value), primaryKeyColname, net::getNode);
    }

    private Set<CyEdge> getEdgeWithValue(CyNetwork net, CyTable table, Object value) {
        String primaryKeyColname = table.getPrimaryKey().getName();
        return getValueFromRows(table.getMatchingRows(COLUMN_ID, value), primaryKeyColname, net::getEdge);
    }

    private <T> Set<T> getValueFromRows(Collection<CyRow> matchingRows, String primaryKeyColname, Function<Long, T> mapper) {
        return matchingRows.stream()
            .map(cyRow -> cyRow.get(primaryKeyColname, Long.class))
            .filter(Objects::nonNull)
            .map(mapper)
            .filter(Objects::nonNull)
            .collect(toSet());
    }

    private Object fixSpecialTypes(Object val, Class<?> req) {
        if (val.getClass() == req) {
            return val;
        } else if (val.getClass() == Integer.class && req == Long.class) {
            return ((Integer) val).longValue();
        }
        return null;
    }
}
