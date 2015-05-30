package com.test.streams;

import com.test.beans.Person;
import com.test.flatmap.Many;
import com.test.flatmap.One;
import com.test.generators.DataGenerator;
import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Matija Vižintin
 * Date: 27. 05. 2015
 * Time: 16.06
 */
public class StreamsTest extends LoggingTimedTest {

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
     * This is a pipeline of operations. This example show how many different operations can be done in a single pipeline.
     *
     * NOTE: peak method is mainly available for debug purposes the examine the flow of elements. Basically it does nothing with the stream. We are
     * using this method to create Many elements for One and to show the capabilities of streams.
     */
    @Test
    public void streamPipeline() {
        IntStream.range(1, 10)
                .mapToObj(value -> new One("One: " + value))        // create Ones
                .peek(one -> IntStream.range(1, 5).mapToObj(value -> new Many(one.getName() + " Many: " + value, one)).forEach(one.getManies()::add))   // create Many-ies for ones
                .flatMap(one -> one.getManies().stream())           // flatten into manies
                .forEach(many -> System.out.println(many.getName()));       // print manies' names
    }

    /**
     * This example show that stream is not executed until the terminal operation is called.
     */
    private int executionCounter;
    @Test
    public void streamNotExecutedTillTerminalOperation() {
        int inputSize = 10;
        List<Person> people = DataGenerator.people(inputSize);

        // crate stream
        Stream<Person> stream = people.parallelStream().map(person -> {executionCounter++; return person;});

        // assert
        Assert.assertEquals(0, executionCounter);       // stream's operation haven't executed

        // call terminal operation
        stream.forEach(person -> {});       // dummy

        // assert
        Assert.assertEquals(inputSize, executionCounter);
    }
}
