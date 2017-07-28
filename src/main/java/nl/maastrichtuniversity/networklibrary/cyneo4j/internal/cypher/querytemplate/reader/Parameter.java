package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
class Parameter {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String type;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
