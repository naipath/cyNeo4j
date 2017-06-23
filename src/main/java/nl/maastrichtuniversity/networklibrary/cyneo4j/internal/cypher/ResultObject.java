package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher;

import java.util.Map;

public class ResultObject {

    private ResType resType;
    private Map<String, Object> properties;
    private Long start;
    private Long end;
    private String type;

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    ResType getResType() {
        return resType;
    }

    Map<String, Object> getNodeProperties() {
        return properties;
    }

    public void setResType(ResType resType) {
        this.resType = resType;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }

    public String getType() {
        return type;
    }
}
