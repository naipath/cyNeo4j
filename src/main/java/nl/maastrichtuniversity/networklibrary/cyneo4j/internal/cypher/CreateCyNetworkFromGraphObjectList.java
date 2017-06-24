package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.*;
import org.cytoscape.model.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

public class CreateCyNetworkFromGraphObjectList implements GraphVisitor {

    private static final String COLUMN_ID = "neoid";
    private CyNetwork currNet;

    public CreateCyNetworkFromGraphObjectList(CyNetwork network) {
        this.currNet = network;
    }

    public void parseRetVal(List<GraphObject> list) {
        list.forEach(item -> {
            item.accept(this);
        });
    }

    @Override
    public void visit(GraphNode graphNode) {
        CyTable defNodeTab = currNet.getDefaultNodeTable();
        if (defNodeTab.getColumn(COLUMN_ID) == null) {
            defNodeTab.createColumn(COLUMN_ID, Long.class, false);
        }

        Long self = graphNode.getId();

        CyNode cyNode = getNodeByNeoId(currNet, self);

        if (cyNode == null) {
            cyNode = currNet.addNode();
            currNet.getRow(cyNode).set(COLUMN_ID, self);
        }

        Map<String, Object> nodeProps = graphNode.getProperties();

        doStuff(defNodeTab, cyNode, nodeProps);

    }

    @Override
    public void visit(GraphEdge graphEdge) {
        CyTable defEdgeTab = currNet.getDefaultEdgeTable();
        if (defEdgeTab.getColumn(COLUMN_ID) == null) {
            defEdgeTab.createColumn(COLUMN_ID, Long.class, false);
        }

        CyTable defNodeTab = currNet.getDefaultNodeTable();
        if (defNodeTab.getColumn(COLUMN_ID) == null) {
            defNodeTab.createColumn(COLUMN_ID, Long.class, false);
        }


        Long self = graphEdge.getId();

        CyEdge cyEdge = getEdgeByNeoId(currNet, self);

        if (cyEdge == null) {

            Long start = graphEdge.getStart();
            Long end = graphEdge.getEnd();
            String type = graphEdge.getType();

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

            Map<String, Object> nodeProps = graphEdge.getProperties();

            doStuff(defEdgeTab, cyEdge, nodeProps);
        }
    }

    @Override
    public void visit(GraphResult neo4jResult) {
        for(GraphObject graphObject : neo4jResult.getAll()) {
            graphObject.accept(this);
        }
    }

    @Override
    public void visit(GraphLong neo4jLong) {

    }

    @Override
    public void visit(GraphUnspecifiedType neo4jUnspecifiedType) {

    }

    private void doStuff(CyTable defEdgeTab, CyIdentifiable cyEdge, Map<String, Object> nodeProps) {
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
