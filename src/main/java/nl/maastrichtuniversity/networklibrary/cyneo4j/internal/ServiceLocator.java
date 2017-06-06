package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steven on 04-06-17.
 */
public class ServiceLocator {

    private final Map<Class<?>,Object> services;

    public ServiceLocator() {
        services = new HashMap<>();
    }

    public <T> void register(T instance) {
        services.put(instance.getClass(), instance);
    }

    public <T> T getService(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }
}
