package ru.otus.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

class Ioc {

    private Ioc() {
    }

    static TestLoggingInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;
        private final HashSet<Method> logMethods = new HashSet<>();

        DemoInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
            logMethods.addAll(Arrays.stream(myClass.getClass().getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(Log.class))
                    .map(this::getInterfaceMethod).toList());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (logMethods.contains(method)) {
                System.err.println("executed method: " + method.getName() + ", param: " +
                        Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", ")));
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }

        private Method getInterfaceMethod(Method method) {
            try {
                return TestLoggingInterface.class.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
