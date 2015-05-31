package com.test.streams;

import com.test.beans.Person;
import com.test.generators.DataGenerator;
import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 12.37
 */
public class ReduceTest extends LoggingTimedTest {

    /**
     * Test reduces the stream to one person using accumulator BinaryOperation. The condition reduces to the oldest person.
     */
    @Test
    public void reduceToOne() {
        List<Person> people = DataGenerator.people(100);

        // reduce to oldest person
        Person oldest = people.stream().reduce((p1, p2) -> p1.getAge() > p2.getAge() ? p1 : p2).get();
        System.out.println("Oldest = " + oldest);

        // assert
        Integer maxAge = people.stream().mapToInt(Person::getAge).max().getAsInt();
        Assert.assertEquals(maxAge, oldest.getAge());
    }

    /**
     * Method reduces the stream to an aggregator. All values from the stream are aggregated to the identity.
     */
    @Test
    public void aggregateWithReduce() {
        List<Person> people = DataGenerator.people(100);

        // aggregate
        Person identity = new Person(0);
        people.stream().reduce(
                identity, (p, p2) -> {
                    p.setAge(p.getAge() + p2.getAge());
                    return p;
                });
        System.out.format("Aggregated age: %d\n", identity.getAge());

        // assert
        Integer totalAge = people.stream().mapToInt(Person::getAge).sum();
        Assert.assertEquals(totalAge, identity.getAge());
    }

    /**
     * Method reduces the stream of people to another type. This example shows how summing (or any other math operation) can be done with reduce.
     */
    @Test
    public void reduceToAnotherType() {
        List<Person> people = DataGenerator.people(100);
        Integer totalAge = people.stream().reduce(0, (integer, person) -> integer += person.getAge(), (integer1, integer2) -> integer1 + integer2);
        System.out.println("TotalAge: " + totalAge);

        // assert
        Integer summed = people.stream().map(Person::getAge).collect(Collectors.summingInt(value -> value));
        Assert.assertEquals(summed, totalAge);
    }

    /**
     * This method runs in "debug" mode and prints out every step that executes. You will note that combiner function is never executed in contrast to
     * parallel execution.
     */
    private boolean combinerSingleExecuted = false;       // global, so can be modified
    @Test
    public void reduceDebugSingle() {
        List<Person> people = DataGenerator.people(10);

        // serial execution
        people.stream().reduce(
                0, (integer, person) -> {
                    System.out.format("Accumulator F: sum: %d, increment: %d\n", integer, person.getAge());
                    return integer + person.getAge();
                }, (integer1, integer2) -> {
                    System.out.printf("Combiner: first: %d, second: %d\n", integer1, integer2);
                    combinerSingleExecuted = true;
                    return integer1 + integer2;
                });

        // assert
        Assert.assertFalse(combinerSingleExecuted);
    }

    /**
     * This method runs in "debug" mode and prints out every step that executes. You will note that combiner function is executed to combine thread
     * results in contract to single execution.
     */
    private LongAdder adder = new LongAdder();
    @Test
    public void reduceDebugParallel() {
        List<Person> people = DataGenerator.people(10);

        // parallel execution
        people.parallelStream().reduce(
                0, (integer, person) -> {
                    System.out.format("Accumulator F: sum: %d, increment: %d\n", integer, person.getAge());
                    return integer + person.getAge();
                }, (integer1, integer2) -> {
                    System.out.printf("Combiner: first: %d, second: %d\n", integer1, integer2);
                    adder.increment();
                    return integer1 + integer2;
                });
        System.out.printf("Combiner executed %d times.", adder.intValue());

        // assert
        Assert.assertTrue(adder.intValue() > 0);
    }
}
