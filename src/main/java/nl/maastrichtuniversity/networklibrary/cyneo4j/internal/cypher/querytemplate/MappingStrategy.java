package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate;

public interface MappingStrategy {
    void accept(MappingStrategyVisitor visitor);
}
