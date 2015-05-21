package com.test;

import com.test.functional.AnnotatedInterface;
import com.test.functional.UnAnnotatedInterface;
import org.junit.Test;

/**
 * Created by Matija Vižintin
 * Date: 21. 05. 2015
 * Time: 20.23
 */
public class FunctionalInterfaceTest {

    /**
     * Testing functional interfaces, one annotated as @FunctionalInterface, the other not. Both works the same way, the difference is that if we want
     * to add another abstract method to a @FunctionalInterface the compiler throws an error.
     */
    @Test
    public void testInterfaces() {
        // functional
        AnnotatedInterface ai = () -> System.out.println("Hello annotated");

        // non-functional
        UnAnnotatedInterface ui = () -> System.out.println("Hello unannotated");
    }

    /**
     * Implement and start a new thread in a single row.
     */
    @Test
    public void usefulExample() {
        new Thread(() -> System.out.println("Say hello in new thread named: " + Thread.currentThread().getName())).start();
    }
}
