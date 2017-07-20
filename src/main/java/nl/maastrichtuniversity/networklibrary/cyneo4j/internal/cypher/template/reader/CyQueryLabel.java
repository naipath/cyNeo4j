package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class CyQueryLabel {

    @XmlAttribute(name="match")
    private String match;

    @XmlElement(name="column")
    private List<CyQueryColumn> columnList;

    public String getMatch() {
        return match;
    }

    public List<CyQueryColumn> getColumnList() {
        return columnList;
    }
}
