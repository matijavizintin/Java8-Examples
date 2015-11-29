package com.test.concurrency;

import com.test.generators.DataGenerator;
import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Matija Vižintin
 * Date: 28. 08. 2015
 * Time: 18.51
 */
public class ConcurrentDataStructuresTest extends LoggingTimedTest {

    /**
     * This test shows basic operations on a concurrent map. This methods work the same as those on a normal map except
     * they are thread safe and expect to be called from multiple threads concurrently.
     */
    @Test
    public void concurrentMapTest() {
        ConcurrentMap<String, String> concurrentMap = DataGenerator.concurrentMap();

        // replacing for loop
        concurrentMap.forEach((key, value) -> System.out.printf("%s => %s\n", key, value));

        // put if absent
        String key = "JAMES";
        String value = "BOND";
        String oldValue = concurrentMap.get(key);
        concurrentMap.putIfAbsent(key, value);
        Assert.assertEquals(oldValue, concurrentMap.get(key));

        // compute
        concurrentMap.compute(key, (k, v) -> key + value);
        Assert.assertEquals(key + value, concurrentMap.get(key));

        // replace all values
        concurrentMap.replaceAll((k, v) -> v.toLowerCase());
    }

    /**
     * Here is the example of working directly with the implementation where we can parametrize when to work with a single
     * thread and were with all the threads in the common fork join pool.
     */
    private Set<Long> threadsUsed = new HashSet<>();

    @Test
    public void concurrentHashMapTest() {
        ConcurrentHashMap<String, String> concurrentHashMap = DataGenerator.concurrentMap();

        // print on all available threads
        threadsUsed.clear();
        concurrentHashMap.forEach(1, (key, value) -> {
            threadsUsed.add(Thread.currentThread().getId());
//            System.out.printf("%s => %s\n", key, value);
        });
        System.out.printf("Executed on %d threads.\n", threadsUsed.size());

        // print on a single thread
        threadsUsed.clear();
        concurrentHashMap.forEach(10 * 1000, (key, value) -> {
            threadsUsed.add(Thread.currentThread().getId());
//            System.out.printf("%s => %s\n", key, value);
        });
        Assert.assertEquals(1, threadsUsed.size());

        // search through map using all threads
        String key = "JAMES";
        threadsUsed.clear();
        String value = concurrentHashMap.search(1, (k, v) -> {
            threadsUsed.add(Thread.currentThread().getId());
            return k.equals(key) ? v : null;
        });
        Assert.assertEquals(DataGenerator.revert(key), value);
        System.out.printf("Executed on %d threads.\n", threadsUsed.size());

        // concat all values on all threads
        threadsUsed.clear();
        String joinedValues = concurrentHashMap.reduce(1, (r, v) -> {
            threadsUsed.add(Thread.currentThread().getId());
            return v;
        }, String::concat);
        System.out.printf("Executed on %d threads.\n", threadsUsed.size());
        System.out.println(joinedValues);
    }
}
