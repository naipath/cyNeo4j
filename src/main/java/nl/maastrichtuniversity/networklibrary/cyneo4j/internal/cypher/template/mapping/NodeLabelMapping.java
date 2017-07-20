package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.mapping;

import java.util.regex.Pattern;

public class NodeLabelMapping {

    private final Pattern pattern;
    private final String columnName;

    public NodeLabelMapping(Pattern pattern, String columnName) {
        this.pattern = pattern;
        this.columnName = columnName;
    }

    public boolean matches(String label) {
        return pattern.matcher(label).matches();
    }

    public String getColumnName() {
        return columnName;
    }
}
