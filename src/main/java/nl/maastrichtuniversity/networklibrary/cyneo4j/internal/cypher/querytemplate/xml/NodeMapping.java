package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.xml;

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

    public void setReferenceIdColumn(String referenceIdColumn) {
        this.referenceIdColumn = referenceIdColumn;
    }

    public void setColumnList(List<NodeColumn> columnList) {
        this.columnList = columnList;
    }

    public List<NodeColumn> getColumnList() {
        return columnList;
    }

    public String getReferenceIdColumn() {
        return referenceIdColumn;
    }
}
