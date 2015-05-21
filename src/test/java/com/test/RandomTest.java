package com.test;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.time.Clock;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by Matija Vižintin
 * Date: 05. 05. 2015
 * Time: 17.06
 */
public class RandomTest {

    public int THREADS = 50;
    public int LOOP = 1000 * 1000;

    @Test
    public void test1() {
        LongAccumulator accumulator = new LongAccumulator((left, right) -> left + right, 0L);
        long start = Clock.systemUTC().millis();
        List<Thread> threads = Lists.newArrayList();
        for (int i = 0; i < THREADS; i++) {
            Thread t = new Thread(() -> {for (int j = 0; j < LOOP; j++) accumulator.accumulate(1L);});
            threads.add(t);
            t.start();
        }

        while (true) {
            boolean allC = true;
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    allC = false;
                    break;
                }
            }
            if (allC) {
                System.out.println("res = " + accumulator.longValue());
                long stop = Clock.systemUTC().millis();
                System.out.println("ACCUMULATOR = " + (stop - start));
                break;
            }
        }
    }

    @Test
    public void test3() {
        LongAdder accumulator = new LongAdder();
        long start = Clock.systemUTC().millis();
        List<Thread> threads = Lists.newArrayList();
        for (int i = 0; i < THREADS; i++) {
            Thread t = new Thread(() -> {for (int j = 0; j < LOOP; j++) accumulator.increment();});
            threads.add(t);
            t.start();
        }

        while (true) {
            boolean allC = true;
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    allC = false;
                    break;
                }
            }
            if (allC) {
                System.out.println("res = " + accumulator.longValue());
                long stop = Clock.systemUTC().millis();
                System.out.println("ADDER = " + (stop - start));
                break;
            }
        }
    }

    @Test
    public void test2() {
        AtomicLong accumulator = new AtomicLong(0L);
        long start = Clock.systemUTC().millis();
        List<Thread> threads = Lists.newArrayList();
        for (int i = 0; i < THREADS; i++) {
            Thread t = new Thread(() -> {for (int j = 0; j < LOOP; j++) accumulator.incrementAndGet();});
            threads.add(t);
            t.start();
        }

        while (true) {
            boolean allC = true;
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    allC = false;
                    break;
                }
            }
            if (allC) {
                System.out.println("res = " + accumulator.longValue());
                long stop = Clock.systemUTC().millis();
                System.out.println("ATOMIC LONG = " + (stop - start));
                break;
            }
        }

    }
}
