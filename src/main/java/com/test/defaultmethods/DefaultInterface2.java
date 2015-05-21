package com.test.defaultmethods;

/**
 * Created by Matija Vižintin
 * Date: 08. 05. 2015
 * Time: 14.11
 */
public interface DefaultInterface2 {
    public final int a = 10;

    default public void method1() {
        System.out.println("Default implementation on interface 2");
    }

    default void method2() {
        System.out.println("DI2 method 2");
    }

    default void method3() {
        System.out.println("DI2 method 3");
    }
}
