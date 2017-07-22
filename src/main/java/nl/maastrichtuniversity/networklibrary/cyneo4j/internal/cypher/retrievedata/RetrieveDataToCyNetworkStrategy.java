package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.retrievedata;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.CopyCyNetworkStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphEdge;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;
import org.cytoscape.model.*;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

public class RetrieveDataToCyNetworkStrategy implements CopyCyNetworkStrategy {

    private static final String COLUMN_REFERENCEID = "refid";

    @Override
    public void createTables(CyNetwork network) {
        CyTable defNodeTab = network.getDefaultNodeTable();
        if (defNodeTab.getColumn(COLUMN_REFERENCEID) == null) {
            defNodeTab.createColumn(COLUMN_REFERENCEID, Long.class, false);
        }
        CyTable defEdgeTab = network.getDefaultEdgeTable();
        if (defEdgeTab.getColumn(COLUMN_REFERENCEID) == null) {
            defEdgeTab.createColumn(COLUMN_REFERENCEID, Long.class, false);
        }
    }

    public void addPropertiesNode(CyNetwork network, GraphNode graphNode) {
        CyTable cyTable = network.getDefaultNodeTable();
        Long nodeId = graphNode.getId();

        CyNode cyNode = network.addNode();
        network.getRow(cyNode).set(COLUMN_REFERENCEID, nodeId);

        for (Map.Entry<String, Object> entry : graphNode.getProperties().entrySet()) {
            createColumn(cyTable, entry);
            setEntry(cyTable, cyNode, entry);
        }
    }

    public void addPropertiesEdge(CyNetwork network, GraphEdge graphEdge) {
        CyTable cyTable = network.getDefaultEdgeTable();

        Long edgeId = graphEdge.getId();
        Long start = graphEdge.getStart();
        Long end = graphEdge.getEnd();
        String type = graphEdge.getType();

        CyNode startNode = getNodeById(network, start).orElseGet(() -> {
            CyNode setup = network.addNode();
            network.getRow(setup).set(COLUMN_REFERENCEID, start);
            return setup;
        });

        CyNode endNode = getNodeById(network, end).orElseGet(() -> {
            CyNode setup = network.addNode();
            network.getRow(setup).set(COLUMN_REFERENCEID, end);
            return setup;
        });

        CyEdge cyEdge = network.addEdge(startNode, endNode, true);

        network.getRow(cyEdge).set(COLUMN_REFERENCEID, edgeId);
        network.getRow(cyEdge).set(CyEdge.INTERACTION, type);

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
