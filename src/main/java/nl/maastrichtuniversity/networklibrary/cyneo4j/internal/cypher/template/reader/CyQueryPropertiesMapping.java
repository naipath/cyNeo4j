package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class CyQueryPropertiesMapping {

    @XmlElement( name = "property" )
    private List<CyQueryProperty> propertyList;

    public List<CyQueryProperty> getPropertyList() {
        return propertyList;
    }
}
