package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j;

import java.util.HashMap;
import java.util.Map;

public class CypherQuery {

    private final String query;
    private final Map<String, String> params;
    private Map<String, Object> parameters;

    public CypherQuery(String query, Map<String, String> params) {
        this.query = query;
        this.params = params;
    }

    public String toJsonString() {
        //TODO: use library
        String jsonParams = params.entrySet().stream().map(this::paramEntryToString).reduce( (a,b) -> a + "," + b).orElse("");
        return "{ \"query\" : \""+ query +"\",\"params\" : {" + jsonParams + "}}";
    }

    private String paramEntryToString(Map.Entry<String, String> entry) {
        return "\"" + entry.getKey() + "\": " + entry.getValue();
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public String getQuery() {
        return query;
    }

    public static final class Builder {

        private String query;
        private Map<String,String> params;

        private Builder() {
            params = new HashMap<>();
        }

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder params(String param, String value) {
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
