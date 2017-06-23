package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.retrievedata.RetrieveDataTask;

public class CommandFactory {

    private final ServiceLocator serviceLocator;


    public CommandFactory(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public RetrieveDataTask createRetrieveDataTask() {
        return new RetrieveDataTask(serviceLocator);
    }

    public static CommandFactory create(ServiceLocator serviceLocator) {
        return new CommandFactory(serviceLocator);
    }
}
