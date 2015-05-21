package com.test;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Matija Vižintin
 * Date: 21. 05. 2015
 * Time: 20.08
 */
public class LambdaTest {

    @Test public void test1() {
        List<Integer> list = IntStream.range(1, 10).boxed().collect(Collectors.toList());

        // lambda 1
        Collections.sort(
                list, (Integer o1, Integer o2) -> {
            return o1.compareTo(o2);
        });

        // lambda 2
        Collections.sort(list, (Integer o1, Integer o2) -> o1.compareTo(o2));

        // lambda 3
        Collections.sort(list, (o1, o2) -> o1.compareTo(o2));

        // method reference
        Collections.sort(list, Integer::compareTo);
    }
}
