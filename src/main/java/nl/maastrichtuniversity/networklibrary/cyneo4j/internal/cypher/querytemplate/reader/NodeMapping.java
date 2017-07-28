package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
class NodeMapping {

    @XmlAttribute(name="referenceIdColumn")
    private String referenceIdColumn;

    @XmlElement(name = "column")
    private List<NodeColumn> columnList;

    public List<NodeColumn> getColumnList() {
        return columnList;
    }

    public String getReferenceIdColumn() {
        return referenceIdColumn;
    }
}
