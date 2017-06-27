package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.CopyDataTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.RetrieveDataTask;

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
}
