package com.test;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 13.57
 */
public class ForkJoinTest {

    @Test
    public void poolTest() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        System.out.println("No of threads: " + pool.getParallelism());      // expected: no of (virtual) cores - 1
    }
}
