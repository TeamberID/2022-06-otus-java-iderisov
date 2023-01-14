package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import javax.naming.ConfigurationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        checkConfigClass(configClass);

        var configInstance = configClass.getConstructor().newInstance();

        var methods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order())).toList();

        for (Method method : methods) {
            String name = method.getAnnotation(AppComponent.class).name();
            if (appComponentsByName.containsKey(name)) {
                throw new IllegalArgumentException("Not unique bean name!");
            }

            var parameters = Arrays.stream(method.getParameterTypes()).map(parType -> getAppComponent(parType)).toList();
            var bean = method.invoke(configInstance, parameters.toArray());

            appComponentsByName.put(name, bean);
            appComponents.add(bean);

        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) throws IllegalArgumentException {

        if (appComponents.stream().filter(comp -> comp.getClass().equals(componentClass) ||
                Arrays.stream(comp.getClass().getInterfaces()).anyMatch(c -> c.equals(componentClass))).count() > 1)
            throw new IllegalArgumentException("Not unique bean");

        for (Object component : appComponents) {
            if (Arrays.stream(component.getClass().getInterfaces()).anyMatch(inter -> inter == componentClass)) {
                return (C) component;
            }
            if (component.getClass() == componentClass) {
                return (C) component;
            }
        }

        throw new IllegalArgumentException("Bean not found");
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (!appComponentsByName.containsKey(componentName)) {
            throw new IllegalArgumentException("Bean not found");
        }
        return (C) appComponentsByName.get(componentName);
    }
}
