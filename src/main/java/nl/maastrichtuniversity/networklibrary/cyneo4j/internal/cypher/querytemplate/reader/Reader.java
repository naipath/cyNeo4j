package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

public class Reader {

    private static final JAXBContext jaxbContext = createJaxbContext();

    private static JAXBContext createJaxbContext() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(QueryTemplate.class);
            return jaxbContext;
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot create jaxb context for reading cyNeo4j query template files");
        }
    }

    public CypherQueryTemplate read(InputStream inputStream) throws ReaderException {
        try {
            StreamSource source = new StreamSource(inputStream);
            JAXBElement<QueryTemplate> jaxbElement = jaxbContext.createUnmarshaller().unmarshal(source, QueryTemplate.class);
            CypherQueryTemplate queryTemplate = create(jaxbElement.getValue());
            return queryTemplate;

        } catch (JAXBException e) {
            throw new ReaderException("Failed processing template", e);
        }
        catch (InternalReaderException e) {
            throw new ReaderException(e.getMessage());
        }
    }

    private CypherQueryTemplate create(QueryTemplate xml) {

        CypherQueryTemplate.Builder builder = CypherQueryTemplate.builder();

        if(xml.getName() == null) {
            throw new InternalReaderException("Name is a required attribute");
        }
        builder.setName(xml.getName());
        builder.setQueryTemplate(xml.getQuery());

        xml.getParameters().getParameterList().forEach(cyQueryParameter -> {
            Class<?> type = getType(cyQueryParameter.getType());
            builder.addParameter(cyQueryParameter.getName(), type);
        });

        builder.setNodeReferenceIdColumn(xml.getMapping().getNodeMapping().getReferenceIdColumn());

        xml.getMapping().getNodeMapping().getColumnList().forEach(column -> {
            Class<?> columnType = getType(column.getType());
            if(column.getId() != null) {
                builder.addNodeColumnMapping(column.getName(), Long.class, new NodeId());
            }
            if(column.getProperty() != null) {
                builder.addNodeColumnMapping(
                        column.getName(),
                        columnType,
                        new NodePropertyValue(column.getProperty().getKey(), columnType));
            }
            if(column.getLabel() != null) {
                builder.addNodeColumnMapping(column.getName(), String.class, new LabelValue(column.getLabel().getMatch()));
            }
            if(column.getExpression() != null) {
                builder.addNodeColumnMapping(
                        column.getName(),
                        columnType,
                        new NodeScriptExpression(column.getExpression().getExpression(),columnType));
            }
        });

        builder.setEdgeReferenceIdColumn(xml.getMapping().getEdgeMapping().getReferenceIdColumn());

        xml.getMapping().getEdgeMapping().getColumnList().forEach(column -> {
            Class<?> columnType = getType(column.getType());
            if(column.getId() != null) {
                builder.addEdgeColumnMapping(column.getName(), Long.class, new EdgeId());;
            }
            if(column.getProperty() != null) {
                builder.addEdgeColumnMapping(
                        column.getName(),
                        columnType,
                        new EdgePropertyValue(column.getProperty().getKey(), columnType));
            }
            if(column.getExpression() != null) {
                builder.addEdgeColumnMapping(
                        column.getName(),
                        columnType,
                        new EdgeScriptExpression(column.getExpression().getExpression(), columnType));
            }
        });
        return builder.build();
    }

    private Class<?> getType(String type) {
        try {
            return ClassLoader.getSystemClassLoader().loadClass(type);
        } catch (ClassNotFoundException e) {
            throw new InternalReaderException("Cannot load type: " + type);
        }
    }
}
