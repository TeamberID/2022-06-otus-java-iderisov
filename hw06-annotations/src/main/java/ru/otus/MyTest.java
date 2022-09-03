package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class MyTest {

    public MyTest() {
    }

    @Before
    public void setUp() {
        System.out.println("setUp");
    }

    @After
    public void tearDown() {
        System.out.println("teardown");
    }

    @Test
    public void testA() {
        System.out.println("test A");
    }

    @Test
    public void testB() {
        System.out.println("test B");
    }

    @Test
    public void testExcpetion() {
        throw new RuntimeException("RuntimeException");
    }


}
