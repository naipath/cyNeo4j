package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.xml;

public interface MappingVisitor {
    void visit(ColumnMapping columnMapping);
    void visit(CopyAll copyAll);
}
