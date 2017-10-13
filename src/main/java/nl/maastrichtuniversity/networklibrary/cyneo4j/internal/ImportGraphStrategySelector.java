package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CopyAllMappingStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.MappingStrategyVisitor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.GraphMapping;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.task.importgraph.CopyAllImportStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.task.importgraph.ImportGraphStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.task.importgraph.MappingImportStrategy;

public class ImportGraphStrategySelector implements MappingStrategyVisitor{
    private ImportGraphStrategy importGraphStrategy;

    @Override
    public void visit(GraphMapping graphMapping) {
        importGraphStrategy = new MappingImportStrategy(graphMapping);
    }

    @Override
    public void visit(CopyAllMappingStrategy copyAllMappingStrategy) {
        importGraphStrategy = new CopyAllImportStrategy();
    }

    public ImportGraphStrategy getImportGraphStrategy() {
        return importGraphStrategy;
    }
}
