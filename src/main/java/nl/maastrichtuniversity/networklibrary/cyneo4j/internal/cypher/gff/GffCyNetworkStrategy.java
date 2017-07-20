package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.gff;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.CopyCyNetworkStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphEdge;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;
import org.cytoscape.model.*;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

class GffCyNetworkStrategy implements CopyCyNetworkStrategy {

    private static final String COLUMN_REFERENCEID = "refid";
    public static final String GENE_ID = "geneID";
    public static final String NODE_TYPE = "nodeType";
    public static final String START = "start";
    public static final String END = "end";
    public static final String LINK_TYPE = "linkType";
    public static final String ORGANISM = "organism";

    @Override
    public void createTables(CyNetwork network) {
        CyTable nodeTable = network.getDefaultNodeTable();
        CyTable edgeTable = network.getDefaultEdgeTable();
        CyTable cyNetworkTable= network.getDefaultNetworkTable();

        nodeTable.createColumn(COLUMN_REFERENCEID, Long.class, true);
        nodeTable.createColumn(GENE_ID, String.class, true);
        nodeTable.createColumn(NODE_TYPE, String.class, true);
        nodeTable.createColumn(START, Long.class, true);
        nodeTable.createColumn(END, Long.class, true);

        edgeTable.createColumn(COLUMN_REFERENCEID, Long.class, true);
        edgeTable.createColumn(LINK_TYPE, String.class, true);

        cyNetworkTable.createColumn(ORGANISM, String.class, true);
    }

    public void addPropertiesNode(CyNetwork network, GraphNode graphNode) {

        CyNode cyNode = getNodeByIdOrElseCreate(network, graphNode.getId());
        CyRow cyRow = network.getRow(cyNode);
        graphNode.getProperty(GENE_ID, String.class).ifPresent( val -> cyRow.set("geneID", val));
        graphNode.getProperty(START, Object::toString).map(Long::valueOf).ifPresent( val -> cyRow.set("start", val));
        graphNode.getProperty(END, Object::toString).map(Long::valueOf).ifPresent( val -> cyRow.set("end", val));
        graphNode.ifLabelPresent("intron", label -> cyRow.set("nodeType", "intron"));
        graphNode.ifLabelPresent("gene", label -> cyRow.set("nodeType", "gene"));
        graphNode.ifLabelPresent("transcript", label -> cyRow.set("nodeType", "transcrip"));
        graphNode.ifLabelPresent("CDS", label -> cyRow.set("nodeType", "CDS"));
        cyRow.set("name",graphNode.getLabels().stream().findFirst().orElse("nolabel") + ":" + graphNode.getProperty(GENE_ID, String.class).orElse("nogenid"));
    }

    public void addPropertiesEdge(CyNetwork network, GraphEdge graphEdge) {

        Long start = graphEdge.getStart();
        Long end = graphEdge.getEnd();

        CyNode startNode = getNodeByIdOrElseCreate(network, start);
        CyNode endNode = getNodeByIdOrElseCreate(network, end);
        CyEdge cyEdge = network.addEdge(startNode, endNode, true);
        CyRow cyRow = network.getRow(cyEdge);

        cyRow.set(COLUMN_REFERENCEID, graphEdge.getId());
        cyRow.set(CyEdge.INTERACTION, CyEdge.Type.DIRECTED);
        cyRow.set(LINK_TYPE, graphEdge.getType());

    }

    private CyNode getNodeByIdOrElseCreate(CyNetwork currNet, Long id) {
        return getNodeById(currNet, id).orElseGet(() -> createNewNode(currNet, id));
    }

    private Optional<CyNode> getNodeById(CyNetwork network, Long id) {
        String primaryKeyColumnName =network.getDefaultNodeTable().getPrimaryKey().getName();
        return network
                .getDefaultNodeTable()
                .getMatchingRows(COLUMN_REFERENCEID, id)
                .stream()
                .findFirst()
                .map(row -> network.getNode(row.get(primaryKeyColumnName, Long.class)));
    }

    private CyNode createNewNode(CyNetwork currNet, Long id) {
        CyNode newNode = currNet.addNode();
        currNet.getRow(newNode).set(COLUMN_REFERENCEID, id);
        return newNode;
    }
}
