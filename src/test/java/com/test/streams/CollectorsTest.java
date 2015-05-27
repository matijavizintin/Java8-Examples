package com.test.streams;

import com.test.beans.Person;
import com.test.generators.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 10.11
 */
public class CollectorsTest {

    /**
     * Simple collectors test. Streams are collected as list or as set.
     */
    @Test
    public void sipleCollectors() {
        // make some operations on the stream and collect with an integrated collector
        List<Integer> list = IntStream.range(1, 50).filter(value -> value < 10).mapToObj(v ->  v).collect(Collectors.toList());
        System.out.println("Collected as list: " + list.getClass());
        Set<Integer> set = IntStream.range(1, 50).filter(value -> value < 10).mapToObj(v -> v).collect(Collectors.toSet());
        System.out.println("Collected as set: " + set.getClass());
    }

    /**
     * Group by collectors test. This is actually a multimap transformation.
     */
    @Test
    public void collectGroupedBy() {
        // group by age in a multimap with collect
        int inputSize = 100;
        List<Person> persons = DataGenerator.people(inputSize);
        Map<Integer, List<Person>> mapped = persons.stream().collect(Collectors.groupingBy(Person::getAge));

        // printout
        mapped.forEach((age, p) -> System.out.format("Age: %d, people: %s\n", age, p));

        // flatten and assert
        List<Person> people = mapped.keySet().stream().flatMap(integer -> mapped.get(integer).stream()).collect(Collectors.toList());
        Assert.assertEquals(inputSize, people.size());
    }

    /**
     * Test with math collectors. They are basically "reduce" functions, but are here to show the included collector/reduce functions and the flexibility
     * of streams.
     */
    @Test
    public void mathCollectors() {
        List<Person> persons = DataGenerator.people(100);

        // average with integrated collector
        Double average = persons.stream().collect(Collectors.averagingInt(Person::getAge));
        System.out.format("Average: %f\n", average);

        // count
        Long counter = persons.stream().collect(Collectors.counting());
        System.out.printf("Count: %d\n", counter);

        // sum - for example the same can be done using mapToIn(Person::getAge) and then calling sum
        Integer sum = persons.stream().collect(Collectors.summingInt(Person::getAge));
        System.out.printf("Sum: %d\n", sum);
        Integer sum2 = persons.stream().mapToInt(Person::getAge).sum();

        // assert
        Assert.assertEquals(sum, sum2);
    }

    /**
     * Test shows collectors work as string joiners. Stream is filtered, mapped and joined in a collector.
     */
    @Test
    public void collectorAsStringJoiner() {
        List<Person> persons = DataGenerator.people(5);

        // join names
        String joined = persons.stream()
                .filter(person -> person.getAge() > 10)
                .map(Person::getName)
                .collect(Collectors.joining(" and ", "Person(s) ", " are older than 10 years."));
        System.out.println(joined);
    }

    /**
     * Test shows a joiner function implemented as collector. Collector instances can be then reused in multiple streams.
     */
    @Test
    public void customCollector() {
        Collector<Person, StringJoiner, String> collector = Collector.of(
                () -> new StringJoiner(" | "), (stringJoiner, person) -> stringJoiner.add(person.getName().toUpperCase()), StringJoiner::merge,
                StringJoiner::toString);

        // join names and print them
        List<Person> persons = DataGenerator.people(5);
        String names = persons.stream().collect(collector);
        System.out.println(names);
    }
}
