package com.test.streams;

import com.test.beans.Person;
import com.test.flatmap.Many;
import com.test.flatmap.One;
import com.test.generators.DataGenerator;
import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Matija Vižintin
 * Date: 27. 05. 2015
 * Time: 16.06
 *
 * API: http://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html
 */
public class StreamsTest extends LoggingTimedTest {

    /**
     * This test shows how can classic for loops be replaced by streams. Even if clauses and transformations can be done in a single stream.
     */
    @Test
    public void replacingALoop() {
        List<Integer> listToFill = new ArrayList<>();
        IntStream.range(0, 10).forEach(listToFill::add);

        // assert list is filled
        Assert.assertEquals(10, listToFill.size());

        // if input value is greater than 5 transform into double value and put into response
        List<Double> response = new ArrayList<>();
        IntStream.range(0, 10)
                .filter(value -> value > 5)             // if condition
                .boxed()                                // transform from primitive to object
                .mapToDouble(Integer::doubleValue)      // transform to double
                .forEach(response::add);                // put into result
        Assert.assertEquals(4, response.size());
    }

    /**
     * Test shows how streams be transformed between types. Beside objects stream there are also streams to work with primitives, IntStream, LongStream
     * and DoubleStream. Every primitive stream has its own functions, predicates, operators, etc.
     */
    @Test
    public void transformationsBetweenStreamTypes() {
        List<Integer> input = DataGenerator.integers(10);
        List<Integer> output = input.stream()           // stream of objects
                .filter(integer -> true)                // here we apply a normal predicate
                .mapToInt(value -> value)               // transform into int stream
                .filter(value -> true)                  // here we apply an IntPredicate
                .mapToObj(value -> value)               // transform back into a stream of objects
                .collect(Collectors.toList());

        // assert
        Assert.assertArrayEquals(input.toArray(), output.toArray());
    }

    /**
     * This is a pipeline of operations. This example show how many different operations can be done in a single pipeline. This examples generates
     * a one-to-many structure and then print names of all many objects. This could be also used to create a one-to-many structure and store it.
     *
     * NOTE: peak method is mainly available for debug purposes the examine the flow of elements. Basically it does nothing with the stream. We are
     * using this method to create Many elements for One and to show the capabilities of streams.
     */
    @Test
    public void streamPipeline() {
        IntStream.range(1, 10)
                .mapToObj(value -> new One("One: " + value))        // create Ones
                // create Many-ies for ones
                .peek(
                        one -> IntStream.range(1, 5)
                                .mapToObj(value -> new Many(one.getName() + " Many: " + value, one))
                                .forEach(one.getManies()::add))
                .flatMap(one -> one.getManies().stream())                   // flatten into manies
                .forEach(many -> System.out.println(many.getName()));       // print manies' names
    }

    /**
     * Test shows how to flatten a list of objects with one-to-many collection. For loops with addAll operations can be replaced by a stream using
     * flatMap.
     */
    @Test
    public void simpleFlatMap() {
        // generate one-to-many objects
        int onesSize = 10;
        int maniesSize = 5;
        List<One> ones = DataGenerator.ones(onesSize, maniesSize);
        List<Many> manies = ones.stream()
                .flatMap(one -> one.getManies().stream())       // transform each one into a stream of manies
                .collect(Collectors.toList());                  // collect everything as a list

        // assert
        Assert.assertEquals(onesSize * maniesSize, manies.size());
    }

    /**
     * This example show that stream is not executed until the terminal operation is called.
     */
    private int executionCounter = 0;
    @Test
    public void streamNotExecutedTillTerminalOperation() {
        int inputSize = 10;
        List<Person> people = DataGenerator.people(inputSize);

        // crate stream
        Stream<Person> stream = people.stream().map(person -> {executionCounter++; return person;});

        // assert
        Assert.assertEquals(0, executionCounter);       // stream's operation haven't executed

        // call terminal operation
        stream.forEach(person -> {});       // dummy

        // assert
        Assert.assertEquals(inputSize, executionCounter);
    }

    /**
     * This test shows how are streams executed. Streams are processed vertically, so each element goes through the whole stream and then the next
     * element enters the stream. This can reduce the number of operations executed. Considering this, the order of operations in a stream is
     * important. First filter and then map to reduce the number of operations.
     *
     * NOTE: This is the case for stateless operations, of course sorting can't be processed that way.
     */
    private int operationsCount = 0;
    @Test
    public void streamExecutionDebug() {
        List<Integer> input = DataGenerator.integers(5);

        // pipeline execution example - check the output
        input.stream()
                .filter(
                    integer -> {
                        System.out.printf("Filter: %s\n", integer);
                        return true;
                })
                .map(
                    integer -> {
                        System.out.printf("Map: %s\n", integer);
                        return integer;
                })
                .forEach(integer -> System.out.printf("ForEach: %s\n", integer));

        // reducing the number of operations
        input.stream()
                .filter(
                        integer -> {
                            operationsCount++;
                            return true;
                        })
                .anyMatch(
                        integer -> {
                            operationsCount++;
                            return true;
                        });

        // assert that only 2 operations are executed - when the first element goes through the pipeline, the execution finishes
        Assert.assertEquals(2, operationsCount);
    }

    /**
     * This test shows that stateful operations (eg. sort) don't execute vertically but horizontally.
     */
    @Test
    public void statefulOperation() {
        List<Integer> input = DataGenerator.integers(10);

        // stream with a stateful operation
        input.stream()
                .filter(
                    integer -> {
                        System.out.printf("Filter1: %s\n", integer);
                        return true;
                    })
                .filter(
                    integer -> {
                        System.out.printf("Filter2: %s\n", integer);
                        return true;
                    })
                .sorted(            // stateful operation; all previous operations in the stream for all elements have to be executed
                    (o1, o2) -> {
                        System.out.printf("Sort: %s <> %s\n", o1, o2);
                        return o1.compareTo(o2);
                    })
                .map(               // from here on, operations are executed vertically
                    integer -> {
                        System.out.printf("Map: %s\n", integer);
                        return integer;
                    })
                .forEach(
                        integer -> {
                            System.out.printf("ForEach: %s\n", integer);
                        });
    }

    /**
     * Streams can't be reused directly. Once a terminal operation is called they are closed. Every time a new stream has to be created. Using suppliers
     * is possible to call multiple operations on a equal (but not the same) stream.
     */
    @Test(expected = IllegalStateException.class)
    public void reusingStreams() {
        // create a simple stream
        Stream<String> simpleStream = DataGenerator.strings(5, 5).stream();

        // create a supplier
        Supplier<Stream<String>> streamSupplier = () -> DataGenerator.strings(5, 5).stream();

        // execute multiple operations on a stream using a supplier
        streamSupplier.get().anyMatch(s -> true);
        streamSupplier.get().anyMatch(s -> false);

        // executing multiple operations directly on a stream in expected to fail in the second attempt to call a stream
        simpleStream.allMatch(s -> true);
        simpleStream.allMatch(s -> true);

        Assert.assertTrue("This line of code shouldn't have been executed.", false);
    }
}
