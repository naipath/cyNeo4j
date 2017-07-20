package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CyQueryNodeMapping {

    @XmlElement(name = "referenceId")
    private CyQueryReferenceIdMapping referenceId;

    @XmlElement(name = "properties")
    private CyQueryPropertiesMapping properties;

    @XmlElement(name = "labels")
    private CyQueryLabelMapping labels;

    public CyQueryReferenceIdMapping getReferenceId() {
        return referenceId;
    }

    public CyQueryPropertiesMapping getProperties() {
        return properties;
    }

    public CyQueryLabelMapping getLabels() {
        return labels;
    }
}
