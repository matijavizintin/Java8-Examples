package com.test.streams;

import com.test.generators.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Matija Vižintin
 * Date: 01. 06. 2015
 * Time: 13.49
 */
public class FinishersTest {

    /**
     * Test shows how to get the first element in the stream. There is also a method {@link Stream#findAny()} that picks one element from the stream.
     */
    @Test
    public void findFirst() {
        List<Double> list = DataGenerator.randomDoubles(10);

        // find first in list
        Double first = list.stream().findFirst().get();

        // assert
        Assert.assertEquals(list.get(0), first);
    }

    /**
     * This is a simple test of *Match operations in streams.
     */
    @Test
    public void match() {
        // all match
        boolean allMatch = DataGenerator.integers(10).stream().allMatch(integer -> integer > 5);
        Assert.assertFalse(allMatch);

        // any match
        boolean anyMatch = DataGenerator.integers(10).stream().anyMatch(integer -> integer > 5);
        Assert.assertTrue(anyMatch);

        // none match
        boolean noneMatch = DataGenerator.integers(10).stream().noneMatch(integer -> integer > 5);
        Assert.assertFalse(noneMatch);
    }
}
