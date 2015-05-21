package com.test;

import com.test.hierarchy.Inner;
import com.test.hierarchy.InnerInner;
import com.test.hierarchy.InnerInnerInner;
import com.test.hierarchy.Outer;
import org.junit.Test;

import java.util.Optional;

/**
 * Created by Matija Vižintin
 * Date: 09. 05. 2015
 * Time: 10.04
 */
public class OptionalTest {

    @Test
    public void test1() {
        optionalTest(null);
        optionalTest("Abcd");
    }

    @Test
    public void nullChecking() {
        InnerInnerInner innerInnerInner = new InnerInnerInner("abc");
        InnerInner innerInner = new InnerInner(innerInnerInner);
        Inner inner = new Inner(innerInner);
        Outer outer = new Outer(inner);

        Optional.ofNullable(outer)
                .flatMap(outer1 -> Optional.ofNullable(outer1.getInner()))
                .flatMap(inner1 -> Optional.ofNullable(inner1.getInnerInner()))
                .flatMap(innerInner1 -> Optional.ofNullable(innerInner1.getInnerInnerInner()))
                .flatMap(innerInnerInner1 -> Optional.ofNullable(innerInnerInner1.getString()))
                .ifPresent(System.out::print);
    }

    private void optionalTest(String parameter) {
        Optional<String> whatever = Optional.ofNullable(parameter);
        System.out.println("whatever.isPresent() = " + whatever.isPresent());
        System.out.println("whatever.orElseGet() = " + whatever.orElseGet(() -> "Empty Value"));
        System.out.println("whatever.map() = " + whatever.map(s -> "Mapped " + s).orElse("Else value"));
    }
}
