package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping;

public interface ValueExpression<T,V> {
    V eval(T val);
}
