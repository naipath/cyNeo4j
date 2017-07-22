package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;


import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class CyQueryParameters {

    @XmlElement(name="parameter")
    private List<CyQueryParameter> parameterList;

    public List<CyQueryParameter> getParameterList() {
        return parameterList;
    }
}
