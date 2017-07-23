package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.GraphNode;

public class NodeMapping<T>  {
    private String columnName;
    private Class<T> columnType;
    private ValueExpression<GraphNode, T> valueExpression;

    public NodeMapping(String columnName, Class<T> columnType, ValueExpression<GraphNode, T> valueExpression) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.valueExpression = valueExpression;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Class<?> getColumnType() {
        return columnType;
    }

    public void setColumnType(Class<T> columnType) {
        this.columnType = columnType;
    }

    public T asValue(Object value) {
        return columnType.cast(value);
    }
}
