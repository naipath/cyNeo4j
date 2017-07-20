package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class CyQueryLabelMapping {

    @XmlElement(name="label")
    private List<CyQueryLabel> labels;

    public List<CyQueryLabel> getLabels() {
        return labels;
    }
}
