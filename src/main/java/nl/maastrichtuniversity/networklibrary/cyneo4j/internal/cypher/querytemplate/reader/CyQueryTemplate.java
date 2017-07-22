package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "cytemplate")
@XmlAccessorType(XmlAccessType.FIELD)
public class CyQueryTemplate {

    @XmlAttribute(name="name")
    private String name;

    @XmlElement(name="query")
    private String query;

    @XmlElement(name = "parameters")
    private CyQueryParameters parameters;

    @XmlElement(name = "mapping")
    private CyQueryMapping mapping;


    public String getQuery() {
        return query;
    }

    public CyQueryParameters getParameters() {
        return parameters;
    }

    public CyQueryMapping getMapping() {
        return mapping;
    }

    public String getName() {
        return name;
    }
}
