package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CyQueryReferenceIdMapping {
    @XmlElement(name = "column")
    private CyQueryColumn column;

    public CyQueryColumn getColumn() {
        return column;
    }
}
