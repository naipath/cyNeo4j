package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
class EdgeColumn {

    @XmlAttribute(name = "type")
    private String type;

    @XmlAttribute(name = "name")
    private String name;

    @XmlElement(name = "property")
    private Property property;

    @XmlElement(name = "id")
    private Id id;

    @XmlElement(name = "edgetype")
    private EdgeType edgeType;

    @XmlElement(name = "expression")
    private Expression expression;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Property getProperty() {
        return property;
    }

    public Id getId() {
        return id;
    }

    public EdgeType getEdgeType() {
        return edgeType;
    }

    public Expression getExpression() {
        return expression;
    }
}
