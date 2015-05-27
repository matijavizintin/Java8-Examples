package com.test.streams;

import com.test.flatmap.Many;
import com.test.flatmap.One;
import com.test.generators.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Matija Vižintin
 * Date: 27. 05. 2015
 * Time: 16.06
 */
public class StreamsTest {

    /**
     * Test shows how to flatten a list of objects with one-to-many collection.
     */
    @Test
    public void simpleFlatMap() {
        // generate one-to-many objects
        int onesSize = 10;
        int maniesSize = 5;
        List<One> ones = DataGenerator.ones(onesSize, maniesSize);
        List<Many> manies = ones.stream().flatMap(one -> one.getManies().stream()).collect(Collectors.toList());

        // assert
        Assert.assertEquals(onesSize * maniesSize, manies.size());
    }

    /**
     * This is a pipeline of operations. TODO..
     */
    @Test
    public void streamPipeline() {
        IntStream.range(1, 10)
                .mapToObj(value -> new One("One: " + value))
                .peek(one -> IntStream.range(1, 5).mapToObj(value -> new Many(one.getName() + " Many: " + value, one)).forEach(one.getManies()::add))
                .flatMap(one -> one.getManies().stream())
                .forEach(many -> System.out.println(many.getName()));
    }
}
