package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
class NodeColumn {

    @XmlAttribute(name = "type")
    private String type;

    @XmlAttribute(name = "name")
    private String name;

    @XmlElement(name = "property")
    Property property;

    @XmlElement(name = "id")
    Id id;

    @XmlElement(name = "expression")
    Expression expression;

    @XmlElement(name = "label")
    Label label;

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

    public Expression getExpression() {
        return expression;
    }

    public Label getLabel() {
        return label;
    }
}
