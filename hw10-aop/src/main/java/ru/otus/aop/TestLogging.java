package ru.otus.aop;

class TestLogging implements TestLoggingInterface {

    @Log
    public void calculation(int param) {
        System.out.println("calculate " + param);
    }

    @Log
    @Override
    public void calculation(String param) {
        System.out.println("calculate " + param);
    }

    @Log
    @Override
    public void calculation(String param, String param2) {
        System.out.println("calculate " + param + " " + param2);
    }


}