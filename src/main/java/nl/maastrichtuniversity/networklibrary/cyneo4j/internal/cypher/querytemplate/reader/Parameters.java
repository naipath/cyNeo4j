package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;


import javax.xml.bind.annotation.XmlElement;
import java.util.List;

class Parameters {

    @XmlElement(name="parameter")
    private List<Parameter> parameterList;

    public List<Parameter> getParameterList() {
        return parameterList;
    }
}
