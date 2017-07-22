package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping;

public class EdgeMapping<T> {
    private String columnName;
    private Class<T> columnType;

    public EdgeMapping(String columnName, Class<T> columnType) {
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
