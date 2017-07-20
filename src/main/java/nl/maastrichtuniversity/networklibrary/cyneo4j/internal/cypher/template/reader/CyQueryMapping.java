package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CyQueryMapping {

    @XmlElement(name = "node")
    private CyQueryNodeMapping nodeMapping;

    @XmlElement(name = "edge")
    private CyQueryEdgeMapping edgeMapping;

    public CyQueryNodeMapping getNodeMapping() {
        return nodeMapping;
    }

    public CyQueryEdgeMapping getEdgeMapping() {
        return edgeMapping;
    }


}
