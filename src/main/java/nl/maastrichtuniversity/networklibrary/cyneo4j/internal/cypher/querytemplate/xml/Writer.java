package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.xml;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CopyAllMappingStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.MappingStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.MappingStrategyVisitor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.EdgeColumnMapping;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.GraphMapping;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.NodeColumnMapping;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Writer {

    private static final JAXBContext jaxbContext = createJaxbContext();

    private static JAXBContext createJaxbContext() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(QueryTemplate.class);
            return jaxbContext;
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot queryTemplate jaxb context for reading cyNeo4j query template files");
        }
    }

    public void write(CypherQueryTemplate template, OutputStream outputStream) throws JAXBException {
        QueryTemplate queryTemplate = queryTemplate(template);
        jaxbContext.createMarshaller().marshal(queryTemplate,outputStream);
    }

    private QueryTemplate queryTemplate(CypherQueryTemplate cypherQueryTemplate) {
        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setName(cypherQueryTemplate.getName());
        queryTemplate.setQuery(cypherQueryTemplate.getCypherQuery());
        queryTemplate.setParameters(parameters(cypherQueryTemplate));
        queryTemplate.setMapping(mapping(cypherQueryTemplate.getMapping()));
        return queryTemplate;

    }

    private Parameters parameters(CypherQueryTemplate template) {
        Parameters parameters = new Parameters();
        for(Map.Entry<String, Class<?>> entry : template.getParameterTypes().entrySet()) {
            Parameter parameter = new Parameter();
            parameter.setName(entry.getKey());
            parameter.setType(entry.getValue().getName());
            parameters.getParameterList().add(parameter);
        }
        return parameters;
    }


    private Mapping mapping(MappingStrategy mappingStrategy) {
        ToMappingVisitor visitor = new ToMappingVisitor();
        mappingStrategy.accept(visitor);
        return visitor.getMapping();
    }

    private static class  ToMappingVisitor implements MappingStrategyVisitor {
        Mapping mapping;

        public Mapping getMapping() {
            return mapping;
        }

        @Override
        public void visit(GraphMapping graphMapping) {
            ColumnMapping columnMapping = new ColumnMapping();

            NodeMapping nodeMapping = new NodeMapping();
            nodeMapping.setReferenceIdColumn(graphMapping.getNodeReferenceIdColumn());
            List<NodeColumn> nodeColumnList = new ArrayList<>();
            for(NodeColumnMapping nodeColumnMapping : graphMapping.getNodeColumnMapping()) {
                NodeColumn nodeColumn = new NodeColumn();

                nodeColumn.setName(nodeColumnMapping.getColumnName());
                nodeColumn.setType(nodeColumnMapping.getColumnType().getName());

                ValueExpression<?, ?> valueExpression = nodeColumnMapping.getValueExpression();
                ToNodeVisitor toNodeVisitor = new ToNodeVisitor(nodeColumn);
                valueExpression.accept(toNodeVisitor);
            }
            columnMapping.setNodeMapping(nodeMapping);

            EdgeMapping edgeMapping = new EdgeMapping();
            edgeMapping.setReferenceIdColumn(graphMapping.getEdgeReferenceIdColumn());
            columnMapping.setEdgeMapping(edgeMapping);
            for(EdgeColumnMapping edgeColumnMapping: graphMapping.getEdgeColumnMapping()) {
                EdgeColumn edgeColumn = new EdgeColumn();

                edgeColumn.setName(edgeColumnMapping.getColumnName());
                edgeColumn.setType(edgeColumnMapping.getColumnType().getName());
                edgeColumn.setEdgeType(new EdgeType());
                ValueExpression<?, ?> valueExpression = edgeColumnMapping.getValueExpression();
                ToEdgeVisitor toEdgeVisitor = new ToEdgeVisitor(edgeColumn);
                valueExpression.accept(toEdgeVisitor);
            }
            mapping = columnMapping;
        }

        @Override
        public void visit(CopyAllMappingStrategy copyAllMappingStrategy) {
            CopyAll copyAll = new CopyAll();
            copyAll.setNetwork(copyAllMappingStrategy.getNetworkName());
            copyAll.setReferenceIdColumn(copyAllMappingStrategy.getReferenceColumn());
            mapping = copyAll;
        }
    }

    private static class ToNodeVisitor implements ValueExpressionVisitor {
        private NodeColumn nodeColumn;
        public ToNodeVisitor(NodeColumn nodeColumn) {
            this.nodeColumn = nodeColumn;
        }

        @Override
        public void visit(NodeId nodeId) {
            nodeColumn.setId(new Id());
        }

        @Override
        public void visit(EdgeId edgeId) {

        }

        @Override
        public <T> void visit(EdgePropertyValue<T> edgePropertyValue) {

        }

        @Override
        public <V> void visit(EdgeScriptExpression vEdgeScriptExpression) {

        }

        @Override
        public void visit(LabelValue labelValue) {
            Label label = new Label();
            label.setMatch(labelValue.getPattern().pattern());
            nodeColumn.setLabel(label);
        }

        @Override
        public <T> void visit(NodePropertyValue<T> tNodePropertyValue) {
            Property property = new Property();
            property.setKey(tNodePropertyValue.getKey());
            nodeColumn.setProperty(property);
        }

        @Override
        public <T> void visit(NodeScriptExpression<T> tNodeScriptExpression) {
            Expression expression = new Expression();
            expression.setExpression(tNodeScriptExpression.getScript());
            nodeColumn.setExpression(expression);
        }
    }

    private static class ToEdgeVisitor implements ValueExpressionVisitor {
        private EdgeColumn edgeColumn;
        public ToEdgeVisitor(EdgeColumn edgeColumn) {
            this.edgeColumn = edgeColumn;
        }

        @Override
        public void visit(NodeId nodeId) {

        }

        @Override
        public void visit(EdgeId edgeId) {
            edgeColumn.setId(new Id());
        }

        @Override
        public <T> void visit(EdgePropertyValue<T> edgePropertyValue) {
            Property property = new Property();
            property.setKey(edgePropertyValue.getKey());
            edgeColumn.setProperty(property);
        }

        @Override
        public <V> void visit(EdgeScriptExpression vEdgeScriptExpression) {
            Expression expression = new Expression();
            expression.setExpression(vEdgeScriptExpression.getScript());
            edgeColumn.setExpression(expression);

        }

        @Override
        public void visit(LabelValue labelValue) {

        }

        @Override
        public <T> void visit(NodePropertyValue<T> tNodePropertyValue) {
        }

        @Override
        public <T> void visit(NodeScriptExpression<T> tNodeScriptExpression) {
        }
    }
}
