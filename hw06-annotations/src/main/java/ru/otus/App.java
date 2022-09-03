package ru.otus;

import java.lang.reflect.InvocationTargetException;

public class App {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        TestStarter starter = new TestStarter("ru.otus.MyTest");
        starter.runTest();
    }

}
