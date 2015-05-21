package com.test.defaultmethods;

/**
 * Created by Matija Vižintin
 * Date: 08. 05. 2015
 * Time: 14.12
 */
public class OverridingClass implements DefaultInterface, DefaultInterface2 {
    @Override public void method1() {
        System.out.println("Overridden method.");
    }

    @Override public void method2() {
        DefaultInterface.super.method2();
    }

    @Override public void method3() {
        DefaultInterface2.super.method3();
    }
}
