package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.GraphMapping;

public interface MappingStrategyVisitor {
    void visit(GraphMapping graphMapping);
    void visit(CopyAllMappingStrategy copyAllMappingStrategy);
}
