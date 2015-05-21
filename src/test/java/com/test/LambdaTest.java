package com.test;

import com.test.generators.DataGenerator;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Created by Matija Vižintin
 * Date: 21. 05. 2015
 * Time: 20.08
 */
public class LambdaTest {

    /**
     * Simple test that shows lambda "evolution". It shows how code can be shortened (and made more elegant) using lambda expressions.
     */
    @Test
    public void evolution() {
        List<Integer> list = DataGenerator.integers(10);

        // lambda in full
        Collections.sort(
                list, (Integer o1, Integer o2) -> {
            return o1.compareTo(o2);
        });

        // lambda shorter
        Collections.sort(list, (Integer o1, Integer o2) -> o1.compareTo(o2));

        // lambda shortest
        Collections.sort(list, (o1, o2) -> o1.compareTo(o2));

        // method reference
        Collections.sort(list, Integer::compareTo);
    }
}
