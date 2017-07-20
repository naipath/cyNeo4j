package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.reader;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.CypherQueryTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

public class CypherQueryTemplateReader {

    private static final JAXBContext jaxbContext = createJaxbContext();

    private static JAXBContext createJaxbContext() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CyQueryTemplate.class);
            return jaxbContext;
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot create jaxb context for reading cyNeo4j query template files");
        }
    }

    public CypherQueryTemplate read(InputStream inputStream) throws CypherQueryTemplateReaderException {
        try {
            StreamSource source = new StreamSource(inputStream);
            JAXBElement<CyQueryTemplate> jaxbElement = jaxbContext.createUnmarshaller().unmarshal(source, CyQueryTemplate.class);
            CypherQueryTemplate queryTemplate = create(jaxbElement.getValue());
            return queryTemplate;

        } catch (JAXBException e) {
            throw new CypherQueryTemplateReaderException("Failed processing template");
        }
        catch (InternalCypherQueryReaderException e) {
            throw new CypherQueryTemplateReaderException(e.getMessage());
        }
    }

    private CypherQueryTemplate create(CyQueryTemplate xml) {
        CypherQueryTemplate.Builder builder = CypherQueryTemplate.builder();

        if(xml.getName() == null) {
            throw new InternalCypherQueryReaderException("Name is a required attribute");
        }
        builder.setName(xml.getName());
        builder.setQueryTemplate(xml.getQuery());

        xml.getParameters().getParameterList().forEach(cyQueryParameter -> {
            Class<?> type = getType(cyQueryParameter.getType());
            builder.addParameter(cyQueryParameter.getName(), type);
        });

        xml.getMapping().getNodeMapping().getProperties().getPropertyList().forEach(cyQueryProperty -> {
            builder.addNodePropertyMapping(
                    cyQueryProperty.getName(),
                    cyQueryProperty.getColumn().getName(),
                    getType(cyQueryProperty.getColumn().getType())
            );
        });

        xml.getMapping().getNodeMapping().getLabels().getLabels().forEach(cyQueryLabel -> {
            //TODO: implement
            //builder.addNodeLabelMapping(cyQueryLabel.getMatch(), );
        });
        CyQueryColumn nodeRefrenceIdColumn = xml.getMapping().getNodeMapping().getReferenceId().getColumn();
        builder.addNodeReferenceIdMapping(nodeRefrenceIdColumn.getName(), getType(nodeRefrenceIdColumn.getType()));

        if(xml.getMapping().getEdgeMapping().getProperties()!= null && xml.getMapping().getEdgeMapping().getProperties().getPropertyList() != null) {
            xml.getMapping().getEdgeMapping().getProperties().getPropertyList().forEach(cyQueryProperty -> {
                builder.addEdgePropertyMapping(
                        cyQueryProperty.getName(),
                        cyQueryProperty.getColumn().getName(),
                        getType(cyQueryProperty.getColumn().getType())
                );
            });
        }

        builder.addInteractionMapping(xml.getMapping().getEdgeMapping().getInteraction().name());

        CyQueryColumn edgeLinkTypeMapping = xml.getMapping().getEdgeMapping().getLinkType().getColumn();
        builder.addEdgeLinkTypeMapping(edgeLinkTypeMapping.getName(), getType(edgeLinkTypeMapping.getType()));

        CyQueryColumn edgeReferenceId  = xml.getMapping().getEdgeMapping().getReferenceId().getColumn();
        builder.addEdgeReferenceIdMapping(edgeReferenceId.getName(), getType(edgeReferenceId.getType()));

        return builder.build();
    }

    private Class<?> getType(String type) {
        try {
            return ClassLoader.getSystemClassLoader().loadClass(type);
        } catch (ClassNotFoundException e) {
            throw new InternalCypherQueryReaderException("Cannot load type: " + type);
        }
    }
}
