package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.*;
import org.cytoscape.model.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

public class CreateCyNetworkFromGraphObjectList implements GraphVisitor {

    private static final String COLUMN_REFERENCEID = "refid";
    private CyNetwork currNet;

    public CreateCyNetworkFromGraphObjectList(CyNetwork network) {
        this.currNet = network;
    }

    public void parseRetVal(List<GraphObject> list) {


        CyTable defNodeTab = currNet.getDefaultNodeTable();

        if (defNodeTab.getColumn(COLUMN_REFERENCEID) == null) {
            defNodeTab.createColumn(COLUMN_REFERENCEID, Long.class, false);
        }
        CyTable defEdgeTab = currNet.getDefaultEdgeTable();
        if (defEdgeTab.getColumn(COLUMN_REFERENCEID) == null) {
            defEdgeTab.createColumn(COLUMN_REFERENCEID, Long.class, false);
        }

        list.forEach(item -> {
            item.accept(this);
        });
    }

    @Override
    public void visit(GraphNode graphNode) {

        CyTable defNodeTab = currNet.getDefaultNodeTable();
        Long nodeId = graphNode.getId();

        CyNode cyNode = getNodeById(currNet, nodeId);

        if (cyNode == null) {
            cyNode = currNet.addNode();
            currNet.getRow(cyNode).set(COLUMN_REFERENCEID, nodeId);
        }

        addPropertiesToRowInCyTable(defNodeTab, cyNode, graphNode.getProperties());

    }

    @Override
    public void visit(GraphEdge graphEdge) {
        CyTable defEdgeTab = currNet.getDefaultEdgeTable();

        Long edgeId = graphEdge.getId();

        CyEdge cyEdge = getEdgeById(currNet, edgeId);

        if (cyEdge == null) {

            Long start = graphEdge.getStart();
            Long end = graphEdge.getEnd();
            String type = graphEdge.getType();

            CyNode startNode = getNodeById(currNet, start);
            CyNode endNode = getNodeById(currNet, end);

            if (startNode == null) {
                startNode = currNet.addNode();
                currNet.getRow(startNode).set(COLUMN_REFERENCEID, start);
            }

            if (endNode == null) {
                endNode = currNet.addNode();
                currNet.getRow(endNode).set(COLUMN_REFERENCEID, end);
            }

            cyEdge = currNet.addEdge(startNode, endNode, true);

            currNet.getRow(cyEdge).set(COLUMN_REFERENCEID, edgeId);
            currNet.getRow(cyEdge).set(CyEdge.INTERACTION, type);

            addPropertiesToRowInCyTable(defEdgeTab, cyEdge, graphEdge.getProperties());
        }
    }

    @Override
    public void visit(GraphResult result) {
        for(GraphObject graphObject : result.getAll()) {
            graphObject.accept(this);
        }
    }

    @Override
    public void visit(GraphLong neo4jLong) {

    }

    @Override
    public void visit(GraphUnspecifiedType neo4jUnspecifiedType) {

    }

    private void addPropertiesToRowInCyTable(CyTable cyTable, CyIdentifiable cyIdentifiable, Map<String, Object> nodeProps) {
        for (Entry<String, Object> entry : nodeProps.entrySet()) {
            if (cyTable.getColumn(entry.getKey()) == null) {
                if (entry.getValue() instanceof List) {
                    cyTable.createListColumn(entry.getKey(), String.class, true);
                } else {
                    cyTable.createColumn(entry.getKey(), entry.getValue().getClass(), true);
                }
            }
//            Object value = fixSpecialTypes(entry.getValue(), cyTable.getColumn(entry.getKey()).getType());
            cyTable.getRow(cyIdentifiable.getSUID()).set(entry.getKey(), entry.getValue());
        }
    }

    private CyNode getNodeById(CyNetwork network, Long id) {
        Set<CyNode> res = getNodesWithValue(network, network.getDefaultNodeTable(), id);
        if (res.size() > 1) {
            throw new IllegalArgumentException("more than one start node found! " + res.toString());
        }
        if (res.size() == 0) {
            return null;
        }
        return res.iterator().next();
    }

    private CyEdge getEdgeById(CyNetwork network, Long id) {
        Set<CyEdge> res = getEdgeWithValue(network, network.getDefaultEdgeTable(), id);
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
        return getValueFromRows(table.getMatchingRows(COLUMN_REFERENCEID, value), primaryKeyColname, net::getNode);
    }

    private Set<CyEdge> getEdgeWithValue(CyNetwork net, CyTable table, Object value) {
        String primaryKeyColname = table.getPrimaryKey().getName();
        return getValueFromRows(table.getMatchingRows(COLUMN_REFERENCEID, value), primaryKeyColname, net::getEdge);
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
        if(val.getClass().isAssignableFrom(req)) {
            return val;
        }
        if(val instanceof Number && req.equals(Long.class)) {
            return ((Number)val).longValue();
        }
        return null;
    }

}
