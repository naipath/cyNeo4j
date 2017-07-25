package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.values.ValueExpression;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;

import java.util.regex.Pattern;

public class LabelValue implements ValueExpression<GraphNode, String> {

    Pattern pattern;

    public LabelValue(String matches) {
        pattern = Pattern.compile(matches);
    }

    @Override
    public String eval(GraphNode val) {
        return val.getLabels().stream()
                .filter(pattern.asPredicate())
                .findFirst()
                .orElse("");
    }
}
