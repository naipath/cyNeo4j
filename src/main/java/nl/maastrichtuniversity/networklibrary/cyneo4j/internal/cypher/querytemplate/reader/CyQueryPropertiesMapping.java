package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class CyQueryPropertiesMapping {

    @XmlElement( name = "property" )
    private List<Property> propertyList;

    public List<Property> getPropertyList() {
        return propertyList;
    }
}
