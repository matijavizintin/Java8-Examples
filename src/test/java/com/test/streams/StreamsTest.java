package com.test.streams;

import com.test.beans.Person;
import com.test.crappy.Streams2Test;
import com.test.crappy.Streams3Test;
import com.test.flatmap.Many;
import com.test.flatmap.One;
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
public class StreamsTest {

    @Test
    public void collectors() {
        List<Integer> list = IntStream.range(1, 50).filter(value -> value < 10).mapToObj(v ->  v).collect(Collectors.toList());
        System.out.println("Collected as list: " + list.getClass());
        Set<Integer> set = IntStream.range(1, 50).filter(value -> value < 10).mapToObj(v -> v).collect(Collectors.toSet());
        System.out.println("Collected as set: " + set.getClass());

        List<Person> persons = new Streams3Test().generatePersons(100);
        Map<Integer, List<Person>> mapped = persons.stream().collect(Collectors.groupingBy(Person::getAge));

        mapped.forEach((age, p) -> System.out.format("Age %d, people %s\n", age, p));
    }

    @Test
    public void mathFunctions() {
        List<Person> persons = new Streams3Test().generatePersons(100);

        Double average = persons.stream().collect(Collectors.averagingInt(Person::getAge));
        System.out.format("Average: %f\n", average);

        Long counter = persons.stream().collect(Collectors.counting());
        System.out.printf("Count: %d\n", counter);

        Integer sum = persons.stream().collect(Collectors.summingInt(Person::getAge));
        System.out.printf("Sum: %d\n", sum);
    }

    @Test
    public void stringJoiners() {
        List<Person> persons = new Streams3Test().generatePersons(5);

        String s = persons.stream()
                .filter(person -> person.getAge() > 10)
                .map(Person::getName)
                .collect(Collectors.joining(" in ", "Osebe ", " so starejše od 10 let."));
        System.out.println(s);
    }

    @Test
    public void customCollector() {
        Collector<Person, StringJoiner, String> collector = Collector.of(
                () -> new StringJoiner(" | "), (stringJoiner, person) -> stringJoiner.add(person.getName().toUpperCase()), StringJoiner::merge,
                StringJoiner::toString);

        List<Person> persons = new Streams3Test().generatePersons(5);
        String names = persons.stream().collect(collector);
        System.out.println(names);
    }

    @Test
    public void flatMap() {
        List<One> ones = new Streams2Test().generateFlatMapData(10, 3);
        ones.stream().flatMap(one -> one.getManies().stream()).map(Many::getName).forEach(System.out::println);
    }

    @Test
    public void pipelineExample() {
        IntStream.range(1, 10)
                .mapToObj(value -> new One("One: " + value))
                .peek(one -> IntStream.range(1, 5).mapToObj(value -> new Many(one.getName() + " Many: " + value, one)).forEach(one.getManies()::add))
                .flatMap(one -> one.getManies().stream())
                .forEach(many -> System.out.println(many.getName()));
    }

}
