package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.xml;


import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

class Parameters {

    @XmlElement(name="parameter")
    private List<Parameter> parameterList = new ArrayList<>();

    public List<Parameter> getParameterList() {
        return parameterList;
    }
}
