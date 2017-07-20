package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.CopyDataTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.RetrieveDataTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.gff.RetrieveGenomeDataTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.RetrieveDataFromQueryTemplateTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery.GeneQuery;

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

    public CopyDataTask createCopyDataTask() {
        return new CopyDataTask(services);
    }

    public RetrieveGenomeDataTask createRetrieveGenomeDataTask(String networkName, GeneQuery geneQuery, String visualStyleTitle) {
        return new RetrieveGenomeDataTask(services, networkName, visualStyleTitle, geneQuery);
    }

    public RetrieveDataFromQueryTemplateTask createRetrieveDataFromQueryTemplateTask(String networkName, CypherQueryTemplate query, String visualStyle) {
        return new RetrieveDataFromQueryTemplateTask(services, networkName, visualStyle, query);
    }
}
