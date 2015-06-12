package com.test.lambda;

import com.test.functional.Adder;
import com.test.generators.DataGenerator;
import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Created by Matija Vižintin
 * Date: 21. 05. 2015
 * Time: 20.08
 */
public class LambdaTest extends LoggingTimedTest {

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

    /**
     * Test shows that variables used in lambdas doesn't have to be explicitly final but have to be effectively final.
     *
     * If the commented line of code is uncommented, variable base won't be final any more and code won't compile.
     */
    @Test
    public void variableScopes() {
        int base = 10;          // base doesn't have to be explicitly final
        Adder adder = increment -> base + increment;

        // but it has to be effectively final           NOTE: if uncommented, code won't compile
        //base = 15;

        // add
        int increment = 5;
        int result = adder.add(increment);
        System.out.printf("%d + %s = %d", base, increment, result);
    }

    static int staticGlobalInt;
    int globalInt;
    /**
     * Method shows that with global variables we have read and write access.
     */
    @Test
    public void globalVariablesScope() {
        Adder adder = increment -> {
            staticGlobalInt += increment;
            globalInt += increment;
            return staticGlobalInt;
        };

        // set global variable
        staticGlobalInt = 10;
        globalInt = 10;

        // add
        int increment = 5;
        int result = adder.add(increment);
        System.out.printf("StaticGlobalInt: %d, GlobalInt: %d, Result: %d", staticGlobalInt, globalInt, result);

        // assert
        Assert.assertEquals("Values are expected to be equals.", result, staticGlobalInt);
        Assert.assertEquals("Values are expected to be equals.", result, globalInt);
    }
}
