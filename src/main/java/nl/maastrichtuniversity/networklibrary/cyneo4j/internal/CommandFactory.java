package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.task.importgraph.ImportGraphTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.task.importgraph.MappingImportStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.task.importgraph.CopyAllImportStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.task.exportnetwork.ExportNetworkToNeo4jTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;

public class CommandFactory {

    private final Services services;

    static CommandFactory create(Services services) {
        return new CommandFactory(services);
    }

    private CommandFactory(Services services) {
        this.services = services;
    }

    public ImportGraphTask createImportGraphTask() {
        CypherQuery cypherQuery = CypherQuery.builder().query("MATCH (n)-[r]->(m) RETURN n,r,m").build();
        return new ImportGraphTask(
                services,
                "Network",
                services.getVisualMappingManager().getDefaultVisualStyle().getTitle(),
                new CopyAllImportStrategy(),
                cypherQuery);
    }
    public ExportNetworkToNeo4jTask createExportNetworkToNeo4jTask() {
        return new ExportNetworkToNeo4jTask(services);
    }

    public ImportGraphTask createRetrieveDataFromQueryTemplateTask(String networkName, CypherQueryTemplate query, String visualStyle) {
        return new ImportGraphTask(services, networkName, visualStyle, new MappingImportStrategy(query.getMapping()),query.createQuery());
    }
    public ImportGraphTask createExecuteCypherQueryTask(String networkName, CypherQuery query, String visualStyle) {
        return new ImportGraphTask(services, networkName, visualStyle, new CopyAllImportStrategy(), query);
    }
}
