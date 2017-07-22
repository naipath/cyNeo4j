package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CyQueryProperty {

    @XmlAttribute(name = "name")
    private String name;

    @XmlElement(name = "column")
    private CyQueryColumn column;

    public String getName() {
        return name;
    }

    public CyQueryColumn getColumn() {
        return column;
    }
}
