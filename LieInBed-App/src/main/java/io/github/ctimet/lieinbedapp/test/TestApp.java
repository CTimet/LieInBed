package io.github.ctimet.lieinbedapp.test;

import java.lang.reflect.Field;

public class TestApp {
    public static void main(String[] args) throws Exception {
        Class<Test> cl = Test.class;
        Field field = cl.getDeclaredField("a");
        field.setAccessible(true);
        field.set(null, "2");
        System.out.println(field.get(null));
    }
}

class Test {
    private static final String a = "1";
}
