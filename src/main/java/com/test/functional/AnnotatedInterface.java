package com.test.functional;

/**
 * Created by Matija Vižintin
 * Date: 21. 05. 2015
 * Time: 20.22
 */
@FunctionalInterface
public interface AnnotatedInterface {
    void implementMe();

    default void implemented() {

    }
}
