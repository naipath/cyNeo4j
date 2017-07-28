package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "cytemplate")
@XmlAccessorType(XmlAccessType.FIELD)
class QueryTemplate {

    @XmlAttribute(name="name")
    private String name;

    @XmlElement(name="query")
    private String query;

    @XmlElement(name = "parameters")
    private Parameters parameters;

    @XmlElement(name = "mapping")
    private ColumnMapping mapping;


    public String getQuery() {
        return query;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public ColumnMapping getMapping() {
        return mapping;
    }

    public String getName() {
        return name;
    }
}
