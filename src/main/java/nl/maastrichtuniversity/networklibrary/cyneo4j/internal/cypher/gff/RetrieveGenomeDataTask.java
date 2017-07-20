package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.gff;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.CreateCyNetworkFromGraphObjectList;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery.*;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphObject;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RetrieveGenomeDataTask extends AbstractTask {

    private static final String NODE_QUERY = "MATCH (n:$label {geneID:$geneID}) RETURN n";
    private static final String EDGE_QUERY = "MATCH ()-[r]->() RETURN r";

    private final Services services;
    private final String networkName;
    private final String visualStyleTitle;
    private final GeneQuery geneQuery;
    private final GeneSchema<CypherQuery> cypherQueryGeneSchema;


    public RetrieveGenomeDataTask(Services services, String networkName, String visualStyleTitle, GeneQuery geneQuery) {
        this.services = services;
        this.networkName = networkName;
        this.visualStyleTitle = visualStyleTitle;
        this.geneQuery = geneQuery;
        cypherQueryGeneSchema = new GffNeo4jGeneSchema();
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        try {
            taskMonitor.setTitle("Importing the Neo4j Graph " + networkName);
            // setup network
            CyNetwork network = services.getCyNetworkFactory().createNetwork();
            network.getRow(network).set(CyNetwork.NAME, networkName);
            services.getCyNetworkManager().addNetwork(network);

            CreateCyNetworkFromGraphObjectList cypherParser = new CreateCyNetworkFromGraphObjectList(network, new GffCyNetworkStrategy());
            taskMonitor.setStatusMessage("Downloading graph");
            CypherQuery cypherQuery = geneQuery.accept(cypherQueryGeneSchema);
            cypherParser.importGraph(executeQuery(cypherQuery));

            CyEventHelper cyEventHelper = services.getCyEventHelper();
            cyEventHelper.flushPayloadEvents();

            taskMonitor.setStatusMessage("Creating View");
            CyNetworkView networkView = services.getCyNetworkViewFactory().createNetworkView(network);
            services.getCyNetworkViewManager().addNetworkView(networkView);

            taskMonitor.setStatusMessage("Applying Layout");
            Set<View<CyNode>> nodes = new HashSet<>();
            CyLayoutAlgorithm layout = services.getCyLayoutAlgorithmManager().getLayout("force-directed");
            insertTasksAfterCurrentTask(layout.createTaskIterator(networkView, layout.createLayoutContext(), nodes, null));

            VisualStyle visualStyle = services.getVisualMappingManager().getAllVisualStyles().stream()
                    .filter(vs -> vs.getTitle().equals(visualStyleTitle))
                    .findFirst().orElseGet(() -> services.getVisualMappingManager().getDefaultVisualStyle());
            visualStyle.apply(networkView);
            networkView.updateView();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private List<GraphObject> executeQuery(CypherQuery query) {
        return services.getNeo4jClient().executeQuery(query);
    }

    private static final class GffNeo4jGeneSchema implements GeneSchema<CypherQuery> {

        @Override
        public CypherQuery createQuery(GenePathQuery genePathQuery) {
            return CypherQuery.builder()
                    .query("MATCH p=(n:gene) - [r:order*] - (m:gene) WHERE n.geneID=$from AND m.geneID=$to RETURN p")
                    .params("from",genePathQuery.getFrom().getIdentifier())
                    .params("to",genePathQuery.getTo().getIdentifier())
                    .build();
        }

        @Override
        public CypherQuery createQuery(GeneDetailsQuery geneDetailsQuery) {
            return CypherQuery.builder()
                    .query("MATCH (n) - [r] - (m) WHERE n.geneID IN $geneIDs  RETURN n, r, m")
                    .params("geneIDs", geneDetailsQuery.getGeneIdList().stream().map(GeneId::getIdentifier).collect(Collectors.toList()))
                    .build();
        }
    }
}
