package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class CyQueryColumn {

    @XmlAttribute(name = "type")
    private String type;

    @XmlAttribute(name = "name")
    private String name;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
