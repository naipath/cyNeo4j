package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.ImportGraphTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.MapToNetworkStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.retrievedata.CopyToNetworkStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.writenetwork.WriteNetworkToNeo4jDataTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.CypherQuery;

public class CommandFactory {

    private final Services services;

    static CommandFactory create(Services services) {
        return new CommandFactory(services);
    }

    private CommandFactory(Services services) {
        this.services = services;
    }

    public ImportGraphTask createRetrieveDataTask() {
        CypherQuery cypherQuery = CypherQuery.builder().query("MATCH (n)-[r]->(m) RETURN n,r,m").build();
        return new ImportGraphTask(services, "Network", services.getVisualMappingManager().getDefaultVisualStyle().getTitle(), new CopyToNetworkStrategy(), cypherQuery);
    }
    public WriteNetworkToNeo4jDataTask createCopyDataTask() {
        return new WriteNetworkToNeo4jDataTask(services);
    }
    public ImportGraphTask createRetrieveDataFromQueryTemplateTask(String networkName, CypherQueryTemplate query, String visualStyle) {
        return new ImportGraphTask(services, networkName, visualStyle, new MapToNetworkStrategy(query.getMapping()),query.createQuery());
    }
    public ImportGraphTask createExecuteCypherQueryTask(String networkName, CypherQuery query, String visualStyle) {
        return new ImportGraphTask(services, networkName, visualStyle, new CopyToNetworkStrategy(), query);
    }
}
