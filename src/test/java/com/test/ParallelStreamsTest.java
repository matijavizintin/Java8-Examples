package com.test;

import com.test.generators.DataGenerator;
import org.junit.Test;

import java.util.List;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 14.19
 */
public class ParallelStreamsTest {

    @Test public void parallel() {
        List<String> list = DataGenerator.strings(5, 100);
        list.parallelStream().filter(
                s -> {
                    System.out.format("Filter: input: %s, thread: %s\n", s, Thread.currentThread().getName());
                    return true;
                }).map(
                s -> {
                    System.out.format("Map: input: %s, thread: %s\n", s, Thread.currentThread().getName());
                    return s.toUpperCase();
                }).forEach(s -> System.out.format("ForEach: input: %s, thread: %s\n", s, Thread.currentThread().getName()));
    }

    @Test public void parallelSort() {
        List<String> list = DataGenerator.strings(5, 10);
        list.parallelStream().filter(
                s -> {
                    System.out.printf("filter: %s, [%s]\n", s, Thread.currentThread().getName());
                    return true;
                }).map(
                s -> {
                    System.out.printf("map: %s, [%s]\n", s, Thread.currentThread().getName());
                    return s.toUpperCase();
                }).sorted(
                (o1, o2) -> {
                    System.out.printf("sort: %s <> %s, [%s]\n", o1, o2, Thread.currentThread().getName());
                    return o1.compareTo(o2);
                }).forEach(
                s -> {
                    System.out.printf("forEach: %s, [%s]\n", s, Thread.currentThread().getName());
                });
    }

    @Test public void parallelReduce() {
        List<Person> people = new StreamsTest().generatePersons(10);
        people.parallelStream().reduce(
                0, (integer, person) -> {
                    System.out.format("Accumulator %s, [%s]\n", integer, Thread.currentThread().getName());
                    return integer + person.getAge();
                }, (integer1, integer2) -> {
                    System.out.printf("Combiner: %s, [%s]\n", integer1, Thread.currentThread().getName());
                    return integer1 + integer2;
                });
    }
}
