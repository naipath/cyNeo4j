package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphEdge;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;
import org.cytoscape.model.*;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

class CopyCyNetworkStrategy {

    private static final String COLUMN_REFERENCEID = "refid";

    void addPropertiesNode(CyNetwork currNet, GraphNode graphNode) {
        CyTable cyTable = currNet.getDefaultNodeTable();
        Long nodeId = graphNode.getId();

        CyNode cyNode = currNet.addNode();
        currNet.getRow(cyNode).set(COLUMN_REFERENCEID, nodeId);

        for (Map.Entry<String, Object> entry : graphNode.getProperties().entrySet()) {
            createColumn(cyTable, entry);
            setEntry(cyTable, cyNode, entry);
        }
    }

    void addPropertiesEdge(CyNetwork currNet, GraphEdge graphEdge) {
        CyTable cyTable = currNet.getDefaultNodeTable();

        Long edgeId = graphEdge.getId();
        Long start = graphEdge.getStart();
        Long end = graphEdge.getEnd();
        String type = graphEdge.getType();

        CyNode startNode = getNodeById(currNet, start).orElseGet(() -> {
            CyNode setup = currNet.addNode();
            currNet.getRow(setup).set(COLUMN_REFERENCEID, start);
            return setup;
        });

        CyNode endNode = getNodeById(currNet, end).orElseGet(() -> {
            CyNode setup = currNet.addNode();
            currNet.getRow(setup).set(COLUMN_REFERENCEID, end);
            return setup;
        });

        CyEdge cyEdge = currNet.addEdge(startNode, endNode, true);

        currNet.getRow(cyEdge).set(COLUMN_REFERENCEID, edgeId);
        currNet.getRow(cyEdge).set(CyEdge.INTERACTION, type);

        for (Map.Entry<String, Object> entry : graphEdge.getProperties().entrySet()) {
            createColumn(cyTable, entry);
            setEntry(cyTable, cyEdge, entry);
        }
    }

    private void createColumn(CyTable cyTable, Map.Entry<String, Object> entry) {
        if (cyTable.getColumn(entry.getKey()) == null) {
            if (entry.getValue() instanceof List) {
                cyTable.createListColumn(entry.getKey(), String.class, true);
            } else {
                cyTable.createColumn(entry.getKey(), entry.getValue().getClass(), true);
            }
        }
    }

    private void setEntry(CyTable cyTable, CyIdentifiable cyId, Map.Entry<String, Object> entry) {
        cyTable.getRow(cyId.getSUID()).set(entry.getKey(), entry.getValue());
    }

    private Optional<CyNode> getNodeById(CyNetwork network, Long id) {
        return getNodesWithValue(network, network.getDefaultNodeTable(), id).stream().findFirst();
    }

    private Set<CyNode> getNodesWithValue(CyNetwork net, CyTable table, Object value) {
        String primaryKeyColname = table.getPrimaryKey().getName();
        return getValueFromRows(table.getMatchingRows(COLUMN_REFERENCEID, value), primaryKeyColname, net::getNode);
    }

    private <T> Set<T> getValueFromRows(Collection<CyRow> matchingRows, String primaryKeyColname, Function<Long, T> mapper) {
        return matchingRows.stream()
            .map(cyRow -> cyRow.get(primaryKeyColname, Long.class))
            .filter(Objects::nonNull)
            .map(mapper)
            .filter(Objects::nonNull)
            .collect(toSet());
    }

}
