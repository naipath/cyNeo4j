package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.*;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values.ValueExpression;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphEdge;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CypherQueryTemplate {

    private String name;
    private String cypherQuery;
    private Map<String,Class<?>> parameterTypes;
    private Map<String,Object> parameters;
    private GraphMapping mapping;

    CypherQueryTemplate(
            String name,
            String query,
            Map<String, Class<?>> parameterTypes,
            GraphMapping mapping) {
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

    public GraphMapping getMapping() {
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
        private String name;
        private Map<String, Class<?>> parameterTypes = new HashMap<>();
        private List<NodeColumnMapping> nodeColumnMapping = new ArrayList<>();
        private List<EdgeColumnMapping> edgeColumnMapping = new ArrayList<>();
        private String nodeReferenceIdColumn;
        private String edgeReferenceIdColumn;

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

        public <T> Builder addNodeColumnMapping(String columnName, Class<T> type, ValueExpression<GraphNode, T> valueExpression) {
            nodeColumnMapping.add(new NodeColumnMapping(columnName, type, valueExpression));
            return this;
        }

        public <T> Builder addEdgeColumnMapping(String columnName, Class<T> type, ValueExpression<GraphEdge, T> valueExpression) {
            edgeColumnMapping.add(new EdgeColumnMapping(columnName, type, valueExpression));
            return this;
        }

        public Builder setNodeReferenceIdColumn(String nodeReferenceIdColumn) {
            this.nodeReferenceIdColumn = nodeReferenceIdColumn;
            return this;
        }
        public Builder setEdgeReferenceIdColumn(String edgeReferenceIdColumn) {
            this.edgeReferenceIdColumn = edgeReferenceIdColumn;
            return this;
        }

        public CypherQueryTemplate build() {
            GraphMapping graphMapping = new GraphMapping(
                    nodeColumnMapping,
                    edgeColumnMapping,
                    nodeReferenceIdColumn,
                    edgeReferenceIdColumn
                    );
            CypherQueryTemplate cypherQueryTemplate = new CypherQueryTemplate(
                    name,
                    query,
                    parameterTypes,
                    graphMapping
            );
            return cypherQueryTemplate;
        }
    }
}
