package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.EdgeMapping;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.GraphToCytoscapeMapping;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.NodeLabelMapping;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.NodeMapping;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;
import org.cytoscape.model.CyEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CypherQueryTemplate {

    private String name;
    private String cypherQuery;
    private Map<String,Class<?>> parameterTypes;
    private Map<String,Object> parameters;
    private GraphToCytoscapeMapping mapping;

    CypherQueryTemplate(
            String name,
            String query,
            Map<String, Class<?>> parameterTypes,
            GraphToCytoscapeMapping mapping) {
        this.name = name;
        this.cypherQuery = query;
        this.parameterTypes = new HashMap<>(parameterTypes);
        this.mapping = mapping;
        this.parameters = new HashMap<>();
        for(String key : parameterTypes.keySet()) {
            parameters.put(key, null);
        }
    }

    public CypherQuery createQuery() {
        return CypherQuery.builder()
                .query(cypherQuery)
                .params(parameters)
                .build();
    }

    public GraphToCytoscapeMapping getMapping() {
        return mapping;
    }

    public String getName() {
        return name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, Class<?>> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameters(Map<String, Object> parameterMap) {
        for(Map.Entry<String,Object> entry : parameterMap.entrySet()) {
            if(this.parameters.containsKey(entry.getKey())) { //TODO: check type
                this.parameters.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public static class Builder {

        private String query;
        private Map<String, Class<?>> parameterTypes = new HashMap<>();
        private Map<String, NodeMapping<?>> nodePropertyMapping = new HashMap<>();
        private List<NodeLabelMapping> nodeLabelMappingList = new ArrayList<>();
        private NodeMapping<?> nodeReferenceIdMapping;
        private Map<String, EdgeMapping<?>> edgePropertyMapping = new HashMap<>();
        private EdgeMapping<?> edgeLinkTypeMapping;
        private EdgeMapping<?> edgeReferenceIdMapping;
        private String name;
        private CyEdge.Type interaction;

        public Builder setName(String name) {
            this.name =name;
            return this;
        }

        public Builder setQueryTemplate(String query) {
            this.query =query;
            return this;
        }

        public Builder addParameter(String parameter, Class<?> type) {
            parameterTypes.put(parameter, type);
            return this;
        }

        public <T> Builder addNodePropertyMapping(String name, String columnName, Class<T> type) {
            nodePropertyMapping.put(name, new NodeMapping<T>(columnName,type));
            return this;
        }

        public <T> Builder addNodeLabelMapping(String name, String match, String columnName, Class<T> type) {
            //TODO, name and type
            Pattern pattern = Pattern.compile(match);
            NodeLabelMapping nodeLabelMapping = new NodeLabelMapping(pattern, columnName);
            nodeLabelMappingList.add(nodeLabelMapping);
            return this;
        }

        public <T> Builder addNodeReferenceIdMapping(String name, Class<T> type) {
            nodeReferenceIdMapping = new NodeMapping<T>(name, type);
            return this;
        }

        public <T> Builder addEdgePropertyMapping(String name, String columnName, Class<T> type) {
            edgePropertyMapping.put(name, new EdgeMapping<T>(columnName, type));
            return this;
        }

        public Builder addInteractionMapping(String interactionValue) {
            interaction = CyEdge.Type.valueOf(interactionValue);
            return this;
        }

        public <T> Builder addEdgeLinkTypeMapping(String name, Class<T> type) {
            edgeLinkTypeMapping = new EdgeMapping<T>(name, type);
            return this;
        }

        public <T> Builder addEdgeReferenceIdMapping(String name, Class<T> type) {
            edgeReferenceIdMapping = new EdgeMapping<T>(name, type);
            return this;
        }

        public CypherQueryTemplate build() {
            GraphToCytoscapeMapping graphToCytoscapeMapping = new GraphToCytoscapeMapping(
                    nodeReferenceIdMapping,
                    nodePropertyMapping,
                    nodeLabelMappingList,
                    edgeReferenceIdMapping,
                    edgeLinkTypeMapping,
                    interaction,
                    edgePropertyMapping
            );
            CypherQueryTemplate cypherQueryTemplate = new CypherQueryTemplate(
                    name,
                    query,
                    parameterTypes,
                    graphToCytoscapeMapping
            );
            return cypherQueryTemplate;
        }
    }


}
