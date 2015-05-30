package com.test;

import com.test.generators.DataGenerator;
import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Matija Vižintin
 * Date: 30. 05. 2015
 * Time: 20.01
 */
public class MapsTest extends LoggingTimedTest {

    /**
     * This is a simple putIfPresent test.
     */
    @Test
    public void put() {
        Map<Integer, String> map = DataGenerator.mapNamesToIntegers(10);

        // remember name al position 5
        String nameAtPosition5 = map.get(5);

        // put if absent with key present
        map.putIfAbsent(5, null);
        Assert.assertNotNull(map.get(5));
        Assert.assertEquals(nameAtPosition5, map.get(5));

        // put if absent without key present
        String newValueAt15 = "NEW_VALUE";
        map.putIfAbsent(15, newValueAt15);
        Assert.assertNotNull(map.get(15));
        Assert.assertEquals(newValueAt15, map.get(15));
    }

    /**
     * This is a simple forEach on a map test.
     */
    @Test
    public void forEach() {
        Map<Integer, String> map = DataGenerator.mapNamesToIntegers(10);

        // for each
        map.forEach((key, value) -> System.out.printf("Key: %d, Value %s\n", key, value));
    }

    /**
     * This is a simple compute test on a map. Using lambda expressions, new values can be set on maps if keys present/absent/both.
     */
    @Test
    public void compute() {
        Map<Integer, String> map = DataGenerator.mapNamesToIntegers(10);

        // compute
        String newValue1 = "NEW_VALUE_AT_1";
        map.compute(1, (integer1, s1) -> newValue1);
        Assert.assertNotNull(map.get(1));
        Assert.assertEquals(newValue1, map.get(1));

        // compute if present
        String newValue2 = "NEW_VALUE_AT_5";
        map.computeIfPresent(5, (integer, s) -> newValue2);
        Assert.assertNotNull(map.get(5));
        Assert.assertEquals(newValue2, map.get(5));

        map.computeIfPresent(15, (integer, s) -> null);     // nothing happens here
        Assert.assertNull(map.get(15));

        // compute if absent
        String newValue3 = "NEW_VALUE_AT_15";
        map.computeIfAbsent(15, integer -> newValue3);
        Assert.assertNotNull(map.get(15));
        Assert.assertEquals(newValue3, map.get(15));
    }

    /**
     * Conditional remove on maps. It removes elements only if the condition in fulfilled.
     */
    @Test
    public void remove() {
        Map<Integer, String> map = DataGenerator.mapNamesToIntegers(15);

        // conditional remove - only if condition is met
        String valueAt5 = map.get(5);
        map.remove(5, valueAt5);
        Assert.assertNull(map.get(5));

        // conditional remove - invalid value, nothing happens
        map.remove(10, "ABCD");
        Assert.assertNotNull(map.get(10));

        // get or default
        String defaultIfMissing = "default";
        Assert.assertEquals(defaultIfMissing, map.getOrDefault(5, defaultIfMissing));
    }

    /**
     * Test shows merge operations.
     *
     * First case shows when key is present and the value is remapped with a remapping function. Second case shows when
     * key is not present and the value is inserted and remapping function is ignored. Basically, if value exists then merge, if not then initialize.
     */
    @Test
    public void merge() {
        Map<Integer, String> map = DataGenerator.mapNamesToIntegers(15);

        // merge
        String oldAt5 = map.get(5);
        String newAt5 = "NEW_VALUE_AT_5";
        map.merge(5, newAt5, String::concat);
        Assert.assertEquals(oldAt5.concat(newAt5), map.get(5));

        // merge with non-existing key
        String newAt15 = "NEW_VALUE_AT_15";
        map.merge(15, newAt15, (s, s2) -> null);
        Assert.assertNotNull(map.get(15));
        Assert.assertEquals(newAt15, map.get(15));
    }
}
