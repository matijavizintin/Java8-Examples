package com.test.streams;

import com.test.generators.DataGenerator;
import com.test.timed.LoggingTimedTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Matija Vižintin
 * Date: 30. 05. 2015
 * Time: 17.49
 */
public class IntermediateOperations extends LoggingTimedTest {

    /**
     * Simple filter test. Filter out elements lower then 5.
     */
    @Test
    public void filter() {
        long filteredCount = DataGenerator.integers(10).stream().filter(value -> value >= 5).count();

        // assert
        Assert.assertEquals(5l, filteredCount);
    }

    /**
     * Simple sorting test. It sorts elements in stream.
     */
    @Test
    public void sorted() {
        // order data
        List<Double> ordered = DataGenerator.randomDoubles(100).stream().sorted().collect(Collectors.toList());

        // assert ordered
        for (int i = 0; i < ordered.size() - 1; i++) {
            Double first = ordered.get(i);
            Double second = ordered.get(i + 1);
            Assert.assertTrue(first <= second);
        }
    }

    /**
     * Simple mapping test. Stream elements are mapped upper-case values.
     */
    @Test
    public void map() {
        List<String> upperCased = DataGenerator.strings(10, 10).stream().map(String::toUpperCase).collect(Collectors.toList());

        // assert
        for (String string : upperCased) {
            Assert.assertTrue(StringUtils.isAllUpperCase(string));
        }
    }

    /**
     * This is a simple of *Match operations in streams.
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
