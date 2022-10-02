package ru.otus.aop;

public class ProxyDemo {
    public static void main(String[] args) {
        TestLoggingInterface testLogging = Ioc.createMyClass();
        testLogging.calculation(1);
        testLogging.calculation("a");
        testLogging.calculation("b", "c");
    }
}



