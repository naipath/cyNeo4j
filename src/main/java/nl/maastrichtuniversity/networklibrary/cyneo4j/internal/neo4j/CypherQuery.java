package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class CypherQuery {

    private final String query;
    private final Map<String, Object> params;

    CypherQuery(String query, Map<String, Object> params) {
        this.query = query;
        this.params = params;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String toJsonString() {
        String jsonParams = params.entrySet().stream()
                .map(this::paramEntryToString)
                .collect(joining(","));
        return "{ \"query\" : \"" + query + "\",\"params\" : {" + jsonParams + "}}";
    }

    private String paramEntryToString(Map.Entry<String, Object> entry) {
        return "\"" + entry.getKey() + "\": " + entry.getValue();
    }

    public String getQuery() {
        return query;
    }

    public static final class Builder {

        private String query;
        private Map<String, Object> params;

        private Builder() {
            params = new HashMap<>();
        }

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder params(String param, Object value) {
            this.params.put(param, value);
            return this;
        }


        public CypherQuery build() {
            return new CypherQuery(query, params);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
