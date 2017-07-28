package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
class ColumnMapping {

    @XmlElement(name = "node")
    private NodeMapping nodeMapping;

    @XmlElement(name = "edge")
    private EdgeMapping edgeMapping;

    public NodeMapping getNodeMapping() {
        return nodeMapping;
    }

    public EdgeMapping getEdgeMapping() {
        return edgeMapping;
    }


}
