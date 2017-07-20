package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.mapping;

public class NodeMapping<T>  {
    private String columnName;
    private Class<T> columnType;

    public NodeMapping(String columnName, Class<T> columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
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
