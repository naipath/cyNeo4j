package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {

    private final Map<Class<?>,Object> services;

    public ServiceLocator() {
        services = new HashMap<>();
    }

    public <T> void register(Class<T> clz, T instance) {
        services.put(clz, instance);
    }

    public <T> void register(T instance) {
        services.put(instance.getClass(), instance);
    }


    public <T> T getService(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }
}
