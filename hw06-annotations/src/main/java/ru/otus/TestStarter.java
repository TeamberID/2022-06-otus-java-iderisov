package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestStarter {

    private final Class testClass;
    private final List<Method> setUpMethods = new ArrayList<>();
    private final List<Method> tearDownMethods = new ArrayList<>();
    private final List<Method> testMethods = new ArrayList<>();

    public TestStarter(String className) throws ClassNotFoundException {
        testClass = Class.forName(className);
    }

    private void prepare() {
        Arrays.stream(testClass.getDeclaredMethods()).forEach(method -> {
            if (method.isAnnotationPresent(Before.class)) {
                setUpMethods.add(method);
            } else if (method.isAnnotationPresent(After.class)) {
                tearDownMethods.add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        });
    }

    public void runTest() {

        prepare();

        int all = 0;
        int failed = 0;

        for (Method testMethod : testMethods) {
            all++;
            try {
                Object object = testClass.getDeclaredConstructor().newInstance();
                try {
                    startMethods(setUpMethods, object);
                    testMethod.invoke(object);
                } catch (Exception e) {
                    failed++;
                } finally {
                    startMethods(tearDownMethods, object);
                }
            } catch (Exception e) {
                System.err.println("Object creation occured error: " + e.getLocalizedMessage());
            }
        }

        printStatistics(all, failed);
    }

    private void printStatistics(int all, int failed) {
        System.out.println("All tests number: " + all);
        if (failed > 0)
            System.err.println("Failed tests number: " + failed);
    }

    private void startMethods(List<Method> methods, Object object) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            method.invoke(object);
        }
    }


}
