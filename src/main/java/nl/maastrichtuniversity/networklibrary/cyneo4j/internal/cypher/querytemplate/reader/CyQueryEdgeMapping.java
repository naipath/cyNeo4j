package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import javax.xml.bind.annotation.XmlElement;

public class CyQueryEdgeMapping {
    @XmlElement(name = "referenceId")
    private CyQueryReferenceIdMapping referenceId;

    @XmlElement(name = "linkType")
    private CyQueryLinkTypeMapping linkType;

    @XmlElement(name = "interaction")
    private CyQueryInteraction interaction;

    @XmlElement(name = "properties")
    private CyQueryPropertiesMapping properties;

    public CyQueryReferenceIdMapping getReferenceId() {
        return referenceId;
    }

    public CyQueryLinkTypeMapping getLinkType() {
        return linkType;
    }

    public CyQueryInteraction getInteraction() {
        return interaction;
    }

    public CyQueryPropertiesMapping getProperties() {
        return properties;
    }
}
