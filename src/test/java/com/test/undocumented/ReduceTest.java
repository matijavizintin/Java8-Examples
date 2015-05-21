package com.test.undocumented;

import com.test.beans.Person;
import com.test.crappy.Streams3Test;
import org.junit.Test;

import java.util.List;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 12.37
 */
public class ReduceTest {

    @Test public void reduceToOne() {
        List<Person> people = new Streams3Test().generatePersons(100);
        Person oldest = people.stream().reduce((p1, p2) -> p1.getAge() > p2.getAge() ? p1 : p2).get();
        System.out.println("oldest = " + oldest);
    }

    @Test public void aggregateWithReduce() {
        List<Person> people = new Streams3Test().generatePersons(100);
        Person pAgg = people.stream().reduce(
                new Person("", 0), (p, p2) -> {
                    p.setAge(p.getAge() + p2.getAge());
                    p.setName(p.getName() + p2.getName());
                    return p;
                });
        System.out.format("Aggregated: Name: %s, Age %d", pAgg.getName(), pAgg.getAge());
    }

    @Test public void reduceToAnotherType() {
        List<Person> people = new Streams3Test().generatePersons(10);
        Integer totalAge = people.stream().reduce(0, (integer, person) -> integer += person.getAge(), (integer1, integer2) -> integer1 + integer2);
        System.out.println("TotalAge: " + totalAge);
    }

    @Test
    public void reduceDebugSingle() {
        // serial
        List<Person> people = new Streams3Test().generatePersons(10);
        Integer totalAge = people.stream().reduce(
                0, (integer, person) -> {
                    System.out.format("Accumulator F: sum: %d, increment: %d\n", integer, person.getAge());
                    return integer + person.getAge();
                }, (integer1, integer2) -> {
                    System.out.printf("Combiner: first: %d, second: %d\n", integer1, integer2);
                    return integer1 + integer2;
                });
        System.out.println("TotalAge serial: " + totalAge);
    }

    @Test
    public void reduceDebugParallel() {
        // parallel
        List<Person> people = new Streams3Test().generatePersons(10);
        Integer totalAge = people.parallelStream().reduce(
                0, (integer, person) -> {
                    System.out.format("Accumulator F: sum: %d, increment: %d\n", integer, person.getAge());
                    return integer + person.getAge();
                }, (integer1, integer2) -> {
                    System.out.printf("Combiner: first: %d, second: %d\n", integer1, integer2);
                    return integer1 + integer2;
                });
        System.out.println("TotalAge parallel: " + totalAge);
    }
}
