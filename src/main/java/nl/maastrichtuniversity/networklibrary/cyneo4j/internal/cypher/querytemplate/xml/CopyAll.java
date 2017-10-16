package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class CopyAll extends Mapping {

    @XmlAttribute(name="referenceIdColumn")
    private String referenceIdColumn;

    @XmlAttribute(name="network")
    private String network;

    public String getReferenceIdColumn() {
        return referenceIdColumn;
    }

    public String getNetwork() {
        return network;
    }

    public void setReferenceIdColumn(String referenceIdColumn) {
        this.referenceIdColumn = referenceIdColumn;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    @Override
    void accept(MappingVisitor visitor) {
        visitor.visit(this);
    }
}
