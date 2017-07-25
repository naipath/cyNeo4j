package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.writenetwork.WriteNetworkToNeo4jDataTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.retrievedata.RetrieveDataTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.ImportQueryTemplateTask;

public class CommandFactory {

    private final Services services;

    private CommandFactory(Services services) {
        this.services = services;
    }

    public RetrieveDataTask createRetrieveDataTask() {
        return new RetrieveDataTask(services);
    }

    static CommandFactory create(Services services) {
        return new CommandFactory(services);
    }

    public WriteNetworkToNeo4jDataTask createCopyDataTask() {
        return new WriteNetworkToNeo4jDataTask(services);
    }

    public ImportQueryTemplateTask createRetrieveDataFromQueryTemplateTask(String networkName, CypherQueryTemplate query, String visualStyle) {
        return new ImportQueryTemplateTask(services, networkName, visualStyle, query);
    }
}
