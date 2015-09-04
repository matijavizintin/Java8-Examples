package com.test.concurrency;

import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

/**
 * Created by Matija Vižintin
 * Date: 28. 08. 2015
 * Time: 18.50
 */
public class AtomicVariablesTest extends LoggingTimedTest {
    private static final int range = 1000 * 1000;
    /**
     * This test show the usage of atomic integer. Incrementing a value in an atomic integer (instructions that are supported by modern CPUs) is
     * usually faster than using a synchronized block.
     */
    @Test
    public void atomicIntegerTest() throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();

        // atomic integer to increment
        AtomicInteger atomicInteger = new AtomicInteger(0);

        // increment
        IntStream.range(0, range).forEach(value -> service.submit(atomicInteger::incrementAndGet));

        // wait for tasks to finish and shutdown
        service.shutdown();

        // assert
        Assert.assertEquals(range, atomicInteger.get());
    }

    /**
     * This test show the usage of long adder. It usually performs better than atomic integer and is preferred when are more write than read
     * operations.
     */
    @Test
    public void longAdderTest() {
        ExecutorService service = Executors.newCachedThreadPool();

        // long accumulator to increment
        LongAdder longAdder = new LongAdder();

        // increment
        int range = 10000;
        IntStream.range(0, range).forEach(value -> service.submit(longAdder::increment));

        // wait for tasks to finish and shutdown
        service.shutdown();

        // assert
        Assert.assertEquals(range, longAdder.sum());
    }

    /**
     * This test show the usage of long accumulator (which is a generalisation of LongAdder). It computes values using a lambda expression so it is more
     * flexible than long adder. There is a big performance trade-off for this flexibility.
     */
    @Test
    public void longAccumulatorTest() {
        ExecutorService service = Executors.newCachedThreadPool();

        // long accumulator to increment
        LongAccumulator longAccumulator = new LongAccumulator((left, right) -> left + right, 0);

        // increment
        IntStream.range(0, range).forEach(value -> service.submit(() -> longAccumulator.accumulate(1)));

        // wait for tasks to finish and shutdown
        service.shutdown();

        // assert
        Assert.assertEquals(range, longAccumulator.get());
    }
}
