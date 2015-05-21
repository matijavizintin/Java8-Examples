package com.test;

import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matija Vižintin
 * Date: 14. 05. 2015
 * Time: 20.39
 */
public class ParallelArraysTest {

    @Test
    public void test1() {
        System.out.println("Start");
        long[] array = new long[1000 * 1000 * 100];
        Arrays.parallelSetAll(array, value -> ThreadLocalRandom.current().nextLong());
        //Arrays.stream(array).limit(1000).forEach(value -> System.out.print(value + " "));
        System.out.println("Generated");

        Stopwatch sw = Stopwatch.createStarted();
        Arrays.parallelSort(array);
        System.out.println("stopwatch: " + sw.elapsed(TimeUnit.MILLISECONDS));

        array = new long[1000 * 1000 * 100];
        Arrays.parallelSetAll(array, value -> ThreadLocalRandom.current().nextLong());
        sw = Stopwatch.createStarted();
        Arrays.sort(array);
        System.out.println("stopwatch: " + sw.elapsed(TimeUnit.MILLISECONDS));
        //Arrays.stream(array).limit(1000).forEach(value -> System.out.print(value + " "));

    }
}
