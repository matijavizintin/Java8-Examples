package com.test.defaultmethods;

/**
 * Created by Matija Vižintin
 * Date: 21. 05. 2015
 * Time: 20.01
 */
public interface Vehicle {

    void car();

    default void bus() {
        System.out.println("Hello I'm a bus.");
    };

    static void plane() {
        System.out.println("Hello I'm a plane.");
    }
}
